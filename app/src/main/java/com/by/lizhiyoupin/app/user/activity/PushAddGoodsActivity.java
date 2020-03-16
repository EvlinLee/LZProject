package com.by.lizhiyoupin.app.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.manager.TabFragmentManager;
import com.by.lizhiyoupin.app.user.adapter.PushAddGoodsAdapter;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.Collection;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/3 10:38
 * Summary: 运营商推送管理 添加商品页
 */
@Route(path = "/app/PushAddGoodsActivity")
public class PushAddGoodsActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = PushAddGoodsActivity.class.getSimpleName();

    private TextView mSubmitTv;
    private TextView mGotoHomeTv;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTv;
    private View mLoadingLayout;
    private View mFailRetry;
    private LinearLayoutManager mLinearLayoutManager;
    private PushAddGoodsAdapter mAdapter;
    private LoadMoreHelperRx<PreciseListBean, Integer> mLoadMoreHelper;
    private boolean hasLoadMore;
    private PreciseListBean mGoodBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_add_goods_layout);
        Intent intent = getIntent();
        mGoodBean = intent.getParcelableExtra(CommonConst.KEY_PUSH_GOODS_SELECT);
        initImmersionBar(Color.WHITE, true);
        initView();
    }

    private void initView() {
        findViewById(R.id.actionbar_rl).setBackgroundColor(Color.WHITE);
        TextView backTv = findViewById(R.id.actionbar_back_tv);
        TextView titleTv = findViewById(R.id.actionbar_title_tv);
        mSubmitTv = findViewById(R.id.submit_tv);
        mGotoHomeTv = findViewById(R.id.goto_home_tv);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyTv = findViewById(R.id.empty_tv);
        mFailRetry = findViewById(R.id.fail_retry);
        mLoadingLayout = findViewById(R.id.loading_layout);

        mSubmitTv.setEnabled(mGoodBean != null);
        backTv.setText("");
        titleTv.setText("橱窗商品");
        backTv.setOnClickListener(this);
        mSubmitTv.setOnClickListener(this);
        mGotoHomeTv.setOnClickListener(this);
        mEmptyTv.setText("还没有推荐的商品，先去首页搜索商品推荐吧~");
        ViewUtil.setDrawableOfTextView(mEmptyTv, R.drawable.emity_box_goods, ViewUtil.DrawableDirection.TOP);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(this, RecyclerView.VERTICAL);
        dividerItemDecoration2.setDividerHeight(DeviceUtil.dip2px(this, 8));
        dividerItemDecoration2.setDividerColor(Color.TRANSPARENT);
        mRecyclerView.addItemDecoration(dividerItemDecoration2);
        mAdapter = new PushAddGoodsAdapter(this, mSubmitTv, mGoodBean);
        RecyclerView.ItemAnimator itemAnimator = mRecyclerView.getItemAnimator();
        if (itemAnimator != null) {
            //取消notify刷新闪烁问题
            itemAnimator.setChangeDuration(0);
        }
        mRecyclerView.setAdapter(mAdapter);
        loadRecyclerView();
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<PreciseListBean, Integer>(this)
                .adapter(mAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyTv)
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<PreciseListBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<PreciseListBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load requestGetPushGoodsList");

                        return ApiService.getFansApi().requestGetPushGoodsList(integer, pageSize)
                                .observeOn(AndroidSchedulers.mainThread())//指定map在主线程
                                .map(new Function<BaseBean<List<PreciseListBean>>, Collection<PreciseListBean>>() {
                                    @Override
                                    public Collection<PreciseListBean> apply(BaseBean<List<PreciseListBean>> beanBaseBean) throws Exception {

                                        if (beanBaseBean.success() && beanBaseBean.data != null) {
                                            List<PreciseListBean> list = beanBaseBean.data;
                                            boolean listEmpty = ArraysUtils.isListEmpty(list);
                                            hasLoadMore = !listEmpty && list.size() == pageSize;
                                            if (integer <= 1 && mAdapter.getSelectBean() == null && !listEmpty) {
                                                //默认选中第一个
                                                PreciseListBean preciseListBean = list.get(0);
                                                mAdapter.setSelectBean(preciseListBean);
                                                mSubmitTv.setEnabled(true);
                                            }
                                            if (integer <= 1 && listEmpty) {
                                                mGotoHomeTv.setVisibility(View.VISIBLE);
                                                mSubmitTv.setVisibility(View.GONE);
                                            } else {
                                                mGotoHomeTv.setVisibility(View.GONE);
                                                mSubmitTv.setVisibility(View.VISIBLE);
                                            }
                                            return list;
                                        }
                                        if (integer <= 1) {
                                            mGotoHomeTv.setVisibility(View.VISIBLE);
                                            mSubmitTv.setVisibility(View.GONE);
                                        }
                                        throw new Exception(beanBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<PreciseListBean> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();

        EndlessRecyclerOnScrollListener mScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    LZLog.i(TAG, "onLoadMore requestGetPushGoodsList");
                    mLoadMoreHelper.loadDataMore();
                }
            }
        };

        mRecyclerView.addOnScrollListener(mScrollListener);
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
            case R.id.submit_tv:
                //提交
                Intent intent = new Intent();
                intent.putExtra(CommonConst.KEY_PUSH_GOODS_SELECT, mAdapter.getSelectBean());
                setResult(PushEditorActivity.PUSH_EDIT_GOODS_REQUEST_CODE, intent);
                finish();
                break;
            case R.id.goto_home_tv:
                //去首页
                CommonSchemeJump.showMainActivity(this, TabFragmentManager.MAIN_HOME_PAGE_TAB_NAME, 0, 0);
                break;
            default:
                break;
        }
    }
}
