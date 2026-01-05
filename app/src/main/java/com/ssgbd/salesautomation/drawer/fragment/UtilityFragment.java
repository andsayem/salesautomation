package com.ssgbd.salesautomation.drawer.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.RetailerRecyclerAdapter_Return_Change;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.adapters.UtilityListRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.dtos.UtilityListDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//import static com.google.android.gms.internal.zzahg.runOnUiThread;


public class UtilityFragment extends Fragment implements View.OnClickListener{

    View rootView;
    private Dialog wdialog;
    RouteRecyclerAdapter routeRecyclerAdapter;
    TextView txt_route_name;
    UtilityListRecyclerAdapter retialer_adapter;
    RecyclerView retailer_list_recyclerView;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<CategoryDTO> categoryDTOS1 = new ArrayList<>();
    String statusId="";

   // private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    LinearLayoutManager linearLayoutManager1;
    ArrayList<UtilityListDTO> utilityListDTOS = new ArrayList<>();
    VolleyMethods vm = new VolleyMethods();

    String routeId="";
    Spinner spinnerOfferType,spinner_reason;
    Button btn_add;
EditText edt_suggestion;
    public RequestQueue queue;
    String REASONID="";
    String REASON_NAME="";
    String SUGGESTION="";
    String REASON_CATEGORY_NAME="";
    String REASON_CATEGORY_ID="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.utility_fragment, container, false);




        retailer_list_recyclerView = (RecyclerView) rootView.findViewById(R.id.retailer_list_recyclerView);
        linearLayoutManager1  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        retailer_list_recyclerView.setLayoutManager(linearLayoutManager1);
        retialer_adapter = new UtilityListRecyclerAdapter( utilityListDTOS,getActivity());
        retailer_list_recyclerView.setAdapter(retialer_adapter);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        edt_suggestion = (EditText) rootView.findViewById(R.id.edt_suggestion);
        initUi();


        return rootView;
    }

    private void initUi() {
        queue = Volley.newRequestQueue(getActivity());
        txt_route_name = (TextView) rootView.findViewById(R.id.txt_route_name);


        CategoryDTO c = new CategoryDTO();
        c.setId("2");
        c.setName("--Select One--");
        categoryDTOS.add(c);

        CategoryDTO c1 = new CategoryDTO();
        c1.setId("1");
        c1.setName("SUGGESTION");
        categoryDTOS.add(c1);

        CategoryDTO c2 = new CategoryDTO();
        c2.setId("0");
        c2.setName("PROBLEM");
        categoryDTOS.add(c2);

        CategoryDTO c3 = new CategoryDTO();
        c3.setId("1");
        c3.setName("SERVICE");
        categoryDTOS.add(c3);

        spinnerOfferType = (Spinner) rootView.findViewById(R.id.spinnerOfferType);
        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
                R.layout.customspinneritem, categoryDTOS);

        spinnerOfferType.setAdapter(adapter);

        spinnerOfferType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

          //      Log.e(">>>><<>>",categoryDTOS.get(pos).getName()+"<<>>"+categoryDTOS.get(pos).getId()+"");
                statusId = categoryDTOS.get(pos).getId();
                REASON_NAME =categoryDTOS.get(pos).getName();
                loadReason(statusId);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        getUtilityList();

    }

    private void loadReason(String id) {

        categoryDTOS1.clear();

        if (id.equalsIgnoreCase("0")) {
            CategoryDTO c11 = new CategoryDTO();
            c11.setId("5");
            c11.setName("SICK");
            categoryDTOS1.add(c11);

            CategoryDTO c21 = new CategoryDTO();
            c21.setId("6");
            c21.setName("PERSONAL PROBLEM");
            categoryDTOS1.add(c21);

            CategoryDTO c31 = new CategoryDTO();
            c31.setId("7");
            c31.setName("OTHERS");
            categoryDTOS1.add(c31);

        }else {

            CategoryDTO cm = new CategoryDTO();
            cm.setId("2");
            cm.setName("--No Reason--");
            categoryDTOS1.add(cm);
        }

        spinner_reason = (Spinner) rootView.findViewById(R.id.spinner_reason);
        CustomArrayAdapter adapter1 = new CustomArrayAdapter(getActivity(),
                R.layout.customspinneritem, categoryDTOS1);

        spinner_reason.setAdapter(adapter1);

        spinner_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

                statusId = categoryDTOS1.get(pos).getId();
                statusId = categoryDTOS1.get(pos).getId();
                REASON_CATEGORY_NAME = categoryDTOS1.get(pos).getName();
                REASON_CATEGORY_ID = categoryDTOS1.get(pos).getId();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public class CustomArrayAdapter extends ArrayAdapter<String> {

        private final LayoutInflater mInflater;
        private final Context mContext;
        private final ArrayList<CategoryDTO> items;
        private final int mResource;

        public CustomArrayAdapter(@NonNull Context context, @LayoutRes int resource,
                                  @NonNull ArrayList objects) {
            super(context, resource, 0, objects);

            mContext = context;
            mInflater = LayoutInflater.from(context);
            mResource = resource;
            items = objects;
        }
        @Override
        public View getDropDownView(int position, @Nullable View convertView,
                                    @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        @Override
        public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        private View createItemView(int position, View convertView, ViewGroup parent){
            final View view = mInflater.inflate(mResource, parent, false);

            TextView offTypeTv = (TextView) view.findViewById(R.id.offer_type_txt);

            CategoryDTO offerData = items.get(position);

            offTypeTv.setText(items.get(position).getName());

            return view;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_add:

                if (edt_suggestion.getText().length()==0){
                    Toast.makeText(getActivity(), "please fill suggestion field.", Toast.LENGTH_SHORT).show();

                }else {
                    setAddUtility(REASON_CATEGORY_ID, REASON_NAME, edt_suggestion.getText().toString());
                }
                break;

        }
    }


    public void getUtilityList(){
        utilityListDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();

        vm.sendRequestToServer2(getActivity(),  getResources().getString(R.string.base_url)+getResources().getString(R.string.utility_list_url),
                jp.jsonGetUtilityList(SharePreference.getUserId(getActivity())), new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        pd.dismiss();
                        try {
                            JSONObject jsonObject1 = new JSONObject(result);
                           // Log.e("resp>>",jsonObject1+"");
                            JSONArray jsonArray = jsonObject1.getJSONArray("utility_list");

                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UtilityListDTO listDTO = new UtilityListDTO();
                                listDTO.setDate(jsonObject.getString("date"));
                                listDTO.setType(jsonObject.getString("type"));
                                listDTO.setReason(optStringNullCheck(jsonObject,"reason"));
                                listDTO.setRemarks(jsonObject.getString("remarks"));
                                utilityListDTOS.add(listDTO);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                retialer_adapter.notifyDataSetChanged();

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


    public void setAddUtility(String reason,String type,String suggestion){
        utilityListDTOS.clear();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        JsonRequestFormate jp = new JsonRequestFormate();

        vm.sendRequestToServer2(getActivity(),  getResources().getString(R.string.base_url)+"api/apps/utility-submit",
                jp.submitUtioityData(reason,type,suggestion,SharePreference.getUserId(getActivity())), new VolleyCallBack() {
                    @Override
                    public void onSuccess(final String result) {
                        pd.dismiss();



                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                                    Toast.makeText(getActivity(), jsonObject1.optString("message"), Toast.LENGTH_SHORT).show();


                                    if (jsonObject1.optString("status").equalsIgnoreCase("0")) {
                                        getUtilityList();
                                        retialer_adapter.notifyDataSetChanged();
                                        edt_suggestion.setText("");
                                        REASON_NAME = "";
                                        SUGGESTION = "";
                                        REASON_CATEGORY_NAME = "";
                                        REASON_CATEGORY_ID = "";
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
