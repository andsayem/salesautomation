package com.ssgbd.salesautomation.dtos;

/**
 * Created by rashed on 5/18/2018.
 */

public class TechnicianListDTO {

    private String id = "";
    private String technicianName = "";
    private String technicianPhone = "";
    private String fo_verify = "";
    private String tsm_verify = "";
    private String point_verify = "";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoint_verify() {
        return point_verify;
    }

    public void setPoint_verify(String point_verify) {
        this.point_verify = point_verify;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getTechnicianPhone() {
        return technicianPhone;
    }

    public void setTechnicianPhone(String technicianPhone) {
        this.technicianPhone = technicianPhone;
    }

    public String getFo_verify() {
        return fo_verify;
    }

    public void setFo_verify(String fo_verify) {
        this.fo_verify = fo_verify;
    }

    public String getTsm_verify() {
        return tsm_verify;
    }

    public void setTsm_verify(String tsm_verify) {
        this.tsm_verify = tsm_verify;
    }

    @Override
    public String toString() {
        return "TechnicianListDTO{" +
                "id='" + id + '\'' +
                ", technicianName='" + technicianName + '\'' +
                ", technicianPhone='" + technicianPhone + '\'' +
                ", fo_verify='" + fo_verify + '\'' +
                ", tsm_verify='" + tsm_verify + '\'' +
                ", point_verify='" + point_verify + '\'' +
                '}';
    }
}
