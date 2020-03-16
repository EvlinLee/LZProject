package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.R;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/4/18 11:11
 * Summary: 自适应 自动缩放字体
 */
public class AutoFitTextView extends AppCompatTextView {
    public static final String TAG = AutoFitTextView.class.getSimpleName();
    private float mDefaultTextSize;
    private Paint mTextPaint;

    private Context mContext;
    private float textFitSize=3f;
    public AutoFitTextView(Context context) {
        this(context, null);
    }

    public AutoFitTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        if (attrs != null) {
            TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.AutoFitTextView);
            textFitSize = typed.getDimension(R.styleable.AutoFitTextView_text_fit_size, DeviceUtil.dip2px(mContext,1));

            typed.recycle();
        }

        initAttr();
    }


    private void initAttr() {
        mTextPaint = new Paint();
        mTextPaint.set(getPaint());
        mDefaultTextSize = getTextSize();
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        refitText(text.toString(), getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refitText(getText().toString(), getWidth());
    }

    public void refitText(String text, int textWidth) {
        LZLog.d("refit", "refit:" + text + "width:" + textWidth);
        if (text.length() < 4) {
            //小于4个字时不缩放
            if(mDefaultTextSize > 0) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, mDefaultTextSize);
            }
            return;
        }
        if (textWidth > 0) {
            int availableTextWidth = textWidth - getPaddingLeft() - getPaddingRight();
            float tsTextSize = mDefaultTextSize;
            mTextPaint.setTextSize(tsTextSize);
            float length = mTextPaint.measureText(text);
            while (length > availableTextWidth) {
                tsTextSize = tsTextSize - textFitSize;
              LZLog.i(TAG,"tsTextSize2=="+tsTextSize);
                mTextPaint.setTextSize(tsTextSize);
                length = mTextPaint.measureText(text);
            }
            setTextSize(TypedValue.COMPLEX_UNIT_PX, tsTextSize);
            invalidate();
    }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
