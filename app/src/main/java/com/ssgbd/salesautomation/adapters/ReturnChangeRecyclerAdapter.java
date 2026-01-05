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
import com.ssgbd.salesautomation.dtos.OrderDTO;
import com.ssgbd.salesautomation.dtos.ReturnChangeDTO;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class ReturnChangeRecyclerAdapter extends RecyclerView.Adapter<ReturnChangeRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<ReturnChangeDTO> returnChangeDTOS;
    public ArrayList<ReturnChangeDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public ReturnChangeRecyclerAdapter(ArrayList<ReturnChangeDTO> items, Context context) {
        this.returnChangeDTOS = items;
        this.context = context;

        this.arrayList = new ArrayList<ReturnChangeDTO>();
        this.arrayList.addAll(returnChangeDTOS);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View view = inflater.from(context).inflate(R.layout.row_return_change,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final ReturnChangeDTO itemFeed = returnChangeDTOS.get(position);

        try {


//            int f = position+1;
//            holder.row_txt_sl_no.setText(String.valueOf(f));

            holder.row_product_category.setText(itemFeed.getReturnCategoryName());
            holder.row_product_name.setText(itemFeed.getReturnProductName());
            holder.row_product_quantity.setText(itemFeed.getReturnQuantity());
            holder.row_product_price.setText(itemFeed.getReturnValue());

            holder.row_product_category_change.setText(itemFeed.getChangeCategoryName());
            holder.row_product_name_change.setText(itemFeed.getChangeProductName());
            holder.row_product_quantity_change.setText(itemFeed.getChangeQuantity());
            holder.row_product_price_change.setText(itemFeed.getChangeValue());



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
        return returnChangeDTOS.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_product_category,row_product_name,row_product_quantity,row_product_price;
         public TextView row_product_category_change,row_product_name_change,row_product_quantity_change,row_product_price_change;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_product_category = (TextView) itemView.findViewById(R.id.row_product_category);
            row_product_name = (TextView) itemView.findViewById(R.id.row_product_name);
            row_product_quantity = (TextView) itemView.findViewById(R.id.row_product_quantity);
            row_product_price = (TextView) itemView.findViewById(R.id.row_product_price);

            row_product_category_change = (TextView) itemView.findViewById(R.id.row_product_category_change);
            row_product_name_change = (TextView) itemView.findViewById(R.id.row_product_name_change);
            row_product_quantity_change = (TextView) itemView.findViewById(R.id.row_product_quantity_change);
            row_product_price_change = (TextView) itemView.findViewById(R.id.row_product_price_change);

            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        returnChangeDTOS.clear();
        if (charText.length() == 0) {
            returnChangeDTOS.addAll(arrayList);
        } else {
            for (ReturnChangeDTO hm : arrayList) {
                if (hm.getReturnProductName().toLowerCase().contains(charText)) {
                    returnChangeDTOS.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
