package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/11 14:44
 * Summary:
 */
public class FansCountBean {
    /**
     * allFansCount : 15
     * commonlyAllFansCount : 9
     * directFansCount : 6
     */

    private int allFansCount;//总粉丝数
    private int commonlyAllFansCount;//普通粉丝
    private int directFansCount;//直属粉丝

    public int getAllFansCount() {
        return allFansCount;
    }

    public void setAllFansCount(int allFansCount) {
        this.allFansCount = allFansCount;
    }

    public int getCommonlyAllFansCount() {
        return commonlyAllFansCount;
    }

    public void setCommonlyAllFansCount(int commonlyAllFansCount) {
        this.commonlyAllFansCount = commonlyAllFansCount;
    }

    public int getDirectFansCount() {
        return directFansCount;
    }

    public void setDirectFansCount(int directFansCount) {
        this.directFansCount = directFansCount;
    }
}
