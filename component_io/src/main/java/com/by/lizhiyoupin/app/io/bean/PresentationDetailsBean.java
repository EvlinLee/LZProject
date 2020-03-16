package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/11/19
 * author:jyx
 * function:
 */
public class PresentationDetailsBean {
    /**
     * amount : 300.0
     * commission : 0.5
     * createdTime : 1574042649000
     * id : 2
     * status : 0
     * type : 1
     * updatedTime : 1574042652000
     */

    private double amount;
    private double commission;
    private String createdTime;
    private Long id;
    private int status;
    private int type;
    private String updatedTime;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

}
