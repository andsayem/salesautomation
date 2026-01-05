package com.ssgbd.salesautomation.dtos;

/**
 * Created by rashed on 5/18/2018.
 */

public class ConfirmOrderDTO {

    private String product_category_id = "";
    private String product_category_name = "";
    private String product_id = "";
    private String product_name = "";
    private String product_qty = "";
    private String wastage_qty = "";
    private String product_price = "";
    private String free_product_qty = "";

    public String getFree_product_qty() {
        return free_product_qty;
    }

    public void setFree_product_qty(String free_product_qty) {
        this.free_product_qty = free_product_qty;
    }

    public String getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(String product_category_id) {
        this.product_category_id = product_category_id;
    }

    public String getProduct_category_name() {
        return product_category_name;
    }

    public void setProduct_category_name(String product_category_name) {
        this.product_category_name = product_category_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getWastage_qty() {
        return wastage_qty;
    }

    public void setWastage_qty(String wastage_qty) {
        this.wastage_qty = wastage_qty;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
