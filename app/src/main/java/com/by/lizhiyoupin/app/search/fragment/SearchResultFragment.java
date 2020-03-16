package com.by.lizhiyoupin.app.search.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.SearchCommodityBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.search.adapter.SearchSortListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/18 11:58
 * Summary: 三方 搜索结果列表
 */
public class SearchResultFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final String TAG = SearchResultFragment.class.getSimpleName();
    //综合
    private TextView mTitleComprehensiveTv;
    //销量
    private LinearLayout mTitleSalesVolumeLl;
    private ImageView mTitleSalesVolumeIv;
    private TextView mTitleSalesVolumeTv;
    //券后价
    private LinearLayout mTitlePostCouponPriceLl;
    private ImageView mTitlePostCouponPriceIv;
    private TextView mTitlePostCouponPriceTv;
    //佣金
    private LinearLayout mTitleCommissionLl;
    private ImageView mTitleCommissionIv;
    private TextView mTitleCommissionTv;
    //样式
    private LinearLayout mTitleSortEtcLl;
    private ImageView mTitleSortEtcIv;
    //排序方向
    private int eSortDirection = CommonConst.E_UPLIMIT_SORT_DOWN;

    //排序类型
    private long mSortType = 1;//排序类型 1 综合排序 2 销量排序 3 券后价排序 4 佣金排序

    private RecyclerView mRecyclerView;
    private Context mContext;
    private SearchSortListAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration2 mDividerItemDecoration;
    private SpaceItemDecoration mSpaceItemDecoration;

    private TextView mEmptyLayout;
    private View mFailRetry;
    private View mloadingLayout;
    private LoadMoreHelperRx<SearchCommodityBean, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mSingleScrollListener;
    private EndlessRecyclerOnScrollListener mGridScrollListener;
    private boolean hasLoadMore;
    //开关
    private CheckBox mSwitchBox;
    private int typeFrom = 0;//要搜索平台
    private String searchTitle = ""; //要去查询的内容
    private String goodsId = "";//默认为""，后台根据id是否为空 查询不同接口搜索
    private SmartRefreshLayout mSmartRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_sort_list_layout, container, false);
        Bundle arguments = getArguments();

        if (arguments != null) {
            String from = arguments.getString(CommonConst.KEY_CURRENT_SEARCH_PLATFORM, CommonConst.SOURCE_TAO_BAO);
            searchTitle = arguments.getString(CommonConst.KEY_SEARCH_TITLE, "");
            switch (from) {
                case CommonConst.SOURCE_TAO_BAO:
                    //只有淘宝可以根据淘口令搜索
                    goodsId = arguments.getString(CommonConst.KEY_SEARCH_ID, "");
                    typeFrom = 0;
                    break;
                case CommonConst.SOURCE_JING_DONG:
                    typeFrom = 1;
                    break;
                case CommonConst.SOURCE_PING_DUO_DUO:
                    typeFrom = 2;
                    break;
            }
        }
        initView(inflate);
        setSmartRefreshLayout();
        return inflate;
    }

    private void setSmartRefreshLayout() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mLoadMoreHelper != null) {
                    mLoadMoreHelper.loadData();
                } else {
                    mSmartRefreshLayout.finishRefresh(1000);
                }

            }
        });
        mSmartRefreshLayout.setEnableAutoLoadMore(false);
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

    private void initView(View root) {
        mSmartRefreshLayout = root.findViewById(R.id.smartRefreshLayout);
        mEmptyLayout = root.findViewById(R.id.empty_layout);
        mFailRetry = root.findViewById(R.id.fail_retry);
        mloadingLayout = root.findViewById(R.id.loading_layout);
        mSwitchBox = root.findViewById(R.id.switch_box);
        mRecyclerView = root.findViewById(R.id.list_rv);
        mTitleComprehensiveTv = root.findViewById(R.id.title_comprehensive_tv);

        mTitleSalesVolumeLl = root.findViewById(R.id.title_sales_volume_ll);
        mTitleSalesVolumeTv = root.findViewById(R.id.title_sales_volume_tv);
        mTitleSalesVolumeIv = root.findViewById(R.id.title_sales_volume_iv);

        mTitlePostCouponPriceLl = root.findViewById(R.id.title_post_coupon_price__ll);
        mTitlePostCouponPriceTv = root.findViewById(R.id.title_post_coupon_price_tv);
        mTitlePostCouponPriceIv = root.findViewById(R.id.title_post_coupon_price_iv);

        mTitleCommissionLl = root.findViewById(R.id.title_commission_ll);
        mTitleCommissionTv = root.findViewById(R.id.title_commission_tv);
        mTitleCommissionIv = root.findViewById(R.id.title_commission_iv);

        mTitleSortEtcLl = root.findViewById(R.id.title_sort_etc_ll);
        mTitleSortEtcIv = root.findViewById(R.id.title_sort_etc_iv);

        mTitleComprehensiveTv.setOnClickListener(this);
        mTitleSalesVolumeLl.setOnClickListener(this);
        mTitlePostCouponPriceLl.setOnClickListener(this);
        mTitleCommissionLl.setOnClickListener(this);
        mTitleSortEtcLl.setOnClickListener(this);

        mSwitchBox.setOnCheckedChangeListener(this);

        mAdapter = new SearchSortListAdapter(mContext);

        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mSpaceItemDecoration = new SpaceItemDecoration(DeviceUtil.dip2px(mContext, 10), 2);

        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mDividerItemDecoration = new DividerItemDecoration2(mContext, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(mContext, 10));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F4F4F7));
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //每行item数据=spanCount/getSpanSize
                //底部最后一个显示加载更多 或暂无数据
                return mLoadMoreHelper.getData().size() == position ? 2 : 1;

            }
        });
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(mSpaceItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        //默认综合排序
        mTitleComprehensiveTv.setSelected(true);
        loadRecyclerView();
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<SearchCommodityBean, Integer>(mContext)
                .adapter(mAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyLayout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 20))
                .loader(new LoadMoreHelperRx.Loader<SearchCommodityBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<SearchCommodityBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "requestSecondLevelList page==" + integer);
                        mSmartRefreshLayout.finishRefresh();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("goodsId", goodsId);
                        map.put("keyWord", searchTitle);
                        map.put("apiType", typeFrom);
                        map.put("sortType", mSortType);
                        map.put("hasCoupon", mSwitchBox.isChecked());
                        map.put("orderByType", eSortDirection == CommonConst.E_UPLIMIT_SORT_UP);
                        map.put("start", (long) integer);
                        map.put("limit", (long) pageSize);
                        return ApiService.getSearchApi().requestSearchCommodityList(map)
                                .map(new Function<BaseBean<List<SearchCommodityBean>>, Collection<SearchCommodityBean>>() {
                                    @Override
                                    public Collection<SearchCommodityBean> apply(BaseBean<List<SearchCommodityBean>> listBaseBean) throws Exception {
                                        LZLog.i(TAG, "requestSecondLevelList" + listBaseBean.success());
                                        if (listBaseBean.success()) {
                                            List<SearchCommodityBean> result = listBaseBean.getResult();
                                            if (ArraysUtils.isListEmpty(result) || result.size() <= 1) {
                                                hasLoadMore = false;
                                            } else {
                                                hasLoadMore = true;
                                            }
                                            return listBaseBean.getResult();
                                        }
                                        throw new Exception(listBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<SearchCommodityBean> data, int pageCount) {
                        return hasLoadMore;
                    }

                }).build();
        mSingleScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                mLoadMoreHelper.loadDataMore();
            }
        };
        mGridScrollListener = new EndlessRecyclerOnScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                mLoadMoreHelper.loadDataMore();
            }
        };
        mRecyclerView.addOnScrollListener(mGridScrollListener);
        mLoadMoreHelper.loadData();

    }

    /**
     * 刷新数据
     *
     * @param title
     */
    public void notifySortListData(String title) {
        LZLog.i(TAG, "刷新数据notifySortListData");
        this.searchTitle = title;
        this.goodsId = "";
        mLoadMoreHelper.loadData();
    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.title_comprehensive_tv:
                //综合
                requestClickRadar(null, mTitleComprehensiveTv, 1);
                break;
            case R.id.title_sales_volume_ll:
                //销量
                requestClickRadar(mTitleSalesVolumeIv, mTitleSalesVolumeTv, 2);
                break;
            case R.id.title_post_coupon_price__ll:
                //券后价
                requestClickRadar(mTitlePostCouponPriceIv, mTitlePostCouponPriceTv, 3);
                break;
            case R.id.title_commission_ll:
                //佣金
                requestClickRadar(mTitleCommissionIv, mTitleCommissionTv, 4);
                break;
            case R.id.title_sort_etc_ll:
                //样式 一列 2列
                setGridRecyclerView();
                break;

        }
    }

    /**
     * isSingle  true 当前是单列 需要切换成双列
     */
    private void setGridRecyclerView() {
        boolean isSingle = mAdapter.type == SearchSortListAdapter.TYPE_SINGLE;
        mAdapter.type = isSingle ? SearchSortListAdapter.TYPE_OTHER : SearchSortListAdapter.TYPE_SINGLE;
        mTitleSortEtcIv.setImageResource(isSingle ? R.drawable.sort_etc : R.drawable.sort_etc_out);
        mRecyclerView.setLayoutManager(isSingle ? mGridLayoutManager : mLinearLayoutManager);
        mRecyclerView.removeItemDecoration(isSingle ? mDividerItemDecoration : mSpaceItemDecoration);
        mRecyclerView.addItemDecoration(isSingle ? mSpaceItemDecoration : mDividerItemDecoration);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.removeOnScrollListener(isSingle ? mSingleScrollListener : mGridScrollListener);
        mRecyclerView.addOnScrollListener(isSingle ? mGridScrollListener : mSingleScrollListener);
    }


    private void requestClickRadar(ImageView clickIv, TextView textView, int orderType) {
        if (orderType == 1) {
            //综合只有降序
            eSortDirection = CommonConst.E_UPLIMIT_SORT_DOWN;
        } else {
            if (!textView.isSelected()){
                eSortDirection=CommonConst.E_UPLIMIT_SORT_DOWN;
            }else {
                eSortDirection = eSortDirection == CommonConst.E_UPLIMIT_SORT_DOWN ? CommonConst.E_UPLIMIT_SORT_UP : CommonConst.E_UPLIMIT_SORT_DOWN;
            }
        }
        mSortType = orderType;
        resetTitleRankDrawable(clickIv, textView, eSortDirection);

        mLoadMoreHelper.loadData();
    }


    private void resetTitleRankDrawable(ImageView clickIv, TextView textView, int eSortDir) {
        //对点击的重置图
        mTitleSalesVolumeIv.setImageResource(R.drawable.sort_normal);
        mTitlePostCouponPriceIv.setImageResource(R.drawable.sort_normal);
        mTitleCommissionIv.setImageResource(R.drawable.sort_normal);
        mTitleComprehensiveTv.setSelected(false);
        mTitleSalesVolumeTv.setSelected(false);
        mTitlePostCouponPriceTv.setSelected(false);
        mTitleCommissionTv.setSelected(false);
        //对点击的设置 图片
        if (clickIv != null) {
            clickIv.setImageResource(getDrawableIdBySortDir(eSortDir));
        }
        if (textView != null) {
            textView.setSelected(true);
        }
    }

    private int getDrawableIdBySortDir(int sortDir) {
        int res = R.drawable.sort_normal;
        switch (sortDir) {
            case 0:
                res = R.drawable.sort_down;
                break;
            case 1:
                res = R.drawable.sort_up;
                break;
        }
        return res;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //仅显示优惠券商品 开关
        //根据开关去请求数据
        mLoadMoreHelper.loadData();
    }
}
