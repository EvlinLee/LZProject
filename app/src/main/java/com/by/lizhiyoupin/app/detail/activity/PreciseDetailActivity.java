package com.by.lizhiyoupin.app.detail.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ResponseCallback;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.PackageUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_sdk.AliSdkManager;
import com.by.lizhiyoupin.app.component_sdk.kaipule.KaipuleUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.detail.contract.PreciseDetailContract;
import com.by.lizhiyoupin.app.detail.fragment.ProductBabyDetailFragment;
import com.by.lizhiyoupin.app.detail.fragment.ProductDetailRecommendFragment;
import com.by.lizhiyoupin.app.detail.fragment.ProductGoodsDetailFragment;
import com.by.lizhiyoupin.app.detail.fragment.ProductGuessYouLikeListFragment;
import com.by.lizhiyoupin.app.detail.presenter.PreciseDetailPresenter;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.LzTransformationUtil;
import com.by.lizhiyoupin.app.io.bean.AuthorizationBean;
import com.by.lizhiyoupin.app.io.bean.HandPickDetailBean;
import com.by.lizhiyoupin.app.io.entity.PreciseDetailEntity;
import com.by.lizhiyoupin.app.io.entity.RequestButtonRecommend;
import com.by.lizhiyoupin.app.io.entity.RequestShoppingCartEntity;
import com.by.lizhiyoupin.app.main.manager.TabFragmentManager;
import com.by.lizhiyoupin.app.manager.AccountManager;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.message_box.listener.ButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/8 19:04
 * Summary: 三方接口的---商品详情页
 */
@Route(path = "/app/PreciseDetailActivity")
public class PreciseDetailActivity extends BaseActivity implements View.OnClickListener,
        PreciseDetailContract.PreciseDetailView, Handler.Callback, View.OnTouchListener {
    public static final String TAG = PreciseDetailActivity.class.getSimpleName();
    public static final int DISMISS_DIALOG_CODE = 600;
    public static final int FINISH_ACTIVITY_CODE = 601;
    private View mActionBarAllTop;//包含statusBar
    private View mActionBarTitleRl;//不包含statusBar
    private View mActionBarBack2Iv;
    private View mBackActionBar;
    private View mActionBarBackTv;
    private View mRecommendTopTv;//推荐 按钮
    private View mLoadingView;
    private ImageView mLoadingiV;
    private NestedScrollView mScrollView;
    private TabLayout mTabLayout;
    private TextView mShareAwardTv;//分享奖励的 ￥2.22
    private TextView mBuySaveTv;//购买立省的 ￥120.22
    //商品详情 顶部页
    private ProductGoodsDetailFragment mGoodsDetailFragment;
    //荔枝推荐
    private ProductDetailRecommendFragment mProductDetailRecommendFragment;
    //宝贝详情
    private ProductBabyDetailFragment mProductBabyDetailFragment;
    //猜你喜欢
    private ProductGuessYouLikeListFragment mProductGuessYouLikeListFragment;

    private View mGuessYouLikeContainerFl;
    private View mBabyDetailContainerFl;
    private String mTitles[] = new String[]{"宝贝", "详情", "推荐"};
    private long mThreeDetailId = -1;//商品详情页id
    private int mPlatformType = 0;//平台

    private PreciseDetailPresenter mPresenter;
    private HandPickDetailBean handpickDetail;
    private int lastTagIndex = 0;
    private boolean content2NavigateFlagInnerLock;
    private boolean tagFlag;
    private boolean isLoadingAuto = false;//是否在授权
    private TextView mShoppingCart;
    private List<String> imagsList;//详情介绍图
    private Handler sHandler;
    private View mBabyLine;
    private Integer mfastBuyCommodityType, mbatch;
    private int mActivityType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_layout);
        ImmersionBar.with(this)
                .navigationBarColorInt(Color.WHITE)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .titleBar(findViewById(R.id.action_bar_ll))
                .flymeOSStatusBarFontColorInt(Color.BLACK)
                .statusBarColorInt(Color.TRANSPARENT)
                .init();
        sHandler = new Handler(Looper.getMainLooper(), this);
        Intent intent = getIntent();
        mThreeDetailId = intent.getLongExtra(CommonConst.KEY_THREE_DETAIL_ID, -1);
        mPlatformType = intent.getIntExtra(CommonConst.KEY_PRODUCT_PLATFORM_TYPE, 0);
        mbatch = intent.getIntExtra(CommonConst.KEY_LIMIT_TIME_TYPE, -1);
        mfastBuyCommodityType = intent.getIntExtra(CommonConst.KEY_LIMIT_PRODUCT_TYPE, -1);
        mActivityType = intent.getIntExtra(CommonConst.KEY_ACTIVITY_FROM_PRODUCT_TYPE, -1);

        mPresenter = new PreciseDetailPresenter(this);
        initView();
        initProductGoodsDetailFragment();
        initProductDetailRecommendFragment();
        initProductBabyDetailFragment();
        initGuessYouLikeFragment();

        mPresenter.requestThreePartyProductDetail(mThreeDetailId, mPlatformType, mbatch, mfastBuyCommodityType,mActivityType);
        if (mPlatformType == CommonConst.PLATFORM_TAO_BAO || mPlatformType == CommonConst.PLATFORM_TIAN_MAO) {
            //淘宝天猫需要， 但单独去取详情图
            mPresenter.requestThreePartyProductDecPic(mThreeDetailId, mPlatformType);
        }
        showRecommendTopView();
    }


    private void initView() {
        mBabyLine = findViewById(R.id.baby_line_view);
        mRecommendTopTv = findViewById(R.id.detail_recommend_top_tv);
        mGuessYouLikeContainerFl = findViewById(R.id.detail_guess_you_like_container_fl);
        mBabyDetailContainerFl = findViewById(R.id.product_detail_baby_detail_container_fl);
        mScrollView = findViewById(R.id.nestScrollView);
        mLoadingView = findViewById(R.id.loading_view);
        mLoadingiV = findViewById(R.id.loading_gif_iv);
        Glide.with(this).load(R.drawable.loading_dialog_g).into(mLoadingiV);
        mActionBarAllTop = findViewById(R.id.action_bar_ll);
        mActionBarTitleRl = findViewById(R.id.action_bar_rl);
        mActionBarBackTv = findViewById(R.id.actionbar_back_tv);
        mActionBarBack2Iv = findViewById(R.id.actionbar_back2_tv);
        mBackActionBar = findViewById(R.id.start_back_actionbar);
        mTabLayout = findViewById(R.id.detail_tabLayout);
        mShareAwardTv = findViewById(R.id.product_share_award_tv);
        mBuySaveTv = findViewById(R.id.product_buy_save_price_tv);
        mActionBarBackTv.setOnClickListener(this);
        mActionBarBack2Iv.setOnClickListener(this);
        findViewById(R.id.product_detail_home_tv).setOnClickListener(this);
        mShoppingCart = findViewById(R.id.product_detail_shopping_tv);
        mShoppingCart.setOnClickListener(this);
        findViewById(R.id.product_share_award_ll).setOnClickListener(this);
        findViewById(R.id.product_buy_save_money_ll).setOnClickListener(this);
        mActionBarAllTop.setBackgroundColor(ColorUtils.setAlphaComponent(Color.WHITE, 0));
        mRecommendTopTv.setOnClickListener(this);
        final int topBannerHeight = DeviceUtil.dip2px(this, 375);
        int barHeight = mActionBarAllTop.getHeight();
        mScrollView.setOnTouchListener(this);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY,
                                       int oldScrollX, int oldScrollY) {
                int dY = (int) (scrollY * 1f / topBannerHeight * 255);
                int oldY = (int) (oldScrollY * 1f / topBannerHeight * 255);
                int change = -1;
                if (oldY <= 255 && dY >= 255) {
                    //上滑
                    change = 2;
                } else if (oldY >= 255 && dY <= 255) {
                    //下拉
                    change = 3;
                }

                if (change == -1) {
                    //状态不变
                } else if (change == 2) {
                    LZLog.i(TAG, "状态2上滑");
                    ImmersionBar.with(PreciseDetailActivity.this)
                            .navigationBarColorInt(Color.WHITE)
                            .keyboardEnable(true)
                            .statusBarDarkFont(true)
                            .titleBar(mActionBarAllTop)
                            .flymeOSStatusBarFontColorInt(Color.BLACK)
                            .statusBarColorInt(Color.WHITE)
                            .init();
                    mActionBarAllTop.setBackgroundColor(ColorUtils.setAlphaComponent(Color.WHITE, 255));
                    mBackActionBar.setVisibility(View.GONE);
                    mActionBarTitleRl.setVisibility(View.VISIBLE);
                } else {
                    LZLog.i(TAG, "状态3下拉");
                    mActionBarAllTop.setBackgroundColor(ColorUtils.setAlphaComponent(Color.WHITE, 0));
                    ImmersionBar.with(PreciseDetailActivity.this)
                            .navigationBarColorInt(Color.WHITE)
                            .keyboardEnable(true)
                            .statusBarDarkFont(true)
                            .titleBar(mActionBarAllTop)
                            .flymeOSStatusBarFontColorInt(Color.BLACK)
                            .statusBarColorInt(Color.TRANSPARENT)
                            .init();
                    mBackActionBar.setVisibility(View.VISIBLE);
                    mActionBarTitleRl.setVisibility(View.GONE);
                }

                if (scrollY < barHeight + mBabyDetailContainerFl.getTop()) {
                    refreshContent2NavigationFlag(0);
                } else if (scrollY < barHeight + mGuessYouLikeContainerFl.getTop()) {
                    refreshContent2NavigationFlag(1);
                } else {
                    refreshContent2NavigationFlag(2);
                }
            }
        });

        for (int i = 0; i < 3; i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setTag(i);
            tab.setText(mTitles[i]);
            mTabLayout.addTab(tab);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Integer tag = (Integer) tab.getTag();
                LZLog.i(TAG, "tag Selected==" + tag);
                tagFlag = false;
                setTabSelectMove(tag);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //选中了再次点击触发
                Integer tag = (Integer) tab.getTag();
                LZLog.i(TAG, "tag Reselected==" + tag);
                setTabSelectMove(tag);

            }
        });


    }

    /**
     * 显示隐藏 加入推荐 按钮
     */
    private void showRecommendTopView() {
        AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        if (accountManager.isLogined() && mPlatformType < 4) {
            int roleLevel = accountManager.getAccountInfo().getRoleLevel();
            if (roleLevel == 4 || roleLevel == 5) {
                //只有登录且运营商或plus运营商 切实淘宝京东拼多多天猫
                mRecommendTopTv.setVisibility(View.VISIBLE);
            }
        } else {
            mRecommendTopTv.setVisibility(View.GONE);
        }
    }

    /**
     * 切换顶部标签
     *
     * @param currentTagIndex 当前模块位置
     */
    private void refreshContent2NavigationFlag(int currentTagIndex) {
        // 上一个位置与当前位置不一致是，解锁内部锁，是导航可以发生变化
        if (lastTagIndex != currentTagIndex) {
            content2NavigateFlagInnerLock = false;
        }
        if (!content2NavigateFlagInnerLock) {
            // 锁定内部锁
            content2NavigateFlagInnerLock = true;
            // 动作是由ScrollView触发主导的情况下，导航标签才可以滚动选中
            if (tagFlag) {
                mTabLayout.setScrollPosition(currentTagIndex, 0, true);
            }
        }
        lastTagIndex = currentTagIndex;
    }

    /**
     * 点击tab 定位
     *
     * @param tag
     */
    private void setTabSelectMove(Integer tag) {
        if (tag == null) {
            return;
        }
        switch (tag) {
            case 0://宝贝
                mScrollView.scrollTo(0, 0);
                break;
            case 1://详情
                int top = mBabyDetailContainerFl.getTop();
                int barHeight = mActionBarAllTop.getHeight();
                mScrollView.scrollTo(0, top - barHeight);
                break;
            case 2://推荐
                int topG = mGuessYouLikeContainerFl.getTop();
                int barHeightG = mActionBarAllTop.getHeight();
                mScrollView.scrollTo(0, topG - barHeightG);
                break;
        }
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
            case R.id.actionbar_back2_tv:
                finish();
                break;
            case R.id.product_detail_home_tv:
                //返回首页
                CommonSchemeJump.showMainActivity(this, TabFragmentManager.MAIN_HOME_PAGE_TAB_NAME, 0, 0);
                break;
            case R.id.product_detail_shopping_tv:
                //加入购物车 即收藏

                boolean isLogin = shouldLoginFirst();
                if (isLogin) {
                    if (handpickDetail == null) {
                        MessageToast.showToast(this, "数据未加载完，请稍候");
                        return;
                    }
                    if (handpickDetail.getIsShoppingCar() == 1) {
                        mPresenter.requestDeleteShoppingCartInfo(handpickDetail.getShoppingCarId());
                        addShoppingCar(false);
                    } else {
                        RequestShoppingCartEntity cartEntity =
                                LzTransformationUtil.transformation2ShoppingCart(handpickDetail,
                                        LiZhiApplication.getApplication().getAccountManager().getAccountId());
                        mPresenter.requestAdd2ShoppingCartInfo(cartEntity);
                        addShoppingCar(true);
                    }
                }
                break;
            case R.id.product_share_award_ll:
                //分享奖励
                if (handpickDetail == null) {
                    LZLog.i(TAG, "handpickDetail==null");
                    return;
                }
                boolean isLoginf = shouldLoginFirst();
                if (isLoginf) {
                    if (handpickDetail != null && !isLoadingAuto) {
                        if (handpickDetail.getIcon() == CommonConst.PLATFORM_TAO_BAO || handpickDetail.getIcon() == CommonConst.PLATFORM_TIAN_MAO) {
                            //淘宝
                            showAutoThree(false);
                        } else {
                            shareBundle();
                        }
                    }

                }

                break;
            case R.id.detail_recommend_top_tv:
                //加入首页的达人推荐,设个按钮显示的前提是 运营商级别 同时商品是3台的
                if (handpickDetail == null) {
                    return;
                }
                if (mRecommendTopTv.isSelected()) {
                    //删除
                    mPresenter.requestDeleteButtonRecommendInfo(handpickDetail.getRecommendId());
                } else {
                    //添加
                    RequestButtonRecommend buttonRecommend =
                            LzTransformationUtil.transformation2RequestButtonRecommend(handpickDetail);
                    mPresenter.requestAddButtonRecommendInfo(buttonRecommend);
                }
                mRecommendTopTv.setSelected(!mRecommendTopTv.isSelected());
                DiaLogManager.showToastDialog(this, getSupportFragmentManager(), mRecommendTopTv.isSelected() ?
                                getResources().getString(R.string.detail_recommend_success_text) :
                                getResources().getString(R.string.detail_recommend_cancel_text),
                        mRecommendTopTv.isSelected() ? R.drawable.toast_detail_success : R.drawable.toast_close, Gravity.TOP, false);
                break;
            case R.id.product_buy_save_money_ll:
                //购买立省
                boolean isLogind = shouldLoginFirst();
                if (!isLogind) {
                    return;
                }
                // TODO: 2019/10/24  授权判断，然后跳转到对应的app
                emPower();
                break;
            default:

                break;
        }
    }

    /**
     * 授权
     */
    private void emPower() {
        if (handpickDetail != null && !isLoadingAuto) {
            if (handpickDetail.getIcon() == CommonConst.PLATFORM_TAO_BAO || handpickDetail.getIcon() == CommonConst.PLATFORM_TIAN_MAO) {
                //淘宝
                showAutoThree(true);
            } else if (handpickDetail.getIcon() == CommonConst.PLATFORM_JING_DONG) {
                //京东
                if (PackageUtil.checkAppInstalled(this,
                        PackageUtil.PackageType.JING_DONG)) {
                    String clickUrl = TextUtils.isEmpty(handpickDetail.getCouponClickUrl()) ? handpickDetail.getItemLink() : handpickDetail.getCouponClickUrl();

                    KaipuleUtils.getInstance(this).openUrlToApp(clickUrl);
                } else {
                    CommonWebJump.showInterceptOtherWebActivity(this,
                            handpickDetail.getItemLink());
                }

            } else if (handpickDetail.getIcon() == CommonConst.PLATFORM_PIN_DUO_DUO) {

                if (PackageUtil.checkAppInstalled(this, PackageUtil.PackageType.PIN_DUO_DUO)) {
                    //拼多多
                    String clickUrl = TextUtils.isEmpty(handpickDetail.getCouponClickUrl()) ? handpickDetail.getItemLink() : handpickDetail.getCouponClickUrl();

                    CommonWebJump.showOtherAppWebView(this, clickUrl);
                } else {
                    ToastUtils.show("手机暂未安装拼多多");
                }

            } else {
                //考拉
                String clickUrl = TextUtils.isEmpty(handpickDetail.getCouponClickUrl()) ? handpickDetail.getItemLink() : handpickDetail.getCouponClickUrl();
                CommonWebJump.showCommonWebActivity(this, clickUrl);
            }
        }
    }

    /**
     * @param selected true 变成 已加购
     */
    private void addShoppingCar(boolean selected) {
        mShoppingCart.setSelected(selected);
        mShoppingCart.setText(selected ? R.string.product_has_add_cart_text :
                R.string.product_add_to_cart_text);
        handpickDetail.setIsShoppingCar(selected ? 1 : 0);
    }

    /**
     * 跳转授权或三方页面
     */
    public void showAutoThree(boolean jumpThreeApp) {
        isLoadingAuto = true;
        String clickUrl = TextUtils.isEmpty(handpickDetail.getCouponClickUrl()) ? handpickDetail.getItemLink() : handpickDetail.getCouponClickUrl();

        AccountManager.setAuthorizeUrl(clickUrl,
                new ResponseCallback<AuthorizationBean>() {
                    @Override
                    public void success(AuthorizationBean authorizationBean) {
                        isLoadingAuto = false;
                        String authorizationUrl = authorizationBean.getAuthorizationUrl();
                        final Uri uri = Uri.parse(authorizationUrl);
                        String queryParameter = uri.getQueryParameter("relationId");
                        if (!TextUtils.isEmpty(queryParameter)) {
                            LZLog.i(TAG, "直接跳转淘宝");
                            if (jumpThreeApp) {
                                jumpTaoOrH5(authorizationUrl);
                            } else {
                                //分享授权 不用跳转淘宝
                                shareBundle();
                            }
                        } else {
                            LZLog.i(TAG, "淘宝授权");
                            DiaLogManager.showAuthDialog(PreciseDetailActivity.this,
                                    getSupportFragmentManager(), authorizationUrl);
                        }
                    }

                    @Override
                    public void error(Throwable throwable) {
                        isLoadingAuto = false;
                        LZLog.w(TAG, "setAuthorizeUrl error==" + throwable);
                    }
                });
    }

    private void jumpTaoOrH5(String authorizationUrl) {
        if (PackageUtil.checkAppInstalled(this, PackageUtil.PackageType.TAO_BAO) || PackageUtil.checkAppInstalled(this, PackageUtil.PackageType.TIAN_MAO)) {
            LZLog.i(TAG, "直接跳转淘宝");
            AliSdkManager.showGoods(this, authorizationUrl, null);
        } else {
            LZLog.i(TAG, "跳转淘宝h5");
            CommonWebJump.showInterceptOtherWebActivity(this, authorizationUrl);
        }
    }

    private boolean shouldLoginFirst() {
        AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        if (!accountManager.isLogined()) {
            CommonSchemeJump.showLoginActivityForResult(this, CommonConst.LOGIN_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonConst.LOGIN_REQUEST_CODE) {
            // TODO: 2019/11/29  如果后期需要登录刷新数据，则打开这个
            showRecommendTopView();
         /*   boolean logined = LiZhiApplication.getApplication().getAccountManager().isLogined();
            if (logined){
                //登录成功后，重新刷新数据
                showLoadingDialog(true);
                mPresenter.requestThreePartyProductDetail(mThreeDetailId, mPlatformType);
            }
*/
        }
    }

    /**
     * 跳转商品分享页面
     */
    private void shareBundle() {
        if (handpickDetail == null) {
            MessageToast.showToast(this, "商品信息未获取到");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putDouble(CommonConst.KEY_SHARE_COMMISSION_PRICE,
                handpickDetail.getCommissionMoney());
        bundle.putDouble(CommonConst.KEY_SHARE_ORIGINAL_PRICE,
                handpickDetail.getDiscountsPriceAfter() + handpickDetail.getCouponAmount());
        bundle.putDouble(CommonConst.KEY_SHARE_AFTER_PRICE,
                handpickDetail.getDiscountsPriceAfter());
        bundle.putString(CommonConst.KEY_SHARE_PRODUCT_TITLE, handpickDetail.getTitle());
        bundle.putString(CommonConst.KEY_SHARE_PRODUCT_VOLUME, String.valueOf(handpickDetail.getVolume()));
        bundle.putString(CommonConst.KEY_SHARE_PRODUCT_COUPONAMOUNT, String.valueOf(handpickDetail.getCouponAmount()));
        bundle.putString(CommonConst.KEY_SHARE_PRODUCT_MAIN_PICTURL, String.valueOf(handpickDetail.getPictUrl()));
        bundle.putString(CommonConst.KEY_SHARE_PRODUCT_RUSH_ADDRESS_URL, String.valueOf(handpickDetail.getItemLink()));
        bundle.putStringArrayList(CommonConst.KEY_SHARE_PRODUCT_ALL_PICTURL, (ArrayList<String>) handpickDetail.getSmallImages());

        // bundle.putInt(CommonConst.KEY_SHARE_FROM_TYPE, CommonConst.KEY_SHARE_THREE_VALUE);
        bundle.putLong(CommonConst.KEY_THREE_DETAIL_ID, handpickDetail.getItemId());
        bundle.putInt(CommonConst.KEY_SHARE_PLATFORM_TYPE, handpickDetail.getIcon());

        CommonSchemeJump.showCreateShareActivity(this, bundle);
    }


    private void initProductGoodsDetailFragment() {
        //商品详情 顶部页
        FragmentManager fragmentManager = getSupportFragmentManager();
        mGoodsDetailFragment = new ProductGoodsDetailFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.product_detail_top_container_fl, mGoodsDetailFragment)
                .commit();
    }

    private void initProductDetailRecommendFragment() {
        //荔枝推荐
        mProductDetailRecommendFragment = new ProductDetailRecommendFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.product_detail_recommend_container_fl,
                        mProductDetailRecommendFragment)
                .commit();
    }

    private void initProductBabyDetailFragment() {
        //宝贝详情
        mProductBabyDetailFragment = new ProductBabyDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.product_detail_baby_detail_container_fl, mProductBabyDetailFragment)
                .commit();
    }

    private void initGuessYouLikeFragment() {
        //猜你喜欢
        mProductGuessYouLikeListFragment = new ProductGuessYouLikeListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.detail_guess_you_like_container_fl, mProductGuessYouLikeListFragment)
                .commit();
    }

    @Override
    public void requestThreePartyProductDetailNoData(String msg) {
        LZLog.i(TAG, "requestThreePartyProductDetailNoData==");
        dismissDelayLoadingDialog(100);
        MessageToast.showToast(this, msg);
    }

    @Override
    public void requestThreePartyProductDetailNoButJump(PreciseDetailEntity entity) {
        LZLog.i(TAG, "requestThreePartyProductDetailNoButJump==" + entity.getJumpUrl());
        mLoadingiV.setVisibility(View.GONE);
        DiaLogManager.showDetailConfirmDialog(getSupportFragmentManager(), "该商品暂无佣金，是否确认继续购买", new ButtonClickListener() {
            @Override
            public void onButtonClick(View buttonView, Bundle arguments, Object tag, CharSequence text, int position) {
                //确定,直接跳转淘宝天猫
                if (PackageUtil.checkAppInstalled(PreciseDetailActivity.this, PackageUtil.PackageType.TAO_BAO) || PackageUtil.checkAppInstalled(PreciseDetailActivity.this, PackageUtil.PackageType.TIAN_MAO)) {
                    LZLog.i(TAG, "直接跳转淘宝");
                    AliSdkManager.showGoods(PreciseDetailActivity.this, entity.getJumpUrl(), null);
                } else {
                    LZLog.i(TAG, "跳转淘宝h5,这个不要去拦截，因为在这个回调里的都是找不到的商品数据");
                    CommonWebJump.showCommonWebActivity(PreciseDetailActivity.this, entity.getJumpUrl());
                }
                finish();
            }
        }, new ButtonClickListener() {
            @Override
            public void onButtonClick(View buttonView, Bundle arguments, Object tag, CharSequence text, int position) {
                //取消
                finish();
            }
        });

    }

    private void dismissDelayLoadingDialog(long delay) {
        sHandler.sendEmptyMessageDelayed(DISMISS_DIALOG_CODE, delay);
    }

    @Override
    public void requestThreePartyProductDetailSuccess(PreciseDetailEntity entity) {
        LZLog.i(TAG, "requestThreePartyProductDetailSuccess==");
        dismissDelayLoadingDialog(100);
        HandPickDetailBean handpickDetail = entity.getHandpickDetail();
        if (handpickDetail == null) {
            return;
        }
        this.handpickDetail = handpickDetail;
        mRecommendTopTv.setSelected(handpickDetail.getIsRecommend() == 1);//设置推荐按钮
        mShareAwardTv.setText("¥" + StringUtils.getFormattedDoubleOrInt(this.handpickDetail.getCommissionMoney()));
        mBuySaveTv.setText("¥" + StringUtils.getFormattedDoubleOrInt(this.handpickDetail.getBuyingProvince()));
        //商品详情
        mGoodsDetailFragment.updateProduct(this.handpickDetail);
        //升级vip
        List<String> textAndUrl = entity.getTextAndUrl();
        mGoodsDetailFragment.updateVip(textAndUrl);
        //店铺信息
        mProductBabyDetailFragment.updateShopStore(this.handpickDetail);
        //商品介绍图
        if (mPlatformType == CommonConst.PLATFORM_TAO_BAO || mPlatformType == CommonConst.PLATFORM_TIAN_MAO) {
            if (imagsList == null) {
                String pictUrl = handpickDetail.getPictUrl();
                List<String> smallImages = handpickDetail.getSmallImages();
                if (!TextUtils.isEmpty(pictUrl)) {
                    if (smallImages == null) {
                        smallImages = new ArrayList<>();
                    }
                    smallImages.add(0, pictUrl);
                }
                mProductBabyDetailFragment.updateShopDecPic(smallImages);
            }
        } else {
            if (mPlatformType == CommonConst.PLATFORM_KAO_LA && mBabyLine != null) {
                mBabyLine.setVisibility(View.GONE);
            }
            mProductBabyDetailFragment.updateShopDecPic(handpickDetail.getPageImages());
        }

        //荔枝推荐
        mProductDetailRecommendFragment.updateData(entity.getRecommend());
        //猜你喜欢
        mProductGuessYouLikeListFragment.updateData(entity.getGuessYouLike());

        if (handpickDetail.getIcon() == CommonConst.PLATFORM_KAO_LA) {
            //考拉数据 不可加入购物车
            mShoppingCart.setVisibility(View.INVISIBLE);
        } else {
            mShoppingCart.setVisibility(View.VISIBLE);
            //刷新加入购物车状态
            addShoppingCar(this.handpickDetail.getIsShoppingCar() == 1);
        }


    }

    @Override
    public void requestThreePartyProductDetailError(String code, Throwable throwable) {
        LZLog.w(TAG, "requestThreePartyProductDetailError==" + throwable);
        if (BaseBean.CODE_PRODUCT_NO_ERROR.equals(code)) {
            ToastUtils.show("该宝贝已下架或非淘客宝贝哦!");
            sHandler.sendEmptyMessageDelayed(FINISH_ACTIVITY_CODE, 1000);
            return;
        }
        ToastUtils.show("数据获取异常，请稍后重试");
        mLoadingView.setVisibility(View.GONE);
        sHandler.sendEmptyMessageDelayed(FINISH_ACTIVITY_CODE, 1000);
    }


    @Override
    public void requestThreePartyProductDecPicSuccess(List<String> entity) {
        LZLog.i(TAG, "requestThreePartyProductDecPicSuccess==");
        if (!ArraysUtils.isListEmpty(entity)) {
            if (entity.size() > 20) {
                imagsList = entity.subList(0, 20);
            } else {
                imagsList = entity;
            }

            //商品详情图片
            mProductBabyDetailFragment.updateShopDecPic(imagsList);
        } else {
            if (handpickDetail != null) {
                mProductBabyDetailFragment.updateShopDecPic(handpickDetail.getPageImages());
            }
        }

    }

    @Override
    public void requestAddButtonRecommendInfoSuccess(Long recommendId) {
        LZLog.i(TAG, "requestAddButtonRecommendInfoSuccess");
        //添加推荐列表 成功
        handpickDetail.setRecommendId(recommendId);

    }

    @Override
    public void requestAddButtonRecommendInfoError(Throwable throwable) {
        LZLog.i(TAG, "requestAddButtonRecommendInfoError==" + throwable);
    }

    @Override
    public void requestDeleteButtonRecommendInfoSuccess(Boolean b) {
        LZLog.i(TAG, "requestDeleteButtonRecommendInfoSuccess");
        //从推荐列表删除成功
        handpickDetail.setRecommendId(0);
    }

    @Override
    public void requestDeleteButtonRecommendInfoError(Throwable throwable) {
        LZLog.i(TAG, "requestDeleteButtonRecommendInfoError==" + throwable);
    }

    @Override
    public void requestThreePartyProductDecPicError(Throwable throwable) {
        LZLog.w(TAG, "requestThreePartyProductDecPicError==" + throwable);
        if (handpickDetail != null) {
            String pictUrl = handpickDetail.getPictUrl();
            List<String> smallImages = handpickDetail.getSmallImages();
            if (!TextUtils.isEmpty(pictUrl)) {
                if (smallImages == null) {
                    smallImages = new ArrayList<>();
                }
                smallImages.add(0, pictUrl);
            }
            mProductBabyDetailFragment.updateShopDecPic(smallImages);
        }
    }

    @Override
    public void requestDeleteShoppingCartInfoSuccess(Boolean b) {
        LZLog.i(TAG, "requestDeleteShoppingCartInfoSuccess==" + b);
    }

    @Override
    public void requestDeleteShoppingCartInfoError(Throwable throwable) {
        LZLog.i(TAG, "requestDeleteShoppingCartInfoError==" + throwable);
    }

    @Override
    public void requestAdd2ShoppingCartInfoSuccess(RequestShoppingCartEntity b) {
        LZLog.i(TAG, "requestAdd2ShoppingCartInfoSuccess==");
        if (handpickDetail != null) {
            handpickDetail.setShoppingCarId(b.getId());
        }

    }

    @Override
    public void requestAdd2ShoppingCartInfoError(Throwable throwable) {
        LZLog.i(TAG, "requestAdd2ShoppingCartInfoError==" + throwable);
    }


    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == DISMISS_DIALOG_CODE) {
            mLoadingView.setVisibility(View.GONE);
            return true;
        } else if (msg.what == FINISH_ACTIVITY_CODE) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sHandler != null) {
            sHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //表明当前的动作是由 ScrollView 触发和主导   mScrollView.setOnTouchListener(this);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            tagFlag = true;
        }
        return false;
    }
}
