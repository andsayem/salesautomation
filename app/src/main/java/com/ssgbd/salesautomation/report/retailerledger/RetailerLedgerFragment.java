package com.ssgbd.salesautomation.report.retailerledger;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter_RetailerLedger;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.RetailerLedgerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.report.FoActivityReportFragment;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class RetailerLedgerFragment extends Fragment implements View.OnClickListener{

    View rootView;
    private Dialog wdialog;
    RouteRecyclerAdapter routeRecyclerAdapter;
    TextView txt_route_list,txt_date_name,txt_to_date;
    RetailerRecyclerAdapter_RetailerLedger retialer_adapter;
    RecyclerView retailer_list_recyclerView;

    LinearLayoutManager linearLayoutManager1;
    ArrayList<RetailerLedgerDTO> retailerDTOS = new ArrayList<>();
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener toDate;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendarTO = Calendar.getInstance();
    String routeId="";
    TextView row_opening_balance_total,row_salse_total,row_collection_total,row_balance_total;
    TextView row_ret_sales,row_open_off,row_monthly_comm,row_val_comm,row_mem_comm;
    public RequestQueue queue;
    float totalOpeningBalance,totalSalse,totalCollection,totalBalance;
    float  returnSales,openOffer,monthlyComm,valueComm,memoComm;
    VolleyMethods vm = new VolleyMethods();
    String fDate="";
    String td="";
    Spinner spinnerYear;
    ArrayList<CategoryDTO> yearDTOS = new ArrayList<>();
    TextView offTypeTv;
    String foid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rootView = inflater.inflate(R.layout.retailer_ledger_fragment, container, false);

        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int month=cldr.get(Calendar.MONTH);
        int year=cldr.get(Calendar.YEAR);


        fDate= day+"/"+(month+1)+"/"+ year;
        td= day+"/"+(month+1)+"/"+ year;


        initUi();

        CategoryDTO c1 = new CategoryDTO();
        c1.setId(SharePreference.getUserId(getActivity()));
        c1.setName(SharePreference.getUserName(getActivity()));
        yearDTOS.add(c1);
        CategoryDTO c11 = new CategoryDTO();
        c11.setId("");
        c11.setName("All");
        yearDTOS.add(c11);

        spinnerYear = (Spinner) rootView.findViewById(R.id.spinnerYear);
        YearArrayAdapter adapter = new YearArrayAdapter(getActivity(),R.layout.customspinneritem, yearDTOS);

        spinnerYear.setAdapter(adapter);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

                // shopType = categoryDTOS.get(pos).getId();
            //    Log.e(">>>shoptype><<>>",yearDTOS.get(pos).getId()+"");
                foid = yearDTOS.get(pos).getId();
                getRetailerLedger(routeId);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        return rootView;
    }

    private void initUi() {
        queue = Volley.newRequestQueue(getActivity());
        txt_date_name = (TextView) rootView.findViewById(R.id.txt_date_name);
        txt_date_name.setOnClickListener(this);
        final Calendar cldr=Calendar.getInstance();
        int year=cldr.get(Calendar.YEAR);
        int day=cldr.get(Calendar.DAY_OF_MONTH);

        String monthname=(String)android.text.format.DateFormat.format("MMMM", new Date());
        txt_date_name.setText("1"+" "+monthname+" "+String.valueOf(year) );

        txt_to_date = (TextView) rootView.findViewById(R.id.txt_to_date);
        txt_to_date.setOnClickListener(this);
        txt_to_date.setText(day+" "+monthname+" "+String.valueOf(year));

        txt_route_list = (TextView) rootView.findViewById(R.id.txt_route_list);
        txt_route_list.setOnClickListener(this);

        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new RetailerRecyclerAdapter_RetailerLedger( retailerDTOS,getActivity(), RetailerLedgerFragment.this);
        retailer_list_recyclerView.setAdapter(retialer_adapter);

        row_opening_balance_total = (TextView) rootView.findViewById(R.id.row_opening_balance_total);
        row_salse_total = (TextView) rootView.findViewById(R.id.row_salse_total);
        row_collection_total = (TextView) rootView.findViewById(R.id.row_collection_total);
        row_balance_total = (TextView) rootView.findViewById(R.id.row_balance_total);

        row_ret_sales = (TextView) rootView.findViewById(R.id.row_ret_sales);
        row_open_off = (TextView) rootView.findViewById(R.id.row_open_off);
        row_monthly_comm = (TextView) rootView.findViewById(R.id.row_monthly_comm);
        row_val_comm = (TextView) rootView.findViewById(R.id.row_val_comm);
        row_mem_comm = (TextView) rootView.findViewById(R.id.row_mem_comm);
        // date picker
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String s = + dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                txt_date_name.setText("From Date :"+s);


//                if (routeId.length()==0) {
//                    Toast.makeText(getActivity(), "Please select a route first.", Toast.LENGTH_SHORT).show();
//
//                }else {
                    retailerDTOS.clear();
                   // getRetailerLedger(routeId,+dayOfMonth+"/"+(monthOfYear+1)+"/"+ year);
                fDate= dayOfMonth+"/"+(monthOfYear+1)+"/"+ year;
                   // Toast.makeText(getActivity(), "Please wait processing your data...", Toast.LENGTH_SHORT).show();
//                }
            }
        };

        // toDate picker
        toDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarTO.set(Calendar.YEAR, year);
                myCalendarTO.set(Calendar.MONTH, monthOfYear);
                myCalendarTO.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String s = + dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                txt_to_date.setText("To Date :"+s);


//                if (routeId.length()==0) {
//                    Toast.makeText(getActivity(), "Please select a route first.", Toast.LENGTH_SHORT).show();
//
//                }else {
                    retailerDTOS.clear();
                   td = dayOfMonth+"/"+(monthOfYear+1)+"/"+ year;
                    getRetailerLedger(routeId);
                    Toast.makeText(getActivity(), "Please wait processing your data...", Toast.LENGTH_SHORT).show();
//                }
            }
        };
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

        RouteDTO routeDTOd = new RouteDTO();
        routeDTOd.setPoint_id("");
        routeDTOd.setTerritory_id("");
        routeDTOd.setRname("All");
        routeDTOd.setRoute_id("all");
        Utility.routeDTOS.add(routeDTOd);

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

              routeId = Utility.routeDTOS.get(position).getRoute_id();

              Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_SHORT).show();
              txt_route_list.setText("Route Name-"+ Utility.routeDTOS.get(position).getRname());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_route_list:
                showRouteListDialog();
                break;

             case R.id.txt_date_name:
                 new DatePickerDialog(getActivity(), date, myCalendar
                         .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                         myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

                case R.id.txt_to_date:
                 new DatePickerDialog(getActivity(), toDate, myCalendarTO
                         .get(Calendar.YEAR), myCalendarTO.get(Calendar.MONTH),
                         myCalendarTO.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    public void getRetailerLedger(String routeId){
        JsonRequestFormate jp = new JsonRequestFormate();
        retailerDTOS.clear();

        vm.sendRequestToServer2(getActivity(), getString(R.string.base_url)+"api/fo/retailer_wise_credit", jp.jsonGetRetailerLedger(SharePreference.getUserId(getActivity()),routeId,foid,fDate,td), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                totalOpeningBalance=0;
                totalSalse=0;
                returnSales=0;
                openOffer=0;
                monthlyComm=0;
                valueComm=0;
                memoComm=0;
                totalCollection=0;
                totalBalance=0;

              //  Log.e("<<>>",result+"<");

                    try {
                        JSONObject respjsonObj = new JSONObject(result);
                        JSONArray statusArray = respjsonObj.getJSONArray("json_data_list");
                        for (int i = 0; i < statusArray.length(); i++) {
                            JSONObject routeObject = statusArray.getJSONObject(i);
                            RetailerLedgerDTO routeDTO = new RetailerLedgerDTO();
                            routeDTO.setRetailerId(routeObject.getString("name_retailer_id"));
                            routeDTO.setRouteName(routeObject.getString("rname"));
                            routeDTO.setOpeningBalance(routeObject.getString("opening_balance"));
                            routeDTO.setAllKindOfSales(routeObject.getString("all_kinds_of_sales"));
                            routeDTO.setCollection(routeObject.getString("collection"));
                            routeDTO.setBalance(routeObject.getString("balance"));
                         //  Log.e("<<>>",routeObject.getString("rname")+routeObject.getString("name_retailer_id")+"<<>>"+routeObject.getString("balance")+"<<>>"+"");
                            routeDTO.setReturnSales(routeObject.getString("Sales_Return"));
                            routeDTO.setOpenOffer(routeObject.getString("OpenOfferSales"));
                            routeDTO.setMonthlYComm(routeObject.getString("Monthly_Comm"));
                            routeDTO.setValueComm(routeObject.getString("ValueWiseComm"));
                            routeDTO.setMemoCom(routeObject.getString("Memo_Comm"));

                            retailerDTOS.add(routeDTO);

                            totalOpeningBalance += Float.parseFloat(routeObject.getString("opening_balance").replace(",",""));
                            totalSalse += Float.parseFloat(routeObject.getString("all_kinds_of_sales").replace(",",""));
                            totalCollection += Float.parseFloat(routeObject.getString("collection").replace(",",""));
                            totalBalance += Float.parseFloat(routeObject.getString("balance").replace(",",""));


                            returnSales+= Float.parseFloat(routeObject.getString("Sales_Return").replace(",",""));;
                            openOffer+= Float.parseFloat(routeObject.getString("OpenOfferSales").replace(",",""));;
                            monthlyComm+= Float.parseFloat(routeObject.getString("Monthly_Comm").replace(",",""));
                            valueComm+= Float.parseFloat(routeObject.getString("ValueWiseComm").replace(",",""));
                            memoComm+= Float.parseFloat(routeObject.getString("Memo_Comm").replace(",",""));

                        }

                        DecimalFormat formatter = new DecimalFormat("#,###");

                        row_opening_balance_total.setText(String.valueOf(formatter.format(Math.round(totalOpeningBalance))));
                        row_salse_total.setText(String.valueOf(formatter.format(Math.round(totalSalse))));
                        row_collection_total.setText(String.valueOf(formatter.format(Math.round(totalCollection))));
                        row_balance_total.setText(String.valueOf(formatter.format(Math.round(totalBalance))));

                        row_ret_sales.setText(String.valueOf(formatter.format(Math.round(returnSales))));
                        row_open_off.setText(String.valueOf(formatter.format(Math.round(openOffer))));
                        row_monthly_comm.setText(String.valueOf(formatter.format(Math.round(monthlyComm))));
                        row_val_comm.setText(String.valueOf(formatter.format(Math.round(valueComm))));
                        row_mem_comm.setText(String.valueOf(formatter.format(Math.round(memoComm))));


                       // Log.e(">>>",doubleToStringNoDecimal(Double.parseDouble(row_opening_balance_total.toString()))+"");
                    } catch (JSONException je) {
                    }

                    retialer_adapter.notifyDataSetChanged();
                    }

        });
    }
    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);;
        formatter .applyPattern("#,###");
        return formatter.format(d);
    }
    public  String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null"))
            return "";
        else
            return json.optString(key, key);
    }

    public class YearArrayAdapter extends ArrayAdapter<String> {

        private final LayoutInflater mInflater;
        private final Context mContext;
        private final ArrayList<CategoryDTO> items;
        private final int mResource;

        public YearArrayAdapter(@NonNull Context context, @LayoutRes int resource,
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

            offTypeTv = (TextView) view.findViewById(R.id.offer_type_txt);

            CategoryDTO offerData = items.get(position);

            offTypeTv.setText(items.get(position).getName());

            return view;
        }
    }

}
