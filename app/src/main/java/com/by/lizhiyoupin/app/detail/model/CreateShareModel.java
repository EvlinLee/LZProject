package com.by.lizhiyoupin.app.detail.model;

import com.by.lizhiyoupin.app.detail.contract.CreateShareContract;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.entity.CreateShareEntity;
import com.by.lizhiyoupin.app.io.service.ApiService;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 19:54
 * Summary:
 */
public class CreateShareModel implements CreateShareContract.CreateShareModel {
    @Override
    public Observable<BaseBean<CreateShareEntity>> requestShareDetail(Long commodityId, int type, int platformType,
                                                                      String title,
                                                                      String zkFinalPrice,
                                                                      String discountsPriceAfter,
                                                                      String volume,
                                                                      String couponAmount,
                                                                      String pictUrl) {

        return ApiService.getShareApi().requestShareDetail(commodityId, type, platformType,
                title, zkFinalPrice, discountsPriceAfter, volume, couponAmount, pictUrl);
    }

}
