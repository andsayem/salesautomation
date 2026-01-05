package com.ssgbd.salesautomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class SCDDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_submit,btn_getdata,btn_logout;
    private Context context;
    private EditText edit_input_userid, edit_input_username,edit_input_phone;
    public RequestQueue queue;
    String smsUrl="https://880sms.com/smsapi?api_key=C20016585b5d65039143f5.68321617&type=text&contacts=";
    String smsbody="Welcome to Annual Business Conference-2022 at Heritage Resort. \nThanks for your Digital Registration.";
    String userType="";

    VolleyMethods vm = new VolleyMethods();
    JsonRequestFormate jrf = new JsonRequestFormate();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scd_dashboard_screen);
        context = this;
        queue = Volley.newRequestQueue(this);
        initUi();
    }

    private void initUi() {

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        btn_getdata = (Button) findViewById(R.id.btn_getdata);
        btn_getdata.setOnClickListener(this);

        edit_input_userid = (EditText) findViewById(R.id.edit_input_userid);
        edit_input_username = (EditText) findViewById(R.id.edit_input_username);
        edit_input_phone = (EditText) findViewById(R.id.edit_input_phone);
        edit_input_phone.setImeActionLabel("Submit", EditorInfo.IME_ACTION_DONE);
        edit_input_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (!isInternetAvailable(context)) {
                        internetAlert(context);
                    } else {
                        submit();
                        //sendSms();
                    }

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_submit:

                if (!isInternetAvailable(context)) {
                    internetAlert(context);
                } else {

                    submit();
                }
                break;

                case R.id.btn_getdata:

                if (!isInternetAvailable(context)) {
                    internetAlert(context);
                } else {
                    if (edit_input_userid.getText().length() == 0) {

                        Toast.makeText(context, "Id not found", Toast.LENGTH_SHORT).show();

                    } else {
                        getData();
                    }
                }
                break;

                case R.id.btn_logout:

               logOutAlert(context);
                break;
        }
    }

    public void submit() {
        String username = edit_input_username.getText().toString();
        String password = edit_input_phone.getText().toString();
        if (username.length() == 0) {
            edit_input_username.setError(getString(R.string.error_blank_field));
        } else if (password.length() == 0) {
            edit_input_phone.setError(getString(R.string.error_blank_field));
        } else {
            doReistration();
        }
        }
    public void getData() {

            final ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("Loading...");
            pd.show();
            vm.sendRequestToServer2(context, getResources().getString(R.string.base_url)+"api/get-registration-data", jrf.getData(edit_input_userid.getText().toString()), new VolleyCallBack() {
                @Override
                public void onSuccess(String result) {
                    try {
//                        Log.e(">>",result+"<<");
                        JSONObject jsonObject = new JSONObject(result);
                       // Log.e(">su>",jsonObject.optString("success")+result+"<<");

                        if (jsonObject.optString("success").equalsIgnoreCase("true")){
                            if (jsonObject.getString("userType").equalsIgnoreCase("guest")) {

//                                userType = jsonObject.getString("guest").toString();
                                userType = jsonObject.getString("userType").toString();
                        //        Log.e("<<>>",userType+"");

                            }else {
                                edit_input_username.setText(jsonObject.getString("userName"));
                                edit_input_phone.setText(jsonObject.getString("phoneNo"));
                                userType = jsonObject.getString("userType").toString();
                            }
                            if (jsonObject.optString("status").equalsIgnoreCase("1")){

                                edit_input_userid.setText("");
                                edit_input_username.setText("");
                                edit_input_phone.setText("");
                                Toast.makeText(context, "user already registered. ", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            edit_input_username.setText("");
                            edit_input_phone.setText("");
                            userType ="guest";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pd.dismiss();
                }
            });
    }

    public void doReistration() {

            final ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("Loading...");
            pd.show();
            vm.sendRequestToServer2(context, getResources().getString(R.string.base_url)+"api/digitalregistration", jrf.registrationFormat(edit_input_userid.getText().toString(), edit_input_username.getText().toString(),edit_input_phone.getText().toString(),userType), new VolleyCallBack() {
                @Override
                public void onSuccess(String result) {
                    try {
                     //   Log.e(">>",result+"<<");
                        sendSms();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optString("status").equalsIgnoreCase("true")){
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                          //  Log.e(">>>>",result+"<<");
                        }
                        else {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pd.dismiss();
                }
            });

    }

    public void sendSms() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, smsUrl+edit_input_phone.getText().toString()+"&senderid=SSG&msg="+smsbody, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //  Log.e("responseLOg>>", response.toString() + "<<-->>");

                if (response.equalsIgnoreCase("2001")){
                    Toast.makeText(context, "Message not sent.", Toast.LENGTH_SHORT).show();
                }else {
               Toast.makeText(context, "Message sent successfully to-"+edit_input_phone.getText().toString(), Toast.LENGTH_SHORT).show();
               edit_input_phone.setText("");
               edit_input_userid.setText("");
               edit_input_username.setText("");
               userType="";
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
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


    public void logOutAlert(final Context context) {
        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.drawable.search_hover)).setTitle("Do you realy want to logout?");

        alertBulder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                Intent loginIntent = new Intent(SCDDashboardActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();

            }
        });

        alertBulder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertBulder.show();
    }
}