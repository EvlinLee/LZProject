package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/29 17:03
 * Summary:
 */
public class MyTabLayout extends TabLayout {
    private static final int TabViewNumber = 6;
    private static final String SCROLLABLE_TAB_MIN_WIDTH = "scrollableTabMinWidth";

    public MyTabLayout(Context context) {
        this(context, null);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTabMinWidth();

    }

    private void initTabMinWidth() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int tabMinWidth = screenWidth / TabViewNumber;

        Field field;
        try {
            field = TabLayout.class.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH);
            field.setAccessible(true);
            field.set(this, tabMinWidth);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
