package com.ssgbd.salesautomation.dtos;

import java.util.ArrayList;

public class FoFeedbackDTO {

    private String feedback_id = "";
    private String foid = "";
    private String fo_comment = "";
    private String tsm_id = "";
    private String feedbacktext = "";

    private String tsm_comment = "";
    private String dsm_id = "";
    private String dsm_comment = "";
    private String agm_id = "";
    private String agm_comment = "";
    private String scd_id = "";
    private String scd_comment = "";
    private String fo_comment_date = "";
    private String tsm_comment_date = "";
    private String dsm_comment_date = "";
    private String agm_comment_date = "";
    private String scd_comment_date = "";
    private String status = "";
    private String foname = "";
    private String foemail = "";
    private String tsmname = "";
    private String tsmemail = "";
    private String dsmname = "";
    private String dsmemail = "";
    private String agmname = "";
    private String agmemail = "";
    private String scdname = "";
    private String scdemail = "";
    private ArrayList<QuestionDTO> qDTOS = new ArrayList<>();

    public String getFeedbacktext() {
        return feedbacktext;
    }

    public void setFeedbacktext(String feedbacktext) {
        this.feedbacktext = feedbacktext;
    }

    public ArrayList<QuestionDTO> getqDTOS() {
        return qDTOS;
    }

    public void setqDTOS(ArrayList<QuestionDTO> qDTOS) {
        this.qDTOS = qDTOS;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getFoid() {
        return foid;
    }

    public void setFoid(String foid) {
        this.foid = foid;
    }

    public String getFo_comment() {
        return fo_comment;
    }

    public void setFo_comment(String fo_comment) {
        this.fo_comment = fo_comment;
    }

    public String getTsm_id() {
        return tsm_id;
    }

    public void setTsm_id(String tsm_id) {
        this.tsm_id = tsm_id;
    }

    public String getTsm_comment() {
        return tsm_comment;
    }

    public void setTsm_comment(String tsm_comment) {
        this.tsm_comment = tsm_comment;
    }

    public String getDsm_id() {
        return dsm_id;
    }

    public void setDsm_id(String dsm_id) {
        this.dsm_id = dsm_id;
    }

    public String getDsm_comment() {
        return dsm_comment;
    }

    public void setDsm_comment(String dsm_comment) {
        this.dsm_comment = dsm_comment;
    }

    public String getAgm_id() {
        return agm_id;
    }

    public void setAgm_id(String agm_id) {
        this.agm_id = agm_id;
    }

    public String getAgm_comment() {
        return agm_comment;
    }

    public void setAgm_comment(String agm_comment) {
        this.agm_comment = agm_comment;
    }

    public String getScd_id() {
        return scd_id;
    }

    public void setScd_id(String scd_id) {
        this.scd_id = scd_id;
    }

    public String getScd_comment() {
        return scd_comment;
    }

    public void setScd_comment(String scd_comment) {
        this.scd_comment = scd_comment;
    }

    public String getFo_comment_date() {
        return fo_comment_date;
    }

    public void setFo_comment_date(String fo_comment_date) {
        this.fo_comment_date = fo_comment_date;
    }

    public String getTsm_comment_date() {
        return tsm_comment_date;
    }

    public void setTsm_comment_date(String tsm_comment_date) {
        this.tsm_comment_date = tsm_comment_date;
    }

    public String getDsm_comment_date() {
        return dsm_comment_date;
    }

    public void setDsm_comment_date(String dsm_comment_date) {
        this.dsm_comment_date = dsm_comment_date;
    }

    public String getAgm_comment_date() {
        return agm_comment_date;
    }

    public void setAgm_comment_date(String agm_comment_date) {
        this.agm_comment_date = agm_comment_date;
    }

    public String getScd_comment_date() {
        return scd_comment_date;
    }

    public void setScd_comment_date(String scd_comment_date) {
        this.scd_comment_date = scd_comment_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoname() {
        return foname;
    }

    public void setFoname(String foname) {
        this.foname = foname;
    }

    public String getFoemail() {
        return foemail;
    }

    public void setFoemail(String foemail) {
        this.foemail = foemail;
    }

    public String getTsmname() {
        return tsmname;
    }

    public void setTsmname(String tsmname) {
        this.tsmname = tsmname;
    }

    public String getTsmemail() {
        return tsmemail;
    }

    public void setTsmemail(String tsmemail) {
        this.tsmemail = tsmemail;
    }

    public String getDsmname() {
        return dsmname;
    }

    public void setDsmname(String dsmname) {
        this.dsmname = dsmname;
    }

    public String getDsmemail() {
        return dsmemail;
    }

    public void setDsmemail(String dsmemail) {
        this.dsmemail = dsmemail;
    }

    public String getAgmname() {
        return agmname;
    }

    public void setAgmname(String agmname) {
        this.agmname = agmname;
    }

    public String getAgmemail() {
        return agmemail;
    }

    public void setAgmemail(String agmemail) {
        this.agmemail = agmemail;
    }

    public String getScdname() {
        return scdname;
    }

    public void setScdname(String scdname) {
        this.scdname = scdname;
    }

    public String getScdemail() {
        return scdemail;
    }

    public void setScdemail(String scdemail) {
        this.scdemail = scdemail;
    }
}
