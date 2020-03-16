package com.by.lizhiyoupin.app.io.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/17 13:33
 * Summary:
 */
public class PreciseListBean implements Parcelable {

    /**
     * commissionMoney : 3.28
     * couponAmount : 5
     * discountsPriceAfter : 20.01
     * icon :
     * itemId : 566553706052
     * pictUrl : //gw.alicdn.com/bao/uploaded/i4/52108144/TB2Y.obbwKTBuNkSne1XXaJoXXa_!!52108144.jpg
     * shopTitle : 薇薇自制女包每日定时上新
     * title : 少女心ins零钱包女迷你可爱韩国女2018新款长款小清新女士钱夹包
     * volume : 80
     * zkFinalPrice : 25
     * commissionRate: 12152.15
     */
    private long itemId;//宝贝id
    private double commissionMoney;//佣金，返红包
    private double couponAmount;//优惠券金额
    private double discountsPriceAfter;//券后价
    private int icon;//淘宝 京东 拼多多icon
    private String pictUrl;//商品主图
    private String shopTitle;//商店名
    private String title;//标题
    private double zkFinalPrice;//原价
    private int volume;//月销量
    private double commissionRate;//佣金比例

    private int selectionType;//类型-1文章 2商品 3视频(根据该字段判断列表显示什么)
    private String vedioUrl;//视频地址
    private String url;//链接地址
    // TODO: 2020/1/3  注意
    //注意添加字段，需要重新实现 Parcelable


    protected PreciseListBean(Parcel in) {
        itemId = in.readLong();
        commissionMoney = in.readDouble();
        couponAmount = in.readDouble();
        discountsPriceAfter = in.readDouble();
        icon = in.readInt();
        pictUrl = in.readString();
        shopTitle = in.readString();
        title = in.readString();
        zkFinalPrice = in.readDouble();
        volume = in.readInt();
        commissionRate = in.readDouble();
        selectionType = in.readInt();
        vedioUrl = in.readString();
        url = in.readString();
    }

    public static final Creator<PreciseListBean> CREATOR = new Creator<PreciseListBean>() {
        @Override
        public PreciseListBean createFromParcel(Parcel in) {
            return new PreciseListBean(in);
        }

        @Override
        public PreciseListBean[] newArray(int size) {
            return new PreciseListBean[size];
        }
    };

    public double getCommissionMoney() {
        return commissionMoney;
    }

    public void setCommissionMoney(double commissionMoney) {
        this.commissionMoney = commissionMoney;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getDiscountsPriceAfter() {
        return discountsPriceAfter;
    }

    public void setDiscountsPriceAfter(int discountsPriceAfter) {
        this.discountsPriceAfter = discountsPriceAfter;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
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

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public void setDiscountsPriceAfter(double discountsPriceAfter) {
        this.discountsPriceAfter = discountsPriceAfter;
    }

    public int getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;
    }

    public String getVedioUrl() {
        return vedioUrl;
    }

    public void setVedioUrl(String vedioUrl) {
        this.vedioUrl = vedioUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(itemId);
        dest.writeDouble(commissionMoney);
        dest.writeDouble(couponAmount);
        dest.writeDouble(discountsPriceAfter);
        dest.writeInt(icon);
        dest.writeString(pictUrl);
        dest.writeString(shopTitle);
        dest.writeString(title);
        dest.writeDouble(zkFinalPrice);
        dest.writeInt(volume);
        dest.writeDouble(commissionRate);
        dest.writeInt(selectionType);
        dest.writeString(vedioUrl);
        dest.writeString(url);
    }
}
