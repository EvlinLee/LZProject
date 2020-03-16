package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.SaveMoneyBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.user.adapter.SaveMoneyAdapter;
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
 * @date :  2019/11/5 11:59
 * Summary: 累计省钱
 */
@Route(path = "/app/SaveMoneyActivity")
public class SaveMoneyActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = SaveMoneyActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private View mLoadingLayout;
    private View mFailRetry;
    private View mActionBarRl;
    private TextView mSaveMoneyTotalTv;
    private TextView mActionBarBack;
    private TextView mActionBarTitle;
    private SaveMoneyAdapter mSaveMoneyAdapter;
    private LoadMoreHelperRx<SaveMoneyBean.DetailsBean, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mSingleScrollListener;
    private boolean hasLoadMore;
    private LinearLayoutManager mLinearLayoutManager;
    private SmartRefreshLayout mSmartRefreshLayout;
    private LinearLayout linear_save;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_money_layout);
        initImmersionBar(Color.WHITE,true);
        initView();
    }

    private void initView() {
        linear_save = findViewById(R.id.linear_save);
        mActionBarRl = findViewById(R.id.actionbar_rl);
        mActionBarBack = findViewById(R.id.actionbar_back_tv);
        mActionBarTitle = findViewById(R.id.actionbar_title_tv);
        mSaveMoneyTotalTv = findViewById(R.id.save_money_total_tv);
        mRecyclerView = findViewById(R.id.save_money_recyclerView);
        mEmptyView = findViewById(R.id.save_money_empty_tv);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mFailRetry = findViewById(R.id.fail_retry);
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mActionBarRl.setBackgroundColor(Color.WHITE);
        mActionBarTitle.setText(R.string.save_money_title_text);
        mActionBarBack.setText("");
        mSaveMoneyTotalTv.setText("0.00");
        mActionBarBack.setOnClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSaveMoneyAdapter = new SaveMoneyAdapter(this);

        DividerItemDecoration2 mDividerItemDecoration = new DividerItemDecoration2(this, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(this, 8));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F2F2F5));
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mRecyclerView.setAdapter(mSaveMoneyAdapter);
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
        mLoadMoreHelper = new LoadMoreBuilderRx<SaveMoneyBean.DetailsBean, Integer>(this)
                .adapter(mSaveMoneyAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyView)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 20))
                .loader(new LoadMoreHelperRx.Loader<SaveMoneyBean.DetailsBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<SaveMoneyBean.DetailsBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load seu");

                        return ApiService.getIncomeApi().requestSaveMoneyList(0, integer, pageSize)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map((Function<BaseBean<SaveMoneyBean>, Collection<SaveMoneyBean.DetailsBean>>) saveMoneyBeanBaseBean -> {
                                    mSmartRefreshLayout.finishRefresh();
                                    if (!isDestroy() && saveMoneyBeanBaseBean.success()) {
                                        SaveMoneyBean result = saveMoneyBeanBaseBean.getResult();
                                        List<SaveMoneyBean.DetailsBean> details = result.getDetails();
                                        hasLoadMore = !ArraysUtils.isListEmpty(details) && details.size() == pageSize;
                                        if (result.getAllSave()==0){
                                            linear_save.setVisibility(View.GONE);
                                        }else{
                                            linear_save.setVisibility(View.VISIBLE);
                                        }
                                        mSaveMoneyTotalTv.setText(StringUtils.getFormattedDouble(result.getAllSave()));
                                        return result.getDetails();
                                    }else{
                                        if (integer<=1){
                                            linear_save.setVisibility(View.GONE);
                                        }
                                    }
                                    throw new Exception(saveMoneyBeanBaseBean.msg);
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
