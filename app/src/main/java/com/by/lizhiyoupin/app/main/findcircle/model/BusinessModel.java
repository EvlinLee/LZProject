package com.by.lizhiyoupin.app.main.findcircle.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;
import com.by.lizhiyoupin.app.io.bean.PreciseBannerIconBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.findcircle.contract.BusinessContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 17:30
 * Summary:
 */
public class BusinessModel implements BusinessContract.BusinessModel {

    @Override
    public Observable<BaseBean<List<BusinessArticleBean>>> requestBusinessScrollArticleList(int start, int limit) {
        return ApiService.getFindCircleApi().requestBusinessScrollArticleList(start,limit);
    }

    @Override
    public Observable<BaseBean<List<PreciseBannerIconBean>>> requestBusinessBannerList() {
        return ApiService.getHomeApi().requestBusinessBannerList();
    }

    @Override
    public Observable<BaseBean<List<BusinessIconBean>>> requestBusinessIconList() {
        return ApiService.getFindCircleApi().requestBusinessIconList();
    }

    @Override
    public Observable<BaseBean<List<BusinessArticleBean>>> requestBusinessGuessList(int start, int limit) {
        return ApiService.getFindCircleApi().requestBusinessGuessList(start, limit);
    }
}
