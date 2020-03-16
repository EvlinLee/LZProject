package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.OrderSupportBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.user.adapter.OrderSupportAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/*
 *jyx
 * 自营订单页面
 * */
@Route(path = "/app/OrderSupportActivity")
public class OrderSupportActivity extends BaseActivity implements View.OnClickListener {
    private TextView actionbar_back_tv;
    private RelativeLayout actionbar_rl;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTv;
    private View mLoadingLayout;
    private View mFailRetry;
    private LoadMoreHelperRx<OrderSupportBean, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private LinearLayoutManager mLinearLayoutManager;
    private OrderSupportAdapter mSupportAdapter;
    private boolean hasLoadMore;
    private SmartRefreshLayout mSmartRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_support);
        initImmersionBar(Color.WHITE, true);
        initView();
    }

    private void initView() {
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        TextView actionbar_title_tv = findViewById(R.id.actionbar_title_tv);
        actionbar_title_tv.setText("自营订单");
        actionbar_back_tv.setText("");
        actionbar_back_tv.setOnClickListener(this);
        actionbar_rl = findViewById(R.id.actionbar_rl);
        actionbar_rl.setBackgroundColor(Color.WHITE);

        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyTv = findViewById(R.id.empty_tv);
        mFailRetry = findViewById(R.id.fail_retry);
        mLoadingLayout = findViewById(R.id.loading_layout);

        DividerItemDecoration2 mDividerItemDecoration = new DividerItemDecoration2(this, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(this, 8));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F2F2F5));
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSupportAdapter = new OrderSupportAdapter(this);
        mRecyclerView.setAdapter(mSupportAdapter);
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
        mLoadMoreHelper = new LoadMoreBuilderRx<OrderSupportBean, Integer>(this)
                .adapter(mSupportAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyTv)
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<OrderSupportBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<OrderSupportBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load requestGetMyOrderList");

                        return ApiService.getOrderApi().requestOrderSupportList(1, integer, pageSize, 10)
                                .observeOn(AndroidSchedulers.mainThread())//指定下面map在主线程
                                .map(new Function<BaseBean<List<OrderSupportBean>>, Collection<OrderSupportBean>>() {
                                    @Override
                                    public Collection<OrderSupportBean> apply(BaseBean<List<OrderSupportBean>> listBaseBean) throws Exception {
                                        mSmartRefreshLayout.finishRefresh();
                                        dismissLoadingDialog();
                                        if (listBaseBean.success()) {
                                            List<OrderSupportBean> list = listBaseBean.data;
                                            boolean listEmpty = ArraysUtils.isListEmpty(list);
                                            hasLoadMore = !listEmpty && list.size() == pageSize;
                                            return list;
                                        }
                                        throw new Exception(listBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<OrderSupportBean> data, int pageCount) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
        }
    }
}
