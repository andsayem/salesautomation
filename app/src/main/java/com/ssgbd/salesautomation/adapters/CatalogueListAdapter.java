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
import com.ssgbd.salesautomation.dtos.CatalogueDTO;
import com.ssgbd.salesautomation.dtos.OfferImageDTO;

import java.util.ArrayList;

/**
 * Created by Rashed on 26/4/2017.
 */
public class CatalogueListAdapter extends RecyclerView.Adapter<CatalogueListAdapter.NewReleasesItemViewHolder>{

    public ArrayList<CatalogueDTO> stockListDTOS;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;

    public CatalogueListAdapter(ArrayList<CatalogueDTO> items, Context context) {
        this.stockListDTOS = items;
        this.context = context;
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_catalogue_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final CatalogueDTO itemFeed = stockListDTOS.get(position);

        try {
            int i=position+1;
            holder.row_sl.setText(String.valueOf(i));
            holder.row_catalogue_name.setText(itemFeed.getBusiness_type());
            holder.row_channel.setText(itemFeed.getBusiness_type());


            if (itemFeed.getStatus().equalsIgnoreCase("0")){
                holder.row_Status.setText("Active");
            }else {
                holder.row_Status.setText("InActive");

            }

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return stockListDTOS.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_sl,row_catalogue_name,row_channel,row_Status;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_sl = (TextView) itemView.findViewById(R.id.row_sl);
            row_catalogue_name = (TextView) itemView.findViewById(R.id.row_catalogue_name);
            row_channel = (TextView) itemView.findViewById(R.id.row_channel);
            row_Status = (TextView) itemView.findViewById(R.id.row_Status);

        }
    }
}
