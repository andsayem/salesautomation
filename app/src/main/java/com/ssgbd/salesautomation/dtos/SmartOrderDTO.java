package com.ssgbd.salesautomation.dtos;

public class SmartOrderDTO {
    private String id="";
    private String retailer_id="";
    private String retailer_name="";
    private String retailer_phone="";
    private String lat_long="";
    private String route_id="";
    private String route_name="";
    private String voice="";
    private String point_id="";
    private String point_name="";
    private String sap_code="";

    private String image="";
    private String text="";
    private String order_status="";

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
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

    public String getPoint_name() {
        return point_name;
    }

    public void setPoint_name(String point_name) {
        this.point_name = point_name;
    }

    public String getSap_code() {
        return sap_code;
    }

    public void setSap_code(String sap_code) {
        this.sap_code = sap_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(String retailer_id) {
        this.retailer_id = retailer_id;
    }

    public String getRetailer_name() {
        return retailer_name;
    }

    public void setRetailer_name(String retailer_name) {
        this.retailer_name = retailer_name;
    }

    public String getRetailer_phone() {
        return retailer_phone;
    }

    public void setRetailer_phone(String retailer_phone) {
        this.retailer_phone = retailer_phone;
    }

    public String getLat_long() {
        return lat_long;
    }

    public void setLat_long(String lat_long) {
        this.lat_long = lat_long;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SmartOrderDTO{" +
                "id='" + id + '\'' +
                ", retailer_id='" + retailer_id + '\'' +
                ", retailer_name='" + retailer_name + '\'' +
                ", retailer_phone='" + retailer_phone + '\'' +
                ", lat_long='" + lat_long + '\'' +
                ", route_name='" + route_name + '\'' +
                ", voice='" + voice + '\'' +
                ", image='" + image + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
