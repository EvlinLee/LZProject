package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;


import com.by.lizhiyoupin.app.common.log.LZLog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public abstract class EndlessRecyclerOnScrollListener extends
        RecyclerView.OnScrollListener {
    private static final String TAG = "EndlessRecyclerOnScrollListener";

    private int previousTotal = 0;
    private boolean loading = true;

    private int currentPage = 1;

    private LinearLayoutManager mLinearLayoutManager;
    private int lastVisibleItem;

    public EndlessRecyclerOnScrollListener(
            LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        LZLog.d(TAG, "onScrolled: lastVisibleItem = " + lastVisibleItem);
        LZLog.d(TAG, "onScrolled: mLinearLayoutManager.getItemCount() = " + mLinearLayoutManager.getItemCount());

        LZLog.d(TAG, "onScrolled: scrollState = " + recyclerView.getScrollState());
        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mLinearLayoutManager.getItemCount()) {
            LZLog.d(TAG, "onScrolled: onLoadMore");
            onLoadMore(0);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

    }

    public abstract void onLoadMore(int currentPage);
}
