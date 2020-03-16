package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/22 16:41
 * Summary:
 */
public class DiamonKongBean {
    private long minId; //当前页
    private List<HandPickDetailBean> data;

    public long getMinId() {
        return minId;
    }

    public void setMinId(long minId) {
        this.minId = minId;
    }

    public List<HandPickDetailBean> getData() {
        return data;
    }

    public void setData(List<HandPickDetailBean> data) {
        this.data = data;
    }
}
