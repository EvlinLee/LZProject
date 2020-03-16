package com.by.lizhiyoupin.app.detail.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.detail.adapter.ShoppingCartAdapter;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ShoppingCartListBean;
import com.by.lizhiyoupin.app.io.bean.ShoppingCartResponse;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collection;

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
 * @date :  2019/11/16 19:28
 * Summary: 购物车
 */

@Route(path = "/app/ShoppingCartActivity")
public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = ShoppingCartActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private View mLoadingLayout;
    private View mFailRetry;
    private View mActionBarRl;
    private SmartRefreshLayout mSmartRefreshLayout;
    private TextView mActionBarBack;
    private TextView mActionBarTitle;
    private ShoppingCartAdapter mShoppingCartAdapter;
    private LoadMoreHelperRx<ShoppingCartListBean, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mSingleScrollListener;
    private boolean hasLoadMore;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DeviceUtil.fullScreen(this, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_layout);
        StatusBarUtils.setColor(this, Color.WHITE, 0);
        initView();
    }

    private void initView() {
        mActionBarRl = findViewById(R.id.actionbar_rl);
        mActionBarBack = findViewById(R.id.actionbar_back_tv);
        mActionBarTitle = findViewById(R.id.actionbar_title_tv);
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty_tv);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mFailRetry = findViewById(R.id.fail_retry);
        mActionBarRl.setBackgroundColor(Color.WHITE);
        mActionBarTitle.setText(R.string.shake_shopping_cart_text);
        mActionBarBack.setText("");
        mActionBarBack.setOnClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mShoppingCartAdapter = new ShoppingCartAdapter(this);


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
        mLoadMoreHelper = new LoadMoreBuilderRx<ShoppingCartListBean, Integer>(this)
                .adapter(mShoppingCartAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyView)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 20))
                .loader(new LoadMoreHelperRx.Loader<ShoppingCartListBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<ShoppingCartListBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load seu" + integer);
                        String apiToken =
                                LiZhiApplication.getApplication().getAccountManager().getUserToken();
                        return ApiService.getShoppingApi().requestGetShoppingCartList(apiToken,
                                integer, pageSize)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Function<BaseBean<ShoppingCartResponse>,
                                        Collection<ShoppingCartListBean>>() {
                                    @Override
                                    public Collection<ShoppingCartListBean> apply(BaseBean<ShoppingCartResponse> baseBean) throws Exception {
                                        mSmartRefreshLayout.finishRefresh();
                                        if (!isDestroy() && baseBean.success() && baseBean.data != null) {
                                            ShoppingCartResponse result = baseBean.getResult();
                                            hasLoadMore = result.getCurrent() < result.getPages();
                                            return result.getRecords();
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
