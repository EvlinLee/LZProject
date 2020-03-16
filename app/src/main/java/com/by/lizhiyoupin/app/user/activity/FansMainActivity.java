package com.by.lizhiyoupin.app.user.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.io.bean.FansCountBean;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.user.contract.FansContrat;
import com.by.lizhiyoupin.app.user.fragment.FansListFragment;
import com.by.lizhiyoupin.app.user.presenter.FansMainPresenter;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.by.lizhiyoupin.app.weight.IncomePageTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/9 15:21
 * Summary: 我的粉丝 页面
 */
@Route(path = "/app/FansMainActivity")
public class FansMainActivity extends BaseMVPActivity<FansContrat.FansMainView, FansContrat.FansMainPresenters> implements FansContrat.FansMainView, CommonFragmentPagerAdapter.FragmentFactoryCallback {
    public static final String TAG = FansMainActivity.class.getSimpleName();
    @BindView(R.id.fans_my_number_tv)
    TextView mFansMyNumberTv;
    @BindView(R.id.fans_exclusive_number_tv)
    TextView mFansExclusiveNumberTv;
    @BindView(R.id.fans_normal_number_tv)
    TextView mFansNormalNumberTv;
    @BindView(R.id.fans_go_to_inviter_tv)
    TextView mFansGoToInviterTv;
    @BindView(R.id.actionbar_back_tv)
    AppCompatTextView mActionbarBackTv;
    @BindView(R.id.actionbar_title_tv)
    AppCompatTextView mActionbarTitleTv;
    @BindView(R.id.actionbar_right_tv)
    AppCompatTextView mActionbarRightTv;
    @BindView(R.id.actionbar_rl)
    RelativeLayout mActionbarRl;
    @BindView(R.id.magicIndicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.fans_viewPager)
    ViewPager mFansViewPager;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<String> mTabList;
    private CommonFragmentPagerAdapter pagerAdapter;
    private boolean canRefresh=true;



    @Override
    public FansContrat.FansMainPresenters getBasePresenter() {
        return new FansMainPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_main_layout);
        StatusBarUtils.setTranslucentForImageView(this, 0,
                findViewById(R.id.actionbar_rl));
        ButterKnife.bind(this);
        initView();
        initIndicator();
        basePresenter.requestFansMain();
        initRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                basePresenter.requestFansMain();
                if (pagerAdapter!=null){
                    int currentItem = mFansViewPager.getCurrentItem();
                    Fragment[] childFragments = pagerAdapter.mChildFragments;
                    FansListFragment fansListFragment= (FansListFragment) childFragments[currentItem];
                    fansListFragment.refresh();
                }

            }
        });

    }


    private void initView() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mActionbarRl.getLayoutParams();
        layoutParams.topMargin=DeviceUtil.getStatusBarHeight(this);
        mTabList = new ArrayList<>();
        mTabList.add("专属粉丝");
        mTabList.add("普通粉丝");
        mActionbarBackTv.setText("");
        ViewUtil.setDrawableOfTextView(mActionbarBackTv, R.drawable.direction_back_left_white, ViewUtil.DrawableDirection.LEFT);
        mActionbarTitleTv.setTextColor(Color.WHITE);
        mActionbarRightTv.setTextColor(Color.WHITE);
        mActionbarTitleTv.setText(R.string.fans_main_title_text);
        mActionbarRightTv.setText(R.string.search_txt);
        ViewUtil.setTextViewFormat(this, mFansMyNumberTv, R.string.fans_my_number_text, 0);
          pagerAdapter =
                new CommonFragmentPagerAdapter(getSupportFragmentManager(), 2, this);
        mFansViewPager.setAdapter(pagerAdapter);
        //添加页面滑动监听,控制 SwipeRefreshLayout与ViewPager滑动冲突
        mFansViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == 1) {
                    mSwipeRefreshLayout.setEnabled(false);//设置不可触发
                } else if (state == 2&&canRefresh) {
                    mSwipeRefreshLayout.setEnabled(true);//设置可触发
                }
            }
        });
    }

    private void initIndicator() {

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTabList == null ? 0 : mTabList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                IncomePageTitleView pagerTitleView = new IncomePageTitleView(context);
                int padding = DeviceUtil.dip2px(FansMainActivity.this, 18);
                pagerTitleView.setPadding(padding, 0, padding, 0);
                pagerTitleView.setNormalColor(getResources().getColor(R.color.color_333333));
                pagerTitleView.setSelectedColor(getResources().getColor(R.color.color_D6B667));
                pagerTitleView.setTypeface(Typeface.DEFAULT_BOLD);
                pagerTitleView.setTextSize(17);
                pagerTitleView.setText(mTabList.get(index));
                pagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mFansViewPager.setCurrentItem(index);
                    }
                });
                return pagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setYOffset(DeviceUtil.dip2px(context, 3));
                indicator.setLineWidth(DeviceUtil.dip2px(context, 15));
                indicator.setRoundRadius(DeviceUtil.dip2px(context, 3));
                indicator.setColors(Color.parseColor("#D6B667"));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mFansViewPager);
    }


    private void refreshData(FansCountBean fansCountBean) {
        ViewUtil.setTextViewFormat(this, mFansMyNumberTv, R.string.fans_my_number_text, fansCountBean.getAllFansCount());
        mFansExclusiveNumberTv.setText(String.valueOf(fansCountBean.getDirectFansCount()));
        mFansNormalNumberTv.setText(String.valueOf(fansCountBean.getCommonlyAllFansCount()));
    }

    @Override
    public void requestFansMainSuccess(FansCountBean fansCountBean) {
        mSwipeRefreshLayout.setRefreshing(false);
        refreshData(fansCountBean);
    }

    @Override
    public void requestFansMainError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick({R.id.actionbar_back_tv, R.id.actionbar_right_tv, R.id.fans_go_to_inviter_tv})
    public void onViewClicked(View view) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (view.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.actionbar_right_tv:
                //搜索
                CommonSchemeJump.showActivity(this, "/app/FansSearchActivity");
                break;
            case R.id.fans_go_to_inviter_tv:
                String superiorId = LiZhiApplication.getApplication().getAccountManager().getAccountInfo().getSuperiorId();
                if (!TextUtils.isEmpty(superiorId)) {
                    DiaLogManager.showMyInviterDialog(this, getSupportFragmentManager());
                } else {
                    DiaLogManager.showInvitationCodeDialog(this,getSupportFragmentManager());
                   // MessageToast.showToast(this, "您还未拥有上级邀请人哦!");
                }
                break;
        }
    }

    @Override
    public Fragment createFragment(int index) {
        Fragment fragment = new FansListFragment();
        Bundle bundle = new Bundle();
        switch (index) {
            case 0://专属粉丝
                bundle.putInt(CommonConst.KEY_FANS_LIST_TYPE, 0);
                break;
            case 1://普通粉丝
                bundle.putInt(CommonConst.KEY_FANS_LIST_TYPE, 1);
                break;
        }
        fragment.setArguments(bundle);
        return fragment;
    }
}
