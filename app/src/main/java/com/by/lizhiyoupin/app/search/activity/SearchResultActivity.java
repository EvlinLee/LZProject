package com.by.lizhiyoupin.app.search.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.search.fragment.SearchResultFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/30 17:28
 * Summary: 搜索结果页
 */
@Route(path = "/app/SearchResultActivity")
public class SearchResultActivity extends BaseActivity implements View.OnClickListener, CommonFragmentPagerAdapter.FragmentFactoryCallback, TextView.OnEditorActionListener {
    private ImageView mActionbarBackIv;
    private EditText mActionbarSearchEt;

    private ViewPager mViewPager;
    private List<String> pageList;
    private MagicIndicator mMagicIndicator;
    private CommonFragmentPagerAdapter mPagerAdapter;
    //搜索栏内容
    private String searchTitle;
    private int currentPlatform = 0;//首次显示的平台页面
    private String goodsId = null;//默认为空或""，后台根据id是否为空 查询不同接口搜索


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_layout);
        initImmersionBar(Color.WHITE,true);
        Intent intent = getIntent();
        searchTitle = intent.getStringExtra(CommonConst.KEY_SEARCH_TITLE);
        goodsId = intent.getStringExtra(CommonConst.KEY_SEARCH_ID);
        String platFrom = intent.getStringExtra(CommonConst.KEY_CURRENT_SEARCH_PLATFORM);
        if (!TextUtils.isEmpty(platFrom)) {
            switch (platFrom) {
                case CommonConst.SOURCE_TAO_BAO:
                    currentPlatform = 0;
                    break;
                case CommonConst.SOURCE_PING_DUO_DUO:
                    currentPlatform = 1;
                    break;
                case CommonConst.SOURCE_JING_DONG:
                    currentPlatform = 2;
                    break;
            }
        }

        initView();

    }

    private void initView() {
        pageList = new ArrayList<>();
        pageList.add("淘宝");
        pageList.add("拼多多");
        pageList.add("京东");
        mActionbarBackIv = findViewById(R.id.actionbar_search_back_iv);
        mActionbarSearchEt = findViewById(R.id.actionbar_search_et);
        mMagicIndicator = findViewById(R.id.magicIndicator);


        mViewPager = findViewById(R.id.search_viewPager);

        mActionbarBackIv.setOnClickListener(this);
        mActionbarSearchEt.setOnEditorActionListener(this);
        mPagerAdapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), 3, this) {
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (pageList.size() > position) {
                    return pageList.get(position);
                } else {
                    return "";
                }
            }
        };


        mViewPager.setAdapter(mPagerAdapter);
        mActionbarSearchEt.setText(searchTitle);

        mViewPager.setCurrentItem(currentPlatform);
        initIndicator();
    }

    private void initIndicator() {
        CommonNavigator mCommonNavigator = new CommonNavigator(this);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return pageList == null ? 0 : pageList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                SimplePagerTitleView pagerTitleView = new SimplePagerTitleView(context) {
                    @Override
                    public void onSelected(int index, int totalCount) {
                        getPaint().setFakeBoldText(true);//加粗
                        setTextColor(mSelectedColor);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        getPaint().setFakeBoldText(false);//normal
                        setTextColor(mNormalColor);
                    }

                };
                pagerTitleView.setText(pageList.get(index));
                pagerTitleView.setTextSize(16);
                pagerTitleView.setNormalColor(getResources().getColor(R.color.color_555555));
                pagerTitleView.setSelectedColor(getResources().getColor(R.color.color_FF005E));
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
                // indicator.setYOffset(DeviceUtil.dip2px(context, 3));
                indicator.setLineWidth(DeviceUtil.dip2px(context, 30));
                // indicator.setRoundRadius(DeviceUtil.dip2px(context, 3));
                indicator.setColors(getResources().getColor(R.color.color_FF005E));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(mCommonNavigator);
        LinearLayout titleContainer = mCommonNavigator.getTitleContainer(); // must after setNavigator
        //使用LinearLayout 的属性 在每一项中间添加分割线，来控制每项间距
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return DeviceUtil.dip2px(SearchResultActivity.this, 50);
            }
        });
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_search_back_iv:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public Fragment createFragment(int index) {
        Bundle intent = new Bundle();
        switch (index) {
            case 0:
                //淘宝
                intent.putString(CommonConst.KEY_CURRENT_SEARCH_PLATFORM, CommonConst.SOURCE_TAO_BAO);
                break;
            case 1:
                //拼多多
                intent.putString(CommonConst.KEY_CURRENT_SEARCH_PLATFORM, CommonConst.SOURCE_PING_DUO_DUO);
                break;
            case 2:
                //京东
                intent.putString(CommonConst.KEY_CURRENT_SEARCH_PLATFORM, CommonConst.SOURCE_JING_DONG);
                break;
        }
        intent.putString(CommonConst.KEY_SEARCH_TITLE, searchTitle);
        intent.putString(CommonConst.KEY_SEARCH_ID, goodsId);
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(intent);
        return fragment;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            //点击搜索的时候隐藏软键盘
            DeviceUtil.hideInputMethod(v);
            // 跳转到搜索结果页去搜索
            for (int i = 0; i < mPagerAdapter.mChildFragments.length; i++) {
                if (mPagerAdapter.mChildFragments[i] instanceof SearchResultFragment) {
                    SearchResultFragment childFragment = (SearchResultFragment) mPagerAdapter.mChildFragments[i];

                    childFragment.notifySortListData(mActionbarSearchEt.getText().toString());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mActionbarSearchEt != null) {
            DeviceUtil.hideInputMethod(mActionbarSearchEt);
        }
    }
}
