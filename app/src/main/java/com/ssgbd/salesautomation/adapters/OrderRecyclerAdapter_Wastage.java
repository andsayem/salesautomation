package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.OrderDTO;
import com.ssgbd.salesautomation.dtos.WastageOrderDTO;
import com.ssgbd.salesautomation.wastage.WastageActivity;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class OrderRecyclerAdapter_Wastage extends RecyclerView.Adapter<OrderRecyclerAdapter_Wastage.NewReleasesItemViewHolder>{

    public ArrayList<WastageOrderDTO> orderList;
    public ArrayList<WastageOrderDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;




    public OrderRecyclerAdapter_Wastage(ArrayList<WastageOrderDTO> items, Context context) {
        this.orderList = items;
        this.context = context;

        this.arrayList = new ArrayList<WastageOrderDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View view = inflater.from(context).inflate(R.layout.row_order_product_wastage,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final WastageOrderDTO itemFeed = orderList.get(position);

        try {


            int f = position+1;
            holder.row_txt_product_name.setText(itemFeed.getProduct_name());
            holder.row_txt_product_qty.setText(itemFeed.getProduct_qty());
            holder.row_txt_product_value.setText(itemFeed.getProduct_price());

            holder.row_txt_product_name_return.setText(itemFeed.getProduct_name_return());
            holder.row_txt_product_qty_return.setText(itemFeed.getProduct_qty_return());
            holder.row_txt_product_value_return.setText(itemFeed.getProduct_price_return());
            holder.row_txt_product_qty_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((WastageActivity)context).deleteAProduct(position);
                }
            });

            holder.row_txt_get_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((WastageActivity)context).getReturnProduct(itemFeed.getProduct_id());
                }
            });

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_txt_sl_no,row_txt_product_name,row_txt_product_qty,row_txt_product_value;
        public TextView row_txt_product_name_return,row_txt_product_qty_return,row_txt_product_value_return;
        public TextView row_txt_product_qty_delete,row_txt_get_product;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_txt_product_name = (TextView) itemView.findViewById(R.id.row_txt_product_name);
            row_txt_product_qty = (TextView) itemView.findViewById(R.id.row_txt_product_qty);
            row_txt_product_value = (TextView) itemView.findViewById(R.id.row_txt_product_value);

            row_txt_product_name_return = (TextView) itemView.findViewById(R.id.row_txt_product_name_return);
            row_txt_product_qty_return = (TextView) itemView.findViewById(R.id.row_txt_product_qty_return);
            row_txt_product_value_return = (TextView) itemView.findViewById(R.id.row_txt_product_value_return);

            row_txt_product_qty_delete = (TextView) itemView.findViewById(R.id.row_txt_product_qty_delete);
            row_txt_get_product = (TextView) itemView.findViewById(R.id.row_txt_get_product);
            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (WastageOrderDTO hm : arrayList) {
                if (hm.getProduct_name().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
