package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/15 16:53
 * Summary:
 */
public class ShakeCouponMainTabBean {
    private String title;
    private int type;

    public ShakeCouponMainTabBean() {
    }

    public ShakeCouponMainTabBean(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
