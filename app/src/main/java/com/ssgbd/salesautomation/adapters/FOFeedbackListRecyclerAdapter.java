package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.FoFeedbackFragment;
import com.ssgbd.salesautomation.drawer.fragment.FoLeaveFragment;
import com.ssgbd.salesautomation.dtos.FOLeaveListDTO;
import com.ssgbd.salesautomation.dtos.FoFeedbackDTO;
import com.ssgbd.salesautomation.utils.SharePreference;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class FOFeedbackListRecyclerAdapter extends RecyclerView.Adapter<FOFeedbackListRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<FoFeedbackDTO> utilityLists;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    FoFeedbackFragment foLeaveFragment;
    String today;

    public FOFeedbackListRecyclerAdapter(ArrayList<FoFeedbackDTO> items, Context context, FoFeedbackFragment fl, String today) {
        this.utilityLists = items;
        this.context = context;
        this.foLeaveFragment=fl;
        this.today = today;
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_feedback_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final FoFeedbackDTO itemFeed = utilityLists.get(position);

        try {

            int s = position+1;

            if (itemFeed.getFo_comment_date().equalsIgnoreCase(today)){
                holder.btn_delete.setVisibility(View.VISIBLE);
                holder.btn_leave.setVisibility(View.GONE);
            }else {
                holder.btn_delete.setVisibility(View.GONE);
                holder.btn_leave.setVisibility(View.VISIBLE);
            }
//
            holder.row_sl.setText(String.valueOf(s));
            holder.row_date.setText(itemFeed.getFo_comment_date());
            holder.row_fo_name.setText(SharePreference.getUserName(context));
            holder.row_comment.setText(itemFeed.getFo_comment());
            holder.row_tsm_comment.setText(itemFeed.getTsm_comment());
            holder.row_dsm_comment.setText(itemFeed.getDsm_comment());
            holder.row_agm_comment.setText(itemFeed.getAgm_comment());
            holder.row_ho_comment.setText(itemFeed.getScd_comment());
            holder.row_feedback.setText(itemFeed.getFeedbacktext());
//            holder.row_type.setText(itemFeed.getLeave_category());
//            holder.row_reason.setText(itemFeed.getReason());
//            holder.row_suggestion.setText(itemFeed.getRemarks());
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // foLeaveFragment.foFeedbackDelete(itemFeed.getFeedback_id());
                }
            });


        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return utilityLists.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_sl,row_date,row_fo_name,row_feedback,row_comment,row_tsm_comment,row_dsm_comment,row_agm_comment,row_ho_comment,row_todate,row_type,row_reason,row_suggestion;
        public Button btn_delete,btn_leave;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_sl = (TextView) itemView.findViewById(R.id.row_sl);
            row_date = (TextView) itemView.findViewById(R.id.row_date);
            row_fo_name = (TextView) itemView.findViewById(R.id.row_fo_name);
            row_comment = (TextView) itemView.findViewById(R.id.row_comment);
            row_tsm_comment = (TextView) itemView.findViewById(R.id.row_tsm_comment);
            row_dsm_comment = (TextView) itemView.findViewById(R.id.row_dsm_comment);
            row_agm_comment = (TextView) itemView.findViewById(R.id.row_agm_comment);
            row_ho_comment = (TextView) itemView.findViewById(R.id.row_ho_comment);
            row_feedback = (TextView) itemView.findViewById(R.id.row_feedback);
            row_type = (TextView) itemView.findViewById(R.id.row_type);
            row_reason = (TextView) itemView.findViewById(R.id.row_reason);
            row_suggestion = (TextView) itemView.findViewById(R.id.row_suggestion);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
            btn_leave = (Button) itemView.findViewById(R.id.btn_leave);

        }
    }
}
