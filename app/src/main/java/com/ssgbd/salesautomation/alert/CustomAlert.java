package com.ssgbd.salesautomation.alert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ssgbd.salesautomation.R;

public class CustomAlert {


    public void internetAlert(final Context context) {
        final AlertDialog.Builder alertBulder = new AlertDialog.Builder(context);
        alertBulder.setIcon(context.getResources().getDrawable(R.mipmap.ssg_logo)).setTitle("Internet Alert").setMessage("Your device is not connected to internet. Connect to internet and try again.");

        alertBulder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        alertBulder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertBulder.setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        alertBulder.show();
    }

    public  boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
