package com.ssgbd.salesautomation.drawer.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.ssgbd.salesautomation.LoginActivity;
import com.ssgbd.salesautomation.PlatformActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;

import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.drawer.SplashActivity;
import com.ssgbd.salesautomation.dtos.OrderVisitStatusDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.gps.GPSTracker;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class VisitFragment extends Fragment implements View.OnClickListener{

    View rootView;
    private Dialog wdialog;
    RouteRecyclerAdapter routeRecyclerAdapter;
    TextView txt_route_list,txt_route_name,txtll,txtdst;
    DatabaseHelper databaseHelper;
    RetailerRecyclerAdapter retialer_adapter;
    RecyclerView retailer_list_recyclerView;

   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<RetailerDTO> retailerDTOS = new ArrayList<>();
    ArrayList<OrderVisitStatusDTO> retailerStatus = new ArrayList<>();

    String routeId="";
    String LAT="",LON="",ADDRESS="",visit_radioData="",R_LAT="",R_LON="";

    private RadioGroup radioGroupVisit,radioGroupNonVisit;
    private RadioButton radio_1, radio_2,  radio_3, radio_4, radio_5;
    private RadioButton radio_11, radio_22,  radio_33;
    public RequestQueue queue;
    String REASONID="";
    VolleyMethods vm = new VolleyMethods();
    String formattedDate="";
    String syncDate="";
    String URL="";
    GPSTracker gps;
    private final Handler handler = new Handler();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.visit_fragment, container, false);
        queue = Volley.newRequestQueue(getActivity());
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
        txtll = (TextView) rootView.findViewById(R.id.txtll);
        txtdst = (TextView) rootView.findViewById(R.id.txtdst);
        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new RetailerRecyclerAdapter( retailerDTOS,getActivity(),this);
        retailer_list_recyclerView.setAdapter(retialer_adapter);
        initUi();
        Date c = Calendar.getInstance().getTime();
       // System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
    //    Log.e("formattedDate>",formattedDate+"");

        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        syncDate = sdf.format(new Date());

       // handler.postDelayed(startRun, 3000);
        checkAttendanceMenu();

    //    syncRetailer("");
        return rootView;
    }

    public void checkAttendanceMenu() {
      //  Log.e("<<1>>","respjsonObj"+"");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.base_url)+"api/apps/api/attendance_check/"+SharePreference.getUserId(getActivity()) , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    final   JSONObject respjsonObj = new JSONObject(response);
//                    Log.e("<<1>>",respjsonObj+"");

                    try{
                        if(respjsonObj.getString("attendence_status").equalsIgnoreCase("true")){

                        }else {
                       //   checkLeave();
                            ((DrawerMain) getActivity()).displayView(1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e("VOLLEY", error.toString());
                //    Utility.dialog.closeProgressDialog();
            }

        }) ;

        queue.add(stringRequest);
    }
    public void checkLeave() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.base_url_hris)+"api/LeaveStatus/GetEmpLeaveStatus?eid="+SharePreference.getEmployeeId(getActivity()) , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               //  Log.e("<<1>>",response+"");

                if (response.equalsIgnoreCase("false")) {
                    ((DrawerMain) getActivity()).displayView(1);
                    Toast.makeText(getActivity(), "আগে আপনার অ্যাটেনডেন্স সম্পন্ন করুন। ", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e("VOLLEY", error.toString());
                //    Utility.dialog.closeProgressDialog();
            }
        }) ;

        queue.add(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderStatus();
    }

    public void getLatLon(String rlat,String rlon) {
        gps = new GPSTracker(getActivity());
      //check if GPS enabled
        if(gps.canGetLocation()){

            LAT = String.valueOf(gps.getLatitude());
            LON = String.valueOf(gps.getLongitude());

            R_LAT = rlat;
            R_LON = rlon;
          //  txtll.setText(LAT+"<-->"+LON+"<<>>"+R_LAT+"<-->"+R_LON);
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
      //  queue = Volley.newRequestQueue(getActivity());
        txt_route_name = (TextView) rootView.findViewById(R.id.txt_route_name);
        txt_route_list = (TextView) rootView.findViewById(R.id.txt_route_list);
        txt_route_list.setOnClickListener(this);
      //  forceLogout();
    }

    public void forceLogout(){

        RequestQueue queue;
        //
        String  url = getResources().getString(R.string.base_url)+"apps/api/force-logout?user_id="+SharePreference.getUserId(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //  Log.e("version>>",response+"");
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equalsIgnoreCase("true")){

                                Utility.LatestRoute="";
                                Utility.ROUTE_ID = "";
                                Utility.ROUTE_NAME = "";
                                Utility.V_RETAILER_ID = "";
                                Utility.V_RETAILER_NAME = "";
                                SharePreference.setUserPointId(getActivity(),"");

                                SharePreference.setIsRetailerBaseSync(getActivity(),"no");
                                SharePreference.setIsCategoryBaseSync(getActivity(),"no");
                                SharePreference.setIsProductBaseSync(getActivity(),"no");

                                SharePreference.setIslogIn(getActivity(), "0");
                                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(loginIntent);
                                getActivity().finish();


                            }else {

                            }

                        }catch (JSONException je){

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //     Log.e("error volley>>",error.toString()+"");
                    }
                }
        ) {
        };
        queue = Volley.newRequestQueue(getActivity());
        queue.getCache().clear();
        postRequest.setShouldCache(false);
        queue.add(postRequest);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
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
                retailerDTOS.clear();

                wdialog.dismiss();
            //  Toast.makeText(getActivity(), Utility.routeDTOS.get(position).getRoute_id(), Toast.LENGTH_SHORT).show();
                Utility.ROUTE_ID = Utility.routeDTOS.get(position).getRoute_id();

                getRetailer(Utility.routeDTOS.get(position).getRoute_id());
              //  Log.e("<<>>", Utility.routeDTOS.get(position).getRoute_id()+"<<>>");
//                for (int i=0;i<retailerDTOS.size();i++){
//                    Log.e("<>",retailerDTOS.get(i).getRetailer_id()+"<>"+retailerDTOS.get(i).getStatus()+"<>");
//                }
                // Log.e("retailerSize",retailerDTOS.size()+"");
              //retialer_adapter.notifyDataSetChanged();
                txt_route_name.setText("Route Name-"+ Utility.routeDTOS.get(position).getRname());
                routeId = Utility.routeDTOS.get(position).getRoute_id();
                Utility.ROUTE_ID=Utility.routeDTOS.get(position).getRoute_id();
                Utility.ROUTE_NAME=Utility.routeDTOS.get(position).getRname();
                retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
                retialer_adapter = new RetailerRecyclerAdapter( retailerDTOS,getActivity(),VisitFragment.this);
                retailer_list_recyclerView.setAdapter(retialer_adapter);

                Utility.LatestRoute= Utility.routeDTOS.get(position).getRoute_id();

                if (!isInternetAvailable(getActivity())) {
                    getOrderStatus();
                } else {
                    retailerStatus(Utility.routeDTOS.get(position).getRoute_id());

                }
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

    public void showVisitDialog() {

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
//                buildGoogleApiClient();
//                mGoogleMap.setMyLocationEnabled(true);
            //    Log.e("permission 111","<<<>>>>"+"00000");

        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_visit);
        final Button btnDoneDialog_visit;
        final ImageView imbtnCross;
        final EditText etSearch;
        final LinearLayout linlay_chkdistence;

        radio_1 = wdialog.findViewById(R.id.radio_1);
        radio_2 = wdialog.findViewById(R.id.radio_2);
        radio_3 = wdialog.findViewById(R.id.radio_3);
        radio_4 = wdialog.findViewById(R.id.radio_4);
        radio_5 = wdialog.findViewById(R.id.radio_5);
        final RelativeLayout rlDialogCross;
        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_visit);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
                linlay_chkdistence = (LinearLayout) wdialog.findViewById(R.id.linlay_chkdistence);
                linlay_chkdistence.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=18.519513,73.868315&destination=18.518496,73.879259&waypoints=18.520561,73.872435|18.519254,73.876614|18.52152,73.877327|18.52019,73.879935&travelmode=driving");
                        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+LAT+","+LON+"&destination="+R_LAT+","+R_LON+"&travelmode=driving");
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
        radioGroupVisit = (RadioGroup) wdialog.findViewById(R.id.radioGroupVisit);
        btnDoneDialog_visit = wdialog.findViewById(R.id.btnDoneDialog_visit);
        btnDoneDialog_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   Log.e("<>",R_LAT+"<>"+R_LON+"<>"+LAT+"<>"+LON+"<>");
                if (R_LAT.equalsIgnoreCase("")||R_LON.equalsIgnoreCase("")||R_LAT.equalsIgnoreCase("null")||R_LON.equalsIgnoreCase("null")){
//                    R_LAT="empty";
//                    R_LON ="empty";
                    Toast.makeText(getActivity(), "-লোকেশন পাওয়া যায় নাই। ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(LAT.equalsIgnoreCase("")||LON.equalsIgnoreCase("")||LAT.equalsIgnoreCase("null")||LON.equalsIgnoreCase("null")){
//                    LAT="empty";
//                    LON="empty";
                    Toast.makeText(getActivity(), "-লোকেশন পাওয়া যায় নাই।_ ", Toast.LENGTH_SHORT).show();

                    return;
                }

                double distance=00;
                try {

                    Location startPoint = new Location("FoLocation");
                    startPoint.setLatitude(Float.parseFloat(LAT));
                    startPoint.setLongitude(Float.parseFloat(LON));

                    Location endPoint = new Location("RetailerLocation");
                    endPoint.setLatitude(Float.parseFloat(R_LAT));
                    endPoint.setLongitude(Float.parseFloat(R_LON));

                    distance = startPoint.distanceTo(endPoint);
                  //  txtdst.setText(String.valueOf(distance));

                 //   Log.e("dist>",distance+"<>"+LAT+"<<>>"+LON+"<<>>");
                  //  Log.e("dist>",distance+"<>"+R_LAT+"<<R>>"+R_LON+"<<R>>");

                }catch (NumberFormatException ne){
                    Toast.makeText(getActivity(), "রিটেইলারের তথ্য হালনাগাদ করুন।", Toast.LENGTH_SHORT).show();
                }

                if (visit_radioData.length()==0){
                    Toast.makeText(getActivity(), "একটি কারণ সিলেক্ট করুন। ", Toast.LENGTH_SHORT).show();
                }else {
                    if (!isInternetAvailable(getActivity())) {
                        internetAlert(getActivity(),"Visit");
                    } else {

                        if (distance>Integer.parseInt(SharePreference.getFoVisitDist(getActivity()))){
                        Toast.makeText(getActivity(), "দয়া করে সঠিক অবস্থানে গিয়ে আপনার ভিজিট দিন।", Toast.LENGTH_SHORT).show();
                        }else {

                            URL = getResources().getString(R.string.base_url) + "apps/api/visit-submit?appsuser_id=" + SharePreference.getUserId(getActivity()) + "&appsglobal_id=1&retailerID=" + Utility.V_RETAILER_ID + "&routeID=" + Utility.ROUTE_ID + "&type=2&reasonID=" + REASONID + "&remark=" + etSearch.getText() + "&lat=" + LAT + "&lon=" + LON + "&location=" + ADDRESS;
                            submitVisit(URL, "Visit");
                        }
                    }
                   // wdialog.dismiss();
                }
            }
        });
        radioGroupVisit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radio_1) {
                    visit_radioData=radio_1.getText().toString();
                 //   Log.e("Stock not available",visit_radioData+"");
                    REASONID = "1";

                } else if(checkedId == R.id.radio_2) {
                    visit_radioData=radio_2.getText().toString();
                    REASONID = "2";
                 //   Log.e("Shop Closed",visit_radioData+"");

                } else if(checkedId == R.id.radio_3){
                    visit_radioData=radio_3.getText().toString();
                    REASONID = "3";
                 //   Log.e("Insufficient funds",visit_radioData+"");

                }else if(checkedId == R.id.radio_4){
                    visit_radioData=radio_4.getText().toString();
                    REASONID = "4";
                  //  Log.e("Products available",visit_radioData+"");

                }else if(checkedId == R.id.radio_5){
                    visit_radioData=radio_5.getText().toString();
                    REASONID = "5";
                 //   Log.e("Others",visit_radioData+"");

                }else{
                  //  Log.e("else","");
                }
            }
        });

        wdialog.show();

            } else {
                //Request Location Permission
                checkLocationPermission();
              //  Log.e("permission 2222","<<<>>>>"+"111111");
            }
        }
    }

    public void showNonVisitDialog() {

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
//                buildGoogleApiClient();
//                mGoogleMap.setMyLocationEnabled(true);

              //  Log.e("permission 111","<<<>>>>"+"00000");

        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_non_visit);
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        radioGroupNonVisit = (RadioGroup) wdialog.findViewById(R.id.myRadioGroup);
        final RelativeLayout rlDialogCross;
        radio_11 = wdialog.findViewById(R.id.radio11);
        radio_22 = wdialog.findViewById(R.id.radio22);
        radio_33 = wdialog.findViewById(R.id.radio33);
        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_search);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog_nonvisit);

        radioGroupNonVisit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radio11) {
                    visit_radioData=radio_11.getText().toString();
                  //  Log.e("Stock not available",visit_radioData+"");
                    REASONID = "1";
                } else if(checkedId == R.id.radio22) {
                    visit_radioData=radio_22.getText().toString();
                   // Log.e("Shop Closed",visit_radioData+"");
                    REASONID = "2";
                } else if(checkedId == R.id.radio33){
                    visit_radioData=radio_33.getText().toString();
                   // Log.e("Shop Closed",visit_radioData+"");
                    REASONID = "3";
                }else{
                   // Log.e("else","<>");
                }
            }

        });

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

                if (visit_radioData.length()==0){
                    Toast.makeText(getActivity(), "Please select any reason.", Toast.LENGTH_SHORT).show();
                }else {
                    if (!isInternetAvailable(getActivity())) {
                        internetAlert(getActivity(), "Non-Visit");
                    } else {
                        URL = getResources().getString(R.string.base_url) + "apps/api/visit-submit?appsuser_id=" + SharePreference.getUserId(getActivity()) + "&appsglobal_id=1&retailerID=" + Utility.V_RETAILER_ID + "&routeID=" + Utility.ROUTE_ID + "&type=1&reasonID=" + REASONID + "&remark=" + etSearch.getText() + "&lat=" + LAT + "&lon=" + LON + "&location=" + ADDRESS;
                        submitVisit(URL,"Non-Visit");
                     //   Log.e("<<>>",URL+"");
                    }
                }
                wdialog.dismiss();
            }
        });
        wdialog.show();

            } else {
                //Request Location Permission
                checkLocationPermission();
              //  Log.e("permission 2222","<<<>>>>"+"111111");
            }
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

    public void submitVisit(String url,final String status){

      //  Log.e("url",url+"");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            //    Log.e("responseLOg>>", response.toString() + "");

                try {
                    JSONObject respjsonObj = new JSONObject(response);
                    visit_radioData="";
                    Toast.makeText(getActivity(), respjsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                   if (respjsonObj.getString("message").equalsIgnoreCase("Success")){
                       wdialog.dismiss();
                       databaseHelper.insertOrderStatus(databaseHelper,SharePreference.getUserId(getActivity()),
                               Utility.V_RETAILER_ID,formattedDate,status,Utility.ROUTE_ID,"yes",URL,Utility.V_RETAILER_NAME );


                       if (!isInternetAvailable(getActivity())) {
                           getOrderStatus();
                       } else {
                           retailerStatus(Utility.LatestRoute);
                       }
                   }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
            }
        });
        queue.add(stringRequest);
    }

    public void getOrderStatus(){
//
        retailerStatus = databaseHelper.getAllStatus(databaseHelper,SharePreference.getUserId(getActivity()),formattedDate);

                    if (retailerStatus.size()==0){

                    }else {

                        for (int i=0;i<retailerDTOS.size();i++){
                            for (int j=0;j<retailerStatus.size();j++){
                                if (retailerDTOS.get(i).getRetailer_id().equalsIgnoreCase(retailerStatus.get(j).getRetailerId())){
                                    retailerDTOS.get(i).setStatus(retailerStatus.get(j).getStatus());
                                }
                            }
                        }
                    }

                    retialer_adapter.notifyDataSetChanged();

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

                if (!isInternetAvailable(getActivity())) {
                    getOrderStatus();
                } else {
                    retailerStatus(Utility.LatestRoute);

                }
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

    public void retailerStatus(String routeId){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("লোডিং ...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer(getActivity(), getResources().getString(R.string.base_url)+"api/retailer-wise-status",
                jp.jsonRetailerStatus(SharePreference.getUserId(getActivity()),routeId), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("statusAll");

               //     Log.e("<<>>",routeId+"<>"+jsonArray+"");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                            for (int u=0;u<retailerDTOS.size();u++){
                                for (int j=0;j<jsonArray.length();j++){
                                    if (retailerDTOS.get(u).getRetailer_id().equalsIgnoreCase(dataObject.getString("retailer_id"))){

                                        if (dataObject.getString("status").equalsIgnoreCase("2")) {
                                            retailerDTOS.get(u).setStatus("Visit");
                                        } if (dataObject.getString("status").equalsIgnoreCase("1")) {
                                            retailerDTOS.get(u).setStatus("Non-Visit");
                                        } if (dataObject.getString("status").equalsIgnoreCase("3")) {
                                            retailerDTOS.get(u).setStatus("Ordered");
                                        }
                                }
                            }
                        }
                    }

                    retialer_adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        });
    }


    public void syncRetailer(String date) {

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

//                          Log.e("cat_retailer>>", jsonObject1 + "");

                            if (jsonObject1.getString("status").equalsIgnoreCase("1")) {

                                try {
                                    JSONArray routeArray = jsonObject1.getJSONArray("retailer_list");
                                    for (int i = 0; i < routeArray.length(); i++) {
                                        JSONObject routeObject = routeArray.getJSONObject(i);

                                        databaseHelper.insertOrUpdateNewRetailer(databaseHelper,
                                                routeObject.getString("retailer_id"),
                                                routeObject.getString("name"),
                                                routeObject.getString("division"),
                                                routeObject.getString("territory"),
                                                routeObject.getString("point_id"),
                                                routeObject.getString("rid"),
                                                routeObject.getString("shop_type"),
                                                routeObject.getString("owner"),
                                                routeObject.getString("mobile"),
                                                routeObject.getString("tnt"),
                                                routeObject.getString("email"),
                                                routeObject.getString("dateandtime"),
                                                routeObject.getString("user"),
                                                routeObject.getString("status"),
                                                routeObject.getString("dob"),
                                                routeObject.getString("vAddress"),
                                                routeObject.getString("global_company_id"),
                                                routeObject.getString("inactive_user"),
                                                routeObject.getString("inactive_date_time"),
                                                routeObject.getString("inactive_ip"),
                                                routeObject.getString("iApproval"),
                                                routeObject.getString("reminding_commission_balance"),
                                                routeObject.getString("opening_balance"),
                                                routeObject.getString("opening_balance_accessories"),
                                                optStringNullCheckZero(routeObject,"serial"),
                                                routeObject.getString("after_retailers"),
                                                routeObject.getString("lat"),
                                                routeObject.getString("lon")

//                                               routeObject.getString("sync"),
//                                               routeObject.getString("reminder_start"),
//                                               routeObject.getString("reminder_end"),
//                                               routeObject.getString("addedOrUpdateType"),
//                                               routeObject.getString("addedOrUpdateDate")
                                        );
                                        //"sync": "No"
                                    }

                                } catch (JSONException je) {
                                    pd.dismiss();
                                }
                                pd.dismiss();
//                                SharePreference.setIsRetailerBaseSync(getActivity(),"yes");
//                                SharePreference.setRetailerBaseSyncDate(getActivity(),syncDate);
                            }else {
                                pd.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                                   // Log.e("<<>>",routeArray+"");
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

                        retialer_adapter.notifyDataSetChanged();
                                    retailerStatus(Utility.LatestRoute);

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