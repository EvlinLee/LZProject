package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * @title: LzCommodityOrderVO
 * @projectName: lzyp
 * @author: 薛志嘉
 * @description: 我的收益记录
 * @date: 2019/11/06 11:00
 */
public class MyIncomeVO {

    /**
     * 累计收益
     */
    private double allIncome;
    /**
     * 可用余额
     */
    private double balance;
    /**
     * 已经提现金额
     */
    private double takeBalance;
    /**
     * 提现中金额
     */
    private double inTakeBalance;
    /**
     * 上月预估收益
     */
    private double lastMonthEstimate;
    /**
     * 上月结算收益
     */
    private double lastMonthActual;
    /**
     * 本月预估收益
     */
    private double nowMonthEstimate;
    /**
     * 导购(佣金)收益
     */
    private List<OrderIncomeVO> orderIncome;
    /**
     * 礼包收益
     */
    private List<GiftIncomeVO> giftIncome;

    public double getAllIncome() {
        return allIncome;
    }

    public void setAllIncome(double allIncome) {
        this.allIncome = allIncome;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getTakeBalance() {
        return takeBalance;
    }

    public void setTakeBalance(double takeBalance) {
        this.takeBalance = takeBalance;
    }

    public double getInTakeBalance() {
        return inTakeBalance;
    }

    public void setInTakeBalance(double inTakeBalance) {
        this.inTakeBalance = inTakeBalance;
    }

    public double getLastMonthEstimate() {
        return lastMonthEstimate;
    }

    public void setLastMonthEstimate(double lastMonthEstimate) {
        this.lastMonthEstimate = lastMonthEstimate;
    }

    public double getLastMonthActual() {
        return lastMonthActual;
    }

    public void setLastMonthActual(double lastMonthActual) {
        this.lastMonthActual = lastMonthActual;
    }

    public double getNowMonthEstimate() {
        return nowMonthEstimate;
    }

    public void setNowMonthEstimate(double nowMonthEstimate) {
        this.nowMonthEstimate = nowMonthEstimate;
    }

    public List<OrderIncomeVO> getOrderIncome() {
        return orderIncome;
    }

    public void setOrderIncome(List<OrderIncomeVO> orderIncome) {
        this.orderIncome = orderIncome;
    }

    public List<GiftIncomeVO> getGiftIncome() {
        return giftIncome;
    }

    public void setGiftIncome(List<GiftIncomeVO> giftIncome) {
        this.giftIncome = giftIncome;
    }

    @Override
    public String toString() {
        return "MyIncomeVO{" +
                "allIncome=" + allIncome +
                ", balance=" + balance +
                ", takeBalance=" + takeBalance +
                ", inTakeBalance=" + inTakeBalance +
                ", lastMonthEstimate=" + lastMonthEstimate +
                ", lastMonthActual=" + lastMonthActual +
                ", nowMonthEstimate=" + nowMonthEstimate +
                ", orderIncome=" + orderIncome +
                ", giftIncome=" + giftIncome +
                '}';
    }
}
