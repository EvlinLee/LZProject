package com.by.lizhiyoupin.app.component_ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.FixMemLeak;
import com.by.lizhiyoupin.app.common.utils.ThreadUtils;
import com.by.lizhiyoupin.app.component_ui.dialog.DetailLoadingDialog;
import com.by.lizhiyoupin.app.component_ui.dialog.LoadingDialog;
import com.by.lizhiyoupin.app.stack.ActivityStack;
import com.gyf.immersionbar.ImmersionBar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/19 20:52
 * Summary:
 */
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    private Dialog mLoadingDialog;
    private Dialog mDteailLoadingDialog;
    private Runnable mLoadingRunnable;
    private Handler mHandler;
    private boolean isBackGround;

    /**
     * Init immersion bar.
     */
    protected void initImmersionBar(@ColorInt int StatusBarColor, boolean fontDark) {
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .navigationBarDarkIcon(true)
                .navigationBarColorInt(Color.WHITE)
                //.autoNavigationBarDarkModeEnable(true)
                .statusBarDarkFont(fontDark)
                .flymeOSStatusBarFontColorInt(fontDark ? Color.BLACK : Color.WHITE)
                .statusBarColorInt(StatusBarColor)
                .init();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mHandler = new Handler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            //8.0版本以上windowIsTranslucent=true与screenOrientation 若不是全屏，同时设置在manifest会冲突
            //8.0版本取消设置方向
            boolean result = fixOrientation();
            LZLog.i(TAG, "onCreate fixOrientation when Oreo, result = " + result);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //微信分享本地图片 防止android.os.FileUriExposedException 异常
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        super.onCreate(savedInstanceState);
        ActivityStack.instance().addActivity(this);
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ActivityStack.instance().removeActivity(this);
        dismissLoadingDialog();
        FixMemLeak.fixHuaweiLeak(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        ActivityStack.instance().removeActivity(this);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (IllegalStateException e) {
            //防止出现IllegalStateException: Can not perform this action after onSaveInstanceState
            LZLog.w(TAG, "onBackPressed Exception, msg" + e.getMessage());
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //当前窗体得到或失去焦点的时候的时候调用。这是这个活动是否是用户可见的最好的指标
        isBackGround = !hasFocus;
        LZLog.d(TAG, "=================>>>> onWindowFocusChanged " + hasFocus);
    }

    public boolean isBackGround() {
        return isBackGround;
    }


    public boolean isDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (this == null || this.isDestroyed()) {
                return true;
            }
        } else {
            if (this == null || this.isFinishing()) {
                return true;
            }
        }
        return false;
    }

    public void showLoadingDialog(){
        showLoadingDialog(false);
    }
    /**
     * 显示loading框
     */
    public void showLoadingDialog(boolean full) {
        if (isDestroy() || isFinishing()) {
            return;
        }

        if (ThreadUtils.isMainThread()) {
            showLoadingDialogOnUI(full);
        } else {
            Runnable runnable = mLoadingRunnable;
            if (runnable == null) {
                runnable = () -> showLoadingDialogOnUI(full);
                mLoadingRunnable = runnable;
            }

            mHandler.post(runnable);
        }
    }

    public void showLoadingDialogDelay(long delay) {
        if (isDestroy()) {
            return;
        }

        mHandler.postDelayed(mLoadingRunnable, delay);
    }

    private void showLoadingDialogOnUI(boolean full) {
        if (isDestroy()) {
            return;
        }
        if (full){
            if (mDteailLoadingDialog==null){
                mDteailLoadingDialog=new DetailLoadingDialog(this);
            }
            mDteailLoadingDialog.show();
        }else {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(this);
            }
            mLoadingDialog.show();
        }

    }

    public void dismissLoadingDialog(){
        dismissLoadingDialog(false);
    }
    /**
     * 隐藏 loading框
     */
    public void dismissLoadingDialog(boolean full) {
        mHandler.removeCallbacks(mLoadingRunnable);
        if (isDestroy()) {
            return;
        }
        if (full){
            if (mDteailLoadingDialog!=null){
                mDteailLoadingDialog.dismiss();
            }
        }else {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
        }

    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    public void initRefreshLayout(SwipeRefreshLayout mSwipeRefreshLayout, SwipeRefreshLayout.OnRefreshListener listener) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setNestedScrollingEnabled(true);
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        mSwipeRefreshLayout.setProgressViewOffset(true, -0, 100);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        mSwipeRefreshLayout.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE);
        //设置触发刷新的距离
        mSwipeRefreshLayout.setDistanceToTriggerSync(200);
        //如果c0hild是自己自定义的view，可以通过这个回调，告诉mSwipeRefreshLayoutchild是否可以滑动
        mSwipeRefreshLayout.setOnChildScrollUpCallback(null);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }
}
