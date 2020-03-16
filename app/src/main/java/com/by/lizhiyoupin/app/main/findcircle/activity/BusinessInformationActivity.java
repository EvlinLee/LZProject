package com.by.lizhiyoupin.app.main.findcircle.activity;

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
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.findcircle.adapter.BusinessConsultationAdapter;

import java.util.Collection;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/21 12:00
 * Summary: 商学院--资讯文章列表页
 */
@Route(path = "/app/BusinessInformationActivity")
public class BusinessInformationActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = BusinessInformationActivity.class.getSimpleName();
    private View mEmptyLayout;
    private View mFailRetry;
    private View mLoadingLayout;
    private RecyclerView mRecyclerView;
    private LoadMoreHelperRx<BusinessArticleBean, Integer> mLoadMoreHelper;
    private BusinessConsultationAdapter mAdapter;
    private boolean hasLoadMore;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DeviceUtil.setStatusBarWordsColor(this,true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_information_layout);
        StatusBarUtils.setColor(this, Color.WHITE,0);
        initView();
    }

    private void initView() {
        TextView backTv = findViewById(R.id.actionbar_back_tv);
        TextView titleTv = findViewById(R.id.actionbar_title_tv);
        findViewById(R.id.actionbar_rl).setBackgroundColor(Color.WHITE);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyLayout =  findViewById(R.id.empty_tv);
        mFailRetry =  findViewById(R.id.fail_retry);
        mLoadingLayout =  findViewById(R.id.loading_layout);
        backTv.setText("");
        backTv.setOnClickListener(this);
        titleTv.setText(R.string.litchi_information_text);
        mAdapter = new BusinessConsultationAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        loadRecyclerView();
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<BusinessArticleBean, Integer>(this)
                .adapter(mAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyLayout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mLoadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<BusinessArticleBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<BusinessArticleBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "requestBusinessScrollArticleList load");

                        return ApiService.getFindCircleApi().requestBusinessScrollArticleList(integer, pageSize)
                                .map(new Function<BaseBean<List<BusinessArticleBean>>, Collection<BusinessArticleBean>>() {
                                    @Override
                                    public Collection<BusinessArticleBean> apply(BaseBean<List<BusinessArticleBean>> listBaseBean) throws Exception {
                                        if (listBaseBean.success()) {
                                            LZLog.i(TAG, "requestBusinessScrollArticleList success");
                                            List<BusinessArticleBean> result = listBaseBean.getResult();
                                            hasLoadMore = !ArraysUtils.isListEmpty(result) && result.size() == pageSize;
                                            return result;
                                        }
                                        throw new Exception(listBaseBean.msg);
                                    }
                                });
                    }
                }).build();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    LZLog.i(TAG, "onLoadMore requestBusinessScrollArticleList");
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        mLoadMoreHelper.loadData();

    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()){
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
        }
    }
}
