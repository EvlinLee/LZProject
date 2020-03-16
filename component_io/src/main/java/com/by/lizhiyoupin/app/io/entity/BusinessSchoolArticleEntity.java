package com.by.lizhiyoupin.app.io.entity;

import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/27 16:26
 * Summary: 商学院底部文章列表
 */
public class BusinessSchoolArticleEntity {
    private List<BusinessArticleBean> bottomArticleList;

    public List<BusinessArticleBean> getBottomArticleList() {
        return bottomArticleList;
    }

    public void setBottomArticleList(List<BusinessArticleBean> bottomArticleList) {
        this.bottomArticleList = bottomArticleList;
    }
}
