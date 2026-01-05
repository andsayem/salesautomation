package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.ProductStrengthDTO;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Rashed on 26/4/2017.
 */
public class ProductStrengthCategoryRecyclerAdapter extends RecyclerView.Adapter<ProductStrengthCategoryRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<ProductStrengthDTO> routeList;
    public ArrayList<ProductStrengthDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    private int lastPosition = -1;
    public ProductStrengthCategoryRecyclerAdapter(ArrayList<ProductStrengthDTO> items, Context context) {
        this.routeList = items;
        this.context = context;
        this.arrayList = new ArrayList<ProductStrengthDTO>();
        this.arrayList.addAll(routeList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               View view = inflater.from(context).inflate(R.layout.row_product_category_strenth,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final ProductStrengthDTO itemFeed = routeList.get(position);

        try {

            holder.txt_product_name.setText(itemFeed.getProductName());
            holder.category_strength.setText(Html.fromHtml(itemFeed.getProductFeature()));

            Picasso.with(context)
                    .load(context.getString(R.string.base_url)+"uploads/features/" + itemFeed.getProductImage())
                    .placeholder(R.mipmap.ssg_logo).into(holder.user_circleImageView);

                        holder.user_circleImageView.setAnimation(null);

            setAnimation(holder.itemView, position);


           // Log.e("Url>",context.getString(R.string.base_url)+"uploads/features/" + itemFeed.getProductImage()+"");

            holder.linlay_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }catch (Exception e){

        }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView category_name,txt_product_name,category_strength;
        public LinearLayout linlay_main;
        ImageView user_circleImageView;

        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            txt_product_name = (TextView) itemView.findViewById(R.id.txt_product_name);
            category_name = (TextView) itemView.findViewById(R.id.category_name);
            category_strength = (TextView) itemView.findViewById(R.id.category_strength);
            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);
            user_circleImageView = (ImageView) itemView.findViewById(R.id.user_circleImageView);


        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        routeList.clear();
        if (charText.length() == 0) {
            routeList.addAll(arrayList);
        } else {
            for (ProductStrengthDTO hm : arrayList) {
                if (hm.getCompanyName().toLowerCase().contains(charText)) {
                    routeList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
