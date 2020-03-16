package com.by.lizhiyoupin.app.activity.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.activity.adapter.ProductThreeListAdapter;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ProductListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;

import java.util.Collection;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 12:00
 * Summary: 值得买
 */
@Route(path = "/app/WorthBuyingActivity")
public class WorthBuyingActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = WorthBuyingActivity.class.getSimpleName();
    private TextView mActionbarTitle;
    private TextView mBack;
    private RecyclerView mRecyclerView;
    private View actionbarRl;
    private View mEmptyLayout;
    private View mFailRetry;
    private View mloadingLayout;
    private LoadMoreHelperRx<ProductListBean, Integer> mLoadMoreHelper;

    private ProductThreeListAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private SpaceItemDecoration mSpaceItemDecoration;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worth_buying_layout);
        initImmersionBar(Color.WHITE,true);
        initView();
    }

    private void initView() {
        actionbarRl = findViewById(R.id.actionbar_rl);
        mBack = findViewById(R.id.actionbar_back_tv);
        mActionbarTitle = findViewById(R.id.actionbar_title_tv);
        mEmptyLayout =  findViewById(R.id.empty_layout);
        mFailRetry =  findViewById(R.id.fail_retry);
        mloadingLayout =  findViewById(R.id.loading_layout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mBack.setText("");
        mBack.setOnClickListener(this);
        mActionbarTitle.setText("值得买");
        actionbarRl.setBackgroundColor(Color.WHITE);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //每行item数据=spanCount/getSpanSize
                //底部最后一个显示加载更多 或暂无数据
                return mLoadMoreHelper.getData().size() == position ? 2 : 1;

            }
        });
        mSpaceItemDecoration = new SpaceItemDecoration(DeviceUtil.dip2px(this, 5), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(mSpaceItemDecoration);
        mAdapter = new ProductThreeListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        loadRecyclerView();
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<ProductListBean, Integer>(this)
                .adapter(mAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyLayout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 20))
                .loader(new LoadMoreHelperRx.Loader<ProductListBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<ProductListBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "requestWorthBuyingList page==" + integer);


                        return ApiService.getActivityApi().requestWorthBuyingList()
                                .map(new Function<BaseBean<List<ProductListBean>>, Collection<ProductListBean>>() {
                                    @Override
                                    public Collection<ProductListBean> apply(BaseBean<List<ProductListBean>> listBaseBean) throws Exception {
                                        LZLog.i(TAG, "requestWorthBuyingList" + listBaseBean.success());
                                        if (listBaseBean.success()) {
                                            return listBaseBean.getResult();
                                        }
                                        throw new Exception(listBaseBean.msg);
                                    }
                                });
                    }
                }).build();
        mLoadMoreHelper.loadData();

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
