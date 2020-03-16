package com.by.lizhiyoupin.app.main.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_banner.Banner;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.entity.ActivitySwitchEntity;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.adapter.preciseadapter.PreciseNewAdapter;
import com.by.lizhiyoupin.app.main.holder.PreciseNewHeaderHolder;
import com.by.lizhiyoupin.app.main.weight.FineSelectChildRvImpl;
import com.by.lizhiyoupin.app.main.weight.RecommendChildRvImpl;
import com.by.lizhiyoupin.app.main.weight.ShakeCouponChildRvImpl;
import com.by.lizhiyoupin.app.main.weight.WantToBuyChildRvImpl;
import com.by.lizhiyoupin.app.sign.activity.SignInActivity;
import com.by.lizhiyoupin.app.weight.rv.ChildRv;
import com.by.lizhiyoupin.app.weight.rv.ParentRV;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/26 17:02
 * Summary: 新版 精选
 */
public class PreciseNewFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = PreciseNewFragment.class.getSimpleName();


    private ParentRV mRecyclerView;
    private ImageView mJumpTopIv;//置顶按钮显示隐藏 与外部RV是否滑动相关联 ParentRv的canScrollVertically
    private SmartRefreshLayout mSmartRefreshLayout;
    private Context mContext;
    private Banner mTopBanner;
    private LinearLayoutManager outlinearLayoutManager;
    List<ChildRv> mChildRVimplList;
    PreciseNewHeaderHolder preciseNewHeaderHolder;
    private ScreenStatusReceiver mScreenStatusReceiver;
    private ImageView mJumpSignInIv;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LZLog.i(TAG, "onAttach");
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_precise_new_layout, container, false);
        initView(inflate);
        registerScreenStatusReceiver();
        showSignInEntro();
        return inflate;
    }


    @Override
    public void onFirstUserInvisible() {
        super.onFirstUserInvisible();
        LZLog.i(TAG, "onFirstUserInvisible==");
        if (mTopBanner != null) {
            mTopBanner.stopAutoPlay();
            LZLog.i(TAG, "stopAutoPlay==");
        }
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        LZLog.i(TAG, "onFirstUserVisible==");
        if (mTopBanner != null) {
            mTopBanner.startAutoPlay();
            LZLog.i(TAG, "startAutoPlay==");
        }
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        LZLog.i(TAG, "onUserVisible==");
        if (getParentFragment() instanceof TabFragmentHome) {
            TabFragmentHome parentFragment = (TabFragmentHome) getParentFragment();
            int currentPager = parentFragment.getCurrentPager();
            if (currentPager == 0) {
                mTopBanner.startAutoPlay();
            }
        }

    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        LZLog.i(TAG, "onUserInvisible==");
        if (mTopBanner != null) {
            mTopBanner.stopAutoPlay();
        }
    }

    /**
     * 是否可以开启下拉刷新
     *
     * @param enable
     */
    public void setEnableRefresh(boolean enable) {
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.setEnableRefresh(enable);
        }
    }

    /**
     * 设置是否可加载更多
     *
     * @param canLoadMore
     */
    private void startEnableLoadMore(boolean canLoadMore) {
        mSmartRefreshLayout.setEnableLoadMore(canLoadMore);
        mSmartRefreshLayout.setEnableAutoLoadMore(canLoadMore);
    }

    private void setSmartRefreshLayout() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                resetRequestList();
            }
        });
    }


    private void initView(View root) {
        mSmartRefreshLayout = root.findViewById(R.id.smartRefreshLayout);
        startEnableLoadMore(false);
        setSmartRefreshLayout();
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mJumpTopIv = root.findViewById(R.id.jump_top_iv);
        mJumpTopIv.setOnClickListener(this);
        mJumpSignInIv = root.findViewById(R.id.jump_sign_in_iv);
        mJumpSignInIv.setOnClickListener(this);

        outlinearLayoutManager = mRecyclerView.initLayoutManager();


        mChildRVimplList = new ArrayList<>();

        mChildRVimplList.add(new FineSelectChildRvImpl(mContext));
        mChildRVimplList.add(new RecommendChildRvImpl(mContext));
        mChildRVimplList.add(new ShakeCouponChildRvImpl(mContext));
        mChildRVimplList.add(new WantToBuyChildRvImpl(mContext));
        mRecyclerView.setJump2Top(mJumpTopIv);
        //精选adapter，目前试一个头+内部条目viewpager
        PreciseNewAdapter preciseNewAdapter = new PreciseNewAdapter(mContext, mChildRVimplList);
        //头 数据
        preciseNewHeaderHolder = new PreciseNewHeaderHolder();
        preciseNewAdapter.setHeaderView(preciseNewHeaderHolder.getOnCreateView(mContext, this, mRecyclerView, mSmartRefreshLayout));

        mRecyclerView.setAdapter(preciseNewAdapter);
        List<String> list = new ArrayList<>();
        list.add("就一个子tab 用来装 viewpager");
        preciseNewAdapter.setListData(list);
        mTopBanner = preciseNewHeaderHolder.getTopBanner();

        //外部rv滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstVisibleItem = outlinearLayoutManager.findFirstVisibleItemPosition();
                mJumpTopIv.setVisibility(firstVisibleItem != 0 ? View.VISIBLE : View.GONE);
                setEnableRefresh(firstVisibleItem == 0);
                //外部RV滑动监听，是第一条目时且手动拖动，则内部rv都滑动到第一个位置，当不是第一条目时，滑动都交给了内部RV，
                // 所以下面的只会在外部与内部RV滑动交替时执行一次，不会一直执行（即内部rv即将滑动时会先走下面的）
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && firstVisibleItem != 0) {
                    //当内部RV不可滑动时即不置顶时，内部所有RV滑动到第一个位置
                    LZLog.i(TAG, "onScrollStateChanged: child rv scroll to---0");
                    for (int i = 0; i < mChildRVimplList.size(); i++) {
                        LinearLayoutManager gridLayoutManager = (LinearLayoutManager) mChildRVimplList.get(i).getLayoutManager();
                        if (gridLayoutManager != null) {
                            gridLayoutManager.scrollToPositionWithOffset(0, 0);
                        }
                    }
                }
            }
        });
        //请求
        preciseNewHeaderHolder.requestMainData();
    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.jump_top_iv:
                //置顶
                for (int i = 0; i < mChildRVimplList.size(); i++) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mChildRVimplList.get(i).getLayoutManager();
                    if (layoutManager != null) {
                        layoutManager.scrollToPositionWithOffset(0, 0);
                    }
                }
                outlinearLayoutManager.scrollToPositionWithOffset(0, 0);
                mJumpTopIv.setVisibility(View.GONE);
                break;
            case R.id.jump_sign_in_iv:
                //签到
                if (LiZhiApplication.getApplication().getAccountManager().isLogined()){
                    startActivity(new Intent(mContext, SignInActivity.class));
                }else {
                    CommonSchemeJump.showLoginActivity(mContext);
                }
                break;
        }
    }


    /**
     * 重置首页请求
     **/
    private void resetRequestList() {
        LZLog.i(TAG, "resetRequestList");
        if (getParentFragment() instanceof TabFragmentHome) {
            boolean initTabHomeFinish = ((TabFragmentHome) getParentFragment()).getInitTabHomeFinish();
            if (!initTabHomeFinish) {
                //tab 栏请求失败，这拉下刷新 球球
                ((TabFragmentHome) getParentFragment()).initData();
            }
        }
        //请求 顶部其他数据
        preciseNewHeaderHolder.requestMainData();
        //限时秒杀
        preciseNewHeaderHolder.requestLimitKill();
        //底部列表
        for (int i = 0; i < mChildRVimplList.size(); i++) {
            mChildRVimplList.get(i).resetRequest();
        }
        showSignInEntro();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (preciseNewHeaderHolder != null) {
            preciseNewHeaderHolder.onDestroyView();
        }
        unregisterScreenStatusReceiver();
    }

    private void registerScreenStatusReceiver() {
        // 需要使用动态注册才能接收到广播
        if (mScreenStatusReceiver == null) {
            mScreenStatusReceiver = new ScreenStatusReceiver();
            IntentFilter screenStatusIF = new IntentFilter();
            screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
            screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
            //screenStatusIF.addAction(Intent.ACTION_USER_PRESENT);//解锁
            mContext.registerReceiver(mScreenStatusReceiver, screenStatusIF);

        }
    }

    private void unregisterScreenStatusReceiver() {
        if (mScreenStatusReceiver != null) {
            mContext.unregisterReceiver(mScreenStatusReceiver);
            mScreenStatusReceiver = null;
        }
    }

    final class ScreenStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                LZLog.i("ppp", "ppp-屏幕亮了");
                if (preciseNewHeaderHolder != null) {
                    preciseNewHeaderHolder.screenStatusOn();
                }
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                LZLog.i("ppp", "ppp-屏幕暗了");
                if (preciseNewHeaderHolder != null) {
                    preciseNewHeaderHolder.screenStatusOff();
                }
            }
        }
    }

    /**
     * 签到入口开关
     */
   private void showSignInEntro() {
       ApiService.getSignInApi().requestSignInCanShow()
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io())
               .subscribe(new DefaultRx2Subscribe<BaseBean<ActivitySwitchEntity>>() {
                   @Override
                   public void onNext(BaseBean<ActivitySwitchEntity> baseBean) {
                       super.onNext(baseBean);
                       if (baseBean.success()&&baseBean.getResult()!=null){
                           if (baseBean.getResult().getSignStatus()==1){
                               LZLog.i(TAG,"显示签到入口");
                               if (mJumpSignInIv!=null){
                                   mJumpSignInIv.setVisibility(View.VISIBLE);
                                   AnimationSet animationSet=new AnimationSet(true);
                                   ScaleAnimation scaleAnimation=new ScaleAnimation(0.8f, 1.0f,
                                           0.8f, 1.0f,
                                           Animation.RELATIVE_TO_SELF, 0.5f,
                                           Animation.RELATIVE_TO_SELF, 0.5f);
                                   scaleAnimation.setDuration(500);//动画持续时间
                                   scaleAnimation.setRepeatCount(Animation.INFINITE);
                                   scaleAnimation.setRepeatMode(Animation.REVERSE);
                                   animationSet.addAnimation(scaleAnimation);
                                   animationSet.setFillAfter(true);//结束后保存状态
                                   mJumpSignInIv.clearAnimation();
                                   mJumpSignInIv.startAnimation(animationSet);//设置给控件
                               }
                           }else {
                               LZLog.i(TAG,"隐藏签到入口");
                               if(mJumpSignInIv!=null){
                                   mJumpSignInIv.setVisibility(View.GONE);
                               }
                           }
                       }else {
                           if(mJumpSignInIv!=null){
                               mJumpSignInIv.setVisibility(View.GONE);
                           }
                       }
                   }

                   @Override
                   public void onError(Throwable throwable) {
                       super.onError(throwable);
                       if(mJumpSignInIv!=null){
                           mJumpSignInIv.setVisibility(View.GONE);
                       }
                   }
               });
   }



}
