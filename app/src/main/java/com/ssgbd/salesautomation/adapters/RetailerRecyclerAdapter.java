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

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.VisitFragment;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.gps.GPSTracker;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

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

    public RetailerRecyclerAdapter(ArrayList<RetailerDTO> items, Context context,VisitFragment visitFragment) {
        this.routeList = items;
        this.context = context;
        this.visitFragment = visitFragment;
        this.arrayList = new ArrayList<RetailerDTO>();
        this.arrayList.addAll(routeList);
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

        try {

            holder.row_retailer_name.setText(itemFeed.getRetailer_name()+" "+"("+itemFeed.getRetailer_id()+")");
            holder.row_statis.setText(itemFeed.getStatus());
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
                    android.location.Location.distanceBetween(
                            retailerLat, retailerLon,
                            currentLat, currentLon,
                            results
                    );

                    float distanceInMeters = results[0];
                    Log.e("ORDER_DISTANCE", "Distance = " + distanceInMeters + " meters");

                    // ৫. Distance check > 300 meters
//                    if (distanceInMeters > 30000000000000000) {
//                        String distanceStr = String.format("%.2f", distanceInMeters) + " meters";
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setTitle("দূরত্ব সতর্কতা 🗺️");
//                        builder.setMessage("আপনি এই মুহূর্তে retailer থেকে " + distanceStr + " দূরে আছেন।\n"
//                                + "দুরত্ব বেশি হওয়ায় এখনই order করা সম্ভব নয়।\n");
//                                //+ "আপনি চাইলে নিচের বাটন দিয়ে retailer-এর location ম্যাপে দেখতে পারেন।");
//                        builder.setPositiveButton("ঠিক আছে", (dialog, which) -> dialog.dismiss());
//                        // View on Map button
//                        builder.setNeutralButton("View on Map", (dialog, which) -> {
//
//                            // Maps direction URI: current location -> retailer location
//                            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="
//                                    + currentLat + "," + currentLon
//                                    + "&destination=" + retailerLat + "," + retailerLon
//                                    + "&travelmode=driving");
//
//                            Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                            intent.setPackage("com.google.android.apps.maps");
//
//                            try {
//                                context.startActivity(intent);
//                            } catch (Exception ex) {
//                                try {
//                                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                                    context.startActivity(unrestrictedIntent);
//                                } catch (Exception innerEx) {
//                                    Toast.makeText(context, "Please install a maps application", Toast.LENGTH_LONG).show();
//                                }
//                            }
//
//                        });
//                        builder.show();
//
//                    } else {
                        // Distance acceptable → Open OrderActivity
                        Intent intent = new Intent(context, OrderActivity.class);
                        intent.putExtra("retailerId", itemFeed.getRetailer_id());
                        intent.putExtra("retailerName", itemFeed.getRetailer_name());
                        intent.putExtra("poient_id", itemFeed.getPoint_id());
                        intent.putExtra("from", "visit");
                        context.startActivity(intent);
                    //}

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
        public TextView row_retailer_name,row_order,row_visit,row_non_visit,row_statis;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_retailer_name = (TextView) itemView.findViewById(R.id.row_retailer_name);
            row_order = (TextView) itemView.findViewById(R.id.row_order);
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
