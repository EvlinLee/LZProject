package com.by.lizhiyoupin.app.component_ui.poster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2018/9/13 17:39
 * Summary: 生成海报 截图
 */
public class ScreenShotPoster {
    public static final String TAG = ScreenShotPoster.class.getSimpleName();

    /**
     * 测量布局
     * @param viewGroup
     * @return
     */
    public static int[] onMeasureAndLayout(Context mContext, ViewGroup viewGroup) {
        int[] widthAndHeight = new int[]{0, 0};
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            measureView(childAt);
        }

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childen = viewGroup.getChildAt(i);
            widthAndHeight[1] += childen.getMeasuredHeight();
            int viewWidth = childen.getMeasuredWidth();
            if (widthAndHeight[0] < viewWidth) {
                widthAndHeight[0] = viewWidth;
            }
        }

        // 如果待绘制的图片宽度大于屏幕宽度，则绘制在(0,0)坐标
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        if (widthAndHeight[0] <= 0 || widthAndHeight[0] > widthPixels) {
            widthAndHeight[0] = viewGroup.getMeasuredWidth();
            viewGroup.layout(0, 0, widthAndHeight[0], widthAndHeight[1]);
        } else {
            int margin = (widthPixels - widthAndHeight[0]) / 2;
            viewGroup.layout(margin, 0, margin + widthAndHeight[0], widthAndHeight[1]);
        }
        return widthAndHeight;
    }

    /**
     * onMeasureAndLayout方法 测量完成后，生成海报图
     * @param mContext
     * @param viewGroup
     * @param wh
     * @return
     */
    public static Bitmap onDrawBitmap(Context mContext, ViewGroup viewGroup, int[] wh) {
        Bitmap bitmap = null;
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        if (wh[0] <= 0) {
            wh[0] = widthPixels;
        }
        bitmap = Bitmap.createBitmap(wh[0], wh[1], Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        viewGroup.draw(canvas);
        try {
            String filePath = FileUtil.getExternalFilesDir(ContextHolder.getInstance().getContext(), "screen_shot").getAbsolutePath() + "/poster.jpg";
            File file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 测量布局生成图片
     * @param viewGroup
     * @param mContext
     * @return
     */
    public static Bitmap shotViewGroup(ViewGroup viewGroup, Context mContext) {
        int h = 0;
        Bitmap bitmap = null;

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            measureView(childAt);
        }

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            h += viewGroup.getChildAt(i).getMeasuredHeight();
        }
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        viewGroup.layout(0, 0, viewGroup.getMeasuredWidth(), h);
        bitmap = Bitmap.createBitmap(widthPixels, h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        viewGroup.draw(canvas);
        try {
            File file = new File(FileUtil.getExternalFilesDir(ContextHolder.getInstance().getContext(), "screen_shot").getAbsolutePath() + "/poster.jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static void measureView(View child) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //headerView的宽度信息
        int widthPixels = lp.width;
        if (lp.width <= 0) {
            widthPixels = ContextHolder.getInstance().getContext().getResources().getDisplayMetrics().widthPixels;
        }
        int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, widthPixels);

        int childMeasureHeight;
        if (lp.height > 0) {
            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
            //最后一个参数表示：适合、匹配
        } else {
            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//未指定
        }
        //将宽和高设置给child
        child.measure(childMeasureWidth, childMeasureHeight);
    }


}
