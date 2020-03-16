package com.by.lizhiyoupin.app.user.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.user.contract.OrderRetrieveContract;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 10:04
 * Summary:
 */
public class OrderRetrieveModel implements OrderRetrieveContract.OrderRetrieveModel {
    @Override
    public Observable<BaseBean<Object>> requestOrderRetrieve(String orderId) {
        return ApiService.getOrderApi().requestOrderFindBack(orderId);
    }
}
