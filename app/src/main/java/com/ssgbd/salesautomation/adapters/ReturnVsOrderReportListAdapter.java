package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.OrderRepotListDTO;
import com.ssgbd.salesautomation.report.DeliveryVsOrderReportFragment;
import com.ssgbd.salesautomation.report.order.ReportWeb;
import com.ssgbd.salesautomation.returnpolicy.report.ReturnVsOrderReportFragment;
import com.ssgbd.salesautomation.utils.SharePreference;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class ReturnVsOrderReportListAdapter extends RecyclerView.Adapter<ReturnVsOrderReportListAdapter.NewReleasesItemViewHolder> {

    public ArrayList<OrderRepotListDTO> orderList;
    public ArrayList<OrderRepotListDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    ReturnVsOrderReportFragment todaysOrderFragment;

    public ReturnVsOrderReportListAdapter(ArrayList<OrderRepotListDTO> items, Context context, ReturnVsOrderReportFragment tof) {
        this.orderList = items;
        this.context = context;
        this.todaysOrderFragment = tof;
        this.arrayList = new ArrayList<OrderRepotListDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_delivery_vs_order_report_list, parent, false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final OrderRepotListDTO itemFeed = orderList.get(position);

        try {
            DecimalFormat twoDForm = new DecimalFormat("#");
            holder.row_sl_no.setText(String.valueOf(position+1));
            holder.row_order_no.setText(itemFeed.getOrderNO());
            holder.row_order_date_time.setText(itemFeed.getOrderDateTime());
            holder.row_txt_customer.setText(itemFeed.getCustomerName());
            holder.row_order_qty.setText(itemFeed.getOrderQty());
            float getOrderValue = Float.parseFloat(itemFeed.getOrderValue());
           // holder.row_orver_value.setText(itemFeed.getOrderValue());

            holder.row_orver_value.setText(String.valueOf(Math.round(Float.valueOf(twoDForm.format(getOrderValue)))));

            holder.row_orver_delivery_qty.setText(itemFeed.getDeliveryQty());

            float getDeliveryValue = Float.parseFloat(itemFeed.getDeliveryValue());
            holder.row_orver_delivery_value.setText(String.valueOf(Math.round(Integer.valueOf(twoDForm.format(getDeliveryValue)))));

            float getFree = Float.parseFloat(itemFeed.getFree());
            holder.row_orver_free.setText(String.valueOf(Math.round(Integer.valueOf(twoDForm.format(getFree)))));

            float getDiscount = Float.parseFloat(itemFeed.getDiscount());
            holder.row_orver_Discount.setText(String.valueOf(Math.round(Integer.valueOf(twoDForm.format(getDiscount)))));

       //   String.valueOf(Math.round(Float.valueOf(twoDForm.format(itemFeed.getDeliveryValue()))));

            holder.row_order_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ReportWeb.class);
                    String url="return-vs-received-details?order_id="+itemFeed.getOrderId()+"&username="+ SharePreference.getUserLoginId(context)+"&password="+SharePreference.getUserLoginPassword(context);
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                    Log.e("<<>>",url+"<<>>");

                  //  http://ssforceuat.ssgbd.com/order-vs-delivery-details?order_id=109695&username=1769&password=12345
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
        public TextView row_sl_no,row_order_no, row_order_date_time, row_txt_customer,row_order_qty,
                row_orver_value,row_orver_delivery_qty,row_orver_delivery_value,row_orver_free,row_orver_Discount;
        public LinearLayout linlay_main;

        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_sl_no = (TextView) itemView.findViewById(R.id.row_sl_no);
            row_order_no = (TextView) itemView.findViewById(R.id.row_order_no);
            row_order_date_time = (TextView) itemView.findViewById(R.id.row_order_date_time);
            row_txt_customer = (TextView) itemView.findViewById(R.id.row_txt_customer);
            row_order_qty = (TextView) itemView.findViewById(R.id.row_order_qty);
            row_orver_value = (TextView) itemView.findViewById(R.id.row_orver_value);
            row_orver_delivery_qty = (TextView) itemView.findViewById(R.id.row_orver_delivery_qty);
            row_orver_delivery_value = (TextView) itemView.findViewById(R.id.row_orver_delivery_value);
            row_orver_free = (TextView) itemView.findViewById(R.id.row_orver_free);
            row_orver_Discount = (TextView) itemView.findViewById(R.id.row_orver_Discount);

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
                if (hm.getOrderId().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
