package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/11/23
 * author:jyx
 * function:
 */
public class ShopGoodsBean {

    /**
     * commissionMoney : 8.28
     * couponAmount : 10
     * discountsPriceAfter : 230.0
     * icon : 0
     * pictUrl : https://img.alicdn.com/bao/uploaded/i1/3157354417/O1CN01VelAaJ1iV1zfZZi2J_
     * !!0-item_pic.jpg
     * title : 城野医生冻干粉修复精华液女 寡肽原液祛痘印淡化痘疤熬夜救星男
     * volume : 582
     */

    private double commissionMoney;
    private int couponAmount;
    private double discountsPriceAfter;
    private int icon;
    private String pictUrl;
    private String title;
    private int volume;
    private Long itemId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getPictUrl() {
        return pictUrl;
    }

    public void setPictUrl(String pictUrl) {
        this.pictUrl = pictUrl;
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

}
