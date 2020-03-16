package com.by.lizhiyoupin.app.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.stack.ActivityStack;
import com.by.lizhiyoupin.app.stack.LoginStack;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import java.util.HashMap;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/23 14:50
 * Summary: 微信登录，分享。 需要放在   包名/wxapi目录下
 */
public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    private static final String TAG = WXEntryActivity.class.getSimpleName();
    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享
    private Context mContext;
    private static IWXAPI mWxApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.instance().addActivity(this);
        mContext = this;
        //这句没有写,是不能执行回调的方法的

        getWxApiInstance(this).handleIntent(getIntent(), this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getWxApiInstance(this).handleIntent(intent, this);
    }

    public static IWXAPI getWxApiInstance(Context context) {
        if (mWxApi == null) {
            registerToWX(context);
        }
        return mWxApi;
    }

    /**
     * 微信登录
     */
    private static void registerToWX(Context context) {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(context, CommonConst.WEIXIN_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(CommonConst.WEIXIN_APP_ID);
    }

    //该方法执行umeng登陆/分享的回调的处理
    @Override
    public void a(com.umeng.weixin.umengwx.b b) {
        //处理友盟和微信原生冲突，导致微信登录code传给后台，后台去获取openid，一直提示code been used
        if (b instanceof com.umeng.weixin.umengwx.i) {
            //处理我们自己的逻辑
            LZLog.i(TAG, "a:--登录---->");
            finish();
        } else {
            //分享返回等回调处理
            LZLog.i(TAG, "a:--分享等---->");
            super.a(b);
        }
    }

    @Override
    protected void a(Intent intent) {
        super.a(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();


        //在onResume中处理从微信授权通过以后不会自动跳转的问题，手动结束该页面

    }
    @Override
    public void onReq(BaseReq baseReq) {
        int type = baseReq.getType();
        LZLog.i(TAG, "onReq:------>" );

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int type = baseResp.getType(); //类型：分享还是登录
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                MessageToast.showToastBottom(this,"拒绝授权微信登录", Gravity.CENTER);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "";
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    message = "取消了微信登录";
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    message = "取消了微信分享";
                }
                MessageToast.showToastBottom(this,message, Gravity.CENTER);
                break;
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    String code = ((SendAuth.Resp) baseResp).code;
                    LZLog.i(TAG, "code:------>" + code);

                    //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
                    wechatLogin(code);

                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    MessageToast.showToastBottom(this,"微信分享成功", Gravity.CENTER);
                }
                break;
        }
    }

    private static void wechatLogin(String code) {
        requestWechatLogin(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> bean) {
                        super.onNext(bean);
                        if (bean.success()) {
                            LZLog.i(TAG, "wechatLogin success==");
                            if (bean.getResult() != null) {
                                //微信授权登录成功
                                UserInfoBean result = bean.getResult();
                                String openId = result.getOpenId();
                                String accessToken = result.getAccessToken();
                                SPUtils.getDefault().putString(CommonConst.KEY_WECHAT_TOKEN, accessToken);
                                SPUtils.getDefault().putString(CommonConst.KEY_WECHAT_OPID, openId);
                                final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                                        .getManager(IAccountManager.class.getName());
                                if (accountManager == null || bean.data == null) {
                                    LZLog.w(TAG, "accountManager或data=null");
                                    return;
                                }
                                accountManager.onLogin(bean.data);
                                LoginStack.instance().finishAllLoginActivity();
                                //CommonSchemeJump.showMainActivity(null);
                            }
                        } else if ("0".equals(bean.code)) {
                            LZLog.i(TAG, "wechatLogin need bind phone!");
                            //首次注册登录，需要去走绑定流程
                            UserInfoBean result = bean.getResult();
                            String openId = result.getOpenId();
                            String accessToken = result.getAccessToken();
                            LZLog.i(TAG, "wechatLogin need bind phone!accessToken="+accessToken);
                            SPUtils.getDefault().putString(CommonConst.KEY_WECHAT_TOKEN, accessToken);
                            SPUtils.getDefault().putString(CommonConst.KEY_WECHAT_OPID, openId);
                            Bundle bundle = new Bundle();
                            bundle.putString(CommonConst.KEY_LOGIN_TYPE_FROM, CommonConst.KEY_LOGIN_TYPE_WX);
                            CommonSchemeJump.showActivity(null, "/app/LoginPhoneActivity", bundle);
                        }else {
                            onError(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.w(TAG, "wechatLogin error==" + throwable);
                        CommonToast.showToast("微信登录失败!");
                    }
                });
    }

    private static Observable<BaseBean<UserInfoBean>> requestWechatLogin(String code) {
        HashMap<String, String> hashMap = new HashMap<>(3);
        hashMap.put("code", code);
        return ApiService.getNewsApi().requestWechatLogin(hashMap);
    }

    @Override
    protected void onDestroy() {
        ActivityStack.instance().removeActivity(this);
        super.onDestroy();
    }
    @Override
    public void finish() {
        ActivityStack.instance().removeActivity(this);
        super.finish();
    }
}
