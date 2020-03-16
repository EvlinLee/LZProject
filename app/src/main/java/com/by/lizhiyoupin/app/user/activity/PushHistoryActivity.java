package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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
import com.by.lizhiyoupin.app.io.bean.PushGoodsBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.user.adapter.PushHistoryAdapter;
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
 * 推送历史页面
 * jyx
 * */
@Route(path = "/app/PushHistoryActivity")
public class PushHistoryActivity extends BaseActivity implements View.OnClickListener {
    private TextView mActionBarBack;
    private TextView mActionBarTitle, actionbar_right_tv;
    private View mActionBarRl;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private View mLoadingLayout;
    private View mFailRetry;
    private SmartRefreshLayout mSmartRefreshLayout;
    private PushHistoryAdapter mShoppingCartAdapter;
    private LoadMoreHelperRx<PushGoodsBean, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mSingleScrollListener;
    private boolean hasLoadMore;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_history);
        initImmersionBar(Color.WHITE, true);
        initBar();
        initView();
    }

    private void initView() {
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty_tv);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mFailRetry = findViewById(R.id.fail_retry);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mShoppingCartAdapter = new PushHistoryAdapter(this);

        DividerItemDecoration2 mDividerItemDecoration = new DividerItemDecoration2(this,
                DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(this, 8));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F2F2F5));
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mRecyclerView.setAdapter(mShoppingCartAdapter);
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
        mLoadMoreHelper = new LoadMoreBuilderRx<PushGoodsBean, Integer>(this)
                .adapter(mShoppingCartAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyView)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 20))
                .loader(new LoadMoreHelperRx.Loader<PushGoodsBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<PushGoodsBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "requestGetPushGoodsList==" + integer);

                        return ApiService.getPushEditApi().requestGetPushGoodsList(integer, pageSize)
                                .map(new Function<BaseBean<List<PushGoodsBean>>, Collection<PushGoodsBean>>() {
                                    @Override
                                    public Collection<PushGoodsBean> apply(BaseBean<List<PushGoodsBean>> baseBean) throws Exception {
                                        mSmartRefreshLayout.finishRefresh();
                                        if (baseBean.success() && baseBean.data != null) {
                                            List<PushGoodsBean> list = baseBean.data;
                                            boolean listEmpty = ArraysUtils.isListEmpty(list);
                                            hasLoadMore = !listEmpty && list.size() == pageSize;
                                            return list;
                                        }
                                        throw new Exception(baseBean.msg);

                                    }
                                });
                    }
                }).build();

        mSingleScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    mLoadMoreHelper.loadDataMore();
                }
            }
        };

        mRecyclerView.addOnScrollListener(mSingleScrollListener);
        mLoadMoreHelper.loadData();
        mShoppingCartAdapter.setHelperRx(mLoadMoreHelper);
    }

    private void initBar() {
        mActionBarRl = findViewById(R.id.actionbar_rl);
        mActionBarBack = findViewById(R.id.actionbar_back_tv);
        mActionBarTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_right_tv = findViewById(R.id.actionbar_right_tv);
        mActionBarRl.setBackgroundColor(Color.WHITE);
        mActionBarTitle.setText("推送列表");
        mActionBarBack.setText("");
        mActionBarBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            default:
                break;
        }
    }
}
