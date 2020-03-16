package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;

import androidx.annotation.NonNull;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/2 10:29
 * Summary:
 */
public class BaseDelegateAdapter extends DelegateAdapter.Adapter<BaseOfViewHolder> {
    public static final int TYPE_HEADER = 901;  //说明是带有Header的
    private LayoutHelper mLayoutHelper;
    private int mLayoutId;
    private Context mContext;
    private int mItemViewType;
    private int count ;
    private View mHeaderView;

    public BaseDelegateAdapter(Context context, LayoutHelper layoutHelper, int count,int layoutId, int itemViewType) {
        this.mContext = context;
        this.mLayoutHelper = layoutHelper;
        this.mLayoutId = layoutId;
        this.mItemViewType = itemViewType;
        this.count = count;
    }

    public void setCount(int count){
        this.count = count;
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public BaseOfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         if (viewType==TYPE_HEADER){
             return new BaseOfViewHolder(mHeaderView);
         }
        if (viewType == mItemViewType) {
            return new BaseOfViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }
        return null;
    }

    @Override
    public final void onBindViewHolder(@NonNull BaseOfViewHolder holder, int position) {

    }


    /**
     * 必须重写不然会出现滑动不流畅的情况
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null) {
            return TYPE_HEADER;
        }
        return mItemViewType;
    }


    /**
     * 条目数量
     *
     * @return 条目数量
     */
    @Override
    public int getItemCount() {
        return count;
    }
}
