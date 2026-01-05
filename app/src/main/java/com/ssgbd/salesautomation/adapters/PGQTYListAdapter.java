package com.ssgbd.salesautomation.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.ForecastEntryListDTO;
import com.ssgbd.salesautomation.dtos.PGPercentageDTO;

import java.util.ArrayList;

/**
 * Created by Rashed on 26/4/2017.
 */
public class PGQTYListAdapter extends RecyclerView.Adapter<PGQTYListAdapter.NewReleasesItemViewHolder>{

    public ArrayList<PGPercentageDTO> stockListDTOS;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public PGQTYListAdapter(ArrayList<PGPercentageDTO> items, Context context) {
        this.stockListDTOS = items;
        this.context = context;

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_pg_qty_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final PGPercentageDTO itemFeed = stockListDTOS.get(position);

        try {
//            holder.row_sl_no.setText(String.valueOf(position+1)+".");
            holder.text_cat_name.setText(itemFeed.getName());
            holder.text_cat_target.setText(itemFeed.getId());
            holder.text_cat_del.setText(itemFeed.getDelivery_qty());

            holder.text_cat_average.setText(itemFeed.getAchieveRate()+"%");

            int achieveRate = Integer.valueOf(itemFeed.getAchieveRate());

            if (achieveRate < 80) {
                // Category 1: Below 80% (Red)
                holder.text_cat_average.setTextColor(Color.parseColor("#ff0000"));
            } else if (achieveRate < 90) {
                // Category 2: 80%-90% (Orange)
                holder.text_cat_average.setTextColor(Color.parseColor("#FFA500")); // Orange color
            } else if (achieveRate <= 100) {
                // Category 3: 90%-100% (Blue)
                holder.text_cat_average.setTextColor(Color.parseColor("#0000FF")); // Blue color
            } else {
                // Category 4: Above 100% (Green)
                holder.text_cat_average.setTextColor(Color.parseColor("#008000")); // Green color
            }

            //#FFA500

//            if (Integer.valueOf(itemFeed.getAchieveRate())<80)
//            {
//                holder.text_cat_average.setTextColor(Color.parseColor("#ff0000"));
//            }else if(Integer.valueOf(itemFeed.getAchieveRate())<80)
//            {
//                holder.text_cat_average.setTextColor(Color.parseColor("#008000"));
//            }

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
        public TextView text_cat_name,text_cat_target,text_cat_del,text_cat_average;



        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            text_cat_name = (TextView) itemView.findViewById(R.id.text_cat_name);
            text_cat_target = (TextView) itemView.findViewById(R.id.text_cat_target);
            text_cat_del = (TextView) itemView.findViewById(R.id.text_cat_del);
            text_cat_average = (TextView) itemView.findViewById(R.id.text_cat_average);
        }
    }
}
