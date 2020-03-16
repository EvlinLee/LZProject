package com.by.lizhiyoupin.app.main.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.io.bean.VipPowerBean;
import com.by.lizhiyoupin.app.main.adapter.ToolPagerAdapter;
import com.by.lizhiyoupin.app.user.adapter.VipToolItemAdapter;
import com.by.lizhiyoupin.app.weight.CircleFillNavigator;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/5 17:12
 * Summary:
 */
public class UserToolsLayout extends LinearLayout implements ViewPager.OnPageChangeListener {
    private MagicIndicator mIndicator;

    private static final int COUNT_PER_PAGE = 6;//每页最多item数
    private static final int COUNT_PER_LINE = 3;//每页列数

    public UserToolsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIndicator =   findViewById(R.id.indexIndicator);
    }

    /**
     * 网络适配icon
     * @param iconEntities
     */
    public void initNetWorkIcon(List<VipPowerBean> iconEntities) {
        //网路适配加载图片
        int size = iconEntities.size();
        final int PAGE_COUNT = (size - 1) / COUNT_PER_PAGE + 1;
        final ArrayList<VipPowerBean>[] iconItems = new ArrayList[PAGE_COUNT];
        for (int i = 0; i < PAGE_COUNT; i++) {
            iconItems[i] = new ArrayList<>(COUNT_PER_PAGE);
        }
        for (int i = 0; i < size; i++) {
            iconItems[i / COUNT_PER_PAGE].add(iconEntities.get(i));
        }
        // 设置间距
        final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.d_07);
        params.bottomMargin = params.topMargin;
        setLayoutParams(params);
        initNetWork(PAGE_COUNT, COUNT_PER_LINE, iconItems, getContext());
    }

    private void initNetWork(final int pageCount, final int countPerLine, final List<VipPowerBean>[] items, Context mActivity) {

        final View[] pagerViews = new View[pageCount];
        final Context context =mActivity;
        VipToolItemAdapter adapter;
        RecyclerView recyclerView;
        final ViewPager viewPager = findViewById(R.id.viewPager);
        for (int i = 0; i < pageCount; i++) {
            recyclerView = (RecyclerView) View.inflate(context, R.layout.vip_tools_recycler_view, null);
            recyclerView.setLayoutManager(new GridLayoutManager(context, countPerLine));
            adapter = new VipToolItemAdapter(context, items[i]);
            recyclerView.setAdapter(adapter);
            adapter.setItemClickable(true);
            pagerViews[i] = recyclerView;
            if (i == 0) { // 计算viewpage的高度
                final int itemHeight = DeviceUtil.dip2px(context, 82);
                final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                params.height = ((items[0].size() - 1) / countPerLine + 1) * itemHeight;
                viewPager.setLayoutParams(params);
            }
        }

        viewPager.setAdapter(new ToolPagerAdapter(pagerViews));
        viewPager.setOnPageChangeListener(this);

        if (pageCount > 1) {
            mIndicator.setVisibility(VISIBLE);
            initIndicator(context,mIndicator,viewPager,pageCount);
        } else {
            mIndicator.setVisibility(GONE);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    private void initIndicator(Context context,MagicIndicator magicIndicator,ViewPager viewPager,int count) {
        CircleFillNavigator circleNavigator = new CircleFillNavigator(context);
        circleNavigator.setCircleColor(getResources().getColor(R.color.color_694117));
        circleNavigator.setNormalFill(false);
        circleNavigator.setStrokeWidth(DeviceUtil.dip2px(context,0.5f));
        circleNavigator.setNormalColor(getResources().getColor(R.color.color_694117));
        circleNavigator.setCircleCount(count);
        circleNavigator.setCircleClickListener(new CircleFillNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                // mViewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }
}
