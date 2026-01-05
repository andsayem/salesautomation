package com.ssgbd.salesautomation.drawer.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.FOFeedbackListRecyclerAdapter;
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


public class FoFeedbackFragmentBak extends Fragment implements View.OnClickListener{

    View rootView;
    private Dialog wdialog;
    TextView txt_route_name,row_sl;
    FOFeedbackListRecyclerAdapter feedback_list_adapter;
    RecyclerView leave_list_recyclerView;
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<FoFeedbackDTO> foLeaveListDTOS = new ArrayList<>();
    ArrayList<QuestionDTO> qDTOS = new ArrayList<>();
    VolleyMethods vm = new VolleyMethods();
    TextView txt_fromdate,txt_todate,txt_search;
    JSONObject  requestObject;
    Button btn_add;
    EditText edt_leave_reason,edt_remarks;
    public RequestQueue queue;
    String PRESENT_DATE="";

    DatePickerDialog picker;
    String FROMDATE="",TODATE="";
    String fromDate="";
    long l;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fo_feedback_fragment, container, false);

        row_sl = (TextView) rootView.findViewById(R.id.row_sl);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        PRESENT_DATE = df.format(c);


        leave_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        leave_list_recyclerView.setLayoutManager(linearLayoutManager1);
      //  feedback_list_adapter = new FOFeedbackListRecyclerAdapter( foLeaveListDTOS,getActivity(), FoFeedbackFragmentBak.this,PRESENT_DATE);
        leave_list_recyclerView.setAdapter(feedback_list_adapter);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        edt_leave_reason = (EditText) rootView.findViewById(R.id.edt_leave_reason);
        initUi();

        txt_fromdate = (TextView) rootView.findViewById(R.id.txt_fromdate);
        txt_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr=Calendar.getInstance();
                int day=cldr.get(Calendar.DAY_OF_MONTH);
                int month=cldr.get(Calendar.MONTH);
                int year=cldr.get(Calendar.YEAR);


                // date picker dialog
                picker=new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

//                              txt_month_year.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                fromDate = year + "-" + (monthOfYear + 1) + "-" +  dayOfMonth;
                                String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }
                                FROMDATE = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    FROMDATE = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }
                                String result="";
                                try {
                                    result = getActivity().getResources().getStringArray(R.array.month_names)[monthOfYear];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    result=Integer.toString(monthOfYear);
                                }
                                txt_fromdate.setText(dayOfMonth+" "+result+" "+year );
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        txt_todate = (TextView) rootView.findViewById(R.id.txt_todate);
        txt_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar cldr=Calendar.getInstance();
                int day=cldr.get(Calendar.DAY_OF_MONTH);
                int month=cldr.get(Calendar.MONTH);
                int year=cldr.get(Calendar.YEAR);


                // date picker dialog
                picker=new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String day = String.valueOf(dayOfMonth);
                                if((dayOfMonth )<10){
                                    day= "0"+dayOfMonth;
                                }
                                TODATE = year + "-" + (monthOfYear + 1) + "-" +  day;

                                if((monthOfYear + 1)<10){
                                    TODATE = year + "-" + "0"+(monthOfYear + 1) + "-" + day;
                                }

                                String result="";
                                try {
                                    result = getActivity().getResources().getStringArray(R.array.month_names)[monthOfYear];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    result=Integer.toString(monthOfYear);
                                }
                                txt_todate.setText(dayOfMonth+" "+result+" "+year );

                            }
                        }, year, month, day);
                picker.show();
            }
        });
        txt_search = (TextView) rootView.findViewById(R.id.txt_search);
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getFeedbackList(FROMDATE, TODATE);


            }
        });

        return rootView;
    }

    private void initUi() {
        queue = Volley.newRequestQueue(getActivity());
        txt_route_name = (TextView) rootView.findViewById(R.id.txt_route_name);


        getFeedbackList(FROMDATE,TODATE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_add:

                feedbackDoalog();
                break;

        }
    }


    public void getFeedbackList(String fromdate,String todate){
        foLeaveListDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();

        vm.sendRequestToServer2(getActivity(),  getResources().getString(R.string.base_url)+"api/feedback-list",
                jp.foFeedbackFormat(SharePreference.getUserId(getActivity()),fromdate,todate), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        pd.dismiss();
                        try {
                            //SharePreference.getUserId(getActivity())

                            JSONObject jsonObject1 = new JSONObject(result);
                        //    Log.e("resp>>",jsonObject1+"");
                            JSONArray jsonArray = jsonObject1.getJSONArray("feedbacklist");

                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject jsonObject2 = jsonObject.getJSONObject("info");

                                FoFeedbackDTO foFeedbackDTO = new FoFeedbackDTO();
                                foFeedbackDTO.setFeedback_id(jsonObject2.getString("feedback_id"));
                                foFeedbackDTO.setFo_comment(optStringNullCheck(jsonObject2,"fo_comment"));

                                try {
                                    String mAlertDateTime =jsonObject2.getString("fo_comment_date");
                                    SimpleDateFormat  dft = new SimpleDateFormat("yyyy-MM-dd");
                                    Date d = dft.parse(mAlertDateTime);

                              //      Log.e("ytt>>",dft.format(d)+"<<<");
                                    foFeedbackDTO.setFo_comment_date(dft.format(d).toString());
                                } catch (ParseException ex) {
                               //     Log.e("ytt>>",ex.toString()+"<<<");
                                }

                                if (optStringNullCheck(jsonObject2,"tsm_comment").equalsIgnoreCase("")){
                                    foFeedbackDTO.setTsm_comment("");
                                }else {
                                    foFeedbackDTO.setTsm_comment("Name :"+optStringNullCheck(jsonObject2,"tsmname")+"\n"+"Comment : "+optStringNullCheck(jsonObject2,"tsm_comment")+"\n"+"Date :"+optStringNullCheck(jsonObject2,"tsm_comment_date"));
                                }
                                if (optStringNullCheck(jsonObject2,"dsm_comment").equalsIgnoreCase("")){
                                    foFeedbackDTO.setDsm_comment("");
                                }else {
                                    foFeedbackDTO.setDsm_comment("Name :"+optStringNullCheck(jsonObject2,"dsmname")+"\n"+"Comment : "+optStringNullCheck(jsonObject2,"dsm_comment")+"\n"+"Date :"+optStringNullCheck(jsonObject2,"dsm_comment_date"));
                                }
                                if (optStringNullCheck(jsonObject2,"agm_comment").equalsIgnoreCase("")){
                                    foFeedbackDTO.setAgm_comment("");
                                }else {
                                    foFeedbackDTO.setAgm_comment("Name :"+optStringNullCheck(jsonObject2,"agmname")+"\n"+"Comment : "+optStringNullCheck(jsonObject2,"agm_comment")+"\n"+"Date :"+optStringNullCheck(jsonObject2,"agm_comment_date"));
                                }

                                if (optStringNullCheck(jsonObject2,"scd_comment").equalsIgnoreCase("")){
                                    foFeedbackDTO.setScd_comment("");

                                }else{

                              foFeedbackDTO.setScd_comment("Name :"+optStringNullCheck(jsonObject2,"scdname")+"\n"+"Comment : "+optStringNullCheck(jsonObject2,"scd_comment")+"\n"+"Date :"+optStringNullCheck(jsonObject2,"scd_comment_date"));

                                }


                                JSONArray qArray = jsonObject.getJSONArray("answerlist");
                                 ArrayList<QDTO> qDTOS = new ArrayList<>();

                                int integer = 5;

                                StringBuilder qa = new StringBuilder(100);


                                for (int q=0;q<qArray.length();q++){
                                    JSONObject sss = qArray.getJSONObject(q);
                                    QDTO qdto = new QDTO();
                                    qdto.setQuestion(sss.getString("question"));
                                    qdto.setAnswer(sss.getString("answer"));

                                    qDTOS.add(qdto);
                                    qa.append("Q :"+qDTOS.get(q).getQuestion()+"\n"+"Ans :"+qDTOS.get(q).getAnswer()+"\n");

                                    foFeedbackDTO.setFeedbacktext(String.valueOf(qa));
                                }

                                foLeaveListDTOS.add(foFeedbackDTO);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                feedback_list_adapter.notifyDataSetChanged();

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


    public void submitAns(){
        foLeaveListDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();

        vm.sendRequestToServer2(getActivity(),  getResources().getString(R.string.base_url)+"api/add-feedback",requestObject.toString(), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();


                      //  Log.e("<<..",result.toString()+"<<>>");

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                                    Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();


                                    if (jsonObject1.optString("status").equalsIgnoreCase("1")) {



                                        getFeedbackList(FROMDATE,TODATE);

                                    }else {
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


    public void foFeedbackDelete(String feedbackId){
        foLeaveListDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();

        vm.sendRequestToServer2(getActivity(),  getResources().getString(R.string.base_url)+"api/delete-feedback",
                jp.submitFeedbackDelete(SharePreference.getUserId(getActivity()),feedbackId), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                              //      Log.e("jsonObject1",jsonObject1+"<>");


                                    if (jsonObject1.optString("status").equalsIgnoreCase("1")) {
                                    // getFeedbackList(FROMDATE,TODATE);

                                        Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();
                                    }else {

                                        Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();
                                    }

                                    getFeedbackList(FROMDATE,TODATE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }

                });
    }



    private void feedbackDoalog() {

        wdialog =new Dialog(getActivity(),android.R.style.Theme_Light_NoTitleBar_Fullscreen);
//      wdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//      WindowManager.LayoutParams.MATCH_PARENT);
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.fo_feedback_dialog_new);
//        final RecyclerView route_list_recyclerView;
//        final Button btn_submit;
//        final ImageView imbtnCross;
//        final EditText edt_comment;
//        final TextView txt_close_dialog;
//        qDTOS.clear();
//        edt_comment= (EditText) wdialog.findViewById(R.id.edt_comment);
//        RadioGroup radio_group_q1 = (RadioGroup) wdialog.findViewById(R.id.radio_group_q1);
//        RadioButton radio_y_q_1 = wdialog.findViewById(R.id.radio_y_q_1);
//        RadioButton radio_n_q_1 = wdialog.findViewById(R.id.radio_n_q_1);
//
//        RadioGroup radio_group_q2 = (RadioGroup) wdialog.findViewById(R.id.radio_group_q2);
//        RadioButton radio_y_q_2 = wdialog.findViewById(R.id.radio_y_q_2);
//        RadioButton radio_n_q_2 = wdialog.findViewById(R.id.radio_n_q_2);
//
//        RadioGroup radio_group_q3 = (RadioGroup) wdialog.findViewById(R.id.radio_group_q3);
//        RadioButton radio_y_q_3 = wdialog.findViewById(R.id.radio_y_q_3);
//        RadioButton radio_n_q_3= wdialog.findViewById(R.id.radio_n_q_3);
//
//        RadioGroup radio_group_q4 = (RadioGroup) wdialog.findViewById(R.id.radio_group_q4);
//        RadioButton radio_y_q_4 = wdialog.findViewById(R.id.radio_y_q_4);
//        RadioButton radio_n_q_4 = wdialog.findViewById(R.id.radio_n_q_4);
//
//        for (int i=0;i<4;i++){
//       //     Log.e("<i>",i+"<>");
//            QuestionDTO questionDTO1 = new QuestionDTO();
//            questionDTO1.setqID(String.valueOf(i+1));
//            questionDTO1.setqAns("YES");
//            qDTOS.add(questionDTO1);
//        }
//
//        for (int t=0;t<qDTOS.size();t++) {
//          //  Log.e("size>",qDTOS.size()+"--"+ qDTOS.get(t).getqID() + "<<>>" + qDTOS.get(t).getqAns() + "000");
//        }
//
//        radio_group_q1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
//        {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch(checkedId){
//                    case R.id.radio_y_q_1:
//                        updateArray("1","YES");
//                        break;
//                    case R.id.radio_n_q_1:
//                        updateArray("1","NO");
//                        break;
//                }
//            }
//        });
//
//        radio_group_q2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
//        {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch(checkedId){
//                    case R.id.radio_y_q_2:
//                        updateArray("2","YES");
//                        break;
//                    case R.id.radio_n_q_2:
//                        updateArray("2","NO");
//                        break;
//                }
//            }
//        });
//
//        radio_group_q3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
//        {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch(checkedId){
//                    case R.id.radio_y_q_3:
//                        updateArray("3","YES");
//                        break;
//                    case R.id.radio_n_q_3:
//                        updateArray("3","NO");
//                        break;
//                }
//            }
//        });
//
//        radio_group_q4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
//        {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch(checkedId){
//                    case R.id.radio_y_q_4:
//                        updateArray("4","YES");
//                        break;
//                    case R.id.radio_n_q_4:
//                        updateArray("4","NO");
//                        break;
//                }
//            }
//        });
//        txt_close_dialog = (TextView)  wdialog.findViewById(R.id.txt_close_dialog);
//        txt_close_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                wdialog.cancel();
//            }
//        });
//
//        btn_submit = (Button)  wdialog.findViewById(R.id.btn_submit);
//        btn_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                  requestObject = new JSONObject();
//                JSONArray jsonArray = new JSONArray();
//                for (int i=0;i<qDTOS.size();i++){
//                    JSONObject  jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("qsno",qDTOS.get(i).getqID());
//                        jsonObject.put("answer",qDTOS.get(i).getqAns());
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    jsonArray.put(jsonObject);
//                }
//
//
////                for(int i = 0; i < qDTOS.size(); i++) {
////                    row_sl.append(qDTOS.get(i).getqAns());
////                }
//
//
//                try {
//
//                requestObject.put("foid",SharePreference.getUserId(getActivity()));
//                requestObject.put("comment",edt_comment.getText().toString());
//                requestObject.put("answerlist",jsonArray);
//             //    Log.e("<<>",requestObject+"<>");
//                    submitAns();
//                   wdialog.cancel();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            }
//        });


        wdialog.show();
    }


    public  void  updateArray(String qid,String qans){

        for (int p=0;p<qDTOS.size();p++){
            if (qDTOS.get(p).getqID().equalsIgnoreCase(qid)){
                qDTOS.remove(p);
            }
        }
        QuestionDTO questionDTO1 = new QuestionDTO();
        questionDTO1.setqID(qid);
        questionDTO1.setqAns(qans);
        qDTOS.add(questionDTO1);

        for (int t=0;t<qDTOS.size();t++) {
          //  Log.e("size>",qDTOS.size()+"--"+ qDTOS.get(t).getqID() + "<<>>" + qDTOS.get(t).getqAns() + "---=");
        }
    }

}
