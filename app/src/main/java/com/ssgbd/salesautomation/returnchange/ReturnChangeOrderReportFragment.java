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

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ReturnChangeReportListAdapter;
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

//import static com.google.android.gms.internal.zzahg.runOnUiThread;


public class ReturnChangeOrderReportFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    ReturnChangeReportListAdapter retialer_adapter;
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
    TextView row_return_qty,row_return_value,row_change_qty,row_change_value,row_excess_amount;
    float total_ret_qty,total_return_value,total_change_qty,total_change_value,total_excess_amount;


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
        retialer_adapter = new ReturnChangeReportListAdapter( changeReportListDTOS,getActivity(),ReturnChangeOrderReportFragment.this);
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
        row_return_qty = (TextView) rootView.findViewById(R.id.row_return_qty);
        row_return_value = (TextView) rootView.findViewById(R.id.row_return_value);
        row_change_qty = (TextView) rootView.findViewById(R.id.row_change_qty);
        row_change_value = (TextView) rootView.findViewById(R.id.row_change_value);
        row_excess_amount = (TextView) rootView.findViewById(R.id.row_excess_amount);

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

                                String result="";  String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }
                                fromDate = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    fromDate = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }
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
                                String todate = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    todate = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }

                                if (!Utility.isInternetAvailable(getActivity())) {
                                    Utility.internetAlert(getActivity());

                                } else {
                                    getReturnOrder(fromDate, todate);

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


    public void getReturnOrder(String fromdate,String todate){
        changeReportListDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),  getResources().getString(R.string.base_url)+"api/return-change-list",
                jp.jsonRerurnList( SharePreference.getUserId(getActivity()),SharePreference.getUserGlobalId(getActivity()),fromdate,todate), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();
                   //     Log.e("result>>",result+"");

                        row_return_qty.setText("");
                        row_return_value.setText("");
                        row_change_qty.setText("");
                        row_change_value.setText("");
                        row_excess_amount.setText("");
                        total_ret_qty=0;
                        total_return_value=0;
                        total_change_qty=0;
                        total_change_value=0;
                        total_excess_amount=0;


                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                          //          Log.e("jsonObject1",jsonObject1+"");
                                   // Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();

                                    JSONArray jsonArray = jsonObject1.getJSONArray("return_change_list");
                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        ReturnChangeReportListDTO listDTO = new ReturnChangeReportListDTO();
                                        listDTO.setReturnOrderId(optStringNullCheck(object,"return_order_id"));
                                        listDTO.setReturnOrder(optStringNullCheck(object,"return_order_no"));
                                        listDTO.setReturnOrderDate(optStringNullCheck(object,"return_order_date"));
                                        listDTO.setQty(optStringNullCheck(object,"total_return_qty"));
                                        listDTO.setValue(optStringNullCheck(object,"total_return_value"));
                                        listDTO.setFo(optStringNullCheck(object,"first_name"));
                                        listDTO.setCustomer(optStringNullCheck(object,"name")+"("+optStringNullCheck(object,"retailer_id")+")"+optStringNullCheck(object,"mobile"));
                                        listDTO.setChangeqty(optStringNullCheck(object,"total_change_qty"));
                                        listDTO.setChangevalue(optStringNullCheck(object,"total_change_value"));


                                        changeReportListDTOS.add(listDTO);
                                        total_ret_qty += Float.parseFloat(optStringNullCheck(object,"total_return_qty"));
                                        total_return_value += Float.parseFloat(optStringNullCheck(object,"total_return_value"));
                                        total_change_qty += Float.parseFloat(optStringNullCheck(object,"total_change_qty"));
                                        total_change_value += Float.parseFloat(optStringNullCheck(object,"total_change_value"));

                                    }
                                    row_return_qty.setText("");
                                    row_return_value.setText("");
                                    row_change_qty.setText("");
                                    row_change_value.setText("");
                                    row_return_qty.setText(String.valueOf(Math.round(total_ret_qty)));
                                    row_return_value.setText(String.valueOf(total_return_value));
                                    row_change_qty.setText(String.valueOf(total_change_qty));
                                    row_change_value.setText(String.valueOf(total_change_value));



                                    retialer_adapter.notifyDataSetChanged();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                    }

                });
    }

    public void excessValue(String value){
        total_excess_amount += Float.parseFloat(value);
        row_excess_amount.setText(String.valueOf(total_excess_amount));

    }

}
