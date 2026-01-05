package com.ssgbd.salesautomation;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductChangeListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ReturnChangeCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ReturnChangeRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.ReturnChangeCategoryDTO;
import com.ssgbd.salesautomation.dtos.ReturnChangeDTO;
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

//import static com.google.android.gms.internal.zzahg.runOnUiThread;

public class ShirtListActivity extends AppCompatActivity implements View.OnClickListener {

    VolleyMethods vm = new VolleyMethods();
    private Context context;
    private Toolbar mToolbar;
    TextView txt_toolbar, txt_retailer_name, txt_category_list;
    JSONObject finalobject;
    private Dialog wdialog, productDialog, returnChangeDialog, dialogProductChange;
    TextView txt_route_list, txt_route_name;
    DatabaseHelper databaseHelper;
    ProductCategoryRecyclerAdapter routeRecyclerAdapter;
    ReturnChangeCategoryRecyclerAdapter returnChangeCategoryRecyclerAdapter;
    ProductListRecyclerAdapter productListRecyclerAdapter;
    ProductChangeListRecyclerAdapter changeListRecyclerAdapter;
    RecyclerView product_list_recyclerView;

    //    private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;

    ArrayList<ReturnChangeDTO> returnChangeDTOS = new ArrayList<>();


    public RelativeLayout rel_lay_addproduct;
    public TextView txt_product_name, txt_product_name_change;
    public Button btn_confirm_return_change;
    private LinearLayout linlay_category_portion;


    // final order
    RecyclerView order_list_recyclerView;


    ArrayList<DBOrderDTO> orderListDTOS = new ArrayList<>();
    ArrayList<ReturnChangeCategoryDTO> returnChangeCategoryDTOS = new ArrayList<>();

    JsonRequestFormate jrf = new JsonRequestFormate();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shirt_screen);
        context = this;

        loadToolBar();
        initUi();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        // formattedDate = df.format(c);
        getTshirtList();
    }

    private void loadToolBar() {

        // database connectivity
        databaseHelper = new DatabaseHelper(context);
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myPath = DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()) {
            databaseHelper.openDataBase();
        }


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_toolbar = (TextView) mToolbar.findViewById(R.id.txt_toolbar);
        txt_toolbar.setText("Return and Change");


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
    }

    private void initUi() {

        btn_confirm_return_change = (Button) findViewById(R.id.btn_confirm_return_change);
        btn_confirm_return_change.setOnClickListener(this);
        linlay_category_portion = (LinearLayout) findViewById(R.id.linlay_category_portion);
        txt_retailer_name = (TextView) findViewById(R.id.txt_retailer_name);
        txt_retailer_name.setText(getIntent().getStringExtra("Shirt List"));
        txt_category_list = (TextView) findViewById(R.id.txt_category_list);
        txt_category_list.setOnClickListener(this);

        // gating data from db

        orderListShow();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        // formattedDate = df.format(c);

    }

    private void orderListShow() {

        order_list_recyclerView = (RecyclerView) findViewById(R.id.order_list_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        order_list_recyclerView.setLayoutManager(linearLayoutManager);
//        returnChangeRecyclerAdapter = new ReturnChangeRecyclerAdapter(returnChangeDTOS,context);
//        order_list_recyclerView.setAdapter(returnChangeRecyclerAdapter);

        order_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {


                new AlertDialog.Builder(context)
                        .setTitle("Alert")
                        .setIcon(R.mipmap.ssg_logo)
                        .setMessage("Delete this item?").setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (returnChangeDTOS.get(position).getReturnQuantity().equalsIgnoreCase("0")) {
                                    //   Toast.makeText(context,returnChangeDTOS.get(position).getReturnQuantity() , Toast.LENGTH_SHORT).show();

                                    returnChangeDTOS.remove(returnChangeDTOS.get(position));


                                    dialog.dismiss();

                                } else {

                                    //    Toast.makeText(context,returnChangeDTOS.get(position).getReturnQuantity() , Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                }
                            }
                        }).show();
            }
        }));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


        }
    }

    public void getTshirtList(){

        RequestQueue queue;
        //
        String  url = getResources().getString(R.string.base_url)+"api/shirt-measurement?user_id="+SharePreference.getUserId(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        //  Log.e("version>>",response+"");

                        try {
                            JSONObject jsonObject = new JSONObject(response);



                        }catch (JSONException je){

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //     Log.e("error volley>>",error.toString()+"");
                    }
                }
        ) {
        };
        queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        postRequest.setShouldCache(false);
        queue.add(postRequest);
    }
}


