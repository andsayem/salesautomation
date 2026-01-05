package com.ssgbd.salesautomation.report.order;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.ssgbd.salesautomation.MyBrowser;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.alert.CustomAlert;
import com.ssgbd.salesautomation.utils.SharePreference;

public class ReportWeb extends AppCompatActivity {

    private WebView webView;
    Context context;
    String TAG = "BucketAmountWeb";
    LinearLayout linlay_exit_from_web;
    CustomAlert customAlert = new CustomAlert();
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
        linlay_exit_from_web = (LinearLayout) findViewById(R.id.linlay_exit_from_web);
        linlay_exit_from_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReportWeb.this.finish();
            }
        });

        Toast.makeText(context, "অনুগ্রহ করে অপেক্ষা করুন ডাটা  প্রসেস হচ্ছে।", Toast.LENGTH_LONG).show();
   //     Log.e(TAG,SharePreference.getUserLoginId(context)+"");
        webView.setWebViewClient(new MyBrowser()); // for open android all page same android mobile
        String url = getString(R.string.base_url)+getIntent().getStringExtra("url");
       // Log.e(TAG,url+"");

        if (!customAlert.isInternetAvailable(context)) {
            customAlert.internetAlert(context);

        } else {
            webView.loadUrl(url);

        }


    }

}
