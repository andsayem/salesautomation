package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.StockListDTO;
import com.ssgbd.salesautomation.utils.Common;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Dialog;


public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.NewReleasesItemViewHolder> {


    private ArrayList<StockListDTO> stockListDTOS;
    private Context context;
    private Animation animation;


    public StockListAdapter(ArrayList<StockListDTO> items, Context context) {

        this.stockListDTOS = items;
        this.context = context;

    }



    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_stock_list, parent, false);

        return new NewReleasesItemViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, int position) {


        StockListDTO item = stockListDTOS.get(position);


        holder.row_product_name.setText(item.getProductName());
        holder.row_product_qty.setText(item.getStockQty());
        holder.row_product_value.setText(item.getStockValue());
        if (item.getStockDemand() != null
                && !item.getStockDemand().isEmpty()
                && !item.getStockDemand().equals("0")) {

            holder.row_product_select.setText(item.getStockDemand());

            holder.row_product_select.setEnabled(false);
            holder.row_product_select.setClickable(false);

        } else {

            holder.row_product_select.setText("Add QTY");

            holder.row_product_select.setEnabled(true);
            holder.row_product_select.setClickable(true);

        }



        holder.row_product_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                int pos = holder.getAdapterPosition();


                if(pos == RecyclerView.NO_POSITION){
                    return;
                }



                StockListDTO itemFeed = stockListDTOS.get(pos);



                Dialog dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.dialog_stock_demand);

                dialog.setCancelable(true);



                Window window = dialog.getWindow();

                if(window != null){

                    window.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    window.setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT)
                    );
                }




                TextView txtProduct =
                        dialog.findViewById(R.id.txtProductName);


                TextView txtStock =
                        dialog.findViewById(R.id.txtCurrentStock);


                TextView txtInfo =
                        dialog.findViewById(R.id.txtInfo);


                EditText edtQty =
                        dialog.findViewById(R.id.edtDemandQty);

                EditText edtRemarks =
                        dialog.findViewById(R.id.edtRemarks);



                Button btnPlus =
                        dialog.findViewById(R.id.btnPlus);


                Button btnMinus =
                        dialog.findViewById(R.id.btnMinus);


                Button btnCancel =
                        dialog.findViewById(R.id.btnCancel);


                Button btnSubmit =
                        dialog.findViewById(R.id.btnSubmit);




                txtProduct.setText(
                        itemFeed.getProductName()
                );


                txtStock.setText(
                        "Stock QTY : "
                                + itemFeed.getStockQty()
                );


                txtInfo.setText(getStockDemandMessage());




                btnPlus.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        int qty = 0;


                        try{

                            qty = Integer.parseInt(
                                    edtQty.getText().toString()
                            );

                        }catch(Exception e){

                            qty = 0;

                        }


                        qty++;


                        edtQty.setText(
                                String.valueOf(qty)
                        );

                    }
                });






                btnMinus.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        int qty = 0;


                        try{

                            qty = Integer.parseInt(
                                    edtQty.getText().toString()
                            );

                        }catch(Exception e){

                            qty = 0;

                        }



                        if(qty > 0){

                            qty--;

                        }



                        edtQty.setText(
                                String.valueOf(qty)
                        );


                    }
                });







                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });






                btnSubmit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        String qty =
                                edtQty.getText()
                                        .toString()
                                        .trim();

                        String remarks = edtRemarks.getText().toString().trim();



                        if(qty.isEmpty() || qty.equals("0")){


                            edtQty.setError(
                                    "Enter Demand Qty"
                            );


                            edtQty.requestFocus();


                            return;

                        }



                        int productId = 0;
                        int currentStock = 0;
                        int demandQty = 0;

                        Log.e("itemFeed" ,itemFeed.toString());

                        try {

                            productId = Integer.parseInt(
                                    itemFeed.getProductid()
                            );

                        } catch (Exception e) {

                            e.printStackTrace();

                        }




                        try {

                            currentStock = Integer.parseInt(
                                    itemFeed.getStockQty()
                            );

                        } catch (Exception e) {

                            e.printStackTrace();

                        }




                        try {

                            demandQty = Integer.parseInt(qty);

                        } catch (Exception e) {

                            e.printStackTrace();

                        }



                        submitDemand(
                                itemFeed,
                                pos,
                                productId,
                                demandQty,
                                currentStock,
                                remarks
                        );



                        dialog.dismiss();


                    }
                });




                dialog.show();


            }
        });


    }


    private String getStockDemandMessage() {

        Calendar today = Calendar.getInstance();

        int currentDay = today.get(Calendar.DAY_OF_MONTH);
        int lastDay = today.getActualMaximum(Calendar.DAY_OF_MONTH);

        int remainingDays = (lastDay - currentDay) + 1;

        SimpleDateFormat sdf =
                new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        Calendar lastDate = (Calendar) today.clone();
        lastDate.set(Calendar.DAY_OF_MONTH, lastDay);

        return "নোট: প্রতি প্রোডাক্টের জন্য মাসে মাত্র একবার স্টক ডিমান্ড করা যাবে। "
                + "আপনি যে স্টক ডিমান্ড প্রদান করবেন, তা বর্তমান তারিখ থেকে চলতি মাসের শেষ দিন ("
                + sdf.format(lastDate.getTime())
                + ") পর্যন্ত, অর্থাৎ বাকি "
                + remainingDays
                + " দিনের চাহিদার জন্য বিবেচিত হবে। তাই অনুগ্রহ করে প্রয়োজন অনুযায়ী সঠিক পরিমাণ উল্লেখ করুন।";
    }



    private void submitDemand(
            StockListDTO itemFeed,
            int position,
            int productId,
            int demandQty,
            int currentStock,
            String remarks
    ){



        JSONObject jsonObject = new JSONObject();



        try {


            jsonObject.put(
                    "fo_id",
                    SharePreference.getUserId(context)
            );


            jsonObject.put(
                    "point_id",
                    SharePreference.getUserPointId(context)
            );


            jsonObject.put(
                    "product_id",
                     productId
            );


            jsonObject.put(
                    "current_stock",
                    currentStock
            );


            jsonObject.put(
                    "stock_demand",
                    demandQty
            );

            jsonObject.put(
                    "remarks",
                    remarks
            );



        } catch (JSONException e) {

            e.printStackTrace();

        }




        Log.e(
                "API_URL",
                Common.getBaseUrl(context)
                        + "api/stock_demand_insert"
        );
        Log.e(
                "POST_DATA",
                jsonObject.toString()
        );
        JsonObjectRequest request =
                new JsonObjectRequest(

                        Request.Method.POST,

                        Common.getBaseUrl(context)
                                + "api/stock_demand_insert",

                        jsonObject,


                        response -> {

                            itemFeed.setStockDemand(String.valueOf(demandQty));

                            notifyItemChanged(position);


                            Toast.makeText(
                                    context,
                                    "Demand Submitted Successfully",
                                    Toast.LENGTH_SHORT
                            ).show();


                        },


                        error -> {

                            try {

                                String response =
                                        new String(error.networkResponse.data);


                                Toast.makeText(
                                        context,
                                        response,
                                        Toast.LENGTH_LONG
                                ).show();


                                Log.e(
                                        "API_ERROR",
                                        response
                                );


                            } catch (Exception e) {


                                Toast.makeText(
                                        context,
                                        error.toString(),
                                        Toast.LENGTH_LONG
                                ).show();


                            }

                        }

                ){



                    @Override
                    public Map<String,String> getHeaders(){

                        Map<String,String> headers = new HashMap<>();

                        headers.put(
                                "Content-Type",
                                "application/json"
                        );

                        headers.put(
                                "Accept",
                                "application/json"
                        );

                        return headers;
                    }

                };




        VolleySingleton
                .getInstance(context)
                .addToRequestQueue(request);



    }






    @Override
    public int getItemCount() {

        return stockListDTOS.size();

    }






    public class NewReleasesItemViewHolder
            extends RecyclerView.ViewHolder {


        TextView row_product_name;
        TextView row_product_qty;
        TextView row_product_value;

        Button row_product_select;



        public NewReleasesItemViewHolder(View itemView) {

            super(itemView);


            row_product_name =
                    itemView.findViewById(
                            R.id.row_product_name
                    );


            row_product_qty =
                    itemView.findViewById(
                            R.id.row_product_qty
                    );


            row_product_value =
                    itemView.findViewById(
                            R.id.row_product_value
                    );


            row_product_select =
                    itemView.findViewById(
                            R.id.row_product_select
                    );


        }

    }


}