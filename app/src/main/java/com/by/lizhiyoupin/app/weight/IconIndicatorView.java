package com.by.lizhiyoupin.app.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.by.lizhiyoupin.app.R;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/30 17:20
 * Summary:
 */
public class IconIndicatorView extends View {

    private RectF mBackRectF;
    private RectF mProgressRectF;

    private Paint backPaint;
    private Paint progressPaint;
    private int regularWidth;
    private float  mProgressStart;
    public IconIndicatorView(Context context) {
        this(context,null);
    }

    public IconIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IconIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.IconIndicatorView);
            regularWidth = typed.getDimensionPixelSize(R.styleable.IconIndicatorView_regularWidth, 50);
            typed.recycle();
        }
        initView();
    }

    private void initView() {
        mBackRectF = new RectF();
        mProgressRectF = new RectF();
        //进度条背景画笔
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(0xffE8E8EB);
        backPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //进度条进度画笔
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setColor(0xffFF005E);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        mBackRectF.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(mBackRectF,  backPaint);

        //画进度
        mProgressRectF.set(mProgressStart, 0, mProgressStart+regularWidth, getHeight());
        canvas.drawRect(mProgressRectF,  progressPaint);
    }

    /**
     * 固定宽度的滑动块宽度
     * @return
     */
    public int getRegularWidth(){
        return regularWidth;
    }

    /**
     * X滑动的距离
     * @param mProgressStart
     */
    public void setDragWidthX(float mProgressStart){
        this.mProgressStart=mProgressStart;
        invalidate();
    }
}
