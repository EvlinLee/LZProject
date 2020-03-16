package com.by.lizhiyoupin.app.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;

/**
 * (Hangzhou) <br/>
 *
 * @author: wzm <br/>
 * @date :  2019/9/2 18:44 <br/>
 * Summary: 图片可居中
 */
public class SpanImageSpan extends ImageSpan {
    public static final int ALIGN_CENTER = 3;//居中
    private   int mVerticalAlignment=ALIGN_BOTTOM;

    public SpanImageSpan(@NonNull Bitmap b) {
        super(b);
    }

    public SpanImageSpan(@NonNull Bitmap b, int verticalAlignment) {
        super(b, verticalAlignment);
        mVerticalAlignment=verticalAlignment;
    }

    public SpanImageSpan(@NonNull Context context, @NonNull Bitmap bitmap) {
        super(context, bitmap);
    }

    public SpanImageSpan(@NonNull Context context, @NonNull Bitmap bitmap, int verticalAlignment) {
        super(context, bitmap, verticalAlignment);
        mVerticalAlignment=verticalAlignment;
    }

    public SpanImageSpan(@NonNull Drawable drawable) {
        super(drawable);
    }

    public SpanImageSpan(@NonNull Drawable drawable, int verticalAlignment) {
        super(drawable, verticalAlignment);
        mVerticalAlignment=verticalAlignment;
    }



    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        if (mVerticalAlignment!=ALIGN_CENTER){
            super.draw(canvas,text,start,end,x,top,y,bottom,paint);
            return;
        }
        Drawable drawable = getDrawable();
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int transY = (y + fontMetricsInt.descent + y + fontMetricsInt.ascent) / 2 - drawable.getBounds().bottom/2;
        canvas.save();
        canvas.translate(x,transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
