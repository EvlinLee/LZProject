package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/11/22
 * author:jyx
 * function:
 */
public class UserHomeBean {

    /**
     * allFans : 3
     * allIncome : 9.0
     * allSave : 73.5
     * lastDayEstimate : 0
     * lastMonthEstimate : 0
     * nowDayEstimate : 0
     * nowMonthEstimate : 0
     */

    private int allFans;
    private double allIncome;
    private double allSave;
    private double lastDayEstimate;
    private double lastMonthEstimate;
    private double nowDayEstimate;
    private double nowMonthEstimate;
    private String bannerImg;
    private String bannerLink;

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }

    public int getAllFans() {
        return allFans;
    }

    public void setAllFans(int allFans) {
        this.allFans = allFans;
    }

    public double getAllIncome() {
        return allIncome;
    }

    public void setAllIncome(double allIncome) {
        this.allIncome = allIncome;
    }

    public double getAllSave() {
        return allSave;
    }

    public void setAllSave(double allSave) {
        this.allSave = allSave;
    }

    public double getLastDayEstimate() {
        return lastDayEstimate;
    }

    public void setLastDayEstimate(double lastDayEstimate) {
        this.lastDayEstimate = lastDayEstimate;
    }

    public double getLastMonthEstimate() {
        return lastMonthEstimate;
    }

    public void setLastMonthEstimate(double lastMonthEstimate) {
        this.lastMonthEstimate = lastMonthEstimate;
    }

    public double getNowDayEstimate() {
        return nowDayEstimate;
    }

    public void setNowDayEstimate(double nowDayEstimate) {
        this.nowDayEstimate = nowDayEstimate;
    }

    public double getNowMonthEstimate() {
        return nowMonthEstimate;
    }

    public void setNowMonthEstimate(double nowMonthEstimate) {
        this.nowMonthEstimate = nowMonthEstimate;
    }

}
