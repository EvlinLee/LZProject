package com.by.lizhiyoupin.app.activity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.activity.adapter.ShakeCouponVideoAdapter;
import com.by.lizhiyoupin.app.activity.adapter.TikTokController2;
import com.by.lizhiyoupin.app.activity.constract.ShakeCouponMainConstract;
import com.by.lizhiyoupin.app.activity.presenter.ShakeCouponMainPresenter;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_video.tiktok.OnViewPagerListener;
import com.by.lizhiyoupin.app.component_video.tiktok.ViewPagerLayoutManager;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBuyBean;
import com.dueeeke.videoplayer.player.VideoView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/16 11:53
 * Summary: 抖券主页
 */
@Route(path = "/app/ShakeCouponMainRvActivity")
public class ShakeCouponMainRvActivity extends BaseMVPActivity<ShakeCouponMainConstract.ShakeCouponMainView, ShakeCouponMainConstract.ShakeCouponMainPresenters> implements ShakeCouponMainConstract.ShakeCouponMainView, View.OnClickListener {
    public static final String TAG = ShakeCouponMainRvActivity.class.getSimpleName();

    private ImageView mActionbarBack2Iv;
    private ShakeCouponBean mShakeCouponBean;
    private int mTabType;
    private long mThreeDetailID;
    private int mPage;//页数
    private int mFrom = 0;//0来自 三方数据，1来自三方+自己配置数据
    private VideoView mVideoView;
    private int mCurrentPosition;
    private RecyclerView mRecyclerView;
    // ShakeCouponVideoAdapter 与 TikTokController2 配合实现
    private ShakeCouponVideoAdapter mAdapter;
    private TikTokController2 mTikTokController;
    private List<ShakeCouponBean> mVideoList;


    @Override
    public ShakeCouponMainConstract.ShakeCouponMainPresenters getBasePresenter() {
        return new ShakeCouponMainPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_coupon_main_rv_layout);
        initImmersionBar(Color.TRANSPARENT, true);
        Intent intent = getIntent();
        mShakeCouponBean = (ShakeCouponBean) intent.getSerializableExtra(CommonConst.KEY_SHAKE_COUPON_VALUE);
        mTabType = intent.getIntExtra(CommonConst.KEY_INDICATOR_TYPE, 0);
        mPage = intent.getIntExtra(CommonConst.KEY_SHAKE_COUPON_PAGE, 1);
        mFrom = intent.getIntExtra(CommonConst.KEY_SHAKE_COUPON_FROM, 0);
        mThreeDetailID = intent.getLongExtra(CommonConst.KEY_THREE_DETAIL_ID, 0);
        if (mShakeCouponBean == null) {
            mShakeCouponBean = new ShakeCouponBean();
        }
        mVideoList = new ArrayList<>();
        mVideoList.add(0, mShakeCouponBean);
        mRecyclerView = findViewById(R.id.recyclerView);
        mActionbarBack2Iv = findViewById(R.id.actionbar_back2_tv);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mActionbarBack2Iv.getLayoutParams();
        layoutParams.setMargins(0, DeviceUtil.getStatusBarHeight(this), 0, 0);
        mActionbarBack2Iv.setLayoutParams(layoutParams);
        mActionbarBack2Iv.setOnClickListener(this);
        initVideoView();
        loadData();
    }


    private void loadData() {
        // basePresenter.requestShakeCouponMainList2(mTabType, mPage, 10, mThreeDetailID);
        basePresenter.requestPreciseShakeCouponList(mTabType, mPage, 10, mThreeDetailID, mFrom);
        basePresenter.requestShakeCouponBarrageList();
    }

    private void initVideoView() {

        mVideoView = new VideoView(this);
        ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mVideoView.setLayoutParams(layoutParams);
        mVideoView.setLooping(true);
        mTikTokController = new TikTokController2(this);
        mVideoView.setVideoController(mTikTokController);

        mAdapter = new ShakeCouponVideoAdapter(this);

        ViewPagerLayoutManager layoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        layoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                //自动播放第一条
                startPlay(0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                if (mCurrentPosition == position) {
                    mVideoView.release();
                    mTikTokController.stopBarrageScroll();
                }
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                if (mCurrentPosition == position) return;
                mCurrentPosition = position;
                mShakeCouponBean = mVideoList.get(position);
                startPlay(position);
                basePresenter.requestShakeCouponBarrageList();
                if (isBottom) {
                    //滑到底部了，需要请求刷新数据
                    basePresenter.requestShakeCouponMainList2(mTabType, mPage = mPage + 1, 10, mThreeDetailID);
                }
            }
        });

    }

    private void startPlay(int position) {
        View itemView = mRecyclerView.getChildAt(0);
        FrameLayout frameLayout = itemView.findViewById(R.id.container);
        ImageView thumb = mTikTokController.findViewById(R.id.iv_thumb);
        new GlideImageLoader(this, mShakeCouponBean.getDyFirstFrame())
                .placeholder(android.R.color.white)
                .into(thumb);
        ViewParent parent = mVideoView.getParent();
        //共用一个player通过布局添加删除控制
        if (parent instanceof FrameLayout) {
            ((FrameLayout) parent).removeView(mVideoView);
        }
        frameLayout.addView(mVideoView);
        mVideoView.setUrl(mVideoList.get(position).getDyVideoUrl());
        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
        //播放
        mTikTokController.refreshProductView(mVideoList.get(position));
        mVideoView.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back2_tv:
                finish();
                break;

        }
    }


    @Override
    public void requestShakeCouponMainSuccess(List<ShakeCouponBean> beanList) {
        LZLog.i(TAG, "requestShakeCouponMainSuccess " + beanList.size());
        if (mVideoList.size() >= 100 && beanList.size() > 0) {
            mVideoList = mVideoList.subList(50, 101);
            mVideoList.addAll(beanList);
            mAdapter.setListData(mVideoList);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(51);
        } else {
            mVideoList.addAll(beanList);
            mAdapter.setListData(mVideoList);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void requestShakeCouponMainError(Throwable throwable) {
        LZLog.i(TAG, "requestShakeCouponMainError==" + throwable);
    }

    @Override
    public void requestShakeCouponBarrageListSuccess(List<ShakeCouponBuyBean> beanList) {
        LZLog.i(TAG, "requestShakeCouponBarrageListSuccess " + beanList.size());
        //弹幕
        if (mTikTokController != null) {
            mTikTokController.setBarrageList(beanList);
        }
    }

    @Override
    public void requestShakeCouponBarrageListError(Throwable throwable) {
        LZLog.i(TAG, "requestShakeCouponBarrageListError==" + throwable);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTikTokController.stopBarrageScroll();
        mVideoView.release();

    }

}
