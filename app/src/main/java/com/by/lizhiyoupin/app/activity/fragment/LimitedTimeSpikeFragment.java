package com.by.lizhiyoupin.app.activity.fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.activity.activity.LimitedTimeSpikeActivity;
import com.by.lizhiyoupin.app.activity.adapter.LimitedTimeSpikeAdapter;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.CountTimeView;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ProduclimitSkilltListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 21:36
 * Summary: 限时秒杀
 */
public class LimitedTimeSpikeFragment extends BaseFragment {
    public static final String TAG = LimitedTimeSpikeFragment.class.getSimpleName();
    private Context mContext;
    private View mLastTimeCl;
    private CountTimeView mCountTimeView;
    private SmartRefreshLayout mSmartRefreshLayout;
    private View mEmptyLayout;
    private View mFailRetry;
    private View mloadingLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private LimitedTimeSpikeAdapter mTimeAdapter;
    private LoadMoreHelperRx<ProduclimitSkilltListBean, Integer> mLoadMoreHelper;
    private boolean hasLoadMore;
    private int mTimeType = 0;
    private boolean showUpdataFlag=false;
    private DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            //倒计时结束
            FragmentActivity activity = getActivity();
            if (activity instanceof LimitedTimeSpikeActivity) {
                ((LimitedTimeSpikeActivity) activity).updateMagicIndicator();
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_limited_time_spike_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTimeType = arguments.getInt(CommonConst.KEY_INDICATOR_TYPE, 0);
        }
        initView(inflate);
        return inflate;
    }

    private void initView(View root) {
        mSmartRefreshLayout = root.findViewById(R.id.smartRefreshLayout);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mLastTimeCl = root.findViewById(R.id.last_time_cl);
        mCountTimeView = root.findViewById(R.id.count_time_view);
        mEmptyLayout = root.findViewById(R.id.empty_layout);
        mFailRetry = root.findViewById(R.id.fail_retry);
        mloadingLayout = root.findViewById(R.id.loading_layout);
        mCountTimeView.registerDataSetObserver(mObserver);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mTimeAdapter = new LimitedTimeSpikeAdapter(mContext, getChildFragmentManager(), mCountTimeView);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(mContext,
                RecyclerView.VERTICAL);
        dividerItemDecoration2.setDividerHeight(DeviceUtil.dip2px(mContext,10));
        dividerItemDecoration2.setDividerColor(Color.TRANSPARENT);
        mRecyclerView.addItemDecoration(dividerItemDecoration2);
        mRecyclerView.setAdapter(mTimeAdapter);
        setSmartRefreshLayout();
        loadRecyclerView();
    }

    private void setSmartRefreshLayout() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mLoadMoreHelper != null) {
                    mLoadMoreHelper.loadData();
                } else {
                    mSmartRefreshLayout.finishRefresh(1000);
                }

            }
        });
        mSmartRefreshLayout.setEnableAutoLoadMore(false);
        mSmartRefreshLayout.setEnableLoadMore(false);

    }

    public void toUpdataFlag(boolean flag){
        this.showUpdataFlag=flag;
    }
    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mLoadMoreHelper.loadData();
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (showUpdataFlag){
            mLoadMoreHelper.loadData();
        }
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<ProduclimitSkilltListBean, Integer>(mContext)
                .adapter(mTimeAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyLayout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .refreshLayout(mSmartRefreshLayout)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<ProduclimitSkilltListBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<ProduclimitSkilltListBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load requestLimitedTimeList ");
                        return ApiService.getActivityApi().requestLimitedSkillTimeList(mTimeType, integer)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Function<BaseBean<List<ProduclimitSkilltListBean>>, Collection<ProduclimitSkilltListBean>>() {
                                    @Override
                                    public Collection<ProduclimitSkilltListBean> apply(BaseBean<List<ProduclimitSkilltListBean>> listBaseBean) throws Exception {
                                        if (listBaseBean.success() && listBaseBean.data != null) {
                                            List<ProduclimitSkilltListBean> lists =
                                                    listBaseBean.data;
                                                mLastTimeCl.setVisibility(View.GONE);
                                                mTimeAdapter.setGetShowTimes(mTimeType);
                                            hasLoadMore = !ArraysUtils.isListEmpty(lists) && lists.size() == pageSize;
                                            return lists;
                                        }
                                        throw new Exception(new Throwable(listBaseBean.msg));
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<ProduclimitSkilltListBean> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    LZLog.i(TAG, "requestLimitedTimeList onLoadMore" + currentPage);
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCountTimeView.unregisterDataSetObserver(mObserver);
    }
}
