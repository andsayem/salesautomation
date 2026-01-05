package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class ProductCategoryRecyclerAdapter extends RecyclerView.Adapter<ProductCategoryRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<CategoryDTO> categoryLists;
    public ArrayList<CategoryDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public ProductCategoryRecyclerAdapter(ArrayList<CategoryDTO> items, Context context) {
        this.categoryLists = items;
        this.context = context;

        this.arrayList = new ArrayList<CategoryDTO>();
        this.arrayList.addAll(categoryLists);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View view = inflater.from(context).inflate(R.layout.row_product_category,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final CategoryDTO itemFeed = categoryLists.get(position);

        try {

            holder.category_name.setText(itemFeed.getName());
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
        public TextView category_name,category_strength;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            category_name = (TextView) itemView.findViewById(R.id.category_name);
            category_strength = (TextView) itemView.findViewById(R.id.category_strength);
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
            for (CategoryDTO hm : arrayList) {
                if (hm.getName().toLowerCase().contains(charText)) {
                    categoryLists.add(hm);
                    notifyDataSetChanged();
                }
            }
        }
        notifyDataSetChanged();
    }
}
