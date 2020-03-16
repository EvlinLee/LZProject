package com.by.lizhiyoupin.app.login.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.component_ui.weight.PwdEditText;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.stack.LoginStack;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/24 11:54
 * Summary: 验证码 页面
 */
@Route(path = "/app/VerificationCodeActivity")
public class VerificationCodeActivity extends BaseActivity implements View.OnClickListener,
        Handler.Callback, PwdEditText.OnTextChangeListener {
    public static final String TAG = VerificationCodeActivity.class.getSimpleName();

    public static final int KEY_COUNT_DOWN_CODE = 2001;
    //倒计时60秒
    public static final int TIME_INTERVAL_COUNT = 60;
    //验证码输入框
    private PwdEditText mVerificationCodeInput;
    //登录
    private TextView mLoginBottomTv;
    //倒计时
    private TextView mVerificationCodeTimeTv;
    //返回
    private TextView mBackIv;
    private TextView mSendPhoneTipTv;
    //错误提示
    private View mCodeErrorTv;
    private Handler mHandler;
    private String mLoginType;
    private String mPhone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginStack.instance().addActivity(this);
        setContentView(R.layout.activity_verification_code_layout);
        ImmersionBar.with(this)
                .navigationBarColorInt(Color.WHITE)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .fitsSystemWindows(false)
                .titleBar(findViewById(R.id.status_bar_view))
                .flymeOSStatusBarFontColorInt(Color.BLACK)
                .statusBarColorInt(Color.WHITE)
                .init();
        mHandler = new Handler(Looper.getMainLooper(), this);
        Intent intent = getIntent();
        mLoginType = intent.getStringExtra(CommonConst.KEY_LOGIN_TYPE_FROM);
        mPhone = intent.getStringExtra(CommonConst.KEY_LOGIN_PHONE);
        if (TextUtils.isEmpty(mLoginType)) {
            mLoginType = CommonConst.KEY_LOGIN_TYPE_PHONE;
        }

        initView();
    }

    private void initView() {
        mBackIv = findViewById(R.id.actionbar_back_tv);
        mSendPhoneTipTv = findViewById(R.id.verification_send_phone_tip_tv);
        mVerificationCodeInput = findViewById(R.id.verificationCodeInput);
        mVerificationCodeTimeTv = findViewById(R.id.verification_code_time_tv);
        mLoginBottomTv = findViewById(R.id.verification_code_login_tv);
        mCodeErrorTv = findViewById(R.id.verification_code_error_tv);
        mCodeErrorTv.setVisibility(View.INVISIBLE);
        mBackIv.setText("");
        mBackIv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.actionbar_back), null, null, null);

        mVerificationCodeTimeTv.setEnabled(false);
        mLoginBottomTv.setEnabled(false);
        mVerificationCodeInput.setOnTextChangeListener(this);
        mLoginBottomTv.setOnClickListener(this);
        mVerificationCodeTimeTv.setOnClickListener(this);
        mBackIv.setOnClickListener(this);
        setTimeCodeText(TIME_INTERVAL_COUNT);
        if (CommonConst.KEY_LOGIN_TYPE_PHONE.equals(mLoginType)) {
            //手机号登录验证方式
            mLoginBottomTv.setText(getResources().getString(R.string.login_next_step));
        } else {
            //手机号注册验证
            mLoginBottomTv.setText(getResources().getString(R.string.login_in_text));
        }
        DeviceUtil.showInputMethodDelay(mVerificationCodeInput, 500);
        requestVerificationCode();
    }

    //发送短信验证码
    private void requestVerificationCode() {
        if (TextUtils.isEmpty(mPhone)) {
            LZLog.i(TAG, "手机号为空");
            return;
        }
        ViewUtil.setTextViewFormat(this, mSendPhoneTipTv,
                R.string.login_verification_send_phone_tip_text, 86, mPhone);

        LoginRequestManager.requestPutPhoneSms(mPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Boolean>>() {
                    @Override
                    public void onNext(BaseBean<Boolean> booleanBaseBean) {
                        super.onNext(booleanBaseBean);
                        if (!booleanBaseBean.success()) {
                            onError(new Throwable(booleanBaseBean.msg));
                            return;
                        }
                        LZLog.i(TAG, "发送验证码 成功==" + booleanBaseBean.data);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "发送验证码 失败==" + throwable);
                        MessageToast.showToast(VerificationCodeActivity.this, "发送验证码失败");
                    }
                });
    }

    /**
     * 重发 倒计时
     *
     * @param time
     */
    private void setTimeCodeText(int time) {
        mVerificationCodeTimeTv.setEnabled(time <= 0);
        if (time > 0) {
            if (mVerificationCodeTimeTv != null) {
                mVerificationCodeTimeTv.setTextColor(getResources().getColor(R.color.color_999999));
                mVerificationCodeTimeTv.setText(String.format(getResources().getString(R.string.login_verification_time_text), time));
            }
            Message msg = Message.obtain();
            msg.what = KEY_COUNT_DOWN_CODE;
            msg.obj = time - 1;
            mHandler.sendMessageDelayed(msg, 1000);
        } else {
            setRetryCodeText();
        }
    }

    /**
     * 重新发送
     */
    private void setRetryCodeText() {
        if (mVerificationCodeTimeTv != null) {
            mVerificationCodeTimeTv.setTextColor(getResources().getColor(R.color.color_5491FE));
            mVerificationCodeTimeTv.setText(getResources().getString(R.string.login_verification_time_retry_text));
        }
    }


    @Override
    public void onTextChange(String pwd) {
        LZLog.i(TAG, "验证码 正在输入==" + pwd);
        if (pwd.length() >= 6) {
            DeviceUtil.hideInputMethod(mVerificationCodeInput);
            mLoginBottomTv.setEnabled(true);
        } else {
            mCodeErrorTv.setVisibility(View.INVISIBLE);
            mLoginBottomTv.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.verification_code_time_tv:
                //重置倒计时
                setTimeCodeText(TIME_INTERVAL_COUNT);
                //重新发送 短信验证码
                requestVerificationCode();
                break;
            case R.id.verification_code_login_tv:
                //登录、下一步
                goNext();
                break;
        }
    }

    public void goNext() {
        if (TextUtils.isEmpty(mVerificationCodeInput.getText()) || TextUtils.isEmpty(mPhone)) {
            LZLog.i(TAG, "输入框验证码或手机号为空");
            return;
        }
        if (CommonConst.KEY_LOGIN_TYPE_PHONE.equals(mLoginType)) {
            LZLog.i(TAG, "手机登录 验证码");
            //手机号登录验证方式
            //请求验证 验证码
            //然后判断 手机号刚注册要跳转 邀请码页
            verificationSmsLogin(mPhone, mVerificationCodeInput.getText().toString());

        } else {
            LZLog.i(TAG, "微信登录绑定手机 验证码");
            //手机号绑定验证，
            verificationSmsWechatLogin(mPhone, mVerificationCodeInput.getText().toString());
        }
    }

    /**
     * 手机号绑定验证，成功关闭登录有关页面
     *
     * @param phone
     * @param smsCode
     */
    private void verificationSmsWechatLogin(String phone, String smsCode) {
        LoginRequestManager.requestWechatBind(phone, smsCode, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        LZLog.i(TAG, "微信绑定 验证注册登录 onNext");
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            if ("2".equals(userInfoBeanBaseBean.getCode())) {
                                //验证码错误
                                mCodeErrorTv.setVisibility(View.VISIBLE);
                                return;
                            }
                            return;
                        }

                        UserInfoBean result = userInfoBeanBaseBean.getResult();
                        if (result != null) {
                            LZLog.i(TAG, "微信注册登录成功！");
                            final IAccountManager accountManager =
                                    (IAccountManager) ComponentManager.getInstance()
                                    .getManager(IAccountManager.class.getName());
                            if (accountManager == null) {
                                LZLog.w(TAG, "accountManager =null");
                                return;
                            }
                            accountManager.onLogin(result);
                            if (result.getFirstTimeLogin() == 1) {
                                //首次登陆注册 跳转邀请码页
                                Bundle bundle = new Bundle();
                                bundle.putString(CommonConst.KEY_LOGIN_PHONE, mPhone);
                                CommonSchemeJump.showActivity(VerificationCodeActivity.this,
                                        "/app/InvitationCodeActivity", bundle);
                            }  else if (result.getFirstLogin()==1){
                                //是否第一次登陆
                                if (result.getRegisterType()==6){
                                    //来源是优惠券，则先跳转一个h5页
                                    CommonWebJump.showCommonWebActivity(VerificationCodeActivity.this,result.getCouponGiftMoneyUrl());
                                }
                                LoginStack.instance().finishAllLoginActivity();
                            }else {
                                LoginStack.instance().finishAllLoginActivity();
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "微信绑定  验证注册登录 onError" + throwable);
                    }

                });
    }


    /**
     * 手机号登录 验证码验证
     *
     * @param smsCode
     */
    private void verificationSmsLogin(String phone, String smsCode) {
        LoginRequestManager.requestRegisterLogin(phone, smsCode, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (!userInfoBeanBaseBean.success()) {
                            if (BaseBean.CODE_VERIFICATION_ERROR.equals(userInfoBeanBaseBean.getCode())) {
                                //验证码错误
                                mCodeErrorTv.setVisibility(View.VISIBLE);
                                return;
                            } else {
                                onError(new Throwable(userInfoBeanBaseBean.msg));
                            }
                            return;
                        }
                        LZLog.i(TAG, "验证注册登录 onNext");
                        UserInfoBean result = userInfoBeanBaseBean.getResult();
                        if (result != null) {
                            LZLog.i(TAG, " 手机登录成功！");
                            final IAccountManager accountManager =
                                    (IAccountManager) ComponentManager.getInstance()
                                    .getManager(IAccountManager.class.getName());
                            if (accountManager == null) {
                                LZLog.w(TAG, "accountManager =null");
                                return;
                            }
                            accountManager.onLogin(result);
                            if (result.getFirstTimeLogin() == 1) {
                                //在app端 首次登陆注册 跳转邀请码页
                                Bundle bundle = new Bundle();
                                bundle.putString(CommonConst.KEY_LOGIN_PHONE, mPhone);
                                CommonSchemeJump.showActivity(VerificationCodeActivity.this,
                                        "/app/InvitationCodeActivity", bundle);
                            } else if (result.getFirstLogin()==1){
                                //是否第一次登陆
                                if (result.getRegisterType()==6){
                                    //来源是优惠券，则先跳转一个h5页
                                    CommonWebJump.showCommonWebActivity(VerificationCodeActivity.this,result.getCouponGiftMoneyUrl());
                                }
                                LoginStack.instance().finishAllLoginActivity();
                            }else {
                                LoginStack.instance().finishAllLoginActivity();
                            }
                        } else {
                            MessageToast.showToastBottom(VerificationCodeActivity.this,
                                    userInfoBeanBaseBean.msg, Gravity.CENTER);
                            LZLog.i(TAG, "验证注册登录 onError" + userInfoBeanBaseBean.msg);
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "验证注册登录 onError" + throwable);
                        MessageToast.showToast(VerificationCodeActivity.this, "网络繁忙,请稍后再试");
                    }


                });
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case KEY_COUNT_DOWN_CODE:
                Integer time = (Integer) msg.obj;
                setTimeCodeText(time);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVerificationCodeInput != null) {
            DeviceUtil.hideInputMethod(mVerificationCodeInput);
        }
    }

    @Override
    protected void onDestroy() {
        LoginStack.instance().removeActivity(this);
        super.onDestroy();
        mHandler.removeMessages(KEY_COUNT_DOWN_CODE);

    }

    @Override
    public void finish() {
        LoginStack.instance().removeActivity(this);
        super.finish();
    }
}
