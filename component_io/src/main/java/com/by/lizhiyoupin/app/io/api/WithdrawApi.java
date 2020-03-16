package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.PresentationDetailsBean;
import com.by.lizhiyoupin.app.io.bean.WithDrawtiBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawDetaisBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawaccountBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * data:2019/11/18
 * author:jyx
 * function:
 */
public interface WithdrawApi {
    /**
     *获取会员信息账户信息
     * @return
     */
    @GET("user-account/v1/account")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<WithdrawaccountBean>> requestWithdrawaccount();

    /**
     *获取会员提现记录列表
     * @return
     */
    @GET("user-extract/v1/extract-all")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<PresentationDetailsBean>>> requestPresentationDetails(@Query("start") int start,
                                                                                   @Query("limit") int limit);

    /**
     *获取会员提现记录列表
     * @return
     */
    @GET("user-extract/v1/extract")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<WithdrawDetaisBean>> requestWithdrawDetails(@Query("eid") Long eid);

    /**
     *增加会员提现记录
     * @return
     */
    @POST("user-extract/v1/extract")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<BaseBean<WithdrawBean>> requestWithdrawOperator(@Body WithDrawtiBean bean);


}
