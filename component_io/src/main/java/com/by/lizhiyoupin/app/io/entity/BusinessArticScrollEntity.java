package com.by.lizhiyoupin.app.io.entity;

import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/27 13:08
 * Summary:
 */
public class BusinessArticScrollEntity {
    public List<BusinessArticleBean> scrollArticleList;



    public List<BusinessArticleBean> getScrollArticleList() {
        return scrollArticleList;
    }

    public void setScrollArticleList(List<BusinessArticleBean> scrollArticleList) {
        this.scrollArticleList = scrollArticleList;
    }
}
