package com.ssgbd.salesautomation.drawer.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.IMSFOActivity;
import com.ssgbd.salesautomation.LoginActivity;
import com.ssgbd.salesautomation.PlatformActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.SCDDashboardActivity;
import com.ssgbd.salesautomation.adapters.IncentiveReportListAdapter;
import com.ssgbd.salesautomation.adapters.OrderVsDeliveryReportListAdapter;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.drawer.SplashActivity;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.IncentiveReportDTO;
import com.ssgbd.salesautomation.dtos.PGPercentageDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.report.DeliveryVsOrderReportFragment;
import com.ssgbd.salesautomation.report.FoActivityReportFragment;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IncentiveFragment extends Fragment implements View.OnClickListener {

    View rootView;
    // product category list
    Spinner spinnerYear;
    Spinner spinnerMonth;
    ArrayList<CategoryDTO> yearDTOS = new ArrayList<>();
    String year="",month="";
    ArrayList<CategoryDTO> monthDTOS = new ArrayList<>();
    TextView offTypeTv,txt_pg_incentive,txt_group_incentive,txt_total_incentive;
    Button btn_search;
    String url="";
    SimpleDateFormat presentYear;
    SimpleDateFormat presentMonth,presentMonth1;
    Date c;
    public RequestQueue queue;
    ArrayList<IncentiveReportDTO> incentiveReportDTOS = new ArrayList<>();
    IncentiveReportListAdapter incentiveReportListAdapter;
    LinearLayoutManager linearLayoutManager1;
    RecyclerView retailer_list_recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.incentive_screen, container, false);
        c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        presentMonth = new SimpleDateFormat("MMMM");
        presentMonth1 = new SimpleDateFormat("MM");
        presentYear = new SimpleDateFormat("yyyy");
        queue = Volley.newRequestQueue(getActivity());

        //  Log.e("formattedDate",df.format(c)+ presentMonth.format(c)+presentYear.format(c)+"<<>>");
        txt_pg_incentive = (TextView) rootView.findViewById(R.id.txt_pg_incentive);
        txt_group_incentive = (TextView) rootView.findViewById(R.id.txt_group_incentive);
        txt_total_incentive = (TextView) rootView.findViewById(R.id.txt_total_incentive);

        btn_search = (Button) rootView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);


        CategoryDTO p = new CategoryDTO();
        p.setId(presentYear.format(c));
        p.setName(presentYear.format(c));
        yearDTOS.add(p);

        CategoryDTO c1 = new CategoryDTO();
        c1.setId("2019");
        c1.setName("2019");
        yearDTOS.add(c1);
        CategoryDTO c11 = new CategoryDTO();
        c11.setId("2020");
        c11.setName("2020");
        yearDTOS.add(c11);
        CategoryDTO c12 = new CategoryDTO();
        c12.setId("2021");
        c12.setName("2021");
        yearDTOS.add(c12);
        CategoryDTO c13 = new CategoryDTO();
        c13.setId("2022");
        c13.setName("2022");
        yearDTOS.add(c13);
        CategoryDTO c14 = new CategoryDTO();
        c14.setId("2023");
        c14.setName("2023");
        yearDTOS.add(c14);

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

                month=monthDTOS.get(pos).getId();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        int month1=cldr.get(Calendar.MONTH);
        int year=cldr.get(Calendar.YEAR);

        String month="";
        if(month1<10){
            month="0"+String.valueOf(month1+1);
        }else {
            month=String.valueOf(month1+1);
        }

        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        incentiveReportListAdapter = new IncentiveReportListAdapter( incentiveReportDTOS,getActivity());
        retailer_list_recyclerView.setAdapter(incentiveReportListAdapter);

        url = getString(R.string.base_url) + "api/incentive/report?fo=" + SharePreference.getUserId(getActivity()) +"&year=" + year + "&month=" +month+"&channel="+SharePreference.getUserBusinessType(getActivity());

        httpRequest(url);
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

                if (!isInternetAvailable(getActivity())) {
                    internetAlert(getActivity());

                } else {
                    url = getString(R.string.base_url) + "api/incentive/report?fo=" + SharePreference.getUserId(getActivity()) +"&year=" + year + "&month=" +month+"&channel="+SharePreference.getUserBusinessType(getActivity());
                    ///api/incentive/report?fo=12831&year=2023&month=02
                    //Log.e("<<>>",url+"<>");

            httpRequest(url);

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

    public  boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }


    public void internetAlert(final Context context) {
        final AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("Internet Alert").setMessage("Your device is not connected to internet. Connect to internet and try again.");

        alertBulder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        alertBulder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertBulder.setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertBulder.show();
    }


    public void httpRequest(String url) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("রিপোর্ট লোড হচ্ছে, দয়া করে অপেক্ষা করুন। ");
        pd.show();
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {


                try {
                    JSONObject respjsonObj = new JSONObject(response);
                    String status = respjsonObj.getString("status");
//                    Log.e("<<>>>",respjsonObj+"<<<<");

                    if (status.equals("1")) {
                        txt_pg_incentive.setText("");
                        txt_group_incentive.setText("");
                        txt_total_incentive.setText("");

                        JSONArray statusArray = respjsonObj.getJSONArray("data");
                        //  Log.e("statusArray",statusArray+"statusArray");
                        for (int i = 0; i < statusArray.length(); i++) {
                            JSONObject routeObject = statusArray.getJSONObject(i);
                            IncentiveReportDTO incentiveReportDTO = new IncentiveReportDTO();
                            incentiveReportDTO.setGroup_name(routeObject.getString("group_name"));
                            incentiveReportDTO.setPg_name(routeObject.getString("pg_name"));

                            incentiveReportDTO.setTarget_qty(routeObject.getString("target_qty"));
                            incentiveReportDTO.setImsQty(routeObject.getString("imsQty"));
                            incentiveReportDTO.setImsValue(routeObject.getString("imsValue"));
                            incentiveReportDTO.setQty_per(routeObject.getString("qty_per"));
                            incentiveReportDTO.setValue_per(routeObject.getString("value_per"));
                            incentiveReportDTO.setCollection(routeObject.getString("collection"));
                            incentiveReportDTO.setIms_contr(routeObject.getString("ims_contr"));
                            incentiveReportDTO.setPg_collection(routeObject.getString("pg_collection"));
                            incentiveReportDTO.setCollection_qty(routeObject.getString("collection_qty"));
                            incentiveReportDTO.setPcs_achv_perc(routeObject.getString("pcs_achv_perc"));
                            incentiveReportDTO.setGroup_achv_perc(routeObject.getString("group_achv_perc"));

                            incentiveReportDTO.setIncentive_amount(routeObject.getString("incentive_amount"));
                            incentiveReportDTO.setGroup_amount(routeObject.getString("group_amount"));

                            incentiveReportDTO.setTotal_amount(routeObject.getString("total_amount"));

//                           tar+= Float.parseFloat(routeObject.getString("target_qty").replace(",",""));
//                            del+= Float.parseFloat(routeObject.getString("delivery_qty").replace(",",""));
//                            //  Log.e("statusArray",tar+"<>"+del+"statusArray");
//                            tar_v+= Float.parseFloat(routeObject.getString("target_value").replace(",",""));
//                            del_v+= Float.parseFloat(routeObject.getString("delivery_value").replace(",",""));
                            incentiveReportDTOS.add(incentiveReportDTO);

                        }
                        incentiveReportListAdapter.notifyDataSetChanged();
                        JSONObject jo =respjsonObj.getJSONObject("summary");

                        txt_pg_incentive.setText(jo.getString("total_incentive_amount"));
                        txt_group_incentive.setText(jo.getString("total_group_amount"));
                        txt_total_incentive.setText(jo.getString("grand_total_incentive_amount"));

pd.dismiss();

                    } else {
                        Toast.makeText(getActivity(),  "কোন তথ্য পাওয়া যায়নি।", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                } catch (JSONException e) {

                    //   Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                    Log.e("<<>>>>",e.toString()+"<<<<");
                pd.dismiss();
                    e.printStackTrace();
                }

                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
                //  Toast.makeText(context, "SSL server error.", Toast.LENGTH_SHORT).show();

                Log.e("<<>>>>",error.toString()+"<<<<");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        queue.add(stringRequest);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
    }

    public void mydat(String p,String g,String t){

        //{"total_incentive_amount":"0","total_group_amount":"3,515","grand_total_incentive_amount":"3,515"}
    }
}
