package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.StockListDTO;

import java.util.ArrayList;

/**
 * Created by Rashed on 26/4/2017.
 */
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.NewReleasesItemViewHolder>{

    public ArrayList<StockListDTO> stockListDTOS;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public StockListAdapter(ArrayList<StockListDTO> items, Context context) {
        this.stockListDTOS = items;
        this.context = context;

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_stock_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final StockListDTO itemFeed = stockListDTOS.get(position);

        try {
           // holder.row_sl_no.setText(String.valueOf(position+1)+".");
            holder.row_product_name.setText(itemFeed.getProductName());
            holder.row_product_qty.setText(itemFeed.getStockQty());
            holder.row_product_value.setText(itemFeed.getStockValue());

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return stockListDTOS.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_product_name,row_sl_no,row_product_qty,row_product_value;
        Button row_product_select;



        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_product_name = (TextView) itemView.findViewById(R.id.row_product_name);
            row_product_qty = (TextView) itemView.findViewById(R.id.row_product_qty);
            row_product_value = (TextView) itemView.findViewById(R.id.row_product_value);
            row_product_select = (Button) itemView.findViewById(R.id.row_product_select);


        }
    }
}
