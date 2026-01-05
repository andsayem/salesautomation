package com.ssgbd.salesautomation.report;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.MyBrowser;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.OrderReportListAdapter;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.bucket.BucketAmountWeb;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.OrderRepotListDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
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

//import static com.google.android.gms.internal.zzahg.runOnUiThread;


public class VisitFrequencyReportFragment extends Fragment implements View.OnClickListener{

    View rootView;
    VolleyMethods vm = new VolleyMethods();
    String ROUTE_ID="";
  //  private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<OrderRepotListDTO> orderRepotListDTOS = new ArrayList<>();
    TextView txt_fromdate,txt_todate,txt_route,txt_search;
    String formattedDate="";
    DatePickerDialog picker;
    String YEAR="",MONTH="";
    private WebView webView;
    Spinner spinnerOfferType,spinnerOfferType1;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<CategoryDTO> categoryDTOS1 = new ArrayList<>();
    boolean isPageError = false;
    private Dialog wdialog;
    RouteRecyclerAdapter routeRecyclerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.visit_frequency_report_fragment, container, false);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        CategoryDTO c1 = new CategoryDTO();
        c1.setId("0");
        c1.setName("Select Year");
        categoryDTOS.add(c1);
        CategoryDTO c11 = new CategoryDTO();
        c11.setId("1");
        c11.setName("2018");
        categoryDTOS.add(c11);
        CategoryDTO c12 = new CategoryDTO();
        c12.setId("2");
        c12.setName("2019");
        categoryDTOS.add(c12);
        CategoryDTO c13 = new CategoryDTO();
        c13.setId("3");
        c13.setName("2020");
        categoryDTOS.add(c13);
        CategoryDTO c14 = new CategoryDTO();
        c14.setId("4");
        c14.setName("2021");
        categoryDTOS.add(c14);
        CategoryDTO c15 = new CategoryDTO();
        c15.setId("5");
        c15.setName("2022");
        categoryDTOS.add(c15);
        CategoryDTO c16 = new CategoryDTO();
        c16.setId("6");
        c16.setName("2023");
        categoryDTOS.add(c16);

        CategoryDTO a = new CategoryDTO();
        a.setId("0");
        a.setName("Select Month");
        categoryDTOS1.add(a);
        CategoryDTO a1 = new CategoryDTO();
        a1.setId("01");
        a1.setName("January");
        categoryDTOS1.add(a1);
        CategoryDTO a2 = new CategoryDTO();
        a2.setId("02");
        a2.setName("February");
        categoryDTOS1.add(a2);
        CategoryDTO a3 = new CategoryDTO();
        a3.setId("03");
        a3.setName("March");
        categoryDTOS1.add(a3);
        CategoryDTO a4 = new CategoryDTO();
        a4.setId("04");
        a4.setName("April");
        categoryDTOS1.add(a4);
        CategoryDTO a5 = new CategoryDTO();
        a5.setId("05");
        a5.setName("May");
        categoryDTOS1.add(a5);
        CategoryDTO a6 = new CategoryDTO();
        a6.setId("06");
        a6.setName("June");
        categoryDTOS1.add(a6);
        CategoryDTO a7 = new CategoryDTO();
        a7.setId("07");
        a7.setName("July");
        categoryDTOS1.add(a7);
        CategoryDTO a8 = new CategoryDTO();
        a8.setId("08");
        a8.setName("August");
        categoryDTOS1.add(a8);
        CategoryDTO a9= new CategoryDTO();
        a9.setId("09");
        a9.setName("September");
        categoryDTOS1.add(a9);
        CategoryDTO a10 = new CategoryDTO();
        a10.setId("10");
        a10.setName("October");
        categoryDTOS1.add(a10);
        CategoryDTO a11 = new CategoryDTO();
        a11.setId("11");
        a11.setName("November");
        categoryDTOS1.add(a11);
        CategoryDTO a12 = new CategoryDTO();
        a12.setId("12");
        a12.setName("December");
        categoryDTOS1.add(a12);


        txt_route = (TextView) rootView.findViewById(R.id.txt_route);
        txt_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRouteListDialog();
            }
        });

        webView = (WebView) rootView.findViewById(R.id.webViewID);
        txt_search = (TextView) rootView.findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utility.isInternetAvailable(getActivity())) {
                    Utility.internetAlert(getActivity());

                } else {

                if (YEAR.equalsIgnoreCase("")||MONTH.equalsIgnoreCase("")||ROUTE_ID.equalsIgnoreCase("")){

                    Toast.makeText(getActivity(), "Please select Year,Month and Route.", Toast.LENGTH_SHORT).show();
               return;
                }

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true); // javascript enable

                Toast.makeText(getActivity(), "Please wait processing your request....", Toast.LENGTH_LONG).show();
              //  webView.setWebViewClient(new WebViewClient()); // for open android all page same android mobile
                    webView.setWebViewClient(new MyBrowser());


                    String url = getString(R.string.base_url)+"apps-visit-frequency-report?point="+SharePreference.getUserPointId(getActivity())+"&username="+SharePreference.getUserLoginId(getActivity())+"&password="+SharePreference.getUserLoginPassword(getActivity())+"&year="+YEAR+"&month="+MONTH+"&route="+ROUTE_ID;
                webView.loadUrl(url);


            }}
        });

       spinnerOfferType = (Spinner) rootView.findViewById(R.id.spinnerOfferType);
       CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
                R.layout.customspinneritem, categoryDTOS);

        spinnerOfferType.setAdapter(adapter);

        spinnerOfferType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

                MONTH=categoryDTOS.get(pos).getName();
               // TYPE_ID = categoryDTOS.get(pos).getId();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerOfferType1 = (Spinner) rootView.findViewById(R.id.spinnerOfferType1);
       CustomArrayAdapter adapter1 = new CustomArrayAdapter(getActivity(),
                R.layout.customspinneritem, categoryDTOS1);

        spinnerOfferType1.setAdapter(adapter1);

        spinnerOfferType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

                YEAR=categoryDTOS1.get(pos).getId();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    private void showRouteListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final TextView tv_dialog_title;

        final RelativeLayout rlDialogCross;

        Utility.routeDTOS.clear();
        try {
            JSONObject respjsonObj = new JSONObject(SharePreference.getRouteData(getActivity()));
            JSONArray routeArray = respjsonObj.getJSONArray("route");
            for (int i = 0; i < routeArray.length(); i++) {
                JSONObject routeObject = routeArray.getJSONObject(i);
                RouteDTO routeDTO = new RouteDTO();
                routeDTO.setPoint_id(routeObject.getString("point_id"));
                routeDTO.setTerritory_id(routeObject.getString("territory_id"));
                routeDTO.setRname(routeObject.getString("rname"));
                routeDTO.setRoute_id(routeObject.getString("route_id"));
                Utility.routeDTOS.add(routeDTO);
            }
        }catch (JSONException je){
        }


        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_search);
        route_list_recyclerView=(RecyclerView) wdialog.findViewById(R.id.route_list_recyclerView);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog);
        tv_dialog_title = (TextView) wdialog.findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText("Please select a route");
        wdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        routeRecyclerAdapter = new RouteRecyclerAdapter( Utility.routeDTOS,getActivity());
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                wdialog.dismiss();
                ROUTE_ID = Utility.routeDTOS.get(position).getRoute_id();
                txt_route.setText(Utility.routeDTOS.get(position).getRname());
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
                routeRecyclerAdapter.filter(query);
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

    public class CustomArrayAdapter extends ArrayAdapter<String> {

        private final LayoutInflater mInflater;
        private final Context mContext;
        private final ArrayList<CategoryDTO> items;
        private final int mResource;

        public CustomArrayAdapter(@NonNull Context context, @LayoutRes int resource,
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

            TextView offTypeTv = (TextView) view.findViewById(R.id.offer_type_txt);

            CategoryDTO offerData = items.get(position);

            offTypeTv.setText(items.get(position).getName());

            return view;
        }
    }




}
