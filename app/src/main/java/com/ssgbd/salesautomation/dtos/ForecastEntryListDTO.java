package com.ssgbd.salesautomation.dtos;

/**
 * Created by rashed on 5/18/2018.
 */

public class ForecastEntryListDTO {



    private String product_id = "";
    private String product_code = "";
    private String product_name = "";
    private String avg_2022_ims_qty = "";
    private String avg_2023_ims_qty = "";
    private String forecastQty = "";

    public String getForecastQty() {
        return forecastQty;
    }

    public void setForecastQty(String forecastQty) {
        this.forecastQty = forecastQty;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getAvg_2022_ims_qty() {
        return avg_2022_ims_qty;
    }

    public void setAvg_2022_ims_qty(String avg_2022_ims_qty) {
        this.avg_2022_ims_qty = avg_2022_ims_qty;
    }

    public String getAvg_2023_ims_qty() {
        return avg_2023_ims_qty;
    }

    public void setAvg_2023_ims_qty(String avg_2023_ims_qty) {
        this.avg_2023_ims_qty = avg_2023_ims_qty;
    }


    @Override
    public String toString() {
        return "ForecastEntryListDTO{" +
                "product_id='" + product_id + '\'' +
                ", product_code='" + product_code + '\'' +
                ", product_name='" + product_name + '\'' +
                ", avg_2022_ims_qty='" + avg_2022_ims_qty + '\'' +
                ", avg_2023_ims_qty='" + avg_2023_ims_qty + '\'' +
                '}';
    }
}
