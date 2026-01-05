package com.ssgbd.salesautomation.report.order;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.ssgbd.salesautomation.adapters.OrderReportListAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;


public class PGWiseReportFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    String ROUTE_ID="";
//    private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    ArrayList<OrderRepotListDTO> orderRepotListDTOS = new ArrayList<>();
    TextView txt_fromdate,txt_todate,txt_category_list,txt_search;
    String formattedDate="";
    DatePickerDialog picker;
    String FROMDATE="",TODATE="";
    DatabaseHelper databaseHelper;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ProductCategoryRecyclerAdapter routeRecyclerAdapter;
    private Dialog wdialog;
    String CATEGORY_ID="";
    private WebView webView;
    LinearLayout linlay_exit_from_web;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.pg_wise_report_fragment, container, false);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
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


        categoryDTOS = databaseHelper.getAllCategoryProductReport(databaseHelper,SharePreference.getUserBusinessType(getActivity()));

        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int month=cldr.get(Calendar.MONTH);
        int year=cldr.get(Calendar.YEAR);

     //   Log.e("dmy>",year+"-"+(month+1)+"-"+day+"");

        String d=year+"-"+(month+1)+"-"+day;


        txt_fromdate = (TextView) rootView.findViewById(R.id.txt_fromdate);
        String monthname=(String)android.text.format.DateFormat.format("MMMM", new Date());
        txt_fromdate.setText("1"+" "+monthname+" "+String.valueOf(year) );
        FROMDATE = "1"+" "+monthname+" "+String.valueOf(year);
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
//                              txt_month_year.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
        TODATE = day+" "+monthname+" "+String.valueOf(year);
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
            }
        });

        txt_category_list = (TextView) rootView.findViewById(R.id.txt_category_list);
        txt_category_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryListDialog();
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

                    if (!Utility.isInternetAvailable(getActivity())) {
                        Utility.internetAlert(getActivity());

                    } else {

                  getReport(FROMDATE,TODATE);

                    }
                }
           }
        });
        getReport(txt_fromdate.getText().toString(),txt_todate.getText().toString());
        return rootView;
    }
 public void getReport(String fromDate,String toDate){
     Log.e("<<>>","url"+"<>");
     WebSettings webSettings = webView.getSettings();
     webSettings.setJavaScriptEnabled(true);
     Toast.makeText(getActivity(), "Please wait processing your request....", Toast.LENGTH_LONG).show();
     webView.setWebViewClient(new MyBrowser());
     String url = getString(R.string.base_url) + "report/pg-wastage?login_user_id=" + SharePreference.getUserLoginId(getActivity()) + "&login_password=" + SharePreference.getUserLoginPassword(getActivity()) + "&userid=" + SharePreference.getUserId(getActivity()) + "&globalid=" + SharePreference.getUserGlobalId(getActivity()) + "&fromdate=" + fromDate + "&todate=" + toDate + "&business_id=" + SharePreference.getUserBusinessType(getActivity()) + "&categoryid=" + CATEGORY_ID;
     webView.loadUrl(url);
   //  Log.e("<<>>",url+"<>");

 }
    private void showCategoryListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final TextView tttt;

        final RelativeLayout rlDialogCross;
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

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        routeRecyclerAdapter = new ProductCategoryRecyclerAdapter( categoryDTOS,getActivity());
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            CATEGORY_ID=categoryDTOS.get(position).getId();
                txt_category_list.setText(categoryDTOS.get(position).getName());

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
}
