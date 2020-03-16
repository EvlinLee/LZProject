package com.by.lizhiyoupin.app.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.io.bean.FindCircleTabListBean;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.by.lizhiyoupin.app.main.findcircle.contract.FindCircleContract;
import com.by.lizhiyoupin.app.main.findcircle.fragment.DailyExplosionsFragment;
import com.by.lizhiyoupin.app.main.findcircle.fragment.FindCircleBusinessSchoolFragment;
import com.by.lizhiyoupin.app.main.findcircle.fragment.FindCircleDailyFragment;
import com.by.lizhiyoupin.app.main.findcircle.presenter.FindCirclelPresenter;
import com.by.lizhiyoupin.app.main.manager.TabFragmentManager;
import com.by.lizhiyoupin.app.weight.CommonNavigatorWrap;
import com.by.lizhiyoupin.app.weight.IncomePageTitleView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.gyf.immersionbar.ImmersionBar;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;

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
 * Summary: 发圈 主页
 */
public class TabFragmentFind extends BaseFragment implements FindCircleContract.FindCircleView,
        CommonFragmentPagerAdapter.FragmentFactoryCallback, TabFragmentManager.SetSubPageIndex {
    public static final String TAG = TabFragmentFind.class.getSimpleName();
    private MagicIndicator mTopMagicIndicator;
    private ViewPager mViewPager;
    private Context mContext;
    private FindCirclelPresenter mFindCirclelPresenter;
    private List<FindCircleTabListBean> mTitleList;
    private CommonFragmentPagerAdapter mPagerAdapter;
    private boolean isInitFinish = false;
    private int mSubPageIndex = -1;
    private VideoViewManager mVideoViewManager;
    private boolean loadFirstSuccess=false;//标记初始化请求数据
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_tab_find_layout, container, false);
        DeviceUtil.setStatusBarHeight(getActivity(), inflate.findViewById(R.id.status_bar_view));
        mFindCirclelPresenter = new FindCirclelPresenter(this);
        mVideoViewManager=VideoViewManager.instance();
        initView(inflate);
        requestFirstLevelList();
        return inflate;
    }

    private void initView(View root) {
        mTopMagicIndicator = root.findViewById(R.id.top_magicIndicator);
        mViewPager = root.findViewById(R.id.viewPager);
    }

    public void requestFirstLevelList(){
        mFindCirclelPresenter.requestFirstLevelList();
    }
    /**
     * 是否加载tab初始化成功
     * @return
     */
    public boolean getInitLoadingTabSuccess(){
        return loadFirstSuccess;
    }
    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();

        StatusBarUtils.addTranslucentColorView(getActivity(), Color.TRANSPARENT, 0);
        ImmersionBar.with(this)
                .navigationBarColorInt(Color.WHITE)
                .navigationBarDarkIcon(true)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .statusBarColorInt(Color.TRANSPARENT)
                .init();
    }

    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        LZLog.i(TAG,"onFirstUserInvisible");
        mVideoViewManager.release();
    }
    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        LZLog.i(TAG,"onUserInvisible");
        mVideoViewManager.release();
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity){
            int currentTab = ((MainActivity) activity).mTabManager.getCurrentTab();
            if (currentTab== TabFragmentManager.TAB_FIND){
                StatusBarUtils.addTranslucentColorView(getActivity(), Color.TRANSPARENT, 0);
                ImmersionBar.with(this)
                        .navigationBarColorInt(Color.WHITE)
                        .navigationBarDarkIcon(true)
                        .keyboardEnable(true)
                        .statusBarDarkFont(true)
                        .statusBarColorInt(Color.TRANSPARENT)
                        .init();
            }
        }


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //控制viewpager内fragment生命周期
        if (mPagerAdapter != null) {
            mPagerAdapter.setChildUserVisibleHint(!hidden);
        }
    }
    public static boolean hasChanged(List<FindCircleTabListBean> oldTabs, List<FindCircleTabListBean> newTabs) {
        if (newTabs == null || newTabs.size() == 0) return false;//没有新数据，认为数据没有改变
        if (oldTabs == null || oldTabs.size() == 0) return true;
        if (oldTabs.size() != newTabs.size()) return true;
        for (int i = 0, size = oldTabs.size(); i < size; i++) {
            FindCircleTabListBean oldTab = oldTabs.get(i);
            FindCircleTabListBean newTab = newTabs.get(i);
            if (oldTab.getId() != newTab.getId()) return true;
            if (!TextUtils.equals(oldTab.getKindName(), newTab.getKindName())) return true;
        }
        return false;
    }

    private void initIndicator(List<FindCircleTabListBean> titleList) {
        if (!hasChanged(this.mTitleList, titleList)) {
            return;
        }
        mTitleList=titleList;
        mViewPager.setAdapter(null);
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

        mPagerAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), mTitleList==null?0:mTitleList.size(), this);
        mViewPager.setAdapter(mPagerAdapter);

        CommonNavigatorWrap commonNavigator = new CommonNavigatorWrap(mContext);
        commonNavigator.setAdjustMode(false);//宽度自适应还是可滑动，false可滑动
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitleList == null ? 0 : mTitleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                IncomePageTitleView pagerTitleView = new IncomePageTitleView(context) {
                    @Override
                    public int getContentLeft() {
                        if (index == 0) {
                            return super.getContentLeft() + DeviceUtil.dip2px(mContext, 20);
                        } else {
                            return super.getContentLeft() + DeviceUtil.dip2px(mContext, 20);
                        }
                    }
                };

                int padding = DeviceUtil.dip2px(mContext, 20);
                pagerTitleView.setPadding(padding, 0, padding, 0);
                pagerTitleView.setNormalColor(getResources().getColor(R.color.color_111111));
                pagerTitleView.setSelectedColor(Color.WHITE);
                pagerTitleView.setTextSize(14);
                pagerTitleView.setTypeface(Typeface.DEFAULT_BOLD);
                pagerTitleView.setText(mTitleList.get(index).getKindName());
                pagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return pagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(getResources().getColor(R.color.color_FF005E));

                return indicator;
            }
        });
        mTopMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mTopMagicIndicator, mViewPager);
        if (mSubPageIndex != -1) {
            LZLog.i(TAG, "初始化完成后，需要跳转tab==" + mSubPageIndex);
            mViewPager.setCurrentItem(mSubPageIndex);
            mSubPageIndex = -1;
        }
        //记录初始化完成
        isInitFinish = true;
    }


    @Override
    public void requestFirstLevelListSuccess(List<FindCircleTabListBean> beanList) {
        LZLog.i(TAG, "requestFirstLevelListSuccess==");
        FindCircleTabListBean dailyBean = new FindCircleTabListBean();
        dailyBean.setKindName("每日爆款");
        beanList.add(0,dailyBean);
        FindCircleTabListBean findCircleTabListBean = new FindCircleTabListBean();
        findCircleTabListBean.setKindName("商学院");
        beanList.add(findCircleTabListBean);
        initIndicator(beanList);
        loadFirstSuccess=true;
    }

    @Override
    public void requestFirstLevelListError(Throwable throwable) {
        LZLog.i(TAG, "requestFirstLevelListError==" + throwable);
        List<FindCircleTabListBean> beanList = new ArrayList<>();
        FindCircleTabListBean dailyBean = new FindCircleTabListBean();
        dailyBean.setKindName("每日爆款");
        beanList.add(dailyBean);
        FindCircleTabListBean findCircleTabListBean = new FindCircleTabListBean();
        findCircleTabListBean.setKindName("商学院");
        beanList.add(findCircleTabListBean);
        initIndicator(beanList);
    }

    @Override
    public Fragment createFragment(int index) {
        Fragment fragment;
        //由于在接口返回后才初始化 ，同时也在最后添加了一个元素，所以mTitleList一定大于0
        if (index == mTitleList.size() - 1) {
            //商学院
            fragment = new FindCircleBusinessSchoolFragment();
        } else if (index==0){
            //每日爆款
            fragment=new DailyExplosionsFragment();
        }else {
            //除了商学院和每日爆款外的tab 如 宣传素材
            fragment = new FindCircleDailyFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(CommonConst.KEY_FIND_CIRCLE_FIRST_TAB_ID, mTitleList.get(index).getId());
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    public void requestSecondLevelListSuccess(List<FindCircleTabListBean> beanList) {
        LZLog.i(TAG, "requestSecondLevelListSuccess==");
    }

    @Override
    public void requestSecondLevelListError(Throwable throwable) {
        LZLog.i(TAG, "requestSecondLevelListError==" + throwable);
    }

    @Override
    public void setSubPageIndex(int subPageIndex, int childIndex) {
        if (isInitFinish) {
            LZLog.i(TAG, "发圈页面已初始化完成，正在跳转一级栏目" + subPageIndex);
            mViewPager.setCurrentItem(subPageIndex);
        } else {
            LZLog.i(TAG, "发圈页面未初始化完成");
            //未初始化完成 则把当前要跳转的子页面记录下来，等在初始化完成再跳转
            mSubPageIndex = subPageIndex;
        }
    }
}
