package com.ssgbd.salesautomation.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.EditText;


import com.ssgbd.salesautomation.R;
import com.ssgbd.salesautomation.dtos.RouteDTO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rashed on 4/13/2017.
 */

public class Utility {

    public static ArrayList<RouteDTO> routeDTOS = new ArrayList<>();

    // live db path
    public static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
    //test db path
//    public static String DB_PATH = "/data/data/com.ssgbd.salesautomation_test/databases/";
//  public static String DB_PATH =Utility.DB_PATH;
    public static int TAB_ITEM_NUMBER = 0;
    public static String ROUTE_ID = "";
    public static String ROUTE_NAME = "";
    public static String package_ = "com.ssgbd.salesautomation";
//  public static String package_ = "com.ssgbd.salesautomation_test";
    public static Bitmap bitmap;
 // visit non visit

    public static String V_RETAILER_ID = "";
    public static String V_RETAILER_NAME = "";
    public static String V_ROUTE_ID = "";
    public static String LatestRoute = "";
    public static String hpwd = "ds#jhmgs$543DBA";

    public static Calendar cldr=Calendar.getInstance();
    public static int to_day=cldr.get(Calendar.DAY_OF_MONTH);
    public static int current_month=cldr.get(Calendar.MONTH);
    public static int current_year=cldr.get(Calendar.YEAR);

  public static   String month_name=(String)android.text.format.DateFormat.format("MMMM", new Date());
      //  txt_fromdate.setText("1"+" "+monthname+" "+String.valueOf(year) );

    // for alert box
    public static void showAlert(Context context, String titleText, String message, String buttonName) {
        final AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.search_normal)).setTitle(titleText).setMessage(message);

        alertBulder.setPositiveButton(buttonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertBulder.show();
    }

    // all email validation check
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean  checkEmail(Context context, String alertTitle, String message, EditText editText) {
        String email = editText.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {

            showAlert(context,alertTitle,message,"OK");
            return false;
        }
        return true;
    }
    public static String optStringNullCheck(final JSONObject json, final String key) {
        if (json.isNull(key)||json.optString(key).equalsIgnoreCase("null"))
            return "";
        else
            return json.optString(key, key);
    }
    // all field empty check

    public static boolean emptyCheck(Context context, String title, String message, EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {

            Utility.showAlert(context,title,message,"OK");
            return false;
        }
        return true;
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static  void datePicker(final EditText editText, Context context) {
        Calendar dateCalender = Calendar.getInstance();
        int year = dateCalender.get(Calendar.YEAR);
        int month = dateCalender.get(Calendar.MONTH);
        int day = dateCalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                //2001-05-03
                editText.setText( year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.setTitle("SELECT DATE");
        datePickerDialog.show();
    }
    public static void internetAlert(final Context context) {
        final AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("ইন্টারনেট এলার্ট").setMessage("আপনার ডিভাইস ইন্টারনেটের সাথে সংযুক্ত নেই। ইন্টারনেটে কানেক্ট করুন এবং আবার চেষ্টা করুন।");

        alertBulder.setPositiveButton("ওয়াই-ফাই", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        alertBulder.setNeutralButton("বাতিল", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertBulder.setNegativeButton("মোবাইল ডাটা", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertBulder.show();
    }



}
