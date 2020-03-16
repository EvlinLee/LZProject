package com.by.lizhiyoupin.app.io.entity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/18 14:19
 * Summary: 购物车请求
 */
public class RequestShoppingCartEntity {
    private  long id;//购物车id
    private  Long userId;//会员id
    private  Long commodityId;//商品id
    private  String commodityName;//商品名称
    private  String shopName;//店铺名称 ,否
    private  int monthSales;//商品月销量
    private  double originalPrice;//商品原价
    private  double realPrice;//商品券后价
    private  String commodityLink;//商品链接
    private  String commodityImg;//商品图片
    private  String commoditySource;//商品来源 0 淘宝 1 京东 2 拼多多
    private  double commissionAmount;//佣金
    private  double couponAmount;//优惠券金额 ,否
    private  String couponLink;//优惠券链接 ,否

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(int monthSales) {
        this.monthSales = monthSales;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public String getCommodityLink() {
        return commodityLink;
    }

    public void setCommodityLink(String commodityLink) {
        this.commodityLink = commodityLink;
    }

    public String getCommodityImg() {
        return commodityImg;
    }

    public void setCommodityImg(String commodityImg) {
        this.commodityImg = commodityImg;
    }

    public String getCommoditySource() {
        return commoditySource;
    }

    public void setCommoditySource(String commoditySource) {
        this.commoditySource = commoditySource;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponLink() {
        return couponLink;
    }

    public void setCouponLink(String couponLink) {
        this.couponLink = couponLink;
    }

    public double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }
}
