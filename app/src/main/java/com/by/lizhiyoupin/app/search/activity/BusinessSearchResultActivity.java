package com.by.lizhiyoupin.app.search.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
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
 * @date :  2019/11/21 16:36
 * Summary: 商学院 搜索结果页
 */
@Route(path = "/app/BusinessSearchResultActivity")
public class BusinessSearchResultActivity extends BaseActivity implements View.OnClickListener {
    private TextView mSearchTv;
    private EditText mSearchEt;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private LoadMoreHelperRx<BusinessArticleBean, Integer> mLoadMoreHelper;
    private View mEmptyLayout;
    private View mFailRetry;
    private View mloadingLayout;
    private BusinessConsultationAdapter mConsultationAdapter;
    private boolean hasLoadMore;
    private String searchTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_search_result_layout);
        initImmersionBar(Color.WHITE,true);
        Intent intent = getIntent();
        searchTitle = intent.getStringExtra(CommonConst.KEY_SEARCH_TITLE);
        initView();

    }

    private void initView() {
        TextView backTv = findViewById(R.id.actionbar_back_tv);
        TextView titleTv = findViewById(R.id.actionbar_title_tv);
        mRecyclerView = findViewById(R.id.recyclerView);
        mSearchEt = findViewById(R.id.search_et);
        mSearchTv = findViewById(R.id.search_tv);
        mEmptyLayout = findViewById(R.id.empty_layout);
        mFailRetry = findViewById(R.id.fail_retry);
        mloadingLayout = findViewById(R.id.loading_layout);
        mSearchTv.setOnClickListener(this);
        backTv.setOnClickListener(this);
        titleTv.setText(R.string.business_title_text);
        mSearchEt.setText(searchTitle);
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String seartxt = mSearchEt.getText().toString();
                    if (TextUtils.isEmpty(seartxt)) {
                        return false;
                    }
                    DeviceUtil.hideInputMethod(mSearchEt);
                    mLoadMoreHelper.loadData();
                    return true;
                }
                return false;
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mConsultationAdapter = new BusinessConsultationAdapter(this);
        mRecyclerView.setAdapter(mConsultationAdapter);
        loadRecyclerView();
    }


    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<BusinessArticleBean, Integer>(this)
                .adapter(mConsultationAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyLayout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<BusinessArticleBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<BusinessArticleBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load requestBusinessSearchList ");

                        return ApiService.getFindCircleApi().requestBusinessSearchList(mSearchEt.getText().toString(), integer, pageSize)
                                .map(new Function<BaseBean<List<BusinessArticleBean>>, Collection<BusinessArticleBean>>() {
                                    @Override
                                    public Collection<BusinessArticleBean> apply(BaseBean<List<BusinessArticleBean>> listBaseBean) throws Exception {
                                        if (listBaseBean.success() && listBaseBean.data != null) {
                                            List<BusinessArticleBean> list = listBaseBean.data;
                                            hasLoadMore = !ArraysUtils.isListEmpty(list) && list.size() == pageSize;
                                            return list;
                                        }
                                        throw new Exception(new Throwable(listBaseBean.msg));
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<BusinessArticleBean> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    LZLog.i(TAG, "requestBusinessSearchList onLoadMore" + currentPage);
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        mLoadMoreHelper.loadData();

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
            case R.id.search_tv:
                DeviceUtil.hideInputMethod(mSearchEt);
                mLoadMoreHelper.loadData();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSearchEt!=null){
            DeviceUtil.hideInputMethod(mSearchEt);
        }
    }
}
