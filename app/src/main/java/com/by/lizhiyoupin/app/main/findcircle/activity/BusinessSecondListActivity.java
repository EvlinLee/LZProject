package com.by.lizhiyoupin.app.main.findcircle.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;
import com.by.lizhiyoupin.app.main.findcircle.contract.BusinessContract;
import com.by.lizhiyoupin.app.main.findcircle.fragment.BusinessSecondListFragment;
import com.by.lizhiyoupin.app.main.findcircle.presenter.BusinessSecondListPresenter;
import com.by.lizhiyoupin.app.weight.IncomePageTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 21:51
 * Summary: 发圈--商学院--文章二级列表页
 */
@Route(path = "/app/BusinessSecondListActivity")
public class BusinessSecondListActivity extends BaseMVPActivity<BusinessContract.BusinessSecondListView, BusinessContract.BusinessSecondListPresenters> implements CommonFragmentPagerAdapter.FragmentFactoryCallback, BusinessContract.BusinessSecondListView, View.OnClickListener {
    public static final String TAG = BusinessSecondListActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private MagicIndicator mMagicIndicator;
    private List<BusinessIconBean> titleList;
    private long mFirstId;
    private String mTitle;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_second_list_layout);
        initImmersionBar(Color.WHITE,true);
        Intent intent = getIntent();
        mFirstId = intent.getLongExtra(CommonConst.KEY_FIND_CIRCLE_FIRST_TAB_ID, 0);
        mTitle = intent.getStringExtra(CommonConst.KEY_ACTIONBAR_TITLE);
        initView();
        basePresenter.requestBusinessSecondTabList(mFirstId);
    }

    @Override
    public BusinessContract.BusinessSecondListPresenters getBasePresenter() {
        return new BusinessSecondListPresenter(this);
    }

    private void initView() {
        TextView backTv = findViewById(R.id.actionbar_back_tv);
        TextView titleTv = findViewById(R.id.actionbar_title_tv);
        findViewById(R.id.actionbar_rl).setBackgroundColor(Color.WHITE);
        mViewPager = findViewById(R.id.viewPager);
        mMagicIndicator = findViewById(R.id.magicIndicator);
        backTv.setText("");
        backTv.setOnClickListener(this);
        titleTv.setText(mTitle);

    }
    private void initIndicator(){
        mViewPager.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(), titleList.size(), this));

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titleList == null ? 0 : titleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                IncomePageTitleView pagerTitleView = new IncomePageTitleView(context);
                int padding= DeviceUtil.dip2px(BusinessSecondListActivity.this, 18);
                pagerTitleView.setPadding(padding,0,padding,0);
                pagerTitleView.setNormalColor(getResources().getColor(R.color.color_555555));
                pagerTitleView.setSelectedColor(getResources().getColor(R.color.color_FF005F));
                pagerTitleView.setText(titleList.get(index).getKindName());
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
                indicator.setYOffset(DeviceUtil.dip2px(context, 3));
                indicator.setLineWidth(DeviceUtil.dip2px(context, 20));
                indicator.setRoundRadius(DeviceUtil.dip2px(context, 3));
                indicator.setColors(Color.parseColor("#FF005F"));

                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }


    @Override
    public Fragment createFragment(int index) {
        Fragment fragment = new BusinessSecondListFragment();
        Bundle bundle=new Bundle();
        bundle.putLong(CommonConst.KEY_FIND_CIRCLE_FIRST_TAB_ID,titleList.get(index).getSuperiorId());
        bundle.putLong(CommonConst.KEY_FIND_CIRCLE_SECOND_TAB_ID,titleList.get(index).getId());
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void requestBusinessSecondTabListSuccess(List<BusinessIconBean> beanList) {
        Log.i(TAG, "requestBusinessSecondTabListSuccess: "+beanList.size());
        titleList=beanList;
        initIndicator();
    }

    @Override
    public void requestBusinessSecondTabListError(Throwable throwable) {
        Log.i(TAG, "requestBusinessSecondTabListError: "+throwable);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()){
            return;
        }
        switch (v.getId()){
            case R.id.actionbar_back_tv:
                finish();
                break;
        }
    }
}
