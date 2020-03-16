package com.by.lizhiyoupin.app.user.fragment;

import android.content.Context;
import android.os.Bundle;
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
import com.by.lizhiyoupin.app.io.bean.IncomeDetailsVO;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.user.adapter.OrderQueryAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/12 15:36
 * Summary:
 */
public class OrderQueryFragment extends BaseFragment {
    public static final String TAG = OrderQueryFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mEmptyTv;
    private View mLoadingLayout;
    private View mFailRetry;
    private LoadMoreHelperRx<IncomeDetailsVO, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private LinearLayoutManager mLinearLayoutManager;
    private OrderQueryAdapter mIncomeDetailAdapter;
    private boolean hasLoadMore;

    private Context mContext;
    private int mSortType = CommonConst.E_UPLIMIT_SORT_DOWN;
    private int mOrderType=1; //必选  订单类型 1我的订单 2团队订单
    private int mPlatformType=1;// 平台类型  1淘宝 2京东 3拼多多
    private int mOrderStatus = 0;  //订单状态 0全部 1待返佣 2已到账 3失效
    private String mOrderId="";//订单编号
    private SmartRefreshLayout mSmartRefreshLayout;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_order_query_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mOrderStatus = arguments.getInt(CommonConst.KEY_ORDER_SEARCH_TYPE, 0);
            mOrderType = arguments.getInt(CommonConst.KEY_ORDER_TYPE, 1);
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

        DividerItemDecoration2 mDividerItemDecoration = new DividerItemDecoration2(mContext, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(mContext, 8));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F2F2F5));
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIncomeDetailAdapter = new OrderQueryAdapter(mContext);
        mRecyclerView.setAdapter(mIncomeDetailAdapter);
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
    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<IncomeDetailsVO, Integer>(mContext)
                .adapter(mIncomeDetailAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyTv)
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<IncomeDetailsVO, Integer>() {
                    @Override
                    public Observable<? extends Collection<IncomeDetailsVO>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load requestGetMyOrderList");

                        return ApiService.getOrderApi().requestGetMyOrderList(mSortType, integer, pageSize, mPlatformType, mOrderStatus, mOrderType, mOrderId)
                                .observeOn(AndroidSchedulers.mainThread())//指定下面map在主线程
                                .map(new Function<BaseBean<List<IncomeDetailsVO>>, Collection<IncomeDetailsVO>>() {
                                    @Override
                                    public Collection<IncomeDetailsVO> apply(BaseBean<List<IncomeDetailsVO>> listBaseBean) throws Exception {
                                        mSmartRefreshLayout.finishRefresh();
                                        dismissLoadingDialog();
                                        if (listBaseBean.success()) {
                                            mIncomeDetailAdapter.setOrderType(mOrderType);
                                            List<IncomeDetailsVO> list = listBaseBean.data;
                                            boolean listEmpty = ArraysUtils.isListEmpty(list);
                                            hasLoadMore = !listEmpty && list.size() == pageSize;
                                            return list;
                                        }
                                        throw new Exception(listBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<IncomeDetailsVO> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();

        mScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    mLoadMoreHelper.loadDataMore();
                }
            }
        };

        mRecyclerView.addOnScrollListener(mScrollListener);
        mLoadMoreHelper.loadData();
    }

    /**
     * @param orderType    必选  订单类型 1我的订单 2团队订单
     * @param platformType 平台类型  1淘宝 2京东 3拼多多
     * @param orderId      订单编号
     */
    public void setRequestParams(int orderType, int platformType,  String orderId) {
        mOrderType = orderType;
        mPlatformType = platformType;
        mOrderId = orderId;
        if (mLoadMoreHelper!=null){
            mLoadMoreHelper.loadData();
        }
    }

}
