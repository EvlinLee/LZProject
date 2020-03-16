package com.by.lizhiyoupin.app.loader.facade;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.loader.ILoadProgressView;
import com.by.lizhiyoupin.app.loader.LoadType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


abstract class DefaultLoadMoreProgressView<T> implements ILoadProgressView<T> {
    private static final String TAG = DefaultLoadMoreProgressView.class.getSimpleName();
    private Context context;

    private SmartRefreshLayout refreshLayout;
    final CommonRecyclerViewAdapter mAdapter;

    // 分页相关layout
    final FrameLayout mFooterView;
    final View mLoadingLayout;
    final View mNoMoreLayout;
    final View mFooterFailRetryLayout;
    TextView noMoreTv;
    CharSequence noMoreText;

    public DefaultLoadMoreProgressView(Context context,
                                       SmartRefreshLayout refreshLayout,
                                       CommonRecyclerViewAdapter adapter,
                                       CharSequence noMoreText,
                                       View.OnClickListener retryClickListener){
       this(context,refreshLayout,adapter,noMoreText, Gravity.CENTER,-1,retryClickListener);
    }

    public DefaultLoadMoreProgressView(Context context,
                                       SmartRefreshLayout refreshLayout,
                                       CommonRecyclerViewAdapter adapter,
                                       CharSequence noMoreText,int noMoreGravity,

                                       int noMoreLayoutColorRes,
                                       View.OnClickListener retryClickListener) {
        this.context = context;
        this.refreshLayout = refreshLayout;
        this.mAdapter = adapter;
        this.noMoreText = noMoreText;

        mFooterView = new FrameLayout(context);
        mFooterView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (adapter != null) {
            adapter.setFooterView(mFooterView);
        }
        final LayoutInflater inflater = LayoutInflater.from(context);
        mLoadingLayout = inflater.inflate(R.layout.list_item_loading, mFooterView, false);
      //  mFooterView.addView(mLoadingLayout);
        mNoMoreLayout = inflater.inflate(R.layout.no_more_text, mFooterView, false);
        noMoreTv = mNoMoreLayout.findViewById(R.id.more);
        if (!TextUtils.isEmpty(noMoreText)) {
            noMoreTv.setText(noMoreText);
        }
        if (noMoreLayoutColorRes!=-1){
            mNoMoreLayout.setBackgroundResource(noMoreLayoutColorRes);
        }
        noMoreTv.setGravity(noMoreGravity);
        mFooterFailRetryLayout = inflater.inflate(R.layout.fail_retry_item, mFooterView, false);
        mFooterFailRetryLayout.setOnClickListener(retryClickListener);
    }

    @Override
    public void showRefreshing(boolean active) {
        if (refreshLayout != null) {
            if (active) {
                //
            } else {
                refreshLayout.finishRefresh();
            }
        }
    }

    @Override
    public void showMsg(String msg) {
    }

    @Override
    public void showLoadError(Throwable e, boolean isEmpty) {
        LZLog.e(TAG, e.getMessage(), e);
    }

    @Override
    public void showLoader(@LoadType int type) {
        if (mFooterView == null) {
            return;
        }
        switch (type) {
            default:
                mFooterView.removeAllViews();
                break;
            case LoadType.LOADING:
                mFooterView.removeAllViews();
                mFooterView.addView(mLoadingLayout);
                break;
            case LoadType.NO_DATA:
                break;
            case LoadType.NO_MORE_DATA:
                mFooterView.removeAllViews();
                mFooterView.addView(mNoMoreLayout);
                break;
            case LoadType.NETWORK_ERROR:
                mFooterView.removeAllViews();
                mFooterView.addView(mFooterFailRetryLayout);
                break;
            case LoadType.REQUEST_ERROR:
                mFooterView.removeAllViews();
                mFooterView.addView(mFooterFailRetryLayout);
                break;
        }
    }
}
