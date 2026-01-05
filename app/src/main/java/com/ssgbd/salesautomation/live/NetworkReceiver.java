//package com.ssgbd.salesautomation.live;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//
//import androidx.core.content.ContextCompat;
//
//public class NetworkReceiver  extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//
//        if (isConnected) {
//            Intent serviceIntent = new Intent(context, LocationForegroundService.class);
//            serviceIntent.setAction("SEND_PENDING_LOCATIONS");
//            ContextCompat.startForegroundService(context, serviceIntent);
//        }
//    }
//}
