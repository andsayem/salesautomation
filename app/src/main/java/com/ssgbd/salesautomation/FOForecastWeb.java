package com.ssgbd.salesautomation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ssgbd.salesautomation.utils.SharePreference;

public class FOForecastWeb extends AppCompatActivity {

    private WebView webView;
    Context context;
    String TAG = "Fo Forecast";
    LinearLayout linlay_exit_from_web;
    AlertDialog.Builder alertBulder;
    boolean isPageError = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        setContentView(R.layout.fo_forecast_web);
        context = this;


        if (!isInternetAvailable(context)) {
            internetAlert(context);
            finish();
        }else{
            //  Log.e("<'>",finalobject+"<>");

        }

        // for exit button
        linlay_exit_from_web = (LinearLayout) findViewById(R.id.linlay_exit_from_web);
        linlay_exit_from_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(1, returnIntent);
                FOForecastWeb.this.finish();
            }
        });

        webView = (WebView) findViewById(R.id.webViewID);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // javascript enable
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }


        webView.setWebViewClient(new MyBrowser());
        // for open android all page same android mobile
        String url = getResources().getString(R.string.base_url)+"api/fo-sales-forecasting/"+SharePreference.getUserId(context)+"/forecasting-entry";
        https://ssforcenewdev.ssgbd.com/api/fo-sales-forecasting/1437/forecasting-entry
       // Log.e("<<>>",url+"");
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        if (!isInternetAvailable(context)) {
            internetAlert(context);

        } else {
            Toast.makeText(context, "একটু অপেক্ষা করুন। ", Toast.LENGTH_LONG).show();
            webView.loadUrl(url);

        }

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(
                WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            isPageError = false;
        //SHOW LOADING IF IT ISNT ALREADY VISIBLE
         //   Log.e("onPageStarted","<<>>"+"");
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (isPageError){
                view.setVisibility(View.GONE);
//                txtError.setVisibility(View.VISIBLE);
//                txtError.setText("error message");
            }

        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            isPageError = true;
        }

    }

//    @Override
//    public void onBackPressed()
//    {
//        if (webView.canGoBack())
//        {
//            webView.goBack();
//        }
//        else
//        {
//            super.onBackPressed();
//        }
//    }

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
