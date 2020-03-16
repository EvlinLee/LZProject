package com.by.lizhiyoupin.app.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_banner.Banner;
import com.by.lizhiyoupin.app.component_banner.BannerConfig;
import com.by.lizhiyoupin.app.component_banner.Transformer;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.BannerImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseHeaderDelegateAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfDelegateAdapter;
import com.by.lizhiyoupin.app.io.LzTransformationUtil;
import com.by.lizhiyoupin.app.io.bean.CommonCategoryBean;
import com.by.lizhiyoupin.app.io.bean.CommonSecondBean;
import com.by.lizhiyoupin.app.io.bean.HomeBannerBean;
import com.by.lizhiyoupin.app.io.bean.HomeIconBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.main.adapter.HomeOtherSortListAdapter;
import com.by.lizhiyoupin.app.main.contract.HomeCommonContract;
import com.by.lizhiyoupin.app.main.presenter.HomeCommonPresenter;
import com.by.lizhiyoupin.app.main.weight.IconToolsLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/2 19:46
 * Summary: 首页 其他tab下页面 本地库数据
 */
public class HomeOtherFragment extends BaseFragment implements HomeCommonContract.HomeCommonView {
    public static final String TAG = HomeOtherFragment.class.getSimpleName();
    public static final int ITEM_TYPE_OF_BANNER = 401;
    public static final int ITEM_TYPE_OF_JUMP_TOP_IMG = 402;


    private RecyclerView mRecyclerView;
    private ImageView mJumpTopIv;
    private SmartRefreshLayout mSmartRefreshLayout;
    private Banner mTopBanner;
    private HomeCommonContract.HomeCommonPresenter mHomeCommonPresenter;


    private VirtualLayoutManager mLayoutManager;
    private DelegateAdapter mDelegateAdapter;
    private List<DelegateAdapter.Adapter> mChildAdapters;

    private Context mContext;
    private IconToolsLayout mIconToolsLayout;
    private List<HomeBannerBean> mBannerList;


    private BaseHeaderDelegateAdapter mBannerIconAdapter;
    private long tabId = -1;

    private BaseOfDelegateAdapter mHeaderAdapter;
    private HomeOtherSortListAdapter mBottomListAdapter;
    private HomeOtherHeaderHolder mHeaderHolder;
    private GridLayoutHelper mSortGridHelper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.v_layout_refresh_layout, container, false);
        mHomeCommonPresenter = new HomeCommonPresenter(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            tabId = arguments.getLong(CommonConst.KEY_FIRST_COMMODITY_ID, -1);
        }
        initView(inflate);
        resetRequestList();
        return inflate;
    }
    /**
     * 是否可以开启下拉刷新
     * @param enable
     */
    public void setEnableRefresh(boolean enable){
        if (mSmartRefreshLayout!=null){
            mSmartRefreshLayout.setEnableRefresh(enable);
        }
    }
    /**
     * 重置 加载数据
     */
    private void resetRequestList() {
        mHomeCommonPresenter.requestSecondBannerIcon(tabId);
        mHeaderHolder.requestSortList(1);
    }

    private void initView(View root) {
        mSmartRefreshLayout = root.findViewById(R.id.smartRefreshLayout);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mJumpTopIv = root.findViewById(R.id.jump_top_iv);
        setSmartRefreshLayout();
// 解决RecyclerView抢焦点把自己置于屏幕中央的问题
        mRecyclerView.setFocusable(false);
        mChildAdapters = new LinkedList<>();
        // 创建VirtualLayoutManager对象
        mLayoutManager = new VirtualLayoutManager(mContext);
        // 同时内部会创建一个 LayoutHelperFinder 对象，用来后续的LayoutHelper查找
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 将VirtualLayoutManager绑定到recyclerView

        /**
         * 步骤2：设置组件复用回收池
         *  正对不同type设置不同的复用池
         * */
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecyclerView.setRecycledViewPool(viewPool);

        //当hasConsistItemType=true的时候，不论是不是属于同一个子adapter，相同类型的item都能复用。表示它们共享一个类型。
        // 当hasConsistItemType=false的时候，不同子adapter之间的类型不共享
        mDelegateAdapter = new DelegateAdapter(mLayoutManager, false);//主Adapter
        mRecyclerView.setAdapter(mDelegateAdapter);
        viewPool.setMaxRecycledViews(ITEM_TYPE_OF_BANNER, 4);//type=ITEM_TYPE_OF_BANNER 的item设置了复用池的大小
        viewPool.setMaxRecycledViews(HomeOtherSortListAdapter.TYPE_OTHER, 10);//  item设置了复用池的大小
        viewPool.setMaxRecycledViews(HomeOtherSortListAdapter.TYPE_SINGLE, 10);// item设置了复用池的大小


        initBannerIconView();
        initSortListContentView();

        //最后将子adapter添加到主adapter
        mDelegateAdapter.addAdapters(mChildAdapters);


        mJumpTopIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutManager.scrollToPosition(0);
                mJumpTopIv.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 粘性头
     */
    private void initSortListHeaderView() {
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

        mHeaderAdapter = new BaseHeaderDelegateAdapter(mContext, stickyLayoutHelper);
        //
        mHeaderHolder = new HomeOtherHeaderHolder();
        mHeaderAdapter.setHeaderView(mHeaderHolder.getOnCreateView(mContext, tabId, mBottomListAdapter,
                mLayoutManager,  mSmartRefreshLayout,mRecyclerView,mJumpTopIv));
        mChildAdapters.add(mHeaderAdapter);

    }

    /**
     * 商品列表
     */
    private void initSortListContentView() {
        int margin = DeviceUtil.dip2px(mContext, 5);
        int margin10 = DeviceUtil.dip2px(mContext, 10);
        mSortGridHelper = new GridLayoutHelper(2);

        mSortGridHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                return mBottomListAdapter.getType()==HomeOtherSortListAdapter.TYPE_SINGLE?
                        2:(mDelegateAdapter.getItemCount()-1==position?2:1);
            }
        });
        mSortGridHelper.setAutoExpand(false);//当一行里视图的个数少于spanCount值的时候，如果autoExpand为true，视图的总宽度会填满可用区域；否则会在屏幕上留空白区域
        mBottomListAdapter = new HomeOtherSortListAdapter(mContext, mSortGridHelper, -1);
        mBottomListAdapter.setType(HomeOtherSortListAdapter.TYPE_OTHER);
        mSortGridHelper.setHGap(margin);
        mSortGridHelper.setVGap(margin);
        mSortGridHelper.setMargin(margin10, margin10, margin10, margin10);
        //顺序不要变
        initSortListHeaderView();

        //要后加list列表
        mChildAdapters.add(mBottomListAdapter);

    }


    private void setSmartRefreshLayout() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
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
        inflate.findViewById(R.id.arc_rectView).setVisibility(View.GONE);
        inflate.findViewById(R.id.icon_list_view).setVisibility(View.VISIBLE);
        mTopBanner.setVisibility(View.GONE);
        mIconToolsLayout = inflate.findViewById(R.id.iconToolsLayout);
        setBannerConfig();
        //创建 LayoutHelper
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        mBannerIconAdapter = new BaseHeaderDelegateAdapter(mContext, linearLayoutHelper);
        mBannerIconAdapter.setHeaderView(inflate);
        //把轮播器添加到集合

        mChildAdapters.add(mBannerIconAdapter);
    }

    private void setBannerConfig() {
        float margin = DeviceUtil.dip2px(mContext, 10);
        mTopBanner.setImageLoader(new BannerImageLoader())
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setOffscreenPageLimit(2)
                .setBannerStyle(BannerConfig.RECTANGLE_INDICATOR)
                .setPageMargin(30)
                .setViewPageMargin((int) margin, 0, (int) margin, 0)
                .setBannerAnimation(Transformer.Default)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        mTopBanner.setOnBannerListener(position -> {
            //banner点击跳转
            LZLog.i(TAG, "setBannerConfig: click banner");
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            scheme.handleUrl(mContext, mBannerList.get(position).getLink());
        });

    }

    public void setTopBannerUpdate(List<HomeBannerBean> bannerList) {
        if (!ArraysUtils.isListEmpty(bannerList)) {
            mBannerList = bannerList;
            if (mTopBanner != null) {
                List<String> bannerPath = new ArrayList<>();
                for (HomeBannerBean bean : mBannerList) {
                    LZLog.i(TAG, "(bean.getImg()" + bean.getImg());
                    bannerPath.add(bean.getImg());
                }
                if (bannerPath.size()>0){
                    mTopBanner.setVisibility(View.VISIBLE);
                }else {
                    mTopBanner.setVisibility(View.GONE);
                }
                mTopBanner.update(bannerPath);
            }
        }
    }

    public void setIconUpdate(List<HomeIconBean> iconList) {
        if (!ArraysUtils.isListEmpty(iconList)) {
            if (mIconToolsLayout != null) {
                mIconToolsLayout.initNetWorkIcon(iconList);
            }
        }
    }

    @Override
    public void requestSecondBannerIconSuccess(CommonSecondBean secondBean) {
        LZLog.i(TAG, "requestSecondBannerIconSuccess");
        //banner
        List<CommonCategoryBean> bannerList = secondBean.getBanner();
        List<HomeBannerBean> banners = LzTransformationUtil.transformation2Banner(bannerList);
        setTopBannerUpdate(banners);
        //icon
        List<CommonCategoryBean> iconList = secondBean.getSecond();
        List<HomeIconBean> icList = LzTransformationUtil.transformation2Icon(iconList);
        setIconUpdate(icList);
    }

    @Override
    public void requestSecondBannerIconError(Throwable throwable) {
        LZLog.i(TAG, "requestSecondBannerIconError" + throwable);
    }
}
