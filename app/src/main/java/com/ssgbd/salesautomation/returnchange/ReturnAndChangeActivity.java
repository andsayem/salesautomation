package com.ssgbd.salesautomation.returnchange;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductChangeListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ReturnChangeCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ReturnChangeRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.dtos.ExceptCatDTO;
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


public class ReturnAndChangeActivity extends AppCompatActivity implements View.OnClickListener{

    VolleyMethods vm = new VolleyMethods();
    private Context context;
    private Toolbar mToolbar;
    TextView txt_toolbar,txt_retailer_name,txt_category_list;
    JSONObject finalobject;
    private Dialog wdialog, productDialog,returnChangeDialog,dialogProductChange;
    DatabaseHelper databaseHelper;
    ProductCategoryRecyclerAdapter routeRecyclerAdapter;
    ReturnChangeCategoryRecyclerAdapter returnChangeCategoryRecyclerAdapter;
    ProductListRecyclerAdapter productListRecyclerAdapter;
    ProductChangeListRecyclerAdapter changeListRecyclerAdapter;
    RecyclerView product_list_recyclerView;

//    private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<ExceptCatDTO> exceptCategoryDTOS = new ArrayList<>();
    ArrayList<ProductDTO> productDtos = new ArrayList<>();
    ArrayList<ProductDTO> productDtosChange = new ArrayList<>();
    ArrayList<ReturnChangeDTO> returnChangeDTOS = new ArrayList<>();

    public RelativeLayout rel_lay_addproduct;
    public TextView txt_product_name,txt_product_name_change,txt_stock_qty,txt_book_qty,txt_stock_qty1,txt_book_qty1;
    public Button btn_confirm_return_change;
    private LinearLayout linlay_category_portion;

    // select product
    String PRODUCT_CATEGORY_ID="";
    String PRODUCT_CATEGORY_ID_CHANGE="";
    String PRODUCT_CATEGORY_NAME="";
    String PRODUCT_CATEGORY_NAME_CHANGE="";
    String PRODUCT_ID="";
    String PRODUCT_ID_CHANGE="";
    String PRODUCT_NAME="";
    String PRODUCT_NAME_CHANGE="";
    double PRODUCT_VALUE=0;
    double PRODUCT_VALUE_CHANGE=0;
    double PRODUCT_QTY= 0;
    double PRODUCT_QTY_CHANGE= 0;
    double PRODUCT_SINGLE_DEPO_PRICE = 0;
    double PRODUCT_SINGLE_DEPO_PRICE_CHANGE = 0;
    String OFFER_TYPE_ID= "";

    EditText row_edt_order_qty,row_edt_order_qty_change,row_edt_value,row_edt_value_change,row_edt_wastage;
    TextView txt_category_list_dialog;
    // final order
    RecyclerView order_list_recyclerView;
    EditText edt_search_order_product;
    ReturnChangeRecyclerAdapter returnChangeRecyclerAdapter;

    //bucket
    TextView txt_bucket_amount,txt_total_amount_return,txt_total_amount_change;
    LinearLayout linlay_buket_amount;
    float ff=0;
    float gg=0;
    float  deleteCalculation=0;
    float  deleteCalculationChange=0;
    String formattedDate="";
    ArrayList<DBOrderDTO> orderListDTOS = new ArrayList<>();
    ArrayList<ReturnChangeCategoryDTO> returnChangeCategoryDTOS = new ArrayList<>();
    JsonRequestFormate jrf = new JsonRequestFormate();

    // amount exss
    String productCheckId="";
    String productCheckValueReturn="";
    String productCheckValueChange="";

    ArrayList<CategoryDTO> categoryDTOSREserve = new ArrayList<>();

    boolean isMultiAdd=false;
    String store_qty="0";
    String store_value="0";

    String CHANNEL_ID="null";
    public RequestQueue queue;

    String product_change_cat_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.return_and_change_screen);
        context = this;
        queue = Volley.newRequestQueue(context);
        loadToolBar();

        exceptionCategory();
        initUi();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
        syncCategory("");

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
        if (file.exists() && !file.isDirectory()){
            databaseHelper.openDataBase();}


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_toolbar = (TextView) mToolbar.findViewById(R.id.txt_toolbar);
        txt_toolbar.setText("Return and Change");

        linlay_buket_amount = (LinearLayout) mToolbar.findViewById(R.id.linlay_buket_amount);
        linlay_buket_amount.setOnClickListener(this);
        linlay_buket_amount.setVisibility(View.GONE);

        txt_total_amount_return = (TextView) findViewById(R.id.txt_total_amount_return);
        txt_total_amount_change = (TextView) findViewById(R.id.txt_total_amount_change);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
    }
    private void initUi() {

        btn_confirm_return_change = (Button) findViewById(R.id.btn_confirm_return_change);
        btn_confirm_return_change.setOnClickListener(this);
        linlay_category_portion = (LinearLayout)findViewById(R.id.linlay_category_portion);
        txt_retailer_name = (TextView) findViewById(R.id.txt_retailer_name);
        txt_retailer_name.setText(getIntent().getStringExtra("retailerName"));
        txt_category_list  = (TextView) findViewById(R.id.txt_category_list);
        txt_category_list.setOnClickListener(this);

        // gating data from db

        orderListShow();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

    }

    private void orderListShow() {

        edt_search_order_product = (EditText) findViewById(R.id.edt_search_order_product);
        order_list_recyclerView = (RecyclerView) findViewById(R.id.order_list_recyclerView);

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        order_list_recyclerView.setLayoutManager(linearLayoutManager);
        returnChangeRecyclerAdapter = new ReturnChangeRecyclerAdapter(returnChangeDTOS,context);
        order_list_recyclerView.setAdapter(returnChangeRecyclerAdapter);

        order_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {


              new AlertDialog.Builder(context)
                        .setTitle("Alert")
                        .setIcon(R.mipmap.ssg_logo)
                        .setMessage("আইটেম টি কে ডিলিট করতে চান ?").setNegativeButton("না", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .setPositiveButton("হা", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (returnChangeDTOS.get(position).getReturnQuantity().equalsIgnoreCase("0")){
                                    //   Toast.makeText(context,returnChangeDTOS.get(position).getReturnQuantity() , Toast.LENGTH_SHORT).show();

                                    deleteCalculationChange=0;
                                  //  deleteCalculation =Float.parseFloat( txt_total_amount_return.getText().toString())- Float.parseFloat(returnChangeDTOS.get(position).getReturnValue());
                                    deleteCalculationChange =Float.parseFloat( txt_total_amount_change.getText().toString())- Float.parseFloat(returnChangeDTOS.get(position).getChangeValue());

                                    returnChangeDTOS.remove(returnChangeDTOS.get(position)) ;


                                  //txt_total_amount_return.setText(String.valueOf(deleteCalculation));
                                    txt_total_amount_change.setText(String.valueOf(deleteCalculationChange));

                                  //  deleteCalculation=0;
                                    deleteCalculationChange=0;

                                    returnChangeRecyclerAdapter.notifyDataSetChanged();
                                    dialog.dismiss();

                                }else {

                                //    Toast.makeText(context,returnChangeDTOS.get(position).getReturnQuantity() , Toast.LENGTH_SHORT).show();

                                    deleteCalculation = 0;
                                    deleteCalculationChange = 0;
                                    deleteCalculation = Float.parseFloat(txt_total_amount_return.getText().toString()) - Float.parseFloat(returnChangeDTOS.get(position).getReturnValue());
                                    deleteCalculationChange = Float.parseFloat(txt_total_amount_change.getText().toString()) - Float.parseFloat(returnChangeDTOS.get(position).getChangeValue());

                                    for (int i = 0; i < returnChangeDTOS.size(); i++) {
                                        if (returnChangeDTOS.get(i).getReturnProductId().equalsIgnoreCase(returnChangeDTOS.get(position).getReturnProductId())) {
                                            returnChangeDTOS.remove(i);
                                        }
                                    }

                                    txt_total_amount_return.setText(String.valueOf(deleteCalculation));
                                    txt_total_amount_change.setText(String.valueOf(deleteCalculationChange));

                                    deleteCalculation = 0;
                                    deleteCalculationChange = 0;

                                    returnChangeRecyclerAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
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
                routeRecyclerAdapter.filter(query);
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

            case R.id.txt_category_list:
                showCategoryListDialog1();
                store_qty="0";
                store_value="0";
                isMultiAdd=false;
                break;

                case R.id.btn_confirm_return_change:


                    if (returnChangeDTOS.size()==0){

                        Toast.makeText(context, "আগে প্রোডাক্ট সিলেক্ট করুন।", Toast.LENGTH_SHORT).show();

                    }else {

                        float returnVal=0;
                        float changeVal=0;
                      //  float ff=changeVal-returnVal;
                        returnVal= Float.parseFloat(txt_total_amount_return.getText().toString());
                        changeVal= Float.parseFloat(txt_total_amount_change.getText().toString());
                        float ff=changeVal-returnVal;
                      //  Log.e("<<>>",ff+"<<>>");
                        if (returnVal>changeVal){

                            Toast.makeText(context, "পরিবর্তন মূল্য অবশ্যই  সমান অথবা বেশি হতে হবে (সর্বোচ্চ ১০০০ টাকা)।", Toast.LENGTH_SHORT).show();

                        }else if(ff>=1000){
                            Toast.makeText(context, "পরিবর্তন মূল্য অবশ্যই ১০০০ টাকার বেশি হবে না।", Toast.LENGTH_SHORT).show();

                        }  else {

                        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
                        alertBulder.setIcon(context.getResources().getDrawable(R.drawable.search_hover)).setTitle("রিটার্ন চেঞ্জ অর্ডার কি কনফার্ম করতে চান ?");

                        alertBulder.setPositiveButton("হাঁ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                JSONObject jsonObject;
                                JSONArray returnArray = new JSONArray();

                                for (int i=0;i<returnChangeDTOS.size();i++){
                                    jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("return_category_id",returnChangeDTOS.get(i).getReturnCategoryId());
                                        jsonObject.put("return_product_id",returnChangeDTOS.get(i).getReturnProductId());
                                        jsonObject.put("return_qty",returnChangeDTOS.get(i).getReturnQuantity());
                                        jsonObject.put("return_value",returnChangeDTOS.get(i).getReturnValue());
                                        jsonObject.put("change_category_id",returnChangeDTOS.get(i).getChangeCategoryId());
                                        jsonObject.put("change_product_id",returnChangeDTOS.get(i).getChangeProductId());
                                        jsonObject.put("change_qty",returnChangeDTOS.get(i).getChangeQuantity());
                                        jsonObject.put("change_value",returnChangeDTOS.get(i).getChangeValue());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    returnArray.put(jsonObject);
                                }

                                 finalobject = new JSONObject();
                                try {

                                    //user object
                                    JSONObject userObject = new JSONObject();
                                    userObject.put("distributor_id",SharePreference.getDistributorID(context));//SharePreferenceBTP.getDistributorID(context)
                                    userObject.put("route_id",Utility.ROUTE_ID);
                                    userObject.put("retailer_id",getIntent().getStringExtra("retailerId"));
                                    userObject.put("point_id",getIntent().getStringExtra("poient_id"));
                                    userObject.put("user_id",SharePreference.getUserId(context));
                                    userObject.put("global_company_id","1");
                                    //adding all object to finalObject
                                    finalobject.put("userinfo", userObject);
                                    finalobject.put("return", returnArray);


                                   // Log.e("final>>",finalobject+"");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                sendReturnChangeData(finalobject.toString());
                              //  Log.e("<<>>",txt_total_amount_return.getText()+""+txt_total_amount_change.getText()+"--"+finalobject.toString()+"");

                            }
                        });

                        alertBulder.setNegativeButton("না", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alertBulder.show();
                        }
                    }
//                    }
                break;
        }
    }
    private void showCategoryListDialog1() {

        categoryDTOS.clear();
        for (CategoryDTO hm : categoryDTOSREserve) {
            categoryDTOS.add(hm);
        }
        productDtos.clear();
        wdialog =new Dialog(context,R.style.full_screen_dialog);
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;

//        categoryDTOS.clear();
//        categoryDTOS = databaseHelper.getAllCategoryProductActiveOnly(databaseHelper,SharePreference.getUserBusinessType(context));

        //Log.e("<<>>",exceptCategoryDTOS.size()+"");
       for (int i=0;i< exceptCategoryDTOS.size();i++){
            for (int c=0;c<categoryDTOS.size();c++){

                if (categoryDTOS.get(c).getId().equals(exceptCategoryDTOS.get(i).getCatgoryId())){
                    categoryDTOS.remove(c);
                }
            }
        }

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

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        routeRecyclerAdapter = new ProductCategoryRecyclerAdapter( categoryDTOS,context);
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

              //  Log.e(">>>ctid>>",categoryDTOS.get(position).getId()+"");
                product_change_cat_id = categoryDTOS.get(position).getId();

                productDtos = databaseHelper.getProductList(databaseHelper,categoryDTOS.get(position).getId());
                txt_category_list.setText(categoryDTOS.get(position).getName());
                PRODUCT_CATEGORY_ID = categoryDTOS.get(position).getId();
                PRODUCT_CATEGORY_NAME = categoryDTOS.get(position).getName();

                PRODUCT_CATEGORY_ID_CHANGE = categoryDTOS.get(position).getId();
                PRODUCT_CATEGORY_NAME_CHANGE = categoryDTOS.get(position).getName();

                CHANNEL_ID = categoryDTOS.get(position).getGid();
                OFFER_TYPE_ID = categoryDTOS.get(position).getOffer_type();

                showProductListDialog1();
                wdialog.dismiss();

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


    private void showReturnChangeCategoryDialog() {
        productDtosChange.clear();
        returnChangeDialog =new Dialog(context,R.style.full_screen_dialog);
        returnChangeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        returnChangeDialog.setContentView(R.layout.dialog_return_change_category);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final TextView tttt;

        final RelativeLayout rlDialogCross;
        etSearch = (EditText)returnChangeDialog.findViewById(R.id.edt_txt_search);
        route_list_recyclerView=(RecyclerView) returnChangeDialog.findViewById(R.id.route_list_recyclerView);
        imbtnCross=(ImageView)returnChangeDialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)returnChangeDialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)returnChangeDialog.findViewById(R.id.btnDoneDialog);

        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnChangeDialog.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnChangeDialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnChangeDialog.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        returnChangeCategoryRecyclerAdapter = new ReturnChangeCategoryRecyclerAdapter( returnChangeCategoryDTOS,context);
        route_list_recyclerView.setAdapter(returnChangeCategoryRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                txt_category_list_dialog.setText(returnChangeCategoryDTOS.get(position).getCategoryName());
                PRODUCT_CATEGORY_ID_CHANGE = returnChangeCategoryDTOS.get(position).getCategoryId();
                PRODUCT_CATEGORY_NAME_CHANGE = returnChangeCategoryDTOS.get(position).getCategoryName();
                productDtosChange = databaseHelper.getProductList(databaseHelper,returnChangeCategoryDTOS.get(position).getCategoryId());

                showProductListForChangeProduct2();
                returnChangeDialog.dismiss();

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
                returnChangeCategoryRecyclerAdapter.filter(query);
            }
        });

        returnChangeDialog.show();
    }
    private void showProductListDialog1() {

        productDialog =new Dialog(context,R.style.full_screen_dialog);
        productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        productDialog.setContentView(R.layout.dialog_product_list_return_change);
        final RecyclerView route_list_recyclerView;
        final Button btnDone,btn_add_product,btn_add_single_product;
        final ImageView imbtnCross,btn_add_product_cross;
        final EditText etSearch;
        row_edt_wastage = (EditText) productDialog.findViewById(R.id.row_edt_wastage);
        row_edt_order_qty = (EditText) productDialog.findViewById(R.id.row_edt_order_qty);
        row_edt_order_qty_change = (EditText) productDialog.findViewById(R.id.row_edt_order_qty_change);
        row_edt_value = (EditText) productDialog.findViewById(R.id.row_edt_value);
        row_edt_value_change = (EditText) productDialog.findViewById(R.id.row_edt_value_change);
        txt_category_list_dialog = (TextView) productDialog.findViewById(R.id.txt_category_list_dialog);
        txt_category_list_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReturnChangeCategoryDialog();
            }
        });

        btn_add_product = (Button) productDialog.findViewById(R.id.btn_add_product);
        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (row_edt_order_qty.getText().toString().equalsIgnoreCase("")||row_edt_order_qty_change.getText().toString().equalsIgnoreCase("")){

                    Toast.makeText(context, "একটি প্রোডাক্ট সিলেক্ট  করুন ", Toast.LENGTH_SHORT).show();
                    return;
                }

                float rr=0,cc=0;

               for (int i=0;i<returnChangeDTOS.size();i++){
                   if (product_change_cat_id.equalsIgnoreCase(returnChangeDTOS.get(i).getChangeCategoryId())){
                       rr+= Float.parseFloat(returnChangeDTOS.get(i).getReturnValue());
                       cc+= Float.parseFloat(returnChangeDTOS.get(i).getChangeValue());
                   }
               }
             //  Log.e("response",rr+"<<>>"+cc+"<<>>");
               if(row_edt_value.getText().toString().equalsIgnoreCase("")){
                   row_edt_value.setText("0");
               }
                if(row_edt_value_change.getText().toString().equalsIgnoreCase("")){
                    row_edt_value_change.setText("0");
                }

               float rv = Float.parseFloat(row_edt_value.getText().toString());
               float cv = Float.parseFloat(row_edt_value_change.getText().toString());
                rr+= rv;
                cc+= cv;
                float gap= cc-rr;
//           if (!SharePreference.getUserBusinessType(context).equals("2"))
             // if (!SharePreference.getUserBusinessType(context).equals("2"))
//              if (SharePreference.getUserBusinessType(context).equalsIgnoreCase("2")){
//                  //no validation for power chanel
//                  Toast.makeText(context, SharePreference.getUserBusinessType(context), Toast.LENGTH_SHORT).show();
//              }else {
                if (!SharePreference.getUserBusinessType(context).equals("2")){
                if (gap>=1000){
                    Toast.makeText(context, "পরিবর্তন ভেল্যু ১০০০ এর বেশি হবে না।", Toast.LENGTH_SHORT).show();
                    return;
              }}

              // Log.e("response",gap+"<>"+"gap");

                for (int p=0;p<returnChangeDTOS.size();p++){
                    if (returnChangeDTOS.get(p).getReturnProductId().equalsIgnoreCase(PRODUCT_ID)){
                        row_edt_order_qty.setText("0");
                        row_edt_value.setText("0");
                    }
                }

                 productCheckId = PRODUCT_ID;

                //return
                ReturnChangeDTO orderDTO = new ReturnChangeDTO();
                orderDTO.setReturnCategoryId(PRODUCT_CATEGORY_ID);
                orderDTO.setReturnCategoryName(PRODUCT_CATEGORY_NAME);
                orderDTO.setReturnProductId(PRODUCT_ID);
                orderDTO.setReturnProductName(PRODUCT_NAME);
                orderDTO.setReturnProductSingleValue(String.valueOf(PRODUCT_SINGLE_DEPO_PRICE));

                if (row_edt_order_qty.getText().toString().equalsIgnoreCase("")){
                    row_edt_order_qty.setText("0");
                    row_edt_value.setText("0");
                }
                orderDTO.setReturnQuantity(row_edt_order_qty.getText().toString());
                orderDTO.setReturnValue(row_edt_value.getText().toString());

                productCheckValueReturn=row_edt_value.getText().toString();

                // change
                orderDTO.setChangeCategoryId(PRODUCT_CATEGORY_ID_CHANGE);
                orderDTO.setChangeCategoryName(PRODUCT_CATEGORY_NAME_CHANGE);
                orderDTO.setChangeProductId(PRODUCT_ID_CHANGE);
                orderDTO.setChangeProductName(PRODUCT_NAME_CHANGE);
                orderDTO.setChangeProductSingleValue(String.valueOf(PRODUCT_SINGLE_DEPO_PRICE_CHANGE));

                if (row_edt_order_qty_change.getText().toString().equalsIgnoreCase("")){
                    row_edt_order_qty_change.setText("0");
                    row_edt_value_change.setText("0");
                }
                orderDTO.setChangeQuantity(row_edt_order_qty_change.getText().toString());
                orderDTO.setChangeValue(row_edt_value_change.getText().toString());
                productCheckValueChange=row_edt_value_change.getText().toString();

                // add all item
                returnChangeDTOS.add(orderDTO);

                //hide add dialog
                rel_lay_addproduct.setVisibility(View.GONE);

                // return total count
                if (row_edt_order_qty.getText().length() == 0) {
                }else  {

                ff = 0;
                for (int i = 0; i < returnChangeDTOS.size(); i++) {
                    ff += Float.parseFloat(returnChangeDTOS.get(i).getReturnValue());
                  //  txt_bucket_amount.setText(String.valueOf(ff));
                    txt_total_amount_return.setText(String.valueOf(ff));
                }
                }

                // change total count
                if (row_edt_order_qty_change.getText().length() == 0) {
                }else  {

                gg = 0;
                for (int i = 0; i < returnChangeDTOS.size(); i++) {
                    gg += Float.parseFloat(returnChangeDTOS.get(i).getChangeValue());
                    txt_total_amount_change.setText(String.valueOf(gg));
                }
                }

                float returnToatal=0;  float changeToatal=0; float rCToatal=0;
                returnToatal = Float.parseFloat(txt_total_amount_return.getText().toString());
                changeToatal = Float.parseFloat(txt_total_amount_change.getText().toString());
                rCToatal =changeToatal-returnToatal;

//                if (SharePreference.getUserBusinessType(context).equalsIgnoreCase("2")){
//                    //no validation for power chanel
//                    Toast.makeText(context, SharePreference.getUserBusinessType(context), Toast.LENGTH_SHORT).show();
//                }else {

                if (!SharePreference.getUserBusinessType(context).equals("2"))
                    if (rCToatal <= 1000) {
                        //  Log.e("<<true>>>",rCToatal+"true");

                    } else {
                        //  Log.e("<<false>>>",rCToatal+"false");

                        deleteCalculation = 0;
                        deleteCalculationChange = 0;
                        deleteCalculation = Float.parseFloat(txt_total_amount_return.getText().toString()) - Float.parseFloat(productCheckValueReturn);
                        deleteCalculationChange = Float.parseFloat(txt_total_amount_change.getText().toString()) - Float.parseFloat(productCheckValueChange);

                        for (int i = 0; i < returnChangeDTOS.size(); i++) {
                            if (returnChangeDTOS.get(i).getReturnProductId().equalsIgnoreCase(productCheckId)) {
                                returnChangeDTOS.remove(i);
                            }
                        }

                        txt_total_amount_return.setText(String.valueOf(deleteCalculation));
                        txt_total_amount_change.setText(String.valueOf(deleteCalculationChange));

                        deleteCalculation = 0;
                        deleteCalculationChange = 0;
                        productCheckId = "";
                        productCheckValueReturn = "";
                        productCheckValueChange = "";
                        Toast.makeText(context, "পরিবর্তন ভেল্যু ১,০০০ এর বেশি হবে না।", Toast.LENGTH_SHORT).show();
                    }
//                }

                returnChangeRecyclerAdapter.notifyDataSetChanged();
            }
        });


        btn_add_single_product = (Button) productDialog.findViewById(R.id.btn_add_single_product);
        btn_add_single_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (row_edt_order_qty.getText().toString().equalsIgnoreCase("")){

                    Toast.makeText(context, "একটি প্রোডাক্ট এড করুন --", Toast.LENGTH_SHORT).show();
                    return;
                }

                btn_add_product.setVisibility(View.GONE);

                if (isMultiAdd==false) {
                    store_qty = row_edt_order_qty.getText().toString();
                    store_value = row_edt_value.getText().toString();
                    isMultiAdd=true;
                }

                for (int p=0;p<returnChangeDTOS.size();p++){
                    if (returnChangeDTOS.get(p).getReturnProductId().equalsIgnoreCase(PRODUCT_ID)){
                        row_edt_order_qty.setText("0");
                        row_edt_value.setText("0");
                    }
                }

                 productCheckId=PRODUCT_ID;

                //return
                ReturnChangeDTO orderDTO = new ReturnChangeDTO();
                orderDTO.setReturnCategoryId(PRODUCT_CATEGORY_ID);
                orderDTO.setReturnCategoryName(PRODUCT_CATEGORY_NAME);
                orderDTO.setReturnProductId(PRODUCT_ID);
                orderDTO.setReturnProductName(PRODUCT_NAME);
                orderDTO.setReturnProductSingleValue(String.valueOf(PRODUCT_SINGLE_DEPO_PRICE));

                if (row_edt_order_qty.getText().toString().equalsIgnoreCase("")){
                    row_edt_order_qty.setText("0");
                    row_edt_value.setText("0");
                }
                orderDTO.setReturnQuantity(row_edt_order_qty.getText().toString());
                orderDTO.setReturnValue(row_edt_value.getText().toString());

                productCheckValueReturn=row_edt_value.getText().toString();

                // change
                orderDTO.setChangeCategoryId(PRODUCT_CATEGORY_ID_CHANGE);
                orderDTO.setChangeCategoryName(PRODUCT_CATEGORY_NAME_CHANGE);
                orderDTO.setChangeProductId(PRODUCT_ID_CHANGE);
                orderDTO.setChangeProductName(PRODUCT_NAME_CHANGE);
                orderDTO.setChangeProductSingleValue(String.valueOf(PRODUCT_SINGLE_DEPO_PRICE_CHANGE));

                if (row_edt_order_qty_change.getText().toString().equalsIgnoreCase("")){
                    row_edt_order_qty_change.setText("0");
                    row_edt_value_change.setText("0");
                }
                orderDTO.setChangeQuantity(row_edt_order_qty_change.getText().toString());
                orderDTO.setChangeValue(row_edt_value_change.getText().toString());
                productCheckValueChange=row_edt_value_change.getText().toString();


                // add all item
                returnChangeDTOS.add(orderDTO);

                //hide add dialog

                // return total count
                if (row_edt_order_qty.getText().length() == 0) {
                }else  {
//                    if (flag.equalsIgnoreCase("1")){
//                    }else {
                ff = 0;
                for (int i = 0; i < returnChangeDTOS.size(); i++) {
                    ff += Float.parseFloat(returnChangeDTOS.get(i).getReturnValue());
                   // txt_bucket_amount.setText(String.valueOf(ff));
                    txt_total_amount_return.setText(String.valueOf(ff));
//                }
                    }
                }

                // change total count
                if (row_edt_order_qty_change.getText().length() == 0) {
                }else  {

                gg = 0;
                for (int i = 0; i < returnChangeDTOS.size(); i++) {
                    gg += Float.parseFloat(returnChangeDTOS.get(i).getChangeValue());
                    txt_total_amount_change.setText(String.valueOf(gg));
                }
                }

                float returnToatal=0;  float changeToatal=0; float rCToatal=0;
                returnToatal = Float.parseFloat(txt_total_amount_return.getText().toString());
                changeToatal = Float.parseFloat(txt_total_amount_change.getText().toString());
                rCToatal =changeToatal-returnToatal;

                Toast.makeText(context, "প্রোডাক্ট এড করা হয়েছে।", Toast.LENGTH_SHORT).show();

                row_edt_order_qty.setText(store_qty);
                row_edt_value.setText(store_value);

                row_edt_order_qty_change.setText("");
                row_edt_value_change.setText("");

                if (rCToatal<=1001){
                  //  Log.e("<<true>>>",rCToatal+"true");

                }else {

                  //  Log.e("<<false>>>",rCToatal+"false");

                    deleteCalculation=0;
                    deleteCalculationChange=0;
                    deleteCalculation =Float.parseFloat( txt_total_amount_return.getText().toString())- Float.parseFloat(productCheckValueReturn);
                    deleteCalculationChange =Float.parseFloat( txt_total_amount_change.getText().toString())- Float.parseFloat(productCheckValueChange);

                    for (int i=0;i<returnChangeDTOS.size();i++){
                        if (returnChangeDTOS.get(i).getReturnProductId().equalsIgnoreCase(productCheckId)){
                            returnChangeDTOS.remove(i);
                        }
                    }

                    txt_total_amount_return.setText(String.valueOf(deleteCalculation));
                    txt_total_amount_change.setText(String.valueOf(deleteCalculationChange));

                    deleteCalculation=0;
                    deleteCalculationChange=0;
                    productCheckId="";
                    productCheckValueReturn="";
                    productCheckValueChange="";
                    Toast.makeText(context, "পরিবর্তন ভেল্যু ১০০০ এর বেশি হবে না।", Toast.LENGTH_SHORT).show();

                }

                returnChangeRecyclerAdapter.notifyDataSetChanged();

            }
        });

        final RelativeLayout rlDialogCross;
        etSearch = (EditText)productDialog.findViewById(R.id.edt_txt_search);
        product_list_recyclerView=(RecyclerView) productDialog.findViewById(R.id.product_list_recyclerView);
        imbtnCross=(ImageView)productDialog.findViewById(R.id.btn_dialog_cross);
        btn_add_product_cross=(ImageView)productDialog.findViewById(R.id.btn_add_product_cross);
        rlDialogCross = (RelativeLayout)productDialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)productDialog.findViewById(R.id.btnDoneDialog);
        rel_lay_addproduct = (RelativeLayout) productDialog.findViewById(R.id.rel_lay_addproduct);
        rel_lay_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txt_product_name = (TextView) productDialog.findViewById(R.id.txt_product_name);
        txt_stock_qty = (TextView) productDialog.findViewById(R.id.txt_stock_qty);
        txt_book_qty = (TextView) productDialog.findViewById(R.id.txt_book_qty);

        txt_stock_qty1 = (TextView) productDialog.findViewById(R.id.txt_stock_qty1);
        txt_book_qty1 = (TextView) productDialog.findViewById(R.id.txt_book_qty1);
        txt_product_name_change = (TextView) productDialog.findViewById(R.id.txt_product_name_change);
        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDialog.dismiss();
            }
        });
        btn_add_product_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_lay_addproduct.setVisibility(View.GONE);
            }
        });
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


//                for (int i=0;i<orderDtos.size();i++){
//                    if (orderDtos.get(i).getProduct_id().equalsIgnoreCase(productDtos.get(position).getProduct_id())){
//                        row_edt_order_qty.setText(orderDtos.get(i).getProduct_qty());
//                    }
//                }

                returnCategoryCheck();

                showAddProduct(productDtos.get(position).getProduct_name(),productDtos.get(position).getProduct_id());

                PRODUCT_ID = productDtos.get(position).getProduct_id();
                PRODUCT_NAME = productDtos.get(position).getProduct_name();

                PRODUCT_ID_CHANGE = productDtos.get(position).getProduct_id();
                PRODUCT_NAME_CHANGE = productDtos.get(position).getProduct_name();

//                if (CHANNEL_ID.equalsIgnoreCase("8")){
//                    PRODUCT_VALUE = Double.parseDouble(productDtos.get(position).getProduct_MRP_price());
//                    PRODUCT_SINGLE_DEPO_PRICE= Double.parseDouble(productDtos.get(position).getProduct_MRP_price());
//
//                    PRODUCT_VALUE_CHANGE = Double.parseDouble(productDtos.get(position).getProduct_MRP_price());
//                    PRODUCT_SINGLE_DEPO_PRICE_CHANGE= Double.parseDouble(productDtos.get(position).getProduct_MRP_price());
//                }else {

                    PRODUCT_VALUE = Double.parseDouble(productDtos.get(position).getProduct_price());
                    PRODUCT_SINGLE_DEPO_PRICE= Double.parseDouble(productDtos.get(position).getProduct_price());

                    PRODUCT_VALUE_CHANGE = Double.parseDouble(productDtos.get(position).getProduct_price());
                    PRODUCT_SINGLE_DEPO_PRICE_CHANGE= Double.parseDouble(productDtos.get(position).getProduct_price());
//                }

//                PRODUCT_VALUE = Double.parseDouble(productDtos.get(position).getProduct_price());
//                PRODUCT_SINGLE_DEPO_PRICE= Double.parseDouble(productDtos.get(position).getProduct_price());
//
//                PRODUCT_VALUE_CHANGE = Double.parseDouble(productDtos.get(position).getProduct_price());
//                PRODUCT_SINGLE_DEPO_PRICE_CHANGE= Double.parseDouble(productDtos.get(position).getProduct_price());

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

        row_edt_order_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (row_edt_order_qty.getText().length()==0){
                    row_edt_value.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
               if (row_edt_order_qty.getText().equals("")){
                   row_edt_value.setText("0");
               }else {
                   try{
                   PRODUCT_QTY = Integer.valueOf(row_edt_order_qty.getText().toString());
                   double totalValue = PRODUCT_VALUE * PRODUCT_QTY;
                   row_edt_value.setText(String.valueOf(totalValue));
               }catch (NumberFormatException ne){
                   } }
            }
        });

        row_edt_order_qty_change.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (row_edt_order_qty_change.getText().length()==0){
                    row_edt_value_change.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
               if (row_edt_order_qty_change.getText().equals("")){
                   row_edt_value_change.setText("0");
               }else {
                   try{
                   PRODUCT_QTY_CHANGE = Integer.valueOf(row_edt_order_qty_change.getText().toString());
                   double totalValue = PRODUCT_VALUE_CHANGE * PRODUCT_QTY_CHANGE;
                   row_edt_value_change.setText(String.valueOf(totalValue));
               }catch (NumberFormatException ne){

                   }
               }
            }
        });

        productDialog.show();
    }

    private void showProductListForChangeProduct2() {

        dialogProductChange =new Dialog(context,R.style.full_screen_dialog);
        dialogProductChange.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProductChange.setContentView(R.layout.dialog_product_list_change);
        final RecyclerView route_list_recyclerView;
        final Button btnDone,btn_add;
        final ImageView imbtnCross,btn_add_product_cross;
        final EditText etSearch;
        final RelativeLayout rlDialogCross;

        etSearch = (EditText)dialogProductChange.findViewById(R.id.edt_txt_search);
        product_list_recyclerView=(RecyclerView) dialogProductChange.findViewById(R.id.product_list_recyclerView);
        imbtnCross=(ImageView)dialogProductChange.findViewById(R.id.btn_dialog_cross);
        btn_add_product_cross=(ImageView)dialogProductChange.findViewById(R.id.btn_add_product_cross);
        rlDialogCross = (RelativeLayout)dialogProductChange.findViewById(R.id.rl_dialog_cross);

        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProductChange.dismiss();
            }
        });

        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProductChange.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        product_list_recyclerView.setLayoutManager(linearLayoutManager);
        changeListRecyclerAdapter = new ProductChangeListRecyclerAdapter(productDtosChange,context);
        product_list_recyclerView.setAdapter(changeListRecyclerAdapter);

        product_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                txt_product_name_change.setText(productDtosChange.get(position).getProduct_name());
                PRODUCT_ID_CHANGE = productDtosChange.get(position).getProduct_id();
                PRODUCT_NAME_CHANGE = productDtosChange.get(position).getProduct_name();
                PRODUCT_VALUE_CHANGE = Double.parseDouble(productDtosChange.get(position).getProduct_price());
                PRODUCT_SINGLE_DEPO_PRICE_CHANGE= Double.parseDouble(productDtosChange.get(position).getProduct_price());

                row_edt_order_qty_change.setText("");
                row_edt_value_change.setText("");
                dialogProductChange.dismiss();
                getStockBooking(txt_stock_qty1,txt_book_qty1,productDtosChange.get(position).getProduct_id(),"0");

            }
        }));

        dialogProductChange.show();
    }

    public void showAddProduct(String productName,String productid){
        getStockBooking(txt_stock_qty,txt_book_qty,productid,"1");
        rel_lay_addproduct.setVisibility(View.VISIBLE);
        txt_product_name.setText(productName);
        txt_product_name_change.setText("Product Name");
        row_edt_order_qty.setText("");
        row_edt_order_qty_change.setText("");
        row_edt_value.setText("");
        row_edt_value_change.setText("");
    }

    public void getStockBooking(final TextView stock, final TextView book,String productid,String val){


        final ProgressDialog pd = new ProgressDialog(context);

        vm.sendRequestToServer(context, getResources().getString(R.string.base_url)+"api/fo/show-stock-me", jrf.getStock(SharePreference.getUserPointId(context),productid), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    //  Log.e("jsonObject>",jsonObject.toString()+"");

                    if (store_value.equalsIgnoreCase("1")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                            stock.setText("Stock Qty :" + jsonObject.getString("stockQty"));
                            book.setText("Booked Qty :" + jsonObject.getString("demandQty"));

                        } else {
                            Toast.makeText(context, "স্টক এবং বুকিং তথ্য পাওয়া যায়নি.", Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                            stock.setText("Stock Qty :" + jsonObject.getString("stockQty"));
                            book.setText("Booked Qty :" + jsonObject.getString("demandQty"));

                        } else {
                            Toast.makeText(context, "স্টক এবং বুকিং তথ্য পাওয়া যায়নি.", Toast.LENGTH_SHORT).show();

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void returnCategoryCheck(){

        returnChangeCategoryDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer2(context, getResources().getString(R.string.base_url)+"api/apps/return-change-allow-category", jrf.getReturnCategoryCheck(PRODUCT_CATEGORY_ID), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                //    Log.e("confirmResponse>1>",jsonObject+"");

                    if (jsonObject.getString("status").equalsIgnoreCase("1")){

                        JSONArray jsonArray = jsonObject.getJSONArray("allow_category_list");

                        for (int i=0;i<jsonArray.length();i++){

                            JSONObject listObject = jsonArray.getJSONObject(i);
                            ReturnChangeCategoryDTO returnChangeCategoryDTO = new ReturnChangeCategoryDTO();
                            returnChangeCategoryDTO.setCategoryId(listObject.getString("allow_category"));
                            returnChangeCategoryDTO.setCategoryName(listObject.getString("category_name"));
                            returnChangeCategoryDTOS.add(returnChangeCategoryDTO);
                        }

                    }else {
                        ReturnChangeCategoryDTO returnChangeCategoryDTO = new ReturnChangeCategoryDTO();
                        returnChangeCategoryDTO.setCategoryId(PRODUCT_CATEGORY_ID);
                        returnChangeCategoryDTO.setCategoryName(PRODUCT_CATEGORY_NAME);
                        returnChangeCategoryDTOS.add(returnChangeCategoryDTO);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        });
    }

    public void sendReturnChangeData(String returnChangeJson){

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer2(context, getResources().getString(R.string.base_url)+"api/apps/return-change-submit", returnChangeJson, new VolleyCallBack() {
            @Override
            public void onSuccess(final String result) {

                runOnUiThread(new Runnable() {
                    public void run() {

                try {

                    JSONObject jsonObject = new JSONObject(result);

                  //  Log.e("<<>>",jsonObject+"");

                    if (jsonObject.getString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        returnChangeDTOS.clear();
                        returnChangeRecyclerAdapter.notifyDataSetChanged();
                        txt_total_amount_return.setText("0");
                        txt_total_amount_change.setText("0");
                        databaseHelper.insertReturnChangeStatus(databaseHelper,SharePreference.getUserId(context),getIntent().getStringExtra("retailerId"),
                                SharePreference.getUserId(context),"Collected",formattedDate);
                            finish();

                    }else if(jsonObject.getString("status").equalsIgnoreCase("1")){
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(context, "Sync failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();

                    }
                });
            }

        });
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
        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("Internet Alert").setMessage("Your device is not connected to internet. Connect to internet and try again.");
        alertBulder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        alertBulder.setNeutralButton("Mobile Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertBulder.show();
    }

    public void exceptionCategory(){

        //  Log.e("url",url+"");https://ssforcenewdev.ssgbd.com/
        StringRequest stringRequest = new StringRequest(Request.Method.GET,  getResources().getString(R.string.base_url)+"api/apps/return-change-exception-category", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respjsonObj = new JSONObject(response);

               //   Log.e("respjsonObj",respjsonObj+"<<>>");
                    if (respjsonObj.getString("status").equalsIgnoreCase("1")) {

                        JSONArray jsonArray = respjsonObj.getJSONArray("except_cat_id");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            ExceptCatDTO exceptCatDTO = new ExceptCatDTO();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            exceptCatDTO.setCatgoryId(jsonObject.getString("cat_id"));
                            exceptCategoryDTOS.add(exceptCatDTO);

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Utility.dialog.closeProgressDialog();
            }
        });
        queue.add(stringRequest);
    }

    public void syncCategory(String date) {
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(context,getResources().getString(R.string.base_url) + "api/ma/sync-master-categories",
                jp.jsonSYNC(SharePreference.getUserId(context),SharePreference.getUserBusinessType(context), SharePreference.getUserPointId(context),date), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {

                        try {
                            JSONObject jsonObject1 = new JSONObject(result);

                            if (jsonObject1.getString("status").equalsIgnoreCase("1")) {
                                try {
                                    // JSONObject respjsonObj = new JSONObject(SharePreference.getRouteData(getActivity()));
                                    JSONArray routeArray = jsonObject1.getJSONArray("category_list");
                                    for (int i = 0; i < routeArray.length(); i++) {
                                        JSONObject routeObject = routeArray.getJSONObject(i);

                                        CategoryDTO categoryDTO =  new CategoryDTO();

                                        categoryDTO.setId(routeObject.getString("id"));
                                        categoryDTO.setGid(routeObject.getString("gid"));
                                        categoryDTO.setG_name(routeObject.getString("g_name"));
                                        categoryDTO.setG_code(routeObject.getString("g_code"));
                                        categoryDTO.setName(routeObject.getString("name"));

                                        categoryDTO.setShort_name(routeObject.getString("short_name"));

                                        categoryDTO.setAvg_price(routeObject.getString("avg_price"));

                                        categoryDTO.setOffer_type(routeObject.getString("offer_type"));

                                        if(routeObject.getString("status").equalsIgnoreCase("0")){
                                            categoryDTOS.add(categoryDTO);
                                            categoryDTOSREserve.add(categoryDTO);
                                        }

                                    }

                                    // Log.e("response",categoryDTOS.size()+"<>");
                                } catch (JSONException je) {
//                        pd.dismiss();
                                }
//                    pd.dismiss();

                            }else {
//                    pd.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}



