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
import com.ssgbd.salesautomation.dtos.VisitReportDTO;
import com.ssgbd.salesautomation.report.AttendanceReportFragment;
import com.ssgbd.salesautomation.report.VisitReportFragment;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class VisitReportListAdapter extends RecyclerView.Adapter<VisitReportListAdapter.NewReleasesItemViewHolder> {

    public ArrayList<VisitReportDTO> orderList;
    public ArrayList<VisitReportDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    VisitReportFragment todaysOrderFragment;


    public VisitReportListAdapter(ArrayList<VisitReportDTO> items, Context context, VisitReportFragment tof) {
        this.orderList = items;
        this.context = context;
        this.todaysOrderFragment = tof;
        this.arrayList = new ArrayList<VisitReportDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_visit_report_list, parent, false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final VisitReportDTO itemFeed = orderList.get(position);

        try {

            holder.row_sl_no.setText(String.valueOf(position+1));
            holder.row_txt_retailer.setText(itemFeed.getRetailerName());
            holder.row_route.setText(itemFeed.getRouteName());
            holder.row_date.setText(itemFeed.getDate());

            if (itemFeed.getStatus().equalsIgnoreCase("3")){
                holder.row_status.setText("Order");
            }else if (itemFeed.getStatus().equalsIgnoreCase("2")){
                holder.row_status.setText("Visit");

            }else if (itemFeed.getStatus().equalsIgnoreCase("1")){
                holder.row_status.setText("Non-Visit");
            }

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
        public TextView row_sl_no,row_date,row_route,row_txt_retailer,row_status;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_sl_no = (TextView) itemView.findViewById(R.id.row_sl_no);
            row_route = (TextView) itemView.findViewById(R.id.row_route);
            row_txt_retailer = (TextView) itemView.findViewById(R.id.row_txt_retailer);
            row_date = (TextView) itemView.findViewById(R.id.row_date);
            row_status = (TextView) itemView.findViewById(R.id.row_status);

            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (VisitReportDTO hm : arrayList) {
                if (hm.getFoName().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
