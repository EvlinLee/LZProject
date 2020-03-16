package com.by.lizhiyoupin.app.component_umeng.share.holder.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

/**
 * ViewHolder基类
 * 需要处理空实现 {@link #initView(ViewGroup)},将View控件初始化，放到此处
 * {@link View.OnAttachStateChangeListener#onDetachedFromWindow()} 释放View生命周期的资源
 *{@link View.OnAttachStateChangeListener#onAttachedToWindow()}  加载View生命周期的资源
 */
@SuppressWarnings("ALL")
public class BaseViewHolder implements View.OnAttachStateChangeListener{

    private Context mContext;

    protected ViewGroup mViewParent;

    public BaseViewHolder(Context context, ViewGroup viewParent) {
        this.mContext = context;
        this.mViewParent = viewParent;

        mViewParent.addOnAttachStateChangeListener(this);
        initView(mViewParent);
    }

    /**
     * 初始化UI控件
     * @param viewGroup  viewGroup == {@link #mViewParent}
     */
    public void initView(ViewGroup viewGroup) {

    }

    public Context getContext() {
        return mContext;
    }

    public String getString(int resId) {
        String string = "";
        if (mContext != null) {
            string = mContext.getResources().getString(resId);
        }
        return string;
    }

    public int getColor(int resId) {
        int color = -1;
        if (mContext != null) {
            color = mContext.getResources().getColor(resId);
        }
        return color;
    }

    public void setEnable(boolean enable) {
        if (mViewParent != null) {
            mViewParent.setEnabled(enable);
        }
    }

    public void setVisable(boolean visable) {
        if (mViewParent != null) {
            mViewParent.setVisibility(visable ? View.VISIBLE : View.GONE);
        }
    }

    public void setClickListener(View.OnClickListener listener) {
        mViewParent.setOnClickListener(listener);
    }

    @Override
    @CallSuper
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    @CallSuper
    public void onViewDetachedFromWindow(View v) {
        if (mViewParent != null) {
            mViewParent.removeOnAttachStateChangeListener(this);
        }
    }

    public @Nullable <T> T findViewById(int resId) {
        try {
            return (T) mViewParent.findViewById(resId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
