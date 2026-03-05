package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.ProductRequisitionDTO;
import com.ssgbd.salesautomation.dtos.ReasonDTO;

import java.util.List;

public class ProductRequisitionAdapter extends RecyclerView.Adapter<ProductRequisitionAdapter.ViewHolder> {

    private List<ProductRequisitionDTO> productList;
    private List<ReasonDTO> reasonList;
    private Context context;

    public ProductRequisitionAdapter(List<ProductRequisitionDTO> productList, List<ReasonDTO> reasonList, Context context) {
        this.productList = productList;
        this.reasonList = reasonList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductRequisitionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_requisition_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRequisitionAdapter.ViewHolder holder, int position) {
        ProductRequisitionDTO dto = productList.get(position);

        holder.txtSl.setText(String.valueOf(position + 1));
        holder.txtPointName.setText(dto.getPointName());
        holder.txtSapCode.setText(dto.getSapCode());
        holder.txtPgName.setText(dto.getCatName()); // API অনুযায়ী catName দেখানো
        holder.txtProductCode.setText(dto.getProductCode() != null ? dto.getProductCode() : "");
        holder.txtProductName.setText(dto.getProductName());
        holder.txtTargetQty.setText(String.valueOf(dto.getTargetQty()));
        holder.txtTargetValue.setText(String.valueOf(dto.getTargetValue()));
        holder.txtReqQty.setText(String.valueOf(dto.getReqQty()));
        holder.txtReqValue.setText(String.valueOf(dto.getReqValue()));
        holder.txtBilledQty.setText(String.valueOf(dto.getBilledQty()));
        holder.txtBilledValue.setText(String.valueOf(dto.getBilledValue()));

        // ==========================
        // Dynamic Reasons
        // ==========================
        holder.reasonContainer.removeAllViews(); // Clear previous views

        if (reasonList != null && !reasonList.isEmpty()) {
            for (ReasonDTO r : reasonList) {
                TextView tv = new TextView(context);
                tv.setTextSize(14);
                tv.setPadding(8, 8, 8, 8);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Check if current product has deduction for this reason
                int deductionQty = dto.getReasonQty(r.getId()); // Method in ProductRequisitionDTO
                tv.setText(String.valueOf(deductionQty));

                holder.reasonContainer.addView(tv);
            }
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSl, txtPointName, txtSapCode, txtPgName, txtProductCode, txtProductName;
        TextView txtTargetQty, txtTargetValue, txtReqQty, txtReqValue, txtBilledQty, txtBilledValue;
        LinearLayout reasonContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSl = itemView.findViewById(R.id.txt_sl);
            txtPointName = itemView.findViewById(R.id.txt_point_name);
            txtSapCode = itemView.findViewById(R.id.txt_sap_code);
            txtPgName = itemView.findViewById(R.id.txt_pg_name);
            txtProductCode = itemView.findViewById(R.id.txt_product_code);
            txtProductName = itemView.findViewById(R.id.txt_product_name);
            txtTargetQty = itemView.findViewById(R.id.txt_target_qty);
            txtTargetValue = itemView.findViewById(R.id.txt_target_value);
            txtReqQty = itemView.findViewById(R.id.txt_req_qty);
            txtReqValue = itemView.findViewById(R.id.txt_req_value);
            txtBilledQty = itemView.findViewById(R.id.txt_billed_qty);
            txtBilledValue = itemView.findViewById(R.id.txt_billed_value);
            reasonContainer = itemView.findViewById(R.id.reason_container);
        }
    }
}