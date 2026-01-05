package com.ssgbd.salesautomation.dtos;

public class OrderRepotListDTO {

    private String orderId="";
    private String orderNO="";
    private String orderDateTime="";
    private String customerName="";
    private String orderQty="";
    private String orderValue="";
    private String deliveryQty="";
    private String deliveryValue="";

    private String replaceQty="";
    private String replaceValue="";
    private String excessAmount="";
    private String free="";
    private String discount="";

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getReplaceQty() {
        return replaceQty;
    }

    public void setReplaceQty(String replaceQty) {
        this.replaceQty = replaceQty;
    }

    public String getReplaceValue() {
        return replaceValue;
    }

    public void setReplaceValue(String replaceValue) {
        this.replaceValue = replaceValue;
    }

    public String getExcessAmount() {
        return excessAmount;
    }

    public void setExcessAmount(String excessAmount) {
        this.excessAmount = excessAmount;
    }

    public String getDeliveryQty() {
        return deliveryQty;
    }

    public void setDeliveryQty(String deliveryQty) {
        this.deliveryQty = deliveryQty;
    }

    public String getDeliveryValue() {
        return deliveryValue;
    }

    public void setDeliveryValue(String deliveryValue) {
        this.deliveryValue = deliveryValue;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(String orderValue) {
        this.orderValue = orderValue;
    }
}
