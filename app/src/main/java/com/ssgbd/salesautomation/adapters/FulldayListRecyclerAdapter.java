package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.FoLeaveFragment;
import com.ssgbd.salesautomation.drawer.fragment.FullDayFragment;
import com.ssgbd.salesautomation.dtos.FOLeaveListDTO;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class FulldayListRecyclerAdapter extends RecyclerView.Adapter<FulldayListRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<FOLeaveListDTO> utilityLists;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    FullDayFragment fulldayFragment;
    String today;

    public FulldayListRecyclerAdapter(ArrayList<FOLeaveListDTO> items, Context context, FullDayFragment fl, String today) {
        this.utilityLists = items;
        this.context = context;
        this.fulldayFragment=fl;
        this.today = today;
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_fullday_leave_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final FOLeaveListDTO itemFeed = utilityLists.get(position);

        try {

            int s = position+1;

            holder.row_sl.setText(String.valueOf(s));
            holder.row_date.setText(itemFeed.getApplydate());
            holder.row_fromdate.setText(itemFeed.getFromdate());
            holder.row_reason.setText(itemFeed.getReason());
            holder.row_suggestion.setText(itemFeed.getRemarks());
            holder.row_note.setText(itemFeed.getStatus());

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return utilityLists.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_sl,row_date,row_fromdate,row_reason,row_suggestion,row_note;



        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_sl = (TextView) itemView.findViewById(R.id.row_sl);
            row_date = (TextView) itemView.findViewById(R.id.row_date);
            row_fromdate = (TextView) itemView.findViewById(R.id.row_fromdate);
            row_reason = (TextView) itemView.findViewById(R.id.row_reason);
            row_suggestion = (TextView) itemView.findViewById(R.id.row_suggestion);
            row_note = (TextView) itemView.findViewById(R.id.row_note);

        }
    }
}
