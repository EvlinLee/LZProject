package com.by.lizhiyoupin.app.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.RecordsBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.adapter.ProductSortListAdapter;

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
 * @date :  2019/9/30 17:50
 * Summary: 商品排序列表页 本地 二级列表页排序
 */
public class SecondSortListFragment extends BaseFragment implements View.OnClickListener, OnCheckedChangeListener {
    private static final String TAG = SecondSortListFragment.class.getSimpleName();

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
    private long mOrderType = 1;//排序类型 1 综合排序 2 销量排序 3 券后价排序 4 佣金排序

    private RecyclerView mRecyclerView;
    private Context mContext;
    private ProductSortListAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration2 mDividerItemDecoration;
    private SpaceItemDecoration mSpaceItemDecoration;
    private long secondKindId = -1; //要去查询的二级类目id
    private View mEmpty;
    private View mFailRetry;
    private View mloadingLayout;
    private LoadMoreHelperRx<RecordsBean, Integer> mLoadMoreHelper;
    private boolean hasLoadMore;
    //开关
    private CheckBox mSwitchBox;

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
            secondKindId = arguments.getLong(CommonConst.KEY_SECOND_COMMODITY_ID, -1);
        }
        initView(inflate);
        return inflate;
    }

    private void initView(View root) {
        mEmpty = root.findViewById(R.id.empty_layout);
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

        mAdapter = new ProductSortListAdapter(mContext);

        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mSpaceItemDecoration = new SpaceItemDecoration(DeviceUtil.dip2px(mContext, 10), 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //每行item数据=spanCount/getSpanSize
                //底部最后一个显示加载更多 或暂无数据
                return mLoadMoreHelper.getData().size() == position ? 2 : 1;

            }
        });
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mDividerItemDecoration = new DividerItemDecoration2(mContext, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(mContext, 10));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F4F4F7));

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(mSpaceItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        //默认综合排序
        mTitleComprehensiveTv.setSelected(true);
        loadRecyclerView();
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<RecordsBean, Integer>(mContext)
                .adapter(mAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmpty)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<RecordsBean>(1, 20) {
                    @Override
                    public Integer pageStrategy(List<RecordsBean> objects, Integer current) {
                        return super.pageStrategy(objects, current);
                    }
                })
                .loader(new LoadMoreHelperRx.Loader<RecordsBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<RecordsBean>> load(Integer integer, int pageSize) {
                        HashMap<String, Long> map = new HashMap<>();
                        map.put("orderByType", mOrderType);
                        map.put("secondKindId", secondKindId);
                        //commodityType商品类型 0全部 1优惠商品
                        map.put("commodityType", mSwitchBox.isChecked() ? 1L : 0L);
                        map.put("start", (long) integer);
                        map.put("limit", (long) pageSize);
                        map.put("desc", (long) eSortDirection);
                        return ApiService.getHomeApi().requestSecondLevelList(map)
                                .map(new Function<BaseBean<List<RecordsBean>>, Collection<RecordsBean>>() {
                                    @Override
                                    public Collection<RecordsBean> apply(BaseBean<List<RecordsBean>> commonListBeanBaseBean) throws Exception {
                                        LZLog.i(TAG, "requestSecondLevelList" + commonListBeanBaseBean.success());
                                        if (commonListBeanBaseBean.success()) {
                                            List<RecordsBean> result = commonListBeanBaseBean.getResult();
                                            hasLoadMore = !ArraysUtils.isListEmpty(result) && result.size() == pageSize;
                                            return result;
                                        }
                                        throw new Exception(commonListBeanBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<RecordsBean> data, int pageCount) {
                        return hasLoadMore;
                    }

                }).build();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                LZLog.i(TAG, "requestSecondLevelList onLoadMore" + currentPage);
                mLoadMoreHelper.loadDataMore();
            }
        });
        mLoadMoreHelper.loadData();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //仅显示优惠券商品 开关
        //根据开关去请求数据
        if (mLoadMoreHelper != null) {
            mLoadMoreHelper.loadData();
        }

    }

    @Override
    public void onClick(View v) {
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
        boolean isSingle = mAdapter.getType() == ProductSortListAdapter.TYPE_SINGLE;
        mAdapter.setType(isSingle ? ProductSortListAdapter.TYPE_OTHER : ProductSortListAdapter.TYPE_SINGLE);
        mTitleSortEtcIv.setImageResource(isSingle ? R.drawable.sort_etc : R.drawable.sort_etc_out);
        mRecyclerView.setLayoutManager(isSingle ? mGridLayoutManager : mLinearLayoutManager);
        mRecyclerView.removeItemDecoration(isSingle ? mDividerItemDecoration : mSpaceItemDecoration);
        mRecyclerView.addItemDecoration(isSingle ? mSpaceItemDecoration : mDividerItemDecoration);
        mAdapter.notifyDataSetChanged();
    }


    private void requestClickRadar(ImageView clickIv, TextView textView, int orderType) {
        if (orderType == 1) {
            //综合只有降序
            eSortDirection = CommonConst.E_UPLIMIT_SORT_DOWN;
        } else {
            if (!textView.isSelected()) {
                eSortDirection = CommonConst.E_UPLIMIT_SORT_DOWN;
            } else {
                eSortDirection = eSortDirection == CommonConst.E_UPLIMIT_SORT_DOWN ? CommonConst.E_UPLIMIT_SORT_UP : CommonConst.E_UPLIMIT_SORT_DOWN;
            }
        }
        mOrderType = orderType;
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


}
