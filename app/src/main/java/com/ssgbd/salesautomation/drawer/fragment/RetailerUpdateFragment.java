package com.ssgbd.salesautomation.drawer.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.RetailerUpdateRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.ProductStrengthDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.gps.GPSTracker;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class RetailerUpdateFragment extends Fragment implements View.OnClickListener{

    View rootView;
    private Dialog wdialog;
    TextView txt_route_list,txt_route_name;
    DatabaseHelper databaseHelper;
    RetailerUpdateRecyclerAdapter retialer_adapter;
    RecyclerView retailer_list_recyclerView;
    EditText edt_txt_name,edt_txt_phone;
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<RetailerDTO> retailerDTOS = new ArrayList<>();
    ArrayList<RetailerDTO> retailerDTOS_update = new ArrayList<>();
    RouteRecyclerAdapter routeRecyclerAdapter;
    String routeId="";
    String RETAILER_ID="";
    String LAT="",LON="";

    public RequestQueue queue;
    VolleyMethods vm = new VolleyMethods();
    String formattedDate="";
    String syncDate="";
    String URL="";
    GPSTracker gps;
    private final Handler handler = new Handler();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    JSONObject finalobject;
    JSONObject retailerobject;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.retailer_update_fragment, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String myPath = DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()){
            databaseHelper.openDataBase();
        }

        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new RetailerUpdateRecyclerAdapter( retailerDTOS_update,getActivity(),this);
        retailer_list_recyclerView.setAdapter(retialer_adapter);
        //retailer_list_recyclerView.addOnItemTouchListener(new Re);
        initUi();
        Date c = Calendar.getInstance().getTime();
       // System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
    //  Log.e("formattedDate>",formattedDate+"");

        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        syncDate = sdf.format(new Date());
        syncRetailer("");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getLatLon() {
        gps = new GPSTracker(getActivity());
      //check if GPS enabled
        if(gps.canGetLocation()){

            LAT = String.valueOf(gps.getLatitude());
            LON = String.valueOf(gps.getLongitude());

        //    Log.e("<<1>>",LAT+"++"+ LON+"");

        }else{
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings

            new AlertDialog.Builder(getActivity())
                    .setTitle("Prominent disclosure \n Background Location Permission Needed")
                    // .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setMessage(getString(R.string.location_text))
                    .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })

                    .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            gps.showSettingsAlert();
                        }
                    })
                    .create()
                    .show();
        }
          //  gps.showSettingsAlert();
    }

    private void initUi() {
        queue = Volley.newRequestQueue(getActivity());
        txt_route_name = (TextView) rootView.findViewById(R.id.txt_route_name);
        txt_route_list = (TextView) rootView.findViewById(R.id.txt_route_list);
        txt_route_list.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

    public void getData(String rid) {

      // Log.e("<<>>>>",url.toString()+"<<<<");

        StringRequest stringRequest = new StringRequest(Request.Method.GET,getString(R.string.base_url)+"api/retailer-lat-long?retailer_id="+rid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //  Log.e("pdata>>",response+ "<<>>");

                try {
                    JSONObject respjsonObj = new JSONObject(response);

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
    }

    public void showRetailerUpdateDialog(String rId,String phone,String rname) {
        RETAILER_ID = rId;
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_update_retailert);
        final Button btnDone;
        final ImageView imbtnCross;

        final RelativeLayout rlDialogCross;

        edt_txt_name = (EditText)wdialog.findViewById(R.id.edt_txt_name);
        edt_txt_phone = (EditText)wdialog.findViewById(R.id.edt_txt_phone);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog_nonvisit);
                edt_txt_phone.setText(phone);
                edt_txt_name.setText(rname);
                edt_txt_name.setEnabled(false);
        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wdialog.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    finalobject = new JSONObject();
                    retailerobject = new JSONObject();
                    retailerobject.put("retailer_id", RETAILER_ID);
                    retailerobject.put("retailerName", edt_txt_name.getText().toString());
                    retailerobject.put("owner", "");
                    retailerobject.put("mobile",edt_txt_phone.getText().toString());
                    retailerobject.put("tnt", "");
                    retailerobject.put("email", "");
                    retailerobject.put("retailerAddress", "");
                    retailerobject.put("fb", "");
                    retailerobject.put("whatsapp", "");
                    retailerobject.put("lat", LAT);
                    retailerobject.put("lon", LON);
                    finalobject.put("retailer_info", retailerobject);

               //   Log.e("finalobject>>", finalobject + "");

                    sendRequest(finalobject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                wdialog.dismiss();
            }
        });
        wdialog.show();

            } else {
                //Request Location Permission
                checkLocationPermission();

            }
        }else {
            wdialog =new Dialog(getActivity());
            wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            wdialog.setContentView(R.layout.dialog_update_retailert);
            final Button btnDone;
            final ImageView imbtnCross;

            final RelativeLayout rlDialogCross;

            edt_txt_name = (EditText)wdialog.findViewById(R.id.edt_txt_name);
            edt_txt_phone = (EditText)wdialog.findViewById(R.id.edt_txt_phone);
            imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
            rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
            btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog_nonvisit);
            edt_txt_phone.setText(phone);
            edt_txt_name.setText(rname);
            edt_txt_name.setEnabled(false);
            imbtnCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wdialog.dismiss();
                }
            });
            rlDialogCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wdialog.dismiss();
                }
            });
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        finalobject = new JSONObject();
                        retailerobject = new JSONObject();
                        retailerobject.put("retailer_id", RETAILER_ID);
                        retailerobject.put("retailerName", edt_txt_name.getText().toString());
                        retailerobject.put("owner", "");
                        retailerobject.put("mobile",edt_txt_phone.getText().toString());
                        retailerobject.put("tnt", "");
                        retailerobject.put("email", "");
                        retailerobject.put("retailerAddress", "");
                        retailerobject.put("fb", "");
                        retailerobject.put("whatsapp", "");
                        retailerobject.put("lat", LAT);
                        retailerobject.put("lon", LON);
                        finalobject.put("retailer_info", retailerobject);

//                    Log.e("finalobject>>", finalobject + "");

                        sendRequest(finalobject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    wdialog.dismiss();
                }
            });
            wdialog.show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_route_list:

                showRouteListDialog();

                break;
        }
    }

    public boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void internetAlert(final Context context,final String status) {
        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher)).setTitle("Internet Alert").setMessage("Your device is not connected to internet. Connect to internet and try again or save this status to offline.");
        alertBulder.setCancelable(false);
        alertBulder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertBulder.setNeutralButton("Save Offline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           //     Log.e("status",status+"");

                databaseHelper.insertOrderStatus(databaseHelper,SharePreference.getUserId(getActivity()),
                       Utility.V_RETAILER_ID,formattedDate,status,Utility.ROUTE_ID ,"no",URL,Utility.V_RETAILER_NAME);

            }
        });
        alertBulder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        alertBulder.show();
    }

public  void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Prominent disclosure \n Background Location Permission Needed")
                       // .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setMessage(getString(R.string.location_text))
                        .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })

                        .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

      //  Log.e("intent>",requestCode+"---"+"");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
       }


    public void sendRequest(String s){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url)+"api/apps/retailer-update", s, new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                pd.dismiss();
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    //   Log.e("response>> inactiv>",jsonObject1.getString("message")+"");

                    if (jsonObject1.getString("status").equalsIgnoreCase("1")){
                        Toast.makeText(getActivity(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();

//                        syncRetailer("");
//
//                        for (int i=0;i<retailerDTOS.size();i++){
//
//                            if (retailerDTOS.get(i).getRouteId().equalsIgnoreCase(Utility.ROUTE_ID)){
//
//                                RetailerDTO retailerDTO = new RetailerDTO();
//                                retailerDTO.setRetailer_id(retailerDTOS.get(i).getRetailer_id());
//                                retailerDTO.setRetailer_name(retailerDTOS.get(i).getRetailer_name());
//                                retailerDTO.setRetailerOwner(retailerDTOS.get(i).getRetailerOwner());
//                                retailerDTO.setRetailerMobileNo(retailerDTOS.get(i).getRetailerMobileNo());
//                                retailerDTO.setLat(retailerDTOS.get(i).getLat());
//                                retailerDTO.setLon(retailerDTOS.get(i).getLon());
//                                // Log.e("<<>>",retailerDTOS.get(i).getLat()+"<>"+retailerDTOS.get(i).getLon()+"<<>>");
//                                retailerDTOS_update.add(retailerDTO);
//                            }
//                        }
//
//                        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
//                        retialer_adapter = new RetailerUpdateRecyclerAdapter( retailerDTOS_update,getActivity(),RetailerUpdateFragment.this);
//                        retailer_list_recyclerView.setAdapter(retialer_adapter);
//
//                        retialer_adapter.notifyDataSetChanged();

                    }else {
                        Toast.makeText(getActivity(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showRouteListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final TextView tv_dialog_title;

        final RelativeLayout rlDialogCross;

        Utility.routeDTOS.clear();
        try {
            JSONObject respjsonObj = new JSONObject(SharePreference.getRouteData(getActivity()));
            JSONArray routeArray = respjsonObj.getJSONArray("route");
            for (int i = 0; i < routeArray.length(); i++) {
                JSONObject routeObject = routeArray.getJSONObject(i);
                RouteDTO routeDTO = new RouteDTO();
                routeDTO.setPoint_id(routeObject.getString("point_id"));
                routeDTO.setTerritory_id(routeObject.getString("territory_id"));
                routeDTO.setRname(routeObject.getString("rname"));
                routeDTO.setRoute_id(routeObject.getString("route_id"));
                Utility.routeDTOS.add(routeDTO);
            }
        }catch (JSONException je){
        }

        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_search);
        route_list_recyclerView=(RecyclerView) wdialog.findViewById(R.id.route_list_recyclerView);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog);
        tv_dialog_title = (TextView) wdialog.findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText("Please select a route");
        wdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wdialog.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialog.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        routeRecyclerAdapter = new RouteRecyclerAdapter( Utility.routeDTOS,getActivity());
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
             //   retailerDTOS.clear();
                retailerDTOS_update.clear();

                wdialog.dismiss();
                //  Toast.makeText(getActivity(), Utility.routeDTOS.get(position).getRoute_id(), Toast.LENGTH_SHORT).show();
                Utility.ROUTE_ID = Utility.routeDTOS.get(position).getRoute_id();

              //  retailerDTOS = databaseHelper.getRetailerList(databaseHelper,Utility.routeDTOS.get(position).getRoute_id());

                for (int i=0;i<retailerDTOS.size();i++){

                    if (retailerDTOS.get(i).getRouteId().equalsIgnoreCase(Utility.ROUTE_ID) && retailerDTOS.get(i).getStatus().equalsIgnoreCase("0")){

                       RetailerDTO retailerDTO = new RetailerDTO();
                       retailerDTO.setRetailer_id(retailerDTOS.get(i).getRetailer_id());
                       retailerDTO.setRetailer_name(retailerDTOS.get(i).getRetailer_name());
                       retailerDTO.setRetailerOwner(retailerDTOS.get(i).getRetailerOwner());
                       retailerDTO.setRetailerMobileNo(retailerDTOS.get(i).getRetailerMobileNo());
                       retailerDTO.setLat(retailerDTOS.get(i).getLat());
                       retailerDTO.setLon(retailerDTOS.get(i).getLon());
                      // Log.e("<<>>",retailerDTOS.get(i).getLat()+"<>"+retailerDTOS.get(i).getLon()+"<<>>");
                       retailerDTOS_update.add(retailerDTO);

                    }
                }

                txt_route_name.setText("Route Name-"+ Utility.routeDTOS.get(position).getRname());
                routeId = Utility.routeDTOS.get(position).getRoute_id();
                Utility.ROUTE_ID=Utility.routeDTOS.get(position).getRoute_id();
                Utility.ROUTE_NAME=Utility.routeDTOS.get(position).getRname();

                retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
                linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
                retialer_adapter = new RetailerUpdateRecyclerAdapter( retailerDTOS_update,getActivity(),RetailerUpdateFragment.this);
                retailer_list_recyclerView.setAdapter(retialer_adapter);

                Utility.LatestRoute= Utility.routeDTOS.get(position).getRoute_id();

            }
        }));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = etSearch.getText().toString().toLowerCase();
                routeRecyclerAdapter.filter(query);
            }
        });

        wdialog.show();
    }

    public void syncRetailer(String date) {
        retailerDTOS.clear();
        retailerDTOS_update.clear();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.retailer_loding));
        pd.setCancelable(false);
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),
                getResources().getString(R.string.base_url) + "api/ma/sync-master-retailers",
                jp.jsonSYNC(SharePreference.getUserId(getActivity()),SharePreference.getUserBusinessType(getActivity()), SharePreference.getUserPointId(getActivity()),date), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {

                        if (result.equalsIgnoreCase("nettooslow")){
                            pd.dismiss();
                        }
                        try {
                            JSONObject jsonObject1 = new JSONObject(result);

                            //   Log.e("cat_retailer>>", jsonObject1 + "");

                            if (jsonObject1.getString("status").equalsIgnoreCase("1")) {

                                try {
                                    JSONArray routeArray = jsonObject1.getJSONArray("retailer_list");
                                    for (int i = 0; i < routeArray.length(); i++) {
                                        JSONObject routeObject = routeArray.getJSONObject(i);

                                        RetailerDTO retailerDTO = new RetailerDTO();

                                        retailerDTO.setRetailer_id(optStringNullCheck(routeObject,"retailer_id"));

                                        retailerDTO.setRetailer_name(optStringNullCheck(routeObject,"name"));
                                        retailerDTO.setPoint_id( optStringNullCheck(routeObject,"point_id"));
                                        retailerDTO.setRouteId(optStringNullCheck(routeObject,"rid"));
                                        retailerDTO.setStatus(optStringNullCheck(routeObject,"status"));
                                        retailerDTO.setRetailerOwner(optStringNullCheck(routeObject,"owner"));
                                        retailerDTO.setRetailerMobileNo(optStringNullCheck(routeObject,"mobile"));
                                        retailerDTO.setLat(optStringNullCheck(routeObject,"lat"));
                                        retailerDTO.setLon(optStringNullCheck(routeObject,"lon"));

                                        retailerDTOS.add(retailerDTO);

                                    }
                                } catch (JSONException je) {
                                    pd.dismiss();
                                }
                                pd.dismiss();

                            }else {
                                pd.dismiss();
                            }

                         //   Log.e("<<>>",retailerDTOS.size()+"<<>>");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null"))
            return "";
        else
            return json.optString(key, key);
    }
}