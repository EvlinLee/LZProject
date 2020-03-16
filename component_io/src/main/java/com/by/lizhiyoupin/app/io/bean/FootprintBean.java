package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/18 12:54
 * Summary:
 */
public class FootprintBean {

    /**
     * commodityId : 1
     * commodityName : 名称
     * commodityImg : jsd/sdjkhfsd/hsd.jpg
     * originalPrice : 1.11
     * realPrice : 1.1
     * commissionAmount : 0.01
     * platformType : 1
     * commodityValid : 1
     * dateStr : 2019-11-12
     * time : 2019-11-12 11:11:11
     */

    private long commodityId; //商品id
    private String commodityName;//商品名称
    private String commodityImg;//商品图片
    private double originalPrice;//原价
    private double realPrice;//券后价
    private double commissionAmount;//佣金,返红包
    private int platformType;//平台类型 0 淘宝 1 京东 2 拼多多
    private int commodityValid;//商品是否有效 0 无效 1 有效
    private String dateStr;//日期

    //自己添加的 用于确定是否选中
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(long commodityId) {
        this.commodityId = commodityId;
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

    public double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public int getCommodityValid() {
        return commodityValid;
    }

    public void setCommodityValid(int commodityValid) {
        this.commodityValid = commodityValid;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }



    //需要重写equals 用来判断对象相等
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FootprintBean)) return false;

        FootprintBean that = (FootprintBean) o;

        return getCommodityId() == that.getCommodityId();

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (getCommodityId() ^ (getCommodityId() >>> 32));
        result = 31 * result + (getCommodityName() != null ? getCommodityName().hashCode() : 0);
        result = 31 * result + (getCommodityImg() != null ? getCommodityImg().hashCode() : 0);
        temp = Double.doubleToLongBits(getOriginalPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getRealPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCommissionAmount());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getPlatformType();
        result = 31 * result + getCommodityValid();
        result = 31 * result + (getDateStr() != null ? getDateStr().hashCode() : 0);

        result = 31 * result + (isSelected() ? 1 : 0);
        return result;
    }
}
