package com.ssgbd.salesautomation.drawer.fragment;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.ssgbd.salesautomation.adapters.ManageOrderListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.SmartManageOrderListRecyclerAdapter;
import com.ssgbd.salesautomation.bucket.BucketAmountWeb;
import com.ssgbd.salesautomation.bucket.ManageOrderActivityWeb;
import com.ssgbd.salesautomation.dtos.ManageOrderDTO;
import com.ssgbd.salesautomation.dtos.SmartOrderDTO;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SmartOrderManageFragment extends Fragment {

    View rootView;
    RecyclerView smart_order_list_recyclerView;
    SmartManageOrderListRecyclerAdapter smart_order_adapter;
    LinearLayoutManager linearLayoutManager1;
    ArrayList<SmartOrderDTO> smartOrderDTOS = new ArrayList<>();

    boolean isPageError = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.smart_order_manage_fragment, container, false);



        initUi(rootView);
        return rootView;
    }

    private void initUi(View rootView) {
        smart_order_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.smart_order_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        smart_order_list_recyclerView.setLayoutManager(linearLayoutManager1);
        smart_order_adapter = new SmartManageOrderListRecyclerAdapter( smartOrderDTOS,getActivity(),SmartOrderManageFragment.this);
        smart_order_list_recyclerView.setAdapter(smart_order_adapter);

//        smart_order_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//
//                Toast.makeText(getActivity(), "Processing...", Toast.LENGTH_LONG).show();
//
////                Intent intent = new Intent(getActivity(),ManageOrderActivityWeb.class);
////                intent.putExtra("orderid",orderListDTOS.get(position).getOrder_id());
////                intent.putExtra("retailerId",orderListDTOS.get(position).getRetailer_id());
////                intent.putExtra("routeId",orderListDTOS.get(position).getRoute_id());
////                startActivity(intent);
//
//            }
//        }));

        getOrderList();
    }

    private void getOrderList() {


        RequestQueue queue = null;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.base_url)+"api/retailer-app/fo/smart-orders?fo_id="+SharePreference.getUserLoginId(getActivity()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                     //   Log.e("<<>>",response+"");

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray routeArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < routeArray.length(); i++) {
                                JSONObject routeObject = routeArray.getJSONObject(i);
                                SmartOrderDTO manageOrderDTO = new SmartOrderDTO();
                                manageOrderDTO.setId(routeObject.getString("id"));
                                manageOrderDTO.setRetailer_id(routeObject.getString("retailer_id"));
                                manageOrderDTO.setRetailer_name(routeObject.getString("retailer_name"));
                                manageOrderDTO.setRetailer_phone(routeObject.getString("retailer_phone"));
                                manageOrderDTO.setLat_long(routeObject.getString("lat_long"));
                                manageOrderDTO.setRoute_id(routeObject.getString("route_id"));
                                manageOrderDTO.setRoute_name(routeObject.getString("route_name"));
                                manageOrderDTO.setPoint_id(routeObject.getString("point_id"));
                                manageOrderDTO.setPoint_name(routeObject.getString("point_name"));
                                manageOrderDTO.setSap_code(routeObject.getString("sap_code"));
                                manageOrderDTO.setVoice(routeObject.getString("voice"));
                                manageOrderDTO.setImage(routeObject.getString("image"));
                                manageOrderDTO.setText(routeObject.getString("text"));
                                manageOrderDTO.setOrder_status(routeObject.getString("order_status"));


                                smartOrderDTOS.add(manageOrderDTO);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        smart_order_adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){

        };

        queue = Volley.newRequestQueue(getActivity());

        queue.add(stringRequest);
    }

    public void getSmartOrderStatus(String smartOrderID) {

        RequestQueue queue = null;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.base_url)+"api/retailer-app/order-acknowledge?id="+smartOrderID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.e("<<>>",response+"");


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("id", smartOrderID);
//
//                //  Log.e("param",params+"");
//                return params;
//            }

        };

        queue = Volley.newRequestQueue(getActivity());

        queue.add(stringRequest);
    }

}
