package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.OrderCollectionDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class OrderNoRecyclerAdapter extends RecyclerView.Adapter<OrderNoRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<OrderCollectionDTO> orderList;
    public ArrayList<OrderCollectionDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public OrderNoRecyclerAdapter(ArrayList<OrderCollectionDTO> items, Context context) {
        this.orderList = items;
        this.context = context;
        this.arrayList = new ArrayList<OrderCollectionDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_order_no,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final OrderCollectionDTO itemFeed = orderList.get(position);

        try {

            holder.route_name.setText("Order No.:"+itemFeed.getOrder_no());
            holder.txt_total_value.setText("IMS: "+itemFeed.getTotal_value()+"; Dues: "+itemFeed.getDue_amount());

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView route_name,txt_total_value;

        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            route_name = (TextView) itemView.findViewById(R.id.route_name);
            txt_total_value = (TextView) itemView.findViewById(R.id.txt_total_value);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (OrderCollectionDTO hm : arrayList) {
                if (hm.getOrder_no().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
