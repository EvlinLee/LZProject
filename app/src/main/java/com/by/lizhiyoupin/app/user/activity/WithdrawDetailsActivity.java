package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.PresentationDetailsBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.user.adapter.WithdrawDetailsAdapter;
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
import io.reactivex.functions.Function;

/*
 * jyx
 * 提现明细页面
 * */
@Route(path = "/app/WithdrawDetailsActivity")
public class WithdrawDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTitle, actionbar_back_tv;
    private WithdrawDetailsAdapter adapater;
    private LoadMoreHelperRx<PresentationDetailsBean, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView withdraw_detail_recyclerView;
    private TextView empty_tv;
    private boolean hasLoadMore;
    private View mFailRetry;
    private View mloadingLayout;
    private SmartRefreshLayout mSmartRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_details);
        initImmersionBar(Color.WHITE,true);
        initBar();

        initView();
    }

    private void initView() {
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mFailRetry = findViewById(R.id.fail_retry);
        mloadingLayout = findViewById(R.id.loading_layout);
        actionbar_back_tv.setOnClickListener(this);
        withdraw_detail_recyclerView = findViewById(R.id.withdraw_detail_recyclerView);//条目列表
        empty_tv = findViewById(R.id.empty_tv);//无数据
        DividerItemDecoration2 mDividerItemDecoration = new DividerItemDecoration2(this,
                DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(this, 8));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F2F2F5));
        withdraw_detail_recyclerView.addItemDecoration(mDividerItemDecoration);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        withdraw_detail_recyclerView.setLayoutManager(mLinearLayoutManager);
        adapater = new WithdrawDetailsAdapter(this);
        withdraw_detail_recyclerView.setAdapter(adapater);
        loadRecyclerView();
        setSmartRefreshLayout();
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
    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<PresentationDetailsBean, Integer>(this)
                .adapter(adapater)
                .recyclerView(withdraw_detail_recyclerView)
                .emptyView(empty_tv)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<PresentationDetailsBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<PresentationDetailsBean>> load(Integer integer, int pageSize) {
                        return ApiService.getWithdrawApi().requestPresentationDetails(integer,
                                pageSize).map(new Function<BaseBean<List<PresentationDetailsBean>>, Collection<PresentationDetailsBean>>() {
                            @Override
                            public Collection<PresentationDetailsBean> apply(BaseBean<List<PresentationDetailsBean>> listBaseBean) throws Exception {
                                mSmartRefreshLayout.finishRefresh();
                                if (listBaseBean.success() && listBaseBean.data != null) {
                                    List<PresentationDetailsBean> list =
                                            listBaseBean.data;
                                    boolean listEmpty = ArraysUtils.isListEmpty(list);
                                    hasLoadMore = !listEmpty && list.size() == pageSize;
                                    return list;
                                }
                                throw new Exception(listBaseBean.msg);
                            }
                        });
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

        withdraw_detail_recyclerView.addOnScrollListener(mScrollListener);
        mLoadMoreHelper.loadData();
    }

    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);

        actionbar_back_tv.setText("");
        mTitle.setText("提现明细");

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
