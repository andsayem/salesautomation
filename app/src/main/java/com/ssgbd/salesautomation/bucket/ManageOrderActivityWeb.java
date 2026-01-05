package com.ssgbd.salesautomation.bucket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.utils.SharePreference;

public class ManageOrderActivityWeb extends AppCompatActivity {

    private WebView webView;
    Context context;
    String TAG = "BucketAmountWeb";
    LinearLayout linlay_exit_from_web;
    boolean isPageError = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bucket_amount_web);
        context = this;
        webView = (WebView) findViewById(R.id.webViewID);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // javascript enable

        webView.setWebViewClient(new WebViewClient()); // for open android all page same android mobile
        String url = getResources().getString(R.string.base_url)+"tabs/loginom/"+getIntent().getStringExtra("orderid")+"/"+getIntent().getStringExtra("retailerId")+"/"+getIntent().getStringExtra("routeId")+"/part_1/"+ SharePreference.getUserLoginId(context)+"/"+SharePreference.getUserLoginPassword(context);

//Log.e("<<>>",url+"");


        linlay_exit_from_web = (LinearLayout) findViewById(R.id.linlay_exit_from_web);
        linlay_exit_from_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ManageOrderActivityWeb.this.finish();
            }
        });

        if (!isInternetAvailable(context)) {
            internetAlert(context);

        } else {
            Toast.makeText(context, "আপনার রিপোর্ট লোড হচ্ছে। অপেক্ষা করুন।", Toast.LENGTH_SHORT).show();
            webView.loadUrl(url);

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
        final AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("Internet Alert").setMessage("Your device is not connected to internet. Connect to internet and try again.");

        alertBulder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        alertBulder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertBulder.setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertBulder.show();
    }
}
