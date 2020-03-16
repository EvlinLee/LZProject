package com.by.lizhiyoupin.app.sign.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.SignInBean;
import com.by.lizhiyoupin.app.io.bean.SignInRedPaperBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.sign.contract.SignInContract;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/9 14:34
 * Summary: 签到model
 */
public class SignInModel implements SignInContract.ISignInModel {
    @Override
    public Observable<BaseBean<SignInBean>> requestSignInInfo(int remindType) {
        return ApiService.getSignInApi().requestSignInInfo(remindType);
    }

    @Override
    public Observable<BaseBean<SignInRedPaperBean>> requestSignInRedPaper(int popupType, String equipmentId) {
        return ApiService.getSignInApi().requestSignInRedPaper(popupType,equipmentId);
    }

    @Override
    public Observable<BaseBean<Object>> requestSignInEveryDay(double signBonusAmount) {
        return ApiService.getSignInApi().requestSignInEveryDay(signBonusAmount);
    }

    @Override
    public Observable<BaseBean<Object>> requestSignInPushSwitch(int remindType) {
        return ApiService.getSignInApi().requestSignInPushSwitch(remindType);
    }
}
