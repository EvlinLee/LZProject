package com.by.lizhiyoupin.app.main.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

/**
 * (Hangzhou) <br/>
 * Author: wzm<br/>
 * Date :  2018/6/25 19:43 </br>
 * Summary:
 */
public class ToolPagerAdapter extends PagerAdapter {
    View[] mPagerViews;

    public ToolPagerAdapter(View[] mPagerViews) {
        this.mPagerViews = mPagerViews;
    }

    @Override
    public int getCount() {
        return mPagerViews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View pager = mPagerViews[position];
        container.addView(pager);
        return pager;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mPagerViews[position]);
    }
}
