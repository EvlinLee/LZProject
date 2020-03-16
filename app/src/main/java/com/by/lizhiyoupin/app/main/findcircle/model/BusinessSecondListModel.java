package com.by.lizhiyoupin.app.main.findcircle.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.findcircle.contract.BusinessContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/21 10:12
 * Summary:
 */
public class BusinessSecondListModel implements BusinessContract.BusinessSecondListModel {

    @Override
    public Observable<BaseBean<List<BusinessIconBean>>> requestBusinessSecondTabList(long superiorId) {
        return ApiService.getFindCircleApi().requestBusinessSecondTabList(superiorId);
    }
}
