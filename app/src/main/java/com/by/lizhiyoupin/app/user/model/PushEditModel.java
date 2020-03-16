package com.by.lizhiyoupin.app.user.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.PushEditBean;
import com.by.lizhiyoupin.app.io.bean.PushMessageDescBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.user.contract.PushEditContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/3 17:15
 * Summary: 运营商push消息管理 model
 */
public class PushEditModel implements PushEditContract.IPushEditModel {

    @Override
    public Observable<BaseBean<Integer>> requestPushEditTips(String time) {
        return ApiService.getPushEditApi().requestPushEditTips(time);
    }

    @Override
    public Observable<BaseBean<PushEditBean>> requestGetPushDetailInfo(long messageId) {
        return  ApiService.getPushEditApi().requestGetPushDetailInfo(messageId);
    }

    @Override
    public Observable<BaseBean<Object>> requestAddPushDetailInfo(PushEditBean editBean) {
        return ApiService.getPushEditApi().requestAddPushDetailInfo(editBean);
    }

    @Override
    public Observable<BaseBean<Object>> requestUpdatePushDetailInfo(PushEditBean editBean) {
        return ApiService.getPushEditApi().requestUpdatePushDetailInfo(editBean);
    }

    @Override
    public Observable<BaseBean<List<PushMessageDescBean>>> requestPushMessageDescList() {
        return ApiService.getPushEditApi().requestPushMessageDescList();
    }
}
