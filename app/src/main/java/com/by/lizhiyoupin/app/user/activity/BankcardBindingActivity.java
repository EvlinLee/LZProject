package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.AlipayBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
import com.by.lizhiyoupin.app.utils.EdittextUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
 * 银行卡绑定页面
 * jyx
 * */
@Route(path = "/app/BankcardBindingActivity")
public class BankcardBindingActivity extends BaseActivity implements View.OnClickListener,
        Handler.Callback {
    private TextView mTitle, actionbar_back_tv, getsms, freelance_service, bind1, bind2, bank_phone;
    private EditText bank_accountname, bank_name, bank_number, bank_sms;
    private LinearLayout bank_bind, bank_sub;
    //倒计时60秒
    public static final int TIME_INTERVAL_COUNT = 60;
    public static final int KEY_COUNT_DOWN_CODE = 2001;
    private Handler mHandler;
    private CheckBox alipay_check_xy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcard_binding);
        initImmersionBar(Color.WHITE, true);
        mHandler = new Handler(Looper.getMainLooper(), this);
        initBar();
        initView();
        initListener();

    }

    private void initListener() {
        // 监听多个输入框
        TextChange textChange = new TextChange();
        bank_accountname.addTextChangedListener(textChange);
        bank_name.addTextChangedListener(textChange);
        bank_number.addTextChangedListener(textChange);
        bank_phone.addTextChangedListener(textChange);
        bank_sms.addTextChangedListener(textChange);
    }

    private void initView() {
        actionbar_back_tv.setOnClickListener(this);
        bank_accountname = findViewById(R.id.bank_accountname);//开户行
        bank_bind = findViewById(R.id.bank_bind);//绑定
        bank_sub = findViewById(R.id.bank_sub);
        bank_name = findViewById(R.id.bank_name);//账户户名
        bank_number = findViewById(R.id.bank_number);//银行账户
        bank_phone = findViewById(R.id.bank_phone);//手机号
        bank_sms = findViewById(R.id.bank_sms);//验证码
        getsms = findViewById(R.id.getsms);//获取验证码
        getsms.setOnClickListener(this);
        bank_bind.setOnClickListener(this);
        freelance_service = findViewById(R.id.freelance_service);//自由服务协议
        freelance_service.setOnClickListener(this);
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        bank_phone.setText(accountManager.getUserPhone());
        alipay_check_xy = findViewById(R.id.alipay_check_xy);
        alipay_check_xy.setOnClickListener(this);
        bind1 = findViewById(R.id.bind1);
        bind2 = findViewById(R.id.bind2);

        bank_name.setText(getIntent().getStringExtra(CommonConst.WITHDRAW_BANKNICKNAME));
        bank_number.setText(getIntent().getStringExtra(CommonConst.WITHDRAW_BANKACCOUTN));
        bank_accountname.setText(getIntent().getStringExtra(CommonConst.WITHDRAW_BANKNAME));
        if (!TextUtils.isEmpty(getIntent().getStringExtra(CommonConst.WITHDRAW_BANKNICKNAME)) && !TextUtils.isEmpty(getIntent().getStringExtra(CommonConst.WITHDRAW_BANKACCOUTN))
                && !TextUtils.isEmpty(getIntent().getStringExtra(CommonConst.WITHDRAW_BANKNAME))) {
            bind1.setText("立即修改");
            bind2.setText("立即修改");
        }


        EdittextUtil.textChinese(bank_name);//只能输入中文
        EdittextUtil.textChinese(bank_accountname);


        alipay_check_xy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!TextUtils.isEmpty(bank_accountname.getText().toString())
                            && !TextUtils.isEmpty(bank_name.getText().toString())
                            && !TextUtils.isEmpty(bank_number.getText().toString())
                            && !TextUtils.isEmpty(bank_sms.getText().toString())
                    ) {
                        bank_sub.setVisibility(View.GONE);
                        bank_bind.setVisibility(View.VISIBLE);
                    } else {
                        bank_bind.setVisibility(View.GONE);
                        bank_sub.setVisibility(View.VISIBLE);
                    }
                } else {
                    bank_bind.setVisibility(View.GONE);
                    bank_sub.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        bank_name.setSelection(bank_name.getText().toString().length());
        DeviceUtil.showInputMethodDelay(bank_name, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(bank_name);
    }

    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setText("");
        mTitle.setText("银行账户绑定");
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
            case R.id.getsms:
                setTimeCodeText(TIME_INTERVAL_COUNT);
                setNumberSms();
                break;
            case R.id.bank_bind:
                setSubmit();
                break;
            case R.id.alipay_check_xy:
                alipay_check_xy.setChecked(alipay_check_xy.isChecked() ? true : false);
                break;
            case R.id.freelance_service:
                CommonWebJump.showCommonWebActivity(this, WebUrlManager.getFreelanceServiceUrl());
                break;

        }
    }

    private void setSubmit() {
        SettingRequestManager.requestBank(bank_number.getText().toString(),
                bank_accountname.getText().toString(), bank_name.getText().toString(),
                bank_sms.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<AlipayBean>>() {
                    @Override
                    public void onNext(BaseBean<AlipayBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (userInfoBeanBaseBean.success()) {
                            MessageToast.showToastBottom(BankcardBindingActivity.this, "绑定银行卡成功", Gravity.CENTER);
                            finish();
                        } else {
                            MessageToast.showToastBottom(BankcardBindingActivity.this, userInfoBeanBaseBean.msg, Gravity.CENTER);
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void setNumberSms() {

        LoginRequestManager.requestPutPhoneSms(bank_phone.getText().toString())
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
//                        LZLog.i(TAG, "发送验证码 成功==" + booleanBaseBean.data);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
//                        LZLog.i(TAG, "发送验证码 失败==" + throwable);
                    }
                });

    }

    private void setTimeCodeText(int time) {
        getsms.setEnabled(time <= 0);
        if (time > 0) {
            if (getsms != null) {

                getsms.setText(time + "s");
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
        if (getsms != null) {
            getsms.setText(getResources().getString(R.string.login_verification_time_retry_text));
        }
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

    // EditText监听器
    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean Sign1 = bank_accountname.getText().length() > 0;
            boolean Sign2 = bank_name.getText().length() > 0;
            boolean Sign3 = bank_number.getText().length() > 0;
            boolean Sign5 = bank_sms.getText().length() > 0;

            if (Sign1 & Sign2 & Sign3 & Sign5 & alipay_check_xy.isChecked()) {
                bank_sub.setVisibility(View.GONE);
                bank_bind.setVisibility(View.VISIBLE);
            } else {
                bank_bind.setVisibility(View.GONE);
                bank_sub.setVisibility(View.VISIBLE);
            }
        }
    }


}
