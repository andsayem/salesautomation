package com.ssgbd.salesautomation.visit;

import static android.view.View.GONE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.volley.misc.Utils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.LoginActivity;
import com.ssgbd.salesautomation.OfferImageShowActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.OrderRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryCalculationRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductListRecyclerAdapter;
import com.ssgbd.salesautomation.bucket.BucketAmountWeb;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.dtos.OrderDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.gps.GPSTracker;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener{

    VolleyMethods vm = new VolleyMethods();
    private Context context;
    private Toolbar mToolbar;
    TextView txt_toolbar,txt_retailer_name,txt_category_list;
    JSONObject finalobject;
    private Dialog wdialog,productDialog;
    TextView txt_route_list,txt_route_name;
    DatabaseHelper databaseHelper;
    ProductCategoryRecyclerAdapter routeRecyclerAdapter;
    ProductCategoryCalculationRecyclerAdapter productCategoryCalculationRecyclerAdapter;
    ProductListRecyclerAdapter productListRecyclerAdapter;
    RecyclerView product_list_recyclerView;

    String CHANNEL_ID="null";

   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<CategoryDTO> categoryDTOSREserve = new ArrayList<>();
    ArrayList<ProductDTO> productDtos = new ArrayList<>();
    ArrayList<OrderDTO> orderDtos = new ArrayList<>();
    ArrayList<OrderDTO> categoryTotalDTOS = new ArrayList<>();


    public RelativeLayout rel_lay_addproduct;
    public TextView txt_product_name,txt_stock_qty,txt_book_qty;

    public Button btn_save_order;
    private LinearLayout linlay_category_portion;
    Button btn_category_total;    // select product
    String PRODUCT_CATEGORY_ID="";
    String PRODUCT_CATEGORY_NAME="";
    String PRODUCT_ID="";
    String PRODUCT_NAME="";
    String PRODUCT_SHORT_NAME="";
    double PRODUCT_VALUE=0;
    double PRODUCT_QTY= 0;
    double PRODUCT_SINGLE_DEPO_PRICE = 0;
    String OFFER_TYPE_ID= "";
    String WESTAGE_QTY="";
    String isSaved="0";
    EditText row_edt_order_qty,row_edt_value,row_edt_wastage;

    String orderStartTime="";
    // final order
    RecyclerView order_list_recyclerView;
    EditText edt_search_order_product;
    OrderRecyclerAdapter orderRecyclerAdapter;

    //bucket
    TextView txt_bucket_amount,txt_bucket_amounttitle;
    LinearLayout linlay_buket_amount;
    float ff=0;
    float catTOT=0;
    float  deleteCalculation=0;

    String formattedDate="";
    String smsFormate="";

    ArrayList<DBOrderDTO> orderListDTOS = new ArrayList<>();

    GPSTracker gps;
    String LAT="";
    String LON="";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
     public RequestQueue queue;
    RadioGroup radio_group_cable;
    RadioButton radio_cash,radio_percent;

    String cableOfferValue="1";
    JsonRequestFormate jrf = new JsonRequestFormate();

    ImageView smart_image;

    MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar seekBar;
    TextView seekbarhint;
    boolean wasPlaying = false;
    FloatingActionButton btn_floating;

    private Handler handler = new Handler();
  //  private MediaPlayer mediaPlayer;
  //  private boolean wasPlaying = false;
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_screen);
        context = this;

        loadToolBar();
        initUi();
        queue = Volley.newRequestQueue(this);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");// hh:mm:ss
        formattedDate = df.format(c);

      //  forceLogout();

        syncCategory("");
        syncProduct("");
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar_order);
        txt_toolbar = (TextView) mToolbar.findViewById(R.id.txt_toolbar);
        txt_toolbar.setText("Order");

        linlay_buket_amount = (LinearLayout) mToolbar.findViewById(R.id.linlay_buket_amount);
        linlay_buket_amount.setOnClickListener(this);
      //linlay_buket_amount.setClickable(false);
        txt_bucket_amount = (TextView) mToolbar.findViewById(R.id.txt_bucket_amount);
        txt_bucket_amount.setVisibility(View.VISIBLE);
        txt_bucket_amounttitle = (TextView) mToolbar.findViewById(R.id.txt_bucket_amounttitle);
        txt_bucket_amounttitle.setVisibility(View.VISIBLE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
    }
    private void initUi() {



        smart_image = (ImageView) findViewById(R.id.smart_image);
        smart_image.setOnClickListener(this);
        // Load image with Picasso
        Picasso.with(context)
                .load(getIntent().getStringExtra("imagelink"))
                .placeholder(R.mipmap.list_placeholder).into(smart_image);

        radio_group_cable = (RadioGroup) findViewById(R.id.radio_group_cable);
        radio_cash = (RadioButton) findViewById(R.id.radio_cash);
        radio_percent = (RadioButton) findViewById(R.id.radio_percent);

        // hide for option not need ---- as per policy only cash option needed
//         if (SharePreference.getUserBusinessType(context).equalsIgnoreCase("8")){
//             radio_group_cable.setVisibility(View.VISIBLE);
//         }

        radio_group_cable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_cash:
                        // do operations specific to this selection
                         cableOfferValue="1";
                       // Toast.makeText(context, cableOfferValue, Toast.LENGTH_SHORT).show();
                        break;

                        case R.id.radio_percent:
                        // do operations specific to this selection
                        cableOfferValue="1";
                      //  Toast.makeText(context, cableOfferValue, Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        });

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

      String orderData =  databaseHelper.getOrderByRetailerId(databaseHelper,getIntent().getStringExtra("retailerId"),formattedDate);

      //Log.e("today>>>",formattedDate+"<<>>"+orderData+"<<");

                try {
                    JSONObject jsonObject = new JSONObject(orderData);

                    JSONArray jsonArray = jsonObject.getJSONArray("order");

                    for (int e=0;e<jsonArray.length();e++){
                        JSONObject orderObject = jsonArray.getJSONObject(e);

                        OrderDTO orderDTO = new OrderDTO();
                        orderDTO.setProduct_category_id(orderObject.getString("category_id"));
                        orderDTO.setProduct_category_name(orderObject.getString("category_name"));
                        orderDTO.setProduct_id(orderObject.getString("product_id"));
                        orderDTO.setProduct_name(orderObject.getString("product_name"));
                      //orderDTO.setProduct_short_code(orderObject.getString("product_short_code"));
                        orderDTO.setProduct_qty(orderObject.getString("product_qty"));
                        orderDTO.setProduct_depo_price(orderObject.getString("unit_price"));
                        orderDTO.setProduct_price(orderObject.getString("price"));
//                      orderDTO.setWastage_qty(orderObject.getString("wastage_qty"));
//                      orderDTO.setProduct_wastage_price(orderObject.getString("wastage_value"));
                        orderDTO.setOffer_type(orderObject.getString("offer_type"));

                        orderDtos.add(orderDTO);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                  orderRecyclerAdapter.notifyDataSetChanged();

                    ff = 0;
                    for (int i = 0; i < orderDtos.size(); i++) {

                        ff += Float.parseFloat(orderDtos.get(i).getProduct_price());
                        txt_bucket_amount.setText(String.valueOf(ff));
                    }

        btn_category_total = (Button) findViewById(R.id.btn_category_total);
        btn_category_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryTotalDTOS.clear();
                showCategoryCalculation();


            }
        });
//        syncCategory("");

        btn_floating = findViewById(R.id.btn_floating);

        btn_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSong();
            }
        });
        seekBar = findViewById(R.id.seekbar);
        seekbarhint = findViewById(R.id.seekbarhint);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekbarhint.setVisibility(View.VISIBLE);
                stopSeekBarUpdate();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekbarhint.setText(formatTime(progress));
                    seekbarhint.setX(getSeekBarThumbPos(seekBar));
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                    startSeekBarUpdate();
                }
                seekbarhint.setVisibility(View.INVISIBLE);
            }
        });

        if (getIntent().getStringExtra("from").equalsIgnoreCase("smartorder")){
            seekBar.setVisibility(View.VISIBLE);
            btn_floating.setVisibility(View.VISIBLE);
            seekbarhint.setVisibility(View.VISIBLE);
            smart_image.setVisibility(View.VISIBLE);
        }
    }

    public void playSong() {
//        try {
//            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                clearMediaPlayer();
//                seekBar.setProgress(0);
//                wasPlaying = true;
//                btn_floating.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_media_play));
//                return;
//            }
//
//            if (!wasPlaying) {
//                if (mediaPlayer == null) {
//                    mediaPlayer = new MediaPlayer();
//                }
//
//                btn_floating.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_media_pause));
//
//                String audioUrl = getIntent().getStringExtra("voice");
//                mediaPlayer.setDataSource(audioUrl);
//
//                mediaPlayer.setOnPreparedListener(mp -> {
//                    seekBar.setMax(mediaPlayer.getDuration());
//                    mediaPlayer.start();
//                    startSeekBarUpdate();
//                });
//
//                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
//                    Log.e("MediaPlayer", "Error occurred: " + what);
//                    return false;
//                });
//
//                mediaPlayer.prepareAsync();
//            }
//            wasPlaying = false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Error playing audio", Toast.LENGTH_SHORT).show();
//        }

        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                // Pause the player if it's playing
                mediaPlayer.pause();
                seekBar.setProgress(mediaPlayer.getCurrentPosition()); // Update seekbar to current position
                btn_floating.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_media_play));
                wasPlaying = true; // Remember we were playing
                return;
            }

            // If not currently playing (either fresh start or paused state)
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            } else if (wasPlaying) {
                // Resume playback if we were previously playing
                mediaPlayer.start();
                btn_floating.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_media_pause));
                wasPlaying = false;
                return;
            }

            // New playback initialization
            btn_floating.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_media_pause));

            String audioUrl = getIntent().getStringExtra("voice");
            mediaPlayer.setDataSource(audioUrl);

            mediaPlayer.setOnPreparedListener(mp -> {
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                startSeekBarUpdate();
                wasPlaying = false; // Reset wasPlaying when new playback starts
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e("MediaPlayer", "Error occurred: " + what);
                btn_floating.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_media_play));
                wasPlaying = false;
                return false;
            });

            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error playing audio", Toast.LENGTH_SHORT).show();
            wasPlaying = false;
            btn_floating.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_media_play));
        }
    }

    public void run() {

        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();


        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            seekBar.setProgress(currentPosition);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }

    private void clearMediaPlayer() {
        if (mediaPlayer != null) {
            stopSeekBarUpdate();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void startSeekBarUpdate() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);

                    // Update every second
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(updateSeekBar);
    }

    private void stopSeekBarUpdate() {
        handler.removeCallbacks(updateSeekBar);
    }
    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private float getSeekBarThumbPos(SeekBar seekBar) {
        float val = (float) (1.0 * seekBar.getProgress() / seekBar.getMax());
        float width = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
        float padding = seekBar.getPaddingLeft();
        float pos = padding + val * width;
        return pos - (seekbarhint.getWidth() / 2);
    }





    private void orderListShow() {

        edt_search_order_product = (EditText) findViewById(R.id.edt_search_order_product);
        order_list_recyclerView = (RecyclerView) findViewById(R.id.order_list_recyclerView);

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        order_list_recyclerView.setLayoutManager(linearLayoutManager);
        orderRecyclerAdapter = new OrderRecyclerAdapter(orderDtos,context);
        order_list_recyclerView.setAdapter(orderRecyclerAdapter);

        order_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                deleteCalculation=0;
               deleteCalculation =Float.parseFloat( txt_bucket_amount.getText().toString())- Float.parseFloat(orderDtos.get(position).getProduct_price());
                new AlertDialog.Builder(context)
                        .setTitle("এলার্ট")
                        .setIcon(R.mipmap.ssg_logo)
                        .setMessage(getString(R.string.delete_alert)).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i=0;i<orderDtos.size();i++){
                                    if (orderDtos.get(i).getProduct_id().equalsIgnoreCase(orderDtos.get(position).getProduct_id())){
                                        orderDtos.remove(i);
                                    }
                                }

                                txt_bucket_amount.setText(String.valueOf(deleteCalculation));

                                deleteCalculation=0;

                                orderRecyclerAdapter.notifyDataSetChanged();
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

            case R.id.smart_image:
                Intent intent = new Intent(context, OfferImageShowActivity.class);
                intent.putExtra("imagelink",getIntent().getStringExtra("imagelink"));

                context.startActivity(intent);

                break;

                case R.id.txt_category_list:
                showCategoryListDialog();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                orderStartTime = sdf.format(new Date());

                break;

                case R.id.linlay_buket_amount:

                    if (isSaved.equalsIgnoreCase("0")){
                        Toast.makeText(context, getString(R.string.order_save_alert_), Toast.LENGTH_SHORT).show();

                    }else {

                        if (!isInternetAvailable(context)) {
                            internetAlert(context);

                        }else{
                          //  Log.e("<'>",finalobject+"<>");
                            confirmOrder();
                        }
                    }
                  //  Log.e("finalobject",finalobject+"");

                break;

                case R.id.btn_save_order:

//                    if (SharePreference.getUserBusinessType(context).equalsIgnoreCase("8"));
//                    if (cableOfferValue.equalsIgnoreCase("") && SharePreference.getUserBusinessType(context).equalsIgnoreCase("8")){
//                        Toast.makeText(context, "ক্যাশ অথবা ক্রেডিট যে কোনো একটি সিলেক্ট করুন। ", Toast.LENGTH_SHORT).show();
//                        return;
//                    }

                    //Initialize Google Play Services
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                  //Location Permission already granted
//                buildGoogleApiClient();
//                mGoogleMap.setMyLocationEnabled(true);

                   //         Log.e("permission 111","<<<>>>>"+"00000");


                            getLatLon();
                    if (orderDtos.size()==0){

                        Toast.makeText(context, getString(R.string.product_choicealert), Toast.LENGTH_SHORT).show();

                    }else{

                        AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
                        alertBulder.setIcon(context.getResources().getDrawable(R.drawable.search_hover)).setTitle(getString(R.string.order_save_alert));

                        alertBulder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                               // linlay_buket_amount.setClickable(true);
                                isSaved = "1";
                                linlay_category_portion.setVisibility(GONE);

                                JSONObject jsonObject;
                                JSONArray jsonArray = new JSONArray();
                                JSONArray exclusiveOfferArray = new JSONArray();
                                try {
                                    JSONObject exclusive = new JSONObject();
                                    exclusive.put("category_id", "");
                                    exclusive.put("product_id", "");
                                    exclusive.put("slab", "");
                                    exclusive.put("qty", "");
                                    exclusiveOfferArray.put(exclusive);
                                }catch (JSONException je){
                                }

                                JSONArray bundleOfferArray = new JSONArray();
                                try {
                                    JSONObject bundle = new JSONObject();
                                    bundle.put("category_id", "");
                                    bundle.put("product_id", "");
                                    bundle.put("slab", "");
                                    bundle.put("qty", "");
                                    bundleOfferArray.put(bundle);
                                }catch (JSONException je){

                                }
                                JSONArray regulerOfferArray = new JSONArray();
                                try {
                                    JSONObject reguler = new JSONObject();
                                    reguler.put("category_id", "");
                                    reguler.put("product_id", "");
                                    reguler.put("slab", "");
                                    reguler.put("qty", "");
                                    regulerOfferArray.put(reguler);
                               }catch (JSONException je){

                                }
                                for (int i=0;i<orderDtos.size();i++){
                                    jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("category_id",orderDtos.get(i).getProduct_category_id());
                                        jsonObject.put("category_name",orderDtos.get(i).getProduct_category_name());
                                        jsonObject.put("product_id",orderDtos.get(i).getProduct_id());
                                        jsonObject.put("product_name",orderDtos.get(i).getProduct_name());
                                        jsonObject.put("product_short_code",orderDtos.get(i).getProduct_short_code());
                                        jsonObject.put("product_qty",orderDtos.get(i).getProduct_qty());
                                        jsonObject.put("wastage_qty",orderDtos.get(i).getWastage_qty());
                                        jsonObject.put("price",orderDtos.get(i).getProduct_price());
                                        jsonObject.put("unit_price",orderDtos.get(i).getProduct_depo_price());
                                        jsonObject.put("wastage_value",orderDtos.get(i).getProduct_wastage_price());

                                        jsonObject.put("status","");
                                        jsonObject.put("offer_type",orderDtos.get(i).getOffer_type());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    jsonArray.put(jsonObject);
                                }

                                 finalobject = new JSONObject();
                                try {
                                    JSONObject discountObject = new JSONObject();
                                    discountObject.put("discount_amount","");
                                    discountObject.put("discount_rate","");

                                    //user object
                                    JSONObject userObject = new JSONObject();
                                    userObject.put("invoice_id","");
                                    userObject.put("distributor_id",SharePreference.getDistributorID(context));//SharePreferenceBTP.getDistributorID(context)
                                    userObject.put("route_id",Utility.ROUTE_ID);
                                    userObject.put("retailer_id",getIntent().getStringExtra("retailerId"));
                                    userObject.put("point_id",getIntent().getStringExtra("poient_id"));
                                    userObject.put("user_id",SharePreference.getUserId(context));
                                    userObject.put("login_user_name",SharePreference.getUserLoginId(context));
                                    userObject.put("login_user_password",SharePreference.getUserLoginPassword(context));
                                    userObject.put("lat",LAT);
                                    userObject.put("long",LON);
                                    userObject.put("start_time",orderStartTime);
                                    userObject.put("global_company_id","1");
                                    userObject.put("is_cash",cableOfferValue);
                                    userObject.put("smart_order_id", getIntent().getStringExtra("smartOrderId"));
                               //  Log.e("<<>>",userObject+"");
                                    //adding all object to finalObject
                                    finalobject.put("userinfo", userObject);
                                    finalobject.put("discount", discountObject);
                                    finalobject.put("order", jsonArray);
                                    finalobject.put("reguler_offer", regulerOfferArray);
                                    finalobject.put("exclusive_offer", exclusiveOfferArray);
                                    finalobject.put("bundle_offer", bundleOfferArray);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                // check if order exist
                                String orderData =  databaseHelper.getOrderByRetailerId(databaseHelper,getIntent().getStringExtra("retailerId"),formattedDate);


                                if (orderData.equalsIgnoreCase("")){
                                    databaseHelper.insertOrder(databaseHelper,Utility.ROUTE_ID,Utility.ROUTE_NAME,getIntent().getStringExtra("retailerId"),
                                            getIntent().getStringExtra("retailerName"), getIntent().getStringExtra("poient_id"),finalobject.toString(),
                                            formattedDate,SharePreference.getUserId(context) );
                                }else {

                                    databaseHelper.updateOrder(databaseHelper,getIntent().getStringExtra("retailerId"),finalobject.toString());
                                }

                                smsFormate ="Retail :" + getIntent().getStringExtra("retailerName")+" \n"  ;

                                for (int i=0;i<orderDtos.size();i++){

            smsFormate+=orderDtos.get(i).getProduct_short_code()+ ", Q"+"-"+orderDtos.get(i).getProduct_qty()+", A"+"-"+orderDtos.get(i).getProduct_price()+"; \n";
          //  smsFormate+= "-"+orderDtos.get(i).getProduct_name()+", Q"+"-"+orderDtos.get(i).getProduct_qty()+", A"+"-"+orderDtos.get(i).getProduct_price()+" \n";

                                }

                                smsFormate+="Total Amt :"+txt_bucket_amount.getText();

                          //      Log.e("m>>>>",smsFormate+"<-->");


//                                sendSMS(smsFormate,"8801313799779");
//                                sendSMS(smsFormate,"8801829282161");


                                orderDtos.clear();
                                orderRecyclerAdapter.notifyDataSetChanged();
                                Toast.makeText(context, "অর্ডার আপনার মোবাইলে সফলভাবে সেভ করা হয়েছে.", Toast.LENGTH_SHORT).show();

                                databaseHelper.insertOrderStatus(databaseHelper,SharePreference.getUserId(context),
                                        getIntent().getStringExtra("retailerId"),formattedDate,"Ordered",Utility.ROUTE_ID ,"yes","",Utility.V_RETAILER_NAME);


                            }
                        });

                        alertBulder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alertBulder.show();
                    }


                        } else {
                            //Request Location Permission
                            checkLocationPermission();
                       //     Log.e("permission 2222","<<<>>>>"+"111111");
                        }

                    }else {
                        getLatLon();
                        if (orderDtos.size()==0){

                            Toast.makeText(context, "অনুগ্রহ করে প্রথমে প্রোডাক্ট  সিলেক্ট করুন।", Toast.LENGTH_SHORT).show();

                        }else{

                            AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
                            alertBulder.setIcon(context.getResources().getDrawable(R.drawable.search_hover)).setTitle("Do you  want to save this Order?");

                            alertBulder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // linlay_buket_amount.setClickable(true);
                                    isSaved = "1";
                                    linlay_category_portion.setVisibility(GONE);

                                    JSONObject jsonObject;
                                    JSONArray jsonArray = new JSONArray();
                                    JSONArray exclusiveOfferArray = new JSONArray();
                                    try {
                                        JSONObject exclusive = new JSONObject();
                                        exclusive.put("category_id", "");
                                        exclusive.put("product_id", "");
                                        exclusive.put("slab", "");
                                        exclusive.put("qty", "");
                                        exclusiveOfferArray.put(exclusive);
                                    }catch (JSONException je){
                                    }

                                    JSONArray bundleOfferArray = new JSONArray();
                                    try {
                                        JSONObject bundle = new JSONObject();
                                        bundle.put("category_id", "");
                                        bundle.put("product_id", "");
                                        bundle.put("slab", "");
                                        bundle.put("qty", "");
                                        bundleOfferArray.put(bundle);
                                    }catch (JSONException je){

                                    }
                                    JSONArray regulerOfferArray = new JSONArray();
                                    try {
                                        JSONObject reguler = new JSONObject();
                                        reguler.put("category_id", "");
                                        reguler.put("product_id", "");
                                        reguler.put("slab", "");
                                        reguler.put("qty", "");
                                        regulerOfferArray.put(reguler);
                                    }catch (JSONException je){

                                    }
                                    for (int i=0;i<orderDtos.size();i++){
                                        jsonObject = new JSONObject();
                                        try {
                                            jsonObject.put("category_id",orderDtos.get(i).getProduct_category_id());
                                            jsonObject.put("category_name",orderDtos.get(i).getProduct_category_name());
                                            jsonObject.put("product_id",orderDtos.get(i).getProduct_id());
                                            jsonObject.put("product_name",orderDtos.get(i).getProduct_name());
                                            jsonObject.put("product_short_code",orderDtos.get(i).getProduct_short_code());
                                            jsonObject.put("product_qty",orderDtos.get(i).getProduct_qty());
                                            jsonObject.put("wastage_qty",orderDtos.get(i).getWastage_qty());
                                            jsonObject.put("price",orderDtos.get(i).getProduct_price());
                                            jsonObject.put("unit_price",orderDtos.get(i).getProduct_depo_price());
                                            jsonObject.put("wastage_value",orderDtos.get(i).getProduct_wastage_price());

                                            jsonObject.put("status","");
                                            jsonObject.put("offer_type",orderDtos.get(i).getOffer_type());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        jsonArray.put(jsonObject);
                                    }

                                    finalobject = new JSONObject();
                                    try {
                                        JSONObject discountObject = new JSONObject();
                                        discountObject.put("discount_amount","");
                                        discountObject.put("discount_rate","");

                                        //user object
                                        JSONObject userObject = new JSONObject();
                                        userObject.put("invoice_id","");
                                        userObject.put("distributor_id",SharePreference.getDistributorID(context));//SharePreferenceBTP.getDistributorID(context)
                                        userObject.put("route_id",Utility.ROUTE_ID);
                                        userObject.put("retailer_id",getIntent().getStringExtra("retailerId"));
                                        userObject.put("point_id",getIntent().getStringExtra("poient_id"));
//                                        userObject.put("point_id","00000");
                                        userObject.put("user_id",SharePreference.getUserId(context));
                                        userObject.put("login_user_name",SharePreference.getUserLoginId(context));
                                        userObject.put("login_user_password",SharePreference.getUserLoginPassword(context));
                                        userObject.put("lat",LAT);
                                        userObject.put("long",LON);

                                        userObject.put("global_company_id","1");
                                        userObject.put("is_cash",cableOfferValue);
                                        //adding all object to finalObject
                                        finalobject.put("userinfo", userObject);
                                        finalobject.put("discount", discountObject);
                                        finalobject.put("order", jsonArray);
                                        finalobject.put("reguler_offer", regulerOfferArray);
                                        finalobject.put("exclusive_offer", exclusiveOfferArray);
                                        finalobject.put("bundle_offer", bundleOfferArray);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    //        Log.e(">>>>>>>",finalobject.toString()+"");

                                    // check if order exist
                                    String orderData =  databaseHelper.getOrderByRetailerId(databaseHelper,getIntent().getStringExtra("retailerId"),formattedDate);


                                    if (orderData.equalsIgnoreCase("")){
                                        databaseHelper.insertOrder(databaseHelper,Utility.ROUTE_ID,Utility.ROUTE_NAME,getIntent().getStringExtra("retailerId"),
                                                getIntent().getStringExtra("retailerName"), getIntent().getStringExtra("poient_id"),finalobject.toString(),
                                                formattedDate,SharePreference.getUserId(context) );
                                    }else {

                                        databaseHelper.updateOrder(databaseHelper,getIntent().getStringExtra("retailerId"),finalobject.toString());
                                    }


                                    smsFormate ="Retail :" + getIntent().getStringExtra("retailerName")+" \n"  ;

                                    for (int i=0;i<orderDtos.size();i++){

                                        smsFormate+=orderDtos.get(i).getProduct_short_code()+ ", Q"+"-"+orderDtos.get(i).getProduct_qty()+", A"+"-"+orderDtos.get(i).getProduct_price()+"; \n";
                                        //  smsFormate+= "-"+orderDtos.get(i).getProduct_name()+", Q"+"-"+orderDtos.get(i).getProduct_qty()+", A"+"-"+orderDtos.get(i).getProduct_price()+" \n";

                                    }

                                    smsFormate+="Total Amt :"+txt_bucket_amount.getText();

                                    //      Log.e("m>>>>",smsFormate+"<-->");


//                                sendSMS(smsFormate,"8801313799779");
//                                sendSMS(smsFormate,"8801829282161");


                                    orderDtos.clear();
                                    orderRecyclerAdapter.notifyDataSetChanged();
                                    Toast.makeText(context, "অর্ডার আপনার মোবাইলে সফলভাবে সেভ করা হয়েছে.", Toast.LENGTH_SHORT).show();

                                    databaseHelper.insertOrderStatus(databaseHelper,SharePreference.getUserId(context),
                                            getIntent().getStringExtra("retailerId"),formattedDate,"Ordered",Utility.ROUTE_ID ,"yes","",Utility.V_RETAILER_NAME);

                                  //  Log.e(">>>>55555>>>",finalobject.toString()+"");

                                }
                            });

                            alertBulder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            alertBulder.show();
                        }
                    }
                break;
        }
    }

    private void showCategoryCalculation() {
        productDtos.clear();
        wdialog =new Dialog(context);
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_category_calculation);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final TextView tttt;

        final RelativeLayout rlDialogCross;
        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_search);
        etSearch.setVisibility(GONE);
        route_list_recyclerView=(RecyclerView) wdialog.findViewById(R.id.route_list_recyclerView);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog);

        // calculation

        ArrayList category_ids = new ArrayList();
        ArrayList<ArrayList> category_list = new ArrayList();
        ArrayList<ArrayList> category_data = new ArrayList();
        ArrayList category_single_data = new ArrayList();
        ArrayList category_owm_data = new ArrayList();

        for (int t=0;t<orderDtos.size();t++){
            if(!category_ids.contains(orderDtos.get(t).getProduct_category_id())){
                category_owm_data = new ArrayList();
                category_owm_data.add(orderDtos.get(t).getProduct_category_id());
                category_owm_data.add(orderDtos.get(t).getProduct_category_name());
                category_list.add(category_owm_data);
                category_ids.add(orderDtos.get(t).getProduct_category_id());
            }
        }
        Double cat_counter = 0.0;
        int total = 0;
        for (int cat = 0; cat < category_ids.size(); cat++) {
            cat_counter=0.0;
            total=0;
            for (int t=0;t<orderDtos.size();t++) {
                if(category_ids.get(cat).equals(orderDtos.get(t).getProduct_category_id())){
                    cat_counter = cat_counter+ Double.valueOf(orderDtos.get(t).getProduct_price());
                    total = total+ Integer.valueOf(orderDtos.get(t).getProduct_qty());
                }
            }
            category_single_data = new ArrayList();
            category_single_data.add(category_list.get(cat).get(0));
            category_single_data.add(category_list.get(cat).get(1));
            category_single_data.add(String.valueOf(cat_counter));
            category_single_data.add(String.valueOf(total));
            category_data.add(category_single_data);

        }
//        Log.e("sixze>>", String.valueOf(category_data.size()));
        for (int t=0;t<category_data.size();t++) {
            // Log.e("valu"+String.valueOf(category_data.get(t).get(1)), String.valueOf(category_data.get(t).get(2))+"--"+String.valueOf(category_data.get(t).get(3)));

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setProduct_category_id(String.valueOf(category_data.get(t).get(0)));
            orderDTO.setProduct_category_name(String.valueOf(category_data.get(t).get(1)));
            orderDTO.setProduct_price(String.valueOf(category_data.get(t).get(2)));
            orderDTO.setProduct_qty(String.valueOf(category_data.get(t).get(3)));
            categoryTotalDTOS.add(orderDTO);

        }

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
        productCategoryCalculationRecyclerAdapter = new ProductCategoryCalculationRecyclerAdapter( categoryTotalDTOS,context);
        route_list_recyclerView.setAdapter(productCategoryCalculationRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


//                wdialog.dismiss();

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
     // categoryDTOS.clear();

      //categoryDTOS = databaseHelper.getAllCategoryProductActiveOnly(databaseHelper,SharePreference.getUserBusinessType(context));

        final RelativeLayout rlDialogCross;
        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_search);
        route_list_recyclerView=(RecyclerView) wdialog.findViewById(R.id.route_list_recyclerView);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog);

        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeRecyclerAdapter.notifyDataSetChanged();
                wdialog.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeRecyclerAdapter.notifyDataSetChanged();

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

                productDtos = databaseHelper.getProductList(databaseHelper,categoryDTOS.get(position).getId());
               //  Log.e("<<>>",categoryDTOS.get(position).getId()+"--"+"");

                txt_category_list.setText(categoryDTOS.get(position).getName());
                PRODUCT_CATEGORY_ID = categoryDTOS.get(position).getId();
                PRODUCT_CATEGORY_NAME = categoryDTOS.get(position).getName();
                OFFER_TYPE_ID = categoryDTOS.get(position).getOffer_type();

                CHANNEL_ID = categoryDTOS.get(position).getGid();

             // Log.e("cat>>>",PRODUCT_CATEGORY_ID+"--"+PRODUCT_CATEGORY_NAME+"");

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

        productDialog =new Dialog(context,R.style.full_screen_dialog);
        productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        productDialog.setContentView(R.layout.dialog_product_list);
        final Button btnDone,btn_add;
        final ImageView imbtnCross,btn_add_product_cross;
        final EditText etSearch;
        final CardView card_view;
        etSearch = (EditText)productDialog.findViewById(R.id.edt_txt_search);
        row_edt_wastage = (EditText) productDialog.findViewById(R.id.row_edt_wastage);
        row_edt_order_qty = (EditText) productDialog.findViewById(R.id.row_edt_order_qty);
        row_edt_value = (EditText) productDialog.findViewById(R.id.row_edt_value);
        card_view = (CardView) productDialog.findViewById(R.id.card_view);
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        RadioGroup rg = (RadioGroup) productDialog.findViewById(R.id.radio_group);
        RadioButton radio_combined = productDialog.findViewById(R.id.radio_combined);
        RadioButton radio_separate = productDialog.findViewById(R.id.radio_separate);

        if (OFFER_TYPE_ID.equalsIgnoreCase("2")){
            radio_separate.setVisibility(View.VISIBLE);
            radio_separate.setChecked(true);
            OFFER_TYPE_ID="1";
        }else if (OFFER_TYPE_ID.equalsIgnoreCase("1")){
            radio_combined.setVisibility(View.VISIBLE);
            OFFER_TYPE_ID="2";
        }else if (OFFER_TYPE_ID.equalsIgnoreCase("3")){
            OFFER_TYPE_ID="2";
            radio_combined.setVisibility(View.VISIBLE);
            radio_separate.setVisibility(View.VISIBLE);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_combined:
                        // do operations specific to this selection
                        OFFER_TYPE_ID = "2";
                        for (int t=0;t<orderDtos.size();t++){
                            if (orderDtos.get(t).getProduct_category_id().equalsIgnoreCase(PRODUCT_CATEGORY_ID)){
                                orderDtos.get(t).setOffer_type(OFFER_TYPE_ID);
                            }
                        }
                        break;
                    case R.id.radio_separate:
                        // do operations specific to this selection

                        OFFER_TYPE_ID = "1";
                        for (int t=0;t<orderDtos.size();t++){
                            if (orderDtos.get(t).getProduct_category_id().equalsIgnoreCase(PRODUCT_CATEGORY_ID)){
                                orderDtos.get(t).setOffer_type(OFFER_TYPE_ID);
                            }
                        }
                        break;
                }
            }
        });


        btn_add = (Button) productDialog.findViewById(R.id.btn_add_product);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                OFFER_TYPE_ID="2";
//
//                  for (int t=0;t<orderDtos.size();t++){
//                    Log.e("1>>",orderDtos.get(t).getProduct_category_id()+"---"+PRODUCT_CATEGORY_ID+"");
//                    if (orderDtos.get(t).getProduct_category_id().equalsIgnoreCase(PRODUCT_CATEGORY_ID)){
//                        Log.e("if>",orderDtos.get(t).getProduct_category_id()+"");
//                        orderDtos.get(t).setOffer_type(OFFER_TYPE_ID);
//                    }
//                }
//                for (OrderDTO user : orderDtos) {
//                    Log.e("1>>",user.getProduct_category_id()+"---"+PRODUCT_CATEGORY_ID+"");
//                    if (user.getProduct_category_id().equalsIgnoreCase(PRODUCT_CATEGORY_ID)) {
//                        Log.e("if>",user.getProduct_category_id()+"");
//                        user.get(index).setUsername("newvalue");
//
//                        user.setOffer_type(OFFER_TYPE_ID);
//                    }
//                }

                for (int p=0;p<orderDtos.size();p++){
                    if (orderDtos.get(p).getProduct_id().equalsIgnoreCase(PRODUCT_ID)){
                        orderDtos.remove(p);
                    }
                }

                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setProduct_category_id(PRODUCT_CATEGORY_ID);
                orderDTO.setProduct_category_name(PRODUCT_CATEGORY_NAME);
                orderDTO.setProduct_id(PRODUCT_ID);
                orderDTO.setProduct_name(PRODUCT_NAME);
                orderDTO.setProduct_short_code(PRODUCT_SHORT_NAME);
                orderDTO.setProduct_depo_price(String.valueOf(PRODUCT_SINGLE_DEPO_PRICE));
                orderDTO.setOffer_type(OFFER_TYPE_ID);

                if (row_edt_wastage.getText().toString().equalsIgnoreCase("")){
                    row_edt_wastage.setText("0");
                }
                int e = Integer.parseInt(String.valueOf(row_edt_wastage.getText()));
                double dd = PRODUCT_SINGLE_DEPO_PRICE*e;
                orderDTO.setProduct_wastage_price(String.valueOf(dd));

                if (row_edt_order_qty.getText().toString().equalsIgnoreCase("")){
                    row_edt_order_qty.setText("0");
                    row_edt_value.setText("0");
                }
                orderDTO.setProduct_qty(row_edt_order_qty.getText().toString());
                orderDTO.setProduct_price(row_edt_value.getText().toString());
                orderDTO.setWastage_qty(row_edt_wastage.getText().toString());

                orderDtos.add(orderDTO);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

                rel_lay_addproduct.setVisibility(GONE);

                if (row_edt_order_qty.getText().length() == 0) {
                }else  {
                ff = 0;
                for (int i = 0; i < orderDtos.size(); i++) {
                    ff += Float.parseFloat(orderDtos.get(i).getProduct_price());
                    txt_bucket_amount.setText(String.valueOf(ff));
                }
                }

                for (int t=0;t<orderDtos.size();t++){
                    if (orderDtos.get(t).getProduct_category_id().equalsIgnoreCase(PRODUCT_CATEGORY_ID)){
                        orderDtos.get(t).setOffer_type(OFFER_TYPE_ID);
                    }
                }

                for (int t=0;t<orderDtos.size();t++){

                }

                orderRecyclerAdapter.notifyDataSetChanged();

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
        rel_lay_addproduct = (RelativeLayout) productDialog.findViewById(R.id.rel_lay_addproduct);
        txt_stock_qty = (TextView) productDialog.findViewById(R.id.txt_stock_qty);
        txt_book_qty = (TextView) productDialog.findViewById(R.id.txt_book_qty);

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
                rel_lay_addproduct.setVisibility(GONE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
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
                PRODUCT_SHORT_NAME = productDtos.get(position).getProduct_shortCode();
                // for cable channel
//                if (CHANNEL_ID.equalsIgnoreCase("8")){
//                    PRODUCT_VALUE = Double.parseDouble(productDtos.get(position).getProduct_MRP_price());
//                    PRODUCT_SINGLE_DEPO_PRICE = Double.parseDouble(productDtos.get(position).getProduct_MRP_price());
//                }else {
                    PRODUCT_VALUE = Double.parseDouble(productDtos.get(position).getProduct_price());
                    PRODUCT_SINGLE_DEPO_PRICE = Double.parseDouble(productDtos.get(position).getProduct_price());
//                }
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

        getStockBooking(txt_stock_qty,txt_book_qty,productid);
        rel_lay_addproduct.setVisibility(View.VISIBLE);
        txt_product_name.setText(productName);
        row_edt_order_qty.setText("");
        row_edt_value.setText("");
        row_edt_wastage.setText("");

    }

    public void getStockBooking(final TextView stock, final TextView book,String productid){


        final ProgressDialog pd = new ProgressDialog(context);

        vm.sendRequestToServer(context, getResources().getString(R.string.base_url)+"api/fo/show-stock-me", jrf.getStock(SharePreference.getUserPointId(context),productid), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                  //  Log.e("jsonObject>",jsonObject.toString()+"");

                    if (jsonObject.getString("status").equalsIgnoreCase("1")){

                        stock.setText("Stock Qty :"+jsonObject.getString("stockQty"));
                        book.setText("Booked Qty :"+jsonObject.getString("demandQty"));

                    }else {
                        Toast.makeText(context, "স্টক এবং বুকিং তথ্য পাওয়া যায়নি.", Toast.LENGTH_SHORT).show();

                    }
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.e("<<>>",resultCode+"<<>>");
//        if (requestCode == 1) {
//
//            Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
//
//        }
//    }

    public void confirmOrder(){

        linlay_buket_amount.setVisibility(GONE);
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage(getString(R.string.loading_text));
        pd.show();
        pd.setCancelable(false);
       // Log.e("send>>",finalobject.toString()+"");
        vm.sendRequestToServer(context, getResources().getString(R.string.base_url)+"api/apps/order_confirm_v2", finalobject.toString(), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                   // Log.e("send>>",jsonObject.toString()+"<<");

                    if (jsonObject.getString("status").equalsIgnoreCase("1")){

                        databaseHelper.deleteAOrder(databaseHelper,getIntent().getStringExtra("retailerId"));
                        Intent intent = new Intent(OrderActivity.this,BucketAmountWeb.class);
                        intent.putExtra("routeId",Utility.ROUTE_ID);
                        intent.putExtra("retailerId",getIntent().getStringExtra("retailerId"));
                        intent.putExtra("poient_id",getIntent().getStringExtra("poient_id"));
                        startActivity(intent);

                        finish();
                    }else {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        linlay_buket_amount.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }

        });
    }

    public void getLatLon() {

        gps = new GPSTracker(context);

// check if GPS enabled
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LAT = String.valueOf(gps.getLatitude());
            LON = String.valueOf(gps.getLongitude());

        //    Log.e(">>>llvisit>>",LAT+"------"+LON+"");

        }else{
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings
            new AlertDialog.Builder(context)
                    .setTitle("Prominent disclosure \n Background Location Permission Needed")
                    // .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setMessage(getString(R.string.location_text))
                    .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })

                    .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            gps.showSettingsAlert();
                        }
                    })
                    .create()
                    .show();

           // gps.showSettingsAlert();
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
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("ইন্টারনেট এলার্ট ").setMessage("আপনার ডিভাইস ইন্টারনেটের সাথে সংযুক্ত নয়। ইন্টারনেটে সংযোগ করুন এবং আবার চেষ্টা করুন।");
        alertBulder.setPositiveButton("ওয়াইফাই", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        alertBulder.setNeutralButton("মোবাইল ডাটা", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertBulder.show();
    }


    public  void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(context)
                        .setTitle("prominent disclosure \n Background Location Permission Needed")
                       // .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setMessage(getString(R.string.location_text))
                        .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })

                        .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions((Activity)context,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity)context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    public String textShortner(String shortTexts){

        String shortText=shortTexts;

        String[] myText = shortTexts.split(" ");
        for (int ii = 0; ii < myText.length; ii++) {
            String s = myText[ii];
            shortText+=(s.charAt(0));
            //shortText+=(s.charAt(1));
        }
        return shortText;
    }


    public void forceLogout(){

        RequestQueue queue;
        //
        String  url = getResources().getString(R.string.base_url)+"apps/api/force-logout?user_id="+SharePreference.getUserId(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        //  Log.e("version>>",response+"");

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equalsIgnoreCase("true")){

                                Utility.LatestRoute="";
                                Utility.ROUTE_ID = "";
                                Utility.ROUTE_NAME = "";
                                Utility.V_RETAILER_ID = "";
                                Utility.V_RETAILER_NAME = "";
                                SharePreference.setUserPointId(context,"");

                                SharePreference.setIsRetailerBaseSync(context,"no");
                                SharePreference.setIsCategoryBaseSync(context,"no");
                                SharePreference.setIsProductBaseSync(context,"no");

                                SharePreference.setIslogIn(context, "0");
                                Intent loginIntent = new Intent(context, LoginActivity.class);
                                startActivity(loginIntent);
                                finish();


                            }else {

                            }

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

    public void syncProduct(String date) {

        final ProgressDialog pd1 = new ProgressDialog(context);
        pd1.setMessage(getString(R.string.product_loding));
        pd1.setCancelable(false);
        pd1.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(context,
                getResources().getString(R.string.base_url) + "api/ma/sync-master-products",
                jp.jsonSYNC(SharePreference.getUserId(context),SharePreference.getUserBusinessType(context), SharePreference.getUserPointId(context),date), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {

                        if (result.equalsIgnoreCase("nettooslow")){
                            pd1.dismiss();
                        }

                        try {
                            JSONObject jsonObject1 = new JSONObject(result);

                            //  Log.e("product",jsonObject1+"");

                            if (jsonObject1.getString("status").equalsIgnoreCase("1")) {

                                try {
                                    JSONArray routeArray = jsonObject1.getJSONArray("product_list");
                                    for (int i = 0; i < routeArray.length(); i++) {
                                        JSONObject routeObject = routeArray.getJSONObject(i);

                                        databaseHelper.insertOrUpdateProduct(databaseHelper,

                                                routeObject.getString("id"),
                                                routeObject.getString("companyid"),
                                                routeObject.getString("category_id"),
                                                routeObject.getString("sub_cat_id"),
                                                routeObject.getString("sap_code"),
                                                routeObject.getString("product_map"),
                                                routeObject.getString("old_code"),
                                                routeObject.getString("sub_group"),
                                                routeObject.getString("name"),
                                                routeObject.getString("ims_name"),
                                                routeObject.getString("commission"),
                                                routeObject.getString("mrp"),
                                                routeObject.getString("depo"),
                                                routeObject.getString("distri"),
                                                routeObject.getString("realtimeprice"),
                                                routeObject.getString("unit"),
                                                routeObject.getString("dateandtime"),
                                                routeObject.getString("user"),
                                                routeObject.getString("factor"),
                                                routeObject.getString("order_by"),
                                                routeObject.getString("status"),
                                                routeObject.getString("stock_qty"),
                                                routeObject.getString("mkt_stock"),
                                                routeObject.getString("vat_percen"),
                                                routeObject.getString("order_status"),
                                                routeObject.getString("active_date"),
                                                routeObject.getString("product_msg"),
                                                routeObject.getString("vat_sap_code"),
                                                routeObject.getString("vat_user"),
                                                routeObject.getString("int_sap_code"),
                                                routeObject.getString("ims_stat"));

                                    }

                                } catch (JSONException je) {
                                    pd1.dismiss();
                                }
                                pd1.dismiss();

                            }else {
                                pd1.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }
}



