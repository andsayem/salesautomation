package com.ssgbd.salesautomation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.drawer.fragment.VisitFragment;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.gps.GPSTracker;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PlatformActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    Button btn_app,btn_web;

    private Button buttonStartThread;
    private volatile boolean stopThread = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.platform_screen);
        context = this;
        buttonStartThread = findViewById(R.id.button_start_thread);
      //  Log.e("response",SharePreference.getAppMode(context)+"response");
        initUi();
     //   showProminentDialog();
        if (SharePreference.getAppMode(context).equalsIgnoreCase("1")){
            btn_web.setVisibility(View.VISIBLE);
            btn_app.setVisibility(View.GONE);
        }else if (SharePreference.getAppMode(context).equalsIgnoreCase("2")){
            btn_web.setVisibility(View.GONE);
            btn_app.setVisibility(View.VISIBLE);
        }else if (SharePreference.getAppMode(context).equalsIgnoreCase("3")){
            btn_web.setVisibility(View.VISIBLE);
            btn_app.setVisibility(View.VISIBLE);
        }
        getAppMode();

    }

    private void initUi() {


        btn_app = (Button) findViewById(R.id.btn_app);
        btn_app.setOnClickListener(this);
        btn_web = (Button) findViewById(R.id.btn_web);
        btn_web.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_app:

                if (SharePreference.getIslogIn(context).equalsIgnoreCase("1")){

                    if (SharePreference.getUserTypeId(context).equalsIgnoreCase("12")){
                    Intent mainIntent = new Intent(PlatformActivity.this, DrawerMain.class);
                    startActivity(mainIntent);
                    this.finish();
                    }else {
                        Intent mainIntent = new Intent(PlatformActivity.this, SCDDashboardActivity.class);
                        startActivity(mainIntent);
                        this.finish();
                    }
                }else {
                    Intent mainIntent = new Intent(PlatformActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    this.finish();
                }
                break;
            case R.id.btn_web:

                if (!isInternetAvailable(context)) {
                     internetAlert(context);
                }else{
                    Intent mainIntent1 = new Intent(PlatformActivity.this, MainActivity_Web.class);
                    startActivity(mainIntent1);
                    this.finish();
                }

                break;
        }
    }
    public  boolean isInternetAvailable(Context context) {
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

    public void getAppMode(){

        RequestQueue queue;
        //
      String  url = getResources().getString(R.string.base_url)+"api/app_mode";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                      //  Log.e("response__",response+"");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            SharePreference.setAppMode(context,jsonObject.getString("app_mode"));

                            if (jsonObject.getString("app_mode").equalsIgnoreCase("1")){
                                btn_web.setVisibility(View.VISIBLE);
                                btn_app.setVisibility(View.GONE);
                            }else if (jsonObject.getString("app_mode").equalsIgnoreCase("2")){
                                btn_web.setVisibility(View.GONE);
                                btn_app.setVisibility(View.VISIBLE);
                            }else if (jsonObject.getString("app_mode").equalsIgnoreCase("3")){
                                btn_web.setVisibility(View.VISIBLE);
                                btn_app.setVisibility(View.VISIBLE);
                            }
                        }catch (JSONException je){
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "aInterfaceError :  নেটওয়ার্ক সমস্যা .", Toast.LENGTH_SHORT).show();

                      //  Log.e("error>>",error.toString()+"");
                    }
                }
        ) {
        };
        queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        postRequest.setShouldCache(false);

        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
     //   postRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));

        queue.add(postRequest);
    }



    public void startThread(View view) {
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable(10);
        new Thread(runnable).start();

    }

    public void stopThread(View view) {
        stopThread = true;
    }

    class ExampleThread extends Thread {
        int seconds;

        ExampleThread(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            for (int i = 0; i < seconds; i++) {
              //  Log.e("TAG", "startThread: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ExampleRunnable implements Runnable {
        int seconds;

        ExampleRunnable(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            for (int i = 0; i < seconds; i++) {
                if (stopThread)
                    return;
                if (i == 5) {
                    /*
                    Handler threadHandler = new Handler(Looper.getMainLooper());
                    threadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                    */
                    /*
                    buttonStartThread.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                    */
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                }
             //   Log.e("TAG", "startThread: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

