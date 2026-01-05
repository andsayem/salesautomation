package com.ssgbd.salesautomation.report;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
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
import com.ssgbd.salesautomation.adapters.AttendanceReportListAdapter;
import com.ssgbd.salesautomation.adapters.AttendanceReportListAdapterSsforce;
import com.ssgbd.salesautomation.dtos.AttendanceReportListDTO;
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
//import static com.google.android.gms.internal.zzahg.runOnUiThread;

public class AttendanceReportFragmentSsforce extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    AttendanceReportListAdapterSsforce retialer_adapter;
    RecyclerView retailer_list_recyclerView;
    public RequestQueue queue;
   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<AttendanceReportListDTO> changeReportListDTOS = new ArrayList<>();
    TextView txt_fromdate,txt_todate,txt_search,txt_total_count;
    String formattedDate="";
    DatePickerDialog picker;
    String fromDate="";
    String FROMDATE="",TODATE="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.attendance_report_fragment_ssforce, container, false);

        queue = Volley.newRequestQueue(getActivity());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        txt_total_count = (TextView) rootView.findViewById(R.id.txt_total_count);

        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new AttendanceReportListAdapterSsforce( changeReportListDTOS,getActivity(), AttendanceReportFragmentSsforce.this);
        retailer_list_recyclerView.setAdapter(retialer_adapter);

        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int month=cldr.get(Calendar.MONTH);
        int year=cldr.get(Calendar.YEAR);

        String d=year+"-"+(month+1)+"-"+day;

        getReport(d,d);
     //   attendanceReportHRIS(d,d);


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
        txt_search = (TextView) rootView.findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getReport(FROMDATE, TODATE);
              //  Log.e("<<>>",FROMDATE+"-"+TODATE+"");
//                attendanceReportHRIS(FROMDATE,TODATE);
            }
        });

        return rootView;
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


    public void getReport(String fromDAte,String TODate){
        changeReportListDTOS.clear();
        retialer_adapter.notifyDataSetChanged();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),  getString(R.string.base_url)+"api/report/fo-attendance",
                jp.jsonReportOrderVsReport( SharePreference.getUserId(getActivity()),fromDAte,TODate), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();
                           // Log.e("result",result+"result");


                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                                  ///  Log.e("jsonObject1",jsonObject1+"");
                                   // Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();

                                    if (jsonObject1.getString("status").equalsIgnoreCase("1")){

                                    JSONArray jsonArray = jsonObject1.getJSONArray("fo_attendance");
                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        AttendanceReportListDTO listDTO = new AttendanceReportListDTO();
                                        //in
                                        listDTO.setDate(optStringNullCheck(object,"date"));
                                        listDTO.setInTime(optStringNullCheck(object,"inTme"));
                                        listDTO.setInTimeRetailerName(optStringNullCheck(object,"inTimeRetailerName"));
                                        listDTO.setInTimeRetailerAddress(optStringNullCheck(object,"inTimeRetailerAddress"));
                                        //out
                                        listDTO.setOutTime(optStringNullCheck(object,"outTime"));
                                        listDTO.setOutTimeRetailerName(optStringNullCheck(object,"outTimeRetailerName"));
                                        listDTO.setOutTimeRetailerAddress(optStringNullCheck(object,"outTimeRetailerAddress"));
                                        listDTO.setWorkingHoure(optStringNullCheck(object,"workingHour"));
                                        listDTO.setRoute(optStringNullCheck(object,"inRoute"));
                                        listDTO.setLeave(optStringNullCheck(object,"leave"));
                                        listDTO.setLeaveName(optStringNullCheck(object,"leaveName"));
                                        listDTO.setAbsent(optStringNullCheck(object,"absent"));
                                        listDTO.setFriday(optStringNullCheck(object,"friday"));


                                        changeReportListDTOS.add(listDTO);
                                    }
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


}
