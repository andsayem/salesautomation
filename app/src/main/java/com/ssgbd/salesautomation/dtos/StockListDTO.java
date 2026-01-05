package com.ssgbd.salesautomation.dtos;

/**
 * Created by rashed on 5/18/2018.
 */

public class StockListDTO {

    private String productid = "";
    private String productName = "";
    private String stockQty = "";
    private String stockValue = "";

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStockQty() {
        return stockQty;
    }

    public void setStockQty(String stockQty) {
        this.stockQty = stockQty;
    }

    public String getStockValue() {
        return stockValue;
    }

    public void setStockValue(String stockValue) {
        this.stockValue = stockValue;
    }

    @Override
    public String toString() {
        return "StockListDTO{" +
                "productid='" + productid + '\'' +
                ", productName='" + productName + '\'' +
                ", stockQty='" + stockQty + '\'' +
                ", stockValue='" + stockValue + '\'' +
                '}';
    }
}
