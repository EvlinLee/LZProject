package com.by.lizhiyoupin.app.user.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FansCountBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.user.contract.FansContrat;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/9 15:28
 * Summary:
 */
public class FansMainModel implements FansContrat.FansMainModel {
    @Override
    public Observable<BaseBean<FansCountBean>> requestFansMain() {
        return ApiService.getNewsApi().requestGetFansCountInfo();
    }
}
