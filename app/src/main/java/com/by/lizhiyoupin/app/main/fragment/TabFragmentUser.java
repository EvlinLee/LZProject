package com.by.lizhiyoupin.app.main.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.SettingConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.UserHomeBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.by.lizhiyoupin.app.main.manager.TabFragmentManager;
import com.by.lizhiyoupin.app.manager.AccountManager;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.user.contract.AvatarContract;
import com.by.lizhiyoupin.app.user.presenter.AvatarPresenter;
import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/26 14:37
 * Summary: 我的
 */
public class TabFragmentUser extends BaseFragment implements AvatarContract.AvatarView,
        View.OnClickListener {
    public static final String TAG = TabFragmentUser.class.getSimpleName();
    private Context mContext;
    private TextView mTitle, user_login, user_name, user_level, invate_code,
            fans_people, moeny_make, money_save, lastmonth_moeny, month_money, yesterday_money,
            today_money;
    private LinearLayout save_money, accument_fans, make_money,
            user_order, my_fans, my_invate, my_vip, my_shopcar, my_foot, my_materiel,
            my_about, my_customer, new_tutorial, my_setting, user_invate, user_code, linear_img,
            user_kong1, my_coupurl, cash_card, cash_payable;
    private RelativeLayout user_profit, user_vip, user_vip2;
    private ImageView user_avatar, user_img, user_touxiang;
    private View user_title2, user_title, user_marketing;

    private View user_operator, user_withdraw;

    private AvatarPresenter mPresenter = new AvatarPresenter(this);
    private String bannerImg, bannerLink, code;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_layout, container, false);
        registerBroadcastReceiver();

        initView(root);
        initBar();
        initRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final IAccountManager accountManager =
                        (IAccountManager) ComponentManager.getInstance()
                        .getManager(IAccountManager.class.getName());
                LoginRequestManager.requestGetuserinfo(accountManager.getAccountInfo().getApiToken());
                mPresenter.requestHomeupdate();
                initBar();

            }
        });
        return root;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        LZLog.i(TAG, "onFirstUserVisible-=");
        StatusBarUtils.addTranslucentColorView(getActivity(), Color.TRANSPARENT, 0);
        ImmersionBar.with(this)
                .navigationBarColorInt(Color.WHITE)
                .navigationBarDarkIcon(true)
                .keyboardEnable(true)
                .statusBarDarkFont(false)
                .statusBarColorInt(Color.TRANSPARENT)
                .init();
        initBar();
        AccountManager accountManager2 = LiZhiApplication.getApplication().getAccountManager();
        if (accountManager2.isLogined()) {
            mPresenter.requestHomeupdate();
            linear_img.setVisibility(View.VISIBLE);
        } else {
            linear_img.setVisibility(View.GONE);
        }

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        LZLog.i(TAG, "onUserVisible-=");
        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            int currentTab = ((MainActivity) activity).mTabManager.getCurrentTab();
            if (currentTab == TabFragmentManager.TAB_USER) {
                StatusBarUtils.addTranslucentColorView(getActivity(), Color.TRANSPARENT, 0);
                ImmersionBar.with(this)
                        .navigationBarColorInt(Color.WHITE)
                        .navigationBarDarkIcon(true)
                        .keyboardEnable(true)
                        .statusBarDarkFont(false)
                        .statusBarColorInt(Color.TRANSPARENT)
                        .init();

                initBar();
                //mPresenter.requestHomeupdate();
                AccountManager accountManager2 =
                        LiZhiApplication.getApplication().getAccountManager();
                if (accountManager2.isLogined()) {
                    mPresenter.requestHomeupdate();
                    linear_img.setVisibility(View.VISIBLE);
                } else {
                    linear_img.setVisibility(View.GONE);
                }
            }
        }


    }

    private void initBar() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        AccountManager accountManager2 = LiZhiApplication.getApplication().getAccountManager();
        if (!accountManager2.isLogined()) {
            user_title.setVisibility(View.VISIBLE);
            user_title2.setVisibility(View.GONE);
            user_vip.setVisibility(View.GONE);
            fans_people.setText("****");
            moeny_make.setText("****");
            money_save.setText("****");
            yesterday_money.setText("****");
            month_money.setText("****");
            lastmonth_moeny.setText("****");
            today_money.setText("****");
            user_marketing.setVisibility(View.GONE);


        } else {
            user_title2.setVisibility(View.VISIBLE);
            user_title.setVisibility(View.GONE);
        }

        int roleLevel = accountManager.getAccountInfo().getRoleLevel();//1-普通 2-超级 3-Plus超级 4-运营商
        // 5-plus运营商
        if (roleLevel == 1) {
            user_level.setText("普通会员");
            user_vip2.setVisibility(View.VISIBLE);
            user_vip.setVisibility(View.VISIBLE);
            user_marketing.setVisibility(View.GONE);


        } else if (roleLevel == 2) {
            user_level.setText("超级会员");
            user_vip2.setVisibility(View.VISIBLE);
            user_vip.setVisibility(View.VISIBLE);
            user_marketing.setVisibility(View.GONE);


        } else if (roleLevel == 3) {
            user_level.setText("plus超级会员");
            user_vip2.setVisibility(View.VISIBLE);
            user_vip.setVisibility(View.VISIBLE);
            user_marketing.setVisibility(View.GONE);


        } else if (roleLevel == 4) {
            user_level.setText("运营商");
            user_vip2.setVisibility(View.GONE);
            user_vip.setVisibility(View.GONE);
            user_marketing.setVisibility(View.VISIBLE);


        } else if (roleLevel == 5) {
            user_level.setText("plus运营商");
            user_vip2.setVisibility(View.GONE);
            user_vip.setVisibility(View.GONE);
            user_marketing.setVisibility(View.VISIBLE);

        }

        String avatar = accountManager.getAccountInfo().getAvatar();
        Glide.with(this).load(avatar).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(user_avatar);

        String superiorId = accountManager.getAccountInfo().getSuperiorId();
        code = accountManager.getAccountInfo().getCode();
        String name = accountManager.getAccountInfo().getName();
        user_name.setText(name);
        if (TextUtils.isEmpty(superiorId)) {//判断是否有上级id
            user_invate.setVisibility(View.VISIBLE);
            user_code.setVisibility(View.GONE);
        } else {
            user_code.setVisibility(View.VISIBLE);
            user_invate.setVisibility(View.GONE);
            invate_code.setText("邀请ID：" + code);
        }
    }

    /**
     * @param texting 复制文本
     */
    private void copy(CharSequence texting) {
        CharSequence text = texting;
        DeviceUtil.putClipboardText(getActivity(), text);
        CommonToast.showToast("已复制");
    }

    private void initView(View root) {
        user_login = root.findViewById(R.id.user_login);
        // user_login.setOnClickListener(this);//立即登录
        user_vip = root.findViewById(R.id.user_vip);
        user_vip.setOnClickListener(this);//跳转vip页面
        user_vip2 = root.findViewById(R.id.user_vip2);
        user_vip2.setOnClickListener(this);//跳转vip页面
        save_money = root.findViewById(R.id.save_money);
        save_money.setOnClickListener(this);//跳转省钱页面
        make_money = root.findViewById(R.id.make_money);
        make_money.setOnClickListener(this);//跳转赚钱页面
        accument_fans = root.findViewById(R.id.accument_fans);
        accument_fans.setOnClickListener(this);//跳转粉丝页面
        user_profit = root.findViewById(R.id.user_profit);
        user_profit.setOnClickListener(this);//跳转收益页面
        user_order = root.findViewById(R.id.user_order);
        user_order.setOnClickListener(this);//订单
        my_fans = root.findViewById(R.id.my_fans);
        my_fans.setOnClickListener(this);//粉丝
        my_invate = root.findViewById(R.id.my_invate);
        my_invate.setOnClickListener(this);//邀请好友
        my_vip = root.findViewById(R.id.my_vip);
        my_vip.setOnClickListener(this);//邀请好友

        my_shopcar = root.findViewById(R.id.my_shopcar);
        my_shopcar.setOnClickListener(this);//购物车
        my_foot = root.findViewById(R.id.my_foot);
        my_foot.setOnClickListener(this);//足迹
        my_about = root.findViewById(R.id.my_about);
        my_about.setOnClickListener(this);//关于我们
        my_customer = root.findViewById(R.id.my_customer);
        my_customer.setOnClickListener(this);//客服
        new_tutorial = root.findViewById(R.id.new_tutorial);
        new_tutorial.setOnClickListener(this);//新手教程
        my_setting = root.findViewById(R.id.my_setting);
        my_setting.setOnClickListener(this);//设置
        user_avatar = root.findViewById(R.id.user_avatar);//头像设置
        user_name = root.findViewById(R.id.user_name);//会员名称
        user_level = root.findViewById(R.id.user_level);//会员等级
        user_invate = root.findViewById(R.id.user_invate);//邀请好友弹框
        user_invate.setOnClickListener(this);
        user_code = root.findViewById(R.id.user_code);//邀请码
        invate_code = root.findViewById(R.id.invate_code);//获取邀请码
        user_code.setOnClickListener(this);
        user_title = root.findViewById(R.id.user_title);
        user_title.setOnClickListener(this);
        user_title2 = root.findViewById(R.id.user_title2);
        fans_people = root.findViewById(R.id.fans_people);//累计粉丝
        moeny_make = root.findViewById(R.id.moeny_make);//累计赚钱
        money_save = root.findViewById(R.id.money_save);//累计省钱
        lastmonth_moeny = root.findViewById(R.id.lastmonth_moeny);//上月预估
        month_money = root.findViewById(R.id.month_moeny);//本月预估
        yesterday_money = root.findViewById(R.id.yesterday_moeny);//昨日预估
        today_money = root.findViewById(R.id.today_moeny);//今日预估
        user_img = root.findViewById(R.id.user_img);
        user_img.setOnClickListener(this);
        user_touxiang = root.findViewById(R.id.user_touxiang);//头像
        user_touxiang.setOnClickListener(this);
        user_avatar.setOnClickListener(this);
        linear_img = root.findViewById(R.id.linear_img);
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);//刷新
        my_materiel = root.findViewById(R.id.my_materiel);
        my_materiel.setOnClickListener(this);//荔枝物料

        user_kong1 = root.findViewById(R.id.user_kong1);//推送编辑
        user_kong1.setOnClickListener(this);

        my_coupurl = root.findViewById(R.id.my_coupurl);//优惠券配置
        my_coupurl.setOnClickListener(this);

        user_operator = root.findViewById(R.id.user_operator);
        user_withdraw = root.findViewById(R.id.user_withdraw);

        user_marketing = root.findViewById(R.id.user_marketing);
        cash_payable = root.findViewById(R.id.cash_payable);//抵扣卡券
        cash_payable.setOnClickListener(this);
        cash_card = root.findViewById(R.id.cash_card);//抵扣礼金
        cash_card.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        final IAccountManager accountManager =
                (IAccountManager) ComponentManager.getInstance()
                        .getManager(IAccountManager.class.getName());
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.cash_card://抵扣礼金
                if (shouldLoginFirst()) {
                    CommonWebJump.showCommonWebActivity(getActivity(),
                            WebUrlManager.getLitchitaosUrl());
                }
                break;
            case R.id.cash_payable://抵扣卡券
                if (shouldLoginFirst()) {
                    CommonWebJump.showCommonWebActivity(getActivity(),
                            WebUrlManager.getLitchiCouponUrl());
                }
                break;
            case R.id.user_title:
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showLoginActivity(mContext);
                }
                break;
            case R.id.user_vip:
                CommonSchemeJump.showMainActivity(getActivity(),
                        TabFragmentManager.MAIN_VIP_TAB_NAME, 0, 0);
                break;
            case R.id.user_vip2:
                CommonSchemeJump.showMainActivity(getActivity(),
                        TabFragmentManager.MAIN_VIP_TAB_NAME, 0, 0);
                break;
            case R.id.save_money:
                //累计省钱
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(mContext, "/app/SaveMoneyActivity");
                }

                break;
            case R.id.make_money:
                //累计赚钱（收益记录）
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(mContext, "/app/IncomeRecordActivity");
                }
                break;
            case R.id.accument_fans:
                //累计粉丝
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(mContext, "/app/FansMainActivity");
                }


                break;
            case R.id.user_profit:
                //收益记录
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(mContext, "/app/IncomeRecordActivity");
                }

                break;
            case R.id.user_order:
                //订单查询 找回
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(mContext, "/app/OrderActivity");
                }

                break;
            case R.id.my_fans:
                //粉丝
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(mContext, "/app/FansMainActivity");
                }


                break;
            case R.id.my_invate:
                //邀请好友
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(getActivity(), "/app/InvateActivity");
                }

                break;
            case R.id.my_vip:
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(getActivity(), "/app/UpgrademembershipActivity");
                }
                break;
            case R.id.my_shopcar:
                //购物车
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showShoppingCartActivity(mContext);
                }

                break;

            case R.id.my_foot:
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showFootprintActivity(mContext);
                }
                break;


            case R.id.my_about:
                CommonSchemeJump.showActivity(getActivity(), "/app/AboutUsActivity");
                break;

            case R.id.my_customer:
                CommonWebJump.showCommonWebActivity(getActivity(),
                        WebUrlManager.getUserContactHelpUrl());
                break;

            case R.id.new_tutorial:
                CommonWebJump.showCommonWebActivity(getActivity(),
                        WebUrlManager.getNewsUserTutorialUrl());
                break;

            case R.id.my_setting:
                int roleLevel = accountManager.getAccountInfo().getRoleLevel();
                if (shouldLoginFirst()) {
                    if (roleLevel == 4 || roleLevel == 5) {
                        bundle.putString(CommonConst.WITHDRAW_OPERATOR,
                                CommonConst.KEY_OPERATE_NAME);
                    }
                    CommonSchemeJump.showSettingActivity(getActivity(), bundle);
                }
                break;
            case R.id.user_invate:
                //填写邀请码
                DiaLogManager.showInvitationCodeDialog(mContext, getChildFragmentManager());
                break;
            case R.id.user_img:
                final ISchemeManager scheme =
                        (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                scheme.handleUrl(mContext, bannerLink);
                break;
            case R.id.user_touxiang:
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(getActivity(), "/app/SettingActivity");
                }
                break;
            case R.id.user_avatar:
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(getActivity(), "/app/SettingActivity");
                }
                break;
            case R.id.user_code:
                copy(code);
                break;
            case R.id.my_materiel://荔枝物料
                CommonWebJump.showCommonWebActivity(getActivity(),
                        WebUrlManager.getLitchiMaterialsUrl());
                break;

            case R.id.user_kong1://推送管理
                CommonSchemeJump.showPushEditActivity(getActivity(), null);
                break;
            case R.id.my_coupurl://优惠券配置
                CommonWebJump.showCommonWebActivity(getActivity(),
                        WebUrlManager.getUserCoupurlUrl());
                break;
        }

    }


    private boolean shouldLoginFirst() {
        AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        if (!accountManager.isLogined()) {
            CommonSchemeJump.showLoginActivity(mContext);
            return false;
        } else {
            return true;
        }
    }

    private TabFragmentUser.LocalBroadcastReceiver mReceiver;

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new TabFragmentUser.LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SettingConst.ACTION_LOGIN_SUCCESS);
            intentFilter.addAction(SettingConst.ACTION_LOGOUT_SUCCESS);
            LocalBroadcastManager.getInstance(LiZhiApplication.getApplication()).registerReceiver(mReceiver, intentFilter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(LiZhiApplication.getApplication()).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void requestAvatorSuccess(String bean) {

    }

    @Override
    public void requestAvatorError(Throwable throwable) {

    }

    @Override
    public void requestUserHomeSuccess(UserHomeBean bean) {
        if (getActivity() instanceof MainActivity && !((MainActivity) getActivity()).isDestroy() && isAdded()) {
            mSwipeRefreshLayout.setRefreshing(false);
            initUserHome(bean);
        }
    }

    private void initUserHome(UserHomeBean bean) {
        fans_people.setText(bean.getAllFans() + "");
        moeny_make.setText(StringUtils.getFormattedDouble(bean.getAllIncome()));
        money_save.setText(StringUtils.getFormattedDouble(bean.getAllSave()));
        yesterday_money.setText(StringUtils.getFormattedDouble(bean.getLastDayEstimate()));
        month_money.setText(StringUtils.getFormattedDouble(bean.getNowMonthEstimate()));
        lastmonth_moeny.setText(StringUtils.getFormattedDouble(bean.getLastMonthEstimate()));
        today_money.setText(StringUtils.getFormattedDouble(bean.getNowDayEstimate()));
        bannerImg = bean.getBannerImg();
        bannerLink = bean.getBannerLink();
        Glide.with(this).load(bannerImg).into(user_img);
        linear_img.setVisibility(TextUtils.isEmpty(bannerImg) ? View.GONE : View.VISIBLE);


    }

    @Override
    public void requestUserHomeError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_LOGIN_SUCCESS.equals(action)) {
                sendVipInfoRefresh();
            } else if (SettingConst.ACTION_LOGOUT_SUCCESS.equals(action)) {
                sendVipInfoRefresh();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcastReceiver();
    }

    private void sendVipInfoRefresh() {
        initBar();
    }
}
