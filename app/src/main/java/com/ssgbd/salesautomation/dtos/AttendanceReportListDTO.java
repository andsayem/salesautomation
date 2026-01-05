package com.ssgbd.salesautomation.dtos;

public class AttendanceReportListDTO {

    private String date = "";
    private String inTime = "";
    private String inTimeRetailerName = "";
    private String inTimeRetailerAddress = "";
    private String outTime = "";
    private String outTimeRetailerName = "";
    private String outTimeRetailerAddress = "";
    private String workingHoure = "";
    private String route = "";
    private String leave = "";
    private String leaveName = "";
    private String absent = "";
    private String friday = "";

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getInTimeRetailerName() {
        return inTimeRetailerName;
    }

    public void setInTimeRetailerName(String inTimeRetailerName) {
        this.inTimeRetailerName = inTimeRetailerName;
    }

    public String getInTimeRetailerAddress() {
        return inTimeRetailerAddress;
    }

    public void setInTimeRetailerAddress(String inTimeRetailerAddress) {
        this.inTimeRetailerAddress = inTimeRetailerAddress;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getOutTimeRetailerName() {
        return outTimeRetailerName;
    }

    public void setOutTimeRetailerName(String outTimeRetailerName) {
        this.outTimeRetailerName = outTimeRetailerName;
    }

    public String getOutTimeRetailerAddress() {
        return outTimeRetailerAddress;
    }

    public void setOutTimeRetailerAddress(String outTimeRetailerAddress) {
        this.outTimeRetailerAddress = outTimeRetailerAddress;
    }

    public String getWorkingHoure() {
        return workingHoure;
    }

    public void setWorkingHoure(String workingHoure) {
        this.workingHoure = workingHoure;
    }
}
