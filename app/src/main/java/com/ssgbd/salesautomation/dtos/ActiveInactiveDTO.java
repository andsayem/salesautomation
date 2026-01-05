package com.ssgbd.salesautomation.dtos;

public class ActiveInactiveDTO {
    private String retailer_id="";
    private String route_id="";
    private String status="";
    private String active_inactive_status="";
    private String retailer_name="";
    private String division_name="";
    private String territory_name="";
    private String point_name="";
    private String rname="";
    private String business_type="";

    public String getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(String retailer_id) {
        this.retailer_id = retailer_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActive_inactive_status() {
        return active_inactive_status;
    }

    public void setActive_inactive_status(String active_inactive_status) {
        this.active_inactive_status = active_inactive_status;
    }

    public String getRetailer_name() {
        return retailer_name;
    }

    public void setRetailer_name(String retailer_name) {
        this.retailer_name = retailer_name;
    }

    public String getDivision_name() {
        return division_name;
    }

    public void setDivision_name(String division_name) {
        this.division_name = division_name;
    }

    public String getTerritory_name() {
        return territory_name;
    }

    public void setTerritory_name(String territory_name) {
        this.territory_name = territory_name;
    }

    public String getPoint_name() {
        return point_name;
    }

    public void setPoint_name(String point_name) {
        this.point_name = point_name;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }
}
