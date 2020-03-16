package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 不可手动滑动RecyclerView
 */
public class CannotScrollRecyclerView extends RecyclerView {

    private boolean running = false;
    private int currentIndex = 4;

    public CannotScrollRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CannotScrollRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CannotScrollRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }
}
