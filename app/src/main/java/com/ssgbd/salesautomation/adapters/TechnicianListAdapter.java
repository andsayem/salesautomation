package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.TechnicianListFragment;
import com.ssgbd.salesautomation.dtos.StockListDTO;
import com.ssgbd.salesautomation.dtos.TechnicianListDTO;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Rashed on 26/4/2017.
 */
public class TechnicianListAdapter extends RecyclerView.Adapter<TechnicianListAdapter.NewReleasesItemViewHolder>{

    public ArrayList<TechnicianListDTO> stockListDTOS;
    private Context context;
    private LayoutInflater inflater;
    private Animation animation;
    TechnicianListFragment technicianListFragment;


    public TechnicianListAdapter(ArrayList<TechnicianListDTO> items, Context context,TechnicianListFragment technicianListFragment) {
        this.stockListDTOS = items;
        this.context = context;
        this.technicianListFragment = technicianListFragment;

    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_technician_list,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final TechnicianListDTO itemFeed = stockListDTOS.get(position);

        try {
            // holder.row_sl_no.setText(String.valueOf(position+1)+".");

            holder.row_technician_name.setText(itemFeed.getTechnicianName());
            holder.row_technician_phone.setText(itemFeed.getTechnicianPhone());


            if (Integer.valueOf(itemFeed.getPoint_verify())==0){

                holder.row_t_status.setText("Verify");

             }
           else if(Integer.valueOf(itemFeed.getFo_verify())==1&&Integer.valueOf(itemFeed.getFo_verify())==0){

                holder.row_t_status.setText("Waiting for TSM verification.");

                }
           else if(Integer.valueOf(itemFeed.getFo_verify())==1&&Integer.valueOf(itemFeed.getFo_verify())==0){

                holder.row_t_status.setText("Waiting for Admin verification.");

                }

           holder.row_t_status.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   technicianListFragment.getStockList(itemFeed.getId());
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
        public TextView row_technician_name,row_technician_phone;
        Button row_t_status;



        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_t_status = (Button) itemView.findViewById(R.id.row_t_status);
            row_technician_name = (TextView) itemView.findViewById(R.id.row_technician_name);
            row_technician_phone = (TextView) itemView.findViewById(R.id.row_technician_phone);

        }
    }
}
