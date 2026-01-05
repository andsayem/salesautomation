package com.ssgbd.salesautomation;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyBrowser extends WebViewClient {

    boolean isPageError = false;
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
