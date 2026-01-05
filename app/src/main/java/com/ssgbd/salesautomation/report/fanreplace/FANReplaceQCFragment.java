package com.ssgbd.salesautomation.report.fanreplace;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.MyBrowser;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.OfferListRecyclerAdapter;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FANReplaceQCFragment extends Fragment implements View.OnClickListener, eeeee {

    View rootView;

    private WebView webView;
    LinearLayout linlay_exit_from_web;
    WebSettings webSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fan_replace_qc_fragment, container, false);

        webView = (WebView) rootView.findViewById(R.id.webViewID);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // javascript enable

        Toast.makeText(getActivity(), "একটু  অপেক্ষা করুন।", Toast.LENGTH_LONG).show();
        webView.setWebViewClient(new MyBrowser()); // for open android all page same android mobile
        String url = getString(R.string.base_url) + "api/fan-replacement/requisition?login_user_id="+SharePreference.getUserLoginId(getActivity())+"&login_password="+SharePreference.getUserLoginPassword(getActivity());

     //   Log.e("<<>>",url+"<<>>");

        if (!Utility.isInternetAvailable(getActivity())) {
            Utility.internetAlert(getActivity());

        } else {

            webView.loadUrl(url);
        }
        return rootView;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            getActivity().onBackPressed();
        }
    }


}
