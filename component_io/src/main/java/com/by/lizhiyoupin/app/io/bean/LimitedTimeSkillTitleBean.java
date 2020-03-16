package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 10:15
 * Summary:
 */
public class LimitedTimeSkillTitleBean {

    /**
     * sessionTime : 12:00
     * batch : 8
     * buyStatus : 1
     */

    private String sessionTime;//时间
    private int batch;//类型
    private int buyStatus; //状态

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public int getBuyStatus() {
        return buyStatus;
    }

    public void setBuyStatus(int buyStatus) {
        this.buyStatus = buyStatus;
    }
}
