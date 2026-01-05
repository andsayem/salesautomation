package com.ssgbd.salesautomation.drawer.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;


import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.CommonStatusCodes;
//import com.google.android.gms.vision.barcode.Barcode;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ForecastEntryListAdapter;
import com.ssgbd.salesautomation.adapters.PGQTYListAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.drawer.dashboard.DashboardTodaysVisitActivity;
import com.ssgbd.salesautomation.dtos.ForecastEntryListDTO;
import com.ssgbd.salesautomation.dtos.NoticeDTO;
import com.ssgbd.salesautomation.dtos.PGPercentageDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.report.order.ReportWeb;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment implements View.OnClickListener {

    View rootView;
    CardView card_view2;
    RequestQueue queue;

    TextView txt_performance,txt_score,txt_new_order, txt_attendance, txt_visit, txt_monthly_target, txt_monthly_strike_rate, txt_monthly_achivement, txt_today_target, txt_today_achivement, txt_today_strike_rate, txt_total_Retailer;
    //GLF CLF LED
    TextView txt_glf_target, txt_glf_achivement, txt_glf_strike_rate;
    TextView txt_clf_target, txt_clf_achivement, txt_clf_strike_rate;
    TextView txt_led_target, txt_led_achivement, txt_led_strike_rate;
    VolleyMethods vm = new VolleyMethods();
    TextView txt_target_achivement,txt_target_achivement_1;
  //  private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    DatabaseHelper databaseHelper;

    // new dashboard
    TextView txt_today_visits, txt_today_orders, txt_total_attendance, txt_total_retailers;
    TextView txt_today_qty, txt_today_value, txt_today_achievement_qty, txt_today_achievement_value, txt_today_achievement_percent_qty,
            txt_today_achievement_percent_value;
    TextView txt_monthly_target_qty, txt_monthly_target_value, txt_monthly_achievement_qty, txt_monthly_achievement_value,
            txt_monthly_achievement_percent_qty, txt_monthly_achievement_percent_value;

    TextView txt_gls_target_qty, txt_gls_target_value, txt_gls_achievement_qty,
            txt_gls_achievement_value, txt_gls_achievement_percent_qty,
            txt_gls_achievement_percent_value,txt_valuHeader,txt_monthlyvaluheder;

    TextView text_marque,txt_led_target_qty, txt_led_target_value, txt_led_achievement_qty,
            txt_led_achievement_value, txt_led_achievement_percent_qty,
            txt_led_achievement_percent_value,text_achivement,text_achivementvalu,txt_total_qty,txt_imsqyt,txt_imsper;

    Button button;
    LinearLayout linlay_new_dashboard, linlay_old;
    LinearLayout linlay_today_visit,linlay_qr_scan;
    PGQTYListAdapter pgqtyListAdapter;
    RecyclerView product_list_recyclerView;
    ArrayList<PGPercentageDTO> stockDTOS = new ArrayList<>();


    TextView txt_not,txt_date,txt_amount,txt_ims_percentage,txt_collection_percentage,fan_text_achivement;
    ImageView imageview,imag_cong;
    private Bitmap bitmap;
    ProgressBar ProgressBar_ims,ProgressBar_collection;
    RelativeLayout rel_lay_con;
    private final Handler handler = new Handler();
    CardView card_view_incentive;
    Button btn_pgwisereport;
    LinearLayout linlay_pgwisereport;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.home_fragment, container, false);

        queue = Volley.newRequestQueue(getActivity());

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        product_list_recyclerView=(RecyclerView) rootView.findViewById(R.id.product_list_recyclerView);

        product_list_recyclerView.setLayoutManager(linearLayoutManager);
        pgqtyListAdapter = new PGQTYListAdapter( stockDTOS,getActivity());
        product_list_recyclerView.setAdapter(pgqtyListAdapter);
        product_list_recyclerView.setNestedScrollingEnabled(false);

        card_view2 = (CardView) rootView.findViewById(R.id.card_view2);
        card_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        initUI(rootView);

        linlay_new_dashboard = (LinearLayout) rootView.findViewById(R.id.linlay_new_dashboard);
        rel_lay_con = (RelativeLayout) rootView.findViewById(R.id.rel_lay_con);
        linlay_old = (LinearLayout) rootView.findViewById(R.id.linlay_old);
        button = (Button) rootView.findViewById(R.id.btn_switch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linlay_new_dashboard.getVisibility() == View.VISIBLE) {
                    linlay_new_dashboard.setVisibility(View.GONE);
                    linlay_old.setVisibility(View.VISIBLE);

                } else {
                    linlay_new_dashboard.setVisibility(View.VISIBLE);
                    linlay_old.setVisibility(View.GONE);
                }
            }
        });

        if (!isInternetAvailable(getActivity())) {

            Toast.makeText(getActivity(), "No Internet you are offline.", Toast.LENGTH_SHORT).show();
        } else {
            getDashboardData();
        }

        imageview = (ImageView) rootView.findViewById(R.id.imageview);
        imag_cong = (ImageView) rootView.findViewById(R.id.imag_cong);

        ProgressBar_ims = (ProgressBar) rootView.findViewById(R.id.ProgressBar_ims);

        ProgressBar_collection = (ProgressBar) rootView.findViewById(R.id.ProgressBar_collection);


        final Calendar cldr=Calendar.getInstance();
        int year=cldr.get(Calendar.YEAR);
        int month1=cldr.get(Calendar.MONTH);
        String month="";
      //  Log.e("<<>>>>",month1+"");
        if((month1+1)<10){
            month="0"+String.valueOf(month1+1);
        }else {
            month=String.valueOf(month1+1);
        }

        int day=cldr.get(Calendar.DAY_OF_MONTH);
        String monthname=(String)android.text.format.DateFormat.format("MMMM", new Date());
        txt_amount = (TextView) rootView.findViewById(R.id.txt_amount);
        txt_amount.setOnClickListener(this);

        txt_ims_percentage = (TextView) rootView.findViewById(R.id.txt_ims_percentage);
        txt_collection_percentage = (TextView) rootView.findViewById(R.id.txt_collection_percentage);
        fan_text_achivement = (TextView) rootView.findViewById(R.id.fan_text_achivement);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        txt_date.setText("Incentive as of "+monthname+", "+String.valueOf(year) );


        Glide.with(getActivity())
                .load(R.raw.incentive_run3)
                .into(imageview);

        Glide.with(getActivity())
                .load(R.raw.con_1)
                .into(imag_cong);

        card_view_incentive = (CardView) rootView.findViewById(R.id.card_view_incentive);

       // Log.e("<<>>>>",month+"");

        // comment for stakeholder requirment /// stop showing incentive
     //   getFoIncentiveStatus(String.valueOf(year),String.valueOf(month));

        return rootView;
    }
    private final Runnable startRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            rel_lay_con.setVisibility(View.GONE);

        }
    };
    private void initUI(View rootView) {

        txt_not = (TextView) rootView.findViewById(R.id.txt_not);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(100); //You can manage the blinking time with this parameter
        anim.setStartOffset(40);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txt_not.startAnimation(anim);

        linlay_today_visit = (LinearLayout) rootView.findViewById(R.id.linlay_today_visit);
        linlay_today_visit.setOnClickListener(this);

        linlay_qr_scan = (LinearLayout) rootView.findViewById(R.id.linlay_qr_scan);
        linlay_qr_scan.setOnClickListener(this);

        btn_pgwisereport = (Button) rootView.findViewById(R.id.btn_pgwisereport);
        btn_pgwisereport.setOnClickListener(this);

        linlay_pgwisereport = (LinearLayout) rootView.findViewById(R.id.linlay_pgwisereport);


        txt_total_qty = (TextView) rootView.findViewById(R.id.txt_total_qty);
        txt_imsqyt = (TextView) rootView.findViewById(R.id.txt_imsqyt);
        txt_imsper = (TextView) rootView.findViewById(R.id.txt_imsper);
        txt_valuHeader = (TextView) rootView.findViewById(R.id.txt_valuHeader);
        txt_monthlyvaluheder = (TextView) rootView.findViewById(R.id.txt_monthlyvaluheder);
        text_achivement = (TextView) rootView.findViewById(R.id.text_achivement);
        text_achivementvalu = (TextView) rootView.findViewById(R.id.text_achivementvalu);
        txt_performance = (TextView) rootView.findViewById(R.id.txt_performance);
        txt_score = (TextView) rootView.findViewById(R.id.txt_score);
        txt_new_order = (TextView) rootView.findViewById(R.id.txt_new_order);
        txt_attendance = (TextView) rootView.findViewById(R.id.txt_attendance);
        txt_visit = (TextView) rootView.findViewById(R.id.txt_visit);
        txt_monthly_target = (TextView) rootView.findViewById(R.id.txt_monthly_target);
        txt_monthly_achivement = (TextView) rootView.findViewById(R.id.txt_monthly_achivement);
        txt_monthly_strike_rate = (TextView) rootView.findViewById(R.id.txt_monthly_strike_rate);
        txt_today_target = (TextView) rootView.findViewById(R.id.txt_today_target);
        txt_today_achivement = (TextView) rootView.findViewById(R.id.txt_today_achivement);
        txt_today_strike_rate = (TextView) rootView.findViewById(R.id.txt_today_strike_rate);
        txt_total_Retailer = (TextView) rootView.findViewById(R.id.txt_total_Retailer);

        txt_target_achivement = (TextView) rootView.findViewById(R.id.txt_target_achivement);
        txt_target_achivement_1 = (TextView) rootView.findViewById(R.id.txt_target_achivement_1);


        txt_glf_target = (TextView) rootView.findViewById(R.id.txt_glf_target);
        txt_glf_achivement = (TextView) rootView.findViewById(R.id.txt_glf_achivement);
        txt_glf_strike_rate = (TextView) rootView.findViewById(R.id.txt_glf_strike_rate);
        txt_clf_target = (TextView) rootView.findViewById(R.id.txt_clf_target);
        txt_clf_achivement = (TextView) rootView.findViewById(R.id.txt_clf_achivement);
        txt_clf_strike_rate = (TextView) rootView.findViewById(R.id.txt_clf_strike_rate);
        txt_led_target = (TextView) rootView.findViewById(R.id.txt_led_target);
        txt_led_achivement = (TextView) rootView.findViewById(R.id.txt_led_achivement);
        txt_led_strike_rate = (TextView) rootView.findViewById(R.id.txt_led_strike_rate);


        databaseHelper = new DatabaseHelper(getActivity());
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myPath = DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()) {
            databaseHelper.openDataBase();
        }

        if (!isInternetAvailable(getActivity())) {

        } else {
//            syncProduct();
//            syncProductCategory();
        }
        // txt_today_visits,txt_today_orders,txt_total_attendance,txt_total_retailers;

        txt_today_visits = (TextView) rootView.findViewById(R.id.txt_today_visits);
        txt_today_orders = (TextView) rootView.findViewById(R.id.txt_today_orders);
        txt_total_attendance = (TextView) rootView.findViewById(R.id.txt_total_attendance);
        txt_total_retailers = (TextView) rootView.findViewById(R.id.txt_total_retailers);

        //txt_today_qty,txt_today_value,txt_today_achievement_qty,txt_today_achievement_value,txt_today_achievement_percent_qty,txt_today_achievement_percent_value;
        txt_today_qty = (TextView) rootView.findViewById(R.id.txt_today_qty);
        txt_today_value = (TextView) rootView.findViewById(R.id.txt_today_value);
        txt_today_achievement_qty = (TextView) rootView.findViewById(R.id.txt_today_achievement_qty);
        txt_today_achievement_value = (TextView) rootView.findViewById(R.id.txt_today_achievement_value);
        txt_today_achievement_percent_qty = (TextView) rootView.findViewById(R.id.txt_today_achievement_percent_qty);
        txt_today_achievement_percent_value = (TextView) rootView.findViewById(R.id.txt_today_achievement_percent_value);


        //txt_monthly_target_qty,txt_monthly_target_value,txt_monthly_achievement_qty,txt_monthly_achievement_value,
        //  txt_monthly_achievement_percent_qty,txt_monthly_achievement_percent_value;
        txt_monthly_target_qty = (TextView) rootView.findViewById(R.id.txt_monthly_target_qty);
        txt_monthly_target_value = (TextView) rootView.findViewById(R.id.txt_monthly_target_value);
        txt_monthly_achievement_qty = (TextView) rootView.findViewById(R.id.txt_monthly_achievement_qty);
        txt_monthly_achievement_value = (TextView) rootView.findViewById(R.id.txt_monthly_achievement_value);
        txt_monthly_achievement_percent_qty = (TextView) rootView.findViewById(R.id.txt_monthly_achievement_percent_qty);
        txt_monthly_achievement_percent_value = (TextView) rootView.findViewById(R.id.txt_monthly_achievement_percent_value);


        //txt_gls_target_qty,txt_gls_target_value,txt_gls_achievement_qty,
        //txt_gls_achievement_value,txt_gls_achievement_percent_qty,
        //txt_gls_achievement_percent_value;


        txt_gls_target_qty = (TextView) rootView.findViewById(R.id.txt_gls_target_qty);
        txt_gls_target_value = (TextView) rootView.findViewById(R.id.txt_gls_target_value);
        txt_gls_achievement_qty = (TextView) rootView.findViewById(R.id.txt_gls_achievement_qty);
        txt_gls_achievement_value = (TextView) rootView.findViewById(R.id.txt_gls_achievement_value);
        txt_gls_achievement_percent_qty = (TextView) rootView.findViewById(R.id.txt_gls_achievement_percent_qty);
        txt_gls_achievement_percent_value = (TextView) rootView.findViewById(R.id.txt_gls_achievement_percent_value);

        //txt_led_target_qty,txt_led_target_value,txt_led_achievement_qty,
        //txt_led_achievement_value,txt_led_achievement_percent_qty,
        //txt_led_achievement_percent_value;
        text_marque = (TextView) rootView.findViewById(R.id.text_marque);
        text_marque.setSelected(true);
        txt_led_target_qty = (TextView) rootView.findViewById(R.id.txt_led_target_qty);
        txt_led_target_value = (TextView) rootView.findViewById(R.id.txt_led_target_value);
        txt_led_achievement_qty = (TextView) rootView.findViewById(R.id.txt_led_achievement_qty);
        txt_led_achievement_value = (TextView) rootView.findViewById(R.id.txt_led_achievement_value);
        txt_led_achievement_percent_qty = (TextView) rootView.findViewById(R.id.txt_led_achievement_percent_qty);
        txt_led_achievement_percent_value = (TextView) rootView.findViewById(R.id.txt_led_achievement_percent_value);
        getNoticeList();

        if (SharePreference.getUserBusinessType(getActivity()).equalsIgnoreCase("1")) {
            txt_target_achivement.setText("GLS");
                    txt_target_achivement_1.setText("LED");
        }else {
            txt_target_achivement.setText("PC");
            txt_target_achivement_1.setText("PC Others");
        }

        Calendar calendar = Calendar.getInstance();

        String this_month = String.valueOf(calendar.get(Calendar.MONTH));

        if((calendar.get(Calendar.MONTH)+1 )<10){
            this_month= "0"+(calendar.get(Calendar.MONTH)+1);
        }else {
            this_month= String.valueOf(calendar.get(Calendar.MONTH)+1);
        }


//        if((calendar.get(Calendar.MONTH) + 1)<10){
//            Month_this = "0"+day;
//        }

      //  Log.e(">bb>>>",this_month+"<<>>");

 getFoPerformanceInfo("api/fo-performance-mark?foid="+SharePreference.getUserId(getActivity())+"&year="+String.valueOf(calendar.get(Calendar.YEAR))+"&month="+ this_month);

  if (SharePreference.getIsvaluhide(getActivity()).equalsIgnoreCase("true")){


  }else {
      txt_valuHeader.setVisibility(View.VISIBLE);
      txt_today_value.setVisibility(View.VISIBLE);
      txt_today_achievement_value.setVisibility(View.VISIBLE);
      txt_today_achievement_percent_value.setVisibility(View.VISIBLE);
      txt_monthlyvaluheder.setVisibility(View.VISIBLE);
      txt_monthly_target_value.setVisibility(View.VISIBLE);
      txt_monthly_achievement_value.setVisibility(View.VISIBLE);
      txt_monthly_achievement_percent_value.setVisibility(View.VISIBLE);
  }

    }

    public boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void getFoPerformanceInfo(String url) {
       // Log.e("<<>>",url+"");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.base_url)+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respjsonObj = new JSONObject(response);
                  //  Log.e(",,..",respjsonObj+",.,,.,.,");
                    String status = respjsonObj.getString("status");
                    JSONObject dashboardObject = respjsonObj.getJSONObject("mark");

                    if (status.equals("1")) {

                        txt_performance.setText("ACH-"+optStringNullCheck(dashboardObject, "achievement")+","+"MEMO-"+optStringNullCheck(dashboardObject, "memo")+","+"PG-"+optStringNullCheck(dashboardObject, "pgcover")+","+"BR-"+optStringNullCheck(dashboardObject, "business_retails"));
                        txt_score.setText(optStringNullCheck(dashboardObject, "total_mark"));


                    } else {
                        Toast.makeText(getActivity(), "Data not found." , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                  //  Log.e("log2", e.getMessage().toString() + "<<");

                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
          //      Log.e("VOLLEY", error.toString());
                //    Utility.dialog.closeProgressDialog();
            }
        });


        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(stringRequest);
    }

    public String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key) || json.optString(key).equalsIgnoreCase("null") || json.isNull(key) || json.optString(key).equalsIgnoreCase(""))
            return "";
        else
            return json.optString(key, key);
    }


    public void getDashboardData() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.loading_text));
        pd.show();
        pd.setCancelable(false);
        JsonRequestFormate jp = new JsonRequestFormate();

        vm.sendRequestToServer2(getActivity(), getString(R.string.base_url)+"api/dashboard-v3",
                jp.jsonDashboardData(SharePreference.getUserId(getActivity()), SharePreference.getUserGlobalId(getActivity())), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        pd.dismiss();

                       // Log.e("<d>",result+"<<");
                        try {
                            JSONObject jsonObject1 = new JSONObject(result);
                            JSONObject dashBoardObject = jsonObject1.getJSONObject("dashboard");
                            //Log.e("dashBoardObject",dashBoardObject+"");

                            txt_today_visits.setText(dashBoardObject.getString("today_visits_before") + " Out Of " + dashBoardObject.getString("today_visits_after"));
                            txt_today_orders.setText(dashBoardObject.getString("today_orders_before") + " Out Of " + dashBoardObject.getString("today_orders_after"));
                            txt_total_attendance.setText(dashBoardObject.getString("attendance"));
                            txt_total_retailers.setText(dashBoardObject.getString("total_retailers"));
                            //today
                            txt_today_qty.setText(dashBoardObject.getString("today_target_qty"));
                            txt_today_value.setText(dashBoardObject.getString("today_target_value"));
                            txt_today_achievement_qty.setText(dashBoardObject.getString("today_achivement_qty"));
                            txt_today_achievement_value.setText(dashBoardObject.getString("today_achivement_value"));
                            txt_today_achievement_percent_qty.setText(dashBoardObject.getString("today_strike_rate_qty"));
                            txt_today_achievement_percent_value.setText(dashBoardObject.getString("today_strike_rate_value"));
                            //monthly
                            txt_monthly_target_qty.setText(dashBoardObject.getString("monthly_target_qty"));
                            txt_monthly_target_value.setText(dashBoardObject.getString("monthly_target"));
                            txt_monthly_achievement_qty.setText(dashBoardObject.getString("monthly_achivement_qty"));
                            txt_monthly_achievement_value.setText(dashBoardObject.getString("monthly_achivement_value"));
                            txt_monthly_achievement_percent_qty.setText(dashBoardObject.getString("monthly_strike_rate_qty"));
                            txt_monthly_achievement_percent_value.setText(dashBoardObject.getString("monthly_strike_rate_value"));
                            fan_text_achivement.setText(dashBoardObject.getString("monthly_fan_strike_rate_value"));

                            float tar=0;
                            float del =0;
                            float percentage = 0;
                            float tar_v=0;
                            float del_v =0;
                            float percentage_v = 0;

                            tar+= Float.parseFloat(dashBoardObject.getString("monthly_target_qty").replace(",",""));
                            del+= Float.parseFloat(dashBoardObject.getString("monthly_achivement_qty").replace(",",""));
                            //  Log.e("statusArray",tar+"<>"+del+"statusArray");
                            tar_v+= Float.parseFloat(dashBoardObject.getString("monthly_target").replace(",",""));
                            del_v+= Float.parseFloat(dashBoardObject.getString("monthly_achivement_value").replace(",",""));


                            JSONArray statusArray = jsonObject1.getJSONArray("pgresult");
                           //  Log.e("statusArray",statusArray+"statusArray");
                            for (int i = 0; i < statusArray.length(); i++) {
                                JSONObject routeObject = statusArray.getJSONObject(i);
                                PGPercentageDTO stockListDTO = new PGPercentageDTO();
                                stockListDTO.setId(routeObject.getString("id"));
                                stockListDTO.setName(routeObject.getString("name"));
                                stockListDTO.setTarget_qty(routeObject.getString("target_qty"));
                                stockListDTO.setDelivery_qty(routeObject.getString("delivery_qty"));
                                stockListDTO.setAchieveRate(routeObject.getString("achieveRate"));

                                stockListDTO.setTarget_value(routeObject.getString("target_value"));
                                stockListDTO.setDelivery_value(routeObject.getString("delivery_value"));


//                                tar+= Float.parseFloat(routeObject.getString("target_qty").replace(",",""));
//                                del+= Float.parseFloat(routeObject.getString("delivery_qty").replace(",",""));
//                            //  Log.e("statusArray",tar+"<>"+del+"statusArray");
//                                tar_v+= Float.parseFloat(routeObject.getString("target_value").replace(",",""));
//                                del_v+= Float.parseFloat(routeObject.getString("delivery_value").replace(",",""));
                                stockDTOS.add(stockListDTO);
                            }

                         //   Log.e("statusArray",tar+"<ll>"+del+"stasstusArray");


                            try {
                                percentage = del / tar * 100;

                                percentage_v = del_v / tar_v * 100;

                                DecimalFormat twoDForm = new DecimalFormat("#");

                                txt_total_qty.setText(String.valueOf(Math.round(Float.valueOf(twoDForm.format(tar)))));
                                txt_imsqyt.setText(String.valueOf(Math.round(Float.valueOf(twoDForm.format(del)))));
                                txt_imsper.setText(String.valueOf(Math.round(Float.valueOf(twoDForm.format(percentage)))) + "%");

                                text_achivement.setText(String.valueOf(Math.round(Float.valueOf(twoDForm.format(percentage)))) + "%");
                                text_achivementvalu.setText(String.valueOf(Math.round(Float.valueOf(twoDForm.format(percentage_v)))) + "%");

                            }catch (NumberFormatException ne){

                            }
                            pgqtyListAdapter.notifyDataSetChanged();

                            //gls
                            txt_gls_target_qty.setText(dashBoardObject.getString("gls_target_qty"));
                            txt_gls_target_value.setText(dashBoardObject.getString("gls_target_value"));
                            txt_gls_achievement_qty.setText(dashBoardObject.getString("gls_achievement_qty"));
                            txt_gls_achievement_value.setText(dashBoardObject.getString("gls_achievement_Value"));
                            txt_gls_achievement_percent_qty.setText(dashBoardObject.getString("gls_strike_rate_qty"));
                            txt_gls_achievement_percent_value.setText(dashBoardObject.getString("gls_strike_rate_value"));
                            //LED
                            txt_led_target_qty.setText(dashBoardObject.getString("led_target_qty"));
                            txt_led_target_value.setText(dashBoardObject.getString("led_target_value"));
                            txt_led_achievement_qty.setText(dashBoardObject.getString("led_achievement_qty"));
                            txt_led_achievement_value.setText(dashBoardObject.getString("led_achievement_Value"));
                            txt_led_achievement_percent_qty.setText(dashBoardObject.getString("led_strike_rate_qty"));
                            txt_led_achievement_percent_value.setText(dashBoardObject.getString("led_strike_rate_value"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    public void getPgWiseReport() {
        stockDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.loading_text));
        pd.show();
        pd.setCancelable(false);
        JsonRequestFormate jp = new JsonRequestFormate();

        vm.sendRequestToServer2(getActivity(), getString(R.string.base_url)+"api/cate_wise_monthly_target_ach",
                jp.jsonPgWiseReport(SharePreference.getUserId(getActivity()), SharePreference.getUserBusinessType(getActivity())), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        pd.dismiss();

                        try {
                            JSONObject jsonObject1 = new JSONObject(result);

                            JSONArray statusArray = jsonObject1.getJSONArray("pgresult");
                            //  Log.e("statusArray",statusArray+"statusArray");
                            for (int i = 0; i < statusArray.length(); i++) {
                                JSONObject routeObject = statusArray.getJSONObject(i);
                                PGPercentageDTO stockListDTO = new PGPercentageDTO();
                                stockListDTO.setId(routeObject.getString("id"));
                                stockListDTO.setName(routeObject.getString("name"));
                                stockListDTO.setTarget_qty(routeObject.getString("target_qty"));
                                stockListDTO.setDelivery_qty(routeObject.getString("delivery_qty"));
                                stockListDTO.setAchieveRate(routeObject.getString("achieveRate"));

                                stockListDTO.setTarget_value(routeObject.getString("target_value"));
                                stockListDTO.setDelivery_value(routeObject.getString("delivery_value"));

                                stockDTOS.add(stockListDTO);
                            }
                            //   Log.e("statusArray",tar+"<ll>"+del+"stasstusArray");

                            pgqtyListAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linlay_today_visit:
                Intent intent = new Intent(getActivity(), DashboardTodaysVisitActivity.class);
                startActivity(intent);
                break;
                case R.id.txt_amount:

                    ((DrawerMain)getActivity()).displayView(53);
                    ((DrawerMain) getActivity()).setTitle("Incentive Report");
                break;

                case R.id.btn_pgwisereport:

                    linlay_pgwisereport.setVisibility(View.VISIBLE);
                    getPgWiseReport();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        startActivityForResult(data, requestCode);
      //  Log.e("intent>",requestCode+"---"+resultCode+"");
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
//                    Barcodee barcode1 = data.getParcelableExtra("bpid");
                    //Barcode barcode2 = data.getParcelableExtra("imei2");
                   /* int barcode1pos = barcode1.getBoundingBox().centerY();
                    int barcode2pos = barcode2.getBoundingBox().centerY();
                    Toast.makeText(this, "IMEI1Y:"+barcode1pos+" IMEI2Y:"+barcode2pos, Toast.LENGTH_LONG).show();*/
//                    String imei1 = barcode1.dismisssplayValue;

                    //-	Toast.makeText(context, imei1, Toast.LENGTH_SHORT).show();
//                    txt_bp_id.setText(imei1);
//                    txt_bp_id_confrim.setText("BP Confirmed !");
//
//                    Utils.BPID = imei1;

//                  JsonRequestFormate jrf = new JsonRequestFormate();
//                  getInvoiceNo(jrf.getInvoiceNo(imei1));

//					String imei2 = barcode2.displayValue;
//					edit_txt_imei1.setText(imei1);
//					edit_txt_imei2.setText(imei2);
//					edit_txt_imei1.setClickable(false);
//					if (imei1.length() == 15 && imei2.length() == 15 && imei1.matches("\\d*") && imei2.matches("\\d*")){
//					}
                }else {
                    Toast.makeText(getActivity(), "Please Try Again..", Toast.LENGTH_SHORT).show();
//                    txt_bp_id_confrim.setText("BP Not Confirmed !");
                }
            }}
    }

    private void getFoIncentiveStatus(String year,String month) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.base_url)+"api/incentive/summary?year="+year+"&month="+month+"&channel="+SharePreference.getUserBusinessType(getActivity())+"&division="+SharePreference.getDivisionId(getActivity())+"&territory="+SharePreference.getTerritoryId(getActivity())+"&point="+SharePreference.getUserPointId(getActivity())+"&fo="+SharePreference.getUserId(getActivity())
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject respjsonObj = new JSONObject(response);
                 //   Log.e("data",respjsonObj+",.,,.,.,");

                    String status = respjsonObj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject dashboardObject = respjsonObj.getJSONObject("data");
                        txt_amount.setText("TK. "+optStringNullCheck(dashboardObject, "total_amount")+" (Approx.)");
                        txt_ims_percentage.setText(optStringNullCheck(dashboardObject, "ims_achv")+"%");

                        ProgressBar_ims.setProgress(Integer.parseInt(optStringNullCheck(dashboardObject, "ims_achv").replace(",", "")));

                    } else {
                         Toast.makeText(getActivity(), respjsonObj.getString("message") , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    Toast.makeText(getActivity(), "J Error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

               // pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                     // Log.e("VOLLEY", error.toString());
                //    Utility.dialog.closeProgressDialog();
              //  pd.dismiss();
            }
        });

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(stringRequest);
    }

    public void getNoticeList(){
        JsonRequestFormate jp = new JsonRequestFormate();
        VolleyMethods vm = new VolleyMethods();

        vm.sendRequestToServer2(getActivity(), getString(R.string.base_url)+"api/app_notic", jp.jsonGetNotice(SharePreference.getUserTypeId(getActivity())), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject respjsonObj = new JSONObject(result);
                    JSONArray statusArray = respjsonObj.getJSONArray("notic_list");
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < statusArray.length(); i++) {
                        JSONObject routeObject = statusArray.getJSONObject(i);
                        NoticeDTO noticeDTO = new NoticeDTO();
                        noticeDTO.setId(routeObject.getString("id"));
                        noticeDTO.setStartDate("Start Date :"+routeObject.getString("start_date"));
                        noticeDTO.setEndDate("End Date :"+routeObject.getString("end_date"));
                        noticeDTO.setNotice(routeObject.getString("notice"));
                        noticeDTO.setStatus(routeObject.getString("status"));
                        noticeDTO.setUserType(routeObject.getString("user_type"));
                        sb.append(" **"+routeObject.getString("notice"));
                    }

                    text_marque.setText(sb.toString());
                } catch (JSONException je) {
                }
            }
        });
    }
}
