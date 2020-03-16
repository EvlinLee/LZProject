package com.by.lizhiyoupin.app.io.bean;

/**
 * @title: LzCommodityOrderVO
 * @projectName: lzyp
 * @author: 薛志嘉
 * @description: 收益明细/订单详情
 * @date: 2019/11/06 11:00
 */
public class IncomeDetailsVO  {

    /**
     * 收益类型 1礼包 2订单
     */
    private int profitType;
    /**
     * 平台类型 0 全部 1淘宝 2京东 3拼多多
     */
    private int platformType;
    /**
     * 粉丝类型 0自己 1专属 2普通
     */
    private int fansType;
    /**
     * 订单状态 1为结算 2已结算
     */
    private int status;
    /**
     * 图片
     */
    private String imgages;
    /**
     * 标题
     */
    private String title;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 实付金额
     */
    private double payment;
    /**
     * 收益（状态为未结算就是预估，为已结算就是实际）
     */
    private double profit;

    public int getProfitType() {
        return profitType;
    }

    public void setProfitType(int profitType) {
        this.profitType = profitType;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public int getFansType() {
        return fansType;
    }

    public void setFansType(int fansType) {
        this.fansType = fansType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImgages() {
        return imgages;
    }

    public void setImgages(String imgages) {
        this.imgages = imgages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "CommissionDetailsVO{" +
                "profitType=" + profitType +
                ", platformType=" + platformType +
                ", fansType=" + fansType +
                ", status=" + status +
                ", imgages='" + imgages + '\'' +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", orderId='" + orderId + '\'' +
                ", payment=" + payment +
                ", profit=" + profit +
                '}';
    }
}
