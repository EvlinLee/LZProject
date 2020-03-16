package com.by.lizhiyoupin.app.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/30 10:25
 * Summary:
 */
public class NestHeaderLayout extends LinearLayout implements NestedScrollingChild2, NestedScrollingParent2 {
    private final NestedScrollingParentHelper mParentHelper;
    private final NestedScrollingChildHelper mChildHelper;
    private Context mContext;
    View edv_header;//需要固定的header
    View edv_content;
    int titleHeight;//header上面还有的高度
    int headerHeight;

    public NestHeaderLayout(Context context) {
        this(context,null);
    }

    public NestHeaderLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NestHeaderLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        edv_header = findViewById(R.id.edv_header);
        edv_content = findViewById(R.id.edv_content);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //需要二次测量 重新定义高度

        titleHeight = DeviceUtil.dip2px(mContext,44+28);
        headerHeight = edv_header.getMeasuredHeight();
        //重新测量高度
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() + headerHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {

        return mChildHelper.startNestedScroll(axes,type);
    }
    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public void stopNestedScroll(int type) {
        mChildHelper.stopNestedScroll(type);
    }
    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return  mChildHelper.hasNestedScrollingParent(type);
    }


    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow, type);
    }
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }
    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
    //parent
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        //判断参数target是哪一个子view以及滚动的方向，然后决定是否要配合其进行嵌套滚动
        return true;
    }


    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mParentHelper.onNestedScrollAccepted(child, target, axes,type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mParentHelper.onStopNestedScroll(target, type);
    }
    //后于child滚动
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }
    //先于child滚动
    //前3个为输入参数， 后一个是输出参数
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //获取需要滚动的距离，根据情况决定自己是否要进行滚动，最后还要将自己滚动消费掉的距离存储在consumed数组中回传给child
        //移动edv_content
        float supposeY = edv_content.getY() - dy;//希望edv_content移动到的位置
        //往上滑,y的边界为titleHeight
        if (dy > 0) {
            if (supposeY >= titleHeight) {
                offset(dy, consumed);
            } else {
                offset((int) (edv_content.getY() - titleHeight), consumed);
            }
        }

        //往下滑,y的边界为titleHeight + headerHeight
        if (dy < 0) {
            if (!ViewCompat.canScrollVertically(target, dy)) {//当target不能向下滑时
                if (supposeY <= titleHeight + headerHeight) {
                    offset(dy, consumed);
                } else {
                    offset((int) (edv_content.getY() - headerHeight - titleHeight), consumed);
                }
            }
        }

    }
    private void offset(int dy, int[] consumed) {
        //第二个参数为正代表向下，为负代表向上
        ViewCompat.offsetTopAndBottom(edv_content, -dy);
        consumed[0] = 0;
        consumed[1] = dy;
    }
}
