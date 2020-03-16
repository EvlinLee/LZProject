package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.PushEditBean;
import com.by.lizhiyoupin.app.io.bean.PushGoodsBean;
import com.by.lizhiyoupin.app.io.bean.PushMessageDescBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/3 17:26
 * Summary:
 */
public interface PushEditApi {
    /**
     * 获取运营商指定日期可推送的消息数量，默认当天
     *
     * @param time 非必填 2020-01-02
     * @return 6
     */
    @GET("message-push/v1/send-number")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Integer>> requestPushEditTips(@Query("time") String time);

    /**
     * 获取消息编辑 文案列表
     *
     * @return
     */
    @GET("interact-describe/v1/list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<PushMessageDescBean>>> requestPushMessageDescList();

    /**
     * 获取推送消息详情
     *
     * @param messageId
     * @return
     */
    @GET("interact-launch/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<PushEditBean>> requestGetPushDetailInfo(@Query("id") Long messageId);

    /**
     * 新增推送消息
     *
     * @param editBean
     * @return
     */
    @POST("interact-launch/v1/info")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<BaseBean<Object>> requestAddPushDetailInfo(@Body PushEditBean editBean);

    /**
     * 修改推送消息
     * @param editBean
     * @return
     */
    @PUT("interact-launch/v1/info")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<BaseBean<Object>> requestUpdatePushDetailInfo(@Body PushEditBean editBean);



    /**
     *  根据推送管理-运营商发送的消息列表
     * @return
     */
    @GET("interact-launch/v1/list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<PushGoodsBean>>> requestGetPushGoodsList(@Query("start") int start,
                                                                      @Query("limit") int limit);


    /**
     * 根据推送商品主键id  删除推送商品数据
     * @return
     */
    @GET("interact-launch/v1/delete")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Object>> requestDeletePushGoodsInfo(@Query("id") Long messageId);
}
