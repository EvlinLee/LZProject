package com.by.lizhiyoupin.app.io.entity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/15 17:13
 * Summary: 活动开关
 */
public class ActivitySwitchEntity {

    /**
     * signStatus : 1  会员签到开关 0 关 1 开
     */

    private int signStatus;//签到活动

    public int getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(int signStatus) {
        this.signStatus = signStatus;
    }
}
