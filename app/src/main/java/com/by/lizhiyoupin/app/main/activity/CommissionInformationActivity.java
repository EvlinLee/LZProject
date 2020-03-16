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
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.InformationBean;
import com.by.lizhiyoupin.app.io.bean.UserMessageVO;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.adapter.CommissionAdapter;
import com.by.lizhiyoupin.app.main.contract.MessageContract;
import com.by.lizhiyoupin.app.main.presenter.MessagePresenter;
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
* 佣金消息
* jyx:
*
* */
@Route(path = "/app/CommissionInformationActivity")
public class CommissionInformationActivity extends BaseMVPActivity<MessageContract.MessageView, MessageContract.MessagePresenters> implements  MessageContract.MessageView,View.OnClickListener {
    private TextView mTitle,actionbar_back_tv,actionbar_right_tv,empty_tv,general_commisson,preferred_commisson;
    private RecyclerView commission_rcy,commission_rcy2;
    private LoadMoreHelperRx<UserMessageVO, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private LinearLayoutManager mLinearLayoutManager,mLinearLayoutManager2;
    private CommissionAdapter adapater;
    private boolean hasLoadMore;
    private View mFailRetry;
    private View mloadingLayout;
    private SmartRefreshLayout mSmartRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_information);
        initImmersionBar(Color.WHITE,true);
        initBar();
        initView();
    }

    @Override
    public MessageContract.MessagePresenters getBasePresenter() {
        return new MessagePresenter(this);
    }

    private void initView() {
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        empty_tv = findViewById(R.id.empty_tv);
        mFailRetry =  findViewById(R.id.fail_retry);
        mloadingLayout =  findViewById(R.id.loading_layout);
        actionbar_back_tv.setOnClickListener(this);
        preferred_commisson = findViewById(R.id.preferred_commisson);//优选佣金
        preferred_commisson.setOnClickListener(this);
        general_commisson = findViewById(R.id.general_commisson);//普通佣金
        general_commisson.setOnClickListener(this);
        commission_rcy = findViewById(R.id.commission_rcy);//条目列表
        commission_rcy2=findViewById(R.id.commission_rcy2);

        DividerItemDecoration2 mDividerItemDecoration = new DividerItemDecoration2(this, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(this, 10));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F2F2F5));
        commission_rcy.addItemDecoration(mDividerItemDecoration);
        commission_rcy2.addItemDecoration(mDividerItemDecoration);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mLinearLayoutManager2 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        commission_rcy.setLayoutManager(mLinearLayoutManager);
        commission_rcy2.setLayoutManager(mLinearLayoutManager2);
        adapater = new CommissionAdapter(this);
        commission_rcy.setAdapter(adapater);
        commission_rcy2.setAdapter(adapater);
        loadRecyclerView(2);
        setSmartRefreshLayout();
        reupdateStatus();
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
    private void loadRecyclerView2(int type) {
        mLoadMoreHelper = new LoadMoreBuilderRx<UserMessageVO, Integer>(this)
                .adapter(adapater)
                .recyclerView(commission_rcy2)
                .emptyView(empty_tv)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1,10))
                .loader(new LoadMoreHelperRx.Loader<UserMessageVO, Integer>() {
                    @Override
                    public Observable<? extends Collection<UserMessageVO>> load(Integer integer,
                                                                                int pageSize) {
                        return ApiService.getMessageApi().requestUserMessage(type, integer, pageSize).map(new Function<BaseBean<List<UserMessageVO>>, Collection<UserMessageVO>>() {
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
                if (hasLoadMore==true){
                    mLoadMoreHelper.loadDataMore();
                }

            }
        };

        commission_rcy2.addOnScrollListener(mScrollListener);
        mLoadMoreHelper.loadData();
    }


    private void loadRecyclerView(int type) {
        mLoadMoreHelper = new LoadMoreBuilderRx<UserMessageVO, Integer>(this)
                .adapter(adapater)
                .recyclerView(commission_rcy)
                .emptyView(empty_tv)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1,10))
                .loader(new LoadMoreHelperRx.Loader<UserMessageVO, Integer>() {
                    @Override
                    public Observable<? extends Collection<UserMessageVO>> load(Integer integer,
                                                                                int pageSize) {
                        return ApiService.getMessageApi().requestUserMessage(type, integer, pageSize).map(new Function<BaseBean<List<UserMessageVO>>, Collection<UserMessageVO>>() {
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
                if (hasLoadMore==true){
                    mLoadMoreHelper.loadDataMore();
                }

            }
        };

        commission_rcy.addOnScrollListener(mScrollListener);
        mLoadMoreHelper.loadData();
    }
    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv= findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setText("");
        mTitle.setText("佣金消息");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.preferred_commisson:
                preferred_commisson.setBackgroundResource(R.drawable.commison_item_radius_5dp);
                general_commisson.setBackgroundResource(R.drawable.shap_none);
                preferred_commisson.setTextColor(Color.parseColor("#FFFF005E"));
                general_commisson.setTextColor(Color.parseColor("#FF111111"));
                commission_rcy.setVisibility(View.GONE);
                commission_rcy2.setVisibility(View.VISIBLE);
                loadRecyclerView2(3);
                break;
            case R.id.general_commisson:
                general_commisson.setBackgroundResource(R.drawable.commison_item_radius_5dp);
                preferred_commisson.setBackgroundResource(R.drawable.shap_none);
                general_commisson.setTextColor(Color.parseColor("#FFFF005E"));
                preferred_commisson.setTextColor(Color.parseColor("#FF111111"));
                commission_rcy.setVisibility(View.VISIBLE);
                commission_rcy2.setVisibility(View.GONE);
                loadRecyclerView(2);
                break;
        }
    }
    private void reupdateStatus() {
        SettingRequestManager.requestMessageStatus("2", "0")
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
        SettingRequestManager.requestMessageStatus("3", "0")
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
    @Override
    public void requestMessageSuccess(List<UserMessageVO> list) {

    }

    @Override
    public void requestMessageError(Throwable throwable) {

    }

    @Override
    public void requestMainMessageSuccess(List<InformationBean> list) {

    }

    @Override
    public void requestMainMessageError(Throwable throwable) {

    }
}
