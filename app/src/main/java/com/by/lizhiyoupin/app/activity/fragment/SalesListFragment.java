package com.by.lizhiyoupin.app.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.activity.adapter.ProductThreeListAdapter;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ProductListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 15:16
 * Summary:
 */
public class SalesListFragment extends BaseFragment {
    public static final String TAG = SalesListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;

    private View mEmptyLayout;
    private View mFailRetry;
    private View mloadingLayout;

    private GridLayoutManager mGridLayoutManager;
    private SpaceItemDecoration mSpaceItemDecoration;
    private LoadMoreHelperRx<ProductListBean, Integer> mLoadMoreHelper;
    private ProductThreeListAdapter mAdapter;
    private Context mContext;
    private boolean hasLoadMore;
    private int saleType;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_sales_list_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            saleType = arguments.getInt(CommonConst.KEY_INDICATOR_TYPE, 0);
        }
        initView(inflate);
        return inflate;
    }

    private void initView(View root) {
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mEmptyLayout = root.findViewById(R.id.empty_layout);
        mFailRetry = root.findViewById(R.id.fail_retry);
        mloadingLayout = root.findViewById(R.id.loading_layout);
        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //每行item数据=spanCount/getSpanSize
                //底部最后一个显示加载更多 或暂无数据
                return mLoadMoreHelper.getData().size() == position ? 2 : 1;

            }
        });
        mSpaceItemDecoration = new SpaceItemDecoration(DeviceUtil.dip2px(mContext, 5), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(mSpaceItemDecoration);
        mAdapter = new ProductThreeListAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        loadRecyclerView();
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<ProductListBean, Integer>(mContext)
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
                        LZLog.i(TAG, "requestSaleList page==" + integer);
                        return ApiService.getActivityApi().requestSaleList(saleType + 1, integer, pageSize, 0, 1)
                                .map(new Function<BaseBean<List<ProductListBean>>, Collection<ProductListBean>>() {
                                    @Override
                                    public Collection<ProductListBean> apply(BaseBean<List<ProductListBean>> listBaseBean) throws Exception {
                                        LZLog.i(TAG, "requestSaleList" + listBaseBean.success());
                                        if (listBaseBean.success()) {
                                            List<ProductListBean> result = listBaseBean.getResult();
                                            hasLoadMore = !ArraysUtils.isListEmpty(result) && result.size() == pageSize;
                                            return result;
                                        }
                                        throw new Exception(listBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<ProductListBean> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                LZLog.i(TAG, "requestSaleList onLoadMore" + currentPage);
                if (hasLoadMore) {
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        mLoadMoreHelper.loadData();

    }

}
