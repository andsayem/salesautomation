package com.ssgbd.salesautomation.drawer.fragment;

import android.app.Activity;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.SCDDashboardActivity;
import com.ssgbd.salesautomation.adapters.ForecastEntryListAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.StockListAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.ForecastEntryListDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.StockListDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
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

public class ForecastEntryFragment extends Fragment implements View.OnClickListener{

    View rootView;
    // product category list
    TextView txt_category_list;
    Dialog wdialog;
    ProductCategoryRecyclerAdapter productCategoryAdapter;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<ForecastEntryListDTO> stockDTOS = new ArrayList<>();

    // get db data
    DatabaseHelper databaseHelper;
   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    Spinner spinnerMonth;
    ArrayList<CategoryDTO> monthDTOS = new ArrayList<>();
    SimpleDateFormat presentMonth,presentMonth1;
    SimpleDateFormat presentYear;
    JSONObject jsonBody;
    // product
    RecyclerView product_list_recyclerView;
    ForecastEntryListAdapter stockListAdapter;
    ArrayList<ProductDTO> productDtos = new ArrayList<>();
    TextView offTypeTv;
    String selectedMonth="";
    String current_year="";
    Date c;
    public RequestQueue queue;
    Button button;
    String catId="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.forecast_entry_fragment, container, false);
        queue = Volley.newRequestQueue(getActivity());


         button =  (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (catId.equalsIgnoreCase("")||selectedMonth.equalsIgnoreCase("0")){
                    Toast.makeText(getActivity(), "আগে মাস এবং পি.জি সিলেক্ট করুন।"  , Toast.LENGTH_SHORT).show();
                }else {

                    try {
                        jsonBody = new JSONObject();

                        JSONArray ja = new JSONArray();
                        JSONObject jo;
                        for (int i = 0; i < stockDTOS.size(); i++) {
                            jo = new JSONObject();
                            jo.put("product_id", stockDTOS.get(i).getProduct_id());
                            jo.put("product_code", stockDTOS.get(i).getProduct_code());
                            jo.put("product_name", stockDTOS.get(i).getProduct_name());
                            jo.put("last_3_month_data", stockDTOS.get(i).getAvg_2022_ims_qty());
                            jo.put("last_year_data_this_month", stockDTOS.get(i).getAvg_2023_ims_qty());
                            jo.put("forecast_qty", stockDTOS.get(i).getForecastQty());

                            ja.put(jo);
                        }

                        jsonBody.put("fo_id", SharePreference.getUserId(getActivity()));
                        jsonBody.put("month", selectedMonth);
                        jsonBody.put("year", current_year);
                        jsonBody.put("data", ja);

//                    Log.e("<</>>",jsonBody+"<</>>");

                    } catch (JSONException je) {

                    }
                    updateForecast(jsonBody.toString());

                }
            }
        });


        c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        presentMonth = new SimpleDateFormat("MMMM");
        presentMonth1 = new SimpleDateFormat("MM");
     //   presentYear = new SimpleDateFormat("yyyy");
     //   current_year = presentYear.format(c);

        selectedMonth  = String.valueOf(presentMonth1.format(c));


        CategoryDTO ww = new CategoryDTO();
        ww.setId("0");
       // ww.setName( presentMonth.format(c)+"24");
        ww.setName( "Select Month");
        ww.setG_code("0");
        monthDTOS.add(ww);

        CategoryDTO c7m = new CategoryDTO();
        c7m.setId("07");
        c7m.setName("July-23");
        c7m.setG_code("2023");
        monthDTOS.add(c7m);
        CategoryDTO c8m = new CategoryDTO();
        c8m.setId("08");
        c8m.setName("August-23");
        c8m.setG_code("2023");
        monthDTOS.add(c8m);
        CategoryDTO c9m = new CategoryDTO();
        c9m.setId("09");
        c9m.setName("September-23");
        c9m.setG_code("2023");
        monthDTOS.add(c9m);
        CategoryDTO c10m = new CategoryDTO();
        c10m.setId("10");
        c10m.setName("October-23");
        c10m.setG_code("2023");
        monthDTOS.add(c10m);
        CategoryDTO c11m = new CategoryDTO();
        c11m.setId("11");
        c11m.setName("November-23");
        c11m.setG_code("2023");
        monthDTOS.add(c11m);
        CategoryDTO c12m = new CategoryDTO();
        c12m.setId("12");
        c12m.setName("December-23");
        c12m.setG_code("2023");
        monthDTOS.add(c12m);

        CategoryDTO c1m = new CategoryDTO();
        c1m.setId("01");
        c1m.setName("January-24");
        c1m.setG_code("2024");
        monthDTOS.add(c1m);
        CategoryDTO c2m = new CategoryDTO();
        c2m.setId("02");
        c2m.setName("February-24");
        c2m.setG_code("2024");
        monthDTOS.add(c2m);
        CategoryDTO c3m = new CategoryDTO();
        c3m.setId("03");
        c3m.setName("March-24");
        c3m.setG_code("2024");
        monthDTOS.add(c3m);
        CategoryDTO c4m = new CategoryDTO();
        c4m.setId("04");
        c4m.setName("April-24");
        c4m.setG_code("2024");
        monthDTOS.add(c4m);
        CategoryDTO c5m = new CategoryDTO();
        c5m.setId("05");
        c5m.setName("May-24");
        c5m.setG_code("2024");
        monthDTOS.add(c5m);
        CategoryDTO c6m = new CategoryDTO();
        c6m.setId("06");
        c6m.setName("June-24");
        c6m.setG_code("2024");
        monthDTOS.add(c6m);

        spinnerMonth = (Spinner) rootView.findViewById(R.id.spinnerMonth);
        YearArrayAdapter adapter1 = new YearArrayAdapter(getActivity(),
                R.layout.customspinneritem, monthDTOS);

        spinnerMonth.setAdapter(adapter1);

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

               //   Log.e(">>>shoptype><<>>",monthDTOS.get(pos).getId()+"");
                selectedMonth =  monthDTOS.get(pos).getId();
                current_year =  monthDTOS.get(pos).getG_code();
                showProductListDialog();
                catId="";
//                productCategoryAdapter.notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        databaseHelper = new DatabaseHelper(getActivity());
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myPath = DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()){
        databaseHelper.openDataBase();}

        txt_category_list = (TextView) rootView.findViewById(R.id.txt_category_list);
        txt_category_list.setOnClickListener(this);

        product_list_recyclerView=(RecyclerView) rootView.findViewById(R.id.product_list_recyclerView);
        showProductListDialog();

        return rootView;
    }


    private void showCategoryListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        categoryDTOS.clear();
        categoryDTOS = databaseHelper.getAllCategoryProductActiveOnly(databaseHelper,SharePreference.getUserBusinessType(getActivity()));

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
        productCategoryAdapter = new ProductCategoryRecyclerAdapter( categoryDTOS,getActivity());
        route_list_recyclerView.setAdapter(productCategoryAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


         txt_category_list.setText(categoryDTOS.get(position).getName());

                if (selectedMonth.equalsIgnoreCase("0")){
                    Toast.makeText(getActivity(), "আগে মাস সিলেক্ট করুন।"  , Toast.LENGTH_SHORT).show();
                }else {


                    catId = categoryDTOS.get(position).getId();
                    getPG(categoryDTOS.get(position).getId());
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
                productCategoryAdapter.filter(query);
            }
        });

        wdialog.show();
    }

    private void showProductListDialog() {

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        product_list_recyclerView.setLayoutManager(linearLayoutManager);
        stockListAdapter = new ForecastEntryListAdapter( stockDTOS,getActivity());
        product_list_recyclerView.setAdapter(stockListAdapter);

//        product_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        }));
        stockDTOS.clear();
        stockListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_category_list:
                showCategoryListDialog();
                break;
        }
    }

    public void updateForecast(String jb){
        VolleyMethods vm = new VolleyMethods();


        vm.sendRequestToServer2(getActivity(), getString(R.string.base_url)+"api/product_forecast_insert", jb, new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {


              //  Log.e("result",result+"<<>>");
                try {
                    JSONObject respjsonObj = new JSONObject(result);
                  if(respjsonObj.getString("status").equalsIgnoreCase("true")) {
                      Toast.makeText(getActivity(), respjsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                      stockDTOS.clear();
                      stockListAdapter.notifyDataSetChanged();
                  } else {
                      Toast.makeText(getActivity(), respjsonObj.getString("message"), Toast.LENGTH_SHORT).show();

                  }
                }catch (JSONException je){

                }


            }

        });
    }
    public void getPG(String catid) {

        stockDTOS.clear();
        showProductListDialog();
        String url = getString(R.string.base_url)+"api/get_product_forecast?category_id="+catid+"&fo_id="+SharePreference.getUserId(getActivity())+"&year="+current_year+"&month="+selectedMonth;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.e("statusArray",response+"statusArray");

                try {
                    JSONObject respjsonObj = new JSONObject(response);
                    JSONArray statusArray = respjsonObj.getJSONArray("data");
                    // Log.e("statusArray",statusArray+"statusArray");
                    for (int i = 0; i < statusArray.length(); i++) {
                        JSONObject routeObject = statusArray.getJSONObject(i);
                        ForecastEntryListDTO stockListDTO = new ForecastEntryListDTO();
                        stockListDTO.setProduct_id(routeObject.getString("product_id"));
                        stockListDTO.setProduct_code(routeObject.getString("product_code"));
                        stockListDTO.setProduct_name(routeObject.getString("product_name"));
                        stockListDTO.setAvg_2022_ims_qty(routeObject.getString("last_3_month_data"));
                        stockListDTO.setAvg_2023_ims_qty(routeObject.getString("last_year_data_this_month"));
                        stockListDTO.setForecastQty(routeObject.getString("forecast_qty"));


                        stockDTOS.add(stockListDTO);
                    }
                } catch (JSONException je) {
                }
                stockListAdapter.notifyDataSetChanged();



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
