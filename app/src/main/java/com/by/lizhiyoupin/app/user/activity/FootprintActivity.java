package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.FootprintBean;
import com.by.lizhiyoupin.app.io.bean.FootprintParentBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.user.adapter.FootprintParentAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/18 09:48
 * Summary: 足迹
 */
@Route(path = "/app/FootprintActivity")
public class FootprintActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = FootprintActivity.class.getSimpleName();
    private TextView mEmptyView;
    private View mLoadingLayout;
    private View mFailRetry;
    private View mActionBarRl;
    private View mDeleteRl;
    private RecyclerView mRecyclerView;
    private LoadMoreHelperRx<FootprintParentBean, Integer> mLoadMoreHelper;
    private SmartRefreshLayout mSmartRefreshLayout;
    private TextView mActionBarBack;
    private TextView mActionBarTitle;
    private TextView mActionBarRight;
    private FootprintParentAdapter mFootprintAdapter;
    private boolean hasLoadMore;
    private LinearLayoutManager mLinearLayoutManager;
    private static final int COMPLETED = 0;
    private static final int COMPRIGHT = 1;

    public static final List<FootprintBean> sSelectList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint_layout);
        initImmersionBar(Color.WHITE,true);
        initView();
    }

    private void initView() {
        sSelectList.clear();
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mActionBarRl = findViewById(R.id.actionbar_rl);
        mActionBarBack = findViewById(R.id.actionbar_back_tv);
        mActionBarTitle = findViewById(R.id.actionbar_title_tv);
        mActionBarRight = findViewById(R.id.actionbar_right_tv);
        mRecyclerView = findViewById(R.id.recyclerView);
        mDeleteRl = findViewById(R.id.delete_rl);
        findViewById(R.id.delete_tv).setOnClickListener(this);


        mEmptyView = findViewById(R.id.empty_layout);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mFailRetry = findViewById(R.id.fail_retry);
        mActionBarRl.setBackgroundColor(Color.WHITE);
        mActionBarTitle.setText(R.string.foot_print_title_text);
        mActionBarBack.setText("");
        mActionBarRight.setText(R.string.foot_print_edit_text);
        mActionBarBack.setOnClickListener(this);
        mActionBarRight.setOnClickListener(this);

        mFootprintAdapter = new FootprintParentAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mFootprintAdapter);
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
        mLoadMoreHelper = new LoadMoreBuilderRx<FootprintParentBean, Integer>(this)
                .adapter(mFootprintAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyView)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mLoadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 15))
                .loader(new LoadMoreHelperRx.Loader<FootprintParentBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<FootprintParentBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load requestGetFootprintList " + integer);
                        String userToken = LiZhiApplication.getApplication().getAccountManager().getUserToken();
                        return ApiService.getNewsApi().requestGetFootprintList(userToken, integer, pageSize)
                                .observeOn(AndroidSchedulers.mainThread())//指定map在主线程
                                .map(new Function<BaseBean<List<FootprintParentBean>>, Collection<FootprintParentBean>>() {
                                    @Override
                                    public Collection<FootprintParentBean> apply(BaseBean<List<FootprintParentBean>> listBaseBean) throws Exception {
                                        mSmartRefreshLayout.finishRefresh();
                                        if (listBaseBean.success() && listBaseBean.data != null) {
                                            LZLog.i(TAG, "load requestGetFootprintList success ");
                                            List<FootprintParentBean> data = listBaseBean.data;
                                            if (data.size()==0){
                                                mActionBarRight.setVisibility(View.GONE); //UI更改操作
                                            }else{
                                                mActionBarRight.setVisibility(View.VISIBLE); //UI更改操作
                                            }
                                            int allSize=0;
                                            for (FootprintParentBean datum : data) {
                                                allSize+=datum.getVoList().size();
                                            }
                                            hasLoadMore = !ArraysUtils.isListEmpty(data) && allSize == pageSize;
                                            return data;
                                        }
                                        throw new Exception(new Throwable(listBaseBean.msg));
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<FootprintParentBean> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    LZLog.i(TAG, "requestGetFootprintList onLoadMore" + currentPage);
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        mLoadMoreHelper.loadData();
    }

    private void changeClickEditView() {
        if (mFootprintAdapter.isEdiTMode()) {
            mActionBarRight.setText(R.string.foot_print_edit_text);
            mDeleteRl.setVisibility(View.GONE);
        } else {
            mActionBarRight.setText(R.string.callback_txt);
            mDeleteRl.setVisibility(View.VISIBLE);
        }
        mFootprintAdapter.setEdiTMode(!mFootprintAdapter.isEdiTMode());
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
                //编辑
                if (ArraysUtils.isListEmpty(mFootprintAdapter.getListData())) {
                    return;
                }
                changeClickEditView();
                mFootprintAdapter.notifyDataSetChanged();
                break;
            case R.id.delete_tv:
                //删除
                if (ArraysUtils.isListEmpty(sSelectList)) {
                    MessageToast.showToast(this, "请选择要删除的足迹!");
                    return;
                }
                showLoadingDialog();
                ApiService.getNewsApi().requestDeleteFootprintInfo(sSelectList).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                            @Override
                            public void onNext(BaseBean<Object> baseBean) {
                                super.onNext(baseBean);
                                if (isDestroy()) {
                                    return;
                                }
                                List<FootprintParentBean> listData = mFootprintAdapter.getListData();
                                int size = listData.size();
                                for (int i = size - 1; i >= 0; i--) {
                                    FootprintParentBean parentBean = listData.get(i);
                                    List<FootprintBean> voList = parentBean.getVoList();
                                    if (voList == null) {
                                        listData.remove(parentBean);
                                        break;
                                    }
                                    for (FootprintBean footprintBean : sSelectList) {
                                        if (voList.contains(footprintBean)) {
                                            voList.remove(footprintBean);
                                        }
                                    }
                                    if (ArraysUtils.isListEmpty(voList)) {
                                        listData.remove(parentBean);
                                    }
                                }
                                sSelectList.clear();

                                if (ArraysUtils.isListEmpty(listData)) {
                                    changeClickEditView();
                                    mLoadMoreHelper.showDeclaredEmpty();
                                } else {
                                    mFootprintAdapter.notifyDataSetChanged();
                                }
                                MessageToast.showToast(FootprintActivity.this, "删除成功");
                                dismissLoadingDialog();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                dismissLoadingDialog();
                            }
                        });
                break;
            default:
                break;
        }
    }
}
