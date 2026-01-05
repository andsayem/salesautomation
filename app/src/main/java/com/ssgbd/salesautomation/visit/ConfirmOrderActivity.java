package com.ssgbd.salesautomation.visit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ConfirmOrderRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.OrderRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductListRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.ConfirmOrderDTO;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.dtos.OrderDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private Toolbar mToolbar;
    TextView txt_toolbar;
    DatabaseHelper databaseHelper;
//    private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<DBOrderDTO> temp_orderDTOS = new ArrayList<>();
    ArrayList<ConfirmOrderDTO> orderDtos = new ArrayList<>();

    public Button btn_add_cart;

    // select product
    String PRODUCT_CATEGORY_ID="";
    String PRODUCT_CATEGORY_NAME="";
    String PRODUCT_ID="";
    String PRODUCT_NAME="";
    double PRODUCT_VALUE=0;
    double PRODUCT_QTY= 0;
    String WESTAGE_QTY="";

    String ORDER_DATA = "";

    EditText row_edt_order_qty,row_edt_value,row_edt_wastage;

    // final order
    RecyclerView order_list_recyclerView;
    EditText edt_search_order_product;
    ConfirmOrderRecyclerAdapter orderRecyclerAdapter;

    //bucket
    TextView txt_bucket_amount;
    LinearLayout linlay_buket_amount;
    float ff=0;

    LinearLayout linlay_delete,lin_lay_offer;
    Button btn_add_newproduct,btn_close_order_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order_screen);
        context = this;

        loadToolBar();
        initUi();
    }

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
        txt_toolbar.setText("Confirm Order");

        linlay_buket_amount = (LinearLayout) mToolbar.findViewById(R.id.linlay_buket_amount);
        linlay_buket_amount.setOnClickListener(this);
        linlay_buket_amount.setVisibility(View.GONE);
        txt_bucket_amount = (TextView) mToolbar.findViewById(R.id.txt_bucket_amount);
        txt_bucket_amount.setVisibility(View.VISIBLE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
    }
    private void initUi() {

        btn_close_order_request = (Button) findViewById(R.id.btn_close_order_request);
        btn_close_order_request.setOnClickListener(this);

        btn_add_newproduct = (Button) findViewById(R.id.btn_add_newproduct);
        btn_add_newproduct.setOnClickListener(this);
        linlay_delete = (LinearLayout) findViewById(R.id.linlay_delete);
        lin_lay_offer = (LinearLayout) findViewById(R.id.lin_lay_offer);







        getIntent().getStringExtra("retailerName");

      //  categoryDTOS = databaseHelper.getAllCategoryProduct(databaseHelper);
    //   String url = getString(R.string.base_url)+"order-process?appsusername="+SharePreferenceBTP.getUserLoginId(context)
     //          +"&appspassword=12345&appsrememberme=0&routeID="+ Utility.ROUTE_ID+"&retailerID="+getIntent().getStringExtra("retailerId");

        temp_orderDTOS = databaseHelper.getOrderList(databaseHelper,getIntent().getStringExtra("retailerId"),"27-10-18");
    //    Log.e("size",temp_orderDTOS.size()+"" );
        for (int i=0;i<temp_orderDTOS.size();i++){

            ORDER_DATA = temp_orderDTOS.get(i).getOrderData();

        }
        orderListShow();
        getOrderJSON();




    }

    private void getOrderJSON() {

        try {
            JSONObject jsonObject = new JSONObject(ORDER_DATA);

            JSONArray jsonArray = jsonObject.getJSONArray("order");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject dataObject = jsonArray.getJSONObject(i);
                ConfirmOrderDTO confirmOrderDTO = new ConfirmOrderDTO();
                confirmOrderDTO.setProduct_name(dataObject.getString("name"));
                confirmOrderDTO.setProduct_category_name(dataObject.getString("productCategoryName"));
                confirmOrderDTO.setProduct_qty(dataObject.getString("qty"));
                confirmOrderDTO.setProduct_price(dataObject.getString("qty"));
                confirmOrderDTO.setWastage_qty(dataObject.getString("wastage_qty"));

                orderDtos.add(confirmOrderDTO);
            }

            orderRecyclerAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void orderListShow() {

        edt_search_order_product = (EditText) findViewById(R.id.edt_search_order_product);
        order_list_recyclerView = (RecyclerView) findViewById(R.id.order_list_recyclerView);

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        order_list_recyclerView.setLayoutManager(linearLayoutManager);
        orderRecyclerAdapter = new ConfirmOrderRecyclerAdapter(orderDtos,context);
        order_list_recyclerView.setAdapter(orderRecyclerAdapter);

        order_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {


                //  Toast.makeText(context, orderDtos.get(position).getProduct_category_name(), Toast.LENGTH_SHORT).show();



                new AlertDialog.Builder(context)
                        .setTitle("Alert")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("Delete this item?").setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                for (int i=0;i<orderDtos.size();i++){
//                                    if (orderDtos.get(i).getProduct_id().equalsIgnoreCase(orderDtos.get(position).getProduct_id())){
//                                        orderDtos.remove(i);
//                                    }
//                                }
//                                orderRecyclerAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }).show();



            }
        }));

        edt_search_order_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = edt_search_order_product.getText().toString().toLowerCase();
            }
        });
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
    @Override
    public void onClick(View view) {

        switch (view.getId()){


            case R.id.btn_close_order_request:

                linlay_delete.setVisibility(View.GONE);
                lin_lay_offer.setVisibility(View.VISIBLE);
                btn_add_newproduct.setVisibility(View.GONE);
                btn_close_order_request.setVisibility(View.GONE);

                break;

                case R.id.btn_add_newproduct:

                finish();

                break;


        }
    }





}



