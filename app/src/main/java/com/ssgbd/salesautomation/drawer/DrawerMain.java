package com.ssgbd.salesautomation.drawer;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;




import android.app.AlertDialog;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;


import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.FOForecastWeb;
import com.ssgbd.salesautomation.FOForecastWebRemark;
import com.ssgbd.salesautomation.FanQCWeb;
import com.ssgbd.salesautomation.ProductStrengthActivity;
import com.ssgbd.salesautomation.ProductTrackingWeb;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.fragment.ActiveInactiveFragment;
import com.ssgbd.salesautomation.drawer.fragment.ActiveInactiveFragment_New;
import com.ssgbd.salesautomation.drawer.fragment.AdminFragment;
import com.ssgbd.salesautomation.drawer.fragment.AllOrderFragment;
import com.ssgbd.salesautomation.drawer.fragment.AttendanceFragmentNew;
import com.ssgbd.salesautomation.drawer.fragment.CatalogueFragment;
import com.ssgbd.salesautomation.drawer.fragment.DirectOrderManageFragment;
import com.ssgbd.salesautomation.drawer.fragment.FoCollectionFragment;
import com.ssgbd.salesautomation.drawer.fragment.FoFeedbackFragment;
import com.ssgbd.salesautomation.drawer.fragment.FoLeaveFragment;
import com.ssgbd.salesautomation.drawer.fragment.ForecastEntryFragment;
import com.ssgbd.salesautomation.drawer.fragment.ForecastNewProductFragment;
import com.ssgbd.salesautomation.drawer.fragment.FullDayFragment;
import com.ssgbd.salesautomation.drawer.fragment.HelpLineFragment;
import com.ssgbd.salesautomation.drawer.fragment.HomeFragment;
import com.ssgbd.salesautomation.drawer.fragment.InOutFragment;
import com.ssgbd.salesautomation.drawer.fragment.IncentiveFragment;
import com.ssgbd.salesautomation.drawer.fragment.LocationUpdates;
import com.ssgbd.salesautomation.drawer.fragment.NoticeListFragment;
import com.ssgbd.salesautomation.drawer.fragment.OfferImageFragment;
import com.ssgbd.salesautomation.drawer.fragment.OrderManageFragment;
import com.ssgbd.salesautomation.drawer.fragment.OrderVisitStatusOfflineDataFragment;
import com.ssgbd.salesautomation.drawer.fragment.ProfileFragment;
import com.ssgbd.salesautomation.drawer.fragment.RetailerUpdateFragment;
import com.ssgbd.salesautomation.drawer.fragment.ShirtMesurmentFragment;
import com.ssgbd.salesautomation.drawer.fragment.SmartOrderManageFragment;
import com.ssgbd.salesautomation.drawer.fragment.TechnicianListFragment;
import com.ssgbd.salesautomation.dtos.NoticeDTO;
import com.ssgbd.salesautomation.gps.GPSTracker;
//import com.ssgbd.salesautomation.live.GPSProvider;
//import com.ssgbd.salesautomation.qr.Qrc_ScannerBarcodeActivity;
//import com.ssgbd.salesautomation.live.LocationForegroundService;
import com.ssgbd.salesautomation.report.AttendanceReportFragmentSsforce;
import com.ssgbd.salesautomation.report.FoActivityReportFragment;
import com.ssgbd.salesautomation.report.bundle.BundleOfferReportFragment;
import com.ssgbd.salesautomation.report.fanreplace.FanQCListFragment;
import com.ssgbd.salesautomation.report.order.SKUWiseReportFragment;
import com.ssgbd.salesautomation.report.retailerledger.RetailerLedgerDetailsReportFragment;
import com.ssgbd.salesautomation.report.retailerledger.RetailerLedgerFragment;
import com.ssgbd.salesautomation.drawer.fragment.ReturnAndChangeFragment;
import com.ssgbd.salesautomation.drawer.fragment.StockListFragment;
import com.ssgbd.salesautomation.drawer.fragment.TodaysOrderFragment;
import com.ssgbd.salesautomation.drawer.fragment.UtilityFragment;
import com.ssgbd.salesautomation.drawer.fragment.VisitFragment;
import com.ssgbd.salesautomation.drawer.fragment.WastageFragment;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.report.AttendanceReportFragment;
import com.ssgbd.salesautomation.report.DeliveryVsOrderReportFragment;
import com.ssgbd.salesautomation.report.VisitFrequencyReportFragment;
import com.ssgbd.salesautomation.report.VisitReportFragment;
import com.ssgbd.salesautomation.report.order.OrderReportFragment;
import com.ssgbd.salesautomation.report.order.PGFreeReportFragment;
import com.ssgbd.salesautomation.report.order.PGWiseReportFragment;
import com.ssgbd.salesautomation.report.wastage.PGWiseWastageReportFragment;
import com.ssgbd.salesautomation.report.wastage.WastegDeliveryReportFragment;
import com.ssgbd.salesautomation.report.wastage.WastegOrderReportFragment;
import com.ssgbd.salesautomation.retailer.NewRetailerFragment1;
import com.ssgbd.salesautomation.returnchange.ReturnChangeDeliveryReportFragment;
import com.ssgbd.salesautomation.returnchange.ReturnChangeOrderReportFragment;
import com.ssgbd.salesautomation.returnpolicy.VisitFragment_RP;
import com.ssgbd.salesautomation.returnpolicy.report.ReturnManageFragment;
import com.ssgbd.salesautomation.returnpolicy.report.ReturnReportFragment;
import com.ssgbd.salesautomation.returnpolicy.report.ReturnVsOrderReportFragment;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rashed on 4/12/2019.
 */

public class DrawerMain extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = DrawerMain.class.getSimpleName();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private TextView txt_Toolbar;
    boolean doubleBackToExitPressedOnce = false;
    private final Handler handler = new Handler();
    DatabaseHelper databaseHelper;
   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_NAME = "ssg.db";
    RequestQueue queue;
    private LinearLayout linlay_Logout;
    String ssss="2024";
    Context context;
    String formattedDate="";
    VolleyMethods vm = new VolleyMethods();
    String syncDate="";
    String currentTime="";
    PackageInfo pInfo = null;
  // PendingIntent pendingIntent;
    AlarmManager manager;
    private Handler mHandler = new Handler();
    JobScheduler mJobScheduler;
    GPSTracker gps;
    String LAT="",LON="";
    Fragment fragmentvisit = null;
 ImageView img_bell;
 TextView txt_unread;
    private Dialog wdialog;
    ArrayList<NoticeDTO> noticeDTOS = new ArrayList<>();
    String android_id = "";
    int REQUEST_LOCATION_PERMISSION=0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       //comment for further use
       // GPSProvider.getGPSTracker(this);


      // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main_drawer);
        context = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // txt_Toolbar = (TextView) mToolbar.findViewById(R.id.);
        txt_unread = (TextView) mToolbar.findViewById(R.id.txt_unread);
        txt_Toolbar = (TextView) mToolbar.findViewById(R.id.txt_toolbar);
        txt_Toolbar.setText("HOME");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setTitle((" "));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
        img_bell = (ImageView) findViewById(R.id.img_bell);
        img_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            drawerFragment.showTabItem(31, "Notice");

            }
        });
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        queue = Volley.newRequestQueue(this);

        databaseHelper = new DatabaseHelper(context);
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
        // display the first navigation drawer view on app launch
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        syncDate = sdf.format(new Date());

        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm");
        currentTime = timeF.format(new Date());

        String url = getString(R.string.base_url) + "apps/api/route?appsuser_id=" + SharePreference.getUserId(context) + "&appsglobal_id=" + SharePreference.getUserGlobalId(context);
        httpRequest(url);

        if (!isInternetAvailable(context)) {
            Toast.makeText(context, "আপনার মোবাইল এ ইন্টারনেট নাই।", Toast.LENGTH_SHORT).show();
        } else {
      // comment 10-3-24
         //   syncRetailer("");
        }
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SharePreference.setAppVersion(context,pInfo.versionName);
        checkAppUpdate(pInfo.versionName);
       // checkAttendance();

        android_id = Settings.Secure.getString(context.getContentResolver(),
        Settings.Secure.ANDROID_ID);
     //   Log.e("<<>>",getString(R.string.base_url_hris)+"api/LoginRepository/GetLoginInfo2?empID="+SharePreference.getEmployeeId(context)+"&fNmIndHr="+android_id+android_id+"<<>>");


        getNoticeList();
        //comment 10-3-24
//        syncCategory("");
//        syncProduct("");
        //sendDInfo();

        SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdff.parse("04/12/2024");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (System.currentTimeMillis() > strDate.getTime()) {

        }else {

            showServeyAlert(context);
        }
        drawerFragment.showTabItem(2, "Visit");

        // this method for location service its working fine
      //  checkPermissions();
    }

    public void updateNotifiCount(){
        txt_unread.setText(String.valueOf(databaseHelper.getUnreadCount(databaseHelper)));

    }


    public void getLatLon() {

        gps = new GPSTracker(context);

        //check if GPS enabled
        if(gps.canGetLocation()){

            LAT = String.valueOf(gps.getLatitude());
            LON = String.valueOf(gps.getLongitude());

//            Log.e(">>>latLOn>>>>",LAT+"------"+LON+"");
        }else{
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings

            new AlertDialog.Builder(context)
                    .setTitle("Prominent disclosure \n Background Location Permission Needed")
                    // .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setMessage(getString(R.string.location_text))
                    .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })

                    .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            gps.showSettingsAlert();
                        }
                    })
                    .create()
                    .show();
        }
    }
    
    public void checkAppUpdate(final String presentVersion){

        RequestQueue queue;
        //
        String  url = getResources().getString(R.string.base_url)+"apps/api/version-con";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                      //  Log.e("version>>",response+"<pv>>"+presentVersion);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("version").equalsIgnoreCase(presentVersion)){

                            }
//                            else if (ssss.equalsIgnoreCase("2024")) {
//
//                            }
                            else {
                                appUpdateAlert(context);
                            }
                        }catch (JSONException je){

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                   //     Log.e("error volley>>",error.toString()+"");
                    }
                }
        ) {
        };
        queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        postRequest.setShouldCache(false);
        queue.add(postRequest);
    }
    public void appUpdateAlert(final Context context) {
        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("আপনার অ্যাপ আপডেট করুন। নতুন ভার্শন - ১.১০.৩");
        alertBulder.setCancelable(false);
        alertBulder.setPositiveButton("আপডেট করুন", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//              goto play store for update
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
//        }).setNegativeButton("এখন নই", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
        });

        alertBulder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAppUpdate(pInfo.versionName);
       // startAlarm();
    }

    public void setTitle(String s){

           txt_Toolbar.setText(s);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //help menu
            if(id == R.id.action_help){
                Toast.makeText(getApplicationContext(), "help selected!", Toast.LENGTH_SHORT).show();
                displayView(3);
                setTitle("HELP");
                return true;
            }
            // help submenu
            if (id==R.id.item_top_game){
                Toast.makeText(getApplicationContext(), "Top ", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id==R.id.item_top_apps){
                Toast.makeText(getApplicationContext(), "Top ", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id==R.id.help2){
                Toast.makeText(getApplicationContext(), "Top ", Toast.LENGTH_SHORT).show();
                return true;
            }
            //search menu
            if(id == R.id.action_search){
                Toast.makeText(getApplicationContext(), "search !", Toast.LENGTH_SHORT).show();
                return true;
            }
            //filter menu
            if(id == R.id.action_filtter){
                Toast.makeText(getApplicationContext(), "filter !", Toast.LENGTH_SHORT).show();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onDrawerItemSelected(View view, int position) {
            displayView(position);
        }

        public void displayView(int position) {
            Fragment fragment = null;
            String title = getString(R.string.app_name);
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                   // title = getString(R.string.title_text);
                    break;
                case 1:
                    fragment = new AttendanceFragmentNew();
                 //   Log.e("case 1","<<>>>>"+"first");
                    title = getString(R.string.title_text);
                    break;
                case 2:
                   // Log.e("case 2","<<>>>>"+"first");
//                    if (SharePreference.getHrisCheckin(context).equalsIgnoreCase(SharePreference.getEmployeeId(context)+formattedDate)){
                        fragment = new VisitFragment();
//                   }else {
//                        fragment = new AttendanceFragmentNew();
//                        Toast.makeText(context, "আগে আপনার অ্যাটেনডেন্স সম্পন্ন করুন। ", Toast.LENGTH_SHORT).show();
//                    }
                 //   checkAttendance( );

                  //  checkAttendanceMenu();

                    title = getString(R.string.title_text);

                    break;
                case 3:
                    fragment = new OrderManageFragment();
                    title = getString(R.string.title_text);
                    break;

                case 4:
                    fragment = new UtilityFragment();
                    title = getString(R.string.title_text);
                    break;

                 case 5:
                    fragment = new NewRetailerFragment1();
                    title = getString(R.string.title_text);
                    break;
                    case 6:
                    fragment = new AdminFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 7:
                    fragment = new TodaysOrderFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 8:
                    fragment = new AllOrderFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 9:
                    fragment = new ActiveInactiveFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 10:
                    fragment = new ReturnAndChangeFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 11:
                    fragment = new WastageFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 12:
                    fragment = new StockListFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 13:
                    fragment = new ReturnChangeOrderReportFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 14:
                    fragment = new DeliveryVsOrderReportFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 15:
                    fragment = new VisitReportFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 16:
                    fragment = new AttendanceReportFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 17:
                    fragment = new RetailerLedgerFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 18:
                    fragment = new OrderVisitStatusOfflineDataFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 19:
                    fragment = new HelpLineFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 20:
                    fragment = new OrderReportFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 21:
                    fragment = new PGWiseReportFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 22:
                    fragment = new PGFreeReportFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 23:
                    fragment = new WastegOrderReportFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 24:
                    fragment = new VisitFrequencyReportFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 25:
                    fragment = new WastegDeliveryReportFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 26:
                    fragment = new PGWiseWastageReportFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 27:
                    fragment = new ReturnChangeDeliveryReportFragment();
                    title = getString(R.string.title_text);
                    break;
                    case 28:
                    fragment = new RetailerLedgerDetailsReportFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 29:
                    fragment = new SettingsFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 30:
                    fragment = new OfferImageFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 31:
                    fragment = new NoticeListFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 32:
                    fragment = new FoLeaveFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 33:
                    fragment = new FoFeedbackFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 34:
                    fragment = new ShirtMesurmentFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 35:
                    fragment = new FoActivityReportFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 36:
                    fragment = new ProfileFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 37:
                    fragment = new CatalogueFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 38:
                    fragment = new InOutFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 39:
                    fragment = new FullDayFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 40:
                    fragment = new ActiveInactiveFragment_New();
                    title = getString(R.string.title_text);
                    break;

                    case 41:
//                        Intent intent = new Intent(this, OnlineExamWeb.class);
//                        startActivity(intent);

                        String url ="https://onlineexam.ssgbd.com/ssforce-login?user_id="+SharePreference.getUserId(context);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);

                    break;

                    case 42:

                       String url1= "https://assessment.ssgbd.com/user_login_submit?email="+SharePreference.getAD(context)+"&password=Abcd@321";
//                       String url= "https://assessmentdev.ssgbd.com/user_login_submit?email="+"rashed.zzaman@ssgbd.com"+"&password=@Ssg12345";

                       Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                       startActivity(browserIntent1);

                    break;

                    case 43:
                        fragment = new RetailerUpdateFragment();
                    break;

                    case 44:
                        Intent ps_intent = new Intent(this, ProductStrengthActivity.class);
                        startActivity(ps_intent);
                    break;

                    case 45:
                        fragment = new TechnicianListFragment();
                    break;

                    case 46:
                        fragment = new AttendanceReportFragmentSsforce();
                    break;

                    case 47:
                        fragment = new FanQCListFragment();
                    break;

                    case 48:
                        fragment = new BundleOfferReportFragment();
                    break;

                    case 49:
                   //     fragment = new FANReplaceQCFragment();
                        Intent iintent = new Intent(this, FanQCWeb.class);
                        startActivity(iintent);
                    break;

                    case 50:
                        fragment = new LocationUpdates();

                        break;

                        case 51:
                        fragment = new ForecastEntryFragment();

                        break;
                        case 52:
                        fragment = new ForecastNewProductFragment();

                        break;

                        case 53:
                        fragment = new IncentiveFragment();

                        break;
                        case 54:
 //                    Intent loginIntent = new Intent(context, Qrc_ScannerBarcodeActivity.class);
//                     startActivity(loginIntent);

                        break;

                        case 55:
                            fragment = new VisitFragment_RP();

                        break;

                        case 56:
                            Intent trackingIntent = new Intent(context, ProductTrackingWeb.class);
                            startActivity(trackingIntent);
                        break;

                        case 57:
                            fragment = new ChangePasswordFragment();

                        break;

                case 58:
                    fragment = new ChangeProfileFragment();

                    break;

                    case 59:
                    fragment = new ReturnManageFragment();

                    break;
                    case 60:
                    fragment = new ReturnReportFragment();

                    break;
                    case 61:
                    fragment = new ReturnVsOrderReportFragment();

                    break;

                case 62:
                    fragment = new SKUWiseReportFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 63:
                    fragment = new FoCollectionFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 64:
                    fragment = new SmartOrderManageFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 65:
                    fragment = new DirectOrderManageFragment();
                    title = getString(R.string.title_text);
                    break;

                    case 66:

              Intent foforecast = new Intent(context, FOForecastWeb.class);
              startActivity(foforecast);

                    break;

                    case 67:

              Intent foforecastremark = new Intent(context, FOForecastWebRemark.class);
              startActivity(foforecastremark);

                    break;

                default:

            }

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();

                // set the toolbar title
                getSupportActionBar().setTitle(title);
            }
        }
    // for exit from apps (press back button in mobile)
    @Override
    public void onBackPressed() {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();

                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Duble tap to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

    }

    public void httpRequest(String url){

        Utility.routeDTOS.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

           // Log.e("response",response+"");
                SharePreference.setRouteData(context,response);

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

                headers.put("Authorization", "Bearer "+SharePreference.getAccesstoken(context));
                return headers;
            }
        };

        queue.add(stringRequest);

    }


    public String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null")||json.isNull(key)||json.optString(key).equalsIgnoreCase(""))
            return "";
        else
            return json.optString(key, key);
    }

    public String optStringNullCheckZero(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null")||json.isNull(key)||json.optString(key).equalsIgnoreCase(""))
            return "0";
        else
            return json.optString(key, key);
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

    public void checkAttendance() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.base_url)+"api/apps/api/attendance_check/"+SharePreference.getUserId(context) , new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                try {
                    JSONObject respjsonObj = new JSONObject(response);
                  //  Log.e("<<>>",respjsonObj+"first");
                    if(respjsonObj.getString("attendence_status").equalsIgnoreCase("true")){
                        displayView(2);

                    }else {
                        displayView(1);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("VOLLEY", error.toString());
              //  Utility.dialog.closeProgressDialog();
            }
        }) ;

        queue.add(stringRequest);
    }

    public void getNoticeList(){
        JsonRequestFormate jp = new JsonRequestFormate();
        VolleyMethods vm = new VolleyMethods();

        vm.sendRequestToServer2(context, getString(R.string.base_url)+"api/app_notic", jp.jsonGetNotice(SharePreference.getUserTypeId(context)), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject respjsonObj = new JSONObject(result);
                    JSONArray statusArray = respjsonObj.getJSONArray("notic_list");
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < statusArray.length(); i++) {
                        JSONObject routeObject = statusArray.getJSONObject(i);
                      //Log.e("noticeDTOS",routeObject+"<<>>");

                        databaseHelper.insertOrUpdateNotice(databaseHelper,
                        routeObject.getString("id"),
                        routeObject.getString("start_date"),
                        routeObject.getString("end_date"),
                        routeObject.getString("notice"),
                        routeObject.getString("status"),
                        routeObject.getString("user_type"),
                        "0");

                    }

                    noticeDTOS =  databaseHelper.getAllNotice(databaseHelper,"0");

//                    Log.e("noticeDTOS",databaseHelper.getUnreadCount(databaseHelper)+"<<size>>");
//                    Log.e("noticeDTOS",noticeDTOS.size()+"");

                    updateNotifiCount();

                } catch (JSONException je) {
                }
            }
        });
    }


    public void showServeyAlert(final Context context) {
        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        // alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher)).setTitle("Internet Alert").setMessage("Your device is not connected to internet. Connect to internet and try again.");
//        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("সার্ভে এলার্ট").setMessage("Super Star Fan সার্ভিস এবং রিপ্লেসমেন্ট সমীক্ষায় আপনাকে স্বাগতম Super Star fan সার্ভিস এবং রিপ্লেসমেন্ট সম্পর্কে আপনার মূল্যবান মতামত জানতে আমরা আগ্রহী। আপনার প্রতিক্রিয়া আমাদের সেবা উন্নত করতে সহায়তা করবে।");
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("সার্ভে এলার্ট").setMessage("এস এস জি বার্ষিক ব্যবসায়িক সম্মেলন (SSG ABC-2024)-জরিপ সম্পর্কিত আপনার অভিজ্ঞতা এবং মূল্যবান মতামত শেয়ার করুন। আপনার মতামত আমাদের আমাদের ভবিষ্যত সেবা উন্নত করতে সহায়তা করবে। অনুগ্রহ করে জরিপটি পূর্ণ করুন।");

        alertBulder.setCancelable(false);

        alertBulder.setPositiveButton("শুরু করুন", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // String url ="https://ee.kobotoolbox.org/x/vaK9ZPWc";
                String url ="https://ee.kobotoolbox.org/single/b2ee4feb559b195d30f138aecfb3d624";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        alertBulder.setNeutralButton("বাতিল", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertBulder.show();
    }


//    private void checkPermissions() {
//        String[] permissions = {
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
//        };
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
//        } else {
//            startLocationService();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
             //   startLocationService();
            }
        }
    }

//    private void startLocationService() {
//        Intent serviceIntent = new Intent(this, LocationForegroundService.class);
//        ContextCompat.startForegroundService(this, serviceIntent);
//    }
    }
