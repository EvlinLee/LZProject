package com.by.lizhiyoupin.app.activity.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.activity.constract.LimitedTimeConstract;
import com.by.lizhiyoupin.app.activity.fragment.LimitedTimeSpikeFragment;
import com.by.lizhiyoupin.app.activity.presenter.LimitedTimeSpikePresenter;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeSkillTitleBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.by.lizhiyoupin.app.weight.LimitedTimePageTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 17:40
 * Summary: 限时秒杀
 */
@Route(path = "/app/LimitedTimeSpikeActivity")
public class LimitedTimeSpikeActivity extends BaseMVPActivity<LimitedTimeConstract.LimitedTimeView, LimitedTimeConstract.LimitedTimePresenters>
        implements View.OnClickListener, CommonFragmentPagerAdapter.FragmentFactoryCallback, LimitedTimeConstract.LimitedTimeView {
    public static final String TAG = LimitedTimeSpikeActivity.class.getSimpleName();

    private TextView mBackTv;
    private TextView mTitleTv;
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private List<LimitedTimeSkillTitleBean> mTimeTitleList;
    private CommonNavigator mCommonNavigator;
    private int  bitch;

    private CommonFragmentPagerAdapter mCommonFragmentPagerAdapter;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DeviceUtil.fullScreen(this, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limites_time_spike_layout);
        bitch = getIntent().getIntExtra(CommonConst.KEY_LIMIT_TIME_TYPE, -1);
        initView();
    }

    @Override
    public LimitedTimeConstract.LimitedTimePresenters getBasePresenter() {
        return new LimitedTimeSpikePresenter(this);
    }

    private void initView() {
        mTimeTitleList = new ArrayList<>();

        mBackTv = findViewById(R.id.actionbar_back_tv);
        mTitleTv = findViewById(R.id.actionbar_title_tv);
        mMagicIndicator = findViewById(R.id.magicIndicator);
        mViewPager = findViewById(R.id.viewPager);
        mBackTv.setText("");
        ViewUtil.setDrawableOfTextView(mBackTv, R.drawable.direction_back_left_white, ViewUtil.DrawableDirection.LEFT);

        ViewUtil.setDrawableOfTextView(mTitleTv, R.drawable.limit_skiller_title, ViewUtil.DrawableDirection.BOTTOM);
        mTitleTv.setPadding(0,0,0,DeviceUtil.dip2px(this,10));
        mBackTv.setOnClickListener(this);
        showLoadingDialog();
        basePresenter.requestJLimitedTimeTitle();

    }

    private void initIndicator() {
        mCommonNavigator = new CommonNavigator(this);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTimeTitleList == null ? 0 : mTimeTitleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                LimitedTimePageTitleView pagerTitleView = new LimitedTimePageTitleView(context);
                pagerTitleView.setTimeText(mTimeTitleList.get(index).getSessionTime());
                if (mTimeTitleList.get(index).getBuyStatus()==1){
                    pagerTitleView.setTipsText("已开抢");
                }else{
                    pagerTitleView.setTipsText("即将开始");
                }

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
                return null;
            }
        });
        mMagicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
        }
    }



    public void updateMagicIndicator() {
        if (mCommonNavigator != null) {
            int currentItem = mViewPager.getCurrentItem();
            int size = mTimeTitleList.size();
            for (int i = 0; i < size; i++) {
                if (i == currentItem + 1) {
                    mTimeTitleList.get(i).setBuyStatus(0);
                    Fragment childFragment = mCommonFragmentPagerAdapter.mChildFragments[i];
                    if (childFragment instanceof LimitedTimeSpikeFragment){
                        ((LimitedTimeSpikeFragment)childFragment).toUpdataFlag(true);
                    }
                } else if (i == currentItem) {
                    mTimeTitleList.get(i).setBuyStatus(1);
                }
            }
            mCommonNavigator.notifyDataSetChanged();
        }
    }

    @Override
    public Fragment createFragment(int index) {
        Fragment fragment = new LimitedTimeSpikeFragment();
        Bundle bundle = new Bundle();
        if (mTimeTitleList != null && mTimeTitleList.size() > index) {
            bundle.putInt(CommonConst.KEY_INDICATOR_TYPE, mTimeTitleList.get(index).getBatch());
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void requestLimitedTimeTitleSuccess(List<LimitedTimeSkillTitleBean> beans) {
        LZLog.i(TAG, "requestLimitedTimeTitleSuccess==");
        mTimeTitleList.clear();
         mTimeTitleList.addAll(beans);
        mCommonFragmentPagerAdapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), mTimeTitleList.size(), this);
        mViewPager.setAdapter(mCommonFragmentPagerAdapter);
        initIndicator();

        if (bitch==6){
            mViewPager.setCurrentItem(0);
        }else if (bitch==7){
            mViewPager.setCurrentItem(1);
        }else if (bitch==8){
            mViewPager.setCurrentItem(2);
        }else if (bitch==9){
            mViewPager.setCurrentItem(3);
        }else if (bitch==10){
            mViewPager.setCurrentItem(4);
        }else if (bitch==-1){
            for (int i=0;i<beans.size();i++){
                if (beans.get(i).getBuyStatus()==1){
                    mViewPager.setCurrentItem(i);
                }

            }
        }
        dismissLoadingDialog();
    }

    @Override
    public void requestLimitedTimeTitleError(Throwable throwable) {
        LZLog.i(TAG, "requestLimitedTimeTitleError==" + throwable);
        dismissLoadingDialog();
    }
}
