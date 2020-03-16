package com.by.lizhiyoupin.app.common.base;

import java.lang.ref.WeakReference;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/20 17:59
 * Summary:
 */
public abstract class BasePresenter<V extends BaseView> {

    private WeakReference<V> mWeakReference;

    /**
     *  先调用 isViewAttached() 判空
     * @return
     */
    public V getMVPView() {
        if (mWeakReference != null) {
            return mWeakReference.get();
        }
        return null;
    }

    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param view view
     */
    public void attachView(V view) {
        mWeakReference = new WeakReference<>(view);
    }

    /**
     * 解除绑定view，一般在onDestroy中调用
     */

    public void detachView() {
        if (mWeakReference != null) {
            mWeakReference.clear();
            mWeakReference = null;
        }
    }

    /**
     * View是否绑定
     *
     * @return true 已经绑定
     */
    public boolean isViewAttached() {
        return mWeakReference != null && mWeakReference.get() != null;
    }
}
