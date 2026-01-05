package com.ssgbd.salesautomation.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class EndPoint {



    public static String LOGIN = "api/v2/login";
    public static String ROUTE = "api/v2/route";

}
