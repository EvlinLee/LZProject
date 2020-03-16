package com.by.lizhiyoupin.app.detail.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodsBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.detail.contract.VipProductDetailContract;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 14:13
 * Summary:
 */
public class VipProductDetailModel implements VipProductDetailContract.VipProductDetailModel {
    @Override
    public Observable<BaseBean<VipGoodsBean>> requestVipGoodsDetail(long id,int activityType) {
        return ApiService.getHomeApi().requestVipGoodsDetail(id,activityType);
    }
}
