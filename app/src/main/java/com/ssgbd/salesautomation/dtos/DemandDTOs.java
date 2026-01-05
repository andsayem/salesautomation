package com.ssgbd.salesautomation.dtos;

public class DemandDTOs {

    public String categoryId="";
    public String categoryName="";
    public String productId="";
    public String productName="";
    public String productQTY="";
    public String productDemandText="";

    public String getProductDemandText() {
        return productDemandText;
    }

    public void setProductDemandText(String productDemandText) {
        this.productDemandText = productDemandText;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQTY() {
        return productQTY;
    }

    public void setProductQTY(String productQTY) {
        this.productQTY = productQTY;
    }
}
