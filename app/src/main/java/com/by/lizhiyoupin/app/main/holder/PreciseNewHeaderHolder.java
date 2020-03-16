package com.by.lizhiyoupin.app.main.holder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_banner.Banner;
import com.by.lizhiyoupin.app.component_banner.BannerConfig;
import com.by.lizhiyoupin.app.component_banner.Transformer;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.ArcRectView;
import com.by.lizhiyoupin.app.component_ui.weight.BannerImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfDelegateAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration2;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.LzTransformationUtil;
import com.by.lizhiyoupin.app.io.bean.HomeBannerBean;
import com.by.lizhiyoupin.app.io.bean.HomeIconBean;
import com.by.lizhiyoupin.app.io.bean.PreciseBannerIconBean;
import com.by.lizhiyoupin.app.io.bean.PreciseSelectionBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.adapter.PreciseRecommendImgAdapter;
import com.by.lizhiyoupin.app.main.adapter.PreciseRushBuyImgAdapter;
import com.by.lizhiyoupin.app.main.fragment.PreciseNewFragment;
import com.by.lizhiyoupin.app.main.fragment.TabFragmentHome;
import com.by.lizhiyoupin.app.main.weight.IconToolsLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/26 17:15
 * Summary: 精选 头
 */
public class PreciseNewHeaderHolder {
    public static final String TAG = PreciseNewHeaderHolder.class.getSimpleName();
    public static final int ITEM_TYPE_OF_BANNER = 301;
    public static final int ITEM_TYPE_OF_RECOMMEND_TOP = 302;
    public static final int ITEM_TYPE_OF_RECOMMEND = 303;
    public static final int ITEM_TYPE_OF_RUSH_BUY_TOP = 304;
    public static final int ITEM_TYPE_OF_RUSH_BUY_BOTTOM = 305;
    public static final int ITEM_TYPE_OF_BOTTOM_LIST = 306;
    public static final int ITEM_TYPE_OF_BOTTOM_ARTICLE_LIST = 307;
    public static final int ITEM_TYPE_OF_BOTTOM_VIDEO_LIST = 308;
    public static final int ITEM_TYPE_OF_BOTTOM_SHAKE_LIST = 309;
    public static final int ITEM_TYPE_OF_BOTTOM_RECOMMEND_LIST = 310;
    public static final int ITEM_TYPE_OF_BOTTOM_WANT_TO_BUY_LIST = 311;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private PreciseNewFragment mNewFragment;
    private SmartRefreshLayout mSmartRefreshLayout;
    private ArcRectView mAreRectView;//弧形
    private Banner mTopBanner;
    private RecyclerView recommendMoreRv;
    private IconToolsLayout mIconToolsLayout;
    private List<HomeBannerBean> mBannerList;
    private List<PreciseBannerIconBean> mRecommendList;

    private BaseOfDelegateAdapter mRecommendImgAdapter;
    private BaseOfDelegateAdapter mRushBuy2Adapter;
    private BaseOfDelegateAdapter mRushBuy4Adapter;
    private LimitSkillNewHeaderHolder limitSkillHeaderHolder;

    public View getOnCreateView(Context context, PreciseNewFragment fragment, RecyclerView recyclerView, SmartRefreshLayout smartRefreshLayout) {
        mContext = context;
        mRecyclerView = recyclerView;
        mNewFragment = fragment;
        mSmartRefreshLayout = smartRefreshLayout;
        View root = LayoutInflater.from(mContext).inflate(R.layout.precise_new_header_layout, recyclerView, false);
        initView(root);

        return root;
    }

    private void initView(View root) {
        initBannerAndIcon(root);
        initSelectionRecommendTopOneView(root);
        initSelectionRecommendView(root);
        initLimitSkillView(root);
        initSelectionRushBuyView2(root);
        initSelectionRushBuyView4(root);
    }


    public Banner getTopBanner() {
        return mTopBanner;
    }


    private void initBannerAndIcon(View inflate) {
        mTopBanner = inflate.findViewById(R.id.top_banner);
        mAreRectView = inflate.findViewById(R.id.arc_rectView);
        mIconToolsLayout = inflate.findViewById(R.id.iconToolsLayout);
        setBannerConfig();


    }

    private void setBannerConfig() {
        float margin = DeviceUtil.dip2px(mContext, 10);
        mTopBanner.setImageLoader(new BannerImageLoader())
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setOffscreenPageLimit(2)
                .setPageMargin(30)
                .setBannerStyle(BannerConfig.RECTANGLE_INDICATOR)
                .setViewPageMargin((int) margin, 0, (int) margin, 0)
                .setBannerAnimation(Transformer.Default)
                .setIndicatorGravity(BannerConfig.CENTER)
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        if (mNewFragment.getParentFragment() instanceof TabFragmentHome) {
                            TabFragmentHome parentFragment = (TabFragmentHome) mNewFragment.getParentFragment();
                            parentFragment.setAppBarLayoutColor(mBannerList.get(position).getColor());
                            mAreRectView.setEndColorInvalidate(mBannerList.get(position).getColor());
                        }

                    }
                })
                .start();
        mTopBanner.setOnBannerListener(position -> {
            //banner点击跳转
            LZLog.i(TAG, "setBannerConfig: click banner");
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            scheme.handleUrl(mContext, mBannerList.get(position).getLink());
        });

    }

    /**
     * 刷新banner数据
     *
     * @param bannerList
     */
    public void setTopBannerUpdate(List<HomeBannerBean> bannerList) {
        LZLog.i(TAG, "setTopBannerUpdate==");
        if (!ArraysUtils.isListEmpty(bannerList)) {
            mBannerList = bannerList;
            if (mTopBanner != null) {
                List<String> bannerPath = new ArrayList<>();
                for (HomeBannerBean bean : bannerList) {
                    bannerPath.add(bean.getImg());
                }
                mTopBanner.update(bannerPath);
            }
        }
    }


    //---------------------------------------------
    private View mTopRecommend;

    private void initSelectionRecommendTopOneView(View root) {
        //推荐一张大图
        mTopRecommend = root.findViewById(R.id.one_pic_recommend);

    }


    /**
     * 推荐顶部 大图
     *
     * @param beanList
     */
    public void setRecommendTopUpdate(List<PreciseBannerIconBean> beanList) {
        LZLog.i(TAG, "setRecommendTopUpdate==");
        if (!ArraysUtils.isListEmpty(beanList)) {
            if (mTopRecommend.getLayoutParams() == null) {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mTopRecommend.setLayoutParams(layoutParams);
            }
            mTopRecommend.getLayoutParams().height = DeviceUtil.dip2px(mContext, 108);
            PreciseBannerIconBean iconBean = (PreciseBannerIconBean) beanList.get(0);
            ImageView mTopIv = mTopRecommend.findViewById(R.id.top_iv);
            mTopIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ISchemeManager schemeManager = (ISchemeManager) ComponentManager.getInstance()
                            .getManager(ISchemeManager.class.getName());
                    if (schemeManager != null) {
                        if (iconBean != null) {
                            schemeManager.handleUrl(mContext, iconBean.getUrl());
                        }
                    }
                }
            });
            if (iconBean != null) {
                try {
                    new GlideImageLoader(mContext, iconBean.getImg())
                            .placeholder(R.drawable.empty_pic_list_h)
                            .error(R.drawable.empty_pic_list_h)
                            .into(mTopIv);
                    //对下面一个的背景设置
                    recommendMoreRv.setBackgroundColor(Color.parseColor(iconBean.getBannerColor()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            if (mTopRecommend != null) {
                if (mTopRecommend.getLayoutParams() == null) {
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mTopRecommend.setLayoutParams(layoutParams);
                }
                mTopRecommend.getLayoutParams().height = 0;
            }
        }
    }


    /**
     * 推荐 多张图
     */
    private void initSelectionRecommendView(View root) {
        recommendMoreRv = root.findViewById(R.id.recommend_more_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        recommendMoreRv.setLayoutManager(gridLayoutManager);
        recommendMoreRv.addItemDecoration(new SpaceItemDecoration2(DeviceUtil.dip2px(mContext, 5), 2));
        mRecommendImgAdapter = new PreciseRecommendImgAdapter(mContext, null, ITEM_TYPE_OF_RECOMMEND);
        recommendMoreRv.setAdapter(mRecommendImgAdapter);
    }


    /**
     * 限时秒杀
     */
    private void initLimitSkillView(View root) {
        limitSkillHeaderHolder = new LimitSkillNewHeaderHolder();
        limitSkillHeaderHolder.getOnCreateView(mContext, root);
    }

    /**
     * 限时抢购 抢购--2列
     */
    private void initSelectionRushBuyView2(View root) {
        RecyclerView rush2Rv = root.findViewById(R.id.rush2_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        rush2Rv.setLayoutManager(gridLayoutManager);
        rush2Rv.addItemDecoration(new SpaceItemDecoration2(DeviceUtil.dip2px(mContext, 1), 2));
        mRushBuy2Adapter = new PreciseRushBuyImgAdapter(mContext, null, ITEM_TYPE_OF_RUSH_BUY_TOP);
        rush2Rv.setAdapter(mRushBuy2Adapter);
    }

    /**
     * 限时抢购 领券--4列
     */
    private void initSelectionRushBuyView4(View root) {
        RecyclerView rush4Rv = root.findViewById(R.id.rush4_recyclerView);
        rush4Rv.setLayoutManager(new GridLayoutManager(mContext, 4));
        rush4Rv.addItemDecoration(new SpaceItemDecoration2(DeviceUtil.dip2px(mContext, 1), 4));
        mRushBuy4Adapter = new PreciseRushBuyImgAdapter(mContext, null, ITEM_TYPE_OF_RUSH_BUY_BOTTOM);
        rush4Rv.setAdapter(mRushBuy4Adapter);
    }


    /**
     * 刷新 icon
     *
     * @param iconList
     */
    public void setIconUpdate(List<HomeIconBean> iconList) {
        if (!ArraysUtils.isListEmpty(iconList)) {
            if (mIconToolsLayout != null) {
                mIconToolsLayout.initNetWorkIcon(iconList);
            }
        }
    }

    /**
     * 刷新 推荐模块图
     *
     * @param beanList
     */
    public void setRecommendUpdate(List<PreciseBannerIconBean> beanList) {
        LZLog.i(TAG, "setRecommendUpdate==");
        if (!ArraysUtils.isListEmpty(beanList) && mRecommendImgAdapter != null) {
            mRecommendList = beanList;
            mRecommendImgAdapter.setListData(mRecommendList);
            mRecommendImgAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新网格 抢购列表图  2列
     *
     * @param beanList
     */
    public void setTopRushBuyUpdate(List<PreciseBannerIconBean> beanList) {
        if (!ArraysUtils.isListEmpty(beanList)) {
            if (mRushBuy2Adapter != null) {
                mRushBuy2Adapter.setListData(beanList);
                mRushBuy2Adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 刷新橫向 领券列表图  4列
     *
     * @param beanList
     */
    public void setBottomRushBuyUpdate(List<PreciseBannerIconBean> beanList) {
        if (!ArraysUtils.isListEmpty(beanList)) {
            if (beanList.size() > 4) {
                beanList = beanList.subList(0, 4);
            }
            if (mRushBuy4Adapter != null) {
                mRushBuy4Adapter.setListData(beanList);
                mRushBuy4Adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 首页 显示秒杀
     */
    public void requestLimitKill() {
        if (limitSkillHeaderHolder != null) {
            limitSkillHeaderHolder.initsData();
        }
    }

    /**
     * 首页普通数据请求（非 好货严选）
     */
    public void requestMainData() {
        ApiService.getHomeApi().requestSelectionChannel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<PreciseSelectionBean>>() {
                    @Override
                    public void onNext(BaseBean<PreciseSelectionBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.data != null) {
                            mSmartRefreshLayout.finishRefresh();
                            PreciseSelectionBean bean = listBaseBean.data;
                            //banner
                            List<PreciseBannerIconBean> bannerList = bean.getBanner();
                            List<HomeBannerBean> banners = LzTransformationUtil.transformationBanner(bannerList);
                            setTopBannerUpdate(banners);
                            //icon
                            List<PreciseBannerIconBean> iconList = bean.getInner();
                            List<HomeIconBean> homeIconBeans = LzTransformationUtil.transformationIcon(iconList);
                            setIconUpdate(homeIconBeans);

                            //推荐 顶部图
                            List<PreciseBannerIconBean> topRecommendTop = bean.getRecommendActivityBanner();
                            setRecommendTopUpdate(topRecommendTop);
                            //推荐 列表
                            List<PreciseBannerIconBean> topRecommend = bean.getRecommendActivity();
                            setRecommendUpdate(topRecommend);

                            //抢购
                            List<PreciseBannerIconBean> topRushBuy = bean.getBuyActivity();
                            setTopRushBuyUpdate(topRushBuy);
                            //领券
                            List<PreciseBannerIconBean> bottomRushBuy = bean.getCouponActivity();
                            setBottomRushBuyUpdate(bottomRushBuy);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mSmartRefreshLayout.finishRefresh();
                    }
                });
    }

    public void screenStatusOn(){
        requestLimitKill();
    }
    public void screenStatusOff(){

    }
    public void onDestroyView() {
        if (limitSkillHeaderHolder != null) {
            limitSkillHeaderHolder.onDestroyView();
        }
    }
}
