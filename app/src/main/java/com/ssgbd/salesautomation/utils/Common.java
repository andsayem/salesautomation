package com.ssgbd.salesautomation.utils;

import android.content.Context;

import com.ssgbd.salesautomation.R;


public class Common {


    public static String getBaseUrl(Context context) {

        return context.getString(R.string.base_url);

    }


    public static String getApiUrl(Context context) {

        return getBaseUrl(context);

    }

}