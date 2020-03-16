package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 10:15
 * Summary:
 */
public class LimitedTimeTitleBean {

    /**
     * rushToBuyDay : 12:00
     * hour_type : 8
     * rushToBuyType : 已开抢
     */

    private String rushToBuyDay;//时间
    private int hour_type;//类型
    private String rushToBuyType; //描述

    public String getRushToBuyDay() {
        return rushToBuyDay;
    }

    public void setRushToBuyDay(String rushToBuyDay) {
        this.rushToBuyDay = rushToBuyDay;
    }

    public int getHour_type() {
        return hour_type;
    }

    public void setHour_type(int hour_type) {
        this.hour_type = hour_type;
    }

    public String getRushToBuyType() {
        return rushToBuyType;
    }

    public void setRushToBuyType(String rushToBuyType) {
        this.rushToBuyType = rushToBuyType;
    }
}
