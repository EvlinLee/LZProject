package com.by.lizhiyoupin.app.main.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.CommonSecondBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.contract.HomeCommonContract;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 17:23
 * Summary:
 */
public class HomeCommonModel implements HomeCommonContract.HomeCommonModel {
    @Override
    public Observable<BaseBean<CommonSecondBean>> requestSecondBannerIcon(Long id) {
        return ApiService.getHomeApi().requestSecondLevel(id);
    }

}
