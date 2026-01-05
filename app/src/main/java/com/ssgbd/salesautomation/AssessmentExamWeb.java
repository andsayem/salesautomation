package com.ssgbd.salesautomation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
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

public class AssessmentExamWeb extends AppCompatActivity {

    private WebView webView;
    Context context;
    String TAG = "BucketAmountWeb";
    LinearLayout linlay_exit_from_web;
    AlertDialog.Builder alertBulder;
    long pageLoadStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        setContentView(R.layout.bucket_amount_web);
        context = this;

        // for exit button
        linlay_exit_from_web = (LinearLayout) findViewById(R.id.linlay_exit_from_web);
        linlay_exit_from_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(1, returnIntent);
                AssessmentExamWeb.this.finish();
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

        Toast.makeText(context, getString(R.string.loading_text), Toast.LENGTH_LONG).show();

//        alertBulder = new AlertDialog.Builder(context);
//        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("Wait").setMessage(getString(R.string.process_order_alert));
//        alertBulder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//   alertBulder.show();

        webView.setWebViewClient(new MyBrowser());
        // for open android all page same android mobile
         String url ="https://assessmentdev.ssgbd.com/";

       // String url ="https://www.prothomalo.com/";
      //  Log.e("url",url+"<<>>"+"");
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        webView.loadUrl(url);
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
            pageLoadStartTime = System.currentTimeMillis();
        //SHOW LOADING IF IT ISNT ALREADY VISIBLE
         //   Log.e("onPageStarted","<<>>"+"");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
           // Log.e("onPageFinished","<<>>"+"");
            long pageLoadTime = System.currentTimeMillis() - pageLoadStartTime;
           // Log.e("LoadTI>>",pageLoadTime+"<<>>"+"");
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
}
