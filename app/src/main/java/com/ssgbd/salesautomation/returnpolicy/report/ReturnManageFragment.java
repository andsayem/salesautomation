package com.ssgbd.salesautomation.returnpolicy.report;

import android.content.Intent;
import android.os.Bundle;
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
import com.ssgbd.salesautomation.adapters.ReturnOrderListRecyclerAdapter;
import com.ssgbd.salesautomation.bucket.ManageOrderActivityWeb;
import com.ssgbd.salesautomation.dtos.ManageOrderDTO;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReturnManageFragment extends Fragment {

    View rootView;
    RecyclerView order_list_recyclerView;
    ReturnOrderListRecyclerAdapter manage_order_adapter;
    LinearLayoutManager linearLayoutManager1;
    ArrayList<ManageOrderDTO> orderListDTOS = new ArrayList<>();
    boolean isPageError = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.return_manage_fragment, container, false);


        initUi(rootView);
        return rootView;
    }

    private void initUi(View rootView) {
        order_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.order_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        order_list_recyclerView.setLayoutManager(linearLayoutManager1);
        manage_order_adapter = new ReturnOrderListRecyclerAdapter( orderListDTOS,getActivity());
        order_list_recyclerView.setAdapter(manage_order_adapter);

        order_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Toast.makeText(getActivity(), "Processing...", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(),ManageReturnActivityWeb.class);
                intent.putExtra("orderid",orderListDTOS.get(position).getOrder_id());
                intent.putExtra("retailerId",orderListDTOS.get(position).getRetailer_id());
                intent.putExtra("routeId",orderListDTOS.get(position).getRoute_id());
                startActivity(intent);

            }
        }));

        getOrderList();
    }

    private void getOrderList() {

        RequestQueue queue = null;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.base_url)+"api/apps/return-order-manage-list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                   //     Log.e("response",response+"");

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray routeArray = jsonObject.getJSONArray("order_manage_list");
                            for (int i = 0; i < routeArray.length(); i++) {
                                JSONObject routeObject = routeArray.getJSONObject(i);
                                ManageOrderDTO manageOrderDTO = new ManageOrderDTO();
                                manageOrderDTO.setRetailer_id(routeObject.getString("retailer_id"));
                                manageOrderDTO.setRoute_id(routeObject.getString("route_id"));
                                manageOrderDTO.setOrder_id(routeObject.getString("order_id"));
                                manageOrderDTO.setOrder_no(routeObject.getString("order_no"));
                                manageOrderDTO.setOrder_date(routeObject.getString("order_date"));
                                manageOrderDTO.setTotal_qty(routeObject.getString("total_qty"));
                                manageOrderDTO.setGrand_total_value(routeObject.getString("grand_total_value"));
                                manageOrderDTO.setUser_id(routeObject.getString("user_id"));
                                manageOrderDTO.setFirst_name(routeObject.getString("first_name"));
                                manageOrderDTO.setMiddle_name(routeObject.getString("middle_name"));
                                manageOrderDTO.setLast_name(routeObject.getString("last_name"));
                                manageOrderDTO.setName(routeObject.getString("name"));
                                manageOrderDTO.setMobile(routeObject.getString("mobile"));
                                orderListDTOS.add(manageOrderDTO);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        manage_order_adapter.notifyDataSetChanged();
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
                params.put("appsuser_id", SharePreference.getUserId(getActivity()));
                params.put("appsglobal_id",SharePreference.getUserGlobalId(getActivity()));

              //  Log.e("param",params+"");
                return params;
            }

        };

        queue = Volley.newRequestQueue(getActivity());

        queue.add(stringRequest);
    }
}
