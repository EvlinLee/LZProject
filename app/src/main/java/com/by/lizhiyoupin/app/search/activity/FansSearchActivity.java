package com.by.lizhiyoupin.app.search.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FansDataBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.user.adapter.FansListAdapter;

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
 * @date :  2019/11/11 17:44
 * Summary: 粉丝搜索
 */
@Route(path = "/app/FansSearchActivity")
public class FansSearchActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG=FansSearchActivity.class.getSimpleName();
    private TextView mEmptyTv;
    private View mLoadingLayout;
    private View mFailRetry;
    private EditText mSearchEt;
    private ImageView mBackIv;
    private View mSearchLl;
    private TextView mSearchTv;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FansListAdapter mAdapter;

    private LoadMoreHelperRx<FansDataBean.FansListBean, Integer> mLoadMoreHelper;
    private boolean hasLoadMore;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private String searchContent="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_search_layout);
        initImmersionBar(Color.BLACK,false);
        initView();
    }

    private void initView() {
        mSearchLl = findViewById(R.id.search_ll);
        mSearchEt = findViewById(R.id.actionbar_search_et);
        mBackIv = findViewById(R.id.actionbar_search_back_iv);
        mSearchTv = findViewById(R.id.actionbar_search_cancel_tv);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyTv = findViewById(R.id.empty_layout);
        mFailRetry = findViewById(R.id.fail_retry);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mBackIv.setOnClickListener(this);
        mSearchTv.setOnClickListener(this);
        mSearchEt.setBackgroundResource(R.drawable.fans_edit_corners_four_shape);
        mSearchEt.setHint(R.string.fans_search_hint_text);
        mBackIv.setImageResource(R.drawable.direction_back_left_white);
        mSearchLl.setBackgroundColor(Color.BLACK);
        mSearchTv.setTextSize(15);
        mSearchTv.setTextColor(Color.WHITE);
        mSearchTv.setText(R.string.search_txt);
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadingView();
                    //点击搜索的时候隐藏软键盘
                    DeviceUtil.hideInputMethod(v);
                    return true;
                }
                return false;
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new FansListAdapter(this,getSupportFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        loadRecyclerView();
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<FansDataBean.FansListBean, Integer>(this)
                .adapter(mAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyTv)
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<FansDataBean.FansListBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<FansDataBean.FansListBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load requestGetFansList");

                        return ApiService.getFansApi().requestGetFansList(0,searchContent, 1, 0, integer, pageSize)
                                .map(new Function<BaseBean<FansDataBean>, Collection<FansDataBean.FansListBean>>() {
                                    @Override
                                    public Collection<FansDataBean.FansListBean> apply(BaseBean<FansDataBean> beanBaseBean) throws Exception {

                                        if (beanBaseBean.success() && beanBaseBean.data != null) {
                                            List<FansDataBean.FansListBean> list = beanBaseBean.data.getFansList();
                                            boolean listEmpty = ArraysUtils.isListEmpty(list);
                                            hasLoadMore = !listEmpty && list.size() == pageSize;
                                            return list;
                                        }
                                        throw new Exception(beanBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<FansDataBean.FansListBean> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();

        mScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    LZLog.i(TAG, "onLoadMore requestGetFansList");
                    mLoadMoreHelper.loadDataMore();
                }
            }
        };

        mRecyclerView.addOnScrollListener(mScrollListener);

    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()){
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_search_cancel_tv:
                //搜索
                loadingView();
                break;
            case R.id.actionbar_search_back_iv:
                finish();
                break;
        }
    }

    private void loadingView(){
        if (TextUtils.isEmpty(mSearchEt.getText().toString())){
            CommonToast.showToast("请输入搜索内容");
            return;
        }
        searchContent=mSearchEt.getText().toString();
        mLoadMoreHelper.loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSearchEt!=null){
            DeviceUtil.hideInputMethod(mSearchEt);
        }
    }
}
