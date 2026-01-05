package com.ssgbd.salesautomation.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.OrderVisitStatusOfflineDataFragment;
import com.ssgbd.salesautomation.drawer.fragment.TodaysOrderFragment;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.dtos.OrderVisitStatusDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class OrderVisitOfflineListRecyclerAdapter extends RecyclerView.Adapter<OrderVisitOfflineListRecyclerAdapter.NewReleasesItemViewHolder> {

    public ArrayList<OrderVisitStatusDTO> orderList;
    public ArrayList<OrderVisitStatusDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    OrderVisitStatusOfflineDataFragment todaysOrderFragment;


    public OrderVisitOfflineListRecyclerAdapter(ArrayList<OrderVisitStatusDTO> items, Context context, OrderVisitStatusOfflineDataFragment tof) {
        this.orderList = items;
        this.context = context;
        this.todaysOrderFragment = tof;

        this.arrayList = new ArrayList<OrderVisitStatusDTO>();
        this.arrayList.addAll(orderList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_todays_order_visit_status, parent, false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final OrderVisitStatusDTO itemFeed = orderList.get(position);

        try {

            holder.row_route_name.setText(itemFeed.getRetailerName());
            holder.row_retailer_name.setText(itemFeed.getStatus());




            holder.row_txt_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!isInternetAvailable(context)) {
                        internetAlert(context);

                    } else {
                      //  todaysOrderFragment.confirmOrder(itemFeed.getOrderData(), itemFeed.getRouteId(), itemFeed.getRetailerId(), itemFeed.getPoientId());

                     //   Log.e("row_txt_confirm",itemFeed.getStatusData()+"row_txt_confirm");
                        todaysOrderFragment.submitVisit(itemFeed.getStatusData(),itemFeed.getRetailerId());
                    }
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
        public TextView row_route_name, row_retailer_name, row_txt_confirm, row_txt_delete, row_txt_edit;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_route_name = (TextView) itemView.findViewById(R.id.row_route_name);
            row_retailer_name = (TextView) itemView.findViewById(R.id.row_retailer_name);
            row_txt_delete = (TextView) itemView.findViewById(R.id.row_txt_delete);
            row_txt_confirm = (TextView) itemView.findViewById(R.id.row_txt_confirm);
            row_txt_edit = (TextView) itemView.findViewById(R.id.row_txt_edit);

            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        orderList.clear();
        if (charText.length() == 0) {
            orderList.addAll(arrayList);
        } else {
            for (OrderVisitStatusDTO hm : arrayList) {
                if (hm.getRetailerName().toLowerCase().contains(charText)) {
                    orderList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void internetAlert(final Context context) {
        final AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("Internet Alert").setMessage("Your device is not connected to internet. Connect to internet and try again.");

        alertBulder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        alertBulder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertBulder.setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertBulder.show();
    }
    public  boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
