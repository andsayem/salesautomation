package com.ssgbd.salesautomation;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicyActivity_Web extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//      getWindow().setFlags(
//      WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//      WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        setContentView(R.layout.privacy_policy_webview);
        webView = (WebView) findViewById(R.id.webViewID);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // javascript enable

//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setAppCachePath("/data/data/" + getPackageName() + "/cache");
//        webView.getSettings().setAppCacheEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

//        if (Build.VERSION.SDK_INT >= 19) {
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }
//        else {
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }

       // webView.setWebViewClient(new WebViewClient()); // for open android all page same android mobile
        webView.loadUrl("https://ssforce.ssgbd.com/demo/downloadExcel/Privacy_Policy.pdf");
       //webView.loadUrl("https://ssforce.ssgbd.com/");
      // Log.e("MainActivity_Web",getString(R.string.base_url)+"<<MainAb");
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
