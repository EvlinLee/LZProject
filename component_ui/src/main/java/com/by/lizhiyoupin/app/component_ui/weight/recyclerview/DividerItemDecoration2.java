package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.by.lizhiyoupin.app.component_ui.R;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 线性布局分割线
 **/
public class DividerItemDecoration2 extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;
    private int mDividerColor;

    protected int mDividerHeight;
    protected int mDividerWidth;

    protected int mOrientation;
    private Paint mPaint;

    private int leftOffset;
    private final Rect mBounds = new Rect();


    public DividerItemDecoration2(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);//系统属性中获取
        a.recycle();
        mDividerColor = ContextCompat.getColor(context, R.color.list_divider);
        mDividerHeight = context.getResources().getDimensionPixelSize(R.dimen.d_01);
        mDividerWidth = context.getResources().getDimensionPixelSize(R.dimen.d_01);
        setOrientation(orientation);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mDividerColor);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerHeight = dividerHeight;
    }

    public void setDividerWidth(int dividerWidth) {
        mDividerWidth = dividerWidth;
    }

    public void setDividerColor(int dividerColor) {
        mDividerColor = dividerColor;
        mPaint.setColor(mDividerColor);
    }

    @Override //在RecyclerView的onDraw中执行
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void setLeftOffset(int offset) {
        leftOffset = offset;
    }

    /**
     * 绘制纵向列表时的分隔线  这时分隔线是横着的
     * 每次 left相同，top根据child变化，right相同，bottom也变化
     *
     * @param c
     * @param parent
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft() + padding;
        final int right = parent.getWidth() - parent.getPaddingRight() - padding;
        if (0 != leftOffset) {
            left += leftOffset;
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - bottomNotDrawOffset - 1; i++) {//最后一个item不画分割线
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }


    //    @Override  //预留item间隙
//    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
//        if (mOrientation == VERTICAL_LIST) {
//            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
//        } else {
//            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
//        }
//    }
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(child.getTranslationX());
            final int left = right - mDividerWidth;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
        canvas.restore();
    }

    private int padding;
    private int bottomNotDrawOffset = 0;

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setBottomNotDrawOffset(int bottomNotDrawOffset) {
        this.bottomNotDrawOffset = bottomNotDrawOffset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildLayoutPosition(view);
        int itemCount = state.getItemCount();
        if (pos >= itemCount - 1 - bottomNotDrawOffset) {
            outRect.set(0, 0, 0, 0);
        } else {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDividerHeight);
            } else {
                outRect.set(0, 0, mDividerWidth, 0);
            }
        }
    }
}