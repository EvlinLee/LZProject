package com.by.lizhiyoupin.app.loader.facade;

import android.content.Context;
import android.view.View;

import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.loader.ILoadProgressView;
import com.by.lizhiyoupin.app.loader.LoadListHelperRx;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class LoadListBuilderRx<T> {
    private LoadListHelperRx.Loader<T> loader;
    private LoadListHelperRx.SingleLoader<T> singleLoader;
    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private View emptyView;
    private View emptyLoadingView;
    private View emptyRetryView;
    private CommonRecyclerViewAdapter adapter;

    public LoadListBuilderRx(Context context) {
        this.context = context;
    }

    public LoadListHelperRx<T> build() {
        LoadListHelperRx<T> helper = new LoadListHelperRx<>();

        ILoadProgressView<T> progressView = new DefaultLoadProgressView<T>(context, refreshLayout) {
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
                        adapter.notifyItemRangeInserted(start, count);
                        break;
                    case DataChangeType.INSERT:
                        adapter.notifyItemInserted(start);
                        break;
                    case DataChangeType.REMOVE:
                        adapter.notifyItemRemoved(start);
                        break;
                }
            }
        };
        helper.setProgressView(progressView);
        helper.setEmptyView(new DefaultEmptyView(recyclerView, emptyLoadingView, emptyRetryView, emptyView, v -> helper.loadData()));
        helper.setLoader(loader);
        helper.setSingleLoader(singleLoader);

        return helper;
    }

    public LoadListBuilderRx<T> recyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public LoadListBuilderRx<T> refreshLayout(SmartRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        return this;
    }

    public LoadListBuilderRx<T> emptyView(View view) {
        this.emptyView = view;
        return this;
    }

    public LoadListBuilderRx<T> emptyLoadingView(View view) {
        this.emptyLoadingView = view;
        return this;
    }

    public LoadListBuilderRx<T> emptyRetryView(View view) {
        this.emptyRetryView = view;
        return this;
    }

    public LoadListBuilderRx<T> adapter(CommonRecyclerViewAdapter adapter) {
        this.adapter = adapter;
        return this;
    }


    public LoadListBuilderRx<T> loader(LoadListHelperRx.Loader<T> loader) {
        this.loader = loader;
        return this;
    }

    public LoadListBuilderRx<T> loader(LoadListHelperRx.SingleLoader<T> singleLoader) {
        this.singleLoader = singleLoader;
        return this;
    }
}
