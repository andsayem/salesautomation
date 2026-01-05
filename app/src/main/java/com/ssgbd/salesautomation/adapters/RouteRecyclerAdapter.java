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
import com.ssgbd.salesautomation.dtos.RouteDTO;
import java.util.ArrayList;



/**
 * Created by Rashed on 26/4/2017.
 */
public class RouteRecyclerAdapter extends RecyclerView.Adapter<RouteRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<RouteDTO> routeList;
    public ArrayList<RouteDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;


    public RouteRecyclerAdapter(ArrayList<RouteDTO> items, Context context) {
        this.routeList = items;
        this.context = context;

        this.arrayList = new ArrayList<RouteDTO>();
        this.arrayList.addAll(routeList);

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_route,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final RouteDTO itemFeed = routeList.get(position);

        try {

            holder.route_name.setText(itemFeed.getRname());
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
        return routeList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView route_name;
        public LinearLayout linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            route_name = (TextView) itemView.findViewById(R.id.route_name);
            linlay_main = (LinearLayout) itemView.findViewById(R.id.linlay_main);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        routeList.clear();
        if (charText.length() == 0) {
            routeList.addAll(arrayList);
        } else {
            for (RouteDTO hm : arrayList) {
                if (hm.getRname().toLowerCase().contains(charText)) {
                    routeList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
