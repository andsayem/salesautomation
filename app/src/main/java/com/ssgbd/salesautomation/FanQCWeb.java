package com.ssgbd.salesautomation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

public class FanQCWeb extends AppCompatActivity {


    private WebView webView;
    LinearLayout linlay_exit_from_web;
    WebSettings webSettings;
    Context context;
    private static final String TAG = "FanQCWeb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//
//        setContentView(R.layout.fan_replace_qc_fragment);
//        context = this;
//        webView = (WebView) findViewById(R.id.webViewID);
//        webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true); // javascript enable
//
//        Toast.makeText(context, "একটু  অপেক্ষা করুন।", Toast.LENGTH_LONG).show();
//        webView.setWebViewClient(new WebViewClient()); // for open android all page same android mobile
//        String url = getString(R.string.base_url) + "api/fan-replacement/requisition?login_user_id="+SharePreference.getUserLoginId(context)+"&login_password="+SharePreference.getUserLoginPassword(context);
//        Log.e("<<>>",url+"<<>>");
//        webView.loadUrl(url);
//
//    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    if (webView.canGoBack()) {
//                        webView.goBack();
//                    } else {
//                        finish();
//                    }
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//}
        super.onCreate(savedInstanceState);
        setupFullScreen();
        setContentView(R.layout.fan_replace_qc_fragment);

        context = this;
        initializeWebView();
        loadWebContent();
    }

    private void setupFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Only enable hardware acceleration if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    private void initializeWebView() {
        webView = findViewById(R.id.webViewID);
        WebSettings webSettings = webView.getSettings();

        // Essential settings
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        // Cache settings
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
     //   webSettings.setAppCacheEnabled(true);

        // Desktop-like user agent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            String desktopUserAgent = webSettings.getUserAgentString()
                    .replace("Mobile", "")
                    .replace("Android", "Desktop");
            webSettings.setUserAgentString(desktopUserAgent);
        }

        // Mixed content handling for Android 5.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        webView.setWebViewClient(new CustomWebViewClient());
    }

    private void loadWebContent() {
        String userId = SharePreference.getUserLoginId(context);
        String password = SharePreference.getUserLoginPassword(context);
        String url = getString(R.string.base_url) +
                "api/fan-replacement/requisition?login_user_id=" + userId +
                "&login_password=" + password;

        Log.d(TAG, "Loading URL: " + url);
        Toast.makeText(context, "একটু অপেক্ষা করুন...", Toast.LENGTH_LONG).show();

        // Clear cache if needed
        webView.clearCache(true);
        webView.loadUrl(url);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            // Show loading indicator if needed
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // Hide loading indicator
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.e(TAG, "WebView error: " + description);
            Toast.makeText(context, "লোড করতে সমস্যা হচ্ছে: " + description,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}