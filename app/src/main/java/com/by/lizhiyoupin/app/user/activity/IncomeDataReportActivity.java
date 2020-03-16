package com.by.lizhiyoupin.app.user.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.main.adapter.HomeFragmentPagerAdapter;
import com.by.lizhiyoupin.app.user.fragment.IncomeDataReportFragment;
import com.by.lizhiyoupin.app.weight.IncomePageTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/6 19:52
 * Summary: 数据报表
 */
@Route(path = "/app/IncomeDataReportActivity")
public class IncomeDataReportActivity extends BaseActivity implements CommonFragmentPagerAdapter.FragmentFactoryCallback {
    @BindView(R.id.actionbar_back_tv)
    AppCompatTextView mActionbarBackTv;
    @BindView(R.id.actionbar_title_tv)
    AppCompatTextView mActionbarTitleTv;
    @BindView(R.id.actionbar_rl)
    RelativeLayout mActionbarRl;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.magicIndicator)
    MagicIndicator mMagicIndicator;

    //0日报，1周报，2月报
    private int mTimeType=0;
    private List<String> mList;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incom_data_record_layout);
        initImmersionBar( getResources().getColor(R.color.color_18181A),false);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mTimeType=intent.getIntExtra(CommonConst.KEY_INCOME_TIME_TYPE, 0);
        initView();
    }

    private void initView() {
        mActionbarRl.setBackgroundColor(getResources().getColor(R.color.color_18181A));
        mActionbarBackTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.direction_back_left_white, 0, 0, 0);
        mActionbarBackTv.setText("");
        mActionbarTitleTv.setTextColor(Color.WHITE);

        int count=0;
        mList = new ArrayList<>();
        switch (mTimeType){
            case 0:
                mActionbarTitleTv.setText("日报数据");
                mMagicIndicator.setVisibility(View.GONE);
                count=1;
                break;
            case 1:
                mActionbarTitleTv.setText("周报数据");
                mList.add("上  周");
                mList.add("近3周");
                mList.add("近6周");
                mMagicIndicator.setVisibility(View.VISIBLE);
                count=3;
             break;
            case 2:
                mActionbarTitleTv.setText("月报数据");
                mList.add("上  月");
                mList.add("近3月");
                mList.add("近6月");
                mMagicIndicator.setVisibility(View.VISIBLE);
                count=3;
                break;
        }

        mViewPager.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager(),count, mList,this));

        initIndicator();
    }

    private void initIndicator(){

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mList == null ? 0 : mList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                IncomePageTitleView pagerTitleView = new IncomePageTitleView(context);
                int padding= DeviceUtil.dip2px(IncomeDataReportActivity.this, 18);
                pagerTitleView.setPadding(padding,0,padding,0);
                pagerTitleView.setNormalColor(getResources().getColor(R.color.color_666666));
                pagerTitleView.setSelectedColor(Color.WHITE);
                pagerTitleView.setText(mList.get(index));
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
                indicator.setFillColor(getResources().getColor(R.color.color_EDC77C));

                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }


    @OnClick({R.id.actionbar_back_tv })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
        }
    }

    @Override
    public Fragment createFragment(int index) {
        IncomeDataReportFragment fragment=new IncomeDataReportFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(CommonConst.KEY_INCOME_TAB_SELECTED,index);
        bundle.putInt(CommonConst.KEY_INCOME_TIME_TYPE,mTimeType);
        fragment.setArguments(bundle);
        return fragment;
    }
}
