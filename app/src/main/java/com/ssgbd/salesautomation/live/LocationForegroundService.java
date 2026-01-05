//package com.ssgbd.salesautomation.live;
//
//import android.Manifest;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Build;
//import android.os.IBinder;
//import android.os.Looper;
//import android.provider.Settings;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.ssgbd.salesautomation.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class LocationForegroundService extends Service {
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationCallback locationCallback;
//    private VolleySingleton volleySingleton;
//    private String serverUrl = "https://ssforce.ssgbd.com/api/app_notic"; // Replace with your API endpoint
//
//    //sendLocationToServer  this method comments due to further use
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        createLocationCallback();
//        volleySingleton = VolleySingleton.getInstance(this);
//
//    }
//
//
////    @RequiresApi(api = Build.VERSION_CODES.O)
////    @Override
////    public int onStartCommand(Intent intent, int flags, int startId) {
////        startForeground(1, createNotification());
////        requestLocationUpdates();
////        return START_STICKY;
////    }
//
////    @RequiresApi(api = Build.VERSION_CODES.O)
////    private Notification createNotification() {
////        NotificationChannel channel = new NotificationChannel(
////                "location_channel", "Location Service", NotificationManager.IMPORTANCE_LOW
////        );
////        getSystemService(NotificationManager.class).createNotificationChannel(channel);
////
////        return new NotificationCompat.Builder(this, "location_channel")
////                .setContentTitle("Location Service")
////                .setContentText("Tracking your location")
////                .setSmallIcon(R.drawable.qr_code_app_icon)
////                .build();
////    }
//
//
//
//    private void createLocationCallback() {
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                Location location = locationResult.getLastLocation();
//                if (location != null) {
//                    double lat = location.getLatitude();
//                    double lon = location.getLongitude();
//                    long timestamp = System.currentTimeMillis();
//
//                    // Send to server
//                //    sendLocationToServer(lat, lon, timestamp);
//                }
//            }
//        };
//    }
//
//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//
//    private void sendLocationToServer(double latitude, double longitude, long timestamp) {
//
//        if (!isNetworkAvailable()) {
//            // Store location locally to send later
//            saveLocationForLater(latitude, longitude, timestamp);
//            return;
//        }
//
//
//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("user_type_id", "12");
//
////            jsonBody.put("latitude", latitude);
////            jsonBody.put("longitude", longitude);
////            jsonBody.put("timestamp", timestamp);
////   jsonBody.put("device_id", Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID)); // Unique device identifier
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                serverUrl,
//                jsonBody,
//                response -> {
//                    // Handle successful response
//                    Log.d("LocationUpdate", response+"Location sent successfully");
//                },
//                error -> {
//                    // Handle error
//                    Log.e("LocationUpdate", "Error sending location: " + error.getMessage());
//
//                    // Optional: Retry logic
//                    if (error.networkResponse != null &&
//                            (error.networkResponse.statusCode == 401 ||
//                                    error.networkResponse.statusCode == 403)) {
//                        // Handle authentication errors
//                    }
//                }) {
////            @Override
////            public Map<String, String> getHeaders() {
////                Map<String, String> headers = new HashMap<>();
////                headers.put("Content-Type", "application/json");
////                // Add your authorization header if needed
////                // headers.put("Authorization", "Bearer your_token");
////                return headers;
////            }
//        };
//
//        // Set retry policy
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000, // 10 seconds timeout
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        volleySingleton.addToRequestQueue(jsonObjectRequest);
//    }
//
//    // ... rest of your service code (onStartCommand, requestLocationUpdates, etc.)
//
//
//    private void requestLocationUpdates() {
//        LocationRequest locationRequest = LocationRequest.create()
//                .setInterval(10000) // 10 seconds
//                .setFastestInterval(5000)
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//        }
//    }
//
//    private void saveLocationForLater(double latitude, double longitude, long timestamp) {
//        SharedPreferences prefs = getSharedPreferences("PendingLocations", MODE_PRIVATE);
//        int count = prefs.getInt("count", 0);
//
//
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("lat_" + count, String.valueOf(latitude));
//        editor.putString("lon_" + count, String.valueOf(longitude));
//        editor.putString("time_" + count, String.valueOf(timestamp));
//        editor.putInt("count", count + 1);
//        editor.apply();
//    }
//    public void sendPendingLocations() {
//        SharedPreferences prefs = getSharedPreferences("PendingLocations", MODE_PRIVATE);
//        int count = prefs.getInt("count", 0);
//
//        if (count > 0 && isNetworkAvailable()) {
//            for (int i = 0; i < count; i++) {
//                double lat = Double.parseDouble(prefs.getString("lat_" + i, "0"));
//                double lon = Double.parseDouble(prefs.getString("lon_" + i, "0"));
//                long time = Long.parseLong(prefs.getString("time_" + i, "0"));
//             //   sendLocationToServer(lat, lon, time);
//              //  Log.e("Location Update", i+"<>"+lon+"");
//            }
//
//            // Clear saved locations
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.clear();
//            editor.apply();
//        }
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        fusedLocationClient.removeLocationUpdates(locationCallback);
//    }
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // Create the notification and start foreground service
//        startForeground(1, createNotification());
//
//        // Handle pending locations if network became available
//        if (intent != null && "SEND_PENDING_LOCATIONS".equals(intent.getAction())) {
//            sendPendingLocations();
//        } else {
//            // Normal service start - begin location updates
//            requestLocationUpdates();
//        }
//
//        // Return START_STICKY to keep service running
//        return START_STICKY;
//    }
//
//    private Notification createNotification() {
//        // Create notification channel for Android O+
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    "location_channel",
//                    "Location Service",
//                    NotificationManager.IMPORTANCE_LOW
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//
//        // Create the notification
//        return new NotificationCompat.Builder(this, "location_channel")
//                .setContentTitle("Location Service")
//                .setContentText("Tracking your location in background")
//                .setSmallIcon(R.mipmap.ssg_logo) // Use your own icon
//                .setPriority(NotificationCompat.PRIORITY_LOW)
//                .setOngoing(true)
//                .build();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}