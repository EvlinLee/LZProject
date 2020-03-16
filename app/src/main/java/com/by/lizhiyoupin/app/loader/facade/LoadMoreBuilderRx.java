package com.by.lizhiyoupin.app.loader.facade;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.loader.ILoadProgressView;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class LoadMoreBuilderRx<T, Page> {
    private LoadMoreHelperRx.Loader<T, Page> loader;
    private IPageStrategy<T, Page> pageStrategy;
    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private View emptyView;
    private View emptyLoadingView;
    private View emptyRetryView;
    private CharSequence noMoreText;
    private CommonRecyclerViewAdapter adapter;
    private ILoadProgressView<T> progressView;
    private int noMoreGravity=Gravity.CENTER;
    private int noMoreLayoutColorRes=-1;

    public LoadMoreBuilderRx(Context context) {
        this.context = context;
    }

    public LoadMoreHelperRx<T, Page> build() {
        LoadMoreHelperRx<T, Page> loadMoreHelper = new LoadMoreHelperRx<>();

        if (progressView == null) {
            progressView=new DefaultLoadMoreProgressView<T>(
                    context,
                    refreshLayout,
                    adapter,
                    noMoreText,noMoreGravity,noMoreLayoutColorRes,
                    v -> loadMoreHelper.loadDataMore()) {
                @Override
                public void showData(List<T> data, @DataChangeType int changeType, int start, int count, boolean hasMore) {
                    if (adapter == null) {
                        return;
                    }
                    adapter.setListData(data);
                    switch (changeType) {
                        default:
                        case DataChangeType.CHANGE:
                            adapter.notifyDataSetChanged();
                            break;
                        case DataChangeType.INSERT_RANGE:
                            adapter.notifyNormalItemRangeInserted(start, count);
                            break;
                        case DataChangeType.INSERT:
                            adapter.notifyNormalItemInserted(start);
                            break;
                        case DataChangeType.REMOVE:
                            adapter.notifyNormalItemRemoved(start, 1);
                            break;
                    }
                }
            };
        }
        loadMoreHelper.setProgressView(progressView);
        loadMoreHelper.setEmptyView(new DefaultEmptyView(recyclerView, emptyLoadingView, emptyRetryView, emptyView, v -> loadMoreHelper.loadData()));
        loadMoreHelper.setPageStrategy(pageStrategy);
        loadMoreHelper.setLoader(loader);
        return loadMoreHelper;
    }

    public LoadMoreBuilderRx<T, Page> recyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public LoadMoreBuilderRx<T, Page> refreshLayout(SmartRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        return this;
    }

    public LoadMoreBuilderRx<T, Page> emptyView(View view) {
        this.emptyView = view;
        return this;
    }

    public LoadMoreBuilderRx<T, Page> emptyLoadingView(View view) {
        this.emptyLoadingView = view;
        return this;
    }

    public LoadMoreBuilderRx<T, Page> progressView(ILoadProgressView<T> loadProgressView) {
        this.progressView = loadProgressView;
        return this;
    }

    public LoadMoreBuilderRx<T, Page> emptyRetryView(View view) {
        this.emptyRetryView = view;
        return this;
    }
    public LoadMoreBuilderRx<T, Page> noMoreText(CharSequence noMoreText) {
        this.noMoreText = noMoreText;
        return this;
    }
    public LoadMoreBuilderRx<T, Page> noMoreGravity(int gravity) {
        this.noMoreGravity = gravity;
        return this;
    } public LoadMoreBuilderRx<T, Page> noMoreLayoutColorRes(int colorRes) {
        this.noMoreLayoutColorRes = colorRes;
        return this;
    }

    public LoadMoreBuilderRx<T, Page> adapter(CommonRecyclerViewAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public LoadMoreBuilderRx<T, Page> pageStrategy(IPageStrategy<T, Page> pageStrategy) {
        this.pageStrategy = pageStrategy;
        return this;
    }

    public LoadMoreBuilderRx<T, Page> loader(LoadMoreHelperRx.Loader<T, Page> loader) {
        this.loader = loader;
        return this;
    }

}
