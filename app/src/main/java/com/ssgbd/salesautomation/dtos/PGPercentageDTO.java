package com.ssgbd.salesautomation.dtos;

public class PGPercentageDTO {

    private String id = "";
    private String name = "";
    private String target_qty = "";
    private String delivery_qty = "";
    private String achieveRate = "";
    private String target_value = "";
    private String delivery_value = "";


    public String getTarget_value() {
        return target_value;
    }

    public void setTarget_value(String target_value) {
        this.target_value = target_value;
    }

    public String getDelivery_value() {
        return delivery_value;
    }

    public void setDelivery_value(String delivery_value) {
        this.delivery_value = delivery_value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget_qty() {
        return target_qty;
    }

    public void setTarget_qty(String target_qty) {
        this.target_qty = target_qty;
    }

    public String getDelivery_qty() {
        return delivery_qty;
    }

    public void setDelivery_qty(String delivery_qty) {
        this.delivery_qty = delivery_qty;
    }

    public String getAchieveRate() {
        return achieveRate;
    }

    public void setAchieveRate(String achieveRate) {
        this.achieveRate = achieveRate;
    }



}
