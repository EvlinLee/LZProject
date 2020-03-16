package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2020/1/3
 * author:jyx
 * function:
 */
public class PushGetGoodsBean {
    private double commissionRate;//佣金比例
    private double commodityCoupon;//优惠券额
    private double commissionMoney;
    private String itemId;//商品id
    private String pictUrl;//商品主图
    private int volume;//月销
    private int icon;//平台标识
    private double discountsPriceAfter;//商品价格
    private String title;//商品标题
    private String url;//	商品地址，为空字符则是已下架
    private String createTime;//创建时间
    private String describe;//推送消息描述
    private Long id;//消息id
    private Long interactId;//	推送消息发送端ID
    private int readStatus;//已读状态 0未读 1已读
    private String updateTime;//修改时间
    private Long userId;//会员id

    public double getCommissionMoney() {
        return commissionMoney;
    }

    public void setCommissionMoney(double commissionMoney) {
        this.commissionMoney = commissionMoney;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public double getCommodityCoupon() {
        return commodityCoupon;
    }

    public void setCommodityCoupon(double commodityCoupon) {
        this.commodityCoupon = commodityCoupon;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPictUrl() {
        return pictUrl;
    }

    public void setPictUrl(String pictUrl) {
        this.pictUrl = pictUrl;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public double getDiscountsPriceAfter() {
        return discountsPriceAfter;
    }

    public void setDiscountsPriceAfter(double discountsPriceAfter) {
        this.discountsPriceAfter = discountsPriceAfter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInteractId() {
        return interactId;
    }

    public void setInteractId(Long interactId) {
        this.interactId = interactId;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
