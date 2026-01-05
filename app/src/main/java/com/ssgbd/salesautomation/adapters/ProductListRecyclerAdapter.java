package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.visit.OrderActivity;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class ProductListRecyclerAdapter extends RecyclerView.Adapter<ProductListRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<ProductDTO> routeList;
    public ArrayList<ProductDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public ProductListRecyclerAdapter(ArrayList<ProductDTO> items, Context context) {
        this.routeList = items;
        this.context = context;

        this.arrayList = new ArrayList<ProductDTO>();
        this.arrayList.addAll(routeList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View view = inflater.from(context).inflate(R.layout.row_product,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final ProductDTO itemFeed = routeList.get(position);

        try {

            holder.row_sl_no.setText(String.valueOf(position+1)+".");
            holder.row_product_name.setText(itemFeed.getProduct_name()+" ("+itemFeed.getProduct_sap_code()+")");
//            holder.row_product_select.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((OrderActivity)context).showAddProduct(itemFeed.getProduct_name());
//
//                }
//            });
//            holder.card_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((OrderActivity)context).showAddProduct(itemFeed.getProduct_name());
//
//                }
//            });


        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_product_name,row_sl_no;
        Button row_product_select;
        CardView card_view;
       // public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_product_name = (TextView) itemView.findViewById(R.id.row_product_name);
            row_sl_no = (TextView) itemView.findViewById(R.id.row_sl_no);
            row_product_select = (Button) itemView.findViewById(R.id.row_product_select);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
           // linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        routeList.clear();
        if (charText.length() == 0) {
            routeList.addAll(arrayList);
        } else {
            for (ProductDTO hm : arrayList) {
                if (hm.getProduct_name().toLowerCase().contains(charText)) {
                    routeList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
