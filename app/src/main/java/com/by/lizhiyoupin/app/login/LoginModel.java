package com.by.lizhiyoupin.app.login;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.login.contract.LoginContract;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/24 10:30
 * Summary:
 */
public class LoginModel implements LoginContract.LoginModel {



    @Override
    public Observable<BaseBean<Boolean>> requestJudgePhoneRegister(String phone) {
        return  ApiService.getNewsApi().requestJudgePhoneRegister(phone);
    }

}
