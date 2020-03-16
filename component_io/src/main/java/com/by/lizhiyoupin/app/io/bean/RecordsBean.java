package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 20:25
 * Summary: 本地服务器返回数据
 */
public class RecordsBean {

    /**
     * id : 1
     * name : 商品名称
     * itemLink : https://lzyp-static.oss-cn-hangzhou.aliyuncs.com/image/default_head_img.png
     * descContext :
     * platformType : 1
     * mainImg : https://lzyp-static.oss-cn-hangzhou.aliyuncs.com/image/default_head_img.png
     * faddish : 0
     * commodityKindId : 1
     * originalPrice : 11.11
     * couponIf : 是否优惠券商品 0 不是 1 是
     * couponId : 1
     * couponAmount:60.0
     * commissionType : 1
     * commissionRate : 0.1
     * monthSales : 1
     * twoHoursSales : 1
     * daySales : 1
     * brandIf : 1
     * brandId : 1
     * createTime : 2019-10-16 11:11:11
     * tMallIf : 1
     * activityType : 1
     * activityStartTime : 2019-10-16 11:11:11
     * activityEndTime : 2019-10-16 11:11:11
     * shopType : 1
     * sellerId : 1
     * shopName :
     * shopId : 1
     * compositeCount : 80
     * realPrice : 80.55
     * commissionAmount : 80.22
     * icon:
     */

    private long id;//native商品id
    private long commodityId;//三方商品id
    private String name;//商品名称
    private String itemLink;//商品链接
    private int icon; //0淘宝 3天猫(本地库使用，不要用platformType)
    private String descContext;//推广文案
    private int platformType;//平台类型 0 淘宝 1 京东 2 拼多多
    private String mainImg;//图片地址
    private int faddish;//是否爆款 0 不是 1 是
    private long commodityKindId;//一級类目id
    private double originalPrice;//商品原价
    private double couponAmount;//优惠券金额
    private int couponIf;//是否优惠券商品 0 不是 1 是
    private long couponId;//优惠券id
    private int commissionType;//佣金类型
    private double commissionRate;//佣金比例
    private long monthSales;//月销量
    private long twoHoursSales;//2小时销量
    private long daySales;//日销量
    private int brandIf;//是否品牌商品 0 不是 1 是
    private long brandId;//品牌id
    private String createTime;//创建时间
    private int tMallIf;//是否天猫超市商品 0 不是 1 是
    private int activityType;//活动类型
    private String activityStartTime;//活动开始时间
    private String activityEndTime;//活动结束时间
    private int shopType;//店铺类型
    private long sellerId;//卖家id
    private String shopName;//店铺名称
    private long shopId;//店铺id
    private int compositeCount;//综合得分
    private double realPrice;//券后价
    private double commissionAmount;//佣金金额

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(long commodityId) {
        this.commodityId = commodityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public String getDescContext() {
        return descContext;
    }

    public void setDescContext(String descContext) {
        this.descContext = descContext;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public String getMainImg() {
        return mainImg;
    }
    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public int getFaddish() {
        return faddish;
    }

    public void setFaddish(int faddish) {
        this.faddish = faddish;
    }

    public long getCommodityKindId() {
        return commodityKindId;
    }

    public void setCommodityKindId(long commodityKindId) {
        this.commodityKindId = commodityKindId;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getCouponIf() {
        return couponIf;
    }

    public void setCouponIf(int couponIf) {
        this.couponIf = couponIf;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public int getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(int commissionType) {
        this.commissionType = commissionType;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public long getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(long monthSales) {
        this.monthSales = monthSales;
    }

    public long getTwoHoursSales() {
        return twoHoursSales;
    }

    public void setTwoHoursSales(long twoHoursSales) {
        this.twoHoursSales = twoHoursSales;
    }

    public long getDaySales() {
        return daySales;
    }

    public void setDaySales(long daySales) {
        this.daySales = daySales;
    }

    public int getBrandIf() {
        return brandIf;
    }

    public void setBrandIf(int brandIf) {
        this.brandIf = brandIf;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int gettMallIf() {
        return tMallIf;
    }

    public void settMallIf(int tMallIf) {
        this.tMallIf = tMallIf;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public int getCompositeCount() {
        return compositeCount;
    }

    public void setCompositeCount(int compositeCount) {
        this.compositeCount = compositeCount;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }
}
