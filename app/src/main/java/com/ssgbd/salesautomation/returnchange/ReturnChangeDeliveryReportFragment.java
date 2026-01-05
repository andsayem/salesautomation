package com.ssgbd.salesautomation.returnchange;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ReturnChangeDeliveryReportListAdapter;
import com.ssgbd.salesautomation.adapters.ReturnChangeReportListAdapter;
import com.ssgbd.salesautomation.dtos.OrderRepotListDTO;
import com.ssgbd.salesautomation.dtos.ReturnChangeReportListDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
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


public class ReturnChangeDeliveryReportFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    ReturnChangeDeliveryReportListAdapter retialer_adapter;
    RecyclerView retailer_list_recyclerView;
    TextView txt_fromdate,txt_todate;

//    private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<ReturnChangeReportListDTO> changeReportListDTOS = new ArrayList<>();
    DatePickerDialog picker;
    String fromDate="";
    String formattedDate="";
    String FROMDATE="",TODATE="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.return_change_report_fragment, container, false);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new ReturnChangeDeliveryReportListAdapter( changeReportListDTOS,getActivity());
        retailer_list_recyclerView.setAdapter(retialer_adapter);
        initUi();
        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int month=cldr.get(Calendar.MONTH);
        int year=cldr.get(Calendar.YEAR);

        String d=year+"-"+(month+1)+"-"+day;
        //setAddUtility(d,d);

        return rootView;
    }

    private void initUi() {
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

//                              txt_month_year.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }
                                fromDate = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    fromDate = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
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


                                String result="";
                                try {
                                    result = getActivity().getResources().getStringArray(R.array.month_names)[monthOfYear];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    result=Integer.toString(monthOfYear);
                                }
                                txt_todate.setText(dayOfMonth+" "+result+" "+year );


                                String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }
                                TODATE = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    TODATE = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }

                                if (!Utility.isInternetAvailable(getActivity())) {
                                    Utility.internetAlert(getActivity());

                                } else {
                                    getReturnChangeReport(fromDate, TODATE);

                                }

                            }
                        }, year, month, day);
                picker.show();
            }
        });

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




    public  void getReturnChangeReport(final String fd, final String td){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest sr = new StringRequest(Request.Method.POST,getString(R.string.base_url)+"api/apps-return-change-report-list", new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {


                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                          //     Log.e("jsonObject1",jsonObject1+"");
                            // Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray = jsonObject1.getJSONArray("order_list");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                ReturnChangeReportListDTO listDTO = new ReturnChangeReportListDTO();
                                listDTO.setReturnOrderId(optStringNullCheck(object,"return_order_id"));
                                listDTO.setReturnOrder(optStringNullCheck(object,"return_order_no"));
                                listDTO.setReturnOrderDate(optStringNullCheck(object,"return_order_date"));
                                listDTO.setQty(optStringNullCheck(object,"total_return_qty"));
                                listDTO.setValue(optStringNullCheck(object,"total_return_value"));
                                listDTO.setChangeqty(optStringNullCheck(object,"total_change_qty"));
                                listDTO.setChangevalue(optStringNullCheck(object,"total_change_value"));
                                listDTO.setFo(optStringNullCheck(object,"first_name"));
                                listDTO.setCustomer(optStringNullCheck(object,"name")+"("+optStringNullCheck(object,"retailer_id")+")"+optStringNullCheck(object,"mobile"));

                                changeReportListDTOS.add(listDTO);
                            }
                            retialer_adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("fromdate",fd);
                params.put("todate",td);

                params.put("user_id",SharePreference.getUserId(getActivity()));
              //  Log.e("params>>",params+"");
                return params;
            }

        };
        queue.add(sr);
    }

}
