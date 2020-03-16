package com.by.lizhiyoupin.app.activity.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.activity.fragment.ShakeCouponListFragment;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponMainTabBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 17:42
 * Summary: 抖券列表页
 */
@Route(path = "/app/ShakeCouponListActivity")
public class ShakeCouponListActivity extends BaseActivity implements View.OnClickListener, CommonFragmentPagerAdapter.FragmentFactoryCallback {
    public static final String TAG = ShakeCouponListActivity.class.getSimpleName();
    private TextView mActionbarTitle;
    private TextView mBack;

    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private View actionbarRl;

    private List<ShakeCouponMainTabBean> mTabBeans;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_coupon_layout);
        initImmersionBar(Color.WHITE,true);
        initView();

    }

    private void initView() {
        mTabBeans = new ArrayList<>();
        mTabBeans.add(new ShakeCouponMainTabBean("全部", 0));
        mTabBeans.add(new ShakeCouponMainTabBean("美食", 11));
        mTabBeans.add(new ShakeCouponMainTabBean("居家", 10));
        mTabBeans.add(new ShakeCouponMainTabBean("美妆", 4));
        mTabBeans.add(new ShakeCouponMainTabBean("母婴", 9));
        mTabBeans.add(new ShakeCouponMainTabBean("其他", 14));


        actionbarRl = findViewById(R.id.actionbar_rl);
        mBack = findViewById(R.id.actionbar_back_tv);
        mActionbarTitle = findViewById(R.id.actionbar_title_tv);
        mMagicIndicator = findViewById(R.id.magicIndicator);
        mViewPager = findViewById(R.id.viewPager);
        mBack.setText("");
        mBack.setOnClickListener(this);
        mActionbarTitle.setText("抖券");
        actionbarRl.setBackgroundColor(Color.WHITE);

        mViewPager.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(), mTabBeans.size(), this));

        initIndicator();
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTabBeans == null ? 0 : mTabBeans.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                SimplePagerTitleView pagerTitleView = new SimplePagerTitleView(context);
                pagerTitleView.setText(mTabBeans.get(index).getTitle());
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
        mMagicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        //使用LinearLayout 的属性 在每一项中间添加分割线，来控制每项间距
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(ShakeCouponListActivity.this, 15);
            }
        });
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.actionbar_back_tv:
                finish();
                break;
        }
    }

    @Override
    public Fragment createFragment(int index) {
        Fragment fragment = new ShakeCouponListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.KEY_INDICATOR_TYPE, mTabBeans.get(index).getType());
        fragment.setArguments(bundle);
        return fragment;
    }
}
