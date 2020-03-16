package com.by.lizhiyoupin.app.detail.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.component_banner.Banner;
import com.by.lizhiyoupin.app.component_banner.BannerConfig;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.component_ui.weight.BannerImageLoader;
import com.by.lizhiyoupin.app.component_umeng.share.manager.ShareManager;
import com.by.lizhiyoupin.app.detail.contract.VipProductDetailContract;
import com.by.lizhiyoupin.app.detail.presenter.VipProductDetailPresenter;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodsBean;
import com.by.lizhiyoupin.app.main.adapter.VipImageAdapter;
import com.by.lizhiyoupin.app.manager.AccountManager;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.by.lizhiyoupin.app.weight.SpanImageSpan;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/6 13:32
 * Summary: vip会员 本地商品详情页
 */
@Route(path = "/app/VipProductDetailActivity")
public class VipProductDetailActivity extends BaseActivity implements View.OnClickListener, VipProductDetailContract.VipProductDetailView {
    public static final String TAG = VipProductDetailActivity.class.getSimpleName();
    private TextView mBackTv;
    private View mActionBarRl;
    private NestedScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private VipImageAdapter mVipImageAdapter;
    private Banner mBanner;
    private ImageView mBannerBottomIv;//banner底部的图
    private TextView mPriceTv;//商品价格
    private TextView mProductTitleTv;//商品标题
    private TextView mProductIntroTv;//商品介绍
    private ImageView mThreePacksIv;//三包
    private ImageView mVipCallIv;//客服
    private TextView mVipBuyTv;//single购买
    private TextView mVipBuyLeftTv;//推广奖
    private TextView mVipBuyRightTv;//立即购买
    private ImageView mVipGoTopTv;//滑到顶部
    private Group mGroup2;//非普通用户显示
    private VipProductDetailContract.VipProductDetailPresenters mPresenters;
    private long detailId = 0;
    private int activityType = -1;
    private VipGoodsBean mVipGoodsBean;
    private String url="http://shizi.lizhiyoupin.com/goods/login?invite_code=";

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DeviceUtil.fullScreen(this, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_product_detail_layout);
        StatusBarUtils.setColor(this, Color.WHITE,0);
        Intent intent = getIntent();
        detailId = intent.getLongExtra(CommonConst.KEY_NATIVE_DETAIL_ID, 0);
        activityType = intent.getIntExtra(CommonConst.KEY_ACTIVITY_FROM_PRODUCT_TYPE, -1);
        mPresenters = new VipProductDetailPresenter(this);
        initView();
        mPresenters.requestVipGoodsDetail(detailId,activityType);
    }

    private void initView() {

        mActionBarRl = findViewById(R.id.actionbar_rl);
        mBackTv = findViewById(R.id.actionbar_back_tv);
        TextView mTitle = findViewById(R.id.actionbar_title_tv);
        findViewById(R.id.actionbar_right_tv).setVisibility(View.GONE);
        mScrollView = findViewById(R.id.vip_nestScrollView);
        mBanner = findViewById(R.id.vip_detail_banner);
        mBannerBottomIv = findViewById(R.id.banner_bottom_iv);
        mPriceTv = findViewById(R.id.vip_detail_price_tv);
        mProductTitleTv = findViewById(R.id.vip_detail_title_tv);
        mProductIntroTv = findViewById(R.id.vip_detail_intro_tv);
        mThreePacksIv = findViewById(R.id.vip_detail_three_packs_iv);
        mRecyclerView = findViewById(R.id.vip_detail_recyclerView);
        mVipCallIv = findViewById(R.id.vip_detail_call_iv);
        mVipBuyTv = findViewById(R.id.vip_detail_buy_Tv);
        mGroup2 = findViewById(R.id.group_2);
        mVipBuyLeftTv = findViewById(R.id.vip_detail_buy_left_Tv);
        mVipBuyRightTv = findViewById(R.id.vip_detail_buy_right_Tv);
        mVipGoTopTv = findViewById(R.id.vip_detail_go_top_tv);

        mActionBarRl.setBackgroundColor(getResources().getColor(R.color.white_100));
        mBackTv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.common_back), null, null, null);
        mTitle.setText("商品详情");
        mBackTv.setText("");
        mBackTv.setOnClickListener(this);
        mVipCallIv.setOnClickListener(this);
        mVipBuyTv.setOnClickListener(this);
        mVipBuyLeftTv.setOnClickListener(this);
        mVipBuyRightTv.setOnClickListener(this);
        mVipGoTopTv.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVipImageAdapter = new VipImageAdapter(this);
        mRecyclerView.setAdapter(mVipImageAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        setBannerConfig();
    }

    /**
     * 设置banner
     */
    private void setBannerConfig() {
        mBanner.setImageLoader(new BannerImageLoader())
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setBannerStyle(BannerConfig.NUM_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.vip_detail_call_iv:
                //客服
                CommonWebJump.showCommonWebActivity(this, WebUrlManager.getUserContactHelpUrl());
                break;
            case R.id.vip_detail_buy_Tv:
                //single购买
                placeAnOrder();
                break;
            case R.id.vip_detail_buy_left_Tv:
                //推广
                UserInfoBean accountInfo = LiZhiApplication.getApplication().getAccountManager().getAccountInfo();
                ShareManager.requestShareBean(this, 0, detailId, accountInfo.getId(),accountInfo.getCode());
                break;
            case R.id.vip_detail_buy_right_Tv:
                //购买
                placeAnOrder();
                break;
            case R.id.vip_detail_go_top_tv:
                mScrollView.scrollTo(0, 0);
                break;
        }
    }

    /**
     * 下单购买
     */
    private void placeAnOrder() {
        if (mVipGoodsBean == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.KEY_NATIVE_DETAIL_ID, detailId);
        bundle.putString(CommonConst.KEY_ORDER_PRODUCT_TITLE, mVipGoodsBean.getTitle());
        bundle.putInt(CommonConst.KEY_ORDER_PRODUCT_NUMBER, 1);
        bundle.putDouble(CommonConst.KEY_ORDER_PRODUCT_PRICE, mVipGoodsBean.getPrice());
        bundle.putString(CommonConst.KEY_ORDER_PRODUCT_PICTURE, mVipGoodsBean.getMainImg());
        CommonSchemeJump.showPayOrderActivity(this, bundle);
    }


    @Override
    public void requestVipGoodsDetailSuccess(VipGoodsBean bean) {
        if (bean == null) {
            return;
        }
        LZLog.i(TAG, "requestVipGoodsDetailSuccess");
        mVipGoodsBean = bean;
        List<String> skatingImgList = bean.getSkatingImgList();
        if (skatingImgList == null) {
            skatingImgList = new ArrayList<>();
        }
        String mainImg = bean.getMainImg();
        if (!TextUtils.isEmpty(mainImg)) {
            skatingImgList.add(0, mainImg);
        }
        mBanner.update(skatingImgList);
        mBanner.startAutoPlay();

        ViewUtil.setMoneyText(this, mPriceTv, "¥" + StringUtils.getFormattedDoubleOrInt(bean.getPrice()),
                R.style.product_money18_E41B26, R.style.product_money33_E41B26);
        mProductTitleTv.setText(getContentTagSpannable(bean.getTitle(), "优选"));
        mProductIntroTv.setText(bean.getDescribe());
        mVipImageAdapter.setListData(bean.getDetailImgList());
        mVipImageAdapter.notifyDataSetChanged();
        AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        UserInfoBean accountInfo = accountManager.getAccountInfo();
        int roleLevel = accountInfo.getRoleLevel();
        if (roleLevel == 1) {
            //普通用户显示 立即购买
            mGroup2.setVisibility(View.GONE);
            mVipBuyTv.setVisibility(View.VISIBLE);
        } else {
            //其他用户 显示推广奖和立即购买
            mVipBuyTv.setVisibility(View.GONE);
            mGroup2.setVisibility(View.VISIBLE);
            mPresenters.setLevelData(this, mVipBuyLeftTv, roleLevel);
        }
    }

    @Override
    public void requestVipGoodsDetailError(Throwable throwable) {
        LZLog.i(TAG, "requestVipGoodsDetailError" + throwable);
    }


    /**
     * 图文混排
     *
     * @param content 内容
     * @param tag     需要边图片的文字
     * @return
     */
    private CharSequence getContentTagSpannable(String content, String tag) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(tag)) {
            return content;
        }
        SpannableString spannableString = new SpannableString(tag + " " + content);
        TextView tagTv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_textview_layout, null);
        tagTv.setText(tag);
        Bitmap bitmap = ViewUtil.convertViewToBitmap(tagTv);
        Drawable d = new BitmapDrawable(bitmap);
        d.setBounds(0, 0, tagTv.getWidth(), tagTv.getHeight());
        ImageSpan span = new SpanImageSpan(d, SpanImageSpan.ALIGN_CENTER);
        spannableString.setSpan(span, 0, tag.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}
