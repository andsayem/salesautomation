package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.OfferImageShowActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.DirectOrderManageFragment;
import com.ssgbd.salesautomation.dtos.DirectOrderDTO;
import com.ssgbd.salesautomation.dtos.SmartOrderDTO;
import com.ssgbd.salesautomation.visit.OrderActivity;

import java.io.IOException;
import java.util.ArrayList;

public class DirectOrderListRecyclerAdapter extends RecyclerView.Adapter<DirectOrderListRecyclerAdapter.NewReleasesItemViewHolder> {

    private ArrayList<DirectOrderDTO> orderList;
    private ArrayList<DirectOrderDTO> arrayList;
    private Context context;

    DirectOrderManageFragment directOrderManageFragment;

    public DirectOrderListRecyclerAdapter(ArrayList<DirectOrderDTO> items,DirectOrderManageFragment directOrderManageFragment, Context context) {
        this.orderList = items;
        this.context = context;
        this.arrayList = new ArrayList<>(orderList);
        this.directOrderManageFragment = directOrderManageFragment;
    }

    @NonNull
    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_manage_direct_order, parent, false);
        return new NewReleasesItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewReleasesItemViewHolder holder, final int position) {
        final DirectOrderDTO item = orderList.get(position);


        holder.row_retailer_name.setText("Retailer: " + item.getName());
        holder.row_retailer_phone.setText("Phone: " + item.getMobile());
        holder.row_route_name.setText("Route : " + item.getRname());
        holder.row_order_no.setText("Order No: " + item.getOrder_no());
        holder.row_order_status.setText( item.getOrder_type());
        holder.row_btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

    directOrderManageFragment.getOrderStatus(item.getOrder_id(), item.getRoute_id(), item.getRetailer_id(), item.getPoint_id(), item.getName());

            }
        });
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.isEmpty()) {
            orderList.addAll(arrayList);
        } else {
            for (DirectOrderDTO item : arrayList) {
                if (item.getOrder_id().toLowerCase().contains(charText)) {
                    orderList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }


    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder {
        public TextView row_retailer_name,row_order_status, row_retailer_phone, row_route_name,row_order_no, row_sound_stop;

        public Button row_btn_order;

        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);

            row_retailer_name = itemView.findViewById(R.id.row_retailer_name);
            row_order_no = itemView.findViewById(R.id.row_order_no);
            row_retailer_phone = itemView.findViewById(R.id.row_retailer_phone);
            row_route_name = itemView.findViewById(R.id.row_route_name);
            row_sound_stop = itemView.findViewById(R.id.row_sound_stop);
            row_sound_stop = itemView.findViewById(R.id.row_sound_stop);
            row_btn_order = itemView.findViewById(R.id.row_btn_order);
            row_order_status = itemView.findViewById(R.id.row_order_status);
        }
    }
}