package com.by.lizhiyoupin.app.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.io.bean.CommonCategoryBean;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.by.lizhiyoupin.app.main.adapter.HomeFragmentPagerAdapter;
import com.by.lizhiyoupin.app.main.contract.HomeContract;
import com.by.lizhiyoupin.app.main.manager.TabFragmentManager;
import com.by.lizhiyoupin.app.main.presenter.TabFragmentHomePresenter;
import com.by.lizhiyoupin.app.manager.AccountManager;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.search.activity.SearchActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/26 14:37
 * Summary: 首页 主
 */
public class TabFragmentHome extends BaseFragment implements CommonFragmentPagerAdapter.FragmentFactoryCallback,
        View.OnClickListener, HomeContract.TabFragmentHomeView, TabFragmentManager.SetSubPageIndex {
    public static final String TAG = TabFragmentHome.class.getSimpleName();

    private TabLayout mTabLayout;
    private View mTabHeaderCl;
    private AppBarLayout mAppBarLt;
    private ViewPager mHomeViewpager;
    private TextView mActionBarTitle;
    private TextView mActionBarRight;
    private TextView mSearchTv;

    private Context mContext;
    private HomeContract.TabFragmentHomePresenter mHomePresenter;
    private List<CommonCategoryBean> mCategoryTitleList;

    private HomeFragmentPagerAdapter mPagerAdapter;
    private int currentStatus = -1;//mAppBarLt 滑到顶部改变 状态栏颜色 1表示滑到顶部
    private int currentSlide = -1;//mAppBarLt  1表示mAppBarLt向上滑动了，2表示向下滑动
    private int mAppBarLtColor = Color.RED;
    private View mEtcOther;
    // 初始化 省钱页tab 完成
    private boolean initTabHomeFinish;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_home_layout, container, false);
        StatusBarUtils.setTranslucentForImageViewInFragment(getActivity(), 255, inflate.findViewById(R.id.app_bar_lt));
        StatusBarUtils.addTranslucentColorView(getActivity(), mAppBarLtColor, 255);
        mHomePresenter = new TabFragmentHomePresenter(this);
        initTabHomeFinish = false;
        initView(inflate);
        initData();
        return inflate;
    }

    protected void initData() {
        showLoadingDialog();
        //获取类目列表
        mHomePresenter.requestCommodityKindList();
    }

    public void setInitTabHomeFinish() {
        initTabHomeFinish = true;
    }

    public boolean getInitTabHomeFinish() {
        return initTabHomeFinish;
    }

    @Override
    public void onResume() {
        super.onResume();
        LZLog.i(TAG, "onResume==");
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        LZLog.i(TAG, "onUserVisible");
        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            int currentTab = ((MainActivity) activity).mTabManager.getCurrentTab();
            if (currentTab == TabFragmentManager.TAB_HOME) {
                DeviceUtil.setStatusBarWordsColor(getActivity(), false);
                mAppBarLt.setBackgroundColor(mAppBarLtColor);
                StatusBarUtils.addTranslucentColorView(getActivity(), mAppBarLtColor, 255);
            }
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        LZLog.i(TAG, "onUserInvisible");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //控制viewpager内fragment生命周期
        if (mPagerAdapter != null) {
            mPagerAdapter.setChildUserVisibleHint(!hidden);
        }
    }

    /**
     * 改变顶部bar颜色
     *
     * @param color
     */
    public void setAppBarLayoutColor(String color) {
        try {
            mAppBarLtColor = Color.parseColor(color);
            if (currentStatus == 1) {
                //上拉到顶部，不让变色
                return;
            }
            mAppBarLt.setBackgroundColor(mAppBarLtColor);
            StatusBarUtils.addTranslucentColorView(getActivity(), mAppBarLtColor, 255);
        } catch (Exception e) {
            e.printStackTrace();
            mAppBarLtColor = Color.RED;
        }
    }


    private void initView(View root) {
        root.findViewById(R.id.actionbar_back_tv).setVisibility(View.GONE);
        mTabLayout = root.findViewById(R.id.home_tabLayout);
        mTabHeaderCl = root.findViewById(R.id.tab_header_cl);
        mAppBarLt = root.findViewById(R.id.app_bar_lt);
        mActionBarTitle = root.findViewById(R.id.actionbar_title_tv);
        mActionBarRight = root.findViewById(R.id.actionbar_right_tv);
        mEtcOther = root.findViewById(R.id.etc_other);
        mEtcOther.setOnClickListener(this);
        int top = DeviceUtil.dip2px(mContext, 95);
        mAppBarLt.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                if (i >= 0 && currentSlide != 1) {
                    if (mHomeViewpager!=null&&mPagerAdapter != null) {
                        LZLog.i(TAG, "上滑 改变====" + i);
                        currentSlide = 1;
                        int currentItem = mHomeViewpager.getCurrentItem();
                        Fragment childFragment = mPagerAdapter.mChildFragments[currentItem];
                        if (childFragment instanceof HomeOtherFragment) {
                            ((HomeOtherFragment) childFragment).setEnableRefresh(true);
                        } else if (childFragment instanceof PreciseNewFragment) {
                            ((PreciseNewFragment) childFragment).setEnableRefresh(true);
                        }
                    }

                } else if (i < 0 && currentSlide != 2) {
                    if (mHomeViewpager!=null&&mPagerAdapter != null) {
                        LZLog.i(TAG, "下滑 改变====" + i);
                        currentSlide = 2;
                        int currentItem = mHomeViewpager.getCurrentItem();
                        Fragment childFragment = mPagerAdapter.mChildFragments[currentItem];
                        if (childFragment instanceof HomeOtherFragment) {
                            ((HomeOtherFragment) childFragment).setEnableRefresh(false);
                        } else if (childFragment instanceof PreciseNewFragment) {
                            ((PreciseNewFragment) childFragment).setEnableRefresh(false);
                        }
                    }
                }
                if (i <= -top && currentStatus != 1) {
                    currentStatus = 1;
                    //滑到顶部
                    //StatusBarUtils.setTranslucentForImageViewInFragment(getActivity(),255 ,mAppBarLt);
                    //  StatusBarUtils.addTranslucentColorView(getActivity(),mAppBarLtColor, 255);
                    // DeviceUtil.setStatusBarWordsColor(getActivity(), ColorUtils.calculateLuminance(Color.WHITE)>0.5);
                } else if (i > -top && currentStatus != 2) {
                    //改变滑动状态的时候需要变色
                    currentStatus = 2;
                    mAppBarLt.setBackgroundColor(mAppBarLtColor);
                    StatusBarUtils.addTranslucentColorView(getActivity(), mAppBarLtColor, 255);
                }
            }
        });

        //搜索框

        mSearchTv = root.findViewById(R.id.home_search_tv);
        mSearchTv.setHint(R.string.home_search_top_hint_text);
        mSearchTv.setOnClickListener(this);
        mHomeViewpager = root.findViewById(R.id.home_viewpager);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mHomeViewpager.getLayoutParams();
        //v-layout多出了一个状态栏高度
        layoutParams.setMargins(0,0,0,DeviceUtil.getStatusBarHeight(mContext));
        mHomeViewpager.setLayoutParams(layoutParams);

        mActionBarRight.setOnClickListener(this);
        mActionBarTitle.setTextColor(Color.WHITE);
        mActionBarTitle.setText(getResources().getString(R.string.home_actionbat_title_text));
        mActionBarRight.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.system_chat), null, null, null);

        mHomeViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                LZLog.i(TAG, "onPageSelected==" + position);
            }
        });

    }

    private void initLocalTab() {
        ArrayList<CommonCategoryBean> localBeans = new ArrayList<>();
        CommonCategoryBean commonCategoryBean = new CommonCategoryBean();
        commonCategoryBean.setKindName("精选");
        localBeans.add(commonCategoryBean);
        refreshTabs(localBeans);
    }

    public void refreshTabs(List<CommonCategoryBean> tabs) {
        if (!TabFragmentHomePresenter.hasChanged(this.mCategoryTitleList, tabs)) {
            return;
        }
        this.mCategoryTitleList = tabs;
        List<String> titles = TabFragmentHomePresenter.names(mCategoryTitleList);

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
        mPagerAdapter = new HomeFragmentPagerAdapter(getChildFragmentManager(), titles == null ? 0 : titles.size(), titles, this);
        //防止触发一次多余的 tab select
        mTabLayout.setupWithViewPager(null);
        mHomeViewpager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mHomeViewpager);
        mHomeViewpager.setCurrentItem(0);


    }

    public int getCurrentPager() {
        if (mHomeViewpager != null) {
            return mHomeViewpager.getCurrentItem();
        } else {
            return 0;
        }
    }

    @Override
    public void setSubPageIndex(int subPageIndex, int childIndex) {
        if (mHomeViewpager != null && mPagerAdapter != null) {
            if (mPagerAdapter.getCount() > subPageIndex) {
                LZLog.i(TAG, "跳转==" + subPageIndex);
                mHomeViewpager.setCurrentItem(subPageIndex);
            }
        }
    }


    @Override
    public Fragment createFragment(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                //精选
                //fragment = new PreciseOfFragment();
                fragment = new PreciseNewFragment();
                break;
            default:
                fragment = new HomeOtherFragment();

                CommonCategoryBean categoryBean = mCategoryTitleList.get(index);
                Bundle bundle = new Bundle();
                bundle.putLong(CommonConst.KEY_FIRST_COMMODITY_ID, categoryBean.getId());
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_right_tv:
                //跳转到消息 页面
                if (shouldLoginFirst()) {
                    CommonSchemeJump.showActivity(mContext, "/app/InformationActivity");
                }
                break;
            case R.id.etc_other:
                setHomeMoreShow();
                break;
            case R.id.home_search_tv:
                CommonSchemeJump.showActivity(mContext, SearchActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 显示隐藏 更多tab
     */
    private void setHomeMoreShow() {
        int[] location = new int[2];
        mTabHeaderCl.getLocationOnScreen(location);
        int top=location[1]+mTabHeaderCl.getHeight()-DeviceUtil.getStatusBarHeight(mContext);
        //距离顶部距离+mTabHeaderCl的高-去状态栏高
        DiaLogManager.showHomeMoreDialog(getActivity(), mCategoryTitleList, top);
    }

    @Override
    public void requestCommodityKindListSuccess(List<CommonCategoryBean> beanList) {
        setInitTabHomeFinish();
        refreshTabs(beanList);
        dismissLoadingDialog();
    }

    @Override
    public void requestCommodityKindListError(Throwable throwable) {
        initLocalTab();
        dismissLoadingDialog();
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

}
