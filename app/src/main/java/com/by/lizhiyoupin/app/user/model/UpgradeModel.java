package com.by.lizhiyoupin.app.user.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ChildcountBean;
import com.by.lizhiyoupin.app.io.bean.PlusTimeBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.user.contract.UpgradeContract;

import io.reactivex.Observable;

/**
 * data:2019/11/19
 * author:jyx
 * function:
 */
public class UpgradeModel implements UpgradeContract.UpgradeModel{

    @Override
    public Observable<BaseBean<ChildcountBean>> requestUpgradeView(int type) {
        return ApiService.getVipApi().requestGetUpgrade(type);
    }


    @Override
    public Observable<BaseBean<String>> requestPlusView() {
        return ApiService.getVipApi().requestGetPlus();
    }

    @Override
    public Observable<BaseBean<String>> requestOperatorView() {
        return ApiService.getVipApi().requestGetOperator();
    }
    @Override
    public Observable<BaseBean<PlusTimeBean>> requestOperatorTimeView() {
        return ApiService.getVipApi().requestGetOperatorTime();
    }

    @Override
    public Observable<BaseBean<PlusTimeBean>> requestPlusTimeView() {
        return ApiService.getVipApi().requestGetPlusTime();
    }

}
