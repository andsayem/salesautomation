package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.ReturnAndChangeFragment;
import com.ssgbd.salesautomation.drawer.fragment.WastageFragment;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.returnchange.ReturnAndChangeActivity;
import com.ssgbd.salesautomation.wastage.WastageActivity;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class RetailerRecyclerAdapter_Westage extends RecyclerView.Adapter<RetailerRecyclerAdapter_Westage.NewReleasesItemViewHolder>{

    public ArrayList<RetailerDTO> routeList;
    public ArrayList<RetailerDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;

    WastageFragment visitFragment;

    public RetailerRecyclerAdapter_Westage(ArrayList<RetailerDTO> items, Context context, WastageFragment visitFragment) {
        this.routeList = items;
        this.context = context;
        this.visitFragment = visitFragment;
        this.arrayList = new ArrayList<RetailerDTO>();
        this.arrayList.addAll(routeList);
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_retailer_wastage,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final RetailerDTO itemFeed = routeList.get(position);

        try {

            holder.row_retailer_name.setText(itemFeed.getRetailer_name()+" ("+itemFeed.getRetailer_id()+")");
            holder.row_statis.setText(itemFeed.getStatus());
            holder.row_return_and_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, WastageActivity.class);
                    intent.putExtra("retailerId",itemFeed.getRetailer_id());
                    intent.putExtra("retailerName",itemFeed.getRetailer_name());
                    intent.putExtra("poient_id",itemFeed.getPoint_id());
                    context.startActivity(intent);
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
        public TextView row_retailer_name,row_return_and_change,row_statis;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_retailer_name = (TextView) itemView.findViewById(R.id.row_retailer_name);
            row_return_and_change = (TextView) itemView.findViewById(R.id.row_return_and_change);
            row_statis = (TextView) itemView.findViewById(R.id.row_statis);

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
