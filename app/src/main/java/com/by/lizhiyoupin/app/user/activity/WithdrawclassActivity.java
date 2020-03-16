package com.by.lizhiyoupin.app.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.io.bean.PresentationDetailsBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawDetaisBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawaccountBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.user.contract.WithdrawContract;
import com.by.lizhiyoupin.app.user.presenter.WithdrawPresenter;

import java.util.List;

import androidx.annotation.Nullable;

/*
 * jyx
 * 提现分类页面
 *
 * */
@Route(path = "/app/WithdrawclassActivity")
public class WithdrawclassActivity extends BaseMVPActivity<WithdrawContract.WithdrawView,
        WithdrawContract.WithdrawPresenters> implements WithdrawContract.WithdrawView,
        View.OnClickListener {
    private TextView mTitle, actionbar_back_tv, commission_pt, immediate_withdrawals3,
            immediate_withdrawals4,
            immediate_withdrawals2, immediate_withdrawals1, commission_yx, commission_lb,
            commission_hd;
    private String account, fullName, bankName, bankAccount;
    private double surplusBalance, commonlyBalance, giftBalance, highBalance, activityBalance;
    private int REQUESTCODE = 120;
    private int CLASSCODE = 130,bindStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawclass);
        initImmersionBar(Color.WHITE,true);
        initBar();
        initView();
        setResult(REQUESTCODE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        basePresenter.requestWithdraw();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CLASSCODE){
            basePresenter.requestWithdraw();
        }
    }


    @Override
    public WithdrawContract.WithdrawPresenters getBasePresenter() {
        return new WithdrawPresenter(this);
    }

    private void initView() {
        actionbar_back_tv.setOnClickListener(this);
        commission_pt = findViewById(R.id.commission_pt);//普通佣金
        commission_yx = findViewById(R.id.commission_yx);//优选佣金
        commission_lb = findViewById(R.id.commission_lb);//礼包佣金
        commission_hd = findViewById(R.id.commission_hd);//活动佣金
        immediate_withdrawals1 = findViewById(R.id.immediate_withdrawals1);//普通
        immediate_withdrawals1.setOnClickListener(this);
        immediate_withdrawals2 = findViewById(R.id.immediate_withdrawals2);//优选
        immediate_withdrawals2.setOnClickListener(this);
        immediate_withdrawals3 = findViewById(R.id.immediate_withdrawals3);//礼包
        immediate_withdrawals3.setOnClickListener(this);
        immediate_withdrawals4 = findViewById(R.id.immediate_withdrawals4);//活动
        immediate_withdrawals4.setOnClickListener(this);


    }

    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);

        actionbar_back_tv.setText("");
        mTitle.setText("提现");
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        int roleLevel = accountManager.getAccountInfo().getRoleLevel();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.WITHDRAW_ACCOUNT, account);
        bundle.putString(CommonConst.WITHDRAW_FULLNAME, fullName);
        bundle.putString(CommonConst.WITHDRAW_BANKNAME, bankName);
        bundle.putString(CommonConst.WITHDRAW_BANKACCOUTN, bankAccount);
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.immediate_withdrawals1:
                bundle.putString(CommonConst.WITHDRAW_STATUS, "1");
                bundle.putString(CommonConst.WITHDRAW_MONEY, String.valueOf(commonlyBalance));
                Log.e("yongjin",commonlyBalance+"");
                withDraw(roleLevel, bundle);
                break;
            case R.id.immediate_withdrawals2:
                bundle.putString(CommonConst.WITHDRAW_STATUS, "3");
                bundle.putString(CommonConst.WITHDRAW_MONEY, String.valueOf(highBalance));
                Log.e("yongjin",highBalance+"");
                withDraw(roleLevel, bundle);
                break;
            case R.id.immediate_withdrawals3:
                bundle.putString(CommonConst.WITHDRAW_STATUS, "2");
                bundle.putString(CommonConst.WITHDRAW_MONEY, String.valueOf(giftBalance));
                Log.e("yongjin",giftBalance+"");
                withDraw(roleLevel, bundle);
                break;
            case R.id.immediate_withdrawals4:
                bundle.putString(CommonConst.WITHDRAW_STATUS, "4");
                bundle.putString(CommonConst.WITHDRAW_MONEY, String.valueOf(activityBalance));
                Log.e("yongjin",activityBalance+"");
                withDraw(roleLevel, bundle);
                break;
        }
    }

    private void withDraw(int roleLevel, Bundle bundle) {
        if (roleLevel == 4 || roleLevel == 5) {
            CommonSchemeJump.showActivityForResult(this, "/app/Operator_withdrawActivity", bundle
                    , CLASSCODE);//运营商提现页面
        } else {

            if (TextUtils.isEmpty(account) && TextUtils.isEmpty(fullName)) {
                CommonSchemeJump.showActivity(this, "/app/AlipayBindingActivity");//支付宝绑定页面
            } else {
                if (bindStatus == 0) {
                    MessageToast.showToastBottom(this, "账户未绑定", Gravity.CENTER);
                    return;
                } else if (bindStatus == 1) {
                    MessageToast.showToastBottom(this, "账户绑定中", Gravity.CENTER);
                    return;
                } else if (bindStatus == 2) {
                    bundle.putString("surplusBalance", surplusBalance + "");
                    CommonSchemeJump.showActivityForResult(this, "/app/User_withdrawActivity",
                            bundle, CLASSCODE);//普通会员提现页面
                } else if (bindStatus == 3) {
                    MessageToast.showToastBottom(this, "账户绑定绑定失败", Gravity.CENTER);
                    return;
                }
            }
        }
    }

    @Override
    public void requestWithdrawSuccess(WithdrawaccountBean bean) {


        initWithdraw(bean);


    }

    private void initWithdraw(WithdrawaccountBean bean) {
        commonlyBalance = bean.getCommonlyBalance();//普通分佣
        giftBalance = bean.getGiftBalance();//礼包余额
        highBalance = bean.getHighBalance();//优选余额
        activityBalance = bean.getActivityBalance();//活动余额
        surplusBalance = bean.getSurplusBalance();//可用余额
        account = bean.getAccount();//支付宝账号
        fullName = bean.getFullName();//支付宝名称
        bankName = bean.getBankName();//银行户名
        bankAccount = bean.getBankAccount();//银行卡账号
        bindStatus = bean.getBindStatus();//支付宝绑定状态
        commission_pt.setText("¥ " + StringUtils.getFormattedDouble(commonlyBalance));
        commission_yx.setText("¥ " + StringUtils.getFormattedDouble(highBalance));
        commission_lb.setText("¥ " + StringUtils.getFormattedDouble(giftBalance));
        commission_hd.setText("¥ " + StringUtils.getFormattedDouble(activityBalance));
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

    }

    @Override
    public void requestWithdrawOperatorError(Throwable throwable) {

    }
}
