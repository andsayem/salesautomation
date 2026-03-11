package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.DeportPaymentDTO;

import java.util.ArrayList;

public class DeportPaymentReportAdapter extends RecyclerView.Adapter<DeportPaymentReportAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DeportPaymentDTO> list;

    public DeportPaymentReportAdapter(ArrayList<DeportPaymentDTO> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_depot_payment_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DeportPaymentDTO item = list.get(position);

        holder.txtPoint.setText(item.getPointName());
        holder.txtSAP.setText(String.valueOf(item.getSapCode()));
        holder.txtCollectionType.setText(item.getCollectionType());
        holder.txtAmount.setText(String.valueOf(item.getAmount()));
        holder.txtPaymentType.setText(item.getPaymentType());
        holder.txtDate.setText(item.getDate());
        holder.txtAckStatus.setText(item.getAckStatus());
        holder.txtBankSlip.setText("View Slip");


        // Set clickable bank slip text
        holder.txtBankSlip.setOnClickListener(v -> {
            String url = item.getBankSlipUrl();
            Log.d("BankSlipURL", item.getBankSlipUrl());
            if(url != null && !url.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPoint, txtSAP, txtCollectionType,
                txtAmount, txtPaymentType, txtDate,
                txtAckStatus, txtBankSlip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPoint = itemView.findViewById(R.id.txt_point);
            txtSAP = itemView.findViewById(R.id.txt_sap);
            txtCollectionType = itemView.findViewById(R.id.txt_collection_type);
            txtAmount = itemView.findViewById(R.id.txt_amount);
            txtPaymentType = itemView.findViewById(R.id.txt_payment_type);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtAckStatus = itemView.findViewById(R.id.txt_ack_status);
            txtBankSlip = itemView.findViewById(R.id.txt_bank_slip);
        }
    }
}