package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.InformationBean;
import com.by.lizhiyoupin.app.io.bean.PushGoodsBean;
import com.by.lizhiyoupin.app.io.bean.UserMessageVO;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * data:2019/11/14
 * author:jyx
 * function:
 */
public interface MessageApi {
    /**
     *
     * 我的消息
     *
     * @return
     */
    @GET("user-message/v1/messages")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<UserMessageVO>>> requestUserMessage(@Query("type") int type,
                                                                 @Query("start") int start,
                                                                 @Query("limit") int limit);


    /**
     * 消息状态
     *
     *
     * @return
     *
     */
    @FormUrlEncoded
    @PUT("user-message/v1/status")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<String>> requestUserMessageStatus(@FieldMap Map<String, String> entity);

    /**
     *
     * 首页消息
     *
     * @return
     */
    @GET("user-message/v1/main-list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<InformationBean>>> requestMainMessage();


    /**
     * 互动消息状态
     *
     *
     * @return
     *
     */
    @FormUrlEncoded
    @PUT("interact-receive/v1/status")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<String>> requestInteractMessageStatus(@FieldMap Map<String, String> entity);

    /**
     *
     * 我的消息
     * 消息中心 --互动消息列表
     * @return
     */
    @GET("interact-receive/v1/list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<PushGoodsBean>>> requestGetGoodsMessage(@Query("start") int start,
                                                                     @Query("limit") int limit);

}
