package com.by.lizhiyoupin.app.main.fragment;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ShopKindBean;
import com.by.lizhiyoupin.app.io.bean.ShopMindBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.adapter.SuperStoreAdapter;

import java.util.Collection;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * jyx
 * 超级购店铺列表
 */
public class SuperStoreFragment extends BaseFragment  {

    private RecyclerView store_rcy;
    private LoadMoreHelperRx<ShopKindBean, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private LinearLayoutManager mLinearLayoutManager;
    private SuperStoreAdapter adapater;
    private boolean hasLoadMore;
    private TextView empty_layout;
    private View mFailRetry;
    private View mloadingLayout;
    private int minid=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_super_store, container, false);
        initView(root);

        return root;
    }
    

    private void initView(View root) {

        mFailRetry = root.findViewById(R.id.fail_retry);
        mloadingLayout = root.findViewById(R.id.loading_layout);
        empty_layout = root.findViewById(R.id.empty_layout);
        store_rcy = root.findViewById(R.id.store_rcy);//条目列表
        DividerItemDecoration2 mDividerItemDecoration = new DividerItemDecoration2(getActivity(), DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(getActivity(), 4));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F2F2F5));
        store_rcy.addItemDecoration(mDividerItemDecoration);
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        store_rcy.setLayoutManager(mLinearLayoutManager);
        adapater = new SuperStoreAdapter(getActivity());
        store_rcy.setAdapter(adapater);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String type = arguments.getString(CommonConst.KEY_FANS_LIST_TYPE);
            loadRecyclerView(type);
        }


    }


    private void loadRecyclerView(String type) {
        mLoadMoreHelper = new LoadMoreBuilderRx<ShopKindBean, Integer>(getActivity())
                .adapter(adapater)
                .recyclerView(store_rcy)
                .emptyView(empty_layout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 4))
                .loader(new LoadMoreHelperRx.Loader<ShopKindBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<ShopKindBean>> load(Integer integer, int pageSize) {
                        return ApiService.getSuperApi().requestShopKind(Integer.parseInt(type),minid/*, integer, pageSize*/)
                                .map(new Function<BaseBean<ShopMindBean>, Collection<ShopKindBean>>() {
                            @Override
                            public Collection<ShopKindBean> apply(BaseBean<ShopMindBean> listBaseBean) throws Exception {
                                if (listBaseBean.success() && listBaseBean.getResult() != null) {
                                    ShopMindBean mindBean=listBaseBean.getResult();

                                    List<ShopKindBean> list =
                                            mindBean.getData();
                                    boolean listEmpty = ArraysUtils.isListEmpty(list);
                                    minid= mindBean.getMin_id();
                                    hasLoadMore = !listEmpty && list.size()>4;
                                    return list;
                                }
                                throw new Exception(listBaseBean.msg);
                            }

                        });

                    }

                    @Override
                    public boolean hasMore(Collection<ShopKindBean> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();

        mScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    mLoadMoreHelper.loadDataMore();
                }
            }
        };

        store_rcy.addOnScrollListener(mScrollListener);
        mLoadMoreHelper.loadData();
    }


    public void refresh() {
        if (isAdded()&& mLoadMoreHelper!=null){
            minid=1;
            mLoadMoreHelper.loadData();
        }
    }
}
