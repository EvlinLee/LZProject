package com.by.lizhiyoupin.app.sign.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.impl.DialogCallBack;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration2;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.LZShare;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.bean.SignInBean;
import com.by.lizhiyoupin.app.io.bean.SignInRedPaperBean;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.main.adapter.DetailSelectionListAdapter;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.sign.adapter.SignInAdapter;
import com.by.lizhiyoupin.app.sign.adapter.SignInWeekAdapter;
import com.by.lizhiyoupin.app.sign.contract.SignInContract;
import com.by.lizhiyoupin.app.sign.presenter.SignInPresenter;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;

import java.util.Collection;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/9 14:24
 * Summary: 签到
 */
@Route(path = "/app/SignInActivity")
public class SignInActivity extends BaseMVPActivity<SignInContract.ISignInView, SignInContract.ISignInPresenter> implements SignInContract.ISignInView, View.OnClickListener, CompoundButton.OnCheckedChangeListener, DialogCallBack<SignInRedPaperBean> {
    public static final String TAG = SignInActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private SignInAdapter mSignInAdapter;
    private RecyclerView mWeekRecyclerView;
    private RecyclerView mGuessLikeRecyclerView;
    private SignInWeekAdapter mSignInWeekAdapter;
    private DetailSelectionListAdapter mLikeAdapter;
    private View mEmptyView;
    private View mFailRetry;
    private View mLoadingLayout;
    private TextView mSignTotalDayTv;//已签到天数
    private TextView mSignTipGiftTv;//瓜分5千万
    private CheckBox mSignInSwitch;//push开关
    private SignInBean mInBean;
    private GridLayoutManager mGuessLikeLayoutManager;
    private String uniqueId;
    private CollapsingToolbarLayout collapsing;

    private LoadMoreHelperRx<PreciseListBean, Integer> mLoadMoreHelper;

    @Override
    public SignInContract.ISignInPresenter getBasePresenter() {
        return new SignInPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_layout);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .navigationBarDarkIcon(true)
                .navigationBarColorInt(Color.WHITE)
                .init();
        boolean logined = LiZhiApplication.getApplication().getAccountManager().isLogined();
        if (!logined) {
            CommonSchemeJump.showLoginActivity(this);
            finish();
            return;
        }
        initView();
        initData();
    }


    private void initView() {

        collapsing = findViewById(R.id.collapsing);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) collapsing.getLayoutParams();
        int statusBarHeight = DeviceUtil.getStatusBarHeight(this);
        layoutParams.setMargins(0, statusBarHeight+ DeviceUtil.dip2px(this, 58), 0, 0);
        findViewById(R.id.status_bar_view).getLayoutParams().height = statusBarHeight;
        uniqueId = DeviceUtil.getImei(this);
        TextView backTv = findViewById(R.id.actionbar_back_tv);
        TextView titleTv = findViewById(R.id.actionbar_title_tv);
        mSignTotalDayTv = findViewById(R.id.statistics_top_day_tv);
        mSignTipGiftTv = findViewById(R.id.sign_in_tip_gift_tv);
        mSignInSwitch = findViewById(R.id.sign_in_switch);
        mRecyclerView = findViewById(R.id.recyclerView);
        mWeekRecyclerView = findViewById(R.id.sign_week_recyclerView);
        mGuessLikeRecyclerView = findViewById(R.id.guess_like_recyclerView);
        mEmptyView = findViewById(R.id.empty_layout);
        mFailRetry = findViewById(R.id.fail_retry);
        mLoadingLayout = findViewById(R.id.loading_layout);
        backTv.setText("");
        backTv.setOnClickListener(this);
        titleTv.setText("日签打卡");
        titleTv.setTextColor(Color.WHITE);
        ViewUtil.setDrawableOfTextView(backTv, R.drawable.direction_back_left_white, ViewUtil.DrawableDirection.LEFT);
        String formatText = String.format(getResources().getString(R.string.sign_in_statistics_day_text), 0);
        SpannableString spannableString2 = new SpannableString(formatText);
        RelativeSizeSpan bigSizeSpan2 = new RelativeSizeSpan(1.5f);
        spannableString2.setSpan(bigSizeSpan2, formatText.lastIndexOf("签") + 1, formatText.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mSignTotalDayTv.setText(spannableString2);
        //签到
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration2(DeviceUtil.dip2px(this, 10), 3));
        mSignInAdapter = new SignInAdapter(this, basePresenter);
        mRecyclerView.setAdapter(mSignInAdapter);
        boolean pushSwitch = SPUtils.getDefault().getBoolean(CommonConst.KEY_SIGN_IN_PUSH_SWITCH, true);
        mSignInSwitch.setChecked(pushSwitch);
        //先初始化 开关，再监听开关变化
        mSignInSwitch.setOnCheckedChangeListener(this);
        //时间线
        mWeekRecyclerView.setLayoutManager(new GridLayoutManager(this, 7));
        mSignInWeekAdapter = new SignInWeekAdapter(this);
        mWeekRecyclerView.setAdapter(mSignInWeekAdapter);

        //猜你喜欢
        mLikeAdapter = new DetailSelectionListAdapter(this);
        mGuessLikeLayoutManager = new GridLayoutManager(this, 2);
        mGuessLikeRecyclerView.setLayoutManager(mGuessLikeLayoutManager);
        SpaceItemDecoration2 spaceItemDecoration2 = new SpaceItemDecoration2(DeviceUtil.dip2px(this, 5), 2);
        spaceItemDecoration2.setHasHeader(true);
        mGuessLikeRecyclerView.addItemDecoration(spaceItemDecoration2);
        mGuessLikeLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //每行item数据=spanCount/getSpanSize
                if (position == 0) {
                    return 2;
                }
                return mLikeAdapter.getItemCount() - 1 == position ? 2 : 1;
            }
        });
        mLikeAdapter.setHeaderView(LayoutInflater.from(this).inflate(R.layout.item_sign_in_like_img_layout, mGuessLikeRecyclerView, false));
        mGuessLikeRecyclerView.setAdapter(mLikeAdapter);

    }

    private void initData() {
        //isNotificationEnabled 1表示开启，0表示关闭，-1表示检测失败
        basePresenter.requestSignInInfo(JPushInterface.isNotificationEnabled(this) == 1 ? 1 : 0);
        basePresenter.requestSignInRedPaper(1);
        initLoadMoreHelper();
    }


    private void initLoadMoreHelper() {
        mLoadMoreHelper = new LoadMoreBuilderRx<PreciseListBean, Integer>(this)
                .adapter(mLikeAdapter)
                .recyclerView(mGuessLikeRecyclerView)
                .emptyView(mEmptyView)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mLoadingLayout)
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<PreciseListBean>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<PreciseListBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<PreciseListBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "requestPreciseSelectionList==" + integer);
                        return ApiService.getHomeApi().requestPreciseSelectionList(integer, pageSize, uniqueId)
                                .map(new Function<BaseBean<List<PreciseListBean>>, Collection<PreciseListBean>>() {
                                    @Override
                                    public Collection<PreciseListBean> apply(BaseBean<List<PreciseListBean>> listBaseBean) throws Exception {
                                        if (listBaseBean.success()) {
                                            return listBaseBean.getResult();
                                        }
                                        throw new Exception(new Throwable(listBaseBean.msg));
                                    }
                                });
                    }

                }).build();

        mGuessLikeRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mGuessLikeLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (mLoadMoreHelper.hasMore()) {
                    LZLog.i(TAG, "requestPreciseSelectionList onLoadMore");
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        mLoadMoreHelper.loadData();
    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            default:
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //签到推送开关
        int notificationEnabled = JPushInterface.isNotificationEnabled(this);
        //isNotificationEnabled 1表示开启，0表示关闭，-1表示检测失败
        if (notificationEnabled == 0) {
            LZLog.i(TAG, "签到推送开关");
            DiaLogManager.showSettingSignInPushTipsDialog(this, getSupportFragmentManager(),
                    "开启系统通知消息", "打开系统通知，每天提醒我来领红包");
            buttonView.setChecked(false);
        } else {
            LZLog.i(TAG, "签到推送开关");
            SPUtils.getDefault().putBoolean(CommonConst.KEY_SIGN_IN_PUSH_SWITCH, isChecked);
            basePresenter.requestSignInPushSwitch(isChecked ? 1 : 0);
        }

    }

    private void refreshView(SignInBean bean) {
        //签到领红包，本周已签%s天
        String formatText = String.format(getResources().getString(R.string.sign_in_statistics_day_text), bean.getSignedDays());
        SpannableString spannableString2 = new SpannableString(formatText);
        RelativeSizeSpan bigSizeSpan2 = new RelativeSizeSpan(1.5f);
        spannableString2.setSpan(bigSizeSpan2, formatText.lastIndexOf("签") + 1, formatText.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mSignTotalDayTv.setText(spannableString2);
        mSignTipGiftTv.setText(bean.getSignDescription());
        mSignInSwitch.setChecked(bean.getSignRemind() == 1);
    }

    @Override
    public void requestSignInInfoSuccess(SignInBean bean) {
        LZLog.i(TAG, "requestSignInInfoSuccess==");
        mInBean = bean;
        refreshView(bean);
        mSignInAdapter.setAllEarnings(bean.getAllEarnings());
        mSignInAdapter.setListData(bean.getUserSignBonusVOList());
        mSignInAdapter.notifyDataSetChanged();

        mSignInWeekAdapter.setListData(bean.getUserSignWeekInfoVOList());
        mSignInWeekAdapter.notifyDataSetChanged();
    }

    @Override
    public void requestSignInInfoError(Throwable throwable) {
        LZLog.i(TAG, "requestSignInInfoError==" + throwable);
    }

    @Override
    public void requestSignInRedPaperSuccess(SignInRedPaperBean bean) {
        LZLog.i(TAG, "requestSignInRedPaperSuccess show==" + bean.isShowDialog());
        if (bean.isShowDialog()) {
            //显示签到红包框
            DiaLogManager.showSignInRedPagerDialog(getSupportFragmentManager(), this, bean, this);
        }
    }

    private SignInRedPaperBean mSignPagerCallbackBean;

    @Override
    public void clickCallback(MessageBox messageBox, SignInRedPaperBean inRedPaperBean) {
        //showSignInRedPagerDialog 的 点击调起分享控件
        //更新签到时间，次数
        SPUtils.getDefault().putInt(CommonConst.KEY_SIGN_IN_TIMES, CommonConst.SIGN_IN_DIALOG_MAX_TIMES);
        SPUtils.getDefault().putLong(CommonConst.KEY_SIGN_IN_EVERY_DAY_TIME, System.currentTimeMillis());
        LZLog.i(TAG, "首下红包 签到成功 ，是否需要显示分享控件" + inRedPaperBean.isShare());
        mSignPagerCallbackBean = inRedPaperBean;
        //签到领红包
        basePresenter.requestSignInEveryDay(mSignPagerCallbackBean.getSignBonusAmount());
    }

    @Override
    public void requestSignInRedPaperError(Throwable throwable) {
        LZLog.i(TAG, "requestSignInRedPaperError==");
    }

    @Override
    public void requestSignInPushSwitchSuccess(String msg) {
        LZLog.i(TAG, "requestSignInPushSwitchSuccess==" + msg);
        // ToastUtils.show(isChecked?"提醒开启成功":"提醒已关闭");
        ToastUtils.show(msg);
    }

    @Override
    public void requestSignInPushSwitchError(Throwable throwable) {
        LZLog.i(TAG, "requestSignInPushSwitchError==" + throwable);
    }

    @Override
    public void requestSignInEveryDaySuccess(Object bean) {
        //收下红包,签到成功
        LZLog.i(TAG, "requestSignInEveryDaySuccess==");
        basePresenter.requestSignInInfo(mInBean.getSignRemind());
        if (mSignPagerCallbackBean != null && mSignPagerCallbackBean.isShare()) {
            //签到成功后，需要弹出分享
            String code = LiZhiApplication.getApplication().getAccountManager().getAccountInfo().getCode();

            List<ChannelListBean> channelListH5 = LZShare.getChannelListH5(WebUrlManager.getSignInShareUrl(code),null,
                    String.format(getResources().getString(R.string.share_sign_in_make_money_text),
                            mInBean != null ? StringUtils.getFormattedDouble0(mInBean.getAllEarnings()) : 10));
            LZShare.share(channelListH5).show();
        }
    }

    @Override
    public void requestSignInEveryDayError(Throwable throwable) {
        //签到成功
        LZLog.i(TAG, "requestSignInEveryDaySuccess==");
        ToastUtils.show("签到失败!");

    }

    @Override
    protected void onDestroy() {
        if (mSignInAdapter != null) {
            for (int i = 0, itemCount = mSignInAdapter.getItemCount(); i < itemCount; i++) {
                SignInAdapter.ViewHolder viewHolderForAdapterPosition = (SignInAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolderForAdapterPosition != null && viewHolderForAdapterPosition.redTimeTv != null) {
                    LZLog.i(TAG, "removeHandlerMessage==" + i);
                    viewHolderForAdapterPosition.redTimeTv.removeHandlerMessage();
                }
            }
        }
        super.onDestroy();
    }
}
