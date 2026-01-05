package com.ssgbd.salesautomation.drawer.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ssgbd.salesautomation.IMSFOActivity;
import com.ssgbd.salesautomation.LoginActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.SCDDashboardActivity;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.StockListAdapter;
import com.ssgbd.salesautomation.adapters.TechnicianListAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.StockListDTO;
import com.ssgbd.salesautomation.dtos.TechnicianListDTO;
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
import java.util.ArrayList;

public class TechnicianListFragment extends Fragment implements View.OnClickListener{

    View rootView;
    // product category list
    Dialog wdialog;
    ProductCategoryRecyclerAdapter productCategoryAdapter;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<TechnicianListDTO> stockDTOS = new ArrayList<>();

    // get db data
    DatabaseHelper databaseHelper;
   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";

    // product
    RecyclerView product_list_recyclerView;
    TechnicianListAdapter stockListAdapter;
    ArrayList<ProductDTO> productDtos = new ArrayList<>();
    public RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.technician_list_fragment, container, false);

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
        queue = Volley.newRequestQueue(getActivity());


        product_list_recyclerView=(RecyclerView) rootView.findViewById(R.id.product_list_recyclerView);
        showProductListDialog();
        String url= "https://qrc.ssgbd.com/api/get_technician?type=fo&fo_code="+SharePreference.getEmployeeId(getActivity());
        getTechnician(url);
        return rootView;
    }


    private void showProductListDialog() {

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        product_list_recyclerView.setLayoutManager(linearLayoutManager);
        stockListAdapter = new TechnicianListAdapter( stockDTOS,getActivity(),TechnicianListFragment.this);
        product_list_recyclerView.setAdapter(stockListAdapter);

//        product_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {

//            }
//        }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.txt_category_list:
//                break;
        }
    }

    public void getTechnician(String url) {
stockDTOS.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Log.e("responsleLOg>>", response.toString() + "<<-->>");

                //   Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                try {
                    JSONObject respjsonObj = new JSONObject(response);


                        JSONArray statusArray = respjsonObj.getJSONArray("data");

                        for (int i = 0; i < statusArray.length(); i++) {

                            JSONObject routeObject = statusArray.getJSONObject(i);
                            TechnicianListDTO technicianListDTO = new TechnicianListDTO();
                            technicianListDTO.setId(routeObject.getString("id"));
                            technicianListDTO.setTechnicianName(routeObject.getString("name"));
                            technicianListDTO.setTechnicianPhone(routeObject.getString("email"));
                            technicianListDTO.setFo_verify(routeObject.getString("fo_verify"));
                            technicianListDTO.setTsm_verify(routeObject.getString("tsm_verify"));
                            technicianListDTO.setPoint_verify(routeObject.getString("point_verify"));

                            stockDTOS.add(technicianListDTO);
                        }

                    stockListAdapter.notifyDataSetChanged();


                } catch (JSONException e) {

                    //   Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();



                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
                //  Toast.makeText(context, "SSL server error.", Toast.LENGTH_SHORT).show();

                Log.e("<<>>>>",error.toString()+"<<<<");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
    }

    public void getStockList(final String id){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        VolleyMethods vm = new VolleyMethods();


        vm.sendRequestToServer2(getActivity(), "https://qrc.ssgbd.com/api/technician_status_update/"+id, jp.technicianUpdate("fo","1"), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                pd.dismiss();


                try {

                    String url= "https://qrc.ssgbd.com/api/get_technician?type=fo&fo_code="+SharePreference.getEmployeeId(getActivity());
                    getTechnician(url);
                    JSONObject respjsonObj = new JSONObject(result);

                    Toast.makeText(getActivity(), respjsonObj.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException je) {
                }
                // Log.e("size",stockDTOS.size()+"statusArray");
                stockListAdapter.notifyDataSetChanged();
            }

        });
    }
}
