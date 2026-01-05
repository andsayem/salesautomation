package com.ssgbd.salesautomation.drawer.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ssgbd.salesautomation.OfferImageShowActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.NoticeListAdapter;
import com.ssgbd.salesautomation.adapters.OfferImageListAdapter;
import com.ssgbd.salesautomation.bucket.BucketAmountWeb;
import com.ssgbd.salesautomation.drawer.SplashActivity;
import com.ssgbd.salesautomation.dtos.NoticeDTO;
import com.ssgbd.salesautomation.dtos.OfferImageDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;
import com.ssgbd.salesautomation.visit.OrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OfferImageFragment extends Fragment {

    View rootView;

    ArrayList<OfferImageDTO> imageDTOS = new ArrayList<>();
    OfferImageListAdapter offerImageListAdapter;
    RecyclerView product_list_recyclerView;

    boolean isImageFitToScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.offer_image_fragment, container, false);

        product_list_recyclerView=(RecyclerView) rootView.findViewById(R.id.product_list_recyclerView);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        product_list_recyclerView.setLayoutManager(linearLayoutManager);
        offerImageListAdapter = new OfferImageListAdapter( imageDTOS,getActivity());
        product_list_recyclerView.setAdapter(offerImageListAdapter);

        product_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getActivity(), OfferImageShowActivity.class);
                intent.putExtra("imagelink",getString(R.string.base_url)+"uploads/offer/"+imageDTOS.get(position).getImageLink());

                startActivity(intent);
            }
        }));

        //offerImage();
        getOfferImage();

        return rootView;
    }


    //https://ssforcedev.ssgbd.com/api/offerimglist

    public void getOfferImage(){
        JsonRequestFormate jp = new JsonRequestFormate();
        VolleyMethods vm = new VolleyMethods();

        vm.sendRequestToServer2(getActivity(), getString(R.string.base_url)+"api/offerimglist", jp.jsonGetOfferImage(SharePreference.getUserBusinessType(getActivity())), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {

               // Log.e("statusArray",result+"statusArray");

                try {
                    JSONObject respjsonObj = new JSONObject(result);
                    JSONArray statusArray = respjsonObj.getJSONArray("img");

                    for (int i = 0; i < statusArray.length(); i++) {
                        JSONObject routeObject = statusArray.getJSONObject(i);
                        OfferImageDTO offerImageDTO = new OfferImageDTO();
                        offerImageDTO.setImageId(routeObject.getString("id"));
                        offerImageDTO.setImageLink(routeObject.getString("images"));
                        offerImageDTO.setImageTitle(routeObject.getString("title"));

                        imageDTOS.add(offerImageDTO);
                    }

                    offerImageListAdapter.notifyDataSetChanged();
                //    Log.e("statusArray",imageDTOS.size()+"notic_list");
                } catch (JSONException je) {
                }

//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    Log.e("offer>>",jsonObject.getString("img")+"");
//                    Picasso.with(getActivity())
//                            .load(jsonObject.getString("img"))
//                            // .resize(500,400)
//                            .placeholder(R.mipmap.offers)
//                            .error(R.mipmap.offers)
//                            .into(imageView );
//                }catch (JSONException je){
//                }

            }

        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

}
