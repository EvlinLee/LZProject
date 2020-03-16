package com.by.lizhiyoupin.app.loader.facade;

import android.view.View;

import com.by.lizhiyoupin.app.loader.IEmptyView;
import com.by.lizhiyoupin.app.loader.LoadType;


public class DefaultEmptyView implements IEmptyView {
    private View contentView;
    private final View mLoadingLayoutCenter;
    private final View mFailRetryLayoutCenter;
    private final View mEmptyView;

    public DefaultEmptyView(View contentView, View loadingLayoutCenter, View failRetryLayoutCenter,
                     View emptyView, View.OnClickListener retryClickListener) {
        this.contentView = contentView;
        this.mLoadingLayoutCenter = loadingLayoutCenter;
        this.mFailRetryLayoutCenter = failRetryLayoutCenter;
        this.mEmptyView = emptyView;
        if (mFailRetryLayoutCenter!=null){
            this.mFailRetryLayoutCenter.setOnClickListener(retryClickListener);
        }
    }

    @Override
    public void showEmpty(@LoadType int type) {
        if (contentView != null) {
            contentView.setVisibility(View.GONE);
        }
        switch (type) {
            default:
                visible(mEmptyView);
                gone(mFailRetryLayoutCenter);
                gone(mLoadingLayoutCenter);
                break;
            case LoadType.NO_DATA:
                visible(mEmptyView);
                gone(mFailRetryLayoutCenter);
                gone(mLoadingLayoutCenter);
                break;
            case LoadType.NO_MORE_DATA:
                break;
            case LoadType.LOADING:
                gone(mEmptyView);
                gone(mFailRetryLayoutCenter);
                visible(mLoadingLayoutCenter);
                break;
            case LoadType.NETWORK_ERROR:
                gone(mEmptyView);
                visible(mFailRetryLayoutCenter);
                gone(mLoadingLayoutCenter);
                break;
            case LoadType.REQUEST_ERROR:
                gone(mEmptyView);
                visible(mFailRetryLayoutCenter);
                gone(mLoadingLayoutCenter);
                break;
        }
    }

    private static void visible(View view) {
        visibility(view, View.VISIBLE);
    }

    private static void gone(View view) {
        visibility(view, View.GONE);
    }

    private static void invisible(View view) {
        visibility(view, View.INVISIBLE);
    }

    private static void visibility(View view, int visibility) {
        if (view != null) view.setVisibility(visibility);
    }

    @Override
    public void hideEmpty() {
        if (contentView != null) {
            contentView.setVisibility(View.VISIBLE);
        }
        if (mLoadingLayoutCenter != null) {
            mLoadingLayoutCenter.setVisibility(View.GONE);
        }
        if (mFailRetryLayoutCenter != null) {
            mFailRetryLayoutCenter.setVisibility(View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
    }
}
