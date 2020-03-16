package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.by.lizhiyoupin.app.component_ui.R;


/**
 * (Hangzhou) <br/>
 *
 * @author: wzm <br/>
 * @date :  2019/6/13 19:41 <br/>
 * Summary: 阴影 控件
 */
public class SimpleShadowLayout extends FrameLayout {
    public static final int SHAPE_RECTANGLE = 0x0001;
    public static final int SHAPE_OVAL = 0x0010;
    /**
     * 阴影颜色
     */
    private int mShadowColor;
    /**
     * 阴影的大小范围
     * radius:模糊半径，radius越大越模糊，越小越清晰，但是如果radius设置为0，则阴影消失不见
     */
    private float mShadowRadius;
    /**
     * 圆角
     */
    private float mCornerRadius;
    /**
     * 阴影 x 轴的偏移量
     * dx:阴影的横向偏移距离，正值向右偏移，负值向左偏移
     */
    private float mDx;
    /**
     * 阴影 y 轴的偏移量
     * dy:阴影的纵向偏移距离，正值向下偏移，负值向上偏移
     */
    private float mDy;
    private Paint shadowPaint;

    /**
     * 阴影的形状，圆形/矩形
     */
    private int mShadowShape = SHAPE_RECTANGLE;
    private boolean mInvalidateShadowOnSizeChanged = true;
    private boolean mForceInvalidateShadow = false;

    public SimpleShadowLayout(Context context) {
        this(context, null);
    }

    public SimpleShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray attr = getTypedArray(context, attrs, R.styleable.SimpleShadowLayout);
        if (attr == null) {
            return;
        }

        try {
            mCornerRadius = attr.getDimension(R.styleable.SimpleShadowLayout_sl_cornerRadius, 0);
            mShadowRadius = attr.getDimension(R.styleable.SimpleShadowLayout_sl_shadowRadius, 0);
            mDx = attr.getDimension(R.styleable.SimpleShadowLayout_sl_shadowDx, 0);
            mDy = attr.getDimension(R.styleable.SimpleShadowLayout_sl_shadowDy, 0);
            mShadowShape = attr.getInt(R.styleable.SimpleShadowLayout_sl_shadowShape, SHAPE_RECTANGLE);
            mShadowColor = attr.getColor(R.styleable.SimpleShadowLayout_sl_shadowColor, getResources().getColor(R.color.black));
        } finally {
            attr.recycle();
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return 0;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0 && (getBackground() == null || mInvalidateShadowOnSizeChanged || mForceInvalidateShadow)) {
            mForceInvalidateShadow = false;
            setBackgroundCompat(w, h);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mForceInvalidateShadow) {
            mForceInvalidateShadow = false;
            setBackgroundCompat(right - left, bottom - top);
        }
    }

    public void setInvalidateShadowOnSizeChanged(boolean invalidateShadowOnSizeChanged) {
        mInvalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged;
    }

    public void invalidateShadow() {
        mForceInvalidateShadow = true;
        requestLayout();
        invalidate();
    }

    private void initView(Context context, AttributeSet attrs) {
        initAttributes(context, attrs);
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.TRANSPARENT);
        shadowPaint.setStyle(Paint.Style.FILL);
        int xPadding = (int) (mShadowRadius + Math.abs(mDx));
        int yPadding = (int) (mShadowRadius + Math.abs(mDy));
        setPadding(xPadding, yPadding, xPadding, yPadding);
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(int w, int h) {
        Bitmap bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowRadius, mDx, mDy, mShadowColor);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable);
        } else {
            setBackground(drawable);
        }
    }


    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius,
                                      float dx, float dy, int shadowColor) {

        Bitmap outputBitmap = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(outputBitmap);

        RectF shadowRect = new RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius);

        if (dy > 0) {
            shadowRect.top += dy;
            shadowRect.bottom -= dy;
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy);
            shadowRect.bottom -= Math.abs(dy);
        }

        if (dx > 0) {
            shadowRect.left += dx;
            shadowRect.right -= dx;
        } else if (dx < 0) {
            shadowRect.left += Math.abs(dx);
            shadowRect.right -= Math.abs(dx);
        }

        shadowPaint.reset();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.TRANSPARENT);
        shadowPaint.setStyle(Paint.Style.FILL);

        if (!isInEditMode()) {
            shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        }
        if (mShadowShape == SHAPE_RECTANGLE) {
            canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        } else if (mShadowShape == SHAPE_OVAL) {
            canvas.drawCircle(shadowRect.centerX(), shadowRect.centerY(), Math.min(shadowRect.width(), shadowRect.height()) / 2, shadowPaint);
        }

        return outputBitmap;
    }
}
