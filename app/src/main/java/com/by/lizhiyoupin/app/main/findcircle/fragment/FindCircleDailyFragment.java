package com.by.lizhiyoupin.app.main.findcircle.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.io.bean.FindCircleTabListBean;
import com.by.lizhiyoupin.app.main.findcircle.contract.FindCircleContract;
import com.by.lizhiyoupin.app.main.findcircle.presenter.FindCirclelPresenter;
import com.dueeeke.videoplayer.player.VideoViewManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 14:56
 * Summary: 发圈--每日爆款等二级主（除了商学院）
 */
public class FindCircleDailyFragment extends BaseFragment implements CommonFragmentPagerAdapter.FragmentFactoryCallback, FindCircleContract.FindCircleView {
    public static final String TAG = FindCircleDailyFragment.class.getSimpleName();
    private MagicIndicator mTabMagicIndicator;
    private ViewPager mViewPager;
    private Context mContext;
    private FindCircleContract.FindCirclelPresenters mPresenters;
    private long superiorId;
    private List<FindCircleTabListBean> mTabList;
    private CommonFragmentPagerAdapter mPagerAdapter;
    private VideoViewManager mVideoViewManager;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_find_circle_daily_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            superiorId = arguments.getLong(CommonConst.KEY_FIND_CIRCLE_FIRST_TAB_ID, 0);
        }
        mVideoViewManager= VideoViewManager.instance();
        mPresenters = new FindCirclelPresenter(this);
        initView(inflate);

        return inflate;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mPresenters.requestSecondLevelList(superiorId);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //控制viewpager内fragment生命周期
        if (mPagerAdapter != null) {
            mPagerAdapter.setChildUserVisibleHint(!hidden);
        }
    }

    private void initView(View root) {
        mTabMagicIndicator = root.findViewById(R.id.tab_magicIndicator);
        mViewPager = root.findViewById(R.id.tab_viewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mVideoViewManager.release();
            }
        });
    }

    private void initIndicator(List<FindCircleTabListBean> tabList) {
        mTabList = tabList;
        mPagerAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), mTabList.size(), this);
        mViewPager.setAdapter(mPagerAdapter);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTabList == null ? 0 : mTabList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                SimplePagerTitleView pagerTitleView = new SimplePagerTitleView(context);
                pagerTitleView.setText(mTabList.get(index).getKindName());
                pagerTitleView.setTextSize(14);
                pagerTitleView.setNormalColor(getResources().getColor(R.color.color_555555));
                pagerTitleView.setSelectedColor(getResources().getColor(R.color.color_FF005E));
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
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setYOffset(DeviceUtil.dip2px(context, 2));
                indicator.setLineWidth(DeviceUtil.dip2px(context, 20));
                indicator.setRoundRadius(DeviceUtil.dip2px(context, 3));
                indicator.setColors(Color.parseColor("#FF005F"));
                return indicator;
            }
        });
        mTabMagicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        //使用LinearLayout 的属性 在每一项中间添加分割线，来控制每项间距
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return DeviceUtil.dip2px(mContext, 20);
            }
        });

        ViewPagerHelper.bind(mTabMagicIndicator, mViewPager);
    }


    @Override
    public Fragment createFragment(int index) {
        Fragment fragment = new DailyChildTabFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.KEY_FIND_CIRCLE_FIRST_TAB_ID, mTabList.get(index).getSuperiorId());
        bundle.putLong(CommonConst.KEY_FIND_CIRCLE_SECOND_TAB_ID, mTabList.get(index).getId());
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void requestSecondLevelListSuccess(List<FindCircleTabListBean> beanList) {
        //得到二级类目
        LZLog.i(TAG, "requestSecondLevelListSuccess==");
        initIndicator(beanList);
    }

    @Override
    public void requestSecondLevelListError(Throwable throwable) {
        LZLog.i(TAG, "requestSecondLevelListError==" + throwable);
    }

    @Override
    public void requestFirstLevelListSuccess(List<FindCircleTabListBean> beanList) {

    }

    @Override
    public void requestFirstLevelListError(Throwable throwable) {

    }
}
