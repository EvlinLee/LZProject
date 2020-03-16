package com.by.lizhiyoupin.app.weight.rv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.main.adapter.preciseadapter.PreciseNewAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/26 13:28
 * Summary:
 */
public class ParentRV extends RecyclerView {
    public static final String TAG = ParentRV.class.getSimpleName();
    private int mMaxDistance;
    private FlingHelper mFlingHelper;
    private float lastY;
    private int totalDy;
    private boolean isStartFling;
    private int velocityY;
    private View jump2Top;
    public ParentRV(@NonNull Context context) {
        this(context, null);
    }

    public ParentRV(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParentRV(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mFlingHelper = new FlingHelper(context);
        this.mMaxDistance = this.mFlingHelper.getVelocityByDistance((double) (DeviceUtil.getScreenHeight(context) * 4));
        this.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    dispatchChildFling();
                }

            }

            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isStartFling()) {
                    setTotalDy(0);
                    setStartFling(false);
                }
                setTotalDy(getTotalDy() + dy);
            }
        });
    }

    public final int getTotalDy() {
        return this.totalDy;
    }

    public final void setTotalDy(int var1) {
        this.totalDy = var1;
    }

    public final boolean isStartFling() {
        return this.isStartFling;
    }

    public final void setStartFling(boolean var1) {
        this.isStartFling = var1;
    }

    private final void dispatchChildFling() {
        if (this.isScrollEnd() && this.velocityY != 0) {
            double splineFlingDistance = this.mFlingHelper.getSplineFlingDistance(this.velocityY);
            if (splineFlingDistance > (double) this.totalDy) {
                LZLog.i(TAG, "childFling dispatchChildFling");
                this.childFling(this.mFlingHelper.getVelocityByDistance(splineFlingDistance - (double) this.totalDy));
            }
        }

        this.totalDy = 0;
        this.velocityY = 0;
    }

    private final void childFling(int velY) {
        ChildRv childRecyclerView = this.findNestedScrollingChildRecyclerView();
        if (childRecyclerView != null) {
            childRecyclerView.fling(0, velY);
        }

    }



    /**
     * 置顶按钮
     * @param jump2top
     */
    public void setJump2Top(View jump2top) {
        this.jump2Top = jump2top;
    }

    public final LinearLayoutManager initLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext()) {

            public boolean canScrollVertically() {
                //判断是否可竖直滑动
                if (jump2Top != null && jump2Top.getVisibility() == View.GONE) {
                    //可置顶按钮隐藏时表示 外部rv可滑动
                    return true;
                }
                ChildRv childRecyclerView = ParentRV.this.findNestedScrollingChildRecyclerView();
                return childRecyclerView == null || childRecyclerView.isScrollTop();
            }

            public boolean supportsPredictiveItemAnimations() {
                //notifyItemChanged()方法更新单个Item时，此Item有闪烁,
                // false 所有的notifyItem*动画都取消了
                return false;
            }
        };
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        this.setLayoutManager((LayoutManager) linearLayoutManager);
        return linearLayoutManager;
    }

    public boolean dispatchTouchEvent(@Nullable MotionEvent ev) {
        if (ev != null && ev.getAction() == 0) {
            this.velocityY = 0;
            this.stopScroll();
        }

        if (ev != null && ev.getAction() != 2) {
            this.lastY = 0.0F;
        }

        boolean var2;
        try {
            var2 = super.dispatchTouchEvent(ev);
        } catch (Exception var4) {
            var4.printStackTrace();
            var2 = false;
        }

        return var2;
    }

    public boolean onTouchEvent(@NonNull MotionEvent e) {
        if (this.lastY == 0.0F) {
            this.lastY = e.getY();
        }
        if (this.isScrollEnd()) {
            ChildRv childRecyclerView = this.findNestedScrollingChildRecyclerView();
            if (childRecyclerView != null) {

                int deltaY = (int) (this.lastY - e.getY());
                if (deltaY != 0) {
                    childRecyclerView.scrollBy(0, deltaY);
                }
            }
        }

        this.lastY = e.getY();

        boolean var10;
        try {
            var10 = super.onTouchEvent(e);
        } catch (Exception var9) {
            var9.printStackTrace();
            var10 = false;
        }

        return var10;
    }

    public boolean fling(int velX, int velY) {
        boolean fling = super.fling(velX, velY);
        if (fling && velY > 0) {
            this.isStartFling = true;
            this.velocityY = velY;
        } else {
            this.velocityY = 0;
        }

        return fling;
    }

    private final boolean isScrollEnd() {
        return !this.canScrollVertically(1);
    }

    private final ChildRv findNestedScrollingChildRecyclerView() {
        Adapter var10000 = this.getAdapter();
        if (!(var10000 instanceof PreciseNewAdapter)) {
            var10000 = null;
        }
        PreciseNewAdapter var6 = (PreciseNewAdapter) var10000;
        if (var6 != null) {
            return var6.getCurrentChildRecyclerView();
        } else {
            return null;
        }
    }


}
