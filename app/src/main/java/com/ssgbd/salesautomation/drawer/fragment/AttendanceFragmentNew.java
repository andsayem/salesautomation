package com.ssgbd.salesautomation.drawer.fragment;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapterAttendance;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.dtos.AttendanceReportListDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;


public class AttendanceFragmentNew extends Fragment implements View.OnClickListener {


    String ADDRESS="";
    TextView txt_address,txt_alert;
    Button btn_checkin,btn_leave;
    View rootView;
    TextView txt_foname,txt_date,txt_location,txt_distributorName;
    String RETAILERID="",ROUTEID="";
    public RequestQueue queue;
    String checkOut="";
    String TAG = "Attendance";
    // ROUTE
    TextView txt_route_list;
    private Dialog wdialog;
    private Dialog wdialogRetailer;
    RouteRecyclerAdapter routeRecyclerAdapter;
    ArrayList<RetailerDTO> retailerDTOS = new ArrayList<>();
    DatabaseHelper databaseHelper;
    RetailerRecyclerAdapterAttendance retialer_adapter;

 // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;

    VolleyMethods vm = new VolleyMethods();
    //Retailer list
    TextView txt_retailer_name;
    TextView txt_in_retailer,txt_in_route,txt_out_time,txt_out_retailer,txt_out_route,txt_out_address,txt_working_hour;

    GPSTracker gps;
    String PRESENT_LATT="",PRESENT_LONN="";
    double distance;
    LinearLayout linlay_chkdistence;
    String R_LAT="0",R_LON="0";
    String formattedDate="";
    LinearLayout linlay_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.attendance_fragment_new, container, false);
        queue = Volley.newRequestQueue(getActivity());
        txt_distributorName = (TextView)rootView.findViewById(R.id.txt_distributorName);
        txt_distributorName.setText(SharePreference.getDistributorName(getActivity()));
        txt_address = (TextView) rootView.findViewById(R.id.txt_address);
        txt_alert = (TextView) rootView.findViewById(R.id.txt_alert);
        btn_checkin = (Button) rootView.findViewById(R.id.btn_checkin);
        btn_leave = (Button) rootView.findViewById(R.id.btn_leave);

        btn_checkin.setText(SharePreference.getButtonText(getActivity()));
        btn_checkin.setOnClickListener(this);

        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

//        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapFrag.getMapAsync(this);

        txt_foname = (TextView) rootView.findViewById(R.id.txt_foname);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        txt_location = (TextView) rootView.findViewById(R.id.txt_location);

        txt_in_retailer = (TextView) rootView.findViewById(R.id.txt_in_retailer);
        txt_in_route = (TextView) rootView.findViewById(R.id.txt_in_route);
        txt_out_time = (TextView) rootView.findViewById(R.id.txt_out_time);
        txt_out_retailer = (TextView) rootView.findViewById(R.id.txt_out_retailer);
        txt_out_route = (TextView) rootView.findViewById(R.id.txt_out_route);
        txt_out_address = (TextView) rootView.findViewById(R.id.txt_out_address);
        txt_working_hour = (TextView) rootView.findViewById(R.id.txt_working_hour);
        checkLocationPermission1();
        initRoute();
        initRetailer();

        getLatLon();
        getReport(formattedDate,formattedDate);

        checkAttendance();

        linlay_chkdistence = (LinearLayout) rootView.findViewById(R.id.linlay_chkdistence);
        linlay_chkdistence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=18.519513,73.868315&destination=18.518496,73.879259&waypoints=18.520561,73.872435|18.519254,73.876614|18.52152,73.877327|18.52019,73.879935&travelmode=driving");
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+PRESENT_LATT+","+PRESENT_LONN+"&destination="+R_LAT+","+R_LON+"&travelmode=driving");
                Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    try {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        startActivity(unrestrictedIntent);
                    } catch (ActivityNotFoundException innerEx) {
                        Toast.makeText(getActivity(), "Please install a maps application", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        linlay_list = (LinearLayout) rootView.findViewById(R.id.linlay_list);
        linlay_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((DrawerMain)getActivity()).displayView(46);
                ((DrawerMain) getActivity()).setTitle("Report");

            }
        });

        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        startActivityForResult(data, requestCode);
       //   Log.e("intent>",requestCode+"---"+resultCode+"<>>"+data);

//        if (requestCode == 0) {
//            if (resultCode == CommonStatusCodes.SUCCESS) {
//                if (data != null) {
////
//                }else {
//                    Toast.makeText(getActivity(), "Please Try Again..", Toast.LENGTH_SHORT).show();
////                    txt_bp_id_confrim.setText("BP Not Confirmed !");
//                }
//            }}
    }
    private void initRetailer() {

        txt_retailer_name = (TextView) rootView.findViewById(R.id.txt_retailer_name);
        txt_retailer_name.setOnClickListener(this);
    }

    private void initRoute() {
        txt_route_list = (TextView) rootView.findViewById(R.id.txt_route_list);
        txt_route_list.setOnClickListener(this);
        databaseHelper = new DatabaseHelper(getActivity());
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myPath = Utility.DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()){
            databaseHelper.openDataBase();}
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Prominent disclosure \nLocation Permission Needed")
                        .setMessage(R.string.location_text)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                                getLatLon();

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
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.btn_checkin:

                if (distance>Integer.parseInt(SharePreference.getFoVisitDist(getActivity()))){
                    Toast.makeText(getActivity(), "দয়া করে সঠিক অবস্থানে গিয়ে আপনার অ্যাটেনডেন্স দিন।", Toast.LENGTH_SHORT).show();
                  //  Toast.makeText(getActivity(),"আপনি "+ String.valueOf(String.format("%.0f", distance))+" মিটার দূরে আছেন।", Toast.LENGTH_SHORT).show();
                    txt_alert.setText("আপনি "+ String.valueOf(String.format("%.0f", distance))+" মিটার দূরে আছেন।");
                }else {
//                Toast.makeText(getActivity(),"আপনি "+ String.valueOf(String.format(" %.0f", distance))+" মিটার দূরে আছেন।", Toast.LENGTH_SHORT).show();

                    //Initialize Google Play Services
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            //Location Permission already granted

                            if (ROUTEID.length() == 0) {
                                txt_route_list.setTextColor(Color.parseColor("#ff0000"));
                                Toast.makeText(getActivity(), "আপনার রুট সিলেক্ট করুন।", Toast.LENGTH_SHORT).show();
                            }
//                        else if(RETAILERID.length()==0){
//                            Toast.makeText(getActivity(), "আপনার রিটেইলার সিলেক্ট করুন।", Toast.LENGTH_SHORT).show();
//                        }
                            else {
                                if (btn_checkin.getText().toString().equalsIgnoreCase("CHECK IN")) {
                                    // String url=getString(R.string.base_url_hris)+"api/CheckInOutInfoSales/UpdateAttendanceFromApp?q=0&"+"id="+SharePreference.getEmployeeId(getActivity())+"&src=SA";
                                    checkOut = "";
                                    //  checkInHRIS(url);
                                    doAttendanceSA(getResources().getString(R.string.base_url) + "api/apps/api/attendance");

                                } else {
                                    checkOut = "1";
                                    //   String url=getString(R.string.base_url_hris)+"api/CheckInOutInfoSales/UpdateAttendanceFromApp?q=1&"+"id="+SharePreference.getEmployeeId(getActivity())+"&src=SA";
                                    // checkInHRIS(url);
                                    doAttendanceSA(getResources().getString(R.string.base_url) + "api/apps/api/attendance");
                                }
                            }
                        } else {
                            //Request Location Permission
                            checkLocationPermission();
                        }
                    } else {
                        //    for <<lolipop>>
                        if (ROUTEID.length() == 0) {
                            txt_route_list.setTextColor(Color.parseColor("#ff0000"));
                            Toast.makeText(getActivity(), "রুট সিলেক্ট করুন।", Toast.LENGTH_SHORT).show();
                        } else {
                            doAttendanceSA(getResources().getString(R.string.base_url) + "api/apps/api/attendance");
                            checkOut = "1";
                        }
                    }
                }
                break;

            case R.id.txt_route_list:
                showRouteListDialog();
                break;
            case R.id.txt_retailer_name:
                if (ROUTEID.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "রুট সিলেক্ট করুন। ", Toast.LENGTH_SHORT).show();
                }else {
                    showRetailerListDialog();
                }
                break;
        }
    }

    private void showRouteListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView,retailer_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        retailerDTOS.clear();
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

                Location startPoint=new Location("FoLocation");
                startPoint.setLatitude(Double.parseDouble(PRESENT_LATT));
                startPoint.setLongitude(Double.parseDouble(PRESENT_LONN));

                if (SharePreference.getDistributorLatLon(getActivity()).equalsIgnoreCase("")||SharePreference.getDistributorLatLon(getActivity()).equalsIgnoreCase("null")) {
                    SharePreference.setDistributorLatLon(getActivity(), "0.0, 0.0");
                }
             //   Log.e("<<>>",SharePreference.getDistributorLatLon(getActivity())+"<<>>");
                // DB point lat lon collect
                StringTokenizer tokens = new StringTokenizer(SharePreference.getDistributorLatLon(getActivity()), ",");
                String l = tokens.nextToken();// this will contain "lat"
                String lo = tokens.nextToken();// this will contain " lon


                R_LAT = l.replaceAll("\\s", "");
                R_LON = lo.replaceAll("\\s", "");
                if (R_LAT.equalsIgnoreCase("")||R_LON.equalsIgnoreCase("")){
                    R_LAT="0";
                    R_LON="0";
                }

                Location endPoint=new Location("DBLocation");
                endPoint.setLatitude(Double.parseDouble(R_LAT));
                endPoint.setLongitude(Double.parseDouble(R_LON));

                distance=startPoint.distanceTo(endPoint);
//                Log.e("<<>>",PRESENT_LATT+"<<>>"+PRESENT_LONN+"<<>>");
//                Log.e("<<>>",R_LAT+"<<>>"+R_LON+"");
//                Log.e("<<>>",distance+"");

                if (distance>Integer.parseInt(SharePreference.getFoVisitDist(getActivity()))){
                   // Toast.makeText(getActivity(),"আপনি "+ String.valueOf(String.format(" %.0f", distance))+" মিটার দূরে আছেন।", Toast.LENGTH_SHORT).show();

                    txt_alert.setText("আপনি "+ String.valueOf(String.format("%.0f", distance))+" মিটার দূরে আছেন।");

                }

                //comment due to retailer attendance off
                //retailerDTOS = databaseHelper.getRetailerList(databaseHelper,Utility.routeDTOS.get(position).getRoute_id());

                ROUTEID=Utility.routeDTOS.get(position).getRoute_id();
            getRetailer(Utility.routeDTOS.get(position).getRoute_id());
                txt_route_list.setText(Utility.routeDTOS.get(position).getRname());
                txt_route_list.setTextColor(Color.parseColor("#636262"));
                txt_retailer_name.setText("--Select Retailer--");
                RETAILERID="";
                wdialog.dismiss();

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

    private void showRetailerListDialog() {
        wdialogRetailer =new Dialog(getActivity());
        wdialogRetailer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialogRetailer.setContentView(R.layout.dialog_retailer_list);
        final RecyclerView retailer_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;

        final RelativeLayout rlDialogCross;

        etSearch = (EditText)wdialogRetailer.findViewById(R.id.edt_txt_search);
        retailer_list_recyclerView=(RecyclerView) wdialogRetailer.findViewById(R.id.retailer_list_recyclerView);
        imbtnCross=(ImageView)wdialogRetailer.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialogRetailer.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialogRetailer.findViewById(R.id.btnDoneDialog);

        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wdialogRetailer.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialogRetailer.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialogRetailer.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        retailer_list_recyclerView.setLayoutManager(linearLayoutManager);
        retialer_adapter = new RetailerRecyclerAdapterAttendance(retailerDTOS,getActivity());
        retailer_list_recyclerView.setAdapter(retialer_adapter);

        retailer_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                txt_retailer_name.setText(retailerDTOS.get(position).getRetailer_name());
                RETAILERID = retailerDTOS.get(position).getRetailer_id();
                txt_retailer_name.setTextColor(Color.parseColor("#636262"));

               if(retailerDTOS.get(position).getLat().equalsIgnoreCase("")||retailerDTOS.get(position).getLat().equalsIgnoreCase("null")){
                   R_LAT="0";
                }else{
                   R_LAT = retailerDTOS.get(position).getLat();
                }
                if(retailerDTOS.get(position).getLon().equalsIgnoreCase("")||retailerDTOS.get(position).getLon().equalsIgnoreCase("null")){
                    R_LON="0";
                }else{
                    R_LON = retailerDTOS.get(position).getLon();
                }

                Location startPoint=new Location("FoLocation");
                startPoint.setLatitude(Float.parseFloat(PRESENT_LATT));
                startPoint.setLongitude(Float.parseFloat(PRESENT_LONN));

                Location endPoint=new Location("RetailerLocation");
                endPoint.setLatitude(Float.parseFloat(R_LAT));
                endPoint.setLongitude(Float.parseFloat(R_LON));

                 distance=startPoint.distanceTo(endPoint);

                if (distance>Integer.parseInt(SharePreference.getFoVisitDist(getActivity()))){
                 //  Toast.makeText(getActivity(),"আপনি রিটেলার থেকে"+ String.valueOf(String.format(" %.0f", distance))+" মিটার দূরে আছেন।", Toast.LENGTH_SHORT).show();
                    txt_alert.setText("আপনি রিটেলার থেকে"+ String.valueOf(String.format(" %.0f", distance))+" মিটার দূরে আছেন।");

                }

                wdialogRetailer.dismiss();
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
                retialer_adapter.filter(query);
            }
        });

        wdialogRetailer.show();
    }

    public  void doAttendanceSA(String url){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              // Log.e("<<>>",response+"");
              // {"status":"1","message":"Success"}

                try {
                    JSONObject respjsonObj = new JSONObject(response);
                    if (respjsonObj.getString("status").equalsIgnoreCase("1")) {

                        if (checkOut.equalsIgnoreCase("1")) {
                            Toast.makeText(getActivity(), "আপনার চেকআউট  সফল হয়েছে।", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "আপনার চেকইন সফল হয়েছে।", Toast.LENGTH_LONG).show();
                        }
                        checkAttendance();
                    } else {
                        Toast.makeText(getActivity(), "আবার চেক ইন দিন।", Toast.LENGTH_LONG).show();
                    }
                }  catch (JSONException e) {

                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("appsUser_id",SharePreference.getUserId(getActivity()));
                params.put("appsGlobal_id",SharePreference.getUserGlobalId(getActivity()));
                params.put("appsLag", PRESENT_LATT);
                params.put("appsLog",PRESENT_LONN);
                params.put("appsLocation",ADDRESS);
                params.put("distributor",SharePreference.getDistributorID(getActivity()));
                params.put("retailer",RETAILERID);
                params.put("route",ROUTEID);
                params.put("appsType","1");

  //            Log.e("<<>>",params+"");

                return params;
            }
        };
        queue.add(sr);
    }

    public String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null")||json.isNull(key)||json.optString(key).equalsIgnoreCase(""))
            return "";
        else
            return json.optString(key, key);
    }

    public void checkAttendance() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.base_url)+"api/apps/api/attendance_check/"+SharePreference.getUserId(getActivity()) , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respjsonObj = new JSONObject(response);
                 //   Log.e("<<>>",respjsonObj+"");
                    if(respjsonObj.getString("attendence_status").equalsIgnoreCase("true")){
                        btn_checkin.setText("CHECK OUT");
                        btn_leave.setVisibility(View.GONE);
                        txt_retailer_name.setVisibility(View.VISIBLE);
                    }else {
                        btn_checkin.setText("CHECK IN");
                        btn_leave.setVisibility(View.GONE);
                        txt_retailer_name.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {

                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                //    Utility.dialog.closeProgressDialog();
            }

        }) ;

        queue.add(stringRequest);
    }
    public void getReport(String fromDAte,String TODate){
       // Log.e("<<>>",fromDAte+TODate+"<>");
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.attendance_loding));
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),  getString(R.string.base_url)+"api/report/fo-attendance",
                jp.jsonReportOrderVsReport( SharePreference.getUserId(getActivity()),fromDAte,TODate), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();
                      //  Log.e("result",result+"result");

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                                     //  Log.e("jsonObject1",jsonObject1+"");
                                    // Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();

                                    if (jsonObject1.getString("status").equalsIgnoreCase("1")){

                                        JSONArray jsonArray = jsonObject1.getJSONArray("fo_attendance");
                                        for (int i=0;i<jsonArray.length();i++){
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            AttendanceReportListDTO listDTO = new AttendanceReportListDTO();
                                            //in
                                            txt_foname.setText(SharePreference.getUserName(getActivity()));
                                            txt_date.setText(optStringNullCheck(object,"date")+"-"+optStringNullCheck(object,"inTme"));
                                            txt_in_retailer.setText(optStringNullCheck(object,"inTimeRetailerName"));
                                            txt_in_route.setText(optStringNullCheck(object,"inRoute"));
                                            txt_location.setText(optStringNullCheck(object,"inTimeRetailerAddress"));
                                            //out
                                            txt_out_time.setText(optStringNullCheck(object,"outTime"));
                                            txt_out_retailer.setText(optStringNullCheck(object,"outTimeRetailerName"));
                                            txt_out_route.setText(optStringNullCheck(object,"outRoute"));
                                            txt_out_address.setText(optStringNullCheck(object,"outTimeRetailerAddress"));
                                            txt_working_hour.setText(optStringNullCheck(object,"workingHour"));
                                        }
                                    }else {
                                        Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }

    public void getLatLon() {

        gps = new GPSTracker(getActivity());

        //check if GPS enabled
        if(gps.canGetLocation()){

            PRESENT_LATT = String.valueOf(gps.getLatitude());
            PRESENT_LONN = String.valueOf(gps.getLongitude());

         //   Log.e("<<>>",PRESENT_LATT+"<<>>"+PRESENT_LONN+"");

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
    }

    public  void checkLocationPermission1() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("prominent disclosure \n Background Location Permission Needed")
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
                                ActivityCompat.requestPermissions((Activity)getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                                getLatLon();
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity)getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    public void getRetailer(String routeId) {
        retailerDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.retailer_loding));
        pd.setCancelable(false);
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),
                getResources().getString(R.string.base_url) + "api/ma/get-master-retailers",
                jp.getRretailer(routeId, SharePreference.getUserPointId(getActivity())), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {

                        if (result.equalsIgnoreCase("nettooslow")){
                            pd.dismiss();
                        }
                        try {
                            JSONObject jsonObject1 = new JSONObject(result);

                            if (jsonObject1.getString("status").equalsIgnoreCase("1")) {

                                try {
                                    JSONArray routeArray = jsonObject1.getJSONArray("retailer_list");
                                    for (int i = 0; i < routeArray.length(); i++) {
                                        JSONObject routeObject = routeArray.getJSONObject(i);
                                        RetailerDTO retailerDTO = new RetailerDTO();
                                        retailerDTO.setRetailer_id( routeObject.getString("retailer_id"));
                                        retailerDTO.setRetailer_name( routeObject.getString("name"));
                                        retailerDTO.setDivision( routeObject.getString("division"));
                                        retailerDTO.setTerritory( routeObject.getString("territory"));
                                        retailerDTO.setPoint_id( routeObject.getString("point_id"));
                                        retailerDTO.setRouteId( routeObject.getString("rid"));
                                        retailerDTO.setShopeType( routeObject.getString("shop_type"));
                                        retailerDTO.setRetailerOwner( routeObject.getString("owner"));
                                        retailerDTO.setRetailerMobileNo( routeObject.getString("mobile"));
                                        retailerDTO.setTnt( routeObject.getString("tnt"));
                                        retailerDTO.setEmail( routeObject.getString("email"));
                                        retailerDTO.setDateandtime( routeObject.getString("dateandtime"));
                                        retailerDTO.setUser( routeObject.getString("user"));
//                                      retailerDTO.setStatus( routeObject.getString("status"));
                                        retailerDTO.setDob( routeObject.getString("dob"));
                                        retailerDTO.setvAddress( routeObject.getString("vAddress"));
                                        retailerDTO.setGlobal_company_id( routeObject.getString("global_company_id"));
                                        retailerDTO.setInactive_user( routeObject.getString("inactive_user"));
                                        retailerDTO.setInactive_date_time( routeObject.getString("inactive_date_time"));
                                        retailerDTO.setInactive_ip( routeObject.getString("inactive_ip"));
                                        retailerDTO.setiApproval( routeObject.getString("iApproval"));
                                        retailerDTO.setReminding_commission_balance( routeObject.getString("reminding_commission_balance"));
                                        retailerDTO.setOpening_balance( routeObject.getString("opening_balance"));
                                        retailerDTO.setOpening_balance_accessories( routeObject.getString("opening_balance_accessories"));
                                        retailerDTO.setSerial( optStringNullCheckZero(routeObject,"serial"));
                                        // Log.e("<<>>",optStringNullCheckZero(routeObject,"serial")+"");
                                        retailerDTO.setAfter_retailers( routeObject.getString("after_retailers"));
                                        retailerDTO.setLat( routeObject.getString("lat"));
                                        retailerDTO.setLon( routeObject.getString("lon"));
                                        retailerDTO.setSync( routeObject.getString("sync"));
                                        retailerDTO.setReminder_start( routeObject.getString("reminder_start"));
                                        retailerDTO.setReminder_end( routeObject.getString("reminder_end"));
                                        retailerDTO.setAddedOrUpdateType( routeObject.getString("addedOrUpdateType"));
                                        retailerDTO.setAddedOrUpdateDate( routeObject.getString("addedOrUpdateDate"));
                                        retailerDTOS.add(retailerDTO);

                                    }

                                  //  retialer_adapter.notifyDataSetChanged();

                                } catch (JSONException je) {
                                    pd.dismiss();
                                }
                                pd.dismiss();

                            }else {
                                pd.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public String optStringNullCheckZero(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null")||json.isNull(key)||json.optString(key).equalsIgnoreCase(""))
            return "0";
        else
            return json.optString(key, key);
    }
}
