package com.ssgbd.salesautomation.report.bundle;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.DatePicker;
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
import com.ssgbd.salesautomation.IMSFOActivity;
import com.ssgbd.salesautomation.LoginActivity;
import com.ssgbd.salesautomation.MyBrowser;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.SCDDashboardActivity;
import com.ssgbd.salesautomation.adapters.OfferListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.report.FoActivityReportFragment;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class BundleOfferReportFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    String ROUTE_ID="";
   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    TextView txt_category_list;
    public RequestQueue queue;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    OfferListRecyclerAdapter offerListRecyclerAdapter;
    ArrayList<CategoryDTO> yearDTOS = new ArrayList<>();

    private Dialog wdialog;
    String CATEGORY_ID="";
    private WebView webView;
    LinearLayout linlay_exit_from_web;
    WebSettings webSettings;
    Spinner spinnerYear;
    TextView offTypeTv;
    SimpleDateFormat presentYear;
    Date c;
    int  year;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.bundle_offer_report_fragment, container, false);
        queue = Volley.newRequestQueue(getActivity());
        presentYear = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        c = Calendar.getInstance().getTime();

      //  getOfferList(String.valueOf(year));
        txt_category_list = (TextView) rootView.findViewById(R.id.txt_category_list);
        txt_category_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOfferListDialog();
            }
        });



        webView = (WebView) rootView.findViewById(R.id.webViewID);

        CategoryDTO p = new CategoryDTO();
        p.setId(presentYear.format(c));
        p.setName(presentYear.format(c));
        yearDTOS.add(p);

        CategoryDTO c13 = new CategoryDTO();
        c13.setId("2024");
        c13.setName("2024");
        yearDTOS.add(c13);
        CategoryDTO c12 = new CategoryDTO();
        c12.setId("2023");
        c12.setName("2023");
        yearDTOS.add(c12);
        CategoryDTO c11 = new CategoryDTO();
        c11.setId("2022");
        c11.setName("2022");
        yearDTOS.add(c11);
        CategoryDTO c1 = new CategoryDTO();
        c1.setId("2021");
        c1.setName("2021");
        yearDTOS.add(c1);





        spinnerYear = (Spinner) rootView.findViewById(R.id.spinnerYear);
        YearArrayAdapter adapter = new YearArrayAdapter(getActivity(),R.layout.customspinneritem, yearDTOS);

        spinnerYear.setAdapter(adapter);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

                // shopType = categoryDTOS.get(pos).getId();
                // Log.e(">>>shoptype><<>>",yearDTOS.get(pos).getId()+"");
              //  year = Integer.getInteger(yearDTOS.get(pos).getId());

                getOfferList(yearDTOS.get(pos).getId());

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    private void showOfferListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final TextView tttt;

        final RelativeLayout rlDialogCross;
        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_search);
        route_list_recyclerView=(RecyclerView) wdialog.findViewById(R.id.route_list_recyclerView);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog);

        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wdialog.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialog.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        offerListRecyclerAdapter = new OfferListRecyclerAdapter( categoryDTOS,getActivity());
        route_list_recyclerView.setAdapter(offerListRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (!Utility.isInternetAvailable(getActivity())) {
                    Utility.internetAlert(getActivity());

                } else {

              CATEGORY_ID=categoryDTOS.get(position).getId();
              txt_category_list.setText(categoryDTOS.get(position).getName());
              webSettings = webView.getSettings();
              webSettings.setJavaScriptEnabled(true); // javascript enable
              Toast.makeText(getActivity(), "একটু  অপেক্ষা করুন।", Toast.LENGTH_LONG).show();
              webView.setWebViewClient(new MyBrowser()); // for open android all page same android mobile
               String url = getString(R.string.base_url) + "apps/retailer-bundle-offer-report-list?point_id="+SharePreference.getUserPointId(getActivity())+"&bundle_offer_id="+categoryDTOS.get(position).getId()+"&login_user_id="+SharePreference.getUserLoginId(getActivity())+"&login_password="+SharePreference.getUserLoginPassword(getActivity());
               webView.loadUrl(url);
               wdialog.dismiss();

                }
            }
        }));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = etSearch.getText().toString().toLowerCase();
                offerListRecyclerAdapter.filter(query);
            }
        });

        wdialog.show();
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

    public  String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null"))
            return "";
        else
            return json.optString(key, key);
    }
    public void getOfferList(String year) {
        categoryDTOS.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,getString(R.string.base_url)+"apps/bundle_offer_list?year="+year+"&business_id="+SharePreference.getUserBusinessType(getActivity()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //  Log.e("<<>>>>",response+"<<<<");

                try {
                    JSONObject respjsonObj = new JSONObject(response);

                    JSONArray jsonArray = respjsonObj.getJSONArray("data");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject  = jsonArray.getJSONObject(i);
                       CategoryDTO categoryDTO = new CategoryDTO();
                        categoryDTO.setId(jsonObject.getString("iId"));
                        categoryDTO.setName(jsonObject.getString("name"));

                        categoryDTOS.add(categoryDTO);
                    }



                } catch (JSONException e) {

                    //   Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                //    Log.e("<<>>>>",e.toString()+"<<<<");

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
                //  Toast.makeText(context, "SSL server error.", Toast.LENGTH_SHORT).show();

             //   Log.e("<<>>>>",error.toString()+"<<<<");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
    }
    public class YearArrayAdapter extends ArrayAdapter<String> {

        private final LayoutInflater mInflater;
        private final Context mContext;
        private final ArrayList<CategoryDTO> items;
        private final int mResource;

        public YearArrayAdapter(@NonNull Context context, @LayoutRes int resource,
                                @NonNull ArrayList objects) {
            super(context, resource, 0, objects);

            mContext = context;
            mInflater = LayoutInflater.from(context);
            mResource = resource;
            items = objects;
        }
        @Override
        public View getDropDownView(int position, @Nullable View convertView,
                                    @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        @Override
        public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        private View createItemView(int position, View convertView, ViewGroup parent){
            final View view = mInflater.inflate(mResource, parent, false);

            offTypeTv = (TextView) view.findViewById(R.id.offer_type_txt);

            CategoryDTO offerData = items.get(position);

            offTypeTv.setText(items.get(position).getName());

            return view;
        }
    }
}
