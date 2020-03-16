package com.by.lizhiyoupin.app.io.entity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/18 16:16
 * Summary: 足迹删除
 */
public class RequestFootprintEntity {
    private Long commodityId;// 商品id
    private int platformType;// 平台类型 0 淘宝 1 京东 2 拼多多
    private String dateStr; //日期 "2019-11-12"

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}
