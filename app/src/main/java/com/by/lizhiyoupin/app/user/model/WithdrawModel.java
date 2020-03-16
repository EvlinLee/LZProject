package com.by.lizhiyoupin.app.user.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.PresentationDetailsBean;
import com.by.lizhiyoupin.app.io.bean.WithDrawtiBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawDetaisBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawaccountBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.user.contract.WithdrawContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * data:2019/11/18
 * author:jyx
 * function:
 */
public class WithdrawModel  implements WithdrawContract.WithdrawModel {
    @Override
    public Observable<BaseBean<WithdrawaccountBean>> requestWithdrawView() {
        return ApiService.getWithdrawApi().requestWithdrawaccount();
    }

    @Override
    public Observable<BaseBean<List<PresentationDetailsBean>>> requestPresentationDetailsView(int start,int limit) {
        return ApiService.getWithdrawApi().requestPresentationDetails(start,limit);
    }

    @Override
    public Observable<BaseBean<WithdrawDetaisBean>> requestWithdrawDetailsView(Long eid) {
        return ApiService.getWithdrawApi().requestWithdrawDetails(eid);
    }

    @Override
    public Observable<BaseBean<WithdrawBean>> requestWithdrawOperatorView(WithDrawtiBean bean) {
        return ApiService.getWithdrawApi().requestWithdrawOperator(bean);
    }
}
