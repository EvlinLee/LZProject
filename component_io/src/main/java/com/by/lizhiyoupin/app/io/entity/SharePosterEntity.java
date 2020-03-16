package com.by.lizhiyoupin.app.io.entity;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/25 11:04
 * Summary: 获取海报
 */
public class SharePosterEntity {
    private List<String> commodityImgList;//生成的二维码图片集合
    private int invalidCommodityCount;//失效商品数量

    public List<String> getCommodityImgList() {
        return commodityImgList;
    }

    public void setCommodityImgList(List<String> commodityImgList) {
        this.commodityImgList = commodityImgList;
    }

    public int getInvalidCommodityCount() {
        return invalidCommodityCount;
    }

    public void setInvalidCommodityCount(int invalidCommodityCount) {
        this.invalidCommodityCount = invalidCommodityCount;
    }
}
