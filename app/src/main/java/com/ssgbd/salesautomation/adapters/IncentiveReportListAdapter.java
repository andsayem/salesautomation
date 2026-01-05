package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.IncentiveReportDTO;
import com.ssgbd.salesautomation.dtos.OrderRepotListDTO;
import com.ssgbd.salesautomation.report.DeliveryVsOrderReportFragment;
import com.ssgbd.salesautomation.report.order.ReportWeb;
import com.ssgbd.salesautomation.utils.SharePreference;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class IncentiveReportListAdapter extends RecyclerView.Adapter<IncentiveReportListAdapter.NewReleasesItemViewHolder> {

    public ArrayList<IncentiveReportDTO> orderList;
    public ArrayList<IncentiveReportDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    RecyclerView retailer_list_recyclerView;

    public IncentiveReportListAdapter(ArrayList<IncentiveReportDTO> items, Context context) {
        this.orderList = items;
        this.context = context;

        this.arrayList = new ArrayList<IncentiveReportDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_incentive_report_list, parent, false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final IncentiveReportDTO itemFeed = orderList.get(position);

        try {
            DecimalFormat twoDForm = new DecimalFormat("#");
            holder.row_sl_no.setText(String.valueOf(position+1));
            holder.row_pg_group.setText(itemFeed.getGroup_name());
            holder.row_pg_name.setText(itemFeed.getPg_name());
            holder.row_target_qty.setText(itemFeed.getTarget_qty());
            holder.row_ims_qty.setText(itemFeed.getImsQty());
            holder.row_qty_percentage.setText(itemFeed.getQty_per()+"%");
            holder.row_ims_contrebution.setText(itemFeed.getIms_contr()+"%");
//            float getOrderValue = Float.parseFloat(itemFeed.getOrderValue());
            holder.row_collection_qty.setText(itemFeed.getCollection_qty());
//
            holder.row_pcs_achiv_percent.setText(itemFeed.getPcs_achv_perc()+"%");
//
            holder.row_group_achiv_percent.setText(itemFeed.getGroup_achv_perc()+"%");
            holder.row_pg_incentive.setText(itemFeed.getIncentive_amount());
            holder.row_group_incentive.setText(itemFeed.getGroup_amount());
            holder.row_total_amount.setText(itemFeed.getTotal_amount());
//
//            float getDeliveryValue = Float.parseFloat(itemFeed.getDeliveryValue());
//            holder.row_orver_delivery_value.setText(String.valueOf(Math.round(Integer.valueOf(twoDForm.format(getDeliveryValue)))));
//
//            float getFree = Float.parseFloat(itemFeed.getFree());
//            holder.row_orver_free.setText(String.valueOf(Math.round(Integer.valueOf(twoDForm.format(getFree)))));
//
//            float getDiscount = Float.parseFloat(itemFeed.getDiscount());
//            holder.row_orver_Discount.setText(String.valueOf(Math.round(Integer.valueOf(twoDForm.format(getDiscount)))));


       //    String.valueOf(Math.round(Float.valueOf(twoDForm.format(itemFeed.getDeliveryValue()))));




        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder {
        public TextView row_sl_no,row_pg_group,row_pg_name,row_target_qty, row_ims_qty, row_qty_percentage,row_ims_contrebution,
                row_collection_qty,row_pcs_achiv_percent,row_group_achiv_percent,row_pg_incentive,row_group_incentive,row_total_amount;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_sl_no = (TextView) itemView.findViewById(R.id.row_sl_no);
            row_pg_group = (TextView) itemView.findViewById(R.id.row_pg_group);
            row_pg_name = (TextView) itemView.findViewById(R.id.row_pg_name);
            row_target_qty = (TextView) itemView.findViewById(R.id.row_target_qty);
            row_ims_qty = (TextView) itemView.findViewById(R.id.row_ims_qty);
            row_qty_percentage = (TextView) itemView.findViewById(R.id.row_qty_percentage);
            row_ims_contrebution = (TextView) itemView.findViewById(R.id.row_ims_contrebution);
            row_collection_qty = (TextView) itemView.findViewById(R.id.row_collection_qty);
            row_pcs_achiv_percent = (TextView) itemView.findViewById(R.id.row_pcs_achiv_percent);
            row_group_achiv_percent = (TextView) itemView.findViewById(R.id.row_group_achiv_percent);
            row_pg_incentive = (TextView) itemView.findViewById(R.id.row_pg_incentive);
            row_group_incentive = (TextView) itemView.findViewById(R.id.row_group_incentive);

            row_total_amount = (TextView) itemView.findViewById(R.id.row_total_amount);

            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (IncentiveReportDTO hm : arrayList) {
                if (hm.getImsQty().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }



}
