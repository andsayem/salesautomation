package com.ssgbd.salesautomation.drawer.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.RouteRecyclerAdapter;
import com.ssgbd.salesautomation.database.DatabaseHelper;
import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.listner.RecyclerClickListener;
import com.ssgbd.salesautomation.utils.SharePreference;
import com.ssgbd.salesautomation.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class NewRetailerFragment extends Fragment {

    View rootView;
    Spinner spinnerOfferType;
    ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
  //  private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    private static String DB_PATH = Utility.DB_PATH;
    private static String DB_NAME = "ssg.db";
    DatabaseHelper databaseHelper;
    private Dialog wdialog;

    RouteRecyclerAdapter routeRecyclerAdapter;

    TextView txt_sapcode,txt_divisionname,txt_poientname,txt_territory_name,txt_route_list;
    EditText edt_txt_retailername,edt_txt_ownername,edt_txt_address,edt_txt_mobile,edt_txt_tntphone,edt_txt_email,edttext_dob;
    LinearLayout linlay_route,linlaydate;

    String statusId="";
    String routeId="";
    String shopType="";
    Button button_send_request;
    VolleyMethods vm = new VolleyMethods();

    JSONObject finalobject;
    JSONObject retailerobject;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.new_retailer_fragment, container, false);



        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {


        databaseHelper = new DatabaseHelper(getActivity());
        try {
            databaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myPath = DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()){
            databaseHelper.openDataBase();}



        CategoryDTO c = new CategoryDTO();
        c.setId("0");
        c.setName("END SHOP");
        categoryDTOS.add(c);

        CategoryDTO c1 = new CategoryDTO();
        c1.setId("1");
        c1.setName("DELAER");
        categoryDTOS.add(c1);




        spinnerOfferType = (Spinner) rootView.findViewById(R.id.spinnerOfferType);
        NewRetailerFragment.CustomArrayAdapter adapter = new NewRetailerFragment.CustomArrayAdapter(getActivity(),
                R.layout.customspinneritem, categoryDTOS);

        spinnerOfferType.setAdapter(adapter);

        spinnerOfferType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.offer_type_txt)).getText().toString();

           //     Log.e(">>>><<>>",categoryDTOS.get(pos).getId()+"<<>>"+categoryDTOS.get(pos).getName()+"");
                shopType = categoryDTOS.get(pos).getId();
             //   Log.e(">>>shoptype><<>>",shopType+"");
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        edt_txt_retailername = (EditText) rootView.findViewById(R.id.edt_txt_retailername);
        edt_txt_ownername = (EditText) rootView.findViewById(R.id.edt_txt_ownername);
        edt_txt_address = (EditText) rootView.findViewById(R.id.edt_txt_address);
        edt_txt_mobile = (EditText) rootView.findViewById(R.id.edt_txt_mobile);
        edt_txt_tntphone = (EditText) rootView.findViewById(R.id.edt_txt_tntphone);
        edt_txt_email = (EditText) rootView.findViewById(R.id.edt_txt_email);
        edttext_dob = (EditText) rootView.findViewById(R.id.edttext_dob);
        linlaydate = (LinearLayout)rootView.findViewById(R.id.linlaydate);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        linlaydate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        txt_sapcode = (TextView) rootView.findViewById(R.id.txt_sapcode);
        txt_sapcode.setText(SharePreference.getSapCode(getActivity()));

        txt_divisionname = (TextView) rootView.findViewById(R.id.txt_divisionname);
        txt_divisionname.setText(SharePreference.getDivisionName(getActivity()));

        txt_route_list = (TextView) rootView.findViewById(R.id.txt_route_list);

        txt_poientname = (TextView) rootView.findViewById(R.id.txt_poientname);
        txt_poientname.setText(SharePreference.getUserPointName(getActivity()));

        txt_territory_name = (TextView) rootView.findViewById(R.id.txt_territory_name);
        txt_territory_name.setText(SharePreference.getTerritoryName(getActivity()));

        linlay_route = (LinearLayout) rootView.findViewById(R.id.linlay_route);
        linlay_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRouteListDialog();
            }
        });

        button_send_request = (Button) rootView.findViewById(R.id.button_send_request);
        button_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_txt_retailername.getText().length()==0){
                    Toast.makeText(getActivity(), "Please Enter Retailer Name", Toast.LENGTH_SHORT).show();
                }else if(edt_txt_ownername.getText().length()==0){
                    Toast.makeText(getActivity(), "Please Enter Owner Name", Toast.LENGTH_SHORT).show();

                }else if(edt_txt_address.getText().length()==0){
                    Toast.makeText(getActivity(), "Please Enter Shop Address", Toast.LENGTH_SHORT).show();

                }else if(routeId.length()==0){
                    Toast.makeText(getActivity(), "Please Select a Route", Toast.LENGTH_SHORT).show();

                }else if(edt_txt_mobile.getText().length()==0){
                    Toast.makeText(getActivity(), "Please Enter a Aalid Phone Number", Toast.LENGTH_SHORT).show();

                }else {

                    try {
                        finalobject = new JSONObject();
                        retailerobject = new JSONObject();
                        retailerobject.put("retailerName", edt_txt_retailername.getText().toString());
                        retailerobject.put("division", SharePreference.getDivisionId(getActivity()));
                        retailerobject.put("territory", SharePreference.getTerritoryId(getActivity()));
                        retailerobject.put("routeid", routeId);
                        retailerobject.put("point_id", SharePreference.getUserPointId(getActivity()));
                        retailerobject.put("shop_type", shopType);
                        retailerobject.put("owner", edt_txt_ownername.getText().toString());
                        retailerobject.put("mobile","88"+ edt_txt_mobile.getText().toString());
                        retailerobject.put("tnt", edt_txt_tntphone.getText().toString());
                        retailerobject.put("email", edt_txt_email.getText().toString());
                        retailerobject.put("retailerAddress", edt_txt_address.getText().toString());
                        retailerobject.put("userid", SharePreference.getUserId(getActivity()));
                        retailerobject.put("global_company_id", SharePreference.getUserGlobalId(getActivity()));

                        finalobject.put("retailer_info", retailerobject);

                   //     Log.e("finalobject>>", finalobject + "");

                        sendRequest();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edttext_dob.setText(sdf.format(myCalendar.getTime()));
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


    public void sendRequest(){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        vm.sendRequestToServer2(getActivity(), getResources().getString(R.string.base_url)+"api/apps/new-retailer-submit", finalobject.toString(), new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                pd.dismiss();
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
//
                //    Log.e("response>> inactiv>",jsonObject1.getString("message")+"");

                    String s = jsonObject1.getString("message");

                    statusId="";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    private void showRouteListDialog() {
        wdialog =new Dialog(getActivity());
        wdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wdialog.setContentView(R.layout.dialog_route_list);
        final RecyclerView route_list_recyclerView;
        final Button btnDone;
        final ImageView imbtnCross;
        final EditText etSearch;

        final RelativeLayout rlDialogCross;

        Utility.routeDTOS.clear();
        try {
            JSONObject respjsonObj = new JSONObject(SharePreference.getRouteData(getActivity()));
            JSONArray routeArray = respjsonObj.getJSONArray("route");
            for (int i = 0; i < routeArray.length(); i++) {
                JSONObject routeObject = routeArray.getJSONObject(i);
                RouteDTO routeDTO = new RouteDTO();
                routeDTO.setPoint_id(routeObject.getString("point_id"));
                routeDTO.setTerritory_id(routeObject.getString("territory_id"));
                routeDTO.setRname(routeObject.getString("rname"));
                routeDTO.setRoute_id(routeObject.getString("route_id"));
                Utility.routeDTOS.add(routeDTO);
            }
        }catch (JSONException je){
        }


        etSearch = (EditText)wdialog.findViewById(R.id.edt_txt_search);
        route_list_recyclerView=(RecyclerView) wdialog.findViewById(R.id.route_list_recyclerView);
        imbtnCross=(ImageView)wdialog.findViewById(R.id.btn_dialog_cross);
        rlDialogCross = (RelativeLayout)wdialog.findViewById(R.id.rl_dialog_cross);
        btnDone = (Button)wdialog.findViewById(R.id.btnDoneDialog);

        wdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        imbtnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wdialog.dismiss();
            }
        });
        rlDialogCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdialog.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        route_list_recyclerView.setLayoutManager(linearLayoutManager);
        routeRecyclerAdapter = new RouteRecyclerAdapter( Utility.routeDTOS,getActivity());
        route_list_recyclerView.setAdapter(routeRecyclerAdapter);

        route_list_recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                txt_route_list.setText("Route Name-"+ Utility.routeDTOS.get(position).getRname());
                routeId = Utility.routeDTOS.get(position).getRoute_id();


                wdialog.dismiss();
            }
        }));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = etSearch.getText().toString().toLowerCase();
                routeRecyclerAdapter.filter(query);
            }
        });

        wdialog.show();
    }
}
