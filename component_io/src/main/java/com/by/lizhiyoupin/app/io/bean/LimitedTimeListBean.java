package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 11:59
 * Summary: 秒杀列表
 */
public class LimitedTimeListBean {
    private long times;//距离下一场秒杀时间 秒
    private long showTimes=-1;//抢购状态 -1 = 已抢购 0 = 正在抢购 1 = 即将开始抢购
    private List<ProductListBean> lists;

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public List<ProductListBean> getLists() {
        return lists;
    }

    public void setLists(List<ProductListBean> lists) {
        this.lists = lists;
    }

    public long getShowTimes() {
        return showTimes;
    }

    public void setShowTimes(long showTimes) {
        this.showTimes = showTimes;
    }
}
