package com.ssgbd.salesautomation.dtos;

import java.util.ArrayList;
import java.util.List;

public class ProductRequisitionDTO {

    private int sl;
    private String pointName;
    private String sapCode;
    private String catName;        // PG Name
    private String productCode;
    private String productName;
    private double targetQty;
    private double targetValue;
    private double reqQty;
    private double reqValue;
    private double billedQty;
    private double billedValue;
    private double freeDownQty;

    // Reasons per product
    private List<ReasonDTO> reasons = new ArrayList<>();

    // Constructor
    public ProductRequisitionDTO(int sl, String pointName, String sapCode,
                                 String catName, String productCode, String productName,
                                 double targetQty, double targetValue, double reqQty,
                                 double reqValue, double billedQty, double billedValue,
                                 double freeDownQty, List<ReasonDTO> reasons) {
        this.sl = sl;
        this.pointName = pointName;
        this.sapCode = sapCode;
        this.catName = catName;
        this.productCode = productCode;
        this.productName = productName;
        this.targetQty = targetQty;
        this.targetValue = targetValue;
        this.reqQty = reqQty;
        this.reqValue = reqValue;
        this.billedQty = billedQty;
        this.billedValue = billedValue;
        this.freeDownQty = freeDownQty;
        this.reasons = reasons;
    }

    // Empty constructor
    public ProductRequisitionDTO() {}

    // ===========================
    // Getters & Setters
    // ===========================
    public int getSl() { return sl; }
    public void setSl(int sl) { this.sl = sl; }

    public String getPointName() { return pointName; }
    public void setPointName(String pointName) { this.pointName = pointName; }

    public String getSapCode() { return sapCode; }
    public void setSapCode(String sapCode) { this.sapCode = sapCode; }

    public String getCatName() { return catName; }
    public void setCatName(String catName) { this.catName = catName; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getTargetQty() { return targetQty; }
    public void setTargetQty(double targetQty) { this.targetQty = targetQty; }

    public double getTargetValue() { return targetValue; }
    public void setTargetValue(double targetValue) { this.targetValue = targetValue; }

    public double getReqQty() { return reqQty; }
    public void setReqQty(double reqQty) { this.reqQty = reqQty; }

    public double getReqValue() { return reqValue; }
    public void setReqValue(double reqValue) { this.reqValue = reqValue; }

    public double getBilledQty() { return billedQty; }
    public void setBilledQty(double billedQty) { this.billedQty = billedQty; }

    public double getBilledValue() { return billedValue; }
    public void setBilledValue(double billedValue) { this.billedValue = billedValue; }

    public double getFreeDownQty() { return freeDownQty; }
    public void setFreeDownQty(double freeDownQty) { this.freeDownQty = freeDownQty; }

    public List<ReasonDTO> getReasons() { return reasons; }
    public void setReasons(List<ReasonDTO> reasons) { this.reasons = reasons; }

    // ===========================
    // Reason Helpers
    // ===========================
    public void addReasonQty(int reasonId, int qty) {
        reasons.add(new ReasonDTO(reasonId, "", qty));
    }

    public int getReasonQty(int reasonId) {
        for (ReasonDTO r : reasons) {
            if (r.getId() == reasonId) return r.getDiductionQty();
        }
        return 0;
    }
}