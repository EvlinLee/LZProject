package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * data:2019/11/23
 * author:jyx
 * function:
 */
public class ShopMdBean {

    private int min_id; //当前页
    private List<ShopGoodsBean> data;

    public int getMin_id() {
        return min_id;
    }

    public void setMin_id(int min_id) {
        this.min_id = min_id;
    }

    public List<ShopGoodsBean> getData() {
        return data;
    }

    public void setData(List<ShopGoodsBean> data) {
        this.data = data;
    }
    }

