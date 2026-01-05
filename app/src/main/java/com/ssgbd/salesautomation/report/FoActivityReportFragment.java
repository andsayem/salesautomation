package com.ssgbd.salesautomation.report;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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

import com.ssgbd.salesautomation.MyBrowser;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.OrderRepotListDTO;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.retailer.NewRetailerActivity;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FoActivityReportFragment extends Fragment implements View.OnClickListener{

    View rootView;
    private WebView webView;
    Spinner spinnerYear;
    Spinner spinnerMonth;
    ArrayList<CategoryDTO> yearDTOS = new ArrayList<>();
    String year="",month="";
    ArrayList<CategoryDTO> monthDTOS = new ArrayList<>();
    TextView offTypeTv;
    Button btn_search;
    String url="";
    SimpleDateFormat presentYear;
    SimpleDateFormat presentMonth,presentMonth1;
    Date c;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fo_activity_report_fragment, container, false);
        c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
         presentMonth = new SimpleDateFormat("MMMM");
         presentMonth1 = new SimpleDateFormat("MM");
         presentYear = new SimpleDateFormat("yyyy");

      //  Log.e("formattedDate",df.format(c)+ presentMonth.format(c)+presentYear.format(c)+"<<>>");

        btn_search = (Button) rootView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        webView = (WebView) rootView.findViewById(R.id.webViewID);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyBrowser());

        CategoryDTO p = new CategoryDTO();
        p.setId(presentYear.format(c));
        p.setName(presentYear.format(c));
        yearDTOS.add(p);

        CategoryDTO c16 = new CategoryDTO();
        c16.setId("2025");
        c16.setName("2025");
        yearDTOS.add(c16);

        CategoryDTO c15 = new CategoryDTO();
        c15.setId("2024");
        c15.setName("2024");
        yearDTOS.add(c15);
        CategoryDTO c14 = new CategoryDTO();
        c14.setId("2023");
        c14.setName("2023");
        yearDTOS.add(c14);
        CategoryDTO c13 = new CategoryDTO();
        c13.setId("2022");
        c13.setName("2022");
        yearDTOS.add(c13);
        CategoryDTO c12 = new CategoryDTO();
        c12.setId("2021");
        c12.setName("2021");
        yearDTOS.add(c12);
        CategoryDTO c11 = new CategoryDTO();
        c11.setId("2020");
        c11.setName("2020");
        yearDTOS.add(c11);
        CategoryDTO c1 = new CategoryDTO();
        c1.setId("2019");
        c1.setName("2019");
        yearDTOS.add(c1);

        spinnerYear = (Spinner) rootView.findViewById(R.id.spinnerYear);
        YearArrayAdapter adapter = new YearArrayAdapter(getActivity(),R.layout.customspinneritem, yearDTOS);

        spinnerYear.setAdapter(adapter);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

        // shopType = categoryDTOS.get(pos).getId();
       // Log.e(">>>shoptype><<>>",yearDTOS.get(pos).getId()+"");
        year = yearDTOS.get(pos).getId();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        CategoryDTO ww = new CategoryDTO();
        ww.setId(presentMonth1.format(c));
        ww.setName( presentMonth.format(c));
        monthDTOS.add(ww);
        CategoryDTO c1m = new CategoryDTO();
        c1m.setId("01");
        c1m.setName("January");
        monthDTOS.add(c1m);
        CategoryDTO c2m = new CategoryDTO();
        c2m.setId("02");
        c2m.setName("February");
        monthDTOS.add(c2m);
        CategoryDTO c3m = new CategoryDTO();
        c3m.setId("03");
        c3m.setName("March");
        monthDTOS.add(c3m);
        CategoryDTO c4m = new CategoryDTO();
        c4m.setId("04");
        c4m.setName("April");
        monthDTOS.add(c4m);
        CategoryDTO c5m = new CategoryDTO();
        c5m.setId("05");
        c5m.setName("May");
        monthDTOS.add(c5m);
        CategoryDTO c6m = new CategoryDTO();
        c6m.setId("06");
        c6m.setName("June");
        monthDTOS.add(c6m);
        CategoryDTO c7m = new CategoryDTO();
        c7m.setId("07");
        c7m.setName("July");
        monthDTOS.add(c7m);
        CategoryDTO c8m = new CategoryDTO();
        c8m.setId("08");
        c8m.setName("August");
        monthDTOS.add(c8m);
        CategoryDTO c9m = new CategoryDTO();
        c9m.setId("09");
        c9m.setName("September");
        monthDTOS.add(c9m);
        CategoryDTO c10m = new CategoryDTO();
        c10m.setId("10");
        c10m.setName("October");
        monthDTOS.add(c10m);
        CategoryDTO c11m = new CategoryDTO();
        c11m.setId("11");
        c11m.setName("November");
        monthDTOS.add(c11m);
        CategoryDTO c12m = new CategoryDTO();
        c12m.setId("12");
        c12m.setName("December");
        monthDTOS.add(c12m);

        spinnerMonth = (Spinner) rootView.findViewById(R.id.spinnerMonth);
        YearArrayAdapter adapter1 = new YearArrayAdapter(getActivity(),
                R.layout.customspinneritem, monthDTOS);

        spinnerMonth.setAdapter(adapter1);

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

                 url = getString(R.string.base_url) + "api/report/fo-activity-report-list?userid=" + SharePreference.getUserId(getActivity()) + "&login_user_id=" + SharePreference.getUserLoginId(getActivity()) + "&login_password=" + SharePreference.getUserLoginPassword(getActivity()) + "&business_id=" + SharePreference.getUserBusinessType(getActivity()) + "&year=" + year + "&month=" +monthDTOS.get(pos).getId();


               //  Log.e("<<>>",url+"");
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


//  offTypeTv.setText("check");

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

          case R.id.btn_search:

              if (!Utility.isInternetAvailable(getActivity())) {
                  Utility.internetAlert(getActivity());

              } else {
                  Toast.makeText(getActivity(), "আপনার রিপোর্ট লোড হচ্ছে। অপেক্ষা করুন।", Toast.LENGTH_SHORT).show();
                  webView.loadUrl(url);

              }

            break;

        }
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
