package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.AttendanceReportListDTO;
import com.ssgbd.salesautomation.report.AttendanceReportFragment;
import com.ssgbd.salesautomation.report.AttendanceReportFragmentSsforce;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class AttendanceReportListAdapterSsforce extends RecyclerView.Adapter<AttendanceReportListAdapterSsforce.NewReleasesItemViewHolder> {

    public ArrayList<AttendanceReportListDTO> orderList;
    public ArrayList<AttendanceReportListDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    AttendanceReportFragmentSsforce todaysOrderFragment;


    public AttendanceReportListAdapterSsforce(ArrayList<AttendanceReportListDTO> items, Context context, AttendanceReportFragmentSsforce tof) {
        this.orderList = items;
        this.context = context;
        this.todaysOrderFragment = tof;

        this.arrayList = new ArrayList<AttendanceReportListDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_attendance_report_list_ssforce, parent, false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final AttendanceReportListDTO itemFeed = orderList.get(position);

        try {

          //  holder.row_sl.setText(String.valueOf(position+1));
            holder.row_date.setText(itemFeed.getDate());
            holder.row_route.setText(itemFeed.getRoute());
            holder.row_time.setText(itemFeed.getInTime());
            if (itemFeed.getFriday().equalsIgnoreCase("1")){
                holder.row_retailername.setText("Friday");
            }else if(itemFeed.getLeave().equalsIgnoreCase("1")) {
                holder.row_retailername.setText(itemFeed.getLeaveName());
            }else if(itemFeed.getAbsent().equalsIgnoreCase("1")) {
                holder.row_retailername.setText("Absent");
            }else {
                holder.row_retailername.setText(itemFeed.getInTimeRetailerName());
            }

            holder.row_retaileraddress.setText(itemFeed.getInTimeRetailerAddress());


            holder.row_timeo.setText(itemFeed.getOutTime());
            holder.row_retailernameo.setText(itemFeed.getWorkingHoure());
            holder.row_retaileraddresso.setText(itemFeed.getOutTimeRetailerAddress());
          // holder.row_workinghour.setText(itemFeed.getFriday());

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
        public TextView row_route,row_date,row_time,row_retailername,row_retaileraddress,row_timeo,row_retailernameo,row_retaileraddresso,row_workinghour;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
          //  row_sl = (TextView) itemView.findViewById(R.id.row_sl);
            row_date = (TextView) itemView.findViewById(R.id.row_date);
            row_route = (TextView) itemView.findViewById(R.id.row_route);
            row_time = (TextView) itemView.findViewById(R.id.row_time);
            row_retailername = (TextView) itemView.findViewById(R.id.row_retailername);
            row_retaileraddress = (TextView) itemView.findViewById(R.id.row_retaileraddress);


            row_timeo = (TextView) itemView.findViewById(R.id.row_timeo);
            row_retailernameo = (TextView) itemView.findViewById(R.id.row_retailernameo);
            row_retaileraddresso = (TextView) itemView.findViewById(R.id.row_retaileraddresso);
            row_workinghour = (TextView) itemView.findViewById(R.id.row_workinghour);


            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (AttendanceReportListDTO hm : arrayList) {
                if (hm.getInTimeRetailerName().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }



}
