package com.by.lizhiyoupin.app.io.entity;

import com.by.lizhiyoupin.app.io.bean.PreciseListBean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/25 16:22
 * Summary:
 */
public class DetailRecommendationEntity {
    private List<PreciseListBean> recommend;

    public List<PreciseListBean> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<PreciseListBean> recommend) {
        this.recommend = recommend;
    }
}
