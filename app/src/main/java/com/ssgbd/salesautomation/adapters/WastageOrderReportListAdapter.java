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
import com.ssgbd.salesautomation.dtos.OrderRepotListDTO;
import com.ssgbd.salesautomation.report.order.OrderReportFragment;
import com.ssgbd.salesautomation.report.order.ReportWeb;
import com.ssgbd.salesautomation.utils.SharePreference;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class WastageOrderReportListAdapter extends RecyclerView.Adapter<WastageOrderReportListAdapter.NewReleasesItemViewHolder> {

    public ArrayList<OrderRepotListDTO> orderList;
    public ArrayList<OrderRepotListDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public WastageOrderReportListAdapter(ArrayList<OrderRepotListDTO> items, Context context) {
        this.orderList = items;
        this.context = context;


        this.arrayList = new ArrayList<OrderRepotListDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_order_report_list, parent, false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final OrderRepotListDTO itemFeed = orderList.get(position);

        try {

            holder.row_sl_no.setText(String.valueOf(position+1));
            holder.row_order_no.setText(itemFeed.getOrderNO());
            holder.row_order_date_time.setText(itemFeed.getOrderDateTime());
            holder.row_txt_customer.setText(itemFeed.getCustomerName());
            holder.row_order_qty.setText(itemFeed.getOrderQty());
            holder.row_orver_value.setText(itemFeed.getOrderValue());

            holder.row_order_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReportWeb.class);
                    String url="apps-wastage-order-details?order_id="+itemFeed.getOrderId()+"&username="+SharePreference.getUserLoginId(context)+"&password="+SharePreference.getUserLoginPassword(context);
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                  //  http://ssforceuat.ssgbd.com/apps-wastage-order-details?order_id=2&username=1769&password=12345
                }
            });

            holder.linlay_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
        public TextView row_sl_no,row_order_no, row_order_date_time, row_txt_customer,row_order_qty,row_orver_value;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_sl_no = (TextView) itemView.findViewById(R.id.row_sl_no);
            row_order_no = (TextView) itemView.findViewById(R.id.row_order_no);
            row_order_date_time = (TextView) itemView.findViewById(R.id.row_order_date_time);
            row_txt_customer = (TextView) itemView.findViewById(R.id.row_txt_customer);
            row_order_qty = (TextView) itemView.findViewById(R.id.row_order_qty);
            row_orver_value = (TextView) itemView.findViewById(R.id.row_orver_value);

            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (OrderRepotListDTO hm : arrayList) {
                if (hm.getOrderNO().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }



}
