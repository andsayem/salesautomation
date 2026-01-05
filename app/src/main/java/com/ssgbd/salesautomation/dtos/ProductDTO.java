package com.ssgbd.salesautomation.dtos;

/**
 * Created by rashed on 5/18/2018.
 */

public class ProductDTO {

    private String product_id = "";
    private String product_name = "";
    private String product_sap_code = "";
    private String product_price = "";
    private String product_shortCode = "";
    private String product_MRP_price = "";

    public String getProduct_sap_code() {
        return product_sap_code;
    }

    public void setProduct_sap_code(String product_sap_code) {
        this.product_sap_code = product_sap_code;
    }

    public String getProduct_MRP_price() {
        return product_MRP_price;
    }

    public void setProduct_MRP_price(String product_MRP_price) {
        this.product_MRP_price = product_MRP_price;
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

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_shortCode() {
        return product_shortCode;
    }

    public void setProduct_shortCode(String product_shortCode) {
        this.product_shortCode = product_shortCode;
    }
}
