package com.by.lizhiyoupin.app.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.AlipayBean;
import com.by.lizhiyoupin.app.io.bean.PresentationDetailsBean;
import com.by.lizhiyoupin.app.io.bean.WithDrawtiBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawDetaisBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawaccountBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
import com.by.lizhiyoupin.app.user.contract.WithdrawContract;
import com.by.lizhiyoupin.app.user.presenter.WithdrawPresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
 * 运营商提现页面
 * jyx
 * */
@Route(path = "/app/Operator_withdrawActivity")
public class Operator_withdrawActivity extends BaseMVPActivity<WithdrawContract.WithdrawView,
        WithdrawContract.WithdrawPresenters> implements WithdrawContract.WithdrawView,
        View.OnClickListener,
        Handler.Callback {
    private TextView mTitle, actionbar_back_tv, actionbar_right_tv, user_submit, getsms, zfb_text
            , bank_text, text_tax, user_phone;
    //倒计时60秒
    public static final int TIME_INTERVAL_COUNT = 60;
    public static final int KEY_COUNT_DOWN_CODE = 2001;
    private Handler mHandler;
    private EditText withdraw_money, withdraw_sms;
    private LinearLayout operate_zfb, operate_bank, linear_bank, linear_zfb;
    private CheckBox operate_imgzfb, operate_imgbank;
    private ImageView img_bank, img_zfb;
    private String status, withdrawmoney;
    private String alipay = "true";
    private String saccount, sbankAccount, sfullName, sidCard, sbankName, bankNickName;
    private int bindStatus;
    private int CLASSCODE = 130;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);
        initImmersionBar(Color.WHITE, true);
        mHandler = new Handler(Looper.getMainLooper(), this);
        initBar();

        initView();
    }

    @Override
    public WithdrawContract.WithdrawPresenters getBasePresenter() {
        return new WithdrawPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListener();
        withdraw_money.setSelection(withdraw_money.getText().toString().length());
        DeviceUtil.showInputMethodDelay(withdraw_money, 500);

    }

    private void initView() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        String userPhone = accountManager.getUserPhone();
        actionbar_back_tv.setOnClickListener(this);
        actionbar_right_tv.setOnClickListener(this);
        user_submit = findViewById(R.id.user_submit);//提交申请
        user_submit.setOnClickListener(this);
        getsms = findViewById(R.id.getsms);//获取验证码
        getsms.setOnClickListener(this);
        user_phone = findViewById(R.id.user_phone);//手机号
        user_phone.setText(userPhone);
        operate_imgzfb = findViewById(R.id.operate_imgzfb);
        operate_imgzfb.setOnClickListener(this);
        operate_imgbank = findViewById(R.id.operate_imgbank);
        operate_imgbank.setOnClickListener(this);
        operate_bank = findViewById(R.id.operate_bank);
        operate_zfb = findViewById(R.id.operate_zfb);
        operate_zfb.setOnClickListener(this);
        operate_bank.setOnClickListener(this);
        linear_zfb = findViewById(R.id.linear_zfb);
        linear_bank = findViewById(R.id.linear_bank);
        zfb_text = findViewById(R.id.zfb_text);
        bank_text = findViewById(R.id.bank_text);
        img_zfb = findViewById(R.id.img_zfb);
        img_zfb.setOnClickListener(this);
        img_bank = findViewById(R.id.img_bank);
        img_bank.setOnClickListener(this);
        text_tax = findViewById(R.id.text_tax);//增值税金额  0.0337
        withdraw_money = findViewById(R.id.withdraw_money);//提现金额
        withdraw_sms = findViewById(R.id.withdraw_sms);//输入验证码
        withdraw_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double v = Double.parseDouble("".equals(s.toString()) ? "0" : s.toString());
                text_tax.setText(String.valueOf(v + (v * 0.0337)));
            }
        });

    }

    private void initListener() {
        Intent intent = getIntent();
        String bankAccount = intent.getStringExtra(CommonConst.WITHDRAW_BANKACCOUTN);
        String account = intent.getStringExtra(CommonConst.WITHDRAW_ACCOUNT);
        status = intent.getStringExtra(CommonConst.WITHDRAW_STATUS);
        withdrawmoney = intent.getStringExtra(CommonConst.WITHDRAW_MONEY);
        if (TextUtils.isEmpty(bankAccount) && TextUtils.isEmpty(account)) {
            operate_imgzfb.setChecked(true);
            alipay = "true";
            linear_zfb.setVisibility(View.VISIBLE);
        } else {
            operate_imgzfb.setChecked(true);
            alipay = "true";
            zfb_text.setText(saccount);
            bank_text.setText(bankAccount);
            linear_zfb.setVisibility(View.VISIBLE);
        }
        operate_imgzfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linear_zfb.setVisibility(View.VISIBLE);
                    operate_imgbank.setChecked(false);
                    alipay = "true";
                } else {
                    linear_zfb.setVisibility(View.GONE);
                }
            }
        });
        operate_imgbank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linear_bank.setVisibility(View.VISIBLE);
                    operate_imgzfb.setChecked(false);
                    alipay = "false";
                } else {
                    linear_bank.setVisibility(View.GONE);
                }
            }
        });


        SettingRequestManager.requestFindInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<AlipayBean>>() {
                    @Override
                    public void onNext(BaseBean<AlipayBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            return;
                        }
                        bindStatus = userInfoBeanBaseBean.getResult().getBindStatus();//支付宝绑定状态
                        saccount = userInfoBeanBaseBean.getResult().getAccount();//支付宝号
                        sbankAccount = userInfoBeanBaseBean.getResult().getBankAccount();//银行卡号
                        sfullName = userInfoBeanBaseBean.getResult().getFullName();//支付宝正式姓名
                        sidCard = userInfoBeanBaseBean.getResult().getIdCard();//身份证号
                        sbankName = userInfoBeanBaseBean.getResult().getBankName();//银行开户行
                        bankNickName = userInfoBeanBaseBean.getResult().getBankNickName();//银行开户名

                        if (bindStatus == 0) {//未绑定
                            zfb_text.setText("");
                        } else if (bindStatus == 1) {//绑定中
                            zfb_text.setText("");
                        } else if (bindStatus == 2) {//绑定成功
                            zfb_text.setText(saccount);
                        } else if (bindStatus == 3) {//绑定失败
                            zfb_text.setText("");
                        }
                        bank_text.setText(sbankAccount);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });

    }

    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_right_tv = findViewById(R.id.actionbar_right_tv);
        actionbar_right_tv.setText("提现明细");
        actionbar_back_tv.setText("");
        mTitle.setText("提现");
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.actionbar_right_tv:
                CommonSchemeJump.showWithdrawDetailsActivity(this);//提现明细页面
                break;
            case R.id.user_submit:
                if (!operate_imgbank.isChecked() && !operate_imgzfb.isChecked()) {
                    CommonToast.showToast("请先选择一个账户");
                } else {
                    if (TextUtils.isEmpty(withdraw_money.getText().toString())) {
                        CommonToast.showToast("提现金额不能为空");
                        return;
                    } else if (Double.parseDouble(withdraw_money.getText().toString()) > Double.parseDouble(withdrawmoney)) {
                        CommonToast.showToast("提现金额不能大于可提现金额");
                        return;
                    } else if (Double.parseDouble(withdraw_money.getText().toString()) < 10) {
                        CommonToast.showToast("提现金额不能小于10元");
                        return;
                    } else if (TextUtils.isEmpty(withdraw_sms.getText().toString())) {
                        CommonToast.showToast("验证码输入不能为空");
                        return;
                    }
                    if (operate_imgzfb.isChecked()) {
                        if (TextUtils.isEmpty(zfb_text.getText().toString())) {
                            CommonToast.showToast("未绑定企业支付宝，请绑定后再试");
                            return;
                        }
                    } else if (operate_imgbank.isChecked()) {
                        if (TextUtils.isEmpty(bank_text.getText().toString())) {
                            CommonToast.showToast("未绑定企业银行账号，请绑定后再试");
                            return;
                        }
                    }
                    WithDrawtiBean withDrawtiBean = new WithDrawtiBean(alipay, status,
                            withdraw_money.getText().toString(), user_phone.getText().toString(),
                            withdraw_sms.getText().toString());
                    basePresenter.requestWithdrawOperator(withDrawtiBean);
                }
                break;
            case R.id.getsms:
                setTimeCodeText(TIME_INTERVAL_COUNT);
                setNumberSms();
                break;
            case R.id.img_bank://银行卡绑定页面
                bundle.putString(CommonConst.WITHDRAW_BANKACCOUTN, sbankAccount);
                bundle.putString(CommonConst.WITHDRAW_BANKNAME, sbankName);
                bundle.putString(CommonConst.WITHDRAW_BANKNICKNAME, bankNickName);
                CommonSchemeJump.showActivity(this, "/app/BankcardBindingActivity", bundle);
                break;
            case R.id.img_zfb://支付宝绑定页面
                bundle.putString(CommonConst.WITHDRAW_ACCOUNT, saccount);
                bundle.putString(CommonConst.WITHDRAW_FULLNAME, sfullName);
                bundle.putString(CommonConst.WITHDRAW_IDCARD, sidCard);
                bundle.putString(CommonConst.WITHDRAW_BINDSTATUS, bindStatus + "");
                CommonSchemeJump.showActivity(this, "/app/AlipayBindingActivity", bundle);
                break;

        }
    }

    private void setNumberSms() {

        LoginRequestManager.requestPutPhoneSms(user_phone.getText().toString())
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
                getsms.setBackgroundResource(R.drawable.sms_bg);
                getsms.setTextColor(Color.parseColor("#FF999999"));
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
            getsms.setBackgroundResource(R.drawable.user_withdraw_sms_bg);
            getsms.setTextColor(Color.parseColor("#FFFFFFFF"));
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


    @Override
    public void requestWithdrawSuccess(WithdrawaccountBean bean) {

    }

    @Override
    public void requestWithdrawError(Throwable throwable) {

    }

    @Override
    public void requestPresentationDetailsSuccess(List<PresentationDetailsBean> list) {

    }

    @Override
    public void requestPresentationDetailsError(Throwable throwable) {

    }

    @Override
    public void requestWithdrawDetailsSuccess(WithdrawDetaisBean bean) {

    }

    @Override
    public void requestWithdrawDetailsError(Throwable throwable) {

    }

    @Override
    public void requestWithdrawOperatorSuccess(WithdrawBean bean) {
        CommonSchemeJump.showActivity(this, "/app/CashSuccessActivity");//提现明细页面
        setResult(CLASSCODE);
        finish();
    }


    @Override
    public void requestWithdrawOperatorError(Throwable throwable) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(withdraw_money);
    }
}
