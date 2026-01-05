package com.ssgbd.salesautomation.drawer.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ProductCategoryRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.StockListAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ForecastNewProductFragment extends Fragment {

    View rootView;
    // product category list
    private int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PICTURE = 1;
    boolean isCamere;
    ImageView img_view;
    private Bitmap bitmap;
    VolleyMethods vm = new VolleyMethods();
    JsonRequestFormate jrf = new JsonRequestFormate();
    TextView txt_browse;
    EditText edt_product_name,edt_mrp,edt_qty;
Button btn_submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.forceast_new_product, container, false);
        img_view = (ImageView) rootView.findViewById(R.id.img_view);

        edt_product_name = (EditText) rootView.findViewById(R.id.edt_product_name);
        edt_mrp = (EditText) rootView.findViewById(R.id.edt_mrp);
        edt_qty = (EditText) rootView.findViewById(R.id.edt_qty);


        txt_browse = (TextView) rootView.findViewById(R.id.txt_browse);
        txt_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    //do your check herere44q2z
                    if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

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
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_qty.getText().toString().equalsIgnoreCase("")||edt_mrp.getText().toString().equalsIgnoreCase("")||edt_product_name.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "প্রোডাক্ট এর নাম কোয়ান্টিটি সঠিক ভাবে লিখুন।", Toast.LENGTH_SHORT).show();
                    
                }else {
                    if (bitmap==null){
                        Toast.makeText(getActivity(), "প্রোডাক্ট এর ছবি তুলুন।", Toast.LENGTH_SHORT).show();

                    }else {
                    sendImage(edt_qty.getText().toString(),edt_mrp.getText().toString(),edt_product_name.getText().toString());
                }
                }

            }
        });

        return rootView;
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void takePhoto() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, TAKE_PICTURE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
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
    public void sendImage(String salesQty,String mrp,String name){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url)+"api/new_product_forecast_insert", jrf.newProduct(getEncodedImage(bitmap),SharePreference.getUserId(getActivity()),salesQty,mrp,name), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                try {

                     //    Log.e("regBody>>>",result+"<<");
                    // {"success":true,"message":"Image uploaded sucessfully."}

                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        //  imageShow();
                        edt_mrp.setText("");
                        edt_product_name.setText("");
                        edt_qty.setText("");
                        bitmap = null;
                        Picasso.with(getActivity())
                                .load(R.mipmap.ssg_logo)
                                .placeholder(R.mipmap.ssg_logo).into(img_view);

                    }else {

                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }
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
    public void getStockList(String catId){
        JsonRequestFormate jp = new JsonRequestFormate();
        VolleyMethods vm = new VolleyMethods();

        vm.sendRequestToServer2(getActivity(), getString(R.string.base_url)+"api/stock-list", jp.stockList(SharePreference.getUserId(getActivity()),catId,SharePreference.getUserPointId(getActivity())), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {

              //  Log.e("statusArray",result+"statusArray");

                try {
                    JSONObject respjsonObj = new JSONObject(result);
                    JSONArray statusArray = respjsonObj.getJSONArray("stockList");
                 //   Log.e("statusArray",statusArray+"statusArray");

                } catch (JSONException je) {
                }
               // Log.e("size",stockDTOS.size()+"statusArray");
            }

        });
    }
}
