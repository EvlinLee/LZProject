package com.by.lizhiyoupin.app.main.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/11 10:03
 * Summary:
 */
public class ActionBarBehavior extends CoordinatorLayout.Behavior<RelativeLayout> {

    private WeakReference<View> dependentView;
    private int statusHeight;
    private float scrollHeight;
    private Context mContext;
    private View getDependentView() {
        return dependentView.get();
    }

    public ActionBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        statusHeight = DeviceUtil.getStatusBarHeight(context);

        scrollHeight = context.getResources().getDimension(R.dimen.fans_collapsed_header_height);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull RelativeLayout child, @NonNull View dependency) {
        //这里找出依赖视图的view
        if (dependency != null && dependency.getId() == R.id.fans_header_cl) {
            dependentView = new WeakReference<>(dependency);
            return true;
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull RelativeLayout child, int layoutDirection) {
        //负责对被 Behavior 控制的视图进行布局

        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull RelativeLayout child, @NonNull View dependency) {
        //根据依赖视图进行调整的方法，当依赖视图发生变化时，这个方法就会被调用
        //目标视图就是behavior属性写的view及child
        //控制child的Y位置为 以来视图的高度（可能被改变，所以不固定）+以来视图移动的高度

    /*    float progress = Math.abs(dependency.getTranslationY() / (dependency.getHeight() - statusHeight - scrollHeight));
        if (progress > 1f) {
            progress = 1f;
        } else if (progress < 0) {
            progress = 0;
        }
        int alpha= (int) (progress*255);

        child.setBackgroundColor(ColorUtils.setAlphaComponent(Color.BLACK, alpha));*/
        child.setTranslationY(statusHeight);
        return true;
    }


}
