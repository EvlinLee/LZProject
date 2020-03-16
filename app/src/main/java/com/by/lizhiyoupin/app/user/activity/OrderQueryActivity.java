package com.by.lizhiyoupin.app.user.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.user.fragment.OrderQueryFragment;
import com.by.lizhiyoupin.app.weight.IncomePageTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/12 15:06
 * Summary: 订单查询
 */
@Route(path = "/app/OrderQueryActivity")
public class OrderQueryActivity extends BaseActivity implements CommonFragmentPagerAdapter.FragmentFactoryCallback, View.OnClickListener {
    public static final String TAG = OrderQueryActivity.class.getSimpleName();
    @BindView(R.id.actionbar_back_tv)
    AppCompatTextView mActionbarBackTv;
    @BindView(R.id.actionbar_title_tv)
    AppCompatTextView mActionbarTitleTv;
    @BindView(R.id.actionbar_right_tv)
    AppCompatTextView mActionbarRightTv;
    @BindView(R.id.actionbar_rl)
    RelativeLayout mActionbarRl;
    @BindView(R.id.home_search_et)
    EditText mHomeSearchEt;
    @BindView(R.id.cancel_action_tv)
    TextView mCancelActionTv;
    @BindView(R.id.top_rl)
    View mTopRl;
    @BindView(R.id.order_me_tv)
    TextView mOrderMeTv;
    @BindView(R.id.order_team_tv)
    TextView mOrderTeamTv;
    @BindView(R.id.magicIndicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private List<String> mList;
    private CommonFragmentPagerAdapter mPagerAdapter;
    private int orderType=1;  //必选  订单类型 1我的订单 2团队订单
    private int platformType = 1;// 平台类型   1淘宝 2京东 3拼多多
    private TextView order_tao, order_jd, order_pin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_query_layout);
        initImmersionBar(Color.WHITE, true);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        order_tao = findViewById(R.id.order_tao);
        order_tao.setOnClickListener(this);
        order_jd = findViewById(R.id.order_jd);
        order_jd.setOnClickListener(this);
        order_pin = findViewById(R.id.order_pin);
        order_pin.setOnClickListener(this);
        orderType = getIntent().getIntExtra(CommonConst.ORDER_SELFPURCHASE,1);
        mList = new ArrayList<>();
        mList.add("全部");
        mList.add("待返佣");
        mList.add("已到账");
        mList.add("失效");
        mActionbarBackTv.setText("");
        mActionbarTitleTv.setTextColor(getResources().getColor(R.color.color_333333));
        mActionbarTitleTv.setText(1==orderType ? "自购订单" : "团队订单");
        mActionbarRightTv.setTextSize(16);
        mActionbarRightTv.setTextColor(getResources().getColor(R.color.color_333333));
        mActionbarRightTv.setText("订单找回");
        mPagerAdapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), mList.size(),
                this);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mPagerAdapter);
        initIndicator();
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mList == null ? 0 : mList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                IncomePageTitleView pagerTitleView = new IncomePageTitleView(context);
                int padding = DeviceUtil.dip2px(OrderQueryActivity.this, 18);
                pagerTitleView.setPadding(padding, 0, padding, 0);
                pagerTitleView.setNormalColor(getResources().getColor(R.color.color_999999));
                pagerTitleView.setSelectedColor(getResources().getColor(R.color.color_FF005E));
                pagerTitleView.setTypeface(Typeface.DEFAULT_BOLD);
                pagerTitleView.setText(mList.get(index));
                pagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return pagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setYOffset(DeviceUtil.dip2px(context, 3));
                indicator.setLineWidth(DeviceUtil.dip2px(context, 25));
                indicator.setRoundRadius(DeviceUtil.dip2px(context, 3));
                indicator.setColors(Color.parseColor("#FFFF005E"));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }


    @OnClick({R.id.actionbar_back_tv, R.id.actionbar_right_tv, R.id.order_me_tv, R.id.order_team_tv,
            R.id.cancel_action_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.actionbar_right_tv:
                //订单找回
                CommonSchemeJump.showActivity(this, OrderRetrieveActivity.class);
                break;
        }
    }

    @Override
    public Fragment createFragment(int index) {
        Fragment fragment = new OrderQueryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.KEY_ORDER_SEARCH_TYPE, index);
        bundle.putInt(CommonConst.KEY_ORDER_TYPE, orderType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_tao:
                platformType = 1;
                order_tao.setTextColor(Color.parseColor("#ffff005e"));
                order_jd.setTextColor(Color.parseColor("#ff555555"));
                order_pin.setTextColor(Color.parseColor("#ff555555"));
                requestRefreshData("");
                break;
            case R.id.order_jd:
                platformType = 2;
                order_tao.setTextColor(Color.parseColor("#ff555555"));
                order_jd.setTextColor(Color.parseColor("#ffff005e"));
                order_pin.setTextColor(Color.parseColor("#ff555555"));
                requestRefreshData("");
                break;
            case R.id.order_pin:
                platformType = 3;
                order_tao.setTextColor(Color.parseColor("#ff555555"));
                order_jd.setTextColor(Color.parseColor("#ff555555"));
                order_pin.setTextColor(Color.parseColor("#ffff005e"));
                requestRefreshData("");
                break;

        }
    }

    /**
     * 刷新数据
     *
     * @param orderId
     */
    private void requestRefreshData(String orderId) {
        for (int i = 0; i < mPagerAdapter.mChildFragments.length; i++) {
            Fragment item = mPagerAdapter.mChildFragments[i];
            if (item instanceof OrderQueryFragment) {
                OrderQueryFragment orderQueryFragment = (OrderQueryFragment) item;
                //刷新数据
                orderQueryFragment.setRequestParams(orderType, platformType, orderId);
            }
        }
    }
}
