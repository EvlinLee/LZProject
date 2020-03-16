package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 15:40
 * Summary: 商品列表bean
 */
public class ProduclimitSkilltListBean {
           /* "buyPercent":10,
           "commissionAmount":1.11,
           "commissionRate":11.11,
           "commodityId":"12345678",
           "commodityImg":"http://122.224.55.208:8220/web/#/page/edit/2/0",
           "commodityName":"阿贾克斯的发挥空间闪电发货SDK剧本",
           "commodityStatus":3,
           "couponAmount":1.11,
           "fastBuyCommodityType":1,
           "minId":"2",
           "platformType":0,
           "realPrice":12.12,*/

    private String commodityId;//商品id
    private int buyPercent;//百分比
    private double commissionAmount; //返红包-佣金
    private double couponAmount;// 优惠券金额（元）
    private double realPrice; //卷后价
    private int fastBuyCommodityType;//是否本地商品 0 三方商品 1 本地商品
    private String commodityImg;//商品主图
    private String commodityName; //商品标题
    private int commodityStatus;//商品购买状态 1 即将开始 2 商品已抢完 3 商品正在快抢中
    private double commissionRate;//佣金比例
    private String minId;//下一页页码
    private int platformType;//平台类型
    private int remindStatus;//提醒状态 0 未提醒 1 已提醒
    private int second;//	距离抢购时间差（秒）
    private double priceDiscount;//几折
    private double originalAmount;//原价
    private int residueCount;//商品剩余数量

    public double getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(double priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public int getResidueCount() {
        return residueCount;
    }

    public void setResidueCount(int residueCount) {
        this.residueCount = residueCount;
    }

    public int getRemindStatus() {
        return remindStatus;
    }

    public void setRemindStatus(int remindStatus) {
        this.remindStatus = remindStatus;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public int getBuyPercent() {
        return buyPercent;
    }

    public void setBuyPercent(int buyPercent) {
        this.buyPercent = buyPercent;
    }

    public double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public int getFastBuyCommodityType() {
        return fastBuyCommodityType;
    }

    public void setFastBuyCommodityType(int fastBuyCommodityType) {
        this.fastBuyCommodityType = fastBuyCommodityType;
    }

    public String getCommodityImg() {
        return commodityImg;
    }

    public void setCommodityImg(String commodityImg) {
        this.commodityImg = commodityImg;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getCommodityStatus() {
        return commodityStatus;
    }

    public void setCommodityStatus(int commodityStatus) {
        this.commodityStatus = commodityStatus;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getMinId() {
        return minId;
    }

    public void setMinId(String minId) {
        this.minId = minId;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }
}
