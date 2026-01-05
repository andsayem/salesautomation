package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.NoticeListFragment;
import com.ssgbd.salesautomation.dtos.NoticeDTO;
import com.ssgbd.salesautomation.dtos.StockListDTO;

import java.util.ArrayList;

/**
 * Created by Rashed on 26/4/2017.
 */
public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.NewReleasesItemViewHolder>{

    public ArrayList<NoticeDTO> stockListDTOS;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    NoticeListFragment  noticeListFragment;

    public NoticeListAdapter(ArrayList<NoticeDTO> items, Context context,NoticeListFragment noticeListFragment) {
        this.stockListDTOS = items;
        this.context = context;
        this.noticeListFragment = noticeListFragment;
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_notice_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final NoticeDTO itemFeed = stockListDTOS.get(position);

        try {
           // holder.row_sl_no.setText(String.valueOf(position+1)+".");
            holder.row_notice.setText(itemFeed.getNotice());
            holder.row_startdate.setText(itemFeed.getStartDate());
            holder.row_enddate.setText(itemFeed.getEndDate());

            if (itemFeed.getReadStatus().equalsIgnoreCase("0")){
                holder.row_linlay_main.setBackgroundColor(Color.parseColor("#7CA629"));
                holder.row_notice.setTextColor(Color.parseColor("#ffffff"));
            }

            holder.row_linlay_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.row_linlay_main.setBackgroundColor(Color.parseColor("#ffffff" ));
                    noticeListFragment.changeNotStatus(holder.getAdapterPosition());
                    holder.row_notice.setTextColor(Color.parseColor("#636262"));
                }
            });

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return stockListDTOS.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_notice,row_startdate,row_enddate;
        Button row_product_select;
        LinearLayout row_linlay_main;


        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_notice = (TextView) itemView.findViewById(R.id.row_notice);
            row_startdate = (TextView) itemView.findViewById(R.id.row_startdate);
            row_enddate = (TextView) itemView.findViewById(R.id.row_enddate);
            row_linlay_main = (LinearLayout) itemView.findViewById(R.id.row_linlay_main);

        }
    }
}
