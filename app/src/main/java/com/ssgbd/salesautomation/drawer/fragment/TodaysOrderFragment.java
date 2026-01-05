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

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.TodaysOrderListRecyclerAdapter;
import com.ssgbd.salesautomation.bucket.BucketAmountWeb;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TodaysOrderFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    DatabaseHelper databaseHelper;
    TodaysOrderListRecyclerAdapter retialer_adapter;
    RecyclerView retailer_list_recyclerView;

   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<DBOrderDTO> orderListDTOS = new ArrayList<>();

    String formattedDate="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.todays_order_fragment, container, false);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

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

        orderListDTOS = databaseHelper.getTodaysOrderListByDate(databaseHelper,formattedDate, SharePreference.getUserId(getActivity()));

        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new TodaysOrderListRecyclerAdapter( orderListDTOS,getActivity(),TodaysOrderFragment.this);
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

    public void confirmOrder(String orderData, final String routeId_, final String retailerId_, final String poientId_){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer(getActivity(), getResources().getString(R.string.base_url)+"api/apps/order_confirm", orderData, new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.getString("status").equalsIgnoreCase("1")){

                        deleteOrderAfterConfirm(retailerId_);

                        Intent intent = new Intent(getActivity(),BucketAmountWeb.class);
                        intent.putExtra("routeId", routeId_);
                        intent.putExtra("retailerId",retailerId_);
                        intent.putExtra("poient_id",poientId_);
                        startActivity(intent);

                    }else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }

        });
    }
    public void deleteOrder(String retailerID){

        databaseHelper.deleteAOrder(databaseHelper,retailerID);
        for (int i=0;i<orderListDTOS.size();i++){
            if (orderListDTOS.get(i).getRetailerId().equalsIgnoreCase(retailerID)){
                orderListDTOS.remove(i);
            }
        }
        retialer_adapter.notifyDataSetChanged();
    }

    public void deleteOrderAfterConfirm(String retailerID){

        databaseHelper.deleteAOrder(databaseHelper,retailerID);
        for (int i=0;i<orderListDTOS.size();i++){
            if (orderListDTOS.get(i).getRetailerId().equalsIgnoreCase(retailerID)){
                orderListDTOS.remove(i);
            }
        }
    }
}
