package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.ProductRequisitionDTO;
import com.ssgbd.salesautomation.dtos.ReasonDTO;

import java.util.ArrayList;

public class ProductRequisitionAdapter
        extends RecyclerView.Adapter<ProductRequisitionAdapter.ViewHolder> {

    private ArrayList<ProductRequisitionDTO> list;
    private ArrayList<ReasonDTO> reasonList;
    private Context context;

    public ProductRequisitionAdapter(ArrayList<ProductRequisitionDTO> list,
                                     ArrayList<ReasonDTO> reasonList,
                                     Context context) {
        this.list = list;
        this.reasonList = reasonList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_product_requisition_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductRequisitionDTO dto = list.get(position);

        holder.txtProduct.setText(dto.getName());
        holder.txtSap.setText(dto.getSapCode());
        holder.txtReqQty.setText(dto.getReqQty());
        holder.txtReqValue.setText(dto.getReqValue());
        holder.txtBillingQty.setText(dto.getBillingQty());
        holder.txtBillingValue.setText(String.valueOf(dto.getBillingValue()));

        // ===============================
        // Dynamic Reason Columns
        // ===============================

        holder.reasonContainer.removeAllViews();

        for (int i = 0; i < reasonList.size(); i++) {

            TextView tv = new TextView(context);

            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    250,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            tv.setPadding(16, 16, 16, 16);
            tv.setTextSize(12);
            //tv.setBackgroundResource(R.drawable.table_cell_bg);

            // Default value 0 (you can customize if needed)
            tv.setText("0");

            holder.reasonContainer.addView(tv);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ===============================
    // ViewHolder
    // ===============================
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtProduct, txtSap, txtReqQty, txtReqValue,
                txtBillingQty, txtBillingValue;

        LinearLayout reasonContainer;

        public ViewHolder(View itemView) {
            super(itemView);

            txtProduct = itemView.findViewById(R.id.txt_product);
            txtSap = itemView.findViewById(R.id.txt_sap);
            txtReqQty = itemView.findViewById(R.id.txt_req_qty);
            txtReqValue = itemView.findViewById(R.id.txt_req_value);
            txtBillingQty = itemView.findViewById(R.id.txt_billing_qty);
            txtBillingValue = itemView.findViewById(R.id.txt_billing_value);

            reasonContainer = itemView.findViewById(R.id.reason_container);
        }
    }
}