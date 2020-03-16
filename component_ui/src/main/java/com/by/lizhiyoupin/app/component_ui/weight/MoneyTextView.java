package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.by.lizhiyoupin.app.component_ui.R;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/26 17:08
 * Summary: 用于 金额  相关TextView
 */
public class MoneyTextView extends AppCompatTextView {
    Typeface typeface;
    public MoneyTextView(Context context) {
        this(context,null);
    }

    public MoneyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MoneyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MoneyTextView);
        boolean bold=typedArray.getBoolean(R.styleable.MoneyTextView_type_bold,false);
        if (bold){
            typeface=Typeface.createFromAsset(context.getAssets(),"fonts/font_bold.otf");
        }else {
            typeface=Typeface.createFromAsset(context.getAssets(),"fonts/font_regular.otf");
        }
        typedArray.recycle();
        setTypeface(typeface);
    }
}
