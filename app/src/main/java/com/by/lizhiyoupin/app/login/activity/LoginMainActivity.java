package com.by.lizhiyoupin.app.login.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.stack.LoginStack;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.by.lizhiyoupin.app.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/25 18:11
 * Summary: 登录主页面
 */
@Route(path = "/app/LoginMainActivity")
public class LoginMainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = LoginMainActivity.class.getSimpleName();
    private TextView mBackTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginStack.instance().addActivity(this);
        setContentView(R.layout.activity_login_main_layout);
        initImmersionBar(Color.WHITE, true);
        initView();
        requestPermission();
    }

    private void requestPermission() {
        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE};
        ActivityCompat.requestPermissions(this, mPermissionList, 123);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void initView() {
        findViewById(R.id.actionbar_rl).setBackgroundColor(getResources().getColor(R.color.white_100));
        mBackTv = findViewById(R.id.actionbar_back_tv);
        mBackTv.setText("");
        mBackTv.setOnClickListener(this);
        ViewUtil.setDrawableOfTextView(mBackTv, R.drawable.actionbar_back, ViewUtil.DrawableDirection.LEFT);
        findViewById(R.id.login_wx_fl).setOnClickListener(this);
        findViewById(R.id.login_phone_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_wx_fl:
                //微信登录
                String opid = SPUtils.getDefault().getString(CommonConst.KEY_WECHAT_OPID, "");
                //   String token = SPUtils.getDefault().getString(CommonConst.KEY_WECHAT_TOKEN, "");
                if (!TextUtils.isEmpty(opid)) {
                    //使用accessToken登录
                    LZLog.i(TAG, "使用token opid登录" + opid);
                    wxLoginWithToken(opid);
                } else {
                    LZLog.i(TAG, "使用code登录");
                    wxLogin();
                }
                break;
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.login_phone_tv:
                //手机一键登录
                Intent intent2 = new Intent(this, LoginPhoneActivity.class);
                intent2.putExtra(CommonConst.KEY_LOGIN_TYPE_FROM, CommonConst.KEY_LOGIN_TYPE_PHONE);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 微信 token登录
     *
     * @param opid
     */
    private void wxLoginWithToken(String opid) {
        ApiService.getNewsApi().requestWechatLoginWithToken(opid).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> bean) {
                        super.onNext(bean);
                        if (bean.success()) {
                            LZLog.i(TAG, "微信token登录成功");
                            final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                                    .getManager(IAccountManager.class.getName());
                            if (accountManager == null || bean.data == null) {
                                LZLog.w(TAG, "accountManager或data=null");
                                return;
                            }
                            accountManager.onLogin(bean.data);
                            LoginStack.instance().finishAllLoginActivity();
                            // CommonSchemeJump.showMainActivity(LoginMainActivity.this);
                        } else if ("0".equals(bean.getCode())) {
                            //token过期，重新授权登录
                            LZLog.i(TAG, "token过期，重新授权登录");
                            wxLogin();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "微信登录失败");
                        CommonToast.showToast("微信登录失败!");
                    }
                });
    }

    /**
     * 微信登录 {@link WXEntryActivity 回调}
     */
    private void wxLogin() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        //微信授权
        WXEntryActivity.getWxApiInstance(this).sendReq(req);

    }


    @Override
    protected void onStart() {
        super.onStart();
        LZLog.i(TAG, "onStart ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LZLog.i(TAG, "onStop ");
    }

    @Override
    protected void onDestroy() {
        LoginStack.instance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        LoginStack.instance().removeActivity(this);
        super.finish();
    }
}
