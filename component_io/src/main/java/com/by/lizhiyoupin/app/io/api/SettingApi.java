package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.AlipayBean;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

/**
 * data:2019/11/5
 * author:jyx
 * function:
 */
public interface SettingApi {
    /**
     * 设置
     *
     * @param entity
     * @param
     * @return
     */
    @FormUrlEncoded
    @PUT("user/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UserInfoBean>> requestSetting(@FieldMap Map<String, String> entity);

    /**
     * 更改手机号
     *
     * @param entity
     * @param
     * @return
     */
    @FormUrlEncoded
    @PUT("user/v1/phone")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UserInfoBean>> requestPhone(@FieldMap Map<String, String> entity);

    /**
     * 支付宝绑定
     *
     * @param entity
     * @param
     * @return
     */
    @FormUrlEncoded
    @PUT("user-account/v1/alipay-account")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<AlipayBean>> requestAlipay(@FieldMap Map<String, String> entity);

    /**
     * 银行卡绑定
     *
     * @param entity
     * @param
     * @return
     */
    @FormUrlEncoded
    @PUT("user-account/v1/bank-account")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<AlipayBean>> requestBank(@FieldMap Map<String, String> entity);

    /**
     *  根据身份证号查询签约结果接口
     *
     * @param entity
     * @param
     * @return
     */
    @FormUrlEncoded
    @PUT("user-account/v1/alipay-bind-qry")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<AlipayBean>> requestAlipayBindFind(@FieldMap Map<String, String> entity);

    /**
     *  根据token查询用户信息
     *
     * @param
     * @param
     * @return
     */
    @GET("user-account/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<AlipayBean>> requestInfo();
}
