package com.by.lizhiyoupin.app.io.entity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/13 11:28
 * Summary:
 */
public class RequestPosterBean {
    String goodsId;
    int platformType;

    public RequestPosterBean(String goodsId, int platformType) {
        this.goodsId = goodsId;
        this.platformType = platformType;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }
}
