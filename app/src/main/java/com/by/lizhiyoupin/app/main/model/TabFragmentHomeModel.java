package com.by.lizhiyoupin.app.main.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.CommonCategoryBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.contract.HomeContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 13:32
 * Summary:
 */
public class TabFragmentHomeModel implements HomeContract.TabFragmentHomeModel {

    @Override
    public Observable<BaseBean<List<CommonCategoryBean>>> requestCommodityKindList() {
        return ApiService.getHomeApi().requestCommodityKindList();
    }
}
