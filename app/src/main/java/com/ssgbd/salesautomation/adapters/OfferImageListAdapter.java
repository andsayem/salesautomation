package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.NoticeDTO;
import com.ssgbd.salesautomation.dtos.OfferImageDTO;

import java.util.ArrayList;

/**
 * Created by Rashed on 26/4/2017.
 */
public class OfferImageListAdapter extends RecyclerView.Adapter<OfferImageListAdapter.NewReleasesItemViewHolder>{

    public ArrayList<OfferImageDTO> stockListDTOS;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;

    public OfferImageListAdapter(ArrayList<OfferImageDTO> items, Context context) {
        this.stockListDTOS = items;
        this.context = context;
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_offer_image_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final OfferImageDTO itemFeed = stockListDTOS.get(position);

        try {
            holder.row_notice.setText(itemFeed.getImageTitle());

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return stockListDTOS.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_notice;
        public ImageView row_image;
        Button row_product_select;

        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_notice = (TextView) itemView.findViewById(R.id.row_notice);
            row_image = (ImageView) itemView.findViewById(R.id.row_image);

        }
    }
}
