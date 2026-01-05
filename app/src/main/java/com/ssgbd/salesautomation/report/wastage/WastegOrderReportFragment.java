package com.ssgbd.salesautomation.report.wastage;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.OrderReportListAdapter;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.WastageOrderReportListAdapter;
import com.ssgbd.salesautomation.dtos.OrderRepotListDTO;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;


public class WastegOrderReportFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    WastageOrderReportListAdapter orderReportListAdapter;
    RecyclerView orde_list_recyclerView;
    String ROUTE_ID="";
//    private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<OrderRepotListDTO> orderRepotListDTOS = new ArrayList<>();
    TextView txt_fromdate,txt_todate,txt_route,txt_search;
    String formattedDate="";
    DatePickerDialog picker;
    String FROMDATE="",TODATE="";

    private Dialog wdialog;
    RouteRecyclerAdapter routeRecyclerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.wasteg_order_report_fragment, container, false);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);


        orde_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.orde_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        orde_list_recyclerView.setLayoutManager(linearLayoutManager1);
        orderReportListAdapter = new WastageOrderReportListAdapter( orderRepotListDTOS,getActivity());
        orde_list_recyclerView.setAdapter(orderReportListAdapter);


        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int month=cldr.get(Calendar.MONTH);
        int year=cldr.get(Calendar.YEAR);

        String d=year+"-"+(month+1)+"-"+day;

        txt_fromdate = (TextView) rootView.findViewById(R.id.txt_fromdate);
        txt_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr=Calendar.getInstance();
                int day=cldr.get(Calendar.DAY_OF_MONTH);
                int month=cldr.get(Calendar.MONTH);
                int year=cldr.get(Calendar.YEAR);


                // date picker dialog
                picker=new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                                String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }
                                FROMDATE = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    FROMDATE = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }

                                String result="";
                                try {
                                    result = getActivity().getResources().getStringArray(R.array.month_names)[monthOfYear];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    result=Integer.toString(monthOfYear);
                                }
                                txt_fromdate.setText(dayOfMonth+" "+result+" "+year );
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        txt_todate = (TextView) rootView.findViewById(R.id.txt_todate);
        txt_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar cldr=Calendar.getInstance();
                int day=cldr.get(Calendar.DAY_OF_MONTH);
                int month=cldr.get(Calendar.MONTH);
                int year=cldr.get(Calendar.YEAR);


                // date picker dialog
                picker=new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                                String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }
                                TODATE = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    TODATE = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }

                                String result="";
                                try {
                                    result = getActivity().getResources().getStringArray(R.array.month_names)[monthOfYear];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    result=Integer.toString(monthOfYear);
                                }
                                txt_todate.setText(dayOfMonth+" "+result+" "+year );

                            }
                        }, year, month, day);
                picker.show();
            }
        });
        txt_route = (TextView) rootView.findViewById(R.id.txt_route);
        txt_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRouteListDialog();
            }
        });

        txt_search = (TextView) rootView.findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Utility.isInternetAvailable(getActivity())) {
                    Utility.internetAlert(getActivity());

                } else {
                    getWastageOrder();

                }


            }
        });

        return rootView;
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

        RouteDTO allDTO = new RouteDTO();
        allDTO.setPoint_id("");
        allDTO.setTerritory_id("");
        allDTO.setRname("All");
        allDTO.setRoute_id("");
        Utility.routeDTOS.add(allDTO);

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

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        routeRecyclerAdapter = new RouteRecyclerAdapter( Utility.routeDTOS,getActivity());
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                wdialog.dismiss();
                ROUTE_ID = Utility.routeDTOS.get(position).getRoute_id();
                txt_route.setText(Utility.routeDTOS.get(position).getRname());
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
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }


    public  String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null"))
            return "";
        else
            return json.optString(key, key);
    }


    public void getReport(){
        orderRepotListDTOS.clear();
        orderReportListAdapter.notifyDataSetChanged();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),  getString(R.string.base_url)+"api/report/order-list",
                jp.jsonOrderReport(  SharePreference.getUserId(getActivity()),SharePreference.getUserGlobalId(getActivity()),FROMDATE,TODATE,ROUTE_ID), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();


                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                                    if (jsonObject1.getString("status").equalsIgnoreCase("0")){
                                        Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                                    }
                                    JSONArray jsonArray = jsonObject1.getJSONArray("order_list");
                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        OrderRepotListDTO listDTO = new OrderRepotListDTO();
                                        listDTO.setOrderId(optStringNullCheck(object,"order_id"));
                                        listDTO.setOrderNO(optStringNullCheck(object,"order_no"));
                                        listDTO.setOrderDateTime(optStringNullCheck(object,"order_date"));
                                        listDTO.setCustomerName(optStringNullCheck(object,"name"));
                                        listDTO.setOrderQty(optStringNullCheck(object,"total_qty"));
                                        listDTO.setOrderValue(optStringNullCheck(object,"total_value"));

                                        orderRepotListDTOS.add(listDTO);
                                    }
                                    orderReportListAdapter.notifyDataSetChanged();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }


    public  void getWastageOrder(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest sr = new StringRequest(Request.Method.POST,getString(R.string.base_url)+"api/apps-wastage-order-list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("0")){
                        Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("wastage_list");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        OrderRepotListDTO listDTO = new OrderRepotListDTO();
                        listDTO.setOrderId(optStringNullCheck(object,"order_id"));
                        listDTO.setOrderNO(optStringNullCheck(object,"order_no"));
                        listDTO.setOrderDateTime(optStringNullCheck(object,"order_date"));
                        listDTO.setCustomerName(optStringNullCheck(object,"name"));
                        listDTO.setOrderQty(optStringNullCheck(object,"total_qty"));
                        listDTO.setOrderValue(optStringNullCheck(object,"total_value"));

                        orderRepotListDTOS.add(listDTO);
                    }
                    orderReportListAdapter.notifyDataSetChanged();


                }catch (JSONException je){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("fromdate",FROMDATE);
                params.put("todate",TODATE);
                params.put("route", ROUTE_ID);
                params.put("user_id",SharePreference.getUserId(getActivity()));

            //    Log.e("params>>",params+"");
                return params;
            }

        };
        queue.add(sr);
    }

}
