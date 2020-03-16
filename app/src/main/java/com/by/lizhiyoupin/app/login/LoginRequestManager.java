package com.by.lizhiyoupin.app.login;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.utils.MD5;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/14 10:20
 * Summary:
 */
public class LoginRequestManager {
    private static final String SMS_DIGEST = "lzyp&*12Bs";

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    public static Observable<BaseBean<Boolean>> requestPutPhoneSms(String phone) {
        HashMap<String, String> hashMap = new HashMap<>(2);
        hashMap.put("phone", phone);
        hashMap.put("validateSign", MD5.Md5(phone + SMS_DIGEST));
        return ApiService.getNewsApi().requestPutPhoneSms(hashMap);
    }

    /**
     * 注册登录
     *
     * @param phone
     * @param smsCode
     * @param inviteCode
     * @return
     */
    public static Observable<BaseBean<UserInfoBean>> requestRegisterLogin(String phone, String smsCode, String inviteCode) {
        HashMap<String, String> hashMap = new HashMap<>(3);
        hashMap.put("phone", phone);
        hashMap.put("smsCode", smsCode);
        hashMap.put("inviteCode", inviteCode);
        return ApiService.getNewsApi().requestPhoneRegister(hashMap);
    }

    /**
     * 微信绑定手机号
     *
     * @param phone
     * @param smsCode
     * @param inviteCode
     * @return
     */
    public static Observable<BaseBean<UserInfoBean>> requestWechatBind(String phone, String smsCode, String inviteCode) {
        HashMap<String, String> hashMap = new HashMap<>(3);
        hashMap.put("phone", phone);
        hashMap.put("smsCode", smsCode);
        hashMap.put("openId", SPUtils.getDefault().getString(CommonConst.KEY_WECHAT_OPID));
        hashMap.put("accessToken", SPUtils.getDefault().getString(CommonConst.KEY_WECHAT_TOKEN));
        hashMap.put("inviteCode", inviteCode);
        return ApiService.getNewsApi().requestWechatBind(hashMap);
    }

    /**
     * 查询邀请人
     *
     * @param inviteCode
     * @return
     */
    public static Observable<BaseBean<UserInfoBean>> requestGetInviter(String inviterPhone, String inviteCode) {
        HashMap<String, String> hashMap = new HashMap<>(2);
        hashMap.put("inviterPhone", inviterPhone);//邀请人手机号
        hashMap.put("inviteCode", inviteCode); //邀请码
        return ApiService.getNewsApi().requestGetInviter(hashMap);
    }

    /**
     * 绑定邀请人
     *
     * @param phone      自己的手机
     * @param inviteCode 他人的邀请码
     * @param otherPhone 他人的手机
     * @return
     */
    public static Observable<BaseBean<UserInfoBean>> requestBindInviter(String phone, String inviteCode, String otherPhone) {
        HashMap<String, String> hashMap = new HashMap<>(3);
        hashMap.put("inviterPhone", otherPhone);//邀请人
        hashMap.put("inviteCode", inviteCode);
        hashMap.put("phone", phone);//被邀请人（自己）
        return ApiService.getNewsApi().requestBindInviter(hashMap);
    }

    /**
     * 查询用户信息
     *
     * @param apiToken
     * @return
     */
    public static Observable<BaseBean<UserInfoBean>> requestGetUserInfo(String apiToken) {
        return ApiService.getNewsApi().requestGetUserInfo(apiToken);
    }

    public static void requestGetuserinfo(String apiToken) {
        requestGetUserInfo(apiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> bean) {
                        super.onNext(bean);
                        if (bean.success() && bean.getResult() != null) {
                            LZLog.i("user", "requestGetuserinfo success");
                            LiZhiApplication.getApplication().getAccountManager().saveAccountInfo(bean.data);
                        } else {
                            onError(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i("user", "requestGetuserinfo onError");
                    }
                });
    }
}
