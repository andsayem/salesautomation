package com.ssgbd.salesautomation.dtos;

import java.util.ArrayList;

public class ProductRequisitionDTO {

    private String catName;
    private String name;
    private String sapCode;
    private int reqDetId;
    private String cancelReason;
    private String reqStatus;
    private int catId;
    private int productId;
    private String reqQty;
    private String reqValue;
    private String billingQty;
    private double billingValue;
    private String mainReqQty;
    private String approvedQty;
    private String allReqDownQty;
    private double allReqDownValue;
    private String allFreeDownQty;
    private String reqDownQty;
    private double reqDownValue;
    private String freeDownQty;
    private double freeDownValue;
    private String inTranQty;
    private String inTranValue;
    private String receiveQty;
    private double receiveValue;
    private int reqId;
    private String reqNo;
    private int pointId;
    private String reqDate;
    private String pointName;
    private int pointSapCode;
    private int businessTypeId;
    private int isDepot;
    private int isDealer;
    private int targetQty;
    private int targetValue;
    private String reqQtyAlt;

    private ArrayList<ReasonDTO> reasons = new ArrayList<>();

    public ProductRequisitionDTO() {
    }

    // ==============================
    // Getter and Setter
    // ==============================

    public String getCatName() { return catName; }
    public void setCatName(String catName) { this.catName = catName; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSapCode() { return sapCode; }
    public void setSapCode(String sapCode) { this.sapCode = sapCode; }

    public int getReqDetId() { return reqDetId; }
    public void setReqDetId(int reqDetId) { this.reqDetId = reqDetId; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public String getReqStatus() { return reqStatus; }
    public void setReqStatus(String reqStatus) { this.reqStatus = reqStatus; }

    public int getCatId() { return catId; }
    public void setCatId(int catId) { this.catId = catId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getReqQty() { return reqQty; }
    public void setReqQty(String reqQty) { this.reqQty = reqQty; }

    public String getReqValue() { return reqValue; }
    public void setReqValue(String reqValue) { this.reqValue = reqValue; }

    public String getBillingQty() { return billingQty; }
    public void setBillingQty(String billingQty) { this.billingQty = billingQty; }

    public double getBillingValue() { return billingValue; }
    public void setBillingValue(double billingValue) { this.billingValue = billingValue; }

    public String getMainReqQty() { return mainReqQty; }
    public void setMainReqQty(String mainReqQty) { this.mainReqQty = mainReqQty; }

    public String getApprovedQty() { return approvedQty; }
    public void setApprovedQty(String approvedQty) { this.approvedQty = approvedQty; }

    public String getAllReqDownQty() { return allReqDownQty; }
    public void setAllReqDownQty(String allReqDownQty) { this.allReqDownQty = allReqDownQty; }

    public double getAllReqDownValue() { return allReqDownValue; }
    public void setAllReqDownValue(double allReqDownValue) { this.allReqDownValue = allReqDownValue; }

    public String getAllFreeDownQty() { return allFreeDownQty; }
    public void setAllFreeDownQty(String allFreeDownQty) { this.allFreeDownQty = allFreeDownQty; }

    public String getReqDownQty() { return reqDownQty; }
    public void setReqDownQty(String reqDownQty) { this.reqDownQty = reqDownQty; }

    public double getReqDownValue() { return reqDownValue; }
    public void setReqDownValue(double reqDownValue) { this.reqDownValue = reqDownValue; }

    public String getFreeDownQty() { return freeDownQty; }
    public void setFreeDownQty(String freeDownQty) { this.freeDownQty = freeDownQty; }

    public double getFreeDownValue() { return freeDownValue; }
    public void setFreeDownValue(double freeDownValue) { this.freeDownValue = freeDownValue; }

    public String getInTranQty() { return inTranQty; }
    public void setInTranQty(String inTranQty) { this.inTranQty = inTranQty; }

    public String getInTranValue() { return inTranValue; }
    public void setInTranValue(String inTranValue) { this.inTranValue = inTranValue; }

    public String getReceiveQty() { return receiveQty; }
    public void setReceiveQty(String receiveQty) { this.receiveQty = receiveQty; }

    public double getReceiveValue() { return receiveValue; }
    public void setReceiveValue(double receiveValue) { this.receiveValue = receiveValue; }

    public int getReqId() { return reqId; }
    public void setReqId(int reqId) { this.reqId = reqId; }

    public String getReqNo() { return reqNo; }
    public void setReqNo(String reqNo) { this.reqNo = reqNo; }

    public int getPointId() { return pointId; }
    public void setPointId(int pointId) { this.pointId = pointId; }

    public String getReqDate() { return reqDate; }
    public void setReqDate(String reqDate) { this.reqDate = reqDate; }

    public String getPointName() { return pointName; }
    public void setPointName(String pointName) { this.pointName = pointName; }

    public int getPointSapCode() { return pointSapCode; }
    public void setPointSapCode(int pointSapCode) { this.pointSapCode = pointSapCode; }

    public int getBusinessTypeId() { return businessTypeId; }
    public void setBusinessTypeId(int businessTypeId) { this.businessTypeId = businessTypeId; }

    public int getIsDepot() { return isDepot; }
    public void setIsDepot(int isDepot) { this.isDepot = isDepot; }

    public int getIsDealer() { return isDealer; }
    public void setIsDealer(int isDealer) { this.isDealer = isDealer; }

    public int getTargetQty() { return targetQty; }
    public void setTargetQty(int targetQty) { this.targetQty = targetQty; }

    public int getTargetValue() { return targetValue; }
    public void setTargetValue(int targetValue) { this.targetValue = targetValue; }

    public String getReqQtyAlt() { return reqQtyAlt; }
    public void setReqQtyAlt(String reqQtyAlt) { this.reqQtyAlt = reqQtyAlt; }

    public ArrayList<ReasonDTO> getReasons() { return reasons; }
    public void setReasons(ArrayList<ReasonDTO> reasons) { this.reasons = reasons; }

}