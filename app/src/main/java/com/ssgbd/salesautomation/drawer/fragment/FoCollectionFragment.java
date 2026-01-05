package com.ssgbd.salesautomation.drawer.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.LoginActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.AdapterCollectionList;
import com.ssgbd.salesautomation.adapters.OrderNoRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapterAttendance;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapterCollection;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter_Westage;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.dtos.CollectionListDTO;
import com.ssgbd.salesautomation.dtos.OrderCollectionDTO;
import com.ssgbd.salesautomation.dtos.OrderVisitStatusDTO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FoCollectionFragment extends Fragment implements View.OnClickListener{

    View rootView;
    private Dialog wdialog;
    RouteRecyclerAdapter routeRecyclerAdapter;
    TextView txt_route_list,txt_route_name,txt_retailer_list,txt_retailer_name,txt_order_list,txt_order_info;
    DatabaseHelper databaseHelper;
    AdapterCollectionList collectionList_adapter;
    OrderNoRecyclerAdapter orderNoRecyclerAdapter;
    RetailerRecyclerAdapterCollection retialer_adapter;
    String RETAILER_ID = "";
    String ROUTE_ID = "";
    String ORDER_ID = "";
    EditText edt_collection_amount;
    Button btn_collection;
   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<RetailerDTO> retailerDTOS = new ArrayList<>();
    ArrayList<OrderCollectionDTO> orderColDTOS = new ArrayList<>();
    ArrayList<CollectionListDTO> collectionListDTOS = new ArrayList<>();
    RecyclerView collection_list_recyclerView ;

    public RequestQueue queue;
    String REASONID="";
    String colTypeID="Regular";
    String formattedDate="";
    EditText edt_search_order;
    Button btn_search;
    LinearLayout linlay_depot;
    TextView txt_lastbalance;
    VolleyMethods vm = new VolleyMethods();
    TextView txt_date_name;
    String fDate="";
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private RadioGroup radioGroupCType;
    private RadioButton radio_reguler, radio_credit_note;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fo_collection_fragment, container, false);


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

        Date c = Calendar.getInstance().getTime();
        // System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        edt_collection_amount = (EditText) rootView.findViewById(R.id.edt_collection_amount);
        edt_search_order = (EditText) rootView.findViewById(R.id.edt_search_order);
        btn_search = (Button) rootView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrder(edt_search_order.getText().toString());
              //  Log.e("<<>>",btn_search.getText().toString()+"<l>");
            }
        });

        btn_collection = (Button) rootView.findViewById(R.id.btn_collection);
        btn_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_collection_amount.getText().length()==0){
                    Toast.makeText(getActivity(), " কালেকশনের পরিমান দিতে হবে। ", Toast.LENGTH_SHORT).show();

                    return;
                }
               if (ROUTE_ID.equalsIgnoreCase("")||RETAILER_ID.equalsIgnoreCase("")){
                   Toast.makeText(getActivity(), "রুট ও রিটেইলার উভয় সিলেক্ট করতে হবে এবং কালেকশনের পরিমান দিতে হবে। ", Toast.LENGTH_SHORT).show();
               }else {

                   if(Utility.isInternetAvailable(getActivity())){

//                   if (orderColDTOS.size()==0) {
                       updateCollection();
//                   }else {
//                       if (ORDER_ID.equalsIgnoreCase("")) {
//                           if (SharePreference.getIsDepot(getActivity()).equalsIgnoreCase("Depot")) {
//                               Toast.makeText(getActivity(), "অর্ডার আইডি পাওয়া যাই নাই", Toast.LENGTH_SHORT).show();
//
//                           } else {
//                               updateCollection();
//                           }
//                       } else {
//                           updateCollection();
//                       }
//                   }
                   }else {
       Toast.makeText(getActivity(), " ইন্টারনেট কানেকশন নাই। ", Toast.LENGTH_SHORT).show();

                   }

               }
            }
        });
        initUi();

        return rootView;
    }

    private void initUi() {

//        Log.e("<<>>",colTypeID+"----kkk");
        queue = Volley.newRequestQueue(getActivity());
        txt_route_name = (TextView) rootView.findViewById(R.id.txt_route_name);
        txt_route_list = (TextView) rootView.findViewById(R.id.txt_route_list);
        txt_route_list.setOnClickListener(this);
        txt_retailer_list = (TextView) rootView.findViewById(R.id.txt_retailer_list);
        txt_retailer_list.setOnClickListener(this);
        txt_retailer_name = (TextView) rootView.findViewById(R.id.txt_retailer_name);
        txt_order_list = (TextView) rootView.findViewById(R.id.txt_order_list);
        txt_order_list.setOnClickListener(this);
        txt_order_info = (TextView) rootView.findViewById(R.id.txt_order_info);
        txt_order_info.setOnClickListener(this);
        edt_search_order = (EditText) rootView.findViewById(R.id.edt_search_order);


        radioGroupCType = (RadioGroup) rootView.findViewById(R.id.radioGroupCType);
        final RelativeLayout rlDialogCross;
        radio_reguler = rootView.findViewById(R.id.radio_reguler);
        radio_reguler.setSelected(true);
        radio_credit_note = rootView.findViewById(R.id.radio_credit_note);
        radioGroupCType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radio_reguler) {

                    //  Log.e("<<>>","Regular"+"");
                    colTypeID = "Regular";
                } else if(checkedId == R.id.radio_credit_note) {

                  //   Log.e("<<>>","Credit Note"+"");
                    colTypeID = "Credit Note";
                }
            }

        });

        collection_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.collection_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        collection_list_recyclerView.setLayoutManager(linearLayoutManager1);
        collectionList_adapter = new AdapterCollectionList( collectionListDTOS,getActivity());
        collection_list_recyclerView.setAdapter(collectionList_adapter);
        txt_lastbalance = (TextView) rootView.findViewById(R.id.txt_lastbalance);
        linlay_depot = (LinearLayout) rootView.findViewById(R.id.linlay_depot);

        if (SharePreference.getIsDepot(getActivity()).equalsIgnoreCase("Depot")){
            linlay_depot.setVisibility(View.VISIBLE);
        }

        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int month=cldr.get(Calendar.MONTH);
        int year=cldr.get(Calendar.YEAR);

        fDate= day+"/"+(month+1)+"/"+ year;

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                myCalendar.add(Calendar.DAY_OF_MONTH, -7);
                Date result = myCalendar.getTime();
                view.setMaxDate(System.currentTimeMillis());
                view.setMinDate(result.getTime());

                String s = + dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                txt_date_name.setText("From Date :"+s);

                fDate= dayOfMonth+"/"+(monthOfYear+1)+"/"+ year;

            }
        };

        txt_date_name = (TextView) rootView.findViewById(R.id.txt_date_name);
        txt_date_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        getCollectionList();
    }

    @Override
    public void onPause() {
        super.onPause();
    //    Log.e("onPause","onPause"+"onPause");
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
        tv_dialog_title.setText("Please select ");
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

    //    Log.e("routeDTOS>",Utility.routeDTOS.size()+"");
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        routeRecyclerAdapter = new RouteRecyclerAdapter( Utility.routeDTOS,getActivity());
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                retailerDTOS.clear();
                txt_route_name.setText(Utility.routeDTOS.get(position).getRname());
                getRetailer(Utility.routeDTOS.get(position).getRoute_id());
                ROUTE_ID = Utility.routeDTOS.get(position).getRoute_id();
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

    private void showRetailerListDialog( ) {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_retailer_list_collection);
        final RecyclerView retailer_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final TextView tv_dialog_title;

        final RelativeLayout rlDialogCross;

        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_search);
        retailer_list_recyclerView=(RecyclerView) wdialog.findViewById(R.id.retailer_list_recyclerView);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog);
        tv_dialog_title = (TextView) wdialog.findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText("Please select ");
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

    //    Log.e("routeDTOS>",Utility.routeDTOS.size()+"");
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        retailer_list_recyclerView.setLayoutManager(linearLayoutManager);
        retialer_adapter = new RetailerRecyclerAdapterCollection(retailerDTOS,getActivity());
        retailer_list_recyclerView.setAdapter(retialer_adapter);
        retialer_adapter.notifyDataSetChanged();
        retailer_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                txt_retailer_name.setText( retailerDTOS.get(position).getRetailer_name()+"("+retailerDTOS.get(position).getRetailer_id()+")");
                getOrderList(retailerDTOS.get(position).getRetailer_id());
               // Log.e("<<>>",SharePreference.getUserId(getActivity())+"<<>>"+retailerDTOS.get(position).getRetailer_id()+"");
                RETAILER_ID = retailerDTOS.get(position).getRetailer_id();
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
                retialer_adapter.filter(query);
            }
        });

        wdialog.show();
    }

    private void showOrderListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_retailer_list_collection);
        final RecyclerView retailer_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final TextView tv_dialog_title;

        final RelativeLayout rlDialogCross;

        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_search);
        retailer_list_recyclerView=(RecyclerView) wdialog.findViewById(R.id.retailer_list_recyclerView);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog);
        tv_dialog_title = (TextView) wdialog.findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText("Please select");
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

        //    Log.e("routeDTOS>",Utility.routeDTOS.size()+"");
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        retailer_list_recyclerView.setLayoutManager(linearLayoutManager);
        orderNoRecyclerAdapter = new OrderNoRecyclerAdapter(orderColDTOS,getActivity());
        retailer_list_recyclerView.setAdapter(orderNoRecyclerAdapter);
        orderNoRecyclerAdapter.notifyDataSetChanged();
        retailer_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                txt_order_info.setText( "Order No.:"+orderColDTOS.get(position).getOrder_no()+"\n"+"IMS: "+orderColDTOS.get(position).getTotal_value()+"; Dues: "+orderColDTOS.get(position).getDue_amount());
                ORDER_ID = orderColDTOS.get(position).getOrder_id();
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
                orderNoRecyclerAdapter.filter(query);
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
                case R.id.txt_retailer_list:
                    if (ROUTE_ID.equalsIgnoreCase("")){
                        Toast.makeText(getActivity(), "আগে রুট সিলেক্ট করুন। ", Toast.LENGTH_SHORT).show();
                    }else {
                        showRetailerListDialog();

                    }
                break;
                case R.id.txt_order_list:

                    if (RETAILER_ID.equalsIgnoreCase("")){
                        Toast.makeText(getActivity(), "আগে রিটেলার সিলেক্ট করুন। ", Toast.LENGTH_SHORT).show();
                    }else {
                        showOrderListDialog();

                    }

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
                                        retailerDTO.setPoint_id( routeObject.getString("point_id"));
                                        retailerDTO.setRouteId( routeObject.getString("rid"));
                                        retailerDTO.setShopeType( routeObject.getString("shop_type"));
                                        retailerDTO.setRetailerOwner( routeObject.getString("owner"));
                                        retailerDTO.setRetailerMobileNo( routeObject.getString("mobile"));
                                        retailerDTO.setTnt( routeObject.getString("tnt"));
                                        retailerDTO.setEmail( routeObject.getString("email"));
                                        retailerDTO.setDateandtime( routeObject.getString("dateandtime"));
                                        retailerDTO.setUser( routeObject.getString("user"));
//                                      retailerDTO.setStatus( routeObject.getString("status"));
                                        retailerDTO.setDob( routeObject.getString("dob"));
                                        retailerDTO.setvAddress( routeObject.getString("vAddress"));
                                        retailerDTO.setGlobal_company_id( routeObject.getString("global_company_id"));
                                        retailerDTO.setInactive_user( routeObject.getString("inactive_user"));
                                        retailerDTO.setInactive_date_time( routeObject.getString("inactive_date_time"));
                                        retailerDTO.setInactive_ip( routeObject.getString("inactive_ip"));
                                        retailerDTO.setiApproval( routeObject.getString("iApproval"));
                                        retailerDTO.setReminding_commission_balance( routeObject.getString("reminding_commission_balance"));
                                        retailerDTO.setOpening_balance( routeObject.getString("opening_balance"));
                                        retailerDTO.setOpening_balance_accessories( routeObject.getString("opening_balance_accessories"));
                                        retailerDTO.setSerial( optStringNullCheckZero(routeObject,"serial"));

                                        retailerDTO.setAfter_retailers( routeObject.getString("after_retailers"));
                                        retailerDTO.setLat( routeObject.getString("lat"));
                                        retailerDTO.setLon( routeObject.getString("lon"));
                                        retailerDTO.setSync( routeObject.getString("sync"));
                                        retailerDTO.setReminder_start( routeObject.getString("reminder_start"));
                                        retailerDTO.setReminder_end( routeObject.getString("reminder_end"));
                                        retailerDTO.setAddedOrUpdateType( routeObject.getString("addedOrUpdateType"));
                                        retailerDTO.setAddedOrUpdateDate( routeObject.getString("addedOrUpdateDate"));
                                        retailerDTOS.add(retailerDTO);

                                    }

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
            return "0";
        else
            return json.optString(key, key);
    }

    public void getOrderList(String retId) {
        orderColDTOS.clear();

      //  Log.e("<<>>","api/fo-order-list?fo_id="+SharePreference.getUserId(getActivity())+"&retailer_id="+retId+"");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.base_url) + "api/fo-order-list?fo_id="+SharePreference.getUserId(getActivity())+"&retailer_id="+retId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respjsonObj = new JSONObject(response);
                  //  Log.e("<<>>",response+"");

                    txt_lastbalance.setText("Last Balance :"+" "+respjsonObj.getString("retailer_balance"));

                    if (respjsonObj.getString("status").equalsIgnoreCase("success")) {
                JSONArray routeArray = respjsonObj.getJSONArray("data");
                for (int i = 0; i < routeArray.length(); i++) {
                    JSONObject routeObject = routeArray.getJSONObject(i);
                    OrderCollectionDTO orderDTOS = new OrderCollectionDTO();
                    orderDTOS.setOrder_id(routeObject.getString("order_id"));
                    orderDTOS.setOrder_no(routeObject.getString("order_no"));
                    orderDTOS.setOrder_type(routeObject.getString("order_type"));
                    orderDTOS.setOrder_date(routeObject.getString("order_date"));
                    orderDTOS.setDistributor_id(routeObject.getString("distributor_id"));
                    orderDTOS.setPoint_id(routeObject.getString("point_id"));
                    orderDTOS.setRoute_id(routeObject.getString("route_id"));
                    orderDTOS.setTotal_value(routeObject.getString("total_value"));
                    orderDTOS.setTotal_delivery_value(routeObject.getString("total_delivery_value"));
                    orderDTOS.setGrand_total_value(routeObject.getString("grand_total_value"));
                    orderDTOS.setTotal_discount_amount(routeObject.getString("total_discount_amount"));
                    orderDTOS.setDue_amount(routeObject.getString("due_amount"));

                    orderColDTOS.add(orderDTOS);
                }
            }

//                    String status = respjsonObj.getString("status");
//                    if (status.equals("1")) {
//                    }else {

//                    }

                } catch (JSONException e) {

                    //   Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                    // Log.e("<<>>>>",e.toString()+"<<<<");

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
                //  Toast.makeText(context, "SSL server error.", Toast.LENGTH_SHORT).show();

                Log.e("<<>>>>",error.toString()+"<<<<");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
    }

    public void getOrder(String orderNo) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.base_url) + "api/order-check?retailer_id="+RETAILER_ID+"&order_code="+orderNo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.e("<<>>",RETAILER_ID+"--"+orderNo+"");

                try {
                    JSONObject respjsonObj = new JSONObject(response);

                   // Log.e("<<>>",respjsonObj+"");
            if (respjsonObj.getString("status").equalsIgnoreCase("success")) {
                JSONObject routeObject = respjsonObj.getJSONObject("data");


                OrderCollectionDTO orderDTOS = new OrderCollectionDTO();
                orderDTOS.setOrder_id(routeObject.getString("order_id"));
                ORDER_ID = routeObject.getString("order_id");
                orderDTOS.setOrder_no(routeObject.getString("order_no"));
                orderDTOS.setOrder_type(routeObject.getString("order_type"));
                orderDTOS.setOrder_date(routeObject.getString("order_date"));
                orderDTOS.setDistributor_id(routeObject.getString("distributor_id"));
                orderDTOS.setPoint_id(routeObject.getString("point_id"));
                orderDTOS.setRoute_id(routeObject.getString("route_id"));
                ROUTE_ID = routeObject.getString("route_id");
                orderDTOS.setTotal_value(routeObject.getString("total_value"));
                orderDTOS.setTotal_delivery_value(routeObject.getString("total_delivery_value"));
                orderDTOS.setGrand_total_value(routeObject.getString("grand_total_value"));
                orderDTOS.setTotal_discount_amount(routeObject.getString("total_discount_amount"));
                txt_order_info.setText( "Order No. :"+routeObject.getString("order_no")+"\n"+"Total Value: "+routeObject.getString("total_value"));


            }else {
               // Toast.makeText(getActivity(), "অর্ডার পাওয়া যাইনাই।", Toast.LENGTH_SHORT).show();
            }

                } catch (JSONException e) {

                    //   Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
                //  Toast.makeText(context, "SSL server error.", Toast.LENGTH_SHORT).show();

                Log.e("<<>>",error.toString()+"<<<<");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
    }

    public void updateCollection(){
        btn_collection.setVisibility(View.GONE);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("লোডিং ...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer(getActivity(), getResources().getString(R.string.base_url)+"api/order-collection",
                jp.jsonFoCollection(RETAILER_ID,ROUTE_ID,SharePreference.getUserPointId(getActivity()),edt_collection_amount.getText().toString(),SharePreference.getUserId(getActivity()),ORDER_ID,colTypeID), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {
                      //  Log.e("<<>>",result+"");
                        try {
                            JSONObject jsonObject = new JSONObject(result);

                          if (jsonObject.getString("status").equalsIgnoreCase("success")){

                              Toast.makeText(getActivity(), "আপনার কালেকশন টি অনুমোদনের জন্য গ্রহণ করা হয়েছে। ", Toast.LENGTH_SHORT).show();

                              txt_lastbalance.setText("");
                              txt_route_name.setText("");
                              txt_retailer_name.setText("");
                              ROUTE_ID="";RETAILER_ID="";ORDER_ID="";
                              edt_collection_amount.setText("");
                              edt_search_order.setText("");
                              txt_order_info.setText("");
                              btn_collection.setVisibility(View.VISIBLE);
                              getCollectionList();
                          }else {
                              Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                              btn_collection.setVisibility(View.VISIBLE);
                          }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pd.dismiss();
                    }
                });
    }

    public void getCollectionList() {
        collectionListDTOS.clear();

      //  Log.e("<<>>",SharePreference.getUserId(getActivity())+"");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.base_url) + "api/order-collection-list?type=0&fo_id="+SharePreference.getUserId(getActivity()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

             //   Log.e("<<>>",response+"");
                try {

                        JSONObject respjsonObj = new JSONObject(response);

                        if (respjsonObj.getString("status").equalsIgnoreCase("success")) {
                            JSONArray routeArray = respjsonObj.getJSONArray("data");
                            for (int i = 0; i < routeArray.length(); i++) {
                                JSONObject routeObject = routeArray.getJSONObject(i);
                                CollectionListDTO collectionListDTO = new CollectionListDTO();

                                collectionListDTO.setPoint_id(routeObject.getString("point_id"));
                                collectionListDTO.setOrder_id(routeObject.getString("order_id"));
                                collectionListDTO.setRoute_id(routeObject.getString("route_id"));
                                collectionListDTO.setRoute_name(routeObject.getString("route_name"));
                                collectionListDTO.setRetailer_id(routeObject.getString("retailer_id"));
                                collectionListDTO.setRetailer_name(routeObject.getString("retailer_name"));
                                collectionListDTO.setCollection_amount(routeObject.getString("collection_amount"));
                                collectionListDTO.setCollectionType(routeObject.getString("collection_type"));
                                collectionListDTO.setStatus(routeObject.getString("status"));

                                collectionListDTOS.add(collectionListDTO);
                            }
                        }

            collectionList_adapter.notifyDataSetChanged();


                } catch (JSONException e) {

                    //   Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
                //  Toast.makeText(context, "SSL server error.", Toast.LENGTH_SHORT).show();

                Log.e("<<>>>>",error.toString()+"<<<<");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
    }


    public void sendSMS(String mobileNo,String text) {

       // Log.e("<<>>",getResources().getString(R.string.base_url)+"apiKey="+getResources().getString(R.string.sms_api_key)+"&senderId="+getResources().getString(R.string.sms_sender_id)+"&transactionType=T&campaignId="+getResources().getString(R.string.campaign_id)+"&mobileNo="+mobileNo+"&message="+text+"");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.base_url)+"apiKey="+getResources().getString(R.string.sms_api_key)+"&senderId="+getResources().getString(R.string.sms_sender_id)+"&transactionType=T&campaignId="+getResources().getString(R.string.campaign_id)+"&mobileNo="+mobileNo+"&message="+text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject respjsonObj = new JSONObject(response);

                    if (respjsonObj.getString("status").equalsIgnoreCase("success")) {
                        JSONArray routeArray = respjsonObj.getJSONArray("data");
                        for (int i = 0; i < routeArray.length(); i++) {
                            JSONObject routeObject = routeArray.getJSONObject(i);
                            CollectionListDTO collectionListDTO = new CollectionListDTO();

                            collectionListDTO.setPoint_id(routeObject.getString("point_id"));
                            collectionListDTO.setOrder_id(routeObject.getString("order_id"));
                            collectionListDTO.setRoute_id(routeObject.getString("route_id"));
                            collectionListDTO.setRoute_name(routeObject.getString("route_name"));
                            collectionListDTO.setRetailer_id(routeObject.getString("retailer_id"));
                            collectionListDTO.setRetailer_name(routeObject.getString("retailer_name"));
                            collectionListDTO.setCollection_amount(routeObject.getString("collection_amount"));
                            collectionListDTO.setStatus(routeObject.getString("status"));

                            collectionListDTOS.add(collectionListDTO);
                        }
                    }

                    collectionList_adapter.notifyDataSetChanged();


                } catch (JSONException e) {

                    //   Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
                //  Toast.makeText(context, "SSL server error.", Toast.LENGTH_SHORT).show();

                Log.e("<<>>>>",error.toString()+"<<<<");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
    }
}
