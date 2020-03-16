package com.by.lizhiyoupin.app.io.entity;

import com.by.lizhiyoupin.app.io.bean.HomeBannerBean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/27 16:26
 * Summary: 商学院 banner列表
 */
public class BusinessSchoolBannerEntity {
    List<HomeBannerBean> bannerList;

    public List<HomeBannerBean> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<HomeBannerBean> bannerList) {
        this.bannerList = bannerList;
    }
}
