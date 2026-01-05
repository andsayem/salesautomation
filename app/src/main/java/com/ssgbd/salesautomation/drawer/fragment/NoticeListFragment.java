package com.ssgbd.salesautomation.drawer.fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.NoticeListAdapter;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.StockListAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.NoticeDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.StockListDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NoticeListFragment extends Fragment implements View.OnClickListener{

    View rootView;
    // product category list
    TextView txt_category_list;
    ArrayList<NoticeDTO> noticeDTOS = new ArrayList<>();

    // product
    RecyclerView product_list_recyclerView;
    NoticeListAdapter noticeListAdapter;
    DatabaseHelper databaseHelper;
    private static String DB_NAME = "ssg.db";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.notice_list_fragment, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String myPath = Utility.DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()){
            databaseHelper.openDataBase();
        }

        txt_category_list = (TextView) rootView.findViewById(R.id.txt_category_list);
        txt_category_list.setOnClickListener(this);
        product_list_recyclerView=(RecyclerView) rootView.findViewById(R.id.product_list_recyclerView);


   //getNoticeList();
        noticeDTOS = databaseHelper.getAllNotice(databaseHelper,"0");

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        product_list_recyclerView.setLayoutManager(linearLayoutManager);
        noticeListAdapter = new NoticeListAdapter( noticeDTOS,getActivity(),NoticeListFragment.this);
        product_list_recyclerView.setAdapter(noticeListAdapter);
//
//        product_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                changeNotStatus(position);
//            }
//        }));

        noticeListAdapter.notifyDataSetChanged();
        return rootView;
    }

    public void changeNotStatus(int position){
        databaseHelper.updateNoticeReadStatus(databaseHelper,"1",noticeDTOS.get(position).getId());

        ((DrawerMain)getActivity()).updateNotifiCount();
     //   noticeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_category_list:

                break;
        }
    }

    public void getNoticeList(){
        JsonRequestFormate jp = new JsonRequestFormate();
        VolleyMethods vm = new VolleyMethods();

        noticeDTOS.clear();
        vm.sendRequestToServer2(getActivity(), getString(R.string.base_url)+"api/app_notic", jp.jsonGetNotice(SharePreference.getUserTypeId(getActivity())), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {


                try {
                    JSONObject respjsonObj = new JSONObject(result);
                    JSONArray statusArray = respjsonObj.getJSONArray("notic_list");
                    for (int i = 0; i < statusArray.length(); i++) {
                        JSONObject routeObject = statusArray.getJSONObject(i);
                        NoticeDTO noticeDTO = new NoticeDTO();
                        noticeDTO.setId(routeObject.getString("id"));
                        noticeDTO.setStartDate("Start Date :"+routeObject.getString("start_date"));
                        noticeDTO.setEndDate("End Date :"+routeObject.getString("end_date"));
                        noticeDTO.setNotice(routeObject.getString("notice"));
                        noticeDTO.setStatus(routeObject.getString("status"));
                        noticeDTO.setUserType(routeObject.getString("user_type"));

                        noticeDTOS.add(noticeDTO);
                    }

                } catch (JSONException je) {
                }
               // Log.e("size",stockDTOS.size()+"statusArray");
                noticeListAdapter.notifyDataSetChanged();
            }

        });
    }
}
