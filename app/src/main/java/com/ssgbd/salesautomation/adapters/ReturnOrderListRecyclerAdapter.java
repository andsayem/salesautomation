package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.ManageOrderDTO;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class ReturnOrderListRecyclerAdapter extends RecyclerView.Adapter<ReturnOrderListRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<ManageOrderDTO> orderList;
    public ArrayList<ManageOrderDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public ReturnOrderListRecyclerAdapter(ArrayList<ManageOrderDTO> items, Context context ) {
        this.orderList = items;
        this.context = context;
        this.arrayList = new ArrayList<ManageOrderDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View view = inflater.from(context).inflate(R.layout.row_manage_order,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final ManageOrderDTO itemFeed = orderList.get(position);

        try {

            holder.row_order_number.setText("Order No: "+itemFeed.getOrder_no());
            holder.row_retailer_anme.setText("Shope Name: "+itemFeed.getName());
            holder.row_order_date.setText("Order Date: "+itemFeed.getOrder_date());


//
//            holder.row_item2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    new AlertDialog.Builder(context)
//                            .setTitle("Alert")
//                            .setIcon(R.mipmap.ic_launcher)
//                            .setMessage("Delete this order?").setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//
//                                    dialog.dismiss();
//                                }
//                            }).show();
//
//
//                }
//            });

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_order_number,row_retailer_anme,row_order_date;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_order_number = (TextView) itemView.findViewById(R.id.row_order_number);
            row_retailer_anme = (TextView) itemView.findViewById(R.id.row_retailer_anme);
            row_order_date = (TextView) itemView.findViewById(R.id.row_order_date);
            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (ManageOrderDTO hm : arrayList) {
                if (hm.getName().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }



}
