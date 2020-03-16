package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 定时滚动
 */
public class SpeedLinearLayoutManager extends LinearLayoutManager {

    private Context mContext;
    private float speed = 0.3f;
    public SpeedLinearLayoutManager(Context context) {
        super(context);
        mContext = context;
    }

    public SpeedLinearLayoutManager(Context context,float speed) {
        super(context);
        mContext = context;
        this.speed = speed;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return SpeedLinearLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return mContext.getResources().getDisplayMetrics().density * speed / displayMetrics.density;
                    }

                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
