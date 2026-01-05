package com.ssgbd.salesautomation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.app.ActionBar;
import android.app.AlertDialog;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ssgbd.salesautomation.adapters.ConfirmOrderRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter_ims;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.ConfirmOrderDTO;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.dtos.OrderVisitStatusDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.gps.GPSTracker;
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

public class IMSFOActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private Toolbar mToolbar;
    TextView txt_toolbar;
    private Dialog wdialog;
    LinearLayoutManager linearLayoutManager1;
    ArrayList<DBOrderDTO> temp_orderDTOS = new ArrayList<>();
    ArrayList<ConfirmOrderDTO> orderDtos = new ArrayList<>();
    ArrayList<OrderVisitStatusDTO> retailerStatus = new ArrayList<>();

    RetailerRecyclerAdapter_ims retialer_adapter;
    RecyclerView retailer_list_recyclerView;

    ArrayList<RetailerDTO> retailerDTOS = new ArrayList<>();
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    String formattedDate="";
    String syncDate="";
    String URL="";
    GPSTracker gps;
    public RequestQueue queue;
    String REASONID="";
    VolleyMethods vm = new VolleyMethods();

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    DatabaseHelper databaseHelper;

    // final order
    String routeId="";
    String LAT="",LON="",ADDRESS="",visit_radioData="";
    private RadioGroup radioGroupVisit,radioGroupNonVisit;
    private RadioButton radio_1, radio_2,  radio_3, radio_4, radio_5;
    private RadioButton radio_11, radio_22,  radio_33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ims_fo_screen);
        context = this;
        queue = Volley.newRequestQueue(this);

        loadToolBar();
        initUi();
    }

    @SuppressLint("WrongConstant")
    private void loadToolBar() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_toolbar = (TextView) mToolbar.findViewById(R.id.txt_toolbar);
        txt_toolbar.setText("Visit ");


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);

        databaseHelper = new DatabaseHelper(context);
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



    }


    private void initUi() {

        String url = getString(R.string.base_url)+"apps/api/route?appsuser_id="+SharePreference.getUserId(context)+"&appsglobal_id="+SharePreference.getUserGlobalId(context);
        httpRequest(url);

    }

    public void httpRequest(String url){

        Utility.routeDTOS.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//Log.e("response",response+"");
                SharePreference.setRouteData(context,response);

                getRouteData();
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

    private void getRouteData() {


        retailer_list_recyclerView = (RecyclerView) findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new RetailerRecyclerAdapter_ims( retailerDTOS,context);
        retailer_list_recyclerView.setAdapter(retialer_adapter);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
     //   Log.e("formattedDate>",formattedDate+"");


        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        syncDate = sdf.format(new Date());




        retailerDTOS.clear();
        try {
            JSONObject respjsonObj = new JSONObject(SharePreference.getRouteData(context));
            JSONArray routeArray = respjsonObj.getJSONArray("route");
            for (int i = 0; i < routeArray.length(); i++) {
                JSONObject routeObject = routeArray.getJSONObject(i);
                RetailerDTO routeDTO = new RetailerDTO();
                routeDTO.setPoint_id(routeObject.getString("point_id"));
                routeDTO.setTerritory(routeObject.getString("territory_id"));
                routeDTO.setRetailer_name(routeObject.getString("rname"));
                routeDTO.setRouteId(routeObject.getString("route_id"));
                retailerDTOS.add(routeDTO);
            }
        }catch (JSONException je){
        }

        retialer_adapter.notifyDataSetChanged();

    }

    public void getLatLon() {

        gps = new GPSTracker(context);

        //check if GPS enabled
        if(gps.canGetLocation()){

            LAT = String.valueOf(gps.getLatitude());
            LON = String.valueOf(gps.getLongitude());

            //    Log.e(">>>llvisit>>",LAT+"------"+LON+"");
        }else{
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings
            new AlertDialog.Builder(context)
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id==android.R.id.home){
           finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){





        }
    }



    public void showVisitDialog() {


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
//                buildGoogleApiClient();
//                mGoogleMap.setMyLocationEnabled(true);

                //    Log.e("permission 111","<<<>>>>"+"00000");


                wdialog =new Dialog(context);
                wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                wdialog.setContentView(R.layout.dialog_visit);
                final Button btnDoneDialog_visit;
                final ImageView imbtnCross;
                final EditText etSearch;

                radio_1 = wdialog.findViewById(R.id.radio_1);
                radio_2 = wdialog.findViewById(R.id.radio_2);
                radio_3 = wdialog.findViewById(R.id.radio_3);
                radio_4 = wdialog.findViewById(R.id.radio_4);
                radio_5 = wdialog.findViewById(R.id.radio_5);
                final RelativeLayout rlDialogCross;
                etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_visit);
                imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
                rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);

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

                        if (visit_radioData.length()==0){
                            Toast.makeText(context, "Please select any reason.", Toast.LENGTH_SHORT).show();
                        }else {

                            if (!isInternetAvailable(context)) {
                              //  internetAlert(getActivity(),"Visit");
                            } else {
                                URL = getResources().getString(R.string.base_url)+"apps/api/visit-submit?appsuser_id="+ SharePreference.getUserId(context)+"&appsglobal_id=1&retailerID="+"0"+"&routeID="+Utility.ROUTE_ID+"&type=2&reasonID="+REASONID+"&remark="+etSearch.getText()+"&lat="+LAT+"&lon="+LON+"&location="+ADDRESS;
                                submitVisit(URL,"Visit");


                            }

                            wdialog.dismiss();
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
    public boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void submitVisit(String url,final String status){

       //   Log.e("url",url+"");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//
              //    Log.e("responseLOg>>", response.toString() + "");

                try {
                    JSONObject respjsonObj = new JSONObject(response);
                    visit_radioData="";
                    Toast.makeText(context, respjsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (respjsonObj.getString("message").equalsIgnoreCase("Success")){
                        databaseHelper.insertOrderStatus(databaseHelper,SharePreference.getUserId(context),
                        Utility.ROUTE_ID,formattedDate,status,Utility.ROUTE_ID,"yes",URL,Utility.V_RETAILER_NAME );

                        getOrderStatus();

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

        retailerStatus = databaseHelper.getAllStatus(databaseHelper,SharePreference.getUserId(context),formattedDate);
        //   Log.e("retailerStatusize>>>", retailerStatus.size()+ "");
        //   Log.e("retailerStatusize>>>", formattedDate+ "");

        if (retailerStatus.size()==0){

        }else {

            for (int i=0;i<retailerDTOS.size();i++){
                for (int j=0;j<retailerStatus.size();j++){
                    if (retailerDTOS.get(i).getRouteId().equalsIgnoreCase(retailerStatus.get(j).getRouteId())){
                        retailerDTOS.get(i).setStatus(retailerStatus.get(j).getStatus());
                    //    Log.e("<<>>",retailerDTOS.get(i).getRouteId()+"<<>>"+retailerStatus.get(j).getRouteId()+"");
                    }
                }
            }
        }

        retialer_adapter.notifyDataSetChanged();

    }
    public void showNonVisitDialog() {


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
//                buildGoogleApiClient();
//                mGoogleMap.setMyLocationEnabled(true);

                //  Log.e("permission 111","<<<>>>>"+"00000");



                wdialog =new Dialog(context);
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
                            Toast.makeText(context, "Please select any reason.", Toast.LENGTH_SHORT).show();
                        }else {
                            if (!isInternetAvailable(context)) {
                            //    internetAlert(getActivity(), "Non-Visit");
                            } else {
                                URL = getResources().getString(R.string.base_url) + "apps/api/visit-submit?appsuser_id=" + SharePreference.getUserId(context) + "&appsglobal_id=1&retailerID=" + "0" + "&routeID=" + Utility.ROUTE_ID + "&type=1&reasonID=" + REASONID + "&remark=" + etSearch.getText() + "&lat=" + LAT + "&lon=" + LON + "&location=" + ADDRESS;
                                submitVisit(URL,"Non-Visit");
                            }
                        }

                        //  String url = getResources().getString(R.string.base_url)+"apps/api/visit-submit?appsuser_id="+ SharePreference.getUserId(getActivity())+"&appsglobal_id=1&retailerID="+Utility.V_RETAILER_ID+"&routeID="+Utility.ROUTE_ID+"&type=1&reasonID="+REASONID+"&remark="+etSearch.getText()+"&lat="+LAT+"&lon="+LON+"&location="+ADDRESS;
                        //  submitVisit(url,etSearch.getText().toString());

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

    public  void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(IMSFOActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(context)
                        .setTitle("prominent disclosure \n Background Location Permission Needed")
                      //  .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setMessage(getString(R.string.location_text))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(IMSFOActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(IMSFOActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(context,
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
                    Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}



