package com.ssgbd.salesautomation.dtos;

public class OrderDetailsDTO {

    private String qty = "";
    private String value = "";
    private String skuShortCode = "";

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSkuShortCode() {
        return skuShortCode;
    }

    public void setSkuShortCode(String skuShortCode) {
        this.skuShortCode = skuShortCode;
    }
}
