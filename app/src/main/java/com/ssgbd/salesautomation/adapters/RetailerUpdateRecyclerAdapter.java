package com.ssgbd.salesautomation.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.RetailerUpdateFragment;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class RetailerUpdateRecyclerAdapter extends RecyclerView.Adapter<RetailerUpdateRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<RetailerDTO> routeList;
    public ArrayList<RetailerDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    RetailerUpdateFragment retailerUpdateFragment;

    public RetailerUpdateRecyclerAdapter(ArrayList<RetailerDTO> items, Context context, RetailerUpdateFragment retailerUpdateFragment) {
        this.routeList = items;
        this.context = context;
        this.arrayList = new ArrayList<RetailerDTO>();
        this.arrayList.addAll(routeList);
        this.retailerUpdateFragment = retailerUpdateFragment;
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_retailer_update,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final RetailerDTO itemFeed = routeList.get(position);

        try {

            holder.row_retailer_name.setText(itemFeed.getRetailer_name()+" "+"("+itemFeed.getRetailer_id()+")");

            //holder.row_statis.setText(itemFeed.getStatus());
            if (itemFeed.getLat().equalsIgnoreCase("")||itemFeed.getLon().equalsIgnoreCase("")){
                holder.row_visit.setText("Not Done");
                holder.row_visit.setTextColor(Color.parseColor("#ff0000"));
            }else {
                holder.row_visit.setText("Done");
                holder.row_visit.setTextColor(Color.parseColor("#0009ff"));

            }


            holder.row_non_visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    retailerUpdateFragment.showRetailerUpdateDialog(routeList.get(position).getRetailer_id(),routeList.get(position).getRetailerMobileNo(),routeList.get(position).getRetailer_name());
                    retailerUpdateFragment.getLatLon();

                    retailerUpdateFragment.getData(routeList.get(position).getRetailer_id());
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
        public TextView row_retailer_name,row_order,row_visit,row_non_visit,row_statis;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_retailer_name = (TextView) itemView.findViewById(R.id.row_retailer_name);
            row_order = (TextView) itemView.findViewById(R.id.row_order);
            row_visit = (TextView) itemView.findViewById(R.id.row_visit);
            row_non_visit = (TextView) itemView.findViewById(R.id.row_non_visit);
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
