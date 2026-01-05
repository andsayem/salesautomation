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
import com.ssgbd.salesautomation.dtos.FeedbackDTO;
import com.ssgbd.salesautomation.dtos.FoFeedbackDTO;
import com.ssgbd.salesautomation.utils.SharePreference;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class FOFeedbackRecyclerAdapter extends RecyclerView.Adapter<FOFeedbackRecyclerAdapter.NewReleasesItemViewHolder>{

    public ArrayList<FeedbackDTO> utilityLists;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    FoFeedbackFragment foLeaveFragment;
    String today;

    public FOFeedbackRecyclerAdapter(ArrayList<FeedbackDTO> items, Context context, FoFeedbackFragment fl) {
        this.utilityLists = items;
        this.context = context;
        this.foLeaveFragment=fl;
        this.today = today;
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_feedback,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final FeedbackDTO itemFeed = utilityLists.get(position);

        holder.row_id.setText(itemFeed.getId());
        holder.row_date.setText(itemFeed.getDate());
        holder.row_replace_value.setText(itemFeed.getRp_value());
        holder.row_replace_text.setText(itemFeed.getRp_text());
        holder.row_free_pending.setText(itemFeed.getFree_pending());
        holder.row_competition_facts.setText(itemFeed.getCompetition_facts());
        holder.row_complain_box.setText(itemFeed.getComplain_box());

        // ,,,row_feedback,row_comment,row_tsm_comment


    }

    @Override
    public int getItemCount() {
        return utilityLists.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_id,row_date,row_fo_name,row_replace_value,row_replace_text,row_free_pending,row_competition_facts,row_complain_box;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_id = (TextView) itemView.findViewById(R.id.row_id);
            row_date = (TextView) itemView.findViewById(R.id.row_date);
            row_replace_value = (TextView) itemView.findViewById(R.id.row_replace_value);
            row_replace_text = (TextView) itemView.findViewById(R.id.row_replace_text);
            row_free_pending  = (TextView) itemView.findViewById(R.id.row_free_pending );
            row_competition_facts = (TextView) itemView.findViewById(R.id.row_competition_facts);
            row_complain_box  = (TextView) itemView.findViewById(R.id.row_complain_box);


        }
    }
}
