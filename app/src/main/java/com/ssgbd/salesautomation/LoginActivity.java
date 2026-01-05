package com.ssgbd.salesautomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
//import com.ssgbd.salesautomation.qr.Qrc_ScannerBarcodeActivity;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private Context context;
    private EditText edit_input_username, edit_input_password;
    public RequestQueue queue;
    TextView text_privacy;

    private static String DB_NAME = "ssg.db";
    DatabaseHelper databaseHelper;
    Switch eSwitch;
    CheckBox chk_remember;
    String TAG= "LoginActivity";
    String day="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login_screen);
        context = this;
        queue = Volley.newRequestQueue(this);
        initUi();
        SimpleDateFormat timeF =  new SimpleDateFormat("hh:mm");

        final Calendar cldr=Calendar.getInstance();
        day =String.valueOf(cldr.get(Calendar.DAY_OF_MONTH));

        databaseHelper = new DatabaseHelper(context);
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }



        String myPath = Utility.DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()){
            databaseHelper.openDataBase();
        }

        databaseHelper.deleteCategoryTable(databaseHelper);
       // databaseHelper.deleteRetailerTable(databaseHelper);

        chk_remember =  (CheckBox) findViewById(R.id.chk_remember);
        chk_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    // checked

                    SharePreference.setRememberPassword(context,"1");
                }
                else
                {
                    // not checked
                    SharePreference.setRememberPassword(context,"0");

                }
            }

        });

        if (SharePreference.getRememberPassword(context).equalsIgnoreCase("1")){
            edit_input_username.setText(SharePreference.getUserLoginId(context));
            edit_input_password.setText(SharePreference.getUserLoginPassword(context));
            chk_remember.setChecked(true);
        }
    }


    private void initUi() {

        text_privacy = (TextView) findViewById(R.id.text_privacy);
        text_privacy.setText(Html.fromHtml(String.format(getString(R.string.privacy_text))));
        text_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this,PrivacyPolicyActivity_Web.class);
//                startActivity(intent);

                String url = getString(R.string.base_url)+"privacy_policy";
                try {

                    Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent1);

                }
                catch(ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }

            }
        });
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        edit_input_username = (EditText) findViewById(R.id.edit_input_username);
        edit_input_password = (EditText) findViewById(R.id.edit_input_password);
        edit_input_password.setImeActionLabel("Login", EditorInfo.IME_ACTION_DONE);
        edit_input_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (!isInternetAvailable(context)) {
                        internetAlert(context);
                    } else {
                        doLogin();
                    }

                    return true;
                }
                return false;
            }
        });


        eSwitch = (Switch) findViewById(R.id.eSwitch);
        eSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusSwitch1, statusSwitch2;
                if (eSwitch.isChecked()){
                    edit_input_password.setTransformationMethod(null);
                }else{
                    edit_input_password.setTransformationMethod(new PasswordTransformationMethod());
                }

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_login:

                if (edit_input_username.getText().toString().equalsIgnoreCase("p_scan")&&edit_input_password.getText().toString().equalsIgnoreCase("*&%ssg"+day)){

//                    Intent intent = new Intent(LoginActivity.this, Qrc_ScannerBarcodeActivity.class);
//                    startActivity(intent);
                   // ((Activity) context).finish();
                    return;
                }

                if (!Utility.isInternetAvailable(context)) {
                    Utility.internetAlert(context);
                } else {

                    doLogin();
                }
                break;
        }
    }

    public void doLogin() {

        String username = edit_input_username.getText().toString();
        String password = edit_input_password.getText().toString();
        if (username.length() == 0) {
            edit_input_username.setError(getString(R.string.error_blank_field));
        } else if (password.length() == 0) {
            edit_input_password.setError(getString(R.string.error_blank_field));
        } else {

            SharePreference.setUserLoginId(context, edit_input_username.getText().toString());
            SharePreference.setUserLoginPassword(context, edit_input_password.getText().toString());

            String url = getString(R.string.base_url)+ "apps/api/login_v2?appsusername=" + edit_input_username.getText().toString() + "&appspassword=" + edit_input_password.getText().toString() + "&appsrememberme=0";

          //  Log.e("<<>>",url+"<<>>");
          // without auth
          httpRequest(url);
         // with auth
//         loginRequest(url,edit_input_username.getText().toString(),edit_input_password.getText().toString());
        }
        }

    public void httpRequest(String url) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.e("<<>>", response+ "point_type");
                try {
                    JSONObject respjsonObj = new JSONObject(response);
                    String status = respjsonObj.getString("status");

                    if (status.equals("1")) {

                        JSONObject infoObject = respjsonObj.getJSONObject("info");
                        SharePreference.setUserId(context, optStringNullCheck(infoObject, "user_id"));
                        SharePreference.setUserName(context, optStringNullCheck(infoObject, "user_name"));
                        SharePreference.setEmployeeId(context, optStringNullCheck(infoObject, "employee_id"));
                        SharePreference.setUserGlobalId(context, optStringNullCheck(infoObject, "global_id"));
                        SharePreference.setUserTypeId(context, optStringNullCheck(infoObject, "user_type_id"));
                        SharePreference.setUserPointId(context, optStringNullCheck(infoObject, "user_point_id"));
                        SharePreference.setUserPointName(context, optStringNullCheck(infoObject, "user_point_name"));
                        SharePreference.setDesignation(context, optStringNullCheck(infoObject, "user_designation"));
                        SharePreference.setDistributorID(context, optStringNullCheck(infoObject, "distributor_id"));
                        SharePreference.setDistributorName(context, optStringNullCheck(infoObject, "distributor_name"));
                        SharePreference.setSapCode(context, optStringNullCheck(infoObject, "distributor_sap_code"));
                        SharePreference.setDivisionId(context, optStringNullCheck(infoObject, "division_id"));
                        SharePreference.setDivisionName(context, optStringNullCheck(infoObject, "division_name"));
                        SharePreference.setTerritoryId(context, optStringNullCheck(infoObject, "territory_id"));
                        SharePreference.setTerritoryName(context, optStringNullCheck(infoObject, "territory_name"));
                        SharePreference.setUserBusinessType(context, optStringNullCheck(infoObject, "user_business_type"));
                        SharePreference.setUserAD(context, optStringNullCheck(infoObject, "user_email"));
                        SharePreference.setDistributorLatLon(context, optStringNullCheck(infoObject, "lat_long"));
                        SharePreference.setIsvaluhide(context, optStringNullCheck(infoObject, "is_value_hide"));
                        SharePreference.setIsGepot(context, optStringNullCheck(infoObject, "point_type"));
                        SharePreference.setFoVisitDist(context, optStringNullCheck(infoObject, "visit_dist"));

                   //     Log.e("<<>>", optStringNullCheck(infoObject, "visit_dist")+"");
//                        if (optStringNullCheck(infoObject, "is_implemented").equalsIgnoreCase("NO")){
//                            Intent intent = new Intent(LoginActivity.this, IMSFOActivity.class);
//                            startActivity(intent);
//                            ((Activity) context).finish();
//                        }else {
                            if (optStringNullCheck(infoObject, "user_type_id").equalsIgnoreCase("12")){
                               // SharePreference.setIslogIn(context, "1");
                                Intent intent = new Intent(LoginActivity.this, DrawerMain.class);
                                startActivity(intent);
                                ((Activity) context).finish();
                            }
                            else{
                 Toast.makeText(context,"আপনি অনুমোদিত ইউজার নন।", Toast.LENGTH_SHORT).show();

                            }


                    } else {
                        Toast.makeText(context, optStringNullCheck(respjsonObj, "message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {


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

    public String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key) || json.optString(key).equalsIgnoreCase("null") || json.isNull(key) || json.optString(key).equalsIgnoreCase(""))
            return "";
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
    public void internetAlert(final Context context) {
        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher)).setTitle("Internet Alert").setMessage("Your device is not connected to internet. Connect to internet and try again.");
        alertBulder.setCancelable(false);
        alertBulder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertBulder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertBulder.show();
    }

}