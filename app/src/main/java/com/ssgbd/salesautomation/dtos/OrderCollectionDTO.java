package com.ssgbd.salesautomation.dtos;

public class OrderCollectionDTO {
    private String order_id="";
    private String order_no="";
    private String order_type="";
    private String order_date="";
    private String distributor_id="";
    private String point_id="";
    private String route_id="";
    private String total_value="";
    private String total_delivery_value="";
    private String grand_total_value="";
    private String total_discount_amount="";
    private String due_amount="";

    public String getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(String due_amount) {
        this.due_amount = due_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {
        this.distributor_id = distributor_id;
    }

    public String getPoint_id() {
        return point_id;
    }

    public void setPoint_id(String point_id) {
        this.point_id = point_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getTotal_value() {
        return total_value;
    }

    public void setTotal_value(String total_value) {
        this.total_value = total_value;
    }

    public String getTotal_delivery_value() {
        return total_delivery_value;
    }

    public void setTotal_delivery_value(String total_delivery_value) {
        this.total_delivery_value = total_delivery_value;
    }

    public String getGrand_total_value() {
        return grand_total_value;
    }

    public void setGrand_total_value(String grand_total_value) {
        this.grand_total_value = grand_total_value;
    }

    public String getTotal_discount_amount() {
        return total_discount_amount;
    }

    public void setTotal_discount_amount(String total_discount_amount) {
        this.total_discount_amount = total_discount_amount;
    }
}
