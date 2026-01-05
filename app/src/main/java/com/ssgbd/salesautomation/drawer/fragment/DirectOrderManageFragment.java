package com.ssgbd.salesautomation.drawer.fragment;

import android.content.Intent;
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
import com.ssgbd.salesautomation.adapters.DirectOrderListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.SmartManageOrderListRecyclerAdapter;
import com.ssgbd.salesautomation.bucket.BucketAmountWeb;
import com.ssgbd.salesautomation.dtos.DirectOrderDTO;
import com.ssgbd.salesautomation.dtos.SmartOrderDTO;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DirectOrderManageFragment extends Fragment {

    View rootView;
    RecyclerView direct_order_list_recyclerView;
    DirectOrderListRecyclerAdapter direct_order_adapter;
    LinearLayoutManager linearLayoutManager1;
    ArrayList<DirectOrderDTO> directOrderDTOS = new ArrayList<>();
    boolean isPageError = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.direct_order_manage_fragment, container, false);

        initUi(rootView);
        return rootView;
    }

    private void initUi(View rootView) {
        direct_order_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.direct_order_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        direct_order_list_recyclerView.setLayoutManager(linearLayoutManager1);
        direct_order_adapter = new DirectOrderListRecyclerAdapter( directOrderDTOS,DirectOrderManageFragment.this,getActivity());
        direct_order_list_recyclerView.setAdapter(direct_order_adapter);

//        direct_order_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.base_url)+"api/retailer-app/fo-order-list?fo_id="+ SharePreference.getUserId(getActivity()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray routeArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < routeArray.length(); i++) {
                                JSONObject routeObject = routeArray.getJSONObject(i);
                                DirectOrderDTO manageOrderDTO = new DirectOrderDTO();

                                manageOrderDTO.setOrder_id(routeObject.getString("order_id"));
                                manageOrderDTO.setOrder_no(routeObject.getString("order_no"));
                                manageOrderDTO.setOrder_date(routeObject.getString("order_date"));
                                manageOrderDTO.setOrder_type(routeObject.getString("order_type"));
                                manageOrderDTO.setDistributor_id(routeObject.getString("distributor_id"));
                                manageOrderDTO.setBusiness_type_id(routeObject.getString("business_type_id"));
                                manageOrderDTO.setDivision_id(routeObject.getString("division_id"));
                                manageOrderDTO.setTerritory_id(routeObject.getString("territory_id"));
                                manageOrderDTO.setPoint_id(routeObject.getString("point_id"));
                                manageOrderDTO.setRoute_id(routeObject.getString("route_id"));
                                manageOrderDTO.setRetailer_id(routeObject.getString("retailer_id"));
                                manageOrderDTO.setFo_id(routeObject.getString("fo_id"));
                                manageOrderDTO.setTotal_qty(routeObject.getString("total_qty"));
                                manageOrderDTO.setTotal_value(routeObject.getString("total_value"));
                                manageOrderDTO.setUpdate_date(routeObject.getString("update_date"));
                                manageOrderDTO.setOrder_status(routeObject.getString("order_status"));
                                manageOrderDTO.setRname(routeObject.getString("rname"));
                                manageOrderDTO.setName(routeObject.getString("name"));
                                manageOrderDTO.setMobile(routeObject.getString("mobile"));

                                directOrderDTOS.add(manageOrderDTO);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        direct_order_adapter.notifyDataSetChanged();
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
//                params.put("fo_id", "4911");
//
//
//              //  Log.e("param",params+"");
//                return params;
//            }

        };

        queue = Volley.newRequestQueue(getActivity());

        queue.add(stringRequest);
    }

    public void getOrderStatus(String orderID,String routeId,String retailerId,String pointId,String retailerName) {

        RequestQueue queue = null;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.base_url)+"api/retailer-app/order-type-update",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                         if( jsonObject.getString("success").equalsIgnoreCase("true")){

                             Utility.ROUTE_ID = routeId;
                             Intent intent = new Intent(getActivity(), BucketAmountWeb.class);
                             intent.putExtra("routeId", routeId);
                             intent.putExtra("retailerId",retailerId);
                             intent.putExtra("retailerName",retailerName);
                             intent.putExtra("poient_id",pointId);
                             startActivity(intent);
                             Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        direct_order_adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("order_id", orderID);

              //  Log.e("param",params+"");
                return params;
            }

        };

        queue = Volley.newRequestQueue(getActivity());

        queue.add(stringRequest);
    }

}
