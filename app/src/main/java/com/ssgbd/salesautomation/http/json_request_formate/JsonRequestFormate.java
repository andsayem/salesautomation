package com.ssgbd.salesautomation.http.json_request_formate;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rashed on 4/19/2017.
 */

public class JsonRequestFormate {

    public String loginFormate(String foId,String feedback){
        String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("foid", foId);
            jsonBody.put("feedback_id", feedback);

            requestBody = jsonBody.toString();

            //   Log.e("json>",jsonBody+"");

        }catch (JSONException je){
        }
        return requestBody;
    }
    public String getStock(String  point,String productId){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("productid", productId);
            jsonBody.put("point", point);

            requestBody = jsonBody.toString();

        }catch (JSONException je){

        }
       // Log.e("requestBody>",requestBody.toString()+"");
        return requestBody;
    }
    public String getSalesInfo(int userId, int year, int month){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("UserId", userId);
            jsonBody.put("Year", year);
            jsonBody.put("Month", month);

            requestBody = jsonBody.toString();

        }catch (JSONException je){

        }
        return requestBody;
    }


    public String doLoginRequest(String userName, String userPassword ){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("Username", userName);
            jsonBody.put("Password", userPassword);


            requestBody = jsonBody.toString();

        }catch (JSONException je){

        }
        return requestBody;
    }

    public String doSignUpRequest(String name, String mobile, String countryCode, String userName, String country, String password ){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("Name", name);
            jsonBody.put("Mobile", mobile);
            jsonBody.put("CountryCode", countryCode);
            jsonBody.put("Username", userName);
            jsonBody.put("Country", country);
            jsonBody.put("Password", password);

            requestBody = jsonBody.toString();



        }catch (JSONException je){

        }
        return requestBody;
    }



    public String doAttendance(String userId, String lat, String lon, String address){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("UserId", userId);
          //jsonBody.put("CheckDateTime", CheckDateTime);
            jsonBody.put("CheckLatitude", lat);
            jsonBody.put("CheckLongitude", lon);
            jsonBody.put("CheckAddress", address);

            requestBody = jsonBody.toString();

        }catch (JSONException je){

        }

    //    Log.e("requestBody",requestBody+"");
        return requestBody;
    }

    public String getLatShopLon(String storeId, String lat, String lon, String userId){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("StoreId", storeId);
            jsonBody.put("Latitude", lat);
            jsonBody.put("Longitude", lon);
            jsonBody.put("UserId", userId);


            requestBody = jsonBody.toString();

        }catch (JSONException je){

        }
        return requestBody;
    }

    public String getReturnCategoryCheck(String categoryId){
         String requestBody = "";
        try {
            JSONObject mainBody = new JSONObject();
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("category_id", categoryId);
            mainBody.put("category_info",jsonBody);

            requestBody = mainBody.toString();

        }catch (JSONException je){

        }
    //    Log.e("rchange>>",requestBody+"");
        return requestBody;
    }

    public String jsonGetUtilityList(String userId){
         String requestBody = "";
        try {
            JSONObject jsonMain = new JSONObject();
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("userid", userId);

            jsonMain.put("utility",jsonBody);
            requestBody = jsonMain.toString();

        }catch (JSONException je){

        }
        return requestBody;
    }

    public String jsonGetFoLeaveList(String userId){
         String requestBody = "";
        try {
            JSONObject jsonMain = new JSONObject();
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("foid", userId);

            jsonMain.put("info",jsonBody);
            requestBody = jsonMain.toString();

        }catch (JSONException je){

        }
        return requestBody;
    }


    public String foFeedbackFormat(String userId,String fromdate, String todate){
         String requestBody = "";
        try {

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("foid", userId);
            jsonBody.put("fromdate",fromdate);
            jsonBody.put("todate", todate);
            requestBody = jsonBody.toString();
        }catch (JSONException je){

        }
        return requestBody;
    }


    public String imageFormat(String email,String profile_image){
         String requestBody = "";
        try {

          JSONObject jsonBody = new JSONObject();

            jsonBody.put("email", email);
            jsonBody.put("profile_image",profile_image);

            requestBody = jsonBody.toString();
          //  Log.e("regBody>>",requestBody+"");
        }catch (JSONException je){

        }

        return requestBody;
    }


    public String newProduct(String image,String fo_id,String product_monthly_sales_qty,String product_mrp,String product_name){
        String requestBody = "";
        try {

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("image", image);
            jsonBody.put("fo_id",fo_id);
            jsonBody.put("product_monthly_sales_qty",product_monthly_sales_qty);
            jsonBody.put("product_mrp",product_mrp);
            jsonBody.put("product_name",product_name);

            requestBody = jsonBody.toString();

          //  Log.e("regBody>>",requestBody+"");

        }catch (JSONException je){

        }

        return requestBody;
    }


    public String getimageformate(String email){
         String requestBody = "";
        try {

          JSONObject jsonBody = new JSONObject();

            jsonBody.put("email", email);

            requestBody = jsonBody.toString();
        }catch (JSONException je){

        }

        return requestBody;
    }

    public String registrationFormat(String userId,String userName,String phoneNo,String userType){
         String requestBody = "";
        try {

          JSONObject jsonBody = new JSONObject();

            jsonBody.put("userId", userId);
            jsonBody.put("userName",userName);
            jsonBody.put("phoneNo",phoneNo);
            jsonBody.put("userType",userType);

            requestBody = jsonBody.toString();
//            Log.e("regBody>>",jsonBody+"");
        }catch (JSONException je){

        }

        return requestBody;
    }

    public String getData(String userId){
         String requestBody = "";
        try {

          JSONObject jsonBody = new JSONObject();

            jsonBody.put("userId", userId);


            requestBody = jsonBody.toString();
          //  Log.e("requestBody?",requestBody+"");
        }catch (JSONException je){

        }

        return requestBody;
    }

    public String submitUtioityData(String reason,String type,String suggestion,String userId){
         String requestBody = "";
        try {
            JSONObject jsonMain = new JSONObject();
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("reason", reason);
            jsonBody.put("type", type);
            jsonBody.put("suggestion", suggestion);
            jsonBody.put("userid", userId);

            jsonMain.put("utility",jsonBody);
            requestBody = jsonMain.toString();

         //  Log.e("json>",jsonMain+"");

        }catch (JSONException je){

        }
        return requestBody;
    }

    public String submitShirtMesurment(String collar,String chest,String waist,String shoulder,String sleeve,String length,String cuff_hole,String arm_hole,String user_id){
         String requestBody = "";
        try {
            JSONObject jsonMain = new JSONObject();
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("collar", collar);
            jsonBody.put("chest", chest);
            jsonBody.put("waist", waist);
            jsonBody.put("shoulder", shoulder);
            jsonBody.put("sleeve", sleeve);
            jsonBody.put("length", length);
            jsonBody.put("cuff_hole", cuff_hole);
            jsonBody.put("arm_hole", arm_hole);
            jsonBody.put("user_id", user_id);

            jsonMain.put("info",jsonBody);
            requestBody = jsonMain.toString();

           //    Log.e("requestBody>",requestBody+"");


        }catch (JSONException je){

        }
        return requestBody;
    }

    public String submitFoLeavApply(String foLoginId,String leaveCategory,String appDate,String fromDate,String toDate,String reason,String remark){
         String requestBody = "";
        try {
            JSONObject jsonMain = new JSONObject();
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("foid", foLoginId);
            jsonBody.put("leave_category", leaveCategory);
            jsonBody.put("applydate", appDate);
            jsonBody.put("fromdate", fromDate);
            jsonBody.put("todate", toDate);
            jsonBody.put("reason", reason);
            jsonBody.put("remarks", remark);

            jsonMain.put("info",jsonBody);
            requestBody = jsonMain.toString();

          // Log.e("json>",jsonMain+"");

        }catch (JSONException je){

        }
        return requestBody;
    }

    public String submitFoAns(String foLoginId,String leaveCategory,String appDate,String fromDate,String toDate,String reason,String remark){
        String requestBody = "";
        try {
            JSONObject jsonMain = new JSONObject();
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonAns = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            jsonBody.put("foid", foLoginId);
            jsonBody.put("comment", leaveCategory);
            jsonAns.put("qsno", appDate);
            jsonAns.put("answer", fromDate);
            jsonMain.put("info",jsonBody);
            jsonMain.put("answerlist",jsonBody);
            requestBody = jsonMain.toString();

          //  Log.e("json>",jsonMain+"");



        }catch (JSONException je){

        }
        return requestBody;
    }



    public String submitFoLeavDelete(String foId,String leaveId){
         String requestBody = "";
        try {
            JSONObject jsonMain = new JSONObject();
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("foid", foId);
            jsonBody.put("leaveid", leaveId);


            jsonMain.put("info",jsonBody);
            requestBody = jsonMain.toString();

        //   Log.e("json>",jsonMain+"");





        }catch (JSONException je){

        }
        return requestBody;
    }

    public String submitFeedbackDelete(String foId,String feedback){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("foid", foId);
            jsonBody.put("feedback_id", feedback);

            requestBody = jsonBody.toString();

        //   Log.e("json>",jsonBody+"");

        }catch (JSONException je){
        }
        return requestBody;
    }



    public String jsonSYNCProductAndCategory(String userId,String userBusinessType,String globalCompanyId){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("user_id", userId);
            jsonPart.put("user_business_type", userBusinessType);
            jsonPart.put("global_company_id", globalCompanyId);

            jsonBody.put("userinfo",jsonPart);
            requestBody = jsonBody.toString();

         //   Log.e("req_body>",requestBody+"");
        }catch (JSONException je){

        }
        return requestBody;
    }

    public String visitListJson(String userId,String globalId){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();


            jsonPart.put("userid", userId);
            jsonPart.put("globalid", globalId);


            jsonBody.put("userinfo",jsonPart);
            requestBody = jsonBody.toString();

           // Log.e("req_body>",requestBody+"");
        }catch (JSONException je){

        }
        return requestBody;
    }
 public String jsonSYNC(String userId,String userBusinessType,String pointId,String lastSyncdate){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("user_id", userId);
            jsonPart.put("user_business_type", userBusinessType);
            jsonPart.put("point_id", pointId);
            jsonPart.put("last_sync_date", lastSyncdate);

            jsonBody.put("userinfo",jsonPart);
            requestBody = jsonBody.toString();

          //  Log.e("response",requestBody+"");

        }catch (JSONException je){

        }
        return requestBody;
    }
    public String getRretailer(String routeId,String pointId){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("route_id", routeId);

            jsonPart.put("point_id", pointId);
            jsonPart.put("last_sync_date", "");

            jsonBody.put("userinfo",jsonPart);
            requestBody = jsonBody.toString();

          //  Log.e("<<>>",requestBody+"");

        }catch (JSONException je){

        }
        return requestBody;
    }

    public String jsonRetailerStatus(String userId,String routeId){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("foid", userId);
            jsonPart.put("type", "Order");
            jsonPart.put("routeid", routeId);


            jsonBody.put("status-retailer",jsonPart);
            requestBody = jsonBody.toString();
        //    Log.e("<<>>",requestBody+"");

        }catch (JSONException je){

        }
        return requestBody;
    }

    public String jsonFoCollection(String retailer_id,String route_id,String point_id,String collection_amount,String fo_id,String order_id,String collection_type){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("retailer_id", retailer_id);
            jsonPart.put("route_id", route_id);
            jsonPart.put("point_id", point_id);
            jsonPart.put("collection_amount", collection_amount);
            jsonPart.put("fo_id", fo_id);
            jsonPart.put("order_id", order_id);
            jsonPart.put("collection_date", "");
            jsonPart.put("collection_type", collection_type);

            jsonBody.put("info",jsonPart);
            requestBody = jsonBody.toString();
        //  Log.e("<<>>",requestBody+"");

        }catch (JSONException je){

        }
        return requestBody;
    }
    public String jsonLiveLocation(String userId,String divisionId,String terriotoryId,String pointId,String lat,String lon){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("foid", userId);
            jsonPart.put("divisionid", divisionId);
            jsonPart.put("territoryid", terriotoryId);
            jsonPart.put("pointid", pointId);
            jsonPart.put("latitude", lat);
            jsonPart.put("longitude", lon);

            jsonBody.put("info",jsonPart);
            requestBody = jsonBody.toString();
          //  Log.e("requestBody>",requestBody+"");


        }catch (JSONException je){

        }
        return requestBody;
    }




    public String jsonDashboardData(String userId,String globalCompanyId){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("user_id", userId);
            jsonPart.put("global_id", globalCompanyId);

            jsonBody.put("info",jsonPart);
            requestBody = jsonBody.toString();
             //  Log.e("info",requestBody+"");

        }catch (JSONException je){

        }

      //  Log.e("requestBody>>",requestBody+"");
        return requestBody;
    }

    public String jsonPgWiseReport(String userId,String business_type_id){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("user_id", userId);
            jsonPart.put("business_type_id", business_type_id);

            jsonBody.put("info",jsonPart);
            requestBody = jsonBody.toString();
            //   Log.e("<<>>",requestBody+"");

        }catch (JSONException je){

        }

      //  Log.e("requestBody>>",requestBody+"");
        return requestBody;
    }

    public String jsonGetNotice(String typeId){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("user_type_id", typeId);
            requestBody=jsonBody.toString();

        }catch (JSONException je){

        }
        return requestBody;
    }

    public String jsonGetOfferImage(String businessTypeId){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("business_type_id", businessTypeId);
            requestBody=jsonBody.toString();

        }catch (JSONException je){

        }


     //   Log.e(">>>",requestBody+"");
        return requestBody;
    }

    public String stockList(String foId,String catId,String pointId){
         String requestBody = "";

        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("foid", foId);
            jsonPart.put("category_id", catId);
            jsonPart.put("point_id", pointId);



            jsonBody.put("info",jsonPart);
            requestBody = jsonBody.toString();
        //    Log.e("requestBody>",requestBody+"");

        }catch (JSONException je){

        }
        return requestBody;
    }

    public String technicianUpdate(String type,String tastus){
         String requestBody = "";

        try {
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("type", type);
            jsonPart.put("tastus", tastus);



            requestBody = jsonPart.toString();
        //    Log.e("requestBody>",requestBody+"");

        }catch (JSONException je){

        }
        return requestBody;
    }

    public String jsonGetRetailerLedger(String userid,String routeid,String foid,String fromdate,String todate){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("userid", userid);
            jsonPart.put("routeid", routeid);
            jsonPart.put("foid", foid);
            jsonPart.put("fromdate", fromdate);
            jsonPart.put("todate", todate);

            jsonBody.put("info",jsonPart);
            requestBody = jsonBody.toString();

            Log.e("<<>>",requestBody+"");
        }catch (JSONException je){

        }
        return requestBody;
    }
public String jsonRerurnList(String userId,String globalCompanyId,String fromdate,String todate){
         String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("userid", userId);
            jsonPart.put("global_company_id", globalCompanyId);
            jsonPart.put("from_date", fromdate);
            jsonPart.put("to_date", todate);

            jsonBody.put("userinfo",jsonPart);
            requestBody = jsonBody.toString();

         //   Log.e("req222ody>>",requestBody+"");

        }catch (JSONException je){

        }
        return requestBody;

}

    public String jsonReportOrderVsReport(String userId,String fromDate,String toDate){
        String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("foid", userId);
            jsonPart.put("from_date", fromDate);
            jsonPart.put("to_date", toDate);

            jsonBody.put("info",jsonPart);
            requestBody = jsonBody.toString();

        }catch (JSONException je){

        }
    //    Log.e("requestBody>",requestBody+"");

        return requestBody;
    }

    public String jsonFoVisitReport(String userId,String fromDate,String toDate,String type){
        String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("foid", userId);
            jsonPart.put("from_date", fromDate);
            jsonPart.put("to_date", toDate);
            jsonPart.put("type", type);

            jsonBody.put("info",jsonPart);
            requestBody = jsonBody.toString();

        }catch (JSONException je){

        }
    //    Log.e("requestBody>",requestBody+"");
        return requestBody;
    }

    public String jsonReportODeliveryVsOrder(String userId,String fromDate,String toDate,String route){
        String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("foid", userId);
            jsonPart.put("from_date", fromDate);
            jsonPart.put("to_date", toDate);
            jsonPart.put("route", route);

            jsonBody.put("info",jsonPart);
            requestBody = jsonBody.toString();


        }catch (JSONException je){

        }
        Log.e("<<>>",requestBody+"");
        return requestBody;
    }


    public String jsonOrderReport(String userId,String globalid,String fromDate,String toDate,String routeId){
        String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("userid", userId);
            jsonPart.put("globalid", globalid);
            jsonPart.put("fromdate", fromDate);
            jsonPart.put("todate", toDate);
            jsonPart.put("routeid", routeId);

            jsonBody.put("userinfo",jsonPart);
            requestBody = jsonBody.toString();

        }catch (JSONException je){

        }
     //   Log.e("requestBody>",requestBody+"");
        return requestBody;
    }


    public String jsonLocationUpdate(String sap_code,String lat_long,String user_id){
        String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonPart = new JSONObject();

            jsonPart.put("sap_code", sap_code);
            jsonPart.put("lat_long", lat_long);
            jsonPart.put("user_id", user_id);

            jsonBody.put("info",jsonPart);
            requestBody = jsonBody.toString();

        }catch (JSONException je){

        }
            //Log.e("requestBody>",requestBody+"");
        return requestBody;
    }



    public String jfLogin(String email,String password){
        String requestBody = "";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("email", email);
            jsonBody.put("password", password);
            requestBody=jsonBody.toString();

        }catch (JSONException je){

        }
        return requestBody;
    }

}
