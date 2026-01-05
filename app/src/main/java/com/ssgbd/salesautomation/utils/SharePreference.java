package com.ssgbd.salesautomation.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rashed on 5/2/2016.
 */
public class SharePreference {

    // App Preferences
    private static final String PREFS_FILE_NAME = "AppPreferences";
    private static final String ACCESSTOKEN = "accesstoken";
    private static final String ACCESSTOKENHRIS = "accesstoken";
    private static final String USER_CATAGORY = "user_catagory";
    private static final String IS_lOG_IN = "islogin";
    private static final String REMEMBER_PASSWORD = "remember";
    private static final String USER_NAME = "user_name";
    private static final String USER_TYPE_ID = "usertypeid";
    private static final String USER_POINT_ID = "userpointid";
    private static final String USER_POINT_NAME = "userpointname";
    private static final String USER_GLOBAL_ID = "user_global_id";
    private static final String USER_DESIGNATION = "user_designation";
    private static final String USER_ID = "user_id";
    private static final String BUTTON_TEXT = "button_text";
    private static final String DISTIBUTORID = "distributorid";
    private static final String DISTIBUTORNAME = "distributorname";
    private static final String SAP_CODE = "sapecode";
    private static final String DIVISION_ID = "divisionid";
    private static final String DIVISION_NAME = "divisionname";
    private static final String TERRITORY_ID = "territoryid";
    private static final String TERRITORY_NAME = "territoryname";
    private static final String USER_LOGIN_ID = "user_login_id";
    private static final String USER_MAIL_ID = "user_mail_id";
    private static final String USER_LOGIN_PASSWORD = "user_login_password";
    private static final String ROUTE_DATA = "route_data";
    private static final String USER_BUSINESS_TYPE = "userbusinesstype";
    private static final String IS_ATTENDANCE = "isattendance";
    private static final String APP_MODE = "appmode";
    private static final String EMP_ID = "empid";
    private static final String SYNC = "sync";
    private static final String HRIS_CHECKIN = "hrischeckin";
    private static final String AD = "ad";
    private static final String DistributorLatLon = "latlon";
    private static final String  ISVALUHIDE = "valuhide";
    private static final String  ISDEPOT = "ISDEPOT";
    private static final String  FOVISTDIST = "fovisitdist";
        private static final String  ANDROID_ID = "androidid";
        private static final String  HRIS_ID = "hrisid";
        private static final String  APP_VERSION = "appversion";


        public static String getAndroidId(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.ANDROID_ID, "");
    }

    public static void setAndroidId(final Context con, final String androidid) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.ANDROID_ID, androidid);
        editor.commit();
    }

    public static String getHrisID(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.HRIS_ID, "");
    }

    public static void setHrisID(final Context con, final String hrisid) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.HRIS_ID, hrisid);
        editor.commit();
    }


   public static String getAppVersion(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.APP_VERSION, "");
    }

    public static void setAppVersion(final Context con, final String androidid) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.APP_VERSION, androidid);
        editor.commit();
    }


    public static String getAccesstoken(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.ACCESSTOKEN, "");
    }

    public static void setAccesstoken(final Context con, final String accesstoken) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.ACCESSTOKEN, accesstoken);
        editor.commit();
    }

    public static String getAccesstokenHRIS(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.ACCESSTOKENHRIS, "");
    }

    public static void setAccesstokenHRIS(final Context con, final String accesstokenhris) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.ACCESSTOKENHRIS, accesstokenhris);
        editor.commit();
    }

    public static String getDistributorLatLon(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.DistributorLatLon, "");
    }

    public static void setDistributorLatLon(final Context con, final String distributorLatLon) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.DistributorLatLon, distributorLatLon);
        editor.commit();
    }

    public static String getIsvaluhide(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.ISVALUHIDE, "");
    }

    public static void setIsvaluhide(final Context con, final String valuhide) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.ISVALUHIDE, valuhide);
        editor.commit();
    }

    public static String getIsDepot(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.ISDEPOT, "");
    }

    public static void setIsGepot(final Context con, final String isdepot) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.ISDEPOT, isdepot);
        editor.commit();
    }

 public static String getFoVisitDist(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.FOVISTDIST, "");
    }

    public static void setFoVisitDist(final Context con, final String fovistdist) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.FOVISTDIST, fovistdist);
        editor.commit();
    }

        public static String getAD(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.AD, "");
    }

    public static void setUserAD(final Context con, final String ad) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.AD, ad);
        editor.commit();
    }

    public static String getUserLoginId(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_LOGIN_ID, "");
    }

    public static void setUserLoginId(final Context con, final String userloginid) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_LOGIN_ID, userloginid);
        editor.commit();
    }
    public static String getUserMailId(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_MAIL_ID, "");
    }

    public static void setUserMailId(final Context con, final String usermailid) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_MAIL_ID, usermailid);
        editor.commit();
    }

    public static String getHrisCheckin(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.HRIS_CHECKIN, "");
    }

    public static void setHrisCheckin(final Context con, final String hrischeckin) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.HRIS_CHECKIN, hrischeckin);
        editor.commit();
    }

    public static String getEmployeeId(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.EMP_ID, "");
    }

    public static void setEmployeeId(final Context con, final String empid) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.EMP_ID, empid);
        editor.commit();
    }

    public static String getSync(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.SYNC, "no");
    }

    public static void setSync(final Context con, final String sync) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.SYNC, sync);
        editor.commit();
    }



    // for login / log out

        public static String getAppMode(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.APP_MODE, "3");
    }

    public static void setAppMode(final Context con, final String appmode) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.APP_MODE, appmode);
        editor.commit();
    }

    // for login / log out

        public static String getIsAttendance(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.IS_ATTENDANCE, "");
    }

    public static void setIsAttendance(final Context con, final String isattendance) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.IS_ATTENDANCE, isattendance);
        editor.commit();
    }
        public static String getIslogIn(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.IS_lOG_IN, "");
    }

    public static void setIslogIn(final Context con, final String islogin) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.IS_lOG_IN, islogin);
        editor.commit();
    }// for login / log out

    public static String getRememberPassword(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.REMEMBER_PASSWORD, "");
    }

    public static void setRememberPassword(final Context con, final String islogin) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.REMEMBER_PASSWORD, islogin);
        editor.commit();
    }



public static String getUserLoginPassword(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_LOGIN_PASSWORD, "");
    }

    public static void setUserLoginPassword(final Context con, final String userloginPasword) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_LOGIN_PASSWORD, userloginPasword);
        editor.commit();
    }// for login / log out



        public static String getUserName(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_NAME, "0");
    }

    public static void setUserName(final Context con, final String username) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_NAME, username);
        editor.commit();
    }




    // user full name
    public static String getUserTypeId(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_TYPE_ID, "");
    }

    public static void setUserTypeId(final Context con, final String usertypeid) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_TYPE_ID, usertypeid);
        editor.commit();
    }


    // user full name
    public static String getUserPointId(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_POINT_ID, "");
    }

    public static void setUserPointId(final Context con, final String userpointid) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_POINT_ID, userpointid);
        editor.commit();
    }

    // user full name
    public static String getUserPointName(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_POINT_NAME, "");
    }

    public static void setUserPointName(final Context con, final String userpointname) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_POINT_NAME, userpointname);
        editor.commit();
    }

    // user full name user_designation
    public static String getDesignation(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_DESIGNATION, "");
    }

    public static void setDesignation(final Context con, final String designation) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_DESIGNATION, designation);
        editor.commit();
    }

    // user id
    public static String getUserId(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_ID, "");
    }

    public static void setUserId(final Context con, final String user_id) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_ID, user_id);
        editor.commit();
    }



    // global id
    public static String getUserGlobalId(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_GLOBAL_ID, "");
    }

    public static void setUserGlobalId(final Context con, final String userGlobalId) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_GLOBAL_ID, userGlobalId);
        editor.commit();
    }
    // buttontext
    public static String getButtonText(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.BUTTON_TEXT, "Check In");
    }

    public static void setButtonText(final Context con, final String buttonText) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.BUTTON_TEXT, buttonText);
        editor.commit();
    }


    // distributor
    public static String getDistributorID(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.DISTIBUTORID, "");
    }

    public static void setDistributorID(final Context con, final String distributorId) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.DISTIBUTORID, distributorId);
        editor.commit();
    }

    // buttontext
    public static String getDistributorName(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.DISTIBUTORNAME, "");
    }

    public static void setDistributorName(final Context con, final String distributorname) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.DISTIBUTORNAME, distributorname);
        editor.commit();
    }

    // buttontext
    public static String getSapCode(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.SAP_CODE, "");
    }

    public static void setSapCode(final Context con, final String sapcode) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.SAP_CODE, sapcode);
        editor.commit();
    }

    // buttontext
    public static String getDivisionId(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.DIVISION_ID, "");
    }

    public static void setDivisionId(final Context con, final String divisionid) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.DIVISION_ID, divisionid);
        editor.commit();
    } // buttontext


    public static String getDivisionName(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.DIVISION_NAME, "");
    }

    public static void setDivisionName(final Context con, final String divisionname) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.DIVISION_NAME, divisionname);
        editor.commit();
    }

    public static String getTerritoryId(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.TERRITORY_ID, "");
    }

    public static void setTerritoryId(final Context con, final String trrritoryid) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.TERRITORY_ID, trrritoryid);
        editor.commit();
    }


    public static String getTerritoryName(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.TERRITORY_NAME, "");
    }

    public static void setTerritoryName(final Context con, final String trrritoryname) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.TERRITORY_NAME, trrritoryname);
        editor.commit();
    }


    public static String getRouteData(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.ROUTE_DATA, "");
    }

    public static void setRouteData(final Context con, final String routename) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.ROUTE_DATA, routename);
        editor.commit();
    }

    public static String getUserBusinessType(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.USER_BUSINESS_TYPE, "");
    }

    public static void setUserBusinessType(final Context con, final String userBusinessType) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.USER_BUSINESS_TYPE, userBusinessType);
        editor.commit();
    }


    //SYNC_RTAILER
    private static final String IS_RETAILER_BASE_SYNC = "isretailerbasesync";
    private static final String RETAILER_BASE_SYNC_DATE = "retailerbasesyncdate";
    //SYNC_PRODUCT_CATEGORY
    private static final String IS_CATEGORY_BASE_SYNC = "iscategorybasesync";
    private static final String CATEGORY_BASE_SYNC_DATE = "categorybasesyncdate";
    //SYNC_RTAILER
    private static final String IS_PRODUCT_BASE_SYNC = "isproductbasesync";
    private static final String PRODUCT_BASE_SYNC_DATE = "productbasesyncdate";

    //////// retailer
    public static String getIsRetailerBaseSync(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.IS_RETAILER_BASE_SYNC, "no");
    }

    public static void setIsRetailerBaseSync(final Context con, final String isretailerbasesync) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.IS_RETAILER_BASE_SYNC, isretailerbasesync);
        editor.commit();
    }

    // retailer base sync date
    public static String getRetailerBaseSyncDate(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.RETAILER_BASE_SYNC_DATE, "");
    }

    public static void setRetailerBaseSyncDate(final Context con, final String retailerbasesyncdate) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.RETAILER_BASE_SYNC_DATE, retailerbasesyncdate);
        editor.commit();
    }
    // **********************category****************************
    public static String getIsCategoryBaseSync(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.IS_CATEGORY_BASE_SYNC, "no");
    }

    public static void setIsCategoryBaseSync(final Context con, final String iscategorybasesync) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.IS_CATEGORY_BASE_SYNC, iscategorybasesync);
        editor.commit();
    }

    // retailer base sync date
    public static String getCategoryBaseSyncDate(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.CATEGORY_BASE_SYNC_DATE, "");
    }

    public static void setCategoryBaseSyncDate(final Context con, final String categorybasesyncdate) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.CATEGORY_BASE_SYNC_DATE, categorybasesyncdate);
        editor.commit();
    }


    // **********************product****************************
    public static String getIsProductBaseSync(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.IS_PRODUCT_BASE_SYNC, "no");
    }

    public static void setIsProductBaseSync(final Context con, final String isproductbasesync) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.IS_PRODUCT_BASE_SYNC, isproductbasesync);
        editor.commit();
    }

    // retailer base sync date
    public static String getProductBaseSyncDate(final Context con) {
        return con.getSharedPreferences(SharePreference.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(SharePreference.PRODUCT_BASE_SYNC_DATE, "");
    }

    public static void setProductBaseSyncDate(final Context con, final String productbasesyncdate) {
        final SharedPreferences prefs = con.getSharedPreferences(
                SharePreference.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharePreference.PRODUCT_BASE_SYNC_DATE, productbasesyncdate);
        editor.commit();
    }
}
