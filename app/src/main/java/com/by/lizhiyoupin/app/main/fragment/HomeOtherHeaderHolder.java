package com.by.lizhiyoupin.app.main.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.RecordsBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.LoadType;
import com.by.lizhiyoupin.app.main.adapter.HomeOtherSortListAdapter;
import com.by.lizhiyoupin.app.main.adapter.ProductSortListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/2 20:28
 * Summary: 首页其他tab的商品列表加载
 */
public class HomeOtherHeaderHolder implements View.OnClickListener, Handler.Callback {
    public static final int REFRESH_LIST_DATA_CODE= 203;
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
    private long tabId = -1;// 一级类目id
    private long start = 1;
    private long limit = 20;
    private boolean hasLoadMore;
    private HomeOtherSortListAdapter mSortAdapter;//排序列表

    private SmartRefreshLayout mSmartRefreshLayout;
    private VirtualLayoutManager mLayoutManager;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private boolean isloadingList=false;//是否正在加载数据

    private Handler mHandler;
    private ImageView mJumpTopIv;
    // 分页相关layout
    private  FrameLayout mFooterView;
    private  View mLoadingLayout;
    private  View mNoMoreLayout;
    private  View mNoDataLayout;

    public HomeOtherHeaderHolder() {
        mHandler=new Handler(Looper.getMainLooper(),this);
    }

    public View getOnCreateView(Context context, long tabId, HomeOtherSortListAdapter mBottomListAdapter,
                                VirtualLayoutManager layoutManager,
                                SmartRefreshLayout smartRefreshLayout,
                                RecyclerView recyclerView,ImageView jumpTopIv) {
        this.mContext = context;
        this.tabId = tabId;
        this.mSortAdapter = mBottomListAdapter;
        this.mLayoutManager=layoutManager;
        this.mSmartRefreshLayout = smartRefreshLayout;
        this.mRecyclerView=recyclerView;
        this.mJumpTopIv=jumpTopIv;
        View sortHeaderView = LayoutInflater.from(context).inflate(R.layout.item_sort_header_layout, null, false);
        initView(sortHeaderView);
        //加载更多
        mFooterView = new FrameLayout(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        mLoadingLayout = inflater.inflate(R.layout.list_item_loading, mFooterView, false);
        mFooterView.addView(mLoadingLayout);
        mNoMoreLayout = inflater.inflate(R.layout.no_more_text, mFooterView, false);
        mNoMoreLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoMoreLayout);
        TextView noMoreTv = mNoMoreLayout.findViewById(R.id.more);
        noMoreTv.setText(R.string.empty_load_more_text);
        noMoreTv.setGravity(Gravity.CENTER);
        mNoDataLayout = inflater.inflate(R.layout.item_home_empty_list_layout, mFooterView, false);
        mNoDataLayout.setVisibility(View.GONE);
        mFooterView.addView(mNoDataLayout);

        mSortAdapter.setFooterView(mFooterView);
        return sortHeaderView;
    }

    /**
     * 重新开始刷新
     */
    public void resetSortRequest() {

        requestSortList(1);
    }

    private void initView(View root) {
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
        mTitleComprehensiveTv.setSelected(true);
        mTitleComprehensiveTv.setOnClickListener(this);
        mTitleSalesVolumeLl.setOnClickListener(this);
        mTitlePostCouponPriceLl.setOnClickListener(this);
        mTitleCommissionLl.setOnClickListener(this);
        mTitleSortEtcLl.setOnClickListener(this);
        mSmartRefreshLayout.setEnableNestedScroll(true);//是否启用嵌套滚动
        mSmartRefreshLayout.setEnableAutoLoadMore(false);//是否开启自动加载更多
        mSmartRefreshLayout.setEnableLoadMore(false);
        //是否在列表不满一页时候开启上拉加载功能
        mSmartRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                 // lastVisibleItem得到的是所有的item数，这里算上头1个和粘性布局1个
                mJumpTopIv.setVisibility(lastVisibleItem>12?View.VISIBLE:View.GONE);
                //预加载
                if (mLayoutManager.getItemCount() > 12&& !isloadingList && lastVisibleItem +12 >= mLayoutManager.getItemCount()) {
                    if (hasLoadMore){
                        LZLog.d("hasLoadMore", "onScrolled: onLoadMore="+start);
                        requestSortList(start);
                    }else {
                        setFootLoaderType(LoadType.NO_MORE_DATA);
                    }
                }
            }
        });
    }


    public void requestSortList(final long iStart) {
        isloadingList=true;
        start=iStart;
        HashMap<String, Long> map = new HashMap<>();
        map.put("orderByType", mOrderType);
        map.put("commodityKindId", tabId);
        map.put("start", iStart);
        map.put("limit", limit);
        map.put("desc", (long) eSortDirection);
        LZLog.i("hasLoadMore","start=="+iStart);
        ApiService.getHomeApi().requestOtherCommonList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<RecordsBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<RecordsBean>> commonListBeanBaseBean) {
                        super.onNext(commonListBeanBaseBean);
                        isloadingList=false;
                        if (commonListBeanBaseBean.success()) {
                            List<RecordsBean> result = commonListBeanBaseBean.getResult();

                            hasLoadMore = !ArraysUtils.isListEmpty(result) && result.size() == limit;
                            if (hasLoadMore) {
                                setFootLoaderType(LoadType.LOADING);
                            } else {
                                setFootLoaderType(LoadType.NO_MORE_DATA);
                            }
                            if (iStart <= 1) {
                                if (ArraysUtils.isListEmpty(result)){
                                    setFootLoaderType(LoadType.NO_DATA);
                                }
                                mSortAdapter.setListData(result);
                                mSortAdapter.notifyDataSetChanged();
                                mSmartRefreshLayout.finishRefresh();
                            } else {
                                mSortAdapter.appendListData(result);
                                //由于加载更多的动画 至少300毫秒，这里延迟刷新
                                mHandler.sendEmptyMessageDelayed(REFRESH_LIST_DATA_CODE,200);
                            }
                            start++;
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        isloadingList=false;
                        if (iStart <= 1) {
                            setFootLoaderType(LoadType.NO_DATA);
                            mSmartRefreshLayout.finishRefresh(false);
                        } else {
                            setFootLoaderType(LoadType.LOADING);
                        }
                    }
                });
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
        resetSortRequest();

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

    /**
     * isSingle  true 当前是单列 需要切换成双列
     */
    public void setGridRecyclerView() {
        boolean isSingle = mSortAdapter.type == ProductSortListAdapter.TYPE_SINGLE;
        mSortAdapter.setType(isSingle ? ProductSortListAdapter.TYPE_OTHER : ProductSortListAdapter.TYPE_SINGLE);
        mTitleSortEtcIv.setImageResource(isSingle ? R.drawable.sort_etc : R.drawable.sort_etc_out);

        mSortAdapter.notifyDataSetChanged();


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

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what==REFRESH_LIST_DATA_CODE){
            mSortAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }


    private void setFootLoaderType(@LoadType int type){
        if (mFooterView == null) {
            return;
        }
        switch (type) {
            default:
                break;
            case LoadType.LOADING:
                mLoadingLayout.setVisibility(View.VISIBLE);
                mNoMoreLayout.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.GONE);
                break;
            case LoadType.NO_DATA:
                mLoadingLayout.setVisibility(View.GONE);
                mNoMoreLayout.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.VISIBLE);
                break;
            case LoadType.NO_MORE_DATA:
                mLoadingLayout.setVisibility(View.GONE);
                mNoMoreLayout.setVisibility(View.VISIBLE);
                mNoDataLayout.setVisibility(View.GONE);
                break;
        }
    }
}
