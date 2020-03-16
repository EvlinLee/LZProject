package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 15:40
 * Summary: 商品列表bean
 */
public class ProductListBean {
    /**
     * commissionMoney : 1.67
     * couponAmount : 3
     * discountsPriceAfter : 16.9
     * icon : 0
     * itemId : 574152505926
     * pictUrl : http://img.alicdn.com/imgextra/i2/732501769/O1CN01iFXyTB1OwFAX0hWJY_!!732501769.jpg_310x310.jpg
     * selectionType : 2
     * shopTitle : 来伊份官方旗舰店
     * title : 来伊份纯蛋糕早餐面包西式糕点心鸡蛋糕营养食品零食小吃原味230g
     * volume : 115973
     * zkFinalPrice : 19.9
     * hasRemind:false
     * remind :0
     */
    private long itemId;//商品信息-宝贝id
    private double commissionMoney; //返红包-佣金
    private int couponAmount;// 优惠券金额（元）
    private double discountsPriceAfter; //卷后价
    private int icon;//抢购图片（3 = 天猫， 0 = 淘宝）
    private String pictUrl;//商品主图
    private int selectionType;	//类型-1文章 2商品 3视频
    private String shopTitle;// 店铺信息-店铺名称
    private String title; //商品标题
    private int volume;//月销量
    private double zkFinalPrice;//原价
    private int remind;//0=已标识提醒，1=未标识提醒

    public int isRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
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

    public int getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;
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
