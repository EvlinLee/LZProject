package com.by.lizhiyoupin.app.io.entity;

import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/27 16:26
 * Summary: 商学院 icon列表
 */
public class BusinessSchoolIconEntity {
    private List<BusinessIconBean> iconBeanList;

    public List<BusinessIconBean> getIconBeanList() {
        return iconBeanList;
    }

    public void setIconBeanList(List<BusinessIconBean> iconList) {
        this.iconBeanList = iconList;
    }
}
