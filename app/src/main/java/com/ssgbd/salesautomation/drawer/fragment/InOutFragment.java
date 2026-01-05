package com.ssgbd.salesautomation.drawer.fragment;

import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
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
import com.ssgbd.salesautomation.adapters.FOInOutListRecyclerAdapter;
import com.ssgbd.salesautomation.dtos.AttendanceReportListDTO;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.FOLeaveListDTO;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;

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


public class InOutFragment extends Fragment implements View.OnClickListener{

    View rootView;
    TextView txt_route_name;
    FOInOutListRecyclerAdapter inout_list_adapter;
    ArrayList<CategoryDTO> leaveDTOS = new ArrayList<>();
    String reasonId="";
    TextView txt_fromdate;
    LinearLayoutManager linearLayoutManager1;
    ArrayList<FOLeaveListDTO> adjustListDTOS = new ArrayList<>();
    VolleyMethods vm = new VolleyMethods();
    RecyclerView adjust_list_recyclerView;
    Spinner adjReasonSpinner;
    Button btn_add;
    EditText edt_inoutcomment,edt_out_time;
    public RequestQueue queue;
    String PRESENT_DATE="";
    String REASON_NAME="";
    DatePickerDialog picker;
    String _DATE="";
    CheckBox ch1,ch2;
    String adjType = "";
    long l;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.in_out_fragment, container, false);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        PRESENT_DATE = df.format(c);

        adjust_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adjust_list_recyclerView.setLayoutManager(linearLayoutManager1);
        inout_list_adapter = new FOInOutListRecyclerAdapter(adjustListDTOS,getActivity(), InOutFragment.this,PRESENT_DATE);
        adjust_list_recyclerView.setAdapter(inout_list_adapter);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        edt_inoutcomment = (EditText) rootView.findViewById(R.id.edt_inoutcomment);
        edt_out_time = (EditText) rootView.findViewById(R.id.edt_out_time);

        ch1=(CheckBox) rootView.findViewById(R.id.checkBox1);
        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getActivity(), "checked", Toast.LENGTH_SHORT).show();
                if (ch1.isChecked()){
                    adjType="in_time";
                    ch2.setChecked(false);
                    edt_out_time.setVisibility(View.GONE);
                    inOutTypeRequest(getString(R.string.base_url_hris)+"adjustment-reason-by-type"+"/in_time");
                   // Log.e("<<in_time>>","");
                }else {
                    adjType="out_time";
                    ch2.setChecked(true);
                }
            }
        });

        ch2=(CheckBox) rootView.findViewById(R.id.checkBox2);
        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getActivity(), "checked", Toast.LENGTH_SHORT).show();
                if (ch2.isChecked()){
                    edt_out_time.setVisibility(View.VISIBLE);
                    adjType="out_time";
                    ch1.setChecked(false);
                    inOutTypeRequest(getString(R.string.base_url_hris)+"adjustment-reason-by-type"+"/out_time");
                }else {
                    edt_out_time.setVisibility(View.GONE);
                    adjType="in_time";
                    ch1.setChecked(true);
                }
            }
        });
        initUi();
        txt_fromdate.setText(PRESENT_DATE);
        //inOutListHRIS();
        adjustLst(getString(R.string.base_url_hris)+"adjustment?limit=30");
        return rootView;
    }

    private void initUi() {

        queue = Volley.newRequestQueue(getActivity());
        txt_route_name = (TextView) rootView.findViewById(R.id.txt_route_name);

        CategoryDTO c = new CategoryDTO();
        c.setId("");
        c.setName("--Select Reason--");
        leaveDTOS.add(c);

        //inOutReason();

        adjReasonSpinner = (Spinner) rootView.findViewById(R.id.spinnerOfferType);
        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
                R.layout.customspinneritem, leaveDTOS);

        adjReasonSpinner.setAdapter(adapter);

        adjReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

                reasonId = leaveDTOS.get(pos).getId();

                REASON_NAME =leaveDTOS.get(pos).getName();
              //loadReason(statusId);
            }

            public void onNothingSelected(AdapterView<?> parent) {
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

//                          txt_month_year.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }

                                _DATE = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    _DATE = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_add:

                if (REASON_NAME.equalsIgnoreCase("--Select Reason--")|| REASON_NAME.length()==0){
                    Toast.makeText(getActivity(), "আপনার ছুটির ধরণ সিলেক্ট করুন।", Toast.LENGTH_SHORT).show();

                    return;
                }

                else if (reasonId.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "কারণ  সিলেক্ট করুন। ।", Toast.LENGTH_SHORT).show();

                }else if (_DATE.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "তারিখ  সিলেক্ট করুন। ।", Toast.LENGTH_SHORT).show();

                }
                else if (adjType.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "অ্যাডজাস্টমেন্ট টাইপ সিলেক্ট করুন।", Toast.LENGTH_SHORT).show();
                } else {

                   // inOutAppley();
                    adjustmentApply(getString(R.string.base_url_hris)+"adjustment");
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



    public void inOutTypeRequest(String url){

        leaveDTOS.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {
                    JSONObject jsonObject1 = new JSONObject(response);

                    if (jsonObject1.getString("success").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject1.getJSONArray("data");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            AttendanceReportListDTO listDTO = new AttendanceReportListDTO();

                            CategoryDTO c = new CategoryDTO();
                            c.setId(optStringNullCheck(object,"id"));
                            c.setName(optStringNullCheck(object,"reason_name"));
                            leaveDTOS.add(c);
                        }
                        inout_list_adapter.notifyDataSetChanged();
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

    private void adjustmentApply(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respjsonObj = new JSONObject(response);
                         //   Log.e("<<>>",respjsonObj+"");
                            if (respjsonObj.getString("success").equalsIgnoreCase("true")) {

                                Toast.makeText(getActivity(), "আবেদন সম্পন্ন হয়েছে।", Toast.LENGTH_SHORT).show();

                                REASON_NAME = "--Select Reason--";
                                _DATE= "";
                                reasonId="";
                                edt_inoutcomment.setText("");

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

                params.put("user_id", SharePreference.getHrisID(getActivity()));

                params.put("adjustment_from", _DATE);
                params.put("adjustment_to", _DATE);
                params.put("adjustment_reason_id", reasonId);
                params.put("remarks", edt_inoutcomment.getText().toString());
                params.put("status", "1");
                params.put("adjusted_time_in", "10:00");
                params.put("adjusted_time_out", "16:20");

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

    private void adjustLst(String url) {
        adjustListDTOS.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respjsonObj = new JSONObject(response);
                           //    Log.e("<<>>",respjsonObj+"");
                            if (respjsonObj.getString("success").equalsIgnoreCase("true")) {

                                JSONObject dataobject = respjsonObj.getJSONObject("data");
                                //   Log.e("<<>>",dataobject+"");
                                JSONArray jsonArray = dataobject.getJSONArray("data");
                                //Log.e("<<>>",jsonArray+"");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    FOLeaveListDTO listDTO = new FOLeaveListDTO();

                                    String dateTimeString = jsonObject.getString("adjustment_from");
                                    String adjustment_from = dateTimeString.split("T")[0];

                                    String dateTimeString1 = jsonObject.getString("created_at");
                                    String created_at = dateTimeString1.split("T")[0];
                                    listDTO.setApplydate(created_at);
                                    listDTO.setFromdate(adjustment_from);
                                    listDTO.setRemarks(optStringNullCheck(jsonObject,"remarks"));
                                    listDTO.setStatus(optStringNullCheck(jsonObject,"status"));

                                    JSONObject reasonbject = jsonObject.getJSONObject("adjustment_reason");
                                   listDTO.setType(reasonbject.getString("adjustment_type"));
                                    adjustListDTOS.add(listDTO);
                                   // adjustment_from

                                }
                                inout_list_adapter.notifyDataSetChanged();
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
                params.put("user_id", SharePreference.getHrisID(getActivity()));

                params.put("adjustment_from", _DATE);
                params.put("adjustment_to", _DATE);
                params.put("adjustment_reason_id", reasonId);
                params.put("remarks", edt_inoutcomment.getText().toString());
                params.put("status", "1");
                params.put("adjusted_time_in", "10:00");
                params.put("adjusted_time_out", "16:20");
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
