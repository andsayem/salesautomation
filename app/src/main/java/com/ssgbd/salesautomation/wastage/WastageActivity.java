package com.ssgbd.salesautomation.wastage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.OrderRecyclerAdapter_Wastage;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductListRecyclerAdapter;

import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;

import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.WastageOrderDTO;
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

public class WastageActivity extends AppCompatActivity implements View.OnClickListener{

    VolleyMethods vm = new VolleyMethods();
    JsonRequestFormate jrf = new JsonRequestFormate();
    private Context context;
    private Toolbar mToolbar;
    TextView txt_toolbar,txt_retailer_name,txt_category_list;
    JSONObject finalobject;
    private Dialog wdialog,productDialog;
    TextView txt_route_list,txt_route_name;
    DatabaseHelper databaseHelper;
    ProductCategoryRecyclerAdapter routeRecyclerAdapter;
    ProductListRecyclerAdapter productListRecyclerAdapter;
    RecyclerView product_list_recyclerView;

//    private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<ProductDTO> productDtos = new ArrayList<>();
    ArrayList<WastageOrderDTO> orderDtos = new ArrayList<>();

    String CHANNEL_ID="null";

    public RelativeLayout rel_lay_addproduct;
    public TextView txt_product_name;
    public Button btn_save_order;
    private LinearLayout linlay_category_portion;

    // select product
    String PRODUCT_CATEGORY_ID="";
    String PRODUCT_CATEGORY_NAME="";
    String PRODUCT_ID="";
    String PRODUCT_ID_RETURN="";
    String PRODUCT_NAME="";
    double PRODUCT_VALUE=0;
    double PRODUCT_QTY= 0;
    double PRODUCT_SINGLE_DEPO_PRICE = 0;
    String OFFER_TYPE_ID= "";
    String WESTAGE_QTY="";
    String isSaved="0";
    EditText row_edt_order_qty,row_edt_value;
    TextView row_edt_order_qty1,row_edt_value1;

    // final order
    RecyclerView order_list_recyclerView;
    EditText edt_search_order_product;
    OrderRecyclerAdapter_Wastage orderRecyclerAdapter;

    //bucket
    TextView txt_bucket_amount,txt_bucket_amounttitle,txt_total_amount,txt_total_amount_return;
    LinearLayout linlay_buket_amount;
    float ff=0;
    float returnPP=0;
    float  deleteCalculation=0;
    float  deleteCalculation1=0;

    String routeId = "";
    String singlePrice = "";
   // String toDayDate = "";
    String orderType = "";
    String status = "1";

    // all data check values
    String isArrayUpdate = "false";
    String isRetailerIdExist = "false";
    ArrayList<CategoryDTO> categoryDTOSREserve = new ArrayList<>();
    String formattedDate="";

    ArrayList<DBOrderDTO> orderListDTOS = new ArrayList<>();

    String isReturn="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wastage_screen_activity);
        context = this;

        loadToolBar();
        initUi();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
        syncCategory("");

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
        txt_toolbar.setText("Wastage");

        linlay_buket_amount = (LinearLayout) mToolbar.findViewById(R.id.linlay_buket_amount);
        linlay_buket_amount.setOnClickListener(this);
      //  linlay_buket_amount.setClickable(false);
        txt_bucket_amount = (TextView) mToolbar.findViewById(R.id.txt_bucket_amount);
        txt_total_amount = (TextView) findViewById(R.id.txt_total_amount);
        txt_total_amount_return = (TextView) findViewById(R.id.txt_total_amount_return);
        txt_bucket_amounttitle = (TextView) mToolbar.findViewById(R.id.txt_bucket_amounttitle);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);

    }
    private void initUi() {

        btn_save_order = (Button) findViewById(R.id.btn_save_order);
        btn_save_order.setOnClickListener(this);
        linlay_category_portion = (LinearLayout)findViewById(R.id.linlay_category_portion);
        txt_retailer_name = (TextView) findViewById(R.id.txt_retailer_name);
        txt_retailer_name.setText(getIntent().getStringExtra("retailerName"));
        txt_category_list  = (TextView) findViewById(R.id.txt_category_list);
        txt_category_list.setOnClickListener(this);


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
        orderRecyclerAdapter = new OrderRecyclerAdapter_Wastage(orderDtos,context);
        order_list_recyclerView.setAdapter(orderRecyclerAdapter);


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
                showCategoryListDialog();
                break;

                case R.id.linlay_buket_amount:

                  //  Log.e("finalobject",finalobject+"");

                break;

                case R.id.btn_save_order:
                    float tt=0,ww=0,rr=0;
                    ww= Float.parseFloat(txt_total_amount.getText().toString());
                    rr= Float.parseFloat(txt_total_amount_return.getText().toString());
                    float ff=rr-ww;

                    if (orderDtos.size()==0) {

                        Toast.makeText(context, "Please select product first", Toast.LENGTH_SHORT).show();
                    }
                     else if(ff>=1000){
                            Toast.makeText(context, "পরিবর্তন মূল্য অবশ্যই ১০০০ টাকার বেশি হবে না.", Toast.LENGTH_SHORT).show();

                        }
                        else {

                        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
                        alertBulder.setIcon(context.getResources().getDrawable(R.drawable.search_hover)).setTitle("Do you  want to submit this wastage list?");

                        alertBulder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                JSONObject jsonObject;
                                JSONArray jsonArray = new JSONArray();

                                for (int i=0;i<orderDtos.size();i++){
                                    jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("wastage_category_id",orderDtos.get(i).getProduct_category_id());
                                        jsonObject.put("wastage_product_id",orderDtos.get(i).getProduct_id());
                                        jsonObject.put("wastage_product_unit_price",orderDtos.get(i).getProduct_depo_price());
                                        jsonObject.put("wastage_qty",orderDtos.get(i).getProduct_qty());
                                        jsonObject.put("wastage_value",orderDtos.get(i).getProduct_price());

                                        jsonObject.put("replace_category_id",orderDtos.get(i).getProduct_category_id_return());
                                        jsonObject.put("replace_product_id",orderDtos.get(i).getProduct_id_return());
                                        jsonObject.put("replace_product_unit_price",orderDtos.get(i).getProduct_depo_price_return());
                                        jsonObject.put("replace_qty",orderDtos.get(i).getProduct_qty_return());
                                        jsonObject.put("replace_value",orderDtos.get(i).getProduct_price_return());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    jsonArray.put(jsonObject);
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
                                    finalobject.put("wastage", jsonArray);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            //   Log.e(">>>>>>>",finalobject.toString()+"");

                                if (!isInternetAvailable(context)) {
                                    internetAlert(context);

                                }else{
                                    confirmOrder(finalobject.toString());
                                }
                            }
                        });

                        alertBulder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alertBulder.show();
                    }
                break;
        }
    }
    private void showCategoryListDialog() {
        categoryDTOS.clear();
        for (CategoryDTO hm : categoryDTOSREserve) {
            categoryDTOS.add(hm);
        }
        productDtos.clear();
        wdialog =new Dialog(context);
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final TextView tttt;

//      categoryDTOS.clear();
//      categoryDTOS = databaseHelper.getAllCategoryProductActiveOnly(databaseHelper,SharePreference.getUserBusinessType(context));

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

               // Log.e(">>>ctid>>",categoryDTOS.get(position).getId()+"");

                productDtos = databaseHelper.getProductList(databaseHelper,categoryDTOS.get(position).getId());
                txt_category_list.setText(categoryDTOS.get(position).getName());
                PRODUCT_CATEGORY_ID = categoryDTOS.get(position).getId();
                PRODUCT_CATEGORY_NAME = categoryDTOS.get(position).getName();
                OFFER_TYPE_ID = categoryDTOS.get(position).getOffer_type();
                CHANNEL_ID = categoryDTOS.get(position).getGid();
                showProductListDialog();
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
    private void showProductListDialog() {

        productDialog =new Dialog(context);
        productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        productDialog.setContentView(R.layout.dialog_product_list_wastage);
        final RecyclerView route_list_recyclerView;
        final Button btnDone,btn_add;
        final ImageView imbtnCross,btn_add_product_cross;
        final EditText etSearch;
        etSearch = (EditText)productDialog.findViewById(R.id.edt_txt_search);

        row_edt_order_qty = (EditText) productDialog.findViewById(R.id.row_edt_order_qty);
        row_edt_value = (EditText) productDialog.findViewById(R.id.row_edt_value);

        row_edt_order_qty1 = (TextView) productDialog.findViewById(R.id.row_edt_order_qty1);
        row_edt_value1 = (TextView) productDialog.findViewById(R.id.row_edt_value1);


        btn_add = (Button) productDialog.findViewById(R.id.btn_add_product);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (isReturn.equalsIgnoreCase("1")){
                 //   Log.e("PRODUCT_ID>",PRODUCT_ID+">>>"+"");

                    for (int p = 0; p < orderDtos.size(); p++) {
                        if (orderDtos.get(p).getProduct_id().equalsIgnoreCase(PRODUCT_ID_RETURN)) {
                            orderDtos.get(p).setProduct_category_id_return(PRODUCT_CATEGORY_ID);
                            orderDtos.get(p).setProduct_category_name_return(PRODUCT_CATEGORY_NAME);
                            orderDtos.get(p).setProduct_id_return(PRODUCT_ID);
                            orderDtos.get(p).setProduct_name_return(PRODUCT_NAME);
                            orderDtos.get(p).setProduct_depo_price_return(String.valueOf(PRODUCT_SINGLE_DEPO_PRICE));

                            if (row_edt_order_qty.getText().toString().equalsIgnoreCase("")) {
                                row_edt_order_qty.setText("0");
                                row_edt_value.setText("0");
                            }

                            orderDtos.get(p).setProduct_qty_return(row_edt_order_qty.getText().toString());
                            orderDtos.get(p).setProduct_price_return(row_edt_value.getText().toString());
                        }
                    }

                    isReturn="0";
                    rel_lay_addproduct.setVisibility(View.GONE);

                }else {

                    for (int p = 0; p < orderDtos.size(); p++) {
                        if (orderDtos.get(p).getProduct_id().equalsIgnoreCase(PRODUCT_ID)) {
                            orderDtos.remove(p);

                        }
                    }

                    WastageOrderDTO orderDTO = new WastageOrderDTO();
                    orderDTO.setProduct_category_id(PRODUCT_CATEGORY_ID);
                    orderDTO.setProduct_category_name(PRODUCT_CATEGORY_NAME);
                    orderDTO.setProduct_id(PRODUCT_ID);
                    orderDTO.setProduct_name(PRODUCT_NAME);
                    orderDTO.setProduct_depo_price(String.valueOf(PRODUCT_SINGLE_DEPO_PRICE));
                    orderDTO.setOffer_type(OFFER_TYPE_ID);

                    orderDTO.setProduct_category_id_return(PRODUCT_CATEGORY_ID);
                    orderDTO.setProduct_category_name_return(PRODUCT_CATEGORY_NAME);
                    orderDTO.setProduct_id_return(PRODUCT_ID);
                    orderDTO.setProduct_name_return(PRODUCT_NAME);
                    orderDTO.setProduct_depo_price_return(String.valueOf(PRODUCT_SINGLE_DEPO_PRICE));


                    if (row_edt_order_qty.getText().toString().equalsIgnoreCase("")) {
                        row_edt_order_qty.setText("0");
                        row_edt_value.setText("0");
                    }
                    orderDTO.setProduct_qty(row_edt_order_qty.getText().toString());
                    orderDTO.setProduct_price(row_edt_value.getText().toString());
                    orderDTO.setProduct_qty_return(row_edt_order_qty.getText().toString());
                    orderDTO.setProduct_price_return(row_edt_value.getText().toString());

                    orderDtos.add(orderDTO);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    rel_lay_addproduct.setVisibility(View.GONE);


                    if (row_edt_order_qty.getText().length() == 0) {
                    } else {

                        ff = 0;
                        returnPP = 0;
                        for (int i = 0; i < orderDtos.size(); i++) {
                            ff += Float.parseFloat(orderDtos.get(i).getProduct_price());
                            returnPP += Float.parseFloat(orderDtos.get(i).getProduct_price_return());
                            txt_total_amount.setText(String.valueOf(ff));
                            txt_total_amount_return.setText(String.valueOf(returnPP));
                        }
                    }
                }
                orderRecyclerAdapter.notifyDataSetChanged();

                ff = 0;
                returnPP = 0;
                for (int i = 0; i < orderDtos.size(); i++) {
                    ff += Float.parseFloat(orderDtos.get(i).getProduct_price());
                    returnPP += Float.parseFloat(orderDtos.get(i).getProduct_price_return());
                    txt_total_amount.setText(String.valueOf(ff));
                    txt_total_amount_return.setText(String.valueOf(returnPP));
                }
            }
        });

        final RelativeLayout rlDialogCross;
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
        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDialog.dismiss();
            }
        });
        btn_add_product_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
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

                showAddProduct(productDtos.get(position).getProduct_name(),productDtos.get(position).getProduct_id());

                PRODUCT_ID = productDtos.get(position).getProduct_id();
                PRODUCT_NAME = productDtos.get(position).getProduct_name();


//                if (CHANNEL_ID.equalsIgnoreCase("8")){
//                    PRODUCT_VALUE = Double.parseDouble(productDtos.get(position).getProduct_MRP_price());
//                    PRODUCT_SINGLE_DEPO_PRICE = Double.parseDouble(productDtos.get(position).getProduct_MRP_price());
//                }else {
                    PRODUCT_VALUE = Double.parseDouble(productDtos.get(position).getProduct_price());
                    PRODUCT_SINGLE_DEPO_PRICE = Double.parseDouble(productDtos.get(position).getProduct_price());
              //  }

//                PRODUCT_VALUE = Double.parseDouble(productDtos.get(position).getProduct_price());
//                PRODUCT_SINGLE_DEPO_PRICE= Double.parseDouble(productDtos.get(position).getProduct_price());


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

                   }
               }
            }
        });

        productDialog.show();
    }

    public void showAddProduct(String productName,String productid){
        rel_lay_addproduct.setVisibility(View.VISIBLE);
        txt_product_name.setText(productName);
        row_edt_order_qty.setText("");
        row_edt_value.setText("");
        getStockBooking(productid,row_edt_order_qty1,row_edt_value1);
    }
    public void getStockBooking(String productid,final TextView stock, final TextView book){


        final ProgressDialog pd = new ProgressDialog(context);

        vm.sendRequestToServer(context, getResources().getString(R.string.base_url)+"api/fo/show-stock-me", jrf.getStock(SharePreference.getUserPointId(context),productid), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);


                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                            stock.setText("Stock Qty :" + jsonObject.getString("stockQty"));
                            book.setText("Booked Qty :" + jsonObject.getString("demandQty"));

                    }else {
                            Toast.makeText(context, "স্টক এবং বুকিং তথ্য পাওয়া যায়নি.", Toast.LENGTH_SHORT).show();

                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void confirmOrder(String finalObject){


        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer(context, getResources().getString(R.string.base_url)+"api/apps/wastage-submit",finalObject, new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    final JSONObject jsonObject = new JSONObject(result);

                 //   Log.e("confirmResponse>1>",jsonObject+"");

                    runOnUiThread(new Runnable() {
                        public void run() {
                            try{
                                if (jsonObject.getString("status").equalsIgnoreCase("0")){

                                    orderDtos.clear();
                                    orderRecyclerAdapter.notifyDataSetChanged();
                                    Toast.makeText(context, "Wastage saved successfully.", Toast.LENGTH_SHORT).show();

                                    databaseHelper.insertWastageStatus(databaseHelper,SharePreference.getUserId(context),getIntent().getStringExtra("retailerId"),
                                            SharePreference.getUserId(context),"Collected",formattedDate);

                                    finish();

                                }else {
                                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
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

    public void getReturnProduct(String productId){

        PRODUCT_ID_RETURN = productId;
        isReturn="1";
        showCategoryListDialog();

    }
    public void deleteAProduct(final int position){


        deleteCalculation=0;
        deleteCalculation1=0;
        deleteCalculation =Float.parseFloat( txt_total_amount.getText().toString())- Float.parseFloat(orderDtos.get(position).getProduct_price());
        deleteCalculation1 =Float.parseFloat( txt_total_amount_return.getText().toString())- Float.parseFloat(orderDtos.get(position).getProduct_price_return());
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
                        for (int i=0;i<orderDtos.size();i++){
                            if (orderDtos.get(i).getProduct_id().equalsIgnoreCase(orderDtos.get(position).getProduct_id())){
                                orderDtos.remove(i);
                            }
                        }

                        txt_total_amount.setText(String.valueOf(deleteCalculation));
                        txt_total_amount_return.setText(String.valueOf(deleteCalculation1));

                        deleteCalculation=0;
                        deleteCalculation1=0;

                        orderRecyclerAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).show();

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
//                                    routeObject.getString("global_company_id"),
//                                    routeObject.getString("status"),
//                                    routeObject.getString("unit"),
                                        categoryDTO.setAvg_price(routeObject.getString("avg_price"));
//                                    routeObject.getString("factor"),
//                                    routeObject.getString("user"),
//                                    routeObject.getString("date_time"),
//                                    routeObject.getString("order_by"),
//                                    routeObject.getString("company_id"),
//                                    routeObject.getString("plant_code"),
//                                    routeObject.getString("vat_percent"),
//                                    routeObject.getString("order_by_la"),
//                                    routeObject.getString("top_group"),
//                                    routeObject.getString("top_name"),
//                                    routeObject.getString("cat_id"),
//                                    routeObject.getString("offer_group"),
//                                    routeObject.getString("LAF"),
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



