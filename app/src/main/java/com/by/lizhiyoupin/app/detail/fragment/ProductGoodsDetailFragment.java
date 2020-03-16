package com.by.lizhiyoupin.app.detail.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ResponseCallback;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.PackageUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_banner.Banner;
import com.by.lizhiyoupin.app.component_banner.BannerConfig;
import com.by.lizhiyoupin.app.component_sdk.AliSdkManager;
import com.by.lizhiyoupin.app.component_sdk.kaipule.KaipuleUtils;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.component_ui.weight.BannerImageLoader;
import com.by.lizhiyoupin.app.io.bean.AuthorizationBean;
import com.by.lizhiyoupin.app.io.bean.HandPickDetailBean;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.main.manager.TabFragmentManager;
import com.by.lizhiyoupin.app.manager.AccountManager;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/10 11:40
 * Summary: 商品详情 顶部页 {@see #PreciseDetailActivity}
 */
public class ProductGoodsDetailFragment extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {
    public static final String TAG = ProductGoodsDetailFragment.class.getSimpleName();
    private Banner mTopBanner;
    private View mTopCl;
    private TextView mTopPriceTv;//￥99
    private TextView mTopMonthlySalesTv;//月销1215
    private TextView mTopRedPacketTv;//返红包12元
    //升级布局
    private View mTopUpgradeTipsRl;
    private TextView mTopUpgradeTipsTv;//升级会员提示
    private TextView mTopUpgradeNowTv;//立即升级
    //商品名称
    private TextView mTopTradeNameTv;
    //优惠券布局
    private View mTopCouponRl;
    private TextView mTopCouponPriceTv;//优惠券布局
    private TextView mTopCouponTimeLimitTv;//优惠券 使用期限
    private TextView mTopCouponReceiveTv;//优惠券立即领取
    private Context mContext;
    private HandPickDetailBean mHandpickDetail;
    private String mVipUpdateUrl;//会员升级
    private List<String> mBannerList;
    private boolean isLoadingAuto = false;//是否在授权


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_goods_detail_layout, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        mBannerList = new ArrayList<>(5);
        mTopBanner = root.findViewById(R.id.product_detail_banner);
        mTopCl = root.findViewById(R.id.product_detail_top_cl);
        mTopPriceTv = root.findViewById(R.id.product_detail_top_price_tv);
        mTopMonthlySalesTv = root.findViewById(R.id.product_detail_top_monthly_sales_tv);
        mTopRedPacketTv = root.findViewById(R.id.product_detail_top_red_packet_tv);
        mTopUpgradeTipsRl = root.findViewById(R.id.product_detail_top_upgrade_tips_rl);
        mTopUpgradeTipsTv = root.findViewById(R.id.product_detail_top_upgrade_tips_tv);
        mTopUpgradeNowTv = root.findViewById(R.id.product_detail_top_upgrade_now_tv);
        mTopTradeNameTv = root.findViewById(R.id.product_detail_top_trade_name_tv);
        mTopCouponRl = root.findViewById(R.id.product_detail_coupon_rl);
        mTopCouponPriceTv = root.findViewById(R.id.product_detail_coupon_price_tv);
        mTopCouponTimeLimitTv = root.findViewById(R.id.product_detail_coupon_time_limit_tv);
        mTopCouponReceiveTv = root.findViewById(R.id.product_detail_coupon_receive_tv);

        mTopUpgradeNowTv.setOnClickListener(this);
        mTopCouponReceiveTv.setOnClickListener(this);
        mTopTradeNameTv.setOnLongClickListener(this);
        setBannerConfig();
    }

    private void setBannerConfig() {
        mTopBanner.setImageLoader(new BannerImageLoader())
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setBannerStyle(BannerConfig.NUM_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }

    private void updateBanner() {
        String pictUrl = mHandpickDetail.getPictUrl();
        List<String> smallImages = mHandpickDetail.getSmallImages();
        mBannerList.add(pictUrl);
        if (!ArraysUtils.isListEmpty(smallImages)) {
            mBannerList.addAll(smallImages);
        }
        mTopBanner.update(mBannerList);
        mTopBanner.startAutoPlay();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_detail_coupon_receive_tv:
                //优惠券立即领取
                if (!shouldLoginFirst()) {
                    return;
                }
                if (mHandpickDetail != null && !isLoadingAuto) {
                    LZLog.i(TAG, "优惠券立即领取");
                    if (mHandpickDetail.getIcon() == CommonConst.PLATFORM_TAO_BAO || mHandpickDetail.getIcon() == CommonConst.PLATFORM_TIAN_MAO) {
                        //淘宝
                        showAutoThree();
                    } else if (mHandpickDetail.getIcon() == CommonConst.PLATFORM_JING_DONG) {
                        //京东
                        if (PackageUtil.checkAppInstalled(mContext, PackageUtil.PackageType.JING_DONG)) {
                            KaipuleUtils.getInstance(mContext).openUrlToApp(mHandpickDetail.getCouponClickUrl());
                        } else {
                            CommonWebJump.showInterceptOtherWebActivity(mContext, mHandpickDetail.getCouponClickUrl());
                        }
                    } else if (mHandpickDetail.getIcon() == CommonConst.PLATFORM_PIN_DUO_DUO) {
                        //拼多多
                        if (PackageUtil.checkAppInstalled(mContext,PackageUtil.PackageType.PIN_DUO_DUO)){
                            //拼多多
                            CommonWebJump.showOtherAppWebView(mContext, mHandpickDetail.getCouponClickUrl());
                        }else{
                            ToastUtils.show("手机暂未安装拼多多");
                        }

                    } else {
                        //考拉
                        CommonWebJump.showCommonWebActivity(mContext, mHandpickDetail.getCouponClickUrl());
                    }
                }
                break;
            case R.id.product_detail_top_upgrade_now_tv:
                //立即升级会员,直接跳转到 vip页
                if (mHandpickDetail != null) {
                    CommonSchemeJump.showMainActivity(mContext, TabFragmentManager.MAIN_VIP_TAB_NAME, 0, 0);
                }
                break;

        }
    }


    @Override
    public boolean onLongClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return true;
        }
        switch (v.getId()) {
            case R.id.product_detail_top_trade_name_tv:
                DeviceUtil.putClipboardText(getActivity(), mTopTradeNameTv.getText());
                CommonToast.showToast("复制成功");
                break;
        }
        return true;
    }

    /**
     * 是否登录
     *
     * @return
     */
    private boolean shouldLoginFirst() {
        AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        if (!accountManager.isLogined()) {
            CommonSchemeJump.showLoginActivityForResult(getActivity(), CommonConst.LOGIN_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 跳转授权或三方页面
     */
    public void showAutoThree() {
        isLoadingAuto = true;
        AccountManager.setAuthorizeUrl(mHandpickDetail.getCouponClickUrl(), new ResponseCallback<AuthorizationBean>() {
            @Override
            public void success(AuthorizationBean authorizationBean) {
                isLoadingAuto = false;
                if (authorizationBean==null){
                    LZLog.w(TAG,"授权返回数据未空");
                    return;
                }
                String authorizationUrl = authorizationBean.getAuthorizationUrl();
                final Uri uri = Uri.parse(authorizationUrl);
                String queryParameter = uri.getQueryParameter("relationId");
                if (!TextUtils.isEmpty(queryParameter)) {

                    if (PackageUtil.checkAppInstalled(mContext, PackageUtil.PackageType.TAO_BAO) || PackageUtil.checkAppInstalled(mContext, PackageUtil.PackageType.TIAN_MAO)) {
                        LZLog.i(TAG, "直接跳转淘宝");
                        AliSdkManager.showGoods(getActivity(), authorizationUrl, null);
                    } else {
                        LZLog.i(TAG, "跳转淘宝h5");
                        CommonWebJump.showInterceptOtherWebActivity(mContext,authorizationUrl);
                    }

                } else {
                    LZLog.i(TAG, "淘宝授权");
                    DiaLogManager.showAuthDialog(mContext, getChildFragmentManager(), authorizationUrl);
                }
            }

            @Override
            public void error(Throwable throwable) {
                isLoadingAuto = false;
                LZLog.w(TAG, "setAuthorizeUrl error==" + throwable);
            }
        });
    }

    public void updateProduct(HandPickDetailBean handpickDetail) {
        mHandpickDetail = handpickDetail;
        updateBanner();
        ViewUtil.setMoneyText(mContext, mTopPriceTv, "¥" + StringUtils.getFormattedDoubleOrInt(handpickDetail.getDiscountsPriceAfter()),
                R.style.product_money17, R.style.product_money26);


        ViewUtil.setTextViewFormat(mContext, mTopMonthlySalesTv,
                R.string.product_monthly_sales_text,
                ViewUtil.getIVolume(handpickDetail.getVolume()));
        ViewUtil.setTextViewFormat(mContext, mTopRedPacketTv,
                R.string.product_red_packet_text,
                StringUtils.getFormattedDoubleOrInt(handpickDetail.getCommissionMoney()));

        if (handpickDetail.getCouponAmount() <= 0) {
            mTopCouponRl.setVisibility(View.GONE);
        } else {
            mTopCouponRl.setVisibility(View.VISIBLE);
            ViewUtil.setTextViewFormat(mContext, mTopCouponTimeLimitTv, R.string.product_detail_limit_time_text,
                    TimeUtils.transDateToDateString(handpickDetail.getCouponStartTime(),"yyyy-MM-dd",7),
                    TimeUtils.transDateToDateString(handpickDetail.getCouponEndTime(),"yyyy-MM-dd",7));
            ViewUtil.setMoneyText(mContext, mTopCouponPriceTv, "¥" + StringUtils.getFormattedDoubleOrInt(handpickDetail.getCouponAmount()),
                    R.style.product_money20, R.style.product_money26_C4171E);
        }


        ViewUtil.setTitleLeftDrawableSpan(mContext, mTopTradeNameTv, "  " + handpickDetail.getTitle(),
                 ViewUtil.getIconImg(handpickDetail.getIcon()), 17);


    }


    /**
     * 升级会员
     *
     * @param vipList
     */
    public void updateVip(List<String> vipList) {
        UserInfoBean accountInfo = LiZhiApplication.getApplication().getAccountManager().getAccountInfo();
        if (accountInfo != null && accountInfo.getRoleLevel() < 4) {
            if (vipList != null && vipList.size() >= 2) {
                mTopUpgradeTipsRl.setVisibility(View.VISIBLE);
                mTopUpgradeTipsTv.setText(vipList.get(0));
                mVipUpdateUrl = vipList.get(1);
            } else {
                mTopUpgradeTipsRl.setVisibility(View.GONE);
            }
        } else {
            mTopUpgradeTipsRl.setVisibility(View.GONE);
        }

    }


}
