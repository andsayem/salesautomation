package com.ssgbd.salesautomation.drawer.dashboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ConfirmOrderRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.DashboardVisitReportRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.ConfirmOrderDTO;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.dtos.OrderRepotListDTO;
import com.ssgbd.salesautomation.dtos.VisitReportDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;

public class DashboardTodaysVisitActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private Toolbar mToolbar;
    TextView txt_toolbar;

    ArrayList<VisitReportDTO> visitDOTOS = new ArrayList<>();
    String ORDER_DATA = "";
    // final order
    RecyclerView visit_list_recyclerView;
    DashboardVisitReportRecyclerAdapter orderRecyclerAdapter;
    //bucket
    TextView txt_bucket_amount;
    LinearLayout linlay_buket_amount;
    LinearLayout linlay_delete,lin_lay_offer;
    VolleyMethods vm = new VolleyMethods();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_todays_visit_screen);
        context = this;

        loadToolBar();
        initUi();
    }

    private void loadToolBar() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_toolbar = (TextView) mToolbar.findViewById(R.id.txt_toolbar);
        txt_toolbar.setText("Todays visit");

        linlay_buket_amount = (LinearLayout) mToolbar.findViewById(R.id.linlay_buket_amount);
        linlay_buket_amount.setOnClickListener(this);
        linlay_buket_amount.setVisibility(View.GONE);
        txt_bucket_amount = (TextView) mToolbar.findViewById(R.id.txt_bucket_amount);
        txt_bucket_amount.setVisibility(View.VISIBLE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
    }
    private void initUi() {

        visitListShow();

        getTodayVisitReport();

    }

    private void visitListShow() {

        visit_list_recyclerView = (RecyclerView) findViewById(R.id.visit_list_recyclerView);

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        visit_list_recyclerView.setLayoutManager(linearLayoutManager);
        orderRecyclerAdapter = new DashboardVisitReportRecyclerAdapter(visitDOTOS,context);
        visit_list_recyclerView.setAdapter(orderRecyclerAdapter);

        visit_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(context, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {




                new AlertDialog.Builder(context)
                        .setTitle("Alert")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("Delete this item?").setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                for (int i=0;i<orderDtos.size();i++){
//                                    if (orderDtos.get(i).getProduct_id().equalsIgnoreCase(orderDtos.get(position).getProduct_id())){
//                                        orderDtos.remove(i);
//                                    }
//                                }
//                                orderRecyclerAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }).show();

            }
        }));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id==android.R.id.home){
           finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){


        }
    }



    public void getTodayVisitReport(){
     //   changeReportListDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(context,  getString(R.string.base_url)+"api/report/today-visits",
                jp.visitListJson( SharePreference.getUserId(context),SharePreference.getUserGlobalId(context)), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();


                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);


                                        JSONArray jsonArray = jsonObject1.getJSONArray("today_visit");
                                        for (int i=0;i<jsonArray.length();i++){
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            VisitReportDTO listDTO = new VisitReportDTO();
                                            listDTO.setRouteName(optStringNullCheck(object,"rname"));
                                            listDTO.setRetailerName(optStringNullCheck(object,"name"));
                                            listDTO.setDate(optStringNullCheck(object,"date"));
                                            listDTO.setStatus(optStringNullCheck(object,"status"));
                                            visitDOTOS.add(listDTO);
                                        }

                                  //      Log.e("vsize>>>",visitDOTOS.size()+"");
                                    orderRecyclerAdapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }
    public  String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null"))
            return "";
        else
            return json.optString(key, key);
    }
}



