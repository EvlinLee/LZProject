package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.by.lizhiyoupin.app.component_ui.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/2 11:53
 * Summary: 头+other
 */
public abstract class BaseOfDelegateAdapter extends DelegateAdapter.Adapter<BaseOfViewHolder> {
    public static final int TYPE_NORMAL = 900;  //说明是不带有header和footer的
    public static final int TYPE_HEADER = 901;  //说明是带有Header的
    public static final int TYPE_FOOTER = 902;  //说明是带有Footer的

    public final Context mContext;
    protected final LayoutInflater mInflater;

    protected List mListData = new ArrayList();

    protected View mHeaderView;
    protected View mFooterView;
    private LayoutHelper mLayoutHelper;
    private RecyclerView mRecyclerView;
    private OnFloatingViewStateChangedListener mFloatingListener;
    private int normalViewType = -1;
    private OnItemClickListener mOnItemClickListener;


    public BaseOfDelegateAdapter(Context context, LayoutHelper layoutHelper, int normalViewType) {
        mContext = context;
        mLayoutHelper = layoutHelper;
        this.normalViewType = normalViewType;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setFloatingViewCallback(final RecyclerView recyclerView, final int floatingViewType, final int floatingViewHeight, OnFloatingViewStateChangedListener listener) {
        mRecyclerView = recyclerView;
        mFloatingListener = listener;

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                if (getItemCount() == 0) {
                if (getNormalItemCount() == 0) {
                    if (mFloatingListener != null) {
                        mFloatingListener.onFloatingViewStateChanged(0, null);
                    }
                    return;
                }

                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == 0 && mHeaderView != null) {
                    return;
                }

                int realPosition = getRealPosition(firstVisibleItemPosition);
                int firstViewType = getNormalViewType(realPosition);
                int translationY = 0;
                Object itemData = null;

                if (firstViewType != floatingViewType) {
                    int secondViewType = getNormalViewType(realPosition + 1);

                    int i = realPosition;
                    while (getNormalViewType(i) != floatingViewType) {
                        i--; //向上寻找到应该悬浮显示的item位置
                    }

                    if (secondViewType != floatingViewType) {
                        translationY = 0;
                    } else {
                        View secondChild = mRecyclerView.getChildAt(1);
                        int secondTop = secondChild.getTop();
//                        DtLog.d(TAG, "onScrolled: secondTop = " + secondTop + ", floatingHeight = " + floatingViewHeight);
                        translationY = floatingViewHeight < secondTop ? 0 : -floatingViewHeight + secondTop;
                    }

                    itemData = getItemData(i);
                } else {
                    translationY = 0;
                    itemData = getItemData(realPosition);
                }

                if (mFloatingListener != null) {
                    mFloatingListener.onFloatingViewStateChanged(translationY, itemData);
                }
            }
        });
    }


    public interface OnFloatingViewStateChangedListener {
        void onFloatingViewStateChanged(int translationY, Object itemData);
    }

    public interface OnItemClickListener {
        void itemClicked(View view, Object object, int position);
    }

    /**
     * 重置
     * @param listData
     */
    public void setListData(List listData) {
        mListData.clear();
        if (listData != null) {
            mListData.addAll(listData);
        }
    }

    /**
     * 追加
     * @param listData
     */
    public void appendListData(List listData){
        if (listData != null) {
            mListData.addAll(listData);
        }
    }


    public List getListData() {
        return mListData;
    }

    public void clearData() {
        mListData.clear();
    }

    public Object getItemData(final int position) {
        final int size = mListData == null ? 0 : mListData.size();
        return position < size && position >= 0 ? mListData.get(position) : null;
    }

    public int inedxOfList(Object object) {
        return mListData != null ? mListData.indexOf(object) : 0;
    }

    public boolean hasHeader() {
        return getHeaderView() != null;
    }

    public final View getHeaderView() {
        return mHeaderView;
    }

    public final void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public final void removeHeaderView() {
        if (mHeaderView == null) {
            return;
        }
        mHeaderView = null;
        notifyItemRemoved(0);
    }

    public boolean hasFooter() {
        return getFooterView() != null;
    }

    public final View getFooterView() {
        return mFooterView;
    }

    public final void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public final void removeFooterView() {
        if (mFooterView == null) {
            return;
        }
        mFooterView = null;
        notifyItemRemoved(getItemCount() - 1);
    }

    @Override
    public final BaseOfViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new BaseOfViewHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new BaseOfViewHolder(mFooterView);
        }

        return createNormalViewHolder(parent, viewType);
    }

    protected abstract BaseOfViewHolder createNormalViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(BaseOfViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER || viewType == TYPE_FOOTER) {
            return;
        }

        final int realPosition = getRealPosition(position);
        holder.mRealPosition = realPosition;

        bindNormalViewHolder(holder, getItemData(realPosition), viewType);

        holder.itemView.setTag(R.id.recycler_list_item_position, realPosition);
        holder.itemView.setTag(holder);
    }

    protected int getRealPosition(int position) {
        return mHeaderView != null ? position - 1 : position;
    }

    public void bindNormalViewHolder(BaseOfViewHolder holder, Object itemData, int viewType) {
        holder.bindData(itemData,viewType);
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return getNormalItemCount();
        } else if (mHeaderView == null || mFooterView == null) {
            return getNormalItemCount() + 1;
        } else {
            return getNormalItemCount() + 2;
        }
    }


    public final int getNormalItemCount() {
        return mListData.size();
    }

    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    *
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1 && mFooterView != null) {
            return TYPE_FOOTER;
        }

        final int realPosition = getRealPosition(position);
        return getNormalViewType(realPosition);
    }

    protected int getNormalViewType(int position) {
        if (normalViewType != -1) {
            return normalViewType;
        }
        return TYPE_NORMAL;
    }

    /**
     * @param position  改变的item位置
     * @param itemCount 已经变更的item的数量（包括自己，即未变更前的size-position）
     */
    public void notifyNormalItemRemoved(int position, int itemCount) {
        if (mHeaderView != null) {
            notifyItemRemoved(position + 1);
        } else {
            notifyItemRemoved(position);
        }
        //notifyItemRemoved后需要notifyItemRangeChanged来更正position
        notifyItemRangeChanged(position, itemCount);
    }

    public void notifyNormalItemChanged(int position) {
        if (mHeaderView != null) {
            notifyItemChanged(position + 1);
        } else {
            notifyItemChanged(position);
        }
    }

    public void notifyNormalItemRangeInserted(int start, int count) {
        if (mHeaderView != null) {
            notifyItemRangeInserted(start + 1, count);
        } else {
            notifyItemRangeInserted(start, count);
        }
    }

    public void notifyNormalItemInserted(int start) {
        if (mHeaderView != null) {
            notifyItemInserted(start + 1);
        } else {
            notifyItemInserted(start);
        }
    }
}
