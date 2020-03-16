package com.by.lizhiyoupin.app.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/17 17:54
 * Summary: 全屏
 */
public class VideoFullView extends VideoView {
    public VideoFullView(Context context) {
        super(context);
    }

    public VideoFullView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoFullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//这里重写onMeasure的方法
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(0, widthMeasureSpec);//得到默认的大小（0，宽度测量规范）
        int height = getDefaultSize(0, heightMeasureSpec);//得到默认的大小（0，高度度测量规范）
        setMeasuredDimension(width, height); //设置测量尺寸,将高和宽放进去
    }

}
