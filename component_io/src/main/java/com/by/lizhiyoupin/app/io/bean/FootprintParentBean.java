package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/18 16:10
 * Summary: 足迹 bean
 */
public class FootprintParentBean {
    private String dateStr;
    private List<FootprintBean> voList;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public List<FootprintBean> getVoList() {
        return voList;
    }

    public void setVoList(List<FootprintBean> voList) {
        this.voList = voList;
    }
}
