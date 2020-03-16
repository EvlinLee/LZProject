package com.by.lizhiyoupin.app.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_banner.Banner;
import com.by.lizhiyoupin.app.component_banner.BannerConfig;
import com.by.lizhiyoupin.app.component_banner.Transformer;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.ArcRectView;
import com.by.lizhiyoupin.app.component_ui.weight.BannerImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseHeaderDelegateAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfDelegateAdapter;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.LzTransformationUtil;
import com.by.lizhiyoupin.app.io.bean.GuideArticleBean;
import com.by.lizhiyoupin.app.io.bean.HomeBannerBean;
import com.by.lizhiyoupin.app.io.bean.HomeIconBean;
import com.by.lizhiyoupin.app.io.bean.PreciseBannerIconBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.bean.PreciseSelectionBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.LoadType;
import com.by.lizhiyoupin.app.main.adapter.PreciseRecommendImgAdapter;
import com.by.lizhiyoupin.app.main.adapter.PreciseRushBuyImgAdapter;
import com.by.lizhiyoupin.app.main.adapter.PreciseSelectionDarenRecommendListAdapter;
import com.by.lizhiyoupin.app.main.adapter.PreciseSelectionListAdapter2;
import com.by.lizhiyoupin.app.main.adapter.PreciseShakeCouponListAdapter;
import com.by.lizhiyoupin.app.main.adapter.PreciseWantToBuyAdapter;
import com.by.lizhiyoupin.app.main.holder.LimitSkillHeaderHolder;
import com.by.lizhiyoupin.app.main.holder.PreciseStickHeaderHolder;
import com.by.lizhiyoupin.app.main.weight.HomeToolsLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/30 19:03
 * Summary: 精选 页面
 */
@Deprecated
public class PreciseOfFragment extends BaseFragment {
    public static final String TAG = PreciseOfFragment.class.getSimpleName();
    public static final int ITEM_TYPE_OF_BANNER = 301;
    public static final int ITEM_TYPE_OF_RECOMMEND_TOP = 302;
    public static final int ITEM_TYPE_OF_RECOMMEND = 303;
    public static final int ITEM_TYPE_OF_RUSH_BUY_TOP = 304;
    public static final int ITEM_TYPE_OF_RUSH_BUY_BOTTOM = 305;
    public static final int ITEM_TYPE_OF_BOTTOM_LIST = 306;
    public static final int ITEM_TYPE_OF_BOTTOM_ARTICLE_LIST = 307;
    public static final int ITEM_TYPE_OF_BOTTOM_VIDEO_LIST = 308;
    public static final int ITEM_TYPE_OF_BOTTOM_SHAKE_LIST = 309;
    public static final int ITEM_TYPE_OF_BOTTOM_RECOMMEND_LIST = 310;
    public static final int ITEM_TYPE_OF_BOTTOM_WANT_TO_BUY_LIST = 311;

    private RecyclerView mRecyclerView;
    private ImageView mJumpTopIv;
    private SmartRefreshLayout mSmartRefreshLayout;
    private Context mContext;


    private VirtualLayoutManager mLayoutManager;
    private DelegateAdapter mDelegateAdapter;
    private List<DelegateAdapter.Adapter> mChildAdapters;
    private ArcRectView mAreRectView;//弧形
    private Banner mTopBanner;

    private HomeToolsLayout mHomeToolsLayout;
    private List<HomeBannerBean> mBannerList;
    private List<PreciseBannerIconBean> mRecommendList;


    private int start = 1;
    private int limit = 20;
    private boolean hasMore;
    private BaseHeaderDelegateAdapter mBannerIconAdapter;
    private BaseOfDelegateAdapter mRecommendTopImgAdapter;
    private BaseOfDelegateAdapter mRecommendImgAdapter;
    private GridLayoutHelper mRecommendImgHelper;
    private BaseOfDelegateAdapter mRushBuy2Adapter;
    private BaseOfDelegateAdapter mRushBuy4Adapter;
    private BaseHeaderDelegateAdapter mStickListHeaderAdapter;
    private BaseOfDelegateAdapter mBottomListAdapter;
    private BaseOfDelegateAdapter mBottomRecommendListAdapter;
    private BaseOfDelegateAdapter mBottomShakeListAdapter;
    private BaseOfDelegateAdapter mBottomWantToBuyListAdapter;
    //是否在加载列表数据
    private boolean isloadingList = false;
    private View mTopRecommend;
    // 分页相关layout
    private FrameLayout mFooterView;
    private View mLoadingLayout;
    private View mNoMoreLayout;
    private View mNoDataLayout;
    private PreciseStickHeaderHolder mStickHeaderHolder;
    private  LimitSkillHeaderHolder limitSkillHeaderHolder;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LZLog.i(TAG, "onAttach");
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.v_layout_refresh_layout, container, false);
        initView(inflate);
        return inflate;
    }

    /**
     * 是否可以开启下拉刷新
     *
     * @param enable
     */
    public void setEnableRefresh(boolean enable) {
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.setEnableRefresh(enable);
        }
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        LZLog.i(TAG, "onFirstUserInvisible==");
        if (mTopBanner != null) {
            mTopBanner.stopAutoPlay();
            LZLog.i(TAG, "stopAutoPlay==");
        }
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        LZLog.i(TAG, "onFirstUserVisible==");
        if (mTopBanner != null) {
            mTopBanner.startAutoPlay();
            LZLog.i(TAG, "startAutoPlay==");
        }
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        LZLog.i(TAG, "onUserVisible==");
        if (getParentFragment() instanceof TabFragmentHome) {
            TabFragmentHome parentFragment = (TabFragmentHome) getParentFragment();
            int currentPager = parentFragment.getCurrentPager();
            if (currentPager == 0) {
                mTopBanner.startAutoPlay();
            }
        }

    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        LZLog.i(TAG, "onUserInvisible==");
        mTopBanner.stopAutoPlay();
    }

    private void initView(View root) {
        mSmartRefreshLayout = root.findViewById(R.id.smartRefreshLayout);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mJumpTopIv = root.findViewById(R.id.jump_top_iv);
        setSmartRefreshLayout();

        mChildAdapters = new LinkedList<>();
        // 创建VirtualLayoutManager对象
        mLayoutManager = new VirtualLayoutManager(mContext);
        // 同时内部会创建一个 LayoutHelperFinder 对象，用来后续的LayoutHelper查找
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 将VirtualLayoutManager绑定到recyclerView
        // 解决RecyclerView抢焦点把自己置于屏幕中央的问题
        mRecyclerView.setFocusable(false);

        /**
         * 步骤2：设置组件复用回收池
         *  正对不同type设置不同的复用池
         * */
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(ITEM_TYPE_OF_BANNER, 4);//type=ITEM_TYPE_OF_BANNER 的item设置了复用池的大小
        viewPool.setMaxRecycledViews(ITEM_TYPE_OF_RECOMMEND, 6);
        viewPool.setMaxRecycledViews(ITEM_TYPE_OF_RUSH_BUY_TOP, 4);
        viewPool.setMaxRecycledViews(ITEM_TYPE_OF_RUSH_BUY_BOTTOM, 4);
        viewPool.setMaxRecycledViews(ITEM_TYPE_OF_BOTTOM_LIST, 6);
        viewPool.setMaxRecycledViews(ITEM_TYPE_OF_BOTTOM_ARTICLE_LIST, 6);
        viewPool.setMaxRecycledViews(ITEM_TYPE_OF_BOTTOM_VIDEO_LIST, 6);
        //当hasConsistItemType=true的时候，不论是不是属于同一个子adapter，相同类型的item都能复用。表示它们共享一个类型。
        // 当hasConsistItemType=false的时候，不同子adapter之间的类型不共享
        mDelegateAdapter = new DelegateAdapter(mLayoutManager, false);//主Adapter
        mRecyclerView.setAdapter(mDelegateAdapter);


        initBannerIconView();
        initSelectionRecommendTopOneView();
        initSelectionRecommendView();
        initLimitSkillView();
        initSelectionRushBuyView2();
        initSelectionRushBuyView4();
        initSelectionBottomImg();
        initSelectionBottomList();
        initSelectionRecommendList();
        initSelectionShakeList();
        initSelectionWantToBuyList();
        initStickySortListHeaderView();
        mChildAdapters.add(mBottomListAdapter);
        //最后将子adapter添加到主adapter
        mDelegateAdapter.addAdapters(mChildAdapters);
        requestMainData();
        startEnableLoadMore(false);
        mSmartRefreshLayout.setEnableNestedScroll(true);//是否启用嵌套滚动

        requestList(start, limit,true);

        mJumpTopIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutManager.scrollToPosition(0);
                mJumpTopIv.setVisibility(View.GONE);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                mJumpTopIv.setVisibility(lastVisibleItem > 20 ? View.VISIBLE : View.GONE);

                //预加载
                if (mLayoutManager.getItemCount() > 10 && !isloadingList && lastVisibleItem + 1 >= mLayoutManager.getItemCount()) {
                    LZLog.d(TAG, "onScrolled: onLoadMore" + start);
                    if (hasMore&&mStickHeaderHolder.getCurrentIndex()!=3) {
                        requestList(start, limit,false);
                    }
                }
            }
        });
    }


    /**
     * 设置是否可加载更多
     *
     * @param canLoadMore
     */
    private void startEnableLoadMore(boolean canLoadMore) {
        mSmartRefreshLayout.setEnableLoadMore(canLoadMore);
        mSmartRefreshLayout.setEnableAutoLoadMore(canLoadMore);
    }

    private void setSmartRefreshLayout() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                LZLog.i(TAG, "onRefresh onRefresh==" + start);
                resetRequestList();
            }
        });
    }


    /**
     * banner+icon
     */
    private void initBannerIconView() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_top_layout, null, false);
        mTopBanner = inflate.findViewById(R.id.top_banner);
        mAreRectView = inflate.findViewById(R.id.arc_rectView);
        mHomeToolsLayout = inflate.findViewById(R.id.homeToolsLayout);
        setBannerConfig();
        //创建 LayoutHelper
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        mBannerIconAdapter = new BaseHeaderDelegateAdapter(mContext, linearLayoutHelper);
        mBannerIconAdapter.setHeaderView(inflate);

        mChildAdapters.add(mBannerIconAdapter);
    }

    /**
     * 一张推荐 大图
     */
    private void initSelectionRecommendTopOneView() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(1);
        mRecommendTopImgAdapter = new BaseHeaderDelegateAdapter(mContext, gridLayoutHelper);
        mTopRecommend = LayoutInflater.from(mContext).inflate(R.layout.item_precise_recommend_top_layout, null, false);
        mRecommendTopImgAdapter.setHeaderView(mTopRecommend);

        mChildAdapters.add(mRecommendTopImgAdapter);

    }

    /**
     * 推荐 多张图
     */
    private void initSelectionRecommendView() {
        mRecommendImgHelper = new GridLayoutHelper(2);
        mRecommendImgHelper.setAutoExpand(false);
        mRecommendImgHelper.setPaddingLeft(DeviceUtil.dip2px(mContext, 10));
        mRecommendImgHelper.setPaddingRight(DeviceUtil.dip2px(mContext, 10));
        mRecommendImgHelper.setPaddingBottom(DeviceUtil.dip2px(mContext, 15));
        mRecommendImgHelper.setHGap(DeviceUtil.dip2px(mContext, 5));
        mRecommendImgAdapter = new PreciseRecommendImgAdapter(mContext, mRecommendImgHelper, ITEM_TYPE_OF_RECOMMEND);

        mChildAdapters.add(mRecommendImgAdapter);
    }

    /*
     * 限时秒杀
     *
     * */
    private void initLimitSkillView() {
        LinearLayoutHelper layoutHelper = new LinearLayoutHelper();
        int margin10 = DeviceUtil.dip2px(mContext, 10);
        layoutHelper.setMargin(margin10, margin10, margin10, 0);
        limitSkillHeaderHolder = new LimitSkillHeaderHolder();
        BaseHeaderDelegateAdapter mRecommendTopImgAdapter = new BaseHeaderDelegateAdapter(mContext, layoutHelper);
        mRecommendTopImgAdapter.setHeaderView(limitSkillHeaderHolder.getOnCreateView(mContext,mRecyclerView,mJumpTopIv));
        mChildAdapters.add(mRecommendTopImgAdapter);
    }

    /**
     * 限时抢购 抢购--2列
     */
    private void initSelectionRushBuyView2() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);

        int margin01 = DeviceUtil.dip2px(mContext, 1);
        gridLayoutHelper.setHGap(DeviceUtil.dip2px(mContext, 1));
        gridLayoutHelper.setVGap(DeviceUtil.dip2px(mContext, 1));
        int margin10 = DeviceUtil.dip2px(mContext, 10);
        gridLayoutHelper.setMargin(margin10, margin10, margin10, margin01);

        mRushBuy2Adapter = new PreciseRushBuyImgAdapter(mContext, gridLayoutHelper, ITEM_TYPE_OF_RUSH_BUY_TOP);


        mChildAdapters.add(mRushBuy2Adapter);
    }

    /**
     * 限时抢购 领券--4列
     */
    private void initSelectionRushBuyView4() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        int margin01 = DeviceUtil.dip2px(mContext, 1);
        gridLayoutHelper.setHGap(margin01);
        gridLayoutHelper.setVGap(margin01);
        int margin10 = DeviceUtil.dip2px(mContext, 10);
        gridLayoutHelper.setMargin(margin10, 0, margin10, 0);
        mRushBuy4Adapter = new PreciseRushBuyImgAdapter(mContext, gridLayoutHelper, ITEM_TYPE_OF_RUSH_BUY_BOTTOM);


        mChildAdapters.add(mRushBuy4Adapter);
    }

    /**
     * 好货严选图片
     */
    private void initSelectionBottomImg() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(1);
        gridLayoutHelper.setMargin(0, DeviceUtil.dip2px(mContext, 30),
                0, DeviceUtil.dip2px(mContext, 5));
        BaseOfDelegateAdapter delegateAdapter = new BaseHeaderDelegateAdapter(mContext, gridLayoutHelper);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.item_bottom_list_top_img_layout, null, false);
        delegateAdapter.setHeaderView(headerView);

        mChildAdapters.add(delegateAdapter);

    }

    /**
     * 好货严选 列表
     */
    private void initSelectionBottomList() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);
        gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mDelegateAdapter.getItemCount() - 1 == position ? 2 : 1;
            }
        });
        gridLayoutHelper.setAutoExpand(false);//当一行里视图的个数少于spanCount值的时候，如果autoExpand为true，视图的总宽度会填满可用区域；否则会在屏幕上留空白区域
        mBottomListAdapter = new PreciseSelectionListAdapter2(mContext, gridLayoutHelper, ITEM_TYPE_OF_BOTTOM_LIST);
        int margin = DeviceUtil.dip2px(mContext, 5);
        int margin10 = DeviceUtil.dip2px(mContext, 10);
        gridLayoutHelper.setHGap(margin);
        gridLayoutHelper.setVGap(margin);
        gridLayoutHelper.setMargin(margin10, margin + margin10, margin10, 0);

        //加载更多
        mFooterView = new FrameLayout(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        mLoadingLayout = inflater.inflate(R.layout.list_item_loading, mFooterView, false);
        mFooterView.addView(mLoadingLayout);
        mNoMoreLayout = inflater.inflate(R.layout.no_more_text, mFooterView, false);
        TextView noMoreTv = mNoMoreLayout.findViewById(R.id.more);
        noMoreTv.setText(R.string.empty_load_more_text);
        noMoreTv.setGravity(Gravity.CENTER);
        mNoMoreLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoMoreLayout);
        mNoDataLayout = inflater.inflate(R.layout.item_home_empty_list_layout, mFooterView, false);
        mNoDataLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoDataLayout);
        mBottomListAdapter.setFooterView(mFooterView);

        //mChildAdapters.add(mBottomListAdapter);

    }

    /**
     * 达人推荐
     */
    private void initSelectionRecommendList() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);
        gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mDelegateAdapter.getItemCount() - 1 == position ? 2 : 1;
            }
        });
        gridLayoutHelper.setAutoExpand(false);//当一行里视图的个数少于spanCount值的时候，如果autoExpand为true，视图的总宽度会填满可用区域；否则会在屏幕上留空白区域
        mBottomRecommendListAdapter = new PreciseSelectionDarenRecommendListAdapter(mContext, gridLayoutHelper, ITEM_TYPE_OF_BOTTOM_RECOMMEND_LIST);
        int margin = DeviceUtil.dip2px(mContext, 5);
        int margin10 = DeviceUtil.dip2px(mContext, 10);
        gridLayoutHelper.setHGap(margin);
        gridLayoutHelper.setVGap(margin);
        gridLayoutHelper.setMargin(margin10, margin + margin10, margin10, 0);

        //加载更多
        FrameLayout   mFooterView = new FrameLayout(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View mLoadingLayout = inflater.inflate(R.layout.list_item_loading, mFooterView, false);
        mFooterView.addView(mLoadingLayout);
        View mNoMoreLayout = inflater.inflate(R.layout.no_more_text, mFooterView, false);
        TextView noMoreTv = mNoMoreLayout.findViewById(R.id.more);
        noMoreTv.setText(R.string.empty_load_more_text);
        noMoreTv.setGravity(Gravity.CENTER);
        mNoMoreLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoMoreLayout);
        View mNoDataLayout = inflater.inflate(R.layout.item_home_empty_list_layout, mFooterView, false);
        mNoDataLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoDataLayout);
        mBottomRecommendListAdapter.setFooterView(mFooterView);
    }

    /**
     * 抖券
     */
    private void initSelectionShakeList() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);
        gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mDelegateAdapter.getItemCount() - 1 == position ? 2 : 1;
            }
        });
        gridLayoutHelper.setAutoExpand(false);//当一行里视图的个数少于spanCount值的时候，如果autoExpand为true，视图的总宽度会填满可用区域；否则会在屏幕上留空白区域
        mBottomShakeListAdapter = new PreciseShakeCouponListAdapter(mContext, gridLayoutHelper, ITEM_TYPE_OF_BOTTOM_SHAKE_LIST,limit);
        int margin = DeviceUtil.dip2px(mContext, 5);
        int margin10 = DeviceUtil.dip2px(mContext, 10);
        gridLayoutHelper.setHGap(margin);
        gridLayoutHelper.setVGap(margin);
        gridLayoutHelper.setMargin(margin10, margin + margin10, margin10, 0);

        //加载更多
        FrameLayout   mFooterView = new FrameLayout(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View mLoadingLayout = inflater.inflate(R.layout.list_item_loading, mFooterView, false);
        mFooterView.addView(mLoadingLayout);
        View mNoMoreLayout = inflater.inflate(R.layout.no_more_text, mFooterView, false);
        TextView noMoreTv = mNoMoreLayout.findViewById(R.id.more);
        noMoreTv.setText(R.string.empty_load_more_text);
        noMoreTv.setGravity(Gravity.CENTER);
        mNoMoreLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoMoreLayout);
        View mNoDataLayout = inflater.inflate(R.layout.item_home_empty_list_layout, mFooterView, false);
        mNoDataLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoDataLayout);
        mBottomShakeListAdapter.setFooterView(mFooterView);
    }

    /**
     * 种草
     */
    private void initSelectionWantToBuyList() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);
        gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mDelegateAdapter.getItemCount() - 1 == position ? 2 : 1;
            }
        });
        gridLayoutHelper.setAutoExpand(false);//当一行里视图的个数少于spanCount值的时候，如果autoExpand为true，视图的总宽度会填满可用区域；否则会在屏幕上留空白区域
        mBottomWantToBuyListAdapter = new PreciseWantToBuyAdapter(mContext, gridLayoutHelper, ITEM_TYPE_OF_BOTTOM_WANT_TO_BUY_LIST);
        int margin = DeviceUtil.dip2px(mContext, 5);
        int margin10 = DeviceUtil.dip2px(mContext, 10);
        gridLayoutHelper.setHGap(margin);
        gridLayoutHelper.setVGap(margin);
        gridLayoutHelper.setMargin(margin10, margin + margin10, margin10, 0);
        //加载更多
        FrameLayout   mFooterView = new FrameLayout(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View mLoadingLayout = inflater.inflate(R.layout.list_item_loading, mFooterView, false);
        mFooterView.addView(mLoadingLayout);
        View mNoMoreLayout = inflater.inflate(R.layout.no_more_text, mFooterView, false);
        TextView noMoreTv = mNoMoreLayout.findViewById(R.id.more);
        noMoreTv.setText(R.string.empty_load_more_text);
        noMoreTv.setGravity(Gravity.CENTER);
        mNoMoreLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoMoreLayout);
        View mNoDataLayout = inflater.inflate(R.layout.item_home_empty_list_layout, mFooterView, false);
        mNoDataLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoDataLayout);
        mBottomWantToBuyListAdapter.setFooterView(mFooterView);
    }

    /**
     * 粘性头
     */
    private void initStickySortListHeaderView() {
        /**
         设置可选固定布局
         */
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();

        // 公共属性
        stickyLayoutHelper.setItemCount(1);// 设置布局里Item个数
        stickyLayoutHelper.setPadding(0, 0, 0, 0);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        stickyLayoutHelper.setMargin(0, 0, 0, 0);// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        stickyLayoutHelper.setBgColor(Color.GRAY);// 设置背景颜色
        // stickyLayoutHelper.setAspectRatio(3);// 设置设置布局内每行布局的宽与高的比

        // 特有属性 true = 组件吸在顶部 false = 组件吸在底部
        stickyLayoutHelper.setStickyStart(true);

        mStickListHeaderAdapter = new BaseHeaderDelegateAdapter(mContext, stickyLayoutHelper);
        //
        mStickHeaderHolder = new PreciseStickHeaderHolder(mBottomListAdapter, mBottomRecommendListAdapter,
                mBottomShakeListAdapter, mBottomWantToBuyListAdapter, mDelegateAdapter);
        mStickListHeaderAdapter.setHeaderView(mStickHeaderHolder.getOnCreateView(mContext, mRecyclerView, stickyLayoutHelper));
        mChildAdapters.add(mStickListHeaderAdapter);
    }


    //下面设置数据
    private void setBannerConfig() {
        float margin = DeviceUtil.dip2px(mContext, 10);
        mTopBanner.setImageLoader(new BannerImageLoader())
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setOffscreenPageLimit(2)
                .setPageMargin(30)
                .setBannerStyle(BannerConfig.RECTANGLE_INDICATOR)
                .setViewPageMargin((int) margin, 0, (int) margin, 0)
                .setBannerAnimation(Transformer.Default)
                .setIndicatorGravity(BannerConfig.CENTER)
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        if (getParentFragment() instanceof TabFragmentHome) {
                            TabFragmentHome parentFragment = (TabFragmentHome) getParentFragment();
                            parentFragment.setAppBarLayoutColor(mBannerList.get(position).getColor());
                            mAreRectView.setEndColorInvalidate(mBannerList.get(position).getColor());
                        }

                    }
                })
                .start();
        mTopBanner.setOnBannerListener(position -> {
            //banner点击跳转
            LZLog.i(TAG, "setBannerConfig: click banner");
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            scheme.handleUrl(mContext, mBannerList.get(position).getLink());
        });

    }

    /**
     * 首页普通数据请求（非 好货严选）
     */
    private void requestMainData() {
        ApiService.getHomeApi().requestSelectionChannel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<PreciseSelectionBean>>() {
                    @Override
                    public void onNext(BaseBean<PreciseSelectionBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.data != null) {
                            PreciseSelectionBean bean = listBaseBean.data;
                            //banner
                            List<PreciseBannerIconBean> bannerList = bean.getBanner();
                            List<HomeBannerBean> banners = LzTransformationUtil.transformationBanner(bannerList);
                            setTopBannerUpdate(banners);
                            //icon
                            List<PreciseBannerIconBean> iconList = bean.getInner();
                            List<HomeIconBean> homeIconBeans = LzTransformationUtil.transformationIcon(iconList);
                            setIconUpdate(homeIconBeans);

                            //推荐 顶部图
                            List<PreciseBannerIconBean> topRecommendTop = bean.getRecommendActivityBanner();
                            setRecommendTopUpdate(topRecommendTop);
                            //推荐 列表
                            List<PreciseBannerIconBean> topRecommend = bean.getRecommendActivity();
                            setRecommendUpdate(topRecommend);

                            //抢购
                            List<PreciseBannerIconBean> topRushBuy = bean.getBuyActivity();
                            setTopRushBuyUpdate(topRushBuy);
                            //领券
                            List<PreciseBannerIconBean> bottomRushBuy = bean.getCouponActivity();
                            setBottomRushBuyUpdate(bottomRushBuy);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    //----------------请求 start-----------------------------
    private Disposable listDisposable;

    /**
     * 重置严选列表请求
     **/
    private void resetRequestList() {
        LZLog.i(TAG, "resetRequestList");
        if (getParentFragment() instanceof TabFragmentHome) {
            boolean initTabHomeFinish = ((TabFragmentHome) getParentFragment()).getInitTabHomeFinish();
            if (!initTabHomeFinish) {
                //tab 栏请求失败，这拉下刷新 球球
                ((TabFragmentHome) getParentFragment()).initData();
            }
        }
        start = 1;
        requestList(start, limit,true);
    }

    /**
     * 严选列表请求
     *
     * @param iStart
     * @param iLimit
     */
    private void requestList(final int iStart, final int iLimit,final boolean reset) {
        isloadingList = true;
        LZLog.i(TAG, "requestPreciseSelectionList requestList==success,iStart=" + iStart);
        if (reset){
            requestList00(iStart, iLimit);
            requestList01(iStart, iLimit);
            requestList02(iStart, iLimit);
            requestList03(iStart, iLimit);
            return;
        }
        switch (mStickHeaderHolder.getCurrentIndex()) {
            case 0:
                requestList00(iStart, iLimit);
                break;
            case 1:
                requestList01(iStart, iLimit);
                break;
            case 2:
                requestList02(iStart, iLimit);
                break;
            case 3:
                requestList03(iStart, iLimit);
                break;
        }

    }


    private void requestList00(final int iStart, final int iLimit) {
        String uniqueId = DeviceUtil.getImei(ContextHolder.getInstance().getContext());
        ApiService.getHomeApi().requestPreciseSelectionList(iStart, iLimit, uniqueId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<PreciseListBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        listDisposable = d;
                    }

                    @Override
                    public void onNext(BaseBean<List<PreciseListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        isloadingList = false;
                        if (listBaseBean.success()) {
                            LZLog.i(TAG, "requestPreciseSelectionList==success,iStart=" + iStart);
                            List<PreciseListBean> result = listBaseBean.getResult();
                            hasMore = !ArraysUtils.isListEmpty(result);
                            if (hasMore) {
                                setFootLoaderType(LoadType.LOADING);
                            } else {
                                if (iStart <= 1) {
                                    setFootLoaderType(LoadType.NO_DATA);
                                } else {
                                    setFootLoaderType(LoadType.NO_MORE_DATA);
                                }
                            }
                            if (iStart <= 1) {
                                mBottomListAdapter.setListData(result);
                                mBottomListAdapter.notifyDataSetChanged();
                                mSmartRefreshLayout.finishRefresh();
                            } else {
                                mBottomListAdapter.appendListData(result);
                                mBottomListAdapter.notifyDataSetChanged();
                            }
                            start++;
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        isloadingList = false;
                        if (iStart <= 1) {
                            setFootLoaderType(LoadType.NO_DATA);
                            mSmartRefreshLayout.finishRefresh(false);
                        } else {
                            mSmartRefreshLayout.finishLoadMore(false);
                            setFootLoaderType(LoadType.LOADING);
                        }
                    }
                });
    }

    private void requestList01(int iStart, int iLimit) {
        //达人推荐
        ApiService.getHomeApi().requestPreciseDarenRecommendList(iStart, iLimit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<PreciseListBean>>>() {


                    @Override
                    public void onNext(BaseBean<List<PreciseListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        isloadingList = false;
                        if (listBaseBean.success()) {
                            LZLog.i(TAG, "requestPreciseDarenRecommendList==success,iStart=" + iStart);
                            List<PreciseListBean> result = listBaseBean.getResult();
                            hasMore = !ArraysUtils.isListEmpty(result);
                            if (hasMore) {
                                setFootLoaderType(LoadType.LOADING);
                            } else {
                                if (iStart <= 1) {
                                    setFootLoaderType(LoadType.NO_DATA);
                                } else {
                                    setFootLoaderType(LoadType.NO_MORE_DATA);
                                }
                            }
                            if (iStart <= 1) {
                                mBottomRecommendListAdapter.setListData(result);
                                mBottomRecommendListAdapter.notifyDataSetChanged();
                                mSmartRefreshLayout.finishRefresh();
                            } else {
                                mBottomRecommendListAdapter.appendListData(result);
                                mBottomRecommendListAdapter.notifyDataSetChanged();
                            }
                            start++;
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        isloadingList = false;
                        if (iStart <= 1) {
                            setFootLoaderType(LoadType.NO_DATA);
                            mSmartRefreshLayout.finishRefresh(false);
                        } else {
                            mSmartRefreshLayout.finishLoadMore(false);
                            setFootLoaderType(LoadType.LOADING);
                        }
                    }
                });
    }


    private void requestList02(final int iStart, final int iLimit) {

        ApiService.getActivityApi().requestPreciseShakeCouponList(0, iStart, iLimit, "",1).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<ShakeCouponBean>>>() {

                    @Override
                    public void onNext(BaseBean<List<ShakeCouponBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        isloadingList = false;
                        if (listBaseBean.success()) {
                            LZLog.i(TAG, "requestPreciseShakeCouponList==success,iStart=" + iStart);
                            List<ShakeCouponBean> result = listBaseBean.getResult();
                            hasMore = !ArraysUtils.isListEmpty(result);
                            if (hasMore) {
                                setFootLoaderType(LoadType.LOADING);
                            } else {
                                if (iStart <= 1) {
                                    setFootLoaderType(LoadType.NO_DATA);
                                } else {
                                    setFootLoaderType(LoadType.NO_MORE_DATA);
                                }
                            }
                            if (iStart <= 1) {
                                mBottomShakeListAdapter.setListData(result);
                                mBottomShakeListAdapter.notifyDataSetChanged();
                                mSmartRefreshLayout.finishRefresh();
                            } else {
                                mBottomShakeListAdapter.appendListData(result);
                                mBottomShakeListAdapter.notifyDataSetChanged();
                            }
                            start++;
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        isloadingList = false;
                        if (iStart <= 1) {
                            setFootLoaderType(LoadType.NO_DATA);
                            mSmartRefreshLayout.finishRefresh(false);
                        } else {
                            mSmartRefreshLayout.finishLoadMore(false);
                            setFootLoaderType(LoadType.LOADING);

                        }
                    }
                });
    }

    /**
     * 请求种草
     * @param iStart
     * @param iLimit
     */
    private void requestList03(int iStart, int iLimit) {
        ApiService.getHomeApi().requestGetGuideArticleList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<GuideArticleBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<GuideArticleBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        isloadingList = false;
                        if (listBaseBean.success()) {
                            LZLog.i(TAG, "requestGetGuideArticleList==success");
                            List<GuideArticleBean> result = listBaseBean.getResult();
                            if (iStart <= 1) {
                                mBottomWantToBuyListAdapter.setListData(result);
                                mBottomWantToBuyListAdapter.notifyDataSetChanged();
                                mSmartRefreshLayout.finishRefresh();
                            } else {
                                mBottomWantToBuyListAdapter.appendListData(result);
                                mBottomWantToBuyListAdapter.notifyDataSetChanged();
                            }
                        }
                        setFootLoaderType(LoadType.NO_DATA);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        isloadingList = false;
                        setFootLoaderType(LoadType.NO_DATA);
                        mSmartRefreshLayout.finishRefresh(false);
                    }
                });
    }


    /**
     * 刷新banner数据
     *
     * @param bannerList
     */
    public void setTopBannerUpdate(List<HomeBannerBean> bannerList) {
        LZLog.i(TAG, "setTopBannerUpdate==");
        if (!ArraysUtils.isListEmpty(bannerList)) {
            mBannerList = bannerList;
            if (mTopBanner != null && mBannerIconAdapter != null) {
                List<String> bannerPath = new ArrayList<>();
                for (HomeBannerBean bean : bannerList) {
                    bannerPath.add(bean.getImg());
                }
                mTopBanner.update(bannerPath);
                mBannerIconAdapter.notifyDataSetChanged();
            }
        }
    }
//----------------请求 end-----------------------------

    /**
     * 刷新 icon
     *
     * @param iconList
     */
    public void setIconUpdate(List<HomeIconBean> iconList) {
        if (!ArraysUtils.isListEmpty(iconList)) {
            if (mHomeToolsLayout != null && mBannerIconAdapter != null) {
                mHomeToolsLayout.setToolsType(HomeToolsLayout.TOOLS_TYPE_NORMAL);
                mHomeToolsLayout.initNetWorkIcon(iconList);
                mBannerIconAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 推荐顶部 大图
     *
     * @param beanList
     */
    public void setRecommendTopUpdate(List<PreciseBannerIconBean> beanList) {
        LZLog.i(TAG, "setRecommendTopUpdate==");
        if (!ArraysUtils.isListEmpty(beanList) && mRecommendTopImgAdapter != null) {
            if (mTopRecommend.getLayoutParams() == null) {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mTopRecommend.setLayoutParams(layoutParams);
            }
            mTopRecommend.getLayoutParams().height = DeviceUtil.dip2px(mContext, 108);
            PreciseBannerIconBean iconBean = (PreciseBannerIconBean) beanList.get(0);
            ImageView mTopIv = mRecommendTopImgAdapter.getHeaderView().findViewById(R.id.top_iv);
            mTopIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ISchemeManager schemeManager = (ISchemeManager) ComponentManager.getInstance()
                            .getManager(ISchemeManager.class.getName());
                    if (schemeManager != null) {
                        if (iconBean != null) {
                            schemeManager.handleUrl(mContext, iconBean.getUrl());
                        }
                    }
                }
            });
            if (mTopIv != null && iconBean != null) {
                try {
                    new GlideImageLoader(mContext, iconBean.getImg())
                            .placeholder(R.drawable.empty_pic_list_h)
                            .error(R.drawable.empty_pic_list_h)
                            .into(mTopIv);
                    //对下面一个的背景设置
                    mRecommendImgHelper.setBgColor(Color.parseColor(iconBean.getBannerColor()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mRecommendTopImgAdapter.notifyDataSetChanged();
        } else {
            if (mTopRecommend != null) {
                if (mTopRecommend.getLayoutParams() == null) {
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mTopRecommend.setLayoutParams(layoutParams);
                }
                mTopRecommend.getLayoutParams().height = 0;
            }
        }
    }

    /**
     * 刷新 推荐模块图
     *
     * @param beanList
     */
    public void setRecommendUpdate(List<PreciseBannerIconBean> beanList) {
        LZLog.i(TAG, "setRecommendUpdate==");
        if (!ArraysUtils.isListEmpty(beanList) && mRecommendImgAdapter != null) {
            mRecommendList = beanList;
            mRecommendImgAdapter.setListData(mRecommendList);
            mRecommendImgAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新网格 抢购列表图  2列
     *
     * @param beanList
     */
    public void setTopRushBuyUpdate(List<PreciseBannerIconBean> beanList) {
        if (!ArraysUtils.isListEmpty(beanList)) {
            if (mRushBuy2Adapter != null) {
                mRushBuy2Adapter.setListData(beanList);
                mRushBuy2Adapter.notifyDataSetChanged();
            }
        }
    }


    /**
     * 刷新橫向 领券列表图  4列
     *
     * @param beanList
     */
    public void setBottomRushBuyUpdate(List<PreciseBannerIconBean> beanList) {
        if (!ArraysUtils.isListEmpty(beanList)) {
            if (beanList.size() > 4) {
                beanList = beanList.subList(0, 4);
            }
            if (mRushBuy4Adapter != null) {
                mRushBuy4Adapter.setListData(beanList);
                mRushBuy4Adapter.notifyDataSetChanged();
            }
        }
    }

    private void setFootLoaderType(@LoadType int type) {
        if (mFooterView == null) {
            return;
        }
        switch (type) {
            case LoadType.LOADING:
                mLoadingLayout.setVisibility(View.VISIBLE);
                mNoMoreLayout.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.GONE);
                break;
            case LoadType.NO_DATA:
                mLoadingLayout.setVisibility(View.GONE);
                mNoMoreLayout.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.VISIBLE);
                break;
            case LoadType.NO_MORE_DATA:
                mLoadingLayout.setVisibility(View.GONE);
                mNoMoreLayout.setVisibility(View.VISIBLE);
                mNoDataLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onDetach() {
        if (listDisposable != null) {
            LZLog.i(TAG, "onDetach");
            if (!listDisposable.isDisposed()) {
                listDisposable.dispose();
            }
        }
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if ( limitSkillHeaderHolder!=null){
            limitSkillHeaderHolder.onDestroyView();
        }
    }
}
