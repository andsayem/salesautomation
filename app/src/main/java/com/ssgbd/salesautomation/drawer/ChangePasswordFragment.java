package com.ssgbd.salesautomation.drawer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.LoginActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.ReturnChangeCategoryDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ChangePasswordFragment extends Fragment {

    View rootView;
    TextView edit_old_password,edit_new_password,edit_confirm_password;
    Button btn_change_password;


    VolleyMethods vm = new VolleyMethods();
    JsonRequestFormate jrf = new JsonRequestFormate();

    TextView txt_back;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // Log.e("log2", "ac");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.change_password_fragment, container, false);

        txt_back = (TextView) rootView.findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerMain) getActivity()).displayView(29);

            }
        });
        edit_old_password = (EditText) rootView.findViewById(R.id.edit_old_password);
        edit_new_password = (EditText) rootView.findViewById(R.id.edit_new_password);
        edit_confirm_password = (EditText) rootView.findViewById(R.id.edit_confirm_password);

        edit_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_change_password = (Button) rootView.findViewById(R.id.btn_change_password);
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_old_password.getText().length()==0){
                    Toast.makeText(getActivity(), "Please enter old password.", Toast.LENGTH_SHORT).show();

                   return;
                }else if(edit_new_password.getText().length()==0){
                    Toast.makeText(getActivity(), "Please enter new password.", Toast.LENGTH_SHORT).show();

                }else if(edit_confirm_password.getText().length()==0){
                    Toast.makeText(getActivity(), "Please enter confirm password.", Toast.LENGTH_SHORT).show();

                }else {
                    if (edit_new_password.getText().toString().equalsIgnoreCase(edit_confirm_password.getText().toString())) {

                        setChangePassword(edit_old_password.getText().toString(), edit_confirm_password.getText().toString());
                    }else {
                        Toast.makeText(getActivity(), "New password and confirm password not match.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return rootView;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

    public void setChangePassword( String oldPassword,String newPassword){

        RequestQueue queue;
        //
        String  url = getResources().getString(R.string.base_url)+"api/apps-change-password?user_id="+ SharePreference.getUserId(getActivity()) +"&oldPassword="+oldPassword+"&newPassword="+newPassword;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                     //   Log.e("version>>",response+"");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("1")){
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                SharePreference.setIslogIn(getActivity(), "0");
                                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                                getActivity().startActivity(loginIntent);
                                getActivity().finish();


                            }else {
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }


                        }catch (JSONException je){

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
               //         Log.e("error volley>>",error.toString()+"");
                    }
                }
        ) {
        };
        queue = Volley.newRequestQueue(getActivity());
        queue.getCache().clear();
        postRequest.setShouldCache(false);
        queue.add(postRequest);
    }



}
