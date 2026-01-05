package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.WastageFragment;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.retailer.EditRetailerActivity;
import com.ssgbd.salesautomation.retailer.NewRetailerActivity;
import com.ssgbd.salesautomation.retailer.NewRetailerFragment1;
import com.ssgbd.salesautomation.wastage.WastageActivity;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class RetailerRecyclerAdapter_NewRetailer1 extends RecyclerView.Adapter<RetailerRecyclerAdapter_NewRetailer1.NewReleasesItemViewHolder>{

    public ArrayList<RetailerDTO> routeList;
    public ArrayList<RetailerDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;

    NewRetailerFragment1 visitFragment;

    public RetailerRecyclerAdapter_NewRetailer1(ArrayList<RetailerDTO> items, Context context, NewRetailerFragment1 visitFragment) {
        this.routeList = items;
        this.context = context;
        this.visitFragment = visitFragment;
        this.arrayList = new ArrayList<RetailerDTO>();
        this.arrayList.addAll(routeList);
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_retailer_new_retailer,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final RetailerDTO itemFeed = routeList.get(position);

        try {

            holder.row_retailer_name.setText(itemFeed.getRetailer_name()+" ("+itemFeed.getRetailer_id()+")");
            holder.row_woner_name.setText(itemFeed.getRetailerOwner());
            holder.row_woner_mobile.setText(itemFeed.getRetailerOwner());

            if (itemFeed.getRetailerMobileNo2().equalsIgnoreCase("")) {
                holder.row_serial.setText("Not Done");
                holder.row_serial.setTextColor(Color.parseColor("#a8237f"));
            }else {
                holder.row_serial.setText("Done");
                holder.row_serial.setTextColor(Color.parseColor("#3A5F0B"));

            }

            holder.row_add_retailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, NewRetailerActivity.class);
                    intent.putExtra("retailerId",itemFeed.getRetailer_id());
                    intent.putExtra("retailerName",itemFeed.getRetailer_name());
                    intent.putExtra("poient_id",itemFeed.getPoint_id());
                    intent.putExtra("retailer_serial",itemFeed.getSerial());

                    context.startActivity(intent);

//                  Toast.makeText(context, "server error", Toast.LENGTH_SHORT).show();

                }
            }); holder.row_edit_retailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, EditRetailerActivity.class);
                    intent.putExtra("retailerId",itemFeed.getRetailer_id());
                    intent.putExtra("retailerName",itemFeed.getRetailer_name());
                    intent.putExtra("poient_id",itemFeed.getPoint_id());
                    intent.putExtra("retailer_serial",itemFeed.getRetailerSerial());
                    intent.putExtra("vAddress",itemFeed.getvAddress());
                    intent.putExtra("phone",itemFeed.getRetailerMobileNo());
                    intent.putExtra("optional_mobile",itemFeed.getRetailerMobileNo2());
                    intent.putExtra("woner",itemFeed.getRetailerOwner());
                    intent.putExtra("shoptype",itemFeed.getShopeType());
                    intent.putExtra("dob",itemFeed.getDob());
                    intent.putExtra("email",itemFeed.getEmail());
                    intent.putExtra("fb",itemFeed.getFb());
                    intent.putExtra("whatsapp",itemFeed.getWhatsapp());

                    context.startActivity(intent);

                //        Toast.makeText(context, "server error", Toast.LENGTH_SHORT).show();

                }
            });



        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_retailer_name,row_serial,row_add_retailer,row_edit_retailer,row_woner_name,row_woner_mobile;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_retailer_name = (TextView) itemView.findViewById(R.id.row_retailer_name);
            row_add_retailer = (TextView) itemView.findViewById(R.id.row_add_retailer);
            row_edit_retailer = (TextView) itemView.findViewById(R.id.row_edit_retailer);
            row_woner_name = (TextView) itemView.findViewById(R.id.row_woner_name);
            row_woner_mobile = (TextView) itemView.findViewById(R.id.row_woner_mobile);
            row_serial = (TextView) itemView.findViewById(R.id.row_serial);

            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        routeList.clear();
        if (charText.length() == 0) {
            routeList.addAll(arrayList);
        } else {
            for (RetailerDTO hm : arrayList) {
                if (hm.getRetailer_name().toLowerCase().contains(charText)) {
                    routeList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
