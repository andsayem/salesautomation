package com.ssgbd.salesautomation.dtos;

/**
 * Created by rashed on 5/18/2018.
 */

public class OrderDTO {

    private String product_category_id = "";
    private String product_category_name = "";
    private String product_id = "";
    private String product_name = "";
    private String product_short_code = "";
    private String product_qty = "";
    private String wastage_qty = "";
    private String product_price = "";
    private String product_depo_price = "";
    private String product_wastage_price = "";
    private String offer_type = "";
    private String categorySubtotal = "";

    public String getOffer_type() {
        return offer_type;
    }

    public String getCategorySubtotal() {
        return categorySubtotal;
    }

    public void setCategorySubtotal(String categorySubtotal) {
        this.categorySubtotal = categorySubtotal;
    }

    public void setOffer_type(String offer_type) {
        this.offer_type = offer_type;
    }



    public String getProduct_depo_price() {
        return product_depo_price;
    }

    public void setProduct_depo_price(String product_depo_price) {
        this.product_depo_price = product_depo_price;
    }

    public String getProduct_wastage_price() {
        return product_wastage_price;
    }

    public void setProduct_wastage_price(String product_wastage_price) {
        this.product_wastage_price = product_wastage_price;
    }

    public String getProduct_short_code() {
        return product_short_code;
    }

    public void setProduct_short_code(String product_short_code) {
        this.product_short_code = product_short_code;
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
