package com.by.lizhiyoupin.app.io.entity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/25 19:35
 * Summary: 详情  推荐 按钮的设置
 */
public class RequestButtonRecommend {
    /**
     * commodityId : 605519097926
     * platformType : 3
     * commodityName : 2019中年男士皮衣秋冬季爸爸男装冬装外套中老年人加绒加厚皮夹克
     * commodityImg : https://img.alicdn.com/bao/uploaded/i1/4251716054/O1CN01rSmM631uamMHZO91M_!!0-item_pic.jpg
     * shopName : 巧恰旗舰店
     * realPrice : 59
     * monthSales : 28402
     * couponAmount : 500
     * commissionAmount : 21.24
     * commissionRate: 21.01佣金比例
     */

    private String commodityId;//商品id
    private int platformType;//平台类型
    private String commodityName;//商品名称
    private String commodityImg;//商品主图
    private String shopName;//店铺名称
    private double realPrice;//商品券后价
    private int monthSales;//月销量
    private double couponAmount;//优惠券金额-商品有优惠卷就传
    private double commissionAmount;//返佣金额(注意：该字段传参请使用详情接口的 basisCommissionMoney 字段
    private double commissionRate;//佣金比例

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityImg() {
        return commodityImg;
    }

    public void setCommodityImg(String commodityImg) {
        this.commodityImg = commodityImg;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public int getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(int monthSales) {
        this.monthSales = monthSales;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }
}
