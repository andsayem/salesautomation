package com.ssgbd.salesautomation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.adapters.OrderRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryCalculationRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductListRecyclerAdapter;
import com.ssgbd.salesautomation.bucket.BucketAmountWeb;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.dtos.OrderDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.gps.GPSTracker;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
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

public class ProductStrengthActivity extends AppCompatActivity {

    VolleyMethods vm = new VolleyMethods();
    private Context context;
    private Toolbar mToolbar;
    TextView txt_toolbar,txt_category_list;
    JSONObject finalobject;
    private Dialog wdialog,productDialog;
    DatabaseHelper databaseHelper;
    ProductCategoryRecyclerAdapter categoryRecyclerAdapter;
    ProductListRecyclerAdapter productListRecyclerAdapter;
    RecyclerView category_list_recyclerView,product_list_recyclerView;

   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<ProductDTO> productDtos = new ArrayList<>();

    // final order
    EditText etSearch;
    String formattedDate="";
    public RequestQueue queue;
    JsonRequestFormate jrf = new JsonRequestFormate();
    String PG_ID="",PRODUCT_ID="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_strangth_screen);
        context = this;

        loadToolBar();
        initUi();
        queue = Volley.newRequestQueue(this);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");// hh:mm:ss
        formattedDate = df.format(c);

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
        txt_toolbar.setText("Product");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
    }
    private void initUi() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        etSearch = (EditText) findViewById(R.id.edt_txt_search);
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
                categoryRecyclerAdapter.filter(query);
            }
        });
        categoryDTOS = databaseHelper.getAllCategoryProductActiveOnly(databaseHelper,SharePreference.getUserBusinessType(context));

        category_list_recyclerView=(RecyclerView) findViewById(R.id.category_list_recyclerView);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        category_list_recyclerView.setLayoutManager(linearLayoutManager);
        categoryRecyclerAdapter = new ProductCategoryRecyclerAdapter( categoryDTOS,context);
        category_list_recyclerView.setAdapter(categoryRecyclerAdapter);

        category_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                productDtos = databaseHelper.getProductList(databaseHelper,categoryDTOS.get(position).getId());
           //   txt_category_list.setText(categoryDTOS.get(position).getName());

                PG_ID = categoryDTOS.get(position).getId();
                showProductListDialog();

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

    private void showProductListDialog() {

        productDialog =new Dialog(context,R.style.full_screen_dialog);
        productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        productDialog.setContentView(R.layout.dialog_product_list_strength);
        final Button btnDone,btn_add;
        final ImageView imbtnCross,btn_add_product_cross;
        final EditText etSearch;
        final CardView card_view;
        etSearch = (EditText)productDialog.findViewById(R.id.edt_txt_search);

        card_view = (CardView) productDialog.findViewById(R.id.card_view);
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_add = (Button) productDialog.findViewById(R.id.btn_add_product);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final RelativeLayout rlDialogCross;

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        product_list_recyclerView=(RecyclerView) productDialog.findViewById(R.id.product_list_recyclerView);
        imbtnCross=(ImageView)productDialog.findViewById(R.id.btn_dialog_cross);
        btn_add_product_cross=(ImageView)productDialog.findViewById(R.id.btn_add_product_cross);
        rlDialogCross = (RelativeLayout)productDialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)productDialog.findViewById(R.id.btnDoneDialog);

        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDialog.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        product_list_recyclerView.setLayoutManager(linearLayoutManager);
        productListRecyclerAdapter = new ProductListRecyclerAdapter( productDtos,context);
        product_list_recyclerView.setAdapter(productListRecyclerAdapter);

        product_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

             // Toast.makeText(context, "work", Toast.LENGTH_SHORT).show();

                PRODUCT_ID = productDtos.get(position).getProduct_id() ;
                Intent ps_intent = new Intent(ProductStrengthActivity.this,ProductStrengthDetails.class);
                ps_intent.putExtra("PG_ID",PG_ID);
                ps_intent.putExtra("PRODUCT_ID",PRODUCT_ID);
                startActivity(ps_intent);

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
                productListRecyclerAdapter.filter(query);
            }
        });
        productDialog.show();
    }
}



