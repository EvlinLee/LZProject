package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/21 16:24
 * Summary: 商品详情信息
 */
public class HandPickDetailBean {
    /**
     * buyingProvince : 38.97
     * commissionMoney : 3.97
     * basisCommissionMoney: 10.9,
     * couponAmount : 35
     * couponClickUrl : https://uland.taobao.com/coupon/edetail?e=Q1GFy9kN34kGQASttHIRqYztQy486cXncx3kpildtUKwdN4LrUP%2BaCnwMMw%2BSgeR8PH5XCQx6c2hN%2F8cXdgBXFvbXe%2ByXDlpIJ1GtccYOa7CKbsAbM2TPOT3b4zuHjpz1ug731VBEQm4W4aFJRXLDPstrKIPpRBjAJtV5SE2TVp%2BgEtRE%2FPIXvMegwm8YE5no7maU72gNXGXJADIDEipPw%3D%3D&traceId=0b013ae915716373845998788e&union_lens=lensId:0b01decb_0c32_16dece355b7_46ef&xId=lgZUDdF0n4Um5BeJMsPtux2G155gEpbRgBoNmaNOJNixxOCweqVR732p8FKMSZRAPnvhgHckXvLMlroD3325ra
     * couponEndTime : 2019-10-31
     * couponStartTime : 2019-10-03
     * discountsPriceAfter : 133.02
     * icon :  0-淘宝
     * itemId : 588947510711
     * pictUrl : https://img.alicdn.com/bao/uploaded/i2/66455205/O1CN012OV5jh1oJw1k63Z3l_!!66455205.jpg
     * sellerId : 66455205
     * shopPictUrl : http://logo.taobaocdn.com/shop-logo/2c/78/TB1AftmRpXXXXb8XFXXSutbFXXX.jpg
     * shopTitle : BAOER STUDIO宝儿家女装
     * shopUrl : http://store.taobao.com/shop/view_shop.htm?user_number_id=66455205
     * smallImages : ["https://img.alicdn.com/i2/66455205/O1CN01XKu46J1oJw1k64uES_!!66455205.jpg","https://img.alicdn.com/i3/66455205/O1CN010W0R1t1oJw1iKWdyn_!!66455205.jpg","https://img.alicdn.com/i1/66455205/O1CN01S4iOTf1oJw1mYIEYQ_!!66455205.jpg","https://img.alicdn.com/i4/66455205/O1CN01Lc7BL41oJw1yjKmxg_!!66455205.jpg"]
     * title : 短外套女春秋韩版宽松2019流行新款宽松bf休闲飞行员夹克棒球服潮
     * volume : 29
     * zkFinalPrice:12.00
     * pageImages:  https://img.jpg 详情介绍图url
     * isShoppingCar: 是否加入购物车 0 未加入 1 已加入
     * shoppingCarId:15215151515151
     * itemLink:商品链接
     * isRecommend: 1, 运营商是否推荐 0 未推荐 1 已推荐
     * recommendId: 1 运营商推荐主键id
     * commissionRate: 21.10佣金比例
     */

    private double buyingProvince;//购买立省
    private double commissionMoney;//返红包+分享奖励（同一个
    private double couponAmount;//优惠卷（多少元）
    private double basisCommissionMoney;//未计算的佣金(总佣金，未根据会员等级进行计算的值
    private String couponClickUrl;//优惠卷-跳转连接
    private String couponEndTime;//优惠卷-结束时间 2020-12-15
    private String couponStartTime;//优惠卷-开始时间
    private double zkFinalPrice;//原价
    private double discountsPriceAfter;//优惠卷-卷后价
    private int icon;// 商品来源 0 淘宝 1 京东 2 拼多多  CommonConst.PLATFORM_TAO_BAO
    private long itemId;//商品信息-宝贝id
    private String pictUrl;//商品主图
    private String sellerId;//卖家id
    private String shopPictUrl;//店铺信息-店铺图标
    private String shopTitle;//店铺信息-店铺名称
    private String shopUrl;//店铺信息-店铺地址
    private String title;//商品标题
    private int volume;//月销量
    private List<String> smallImages; //商品轮播图(多张)  首张需要加上pictUrl商品主图
    private List<String> pageImages;//详情介绍图url
    private int isShoppingCar;//是否加入购物车 0 未加入 1 已加入
    private long shoppingCarId;//购物车id
    private String itemLink;//商品链接
    private int isRecommend;//运营商是否推荐 0 未推荐 1 已推荐
    private long recommendId;//运营商推荐主键id
    private double commissionRate;//佣金比例

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public long getShoppingCarId() {
        return shoppingCarId;
    }

    public void setShoppingCarId(long shoppingCarId) {
        this.shoppingCarId = shoppingCarId;
    }

    public int getIsShoppingCar() {
        return isShoppingCar;
    }

    public void setIsShoppingCar(int isShoppingCar) {
        this.isShoppingCar = isShoppingCar;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public List<String> getPageImages() {
        return pageImages;
    }

    public void setPageImages(List<String> pageImages) {
        this.pageImages = pageImages;
    }

    public double getBuyingProvince() {
        return buyingProvince;
    }

    public void setBuyingProvince(double buyingProvince) {
        this.buyingProvince = buyingProvince;
    }

    public double getCommissionMoney() {
        return commissionMoney;
    }

    public void setCommissionMoney(double commissionMoney) {
        this.commissionMoney = commissionMoney;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponClickUrl() {
        return couponClickUrl;
    }

    public void setCouponClickUrl(String couponClickUrl) {
        this.couponClickUrl = couponClickUrl;
    }

    public String getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(String couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public String getCouponStartTime() {
        return couponStartTime;
    }

    public void setCouponStartTime(String couponStartTime) {
        this.couponStartTime = couponStartTime;
    }

    public double getDiscountsPriceAfter() {
        return discountsPriceAfter;
    }

    public void setDiscountsPriceAfter(double discountsPriceAfter) {
        this.discountsPriceAfter = discountsPriceAfter;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getPictUrl() {
        return pictUrl;
    }

    public void setPictUrl(String pictUrl) {
        this.pictUrl = pictUrl;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getShopPictUrl() {
        return shopPictUrl;
    }

    public void setShopPictUrl(String shopPictUrl) {
        this.shopPictUrl = shopPictUrl;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public List<String> getSmallImages() {
        return smallImages;
    }

    public void setSmallImages(List<String> smallImages) {
        this.smallImages = smallImages;
    }

    public double getZkFinalPrice() {
        return zkFinalPrice;
    }

    public void setZkFinalPrice(double zkFinalPrice) {
        this.zkFinalPrice = zkFinalPrice;
    }

    public double getBasisCommissionMoney() {
        return basisCommissionMoney;
    }

    public void setBasisCommissionMoney(double basisCommissionMoney) {
        this.basisCommissionMoney = basisCommissionMoney;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(long recommendId) {
        this.recommendId = recommendId;
    }
}
