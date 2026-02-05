package com.ssgbd.salesautomation.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.VisitFragment;
import com.ssgbd.salesautomation.dtos.ConfirmOrderListDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.gps.GPSTracker;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class RetailerRecyclerAdapter extends RecyclerView.Adapter<RetailerRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<RetailerDTO> routeList;
    public ArrayList<RetailerDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    VisitFragment visitFragment;
    String userId;
    private String savedRouteId = "";
    private boolean hasShownToast = false; // <-- Flag for single Toast

    public RetailerRecyclerAdapter(ArrayList<RetailerDTO> items, Context context,VisitFragment visitFragment) {
        this.routeList = items;
        this.context = context;
        this.visitFragment = visitFragment;
        this.arrayList = new ArrayList<RetailerDTO>();
        this.arrayList.addAll(routeList);
        // Safe SharePreference load
        if (context != null) {
            this.userId = SharePreference.getUserId(context);
           // Toast.makeText(context, "User ID: " + userId, Toast.LENGTH_LONG).show();
            //Log.d("RetailerAdapter", "Loaded User ID: " + userId);
        }



        // Fetch saved route id from API
        fetchSavedRouteId();
    }
    private void fetchSavedRouteId() {
        if (context == null) return;

        String userId = SharePreference.getUserId(context);
        if (userId.isEmpty()) {
            Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

         String url = context.getString(R.string.base_url) + "api/get-saved-route?userId=" + userId;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("status");
                        if (status.equals("1")) {
                            // result array থেকে প্রথম object
                            JSONArray resultArray = obj.getJSONArray("result");
                            if (resultArray.length() > 0) {
                                JSONObject firstItem = resultArray.getJSONObject(0);
                                savedRouteId = firstItem.getString("routes"); // এখানে 'routes' field
                                notifyDataSetChanged();
                               // Toast.makeText(context, "Saved Route ID: " + savedRouteId, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                       // Toast.makeText(context, "JSON Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("API_ERROR", error.toString());
                   // Toast.makeText(context, "API Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_retailer,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final RetailerDTO itemFeed = routeList.get(position);



        // Get current route id from SharePreference

        try {

            holder.row_retailer_name.setText(itemFeed.getRetailer_name()+" "+"("+itemFeed.getRetailer_id()+")");
            holder.row_statis.setText(itemFeed.getStatus());
            // Hide buttons if savedRouteId matches
           // Toast.makeText(context, "Item Route ID: " + itemFeed.getRouteId(), Toast.LENGTH_SHORT).show();
            if (routeList.size() < 20 && !hasShownToast) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Title
                builder.setTitle("⚠ সতর্কবার্তা");
                builder.setMessage("এই রুটে ২০টির কম রিটেলার রয়েছে।\n\nবর্তমানে আছে: "
                        + routeList.size()
                        + "\n\nঅনুগ্রহ করে রিটেলার সংখ্যা বাড়ান, তারপর অর্ডার ও ভিজিট করতে পারেন।");

                builder.setCancelable(false);

                // OK button with custom color
                builder.setPositiveButton("ঠিক আছে", (dialog, which) -> dialog.dismiss());

                AlertDialog dialog = builder.create();

                // Customize dialog background and title
                dialog.setOnShowListener(d -> {
                    // Background color
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_light);

                    // Title color & size
                    int textViewId = context.getResources().getIdentifier("alertTitle", "id", "android");
                    TextView tv = dialog.findViewById(textViewId);
                    if (tv != null) {
                        tv.setTextColor(Color.WHITE);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    }

                    // Message color & size
                    TextView messageView = dialog.findViewById(android.R.id.message);
                    if (messageView != null) {
                        messageView.setTextColor(Color.WHITE);
                        messageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    }

                    // Button color
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if (positiveButton != null) {
                        positiveButton.setTextColor(Color.YELLOW);
                    }
                });

                dialog.show();

                hasShownToast = true;
            }



            if (routeList.size() < 20) {

                // 20 or more → hide all buttons
                holder.row_call_order.setVisibility(View.GONE);
                holder.row_visit.setVisibility(View.GONE);
                holder.row_order.setVisibility(View.GONE);
                holder.row_non_visit.setVisibility(View.GONE);
            } else {
                if (savedRouteId != null && savedRouteId.equals(itemFeed.getRouteId())) {
                    holder.row_call_order.setVisibility(View.GONE);
                    holder.row_visit.setVisibility(View.VISIBLE);
                    holder.row_order.setVisibility(View.VISIBLE);

                } else {
                    holder.row_call_order.setVisibility(View.VISIBLE);
                    holder.row_visit.setVisibility(View.GONE);
                    holder.row_order.setVisibility(View.GONE);

                }
            }

            holder.row_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // ১. Check if retailer location exists
                    if (itemFeed.getLat().equalsIgnoreCase("") || itemFeed.getLon().equalsIgnoreCase("")) {
                        Toast.makeText(context, "Retailer location not available", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // ২. Check location permission
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
                        return;
                    }

                    // ৩. Get current location using GPSTracker
                    GPSTracker gps = new GPSTracker((Activity) context);
                    if (!gps.canGetLocation()) {
                        gps.showSettingsAlert();
                        return;
                    }

                    double currentLat = gps.getLatitude();
                    double currentLon = gps.getLongitude();

                    double retailerLat = Double.parseDouble(itemFeed.getLat());
                    double retailerLon = Double.parseDouble(itemFeed.getLon());

                    // ৪. Calculate distance
                    float[] results = new float[1];
                    Location.distanceBetween(
                            retailerLat, retailerLon,
                            currentLat, currentLon,
                            results
                    );

                    float distanceInMeters = results[0];
                    Log.e("ORDER_DISTANCE", "Distance = " + distanceInMeters + " meters");

                    // ৫. Distance check > 300 meters
                    if (distanceInMeters > 60) {
                        String distanceStr = String.format("%.2f", distanceInMeters) + " meters";

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("দূরত্ব সতর্কতা 🗺️");
                        builder.setMessage("আপনি এই মুহূর্তে retailer থেকে " + distanceStr + " দূরে আছেন।\n"
                                + "দুরত্ব বেশি হওয়ায় এখনই order করা সম্ভব নয়।\n");
                                //+ "আপনি চাইলে নিচের বাটন দিয়ে retailer-এর location ম্যাপে দেখতে পারেন।");
                        builder.setPositiveButton("ঠিক আছে", (dialog, which) -> dialog.dismiss());
                        // View on Map button
                        builder.setNeutralButton("View on Map", (dialog, which) -> {

                            // Maps direction URI: current location -> retailer location
                            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="
                                    + currentLat + "," + currentLon
                                    + "&destination=" + retailerLat + "," + retailerLon
                                    + "&travelmode=driving");

                            Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            intent.setPackage("com.google.android.apps.maps");

                            try {
                                context.startActivity(intent);
                            } catch (Exception ex) {
                                try {
                                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    context.startActivity(unrestrictedIntent);
                                } catch (Exception innerEx) {
                                    Toast.makeText(context, "Please install a maps application", Toast.LENGTH_LONG).show();
                                }
                            }

                        });
                        builder.show();

                    } else {
                        // Distance acceptable → Open OrderActivity
                        Intent intent = new Intent(context, OrderActivity.class);
                        intent.putExtra("retailerId", itemFeed.getRetailer_id());
                        intent.putExtra("retailerName", itemFeed.getRetailer_name());
                        intent.putExtra("poient_id", itemFeed.getPoint_id());
                        intent.putExtra("from", "visit");
                        intent.putExtra("is_call_order", false);
                        context.startActivity(intent);
                    }

                }
            });


            holder.row_call_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // ১. Check if retailer location exists
                    if (itemFeed.getLat().equalsIgnoreCase("") || itemFeed.getLon().equalsIgnoreCase("")) {
                        Toast.makeText(context, "Retailer location not available", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // ২. Check location permission
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
                        return;
                    }

                    // ৩. Get current location using GPSTracker
                    GPSTracker gps = new GPSTracker((Activity) context);
                    if (!gps.canGetLocation()) {
                        gps.showSettingsAlert();
                        return;
                    }

                    double currentLat = gps.getLatitude();
                    double currentLon = gps.getLongitude();

                    double retailerLat = Double.parseDouble(itemFeed.getLat());
                    double retailerLon = Double.parseDouble(itemFeed.getLon());

                    // ৪. Calculate distance
                    float[] results = new float[1];
                    Location.distanceBetween(
                            retailerLat, retailerLon,
                            currentLat, currentLon,
                            results
                    );



                        // Distance acceptable → Open OrderActivity
                        Intent intent = new Intent(context, OrderActivity.class);
                        intent.putExtra("retailerId", itemFeed.getRetailer_id());
                        intent.putExtra("retailerName", itemFeed.getRetailer_name());
                        intent.putExtra("poient_id", itemFeed.getPoint_id());
                        intent.putExtra("from", "visit");
                        intent.putExtra("is_call_order", true);   // or false
                        context.startActivity(intent);


                }
            });



            holder.row_visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (itemFeed.getStatus().equalsIgnoreCase("Visit")){

                        Toast.makeText(context, "Already visited", Toast.LENGTH_SHORT).show();

                        return;
                    }if (itemFeed.getStatus().equalsIgnoreCase("Ordered")){

                        Toast.makeText(context, "Already Ordered", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    visitFragment.showVisitDialog();
                    Utility.V_RETAILER_ID = itemFeed.getRetailer_id();
                    Utility.V_RETAILER_NAME = itemFeed.getRetailer_name();
                  if (itemFeed.getLat().equalsIgnoreCase("")||itemFeed.getLon().equalsIgnoreCase("")){
                      visitFragment.getLatLon("0", "0");
                  }else {
                      visitFragment.getLatLon(itemFeed.getLat(), itemFeed.getLon());
                  }

                }
            });

            holder.row_non_visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemFeed.getStatus().equalsIgnoreCase("Visit")){

                        Toast.makeText(context, "Already visited", Toast.LENGTH_SHORT).show();

                        return;
                    }if (itemFeed.getStatus().equalsIgnoreCase("Ordered")){

                        Toast.makeText(context, "Already Ordered", Toast.LENGTH_SHORT).show();

                        return;
                    } if (itemFeed.getStatus().equalsIgnoreCase("Non-Visit")){

                        Toast.makeText(context, "Already submitted", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    Utility.V_RETAILER_ID = itemFeed.getRetailer_id();
                    Utility.V_RETAILER_NAME = itemFeed.getRetailer_name();
                    visitFragment.showNonVisitDialog();



                    if (itemFeed.getLat().equalsIgnoreCase("")||itemFeed.getLon().equalsIgnoreCase("")){
                        visitFragment.getLatLon("0", "0");
                    }else {
                        visitFragment.getLatLon(itemFeed.getLat(),itemFeed.getLon());
                    }
                }
            });


        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_retailer_name,row_order, row_call_order, row_visit,row_non_visit,row_statis;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_retailer_name = (TextView) itemView.findViewById(R.id.row_retailer_name);
            row_order = (TextView) itemView.findViewById(R.id.row_order);
            row_call_order = (TextView) itemView.findViewById(R.id.row_call_order);
            row_visit = (TextView) itemView.findViewById(R.id.row_visit);
            row_non_visit = (TextView) itemView.findViewById(R.id.row_non_visit);
            row_statis = (TextView) itemView.findViewById(R.id.row_statis);
            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        routeList.clear();
        if (charText.length() == 0) {
            routeList.addAll(arrayList);
        } else {
            for (RetailerDTO hm : arrayList) {
                if (hm.getRetailer_name().toLowerCase().contains(charText)) {
                    routeList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
