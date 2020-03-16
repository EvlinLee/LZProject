package com.by.lizhiyoupin.app.component_ui.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ThreadUtils;
import com.by.lizhiyoupin.app.component_ui.dialog.LoadingDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/19 20:53
 * Summary:
 */
public class BaseFragment extends BaseLazyFragment {
    private static final String TAG=BaseFragment.class.getSimpleName();
    private LoadingDialog mLoadingDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onFirstUserVisible() {
        LZLog.d(TAG, "lifecycle onUserVisible() : first = true " + this);

    }

    @Override
    public void onUserVisible() {
        LZLog.d(TAG, "lifecycle onUserVisible() : first = false " + this);

    }

    @Override
    public void onFirstUserInvisible() {
        LZLog.d(TAG, "lifecycle onUserInvisible() : first = true " + this);

    }

    @Override
    public void onUserInvisible() {
        LZLog.d(TAG, "lifecycle onUserInvisible() : first = false " + this);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LZLog.d(TAG, "onHiddenChanged : " + this + " , hidden = " + hidden);
        super.onHiddenChanged(hidden);
        setUserVisibleHint(!hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LZLog.d(TAG, "setUserVisibleHint() isVisibleToUser = " + isVisibleToUser + " : " + this);
        super.setUserVisibleHint(isVisibleToUser);
    }
    //判断activity是不是空
    public boolean isActivityDestroy(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity()==null || getActivity().isDestroyed()) {
                return true;
            }
        }else {
            if(getActivity()==null || getActivity().isFinishing()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean isRealHidden() {
        return isHidden();
    }

    public void showLoadingDialog() {
        if (isActivityDestroy()) {
            return;
        }
        ThreadUtils.runOnUiThread(() -> {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(getContext());
            }
            mLoadingDialog.show();
        });
    }

    public void dismissLoadingDialog() {
        if (isActivityDestroy()) {
            return;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
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
