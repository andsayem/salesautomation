package com.ssgbd.salesautomation.report.retailerledger;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.MyBrowser;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapterAttendance;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.OrderRepotListDTO;
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


public class RetailerLedgerDetailsReportFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    String ROUTE_ID="";
//    private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    ArrayList<OrderRepotListDTO> orderRepotListDTOS = new ArrayList<>();
    TextView txt_fromdate,txt_todate,txt_route_list,txt_retailer_list,txt_search;
    String formattedDate="";
    DatePickerDialog picker;
    String FROMDATE="",TODATE="";
    DatabaseHelper databaseHelper;
    RouteRecyclerAdapter routeRecyclerAdapter;
    ArrayList<RetailerDTO> retailerDTOS = new ArrayList<>();

    RetailerRecyclerAdapterAttendance retialer_adapter;
    private Dialog wdialog,wdialogRetailer;
    String ROUTEID="",RETAILERID="";
    private WebView webView;
    LinearLayout linlay_exit_from_web;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.ladger_details_report_fragment, container, false);
        Date c = Calendar.getInstance().getTime();
       // System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
        databaseHelper = new DatabaseHelper(getActivity());
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myPath = DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()){
            databaseHelper.openDataBase();}


        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int month=cldr.get(Calendar.MONTH);
        int year=cldr.get(Calendar.YEAR);

     //   Log.e("dmy>",year+"-"+(month+1)+"-"+day+"");

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
        txt_route_list = (TextView) rootView.findViewById(R.id.txt_route_list);
        txt_route_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRouteListDialog();
            }
        });
        txt_retailer_list = (TextView) rootView.findViewById(R.id.txt_retailer_list);
        txt_retailer_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRetailerListDialog();
            }
        });
        webView = (WebView) rootView.findViewById(R.id.webViewID);
        txt_search = (TextView) rootView.findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (FROMDATE.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please select date range.", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true); // javascript enable

                    webView.setWebViewClient(new MyBrowser()); // for open android all page same android mobile
                    String url = getString(R.string.base_url) + "report/ledger-details?login_user_id=" + SharePreference.getUserLoginId(getActivity()) + "&login_password=" + SharePreference.getUserLoginPassword(getActivity()) + "&userid=" + SharePreference.getUserId(getActivity()) + "&globalid=" + SharePreference.getUserGlobalId(getActivity()) + "&fromdate=" + FROMDATE + "&todate=" + TODATE + "&business_id=" + SharePreference.getUserBusinessType(getActivity()) + "&retailer_id=" + RETAILERID;

                  //  Log.e("url>",url+"<<");

                    if (!Utility.isInternetAvailable(getActivity())) {
                        Utility.internetAlert(getActivity());

                    } else {
                        Toast.makeText(getActivity(), "আপনার রিপোর্ট লোড হচ্ছে। অপেক্ষা করুন।", Toast.LENGTH_LONG).show();

                        webView.loadUrl(url);
                    }

                }

           }
        });


        return rootView;
    }

    private void showRouteListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView,retailer_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        retailerDTOS.clear();
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

     //   Log.e("routeDTOS>",Utility.routeDTOS.size()+"");
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        routeRecyclerAdapter = new RouteRecyclerAdapter( Utility.routeDTOS,getActivity());
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

//        LinearLayoutManager linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//
//
//        retailer_list_recyclerView.setLayoutManager(linearLayoutManager);
//        retialer_adapter = new RetailerRecyclerAdapterAttendance(retailerDTOS,getActivity());
//        retailer_list_recyclerView.setAdapter(retialer_adapter);


        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


           //     retailerDTOS = databaseHelper.getRetailerList(databaseHelper,Utility.routeDTOS.get(position).getRoute_id());

                getRetailer(Utility.routeDTOS.get(position).getRoute_id());


                ROUTEID=Utility.routeDTOS.get(position).getRoute_id();


                txt_route_list.setText(Utility.routeDTOS.get(position).getRname());
                txt_route_list.setTextColor(Color.parseColor("#636262"));
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


    private void showRetailerListDialog() {
        wdialogRetailer =new Dialog(getActivity());
        wdialogRetailer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialogRetailer.setContentView(R.layout.dialog_retailer_list);
        final RecyclerView retailer_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;

        final RelativeLayout rlDialogCross;
//        retailerDTOS.clear();
//        retailerDTOS = databaseHelper.getRetailerList(databaseHelper,ROUTEID);


        etSearch = (EditText)wdialogRetailer.findViewById(R.id.edt_txt_search);
        retailer_list_recyclerView=(RecyclerView) wdialogRetailer.findViewById(R.id.retailer_list_recyclerView);
        imbtnCross=(ImageView)wdialogRetailer.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialogRetailer.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialogRetailer.findViewById(R.id.btnDoneDialog);

        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wdialogRetailer.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialogRetailer.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialogRetailer.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        retailer_list_recyclerView.setLayoutManager(linearLayoutManager);
        retialer_adapter = new RetailerRecyclerAdapterAttendance(retailerDTOS,getActivity());
        retailer_list_recyclerView.setAdapter(retialer_adapter);

        retailer_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                txt_retailer_list.setText(retailerDTOS.get(position).getRetailer_name());
                RETAILERID = retailerDTOS.get(position).getRetailer_id();
                txt_retailer_list.setTextColor(Color.parseColor("#636262"));

                wdialogRetailer.dismiss();
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

        wdialogRetailer.show();
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
                                    // Log.e("<<>>",routeArray+"");
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
//                                        retailerDTO.setStatus( routeObject.getString("status"));
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

                                 //   retialer_adapter.notifyDataSetChanged();

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

}
