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
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.DemandDTOs;
import com.ssgbd.salesautomation.dtos.ProductDTO;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class DemandRecyclerAdapter extends RecyclerView.Adapter<DemandRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<DemandDTOs> categoryLists;
    public ArrayList<DemandDTOs> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public DemandRecyclerAdapter(ArrayList<DemandDTOs> items, Context context) {
        this.categoryLists = items;
        this.context = context;

        this.arrayList = new ArrayList<DemandDTOs>();
        this.arrayList.addAll(categoryLists);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View view = inflater.from(context).inflate(R.layout.row_demand_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final DemandDTOs itemFeed = categoryLists.get(position);

        try {

            holder.category_name.setText("PG :"+itemFeed.getCategoryName());
            holder.product_name.setText("SKU :"+itemFeed.getProductName());
            holder.product_qty.setText("QTY :"+itemFeed.getProductQTY());
            holder.row_txt_demand.setText("NOTE :"+itemFeed.getProductDemandText());

            holder.linlay_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView category_name,product_name,product_qty,row_txt_demand;
        public LinearLayout linlay_main;

        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            category_name = (TextView) itemView.findViewById(R.id.category_name);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_qty = (TextView) itemView.findViewById(R.id.product_qty);
            row_txt_demand = (TextView) itemView.findViewById(R.id.row_txt_demand);
            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        categoryLists.clear();
        if (charText.length() == 0) {
            categoryLists.addAll(arrayList);
            notifyDataSetChanged();
        } else {
            for (DemandDTOs hm : arrayList) {
                if (hm.getCategoryName().toLowerCase().contains(charText)) {
                    categoryLists.add(hm);
                    notifyDataSetChanged();
                }
            }
        }
        notifyDataSetChanged();
    }
}
