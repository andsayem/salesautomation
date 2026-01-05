package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.ReturnChangeReportListDTO;
import com.ssgbd.salesautomation.report.order.OrderReportFragment;
import com.ssgbd.salesautomation.report.order.PGWiseReportFragment;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class PGWiseReportListAdapter extends RecyclerView.Adapter<PGWiseReportListAdapter.NewReleasesItemViewHolder> {

    public ArrayList<ReturnChangeReportListDTO> orderList;
    public ArrayList<ReturnChangeReportListDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    PGWiseReportFragment todaysOrderFragment;


    public PGWiseReportListAdapter(ArrayList<ReturnChangeReportListDTO> items, Context context, PGWiseReportFragment tof) {
        this.orderList = items;
        this.context = context;
        this.todaysOrderFragment = tof;

        this.arrayList = new ArrayList<ReturnChangeReportListDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_order_vs_delivery_report_list, parent, false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final ReturnChangeReportListDTO itemFeed = orderList.get(position);

        try {

            holder.row_return_order.setText(itemFeed.getReturnOrder());
            holder.row_return_order_date.setText(itemFeed.getReturnOrderDate());
            holder.row_fo.setText(itemFeed.getFo());
            holder.row_customer.setText(itemFeed.getCustomer());
            holder.row_qty.setText(itemFeed.getQty());
            holder.row_value.setText(itemFeed.getValue());

            holder.linlay_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Intent intent = new Intent(context, ReturnChangeReportWeb.class);
//
//                    intent.putExtra("orderId", itemFeed.getReturnOrderId());
//                    context.startActivity(intent);
                }
            });



        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder {
        public TextView row_return_order,row_return_order_date,row_fo,row_customer,row_qty,row_value;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_return_order = (TextView) itemView.findViewById(R.id.row_return_order);
            row_return_order_date = (TextView) itemView.findViewById(R.id.row_return_order_date);
            row_fo = (TextView) itemView.findViewById(R.id.row_fo);
            row_customer = (TextView) itemView.findViewById(R.id.row_customer);
            row_qty = (TextView) itemView.findViewById(R.id.row_qty);
            row_value = (TextView) itemView.findViewById(R.id.row_value);

            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (ReturnChangeReportListDTO hm : arrayList) {
                if (hm.getCustomer().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }



}
