package com.by.lizhiyoupin.app.weight.rv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.loader.LoadType;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.annotations.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/26 14:00
 * Summary:
 */
public class ChildRv extends RecyclerView implements RvVpInterface{
public static final String TAG=ChildRv.class.getSimpleName();
    private FlingHelper mFlingHelper;
    private int mMaxDistance;
    private int mVelocityY;
    private boolean isStartFling;
    private int totalDy;

    private ParentRV mParentRecyclerView;

    public ChildRv(@NonNull Context context) {
        this(context, null);
    }

    public ChildRv(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChildRv(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mFlingHelper = new FlingHelper(context);
        this.mMaxDistance = this.mFlingHelper.getVelocityByDistance((double) (DeviceUtil.getScreenHeight(context) * 4));
        this.setOverScrollMode(2);
        this.initScrollListener();
    }

    public final boolean isStartFling() {
        return this.isStartFling;
    }

    public final void setStartFling(boolean var1) {
        this.isStartFling = var1;
    }

    public final int getTotalDy() {
        return this.totalDy;
    }

    public final void setTotalDy(int var1) {
        this.totalDy = var1;
    }


    public final ParentRV getMParentRecyclerView() {
        return this.mParentRecyclerView;
    }

    public final void setMParentRecyclerView(@Nullable ParentRV var1) {
        this.mParentRecyclerView = var1;
    }

    private final void initScrollListener() {
        this.addOnScrollListener((OnScrollListener) (new OnScrollListener() {
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isStartFling()) {
                    setTotalDy(0);
                    setStartFling(false);
                }
                setTotalDy(getTotalDy() + dy);
            }

            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_IDLE) {
                    dispatchParentFling();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        }));
    }

    private final void dispatchParentFling() {
        this.mParentRecyclerView = this.findParentRecyclerView();
        ParentRV var10000 = this.mParentRecyclerView;
        if (var10000 != null) {
            ParentRV var1 = var10000;
            if (this.isScrollTop() && this.mVelocityY != 0) {
                double flingDistance = this.mFlingHelper.getSplineFlingDistance(this.mVelocityY);
                if (flingDistance > (double) Math.abs(var1.getTotalDy())) {
                    var1.fling(0, -this.mFlingHelper.getVelocityByDistance(flingDistance + (double) var1.getTotalDy()));
                }

                var1.setTotalDy(0);
                this.mVelocityY = 0;
            }
        }

    }

    public boolean dispatchTouchEvent(@Nullable MotionEvent ev) {
        if (ev != null && ev.getAction() == 0) {
            this.mVelocityY = 0;
        }

        return super.dispatchTouchEvent(ev);
    }

    public boolean fling(int velocityX, int velocityY) {
        if (!this.isAttachedToWindow()) {
            return false;
        } else {
            boolean fling = super.fling(velocityX, velocityY);
            if (fling && velocityY < 0) {
                this.isStartFling = true;
                this.mVelocityY = velocityY;
            } else {
                this.mVelocityY = 0;
            }

            return fling;
        }
    }

    public final boolean isScrollTop() {
        return !this.canScrollVertically(-1);
    }

    private final ParentRV findParentRecyclerView() {
        ViewParent parentView;
        for (parentView = this.getParent(); !(parentView instanceof ParentRV); parentView = parentView.getParent()) {
           // Intrinsics.checkExpressionValueIsNotNull(parentView, "parentView");
            if (parentView==null){
                return null;
            }
            //遍历找到 父 rv给 parentView
        }

        ViewParent var10000 = parentView;
        if (!(parentView instanceof ParentRV)) {
            var10000 = null;
        }

        return (ParentRV) var10000;
    }





    protected FrameLayout mFooterView;
    protected View mLoadingLayout;
    protected View mNoMoreLayout;
    protected View mNoDataLayout;
    protected View getFooterView(Context mContext){
        //加载更多
        mFooterView = new FrameLayout(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        mLoadingLayout = inflater.inflate(R.layout.list_item_loading, mFooterView, false);
        mFooterView.addView(mLoadingLayout);
        mNoMoreLayout = inflater.inflate(R.layout.no_more_text, mFooterView, false);
        TextView noMoreTv = mNoMoreLayout.findViewById(R.id.more);
        noMoreTv.setText(R.string.empty_load_more_text);
        noMoreTv.setGravity(Gravity.CENTER);
        mNoMoreLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoMoreLayout);
        mNoDataLayout = inflater.inflate(R.layout.item_home_empty_list_layout, mFooterView, false);
        mNoDataLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoDataLayout);
        return mFooterView;
    }

    protected void setFootLoaderType(@LoadType int type) {
        if (mFooterView == null) {
            return;
        }
        switch (type) {
            case LoadType.LOADING:
                mLoadingLayout.setVisibility(View.VISIBLE);
                mNoMoreLayout.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.GONE);
                break;
            case LoadType.NO_DATA:
                mNoDataLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setVisibility(View.GONE);
                mNoMoreLayout.setVisibility(View.GONE);
                break;
            case LoadType.NO_MORE_DATA:
                mNoMoreLayout.setVisibility(View.VISIBLE);
                mNoDataLayout.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void resetRequest() {

    }
}
