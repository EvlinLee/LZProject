package com.by.lizhiyoupin.app.main.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.weight.DotIndicatorView;
import com.by.lizhiyoupin.app.io.bean.HomeIconBean;
import com.by.lizhiyoupin.app.main.adapter.HomeToolItemAdapter;
import com.by.lizhiyoupin.app.main.adapter.ToolPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by  wzm;
 *   icon个数 布局设置
 */
public final class HomeToolsLayout extends LinearLayout implements ViewPager.OnPageChangeListener {
    private DotIndicatorView mIndicator;
    public static final String TOOLS_TYPE_NORMAL="precise";
    public static final String TOOLS_TYPE_OHTHER="other";
    private static final int COUNT_PER_PAGE = 10;
    private static final int COUNT_PER_LINE = 5;
    private String mToolsType=TOOLS_TYPE_NORMAL;
    public HomeToolsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIndicator = (DotIndicatorView) findViewById(R.id.indexIndicator);
    }

    /**
     *
     * @param toolsType
     */
    public void setToolsType(String toolsType){
        this.mToolsType=toolsType;
    }
    /**
     * 网络适配icon
     * @param iconEntities
     */
    public void initNetWorkIcon(List<HomeIconBean> iconEntities) {
        //网路适配加载图片
        int size = iconEntities.size();
        final int PAGE_COUNT = (size - 1) / COUNT_PER_PAGE + 1;
        final ArrayList<HomeIconBean>[] iconItems = new ArrayList[PAGE_COUNT];
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

    private void initNetWork(final int pageCount, final int countPerLine, final List<HomeIconBean>[] items, Context mActivity) {
        if (pageCount > 1) {
            mIndicator.setVisibility(VISIBLE);
            mIndicator.setIndicatorCount(pageCount, 0);
        } else {
            mIndicator.setVisibility(GONE);
        }

        final View[] pagerViews = new View[pageCount];
        final Context context =mActivity;
        HomeToolItemAdapter adapter;
        RecyclerView recyclerView;
        final ViewPager viewPager = findViewById(R.id.viewPager);
        for (int i = 0; i < pageCount; i++) {
            recyclerView = (RecyclerView) View.inflate(context, R.layout.recycler_view, null);
            recyclerView.setLayoutManager(new GridLayoutManager(context, countPerLine));
            adapter = new HomeToolItemAdapter(context, items[i]);
            adapter.setHomeToolsType(mToolsType);
            recyclerView.setAdapter(adapter);
            adapter.setItemClickable(true);
            pagerViews[i] = recyclerView;
            if (i == 0) { // 计算viewpage的高度
                final int itemHeight = DeviceUtil.dip2px(context, 82);
                final LayoutParams params = (LayoutParams) viewPager.getLayoutParams();
                params.height = ((items[0].size() - 1) / countPerLine + 1) * itemHeight;
                viewPager.setLayoutParams(params);
            }
        }

        viewPager.setAdapter(new ToolPagerAdapter(pagerViews));
        viewPager.setOnPageChangeListener(this);
        mIndicator.setIndicatorCount(pageCount);
        mIndicator.switchIndex(0);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.switchIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}