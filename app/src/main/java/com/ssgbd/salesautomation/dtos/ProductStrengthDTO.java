package com.ssgbd.salesautomation.dtos;

public class ProductStrengthDTO {

    private String productName="";
    private String companyName="";
    private String productImage="";
    private String productFeature="";

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductFeature() {
        return productFeature;
    }

    public void setProductFeature(String productFeature) {
        this.productFeature = productFeature;
    }

    @Override
    public String toString() {
        return "ProductStrengthDTO{" +
                "companyName='" + companyName + '\'' +
                ", productImage='" + productImage + '\'' +
                ", productFeature='" + productFeature + '\'' +
                '}';
    }
}
