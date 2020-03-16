package com.by.lizhiyoupin.app.user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.SettingConst;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_umeng.share.manager.IShareManager;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.ChildcountBean;
import com.by.lizhiyoupin.app.io.bean.PlusTimeBean;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.user.contract.UpgradeContract;
import com.by.lizhiyoupin.app.user.presenter.UpgradePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/*
 * 升级会员权益
 *
 *jyx
 * */
@Route(path = "/app/UpgrademembershipActivity")
public class UpgrademembershipActivity extends BaseMVPActivity<UpgradeContract.UpgradeView,
        UpgradeContract.UpgradePresenters> implements UpgradeContract.UpgradeView,
        View.OnClickListener {
    private TextView mTitle, actionbar_back_tv, progress1_title1, progress1_title2, must_invate,
            progress_num1, progress_num2, progress_time, must_join;
    private LinearLayout invate_bg, rlue_pt, rlue_operator, rlue_plus, linear_operator, linear_pt
            , linear_plus, linear_operator1, progress_linear, linear_progress1, linear_progress2;
    private ProgressBar progress1, progress2;
    private int roleLevel;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrademembership);
        initImmersionBar(Color.WHITE,true);
        registerBroadcastReceiver();
        initBar();
        initView();

        initRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendVipInfoRefresh();
                initView();
            }
        });

    }

    @Override
    public UpgradeContract.UpgradePresenters getBasePresenter() {
        return new UpgradePresenter(this);
    }

    private void initView() {
        invate_bg = findViewById(R.id.invate_bg);
        rlue_operator = findViewById(R.id.rlue_operator);//运营商规则
        rlue_plus = findViewById(R.id.rlue_plus);//plus超级会员规则
        rlue_pt = findViewById(R.id.rlue_pt);//普通会员规则
        linear_pt = findViewById(R.id.linear_pt);
        linear_plus = findViewById(R.id.linear_plus);
        linear_operator = findViewById(R.id.linear_operator);
        linear_operator1 = findViewById(R.id.linear_operator1);
        progress1 = findViewById(R.id.progress1);
        progress2 = findViewById(R.id.progress2);
        progress_linear = findViewById(R.id.progress_linear);
        linear_progress1 = findViewById(R.id.linear_progress1);
        linear_progress2 = findViewById(R.id.linear_progress2);
        progress1_title1 = findViewById(R.id.progress1_title1);
        progress1_title2 = findViewById(R.id.progress1_title2);
        must_invate = findViewById(R.id.must_invate);//立即邀请好友
        must_invate.setOnClickListener(this);
        progress_num1 = findViewById(R.id.progress_num1);//进度条1
        progress_num2 = findViewById(R.id.progress_num2);//进度条2
        progress_time = findViewById(R.id.progress_time);//任务时间
        must_join = findViewById(R.id.must_join);//立即参加活动
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);//刷新


        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        roleLevel = accountManager.getAccountInfo().getRoleLevel();//会员等级
        int plusVipMissionStatus = accountManager.getAccountInfo().getPlusVipMissionStatus();
        //plus会员任务状态 0未开启  1开启  2成功
        int plusOperatorMissionStatus =
                accountManager.getAccountInfo().getPlusOperatorMissionStatus();//plus运营商任务状态 0未开启
        //  1保级成功  2升级任务  3成功  4任务失败
        String superiorId = accountManager.getAccountInfo().getSuperiorId();//上级id
        if (roleLevel == 1) {//普通
            if (TextUtils.isEmpty(superiorId)) {//判断有无上级id
                must_join.setVisibility(View.VISIBLE);
                rlue_pt.setVisibility(View.VISIBLE);
                linear_pt.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
                must_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TimeUtils.isFrequentOperation()) {
                            return;
                        }
                        //填写邀请码
                        DiaLogManager.showInvitationCodeDialog(UpgrademembershipActivity.this,
                                getSupportFragmentManager());
                    }
                });
            } else {
                progress_linear.setVisibility(View.VISIBLE);
                invate_bg.setBackgroundResource(R.drawable.pu_invate);
                rlue_pt.setVisibility(View.VISIBLE);
                linear_pt.setVisibility(View.VISIBLE);
                linear_progress1.setVisibility(View.GONE);
                progress1_title2.setText("专属粉丝");
                must_join.setVisibility(View.GONE);
                basePresenter.requestUpgrade(1);
                linear_progress2.setVisibility(View.VISIBLE);
                progress_time.setVisibility(View.GONE);
            }

        } else if (roleLevel == 2) {//超级
            if (plusVipMissionStatus == 0) {//判断plus超级会员任务是否开启
                must_join.setVisibility(View.VISIBLE);
                rlue_plus.setVisibility(View.VISIBLE);
                linear_plus.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
                must_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TimeUtils.isFrequentOperation()) {
                            return;
                        }
                        basePresenter.requestPlus();
                    }
                });
            } else {
                progress_linear.setVisibility(View.VISIBLE);
                invate_bg.setBackgroundResource(R.drawable.plus_invate);
                rlue_plus.setVisibility(View.VISIBLE);
                linear_plus.setVisibility(View.VISIBLE);
                linear_progress1.setVisibility(View.GONE);
                progress1_title2.setText("专属超级会员");
                linear_progress2.setVisibility(View.VISIBLE);
                basePresenter.requestUpgrade(2);
                basePresenter.requestPlusTime();
            }

        } else if (roleLevel == 3) {//plus超级
            progress_linear.setVisibility(View.VISIBLE);
            invate_bg.setBackgroundResource(R.drawable.plus_invate);
            rlue_plus.setVisibility(View.VISIBLE);
            linear_plus.setVisibility(View.VISIBLE);
            linear_progress1.setVisibility(View.GONE);
            basePresenter.requestPlusTime();
            basePresenter.requestUpgrade(2);
        } else if (roleLevel == 4) {//运营商
            if (plusOperatorMissionStatus == 0 || plusOperatorMissionStatus == 4) {
                must_join.setVisibility(View.VISIBLE);
                rlue_operator.setVisibility(View.VISIBLE);
                linear_operator.setVisibility(View.VISIBLE);
                invate_bg.setBackgroundResource(R.drawable.operator_invate);
                mSwipeRefreshLayout.setRefreshing(false);
                must_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TimeUtils.isFrequentOperation()) {
                            return;
                        }
                        basePresenter.requestOperator();
                    }
                });
            } else {
                progress_linear.setVisibility(View.VISIBLE);
                invate_bg.setBackgroundResource(R.drawable.operator_invate);
                rlue_operator.setVisibility(View.VISIBLE);
                linear_operator.setVisibility(View.VISIBLE);
                progress1_title1.setText("运营商");
                progress_time.setVisibility(View.VISIBLE);
                basePresenter.requestUpgrade(3);
                basePresenter.requestOperatorTime();
            }
        } else if (roleLevel == 5) {//plus运营商
            progress_linear.setVisibility(View.VISIBLE);
            invate_bg.setBackgroundResource(R.drawable.operator_keep);
            rlue_operator.setVisibility(View.VISIBLE);
            linear_operator1.setVisibility(View.VISIBLE);
            progress1_title1.setText("运营商");
            progress_time.setVisibility(View.VISIBLE);
            basePresenter.requestUpgrade(3);
            basePresenter.requestOperatorTime();
        }

//
    }

    private void initBar() {
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setOnClickListener(this);
        actionbar_back_tv.setText("");
        mTitle.setText("免费升级权益");
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
            case R.id.must_invate:
                shareWithPlat();//立即邀请好友
                break;

        }
    }

    private void sendVipInfoRefresh() {
        initView();
    }

    private void shareWithPlat() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        String code = accountManager.getAccountInfo().getCode();

        List<ChannelListBean> channelListBeans = new ArrayList<>();

        ChannelListBean channelListBean = new ChannelListBean();
        ChannelListBean channelListBean2 = new ChannelListBean();
        channelListBean.setCode(0);
        channelListBean.setName("微信");
        channelListBean.setShareTitle("快来和我一起省钱赚钱吧");
        channelListBean.setShareType(2);
        channelListBean.setDesc("大品牌有保障，隐藏优惠不用抢，天天最低价，随心买，任性赚！");
        channelListBean.setLink(WebUrlManager.getRegisterShareUrl(code));
        channelListBeans.add(channelListBean);

        channelListBean2.setCode(1);
        channelListBean2.setName("朋友圈");
        channelListBean2.setShareTitle("快来和我一起省钱赚钱吧");
        channelListBean2.setShareType(2);
        channelListBean2.setDesc("大品牌有保障，隐藏优惠不用抢，天天最低价，随心买，任性赚！");
        channelListBean2.setLink(WebUrlManager.getRegisterShareUrl(code));
        channelListBeans.add(channelListBean2);


        final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                .getManager(IShareManager.class.getName());
        if (shareManager == null) {
            return;
        }
        shareManager.showShareDialog(this, channelListBeans);
    }

    @Override
    public void requestUpgradeSuccess(ChildcountBean bean) {
        mSwipeRefreshLayout.setRefreshing(false);
        fannum(bean, roleLevel);
    }

    private void fannum(ChildcountBean bean, int roleLevel) {
        if (roleLevel == 1) {
            progress2.setProgress(bean.getChildCount());
            progress_num2.setText(bean.getChildCount() + "");
        } else if (roleLevel == 2) {
            progress2.setProgress(bean.getChildCount());
            progress_num2.setText(bean.getChildCount() + "");
        } else if (roleLevel == 3) {
            progress2.setProgress(bean.getChildCount());
            progress_num2.setText(bean.getChildCount() + "");
        } else if (roleLevel == 4) {
            progress2.setProgress(bean.getChildCount());
            progress1.setProgress(bean.getOperateCount());
            progress_num1.setText(bean.getOperateCount() + "");
            progress_num2.setText(bean.getChildCount() + "");
        } else if (roleLevel == 5) {
            progress2.setProgress(bean.getChildCount());
            progress1.setProgress(bean.getOperateCount());
            progress_num1.setText(bean.getOperateCount() + "");
            progress_num2.setText(bean.getChildCount() + "");
        }

    }

    @Override
    public void requestUpgradeError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void requestPlusSuccess(boolean bean) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (bean == true) {
            UserInfoBean accountInfo =
                    LiZhiApplication.getApplication().getAccountManager().getAccountInfo();
            accountInfo.setPlusVipMissionStatus(1);
            LiZhiApplication.getApplication().getAccountManager().saveAccountInfo(accountInfo);
            must_join.setVisibility(View.GONE);
            invate_bg.setBackgroundResource(R.drawable.plus_invate);
            rlue_plus.setVisibility(View.VISIBLE);
            linear_plus.setVisibility(View.VISIBLE);
            linear_progress1.setVisibility(View.GONE);
            progress1_title2.setText("专属超级会员");
            linear_progress2.setVisibility(View.VISIBLE);
            progress_time.setVisibility(View.GONE);
            progress_linear.setVisibility(View.VISIBLE);
            basePresenter.requestUpgrade(2);
            basePresenter.requestPlusTime();
        }

    }

    @Override
    public void requestPlusError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
        showThrowable(throwable);
    }

    @Override
    public void requestOperatorSuccess(boolean bean) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (bean == true) {
            must_join.setVisibility(View.GONE);
            progress_linear.setVisibility(View.VISIBLE);
            invate_bg.setBackgroundResource(R.drawable.operator_invate);
            rlue_operator.setVisibility(View.VISIBLE);
            linear_operator.setVisibility(View.VISIBLE);
            progress1_title1.setText("运营商");
            progress_time.setVisibility(View.VISIBLE);
            basePresenter.requestUpgrade(3);
            basePresenter.requestOperatorTime();

        }

    }

    @Override
    public void requestOperatorError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
        showThrowable(throwable);
    }

    @Override
    public void requestOperatorTimeSuccess(PlusTimeBean bean) {
        mSwipeRefreshLayout.setRefreshing(false);
        plusTime(bean);
    }

    @Override
    public void requestOperatorTimeError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
        showThrowable(throwable);
    }

    @Override
    public void requestPlusTimeSuccess(PlusTimeBean bean) {
        mSwipeRefreshLayout.setRefreshing(false);
        plusTime(bean);
    }

    private void plusTime(PlusTimeBean bean) {
        progress_time.setVisibility(View.VISIBLE);
        progress_time.setText("当前周期：" + bean.getStartTime() + " - " + bean.getEndTime());
    }

    @Override
    public void requestPlusTimeError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
        showThrowable(throwable);
    }


    private void showThrowable(Throwable throwable) {

    }

    private LocalBroadcastReceiver mReceiver;

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SettingConst.ACTION_LOGIN_SUCCESS);
            LocalBroadcastManager.getInstance(LiZhiApplication.getApplication()).registerReceiver(mReceiver, intentFilter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(LiZhiApplication.getApplication()).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_LOGIN_SUCCESS.equals(action)) {
                sendVipInfoRefresh();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }
}
