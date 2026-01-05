package com.ssgbd.salesautomation.report.fanreplace;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.FanQcListAdapter;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.VisitReportDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;


public class FanQCListFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    FanQcListAdapter fanQcListAdapter;
    RecyclerView retailer_list_recyclerView;


    LinearLayoutManager linearLayoutManager1;
    ArrayList<VisitReportDTO> visitReportListDTOS = new ArrayList<>();
    TextView txt_fromdate,txt_todate,txt_search;
    String formattedDate="";
    DatePickerDialog picker;
    String fromDate="";
    String toDate="";
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    Spinner spinnerOfferType;
    String TYPE_ID="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fan_qc_list_fragment, container, false);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);


        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        fanQcListAdapter = new FanQcListAdapter( visitReportListDTOS,getActivity(), FanQCListFragment.this);
        retailer_list_recyclerView.setAdapter(fanQcListAdapter);



        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int month=cldr.get(Calendar.MONTH);
        int year=cldr.get(Calendar.YEAR);

 //       Log.e("dmy>",year+"-"+(month+1)+"-"+day+"");

        String d=year+"-"+(month+1)+"-"+day;

      getReport(d,d);

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
                                fromDate = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    fromDate = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
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

//                              txt_month_year.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                         //       Log.e(">>",dayOfMonth + "/" + (monthOfYear + 1) + "/" + year+"");

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
                                toDate = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    toDate = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }

                            }
                        }, year, month, day);
                picker.show();
            }
        });

        txt_search = (TextView) rootView.findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReport(fromDate,toDate );
            }
        });

        CategoryDTO c1 = new CategoryDTO();
        c1.setId("");
        c1.setName("All");
        categoryDTOS.add(c1);
        CategoryDTO c11 = new CategoryDTO();
        c11.setId("3");
        c11.setName("Order");
        categoryDTOS.add(c11);
        CategoryDTO c12 = new CategoryDTO();
        c12.setId("1");
        c12.setName("Non-Visit");
        categoryDTOS.add(c12);
        CategoryDTO c13 = new CategoryDTO();
        c13.setId("2");
        c13.setName("Visit");
        categoryDTOS.add(c13);



        spinnerOfferType = (Spinner) rootView.findViewById(R.id.spinnerOfferType);
        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
                R.layout.customspinneritem, categoryDTOS);

        spinnerOfferType.setAdapter(adapter);

        spinnerOfferType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

             //   Log.e(">>>><<>>",categoryDTOS.get(pos).getId()+"<<>>"+categoryDTOS.get(pos).getName()+"");
                TYPE_ID = categoryDTOS.get(pos).getId();

            }

            public void onNothingSelected(AdapterView<?> parent) {
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
        visitReportListDTOS.clear();
        fanQcListAdapter.notifyDataSetChanged();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),  getString(R.string.base_url)+"api/report/fo-visit",
                jp.jsonFoVisitReport( SharePreference.getUserId(getActivity()),fromDAte,TODate,TYPE_ID), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                             //       Log.e("jsonObject1",jsonObject1+"");
                                   // Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();

                                    if (jsonObject1.getString("status").equalsIgnoreCase("1")){

                                    JSONArray jsonArray = jsonObject1.getJSONArray("fo_visit");
                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        VisitReportDTO listDTO = new VisitReportDTO();
                                        listDTO.setFoName(optStringNullCheck(object,"fo_name"));
                                        listDTO.setStatus(optStringNullCheck(object,"status"));
                                        listDTO.setDate(optStringNullCheck(object,"date"));
                                        listDTO.setRetailerName(optStringNullCheck(object,"retailerName"));
                                        listDTO.setRouteName(optStringNullCheck(object,"routeName"));

                                        visitReportListDTOS.add(listDTO);
                                    }
                                        fanQcListAdapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                                {"status":"0","return_change_list":[{"return_order_id":1757,
//                                        "return_order_no":"37239-19072011668",
//                                        "return_order_date":"2019-08-01 12:11:23",
//                                        "qty":20,
//                                        "value":"5160.00",
//                                        "foname":"Anowar Hossain",
//                                        "customername":"Joshim Electric",
//                                        "customermobile":"8801720530780"}]}

                            }
                        });
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
}
