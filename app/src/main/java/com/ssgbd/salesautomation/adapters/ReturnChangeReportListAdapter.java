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
import com.ssgbd.salesautomation.dtos.ReturnChangeReportListDTO;
import com.ssgbd.salesautomation.returnchange.ReturnChangeOrderReportFragment;
import com.ssgbd.salesautomation.returnchange.ReturnChangeReportWeb;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class ReturnChangeReportListAdapter extends RecyclerView.Adapter<ReturnChangeReportListAdapter.NewReleasesItemViewHolder> {

    public ArrayList<ReturnChangeReportListDTO> orderList;
    public ArrayList<ReturnChangeReportListDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    ReturnChangeOrderReportFragment returnChangeOrderReportFragment;


    public ReturnChangeReportListAdapter(ArrayList<ReturnChangeReportListDTO> items, Context context,ReturnChangeOrderReportFragment returnChangeOrderReportFragment) {
        this.orderList = items;
        this.context = context;
        this.returnChangeOrderReportFragment= returnChangeOrderReportFragment;

        this.arrayList = new ArrayList<ReturnChangeReportListDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_return_change_report_list, parent, false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final ReturnChangeReportListDTO itemFeed = orderList.get(position);

        try {

            holder.row_sl.setText(String.valueOf(position+1));
            holder.row_return_order.setText(itemFeed.getReturnOrder());
            holder.row_return_order_date.setText(itemFeed.getReturnOrderDate());
            holder.row_fo.setText(itemFeed.getFo());
            holder.row_customer.setText(itemFeed.getCustomer());
            holder.row_return_qty.setText(itemFeed.getQty());
            holder.row_return_value.setText(itemFeed.getValue());

            holder.row_change_qty.setText(itemFeed.getChangeqty());
            holder.row_change_value.setText(itemFeed.getChangevalue());

            float i = Float.parseFloat(itemFeed.getValue());
            float ii = Float.parseFloat(itemFeed.getChangevalue());
            float f=ii-i;


            holder.row_excess_amount.setText(String.valueOf(f));


            holder.row_return_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ReturnChangeReportWeb.class);

                    intent.putExtra("orderId", itemFeed.getReturnOrderId());
                    context.startActivity(intent);
                }
            });

            returnChangeOrderReportFragment.excessValue(String.valueOf(holder.row_excess_amount.getText()));

        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder {
        public TextView row_sl,row_return_order,row_return_order_date,row_fo,row_customer,row_return_qty,row_return_value;
        public TextView row_change_qty,row_change_value,row_excess_amount;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_sl = (TextView) itemView.findViewById(R.id.row_sl);
            row_return_order = (TextView) itemView.findViewById(R.id.row_return_order);
            row_return_order_date = (TextView) itemView.findViewById(R.id.row_return_order_date);
            row_fo = (TextView) itemView.findViewById(R.id.row_fo);
            row_customer = (TextView) itemView.findViewById(R.id.row_customer);
            row_return_qty = (TextView) itemView.findViewById(R.id.row_return_qty);
            row_return_value = (TextView) itemView.findViewById(R.id.row_return_value);

            row_change_qty = (TextView) itemView.findViewById(R.id.row_change_qty);
            row_change_value = (TextView) itemView.findViewById(R.id.row_change_value);
            row_excess_amount = (TextView) itemView.findViewById(R.id.row_excess_amount);

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
