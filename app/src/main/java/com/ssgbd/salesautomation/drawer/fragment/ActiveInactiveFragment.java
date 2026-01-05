package com.ssgbd.salesautomation.drawer.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter_ActiveINact;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.bucket.BucketAmountWeb;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
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
import java.util.ArrayList;
import java.util.List;

public class ActiveInactiveFragment extends Fragment implements View.OnClickListener {

    View rootView;
    Spinner spinnerOfferType;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    DatabaseHelper databaseHelper;
    TextView txt_route_list,txt_route_name,txt_retailer_list,txt_retailer_name;
    RouteRecyclerAdapter routeRecyclerAdapter;
    RetailerRecyclerAdapter_ActiveINact retailerRecyclerAdapter;
    ArrayList<RetailerDTO> retailerDTOS = new ArrayList<>();
    Button button_send_request;
    String routeId="";
    String retailerId="";
    String statusId="";
    private Dialog wdialog;
    private Dialog retailerDialog;
    VolleyMethods vm = new VolleyMethods();
    JSONObject finalobject;
    JSONObject retailerobject;
    public RequestQueue queue;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      //  Log.e("log2", "ac");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.active_inactive_retailer_fragment, container, false);

         // get db connection
        queue = Volley.newRequestQueue(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myPath = Utility.DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()){
            databaseHelper.openDataBase();}
         initUI();


            CategoryDTO c = new CategoryDTO();
            c.setId("2");
            c.setName("--Select Status--");
            categoryDTOS.add(c);

            CategoryDTO c1 = new CategoryDTO();
            c1.setId("1");
            c1.setName("Activation Request");
            categoryDTOS.add(c1);

            CategoryDTO c2 = new CategoryDTO();
            c2.setId("0");
            c2.setName("Inactive Request");
            categoryDTOS.add(c2);


        spinnerOfferType = (Spinner) rootView.findViewById(R.id.spinnerOfferType);
        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
                R.layout.customspinneritem, categoryDTOS);

        spinnerOfferType.setAdapter(adapter);

        spinnerOfferType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

              //  Log.e(">>>><<>>",categoryDTOS.get(pos).getId()+"<<>>"+categoryDTOS.get(pos).getId()+"");
                statusId = categoryDTOS.get(pos).getId();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        httpRequest();
        return rootView;
    }

    private void initUI() {


        txt_route_list = (TextView) rootView.findViewById(R.id.txt_route_list);
        txt_route_list.setOnClickListener(this);
        txt_route_name = (TextView) rootView.findViewById(R.id.txt_route_name);

        txt_retailer_list = (TextView) rootView.findViewById(R.id.txt_retailer_list);
        txt_retailer_list.setOnClickListener(this);
        txt_retailer_name = (TextView) rootView.findViewById(R.id.txt_retailer_name);

        button_send_request = (Button) rootView.findViewById(R.id.button_send_request);
        button_send_request.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_route_list:
                showRouteListDialog();
            break;
            case R.id.txt_retailer_list:
                if (routeId.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please select a route.", Toast.LENGTH_SHORT).show();
                }else {
                    showRetailerListDialog();
                }
            break;

            case R.id.button_send_request:

                if (routeId.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please select a route.", Toast.LENGTH_SHORT).show();

                }else if(retailerId.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please select a retailer.", Toast.LENGTH_SHORT).show();
                }else if (statusId.equalsIgnoreCase("")||statusId.equalsIgnoreCase("2")){
                    Toast.makeText(getActivity(), "Please select a status.", Toast.LENGTH_SHORT).show();

                }else {
                    try {
                    finalobject = new JSONObject();
                    retailerobject = new JSONObject();
                        retailerobject.put("routeid",routeId);
                        retailerobject.put("retailerid",retailerId);
                        retailerobject.put("status",statusId);
//                        Log.e("statusId>>",statusId+"");
                        retailerobject.put("userid", SharePreference.getUserId(getActivity()));
                        retailerobject.put("global_company_id",SharePreference.getUserGlobalId(getActivity()));
                        finalobject.put("retailer_info",retailerobject);
                    //    Log.e("finalobject>>",finalobject+"");

                        sendRequest();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                break;
        }
    }

    public class CustomArrayAdapter extends ArrayAdapter<String>{

        private final LayoutInflater mInflater;
        private final Context mContext;
        private final ArrayList<CategoryDTO> items;
        private final int mResource;

        public CustomArrayAdapter(@NonNull Context context, @LayoutRes int resource,
                                  @NonNull ArrayList objects) {
            super(context, resource, 0, objects);

            mContext = context;
            mInflater = LayoutInflater.from(context);
            mResource = resource;
            items = objects;
        }
        @Override
        public View getDropDownView(int position, @Nullable View convertView,
                                    @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        @Override
        public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        private View createItemView(int position, View convertView, ViewGroup parent){
            final View view = mInflater.inflate(mResource, parent, false);

            TextView offTypeTv = (TextView) view.findViewById(R.id.offer_type_txt);

            CategoryDTO offerData = items.get(position);

            offTypeTv.setText(items.get(position).getName());

            return view;
        }
    }



    private void showRouteListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;

        final RelativeLayout rlDialogCross;
      //  Log.e("routeDTOS>", Utility.routeDTOS.size()+"");

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


//                retailerDTOS = databaseHelper.getRetailerList(databaseHelper,Utility.routeDTOS.get(position).getRoute_id());
                txt_route_name.setText("Route Name-"+ Utility.routeDTOS.get(position).getRname());
                routeId = Utility.routeDTOS.get(position).getRoute_id();


                wdialog.dismiss();
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
    private void showRetailerListDialog() {
        retailerDialog =new Dialog(getActivity());
        retailerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        retailerDialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        retailerDTOS.clear();
        retailerDTOS = databaseHelper.getRetailerList(databaseHelper,routeId);

        final RelativeLayout rlDialogCross;
        etSearch = (EditText)retailerDialog.findViewById(R.id.edt_txt_search);
        route_list_recyclerView=(RecyclerView) retailerDialog.findViewById(R.id.route_list_recyclerView);
        imbtnCross=(ImageView)retailerDialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)retailerDialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)retailerDialog.findViewById(R.id.btnDoneDialog);

        retailerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retailerDialog.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retailerDialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retailerDialog.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        retailerRecyclerAdapter = new RetailerRecyclerAdapter_ActiveINact(retailerDTOS,getActivity(),ActiveInactiveFragment.this);
        route_list_recyclerView.setAdapter(retailerRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                txt_retailer_name.setText(retailerDTOS.get(position).getRetailer_name());
                retailerDialog.dismiss();
                retailerId = retailerDTOS.get(position).getRetailer_id();
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
                retailerRecyclerAdapter.filter(query);
            }
        });

        retailerDialog.show();
    }
    public void sendRequest(){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url)+"api/apps/retailer-active-inactive-submit", finalobject.toString(), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                pd.dismiss();
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
//
                  //      Log.e("response>> inactiv>",jsonObject1.getString("message")+"");

                    String s = jsonObject1.getString("message");
                    logOutAlert(getActivity(),s);
                        routeId="";
                        retailerId="";
                        statusId="";

                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void logOutAlert(final Context context,String msg) {
        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.drawable.search_hover)).setTitle(msg);

        alertBulder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {;

            }
        });
        alertBulder.show();
    }
    public void httpRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.base_url)+"api/retailer?fo="+SharePreference.getUserId(getActivity()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               //  Log.e("responsleLOg>>", response.toString() + "<<-->>");


                try {
                    JSONObject respjsonObj = new JSONObject(response);


                } catch (JSONException e) {

                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
                Toast.makeText(getActivity(), "SSL server error.", Toast.LENGTH_SHORT).show();

                //  Log.e("<<>>>>",error.toString()+"<<<<");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
    }

}
