package com.ssgbd.salesautomation.drawer.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.FOLeaveListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.dtos.AttendanceReportListDTO;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.FOLeaveListDTO;
import com.ssgbd.salesautomation.dtos.LeaveRelieverDTO;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;


public class FoLeaveFragment extends Fragment implements View.OnClickListener{

    View rootView;
    private Dialog wdialog;
    RouteRecyclerAdapter routeRecyclerAdapter;
    TextView txt_route_name;
    FOLeaveListRecyclerAdapter leave_list_adapter;
    RecyclerView leave_list_recyclerView;
    ArrayList<CategoryDTO> leaveDTOS = new ArrayList<>();
    ArrayList<LeaveRelieverDTO> leaveRelieverDTOS = new ArrayList<>();
    ArrayList<CategoryDTO> categoryDTOS1 = new ArrayList<>();
    String statusId="";
    String reliever_id="";
    TextView txt_fromdate,txt_todate;
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<FOLeaveListDTO> foLeaveListDTOS = new ArrayList<>();
    VolleyMethods vm = new VolleyMethods();


    Spinner spinnerOfferType,spinnerOfferType_reliever;
    Button btn_add;
    EditText edt_leave_reason,edt_address,edt_contactnumber,edt_todaydate;
    public RequestQueue queue;
    String PRESENT_DATE="";
    String LEAVE_NAME="";
    String LEAVE_RELIEVER_NAME="";

    String REASON_CATEGORY_ID="";
    DatePickerDialog picker;
    String FROMDATE="",TODATE="";
    long l;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fo_leave_fragment, container, false);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dff = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        PRESENT_DATE = df.format(c);
        String s=dff.format(c);

        edt_todaydate = (EditText) rootView.findViewById(R.id.edt_todaydate);
        edt_todaydate.setText(s);

        leave_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        leave_list_recyclerView.setLayoutManager(linearLayoutManager1);
        leave_list_adapter = new FOLeaveListRecyclerAdapter( foLeaveListDTOS,getActivity(),FoLeaveFragment.this,PRESENT_DATE);
        leave_list_recyclerView.setAdapter(leave_list_adapter);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        edt_leave_reason = (EditText) rootView.findViewById(R.id.edt_leave_reason);
        edt_address = (EditText) rootView.findViewById(R.id.edt_address);
        edt_contactnumber = (EditText) rootView.findViewById(R.id.edt_contactnumber);


        initUi();
        return rootView;
    }

    private void initUi() {

        queue = Volley.newRequestQueue(getActivity());
        txt_route_name = (TextView) rootView.findViewById(R.id.txt_route_name);

        CategoryDTO c = new CategoryDTO();
        c.setId("000");
        c.setName("--Select Leave Type--");
        leaveDTOS.add(c);

        LeaveRelieverDTO lr = new LeaveRelieverDTO();
        lr.setId("000");
        lr.setName("--Select Leave Reliever--");
        lr.setEmployee_code("000");
        leaveRelieverDTOS.add(lr);

      //getLeaveType();
        leaveTypeRequest(getString(R.string.base_url_hris)+"leave-types");
        leaveRelieverRequest(getString(R.string.base_url_hris)+"leave-reliever-list/"+SharePreference.getHrisID(getActivity()));

        spinnerOfferType = (Spinner) rootView.findViewById(R.id.spinnerOfferType);
        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
                R.layout.customspinneritem, leaveDTOS);

        spinnerOfferType.setAdapter(adapter);

        spinnerOfferType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

                statusId = leaveDTOS.get(pos).getId();
                LEAVE_NAME =leaveDTOS.get(pos).getName();
              //loadReason(statusId);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerOfferType_reliever = (Spinner) rootView.findViewById(R.id.spinnerOfferType_reliever);
        CustomArrayAdapterRL adapterrel = new CustomArrayAdapterRL(getActivity(),
                R.layout.customspinneritem, leaveRelieverDTOS);

        spinnerOfferType_reliever.setAdapter(adapterrel);

        spinnerOfferType_reliever.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

                reliever_id = leaveRelieverDTOS.get(pos).getId();
                LEAVE_RELIEVER_NAME =leaveRelieverDTOS.get(pos).getName();
              //loadReason(statusId);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //getFoLeaveList();
       // leaveListHRIS();//
        leaveLst(getString(R.string.base_url_hris)+"leave-applications");
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

//                              txt_month_year.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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

               // picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            }
        });

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

    }

    public class CustomArrayAdapter extends ArrayAdapter<String> {

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

    public class CustomArrayAdapterRL extends ArrayAdapter<String> {

        private final LayoutInflater mInflater;
        private final Context mContext;
        private final ArrayList<LeaveRelieverDTO> items;
        private final int mResource;

        public CustomArrayAdapterRL(@NonNull Context context, @LayoutRes int resource,
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

            LeaveRelieverDTO offerData = items.get(position);

            offTypeTv.setText(items.get(position).getName()+"("+items.get(position).getEmployee_code()+")");

            return view;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_add:

                if (LEAVE_NAME.equalsIgnoreCase("--Select Leave Type--")|| LEAVE_NAME.length()==0){
                    Toast.makeText(getActivity(), "আপনার ছুটির ধরণ সিলেক্ট করুন।", Toast.LENGTH_SHORT).show();

                    return;
                }

                else if (FROMDATE.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "তারিখ  সিলেক্ট করুন। ।", Toast.LENGTH_SHORT).show();

                } else if (TODATE.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "তারিখ  সিলেক্ট করুন। ।", Toast.LENGTH_SHORT).show();

                }  else if (reliever_id.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "আপনার লিভ রিলিভার সিলেক্ট করুন।", Toast.LENGTH_SHORT).show();

                } else if (edt_leave_reason.getText().length()==0){
                    Toast.makeText(getActivity(), "আপনার ছুটির কারন  লিখুন ।", Toast.LENGTH_SHORT).show();

                }else {
                    //foLeaveApplay( LEAVE_NAME, PRESENT_DATE, FROMDATE, TODATE, edt_leave_reason.getText().toString(),edt_remarks.getText().toString());
                  //  applayLeave(statusId,edt_leave_reliver.getText().toString(),edt_address.getText().toString(),edt_contactnumber.getText().toString(),edt_leave_reason.getText().toString(),edt_remarks.getText().toString());
                    leaveApplay(getString(R.string.base_url_hris)+"leave-applications");
                }
                break;

        }
    }


    public  String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null"))
            return "-";
        else
            return json.optString(key, key);
    }



    public void leaveTypeRequest(String url){

        Utility.routeDTOS.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //   Log.e("<<>>",response+"");

                try {
                    JSONObject jsonObject1 = new JSONObject(response);

                    if (jsonObject1.getString("success").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject1.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        AttendanceReportListDTO listDTO = new AttendanceReportListDTO();

                        CategoryDTO c = new CategoryDTO();
                        c.setId(optStringNullCheck(object,"id"));
                        c.setName(optStringNullCheck(object,"description"));
                        leaveDTOS.add(c);

                    }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("VOLLEY", error.toString());
                //    Utility.dialog.closeProgressDialog();
                //   systemBackup(context);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");

                headers.put("Authorization", "Bearer "+SharePreference.getAccesstokenHRIS(getActivity()).trim());

                return headers;
            }
        };

        queue.add(stringRequest);

    }
    public void leaveRelieverRequest(String url){

        Utility.routeDTOS.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //   Log.e("<<>>",response+"");

                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                      // Log.e("<<>>",jsonObject1+"");

                    if (jsonObject1.getString("success").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject1.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        LeaveRelieverDTO c = new LeaveRelieverDTO();
                        c.setId(optStringNullCheck(object,"id"));
                        c.setName(optStringNullCheck(object,"name"));
                        c.setEmployee_code(optStringNullCheck(object,"employee_code"));
                        leaveRelieverDTOS.add(c);

                    }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("VOLLEY", error.toString());
                //    Utility.dialog.closeProgressDialog();
                //   systemBackup(context);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");

                headers.put("Authorization", "Bearer "+SharePreference.getAccesstokenHRIS(getActivity()).trim());

                return headers;
            }
        };

        queue.add(stringRequest);

    }

    public void leaveListHRIS(String url){


        Utility.routeDTOS.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //   Log.e("<<>>",response+"");

                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    // Log.e("<<>>",jsonObject1+"");

                    if (jsonObject1.getString("success").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject1.getJSONArray("data");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            LeaveRelieverDTO c = new LeaveRelieverDTO();
                            c.setId(optStringNullCheck(object,"id"));
                            c.setName(optStringNullCheck(object,"name"));
                            c.setEmployee_code(optStringNullCheck(object,"employee_code"));
                            leaveRelieverDTOS.add(c);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("VOLLEY", error.toString());
                //    Utility.dialog.closeProgressDialog();
                //   systemBackup(context);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");

                headers.put("Authorization", "Bearer "+SharePreference.getAccesstokenHRIS(getActivity()).trim());

                return headers;
            }
        };

        queue.add(stringRequest);

    }


    private void leaveApplay(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respjsonObj = new JSONObject(response);
                          //  Log.e("<<>>",respjsonObj+"");
                        if (respjsonObj.getString("success").equalsIgnoreCase("true")) {


                            LEAVE_NAME = "--Select Leave Type--";
                            reliever_id = "";
                            edt_address.setText("");
                            edt_contactnumber.setText("");
                            edt_leave_reason.setText("");
                            txt_todate.setText("");
                            txt_fromdate.setText("");
                            Toast.makeText(getActivity(), "ছুটির আবেদন সম্পন্ন হয়েছে। ", Toast.LENGTH_SHORT).show();
                            leaveLst(getString(R.string.base_url_hris)+"leave-applications");

                        }
                            // Handle response
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Error parsing response: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("JSONError", "Error parsing response", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Request failed";
                        if (error.networkResponse != null) {
                            errorMessage += " - Status: " + error.networkResponse.statusCode;
                            try {
                                String responseBody = new String(error.networkResponse.data,
                                        "utf-8");
                                errorMessage += " - Response: " + responseBody;
                                Log.e("VolleyError", responseBody);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("VolleyError", "Error occurred", error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json"); // Changed from urlencoded
                headers.put("Content-Type", "application/json"); // Changed to match likely server expectation
                headers.put("Authorization", "Bearer " + SharePreference.getAccesstokenHRIS(getActivity()).trim());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", "");
                params.put("user_id", SharePreference.getHrisID(getActivity()));

                params.put("leave_type_id", statusId);
                params.put("leave_reliever", reliever_id);
                params.put("start_date", FROMDATE);
                params.put("end_date", TODATE);
                params.put("reason", edt_leave_reason.getText().toString());
                params.put("address_during_leave", edt_address.getText().toString());
                params.put("days_requested", "");
                params.put("contact_number", edt_contactnumber.getText().toString());
                params.put("status", "");
                // ... rest of your params
               // Log.e("<<>>", params.toString()+"");
                return params;
            }

            // Add this to properly handle JSON body if server expects it
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return new JSONObject(getParams()).toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("VolleyError", "Encoding error", e);
                    return null;
                }
            }
        };

        // Set retry policy
        sr.setRetryPolicy(new DefaultRetryPolicy(
                10000, // timeout in ms
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(sr);
    }

    private void leaveLst(String url) {
        foLeaveListDTOS.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respjsonObj = new JSONObject(response);

                        if (respjsonObj.getString("success").equalsIgnoreCase("true")) {

                    JSONObject dataobject = respjsonObj.getJSONObject("data");
                         //   Log.e("<<>>",dataobject+"");
                            JSONArray jsonArray = dataobject.getJSONArray("data");
                            //Log.e("<<>>",jsonArray+"");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FOLeaveListDTO listDTO = new FOLeaveListDTO();
                        String dateTimeString1 = jsonObject.getString("created_at");
                        String created_at = dateTimeString1.split("T")[0];
                        listDTO.setApplydate(created_at);
                        listDTO.setFromdate(jsonObject.getString("start_date"));
                      listDTO.setTodate(jsonObject.getString("end_date"));
                      listDTO.setRemarks(optStringNullCheck(jsonObject,"status"));
                      listDTO.setStatus(jsonObject.getString("status"));
                        foLeaveListDTOS.add(listDTO);

                    }
                            leave_list_adapter.notifyDataSetChanged();
                        }
                            // Handle response
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Error parsing response: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("JSONError", "Error parsing response", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Request failed";
                        if (error.networkResponse != null) {
                            errorMessage += " - Status: " + error.networkResponse.statusCode;
                            try {
                                String responseBody = new String(error.networkResponse.data,
                                        "utf-8");
                                errorMessage += " - Response: " + responseBody;
                                Log.e("VolleyError", responseBody);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("VolleyError", "Error occurred", error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json"); // Changed from urlencoded
                headers.put("Content-Type", "application/json"); // Changed to match likely server expectation
                headers.put("Authorization", "Bearer " + SharePreference.getAccesstokenHRIS(getActivity()).trim());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", "");
                params.put("user_id", SharePreference.getHrisID(getActivity()));

                params.put("leave_type_id", statusId);
                params.put("leave_reliever", reliever_id);
                params.put("start_date", FROMDATE);
                params.put("end_date", TODATE);
                params.put("reason", edt_leave_reason.getText().toString());
                params.put("address_during_leave", edt_address.getText().toString());
                params.put("days_requested", "");
                params.put("contact_number", edt_contactnumber.getText().toString());
                params.put("status", "");
                // ... rest of your params
              //  Log.e("<<>>", params.toString()+"");
                return params;
            }

            // Add this to properly handle JSON body if server expects it
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return new JSONObject(getParams()).toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("VolleyError", "Encoding error", e);
                    return null;
                }
            }
        };

        // Set retry policy
        sr.setRetryPolicy(new DefaultRetryPolicy(
                10000, // timeout in ms
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(sr);
    }

}
