package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 10:15
 * Summary:
 */
public class LimitedTimeSkillBean {
/*
*
*  "id":1,
            "second":2000,
            "batch":7,
            "sessionTime":"10:00",
* */

private long id;
private int second;
private int batch;
private String sessionTime;
private int buyStatus;
private List<LimitSkillTimeBean> commodityList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public int getBuyStatus() {
        return buyStatus;
    }

    public void setBuyStatus(int buyStatus) {
        this.buyStatus = buyStatus;
    }

    public List<LimitSkillTimeBean> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<LimitSkillTimeBean> commodityList) {
        this.commodityList = commodityList;
    }
}
