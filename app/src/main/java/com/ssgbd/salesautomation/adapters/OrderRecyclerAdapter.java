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

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<OrderDTO> orderList;
    public ArrayList<OrderDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public OrderRecyclerAdapter(ArrayList<OrderDTO> items, Context context) {
        this.orderList = items;
        this.context = context;

        this.arrayList = new ArrayList<OrderDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View view = inflater.from(context).inflate(R.layout.row_order_product,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final OrderDTO itemFeed = orderList.get(position);

        try {


            int f = position+1;
            holder.row_txt_sl_no.setText(String.valueOf(f));
            holder.row_txt_product_name.setText(itemFeed.getProduct_name());
            holder.row_txt_product_qty.setText(itemFeed.getProduct_qty());
            holder.row_txt_product_value.setText(itemFeed.getProduct_price());
            holder.row_txt_product_wastage.setText(itemFeed.getWastage_qty());
            holder.linlay_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
        public TextView row_txt_sl_no,row_txt_product_name,row_txt_product_qty,row_txt_product_value,row_txt_product_wastage;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_txt_sl_no = (TextView) itemView.findViewById(R.id.row_txt_sl_no);
            row_txt_product_name = (TextView) itemView.findViewById(R.id.row_txt_product_name);
            row_txt_product_qty = (TextView) itemView.findViewById(R.id.row_txt_product_qty);
            row_txt_product_value = (TextView) itemView.findViewById(R.id.row_txt_product_value);
            row_txt_product_wastage = (TextView) itemView.findViewById(R.id.row_txt_product_wastage);
            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (OrderDTO hm : arrayList) {
                if (hm.getProduct_name().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
