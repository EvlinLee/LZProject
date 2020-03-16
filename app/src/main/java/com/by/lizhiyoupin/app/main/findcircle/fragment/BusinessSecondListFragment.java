package com.by.lizhiyoupin.app.main.findcircle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.findcircle.adapter.BusinessConsultationAdapter;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 21:56
 * Summary: 商学院--二级文章列表
 */
public class BusinessSecondListFragment extends BaseFragment {
    public static final String TAG = BusinessSecondListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TextView mEmptyTv;
    private View mLoadingLayout;
    private View mFailRetry;
    private Context mContext;
    private LinearLayoutManager mLinearLayoutManager;
    private LoadMoreHelperRx<BusinessArticleBean, Integer> mLoadMoreHelper;
    private BusinessConsultationAdapter mConsultationAdapter;
    private long ringFirstKindId;
    private long ringSecondKindId;
    private boolean hasLoadMore;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_business_second_list_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            ringFirstKindId = arguments.getLong(CommonConst.KEY_FIND_CIRCLE_FIRST_TAB_ID, 0);
            ringSecondKindId = arguments.getLong(CommonConst.KEY_FIND_CIRCLE_SECOND_TAB_ID, 0);
        }

        initView(inflate);
        return inflate;
    }

    private void initView(View root) {
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mEmptyTv = root.findViewById(R.id.empty_tv);
        mFailRetry = root.findViewById(R.id.fail_retry);
        mLoadingLayout = root.findViewById(R.id.loading_layout);

        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mConsultationAdapter = new BusinessConsultationAdapter(mContext);
        mRecyclerView.setAdapter(mConsultationAdapter);
        loadRecyclerView();

    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<BusinessArticleBean, Integer>(mContext)
                .adapter(mConsultationAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyTv)
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 20))
                .loader(new LoadMoreHelperRx.Loader<BusinessArticleBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<BusinessArticleBean>> load(Integer integer, int pageSize) {
                        Log.i(TAG, "requestBusinessSecondList: " + integer);

                        return ApiService.getFindCircleApi().requestBusinessSecondList(ringFirstKindId, ringSecondKindId, integer, pageSize)
                                .map(new Function<BaseBean<List<BusinessArticleBean>>, Collection<BusinessArticleBean>>() {
                                    @Override
                                    public Collection<BusinessArticleBean> apply(BaseBean<List<BusinessArticleBean>> listBaseBean) throws Exception {
                                        Log.i(TAG, "requestBusinessSecondList: apply ");
                                        if (listBaseBean.success()) {
                                            List<BusinessArticleBean> result = listBaseBean.getResult();
                                            hasLoadMore = !ArraysUtils.isListEmpty(result) && result.size() == pageSize;
                                            return result;
                                        }

                                        throw new Exception(listBaseBean.code);
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
                LZLog.i(TAG, "requestBusinessSecondList onLoadMore" + currentPage);
                if (hasLoadMore) {
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        mLoadMoreHelper.loadData();
    }

}
