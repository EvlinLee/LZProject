package com.by.lizhiyoupin.app.main.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/26 16:33
 * Summary:渐变色 TextView
 */
public class GradientTextView extends AppCompatTextView {
    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private int mViewWidth = 0;
    private Rect mTextBound = new Rect();
    private int mStartColor;
    private int mEndColor;
    private int mSize;

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttr(context,attrs);
        mPaint = getPaint();
        mPaint.setTextSize(mSize);
    }

    private void readAttr(Context context,AttributeSet set) {
        TypedArray typedArray = getContext().obtainStyledAttributes(set, R.styleable.GradientTextView);
        mStartColor = typedArray.getColor(R.styleable.GradientTextView_startGradientColor, Color.parseColor("#FF3A80"));
        mEndColor = typedArray.getColor(R.styleable.GradientTextView_endGradientColor, Color.parseColor("#FF3745"));
        mSize = typedArray.getDimensionPixelSize(R.styleable.GradientTextView_TextGradientSize, DeviceUtil.dip2px(context, 12));
        typedArray.recycle();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        mViewWidth = getMeasuredWidth();
        String mTipText = getText().toString();
        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                new int[]{mStartColor, mEndColor},
                null, Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
    }
}
