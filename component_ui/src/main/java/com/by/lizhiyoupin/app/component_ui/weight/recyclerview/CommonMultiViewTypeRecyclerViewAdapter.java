package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommonMultiViewTypeRecyclerViewAdapter<T> extends CommonBaseRecyclerViewAdapter<T> implements View.OnClickListener {
    public static final int ADAPTER_HEADER_TYPE = 920;//头
    public View mHheaderView;

    public CommonMultiViewTypeRecyclerViewAdapter(Context context, List<T> data) {
        super(context, data, 0);
    }

    public void setHeaderView(View headerView) {
        this.mHheaderView = headerView;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ADAPTER_HEADER_TYPE && mHheaderView != null) {
            CommonRecyclerViewHolder header = new CommonRecyclerViewHolder(mHheaderView, this);
            mHheaderView.setTag(header);
            return header;
        }
        final int itemLayoutId = getItemLayoutId(viewType);
        final View convertView = mInflater.inflate(itemLayoutId, parent, false);
        final CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(convertView, this);
        convertView.setTag(holder);
        if (mItemClickable) {
            convertView.setOnClickListener(this);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ADAPTER_HEADER_TYPE ) {
            return;
        }
        convert(holder, getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public T getItem(int position) {
        final List<T> data = mData;
        final int size = data == null ? 0 : data.size();
        return position >= 0 && position < size ? data.get(position) : null;
    }

    protected abstract int getItemLayoutId(int viewType);
}
