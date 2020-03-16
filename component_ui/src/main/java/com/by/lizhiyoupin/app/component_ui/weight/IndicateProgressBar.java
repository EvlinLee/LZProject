package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.R;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/5 16:45
 * Summary:
 */
public class IndicateProgressBar extends  View {

    private float x = 10;
    private String progressText = "0%";
    private static final String TAG = IndicateProgressBar.class.getSimpleName();
    private int width;
    private int height;
    private int cirlceCenterY;
    private int bgCenterY;
    private int bgCenterY2;
    private Paint backPaint;
    private Paint progressPaint;
    private Paint indicateTextPaint;
    private Paint indicateBackPaint;
    private int radius = 10; //进度条四个角的角度px
    private int indicatorRadius = 8; //进度指示器四个角的角度px
    private int defaultMargin = 30; //进度指示器默认多一点长度
    private int max = 100;//进度最大值
    private int progress = 0;//进度0-100
    private boolean isCanTouch = true;//进度条是否可以手动拖动
    private int startProgressColor = 0xfff29310;
    private int textColor = 0xffef4f37;
    private int gray = 0xfff5f5f5;
    private RectF mBackRectF;
    private RectF mProgressRectF;
    private  Bitmap bitmap;
    public IndicateProgressBar(Context context) {
        this(context, null);
    }

    public IndicateProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBitmap(context);
        initView(context);
    }

    private void initView(Context context) {
//        Android在用画笔的时候有三种Style，分别是
//        Paint.Style.STROKE 只绘制图形轮廓（描边）//空心效果
//        Paint.Style.FILL 只绘制图形内容
//        Paint.Style.FILL_AND_STROKE 既绘制轮廓也绘制内容

        //Paint.ANTI_ALIAS_FLAG 抗锯齿
        textColor=getResources().getColor(R.color.color_666666);
        startProgressColor=context.getResources().getColor(R.color.color_8889FA);
        //进度条背景画笔
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(gray);
        backPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //进度条进度画笔
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setColor(startProgressColor);
        //进度条指示器框画笔
        indicateBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicateBackPaint.setColor(textColor);
        indicateBackPaint.setTextSize(32);

        //进度条指示器文本画笔
        indicateTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicateTextPaint.setColor(textColor);
        indicateTextPaint.setTextSize(18);
        cirlceCenterY= DeviceUtil.dip2px(context,10);
        bgCenterY= DeviceUtil.dip2px(context,18);
        bgCenterY2= DeviceUtil.dip2px(context,9);
        mBackRectF = new RectF();

        mProgressRectF = new RectF();

    }

    private void initBitmap(Context context) {
          bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.download_progress_)).getBitmap();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int start=bitmap.getWidth()/2;
        width = getWidth()-bitmap.getWidth();//(int) (indicateTextPaint.measureText(max + "%") );
        height = getHeight();
        //画背景
        mBackRectF.set(start, height -bgCenterY, start+width, height-bgCenterY2);
        canvas.drawRoundRect(mBackRectF, radius, radius, backPaint);

        //画进度
        mProgressRectF.set(start, height -bgCenterY, start+width * getScale(), height-bgCenterY2 );
      //  Shader shader = new LinearGradient(start, 0, 400, 400, startProgressColor, Color.RED, Shader.TileMode.REPEAT);//渐变
       // progressPaint.setShader(shader);
        canvas.drawRoundRect(mProgressRectF, radius, radius, progressPaint);

        //画指示器边框
        float left = getScale() * width+start;

        if (left <= 0f) {
            left = start;
        }
        if (left >= width+start) {
            left = width+start;
        }
        //外圆
        indicateBackPaint.setColor(startProgressColor);
        canvas.drawCircle(left,height-bgCenterY+bgCenterY2/2,bgCenterY2,indicateBackPaint);
        //内圆
        indicateBackPaint.setColor(Color.WHITE);
        canvas.drawCircle(left,height-bgCenterY+bgCenterY2/2,bgCenterY2/2,indicateBackPaint);

        //画百分比外框
        canvas.drawBitmap(bitmap,left-bitmap.getWidth()/2,5,indicateBackPaint);
        //画百分比
        canvas.drawText(progressText, left- indicateTextPaint.measureText(progressText)/2, bitmap.getHeight()/2+5, indicateTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isCanTouch) {//开启可手动拖动
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    x = event.getX();//基于控件的坐标
                /*float rawX = event.getRawX();//基于屏幕的坐标
                Log.i(TAG, "x==: " + x);
                Log.i(TAG, "rawX==: " + rawX);
                Log.i(TAG, "width==: " + width);
                Log.i(TAG, "height==: " + height);*/
                    int count = (int) x * 100 / width;
                    if (count > 100) {
                        count = 100;
                    } else if (count < 0) {
                        count = 0;
                    }
                    progressText = count + "%";
                    setProgress(count);
                    Log.i(TAG, "progressText==: " + progressText);
                    invalidate();//主线程中调用刷新
                    // postInvalidate();//可在非UI线程中调用刷新,底层还是使用handler发送到主线程刷新重绘

                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }

            return true;
        }else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * 是否开启拖动有效
     * 默认可以手动拖动
     *
     * @param isOpen true开启可以手动拖动
     */
    public void setCanTouch(boolean isOpen) {
        isCanTouch = isOpen;
    }


    /**
     * 设置进度，getScale（）内会调用
     *
     * @param progress 0-100 ，最大进度默认100
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * 设置进度，getScale（）内会调用
     *
     * @param progress 进度 0-100
     * @param max      最大进度 ，不写则默认100
     */
    public void setProgress(int progress, int max) {
        this.progress = progress;
        this.max = max;
    }

    public void setProgressInvalidate(int progress){
        this.progress = progress;
        invalidate();
    }
    /**
     * 进度显示百分数
     *
     * @param strText 如写 70%
     */
    private void setProgeressText(String strText) {
        progressText = strText;
    }

    /**
     * 进度比例小数
     *
     * @return
     */
    private float getScale() {
        float scale;
        if (max == 0) {
            scale = 0;
        } else {
            scale = (float) progress / (float) max;
        }
        setProgeressText((int) (scale * 100) + "%");
        return scale;
    }
}
