package com.by.lizhiyoupin.app.login.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.SplitUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.login.contract.LoginContract;
import com.by.lizhiyoupin.app.login.presenter.LoginPhonePresenter;
import com.by.lizhiyoupin.app.stack.LoginStack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/23 16:57
 * Summary: 手机号输入页
 */
@Route(path = "/app/LoginPhoneActivity")
public class LoginPhoneActivity extends BaseActivity implements View.OnClickListener, TextWatcher, LoginContract.LoginView {
    public static final String TAG = LoginPhoneActivity.class.getSimpleName();
    private TextView mLoginNextTv;
    private TextView mBindPhoneTv;
    private TextView mLoginLinkTv;
    private TextView mLoginErrorTv;
    private TextView mLoginPhoneInputEt;
    private TextView mLoginBackIv;
    private LoginContract.LoginPresenter mLoginPresenter;
    private String mLoginType;


    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(mLoginPhoneInputEt);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_login_phone_layout);
        LoginStack.instance().addActivity(this);
        initImmersionBar(Color.WHITE,true);
        mLoginPresenter = new LoginPhonePresenter(this, this);
        Intent intent = getIntent();
        mLoginType = intent.getStringExtra(CommonConst.KEY_LOGIN_TYPE_FROM);
        if (TextUtils.isEmpty(mLoginType)) {
            mLoginType = CommonConst.KEY_LOGIN_TYPE_PHONE;
        }
        initView();
        DeviceUtil.showInputMethodDelay(mLoginPhoneInputEt, 500);
    }

    private void initView() {
        mLoginBackIv = findViewById(R.id.actionbar_back_tv);
        mBindPhoneTv = findViewById(R.id.bind_phone_tv);
        mLoginPhoneInputEt = findViewById(R.id.login_phone_input_et);
        mLoginErrorTv = findViewById(R.id.login_phone_error_tv);
        mLoginNextTv = findViewById(R.id.login_phone_next_tv);
        mLoginLinkTv = findViewById(R.id.login_phone_link_tv);

        mLoginBackIv.setText("");
        mLoginBackIv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.actionbar_back), null, null, null);

        mLoginNextTv.setEnabled(false);
        mLoginBackIv.setOnClickListener(this);
        mLoginNextTv.setOnClickListener(this);
        mLoginPhoneInputEt.addTextChangedListener(this);
        if (CommonConst.KEY_LOGIN_TYPE_PHONE.equals(mLoginType)) {
            mBindPhoneTv.setText(R.string.login_with_phone_text);
        } else {
            mBindPhoneTv.setText(R.string.login_bind_phone_text);
        }
        setLoginLinkText();
        DeviceUtil.showInputMethodDelay(mLoginPhoneInputEt, 500);
    }


    private void setLoginLinkText() {
        SpannableStringBuilder spanText = new SpannableStringBuilder(getResources().getString(R.string.login_agreement_privacy));
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                LZLog.i(TAG, "跳转《隐私政策》");
                CommonWebJump.showCommonWebActivity(LoginPhoneActivity.this,  WebUrlManager.getLoginPrivacyProtocolUrl());
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(LoginPhoneActivity.this, R.color.link_color));
                ds.setUnderlineText(false);
            }
        }, 19, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                LZLog.i(TAG, "跳转《荔枝优品用户协议》");
                CommonWebJump.showCommonWebActivity(LoginPhoneActivity.this, WebUrlManager.getLoginRegisterServiceUrl());
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(LoginPhoneActivity.this, R.color.link_color));
                ds.setUnderlineText(false);
            }
        }, 8, 18, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mLoginLinkTv.setText(spanText);
        mLoginLinkTv.setHighlightColor(Color.TRANSPARENT);
        mLoginLinkTv.setMovementMethod(LinkMovementMethod.getInstance()); //设置后 点击事件才能起效
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
            case R.id.login_phone_next_tv:
                goNext();
                break;
            default:
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() < 11) {
            mLoginNextTv.setEnabled(false);
            mLoginErrorTv.setVisibility(View.INVISIBLE);
            return;
        }
        boolean mobile = SplitUtils.isMobile(s.toString().trim());
        if (!mobile) {
            mLoginErrorTv.setVisibility(View.VISIBLE);
            mLoginNextTv.setEnabled(false);
        } else {
            //是有效手机号
            mLoginErrorTv.setVisibility(View.INVISIBLE);
            mLoginNextTv.setEnabled(true);
        }
    }


    @Override
    public void changePhone() {
        //换个手机号试试
        LZLog.i(TAG, "changePhone");
        mLoginPhoneInputEt.setText("");
    }

    private void goNext() {
        if (CommonConst.KEY_LOGIN_TYPE_PHONE.equals(mLoginType)) {
            if (TextUtils.isEmpty(mLoginPhoneInputEt.getText())) {
                LZLog.w(TAG, "手机号为空");
                return;
            }
            //手机号登录，直接去验证码页
            Bundle bundle = new Bundle();
            bundle.putString(CommonConst.KEY_LOGIN_TYPE_FROM, CommonConst.KEY_LOGIN_TYPE_PHONE);
            bundle.putString(CommonConst.KEY_LOGIN_PHONE, mLoginPhoneInputEt.getText().toString());
            CommonSchemeJump.showActivity(this, "/app/VerificationCodeActivity", bundle);

        } else {
            //绑定手机号，需要判定该手机号是否已注册，已注册需要弹框
            //请求服务器判断，
            showLoadingDialog();
            mLoginPresenter.requestJudgePhoneRegister(mLoginPhoneInputEt.getText().toString().trim());

        }
    }

    @Override
    public void toLogin() {
        //去登录--跳转到手机号登录
        LZLog.i(TAG, "toLogin");
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_LOGIN_TYPE_FROM, CommonConst.KEY_LOGIN_TYPE_PHONE);
        CommonSchemeJump.showActivity(this, "/app/LoginPhoneActivity", bundle);

    }

    @Override
    public void requestJudgePhoneRegisterSuccess(Boolean register) {
        LZLog.i(TAG, "requestJudgePhoneRegisterSuccess==" + register);
        dismissLoadingDialog();
        if (register) {
            //已注册需要弹框
            mLoginPresenter.showTipDialog();
        } else {
            //未注册，跳转验证码页 去注册
            Bundle bundle = new Bundle();
            bundle.putString(CommonConst.KEY_LOGIN_TYPE_FROM, CommonConst.KEY_LOGIN_TYPE_WX);
            bundle.putString(CommonConst.KEY_LOGIN_PHONE, mLoginPhoneInputEt.getText().toString());
            CommonSchemeJump.showActivity(this, "/app/VerificationCodeActivity", bundle);

        }

    }

    @Override
    public void requestJudgePhoneRegisterError(Throwable throwable) {
        LZLog.i(TAG, "requestJudgePhoneRegisterError==" + throwable);
        dismissLoadingDialog();

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