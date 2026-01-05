package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.ReturnAndChangeFragment;
import com.ssgbd.salesautomation.drawer.fragment.UtilityFragment;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.UtilityListDTO;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class UtilityListRecyclerAdapter extends RecyclerView.Adapter<UtilityListRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<UtilityListDTO> utilityLists;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;

    public UtilityListRecyclerAdapter(ArrayList<UtilityListDTO> items, Context context) {
        this.utilityLists = items;
        this.context = context;
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_utility_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final UtilityListDTO itemFeed = utilityLists.get(position);

        try {

            holder.row_date.setText(itemFeed.getDate());
            holder.row_type.setText(itemFeed.getType());
            holder.row_reason.setText(itemFeed.getReason());
            holder.row_suggestion.setText(itemFeed.getRemarks());



        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return utilityLists.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_date,row_type,row_reason,row_suggestion;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_date = (TextView) itemView.findViewById(R.id.row_date);
            row_type = (TextView) itemView.findViewById(R.id.row_type);
            row_reason = (TextView) itemView.findViewById(R.id.row_reason);
            row_suggestion = (TextView) itemView.findViewById(R.id.row_suggestion);


        }
    }


}
