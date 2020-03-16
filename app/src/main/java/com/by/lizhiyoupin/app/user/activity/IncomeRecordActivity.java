package com.by.lizhiyoupin.app.user.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.SettingConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.GsonUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.storage.PrefCache;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.io.bean.MyIncomeVO;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.user.adapter.CommissionRecyclerViewAdapter;
import com.by.lizhiyoupin.app.user.adapter.GiftRecyclerViewAdapter;
import com.by.lizhiyoupin.app.user.contract.IncomeContract;
import com.by.lizhiyoupin.app.user.presenter.IncomeRecordPresenter;
import com.by.lizhiyoupin.app.weight.IncomePageTitleView;
import com.gyf.immersionbar.ImmersionBar;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/5 17:42
 * Summary: 收益记录
 */
@Route(path = "/app/IncomeRecordActivity")
public class IncomeRecordActivity extends BaseMVPActivity<IncomeContract.IncomeRecordView, IncomeContract.IncomeRecordPresenters> implements IncomeContract.IncomeRecordView {
    public static final String TAG = IncomeRecordActivity.class.getSimpleName();
    @BindView(R.id.sum_of_money_tv)
    TextView mSumOfMoneyTv;
    @BindView(R.id.nestScrollView)
    NestedScrollView mNestScrollView;
    @BindView(R.id.with_drawable_amount_tv)
    TextView mWithDrawableAmountTv;
    @BindView(R.id.takeBalance_tv)
    TextView mTakeBalanceTv;
    @BindView(R.id.inTakeBalance_tv)
    TextView mInTakeBalanceTv;
    @BindView(R.id.income_record_cash_out_tv)
    TextView mIncomeRecordCashOutTv;
    @BindView(R.id.income_record_last_month_profit_tv)
    TextView mIncomeRecordLastMonthProfitTv;
    @BindView(R.id.income_record_last_month_settlement_tv)
    TextView mIncomeRecordLastMonthSettlementTv;
    @BindView(R.id.income_record_this_month_profit_tv)
    TextView mIncomeRecordThisMonthProfitTv;
    @BindView(R.id.income_record_tip_tv)
    TextView mIncomeRecordTipTv;
    @BindView(R.id.commission_recyclerView)
    RecyclerView mCommissionRecyclerView;
    @BindView(R.id.gift_recyclerView)
    RecyclerView mGiftRecyclerView;
    @BindView(R.id.actionbar_back_tv)
    AppCompatTextView mActionbarBackTv;
    @BindView(R.id.actionbar_title_tv)
    AppCompatTextView mActionbarTitleTv;
    @BindView(R.id.actionbar_right_tv)
    AppCompatTextView mActionbarRightTv;
    @BindView(R.id.actionbar_rl)
    RelativeLayout mActionbarRl;
    @BindView(R.id.empty_layout)
    View mEmptyLayout;
    MagicIndicator mMagicIndicator;
    private List<String> mTabList;
    private CommissionRecyclerViewAdapter mCommissionAdapter;
    private GiftRecyclerViewAdapter mGiftAdapter;
    private int topHeight;
    private int REQUESTCODE=120;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public IncomeContract.IncomeRecordPresenters getBasePresenter() {
        return new IncomeRecordPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_record_layout);
        initImmersionBar(Color.TRANSPARENT,false);
        ButterKnife.bind(this);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);//刷新
        initView();
        showLoadingDialog();
        String mIncomeVO = PrefCache.getString(SettingConst.KEY_INCOME_RECORD_CACHE);
        MyIncomeVO myIncomeVO = GsonUtil.gsonToBean(mIncomeVO, MyIncomeVO.class);
        if (myIncomeVO != null) {
            LZLog.i(TAG, "先读取sp缓存数据");
            dismissLoadingDialog();
            refreshData(myIncomeVO);
        }
        basePresenter.requestIncomeRecord();

        initRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                basePresenter.requestIncomeRecord();
            }
        });
    }




    private void initView() {
        mMagicIndicator = findViewById(R.id.magicIndicator);
        topHeight = DeviceUtil.dip2px(this, 130);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mActionbarRl.getLayoutParams();
        layoutParams.setMargins(0,DeviceUtil.getStatusBarHeight(this),0,0);
        mActionbarRl.setLayoutParams(layoutParams);
        // 解决RecyclerView抢焦点把自己置于屏幕中央的问题
        mCommissionRecyclerView.setFocusable(false);
        mGiftRecyclerView.setFocusable(false);
        //佣金收益
        mCommissionRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mCommissionAdapter = new CommissionRecyclerViewAdapter(this);
        mCommissionRecyclerView.setAdapter(mCommissionAdapter);

        //礼包收益
        mGiftRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mGiftAdapter = new GiftRecyclerViewAdapter(this);
        mGiftRecyclerView.setAdapter(mGiftAdapter);

        mNestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > topHeight && oldScrollY <= topHeight) {
                    ImmersionBar.with(IncomeRecordActivity.this)
                            .navigationBarColorInt(Color.WHITE)
                            .keyboardEnable(true)
                            .statusBarDarkFont(false)
                            .flymeOSStatusBarFontColorInt(Color.WHITE)
                            .statusBarColorInt(getResources().getColor(R.color.color_18181A))
                            .init();
                    mActionbarRl.setBackgroundColor(getResources().getColor(R.color.color_18181A));
                }
                if (scrollY < topHeight && oldScrollY >= topHeight) {
                   ImmersionBar.with(IncomeRecordActivity.this)
                            .navigationBarColorInt(Color.WHITE)
                            .keyboardEnable(true)
                            .statusBarDarkFont(false)
                            .flymeOSStatusBarFontColorInt(Color.TRANSPARENT)
                            .statusBarColorInt(Color.TRANSPARENT)
                            .init();
                    mActionbarRl.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        mActionbarBackTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.direction_back_left_white, 0, 0, 0);
        mActionbarBackTv.setText("");
        mActionbarTitleTv.setTextColor(getResources().getColor(R.color.white_100));
        mActionbarRightTv.setTextColor(getResources().getColor(R.color.white_100));
        mActionbarTitleTv.setText(R.string.income_record_actionbar_title_text);
        mActionbarRightTv.setText(R.string.income_record_actionbar_right_text);
        mActionbarRightTv.setTextSize(16);
        mTabList = new ArrayList<>();
        mTabList.add("佣金收益");
        mTabList.add("礼包收益");
        initIndicator();
        

    }
    private void initIndicator() {

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTabList == null ? 0 : mTabList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                IncomePageTitleView pagerTitleView = new IncomePageTitleView(context);
                int padding = DeviceUtil.dip2px(IncomeRecordActivity.this, 18);
                pagerTitleView.setPadding(padding, 0, padding, 0);
                pagerTitleView.setNormalColor(getResources().getColor(R.color.color_333333));
                pagerTitleView.setSelectedColor(getResources().getColor(R.color.color_D6B667));
                pagerTitleView.setTypeface(Typeface.DEFAULT_BOLD);
                pagerTitleView.setTextSize(17);
                pagerTitleView.setText(mTabList.get(index));
                pagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTabViewContent(index);
                        //单独使用，不配合 viewPage 需要自己刷新
                        mMagicIndicator.onPageSelected(index);
                        commonNavigator.notifyDataSetChanged();
                    }
                });
                return pagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setYOffset(DeviceUtil.dip2px(context, 3));
                indicator.setLineWidth(DeviceUtil.dip2px(context, 15));
                indicator.setRoundRadius(DeviceUtil.dip2px(context, 3));
                indicator.setColors(Color.parseColor("#D6B667"));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);

    }
    private void showTabViewContent( int selectedTabPosition) {
        if (selectedTabPosition == 0) {
            mGiftRecyclerView.setVisibility(View.GONE);
            mCommissionRecyclerView.setVisibility(View.VISIBLE);
            if (mCommissionAdapter.getListData().size() <= 0) {
                mEmptyLayout.setVisibility(View.VISIBLE);
            } else {
                mEmptyLayout.setVisibility(View.GONE);
            }
        } else {
            mCommissionRecyclerView.setVisibility(View.GONE);
            mGiftRecyclerView.setVisibility(View.VISIBLE);
            if (mGiftAdapter.getListData().size() <= 0) {
                mEmptyLayout.setVisibility(View.VISIBLE);
            } else {
                mEmptyLayout.setVisibility(View.GONE);
            }
        }

    }

    private void refreshData(MyIncomeVO myIncomeVO) {
        //累计收益
        mSumOfMoneyTv.setText(StringUtils.getFormattedDouble(myIncomeVO.getAllIncome()));
        //可用余额
        mWithDrawableAmountTv.setText(StringUtils.getFormattedDouble(myIncomeVO.getBalance()));
        //已经提现金额
        mTakeBalanceTv.setText(StringUtils.getFormattedDouble(myIncomeVO.getTakeBalance()));
        //提现中金额
        mInTakeBalanceTv.setText(StringUtils.getFormattedDouble(myIncomeVO.getInTakeBalance()));
        //上月预估收益
        mIncomeRecordLastMonthProfitTv.setText(StringUtils.getFormattedDouble(myIncomeVO.getLastMonthEstimate()));
        //上月结算收益
        mIncomeRecordLastMonthSettlementTv.setText(StringUtils.getFormattedDouble(myIncomeVO.getLastMonthActual()));
        //本月预估收益
        mIncomeRecordThisMonthProfitTv.setText(StringUtils.getFormattedDouble(myIncomeVO.getNowMonthEstimate()));
        //佣金收益
        mCommissionAdapter.setListData(myIncomeVO.getOrderIncome());
        mCommissionAdapter.notifyDataSetChanged();
        //礼包收益
        mGiftAdapter.setListData(myIncomeVO.getGiftIncome());
        mGiftAdapter.notifyDataSetChanged();

    }

    @OnClick({R.id.actionbar_back_tv, R.id.actionbar_right_tv, R.id.income_record_cash_out_tv,R.id.sum_of_money_tv,R.id.with_drawable_amount_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.actionbar_right_tv:
                //明细
                CommonSchemeJump.showActivity(this, "/app/IncomeDetailActivity");
                break;
            case R.id.income_record_cash_out_tv:
                //提现
                CommonSchemeJump.showActivityForResult(this,"/app/WithdrawclassActivity",REQUESTCODE);
                break;
            case R.id.sum_of_money_tv://累计收益金额
                DiaLogManager.showIncomeRecordDialog(this,getSupportFragmentManager());
                break;
            case R.id.with_drawable_amount_tv://可提现金额
                DiaLogManager.showIncomeRecordDialog(this,getSupportFragmentManager());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUESTCODE){
            basePresenter.requestIncomeRecord();
        }
    }

    @Override
    public void requestIncomeRecordSuccess(MyIncomeVO myIncomeVO) {
        LZLog.i(TAG, "requestIncomeRecordSuccess");
        dismissLoadingDialog();
        refreshData(myIncomeVO);
        PrefCache.putString(SettingConst.KEY_INCOME_RECORD_CACHE, GsonUtil.gsonString(myIncomeVO), PrefCache.expireInDays(1));
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void requestIncomeRecordError(Throwable throwable) {
        LZLog.i(TAG, "requestIncomeRecordError==" + throwable);
        mSwipeRefreshLayout.setRefreshing(false);
        dismissLoadingDialog();
        if (mGiftRecyclerView.getVisibility()==View.GONE){
            showTabViewContent(0);
        }else{
            showTabViewContent(1);
        }

    }


}
