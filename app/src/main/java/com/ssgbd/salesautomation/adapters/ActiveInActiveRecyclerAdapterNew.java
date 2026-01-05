package com.ssgbd.salesautomation.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.fragment.ActiveInactiveFragment_New;
import com.ssgbd.salesautomation.dtos.ActiveInactiveDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.utils.SharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Rashed on 26/4/2017.
 */
public class ActiveInActiveRecyclerAdapterNew extends RecyclerView.Adapter<ActiveInActiveRecyclerAdapterNew.NewReleasesItemViewHolder>{

    public ArrayList<ActiveInactiveDTO> routeList;
    public ArrayList<ActiveInactiveDTO> arrayList;
    private Context context;
    private LayoutInflater inflater;
    ActiveInactiveFragment_New ainew;
    JSONObject  finalobject;
    public ActiveInActiveRecyclerAdapterNew(ArrayList<ActiveInactiveDTO> items, Context context,ActiveInactiveFragment_New ainew) {
        this.routeList = items;
        this.context = context;
        this.arrayList = new ArrayList<ActiveInactiveDTO>();
        this.arrayList.addAll(routeList);
        this.ainew = ainew;
    }

    @Override
    public NewReleasesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.row_active_inactive_retailer_new,parent,false);
        NewReleasesItemViewHolder holder = new NewReleasesItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final NewReleasesItemViewHolder holder, final int position) {

        final ActiveInactiveDTO itemFeed = routeList.get(position);

        try {

            int i=position+1 ;
            holder.row_retail_sl.setText(String.valueOf(i));
            holder.row_retail_code.setText(itemFeed.getRetailer_id());
            holder.row_retail_name.setText(itemFeed.getRetailer_name());
            holder.row_retail_route.setText(itemFeed.getRname());
            holder.row_retail_division.setText(itemFeed.getDivision_name());
            holder.row_retail_territory.setText(itemFeed.getTerritory_name());
            holder.row_retail_point.setText(itemFeed.getPoint_name());

           if ( itemFeed.getStatus().equalsIgnoreCase("0")){
            holder.row_retail_status.setText("Active");
           }else {
               holder.row_retail_status.setText("In-active");
           }

           if(Integer.valueOf(itemFeed.getStatus())==1&&Integer.valueOf(itemFeed.getActive_inactive_status())!=0){
                holder.row_retail_process.setText("Request for Active");
            }

            else if ( Integer.valueOf(itemFeed.getStatus())==0&&Integer.valueOf(itemFeed.getActive_inactive_status())!=0){
                holder.row_retail_process.setText("Request for Inactive");

            }
            //  holder.row_retail_status.setText(itemFeed.getPoint_name());
            if (itemFeed.getActive_inactive_status().equalsIgnoreCase("1")) {
                holder.row_retail_process_status.setText("FO Requested, waiting for TSM Approval");
            }
            if (itemFeed.getActive_inactive_status().equalsIgnoreCase("2")) {
                holder.row_retail_process_status.setText("TSM Requested, waiting for DSM Approval");
            }
            if (itemFeed.getActive_inactive_status().equalsIgnoreCase("3")) {
                holder.row_retail_process_status.setText("DSM Requested, waiting for AGM Approval");
            }

            if(Integer.valueOf(itemFeed.getStatus())==0&&Integer.valueOf(itemFeed.getActive_inactive_status())==0){
                holder.row_retail_action.setText("In-Active");
            }

            else if ( Integer.valueOf(itemFeed.getStatus())==1&&Integer.valueOf(itemFeed.getActive_inactive_status())==0){
                holder.row_retail_action.setText("Active");
            }

            holder.row_retail_action.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (holder.row_retail_action.getText().toString().equalsIgnoreCase("Active")){
                      try {
                      finalobject = new JSONObject();
                     JSONObject retailerobject = new JSONObject();
                      retailerobject.put("routeid",itemFeed.getRoute_id());
                      retailerobject.put("retailerid",itemFeed.getRetailer_id());
                      retailerobject.put("status","1");
                      retailerobject.put("userid", SharePreference.getUserId(context));
                      retailerobject.put("global_company_id",SharePreference.getUserGlobalId(context));
                      finalobject.put("retailer_info",retailerobject);
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                      ainew.sendRequest(finalobject.toString());
                  }else if(holder.row_retail_action.getText().toString().equalsIgnoreCase("In-Active")){

                      try {
                          finalobject = new JSONObject();
                          JSONObject retailerobject = new JSONObject();
                          retailerobject.put("routeid",itemFeed.getRoute_id());
                          retailerobject.put("retailerid",itemFeed.getRetailer_id());
                          retailerobject.put("status","0");
                          retailerobject.put("userid", SharePreference.getUserId(context));
                          retailerobject.put("global_company_id",SharePreference.getUserGlobalId(context));
                          finalobject.put("retailer_info",retailerobject);
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                      ainew.sendRequest(finalobject.toString());
                  }
              }
          });

        }catch (Exception e){
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class NewReleasesItemViewHolder extends RecyclerView.ViewHolder{
        public TextView row_retail_sl,row_retail_code,row_retail_name,row_retail_route,row_retail_division;
        public TextView row_retail_territory,row_retail_point,row_retail_process,row_retail_status,row_retail_process_status,row_retail_action;
        public LinearLayout linlay_main;

        public NewReleasesItemViewHolder(View itemView) {
            super(itemView);
            row_retail_sl = (TextView) itemView.findViewById(R.id.row_retail_sl);
            row_retail_code = (TextView) itemView.findViewById(R.id.row_retail_code);
            row_retail_name = (TextView) itemView.findViewById(R.id.row_retail_name);
            row_retail_route = (TextView) itemView.findViewById(R.id.row_retail_route);
            row_retail_division = (TextView) itemView.findViewById(R.id.row_retail_division);
            row_retail_territory = (TextView) itemView.findViewById(R.id.row_retail_territory);
            row_retail_point = (TextView) itemView.findViewById(R.id.row_retail_point);
            row_retail_status = (TextView) itemView.findViewById(R.id.row_retail_status);
            row_retail_process = (TextView) itemView.findViewById(R.id.row_retail_process);
            row_retail_process_status = (TextView) itemView.findViewById(R.id.row_retail_process_status);
            row_retail_action = (TextView) itemView.findViewById(R.id.row_retail_action);

        }
    }
    public void filter(String charText) {
        charText = charText.toLowerCase();
        routeList.clear();
        if (charText.length() == 0) {
            routeList.addAll(arrayList);
        } else {
            for (ActiveInactiveDTO hm : arrayList) {
                if (hm.getRname().toLowerCase().contains(charText)) {
                    routeList.add(hm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
