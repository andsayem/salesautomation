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
import com.ssgbd.salesautomation.dtos.ConfirmOrderListDTO;
import com.ssgbd.salesautomation.dtos.OrderDTO;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class OrderListRecyclerAdapter extends RecyclerView.Adapter<OrderListRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<ConfirmOrderListDTO> orderList;
    public ArrayList<ConfirmOrderListDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public OrderListRecyclerAdapter(ArrayList<ConfirmOrderListDTO> items, Context context) {
        this.orderList = items;
        this.context = context;

        this.arrayList = new ArrayList<ConfirmOrderListDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View view = inflater.from(context).inflate(R.layout.row_confirm_order_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final ConfirmOrderListDTO itemFeed = orderList.get(position);

        try {


            int f = position+1;
           // holder.row_txt_sl_no.setText(String.valueOf(f));

            holder.row_txt_retailername.setText(itemFeed.getName());
            holder.row_txt_order_number.setText("Order No :"+itemFeed.getOrderNo());
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
        public TextView row_txt_retailername,row_txt_order_number;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_txt_retailername = (TextView) itemView.findViewById(R.id.row_txt_retailername);
            row_txt_order_number = (TextView) itemView.findViewById(R.id.row_txt_order_number);

            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (ConfirmOrderListDTO hm : arrayList) {
                if (hm.getFirstName().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
