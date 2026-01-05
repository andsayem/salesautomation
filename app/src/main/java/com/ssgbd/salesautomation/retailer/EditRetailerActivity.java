package com.ssgbd.salesautomation.retailer;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
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
import java.util.Locale;

public class EditRetailerActivity extends AppCompatActivity implements View.OnClickListener{

   Context context;
   Toolbar mToolbar;
   TextView txt_toolbar;
   Spinner spinnerOfferType;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
//  private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
   private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    DatabaseHelper databaseHelper;
    private Dialog wdialog;

    RouteRecyclerAdapter routeRecyclerAdapter;

    TextView txt_sapcode,txt_divisionname,txt_poientname,txt_territory_name,txt_route_list,txt_shopetype;
    EditText edt_txt_retailername,edt_txt_ownername,edt_txt_address,edt_txt_mobile,edt_txt_mobile2,
            edt_txt_email,edttext_dob,edt_txt_facebook,edt_txt_whatsapp;
    LinearLayout linlay_route,linlaydate;

    String statusId="";
    String routeId="";
    String shopType="";
    Button button_send_request;
    VolleyMethods vm = new VolleyMethods();

    JSONObject finalobject;
    JSONObject retailerobject;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_retailer_activity_screen);
        context = this;

        loadToolBar();
        initUi();
    }

    private void loadToolBar() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_toolbar = (TextView) mToolbar.findViewById(R.id.txt_toolbar);
        txt_toolbar.setText("Edit Retailer");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);

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

        CategoryDTO c103 = new CategoryDTO();
        c103.setId("009");
        c103.setName("Select Shop Type");
        categoryDTOS.add(c103);
        CategoryDTO c1 = new CategoryDTO();
        c1.setId("2");
        c1.setName(" Electric Shop");
        categoryDTOS.add(c1);
        CategoryDTO c11 = new CategoryDTO();
        c11.setId("1");
        c11.setName("DELAER");
        categoryDTOS.add(c11);
        CategoryDTO c12 = new CategoryDTO();
        c12.setId("3");
        c12.setName("Whole Seller");
        categoryDTOS.add(c12);
        CategoryDTO c13 = new CategoryDTO();
        c13.setId("4");
        c13.setName("Hardware-Shop");
        categoryDTOS.add(c13);
        CategoryDTO c14 = new CategoryDTO();
        c14.setId("5");
        c14.setName("Grocery/General Store");
        categoryDTOS.add(c14);
        CategoryDTO c15 = new CategoryDTO();
        c15.setId("6");
        c15.setName("Other");
        categoryDTOS.add(c15);
        CategoryDTO c = new CategoryDTO();
        c.setId("7");
        c.setName("END SHOP");
        categoryDTOS.add(c);

        spinnerOfferType = (Spinner) findViewById(R.id.spinnerOfferType);
        CustomArrayAdapter adapter = new CustomArrayAdapter(context,
        R.layout.customspinneritem, categoryDTOS);

        spinnerOfferType.setAdapter(adapter);

        spinnerOfferType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

              //  Log.e("<<>>",categoryDTOS.get(pos).getId()+"<<>>"+categoryDTOS.get(pos).getName()+"");
                if (categoryDTOS.get(pos).getId().equalsIgnoreCase("009")){

                }else {
                    shopType = categoryDTOS.get(pos).getId();
                    txt_shopetype.setText(categoryDTOS.get(pos).getName());
                }
             // Log.e(">>>shoptype><<>>",shopType+"");
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        edt_txt_retailername = findViewById(R.id.edt_txt_retailername);
        edt_txt_retailername.setText(getIntent().getStringExtra("retailerName"));

        edt_txt_ownername = findViewById(R.id.edt_txt_ownername);
        edt_txt_ownername.setText(getIntent().getStringExtra("woner"));

        edt_txt_address =  findViewById(R.id.edt_txt_address);
        edt_txt_address.setText(getIntent().getStringExtra("vAddress"));

        edt_txt_mobile =  findViewById(R.id.edt_txt_mobile);
        edt_txt_mobile.setText(getIntent().getStringExtra("phone"));

        edt_txt_mobile2 =  findViewById(R.id.edt_txt_mobile2);
       if (getIntent().getStringExtra("optional_mobile").equalsIgnoreCase("null")) {
           edt_txt_mobile2.setText("");
       }else {
           edt_txt_mobile2.setText(getIntent().getStringExtra("optional_mobile"));
       }

       // edt_txt_tntphone = (EditText) findViewById(R.id.edt_txt_tntphone);
        edt_txt_email = findViewById(R.id.edt_txt_email);
        edt_txt_email.setText(getIntent().getStringExtra("email"));
        edttext_dob =  findViewById(R.id.edttext_dob);
        edttext_dob.setText(getIntent().getStringExtra("dob"));

        txt_shopetype =  findViewById(R.id.txt_shopetype);

        shopType=getIntent().getStringExtra("shoptype");
       if (getIntent().getStringExtra("shoptype").equalsIgnoreCase("1")){
           txt_shopetype.setText("DELAER");
       }else if(getIntent().getStringExtra("shoptype").equalsIgnoreCase("2")){
           txt_shopetype.setText("Electric Shop");
       }else if(getIntent().getStringExtra("shoptype").equalsIgnoreCase("3")){
           txt_shopetype.setText("Whole Seller");
       }else if(getIntent().getStringExtra("shoptype").equalsIgnoreCase("4")){
           txt_shopetype.setText("Hardware-Shop");
       }else if(getIntent().getStringExtra("shoptype").equalsIgnoreCase("5")){
           txt_shopetype.setText("Grocery/General Store");
       }else if(getIntent().getStringExtra("shoptype").equalsIgnoreCase("6")){
           txt_shopetype.setText("Other");
       }else if(getIntent().getStringExtra("shoptype").equalsIgnoreCase("7")){
           txt_shopetype.setText("END SHOP");
       }


        edt_txt_facebook =  findViewById(R.id.edt_txt_facebook);
       edt_txt_facebook.setText(getIntent().getStringExtra("fb"));
        edt_txt_whatsapp =  findViewById(R.id.edt_txt_whatsapp);
        edt_txt_whatsapp.setText(getIntent().getStringExtra("whatsapp"));
        linlaydate = findViewById(R.id.linlaydate);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        linlaydate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txt_sapcode =  findViewById(R.id.txt_sapcode);
        txt_sapcode.setText(SharePreference.getSapCode(context));

        txt_divisionname =  findViewById(R.id.txt_divisionname);
        txt_divisionname.setText(SharePreference.getDivisionName(context));

        txt_route_list =  findViewById(R.id.txt_route_list);
        txt_route_list.setText(Utility.ROUTE_NAME);

        txt_poientname =  findViewById(R.id.txt_poientname);
        txt_poientname.setText(SharePreference.getUserPointName(context));

        txt_territory_name = findViewById(R.id.txt_territory_name);
        txt_territory_name.setText(SharePreference.getTerritoryName(context));

        linlay_route =  findViewById(R.id.linlay_route);
        linlay_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   showRouteListDialog();
            }
        });

        button_send_request =  findViewById(R.id.button_send_request);
        button_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_txt_retailername.getText().length()==0){
                    Toast.makeText(context, "Please Enter Retailer Name", Toast.LENGTH_SHORT).show();
                }
//                else if(edt_txt_ownername.getText().length()==0){
//                    Toast.makeText(context, "Please Enter Owner Name", Toast.LENGTH_SHORT).show();
//                }
                else if(edt_txt_address.getText().length()==0){
                    Toast.makeText(context, "Please Enter Shop Address", Toast.LENGTH_SHORT).show();

                }
//                else if(routeId.length()==0){
//                    Toast.makeText(context, "Please Select a Route", Toast.LENGTH_SHORT).show();
//                }
                else if(edt_txt_mobile.getText().length()==0){
                    Toast.makeText(context, "Please Enter a Aalid Phone Number", Toast.LENGTH_SHORT).show();

                }else {

                    try {
                        finalobject = new JSONObject();
                        retailerobject = new JSONObject();
                        retailerobject.put("retailer_id", getIntent().getStringExtra("retailerId"));
                        retailerobject.put("retailerName", edt_txt_retailername.getText().toString());
                        retailerobject.put("owner", edt_txt_ownername.getText().toString());
                        retailerobject.put("mobile",edt_txt_mobile.getText().toString());
                        retailerobject.put("optional_mobile",edt_txt_mobile2.getText().toString());
                        retailerobject.put("tnt", "");
                        retailerobject.put("email", edt_txt_email.getText().toString());
                        retailerobject.put("retailerAddress", edt_txt_address.getText().toString());
                        retailerobject.put("dob", edttext_dob.getText().toString());

                        retailerobject.put("fb", edt_txt_facebook.getText().toString());
                        retailerobject.put("whatsapp", edt_txt_whatsapp.getText().toString());
                        retailerobject.put("shop_type", shopType);
                        retailerobject.put("lat", "");
                        retailerobject.put("lon", "");
                        finalobject.put("retailer_info", retailerobject);

                     //   Log.e("<<>>", finalobject + "");

                        sendRequest();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    private void initUi() {

    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edttext_dob.setText(sdf.format(myCalendar.getTime()));
    }
    public class CustomArrayAdapter extends ArrayAdapter<String> {

        private final LayoutInflater mInflater;
        private final Context mContext;
        private final ArrayList<CategoryDTO> items;
        private final int mResource;

        public CustomArrayAdapter(@NonNull Context context, @LayoutRes int resource,
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

            TextView offTypeTv =  view.findViewById(R.id.offer_type_txt);

            CategoryDTO offerData = items.get(position);

            offTypeTv.setText(items.get(position).getName());

            return view;
        }
    }

    public void sendRequest(){

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer2(context, getResources().getString(R.string.base_url)+"api/apps/retailer-update", finalobject.toString(), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                pd.dismiss();
                try {
                    JSONObject jsonObject1 = new JSONObject(result);

                 //   Log.e("response>> inactiv>",jsonObject1.getString("message")+"");

                   // String s = jsonObject1.getString("message");

                    statusId="";

                    if (jsonObject1.getString("status").equalsIgnoreCase("1")){
                        Toast.makeText(context, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(context, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showRouteListDialog() {
        wdialog =new Dialog(context);
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;

        final RelativeLayout rlDialogCross;

        Utility.routeDTOS.clear();
        try {
            JSONObject respjsonObj = new JSONObject(SharePreference.getRouteData(context));
            JSONArray routeArray = respjsonObj.getJSONArray("route");
            for (int i = 0; i < routeArray.length(); i++) {
                JSONObject routeObject = routeArray.getJSONObject(i);
                RouteDTO routeDTO = new RouteDTO();
                routeDTO.setPoint_id(routeObject.getString("point_id"));
                routeDTO.setTerritory_id(routeObject.getString("territory_id"));
                routeDTO.setRname(routeObject.getString("rname"));
                routeDTO.setRoute_id(routeObject.getString("route_id"));
                Utility.routeDTOS.add(routeDTO);
            }
        }catch (JSONException je){
            Log.e("",je+"");
        }


        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_search);
        route_list_recyclerView=(RecyclerView) wdialog.findViewById(R.id.route_list_recyclerView);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog);

        wdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        routeRecyclerAdapter = new RouteRecyclerAdapter( Utility.routeDTOS,context);
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                txt_route_list.setText("Route Name-"+ Utility.routeDTOS.get(position).getRname());
                routeId = Utility.routeDTOS.get(position).getRoute_id();

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
                break;

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
}



