package com.ssgbd.salesautomation.drawer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.LoginActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class SettingsFragment extends Fragment {

    View rootView;

    LinearLayout lin_cngpassword,lin_cngpropic,linlayprivacy,linlayabout,linlaypermission,linlayfeedback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.settings_fragment, container, false);

        lin_cngpassword = (LinearLayout) rootView.findViewById(R.id.lin_cngpassword);
        lin_cngpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerMain) getActivity()).displayView(57);
            }
        });

        lin_cngpropic = (LinearLayout) rootView.findViewById(R.id.lin_cngpropic);
        lin_cngpropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerMain) getActivity()).displayView(58);
            }
        });

        linlayfeedback = (LinearLayout) rootView.findViewById(R.id.linlayfeedback);
        linlayfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerMain) getActivity()).displayView(33);
            }
        });

        linlayprivacy = (LinearLayout) rootView.findViewById(R.id.linlayprivacy);
        linlayprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.base_url)+"privacy_policy";
                try {

                    Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent1);

                }
                catch(
                        ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        linlayabout = (LinearLayout) rootView.findViewById(R.id.linlayabout);
        linlayabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "App Version:"+SharePreference.getAppVersion(getActivity()), Toast.LENGTH_SHORT).show();

            }
        });

        linlaypermission = (LinearLayout) rootView.findViewById(R.id.linlaypermission);
        linlaypermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "'Camera','Internet','Storage' ", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

}
