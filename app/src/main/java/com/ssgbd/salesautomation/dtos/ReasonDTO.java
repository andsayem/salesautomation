package com.ssgbd.salesautomation.dtos;

public class ReasonDTO {

    private int id;
    private String reason;
    private int diductionQty;

    // Parameterized constructor
    public ReasonDTO(int id, String reason, int diductionQty) {
        this.id = id;
        this.reason = reason;
        this.diductionQty = diductionQty;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public int getDiductionQty() { return diductionQty; }
    public void setDiductionQty(int diductionQty) { this.diductionQty = diductionQty; }
}