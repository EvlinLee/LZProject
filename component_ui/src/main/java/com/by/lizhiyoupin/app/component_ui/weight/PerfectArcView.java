package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.R;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/5 14:22
 * Summary:
 */
public class PerfectArcView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private int mHeight;
    private int mWidth;
    private RectF mRect = new RectF();
    private RectF mBitmapRect = new RectF();
    private Point mCircleCenter;
    private float mRadius;
    private int mStartColor;
    private int mEndColor;
    private LinearGradient mLinearGradient;
    private Context mContext;

    public PerfectArcView(Context context) {
        this(context, null);
    }

    public PerfectArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public PerfectArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        readAttr(attrs);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.temp_list_detail_pics);
        mCircleCenter = new Point();
    }

    private void readAttr(AttributeSet set) {
        TypedArray typedArray = getContext().obtainStyledAttributes(set, R.styleable.PerfectArcView);
        mStartColor = typedArray.getColor(R.styleable.PerfectArcView_startColor, Color.parseColor("#FF3A80"));
        mEndColor = typedArray.getColor(R.styleable.PerfectArcView_endColor, Color.parseColor("#FF3745"));
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getHeight();
        int width = getWidth();
        mWidth = width;
        // 半径
        mRadius = width * 2;
        // 矩形
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = width;
        mRect.bottom = mHeight;
        mBitmapRect.left = DeviceUtil.dip2px(mContext, 10);
        mBitmapRect.right = width - DeviceUtil.dip2px(mContext, 10);
        mBitmapRect.top = mHeight / 2;
        mBitmapRect.bottom = mHeight;
        // 圆心坐标
        mCircleCenter.x = width / 2;
        mCircleCenter.y = mHeight - width * 2;

        mLinearGradient = new LinearGradient(width / 2, 0, width / 2, mHeight, mStartColor, mEndColor, Shader.TileMode.MIRROR);
    }


    /**
     * @param startColor
     * @param endColor
     */
    public void setColor(@ColorInt int startColor, @ColorInt int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        mLinearGradient = new LinearGradient(mWidth / 2, 0, mWidth / 2, mHeight, mStartColor, mEndColor, Shader.TileMode.MIRROR);
        invalidate();
    }

    public void setBitmap(int drawableRes){
        mBitmap = BitmapFactory.decodeResource(getResources(), drawableRes);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        int canvasWidth = getWidth();
        int canvasHeight =getHeight();
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        //画一个半径屏幕宽2倍的圆，部分在屏幕外部
        canvas.drawCircle(mCircleCenter.x, mCircleCenter.y, mRadius, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mPaint.setShader(mLinearGradient);//绘制渐变色
        canvas.drawRect(mRect, mPaint);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, null, mBitmapRect, mPaint);
        }
        canvas.restoreToCount(layerId);
        mPaint.setXfermode(null);

    }

}
