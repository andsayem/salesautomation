package com.ssgbd.salesautomation.retailer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter_NewRetailer1;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter_Westage;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
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
import java.util.Collections;
import java.util.Comparator;


public class NewRetailerFragment1 extends Fragment implements View.OnClickListener{

    View rootView;
    private Dialog wdialog;
    RouteRecyclerAdapter routeRecyclerAdapter;
    TextView txt_route_list,txt_route_name;
    DatabaseHelper databaseHelper;
    RetailerRecyclerAdapter_NewRetailer1 retialer_adapter;
    RecyclerView retailer_list_recyclerView;

//  private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<RetailerDTO> retailerDTOS = new ArrayList<>();

    String routeId="";
    VolleyMethods vm = new VolleyMethods();
    public RequestQueue queue;
    String REASONID="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.new_retailer_fragment_1, container, false);


        databaseHelper = new DatabaseHelper(getActivity());
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

        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new RetailerRecyclerAdapter_NewRetailer1( retailerDTOS,getActivity(),this);
        retailer_list_recyclerView.setAdapter(retialer_adapter);
        initUi();

        return rootView;
    }

    private void initUi() {
        queue = Volley.newRequestQueue(getActivity());
        txt_route_name = (TextView) rootView.findViewById(R.id.txt_route_name);
        txt_route_list = (TextView) rootView.findViewById(R.id.txt_route_list);
        txt_route_list.setOnClickListener(this);

        try{

        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        }catch (SecurityException se){

        }
    }
    @Override
    public void onResume() {
        getRetailer(routeId);
        super.onResume();
    }

    @Override
    public void onPause() {
       // Log.e("<<>>", "OnPause of HomeFragment");
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }
    private void showRouteListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final TextView tv_dialog_title;

        final RelativeLayout rlDialogCross;

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
        tv_dialog_title = (TextView) wdialog.findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText("Please select a route");
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

    //  Log.e("routeDTOS>",Utility.routeDTOS.size()+"");
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        routeRecyclerAdapter = new RouteRecyclerAdapter( Utility.routeDTOS,getActivity());
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                retailerDTOS.clear();

                wdialog.dismiss();
                getRetailer(Utility.routeDTOS.get(position).getRoute_id());
                Utility.ROUTE_ID = Utility.routeDTOS.get(position).getRoute_id();

                txt_route_name.setText("Route Name-"+ Utility.routeDTOS.get(position).getRname());
                routeId = Utility.routeDTOS.get(position).getRoute_id();
                Utility.ROUTE_ID=Utility.routeDTOS.get(position).getRoute_id();
                Utility.ROUTE_NAME=Utility.routeDTOS.get(position).getRname();
                retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);

                Collections.sort(retailerDTOS, new Comparator<RetailerDTO>() {
                    @Override
                    public int compare(RetailerDTO l1, RetailerDTO l2) {

                       // Log.e(">>>>",l1.getRetailerSerial()+"--"+l2.getRetailerSerial()+"");

                        return Float.valueOf(l1.getRetailerSerial()).compareTo(Float.valueOf(l2.getRetailerSerial()));
                    }
                });

                retialer_adapter = new RetailerRecyclerAdapter_NewRetailer1( retailerDTOS,getActivity(), NewRetailerFragment1.this);
                retailer_list_recyclerView.setAdapter(retialer_adapter);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_route_list:
                showRouteListDialog();
                break;
        }
    }


    public void getRetailer(String routeId) {
        retailerDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.retailer_loding));
        pd.setCancelable(false);
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),
                getResources().getString(R.string.base_url) + "api/ma/get-master-retailers",
                jp.getRretailer(routeId, SharePreference.getUserPointId(getActivity())), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {

                     //  Log.e("<<>>",result+"<<>>");

                        if (result.equalsIgnoreCase("nettooslow")){
                            pd.dismiss();
                        }
                        try {
                            JSONObject jsonObject1 = new JSONObject(result);

                            if (jsonObject1.getString("status").equalsIgnoreCase("1")) {

                                try {
                                    JSONArray routeArray = jsonObject1.getJSONArray("retailer_list");
                                    for (int i = 0; i < routeArray.length(); i++) {
                                        JSONObject routeObject = routeArray.getJSONObject(i);
                                        RetailerDTO retailerDTO = new RetailerDTO();
                                        retailerDTO.setRetailer_id( routeObject.getString("retailer_id"));
                                        retailerDTO.setRetailer_name( routeObject.getString("name"));
                                        retailerDTO.setDivision( routeObject.getString("division"));
                                        retailerDTO.setTerritory( routeObject.getString("territory"));
                                      //retailerDTO.setAddress( routeObject.getString("address"));
                                        retailerDTO.setPoint_id( routeObject.getString("point_id"));
                                        retailerDTO.setRouteId( routeObject.getString("rid"));
                                        retailerDTO.setShopeType( optStringNullCheckZero(routeObject,"shop_type") );
                                      //retailerDTO.setRetailerOwner( routeObject.getString("owner"));
                                        retailerDTO.setRetailerOwner( optStringNullCheckZero(routeObject,"owner"));
                                        retailerDTO.setRetailerMobileNo(optStringNullCheckZero(routeObject,"mobile"));
                                        retailerDTO.setRetailerMobileNo2(optStringNullCheckZero(routeObject,"optional_mobile"));
                                        retailerDTO.setTnt( routeObject.getString("tnt"));
                                        retailerDTO.setEmail(optStringNullCheckZero(routeObject,"email"));
                                        retailerDTO.setDateandtime( routeObject.getString("dateandtime"));
                                        retailerDTO.setUser( routeObject.getString("user"));
//                                      retailerDTO.setStatus( routeObject.getString("status"));
                                        retailerDTO.setDob(optStringNullCheckZero(routeObject,"dob") );
                                        retailerDTO.setvAddress(optStringNullCheckZero(routeObject,"vAddress"));
                                        retailerDTO.setGlobal_company_id( routeObject.getString("global_company_id"));
                                        retailerDTO.setInactive_user( routeObject.getString("inactive_user"));
                                        retailerDTO.setInactive_date_time( routeObject.getString("inactive_date_time"));
                                        retailerDTO.setInactive_ip( routeObject.getString("inactive_ip"));
                                        retailerDTO.setiApproval( routeObject.getString("iApproval"));
                                        retailerDTO.setReminding_commission_balance( routeObject.getString("reminding_commission_balance"));
                                        retailerDTO.setOpening_balance( routeObject.getString("opening_balance"));
                                        retailerDTO.setOpening_balance_accessories( routeObject.getString("opening_balance_accessories"));
                                        retailerDTO.setSerial( optStringNullCheckZero(routeObject,"serial"));
                                       // Log.e("<<>>",optStringNullCheckZero(routeObject,"serial")+"");
                                        retailerDTO.setAfter_retailers( routeObject.getString("after_retailers"));
                                        retailerDTO.setLat( routeObject.getString("lat"));
                                        retailerDTO.setLon( routeObject.getString("lon"));
                                        retailerDTO.setSync( routeObject.getString("sync"));
                                        retailerDTO.setReminder_start( routeObject.getString("reminder_start"));
                                        retailerDTO.setReminder_end( routeObject.getString("reminder_end"));
                                        retailerDTO.setAddedOrUpdateType( routeObject.getString("addedOrUpdateType"));
                                        retailerDTO.setAddedOrUpdateDate( routeObject.getString("addedOrUpdateDate"));
                                        retailerDTO.setFb(optStringNullCheckZero(routeObject,"fb"));
                                        retailerDTO.setWhatsapp(optStringNullCheckZero(routeObject,"whatsapp"));

                                        retailerDTOS.add(retailerDTO);

                                    }

                                    retialer_adapter.notifyDataSetChanged();

                                } catch (JSONException je) {
                                    pd.dismiss();
                                }
                                pd.dismiss();

                            }else {
                                pd.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public String optStringNullCheckZero(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null")||json.isNull(key)||json.optString(key).equalsIgnoreCase(""))
            return "";
        else
            return json.optString(key, key);
    }
}
