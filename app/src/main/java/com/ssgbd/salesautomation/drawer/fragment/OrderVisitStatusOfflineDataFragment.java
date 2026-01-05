package com.ssgbd.salesautomation.drawer.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.OrderVisitOfflineListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.TodaysOrderListRecyclerAdapter;
import com.ssgbd.salesautomation.bucket.BucketAmountWeb;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.dtos.OrderVisitStatusDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class OrderVisitStatusOfflineDataFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    DatabaseHelper databaseHelper;
    OrderVisitOfflineListRecyclerAdapter retialer_adapter;
    RecyclerView retailer_list_recyclerView;

   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<OrderVisitStatusDTO> retailerStatus = new ArrayList<>();
    public RequestQueue queue;

    String formattedDate="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.order_visit_offline_data_fragment, container, false);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
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
            databaseHelper.openDataBase();}

        retailerStatus = databaseHelper.getAllStatus_offline(databaseHelper,SharePreference.getUserId(getActivity()),"no",formattedDate);


     //   Log.e("size>>>>",retailerStatus.size()+"");

        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new OrderVisitOfflineListRecyclerAdapter( retailerStatus,getActivity(), OrderVisitStatusOfflineDataFragment.this);
        retailer_list_recyclerView.setAdapter(retialer_adapter);
        initUi();


        return rootView;
    }

    private void initUi() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    public void submitVisit(final String url, final String retailerId){

     //   Log.e("url",url+"");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

           //     Log.e("responseLOg>>", response.toString() + "");

                try {
                    JSONObject respjsonObj = new JSONObject(response);
                    Toast.makeText(getActivity(), respjsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (respjsonObj.getString("message").equalsIgnoreCase("Success")){
                        databaseHelper.updateOrderVisitOffline(databaseHelper,retailerId,"yes" );
                        retailerStatus.clear();
                        retailerStatus = databaseHelper.getAllStatus_offline(databaseHelper,SharePreference.getUserId(getActivity()),"no",formattedDate);
                        retialer_adapter.notifyDataSetChanged();
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

}
