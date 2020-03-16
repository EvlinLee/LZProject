package com.by.lizhiyoupin.app.main.weight;

import android.content.Context;
import android.util.AttributeSet;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration2;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.GuideArticleBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.LoadType;
import com.by.lizhiyoupin.app.main.adapter.PreciseWantToBuyAdapter;
import com.by.lizhiyoupin.app.main.holder.PreciseNewHeaderHolder;
import com.by.lizhiyoupin.app.weight.rv.ChildRv;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/26 20:01
 * Summary: 种草  RecyclerView
 */
public class WantToBuyChildRvImpl extends ChildRv {
    public static final String TAG = WantToBuyChildRvImpl.class.getSimpleName();
    private PreciseWantToBuyAdapter mAdapter2;
    private Context mContext;
    private int start = 1;
    private int limit = 20;
    private Disposable mDisposable;
    private boolean hasMore;
    private boolean isloadingList;

    public WantToBuyChildRvImpl(@NonNull Context context) {
        this(context, null);
    }

    public WantToBuyChildRvImpl(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WantToBuyChildRvImpl(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
       /* gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return gridLayoutManager.getItemCount() - 1 == position ? 2 : 1;
            }
        });*/
        addItemDecoration(new SpaceItemDecoration2(DeviceUtil.dip2px(mContext, 5), 1));
        setLayoutManager(gridLayoutManager);
        mAdapter2 = new PreciseWantToBuyAdapter(mContext, null, PreciseNewHeaderHolder.ITEM_TYPE_OF_BOTTOM_WANT_TO_BUY_LIST);
        mAdapter2.setFooterView(getFooterView(mContext));
        setAdapter(mAdapter2);
        requestList03(start, limit);
    }


    /**
     * 重新刷新 请求
     */
    public void resetRequest() {
        start = 1;
        limit = 20;
        requestList03(1, 20);
    }

    /**
     * 请求种草
     *
     * @param iStart
     * @param iLimit
     */
    private void requestList03(int iStart, int iLimit) {
        ApiService.getHomeApi().requestGetGuideArticleList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<GuideArticleBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<GuideArticleBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        isloadingList = false;

                        if (listBaseBean.success()) {
                            LZLog.i(TAG, "requestGetGuideArticleList==success");
                            List<GuideArticleBean> result = listBaseBean.getResult();
                            if (iStart <= 1) {
                                mAdapter2.setListData(result);
                                mAdapter2.notifyDataSetChanged();
                            } else {
                                int oldSize=mAdapter2.getListData().size();
                                mAdapter2.appendListData(result);
                                mAdapter2.notifyItemRangeChanged(oldSize,result.size());
                            }
                        }
                        setFootLoaderType(LoadType.NO_MORE_DATA);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        isloadingList = false;
                        setFootLoaderType(LoadType.NO_DATA);
                    }
                });
    }

}
