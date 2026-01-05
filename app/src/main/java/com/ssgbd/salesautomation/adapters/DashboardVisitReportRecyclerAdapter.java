package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.ConfirmOrderDTO;
import com.ssgbd.salesautomation.dtos.VisitReportDTO;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class DashboardVisitReportRecyclerAdapter extends RecyclerView.Adapter<DashboardVisitReportRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<VisitReportDTO> orderList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public DashboardVisitReportRecyclerAdapter(ArrayList<VisitReportDTO> items, Context context) {
        this.orderList = items;
        this.context = context;



    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View view = inflater.from(context).inflate(R.layout.row_visit_report_product,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final VisitReportDTO itemFeed = orderList.get(position);

      //  Log.e("<<adp>>",orderList.size()+"");
        try {

            int f = position+1;
            holder.row_txt_sl_no.setText(String.valueOf(f));
            holder.row_txt_route_name.setText(itemFeed.getRouteName());
            holder.row_retailer_name.setText(itemFeed.getRetailerName());
            holder.row_txt_date.setText(itemFeed.getDate());
            if (itemFeed.getStatus().equalsIgnoreCase("2")){
                holder.row_txt_status.setText("visit");
            }
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
        public TextView row_txt_sl_no,row_txt_route_name,row_retailer_name,row_txt_date,row_txt_status;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_txt_sl_no = (TextView) itemView.findViewById(R.id.row_txt_sl_no);
            row_txt_route_name = (TextView) itemView.findViewById(R.id.row_txt_route_name);
            row_retailer_name = (TextView) itemView.findViewById(R.id.row_retailer_name);
            row_txt_date = (TextView) itemView.findViewById(R.id.row_txt_date);
            row_txt_status = (TextView) itemView.findViewById(R.id.row_txt_status);
            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }


}
