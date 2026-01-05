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
import com.ssgbd.salesautomation.report.retailerledger.RetailerLedgerFragment;
import com.ssgbd.salesautomation.dtos.RetailerLedgerDTO;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class RetailerRecyclerAdapter_RetailerLedger extends RecyclerView.Adapter<RetailerRecyclerAdapter_RetailerLedger.NewReleasesItemViewHolder>{

    public ArrayList<RetailerLedgerDTO> routeList;
    public ArrayList<RetailerLedgerDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    RetailerLedgerFragment visitFragment;

    public RetailerRecyclerAdapter_RetailerLedger(ArrayList<RetailerLedgerDTO> items, Context context, RetailerLedgerFragment visitFragment) {
        this.routeList = items;
        this.context = context;
        this.visitFragment = visitFragment;
        this.arrayList = new ArrayList<RetailerLedgerDTO>();
        this.arrayList.addAll(routeList);
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_retailer_ledger,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final RetailerLedgerDTO itemFeed = routeList.get(position);

        try {

//            holder.row_retailer_name.setText("Retailer :"+itemFeed.getRetailerId());
//            holder.row_opening_balance.setText("Opening Balance :"+itemFeed.getOpeningBalance());
//            holder.row_salse.setText("Sales(All Kinds of Sales) :"+itemFeed.getAllKindOfSales());
//            holder.row_collection.setText("Collection :"+itemFeed.getCollection());
//            holder.row_balance.setText("Balance (TK) :"+itemFeed.getBalance());

            int i= position;
            holder.row_retailer_sl.setText(String.valueOf(i+1));
            holder.row_retailer_name.setText(itemFeed.getRetailerId());
            holder.row_opening_balance.setText(itemFeed.getOpeningBalance());
            holder.row_salse.setText(itemFeed.getAllKindOfSales());
            holder.row_collection.setText(itemFeed.getCollection());
            holder.row_balance.setText(itemFeed.getBalance());


            holder.row_return_sales.setText(itemFeed.getReturnSales());
            holder.row_open_offer.setText(itemFeed.getOpenOffer());
            holder.row_monthly_commn.setText(itemFeed.getMonthlYComm());
            holder.row_value_comm.setText(itemFeed.getValueComm());
            holder.row_memo_comm.setText(itemFeed.getMemoCom());

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_retailer_sl,row_retailer_name,row_opening_balance,row_salse,row_collection,row_balance;
        public LinearLayout linlay_main;
        public TextView  row_return_sales,row_open_offer,row_monthly_commn,row_value_comm,row_memo_comm;

        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_retailer_sl = (TextView) itemView.findViewById(R.id.row_retailer_sl);
            row_retailer_name = (TextView) itemView.findViewById(R.id.row_retailer_name);
            row_opening_balance = (TextView) itemView.findViewById(R.id.row_opening_balance);
            row_salse = (TextView) itemView.findViewById(R.id.row_salse);
            row_collection = (TextView) itemView.findViewById(R.id.row_collection);
            row_balance = (TextView) itemView.findViewById(R.id.row_balance);

            row_return_sales = (TextView) itemView.findViewById(R.id.row_return_sales);
            row_open_offer = (TextView) itemView.findViewById(R.id.row_open_offer);
            row_monthly_commn = (TextView) itemView.findViewById(R.id.row_monthly_commn);
            row_value_comm = (TextView) itemView.findViewById(R.id.row_value_comm);
            row_memo_comm = (TextView) itemView.findViewById(R.id.row_memo_comm);

            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        routeList.clear();
        if (charText.length() == 0) {
            routeList.addAll(arrayList);
        } else {
            for (RetailerLedgerDTO hm : arrayList) {
                if (hm.getRetailerId().toLowerCase().contains(charText)) {
                    routeList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
