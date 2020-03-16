package com.by.lizhiyoupin.app.io.entity;

import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;

import java.util.ArrayList;
import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/27 11:00
 * Summary:
 */
public class BusinessSchoolMultipleEntity {


    // icon列表
    private BusinessSchoolIconEntity iconEntity;
    //滚动文章列表
    private BusinessArticScrollEntity mArticleScrollEntity;
    //底部推荐文章列表
    private List<BusinessArticleBean> bottomArticleList;

    public BusinessSchoolMultipleEntity() {
        iconEntity=new BusinessSchoolIconEntity();
        mArticleScrollEntity=new BusinessArticScrollEntity();
        bottomArticleList=new ArrayList<>();
    }


    public BusinessSchoolIconEntity getIconEntity() {
        return iconEntity;
    }
    public BusinessArticScrollEntity getArticleScrollEntity() {
        return mArticleScrollEntity;
    }



    private List<Object> arrays = new ArrayList<>();
    public int getAllListSize(){
        return arrays.size();
    }
    /**
     * 整体刷新
     * @return
     */
    public synchronized List<Object> toList() {
        arrays.clear();
        arrays.add(0,iconEntity);
        arrays.add(1,mArticleScrollEntity);
        arrays.addAll(bottomArticleList);
        return arrays;
    }

    public List<Object> updateIcon(List<BusinessIconBean> iconBeanList){
        iconEntity.setIconBeanList(iconBeanList);
        return  arrays;
    }
    public List<Object> updateScroll(List<BusinessArticleBean> scrollList){
        mArticleScrollEntity.setScrollArticleList(scrollList);
        return  arrays;
    }

    /**
     * 加载更多 加载底部数据调用
     * @param bottomList
     * @return
     */
    public List<Object> updateBottomArticle(List<BusinessArticleBean>  bottomList){
        if (bottomArticleList.size()==0){
            if (!ArraysUtils.isListEmpty(bottomList)){
                bottomArticleList=bottomList;
            }
            arrays.addAll(bottomList);
        }else {
            bottomArticleList.addAll(bottomList);
            arrays.addAll(bottomList);
        }
        return  arrays;
    }

    /**
     * 首次或重新刷新底部数据 调用
     * @param bottomList
     * @return
     */
    public List<Object> clearBottomAndupdate(List<BusinessArticleBean>  bottomList){
        bottomArticleList.clear();
        bottomArticleList.addAll(bottomList);
        return toList();
    }

}
