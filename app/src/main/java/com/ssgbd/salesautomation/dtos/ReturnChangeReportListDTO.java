package com.ssgbd.salesautomation.dtos;

/**
 * Created by rashed on 5/18/2018.
 */

public class ReturnChangeReportListDTO {

    private String returnOrderId = "";
    private String returnOrder = "";
    private String returnOrderDate = "";
    private String fo = "";
    private String customer = "";
    private String qty = "";
    private String value = "";
    private String changeqty = "";
    private String changevalue = "";
    private String workignHour = "";

    public String getChangeqty() {
        return changeqty;
    }

    public void setChangeqty(String changeqty) {
        this.changeqty = changeqty;
    }

    public String getChangevalue() {
        return changevalue;
    }

    public void setChangevalue(String changevalue) {
        this.changevalue = changevalue;
    }

    public String getWorkignHour() {
        return workignHour;
    }

    public void setWorkignHour(String workignHour) {
        this.workignHour = workignHour;
    }

    public String getReturnOrderId() {
        return returnOrderId;
    }

    public void setReturnOrderId(String returnOrderId) {
        this.returnOrderId = returnOrderId;
    }

    public String getReturnOrder() {
        return returnOrder;
    }

    public void setReturnOrder(String returnOrder) {
        this.returnOrder = returnOrder;
    }

    public String getReturnOrderDate() {
        return returnOrderDate;
    }

    public void setReturnOrderDate(String returnOrderDate) {
        this.returnOrderDate = returnOrderDate;
    }

    public String getFo() {
        return fo;
    }

    public void setFo(String fo) {
        this.fo = fo;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

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
}
