package com.ssgbd.salesautomation.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by DELL on 4/18/2018.
 */

public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

//        if (intent.getAction().equals(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE)) {
//            Log.e("ACTION", "ACTION_DATE_CHANGED received");
//        }

        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {

        //    Log.e("received", "ACTION_DATE_CHANGED");
            Toast.makeText(context, "ACTION_DATE_CHANGED", Toast.LENGTH_SHORT).show();


            SharePreference.setButtonText(context,"Check In");
        }
    }

}