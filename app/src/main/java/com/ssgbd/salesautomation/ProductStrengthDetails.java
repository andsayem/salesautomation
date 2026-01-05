package com.ssgbd.salesautomation;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductStrengthCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductStrengthOthersCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.ProductStrengthDTO;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
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

public class ProductStrengthDetails extends AppCompatActivity {

    VolleyMethods vm = new VolleyMethods();
    private Context context;
    private Toolbar mToolbar;
    TextView txt_toolbar,txt_category_list;
    JSONObject finalobject;
    private Dialog wdialog,productDialog;
    DatabaseHelper databaseHelper;
    ProductStrengthCategoryRecyclerAdapter categoryRecyclerAdapter;
    ProductStrengthOthersCategoryRecyclerAdapter productStrengthCategoryRecyclerAdapter;
    ProductListRecyclerAdapter productListRecyclerAdapter;
    RecyclerView category_list_recyclerView,others_list_recyclerView;

 // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<ProductDTO> productDtos = new ArrayList<>();
    ArrayList<ProductStrengthDTO> pStrengthDTOS = new ArrayList<>();
    ArrayList<ProductStrengthDTO> p0thStrengthDTOS = new ArrayList<>();
    // final order

    String formattedDate="";
    public RequestQueue queue;
    JsonRequestFormate jrf = new JsonRequestFormate();
    TextView txt_pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_strangth_details_screen);
        context = this;

        loadToolBar();
        initUi();
        queue = Volley.newRequestQueue(this);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");// hh:mm:ss
        formattedDate = df.format(c);

        getData(getString(R.string.base_url)+"api/product-strength/"+getIntent().getStringExtra("PG_ID")+"/"+getIntent().getStringExtra("PRODUCT_ID"));
    //  getData(getString(R.string.base_url)+"api/product-strength/5/315");

    }
    @SuppressLint("WrongConstant")
    private void loadToolBar() {
        databaseHelper = new DatabaseHelper(context);
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myPath = DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()){
            databaseHelper.openDataBase();}
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_toolbar = (TextView) mToolbar.findViewById(R.id.txt_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        txt_toolbar.setText("Product");

    }
    private void initUi() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        txt_pg = (TextView) findViewById(R.id.txt_pg);

       // categoryDTOS = databaseHelper.getAllCategoryProductActiveOnly(databaseHelper,SharePreference.getUserBusinessType(context));

        category_list_recyclerView=(RecyclerView) findViewById(R.id.category_list_recyclerView);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        category_list_recyclerView.setLayoutManager(linearLayoutManager);
        categoryRecyclerAdapter = new ProductStrengthCategoryRecyclerAdapter( pStrengthDTOS,context);
        category_list_recyclerView.setAdapter(categoryRecyclerAdapter);

        category_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            //    productDtos = databaseHelper.getProductList(databaseHelper,categoryDTOS.get(position).getId());
           //   txt_category_list.setText(categoryDTOS.get(position).getName());


            }
        }));

        //others

                others_list_recyclerView=(RecyclerView) findViewById(R.id.others_list_recyclerView);
        LinearLayoutManager linearLayoutManager1  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        others_list_recyclerView.setLayoutManager(linearLayoutManager1);
        productStrengthCategoryRecyclerAdapter = new ProductStrengthOthersCategoryRecyclerAdapter( p0thStrengthDTOS,context);
        others_list_recyclerView.setAdapter(productStrengthCategoryRecyclerAdapter);

        others_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //    productDtos = databaseHelper.getProductList(databaseHelper,categoryDTOS.get(position).getId());
                //   txt_category_list.setText(categoryDTOS.get(position).getName());


            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id==android.R.id.home){
           finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getData(String url) {
        // Log.e("<<>>>>",url.toString()+"<<<<");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //  Log.e("pdata>>", response.toString() + "<<-->>");
                //   Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                try {
                    JSONObject respjsonObj = new JSONObject(response);
//                    String status = respjsonObj.getString("status");
                    JSONObject infoObject = respjsonObj.getJSONObject("productDetails");
                    txt_pg.setText("Product Group :" + infoObject.getString("category_name"));
                    JSONArray ssgArray = respjsonObj.getJSONArray("ownfeaturelist");
                    JSONArray othersArray = respjsonObj.getJSONArray("othersfeaturelist");

                    for (int i=0;i<ssgArray.length();i++){
                        JSONObject routeObject = ssgArray.getJSONObject(i);
                        ProductStrengthDTO productStrengthDTO = new ProductStrengthDTO();
                        productStrengthDTO.setProductName(infoObject.getString("product_name"));
                        productStrengthDTO.setCompanyName(routeObject.getString("company_name"));
                        productStrengthDTO.setProductImage(routeObject.getString("product_image"));
                        productStrengthDTO.setProductFeature(routeObject.getString("features"));

                        pStrengthDTOS.add(productStrengthDTO);
                    }
                    categoryRecyclerAdapter.notifyDataSetChanged();


                    for (int i=0;i<othersArray.length();i++){
                        JSONObject routeObject = othersArray.getJSONObject(i);
                        ProductStrengthDTO productStrengthDTO = new ProductStrengthDTO();
                        productStrengthDTO.setCompanyName(routeObject.getString("company_name"));
                        productStrengthDTO.setProductImage(routeObject.getString("product_image"));
                        productStrengthDTO.setProductFeature(routeObject.getString("features"));

                        p0thStrengthDTOS.add(productStrengthDTO);
                    }
                    productStrengthCategoryRecyclerAdapter.notifyDataSetChanged();


                } catch (JSONException e) {

                    //   Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                    //   Log.e("<<>>>>",e.toString()+"<<<<");

                    e.printStackTrace();
                }

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
    public String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key) || json.optString(key).equalsIgnoreCase("null") || json.isNull(key) || json.optString(key).equalsIgnoreCase(""))
            return "";
        else
            return json.optString(key, key);
    }
}



