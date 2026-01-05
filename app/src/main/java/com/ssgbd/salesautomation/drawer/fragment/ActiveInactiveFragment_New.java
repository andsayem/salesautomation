package com.ssgbd.salesautomation.drawer.fragment;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.ssgbd.salesautomation.adapters.ActiveInActiveRecyclerAdapterNew;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter_ActiveINact;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.dtos.ActiveInactiveDTO;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.OrderRepotListDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActiveInactiveFragment_New extends Fragment implements View.OnClickListener {

    View rootView;

    RecyclerView retailer_list_a_i_recyclerView;
    public RequestQueue queue;
    ActiveInActiveRecyclerAdapterNew activeInActiveRecyclerAdapterNew;
    LinearLayoutManager linearLayoutManager1;
    ArrayList<ActiveInactiveDTO> retailerDTOS = new ArrayList<>();
    VolleyMethods vm = new VolleyMethods();
    Button btn_ok;
    LinearLayout linlayhide;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.active_inactive_retailer_fragment_new, container, false);

         // get db connection
        queue = Volley.newRequestQueue(getActivity());

        retailer_list_a_i_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_a_i_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        retailer_list_a_i_recyclerView.setLayoutManager(linearLayoutManager1);
        activeInActiveRecyclerAdapterNew = new ActiveInActiveRecyclerAdapterNew( retailerDTOS,getActivity(),ActiveInactiveFragment_New.this);
        retailer_list_a_i_recyclerView.setAdapter(activeInActiveRecyclerAdapterNew);
        linlayhide = (LinearLayout) rootView.findViewById(R.id.linlayhide);
        httpRequest();

        btn_ok = (Button) rootView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
//                        httpRequest();
//                        linlayhide.setVisibility(View.GONE);
                        ((DrawerMain)getActivity()).displayView(40);
                    }
                });


            }
        });

        return rootView;
    }

    private void initUI() {

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

    public void httpRequest() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
       // Log.e("<<>>",        SharePreference.getUserId(getActivity())+ "<<-->>");

        pd.setMessage("লোডিং ...");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.base_url)+"api/retailer?fo="+SharePreference.getUserId(getActivity()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

             //    Log.e("responsleLOg>>", SharePreference.getUserId(getActivity())+ "<<-->>");
                try {
                    JSONObject respjsonObj = new JSONObject(response);

                    JSONArray jsonArray = respjsonObj.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ActiveInactiveDTO listDTO = new ActiveInactiveDTO();

                        listDTO.setRetailer_id(jsonObject.getString("retailer_id"));
                        listDTO.setRoute_id(jsonObject.getString("route_id"));
                        listDTO.setStatus(jsonObject.getString("status"));
                        listDTO.setActive_inactive_status(jsonObject.getString("active_inactive_status"));
                        listDTO.setRetailer_name(jsonObject.getString("retailer_name"));
                        listDTO.setDivision_name(jsonObject.getString("division_name"));
                        listDTO.setTerritory_name(jsonObject.getString("territory_name"));
                        listDTO.setPoint_name(jsonObject.getString("point_name"));
                        listDTO.setRname(jsonObject.getString("rname"));
                        listDTO.setBusiness_type(jsonObject.getString("business_type"));

                        retailerDTOS.add(listDTO);
                    }

                    activeInActiveRecyclerAdapterNew.notifyDataSetChanged();

                } catch (JSONException e) {

                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
                Toast.makeText(getActivity(), "SSL server error.", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                //  Log.e("<<>>>>",error.toString()+"<<<<");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
    }

    public void sendRequest(String finalobject){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url)+"api/apps/retailer-active-inactive-submit", finalobject, new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                pd.dismiss();
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
//
                         // Log.e("response>> inactiv>",jsonObject1.getString("message")+"");
                   //       Log.e("response>> inactiv>",result+"");
                    linlayhide.setVisibility(View.VISIBLE);

//                    httpRequest();
//                    activeInActiveRecyclerAdapterNew.notifyDataSetChanged();
                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
