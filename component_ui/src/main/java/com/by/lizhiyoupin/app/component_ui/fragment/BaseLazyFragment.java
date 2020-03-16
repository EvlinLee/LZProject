package com.by.lizhiyoupin.app.component_ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/20 15:16
 * Summary:
 */
public abstract class BaseLazyFragment extends Fragment {
    private static final String TAG = BaseLazyFragment.class.getSimpleName();
    private boolean isPrepared;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;
    /**
     * fragment嵌套时，防止手动调用子fragment的setUserVisibleHint导致
     * onResume时onUserVisible操作重复
     */
    private boolean isClassAChildFragment = true;

    public void setClassAChildFragment(boolean isChildFragment) {
        this.isClassAChildFragment = isChildFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint() && isClassAChildFragment) {
            onUserVisible();
        }
    }

    public boolean isFirstResume() {
        return isFirstResume;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint() && isClassAChildFragment) {
            onUserInvisible();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        final boolean lastIsVisibleToUser = getUserVisibleHint();
        super.setUserVisibleHint(isVisibleToUser);
        if (!isPrepared || lastIsVisibleToUser != isVisibleToUser) { // 避免反复调用
            if (isVisibleToUser) {
                if (isFirstVisible) {
                    isFirstVisible = false;
                    initPrepare();
                } else {
                    onUserVisible();
                }
            } else {
                if (isFirstInvisible) {
                    isFirstInvisible = false;
                    onFirstUserInvisible();
                } else {
                    onUserInvisible();
                }
            }
        }
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public abstract void onFirstUserVisible();

    /**
     * fragment可见（切换回来或者onResume,除去第一次）
     */
    public abstract void onUserVisible();

    /**
     * 第一次fragment不可见（不建议在此处理事件，除去第一次）
     */
    public abstract void onFirstUserInvisible();

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public abstract void onUserInvisible();
}
