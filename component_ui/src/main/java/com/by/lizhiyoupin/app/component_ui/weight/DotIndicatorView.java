package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.R;

import androidx.core.content.ContextCompat;


public final class DotIndicatorView extends LinearLayout {
    private int mIndicatorCount;
    private View[] mIndicators;

    private int mCurrentIndex;

    private Drawable mSelectedDrawable;
    private Drawable mNormalDrawable;
    private int selectedWidth;
    private int unSelectedWidth;

    public DotIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mSelectedDrawable = ContextCompat.getDrawable(context, R.drawable.dot_orange);
        mNormalDrawable = ContextCompat.getDrawable(context, R.drawable.dot_gray);
        selectedWidth= DeviceUtil.dip2px(context, 40);
        unSelectedWidth= DeviceUtil.dip2px(context, 14);
    }

    public void setSelectedDrawable(Drawable drawable) {
        this.mSelectedDrawable = drawable;
    }

    public void setNormalDrawable(Drawable drawable) {
        this.mNormalDrawable = drawable;
    }

    public void setIndicatorCount(int count) {
        setIndicatorCount(count, 0);
    }

    public void setIndicatorCount(int count, int selectedIndex) {
        if (mIndicatorCount > 0 || count <= 0) {
            // 如果已经初始化就不在初始化了
            return;
        }

        this.mIndicatorCount = count;
        mIndicators = new View[count];

        final Context context = getContext();
        final Resources res = context.getResources();
         int indicatorSize = unSelectedWidth;//DeviceUtil.dip2px(context, 4.5f);
        final int indicatorMargin = DeviceUtil.dip2px(context, 8);
        LayoutParams params = null;
        View indicator = null;
        mCurrentIndex = selectedIndex;
        for (int i = 0; i < count; i++) {
            if (i==0){
                indicatorSize=selectedWidth;
            }else {
                indicatorSize=unSelectedWidth;
            }
            params = new LayoutParams(indicatorSize, indicatorSize);
            if (i < count - 1) {
                params.rightMargin = indicatorMargin;
            }
            indicator = new View(context);
            if (i == selectedIndex) {
                indicator.setBackgroundDrawable(mSelectedDrawable);
            } else {
                indicator.setBackgroundDrawable(mNormalDrawable);
            }
            addView(indicator, params);
            mIndicators[i] = indicator;
        }
    }

    public void switchIndex(final int index) {
        if (mCurrentIndex != index) {
            ViewGroup.LayoutParams selectedParams = mIndicators[index].getLayoutParams();
            selectedParams.width=selectedWidth;
            mIndicators[index].setLayoutParams(selectedParams);
            mIndicators[index].setBackgroundDrawable(mSelectedDrawable);

            ViewGroup.LayoutParams unSelectedParams = mIndicators[mCurrentIndex].getLayoutParams();
            unSelectedParams.width=unSelectedWidth;
            mIndicators[mCurrentIndex].setLayoutParams(unSelectedParams);
            mIndicators[mCurrentIndex].setBackgroundDrawable(mNormalDrawable);
            mCurrentIndex = index;
        }
    }
    public void resetIndicatorCount(int count){
        mIndicatorCount=0;
        mIndicators=null;
        mCurrentIndex=0;
        removeAllViews();
        setIndicatorCount(count);
    }
}
