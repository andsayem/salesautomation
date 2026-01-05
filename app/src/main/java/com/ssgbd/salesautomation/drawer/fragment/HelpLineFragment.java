package com.ssgbd.salesautomation.drawer.fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.utils.SharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HelpLineFragment extends Fragment {

    View rootView;
    // product category list
    TextView txt_title,txt_support_person,txt_support_mobileno,txt_support_email;
    LinearLayout linlayphone;
    public RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.help_line_fragment, container, false);
        queue = Volley.newRequestQueue(getActivity());

        linlayphone = (LinearLayout) rootView.findViewById(R.id.linlayphone);
        txt_title = (TextView) rootView.findViewById(R.id.txt_title);
        SpannableString content = new SpannableString("Support Person Info :");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txt_title.setText(content);



        txt_support_person = (TextView) rootView.findViewById(R.id.txt_support_person);
        txt_support_mobileno = (TextView) rootView.findViewById(R.id.txt_support_mobileno);
        txt_support_email = (TextView) rootView.findViewById(R.id.txt_support_email);

        getHelpLineInfo(getString(R.string.base_url)+"api/apps-helpline?user_id="+ SharePreference.getUserId(getActivity()));

        linlayphone.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Intent.ACTION_DIAL);
              intent.setData(Uri.parse("tel:"+txt_support_mobileno.getText()));
              startActivity(intent);
          }
      });

        txt_support_email.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              try{
                  Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + txt_support_email.getText()));
                  intent.putExtra(Intent.EXTRA_SUBJECT, "");
                  intent.putExtra(Intent.EXTRA_TEXT, "");
                  startActivity(intent);
              }catch(ActivityNotFoundException e){
                  //TODO smth
              }
          }
      });


        return rootView;
    }

    public void getHelpLineInfo(String url) {



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


              //  Log.e("response",response+"");
                try {
                    JSONObject respjsonObj = new JSONObject(response);
                    JSONObject object = respjsonObj.getJSONObject("helpline");
                    txt_support_person.setText(object.getString("contact_name"));
                    txt_support_mobileno.setText(object.getString("mobile_no"));
                    txt_support_email.setText(object.getString("email"));


                } catch (JSONException e) {

                 //   Log.e("url",e.getMessage().toString()+"<<");

                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("VOLLEY", error.toString());
                //    Utility.dialog.closeProgressDialog();
            }


        }) ;

        queue.add(stringRequest);

    }

}