package com.ssgbd.salesautomation.report;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.ProductRequisitionAdapter;
import com.ssgbd.salesautomation.dtos.ProductRequisitionDTO;
import com.ssgbd.salesautomation.dtos.ReasonDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProductRequisitionReportFragment extends Fragment {

    private View rootView;
    private VolleyMethods vm = new VolleyMethods();

    private RecyclerView recyclerView;
    private ProductRequisitionAdapter adapter;

    private ArrayList<ProductRequisitionDTO> reportList = new ArrayList<>();
    private ArrayList<ReasonDTO> reasonList = new ArrayList<>();

    private TextView txtFromDate, txtToDate, txtSearch, txtTotalCount;
    private LinearLayout headerReasonContainer;

    private String FROMDATE, TODATE;
    private DatePickerDialog picker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.product_requisition_fragment, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        txtFromDate = rootView.findViewById(R.id.txt_fromdate);
        txtToDate = rootView.findViewById(R.id.txt_todate);
        txtSearch = rootView.findViewById(R.id.txt_search);
        txtTotalCount = rootView.findViewById(R.id.txt_total_count);
        headerReasonContainer = rootView.findViewById(R.id.header_reason_container);

        adapter = new ProductRequisitionAdapter(reportList, reasonList, getActivity());
        recyclerView.setAdapter(adapter);

        initDate();

        txtFromDate.setOnClickListener(v -> showDatePicker(true));
        txtToDate.setOnClickListener(v -> showDatePicker(false));
        txtSearch.setOnClickListener(v -> getReport(FROMDATE, TODATE));

        // Initial Load
        getReport(FROMDATE, TODATE);

        return rootView;
    }

    private void initDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        cal.set(Calendar.DAY_OF_MONTH, 1);
        FROMDATE = sdf.format(cal.getTime());

        cal = Calendar.getInstance();
        TODATE = sdf.format(cal.getTime());

        txtFromDate.setText(FROMDATE);
        txtToDate.setText(TODATE);
    }

    private void showDatePicker(boolean isFrom) {
        Calendar cal = Calendar.getInstance();

        picker = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {

                    Calendar selected = Calendar.getInstance();
                    selected.set(year, monthOfYear, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(selected.getTime());

                    if (isFrom) {
                        FROMDATE = formattedDate;
                        txtFromDate.setText(FROMDATE);
                    } else {
                        TODATE = formattedDate;
                        txtToDate.setText(TODATE);
                    }

                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        picker.show();
    }

    private void getReport(String fromDate, String toDate) {

        reportList.clear();
        reasonList.clear();
        adapter.notifyDataSetChanged();
        headerReasonContainer.removeAllViews();

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        JsonRequestFormate jp = new JsonRequestFormate();

        vm.sendRequestToServer2(getActivity(),
                getString(R.string.base_url) + "api/apps/product-requisition-report-list",
                jp.jsonProductRequisition(
                        SharePreference.getUserId(getActivity()),
                        fromDate,
                        toDate,
                        ""
                ),
                new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {

                        pd.dismiss();
                        Log.e("API_RESPONSE", result);

                        try {
                            JSONArray jsonArray = new JSONArray(result);

                            // ========= Global reasons =========
                            if (jsonArray.length() > 0) {
                                JSONArray reasons = jsonArray.getJSONObject(0).optJSONArray("reasons");
                                if (reasons != null) {
                                    for (int j = 0; j < reasons.length(); j++) {
                                        JSONObject r = reasons.getJSONObject(j);
                                        ReasonDTO reasonDTO = new ReasonDTO(
                                                r.optInt("id"),
                                                r.optString("reason"),
                                                r.optInt("diductionQty")
                                        );
                                        reasonList.add(reasonDTO);

                                        // Add header TextView dynamically
                                        TextView tv = new TextView(getActivity());
                                        tv.setLayoutParams(new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT
                                        ));
                                        tv.setText(r.optString("reason"));
                                        tv.setPadding(16, 0, 16, 0);
                                        tv.setTextSize(12);
                                        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                        headerReasonContainer.addView(tv);
                                    }
                                }
                            }

                            // ========= Parse each product row =========
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                ProductRequisitionDTO dto = new ProductRequisitionDTO();
                                dto.setCatName(obj.optString("catName"));
                                dto.setProductName(obj.optString("name"));
                                dto.setSapCode(obj.optString("sap_code"));
                                dto.setReqQty(obj.optDouble("reqQty", 0));
                                dto.setReqValue(obj.optDouble("reqValue", 0));
                                dto.setBilledQty(obj.optDouble("billingQty", 0));
                                dto.setBilledValue(obj.optDouble("billingValue", 0));
                                dto.setPointName(obj.optString("point_name"));
                                dto.setFreeDownQty(obj.optDouble("free_down_qty", 0));

                                // Product-level reasons
                                JSONArray prodReasons = obj.optJSONArray("reasons");
                                if (prodReasons != null) {
                                    for (int k = 0; k < prodReasons.length(); k++) {
                                        JSONObject r = prodReasons.getJSONObject(k);
                                        dto.addReasonQty(
                                                r.optInt("id"),
                                                r.optInt("diductionQty")
                                        );
                                    }
                                }

                                reportList.add(dto);
                            }

                            adapter.notifyDataSetChanged();
                            txtTotalCount.setText("Total: " + reportList.size());

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Data parse error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}