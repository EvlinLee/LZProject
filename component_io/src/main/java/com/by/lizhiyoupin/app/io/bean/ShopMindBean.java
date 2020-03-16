package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * data:2019/11/23
 * author:jyx
 * function:
 */
public class ShopMindBean {

    private int min_id; //当前页
    private List<ShopKindBean> data;

    public int getMin_id() {
        return min_id;
    }

    public void setMin_id(int min_id) {
        this.min_id = min_id;
    }

    public List<ShopKindBean> getData() {
        return data;
    }

    public void setData(List<ShopKindBean> data) {
        this.data = data;
    }
    }

