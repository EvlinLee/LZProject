package com.by.lizhiyoupin.app.main.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.SettingConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration2;
import com.by.lizhiyoupin.app.component_umeng.share.manager.ShareManager;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodListBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodsBean;
import com.by.lizhiyoupin.app.io.bean.VipPowerBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.by.lizhiyoupin.app.main.adapter.VipProductListAdapter;
import com.by.lizhiyoupin.app.main.contract.TabFragmentVipContract;
import com.by.lizhiyoupin.app.main.manager.TabFragmentManager;
import com.by.lizhiyoupin.app.main.presenter.TabFragmentVipPresenter;
import com.by.lizhiyoupin.app.main.weight.UserToolsLayout;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/26 14:37
 * Summary: 超级会员
 */
public class TabFragmentVip extends BaseFragment implements View.OnClickListener, TabFragmentVipContract.TabFragmentVipView {
    public static final String TAG = TabFragmentVip.class.getSimpleName();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UserToolsLayout presentInterestTools;//当前权益
    private UserToolsLayout mVipSuperMember;//超级会员权益
    private TextView mVipSuperMemberMoreTv;//更多超级会员权益
    private RecyclerView mRecyclerView;
    private TextView mRvTitleTv;
    private ImageView mVipPhotoIv;//头像
    private ImageView mVipPhotoIconIv;//头像上的皇冠
    private TextView mVipNickNameTv;//昵称
    private TextView mVipTagTv;//普通会员 超级会员等
    private TextView mVipExpireTipTv;//会员到期说明
    private TextView mVipAddCodeTv;//填写邀请码
    private View mVipInviteRl;//邀请码 xxx 复制
    private TextView mVipInviteNumberTv;//邀请码 xxx
    private TextView mVipInviteCopyTv;//复制
    private SpaceItemDecoration2 mSpaceItemDecoration;
    private GridLayoutManager mGridLayoutManager;
    private Context mContext;
    private VipProductListAdapter mAdapter;
    private TextView mTitle;
    private NestedScrollView mScrollView;
    private ImageView mVipUpgradeIv;
    private View mFansPowerCl;
    private TextView mFansPowerTv;
    private View mActionBarLl;//包含statusBar
    private TabFragmentVipContract.TabFragmentVipPresenters mVipPresenters;
    private LoadMoreHelperRx<VipGoodsBean, Integer> mLoadMoreHelper;
    private boolean hasLoadMore;
    private int[] superList = new int[]{R.drawable.fans_normal_power_pic, R.drawable.fans_zs_power_pic,
            R.drawable.fans_zg_power_pic, R.drawable.fans_wg_power_pic,
            R.drawable.fans_sh_power_pic, R.drawable.fans_xr_power_pic};
    private String[] superTitle = new String[]{"普通粉丝奖金", "专属粉丝奖金", "自购分享赚钱", "网购优惠券", "生活优惠券", "新人免单"};
    private ViewPager mViewPager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vip_layout, container, false); 

        mVipPresenters = new TabFragmentVipPresenter(this);
        initBar(root);
        initView(root);
        registerBroadcastReceiver();
        initRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                        .getManager(IAccountManager.class.getName());
                LoginRequestManager.requestGetuserinfo(accountManager.getAccountInfo().getApiToken());
                refreshData();
                sendVipInfoRefresh();

            }
        });
        return root;
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            int currentTab = ((MainActivity) activity).mTabManager.getCurrentTab();
            if (currentTab == TabFragmentManager.TAB_VIP) {
                StatusBarUtils.addTranslucentColorView(getActivity(), Color.TRANSPARENT, 0);
                ImmersionBar.with(this)
                        .navigationBarColorInt(Color.WHITE)
                        .navigationBarDarkIcon(true)
                        .keyboardEnable(true)
                        .statusBarDarkFont(false)
                        .statusBarColorInt(Color.TRANSPARENT)
                        .init();

            }
        }
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        StatusBarUtils.addTranslucentColorView(getActivity(), Color.TRANSPARENT, 0);
        ImmersionBar.with(this)
                .navigationBarColorInt(Color.WHITE)
                .navigationBarDarkIcon(true)
                .keyboardEnable(true)
                .statusBarDarkFont(false)
                .statusBarColorInt(Color.TRANSPARENT)
                .init();

    }

    private void refreshData() {
        IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
        if (accountManager != null) {
            mVipPresenters.requestVipPowerList(accountManager.getUserPhone());
        }
        if (mLoadMoreHelper != null) {
            mLoadMoreHelper.loadData();
        }

    }

    private void initBar(View root) {
        DeviceUtil.setStatusBarHeight(getActivity(), root.findViewById(R.id.status_bar_view));
        root.findViewById(R.id.actionbar_back_tv).setVisibility(View.GONE);
        mTitle = root.findViewById(R.id.actionbar_title_tv);
        TextView mRightTv = root.findViewById(R.id.actionbar_right_tv);
        mRightTv.setOnClickListener(this);
        mRightTv.setText("");
        mRightTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, mContext.getResources().getDrawable(R.drawable.actionbar_share));
        mTitle.setTextColor(getResources().getColor(R.color.white_100));
        mTitle.setText("我的优选");
    }

    private void initView(View root) {
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        mScrollView = root.findViewById(R.id.vip_scrollview);
        mActionBarLl = root.findViewById(R.id.action_bar_ll);
        mVipUpgradeIv = root.findViewById(R.id.vip_upgrade_iv);
        mVipSuperMember = root.findViewById(R.id.vip_super_member_tools_lt);
        mVipPhotoIv = root.findViewById(R.id.user_photo_iv);
        mVipPhotoIconIv = root.findViewById(R.id.vip_photo_icon_iv);
        mVipNickNameTv = root.findViewById(R.id.vip_nickName_tv);
        mVipTagTv = root.findViewById(R.id.vip_tag_tv);
        mVipExpireTipTv = root.findViewById(R.id.vip_expire_tip_tv);
        mVipAddCodeTv = root.findViewById(R.id.vip_add_code_tv);
        mVipInviteRl = root.findViewById(R.id.vip_invite_rl);
        mVipInviteNumberTv = root.findViewById(R.id.vip_invite_number_tv);
        mVipInviteCopyTv = root.findViewById(R.id.vip_invite_copy_tv);
        presentInterestTools = root.findViewById(R.id.vip_present_interest_tools_lt);
        mViewPager = presentInterestTools.findViewById(R.id.viewPager);
        mRvTitleTv = root.findViewById(R.id.vip_bottom_list_title_tv);
        mRecyclerView = root.findViewById(R.id.vip_list_rv);
        mVipSuperMemberMoreTv = root.findViewById(R.id.vip_super_member_more_tv);
        mFansPowerTv = root.findViewById(R.id.fans_power_tv);
        mFansPowerCl = root.findViewById(R.id.fans_power_cl);

        mVipSuperMemberMoreTv.setOnClickListener(this);
        mVipUpgradeIv.setOnClickListener(this);
        mVipInviteCopyTv.setOnClickListener(this);
        mVipAddCodeTv.setOnClickListener(this);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int dY = (int) (scrollY * 1f / mActionBarLl.getHeight() * 255);
                if (dY > 255) {
                    dY = 255;
                }
                mActionBarLl.setBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(v.getContext(), R.color.color_C42021), dY));

            }
        });


        List<VipPowerBean> entities = new ArrayList<>();
        for (int i = 0; i < superList.length; i++) {
            VipPowerBean configChinaIconRspEntity = new VipPowerBean();
            configChinaIconRspEntity.setName(superTitle[i]);
            configChinaIconRspEntity.setDrawRes(superList[i]);
            configChinaIconRspEntity.setNativeDraw(true);
            entities.add(configChinaIconRspEntity);
        }
        //超级会员权益
        mVipSuperMember.initNetWorkIcon(entities);

        mSpaceItemDecoration = new SpaceItemDecoration2(DeviceUtil.dip2px(mContext, 5), 2);
        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //每行item数据=spanCount/getSpanSize
                //底部最后一个显示加载更多 或暂无数据
                return mLoadMoreHelper.getData().size() == position ? 2 : 1;

            }
        });
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(mSpaceItemDecoration);
        mAdapter = new VipProductListAdapter(mContext, getChildFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

        sendVipInfoRefresh();
        loadRecyclerView();

        //添加页面滑动监听,控制 SwipeRefreshLayout与ViewPager滑动冲突
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == 1) {
                    mSwipeRefreshLayout.setEnabled(false);//设置不可触发
                } else if (state == 2) {
                    mSwipeRefreshLayout.setEnabled(true);//设置可触发
                }
            }
        });
    }

    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<VipGoodsBean, Integer>(mContext)
                .adapter(mAdapter)
                .emptyLoadingView(null)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F7F7F7)
                .recyclerView(mRecyclerView)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 20))
                .loader(new LoadMoreHelperRx.Loader<VipGoodsBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<VipGoodsBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "requestVipGoodsList load" + integer);

                        return ApiService.getHomeApi().requestVipGoodsList(integer, pageSize)
                                .map(new Function<BaseBean<VipGoodListBean>, Collection<VipGoodsBean>>() {
                                    @Override
                                    public Collection<VipGoodsBean> apply(BaseBean<VipGoodListBean> vipGoodListBeanBaseBean) throws Exception {
                                        LZLog.i(TAG, "requestVipGoodsList" + vipGoodListBeanBaseBean.success());
                                        if (vipGoodListBeanBaseBean.success()) {
                                            VipGoodListBean result = vipGoodListBeanBaseBean.getResult();
                                           // int current = result.getCurrent();
                                           // int totalPage = result.getPages();
                                            List<VipGoodsBean> records = result.getRecords();
                                            hasLoadMore = !ArraysUtils.isListEmpty(records)&&records.size()==pageSize;
                                            return result.getRecords();
                                        }
                                        throw new Exception(vipGoodListBeanBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<VipGoodsBean> data, int pageCount) {
                        return hasLoadMore;
                    }

                }).build();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                LZLog.i(TAG, "requestVipGoodsList onLoadMore");
                if (hasLoadMore) {
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        refreshData();
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_right_tv:
                //分享
                showShareDialog();
                break;
            case R.id.vip_upgrade_iv:
                //使升级vip列表置顶
                int top = mRecyclerView.getTop();
                int height = mActionBarLl.getHeight();
                mScrollView.smoothScrollTo(0, top - height);
                break;
            case R.id.vip_invite_copy_tv:
                //复制邀请码
                CharSequence text = mVipInviteNumberTv.getText();
                DeviceUtil.putClipboardText(mContext, text);
                MessageToast.showToast(mContext, "已复制");
                break;
            case R.id.vip_add_code_tv:
                //填写邀请码
                DiaLogManager.showInvitationCodeDialog(mContext, getChildFragmentManager());
                break;
            case R.id.vip_super_member_more_tv:
                //跳转 荔枝权益 页
                CommonSchemeJump.showActivity(mContext, "/app/PowerDescActivity");
                break;
        }
    }

    private void showShareDialog() {
        UserInfoBean accountInfo = LiZhiApplication.getApplication().getAccountManager().getAccountInfo();
        ShareManager.requestShareBean(getActivity(), 1, null, accountInfo.getId(), accountInfo.getCode());
    }

    @Override
    public void requestVipPowerListSuccess(List<VipPowerBean> list) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (!ArraysUtils.isListEmpty(list)) {
            presentInterestTools.initNetWorkIcon(list);
        }
    }

    @Override
    public void requestVipPowerListError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 用户登录状态（或相关信息）改变，修改相应信息
     */
    private void sendVipInfoRefresh() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        UserInfoBean userInfoBean = accountManager.getAccountInfo();
        mVipNickNameTv.setText(userInfoBean.getName());
        new GlideImageLoader(mContext, userInfoBean.getAvatar())
                .error(R.drawable.default_face)
                .into(mVipPhotoIv);

        String vipLevel;
        mAdapter.setVipLevel(userInfoBean.getRoleLevel());
        showAndHideFans(userInfoBean.getRoleLevel() > 1);
        if (userInfoBean.getRoleLevel() > 1) {
            mRvTitleTv.setText("99礼包");
        } else {
            mRvTitleTv.setText("如何成为荔枝优品超级会员\n购买礼包尊享超级会员权益");
        }
        switch (userInfoBean.getRoleLevel()) {
            case 1:
                vipLevel = "普通会员";
                mVipPhotoIconIv.setImageResource(R.drawable.vip_normal_photo_icon);
                mVipUpgradeIv.setVisibility(View.VISIBLE);
                String superiorId = userInfoBean.getSuperiorId();
                if (TextUtils.isEmpty(superiorId)) {
                    //没有邀请码
                    mVipAddCodeTv.setVisibility(View.VISIBLE);
                    mVipInviteRl.setVisibility(View.GONE);
                    mVipExpireTipTv.setVisibility(View.GONE);
                } else {
                    String code = userInfoBean.getCode();
                    mVipInviteRl.setVisibility(View.VISIBLE);
                    mVipExpireTipTv.setVisibility(View.GONE);
                    mVipAddCodeTv.setVisibility(View.GONE);
                    mVipInviteNumberTv.setText(code);
                }
                break;
            case 2:
                vipLevel = "超级会员";
                mVipPhotoIconIv.setImageResource(R.drawable.vip_super_photo_icon);
                mVipUpgradeIv.setVisibility(View.GONE);
                setSuperVipTimeEnd(userInfoBean, vipLevel);
                break;
            case 3:
                vipLevel = "Plus超级会员";
                mVipPhotoIconIv.setImageResource(R.drawable.vip_super_photo_icon);
                mVipUpgradeIv.setVisibility(View.GONE);
                setSuperVipTimeEnd(userInfoBean, "超级会员");
                break;
            case 4:
                vipLevel = "运营商";
                mVipPhotoIconIv.setImageResource(R.drawable.vip_operator_photo_icon);
                mVipUpgradeIv.setVisibility(View.GONE);
                setSuperVipTimeEnd(userInfoBean, vipLevel);
                break;
            case 5:
                vipLevel = " plus运营商";
                mVipPhotoIconIv.setImageResource(R.drawable.vip_operator_photo_icon);
                mVipUpgradeIv.setVisibility(View.GONE);
                setSuperVipTimeEnd(userInfoBean, "运营商");
                break;
            default:
                vipLevel = "普通会员";
                break;

        }
        mVipTagTv.setText(vipLevel);

    }

    /**
     * 超级会员及以上才显示 粉丝权益
     *
     * @param show
     */
    private void showAndHideFans(boolean show) {
        if (show) {
            mFansPowerTv.setVisibility(View.VISIBLE);
            mFansPowerCl.setVisibility(View.VISIBLE);
        } else {
            mFansPowerTv.setVisibility(View.GONE);
            mFansPowerCl.setVisibility(View.GONE);
        }
    }

    /**
     * 设置会员到期时间提示
     *
     * @param userInfoBean
     * @param vipLevel
     */
    private void setSuperVipTimeEnd(UserInfoBean userInfoBean, String vipLevel) {
        mVipAddCodeTv.setVisibility(View.GONE);
        mVipInviteRl.setVisibility(View.GONE);
        mVipExpireTipTv.setVisibility(View.VISIBLE);
        String levelEndTime = userInfoBean.getRoleLevelEndTime();
        ViewUtil.setTextViewFormat(mContext, mVipExpireTipTv, R.string.vip_tip_vip_overdue_text,
                vipLevel, TimeUtils.getFormatEndTime(levelEndTime));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcastReceiver();
    }


    private LocalBroadcastReceiver mReceiver;

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SettingConst.ACTION_LOGIN_SUCCESS);
            intentFilter.addAction(SettingConst.ACTION_LOGOUT_SUCCESS);
            LocalBroadcastManager.getInstance(LiZhiApplication.getApplication()).registerReceiver(mReceiver, intentFilter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(LiZhiApplication.getApplication()).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_LOGIN_SUCCESS.equals(action)) {
                sendVipInfoRefresh();
               // refreshData();
            } else if (SettingConst.ACTION_LOGOUT_SUCCESS.equals(action)) {
                sendVipInfoRefresh();
            }
        }
    }


}
