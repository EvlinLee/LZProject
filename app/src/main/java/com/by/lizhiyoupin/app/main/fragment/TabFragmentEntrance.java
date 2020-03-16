package com.by.lizhiyoupin.app.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.io.bean.ShopBannerBean;
import com.by.lizhiyoupin.app.io.bean.ShopMdBean;
import com.by.lizhiyoupin.app.io.bean.ShopMindBean;
import com.by.lizhiyoupin.app.io.bean.SuperAttionBean;
import com.by.lizhiyoupin.app.io.bean.SuperKindBean;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.by.lizhiyoupin.app.main.adapter.HomeFragmentPagerAdapter;
import com.by.lizhiyoupin.app.main.adapter.SuperBigAdapter;
import com.by.lizhiyoupin.app.main.contract.SuperContract;
import com.by.lizhiyoupin.app.main.manager.TabFragmentManager;
import com.by.lizhiyoupin.app.main.presenter.SuperPresenter;
import com.by.lizhiyoupin.app.weight.IncomePageTitleView;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/26 14:38
 * Summary:   超级导航
 */
public class TabFragmentEntrance extends BaseFragment implements SuperContract.SuperView,
        CommonFragmentPagerAdapter.FragmentFactoryCallback {
    public static final String TAG = TabFragmentEntrance.class.getSimpleName();
    private ViewPager fans_viewPager;
    private AppBarLayout mAppBarLt;
    private Context mContext;
    private List<String> mTabList;

    private HomeFragmentPagerAdapter mPagerAdapter;
    private RecyclerView super_icon;
    private SuperBigAdapter mBigAdapter;

    private GridLayoutManager mLinearLayoutManager;
    private SuperPresenter mPresenter = new SuperPresenter(this);
    private Fragment fragment;
    private Long id;
    private List<SuperKindBean> sist;
    private int currentStatus = -1;
    private MagicIndicator magicIndicator;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean canRefresh = true;
    private TextView toolbar_title;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_entrance_layout, container, false);
        mPresenter.requestSuperKind();
        mPresenter.requestSuperBanner();
        initView(root);


        return root;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        StatusBarUtils.addTranslucentColorView(getActivity(), Color.TRANSPARENT, 0);
        ImmersionBar.with(TabFragmentEntrance.this)
                .navigationBarColorInt(Color.WHITE)
                .navigationBarDarkIcon(true)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .statusBarColorInt(Color.TRANSPARENT)
                .init();

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            int currentTab = ((MainActivity) activity).mTabManager.getCurrentTab();
            if (currentTab == TabFragmentManager.TAB_ENTRANCE) {
                ImmersionBar.with(this).keyboardEnable(true)
                        .navigationBarColorInt(Color.WHITE)
                        .navigationBarDarkIcon(true)
                        .statusBarDarkFont(true)
                        .init();

                StatusBarUtils.addTranslucentColorView(getActivity(), currentStatus == 1 ?
                        Color.WHITE : Color.TRANSPARENT, currentStatus == 1 ? 255 : 0);
            }
        }


    }


    private void initView(View root) {
        mAppBarLt = root.findViewById(R.id.app_bar_lt);
        super_icon = root.findViewById(R.id.super_icon);
        fans_viewPager = root.findViewById(R.id.fans_viewPager);
        int titleoffset = DeviceUtil.getStatusBarHeight(mContext) + DeviceUtil.dip2px(mContext, 44);
        root.findViewById(R.id.toolbar).getLayoutParams().height = titleoffset;
        magicIndicator = root.findViewById(R.id.magicIndicator);
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);//刷新
        toolbar_title = root.findViewById(R.id.toolbar_title);
        FrameLayout.LayoutParams layoutParams =
                (FrameLayout.LayoutParams) toolbar_title.getLayoutParams();
        layoutParams.height = titleoffset;
        toolbar_title.setLayoutParams(layoutParams);
        toolbar_title.setPadding(0, DeviceUtil.getStatusBarHeight(mContext), 0, 0);
        toolbar_title.setBackgroundColor(Color.argb(0, 255, 255, 255));
        mTabList = new ArrayList<>();
        int top = DeviceUtil.dip2px(mContext, 350) - DeviceUtil.getStatusBarHeight(mContext);
        mAppBarLt.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i <= -top && currentStatus != 1) {
                    currentStatus = 1;
                    LZLog.i(TAG, "onOffsetChanged" + i);
                    ImmersionBar.with(TabFragmentEntrance.this)
                            .navigationBarColorInt(Color.WHITE)
                            .navigationBarDarkIcon(true)
                            .keyboardEnable(true)
                            .statusBarDarkFont(true)
                            .init();
                    StatusBarUtils.addTranslucentColorView(getActivity(), Color.WHITE, 255);

                    //滑到顶部
                } else if (i > -top && currentStatus != 2) {
                    //改变滑动状态的时候需要变色
                    currentStatus = 2;
                    LZLog.i(TAG, "onOffsetChangedssss" + i);
                    ImmersionBar.with(TabFragmentEntrance.this)
                            .navigationBarColorInt(Color.WHITE)
                            .navigationBarDarkIcon(true)
                            .keyboardEnable(true)
                            .statusBarDarkFont(true)
                            .init();
                    StatusBarUtils.addTranslucentColorView(getActivity(), Color.TRANSPARENT, 0);

                }

                if (i < -50 && canRefresh) {
                    mSwipeRefreshLayout.setEnabled(false);//设置可触发
                    canRefresh = false;
                } else if (i > -50 && !canRefresh) {
                    canRefresh = true;
                    mSwipeRefreshLayout.setEnabled(true);
                }


                float titley = Math.abs(i) * 1f / titleoffset;

                if (titley > 1) {
                    titley = 1;

                }
                toolbar_title.setBackgroundColor(Color.argb((int) (titley * 255), 255, 255, 255));

            }
        });


        initRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestSuperBanner();
                if (mPagerAdapter != null) {
                    int currentItem = fans_viewPager.getCurrentItem();
                    Fragment[] childFragments = mPagerAdapter.mChildFragments;
                    SuperStoreFragment superStoreFragment =
                            (SuperStoreFragment) childFragments[currentItem];
                    superStoreFragment.refresh();
                }
            }
        });

        super_icon.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mSwipeRefreshLayout.setEnabled(mLinearLayoutManager.findFirstVisibleItemPosition() == 0);
            }
        });


    }

    private void initIndicator(List<String> tabList) {

        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdjustMode(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return tabList == null ? 0 : tabList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                IncomePageTitleView pagerTitleView = new IncomePageTitleView(context);
                int padding = DeviceUtil.dip2px(getActivity(), 18);
                pagerTitleView.setPadding(padding, 0, padding, 0);
                pagerTitleView.setNormalColor(getResources().getColor(R.color.color_FF555555));
                pagerTitleView.setSelectedColor(getResources().getColor(R.color.color_FFFF005F));
                pagerTitleView.setTypeface(Typeface.DEFAULT);
                pagerTitleView.setTextSize(14);
                pagerTitleView.setText(tabList.get(index));
                pagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fans_viewPager.setCurrentItem(index);
                    }
                });
                return pagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setYOffset(DeviceUtil.dip2px(context, 2));
                indicator.setLineWidth(DeviceUtil.dip2px(context, 20));
                indicator.setRoundRadius(DeviceUtil.dip2px(context, 1));
                indicator.setColors(Color.parseColor("#FFFF005F"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, fans_viewPager);
        //添加页面滑动监听,控制 SwipeRefreshLayout与ViewPager滑动冲突
        fans_viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == 1) {
                    mSwipeRefreshLayout.setEnabled(false);//设置不可触发
                } else if (state == 2 && canRefresh) {
                    mSwipeRefreshLayout.setEnabled(true);//设置可触发
                }
            }
        });
    }

    @Override
    public Fragment createFragment(int index) {
        fragment = new SuperStoreFragment();
        SuperKindBean superKindBean = sist.get(index);
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_FANS_LIST_TYPE, String.valueOf(superKindBean.getId()));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void requestSuperKindSuccess(List<SuperKindBean> list) {
        superKind(list);
    }

    private void superKind(List<SuperKindBean> list) {
        for (int i = 0; i < list.size(); i++) {
            mTabList.add(list.get(i).getKindName());
        }
        this.sist = list;
        if (mPagerAdapter != null && mPagerAdapter.mChildFragments != null) {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment fragment : mPagerAdapter.mChildFragments) {
                if (fragment == null) {
                    continue;
                }
                ft.detach(fragment);
            }
            ft.commitNow();
        }
        mPagerAdapter = new HomeFragmentPagerAdapter(getChildFragmentManager(), mTabList == null
                ? 0 : mTabList.size(), mTabList, this);
        fans_viewPager.setAdapter(mPagerAdapter);
        fans_viewPager.setCurrentItem(0);
        fans_viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {//滑动监听
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                id = list.get(position).getId();
            }
        });
        initIndicator(mTabList);
    }

    @Override
    public void requestSuperKindError(Throwable throwable) {

    }

    @Override
    public void requestShopKindSuccess(ShopMindBean list) {

    }

    @Override
    public void requestShopKindError(Throwable throwable) {

    }

    @Override
    public void requestFollowShopSuccess(SuperAttionBean bean) {

    }

    @Override
    public void requestFollowShopError(Throwable throwable) {

    }

    @Override
    public void requestShopGoodsSuccess(ShopMdBean list) {

    }

    @Override
    public void requestShopGoodsError(Throwable throwable) {

    }

    @Override
    public void requestShopBannerSuccess(List<ShopBannerBean> list) {
        shopBanner(list);

    }

    private void shopBanner(List<ShopBannerBean> list) {
        mSwipeRefreshLayout.setRefreshing(false);
        mLinearLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.HORIZONTAL,
                false);
        super_icon.setLayoutManager(mLinearLayoutManager);
        mBigAdapter = new SuperBigAdapter(getActivity(), list);
        super_icon.setAdapter(mBigAdapter);
    }


    @Override
    public void requestShopBannerError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
