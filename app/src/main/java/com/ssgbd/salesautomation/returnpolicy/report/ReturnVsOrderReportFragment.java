package com.ssgbd.salesautomation.returnpolicy.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.OrderVsDeliveryReportListAdapter;
import com.ssgbd.salesautomation.adapters.ReturnVsOrderReportListAdapter;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;


public class ReturnVsOrderReportFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    ReturnVsOrderReportListAdapter retialer_adapter;
    RecyclerView retailer_list_recyclerView;
    LinearLayoutManager linearLayoutManager1;
    ArrayList<OrderRepotListDTO> changeReportListDTOS = new ArrayList<>();
    TextView txt_fromdate,txt_todate,txt_route,txt_search,total_qty_free,total_value_discount;
    String formattedDate="";
    DatePickerDialog picker;
    String fromDate="";
    private Dialog wdialog;
    RouteRecyclerAdapter routeRecyclerAdapter;
    String FROMDATE="",TODATE="";     String ROUTE_ID="";
    TextView txt_total_count;
    TextView total_qty,total_value,total_qty_d,total_value_d;
    SimpleDateFormat presentYear;

    float totalqty,totalvalue,totalqty_d,totalvalue_d,free,discount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.order_vs_delivery_report_fragment, container, false);
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new ReturnVsOrderReportListAdapter( changeReportListDTOS,getActivity(), ReturnVsOrderReportFragment.this);
        retailer_list_recyclerView.setAdapter(retialer_adapter);

        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int month=cldr.get(Calendar.MONTH);
        int year=cldr.get(Calendar.YEAR);

      //  Log.e("dmy>",year+"-"+(month+1)+"-"+day+"");
        presentYear = new SimpleDateFormat("yyyy");

        String d=year+"-"+(month+1)+"-"+day;

      //  Log.e(",<>>",year+"-"+(month+1)+"-1"+"---"+d+"");
        getReport(year+"-"+(month+1)+"-1",d);
        TODATE = d;


        txt_total_count = (TextView) rootView.findViewById(R.id.txt_total_count);
        total_qty = (TextView) rootView.findViewById(R.id.total_qty);
        total_value = (TextView) rootView.findViewById(R.id.total_value);
        total_qty_d = (TextView) rootView.findViewById(R.id.total_qty_d);
        total_value_d = (TextView) rootView.findViewById(R.id.total_value_d);


        txt_fromdate = (TextView) rootView.findViewById(R.id.txt_fromdate);
        String monthname=(String)android.text.format.DateFormat.format("MMMM", new Date());
        txt_fromdate.setText("1"+" "+monthname+" "+String.valueOf(year) );
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

//                              txt_month_year.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                fromDate = year + "-" + (monthOfYear + 1) + "-" +  dayOfMonth;
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

        txt_todate.setText(day+" "+monthname+" "+String.valueOf(year));
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

        total_qty_free = (TextView) rootView.findViewById(R.id.total_qty_free);
        total_value_discount = (TextView) rootView.findViewById(R.id.total_value_discount);
        txt_search = (TextView) rootView.findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getReport(FROMDATE, TODATE);
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
            return "0";
        else
            return json.optString(key, key);
    }


    public void getReport(String fromDAte,String TODate){
        changeReportListDTOS.clear();
        retialer_adapter.notifyDataSetChanged();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),  getString(R.string.base_url)+"api/report/app-return-order-delivery",
                jp.jsonReportODeliveryVsOrder( SharePreference.getUserId(getActivity()),fromDAte,TODate,ROUTE_ID), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();

                        total_qty.setText("");
                        total_value.setText("");
                        totalqty=0;
                        totalvalue=0;
                        total_qty_d.setText("");
                        total_value_d.setText("");
                        totalqty_d=0;
                        totalvalue_d=0;
                        free = 0;
                        discount = 0;
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                                  //  Log.e("jsonObject1",jsonObject1+"");
                                   // Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();

                                    if (jsonObject1.getString("status").equalsIgnoreCase("1")){

                                    JSONArray jsonArray = jsonObject1.getJSONArray("order_vs_delivery");
                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        OrderRepotListDTO listDTO = new OrderRepotListDTO();
                                        listDTO.setOrderId(optStringNullCheck(object,"order_id"));
                                        listDTO.setOrderNO(optStringNullCheck(object,"order"));
                                        listDTO.setCustomerName(optStringNullCheck(object,"customer"));
                                        listDTO.setOrderDateTime(optStringNullCheck(object,"date"));
                                        listDTO.setOrderQty(optStringNullCheck(object,"qty"));
                                        listDTO.setOrderValue(optStringNullCheck(object,"value"));
                                        listDTO.setDeliveryQty(optStringNullCheck(object,"delivery_qty"));
                                        listDTO.setDeliveryValue(optStringNullCheck(object,"delivery_value"));
                                        listDTO.setFree(optStringNullCheck(object,"free_value"));
                                        listDTO.setDiscount(optStringNullCheck(object,"discount"));

                                        changeReportListDTOS.add(listDTO);

                                        totalqty += Float.parseFloat(optStringNullCheck(object,"qty"));
                                        totalvalue += Float.parseFloat(optStringNullCheck(object,"value"));

                                        totalqty_d += Float.parseFloat(optStringNullCheck(object,"delivery_qty"));
                                        totalvalue_d += Float.parseFloat(optStringNullCheck(object,"delivery_value"));

                                        free +=Float.parseFloat(optStringNullCheck(object,"free_value"));
                                        discount +=Float.parseFloat(optStringNullCheck(object,"discount"));

                                    }
                                        DecimalFormat twoDForm = new DecimalFormat("#");

                                        total_qty.setText(String.valueOf(Math.round(  Float.valueOf(twoDForm.format(totalqty)))));
                                        total_value.setText(String.valueOf(  Float.valueOf(twoDForm.format(totalvalue))));
                                        total_qty_d.setText(String.valueOf(Math.round( Float.valueOf(twoDForm.format(totalqty_d)))));
                                        total_value_d.setText(String.valueOf( Float.valueOf(twoDForm.format(totalvalue_d))));
                                        total_qty_free.setText(String.valueOf( Float.valueOf(twoDForm.format(free))));
                                        total_value_discount.setText(String.valueOf( Float.valueOf(twoDForm.format(discount))));

                                        txt_total_count.setText(" About"+String.valueOf(changeReportListDTOS.size())+" results");

                                        retialer_adapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }


    public  boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }


    public void internetAlert(final Context context) {
        final AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("Internet Alert").setMessage("Your device is not connected to internet. Connect to internet and try again.");

        alertBulder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        alertBulder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertBulder.setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertBulder.show();
    }
}
