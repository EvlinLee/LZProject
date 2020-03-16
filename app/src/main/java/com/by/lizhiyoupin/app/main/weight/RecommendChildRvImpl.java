package com.by.lizhiyoupin.app.main.weight;

import android.content.Context;
import android.util.AttributeSet;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration2;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.LoadType;
import com.by.lizhiyoupin.app.main.adapter.PreciseSelectionDarenRecommendListAdapter;
import com.by.lizhiyoupin.app.main.holder.PreciseNewHeaderHolder;
import com.by.lizhiyoupin.app.weight.rv.ChildRv;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/26 20:01
 * Summary: 达人推荐 RecyclerView
 */
public class RecommendChildRvImpl extends ChildRv   {
    public static final String TAG=RecommendChildRvImpl.class.getSimpleName();
    private PreciseSelectionDarenRecommendListAdapter mAdapter2;
    private Context mContext;
    private int start = 1;
    private int limit = 20;
    private Disposable mDisposable;
    private boolean hasMore;
    private boolean isloadingList;

    public RecommendChildRvImpl(@NonNull Context context) {
        this(context,null);
    }

    public RecommendChildRvImpl(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecommendChildRvImpl(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext=context;
        initRecyclerView();
    }
    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return gridLayoutManager.getItemCount() - 1 == position ? 2 : 1;
            }
        });
        addItemDecoration(new SpaceItemDecoration2(DeviceUtil.dip2px(mContext,5),2));
        setLayoutManager(gridLayoutManager);
        mAdapter2 = new PreciseSelectionDarenRecommendListAdapter(mContext,null, PreciseNewHeaderHolder.ITEM_TYPE_OF_BOTTOM_RECOMMEND_LIST);
        mAdapter2.setFooterView(getFooterView(mContext));
        setAdapter(mAdapter2);
        requestList01(start,limit);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                //预加载
                int itemCount = gridLayoutManager.getItemCount();
                if (itemCount > 10 && !isloadingList && lastVisibleItem + 10 >= itemCount) {
                    if (hasMore) {
                        isloadingList=true;
                        LZLog.i(TAG, "onLoadMore: 达人推荐 加载更多" + start);
                        requestList01(start, limit);
                    }
                }
            }
        });

    }



    /**
     * 重新刷新 请求
     */
    public void resetRequest(){
        start=1;
        limit=20;
        requestList01(1,20);
    }

    private void requestList01(int iStart, int iLimit) {
        //达人推荐
        ApiService.getHomeApi().requestPreciseDarenRecommendList(iStart, iLimit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<PreciseListBean>>>() {


                    @Override
                    public void onNext(BaseBean<List<PreciseListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        isloadingList = false;
                        if (listBaseBean.success()) {
                            LZLog.i(TAG, "requestPreciseDarenRecommendList==success,iStart=" + iStart);
                            List<PreciseListBean> result = listBaseBean.getResult();
                            hasMore = !ArraysUtils.isListEmpty(result);
                            if (hasMore) {
                                setFootLoaderType(LoadType.LOADING);
                            } else {
                                if (iStart <= 1) {
                                    setFootLoaderType(LoadType.NO_DATA);
                                } else {
                                    setFootLoaderType(LoadType.NO_MORE_DATA);
                                }
                            }
                            if (iStart <= 1) {
                                mAdapter2.setListData(result);
                                mAdapter2.notifyDataSetChanged();
                            } else {

                                int oldSize=mAdapter2.getListData().size();
                                mAdapter2.appendListData(result);
                                mAdapter2.notifyItemRangeChanged(oldSize,result.size());

                            }
                            start++;
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        isloadingList = false;
                        if (iStart <= 1) {
                            setFootLoaderType(LoadType.NO_DATA);
                        } else {
                            setFootLoaderType(LoadType.LOADING);
                        }
                    }
                });
    }


}
