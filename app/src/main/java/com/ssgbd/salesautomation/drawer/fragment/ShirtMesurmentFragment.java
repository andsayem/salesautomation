package com.ssgbd.salesautomation.drawer.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.PlatformActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.ShirtListActivity;
import com.ssgbd.salesautomation.adapters.FOFeedbackListRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.drawer.SplashActivity;
import com.ssgbd.salesautomation.dtos.FoFeedbackDTO;
import com.ssgbd.salesautomation.dtos.QDTO;
import com.ssgbd.salesautomation.dtos.QuestionDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;


public class ShirtMesurmentFragment extends Fragment implements View.OnClickListener {

    View rootView;
    private Dialog wdialog;
    RouteRecyclerAdapter routeRecyclerAdapter;
    private final Handler handler = new Handler();
    // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    VolleyMethods vm = new VolleyMethods();
    JSONObject requestObject;
    Button btn_add;
    EditText edt_leave_reason, edt_remarks;
    public RequestQueue queue;
    String PRESENT_DATE = "";
    DatePickerDialog picker;
    String FROMDATE = "", TODATE = "";
    String fromDate = "";
    ImageView img;
    long l;
   EditText edt_collar,edt_chest,edt_waist,edt_shoulder,edt_sleeve,edt_length,edt_cuff_hole,edt_armhole;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.shirt_mesurment_fragment, container, false);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        PRESENT_DATE = df.format(c);

        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        img = (ImageView) rootView.findViewById(R.id.img);
        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);
        img.setAnimation(anim);
       // Later.. stop the animation


        handler.postDelayed(startRun, 1500);
        img.setOnClickListener(this);


        initUi();
        edt_collar = (EditText) rootView.findViewById(R.id.edt_collar);
        edt_chest = (EditText) rootView.findViewById(R.id.edt_chest);
        edt_waist = (EditText) rootView.findViewById(R.id.edt_waist);
        edt_shoulder = (EditText) rootView.findViewById(R.id.edt_shoulder);
        edt_sleeve = (EditText) rootView.findViewById(R.id.edt_sleeve);
        edt_length = (EditText) rootView.findViewById(R.id.edt_length);
        edt_cuff_hole = (EditText) rootView.findViewById(R.id.edt_cuff_hole);
        edt_armhole = (EditText) rootView.findViewById(R.id.edt_armhole);
      
        return rootView;
    }

    private void initUi() {
        queue = Volley.newRequestQueue(getActivity());


     //   getFeedbackList(FROMDATE, TODATE);

    }
    private final Runnable startRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

         img.setAnimation(null);

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img:

//                Intent intent = new Intent(getActivity(), ShirtListActivity.class);
//                startActivity(intent);

                break;

            case R.id.btn_add:

                if (edt_collar.getText().toString().equalsIgnoreCase("")||edt_chest.getText().toString().equalsIgnoreCase("")
                ||edt_waist.getText().toString().equalsIgnoreCase("")||edt_shoulder.getText().toString().equalsIgnoreCase("")
                ||edt_sleeve.getText().toString().equalsIgnoreCase("")||edt_length.getText().toString().equalsIgnoreCase("")
                ||edt_cuff_hole.getText().toString().equalsIgnoreCase("")||edt_armhole.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "দয়া করে সকল ফিল্ড পূরণ করুন।", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "ফিল্ড পূরণ ", Toast.LENGTH_SHORT).show();
                    sendMesurment(edt_collar.getText().toString(),edt_chest.getText().toString(),edt_waist.getText().toString(),edt_shoulder.getText().toString(),
                    edt_sleeve.getText().toString(),edt_length.getText().toString(),edt_cuff_hole.getText().toString(),edt_armhole.getText().toString());
                }
                break;

        }
    }


    public void sendMesurment(String collar,String chest,String waist,String shoulder,String sleeve,String length,String cuff_hole,String arm_hole) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();

        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url) + "api/shirt-measurement-store",
                jp.submitShirtMesurment(collar,chest,waist,shoulder,sleeve,length,cuff_hole,arm_hole,SharePreference.getUserId(getActivity())), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        pd.dismiss();
                        try {

                            JSONObject jsonObject1 = new JSONObject(result);
                       //         Log.e("resp>>",jsonObject1+"");
                            
                                    if(jsonObject1.optString("status").equalsIgnoreCase("1")){
                                     Toast.makeText(getActivity(), "আপনার তথ্য গ্রহণ করা হয়েছে। ", Toast.LENGTH_SHORT).show();
                                        edt_collar.setText("");
                                        edt_chest.setText("");
                                        edt_waist.setText("");
                                        edt_shoulder.setText("");
                                        edt_sleeve.setText("");
                                        edt_length.setText("");
                                        edt_cuff_hole.setText("");
                                        edt_armhole.setText("");

                                    }else{

                                        Toast.makeText(getActivity(), "আপনার তথ্য গ্রহণ করা হয়নি। পুনরায় চেষ্টা করুন।", Toast.LENGTH_SHORT).show();

                                    }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                });
    }
    public String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key) || json.optString(key).equalsIgnoreCase("null"))
            return "";
        else
            return json.optString(key, key);
    }


    public void submitAns() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();

        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url) + "api/add-feedback", requestObject.toString(), new VolleyCallBack() {
            @Override
            public void onSuccess(final String result) {
                pd.dismiss();
                //Log.e("<<..", result.toString() + "<<>>");
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            JSONObject jsonObject1 = new JSONObject(result);
                            Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();
                            if (jsonObject1.optString("status").equalsIgnoreCase("1")) {


                            } else {
                                Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        });
    }


    public void foFeedbackDelete(String feedbackId) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();

        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url) + "api/delete-feedback",
                jp.submitFeedbackDelete(SharePreference.getUserId(getActivity()), feedbackId), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                                   // Log.e("jsonObject1", jsonObject1 + "<>");


                                    if (jsonObject1.optString("status").equalsIgnoreCase("1")) {
                                        // getFeedbackList(FROMDATE,TODATE);

                                        Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }

                });
    }

}
