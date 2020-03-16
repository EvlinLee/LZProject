package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/21 09:38
 * Summary: 搜索三方服务器 商品列表
 */
public class SearchCommodityBean {

    /**
     * commissionMoney : 9.93
     * couponAmount : 10
     * discountsPriceAfter : 29.8
     * itemId : 583808345306
     * pictUrl : //gw.alicdn.com/bao/uploaded/i4/3252696262/O1CN01HURIZV1w82eDxlucp_!!3252696262.jpg
     * shopTitle : HOT KISS 店主风实拍原创定制
     * title : 休闲斜挎小包包女2019新款潮韩版撞色宽肩带小方包时尚尼龙单肩包
     * volume : 50
     * zkFinalPrice : 39.8
     * icon:
     */

    private double commissionMoney; //返红包-佣金
    private int couponAmount; //优惠券（元）
    private double discountsPriceAfter;//卷后价
    private long itemId;//宝贝id
    private String pictUrl;//商品主图
    private String shopTitle;//店铺信息-店铺名称
    private String title;//商品标题
    private int icon;//icon图
    private int volume;//月销量
    private double zkFinalPrice; //原价

    public double getCommissionMoney() {
        return commissionMoney;
    }

    public void setCommissionMoney(double commissionMoney) {
        this.commissionMoney = commissionMoney;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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
