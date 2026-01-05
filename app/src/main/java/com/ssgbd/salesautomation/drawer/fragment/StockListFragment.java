package com.ssgbd.salesautomation.drawer.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.StockListAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.StockListDTO;
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
import java.util.ArrayList;

public class StockListFragment extends Fragment implements View.OnClickListener{

    View rootView;
    // product category list
    TextView txt_category_list;
    Dialog wdialog;
    ProductCategoryRecyclerAdapter productCategoryAdapter;
    ArrayList<CategoryDTO> categoryDTOSREserve = new ArrayList<>();

    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<StockListDTO> stockDTOS = new ArrayList<>();
    VolleyMethods vm = new VolleyMethods();
    // get db data
    DatabaseHelper databaseHelper;
   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";

    // product
    RecyclerView product_list_recyclerView;
    StockListAdapter stockListAdapter;
    ArrayList<ProductDTO> productDtos = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.stock_list_fragment, container, false);


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
        syncCategory("");
        return rootView;
    }


    private void showCategoryListDialog() {

//        categoryDTOS.clear();
//        for (CategoryDTO hm : categoryDTOSREserve) {
//            categoryDTOS.add(hm);
//        }
       // productDtos.clear();

        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
//        categoryDTOS.clear();
//        categoryDTOS = databaseHelper.getAllCategoryProductActiveOnly(databaseHelper,SharePreference.getUserBusinessType(getActivity()));

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
        //productDtos = databaseHelper.getProductList(databaseHelper,categoryDTOS.get(position).getId());
         getStockList(categoryDTOS.get(position).getId());

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
                productCategoryAdapter.filter(query);
            }
        });

        wdialog.show();
    }

    private void showProductListDialog() {

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        product_list_recyclerView.setLayoutManager(linearLayoutManager);
        stockListAdapter = new StockListAdapter( stockDTOS,getActivity());
        product_list_recyclerView.setAdapter(stockListAdapter);

        product_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }));

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

    public void getStockList(String catId){
        JsonRequestFormate jp = new JsonRequestFormate();
        VolleyMethods vm = new VolleyMethods();
//        Log.e("stock","result"+"statusArray");
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.product_loding));
        pd.setCancelable(false);
        pd.show();
        stockDTOS.clear();
        vm.sendRequestToServer2(getActivity(), getString(R.string.base_url)+"api/stock-list", jp.stockList(SharePreference.getUserId(getActivity()),catId,SharePreference.getUserPointId(getActivity())), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {

//                Log.e("stock",result+"statusArray");

                try {
                    JSONObject respjsonObj = new JSONObject(result);
                    if(respjsonObj.getString("status").equalsIgnoreCase("1")){
                        Toast.makeText(getActivity(), "No product found", Toast.LENGTH_SHORT).show();
                       pd.dismiss();

                    }else {
                        JSONArray statusArray = respjsonObj.getJSONArray("stockList");
                        //   Log.e("statusArray",statusArray+"statusArray");
                        for (int i = 0; i < statusArray.length(); i++) {
                            JSONObject routeObject = statusArray.getJSONObject(i);
                            StockListDTO stockListDTO = new StockListDTO();
                            stockListDTO.setProductName(routeObject.getString("product_name"));
                            stockListDTO.setStockQty(routeObject.getString("stock_qty"));
                            stockListDTO.setStockValue(routeObject.getString("stock_value"));

                            stockDTOS.add(stockListDTO);
                        }
                        pd.dismiss();
                    }
                } catch (JSONException je) {
                }
               // Log.e("size",stockDTOS.size()+"statusArray");
             //   showProductListDialog();
                stockListAdapter.notifyDataSetChanged();
            }

        });
    }

    public void syncCategory(String date) {
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),getResources().getString(R.string.base_url) + "api/ma/sync-master-categories",
                jp.jsonSYNC(SharePreference.getUserId(getActivity()),SharePreference.getUserBusinessType(getActivity()), SharePreference.getUserPointId(getActivity()),date), new VolleyCallBack() {
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
