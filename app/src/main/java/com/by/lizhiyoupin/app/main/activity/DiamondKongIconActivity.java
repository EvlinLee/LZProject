package com.by.lizhiyoupin.app.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.DiamonKongBean;
import com.by.lizhiyoupin.app.io.bean.HandPickDetailBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.adapter.DiamondKongSortListAdapter;
import com.by.lizhiyoupin.app.main.adapter.ProductSortListAdapter;
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
 * @date :  2019/11/22 16:40
 * Summary: 金刚区排序列表页
 */
@Route(path = "/app/DiamondKongIconActivity")
public class DiamondKongIconActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final String TAG = DiamondKongIconActivity.class.getSimpleName();
    private TextView mTitleTv;
    private String titleName;
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
    private RecyclerView mRecyclerView;
    private Context mContext;
    private DiamondKongSortListAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration2 mDividerItemDecoration;
    private SpaceItemDecoration mSpaceItemDecoration;

    private View mEmptyLayout;
    private View mFailRetry;
    private View mloadingLayout;
    private LoadMoreHelperRx<HandPickDetailBean, Integer> mLoadMoreHelper;
    //排序方向
    private int eSortDirection = CommonConst.E_UPLIMIT_SORT_DOWN;

    //排序类型
    private long mOrderType = 1;//排序类型 1 综合排序 2 销量排序 3 券后价排序 4 佣金排序
    private boolean hasLoadMore;
    private String mType;//金刚区入口类型。(截取 例如 litchi://nine_to_nine) 截取值 nine_to_nine
    private long min_id = 1;//默认传1，参见返回的”min_id” 如果有返回，请求下一页的时候一定要取值在传到后台
    //开关
    private CheckBox mSwitchBox;
    private SmartRefreshLayout mSmartRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_diamond_kong_layout);
        initImmersionBar(Color.WHITE, true);
        Intent intent = getIntent();
        titleName = intent.getStringExtra(CommonConst.KEY_ACTIONBAR_TITLE);
        mType = intent.getStringExtra(CommonConst.KEY_HOME_ICON_TYPE);
        if ("kaola".equals(mType)) {
            findViewById(R.id.line_a).setVisibility(View.GONE);
            findViewById(R.id.head_a).setVisibility(View.GONE);
            findViewById(R.id.line_b).setVisibility(View.GONE);
            findViewById(R.id.switch_rl).setVisibility(View.GONE);
        }
        initView();
    }

    private void initView() {
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        TextView backTv = findViewById(R.id.actionbar_back_tv);
        backTv.setText("");
        backTv.setOnClickListener(this);
        mTitleTv = findViewById(R.id.actionbar_title_tv);
        mTitleTv.setText(titleName);

        mEmptyLayout = findViewById(R.id.empty_layout);
        mFailRetry = findViewById(R.id.fail_retry);
        mloadingLayout = findViewById(R.id.loading_layout);
        mSwitchBox = findViewById(R.id.switch_box);
        mRecyclerView = findViewById(R.id.list_rv);
        mTitleComprehensiveTv = findViewById(R.id.title_comprehensive_tv);

        mTitleSalesVolumeLl = findViewById(R.id.title_sales_volume_ll);
        mTitleSalesVolumeTv = findViewById(R.id.title_sales_volume_tv);
        mTitleSalesVolumeIv = findViewById(R.id.title_sales_volume_iv);

        mTitlePostCouponPriceLl = findViewById(R.id.title_post_coupon_price__ll);
        mTitlePostCouponPriceTv = findViewById(R.id.title_post_coupon_price_tv);
        mTitlePostCouponPriceIv = findViewById(R.id.title_post_coupon_price_iv);

        mTitleCommissionLl = findViewById(R.id.title_commission_ll);
        mTitleCommissionTv = findViewById(R.id.title_commission_tv);
        mTitleCommissionIv = findViewById(R.id.title_commission_iv);

        mTitleSortEtcLl = findViewById(R.id.title_sort_etc_ll);
        mTitleSortEtcIv = findViewById(R.id.title_sort_etc_iv);

        mTitleComprehensiveTv.setOnClickListener(this);
        mTitleSalesVolumeLl.setOnClickListener(this);
        mTitlePostCouponPriceLl.setOnClickListener(this);
        mTitleCommissionLl.setOnClickListener(this);
        mTitleSortEtcLl.setOnClickListener(this);

        mSwitchBox.setOnCheckedChangeListener(this);

        mAdapter = new DiamondKongSortListAdapter(mContext);

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
        setSmartRefreshLayout();
    }

    private void setSmartRefreshLayout() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mLoadMoreHelper != null) {
                    min_id = 1;
                    mLoadMoreHelper.loadData();
                } else {
                    mSmartRefreshLayout.finishRefresh(1000);
                }

            }
        });
        mSmartRefreshLayout.setEnableAutoLoadMore(false);
        mSmartRefreshLayout.setEnableLoadMore(false);

    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<HandPickDetailBean, Integer>(mContext)
                .adapter(mAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyLayout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 20))
                .loader(new LoadMoreHelperRx.Loader<HandPickDetailBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<HandPickDetailBean>> load(Integer integer, int pageSize) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("type", mType);
                        map.put("minId", String.valueOf(min_id));
                        //排序类型 1 综合排序 2 销量排序 3 券后价排序 4 佣金排序
                        map.put("orderByType", String.valueOf(mOrderType));
                        //commodityType商品类型 0全部 1优惠商品
                        map.put("commodityType", String.valueOf(mSwitchBox.isChecked() ? 1L : 0L));
                        map.put("start", String.valueOf(integer));
                        map.put("limit", String.valueOf(pageSize));
                        map.put("desc", String.valueOf(eSortDirection));
                        return ApiService.getHomeApi().requestDiamondKongList(map)
                                .map(new Function<BaseBean<DiamonKongBean>, Collection<HandPickDetailBean>>() {
                                    @Override
                                    public Collection<HandPickDetailBean> apply(BaseBean<DiamonKongBean> beanBaseBean) throws Exception {
                                        LZLog.i(TAG, "requestDiamondKongList" + beanBaseBean.success());
                                        mSmartRefreshLayout.finishRefresh();
                                        if (beanBaseBean.success() && beanBaseBean.getResult() != null) {
                                            DiamonKongBean diamonKongBean = beanBaseBean.getResult();
                                            List<HandPickDetailBean> list = diamonKongBean.getData();
                                            min_id = diamonKongBean.getMinId();
                                            hasLoadMore = !ArraysUtils.isListEmpty(list) && list.size() == pageSize;
                                            return list;
                                        }
                                        throw new Exception(beanBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<HandPickDetailBean> data, int pageCount) {
                        return hasLoadMore;
                    }

                }).build();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                LZLog.i(TAG, "requestDiamondKongList onLoadMore" + currentPage);
                if (hasLoadMore) {
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        mLoadMoreHelper.loadData();

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //仅显示优惠券商品 开关
        //根据开关去请求数据
        if (mLoadMoreHelper != null) {
            min_id = 1;
            mLoadMoreHelper.loadData();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_comprehensive_tv:
                //综合
                min_id = 1;
                requestClickRadar(null, mTitleComprehensiveTv, 1);
                break;
            case R.id.title_sales_volume_ll:
                //销量
                min_id = 1;
                requestClickRadar(mTitleSalesVolumeIv, mTitleSalesVolumeTv, 2);
                break;
            case R.id.title_post_coupon_price__ll:
                //券后价
                min_id = 1;
                requestClickRadar(mTitlePostCouponPriceIv, mTitlePostCouponPriceTv, 3);
                break;
            case R.id.title_commission_ll:
                //佣金
                min_id = 1;
                requestClickRadar(mTitleCommissionIv, mTitleCommissionTv, 4);
                break;
            case R.id.title_sort_etc_ll:
                //样式 一列 2列 
                setGridRecyclerView();
                break;
            case R.id.actionbar_back_tv:
                finish();
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
