package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract class CommonBaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> implements View.OnClickListener {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mData=new ArrayList<>();
    protected final int mItemLayoutId;
    protected boolean mItemClickable;

    public CommonBaseRecyclerViewAdapter(Context context, List<T> data, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData.clear();
        if (data!=null&&data.size()>0){
            this.mData.addAll(data);
        }
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View convertView = mInflater.inflate(mItemLayoutId, parent, false);
        final CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(convertView, this);
        convertView.setTag(holder);
        if (mItemClickable) {
            convertView.setOnClickListener(this);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract void convert(CommonRecyclerViewHolder holder, T item, int position);

    public void setItemClickable(final boolean clickable) {
        mItemClickable = clickable;
    }

    @Override
    public final void onClick(View view) {
        final CommonRecyclerViewHolder holder = (CommonRecyclerViewHolder) view.getTag();
        onItemClick(view, holder, holder.getLayoutPosition());
    }

    protected void onItemClick(final View v, CommonRecyclerViewHolder holder, final int position) {
    }

    protected void onItemChildClick(final View v, CommonRecyclerViewHolder holder, final int position) {
    }

    public void setData(List<T> data) {
        this.mData.clear();
        if (data!=null&&data.size()>0){
            this.mData.addAll(data);
        }
    }

    public List<T> getData() {
        return mData;
    }
}
