package com.ssgbd.salesautomation.http.volly_method;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssgbd.salesautomation.http.interfaces.VolleyCallBack;
import com.ssgbd.salesautomation.utils.SharePreference;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by rashed on 4/19/2017.
 */

public class VolleyMethods {
    public RequestQueue queue;

    public void sendRequestToServer(final Context context, String URL, final String requestBoddy, final VolleyCallBack callback){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                 callback.onSuccess(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                          //  Log.e("error onerror>>",error.toString()+"");


                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");

                        callback.onSuccess(responseBody);

                    } catch (UnsupportedEncodingException errorr) {
                    }
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return  requestBoddy == null ? null : requestBoddy.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBoddy, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    String message = "";
                    if (response != null) {
//                        responseString = String.valueOf(response.data);
                        // can get more details such as response.headers
                        message = new String(response.data);
                      //  callback.onSuccess(message.toString());

                    }
                    return Response.success(message, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue = Volley.newRequestQueue(context);
            queue.getCache().clear();
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(stringRequest);
        }
//    public void sendRequestToServer2(final Context context, String URL, final String requestBody, final VolleyCallBack callback) {
//       // Log.e("url", "  URL: " + URL);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
//                response -> {
//                    // Success
//                    callback.onSuccess(response);
//                },
//                error -> {
//                    // Error handling
//                    String errorMsg = "nettooslow";
//
//                    if (error.networkResponse != null && error.networkResponse.data != null) {
//                        errorMsg = new String(error.networkResponse.data);
//                        Log.e("VolleyError", "Status Code: " + error.networkResponse.statusCode);
//                        Log.e("VolleyError", "Error Body: " + errorMsg);
//                    } else if (error instanceof com.android.volley.TimeoutError) {
//                        errorMsg = "Request Timeout";
//                    } else if (error instanceof com.android.volley.NoConnectionError) {
//                        errorMsg = "No Connection";
//                    }
//
//                    Toast.makeText(context, "Unable to connect.", Toast.LENGTH_SHORT).show();
//                    callback.onSuccess(errorMsg);
//                }) {
//
//            // ✅ Important: tell server we're sending JSON
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//            // ✅ Include Authorization header if token exists
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Accept", "application/json");
//                headers.put("Content-Type", "application/json");
//
//                String token = SharePreference.getAccesstoken(context);
//                if (token != null && !token.isEmpty()) {
//                    headers.put("Authorization", "Bearer " + token);
//                }
//
//                return headers;
//            }
//
//            // ✅ Convert requestBody to bytes
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return requestBody == null ? null : requestBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    return null;
//                }
//            }
//
//            // ✅ Proper network response parsing
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                try {
//                    String resp = new String(response.data, "utf-8");
//                    return Response.success(resp, HttpHeaderParser.parseCacheHeaders(response));
//                } catch (Exception e) {
//                    return Response.success("Response parse error", HttpHeaderParser.parseCacheHeaders(response));
//                }
//            }
//        };
//
//        stringRequest.setShouldCache(false);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                60000, // 60 sec timeout
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//        queue.add(stringRequest);
//    }

        public void sendRequestToServer2(final Context context, String URL, final String requestBoddy, final VolleyCallBack callback){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                 callback.onSuccess(response.toString());
                 //   Log.e("onsuccess>>",response.toString()+"");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
               //        Log.e("error onerror>>",error.toString()+"");
                    callback.onSuccess("nettooslow");

                    Toast.makeText(context, "Unable to connect.", Toast.LENGTH_SHORT).show();

                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return  requestBoddy == null ? null : requestBoddy.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBoddy, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    String message = "";
                    if (response != null) {
//                        responseString = String.valueOf(response.data);
                        // can get more details such as response.headers
                        message = new String(response.data);
                     //   callback.onSuccess(message.toString());

                      //  Log.e("networdkres>>",message.toString()+"");

                    }
                    return Response.success(message, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue = Volley.newRequestQueue(context);
            queue.getCache().clear();
            stringRequest.setShouldCache(false);
            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 9000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                //    Log.e("v retry>>>",error.toString()+"<<>>");
                }
            });
            queue.add(stringRequest);
        }


    public void sendRequestWheader(final Context context,
                                   String URL,
                                   final String requestBoddy,
                                   final VolleyCallBack callback, VolleyCallBack volleyCallBack){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error.networkResponse != null) {
                            String errorMsg = new String(error.networkResponse.data);
                            Log.e("VolleyError", "Status Code: " + error.networkResponse.statusCode);
                            Log.e("VolleyError", "Error Body: " + errorMsg);
                            callback.onSuccess(errorMsg);
                        } else {
                            callback.onSuccess("nettooslow");
                        }

                        Toast.makeText(context, "Unable to connect.", Toast.LENGTH_SHORT).show();
                    }
                }) {

            // ✅ VERY IMPORTANT
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            // ✅ HEADER
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");

                String token = SharePreference.getAccesstoken(context);
                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                }

                return headers;
            }

            // ✅ BODY
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBoddy == null ? null : requestBoddy.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }

            // ✅ PROPER RESPONSE PARSE
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                try {
                    String message = new String(response.data, "utf-8");
                    return Response.success(message,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (Exception e) {
                    return Response.success("Response parse error",
                            HttpHeaderParser.parseCacheHeaders(response));
                }
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,  // 60 sec timeout
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }


}
