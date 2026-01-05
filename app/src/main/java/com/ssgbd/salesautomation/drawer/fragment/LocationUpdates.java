package com.ssgbd.salesautomation.drawer.fragment;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.Map;


public class LocationUpdates extends Fragment implements View.OnClickListener {

    String ADDRESS="";
    TextView txt_address;
    Button btn_checkin,btn_leave;
    View rootView;
    TextView txt_distributorName;
    public RequestQueue queue;
    // ROUTE
    VolleyMethods vm = new VolleyMethods();
    //Retailer list
    TextView txt_location_update;

    GPSTracker gps;
    String LATT="",LONN="";

    LinearLayout linlay_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.location_updates, container, false);
        queue = Volley.newRequestQueue(getActivity());
        txt_distributorName = (TextView)rootView.findViewById(R.id.txt_distributorName);
        txt_distributorName.setText(SharePreference.getDistributorName(getActivity()));
        txt_address = (TextView) rootView.findViewById(R.id.txt_address);
        btn_checkin = (Button) rootView.findViewById(R.id.btn_checkin);
        btn_leave = (Button) rootView.findViewById(R.id.btn_leave);

        btn_checkin.setOnClickListener(this);

        txt_location_update = (TextView) rootView.findViewById(R.id.txt_location_update);

        getLatLon();

        linlay_list = (LinearLayout) rootView.findViewById(R.id.linlay_list);


        return rootView;
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

                //Initialize Google Play Services
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Location Permission already granted

                        if (LATT.length()<2&&LONN.length()<2){
                            Toast.makeText(getActivity(), "লোকেশন পাওয়া যাই নাই। ", Toast.LENGTH_SHORT).show();
                        }
                        else if (LATT.equalsIgnoreCase("0.0")&&LONN.equalsIgnoreCase("0.0")){
                            Toast.makeText(getActivity(), "লোকেশন পাওয়া যাই নাই। (0.0 invalid)", Toast.LENGTH_SHORT).show();
                        }
                        else {
                                sendLocationUpdates(LATT+","+LONN);
                        }
                    } else {
                        //Request Location Permission
                        checkLocationPermission();
                    }
                }else {
                //    for <<lolipop>>

                    if (LATT.length()<2&&LONN.length()<2){
                        Toast.makeText(getActivity(), "লোকেশন পাওয়া যাই নাই। ", Toast.LENGTH_SHORT).show();
                    }  else {
                        sendLocationUpdates("");

                    }
                }
                break;
        }
    }
    public String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null")||json.isNull(key)||json.optString(key).equalsIgnoreCase(""))
            return "";
        else
            return json.optString(key, key);
    }

    public void sendLocationUpdates(String lat_lon){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.attendance_loding));
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),  getString(R.string.base_url)+"api/distributor-lat-long-collect",
                jp.jsonLocationUpdate(SharePreference.getSapCode(getActivity()),lat_lon, SharePreference.getUserId(getActivity())), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();

                      //  Log.e("<<>>",lat_lon+"<<>>");
                       try {
                           JSONObject jsonObject = new JSONObject(result);

                           if (jsonObject.getString("status").equalsIgnoreCase("1")){
                               SharePreference.setDistributorLatLon(getActivity(),lat_lon);
                               Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                           }else {
                               Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                           }
                           //  imageShow();

                       }catch (JSONException je){
                       }
                    }

                });
    }

    public void getLatLon() {

        gps = new GPSTracker(getActivity());

        //check if GPS enabled
        if(gps.canGetLocation()){

            LATT = String.valueOf(gps.getLatitude());
            LONN = String.valueOf(gps.getLongitude());
            txt_location_update.setText(LATT+"--"+LONN);
             //  Log.e(">>>llvisit>>",LATT+"------"+LONN+"");
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
}
