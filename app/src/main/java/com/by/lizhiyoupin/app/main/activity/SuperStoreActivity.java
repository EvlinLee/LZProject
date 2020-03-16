package com.by.lizhiyoupin.app.main.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ShopBannerBean;
import com.by.lizhiyoupin.app.io.bean.ShopGoodsBean;
import com.by.lizhiyoupin.app.io.bean.ShopMdBean;
import com.by.lizhiyoupin.app.io.bean.ShopMindBean;
import com.by.lizhiyoupin.app.io.bean.SuperAttionBean;
import com.by.lizhiyoupin.app.io.bean.SuperKindBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.adapter.BrandStoreAdapter;
import com.by.lizhiyoupin.app.main.contract.SuperContract;
import com.by.lizhiyoupin.app.main.presenter.SuperPresenter;
import com.by.lizhiyoupin.app.manager.AccountManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/*
 * 店铺信息
 * jyx
 * */
@Route(path = "/app/SuperStoreActivity")
public class SuperStoreActivity extends BaseMVPActivity<SuperContract.SuperView,
        SuperContract.SuperPresenters> implements SuperContract.SuperView, View.OnClickListener {
    private TextView shop_comprehensive,shop_sellers;
    private int shopType = 0;//类型，0升序，1降序
    private int sortType;//排序类型 1综合 2热卖榜
    private int eSortDirection = CommonConst.E_UPLIMIT_SORT_DOWN;
    private RecyclerView shop_rcy;
    private LoadMoreHelperRx<ShopGoodsBean, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private GridLayoutManager mLinearLayoutManager;
    private BrandStoreAdapter adapater;
    private ImageView store_logo,back;
    private TextView store_describle,store_name,store_content,shop_concern,shop_see;
    private   String shop_id,shop_status,shop_url;
    private int shopId, pageNo, pageSize;
    private boolean hasLoadMore;
    private int type=0;//关注类型
    private TextView empty_layout;
    private View mFailRetry;
    private View mloadingLayout;
    private SmartRefreshLayout mSmartRefreshLayout;
    private AppBarLayout mAppBarLt;
    private int currentStatus = -1;
    private int minId=1;
    private SpaceItemDecoration mSpaceItemDecoration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_store);
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .flymeOSStatusBarFontColorInt(Color.BLACK)
                .statusBarColorInt(Color.TRANSPARENT)
                .init();
        initBar();
        initView();

    }

    private void initBar() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        int statusBarHeight = DeviceUtil.getStatusBarHeight(this);
        findViewById(R.id.toolbar).getLayoutParams().height=statusBarHeight;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) back.getLayoutParams();
        View title = findViewById(R.id.title_super);
        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) title.getLayoutParams();
        layoutParams1.setMargins(0,statusBarHeight,0,0);
        layoutParams.setMargins(0,statusBarHeight,0,0);

    }

    @Override
    public SuperContract.SuperPresenters getBasePresenter() {
        return new SuperPresenter(this);
    }

    private void initView() {
        mAppBarLt = findViewById(R.id.app_bar_lt);
        mFailRetry =  findViewById(R.id.fail_retry);
        mloadingLayout =  findViewById(R.id.loading_layout);
        empty_layout = findViewById(R.id.empty_layout);//无数据
        shop_comprehensive = findViewById(R.id.shop_comprehensive);
        shop_comprehensive.setOnClickListener(this);//综合
        shop_sellers = findViewById(R.id.shop_sellers);
        shop_sellers.setOnClickListener(this);//热卖榜

        store_logo = findViewById(R.id.store_logo);//店铺头像
        store_describle = findViewById(R.id.store_describle);//店铺描述
        store_name = findViewById(R.id.store_name);//店铺名称
        store_content = findViewById(R.id.store_content);//店铺内容

        shop_concern = findViewById(R.id.shop_concern);//关注店铺
        shop_concern.setOnClickListener(this);
        shop_see = findViewById(R.id.shop_see);//进店看看
        shop_see.setOnClickListener(this);
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);

        Intent intent = getIntent();
        shop_url = intent.getStringExtra(CommonConst.SUPER_SHOPURL);//店铺url
        shop_id= intent.getStringExtra(CommonConst.SUPER_SHOPID);//店铺id
        shop_status = intent.getStringExtra(CommonConst.SUPER_SHOPSTATUS);//关注店铺状态
        Glide.with(this).load(intent.getStringExtra(CommonConst.SUPER_SHOPIMG))
                .transform(new RoundedCornersTransformation(DeviceUtil.dip2px(this, 4),0,RoundedCornersTransformation.CornerType.ALL))
                .error(R.drawable.empty_pic_list)
                .placeholder(R.drawable.empty_pic_list)
                .into(store_logo);
        store_describle.setText(intent.getStringExtra(CommonConst.SUPER_SHOPDESCRIBLE));
        store_name.setText(intent.getStringExtra(CommonConst.SUPER_SHOPNAME));
        store_content.setText(intent.getStringExtra(CommonConst.SUPER_SHOPCONTENT));
        if ("0".equals(shop_status)){//未关注
            type=1;
            shop_concern.setText("关注店铺");
        }else{//关注
            type=2;
            shop_concern.setText("取消关注");
        }
        shop_rcy = findViewById(R.id.shop_rcy);
        mSpaceItemDecoration = new SpaceItemDecoration(DeviceUtil.dip2px(this, 8), 2);
        shop_rcy.addItemDecoration(mSpaceItemDecoration);
        mLinearLayoutManager = new GridLayoutManager(this, 2);
        shop_rcy.setLayoutManager(mLinearLayoutManager);
        adapater = new BrandStoreAdapter(this);
        mLinearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapater.getItemCount()-1==position?2:1;
            }
        });
        shop_rcy.setAdapter(adapater);
        loadRecyclerView(shop_id,1,10,0,1);
        setSmartRefreshLayout();
        int top=DeviceUtil.dip2px(this,120);
        mAppBarLt.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i <= -top && currentStatus != 1) {
                    //上滑
                    currentStatus = 1;
                    ImmersionBar.with(SuperStoreActivity.this)
                            .navigationBarColorInt(Color.WHITE)
                            //.titleBar(findViewById(R.id.app_bar_lt))
                            .statusBarDarkFont(false)
                            .flymeOSStatusBarFontColorInt( Color.WHITE)
                            .statusBarColor("#FFE34E53")
                            .init();

                }else if (i > -top && currentStatus != 2) {
                //改变滑动状态的时候需要变色
                    currentStatus = 2;
                    ImmersionBar.with(SuperStoreActivity.this)
                            //.titleBar(findViewById(R.id.app_bar_lt))
                            .navigationBarColorInt(Color.WHITE)
                            .statusBarDarkFont(false)
                            .flymeOSStatusBarFontColorInt( Color.WHITE)
                            .statusBarColorInt(Color.TRANSPARENT)
                            .init();
                }
            }
        });

    }

    private void setSmartRefreshLayout() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mLoadMoreHelper != null) {
                    minId=1;
                    mLoadMoreHelper.loadData();
                } else {
                    mSmartRefreshLayout.finishRefresh(1000);
                }

            }
        });
        mSmartRefreshLayout.setEnableAutoLoadMore(false);
        mSmartRefreshLayout.setEnableLoadMore(false);


    }

    private void loadRecyclerView(String shop_id,int i, int i1, int sortType, int shopType) {
        mLoadMoreHelper = new LoadMoreBuilderRx<ShopGoodsBean, Integer>(this)
                .adapter(adapater)
                .recyclerView(shop_rcy)
                 .emptyView(empty_layout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1,4))
                .loader(new LoadMoreHelperRx.Loader<ShopGoodsBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<ShopGoodsBean>> load(Integer integer, int pageSize) {
                            return ApiService.getSuperApi().requestShopGoods(Integer.parseInt(shop_id),minId/*,integer,pageSize*/,sortType,shopType).map(new Function<BaseBean<ShopMdBean>, Collection<ShopGoodsBean>>() {
                                @Override
                                public Collection<ShopGoodsBean> apply(BaseBean<ShopMdBean> listBaseBean) throws Exception {
                                    mSmartRefreshLayout.finishRefresh();
                                    if (listBaseBean.success() && listBaseBean.getResult() != null) {
                                        ShopMdBean mindBean=listBaseBean.getResult();
                                        minId= mindBean.getMin_id();
                                        List<ShopGoodsBean> list =
                                                mindBean.getData();
                                        boolean listEmpty = ArraysUtils.isListEmpty(list);
                                        hasLoadMore = !listEmpty && list.size()>4;
                                        return list;
                                    }
                                    throw new Exception(listBaseBean.msg);
                                }
                            });
                    }
                    @Override
                    public boolean hasMore(Collection<ShopGoodsBean> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();

        shop_rcy.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    LZLog.i(TAG, "requestGetFansList onLoadMore" + currentPage);
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        mLoadMoreHelper.loadData();
    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()){
            case R.id.shop_comprehensive://综合
                shop_comprehensive.setTextColor(Color.parseColor("#FFFF005F"));
                shop_sellers.setTextColor(Color.parseColor("#FF555555"));
                minId=1;
                loadRecyclerView(shop_id,1,10,0,1);
                /*shop_rcy.setVisibility(View.VISIBLE);
                shop_rcy2.setVisibility(View.GONE);*/
                break;
            case R.id.shop_sellers://热卖榜
                shop_comprehensive.setTextColor(Color.parseColor("#FF555555"));
                shop_sellers.setTextColor(Color.parseColor("#FFFF005F"));
                requestClickRadar(shop_sellers, 3);
               /* shop_rcy.setVisibility(View.GONE);
                shop_rcy2.setVisibility(View.VISIBLE);*/
                break;

            case R.id.shop_concern://关注店铺

                if (!shouldLoginFirst()){
                    CommonSchemeJump.showLoginActivity(this);
                }else{
                    if (type==1){
                        basePresenter.requestFollowShop(Integer.parseInt(shop_id),0);
                    }else{
                        basePresenter.requestFollowShop(Integer.parseInt(shop_id),1);
                    }
                }
                break;
            case R.id.shop_see://进店看看
                CommonWebJump.showInterceptOtherWebActivity(this, shop_url);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void requestClickRadar(TextView clickTv, int i) {
        eSortDirection = eSortDirection == CommonConst.E_UPLIMIT_SORT_DOWN ? CommonConst.E_UPLIMIT_SORT_UP : CommonConst.E_UPLIMIT_SORT_DOWN;
        sortType = i;
        resetTitleRankDrawable(clickTv, eSortDirection);
         mLoadMoreHelper.loadData();
    }

    private void resetTitleRankDrawable(TextView clickTv, int eSortDir) {

        ViewUtil.setDrawableOfTextView(shop_sellers, R.drawable.shop_direction_normal, ViewUtil.DrawableDirection.RIGHT);
        //对点击的设置 图片
        if (clickTv != null) {
            ViewUtil.setDrawableOfTextView(clickTv, getDrawableIdBySortDir(eSortDir), ViewUtil.DrawableDirection.RIGHT);
        }
    }
    private int getDrawableIdBySortDir(int sortDir) {
        int res = R.drawable.shop_direction_normal;
        switch (sortDir) {
            case 0:
                res = R.drawable.shop_direction_top;
                minId=1;
                loadRecyclerView(shop_id,1,10,1,0);
                break;
            case 1:
                minId=1;
                res = R.drawable.shop_direction_down;
                loadRecyclerView(shop_id,1,10,1,1);
                break;
        }
        return res;
    }

    @Override
    public void requestSuperKindSuccess(List<SuperKindBean> list) {

    }

    @Override
    public void requestSuperKindError(Throwable throwable) {

    }

    @Override
    public void requestShopKindSuccess(ShopMindBean bean) {

    }


    @Override
    public void requestShopKindError(Throwable throwable) {

    }

    @Override
    public void requestFollowShopSuccess(SuperAttionBean bean) {
            superAttion(bean);
    }

    private void superAttion(SuperAttionBean bean) {

        int followStateCode = bean.getFollowStateCode();
        if (followStateCode==0){
            type=1;
            shop_concern.setText("关注店铺");
        }else if (followStateCode==1){
            type=2;
            shop_concern.setText("取消关注");
        }
        MessageToast.showToastBottom(SuperStoreActivity.this,bean.getFollowStateExplain(),Gravity.CENTER);
    }

    @Override
    public void requestFollowShopError(Throwable throwable) {

    }

    @Override
    public void requestShopGoodsSuccess(ShopMdBean bean) {

    }


    @Override
    public void requestShopGoodsError(Throwable throwable) {

    }

    @Override
    public void requestShopBannerSuccess(List<ShopBannerBean> list) {

    }

    @Override
    public void requestShopBannerError(Throwable throwable) {

    }

    private boolean shouldLoginFirst() {
        AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        if (!accountManager.isLogined()) {
            CommonSchemeJump.showLoginActivity(this);
            return false;
        } else {
            return true;
        }
    }
}
