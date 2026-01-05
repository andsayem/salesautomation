package com.ssgbd.salesautomation.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import com.ssgbd.salesautomation.dtos.CategoryDTO;
import com.ssgbd.salesautomation.dtos.DBOrderDTO;
import com.ssgbd.salesautomation.dtos.NoticeDTO;
import com.ssgbd.salesautomation.dtos.OrderVisitStatusDTO;
import com.ssgbd.salesautomation.dtos.ProductDTO;
import com.ssgbd.salesautomation.dtos.RetailerDTO;
import com.ssgbd.salesautomation.dtos.RouteDTO;
import com.ssgbd.salesautomation.utils.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
     * Created by rashed on 10/20/2015.
     */
    public class DatabaseHelper extends SQLiteOpenHelper {
        //  private static String DB_PATH = "/data/data/com.ssgbd.salesautomation/databases/";
        private static String DB_NAME = "ssg.db";
        private SQLiteDatabase myDataBase;
        private final Context myContext;

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, 10);

            this.myContext = context;
        }



        public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if(dbExist){
            //do nothing - database already exist
        }else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            this.close();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = Utility.DB_PATH+ DB_NAME;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory());
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        }catch(SQLiteException e){

            //database does't exist yet.
        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = Utility.DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = Utility.DB_PATH + DB_NAME;

        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  //      Log.e("array","onUpgrade");

        if(newVersion > oldVersion) {
            try {
                upgradeCopyDatabase();
            }
            catch (IOException e){
            }
        }
    }

    private void upgradeCopyDatabase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = Utility.DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public static ArrayList<RetailerDTO>  getRetailerList(SQLiteOpenHelper dbHelper, String routeId) {
        ArrayList<RetailerDTO> divisionalists = new ArrayList<RetailerDTO>();

        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + "retailers" + " WHERE " + "rid" +
                    "='" + routeId + "'" +" AND " + "status" + "='"+"0" +"'";

//            String selectQuery = "SELECT  * FROM " + "product_category" + " WHERE " + "gid" +
//                    "='" + gid + "'" +" AND " + "status" + "='"+ "0" + "'";

            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    RetailerDTO retailerDTO = new RetailerDTO();

                    retailerDTO.setRetailer_id(cursor.getString(0));
                    retailerDTO.setRetailer_name(cursor.getString(1));
                    retailerDTO.setDivision(cursor.getString(2));
                    retailerDTO.setTerritory(cursor.getString(3));
                    retailerDTO.setPoint_id(cursor.getString(4));
                    retailerDTO.setShopeType(cursor.getString(6));
                    retailerDTO.setRetailerOwner(cursor.getString(7));
                    retailerDTO.setRetailerMobileNo(cursor.getString(8));
                   // retailerDTO.setStatus(cursor.getString(13));
                    retailerDTO.setDob(cursor.getString(14));
                    retailerDTO.setAddress(cursor.getString(15));
                    retailerDTO.setRetailerSerial(cursor.getString(24));
                    retailerDTO.setLat(cursor.getString(31));
                    retailerDTO.setLon(cursor.getString(32));
                  // retailerDTO.setStatus(cursor.getString(1));

//                    Log.e("Name>>",cursor.getString(1)+"");
//                    Log.e("Status>>",cursor.getString(13)+"");

                    // adding category to list
                    divisionalists.add(retailerDTO);
                } while (cursor.moveToNext());
            }else {
            //    Log.e("No data","<<>>found");
            }

        } catch (Exception e) {
            // TODO: handle exception
           // Log.e("Exception: ", e + "<>");
        }

        // return category list
        return divisionalists;
    }

    public static ArrayList<ProductDTO> getProductList(SQLiteOpenHelper dbHelper, String categoryId) {
        ArrayList<ProductDTO> productDTOS = new ArrayList<ProductDTO>();

        try {


            // Select All Query
            String selectQuery = "SELECT  * FROM " + "product" + " WHERE " + "category_id" +
                    "='" + categoryId + "'"+" AND " + "status" + "='"+ "0" + "'";
            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
        //    Log.e("<<>>",cursor+"<<>>");
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    ProductDTO productDTO = new ProductDTO();

                    productDTO.setProduct_id(cursor.getString(0));
                    productDTO.setProduct_sap_code(cursor.getString(4));
                    productDTO.setProduct_name(cursor.getString(9));
                    productDTO.setProduct_MRP_price(cursor.getString(12));
                    productDTO.setProduct_price(cursor.getString(13));
                    productDTO.setProduct_shortCode(cursor.getString(5));

                    // adding category to list
                    productDTOS.add(productDTO);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
          //  Log.e("Exception: ", e + "<>");
        }

        // return category list
        return productDTOS;
    }

    public static ArrayList<CategoryDTO> getAllCategoryProduct(SQLiteOpenHelper dbHelper, String gid) {
        ArrayList<CategoryDTO> symptomLists = new ArrayList<CategoryDTO>();
        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + "product_category" + " WHERE " + "gid" +
                    "='" + gid + "'";





            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    CategoryDTO symptomList = new CategoryDTO();
                    symptomList.setId(cursor.getString(0));
                    symptomList.setGid(cursor.getString(1));
                    symptomList.setG_name(cursor.getString(2));
                    symptomList.setG_code(cursor.getString(3));
                    symptomList.setName(cursor.getString(4));
                    symptomList.setShort_name(cursor.getString(5));
                    symptomList.setAvg_price(cursor.getString(6));
                    symptomList.setOffer_type(cursor.getString(23));

                    symptomLists.add(symptomList);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
           // Log.e("Exception: ", e + "<>");
        }

        // return category list
        return symptomLists;
    }

    public static ArrayList<CategoryDTO> getAllCategoryProductActiveOnly(SQLiteOpenHelper dbHelper, String gid) {
        ArrayList<CategoryDTO> symptomLists = new ArrayList<CategoryDTO>();
        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + "product_category" + " WHERE " + "gid" +
                    "='" + gid + "'"+" AND " + "status" + "='"+ "0" + "'";

//            String selectQuery = "SELECT  order_data FROM "
//            + "ssgorder" + " WHERE "
//            + "retailer_id" + "='" + retailerId+ "'"+" AND " + "mydate" + "='"+ myDate + "'";

            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    CategoryDTO symptomList = new CategoryDTO();
                    symptomList.setId(cursor.getString(0));
                    symptomList.setGid(cursor.getString(1));
                    symptomList.setG_name(cursor.getString(2));
                    symptomList.setG_code(cursor.getString(3));
                    symptomList.setName(cursor.getString(4));
                    symptomList.setShort_name(cursor.getString(5));
                    symptomList.setAvg_price(cursor.getString(6));
                    symptomList.setOffer_type(cursor.getString(23));

                    symptomLists.add(symptomList);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
         //   Log.e("Exception: ", e + "<>");
        }

        // return category list
        return symptomLists;
    }


    public static ArrayList<CategoryDTO> getAllCategoryProductReport(SQLiteOpenHelper dbHelper, String gid) {
        ArrayList<CategoryDTO> symptomLists = new ArrayList<CategoryDTO>();
        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + "product_category" + " WHERE " + "gid" +
                    "='" + gid + "'";


            CategoryDTO sss = new CategoryDTO();
            sss.setId("");
            sss.setName("----Category---");
            symptomLists.add(sss);


            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    CategoryDTO symptomList = new CategoryDTO();
                    symptomList.setId(cursor.getString(0));
                    symptomList.setGid(cursor.getString(1));
                    symptomList.setG_name(cursor.getString(2));
                    symptomList.setG_code(cursor.getString(3));
                    symptomList.setName(cursor.getString(4));
                    symptomList.setShort_name(cursor.getString(5));
                    symptomList.setAvg_price(cursor.getString(6));
                    symptomList.setOffer_type(cursor.getString(23));

                    symptomLists.add(symptomList);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

        // return category list
        return symptomLists;
    }

// get all data form
    public ArrayList<CategoryDTO> getAllCategoryProduct(SQLiteOpenHelper sqLiteOpenHelper){

        ArrayList<CategoryDTO> symptomLists = new ArrayList<CategoryDTO>();
        myDataBase = getReadableDatabase();

        Cursor cursor = myDataBase.query("product_category",new String[]{"id","gid","g_name","g_code","name","short_name","avg_price","offer_type",},null,null,null,null,null);

        if (cursor.moveToFirst()){
            do{
                CategoryDTO symptomList = new CategoryDTO();
                symptomList.setId(cursor.getString(0));
                symptomList.setGid(cursor.getString(1));
                symptomList.setG_name(cursor.getString(2));
                symptomList.setG_code(cursor.getString(3));
                symptomList.setName(cursor.getString(4));
                symptomList.setShort_name(cursor.getString(5));
                symptomList.setAvg_price(cursor.getString(6));
                symptomList.setOffer_type(cursor.getString(23));

                symptomLists.add(symptomList);
            }while (cursor.moveToNext());
        }
        return symptomLists;
    }

// get all data form
    public ArrayList<CategoryDTO> getAllCategoryProduct1(SQLiteOpenHelper sqLiteOpenHelper){

        ArrayList<CategoryDTO> symptomLists = new ArrayList<CategoryDTO>();
        myDataBase = getReadableDatabase();

        Cursor cursor = myDataBase.query("order",new String[]{"id","data",},null,null,null,null,null);

        if (cursor.moveToFirst()){
            do{
                CategoryDTO symptomList = new CategoryDTO();
                symptomList.setId(cursor.getString(0));
                symptomList.setGid(cursor.getString(1));


                symptomLists.add(symptomList);
            }while (cursor.moveToNext());
        }
        return symptomLists;
    }
//    }
    public static  int getSize(){

        int size = 0;

        return  size;
    }

public static ArrayList<DBOrderDTO> getAllOrderList(SQLiteOpenHelper dbHelper) {
    ArrayList<DBOrderDTO> productDTOS = new ArrayList<DBOrderDTO>();

    try {
        // Select All Query
//            String selectQuery = "SELECT  * FROM " + "ssgorder" + " WHERE " + "retailer_id" +
//                    "='" + retailerId + "'";


        String selectQuery = "SELECT  * FROM " + "ssgorder";

   //     Log.e("get dta>>",selectQuery+"");
//            String selectQuery = "SELECT  * FROM " + "retailer" + " WHERE " + "rid" +
//                    "='" + categoryId + "'";

        // getWritableDatabase create and/or open a database that will be
        // used
        // for reading and writing. The first time this is called
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Cursor provides random read-write access to the result set
        // returned
        // by a database query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                DBOrderDTO productDTO = new DBOrderDTO();

                productDTO.setRetailerId(cursor.getString(0));
                productDTO.setOrderData(cursor.getString(1));
                productDTO.setRouteId(cursor.getString(2));
                productDTO.setMyDate(cursor.getString(3));
                productDTO.setRetailerName(cursor.getString(4));
                productDTO.setRouteName(cursor.getString(5));
                productDTO.setPoientId(cursor.getString(6));
                // adding category to list
                productDTOS.add(productDTO);
            } while (cursor.moveToNext());
        }

    } catch (Exception e) {
        // TODO: handle exception
        Log.e("Exception: ", e + "<>");
    }

    // return category list
    return productDTOS;
}
    public static ArrayList<DBOrderDTO> getTodaysOrderListByDate(SQLiteOpenHelper dbHelper,String myDate,String userId) {
        ArrayList<DBOrderDTO> productDTOS = new ArrayList<DBOrderDTO>();

        try {
            // Select All Query
//            String selectQuery = "SELECT  * FROM " + "ssgorder" + " WHERE " + "retailer_id" +
//                    "='" + retailerId + "'";


            String selectQuery = "SELECT  * FROM " + "ssgorder" + " WHERE " +  "mydate" + "='"+ myDate + "'"+" AND " + "userid" + "='"+ userId + "'";

            // + "retailer_id" + "='" + retailerId+ "'"+" AND " + "mydate" + "='"+ myDate + "'";

         //   Log.e("get dta>>",selectQuery+"");
//            String selectQuery = "SELECT  * FROM " + "retailer" + " WHERE " + "rid" +
//                    "='" + categoryId + "'";

            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    DBOrderDTO productDTO = new DBOrderDTO();

                    productDTO.setRetailerId(cursor.getString(0));
                    productDTO.setOrderData(cursor.getString(1));
                    productDTO.setRouteId(cursor.getString(2));
                    productDTO.setMyDate(cursor.getString(3));
                    productDTO.setRetailerName(cursor.getString(4));
                    productDTO.setRouteName(cursor.getString(5));
                    productDTO.setPoientId(cursor.getString(6));

                    // adding category to list
                    productDTOS.add(productDTO);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

        // return category list
        return productDTOS;
    }

    public String getOrderByRetailerId(SQLiteOpenHelper sqLiteOpenHelper,String retailerId,String myDate) {

        String orderDAta = "";
        myDataBase = getReadableDatabase();
        Cursor cursor = null;

        // Select All Query
        String selectQuery = "SELECT  order_data FROM "
                + "ssgorder" + " WHERE "
                + "retailer_id" + "='" + retailerId+ "'"+" AND " + "mydate" + "='"+ myDate + "'";

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        try {
            cursor = myDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                orderDAta = cursor.getString(0);
            }
        }finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return orderDAta;
    }

    public static ArrayList<DBOrderDTO> getTodaysOrderListByRetailerID(SQLiteOpenHelper dbHelper,String retailerId) {
        ArrayList<DBOrderDTO> productDTOS = new ArrayList<DBOrderDTO>();

        try {

            String selectQuery = "SELECT  * FROM " + "ssgorder" + " WHERE " +  "mydate" + "='"+ retailerId + "'";

        //    Log.e("get dta>>",selectQuery+"");

            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    DBOrderDTO productDTO = new DBOrderDTO();

                    productDTO.setRetailerId(cursor.getString(0));
                    productDTO.setOrderData(cursor.getString(1));
                    productDTO.setRouteId(cursor.getString(2));
                    productDTO.setMyDate(cursor.getString(3));
                    productDTO.setRetailerName(cursor.getString(4));
                    productDTO.setRouteName(cursor.getString(5));
                    productDTO.setPoientId(cursor.getString(6));

                    // adding category to list
                    productDTOS.add(productDTO);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

        // return category list
        return productDTOS;
    }
    public static ArrayList<DBOrderDTO> getOrderList(SQLiteOpenHelper dbHelper, String retailerId,String myDate) {
        ArrayList<DBOrderDTO> productDTOS = new ArrayList<DBOrderDTO>();

        try {
            // Select All Query
//            String selectQuery = "SELECT  * FROM " + "ssgorder" + " WHERE " + "retailer_id" +
//                    "='" + retailerId + "'";


            String selectQuery = "SELECT  * FROM " + "ssgorder" + " WHERE " + "retailer_id" +
                    "='" + retailerId + "'"+" AND " + "mydate" + "='"+ myDate + "'";

        //    Log.e("get dta>>",selectQuery+"");
//            String selectQuery = "SELECT  * FROM " + "retailer" + " WHERE " + "rid" +
//                    "='" + categoryId + "'";

            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    DBOrderDTO productDTO = new DBOrderDTO();

                    productDTO.setRetailerId(cursor.getString(0));
                    productDTO.setOrderData(cursor.getString(1));
                    productDTO.setRouteId(cursor.getString(2));
                    productDTO.setMyDate(cursor.getString(3));
                    productDTO.setRetailerName(cursor.getString(4));
                    productDTO.setRouteName(cursor.getString(5));
                    productDTO.setPoientId(cursor.getString(7));



                    // adding category to list
                    productDTOS.add(productDTO);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
        //    Log.e("Exception: ", e + "<>");
        }

        // return category list
        return productDTOS;
    }

    public static void insertOrder(SQLiteOpenHelper dbHelper,
                                   String routeId,String routeName,
                                   String retailerId,String retailerName,
                                   String poientId,
                                   String orderData,String myDate,String userId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

String sql1 = "insert into " + "ssgorder" + " (" + "route_id" + "," +
        "" + "route_name" + "," + "retailer_id" + "," +
        "" + "retailer_name" + ","+ "poient_id" + "," + "order_data" + ","  + "mydate" + ","+"userid"+") values('"+routeId+  "','"+routeName+  "','"+retailerId+ "','"+retailerName+  "','"+poientId+  "','"+orderData+  "','"+myDate+"','"+userId+"');";


        try {
            db.execSQL(sql1);
        //    Log.e("sql: ", sql1 + "<>");

        } catch (SQLException e) {

        }
    }

    public static void updateOrder(SQLiteOpenHelper dbHelper,String retailer_id,String orderData) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE ssgorder   SET order_data='"+orderData+  "' WHERE retailer_id= '"+retailer_id+"'");

    }

    public static void deleteAOrder(SQLiteOpenHelper dbHelper,String retailerId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM ssgorder  WHERE retailer_id='"+retailerId+ "'");

    }
public static void deleteAOrder(SQLiteOpenHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //db.execSQL("DELETE FROM ssgorder  WHERE ''='"+retailerId+ "'");

    }

    public static void insertProduct(SQLiteOpenHelper dbHelper,
                                   String id,String companyid,String category_id,String sub_cat_id,String sap_code,String product_map,String old_code
            ,String sub_group,String name,String ims_name,String commission, String mrp,String depo
            ,String distri,String realtimeprice,String unit,String dateandtime, String user,String factor
            ,String order_by,String status,String stock_qty,String mkt_stock, String vat_percen,String order_status,
                                     String active_date,String product_msg,String vat_sap_code,String vat_user,String int_sap_code,
                                     String ims_stat) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql1 = "insert into " + "product" + " (" + "id" + ", " + "companyid" + "," + "category_id" + "," + "sub_cat_id" + "," +
                ""+ "sap_code" + "," + "product_map" + ","  + "old_code"+ "," +
                "sub_group"+","+"name"+","+"ims_name"+","+"commission"+","+"mrp"+","+"depo"+","+"distri"+","+"realtimeprice"+"" +
                ""+"unit"+","+"dateandtime"+","+"user"+","+"factor"+","+"order_by"+","+"status"+","+"stock_qty"+","+"mkt_stock"+"," +
                ""+"vat_percen"+","+"order_status"+","+"active_date"+","+"product_msg"+","+"vat_sap_code"+","+"vat_user"+","+"int_sap_code"+","+"ims_stat"+")"
                + " values('"+id+  "','"+companyid+  "','"+category_id+ "','"+sub_cat_id+  "','"+sap_code+  "','"+product_map+  "','"+old_code+"'," +
                "'"+sub_group+  "','"+name+  "','"+ims_name+ "','"+commission+  "','"+mrp+  "','"+depo+  "','"+distri+"'" +
                "'"+realtimeprice+"','"+unit+"','"+dateandtime+"','"+user+"','"+factor+"','"+order_by+"','"+status+"'" +
 "'"+stock_qty+"','"+mkt_stock+"','"+vat_percen+"','"+order_status+"','"+active_date+"','"+product_msg+"','"+vat_sap_code+"','"+vat_user+"','"+int_sap_code+"','"+ims_stat+"');";
        try {
            db.execSQL(sql1);
          //  Log.e("sql: ", sql1 + "<>");
        } catch (SQLException e) {
        }
    }


    public static void insertProductCategory(SQLiteOpenHelper dbHelper,
                                   String id,String gid,String g_name,String g_code,String name,String short_name,String global_company_id
            ,String status,String unit,String avg_price,String factor, String user,String date_time
            ,String order_by,String company_id,String plant_code,String vat_percent, String order_by_la,String top_group
            ,String top_name,String cat_id,String offer_group,String LAF, String offer_type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql1 = "insert into " + "product_category" + " (" + "id" + ", " + "gid" + "," + "g_name" + "," + "g_code" + ","+ "name" + "," + "short_name" + ","  + "global_company_id"+ "," +
                ""+"status"+","+"unit"+","+"avg_price"+","+"factor"+","+"user"+","+"date_time"+","+"order_by"+","+"company_id"+"" +
                ""+"plant_code"+","+"vat_percent"+","+"order_by_la"+","+"top_group"+","+"top_name"+","+"cat_id"+","+"offer_group"+","+"LAF"+","+"offer_type"+")"
                + " values('"+id+  "','"+gid+  "','"+g_name+ "','"+g_code+  "','"+name+  "','"+short_name+  "','"+global_company_id+"'," +
                "'"+status+  "','"+unit+  "','"+avg_price+ "','"+factor+  "','"+user+  "','"+date_time+  "','"+order_by+"'" +
                "'"+company_id+"','"+plant_code+"','"+vat_percent+"','"+order_by_la+"','"+top_group+"','"+top_name+"','"+cat_id+"'" +
                "'"+offer_group+"','"+LAF+"','"+offer_type+"');";
        try {
            db.execSQL(sql1);
          //  Log.e("sql: ", sql1 + "<>");

        } catch (SQLException e) {
        }
    }

    public static void insertRetailer(SQLiteOpenHelper dbHelper,
                                   String retailer_id,String name,String division,String territory,String point_id,String rid,String shop_type
            ,String owner,String mobile,String tnt,String email, String dateandtime,String user
            ,String status,String dob,String vAddress,String global_company_id, String inactive_user,String inactive_date_time
            ,String inactive_ip,String iApproval,String reminding_commission_balance,String opening_balance, String opening_balance_accessories,
                                      String serial,String after_retailers) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql1 = "insert into " + "retailers" + " (" + "retailer_id" + ", " + "name" + "," + "division" + "," + "territory" + ","+ "point_id" + "," + "rid" + ","  + "shop_type"+ "," +
                ""+"owner"+","+"mobile"+","+"tnt"+","+"email"+","+"dateandtime"+","+"user"+","+"status"+","+"dob"+"," +
                ""+"vAddress"+","+"global_company_id"+","+"inactive_user"+","+"inactive_date_time"+","+"inactive_ip"+"," +
                ""+"iApproval"+","+"reminding_commission_balance"+","+"opening_balance"+"," +
                ""+"opening_balance_accessories"+","+"serial"+","+"after_retailers"+")"
                + " values('"+retailer_id+  "','"+name+  "','"+division+ "','"+territory+  "','"+point_id+  "','"+rid+  "','"+shop_type+"'," +
                "'"+owner+  "','"+mobile+  "','"+tnt+ "','"+email+  "','"+dateandtime+  "','"+user+  "','"+status+"'," +
                "'"+dob+"','"+vAddress+"','"+global_company_id+"','"+inactive_user+"','"+inactive_date_time+"','"+inactive_ip+"','"+iApproval+"'," +
                "'"+reminding_commission_balance+"','"+opening_balance+"','"+opening_balance_accessories+"','"+serial+"','"+after_retailers+"');";
        try {
            db.execSQL(sql1);
         //   Log.e("sql: ", sql1 + "<>");

        } catch (SQLException e) {
        }
    }


    public static void insertOrUpdateNewRetailer(SQLiteOpenHelper dbHelper, String retailer_id,String name,String division,String territory,String point_id,String rid,String shop_type
            ,String owner,String mobile,String tnt,String email, String dateandtime,String user
            ,String status,String dob,String vAddress,String global_company_id, String inactive_user,String inactive_date_time
            ,String inactive_ip,String iApproval,String reminding_commission_balance,String opening_balance, String opening_balance_accessories,
             String serial,String after_retailers,String lat,String lon) {

        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + "retailers" + " WHERE " + "retailer_id" +
                    "='" + retailer_id + "'";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

          //  Log.e("ret id>>> >>: ", retailer_id + "<>");

            if (cursor.moveToFirst()) {
                do {
                    try {

                    db.execSQL("UPDATE retailers   SET name='"+name+"',division='"+division+"',territory='"+territory+"',point_id='"+point_id+"',rid='"+rid+"',shop_type='"+shop_type+"',owner='"+owner+  "',mobile='"+mobile+  "',tnt='"+tnt+ "',email='"+email+  "',dateandtime='"+dateandtime+  "',user='"+user+"',status='"+status+"',dob='"+dob+"',vAddress='"+vAddress+"',global_company_id='"+global_company_id+"',inactive_user='"+inactive_user+"',inactive_date_time='"+inactive_date_time+"',inactive_ip='"+inactive_ip+"',iApproval='"+iApproval+"',reminding_commission_balance='"+reminding_commission_balance+"',opening_balance='"+opening_balance+"',opening_balance_accessories='"+opening_balance_accessories+"',serial='"+serial+"',after_retailers='"+after_retailers+"',lat='"+lat+"',lon='"+lon+"' WHERE retailer_id= '"+retailer_id+"'");
                    } catch (SQLException e) {
                      //  Log.e("update >>: ", e + "<>");
                    }

                } while (cursor.moveToNext());
            }else {

                String sql1 = "insert into " + "retailers" + " (" + "retailer_id" + ", " + "name" + "," + "division" + "," + "territory" + ","+ "point_id" + "," + "rid" + ","  + "shop_type"+ "," +
                        ""+"owner"+","+"mobile"+","+"tnt"+","+"email"+","+"dateandtime"+","+"user"+","+"status"+","+"dob"+"," +
                        ""+"vAddress"+","+"global_company_id"+","+"inactive_user"+","+"inactive_date_time"+","+"inactive_ip"+"," +
                        ""+"iApproval"+","+"reminding_commission_balance"+","+"opening_balance"+"," +
                        ""+"opening_balance_accessories"+","+"serial"+","+"after_retailers"+","+"lat"+","+"lon"+")"
                        + " values('"+retailer_id+  "','"+name+  "','"+division+ "','"+territory+  "','"+point_id+  "','"+rid+  "','"+shop_type+"'," +
                        "'"+owner+  "','"+mobile+  "','"+tnt+ "','"+email+  "','"+dateandtime+  "','"+user+  "','"+status+"'," +
                        "'"+dob+"','"+vAddress+"','"+global_company_id+"','"+inactive_user+"','"+inactive_date_time+"','"+inactive_ip+"','"+iApproval+"'," +
                        "'"+reminding_commission_balance+"','"+opening_balance+"','"+opening_balance_accessories+"','"+serial+"','"+after_retailers+"','"+lat+"','"+lon+"');";

                try {
                    db.execSQL(sql1);

                } catch (SQLException e) {
                    Log.e("Exception: ",retailer_id+"<<<>>>>"+ e + "<>");

                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

        // return category list
    }
public static void insertOrUpdateCategory(SQLiteOpenHelper dbHelper,
                                          String id,String gid,String g_name,String g_code,String name,String short_name,String global_company_id
        ,String status,String unit,String avg_price,String factor, String user,String date_time
        ,String order_by,String company_id,String plant_code,String vat_percent, String order_by_la,String top_group
        ,String top_name,String cat_id,String offer_group,String LAF, String offer_type) {

        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + "product_category" + " WHERE " + "id" +
                    "='" + id + "'";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

          //  Log.e("idd >>: ", id + "<>");

            if (cursor.moveToFirst()) {
                do {
                    try {

                    db.execSQL("UPDATE product_category   SET gid='"+gid+  "',g_name='"+g_name+ "',g_code='"+g_code+  "',name='"+name+  "',short_name='"+short_name+  "',global_company_id='"+global_company_id+"',status='"+status+  "',unit='"+unit+  "',avg_price='"+avg_price+ "',factor='"+factor+  "',user='"+user+  "',date_time='"+date_time+  "',order_by='"+order_by+"',company_id='"+company_id+"',plant_code='"+plant_code+"',vat_percent='"+vat_percent+"',order_by_la='"+order_by_la+"',top_group='"+top_group+"',top_name='"+top_name+"',cat_id='"+cat_id+"',offer_group='"+offer_group+"',LAF='"+LAF+"',offer_type='"+offer_type+"'   WHERE id= '"+id+"'");
                    } catch (SQLException e) {
                     //   Log.e("excep >>: ", e + "<>");
                    }

                } while (cursor.moveToNext());
            }else {

                String sql1 = "insert into " + "product_category" + " (" + "id" + ", " + "gid" + "," + "g_name" + "," + "g_code" + ","+ "name" + "," + "short_name" + ","  + "global_company_id"+ "," +
                        ""+"status"+","+"unit"+","+"avg_price"+","+"factor"+","+"user"+","+"date_time"+","+"order_by"+","+"company_id"+"," +
                        ""+"plant_code"+","+"vat_percent"+","+"order_by_la"+","+"top_group"+","+"top_name"+","+"cat_id"+","+"offer_group"+","+"LAF"+","+"offer_type"+")"
                        + " values('"+id+  "','"+gid+  "','"+g_name+ "','"+g_code+  "','"+name+  "','"+short_name+  "','"+global_company_id+"'," +
                        "'"+status+  "','"+unit+  "','"+avg_price+ "','"+factor+  "','"+user+  "','"+date_time+  "','"+order_by+"'," +
                        "'"+company_id+"','"+plant_code+"','"+vat_percent+"','"+order_by_la+"','"+top_group+"','"+top_name+"','"+cat_id+"'," +
                        "'"+offer_group+"','"+LAF+"','"+offer_type+"');";
                try {
                    db.execSQL(sql1);
                 //   Log.e("sql: ", sql1 + "<>");

                } catch (SQLException e) {
                   // Log.e("excep >>: ", e + "<>");
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

        // return category list
    }


    public static void insertOrUpdateProduct(SQLiteOpenHelper dbHelper,
                                   String id,String companyid,String category_id,String sub_cat_id,String sap_code,String product_map,String old_code
            ,String sub_group,String name,String ims_name,String commission, String mrp,String depo
            ,String distri,String realtimeprice,String unit,String dateandtime, String user,String factor
            ,String order_by,String status,String stock_qty,String mkt_stock, String vat_percen,String order_status,
                                              String active_date,String product_msg,String vat_sap_code,String vat_user,String int_sap_code,
                                              String ims_stat) {
      //  Log.e("<<>>",category_id + "<cid>");

        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + "product" + " WHERE " + "id" +
                    "='" + id + "'";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);



            if (cursor.moveToFirst()) {
                do {
                    try {
                      //  Log.e("<<>>",category_id + "<cid1>");
                    db.execSQL("UPDATE product   SET companyid='"+companyid+  "',category_id='"+category_id+ "',sub_cat_id='"+sub_cat_id+  "',sap_code='"+sap_code+  "',product_map='"+product_map+  "',old_code='"+old_code+"',sub_group='"+sub_group+  "',name='"+name+  "',ims_name='"+ims_name+ "',commission='"+commission+  "',mrp='"+mrp+  "',depo='"+depo+ "',distri='"+distri+"',realtimeprice='"+realtimeprice+"',unit='"+unit+"',dateandtime='"+dateandtime+"',user='"+user+"',factor='"+factor+"',order_by='"+order_by+"',status='"+status+"',stock_qty='"+stock_qty+"',mkt_stock='"+mkt_stock+"',vat_percen='"+vat_percen+"',order_status='"+order_status+"',active_date='"+active_date+"',product_msg='"+product_msg+"',vat_sap_code='"+vat_sap_code+"',vat_user='"+vat_user+"',int_sap_code='"+int_sap_code+"',ims_stat='"+ims_stat+"'   WHERE id= '"+id+"'");
                    } catch (SQLException e) {
                     //   Log.e("<<>>: ", e + "<e>");
                    }

                } while (cursor.moveToNext());
            }else {
              //  Log.e("<<>>",category_id + "<cidj>");
                String sql1 = "insert into " + "product" + " (" + "id" + ", " + "companyid" + "," + "category_id" + "," + "sub_cat_id" + "," +
                        ""+ "sap_code" + "," + "product_map" + ","  + "old_code"+ "," +
                        "sub_group"+","+"name"+","+"ims_name"+","+"commission"+","+"mrp"+","+"depo"+","+"distri"+","+"realtimeprice"+"," +
                        ""+"unit"+","+"dateandtime"+","+"user"+","+"factor"+","+"order_by"+","+"status"+","+"stock_qty"+","+"mkt_stock"+"," +
                        ""+"vat_percen"+","+"order_status"+","+"active_date"+","+"product_msg"+","+"vat_sap_code"+","+"vat_user"+","+"int_sap_code"+","+"ims_stat"+")"
                        + " values('"+id+  "','"+companyid+  "','"+category_id+ "','"+sub_cat_id+  "','"+sap_code+  "','"+product_map+  "','"+old_code+"'," +
                        "'"+sub_group+  "','"+name+  "','"+ims_name+ "','"+commission+  "','"+mrp+  "','"+depo+  "','"+distri+"'," +
                        "'"+realtimeprice+"','"+unit+"','"+dateandtime+"','"+user+"','"+factor+"','"+order_by+"','"+status+"'," +
                        "'"+stock_qty+"','"+mkt_stock+"','"+vat_percen+"','"+order_status+"','"+active_date+"','"+product_msg+"','"+vat_sap_code+"','"+vat_user+"','"+int_sap_code+"','"+ims_stat+"');";
                try {
                    db.execSQL(sql1);
                   // Log.e("sql: ", sql1 + "<>");

                } catch (SQLException e) {
                    Log.e("excep >>: ", e + "<>");
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

        // return category list
    }



    public static void insertOrderStatus(SQLiteOpenHelper dbHelper,String foId,String retailerId,String date,String status,String routeId,String sync,String statusdata,String retailername) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql1 = "insert into " + "status_order_visit_nonvisit" + " (foid,retailerid,date,status,routeid,sync,statusdata,retailername) values('"+foId+"','"+retailerId+"','"+date+"','"+status+"','"+routeId+"','"+sync+"','"+statusdata+"','"+retailername+"');";


        try {
            db.execSQL(sql1);
         //   Log.e("sql: >> ", sql1 + "<>");

        } catch (SQLException e) {
           // Log.e("sql: >> ", e.toString() + "<>");
        }
    }
    public static ArrayList<OrderVisitStatusDTO> getAllStatus(SQLiteOpenHelper dbHelper, String foid, String date) {
        ArrayList<OrderVisitStatusDTO> visitLists = new ArrayList<OrderVisitStatusDTO>();
        try {
            // Select All Query
           String selectQuery = "SELECT  * FROM status_order_visit_nonvisit WHERE foid='" + foid + "' AND date='"+ date + "'";


            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    OrderVisitStatusDTO retailerDTO = new OrderVisitStatusDTO();
                    retailerDTO.setFoId(cursor.getString(0));
                    retailerDTO.setRetailerId(cursor.getString(1));
                    retailerDTO.setDate(cursor.getString(2));
                    retailerDTO.setStatus(cursor.getString(3));
                    retailerDTO.setRouteId(cursor.getString(4));

                    visitLists.add(retailerDTO);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
         //   Log.e("Exception: ", e + "<>");
        }

        // return category list
        return visitLists;
    }

    public static ArrayList<OrderVisitStatusDTO> getAllStatus_offline(SQLiteOpenHelper dbHelper, String foid,String sync,String date) {
        ArrayList<OrderVisitStatusDTO> symptomLists = new ArrayList<OrderVisitStatusDTO>();
        try {
            // Select All Query
           String selectQuery = "SELECT  * FROM status_order_visit_nonvisit WHERE foid='" + foid + "' AND sync='" + sync + "' AND date='"+ date + "'";


            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    OrderVisitStatusDTO retailerDTO = new OrderVisitStatusDTO();
                    retailerDTO.setFoId(cursor.getString(0));
                    retailerDTO.setRetailerId(cursor.getString(1));
                    retailerDTO.setDate(cursor.getString(2));
                    retailerDTO.setStatus(cursor.getString(3));
                    retailerDTO.setRouteId(cursor.getString(4));
                    retailerDTO.setSync(cursor.getString(5));
                    retailerDTO.setStatusData(cursor.getString(6));
                    retailerDTO.setRetailerName(cursor.getString(7));

                    symptomLists.add(retailerDTO);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
          //  Log.e("Exception: ", e + "<>");
        }

        // return category list
        return symptomLists;
    }

    public static void updateOrderVisitOffline(SQLiteOpenHelper dbHelper,String retailer_id,String sync) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE status_order_visit_nonvisit   SET sync='"+sync+  "' WHERE retailerid= '"+retailer_id+"'");


    }

    public static void deleteOrderStatus(SQLiteOpenHelper dbHelper,String date) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM status_order_visit_nonvisit  WHERE date != '"+date +"'");

    }
    public static void deleteCategoryTable(SQLiteOpenHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM product_category");

    }

    public static void deleteRetailerTable(SQLiteOpenHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM retailers");

    }

    public static void insertReturnChangeStatus(SQLiteOpenHelper dbHelper,String foId,String retailerId,String retailername,String status,String date) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql1 = "insert into " + "return_change_status" + " (foid,retailerid,retailername,status,date) values('"+foId+"','"+retailerId+"','"+retailername+"','"+status+"','"+date+"');";


        try {
            db.execSQL(sql1);
         //   Log.e("sql: >> ", sql1 + "<>");

        } catch (SQLException e) {
      //      Log.e("sql: >> ", e.toString() + "<>");
        }
    }
    public static ArrayList<OrderVisitStatusDTO> getAllReturnChangeStatus(SQLiteOpenHelper dbHelper, String foid, String date) {
        ArrayList<OrderVisitStatusDTO> symptomLists = new ArrayList<OrderVisitStatusDTO>();
        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM return_change_status WHERE foid='" + foid + "' AND date='"+ date + "'";


            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    OrderVisitStatusDTO retailerDTO = new OrderVisitStatusDTO();
                    retailerDTO.setFoId(cursor.getString(0));
                    retailerDTO.setRetailerId(cursor.getString(1));
                    retailerDTO.setRetailerName(cursor.getString(2));
                    retailerDTO.setStatus(cursor.getString(3));

                    symptomLists.add(retailerDTO);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

        // return category list
        return symptomLists;
    }
    public static void insertWastageStatus(SQLiteOpenHelper dbHelper,String foid,String retailerid,String retailername,String status,String date) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql1 = "insert into " + "wastage_status" + " (foid,retailerid,retailername,status,date) values('"+foid+"','"+retailerid+"','"+retailername+"','"+status+"','"+date+"');";


        try {
            db.execSQL(sql1);
          //  Log.e("sql: >> ", sql1 + "<>");

        } catch (SQLException e) {
          //  Log.e("sql: >> ", e.toString() + "<>");
        }
    }



    public static ArrayList<OrderVisitStatusDTO> getAllWastageStatus(SQLiteOpenHelper dbHelper, String foid, String date) {
        ArrayList<OrderVisitStatusDTO> symptomLists = new ArrayList<OrderVisitStatusDTO>();
        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM wastage_status WHERE foid='" + foid + "' AND date='"+ date + "'";


            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    OrderVisitStatusDTO retailerDTO = new OrderVisitStatusDTO();
                    retailerDTO.setFoId(cursor.getString(0));
                    retailerDTO.setRetailerId(cursor.getString(1));
                    retailerDTO.setRetailerName(cursor.getString(2));
                    retailerDTO.setStatus(cursor.getString(3));

                    symptomLists.add(retailerDTO);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

        // return category list
        return symptomLists;
    }
    public static void insertOrUpdateNotice(SQLiteOpenHelper dbHelper,
                 String id,String start_date,String end_date,String notice,String status,String user_type,String read_status
           ) {

        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + "notice" + " WHERE " + "id" +
                    "='" + id + "'";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // Log.e("routeObject >>: ", id + "<>");

            if (cursor.moveToFirst()) {
                do {
                    try {

db.execSQL("UPDATE notice   SET id='"+id+  "',start_date='"+start_date+ "',end_date='"+end_date+  "',notice='"+notice+  "',status='"+status+  "',user_type='"+user_type+"'   WHERE id= '"+id+"'");
                    } catch (SQLException e) {
                        Log.e("excep >>: ", e + "<>");
                    }

                } while (cursor.moveToNext());
            }else {

                String sql1 = "insert into " + "notice" + " (" + "id" + ", " + "start_date" + "," + "end_date" + "," + "notice" + "," +
                        ""+ "status" + "," + "user_type" + "," + "read_status" + ")"
                        + " values('"+id+  "','"+start_date+  "','"+end_date+ "','"+notice+  "','"+status+  "','"+user_type+  "','"+read_status+"');";
                try {
                    db.execSQL(sql1);
                    //    Log.e("sql: ", sql1 + "<>");

                } catch (SQLException e) {
                    //    Log.e("excep >>: ", e + "<>");
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

    }

    public static ArrayList<NoticeDTO> getAllNotice(SQLiteOpenHelper dbHelper,String s) {
        ArrayList<NoticeDTO> noticeDTOS = new ArrayList<NoticeDTO>();
        try {
            // Select All Query

            String selectQuery = "SELECT  * FROM " + "notice" + " WHERE " + "status" +
                    "='" + s + "'";


            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    NoticeDTO noticeList = new NoticeDTO();
                    noticeList.setId(cursor.getString(0));
                    noticeList.setStartDate(cursor.getString(1));
                    noticeList.setEndDate(cursor.getString(2));
                    noticeList.setNotice(cursor.getString(3));
                    noticeList.setStatus(cursor.getString(4));
                    noticeList.setUserType(cursor.getString(5));
                    noticeList.setReadStatus(cursor.getString(6));

                    noticeDTOS.add(noticeList);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

        // return category list
        return noticeDTOS;
    }


    public static int getUnreadCount(SQLiteOpenHelper dbHelper) {


        int unread = 0;
        try {
            // Select All Query


            String selectQuery = "SELECT  * FROM " + "notice" + " WHERE " + "read_status" +
                    "='" + "0" + "'";
           // Log.e("routeObject >>: ", selectQuery + "<>");

            // getWritableDatabase create and/or open a database that will be
            // used
            // for reading and writing. The first time this is called
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor provides random read-write access to the result set
            // returned
            // by a database query
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            unread=  cursor.getCount();
          //  Log.e("routeObject >>: ", unread + "<>");

//            if (cursor.moveToFirst()) {
//                do {
//
//                    NoticeDTO noticeList = new NoticeDTO();
//                    noticeList.setId(cursor.getString(0));
//                    noticeList.setStartDate(cursor.getString(1));
//                    noticeList.setEndDate(cursor.getString(2));
//                    noticeList.setNotice(cursor.getString(3));
//                    noticeList.setStatus(cursor.getString(4));
//                    noticeList.setUserType(cursor.getString(5));
//                    noticeList.setReadStatus(cursor.getString(6));
//
//                } while (cursor.moveToNext());
//            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Exception: ", e + "<>");
        }

        // return category list
        return unread;
    }

    public static void updateNoticeReadStatus(SQLiteOpenHelper dbHelper,String read_status,String id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE notice   SET read_status='"+read_status+  "' WHERE id= '"+id+"'");


    }
}
