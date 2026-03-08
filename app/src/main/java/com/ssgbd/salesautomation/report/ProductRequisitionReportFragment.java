package com.ssgbd.salesautomation.report;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.utils.SharePreference;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProductRequisitionReportFragment extends Fragment {

    private View rootView;

    private WebView webViewReport;

    private TextView txtFromDate, txtToDate, txtSearch;

    private String FROMDATE, TODATE;

    private DatePickerDialog picker;

    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.product_requisition_fragment, container, false);

        txtFromDate = rootView.findViewById(R.id.txt_fromdate);
        txtToDate = rootView.findViewById(R.id.txt_todate);
        txtSearch = rootView.findViewById(R.id.txt_search);
        webViewReport = rootView.findViewById(R.id.webViewReport);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);

        setupWebView();

        initDate();

        txtFromDate.setOnClickListener(v -> showDatePicker(true));
        txtToDate.setOnClickListener(v -> showDatePicker(false));
        txtSearch.setOnClickListener(v -> getReport(FROMDATE, TODATE));

        // Initial load
        getReport(FROMDATE, TODATE);

        return rootView;
    }

    private void setupWebView() {
        WebSettings webSettings = webViewReport.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webViewReport.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }
        });
    }

    private void initDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        // First day of current month
        cal.set(Calendar.DAY_OF_MONTH, 1);
        FROMDATE = sdf.format(cal.getTime());

        // Today
        cal = Calendar.getInstance();
        TODATE = sdf.format(cal.getTime());

        txtFromDate.setText(FROMDATE);
        txtToDate.setText(TODATE);
    }

    private void showDatePicker(boolean isFrom) {
        Calendar cal = Calendar.getInstance();

        picker = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {

                    Calendar selected = Calendar.getInstance();
                    selected.set(year, monthOfYear, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(selected.getTime());

                    if (isFrom) {
                        FROMDATE = formattedDate;
                        txtFromDate.setText(FROMDATE);
                    } else {
                        TODATE = formattedDate;
                        txtToDate.setText(TODATE);
                    }

                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        picker.show();
    }

    private void getReport(String fromDate, String toDate) {
        try {
            String userId = "6382"; // বা SharePreference.getUserId(getActivity());
            String url = getString(R.string.base_url) + "api/apps/product-requisition-report-list";

            // JSON তৈরি
            JSONObject info = new JSONObject();
            info.put("foid", Integer.parseInt(userId));
            info.put("from_date", fromDate);
            info.put("to_date", toDate);
            info.put("pg_type", "");

            JSONObject main = new JSONObject();
            main.put("info", info);

            // WebView POST expects byte[]
            webViewReport.postUrl(url, main.toString().getBytes("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}