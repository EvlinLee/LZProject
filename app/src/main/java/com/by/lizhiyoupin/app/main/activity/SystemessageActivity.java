package com.by.lizhiyoupin.app.main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserMessageVO;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.adapter.SystemMessageAdapter;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
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
import io.reactivex.schedulers.Schedulers;

/*
 * 系统消息
 * jyx
 * */
@Route(path = "/app/SystemessageActivity")
public class SystemessageActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTitle, actionbar_back_tv, actionbar_right_tv, empty_tv;
    private RecyclerView system_rcy;
    private LoadMoreHelperRx<UserMessageVO, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private LinearLayoutManager mLinearLayoutManager;
    private SystemMessageAdapter adapater;
    private boolean hasLoadMore;
    private View mFailRetry;
    private View mloadingLayout;
    private SmartRefreshLayout mSmartRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemessage);
        initImmersionBar(Color.WHITE,true);
        initBar();
        initView();
    }

    private void initView() {
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mFailRetry =  findViewById(R.id.fail_retry);
        mloadingLayout =  findViewById(R.id.loading_layout);
        actionbar_back_tv.setOnClickListener(this);
        system_rcy = findViewById(R.id.system_rcy);//条目列表
        empty_tv = findViewById(R.id.empty_tv);//无数据
        DividerItemDecoration2 mDividerItemDecoration = new DividerItemDecoration2(this,
                DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(this, 10));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F2F2F5));
        system_rcy.addItemDecoration(mDividerItemDecoration);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        system_rcy.setLayoutManager(mLinearLayoutManager);
        adapater = new SystemMessageAdapter(this);
        system_rcy.setAdapter(adapater);
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
        mLoadMoreHelper = new LoadMoreBuilderRx<UserMessageVO, Integer>(this)
                .adapter(adapater)
                .recyclerView(system_rcy)
                .emptyView(empty_tv)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<UserMessageVO, Integer>() {
                    @Override
                    public Observable<? extends Collection<UserMessageVO>> load(Integer integer,
                                                                                int pageSize) {


                        return ApiService.getMessageApi().requestUserMessage(1, integer, pageSize).map(new Function<BaseBean<List<UserMessageVO>>, Collection<UserMessageVO>>() {
                            @Override
                            public Collection<UserMessageVO> apply(BaseBean<List<UserMessageVO>> listBaseBean) throws Exception {
                                mSmartRefreshLayout.finishRefresh();
                                if (listBaseBean.success() && listBaseBean.data != null) {
                                    List<UserMessageVO> list =
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
                if (hasLoadMore == true) {
                    mLoadMoreHelper.loadDataMore();
                }

            }
        };

        system_rcy.addOnScrollListener(mScrollListener);
        mLoadMoreHelper.loadData();
    }

    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setText("");
        actionbar_right_tv = findViewById(R.id.actionbar_right_tv);
        actionbar_right_tv.setText("全部已读");
        actionbar_right_tv.setOnClickListener(this);
        mTitle.setText("系统消息");
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.actionbar_right_tv:
                reupdateStatus();
                break;
        }
    }

    private void reupdateStatus() {
        SettingRequestManager.requestMessageStatus("1", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<String>>() {
                    @Override
                    public void onNext(BaseBean<String> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            return;
                        }
                        mLoadMoreHelper.loadData();

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
}
