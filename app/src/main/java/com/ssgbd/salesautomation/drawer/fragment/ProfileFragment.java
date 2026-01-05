package com.ssgbd.salesautomation.drawer.fragment;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.drawer.DrawerMain;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {

    View rootView;
    // product category list
    TextView txt_user_name,txt_user_division,txt_user_teritory,txt_user_point,txt_user_distributor,txt_user_designation,txt_edit_pic,
            txt_user_id,txt_user_mailid;
    LinearLayout linlayphone;
    public RequestQueue queue;
    ImageView user_circleImageView;
    VolleyMethods vm = new VolleyMethods();
    JsonRequestFormate jrf = new JsonRequestFormate();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        queue = Volley.newRequestQueue(getActivity());

        txt_user_name = (TextView) rootView.findViewById(R.id.txt_user_name);
        txt_user_name.setText(SharePreference.getUserName(getActivity()));
        txt_user_division = (TextView) rootView.findViewById(R.id.txt_user_division);
        txt_user_division.setText("Division :" + SharePreference.getDivisionName(getActivity()));
        txt_user_teritory = (TextView) rootView.findViewById(R.id.txt_user_teritory);
        txt_user_teritory.setText("Territory :" + SharePreference.getTerritoryName(getActivity()));
        txt_user_distributor = (TextView) rootView.findViewById(R.id.txt_user_distributor);
        txt_user_distributor.setText("Distributor :" + SharePreference.getDistributorName(getActivity()));
        txt_user_point = (TextView) rootView.findViewById(R.id.txt_user_point);
        txt_user_point.setText("Point :" + SharePreference.getUserPointName(getActivity()));
        txt_user_id = (TextView) rootView.findViewById(R.id.txt_user_id);
        txt_user_id.setText("User Id : " + SharePreference.getUserLoginId(getActivity()));

        txt_user_mailid = (TextView) rootView.findViewById(R.id.txt_user_mailid);
        txt_user_mailid.setText("Mail Id :" + SharePreference.getAD(getActivity()));
        txt_user_designation = (TextView) rootView.findViewById(R.id.txt_user_designation);
        txt_user_designation.setText(SharePreference.getDesignation(getActivity()));

   //   getHelpLineInfo(getString(R.string.base_url)+"api/apps-helpline?user_id="+ SharePreference.getUserId(getActivity()));

        user_circleImageView =(ImageView) rootView.findViewById(R.id.user_circleImageView);


   //   Log.e("call","call"+"");

        txt_edit_pic=(TextView) rootView.findViewById(R.id.txt_edit_pic);
        txt_edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerMain) getActivity()).displayView(29);
            }
        });
        imageShow();
        return rootView;
    }

    public void imageShow() {

                    final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.show();
            vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url)+"api/get-profile-pic", jrf.getimageformate(SharePreference.getUserLoginId(getActivity())), new VolleyCallBack() {
                @Override
                public void onSuccess(String result) {
                    try {

                        JSONObject jsonObject = new JSONObject(result);

                        Picasso.with(getActivity())
                                .load(getString(R.string.base_url)+jsonObject.getString("image"))
                                .placeholder(R.mipmap.user).into(user_circleImageView);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pd.dismiss();
                }
            });


    }

}