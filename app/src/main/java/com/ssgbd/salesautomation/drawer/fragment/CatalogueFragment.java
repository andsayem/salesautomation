package com.ssgbd.salesautomation.drawer.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.OfferImageShowActivity;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.CatalogueListAdapter;
import com.ssgbd.salesautomation.adapters.OfferImageListAdapter;
import com.ssgbd.salesautomation.dtos.CatalogueDTO;
import com.ssgbd.salesautomation.dtos.OfferImageDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CatalogueFragment extends Fragment {

    View rootView;

    ArrayList<CatalogueDTO> catalogueDTOS = new ArrayList<>();
    CatalogueListAdapter catatogueListAdapter;
    RecyclerView product_list_recyclerView;

    boolean isImageFitToScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.catalogue_fragment, container, false);

        product_list_recyclerView=(RecyclerView) rootView.findViewById(R.id.product_list_recyclerView);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        product_list_recyclerView.setLayoutManager(linearLayoutManager);
        catatogueListAdapter = new CatalogueListAdapter( catalogueDTOS,getActivity());
        product_list_recyclerView.setAdapter(catatogueListAdapter);

        product_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Uri path = Uri.parse("https://ssforce.ssgbd.com/uploads/offer/"+catalogueDTOS.get(position).getFile_name());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e) {

                }
            }
        }));

        getAppMode();
        return rootView;
    }

    public void getAppMode(){

        RequestQueue queue;
        //
        String  url = getResources().getString(R.string.base_url)+"api/catalogue?user_type_id="+SharePreference.getUserTypeId(getActivity())+"&business_type_id="+SharePreference.getUserBusinessType(getActivity());
    //    Log.e("url>>",url+"<---");
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.e("response__",response+"");


                            try {
                                JSONObject respjsonObj = new JSONObject(response);
                                JSONArray statusArray = respjsonObj.getJSONArray("info");

                                for (int i = 0; i < statusArray.length(); i++) {
                                    JSONObject routeObject = statusArray.getJSONObject(i);
                                    CatalogueDTO catalogueDTO = new CatalogueDTO();
                                    catalogueDTO.setId(routeObject.getString("id"));
                                    catalogueDTO.setBusiness_type(routeObject.getString("business_type"));
                                    catalogueDTO.setFile_name(routeObject.getString("images"));
                                    catalogueDTO.setStatus(routeObject.getString("status"));

                                    catalogueDTOS.add(catalogueDTO);
                                }

                                catatogueListAdapter.notifyDataSetChanged();

                        }catch (JSONException je){
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                   //     Log.e("error>>",error.toString()+"");
                    }
                }
        ) {
        };
        queue = Volley.newRequestQueue(getActivity());
        queue.getCache().clear();
        postRequest.setShouldCache(false);

        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        //   postRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));

        queue.add(postRequest);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(rootView);
    }

}
