package com.by.lizhiyoupin.app.manager;

import android.content.Intent;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ResponseCallback;
import com.by.lizhiyoupin.app.common.SettingConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_sdk.AliSdkManager;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.AuthorizationBean;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.config.AccountConfig;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.jupsh.PushSetValue;
import com.by.lizhiyoupin.app.login.LoginRequestManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/23 10:24
 * Summary: 用户信息管理
 */
public class AccountManager implements IAccountManager {
    public static final String TAG = AccountManager.class.getSimpleName();

    public AccountManager() {
        super();
    }

    public void init() {
        LZLog.d(TAG, "AccountManager constructor init");
        UserInfoBean accountInfoPref = AccountConfig.getAccountInfoPref();
        if (accountInfoPref != null && accountInfoPref.getId() > 0) {
            //初始化用户信息 缓存数据
            onLogin(accountInfoPref);
            LoginRequestManager.requestGetuserinfo(accountInfoPref.getApiToken());
        } else {
            onLogout();
        }
    }

    @Override
    public boolean isLogined() {
        return AccountConfig.isLogin();
    }

    @Override
    public long getAccountId() {
        UserInfoBean userInfoBean = getAccountInfo();
        return userInfoBean.getId();
    }

    @Override
    public String getUserPhone() {
        UserInfoBean userInfoBean = getAccountInfo();
        return userInfoBean.getPhone();
    }

    @Override
    public String getUserToken() {
        UserInfoBean userInfoBean = getAccountInfo();
        return userInfoBean.getApiToken();
    }

    @Override
    public synchronized UserInfoBean getAccountInfo() {
        //不为空
        return AccountConfig.getAccountInfo();
    }

    @Override
    public void onLogout() {
        removeAccountInfo();
        PushSetValue.setUnbindPush();
        AliSdkManager.logoutTaoBao();
    }

    @Override
    public void onLogin(UserInfoBean userInfoBean) {
        saveAccountInfo(userInfoBean);
        PushSetValue.setPushvalue();
    }


    @Override
    public void removeAccountInfo() {
        AccountConfig.setAccountInfo(new UserInfoBean());
        AccountConfig.removeAccountInfo();
        SPUtils.getDefault().putString(CommonConst.KEY_WECHAT_OPID, "");
        SPUtils.getDefault().putString(CommonConst.KEY_WECHAT_TOKEN, "");
        LocalBroadcastManager.getInstance(LiZhiApplication.getApplication())
                .sendBroadcast(new Intent(SettingConst.ACTION_LOGOUT_SUCCESS));
    }

    /**
     * 用户账户信息保存到本地，非ui线程调用
     */
    @Override
    public void saveAccountInfo(UserInfoBean userInfoBean) {
        LZLog.d(TAG, "saveAccountInfoPref");
        AccountConfig.setAccountInfo(userInfoBean);
        AccountConfig.saveAccountInfoPref(userInfoBean);
        LocalBroadcastManager.getInstance(LiZhiApplication.getApplication())
                .sendBroadcast(new Intent(SettingConst.ACTION_LOGIN_SUCCESS));
    }

    /**
     * 用户淘宝等授权
     *
     * @param url
     * @param callback
     */
    public static void setAuthorizeUrl(String url, ResponseCallback<AuthorizationBean> callback) {
        ApiService.getNewsApi().requestGetAuthorizeUrl(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<AuthorizationBean>>() {
                    @Override
                    public void onNext(BaseBean<AuthorizationBean> bean) {
                        super.onNext(bean);

                        if (bean.success()) {
                            LZLog.i(TAG, "requestGetAuthorizeUrl success");
                            callback.success(bean.data);
                        } else {
                            callback.error(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "requestGetAuthorizeUrl onError" + throwable);
                        callback.error(throwable);
                    }
                });
    }


}
