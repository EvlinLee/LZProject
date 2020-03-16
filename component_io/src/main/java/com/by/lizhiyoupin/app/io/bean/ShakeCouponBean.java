package com.by.lizhiyoupin.app.io.bean;

import java.io.Serializable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 20:18
 * Summary:抖券
 */
public class ShakeCouponBean implements Serializable {
    private static final long serialVersionUID = -706210544600464483L;
    /**
     * commissionMoney : 14.75
     * couponAmount : 50
     * discountsPriceAfter : 149.0
     * dyFirstFrame : https://p9-dy.byteimg.com/img/tos-cn-p-0015/f62d1b8cfa24424d99a7c39f4b06b5a1~c5_300x400.jpeg
     * dyGoodsId : 445373
     * dyShareCount : 4
     * dyThumbsCount : 14
     * dyTitle : 三个动作 每个20秒 5组。#健身打卡 #健身 #有氧#运动
     * dyVideoUrl : http://video.haodanku.com/10100d164e36ef2430466a39a1d37fbc.mp4?attname=1573647569.mp4
     * icon : 0
     * itemId : 576034670770
     * pictUrl : https://img.alicdn.com/imgextra/i1/4099407085/O1CN013Q6TtU22Cyg2vnPHy_!!4099407085.jpg_310x310.jpg
     * shopTitle : 动动嗒嗒旗舰店
     * title : 大胸运动减震文胸 动动嗒嗒大胸运动内衣女定型跑步健身大码背心
     * vedioUrl : http://video.haodanku.com/10100d164e36ef2430466a39a1d37fbc.mp4?attname=1573647569.mp4
     * volume : 298
     * zkFinalPrice : 199.0
     */

    private double commissionMoney;//返红包-佣金
    private int couponAmount;//优惠券（元）
    private double discountsPriceAfter;//卷后价
    private String dyFirstFrame;//抖音视频第一帧图片
    private String dyGoodsId;//抖音商品ID
    private int dyShareCount;//抖音视频分享量
    private int dyThumbsCount;//抖音视频点赞数
    private String dyTitle;//抖音标题
    private String dyVideoUrl;//抖音视频地址
    private int icon;//图标（來源类型）
    private long itemId;//商品信息-宝贝id
    private String pictUrl;//商品主图
    private String shopTitle;//店铺信息-店铺名称
    private String title;//商品标题
    private String vedioUrl;//视频地址
    private int volume;//月销量
    private double zkFinalPrice;//原价

    public double getCommissionMoney() {
        return commissionMoney;
    }

    public void setCommissionMoney(double commissionMoney) {
        this.commissionMoney = commissionMoney;
    }

    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getDiscountsPriceAfter() {
        return discountsPriceAfter;
    }

    public void setDiscountsPriceAfter(double discountsPriceAfter) {
        this.discountsPriceAfter = discountsPriceAfter;
    }

    public String getDyFirstFrame() {
        return dyFirstFrame;
    }

    public void setDyFirstFrame(String dyFirstFrame) {
        this.dyFirstFrame = dyFirstFrame;
    }

    public String getDyGoodsId() {
        return dyGoodsId;
    }

    public void setDyGoodsId(String dyGoodsId) {
        this.dyGoodsId = dyGoodsId;
    }

    public int getDyShareCount() {
        return dyShareCount;
    }

    public void setDyShareCount(int dyShareCount) {
        this.dyShareCount = dyShareCount;
    }

    public int getDyThumbsCount() {
        return dyThumbsCount;
    }

    public void setDyThumbsCount(int dyThumbsCount) {
        this.dyThumbsCount = dyThumbsCount;
    }

    public String getDyTitle() {
        return dyTitle;
    }

    public void setDyTitle(String dyTitle) {
        this.dyTitle = dyTitle;
    }

    public String getDyVideoUrl() {
        return dyVideoUrl;
    }

    public void setDyVideoUrl(String dyVideoUrl) {
        this.dyVideoUrl = dyVideoUrl;
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

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVedioUrl() {
        return vedioUrl;
    }

    public void setVedioUrl(String vedioUrl) {
        this.vedioUrl = vedioUrl;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getZkFinalPrice() {
        return zkFinalPrice;
    }

    public void setZkFinalPrice(double zkFinalPrice) {
        this.zkFinalPrice = zkFinalPrice;
    }
}
