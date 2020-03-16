package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.UserHomeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * data:2019/11/22
 * author:jyx
 * function:
 */
public interface UserHomeApi {
    /**
     * 我的界面综合收益
     * @return
     */
    @GET("income/v1/user-home")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UserHomeBean>> requestUserHome();

}
