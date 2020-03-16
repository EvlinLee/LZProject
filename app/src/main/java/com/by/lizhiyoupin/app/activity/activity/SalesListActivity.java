package com.by.lizhiyoupin.app.activity.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.activity.fragment.SalesListFragment;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
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
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 11:59
 * Summary: 销量榜
 */
@Route(path = "/app/SalesListActivity")
public class SalesListActivity extends BaseActivity implements View.OnClickListener, CommonFragmentPagerAdapter.FragmentFactoryCallback {
    public static final String TAG = SalesListActivity.class.getSimpleName();

    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private List<String> mTitleList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_list_layout);
        initImmersionBar(Color.WHITE,true);
        initView();
    }


    private void initView() {
        mTitleList=new ArrayList<>();
        mTitleList.add("最近两小时");
        mTitleList.add("全天疯抢榜");
        findViewById(R.id.actionbar_back_tv).setOnClickListener(this);
        mMagicIndicator = findViewById(R.id.magicIndicator);
        mViewPager = findViewById(R.id.viewPager);

        mViewPager.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(),mTitleList.size(),this));
        initIndicator();
    }
    private void initIndicator(){

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitleList == null ? 0 : mTitleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                IncomePageTitleView pagerTitleView = new IncomePageTitleView(context);
                int padding= DeviceUtil.dip2px(SalesListActivity.this, 10);
                pagerTitleView.setPadding(padding,0,padding,0);
                pagerTitleView.setNormalColor(getResources().getColor(R.color.color_666666));
                pagerTitleView.setSelectedColor(Color.WHITE);
                pagerTitleView.setTextSize(14);
                pagerTitleView.setTypeface(Typeface.DEFAULT_BOLD);
                pagerTitleView.setText(mTitleList.get(index));
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
        mMagicIndicator.setNavigator(commonNavigator);
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

    @Override
    public Fragment createFragment(int index) {
        Fragment fragment=new SalesListFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(CommonConst.KEY_INDICATOR_TYPE,index);
        fragment.setArguments(bundle);
        return fragment;
    }
}
