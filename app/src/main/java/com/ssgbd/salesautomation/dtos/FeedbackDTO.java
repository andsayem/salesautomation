package com.ssgbd.salesautomation.dtos;

public class FeedbackDTO {

    private String id = "";
    private String fo_id = "";
    private String date = "";
    private String rp_value = "";
    private String rp_text = "";
    private String free_pending = "";
    private String competition_facts = "";
    private String complain_box = "";

    private String demandList = "";

    public String getDemandList() {
        return demandList;
    }

    public void setDemandList(String demandList) {
        this.demandList = demandList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFo_id() {
        return fo_id;
    }

    public void setFo_id(String fo_id) {
        this.fo_id = fo_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRp_value() {
        return rp_value;
    }

    public void setRp_value(String rp_value) {
        this.rp_value = rp_value;
    }

    public String getRp_text() {
        return rp_text;
    }

    public void setRp_text(String rp_text) {
        this.rp_text = rp_text;
    }

    public String getFree_pending() {
        return free_pending;
    }

    public void setFree_pending(String free_pending) {
        this.free_pending = free_pending;
    }

    public String getCompetition_facts() {
        return competition_facts;
    }

    public void setCompetition_facts(String competition_facts) {
        this.competition_facts = competition_facts;
    }

    public String getComplain_box() {
        return complain_box;
    }

    public void setComplain_box(String complain_box) {
        this.complain_box = complain_box;
    }
}
