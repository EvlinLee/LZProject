package com.by.lizhiyoupin.app.main.findcircle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_banner.Banner;
import com.by.lizhiyoupin.app.component_banner.BannerConfig;
import com.by.lizhiyoupin.app.component_banner.Transformer;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.BannerImageLoader;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;
import com.by.lizhiyoupin.app.io.bean.HomeBannerBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.main.findcircle.adapter.BusinessSchoolAdapter;
import com.by.lizhiyoupin.app.main.findcircle.contract.BusinessContract;
import com.by.lizhiyoupin.app.main.findcircle.presenter.BusinessPresenter;
import com.by.lizhiyoupin.app.main.fragment.TabFragmentFind;
import com.by.lizhiyoupin.app.main.weight.BusinessSchoolScrollLayout;
import com.by.lizhiyoupin.app.main.weight.ToolsBusinessLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/26 21:05
 * Summary: 商学院 内容页
 */

public class FindCircleBusinessSchoolFragment extends BaseFragment implements BusinessContract.BusinessView {
    public static final String TAG = FindCircleBusinessSchoolFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private BusinessSchoolAdapter mMultipleItemQuickAdapter;
    private BusinessContract.BusinessPresenters mBusinessPresenters;
    private LinearLayoutManager mLinearLayoutManager;
    private int currentPage = 1;
    private boolean hasLoadMore;
    private Context mContext;
    private List<HomeBannerBean> mBannerBeanList;
    private List<BusinessIconBean> mIconList;
    private Banner mConsultantBanner;
    private BusinessSchoolScrollLayout mScrollLayout;
    private ToolsBusinessLayout mBusinessLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_find_business_school_layout, container, false);
        initView(inflate);
        mBusinessPresenters = new BusinessPresenter(this);
        refreshData();
        return inflate;
    }

    private void initView(View root) {

        mRecyclerView = root.findViewById(R.id.recyclerView);
        smartRefreshLayout = root.findViewById(R.id.smartRefreshLayout);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMultipleItemQuickAdapter = new BusinessSchoolAdapter(mContext);
        mRecyclerView.setAdapter(mMultipleItemQuickAdapter);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Fragment parentFragment = getParentFragment();
                if (parentFragment instanceof TabFragmentFind) {
                    boolean initLoadingTabSuccess = ((TabFragmentFind) parentFragment).getInitLoadingTabSuccess();
                    if (!initLoadingTabSuccess) {
                        ((TabFragmentFind) parentFragment).requestFirstLevelList();
                    } else {
                        refreshData();
                    }
                } else {
                    refreshData();
                }
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (hasLoadMore) {
                    requestBottomList(currentPage + 1);
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
        });

        initHeaderView();
    }

    private void initHeaderView() {
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.item_business_header_banner_layout, null, false);
        mMultipleItemQuickAdapter.setHeaderView(headerView);
        //banner 图
        initBannerList(headerView);
        //icon 列表
        initIIconList(headerView);
        //滚动资讯
        initScrollList(headerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                if (lastVisibleItem != 0) {
                    Log.i(TAG, "onScrollStateChanged: "+"不可見");
                    mScrollLayout.stopScroll();
                } else {
                    mScrollLayout.startScroll();
                }
            }
        });
    }

    private void initBannerList(View headerView) {
        //banner 图
        mConsultantBanner = headerView.findViewById(R.id.banner);
        headerView.findViewById(R.id.search_fl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //商学院 搜索
                CommonSchemeJump.showBusinessSearchActivity(mContext);
            }
        });
        int margin = DeviceUtil.dip2px(mContext, 10);
        mConsultantBanner.setImageLoader(new BannerImageLoader())
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setOffscreenPageLimit(2)
                .setIndicatorGravity(BannerConfig.CENTER)
                .setBannerStyle(BannerConfig.RECTANGLE_INDICATOR)
                .setPageMargin(30)
                .setViewPageMargin(margin, 0, margin, 0)
                .setBannerAnimation(Transformer.ZoomOutSlide)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();

        mConsultantBanner.setOnBannerListener(position -> {
            //banner点击跳转
            LZLog.i(TAG, "点击banner");
            HomeBannerBean bannerBean = mBannerBeanList.get(position);
            if (bannerBean != null) {
                final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                scheme.handleUrl(mContext, bannerBean.getLink());
            }
        });
    }

    private void initIIconList(View headerView) {
        //icon 列表
        mBusinessLayout = headerView.findViewById(R.id.toolsLayout);


    }

    private void initScrollList(View headerView) {
        //滚动资讯
        mScrollLayout = headerView.findViewById(R.id.scroll_lt);
        headerView.findViewById(R.id.scroll_root_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //到资讯列表
                CommonSchemeJump.showBusinessInformationActivity(mContext);
            }
        });

    }

    private void refreshData() {
        mBusinessPresenters.requestBusinessBannerList();
        mBusinessPresenters.requestBusinessIconList();
        mBusinessPresenters.requestBusinessScrollArticleList(1, 10);
        requestBottomList(1);
    }

    /**
     * 请求刷新底部文章列表
     *
     * @param page
     */
    private void requestBottomList(int page) {
        currentPage = page;
        LZLog.i(TAG, "requestBusinessGuessList==" + page);
        mBusinessPresenters.requestBusinessGuessList(page, 10);
    }

    @Override
    public void requestBusinessBannerListSuccess(List<HomeBannerBean> bannerList) {
        LZLog.i(TAG, "requestBusinessBannerListSuccess==");
        if (bannerList != null) {
            mBannerBeanList = bannerList;
            List<String> bannerPath = new ArrayList<>();
            for (HomeBannerBean bean : bannerList) {
                bannerPath.add(bean.getImg());
            }
            mConsultantBanner.update(bannerPath);
        }
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void requestBusinessBannerListError(Throwable throwable) {
        LZLog.i(TAG, "requestBusinessBannerListError==" + throwable);
        smartRefreshLayout.finishRefresh();

    }

    @Override
    public void requestBusinessIconListSuccess(List<BusinessIconBean> beanList) {
        LZLog.i(TAG, "requestBusinessIconListSuccess==" + beanList.size());
        mIconList = beanList;
        mBusinessLayout.initNetWorkIcon(mIconList);
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void requestBusinessIconListError(Throwable throwable) {
        LZLog.i(TAG, "requestBusinessIconListError==" + throwable);
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void requestBusinessScrollArticleListSuccess(List<BusinessArticleBean> beanList) {
        LZLog.i(TAG, "requestBusinessScrollArticleListSuccess==" + beanList.size());
        //滚动资讯
        mScrollLayout.updateView(beanList);
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void requestBusinessScrollArticleListError(Throwable throwable) {
        LZLog.i(TAG, "requestBusinessScrollArticleListError==" + throwable);
        smartRefreshLayout.finishRefresh();
    }


    @Override
    public void requestBusinessGuessListSuccess(List<BusinessArticleBean> result, int page) {
        LZLog.i(TAG, "requestBusinessGuessList success" + page);

        hasLoadMore = !ArraysUtils.isListEmpty(result) && result.size() == 10;
        if (currentPage > 1 && !ArraysUtils.isListEmpty(result)) {
            //加载更多的数据
            mMultipleItemQuickAdapter.getListData().addAll(result);
            mMultipleItemQuickAdapter.notifyDataSetChanged();
        } else {
            //加载第一页
            mMultipleItemQuickAdapter.setListData(result);
            mMultipleItemQuickAdapter.notifyDataSetChanged();
        }


        if (hasLoadMore) {
            if (page == 1) {
                smartRefreshLayout.finishRefresh();
            } else {
                smartRefreshLayout.finishLoadMore();
            }
        } else {
            smartRefreshLayout.setNoMoreData(true);
        }
    }

    @Override
    public void requestBusinessGuessListError(Throwable throwable, int page) {
        if (page != 1) {
            smartRefreshLayout.finishLoadMore();
        } else {
            smartRefreshLayout.finishRefresh();
        }

    }

}
