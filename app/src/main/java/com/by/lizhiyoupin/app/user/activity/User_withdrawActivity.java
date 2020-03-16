package com.by.lizhiyoupin.app.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.AlipayBean;
import com.by.lizhiyoupin.app.io.bean.PresentationDetailsBean;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.bean.WithDrawtiBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawDetaisBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawaccountBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
import com.by.lizhiyoupin.app.user.contract.WithdrawContract;
import com.by.lizhiyoupin.app.user.presenter.WithdrawPresenter;
import com.by.lizhiyoupin.app.utils.EdittextUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
 * 普通用户提现页面
 * jyx
 * */
@Route(path = "/app/User_withdrawActivity")
public class User_withdrawActivity extends BaseMVPActivity<WithdrawContract.WithdrawView,
        WithdrawContract.WithdrawPresenters> implements WithdrawContract.WithdrawView,
        View.OnClickListener,
        Handler.Callback {
    private TextView mTitle, actionbar_back_tv, actionbar_right_tv, user_submit, getsms, tv_money
            , user_phone;
    private EditText edit_money, withdraw_sms, alipay_name, alipay_account;
    //倒计时60秒
    public static final int TIME_INTERVAL_COUNT = 60;
    public static final int KEY_COUNT_DOWN_CODE = 2001;
    private Handler mHandler;
    private String status, withdrawmoney;
    private String saccount, sbankAccount, sfullName, sidCard, sbankName, bankNickName;
    private int bindStatus;
    private int CLASSCODE = 130;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_withdraw);
        initImmersionBar(Color.WHITE, true);
        mHandler = new Handler(Looper.getMainLooper(), this);
        initBar();
        initView();
    }

    @Override
    public WithdrawContract.WithdrawPresenters getBasePresenter() {
        return new WithdrawPresenter(this);
    }

    private void initView() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        String userPhone = accountManager.getUserPhone();
        UserInfoBean accountInfo = accountManager.getAccountInfo();
        String full_name = accountInfo.getFullName();
        String alipayAccount = accountInfo.getAlipayAccount();
        actionbar_back_tv.setOnClickListener(this);
        actionbar_right_tv.setOnClickListener(this);
        user_submit = findViewById(R.id.user_submit);
        user_submit.setOnClickListener(this);//提交申请
        getsms = findViewById(R.id.getsms);//获取验证码
        getsms.setOnClickListener(this);
        user_phone = findViewById(R.id.user_phone);//手机号
        tv_money = findViewById(R.id.tv_money);
        tv_money.setOnClickListener(this);
        edit_money = findViewById(R.id.edit_money);
        withdraw_sms = findViewById(R.id.withdraw_sms);
        alipay_name = findViewById(R.id.alipay_name);
        alipay_account = findViewById(R.id.alipay_account);
        alipay_account.setText(alipayAccount);
        alipay_name.setText(full_name);
        user_phone.setText(userPhone);
        Intent intent = getIntent();
        status = intent.getStringExtra(CommonConst.WITHDRAW_STATUS);
        withdrawmoney = intent.getStringExtra(CommonConst.WITHDRAW_MONEY);
        tv_money.setText("10元起提（当月可提现金额剩余" + withdrawmoney + "元）");

        EdittextUtil.StringWatcher(alipay_account);//支付宝号限制不输入中文
        EdittextUtil.textChinese(alipay_name);//姓名限制输入中文


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

                        alipay_name.setText(sfullName);
                        alipay_account.setText(saccount);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(alipay_account);
    }

    @Override
    protected void onResume() {
        super.onResume();
        alipay_account.setSelection(alipay_account.getText().toString().length());
        DeviceUtil.showInputMethodDelay(alipay_account, 500);
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
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.actionbar_right_tv:
                CommonSchemeJump.showWithdrawDetailsActivity(this);//提现明细页面
                break;
            case R.id.user_submit:

                if (TextUtils.isEmpty(edit_money.getText().toString())) {
                    CommonToast.showToast("提现金额不能输入为空");
                    return;
                } else if (Double.parseDouble(edit_money.getText().toString()) > Double.parseDouble(withdrawmoney)) {
                    CommonToast.showToast("提现金额不能大于可提现金额");
                    return;
                } else if (Double.parseDouble(edit_money.getText().toString()) < 10) {
                    CommonToast.showToast("提现金额不能小于10元");
                    return;
                } else if (TextUtils.isEmpty(withdraw_sms.getText().toString())) {
                    CommonToast.showToast("验证码不能输入为空");
                    return;
                } else if (TextUtils.isEmpty(alipay_name.getText().toString())) {
                    CommonToast.showToast("真实姓名不能输入为空");
                    return;
                } else if (TextUtils.isEmpty(alipay_account.getText().toString())) {
                    CommonToast.showToast("支付宝号不能输入为空");
                    return;
                }
                WithDrawtiBean withDrawtiBean = new WithDrawtiBean("true", status,
                        edit_money.getText().toString(), user_phone.getText().toString(),
                        withdraw_sms.getText().toString());
                basePresenter.requestWithdrawOperator(withDrawtiBean);
                break;
            case R.id.getsms:
                setTimeCodeText(TIME_INTERVAL_COUNT);
                setNumberSms();
                break;
            case R.id.tv_money:
                tv_money.setVisibility(View.GONE);
                edit_money.setVisibility(View.VISIBLE);
                edit_money.setSelection(edit_money.getText().toString().length());
                DeviceUtil.showInputMethodDelay(edit_money, 500);
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
            getsms.setBackgroundResource(R.drawable.user_withdraw_sms_bg);
            getsms.setTextColor(Color.parseColor("#FFFFFFFF"));
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
        CommonSchemeJump.showActivity(this, "/app/CashSuccessActivity");//提现成功页面
        setResult(CLASSCODE);
        finish();
    }

    @Override
    public void requestWithdrawOperatorError(Throwable throwable) {

    }
}
