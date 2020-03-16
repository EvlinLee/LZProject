package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.AuthorizationBean;
import com.by.lizhiyoupin.app.io.bean.FansCountBean;
import com.by.lizhiyoupin.app.io.bean.FootprintBean;
import com.by.lizhiyoupin.app.io.bean.FootprintParentBean;
import com.by.lizhiyoupin.app.io.bean.UpdateViersionBean;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.entity.NewsRequestParams;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface NewsApi {

    @POST("info/qryNews")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<BaseBean<Object>> requestNewsList(@Body NewsRequestParams entity);

    /**
     * 手机号注册登录
     *
     * @param entity
     * @return
     */
    @POST("user/v1/register")
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UserInfoBean>> requestPhoneRegister(@FieldMap Map<String, String> entity);

    /**
     * 判断手机号是否已注册
     *
     * @param phone
     * @return
     */
    @GET("user/v1/phone-if-register")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Boolean>> requestJudgePhoneRegister(@Query("phone") String phone);

    /**
     * 微信授权登录
     *
     * @param entity
     * @return
     */
    @POST("user/v1/wechat-login")
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UserInfoBean>> requestWechatLogin(@FieldMap Map<String, String> entity);

    /**
     * 微信token登录
     *
     * @param openId
     * @return
     */
    @POST("user/v1/wechat-login-token")
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UserInfoBean>> requestWechatLoginWithToken(@Field("openId") String openId);

    /**
     * 微信绑定手机号
     *
     * @param entity
     * @return
     */
    @PUT("user/v1/wechat-bind")
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UserInfoBean>> requestWechatBind(@FieldMap Map<String, String> entity);

    /**
     * 发送验证码
     *
     * @param entity
     * @return
     */
    @FormUrlEncoded
    @PUT("user/v1/phone-sms")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Boolean>> requestPutPhoneSms(@FieldMap Map<String, String> entity);

    /**
     * 绑定邀请人
     *
     * @param entity
     * @return
     */
    @FormUrlEncoded
    @PUT("user/v1/inviter-bind")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UserInfoBean>> requestBindInviter(@FieldMap Map<String, String> entity);

    /**
     * 邀请人信息查询
     *
     * @param entity
     * @return
     */
    @GET("user/v1/inviter-info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UserInfoBean>> requestGetInviter(@QueryMap Map<String, String> entity);

    /**
     * 获取用户信息
     *
     * @param apiToken
     * @return
     */
    @GET("user/v1/info-token-front")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UserInfoBean>> requestGetUserInfo(@Query("apiToken") String apiToken);

    /**
     * 校验会员信息是否已经淘宝授权
     *
     * @param url
     * @return
     */
    @GET("user-taobao/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<AuthorizationBean>> requestGetAuthorizeUrl(@Query("url") String url);

    /**
     * 获取粉丝数量信息
     *
     * @return
     */
    @GET("user/v1/fans-gather")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<FansCountBean>> requestGetFansCountInfo();

    /**
     * 足迹
     *
     * @param apiToken
     * @param start
     * @param limit
     * @return
     */
    @GET("user-footprint/v1/page")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<FootprintParentBean>>> requestGetFootprintList(@Query("apiToken") String apiToken,
                                                                            @Query("start") int start,
                                                                            @Query("limit") int limit);

    /**
     * 删除会员足迹
     * RequestFootprintEntity
     *
     * @return
     */
    @PUT("user-footprint/v1/info")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<BaseBean<Object>> requestDeleteFootprintInfo(@Body List<FootprintBean> entity);

    /**
     * 定时发送秒杀推送消息接口
     *
     * @param uid    必传 用户ID
     * @param type   必传 消息类型 1秒杀推送
     * @param msgId  消息任务ID
     * @param title  标题
     * @param link   链接地址
     * @param second 秒：多少秒后执行
     * @return
     */
    @GET("user-message/v1/message-seckill")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Object>> requestPushOfLimitTime(@Query("uid") long uid,
                                                        @Query("type") int type,
                                                        @Query("msgId") String msgId,
                                                        @Query("title") String title,
                                                        @Query("link") String link,
                                                        @Query("second") int second);

    /**
     *刪除定时发送秒杀推送消息
     * @param msgId
     * @return
     */
    @GET("user-message/v1/message-cancel")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Object>> requestDeletePushOfLimitTime(@Query("msgId") String msgId);

    /**
     * 版本更新
     * @param version  版本号
     * @param deviceType  设备类型（1安卓 2苹果）
     * @return
     */
    @POST("app/version/searchVersion")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<UpdateViersionBean>> requestUpdateVersion(@Query("version") String version, @Query("deviceType") int deviceType);


}
