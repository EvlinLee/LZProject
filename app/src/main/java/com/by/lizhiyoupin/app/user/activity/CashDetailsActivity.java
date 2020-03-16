package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.io.bean.PresentationDetailsBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawDetaisBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawaccountBean;
import com.by.lizhiyoupin.app.user.contract.WithdrawContract;
import com.by.lizhiyoupin.app.user.presenter.WithdrawPresenter;

import java.util.List;

/*
 * jyx
 * 提现详情页面
 *
 * */
@Route(path = "/app/CashDetailsActivity")
public class CashDetailsActivity extends BaseMVPActivity<WithdrawContract.WithdrawView,
        WithdrawContract.WithdrawPresenters> implements WithdrawContract.WithdrawView,
        View.OnClickListener {
    private TextView mTitle, actionbar_back_tv, actionbar_right_tv, tixian_success, faile_result,
            cash_time, cash_tijao, cash_money, cash_charge, cash_actual, pay_type, pay_account,
            cash_uptime;
    private LinearLayout group2, group1;
    private ImageView cash_details_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_details);
        initImmersionBar(Color.WHITE, true);
        initBar();
        initView();
    }

    @Override
    public WithdrawContract.WithdrawPresenters getBasePresenter() {
        return new WithdrawPresenter(this);
    }

    private void initView() {
        actionbar_back_tv.setOnClickListener(this);
        group2 = findViewById(R.id.group2);
        group1 = findViewById(R.id.group1);
        cash_details_success = findViewById(R.id.cash_details_success);
        tixian_success = findViewById(R.id.tixian_success);
        faile_result = findViewById(R.id.faile_result);//失败原因
        cash_time = findViewById(R.id.cash_time);//提现成功时间
        cash_tijao = findViewById(R.id.cash_tijao);//审核提交时间
        cash_money = findViewById(R.id.cash_money);//提现金额
        cash_charge = findViewById(R.id.cash_charge);//提现手续费
        cash_actual = findViewById(R.id.cash_actual);//实际到账金额
        pay_type = findViewById(R.id.pay_type);//提现类型
        pay_account = findViewById(R.id.pay_account);//提现账号
        cash_uptime = findViewById(R.id.cash_uptime);
        String eid = getIntent().getStringExtra(CommonConst.WITHDRAW_EID);
        basePresenter.requestWithdrawDetails(Long.valueOf(eid));

    }

    private void initBar() {

        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_right_tv = findViewById(R.id.actionbar_right_tv);
        actionbar_right_tv.setText("");
        actionbar_back_tv.setText("");
        mTitle.setText("详情");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
        }
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
        initDetails(bean);
    }

    private void initDetails(WithdrawDetaisBean bean) {
        int status = bean.getStatus();
        cash_money.setText("¥ " + StringUtils.getFormattedDouble(bean.getAmount()));
        cash_charge.setText("¥ " + StringUtils.getFormattedDouble(bean.getCommission()));
        cash_actual.setText("¥ " + StringUtils.getFormattedDouble(bean.getAmount() - bean.getCommission()));
        cash_time.setText(TimeUtils.transForDate(Long.parseLong(bean.getUpdatedTime()), "yyyy-MM-dd HH:mm:ss"));
        cash_tijao.setText("提交时间：" + TimeUtils.transForDate(Long.parseLong(bean.getCreatedTime()), "yyyy-MM-dd HH:mm:ss"));
        if (TextUtils.isEmpty(bean.getAccount())) {
            pay_type.setText("银行卡");
            pay_account.setText(bean.getBankAccount());
        } else {
            pay_type.setText(bean.getAccountType() == 0 ? "支付宝" : "微信");
            pay_account.setText(bean.getAccount());

        }
        if (status == 0) {//失败
            group1.setVisibility(View.GONE);
            group2.setVisibility(View.VISIBLE);
            cash_details_success.setImageResource(R.drawable.cash_faile);
            tixian_success.setText("提现失败");
            faile_result.setVisibility(View.VISIBLE);
            faile_result.setText("失败原因：" + bean.getRemark());
        } else if (status == 1) {//审核中
            group2.setVisibility(View.GONE);
            group1.setVisibility(View.VISIBLE);
            cash_uptime.setText("提交时间：" + TimeUtils.transForDate(Long.parseLong(bean.getCreatedTime()), "yyyy-MM-dd HH:mm:ss"));

        } else if (status == 2) {//审核成功
            group1.setVisibility(View.GONE);
            group2.setVisibility(View.VISIBLE);
        }
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
