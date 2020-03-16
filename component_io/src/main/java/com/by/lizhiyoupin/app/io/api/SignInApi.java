package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.SignInBean;
import com.by.lizhiyoupin.app.io.bean.SignInRedPaperBean;
import com.by.lizhiyoupin.app.io.entity.ActivitySwitchEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/9 19:28
 * Summary:
 */
public interface SignInApi {
    /**
     * 获取会员昨天今天明天的 签到信息
     * @param remindType 提醒类型(手机APP是否允许通知) 0 提醒关闭 1 提醒成功
     * @return
     */
    @GET("sign-bonus/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<SignInBean>> requestSignInInfo(@Query("remindType") int remindType);

    /**
     * 获取会员弹出框金额，即签到前置条件
     * @param popupType 弹窗类型 0 首页 1 签到页
     * @param equipmentId 非必填  设备号（用来唯一标识一个设备）
     * @return
     */
    @GET("sign-bonus/v1/popup")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<SignInRedPaperBean>> requestSignInRedPaper(@Query("popupType") int popupType, @Query("equipmentId") String equipmentId);

    /**
     * 签到领取红包接口 即签到
     * @param signBonusAmount 红包金额
     * @return
     */
    @PUT("sign-bonus/v1/bonus")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Object>> requestSignInEveryDay(@Query("signBonusAmount") double signBonusAmount );

    /**
     * 签到推送开关
     * @param remindType 提醒类型 0 不提醒 1 提醒
     * @return
     */
    @PUT("sign-bonus/v1/remind")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Object>> requestSignInPushSwitch(@Query("remindType") int remindType );

    /**
     * 获取会员签到活动是否启动开关
     * @return
     */
    @GET("sign-bonus/v1/switch")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<ActivitySwitchEntity>> requestSignInCanShow();

}
