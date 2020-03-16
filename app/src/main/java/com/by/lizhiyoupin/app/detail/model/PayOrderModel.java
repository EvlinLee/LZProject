package com.by.lizhiyoupin.app.detail.model;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.detail.contract.PayOrderContract;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.AddressBean;
import com.by.lizhiyoupin.app.io.bean.WeiXinPayVO;
import com.by.lizhiyoupin.app.io.entity.RequestAddressEntity;
import com.by.lizhiyoupin.app.io.service.ApiService;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/25 09:56
 * Summary:
 */
public class PayOrderModel implements PayOrderContract.PayOrderModel {


    @Override
    public Observable<BaseBean<List<AddressBean>>> requestGetUserAddress() {
        String apiToken = LiZhiApplication.getApplication().getAccountManager().getAccountInfo().getApiToken();
        return ApiService.getVipApi().requestGetUserAddress(apiToken);
    }

    @Override
    public Observable<BaseBean<AddressBean>> requestUpdateUserAddress(RequestAddressEntity entity) {

        return ApiService.getVipApi().requestUpdateUserAddress(entity);
    }

    @Override
    public Observable<BaseBean<String>> requestPaymentOrder(Long gid,int type,Long userAddressId) {
        return ApiService.getOrderApi().requestPaymentOrder(gid,type,userAddressId);
    }

    @Override
    public Observable<BaseBean<WeiXinPayVO>> requestWXPaymentOrder(Long gid, int type,Long userAddressId) {
        return ApiService.getOrderApi().requestWXPaymentOrder(gid,type,userAddressId);
    }
}
