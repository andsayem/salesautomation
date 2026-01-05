package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.IMSFOActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.VisitFragment;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class RetailerRecyclerAdapter_ims extends RecyclerView.Adapter<RetailerRecyclerAdapter_ims.NewReleasesItemViewHolder>{

    public ArrayList<RetailerDTO> routeList;
    public ArrayList<RetailerDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public RetailerRecyclerAdapter_ims(ArrayList<RetailerDTO> items, Context context) {
        this.routeList = items;
        this.context = context;
        this.arrayList = new ArrayList<RetailerDTO>();
        this.arrayList.addAll(routeList);
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_retailer,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final RetailerDTO itemFeed = routeList.get(position);

    //    Log.e(">>>>",holder.getAdapterPosition()+"");

        try {


            holder.row_retailer_name.setText(itemFeed.getRetailer_name()+" "+"("+itemFeed.getRetailer_id()+")");
            holder.row_statis.setText(itemFeed.getStatus());
            holder.row_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "Order", Toast.LENGTH_SHORT).show();

                }
            });

            holder.row_visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (itemFeed.getStatus().equalsIgnoreCase("Visit")){

                        Toast.makeText(context, "Already visited", Toast.LENGTH_SHORT).show();

                        return;
                    }if (itemFeed.getStatus().equalsIgnoreCase("Ordered")){

                        Toast.makeText(context, "Already Ordered", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    ((IMSFOActivity)context).showVisitDialog();

                    Utility.ROUTE_ID = itemFeed.getRouteId();
                    Utility.V_RETAILER_ID = itemFeed.getRouteId();
                    Utility.V_RETAILER_NAME = itemFeed.getRetailer_name();
                    ((IMSFOActivity)context).getLatLon();
                    ((IMSFOActivity)context).getLatLon();



                }
            });

            holder.row_non_visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemFeed.getStatus().equalsIgnoreCase("Visit")){

                        Toast.makeText(context, "Already visited", Toast.LENGTH_SHORT).show();

                        return;
                    }if (itemFeed.getStatus().equalsIgnoreCase("Ordered")){

                        Toast.makeText(context, "Already Ordered", Toast.LENGTH_SHORT).show();

                        return;
                    } if (itemFeed.getStatus().equalsIgnoreCase("Non-Visit")){

                        Toast.makeText(context, "Already submitted", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    Utility.ROUTE_ID = itemFeed.getRouteId();
                    Utility.V_RETAILER_NAME = itemFeed.getRetailer_name();
                    ((IMSFOActivity)context).showNonVisitDialog();
                    ((IMSFOActivity)context).getLatLon();

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
