package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.CollectionListDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class AdapterCollectionList extends RecyclerView.Adapter<AdapterCollectionList.NewReleasesItemViewHolder>{

    public ArrayList<CollectionListDTO> routeList;
    public ArrayList<CollectionListDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public AdapterCollectionList(ArrayList<CollectionListDTO> items, Context context) {
        this.routeList = items;
        this.context = context;
        this.arrayList = new ArrayList<CollectionListDTO>();
        this.arrayList.addAll(routeList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_collection_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final CollectionListDTO itemFeed = routeList.get(position);

        try {

            holder.route_name.setText("Route : "+itemFeed.getRoute_name());
            holder.retailer_name.setText("Retailer : "+itemFeed.getRetailer_name()+"("+itemFeed.getRetailer_id()+")");
            holder.txt_amount.setText("Amount : "+itemFeed.getCollection_amount());
            holder.txt_status.setText("Status : "+itemFeed.getStatus());
            holder.txt_collection_type.setText("Collection Type : "+itemFeed.getCollectionType());


        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView route_name,retailer_name,txt_amount,txt_status,txt_collection_type;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            route_name = (TextView) itemView.findViewById(R.id.route_name);
            retailer_name = (TextView) itemView.findViewById(R.id.retailer_name);
            txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            txt_collection_type = (TextView) itemView.findViewById(R.id.txt_collection_type);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        routeList.clear();
        if (charText.length() == 0) {
            routeList.addAll(arrayList);
        } else {
            for (CollectionListDTO hm : arrayList) {
                if (hm.getOrder_id().toLowerCase().contains(charText)) {
                    routeList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
