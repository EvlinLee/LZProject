package com.by.lizhiyoupin.app.user.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.UserHomeBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.user.contract.AvatarContract;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * data:2019/11/9
 * author:jyx
 * function:
 */
public class AvatarModel implements AvatarContract.AvatarModel {


    @Override
    public Observable<BaseBean<String>> requestAvatar(MultipartBody.Part file) {
        return ApiService.getAvatarApi().requestAvatarUrl(file);
    }

    @Override
    public Observable<BaseBean<UserHomeBean>> requestUserHome() {
        return ApiService.getUserHomeApi().requestUserHome();
    }

}
