package com.by.lizhiyoupin.app.io.bean;

/**
 * @title: CommissionReport
 * @projectName: Lzyp2
 * @author: xuezhijia
 * @description: 收益报表
 * @date: 2019/11/6 11:51
 */
public class IncomeReport {
private String title;
    /**
     * 预估总收益
     */
    private double allEstimate;
    /**
     * 预估导购订单佣金
     */
    private double allShoppingEstimate;
    /**
     * 预估京东订单佣金
     */
    private double allJdEstimate;
    /**
     * 预估淘宝订单佣金
     */
    private double allTbEstimate;
    /**
     * 预估拼多多订单佣金
     */
    private double allPddEstimate;
    /**
     * 预估优选订单佣金
     */
    private double allGiftEstimate;

    public double getAllEstimate() {
        return allEstimate;
    }

    public void setAllEstimate(double allEstimate) {
        this.allEstimate = allEstimate;
    }

    public double getAllShoppingEstimate() {
        return allShoppingEstimate;
    }

    public void setAllShoppingEstimate(double allShoppingEstimate) {
        this.allShoppingEstimate = allShoppingEstimate;
    }

    public double getAllJdEstimate() {
        return allJdEstimate;
    }

    public void setAllJdEstimate(double allJdEstimate) {
        this.allJdEstimate = allJdEstimate;
    }

    public double getAllTbEstimate() {
        return allTbEstimate;
    }

    public void setAllTbEstimate(double allTbEstimate) {
        this.allTbEstimate = allTbEstimate;
    }

    public double getAllPddEstimate() {
        return allPddEstimate;
    }

    public void setAllPddEstimate(double allPddEstimate) {
        this.allPddEstimate = allPddEstimate;
    }

    public double getAllGiftEstimate() {
        return allGiftEstimate;
    }

    public void setAllGiftEstimate(double allGiftEstimate) {
        this.allGiftEstimate = allGiftEstimate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "IncomeReport{" +
                "title='" + title + '\'' +
                ", allEstimate=" + allEstimate +
                ", allShoppingEstimate=" + allShoppingEstimate +
                ", allJdEstimate=" + allJdEstimate +
                ", allTbEstimate=" + allTbEstimate +
                ", allPddEstimate=" + allPddEstimate +
                ", allGiftEstimate=" + allGiftEstimate +
                '}';
    }
}