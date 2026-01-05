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


public class ChangeProfileFragment extends Fragment {

    View rootView;
    TextView txt_browse;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PICTURE = 1;
    boolean isCamere;
    ImageView img_view;
    private Bitmap bitmap;
    VolleyMethods vm = new VolleyMethods();
    JsonRequestFormate jrf = new JsonRequestFormate();
    Button btn_send;
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
         rootView = inflater.inflate(R.layout.change_profile_picture_fragment, container, false);

        txt_back = (TextView) rootView.findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerMain) getActivity()).displayView(29);
            }
        });

        img_view = (ImageView) rootView.findViewById(R.id.img_view);
        img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        txt_browse = (TextView) rootView.findViewById(R.id.txt_browse);
        txt_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    //do your check herere44q2z
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setTitle("Alert").setMessage("Please select your desire option.")
                                .setNegativeButton(" ", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub

                                        showFileChooser();
                                        isCamere = false;
                                    }
                                }).setPositiveButton(" ", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        takePhoto();
                                        isCamere = true;
                                    }
                                }).create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                Button buttonNegetive = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                                // if you do the following it will be left aligned, doesn't look
                                // correct
                                // button.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play,
                                // 0, 0, 0);

                                Drawable drawable = getActivity().getResources().getDrawable(
                                        R.mipmap.ic_camere);
                                Drawable drawableN = getActivity().getResources().getDrawable(
                                        R.mipmap.ic_galary);

                                // set the bounds to place the drawable a bit right
                                drawable.setBounds((int) (drawable.getIntrinsicWidth() * 0.5),
                                        0, (int) (drawable.getIntrinsicWidth() * 1.5),
                                        drawable.getIntrinsicHeight());
                                button.setCompoundDrawables(drawable, null, null, null);

                                // set the bounds to place the drawable a bit right
                                drawableN.setBounds((int) (drawable.getIntrinsicWidth() * 0.5),
                                        0, (int) (drawable.getIntrinsicWidth() * 1.5),
                                        drawable.getIntrinsicHeight());
                                button.setCompoundDrawables(drawable, null, null, null);

                                buttonNegetive.setCompoundDrawables(drawableN, null, null, null);
                            }
                        });
                        dialog.show();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                    }
                } else {
                }

            }
        });

        btn_send = (Button) rootView.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(bitmap==null){
                    Toast.makeText(getActivity(), "আগে একটি ছবি সিলেক্ট করুন।", Toast.LENGTH_SHORT).show();
                }else {
                    sendImage();    
                }
               
            }
        });



        imageShow();

        return rootView;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void takePhoto() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, TAKE_PICTURE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (isCamere == true) {
            if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
               // Log.e("photo>>", photo + "<<");
                img_view.setImageBitmap(photo);


                bitmap = ((BitmapDrawable) img_view.getDrawable()).getBitmap();

                Utility.bitmap = photo;
            }
        } else {


            try {
                Uri filePath = data.getData();
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                if (bitmap.getWidth() > bitmap.getHeight()) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
             //   Log.e("bitmap", bitmap + "camera");

                //Setting the Bitmap to ImageView
                img_view.setImageBitmap(bitmap);
                Utility.bitmap = bitmap;
            } catch (IOException | NullPointerException ne) {
                ne.printStackTrace();
            }
        }
    }

    public void sendImage(){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url)+"api/profile-pic-upload", jrf.imageFormat(SharePreference.getUserLoginId(getActivity()),getEncodedImage(bitmap)), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {



               //     Log.e(">>",result+"<<");
                   // {"success":true,"message":"Image uploaded sucessfully."}


                    JSONObject jsonObject = new JSONObject(result);

                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                  //  imageShow();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        });
    }
    public String getEncodedImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

    public void imageShow() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url) + "api/get-profile-pic", jrf.getimageformate(SharePreference.getUserLoginId(getActivity())), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {

                    JSONObject jsonObject = new JSONObject(result);

                    Picasso.with(getActivity())
                            .load(getString(R.string.base_url) + jsonObject.getString("image"))
                            .placeholder(R.mipmap.user).into(img_view);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        });

    }
}
