package com.ssgbd.salesautomation.report;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.adapters.DeportPaymentReportAdapter;
import com.ssgbd.salesautomation.dtos.DeportPaymentDTO;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.http.json_request_formate.JsonRequestFormate;
import com.ssgbd.salesautomation.http.volly_method.VolleyMethods;
import com.ssgbd.salesautomation.utils.SharePreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DeportPaymentReportFragment extends Fragment {

    private View rootView;
    private VolleyMethods vm = new VolleyMethods();
    private DeportPaymentReportAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<DeportPaymentDTO> reportList = new ArrayList<>();
    private TextView txtFromDate, txtToDate, txtSearch, txtTotalCount;
    private String FROMDATE, TODATE;
    private DatePickerDialog picker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.deport_payment_report_fragment, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_depot_payment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        txtFromDate = rootView.findViewById(R.id.txt_fromdate);
        txtToDate = rootView.findViewById(R.id.txt_todate);
        txtSearch = rootView.findViewById(R.id.txt_search);
        txtTotalCount = rootView.findViewById(R.id.txt_total_count);

        adapter = new DeportPaymentReportAdapter(reportList, getActivity());
        recyclerView.setAdapter(adapter);

        // Initialize dates: start of month to today
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");

        String todayStr = String.format("%04d-%02d-%02d", year, month + 1, day);
        String startMonthStr = String.format("%04d-%02d-01", year, month + 1);

        FROMDATE = startMonthStr;
        TODATE = todayStr;

        txtFromDate.setText("1 " + monthFormat.format(new Date()) + " " + year);
        txtToDate.setText(day + " " + monthFormat.format(new Date()) + " " + year);

        // Date pickers
        txtFromDate.setOnClickListener(v -> showDatePicker(true));
        txtToDate.setOnClickListener(v -> showDatePicker(false));

        // Search button
        txtSearch.setOnClickListener(v -> getReport(FROMDATE, TODATE));

        // Initial load
        getReport(FROMDATE, TODATE);

        return rootView;
    }

    private void showDatePicker(boolean isFrom) {
        Calendar cal = Calendar.getInstance();
        picker = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String dayStr = String.format("%02d", dayOfMonth);
                    String monthStr = String.format("%02d", monthOfYear + 1);
                    String date = year + "-" + monthStr + "-" + dayStr;

                    if (isFrom) {
                        FROMDATE = date;
                        txtFromDate.setText(dayOfMonth + " " + getMonthName(monthOfYear) + " " + year);
                    } else {
                        TODATE = date;
                        txtToDate.setText(dayOfMonth + " " + getMonthName(monthOfYear) + " " + year);
                    }
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    private String getMonthName(int month) {
        String[] months = getActivity().getResources().getStringArray(R.array.month_names);
        return (month >= 0 && month < months.length) ? months[month] : "";
    }

    private void getReport(String fromDate, String toDate) {
        reportList.clear();
        adapter.notifyDataSetChanged();

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        JsonRequestFormate jp = new JsonRequestFormate();
        vm.sendRequestToServer2(getActivity(),
                getString(R.string.base_url) + "api/apps/depot-payment-list",
                jp.jsonReportDeportPayment( SharePreference.getUserLoginId(getActivity()), fromDate, toDate),
                new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {

                        pd.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray arr = jsonObject.getJSONArray("results");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                DeportPaymentDTO dto = new DeportPaymentDTO();
                                dto.setPointName(obj.optString("point_name", ""));
                                dto.setSapCode(Integer.parseInt(obj.optString("sap_code", "")));
                                dto.setCollectionType(obj.optString("collection_type", ""));
                                dto.setAmount(Double.parseDouble(obj.optString("trans_amount", "0")));
                                dto.setPaymentType(obj.optString("payment_type", ""));
                                dto.setDate(obj.optString("trans_date", ""));
                                dto.setAckStatus(obj.optString("ack_status", ""));
                                dto.setBankSlipUrl(obj.optString("bank_deposit_slip", "")); // কোনো public/base URL add করবেন না
                                reportList.add(dto);
                            }

                            adapter.notifyDataSetChanged();
                            txtTotalCount.setText("About: " + reportList.size() + " results");

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Data parse error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}