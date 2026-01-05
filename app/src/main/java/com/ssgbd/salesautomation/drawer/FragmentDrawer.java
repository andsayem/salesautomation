package com.ssgbd.salesautomation.drawer;

/**
 * Created by Rashed on 29/07/15.
 */

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat; import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.shadow.ShadowRenderer;
import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.LoginActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.NavigationDrawerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.NavDrawerItem;

import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

//import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentDrawer extends Fragment implements View.OnClickListener {
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    DatabaseHelper databaseHelper;

    private static String TAG = FragmentDrawer.class.getSimpleName();
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    // List<Test> array = new ArrayList<>();
    RelativeLayout linLayHome;
    private LinearLayout linlay_change_password, linlay_help_line,linlay_online_exam,linlay_catalogue,linlay_drawer_leave, linlay_visit_frequency_report, linlay_Logout, linlay_drawer_home, linlay_drawer_visit, linlay_all_order, linlay_todays_order, linlay_drawer_ordermanage,linlay_drawer_smart_ordermanage, linlay_drawer_attendance,
            linlay_drawer_utility, linlay_drawer_retailermanage,  linlay_drawer_retailer_ledger;
    LinearLayout linlay_retailer_layout,linlay_drawer_direct_ordermanage,linlay_share_feedback,linlay_drawer_bundle_offer,linlay_drawer_fan_qc, linlay_drawer_return_change, linlay_drawer_return_change_report, linlay_drawer_westage, linlay_drawer_stocklist, linlay_drawer_new_retailer, linlay_drawer_retailer_status,linlay_drawer_retailer_status_new;
    LinearLayout linlay_drawer_order_report, linlay_drawer_manage_requisition,linlay_drawer_order_visit_offline, linlay_order_report_layout, linlay_order_report, linlay_order_vs_delever, linlay_drawer_visit_report, linlay_drawer_attendance_report;
    TextView txt_user_name, txt_user_division, txt_user_point, txt_user_designation,txt_indicator_forecast, txt_indicator, txt_app_version, txt_indicator_orderreport,txt_indicator_returnchangereport, txt_indicator_returnchangereport_policy;
    View view_retailer, view_report, view_repor1t;
    RelativeLayout nav_header_container;
    LinearLayout linlay_drawer_retailer_ledger_details,linlay_drawer_fo_collection,linlay_drawer_return_change_deliver_report,linlay_wastage_order_report, linlay_wastage_delivery_report,linlay_pg_wise_wastage_report, linlay_drawer_wastage_report,
            linlay_drawer_return_cnange_report,linlay_drawer_fo_forecast,linlay_drawer_fo_forecast_remark,linlay_drawer_return_cnange_report_policy,linlay_drawer_return_change_deliver_report_policy,linlay_drawer_return_change_order_report_policy,linlay_drawer_return_manage_policy,linlay_drawer_return_policy, linlay_drawer_forecast_data,linlay_forecast,linlay_drawer_forcast_entry,linlay_drawer_new_product_entry,linlay_drawer_pg_wise_report,linlay_drawer_sku_wise_report, linlay_drawer_pg_free_report,
         linlay_order_report_lay,linlay_fanreplace,linlay_drawer_attendance_report_lay1,linlay_drawer_location_update,linlay_drawer_fan_replace,linlay_drawer_foactivity,
            linlay_return_change_report,linlay_return_change_report_policy, linlay_show_offer,linlay_drawer_retailer_ledger_report,
              linlay_retailer_ledger_report,linlay_drawer_incentive, linlay_drawer_product_strength,linlay_technicianak,linlay_wastage_order_list,linlay_show_notice,linlay_shirtmeasurement,linlay_user_profile,linlay_scan_product,linlay_scan_product_1;
    String formattedDate="";
    LinearLayout linlay_drawer_hr_inout,linlay_drawer_hr_fullday,linlay_assessment,linlay_drawer_retailer_update;


    TextView txt_indicator_wastagereport, txt_indicator_retailerledgerreport,txt_indicator_attendancereport,txt_indicator_fanreplace;

    private SeekBar seekBarBlur;
    private CircleImageView user_circleImageView;

    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    VolleyMethods vm = new VolleyMethods();
    JsonRequestFormate jrf = new JsonRequestFormate();
    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        databaseHelper = new DatabaseHelper(getActivity());

        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myPath = Utility.DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()) {

            databaseHelper.openDataBase();
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.drawerList);
        linLayHome = (RelativeLayout) rootView.findViewById(R.id.linLayBrowse);
        linLayHome.setOnClickListener(this);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        initUI(rootView);
        txt_app_version = (TextView) rootView.findViewById(R.id.txt_app_version);

        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.ssgbd.salesautomation", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            //    Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        txt_app_version.setText("App Version: " + pInfo.versionName);

        imageShow();


        return rootView;
    }

    private void initUI(View v) {
        linlay_drawer_home = (LinearLayout) v.findViewById(R.id.linlay_drawer_home);
        linlay_drawer_home.setOnClickListener(this);

        linlay_drawer_visit = (LinearLayout) v.findViewById(R.id.linlay_drawer_visit);
        linlay_drawer_visit.setOnClickListener(this);

        linlay_drawer_fo_collection = (LinearLayout) v.findViewById(R.id.linlay_drawer_fo_collection);
        linlay_drawer_fo_collection.setOnClickListener(this);

        linlay_todays_order = (LinearLayout) v.findViewById(R.id.linlay_todays_order);
        linlay_todays_order.setOnClickListener(this);

        linlay_wastage_delivery_report = (LinearLayout) v.findViewById(R.id.linlay_wastage_delivery_report);
        linlay_wastage_delivery_report.setOnClickListener(this);

        linlay_all_order = (LinearLayout) v.findViewById(R.id.linlay_all_order);
        linlay_all_order.setOnClickListener(this);

        linlay_drawer_retailer_ledger_details = (LinearLayout) v.findViewById(R.id.linlay_drawer_retailer_ledger_details);
        linlay_drawer_retailer_ledger_details.setOnClickListener(this);


        linlay_drawer_hr_inout = (LinearLayout) v.findViewById(R.id.linlay_drawer_hr_inout);
        linlay_drawer_hr_inout.setOnClickListener(this);

        linlay_drawer_hr_fullday = (LinearLayout) v.findViewById(R.id.linlay_drawer_hr_fullday);
        linlay_drawer_hr_fullday.setOnClickListener(this);

        linlay_drawer_ordermanage = (LinearLayout) v.findViewById(R.id.linlay_drawer_ordermanage);
        linlay_drawer_ordermanage.setOnClickListener(this);

        linlay_drawer_smart_ordermanage = (LinearLayout) v.findViewById(R.id.linlay_drawer_smart_ordermanage);
        linlay_drawer_smart_ordermanage.setOnClickListener(this);

        linlay_drawer_smart_ordermanage = (LinearLayout) v.findViewById(R.id.linlay_drawer_smart_ordermanage);
        linlay_drawer_smart_ordermanage.setOnClickListener(this);

        linlay_visit_frequency_report = (LinearLayout) v.findViewById(R.id.linlay_visit_frequency_report);
        linlay_visit_frequency_report.setOnClickListener(this);

        linlay_pg_wise_wastage_report = (LinearLayout) v.findViewById(R.id.linlay_pg_wise_wastage_report);
        linlay_pg_wise_wastage_report.setOnClickListener(this);

        linlay_show_offer = (LinearLayout) v.findViewById(R.id.linlay_show_offer);
        linlay_show_offer.setOnClickListener(this);

        linlay_help_line = (LinearLayout) v.findViewById(R.id.linlay_help_line);
        linlay_help_line.setOnClickListener(this);

        linlay_online_exam = (LinearLayout) v.findViewById(R.id.linlay_online_exam);
        linlay_online_exam.setOnClickListener(this);

        linlay_assessment = (LinearLayout) v.findViewById(R.id.linlay_assessment);
        linlay_assessment.setOnClickListener(this);

        linlay_drawer_retailer_update = (LinearLayout) v.findViewById(R.id.linlay_drawer_retailer_update);
        linlay_drawer_retailer_update.setOnClickListener(this);

        linlay_catalogue = (LinearLayout) v.findViewById(R.id.linlay_catalogue);
        linlay_catalogue.setOnClickListener(this);

        linlay_share_feedback = (LinearLayout) v.findViewById(R.id.linlay_share_feedback);
        linlay_share_feedback.setOnClickListener(this);

        linlay_drawer_bundle_offer = (LinearLayout) v.findViewById(R.id.linlay_drawer_bundle_offer);
        linlay_drawer_bundle_offer.setOnClickListener(this);

        linlay_drawer_fan_qc = (LinearLayout) v.findViewById(R.id.linlay_drawer_fan_qc);
        linlay_drawer_fan_qc.setOnClickListener(this);

        linlay_drawer_leave = (LinearLayout) v.findViewById(R.id.linlay_drawer_leave);
        linlay_drawer_leave.setOnClickListener(this);

        linlay_drawer_return_change_deliver_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_return_change_deliver_report);
        linlay_drawer_return_change_deliver_report.setOnClickListener(this);

        linlay_change_password = (LinearLayout) v.findViewById(R.id.linlay_change_password);
        linlay_change_password.setOnClickListener(this);

        linlay_drawer_stocklist = (LinearLayout) v.findViewById(R.id.linlay_drawer_stocklist);
        linlay_drawer_stocklist.setOnClickListener(this);

        linlay_drawer_attendance = (LinearLayout) v.findViewById(R.id.linlay_drawer_attendance);
        linlay_drawer_attendance.setOnClickListener(this);
        linlay_drawer_utility = (LinearLayout) v.findViewById(R.id.linlay_drawer_utility);
        linlay_drawer_utility.setOnClickListener(this);
        linlay_drawer_retailermanage = (LinearLayout) v.findViewById(R.id.linlay_drawer_retailermanage);
        linlay_drawer_retailermanage.setOnClickListener(this);

        linlay_drawer_return_change_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_return_change_order_report);
        linlay_drawer_return_change_report.setOnClickListener(this);

        linlay_drawer_return_cnange_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_return_cnange_report);
        linlay_drawer_return_cnange_report.setOnClickListener(this);

        linlay_drawer_fo_forecast = (LinearLayout) v.findViewById(R.id.linlay_drawer_fo_forecast);
        linlay_drawer_fo_forecast.setOnClickListener(this);

        linlay_drawer_fo_forecast_remark = (LinearLayout) v.findViewById(R.id.linlay_drawer_fo_forecast_remark);
        linlay_drawer_fo_forecast_remark.setOnClickListener(this);

        linlay_drawer_return_cnange_report_policy = (LinearLayout) v.findViewById(R.id.linlay_drawer_return_cnange_report_policy);
        linlay_drawer_return_cnange_report_policy.setOnClickListener(this);

        linlay_drawer_return_change_deliver_report_policy = (LinearLayout) v.findViewById(R.id.linlay_drawer_return_change_deliver_report_policy);
        linlay_drawer_return_change_deliver_report_policy.setOnClickListener(this);

        linlay_drawer_return_change_order_report_policy = (LinearLayout) v.findViewById(R.id.linlay_drawer_return_change_order_report_policy);
        linlay_drawer_return_change_order_report_policy.setOnClickListener(this);

        linlay_drawer_return_manage_policy = (LinearLayout) v.findViewById(R.id.linlay_drawer_return_manage_policy);
        linlay_drawer_return_manage_policy.setOnClickListener(this);

        linlay_drawer_return_policy = (LinearLayout) v.findViewById(R.id.linlay_drawer_return_policy);
        linlay_drawer_return_policy.setOnClickListener(this);

        if (SharePreference.getIsDepot(getActivity()).equalsIgnoreCase("Distributor")){
            linlay_drawer_return_cnange_report_policy.setVisibility(View.GONE);
        }

        linlay_drawer_forecast_data = (LinearLayout) v.findViewById(R.id.linlay_drawer_forecast_data);
        linlay_drawer_forecast_data.setOnClickListener(this);

        linlay_forecast = (LinearLayout) v.findViewById(R.id.linlay_forecast);
        linlay_drawer_forcast_entry = (LinearLayout) v.findViewById(R.id.linlay_drawer_forcast_entry);
        linlay_drawer_forcast_entry.setOnClickListener(this);

        linlay_drawer_new_product_entry = (LinearLayout) v.findViewById(R.id.linlay_drawer_new_product_entry);
        linlay_drawer_new_product_entry.setOnClickListener(this);

        linlay_return_change_report = (LinearLayout) v.findViewById(R.id.linlay_return_change_report);
        linlay_return_change_report.setOnClickListener(this);

        linlay_return_change_report_policy = (LinearLayout) v.findViewById(R.id.linlay_return_change_report_policy);
        linlay_return_change_report_policy.setOnClickListener(this);

        linlay_drawer_product_strength = (LinearLayout) v.findViewById(R.id.linlay_drawer_product_strength);
        linlay_drawer_product_strength.setOnClickListener(this);

        linlay_drawer_incentive = (LinearLayout) v.findViewById(R.id.linlay_drawer_incentive);
        linlay_drawer_incentive.setOnClickListener(this);

        linlay_technicianak = (LinearLayout) v.findViewById(R.id.linlay_technicianak);
        linlay_technicianak.setOnClickListener(this);

        linlay_drawer_pg_wise_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_pg_wise_report);
        linlay_drawer_pg_wise_report.setOnClickListener(this);

        linlay_drawer_sku_wise_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_sku_wise_report);
        linlay_drawer_sku_wise_report.setOnClickListener(this);

        linlay_drawer_pg_free_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_pg_free_report);
        linlay_drawer_pg_free_report.setOnClickListener(this);

        linlay_order_report_layout = (LinearLayout) v.findViewById(R.id.linlay_order_report_layout);
        linlay_fanreplace = (LinearLayout) v.findViewById(R.id.linlay_fanreplace);
        linlay_drawer_order_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_order_report);
        linlay_drawer_order_report.setOnClickListener(this);

        linlay_drawer_manage_requisition = (LinearLayout) v.findViewById(R.id.linlay_drawer_manage_requisition);
        linlay_drawer_manage_requisition.setOnClickListener(this);
        linlay_drawer_order_visit_offline = (LinearLayout) v.findViewById(R.id.linlay_drawer_order_visit_offline);
        linlay_drawer_order_visit_offline.setOnClickListener(this);
        linlay_order_report = (LinearLayout) v.findViewById(R.id.linlay_order_report);
        linlay_order_report.setOnClickListener(this);

        linlay_order_vs_delever = (LinearLayout) v.findViewById(R.id.linlay_order_vs_delever);
        linlay_order_vs_delever.setOnClickListener(this);
        linlay_drawer_visit_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_visit_report);
        linlay_drawer_visit_report.setOnClickListener(this);
        linlay_drawer_attendance_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_attendance_report);
        linlay_drawer_attendance_report.setOnClickListener(this);

        linlay_retailer_layout = (LinearLayout) v.findViewById(R.id.linlay_retailer_layout);
        linlay_drawer_new_retailer = (LinearLayout) v.findViewById(R.id.linlay_drawer_new_retailer);
        linlay_drawer_new_retailer.setOnClickListener(this);

        linlay_drawer_return_change = (LinearLayout) v.findViewById(R.id.linlay_drawer_return_change);
        linlay_drawer_return_change.setOnClickListener(this);

        linlay_drawer_retailer_status = (LinearLayout) v.findViewById(R.id.linlay_drawer_retailer_status);
        linlay_drawer_retailer_status.setOnClickListener(this);
        linlay_drawer_retailer_status_new = (LinearLayout) v.findViewById(R.id.linlay_drawer_retailer_status_new);
        linlay_drawer_retailer_status_new.setOnClickListener(this);

        linlay_drawer_westage = (LinearLayout) v.findViewById(R.id.linlay_drawer_westage);
        linlay_drawer_westage.setOnClickListener(this);

        linlay_drawer_foactivity = (LinearLayout) v.findViewById(R.id.linlay_drawer_foactivity);
        linlay_drawer_foactivity.setOnClickListener(this);

        linlay_drawer_retailer_ledger = (LinearLayout) v.findViewById(R.id.linlay_drawer_retailer_ledger_summery);
        linlay_drawer_retailer_ledger.setOnClickListener(this);

        txt_indicator_forecast = (TextView) v.findViewById(R.id.txt_indicator_forecast);
        txt_indicator = (TextView) v.findViewById(R.id.txt_indicator);
        view_retailer = (View) v.findViewById(R.id.view_retailer);
        view_report = (View) v.findViewById(R.id.view_report);
        view_repor1t = (View) v.findViewById(R.id.view_repor1t);

        linlay_Logout = (LinearLayout) v.findViewById(R.id.linlay_Logout);
        linlay_Logout.setOnClickListener(this);

        linlay_wastage_order_report = (LinearLayout) v.findViewById(R.id.linlay_wastage_order_report);
        linlay_wastage_order_report.setOnClickListener(this);
        linlay_drawer_direct_ordermanage = (LinearLayout) v.findViewById(R.id.linlay_drawer_direct_ordermanage);
        linlay_drawer_direct_ordermanage.setOnClickListener(this);

        linlay_wastage_order_list = (LinearLayout) v.findViewById(R.id.linlay_wastage_order_list);
        linlay_wastage_order_list.setOnClickListener(this);

        linlay_show_notice = (LinearLayout) v.findViewById(R.id.linlay_show_notice);
        linlay_show_notice.setOnClickListener(this);

        linlay_shirtmeasurement = (LinearLayout) v.findViewById(R.id.linlay_shirtmeasurement);
        linlay_shirtmeasurement.setOnClickListener(this);

        linlay_user_profile = (LinearLayout) v.findViewById(R.id.linlay_user_profile);
        linlay_user_profile.setOnClickListener(this);

        linlay_scan_product = (LinearLayout) v.findViewById(R.id.linlay_scan_product);
        linlay_scan_product.setOnClickListener(this);

        linlay_scan_product_1 = (LinearLayout) v.findViewById(R.id.linlay_scan_product_1);
        linlay_scan_product_1.setOnClickListener(this);

        linlay_drawer_wastage_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_wastage_report);
        linlay_drawer_wastage_report.setOnClickListener(this);

        linlay_retailer_ledger_report = (LinearLayout) v.findViewById(R.id.linlay_retailer_ledger_report);
        linlay_drawer_retailer_ledger_report = (LinearLayout) v.findViewById(R.id.linlay_drawer_retailer_ledger_report);
        linlay_drawer_retailer_ledger_report.setOnClickListener(this);

        txt_indicator_retailerledgerreport = (TextView) v.findViewById(R.id.txt_indicator_retailerledgerreport);
        txt_indicator_wastagereport = (TextView) v.findViewById(R.id.txt_indicator_wastagereport);
        txt_user_name = (TextView) v.findViewById(R.id.txt_user_name);
        txt_user_name.setText(SharePreference.getUserName(getActivity()));

        txt_user_division = (TextView) v.findViewById(R.id.txt_user_division);
        txt_user_division.setText("Division :" + SharePreference.getDivisionName(getActivity()));

        txt_user_point = (TextView) v.findViewById(R.id.txt_user_point);
        txt_user_point.setText("Point :" + SharePreference.getUserPointName(getActivity()));
        txt_indicator_orderreport = (TextView) v.findViewById(R.id.txt_indicator_orderreport);
        txt_indicator_returnchangereport = (TextView) v.findViewById(R.id.txt_indicator_returnchangereport);
        txt_indicator_returnchangereport_policy = (TextView) v.findViewById(R.id.txt_indicator_returnchangereport_policy);
        txt_user_designation = (TextView) v.findViewById(R.id.txt_user_designation);
        txt_user_designation.setText(SharePreference.getDesignation(getActivity()));

        nav_header_container = (RelativeLayout) v.findViewById(R.id.nav_header_container);
        nav_header_container.setOnClickListener(this);

        seekBarBlur = (SeekBar) v.findViewById(R.id.seekBar);
        user_circleImageView = (CircleImageView) v.findViewById(R.id.user_circleImageView);


        linlay_order_report_lay = (LinearLayout) v.findViewById(R.id.linlay_order_report_lay);
        linlay_order_report_lay.setOnClickListener(this);

        linlay_drawer_attendance_report_lay1 = (LinearLayout) v.findViewById(R.id.linlay_drawer_attendance_report_lay1);
        linlay_drawer_attendance_report_lay1.setOnClickListener(this);

        linlay_drawer_fan_replace = (LinearLayout) v.findViewById(R.id.linlay_drawer_fan_replace);
        linlay_drawer_fan_replace.setOnClickListener(this);

        linlay_drawer_location_update = (LinearLayout) v.findViewById(R.id.linlay_drawer_location_update);
        linlay_drawer_location_update.setOnClickListener(this);

        txt_indicator_attendancereport = (TextView) v.findViewById(R.id.txt_indicator_attendancereport);
        txt_indicator_fanreplace = (TextView) v.findViewById(R.id.txt_indicator_fanreplace);

    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linLayBrowse:
                drawerListener.onDrawerItemSelected(v, 0);
                mDrawerLayout.closeDrawer(containerView);
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = getActivity().getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }
                break;

            case R.id.nav_header_container:
                Toast.makeText(getActivity(), "Please go to profile screen to change user photo.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linlay_Logout:
                logOutAlert(getActivity());
                break;

            case R.id.linlay_drawer_home:
                showTabItem(0, "HOME");
                break;
            case R.id.linlay_drawer_attendance:

                checkPermissionLocation();
                break;
            case R.id.linlay_drawer_visit:
                showTabItem(2, "VISIT");
                break;

            case R.id.linlay_drawer_ordermanage:
                showTabItem(3, "ORDER MANAGE");
                break;

            case R.id.linlay_drawer_utility:
                showTabItem(4, "UTILITY");
                break;

            case R.id.linlay_drawer_retailermanage:

                if (linlay_retailer_layout.getVisibility() == View.VISIBLE) {
                    linlay_retailer_layout.setVisibility(View.GONE);
                    txt_indicator.setText("+");
                    view_retailer.setVisibility(View.GONE);
                } else {
                    linlay_retailer_layout.setVisibility(View.VISIBLE);
                    txt_indicator.setText("-");
                    view_retailer.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.linlay_drawer_new_retailer:
                showTabItem(5, "New Retailer");
                break;

            case R.id.linlay_todays_order:
                showTabItem(7, "Today's Order(offline)");
                break;

            case R.id.linlay_all_order:
                showTabItem(8, "All Order");
                break;

            case R.id.linlay_drawer_retailer_status:
                showTabItem(9, "Active /Inactive Retailer");
                break;

            case R.id.linlay_drawer_return_change:
                showTabItem(10, "Return & Change");
                break;

            case R.id.linlay_drawer_westage:
                showTabItem(11, "Wastage");
                break;

            case R.id.linlay_drawer_stocklist:
                showTabItem(12, "Stock List");
                break;

            case R.id.linlay_drawer_return_change_order_report:
                showTabItem(13, "Return Change Report");
                break;

            case R.id.linlay_drawer_order_report:

                if (linlay_order_report_layout.getVisibility() == View.GONE) {
                    linlay_order_report_layout.setVisibility(View.VISIBLE);
                    txt_indicator_orderreport.setText("-");
                    view_report.setVisibility(View.VISIBLE);
                } else {
                    linlay_order_report_layout.setVisibility(View.GONE);
                    txt_indicator_orderreport.setText("+");
                    view_report.setVisibility(View.GONE);
                }
                break;
            case R.id.linlay_order_report:
                showTabItem(20, "Order Report");
                break;

            case R.id.linlay_drawer_pg_wise_report:
                showTabItem(21, "PG Wise Report");
                break;
            case R.id.linlay_drawer_pg_free_report:
                showTabItem(22, "PG Free Report");
                break;

            case R.id.linlay_order_vs_delever:
                showTabItem(14, "Delivery vs Order");
                break;
            case R.id.linlay_drawer_visit_report:
                showTabItem(15, "Visit Report");
                break;
            case R.id.linlay_drawer_attendance_report:
                showTabItem(16, "Attendance Report");
                break;

            case R.id.linlay_drawer_wastage_report:

                if (linlay_wastage_order_report.getVisibility() == View.GONE) {
                    linlay_wastage_order_report.setVisibility(View.VISIBLE);
                    txt_indicator_wastagereport.setText("-");
                    view_repor1t.setVisibility(View.VISIBLE);
                } else {
                    linlay_wastage_order_report.setVisibility(View.GONE);
                    txt_indicator_wastagereport.setText("+");
                    view_repor1t.setVisibility(View.GONE);
                }
                break;

            case R.id.linlay_drawer_attendance_report_lay1:

                loginHris(getString(R.string.base_url_hris)+"auth/login");
                if (linlay_order_report_lay.getVisibility()==View.GONE){
                    linlay_order_report_lay.setVisibility(View.VISIBLE);
                    txt_indicator_attendancereport.setText("-");
                 //   view_repor1t.setVisibility(View.VISIBLE);
                }else {
                    linlay_order_report_lay.setVisibility(View.GONE);
                    txt_indicator_attendancereport.setText("+");
                   // view_repor1t.setVisibility(View.GONE);
                }
                break;

                case R.id.linlay_drawer_fan_replace:

                if (linlay_fanreplace.getVisibility()==View.GONE){
                    linlay_fanreplace.setVisibility(View.VISIBLE);
                    txt_indicator_fanreplace.setText("-");
                 //   view_repor1t.setVisibility(View.VISIBLE);
                }else {
                    linlay_fanreplace.setVisibility(View.GONE);
                    txt_indicator_fanreplace.setText("+");
                   // view_repor1t.setVisibility(View.GONE);
                }
                break;

                case R.id.linlay_drawer_return_cnange_report:

                if (linlay_return_change_report.getVisibility()==View.GONE){
                    linlay_return_change_report.setVisibility(View.VISIBLE);
                    txt_indicator_returnchangereport.setText("-");
                 //   view_repor1t.setVisibility(View.VISIBLE);
                }else {
                    linlay_return_change_report.setVisibility(View.GONE);
                    txt_indicator_returnchangereport.setText("+");
                   // view_repor1t.setVisibility(View.GONE);
                }

                break;


            case R.id.linlay_drawer_return_cnange_report_policy:

                if (linlay_return_change_report_policy.getVisibility()==View.GONE){
                    linlay_return_change_report_policy.setVisibility(View.VISIBLE);
                    txt_indicator_returnchangereport_policy.setText("-");
                    //   view_repor1t.setVisibility(View.VISIBLE);
                }else {
                    linlay_return_change_report_policy.setVisibility(View.GONE);
                    txt_indicator_returnchangereport_policy.setText("+");
                    // view_repor1t.setVisibility(View.GONE);
                }

                break;

                case R.id.linlay_drawer_forecast_data:

                if (linlay_forecast.getVisibility()==View.GONE){
                    linlay_forecast.setVisibility(View.VISIBLE);
                    txt_indicator_forecast.setText("-");
                }else {
                    linlay_forecast.setVisibility(View.GONE);
                    txt_indicator_forecast.setText("+");
                }

                break;

            case R.id.linlay_drawer_return_change_deliver_report:

                showTabItem(27, "Return Order");
                break;

                case R.id.linlay_drawer_retailer_ledger_report:

                if (linlay_retailer_ledger_report.getVisibility()==View.GONE){
                    linlay_retailer_ledger_report.setVisibility(View.VISIBLE);
                    txt_indicator_retailerledgerreport.setText("-");
                }else {
                    linlay_retailer_ledger_report.setVisibility(View.GONE);
                    txt_indicator_retailerledgerreport.setText("+");
                }
                break;

            case R.id.linlay_drawer_retailer_ledger_summery:
                showTabItem(17, "Retailer Ledger");
                break;

                case R.id.linlay_drawer_retailer_ledger_details:
                showTabItem(28, "Ledger Details");
                break;

            case R.id.linlay_drawer_order_visit_offline:
                showTabItem(18, "Visit offline status");
                break;
            case R.id.linlay_visit_frequency_report:
                showTabItem(24, "Visit Frequency Report");
                break;
            case R.id.linlay_help_line:
                showTabItem(19, "Help Line");

                break;

            case R.id.linlay_change_password:

                showTabItem(29, "Settings");
                break;

            case R.id.linlay_wastage_order_list:
                showTabItem(23, "Wastage Order Report");
                break;
            case R.id.linlay_wastage_delivery_report:
                showTabItem(25, "Wastage Delivery Report");
                break;
            case R.id.linlay_pg_wise_wastage_report:
                showTabItem(26, "PG Wastage Report");
                break;

                case R.id.linlay_show_offer:
                showTabItem(30, "Offers");
                break;

                case R.id.linlay_show_notice:
                showTabItem(31, "Notice");
                break;

            case R.id.linlay_drawer_leave:
                showTabItem(32, "LEAVE");

                break;

                case R.id.linlay_share_feedback:
                showTabItem(33, "Share Feedback");

                break;

                case R.id.linlay_shirtmeasurement:
                showTabItem(34, "Shirt Measurement");
                break;

                case R.id.linlay_drawer_foactivity:
                showTabItem(35, "FO Activity Report");
                break;

                case R.id.linlay_user_profile:
                showTabItem(36, "Profile");
                break;

            case R.id.linlay_catalogue:
                showTabItem(37, "Catalogue");

                break;

            case R.id.linlay_drawer_hr_inout:
                showTabItem(38, "In-out");
                break;

            case R.id.linlay_drawer_hr_fullday:
                showTabItem(39, "Full day adjustment");
                break;

            case R.id.linlay_drawer_retailer_status_new:
                showTabItem(40, "Active /Inactive Retailer");
                break;

                case R.id.linlay_online_exam:
                showTabItem(41, "Online Exam");
                break;

                case R.id.linlay_assessment:
                showTabItem(42, "Assessment");
                break;

                case R.id.linlay_drawer_retailer_update:
                showTabItem(43, "Retailer Update");
                break;

                case R.id.linlay_drawer_product_strength:
                showTabItem(44, "Product Strength");
                break;

                case R.id.linlay_technicianak:
                showTabItem(45, "Technician");
                break;

                case R.id.linlay_drawer_manage_requisition:
                showTabItem(47, "Fan QC");
                break;

                case R.id.linlay_drawer_bundle_offer:
                showTabItem(48, "Bundle Offer");
                break;

                case R.id.linlay_drawer_fan_qc:
                showTabItem(49, "Automation");
                break;

                case R.id.linlay_drawer_location_update:
                showTabItem(50, "Location Updates");
                break;

                case R.id.linlay_drawer_forcast_entry:
                showTabItem(51, "Forecast");
                break;

            case R.id.linlay_drawer_new_product_entry:
                        showTabItem(52, "Product Entry");
                break;

                case R.id.linlay_drawer_incentive:
                        showTabItem(53, "Incentive Report");
                break;
                case R.id.linlay_scan_product:
                        showTabItem(54, "Scan Product");
                break;

                case R.id.linlay_drawer_return_policy:
                        showTabItem(55, "Return Policy");
                break;

                case R.id.linlay_scan_product_1:
                        showTabItem(56, "Scan Porduct");
                break;

                case R.id.linlay_drawer_return_manage_policy:
                        showTabItem(59, "Return Manage");
                break;

                case R.id.linlay_drawer_return_change_order_report_policy:
                        showTabItem(60, "Return Order");
                break;

                case R.id.linlay_drawer_return_change_deliver_report_policy:
                        showTabItem(61, "Return VS Received");
                break;


            case R.id.linlay_drawer_sku_wise_report:
                showTabItem(62, "SKU Wise Report");
                break;

                case R.id.linlay_drawer_fo_collection:
                showTabItem(63, "Collection");
                break;

            case R.id.linlay_drawer_smart_ordermanage:
                showTabItem(64, "Smart Order MANAGE");
                break;

                case R.id.linlay_drawer_direct_ordermanage:
                showTabItem(65, "Direct Order MANAGE");
                break;

                case R.id.linlay_drawer_fo_forecast:
                showTabItem(66, "FO FORECAST");
                break;

                case R.id.linlay_drawer_fo_forecast_remark:
                showTabItem(67, "FO FORECAST REMARK");
                break;

        }
    }

    private void loginHris(String url) {



            RequestQueue queue = Volley.newRequestQueue(getActivity());
            StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    // Log.e("<<>>",response+"");
                    // {"status":"1","message":"Success"}


                    try {
                        JSONObject respjsonObj = new JSONObject(response);
                                  //  Log.e("<<>>",respjsonObj+"");
                        SharePreference.setAccesstokenHRIS(getActivity(),respjsonObj.getString("access_token"));
                        JSONObject userObject = respjsonObj.getJSONObject("user");
                        SharePreference.setHrisID(getActivity(),userObject.getString("id"));
                       // Log.e("<<>>",SharePreference.getHrisID(getActivity())+"");

                    //
//                                "token_type": "Bearer"

                    }  catch (JSONException e) {

                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("user_name",SharePreference.getEmployeeId(getActivity()));
                    params.put("password",Utility.hpwd);

                    return params;
                }
            };
            queue.add(sr);

    }

    private void showMainContaint(int displayView, String title) {
        ((DrawerMain) getActivity()).displayView(displayView);
        mDrawerLayout.closeDrawer(containerView);
        ((DrawerMain) getActivity()).setTitle(title);
    }

    public void showTabItem(int tabNo, String title) {
        Utility.TAB_ITEM_NUMBER = tabNo;
        mDrawerLayout.closeDrawer(containerView);
        ((DrawerMain) getActivity()).displayView(tabNo);
        ((DrawerMain) getActivity()).setTitle(title);
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    public void logOutAlert(final Context context) {
        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.drawable.search_hover)).setTitle("আপনি কি লগ আউট করতে চান ?");

        alertBulder.setPositiveButton("হা", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Utility.LatestRoute="";
                Utility.ROUTE_ID = "";
                Utility.ROUTE_NAME = "";
                Utility.V_RETAILER_ID = "";
                Utility.V_RETAILER_NAME = "";
                SharePreference.setUserPointId(context,"");

                SharePreference.setIsRetailerBaseSync(context,"no");
                SharePreference.setIsCategoryBaseSync(context,"no");
                SharePreference.setIsProductBaseSync(context,"no");

                SharePreference.setIslogIn(context, "0");
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(loginIntent);
                getActivity().finish();

            }
        });

        alertBulder.setNegativeButton("না", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertBulder.show();
    }


    public void checkPermissionLocation() {

        // for location service
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setCancelable(false);
            dialog.setMessage(getActivity().getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getActivity().getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);

                }
            });

            dialog.show();
        } else {
            showTabItem(1, "ATTENDANCE");
        }
    }

    public void imageShow() {

        final ProgressDialog pd = new ProgressDialog(getActivity());


        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url) + "api/get-profile-pic", jrf.getimageformate(SharePreference.getUserLoginId(getActivity())), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {

                    JSONObject jsonObject = new JSONObject(result);

                    Picasso.with(getActivity())
                            .load(getString(R.string.base_url) + jsonObject.getString("image"))
                            .placeholder(R.mipmap.user).into(user_circleImageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
