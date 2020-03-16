package com.by.lizhiyoupin.app.loader.facade;

import android.content.Context;

import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

public class DefaultAdapterLoadProgressView<T> extends DefaultLoadProgressView<T> {
    private CommonRecyclerViewAdapter adapter;

    public DefaultAdapterLoadProgressView(Context context,CommonRecyclerViewAdapter adapter, SmartRefreshLayout refreshLayout) {
        super(context, refreshLayout);
        this.adapter = adapter;
    }

    @Override
    public void showData(List<T> data, int changeType, int start, int count, boolean hasMore) {
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
}
