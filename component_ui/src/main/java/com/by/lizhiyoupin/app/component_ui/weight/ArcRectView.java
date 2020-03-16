package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.R;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/26 15:35
 * Summary:  圆形弧下面透明，上面可设置颜色
 */
public class ArcRectView extends View {
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private Path mPath = new Path();
    private int mArcHeight;// 圆弧高度
    private int mEndColor;
    private int mRectHeight;//矩形颜色高度

    public ArcRectView(Context context) {
        this(context, null);

    }

    public ArcRectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ArcRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcRectView);
        mArcHeight = typedArray.getDimensionPixelSize(R.styleable.ArcRectView_arc_Height, DeviceUtil.dip2px(context, 30));
        mRectHeight = typedArray.getDimensionPixelSize(R.styleable.ArcRectView_rect_Height, DeviceUtil.dip2px(context, 40));
        mEndColor = typedArray.getColor(R.styleable.ArcRectView_arc_Color, Color.WHITE);
        typedArray.recycle();
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        //invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPaint.setColor(mEndColor);
        mPath.moveTo(0, 0);
        mPath.lineTo(0, mRectHeight);
        mPath.quadTo(mWidth / 2, mArcHeight, mWidth, mRectHeight);
        mPath.lineTo(mWidth, 0);
        mPath.lineTo(0, 0);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    public void setEndColorInvalidate(String color) {
        try {
            mEndColor = Color.parseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
            mEndColor=Color.WHITE;
        }finally {
            postInvalidate();
        }
    }
}
