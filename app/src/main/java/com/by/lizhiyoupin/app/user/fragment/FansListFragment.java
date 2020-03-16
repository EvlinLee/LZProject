package com.by.lizhiyoupin.app.user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FansDataBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.user.adapter.FansListAdapter;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
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
 * @date :  2019/11/9 19:39
 * Summary: 粉丝列表
 */
public class FansListFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = FansListFragment.class.getSimpleName();
    private RecyclerView fansRecyclerView;
    private Context mContext;
    private View mEmptyLayout;
    private View mFailRetry;
    private View mloadingLayout;
    private TextView mFansTimeTv;
    private TextView mTotalIncomeTv;
    private TextView mFansAmountTv;
    private LoadMoreHelperRx<FansDataBean.FansListBean, Integer> mLoadMoreHelper;
    private FansListAdapter mAdapter;
    private int fansType = 0;//粉丝类型，0专属粉丝，1普通粉丝
    private int sortType;//排序类型 1时间 2收益 3粉丝数
    private int eSortDirection = CommonConst.E_UPLIMIT_SORT_DOWN;
    private boolean hasLoadMore;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_fans_list_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            fansType = arguments.getInt(CommonConst.KEY_FANS_LIST_TYPE, 0);
        }
        initView(inflate);
        loadRecyclerView();
        return inflate;
    }

    private void initView(View root) {
        fansRecyclerView = root.findViewById(R.id.fans_recyclerView);
        mFansTimeTv = root.findViewById(R.id.fans_join_time_tv);
        mTotalIncomeTv = root.findViewById(R.id.fans_total_income_tv);
        mFansAmountTv = root.findViewById(R.id.fans_amount_tv);
        mEmptyLayout = root.findViewById(R.id.empty_layout);
        mFailRetry = root.findViewById(R.id.fail_retry);
        mloadingLayout = root.findViewById(R.id.loading_layout);
        mFansTimeTv.setOnClickListener(this);
        mTotalIncomeTv.setOnClickListener(this);
        mFansAmountTv.setOnClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        fansRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new FansListAdapter(mContext, getChildFragmentManager());
        fansRecyclerView.setAdapter(mAdapter);
        ViewUtil.setTextViewFormat(mContext, mFansTimeTv, R.string.fans_join_time_text, 0);
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<FansDataBean.FansListBean, Integer>(mContext)
                .adapter(mAdapter)
                .recyclerView(fansRecyclerView)
                .emptyView(mEmptyLayout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<FansDataBean.FansListBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<FansDataBean.FansListBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load requestGetFansList " + integer);
                        return ApiService.getFansApi().requestGetFansList(fansType + 1, "", sortType, eSortDirection, integer, pageSize)
                                .observeOn(AndroidSchedulers.mainThread())//指定map在主线程
                                .map(new Function<BaseBean<FansDataBean>, Collection<FansDataBean.FansListBean>>() {
                                    @Override
                                    public Collection<FansDataBean.FansListBean> apply(BaseBean<FansDataBean> listBaseBean) throws Exception {
                                        if (listBaseBean.success() && listBaseBean.data != null) {
                                            List<FansDataBean.FansListBean> fansList = listBaseBean.data.getFansList();
                                            hasLoadMore = !ArraysUtils.isListEmpty(fansList) && fansList.size() == pageSize;

                                            ViewUtil.setTextViewFormat(mContext, mFansTimeTv, R.string.fans_join_time_text, listBaseBean.data.getNowFansCount());
                                            return fansList;
                                        }
                                        throw new Exception(new Throwable(listBaseBean.msg));
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<FansDataBean.FansListBean> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();
        fansRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    LZLog.i(TAG, "requestGetFansList onLoadMore" + currentPage);
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        mLoadMoreHelper.loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fans_join_time_tv:
                //1时间 2收益 3粉丝数
                requestClickRadar(mFansTimeTv, 1);
                break;
            case R.id.fans_total_income_tv:
                requestClickRadar(mTotalIncomeTv, 2);
                break;
            case R.id.fans_amount_tv:
                requestClickRadar(mFansAmountTv, 3);
                break;
        }
    }


    private void requestClickRadar(TextView clickTv, int orderType) {
        if (!clickTv.isSelected()) {
            eSortDirection = CommonConst.E_UPLIMIT_SORT_DOWN;
        } else {
            eSortDirection = eSortDirection == CommonConst.E_UPLIMIT_SORT_DOWN ? CommonConst.E_UPLIMIT_SORT_UP : CommonConst.E_UPLIMIT_SORT_DOWN;
        }
        sortType = orderType;
        resetTitleRankDrawable(clickTv, eSortDirection);
        mLoadMoreHelper.loadData();
    }

    private void resetTitleRankDrawable(TextView clickTv, int eSortDir) {
        //对点击的重置图
        ViewUtil.setDrawableOfTextView(mFansTimeTv, R.drawable.fans_direction_normal, ViewUtil.DrawableDirection.RIGHT);
        ViewUtil.setDrawableOfTextView(mTotalIncomeTv, R.drawable.fans_direction_normal, ViewUtil.DrawableDirection.RIGHT);
        ViewUtil.setDrawableOfTextView(mFansAmountTv, R.drawable.fans_direction_normal, ViewUtil.DrawableDirection.RIGHT);
        mFansTimeTv.setSelected(false);
        mTotalIncomeTv.setSelected(false);
        mFansAmountTv.setSelected(false);
        //对点击的设置 图片
        if (clickTv != null) {
            clickTv.setSelected(true);
            ViewUtil.setDrawableOfTextView(clickTv, getDrawableIdBySortDir(eSortDir), ViewUtil.DrawableDirection.RIGHT);
        }
    }

    private int getDrawableIdBySortDir(int sortDir) {
        int res = R.drawable.sort_normal;
        switch (sortDir) {
            case 0:
                res = R.drawable.fans_direction_down;
                break;
            case 1:
                res = R.drawable.fans_direction_up;
                break;
        }
        return res;
    }

    public void refresh() {
        if (isAdded()&& mLoadMoreHelper!=null){
            mLoadMoreHelper.loadData();
        }
    }
}
