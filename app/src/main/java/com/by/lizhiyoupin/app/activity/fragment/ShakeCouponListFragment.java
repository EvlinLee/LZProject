package com.by.lizhiyoupin.app.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.activity.adapter.ShakeCouponListAdapter;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
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
 * @date :  2019/11/15 17:03
 * Summary: 抖券列表
 */
public class ShakeCouponListFragment extends BaseFragment {
    public static final String TAG = ShakeCouponListFragment.class.getSimpleName();
    private Context mContext;

    private View mEmptyLayout;
    private View mFailRetry;
    private View mloadingLayout;
    private RecyclerView mRecyclerView;

    private LoadMoreHelperRx<ShakeCouponBean, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private ShakeCouponListAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private SpaceItemDecoration mSpaceItemDecoration;
    private boolean hasLoadMore;
    private int mType = 0;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_shake_coupon_list_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mType = arguments.getInt(CommonConst.KEY_INDICATOR_TYPE, 0);
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
        mAdapter = new ShakeCouponListAdapter(mContext,mType);
        mRecyclerView.setAdapter(mAdapter);
        loadRecyclerView();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        mLoadMoreHelper.loadData();
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<ShakeCouponBean, Integer>(mContext)
                .adapter(mAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyLayout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<ShakeCouponBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<ShakeCouponBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "requestShakeCouponList page==" + integer);
                        return ApiService.getActivityApi().requestShakeCouponList(mType, integer, pageSize)
                                .map(new Function<BaseBean<List<ShakeCouponBean>>, Collection<ShakeCouponBean>>() {
                                    @Override
                                    public Collection<ShakeCouponBean> apply(BaseBean<List<ShakeCouponBean>> listBaseBean) throws Exception {
                                        LZLog.i(TAG, "requestShakeCouponList" + listBaseBean.success());
                                        if (listBaseBean.success()) {
                                            List<ShakeCouponBean> result = listBaseBean.getResult();
                                            hasLoadMore = !ArraysUtils.isListEmpty(result) && result.size() == pageSize;

                                            return result;
                                        }
                                        throw new Exception(listBaseBean.msg);
                                    }
                                });
                    }
                }).build();
        mScrollListener = new EndlessRecyclerOnScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    mLoadMoreHelper.loadDataMore();
                }
            }
        };

        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
