package com.by.lizhiyoupin.app.weight;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.stack.ActivityStack;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/30 11:55
 * Summary:
 */
public class HomeHeaderLayout extends LinearLayout implements NestedScrollingParent {
    private final NestedScrollingParentHelper mParentHelper;
    private Context mContext;
    View edv_title,edv_header;//需要固定的header
    View edv_content;
    int titleHeight;//header上面还有的高度
    int headerHeight;
    int statusBarHeight;
    int mLastScrollerY;
    private OverScroller mScroller;
    public HomeHeaderLayout(Context context) {
        this(context,null);
    }

    public HomeHeaderLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HomeHeaderLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(getContext());
        mParentHelper=new NestedScrollingParentHelper(this);
        Activity appCompatActivity = ActivityStack.currentActivity();
        if (appCompatActivity!=null){
            statusBarHeight = DeviceUtil.getStatusBarHeight(appCompatActivity);
        }
    }

    public void setFullScreen(){
        //全屏后 则不计算状态栏高度
        statusBarHeight=0;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        edv_title = findViewById(R.id.edv_title);
        edv_header = findViewById(R.id.edv_header);
        edv_content = findViewById(R.id.edv_content);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

       // titleHeight = DeviceUtil.dip2px(mContext,44+40);
        titleHeight = edv_title.getMeasuredHeight()+statusBarHeight;
        headerHeight = edv_header.getMeasuredHeight();
        //重新测量高度，防止上滑后底部空出
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() + headerHeight+titleHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }



//以下：NestedScrollingParent接口------------------------------

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    //移动edv_content

    /**
     *
     * @param target  子View
     * @param dx 子View  x滑动距离
     * @param dy 子View  y滑动距离
     * @param consumed  父View消费的距离
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        float supposeY = edv_content.getY() - dy;//期望edv_content滑动后的位置
        //往上滑,y的边界为titleHeight
        if (dy > 0) {
            if (supposeY >= titleHeight) {
                //edv_content在edv_title下面时的滑动
                offset(dy, consumed);
            } else {
                //edv_content滑到edv_title边时，父不滑动了
                offset(0, consumed);
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

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

//以下：NestedScrollingParent接口的其他方法

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        mParentHelper.onStopNestedScroll(target);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }





}
