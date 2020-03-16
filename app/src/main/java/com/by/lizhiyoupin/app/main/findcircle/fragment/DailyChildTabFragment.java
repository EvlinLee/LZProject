package com.by.lizhiyoupin.app.main.findcircle.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FindCircleChildListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.findcircle.adapter.DailyChildAdapter;
import com.dueeeke.videoplayer.player.VideoView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 16:01
 * Summary: 发圈---2级tab下  二级子 如时拍，猫超
 */
public class DailyChildTabFragment extends BaseFragment {
    public static final String TAG = DailyChildTabFragment.class.getSimpleName();
    private Context mContext;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTv;
    private View mLoadingLayout;
    private View mFailRetry;
    private SmartRefreshLayout mSmartRefreshLayout;
    private long ringFirstKindId;
    private long ringSecondKindId;
    private LinearLayoutManager mLinearLayoutManager;
    private DailyChildAdapter mChildAdapter;

    private LoadMoreHelperRx<FindCircleChildListBean, Integer> mLoadMoreHelper;
    private boolean hasLoadMore;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_daily_child_tab_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            ringFirstKindId = arguments.getLong(CommonConst.KEY_FIND_CIRCLE_FIRST_TAB_ID, 0);
            ringSecondKindId = arguments.getLong(CommonConst.KEY_FIND_CIRCLE_SECOND_TAB_ID, 0);
        }

        initView(inflate);
        return inflate;
    }

    private void initView(View root) {
        mSmartRefreshLayout = root.findViewById(R.id.smartRefreshLayout);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mEmptyTv = root.findViewById(R.id.empty_tv);
        mFailRetry = root.findViewById(R.id.fail_retry);
        mLoadingLayout = root.findViewById(R.id.loading_layout);

        mLinearLayoutManager = new LinearLayoutManager(mContext);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(mContext, RecyclerView.VERTICAL );
        dividerItemDecoration2.setDividerHeight(DeviceUtil.dip2px(mContext, 8));
        dividerItemDecoration2.setDividerColor(Color.TRANSPARENT);
        mRecyclerView.addItemDecoration(dividerItemDecoration2);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mChildAdapter = new DailyChildAdapter(mContext,getChildFragmentManager());
        View view = new View(mContext);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        mChildAdapter.setHeaderView(view);
        mRecyclerView.setAdapter(mChildAdapter);
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                //当添加子View时回调
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                //当移除子View时回调
                VideoView videoView = view.findViewById(R.id.videoView);
                if (videoView != null && !videoView.isFullScreen()) {
                    videoView.release();
                }
            }
        });
        loadRecyclerView();
        setSmartRefreshLayout();
    }
    private void setSmartRefreshLayout(){
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mLoadMoreHelper!=null){
                    mLoadMoreHelper.loadData();
                }else {
                    mSmartRefreshLayout.finishRefresh(1000);
                }

            }
        });
        mSmartRefreshLayout.setEnableAutoLoadMore(false);
        mSmartRefreshLayout.setEnableLoadMore(false);

    }
    private void refreshData() {
        if (mLoadMoreHelper != null) {
            mLoadMoreHelper.loadData();
        }
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        LZLog.i(TAG, "onFirstUserVisible");
        refreshData();
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<FindCircleChildListBean, Integer>(mContext)
                .adapter(mChildAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyTv)
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .refreshLayout(mSmartRefreshLayout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 4))
                .loader(new LoadMoreHelperRx.Loader<FindCircleChildListBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<FindCircleChildListBean>> load(Integer integer, int pageSize) {
                        Log.i(TAG, "requestTabChildList: " + integer);

                        return ApiService.getFindCircleApi().requestTabChildList(ringFirstKindId, ringSecondKindId, integer, pageSize)
                                .map(new Function<BaseBean<List<FindCircleChildListBean>>, Collection<FindCircleChildListBean>>() {
                                    @Override
                                    public Collection<FindCircleChildListBean> apply(BaseBean<List<FindCircleChildListBean>> listBaseBean) throws Exception {
                                        Log.i(TAG, "requestTabChildList: apply ");
                                        if (listBaseBean.success()) {
                                            List<FindCircleChildListBean> result = listBaseBean.getResult();
                                            hasLoadMore = !ArraysUtils.isListEmpty(result) && result.size() == pageSize;
                                            return result;
                                        }

                                        throw new Exception(listBaseBean.code);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<FindCircleChildListBean> data, int pageCount) {
                        return hasLoadMore;
                    }

                }).build();

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                LZLog.i(TAG, "requestTabChildList onLoadMore" + currentPage);
                if (hasLoadMore) {
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });

    }

}
