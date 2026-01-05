package com.ssgbd.salesautomation.returnpolicy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.VisitFragment;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class RetailerRecyclerAdapter_RP extends RecyclerView.Adapter<RetailerRecyclerAdapter_RP.NewReleasesItemViewHolder>{

    public ArrayList<RetailerDTO> routeList;
    public ArrayList<RetailerDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    VisitFragment_RP visitFragment;

    public RetailerRecyclerAdapter_RP(ArrayList<RetailerDTO> items, Context context, VisitFragment_RP visitFragment) {
        this.routeList = items;
        this.context = context;
        this.visitFragment = visitFragment;
        this.arrayList = new ArrayList<RetailerDTO>();
        this.arrayList.addAll(routeList);
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_retailer_rp,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final RetailerDTO itemFeed = routeList.get(position);

        try {

            holder.row_retailer_name.setText(itemFeed.getRetailer_name()+" "+"("+itemFeed.getRetailer_id()+")");
            holder.row_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, OrderActivity_RP.class);
                    intent.putExtra("retailerId",itemFeed.getRetailer_id());
                    intent.putExtra("retailerName",itemFeed.getRetailer_name());
                    intent.putExtra("poient_id",itemFeed.getPoint_id());
                    intent.putExtra("from","visit");
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
        public TextView row_retailer_name,row_order;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_retailer_name = (TextView) itemView.findViewById(R.id.row_retailer_name);
            row_order = (TextView) itemView.findViewById(R.id.row_order);

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
