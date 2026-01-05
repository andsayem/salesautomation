package com.ssgbd.salesautomation.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.ForecastEntryListDTO;
import com.ssgbd.salesautomation.dtos.StockListDTO;

import java.util.ArrayList;

/**
 * Created by Rashed on 26/4/2017.
 */
public class ForecastEntryListAdapter extends RecyclerView.Adapter<ForecastEntryListAdapter.NewReleasesItemViewHolder>{

    public ArrayList<ForecastEntryListDTO> stockListDTOS;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public ForecastEntryListAdapter(ArrayList<ForecastEntryListDTO> items, Context context) {
        this.stockListDTOS = items;
        this.context = context;

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_forecast_entry_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final ForecastEntryListDTO itemFeed = stockListDTOS.get(position);

        try {
           // holder.row_sl_no.setText(String.valueOf(position+1)+".");
            holder.row_product_id.setText(itemFeed.getProduct_id());
            holder.row_product_name.setText(itemFeed.getProduct_name());
            holder.row_product_qty.setText(itemFeed.getAvg_2022_ims_qty());
            holder.row_product_value.setText(itemFeed.getAvg_2023_ims_qty());
            holder.row_forecastQty.setText(itemFeed.getForecastQty());

            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.showSoftInput(holder.row_forecastQty, 0);

//            holder.row_forecastQty.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    holder.row_forecastQty.requestFocus();
//                    InputMethodManager imm=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(holder.row_forecastQty, 0);
//
//                }
//            });
            holder.row_forecastQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                   // Log.e("<</>>",itemFeed.getProduct_name()+"<</>>");

                }

                @Override
                public void afterTextChanged(Editable s) {
                   // Log.e("<</>>",holder.row_forecastQty.getText()+"<</>>");
                    stockListDTOS.get(position).setForecastQty(holder.row_forecastQty.getText().toString());

                }
            });

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return stockListDTOS.size();

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_product_id,row_product_name,row_sl_no,row_product_qty,row_product_value;
        Button row_product_select;
        EditText row_forecastQty;



        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_product_id = (TextView) itemView.findViewById(R.id.row_product_id);
            row_product_name = (TextView) itemView.findViewById(R.id.row_product_name);
            row_product_qty = (TextView) itemView.findViewById(R.id.row_product_qty);
            row_product_value = (TextView) itemView.findViewById(R.id.row_product_value);
            row_product_select = (Button) itemView.findViewById(R.id.row_product_select);
            row_forecastQty = (EditText) itemView.findViewById(R.id.row_forecastQty);

        }
    }
}
