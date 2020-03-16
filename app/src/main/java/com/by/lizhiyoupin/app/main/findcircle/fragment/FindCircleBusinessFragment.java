package com.by.lizhiyoupin.app.main.findcircle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_banner.Banner;
import com.by.lizhiyoupin.app.component_banner.BannerConfig;
import com.by.lizhiyoupin.app.component_banner.Transformer;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.BannerImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CannotScrollRecyclerView;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpeedLinearLayoutManager;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;
import com.by.lizhiyoupin.app.io.bean.HomeBannerBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.findcircle.adapter.BusinessArticleScrollAdapter;
import com.by.lizhiyoupin.app.main.findcircle.adapter.BusinessConsultationAdapter;
import com.by.lizhiyoupin.app.main.findcircle.contract.BusinessContract;
import com.by.lizhiyoupin.app.main.findcircle.presenter.BusinessPresenter;
import com.by.lizhiyoupin.app.main.weight.ToolsBusinessLayout;

import java.util.ArrayList;
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
 * @date :  2019/11/19 15:57
 * Summary: 发圈--商学院
 * CoordinatorLayout 滑动错误 位置出现错误 所以弃用
 */
@Deprecated
public class FindCircleBusinessFragment extends BaseFragment implements View.OnClickListener, BusinessContract.BusinessView, Handler.Callback {
    public static final String TAG = FindCircleBusinessFragment.class.getSimpleName();
    public static final int SCROLL_ARTICLE_CODE = 601;
    private Context mContext;

    private ToolsBusinessLayout mToolsBusinessLayout;
    private Banner mBanner;
    private TextView mConsultationTv;
    private View mEmptyLayout;
    private View mFailRetry;
    private View mloadingLayout;
    private LoadMoreHelperRx<BusinessArticleBean, Integer> mLoadMoreHelper;
    private CannotScrollRecyclerView mArticleRecyclerView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private BusinessArticleScrollAdapter mArticleScrollAdapter;
    private BusinessConsultationAdapter mBusinessAdapter;
    private BusinessContract.BusinessPresenters mBusinessPresenters;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private Handler mHandler;
    private int currentScrollPosition = 1;
    private boolean hasLoadMore;
    private List<HomeBannerBean> mBannerList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_find_business_layout, container, false);
        mBusinessPresenters = new BusinessPresenter(this);
        mHandler = new Handler(Looper.getMainLooper(), this);
        initView(inflate);
        loadRecyclerView();
        refreshData();
        return inflate;
    }

    private void refreshData() {
        mBusinessPresenters.requestBusinessBannerList();
        mBusinessPresenters.requestBusinessIconList();
        mBusinessPresenters.requestBusinessScrollArticleList(0, 10);
        mLoadMoreHelper.loadData();
    }

    private void initView(View root) {

        mBanner = root.findViewById(R.id.banner);
        mToolsBusinessLayout = root.findViewById(R.id.toolsLayout);
        mConsultationTv = root.findViewById(R.id.consultation_tv);
        mArticleRecyclerView = root.findViewById(R.id.article_recyclerView);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mEmptyLayout = root.findViewById(R.id.empty_layout);
        mFailRetry = root.findViewById(R.id.fail_retry);
        mloadingLayout = root.findViewById(R.id.loading_layout);
        root.findViewById(R.id.search_fl).setOnClickListener(this);

        mArticleRecyclerView.setLayoutManager(new SpeedLinearLayoutManager(getContext()));
        mArticleScrollAdapter = new BusinessArticleScrollAdapter(getContext());
        mArticleRecyclerView.setNestedScrollingEnabled(false);
        mArticleRecyclerView.setClickable(false);
        mArticleRecyclerView.setAdapter(mArticleScrollAdapter);

        mConsultationTv.setOnClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBusinessAdapter = new BusinessConsultationAdapter(mContext);
        mRecyclerView.setAdapter(mBusinessAdapter);
        setBannerConfig();
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<BusinessArticleBean, Integer>(mContext)
                .adapter(mBusinessAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyLayout)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<BusinessArticleBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<BusinessArticleBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "requestBusinessGuessList load");

                        return ApiService.getFindCircleApi().requestBusinessGuessList(integer, pageSize)
                                .map(new Function<BaseBean<List<BusinessArticleBean>>, Collection<BusinessArticleBean>>() {
                                    @Override
                                    public Collection<BusinessArticleBean> apply(BaseBean<List<BusinessArticleBean>> listBaseBean) throws Exception {
                                        if (listBaseBean.success()) {
                                            LZLog.i(TAG, "requestBusinessGuessList success");
                                            List<BusinessArticleBean> result = listBaseBean.getResult();
                                            hasLoadMore = !ArraysUtils.isListEmpty(result) && result.size() == pageSize;
                                            return result;
                                        }
                                        throw new Exception(listBaseBean.msg);
                                    }
                                });
                    }
                }).build();
        mScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    LZLog.i(TAG, "onLoadMore requestBusinessGuessList");
                    mLoadMoreHelper.loadDataMore();
                }
            }
        };

        mRecyclerView.addOnScrollListener(mScrollListener);

    }

    private void setBannerConfig() {
        float margin = DeviceUtil.dip2px(mContext, 10);
        mBanner.setImageLoader(new BannerImageLoader())
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setOffscreenPageLimit(2)
                .setIndicatorGravity(BannerConfig.CENTER)
                .setBannerStyle(BannerConfig.RECTANGLE_INDICATOR)
                .setPageMargin(30)
                .setViewPageMargin((int) margin, 0, (int) margin, 0)
                .setBannerAnimation(Transformer.ZoomOutSlide)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();

        mBanner.setOnBannerListener(position -> {
            //banner点击跳转
            LZLog.i(TAG, "点击banner");
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            scheme.handleUrl(mContext, mBannerList.get(position).getLink());

        });

    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.consultation_tv:
                //到资讯列表
                CommonSchemeJump.showBusinessInformationActivity(mContext);
                break;
            case R.id.search_fl:
                CommonSchemeJump.showBusinessSearchActivity(mContext);
                break;
        }
    }

    @Override
    public void requestBusinessScrollArticleListSuccess(List<BusinessArticleBean> beanList) {
        LZLog.i(TAG, "requestBusinessScrollArticleListSuccess==" + beanList.size());
        //滚动资讯

        mArticleScrollAdapter.setListData(beanList);
        mArticleScrollAdapter.notifyDataSetChanged();
        mHandler.sendEmptyMessageDelayed(SCROLL_ARTICLE_CODE, 3000);
    }

    @Override
    public void requestBusinessScrollArticleListError(Throwable throwable) {
        LZLog.i(TAG, "requestBusinessScrollArticleListError==" + throwable);
    }

    @Override
    public void requestBusinessGuessListSuccess(List<BusinessArticleBean> beanList, int page) {

    }

    @Override
    public void requestBusinessGuessListError(Throwable throwable, int page) {

    }

    @Override
    public void requestBusinessBannerListSuccess(List<HomeBannerBean> bannerList) {
        LZLog.i(TAG, "requestBusinessBannerListSuccess==" + bannerList.size());
        if (!ArraysUtils.isListEmpty(bannerList)) {
            mBannerList = bannerList;
            if (mBanner != null) {
                List<String> bannerPath = new ArrayList<>();
                for (HomeBannerBean bean : bannerList) {
                    bannerPath.add(bean.getImg());
                }
                mBanner.update(bannerPath);
            }
        }

    }

    @Override
    public void requestBusinessBannerListError(Throwable throwable) {
        LZLog.i(TAG, "requestBusinessBannerListError==" + throwable);

    }

    @Override
    public void requestBusinessIconListSuccess(List<BusinessIconBean> beanList) {
        LZLog.i(TAG, "requestBusinessIconListSuccess==" + beanList.size());
        mToolsBusinessLayout.initNetWorkIcon(beanList);
    }

    @Override
    public void requestBusinessIconListError(Throwable throwable) {
        LZLog.i(TAG, "requestBusinessIconListError==" + throwable);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (SCROLL_ARTICLE_CODE == msg.what) {
            mArticleRecyclerView.smoothScrollToPosition(currentScrollPosition);
            currentScrollPosition++;
            mHandler.sendEmptyMessageDelayed(SCROLL_ARTICLE_CODE, 3000);
            return true;
        }
        return false;
    }
}
