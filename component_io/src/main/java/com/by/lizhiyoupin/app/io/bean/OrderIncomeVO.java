package com.by.lizhiyoupin.app.io.bean;

/**
 * @title: LzCommodityOrderVO
 * @projectName: lzyp
 * @author: 薛志嘉
 * @description: 导购（佣金）订单收益
 * @date: 2019/11/06 11:00
 */
public class OrderIncomeVO   {

    /**
     * 查询类型 1日 2本周 3本月
     */
    private int type;
    /**
     * 总付款笔数
     */
    private int allPaymentNumber;
    /**
     * 总预估收益
     */
    private double allEstimate;
    /**
     * 粉丝付款笔数
     */
    private int fansPaymentNumber;
    /**
     * 粉丝预估收益
     */
    private double fansEstimate;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAllPaymentNumber() {
        return allPaymentNumber;
    }

    public void setAllPaymentNumber(int allPaymentNumber) {
        this.allPaymentNumber = allPaymentNumber;
    }

    public double getAllEstimate() {
        return allEstimate;
    }

    public void setAllEstimate(double allEstimate) {
        this.allEstimate = allEstimate;
    }

    public int getFansPaymentNumber() {
        return fansPaymentNumber;
    }

    public void setFansPaymentNumber(int fansPaymentNumber) {
        this.fansPaymentNumber = fansPaymentNumber;
    }

    public double getFansEstimate() {
        return fansEstimate;
    }

    public void setFansEstimate(double fansEstimate) {
        this.fansEstimate = fansEstimate;
    }

    @Override
    public String toString() {
        return "CommissionProfitVO{" +
                "type=" + type +
                ", allPaymentNumber=" + allPaymentNumber +
                ", allEstimate=" + allEstimate +
                ", fansPaymentNumber=" + fansPaymentNumber +
                ", fansEstimate=" + fansEstimate +
                '}';
    }
}
