package com.ssgbd.salesautomation.drawer.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.DemandRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.FOFeedbackListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.FOFeedbackRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.FOLeaveListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryCalculationRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.ProductListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.DemandDTOs;
import com.ssgbd.salesautomation.dtos.FOLeaveListDTO;
import com.ssgbd.salesautomation.dtos.FeedbackDTO;
import com.ssgbd.salesautomation.dtos.FoFeedbackDTO;
import com.ssgbd.salesautomation.dtos.OrderDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.QDTO;
import com.ssgbd.salesautomation.dtos.QuestionDTO;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;


public class FoFeedbackFragment extends Fragment implements View.OnClickListener{

    View rootView;
    private Dialog wdialog,productDialog,wdialogDemand,wdialog_category;
    TextView txt_route_name,row_sl;
    FOFeedbackRecyclerAdapter feedback_list_adapter;
    RecyclerView feedback_list_recyclerView;
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<FeedbackDTO> feedbackDTOS = new ArrayList<>();
    ArrayList<QuestionDTO> qDTOS = new ArrayList<>();
    VolleyMethods vm = new VolleyMethods();
    TextView txt_fromdate,txt_todate,txt_search,txt_category_list,txt_product_list;
    Button btn_add_demand,btn_show_dimand;
    JSONObject  requestObject;
    Button btn_add;
    EditText edt_leave_reason,edt_remarks,edt_txt_demandqty;
    public RequestQueue queue;
    String PRESENT_DATE="";
    EditText edt_replacement_value,edt_demand_text,edt_replacement_feedback,edt_freepending_feedback,edt_competition_feedback,edt_complain_feedback;
    DatePickerDialog picker;
    String FROMDATE="",TODATE="";
    String fromDate="";
    long l;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<CategoryDTO> categoryDTOSREserve = new ArrayList<>();
    ArrayList<ProductDTO> productDtos = new ArrayList<>();
    ArrayList<DemandDTOs> demandDtos = new ArrayList<>();
    DemandRecyclerAdapter demandRecyclerAdapter;
    ProductCategoryRecyclerAdapter routeRecyclerAdapter;
    ProductListRecyclerAdapter productListRecyclerAdapter;
    DatabaseHelper databaseHelper;
    RecyclerView product_list_recyclerView;
    String PRODUCT_ID="",PRODUCT_NAME="";
    String CATEGORY_ID="",CATEGORY_NAME="";
    JSONObject feedJson;
    String today="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fo_feedback_fragment, container, false);

        row_sl = (TextView) rootView.findViewById(R.id.row_sl);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        final Calendar cldr=Calendar.getInstance();
        int day=cldr.get(Calendar.DAY_OF_MONTH);
        today = String.valueOf(day);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        PRESENT_DATE = df.format(c);
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

        feedback_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.feedback_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        feedback_list_recyclerView.setLayoutManager(linearLayoutManager1);
        feedback_list_adapter = new FOFeedbackRecyclerAdapter( feedbackDTOS,getActivity(), FoFeedbackFragment.this);
        feedback_list_recyclerView.setAdapter(feedback_list_adapter);

        feedback_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                demandDtos.clear();
                try {
                    JSONArray jsonArray = new JSONArray(feedbackDTOS.get(position).getDemandList());
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        DemandDTOs productDTO = new DemandDTOs();
                        productDTO.setCategoryName(jsonObject.getString("category_name"));
                        productDTO.setProductName(jsonObject.getString("category_name"));
                        productDTO.setProductQTY(jsonObject.getString("pd_qty"));
                        productDTO.setProductDemandText(jsonObject.getString("pd_text"));

                        demandDtos.add(productDTO);

                    }
                    demandListDialog();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }));


        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        edt_leave_reason = (EditText) rootView.findViewById(R.id.edt_leave_reason);
        initUi();
        syncCategory("");
        txt_fromdate = (TextView) rootView.findViewById(R.id.txt_fromdate);
        txt_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr=Calendar.getInstance();
                int day=cldr.get(Calendar.DAY_OF_MONTH);
                int month=cldr.get(Calendar.MONTH);
                int year=cldr.get(Calendar.YEAR);

                // date picker dialog
                picker=new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

//                              txt_month_year.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                fromDate = year + "-" + (monthOfYear + 1) + "-" +  dayOfMonth;
                                String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }
                                FROMDATE = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    FROMDATE = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }
                                String result="";
                                try {
                                    result = getActivity().getResources().getStringArray(R.array.month_names)[monthOfYear];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    result=Integer.toString(monthOfYear);
                                }
                                txt_fromdate.setText(dayOfMonth+" "+result+" "+year );
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        txt_todate = (TextView) rootView.findViewById(R.id.txt_todate);
        txt_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr=Calendar.getInstance();
                int day=cldr.get(Calendar.DAY_OF_MONTH);
                int month=cldr.get(Calendar.MONTH);
                int year=cldr.get(Calendar.YEAR);

                // date picker dialog
                picker=new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }
                                TODATE = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    TODATE = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }
                                String result="";
                                try {
                                    result = getActivity().getResources().getStringArray(R.array.month_names)[monthOfYear];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    result=Integer.toString(monthOfYear);
                                }
                                txt_todate.setText(dayOfMonth+" "+result+" "+year );

                            }
                        }, year, month, day);
                picker.show();
            }
        });
        txt_search = (TextView) rootView.findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   getFeedbackList(FROMDATE, TODATE);

            }
        });

        return rootView;
    }

    private void initUi() {
        queue = Volley.newRequestQueue(getActivity());
        txt_route_name = (TextView) rootView.findViewById(R.id.txt_route_name);

        getFeedbackList();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_add:

                demandDtos.clear();
                feedbackDialog();
                break;
        }
    }


    public void getFeedbackList(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.base_url)+"api/fo-feedback-share?fo_id="+SharePreference.getUserId(getActivity()) , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //  Log.e("<<>>",response+"<<>>");
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FeedbackDTO feedbackDTO = new FeedbackDTO();

                        feedbackDTO.setId(jsonObject.getString("id"));
                        feedbackDTO.setFo_id(jsonObject.getString("fo_id"));
                        feedbackDTO.setDate(jsonObject.getString("date"));
                        feedbackDTO.setRp_value(jsonObject.getString("rp_value"));
                        feedbackDTO.setRp_text(jsonObject.getString("rp_text"));
                        feedbackDTO.setFree_pending(jsonObject.getString("free_pending"));
                        feedbackDTO.setCompetition_facts(jsonObject.getString("competition_facts"));
                        feedbackDTO.setComplain_box(jsonObject.getString("complain_box"));
                        feedbackDTO.setDemandList(jsonObject.getString("details"));
                        //  Log.e("<<>>",jsonObject.getString("details")+"<<>>");

                        feedbackDTOS.add(feedbackDTO);
                    }
                  //  Log.e("<<>>",feedbackDTOS.size()+"<<>>");

                    feedback_list_adapter.notifyDataSetChanged();
                } catch (JSONException e) {

                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                //    Utility.dialog.closeProgressDialog();
            }

        }) ;

        queue.add(stringRequest);

    }

    public  String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null"))
            return "";
        else
            return json.optString(key, key);
    }

    private void feedbackDialog() {

        wdialog =new Dialog(getActivity(),android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.fo_feedback_dialog_new);

        Button btn_send_feedback;
        final TextView txt_close_dialog,txt_send_date;

        final EditText edt_txt_demandqty;

        txt_category_list = (TextView)  wdialog.findViewById(R.id.txt_category_list);
        txt_product_list = (TextView)  wdialog.findViewById(R.id.txt_product_list);
        edt_txt_demandqty = (EditText) wdialog.findViewById(R.id.edt_txt_demandqty);
        btn_show_dimand = (Button) wdialog.findViewById(R.id.btn_show_dimand);
        edt_demand_text = (EditText) wdialog.findViewById(R.id.edt_demand_text);
        edt_replacement_value = (EditText) wdialog.findViewById(R.id.edt_replacement_value);
        edt_replacement_feedback = (EditText) wdialog.findViewById(R.id.edt_replacement_feedback);
        edt_freepending_feedback = (EditText) wdialog.findViewById(R.id.edt_freepending_feedback);
        edt_competition_feedback = (EditText) wdialog.findViewById(R.id.edt_competition_feedback);
        edt_complain_feedback = (EditText) wdialog.findViewById(R.id.edt_complain_feedback);
        btn_send_feedback = (Button) wdialog.findViewById(R.id.btn_send_feedback);
        txt_send_date = (TextView)  wdialog.findViewById(R.id.txt_send_date);
        txt_send_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr=Calendar.getInstance();
                int day=cldr.get(Calendar.DAY_OF_MONTH);
                int month=cldr.get(Calendar.MONTH);
                int year=cldr.get(Calendar.YEAR);

                // date picker dialog
                picker=new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

//                              txt_month_year.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                fromDate = year + "-" + (monthOfYear + 1) + "-" +  dayOfMonth;
                                String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }
                                FROMDATE = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    FROMDATE = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }
                                String result="";
                                try {
                                    result = getActivity().getResources().getStringArray(R.array.month_names)[monthOfYear];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    result=Integer.toString(monthOfYear);
                                }
                                txt_send_date.setText(dayOfMonth+" "+result+" "+year );
                            }
                        }, year, month, day);
                picker.show();
            }

        });
        txt_close_dialog = (TextView)  wdialog.findViewById(R.id.txt_close_dialog);
       txt_close_dialog.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               wdialog.dismiss();
           }
       });

        btn_show_dimand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            demandListDialog();

            }
        });
        btn_add_demand = (Button) wdialog.findViewById(R.id.btn_add_demand);
        btn_add_demand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_txt_demandqty.getText().toString().equalsIgnoreCase("")|| CATEGORY_ID.equalsIgnoreCase("")||PRODUCT_ID.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "প্রোডাক্ট/কোয়ান্টিটি অ্যাড করুন। ", Toast.LENGTH_SHORT).show();
                }else {
                    DemandDTOs productDTO = new DemandDTOs();
                    productDTO.setCategoryId(CATEGORY_ID);
                    productDTO.setCategoryName(CATEGORY_NAME);
                    productDTO.setProductId(PRODUCT_ID);
                    productDTO.setProductName(PRODUCT_NAME);
                    productDTO.setProductQTY(edt_txt_demandqty.getText().toString());
                    productDTO.setProductDemandText(edt_demand_text.getText().toString());
                    demandDtos.add(productDTO);

                    edt_txt_demandqty.setText("");
                    edt_demand_text.setText("");
                    CATEGORY_ID = "";
                    PRODUCT_ID = "";
                    txt_category_list.setText("PG");
                    txt_product_list.setText("SKU");
                }

            }
        });
        txt_category_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         showCategoryListDialog();
            }
        });

        btn_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                feedJson = new JSONObject();

                try {
                    feedJson.put("fo_id",SharePreference.getUserId(getActivity()));
                    feedJson.put("date",PRESENT_DATE);
                    feedJson.put("rp_value",edt_replacement_value.getText().toString());
                    feedJson.put("rp_text",edt_replacement_feedback.getText().toString());
                    feedJson.put("free_pending",edt_freepending_feedback.getText().toString());
                    feedJson.put("competition_facts",edt_competition_feedback.getText().toString());
                    feedJson.put("complain_box",edt_complain_feedback.getText().toString());

                    JSONArray jsonArray = new JSONArray();


                    for (int i=0;i<demandDtos.size();i++){
                        JSONObject  jsonObject = new JSONObject();
                            jsonObject.put("category_id",demandDtos.get(i).getCategoryId());
                            jsonObject.put("product_id",demandDtos.get(i).getProductId());
                            jsonObject.put("pd_qty",demandDtos.get(i).getProductQTY());
                            jsonObject.put("pd_text",demandDtos.get(i).getProductDemandText());

                        jsonArray.put(jsonObject);

                    }

                    feedJson.put("details",jsonArray);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            //    Log.e("<<>>",feedJson+"");

             sendFeedback(feedJson.toString());

            }
        });

        wdialog.show();
    }

    private void demandListDialog() {

        wdialogDemand =new Dialog(getActivity());
        wdialogDemand.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialogDemand.setContentView(R.layout.dialog_dimand_list);
        final RecyclerView demand_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;
        final RelativeLayout rlDialogCross;
        demand_list_recyclerView=(RecyclerView) wdialogDemand.findViewById(R.id.demand_list_recyclerView);
        imbtnCross=(ImageView)wdialogDemand.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialogDemand.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialogDemand.findViewById(R.id.btnDoneDialog);
        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wdialogDemand.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wdialogDemand.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialogDemand.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        demand_list_recyclerView.setLayoutManager(linearLayoutManager);
        demandRecyclerAdapter = new DemandRecyclerAdapter( demandDtos,getActivity());
        demand_list_recyclerView.setAdapter(demandRecyclerAdapter);

        demand_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                new AlertDialog.Builder(getActivity())
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
                                for (int i=0;i<demandDtos.size();i++){
                                    if (demandDtos.get(i).getProductId().equalsIgnoreCase(demandDtos.get(position).getProductId())){
                                        demandDtos.remove(i);
                                    }
                                }
                                demandRecyclerAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }).show();
            }
        }));

        wdialogDemand.show();
    }

    private void showCategoryListDialog() {
        categoryDTOS.clear();
        for (CategoryDTO hm : categoryDTOSREserve) {
            categoryDTOS.add(hm);
        }
        productDtos.clear();
        wdialog_category =new Dialog(getActivity());
        wdialog_category.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog_category.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final ImageView imbtnCross;
        final EditText etSearch;
        final RelativeLayout rlDialogCross;
        etSearch = (EditText) wdialog_category.findViewById(R.id.edt_txt_search);
        route_list_recyclerView=(RecyclerView) wdialog_category.findViewById(R.id.route_list_recyclerView);
        imbtnCross= (ImageView) wdialog_category.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout) wdialog_category.findViewById(R.id.rl_dialog_cross);
        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   routeRecyclerAdapter.notifyDataSetChanged();
                wdialog_category.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  routeRecyclerAdapter.notifyDataSetChanged();

                wdialog_category.dismiss();
            }
        });


        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        routeRecyclerAdapter = new ProductCategoryRecyclerAdapter( categoryDTOS,getActivity());
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                productDtos = databaseHelper.getProductList(databaseHelper,categoryDTOS.get(position).getId());
                txt_category_list.setText(categoryDTOS.get(position).getName());
                CATEGORY_ID = categoryDTOS.get(position).getGid();
                CATEGORY_NAME = categoryDTOS.get(position).getName();
                showProductListDialog();
                wdialog_category.dismiss();

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

        wdialog_category.show();
    }

    private void showProductListDialog() {

        productDialog =new Dialog(getActivity(),R.style.full_screen_dialog);
        productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        productDialog.setContentView(R.layout.dialog_product_list);
        final Button btnDone,btn_add;
        final ImageView btn_dialog_cross,btn_add_product_cross;
        final EditText etSearch;
        final CardView card_view;

        etSearch = (EditText)productDialog.findViewById(R.id.edt_txt_search);
        btn_dialog_cross = (ImageView) productDialog.findViewById(R.id.btn_dialog_cross);
        btn_dialog_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               productDialog.dismiss();
            }
        });


        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        product_list_recyclerView=(RecyclerView) productDialog.findViewById(R.id.product_list_recyclerView);

        product_list_recyclerView.setLayoutManager(linearLayoutManager);
        productListRecyclerAdapter = new ProductListRecyclerAdapter( productDtos,getActivity());
        product_list_recyclerView.setAdapter(productListRecyclerAdapter);

        product_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                PRODUCT_ID = productDtos.get(position).getProduct_id();
                PRODUCT_NAME = productDtos.get(position).getProduct_name();
                txt_product_list.setText(productDtos.get(position).getProduct_name());
                productDialog.dismiss();
            }
        }));

        productDialog.show();
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

    public void sendFeedback(String feedJson) {
        vm.sendRequestToServer2(getActivity(),getResources().getString(R.string.base_url) + "api/fo-feedback-store",feedJson, new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {

                    //    Log.e("<<>>",result+"<<>>");
                        try {
                            JSONObject jsonObject1 = new JSONObject(result);
                            if (jsonObject1.getString("message").equalsIgnoreCase("Feedback and details created successfully")){

                               edt_replacement_value.setText("");
                                edt_replacement_feedback.setText("");
                                edt_freepending_feedback.setText("");
                                edt_competition_feedback.setText("");
                                edt_complain_feedback.setText("");

                                demandDtos.clear();


                            }
                            Toast.makeText(getActivity(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}