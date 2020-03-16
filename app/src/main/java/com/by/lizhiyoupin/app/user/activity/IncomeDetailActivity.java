package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.IncomeDetailsVO;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.popup.InComeDetailPopupWindow;
import com.by.lizhiyoupin.app.user.adapter.IncomeDetailAdapter;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/6 16:39
 * Summary: 收益明细
 */
@Route(path = "/app/IncomeDetailActivity")
public class IncomeDetailActivity extends BaseActivity {
    public static final String TAG = IncomeDetailActivity.class.getSimpleName();
    @BindView(R.id.actionbar_back_tv)
    AppCompatTextView mActionbarBackTv;
    @BindView(R.id.actionbar_title_tv)
    AppCompatTextView mActionbarTitleTv;
    @BindView(R.id.actionbar_right_tv)
    AppCompatTextView mActionbarRightTv;
    @BindView(R.id.actionbar_rl)
    RelativeLayout mActionbarRl;
    @BindView(R.id.condition_first_tv)
    TextView mConditionFirstTv;
    @BindView(R.id.condition_second_tv)
    TextView mConditionSecondTv;
    @BindView(R.id.income_detail_recyclerView)
    RecyclerView mIncomeDetailRecyclerView;
    @BindView(R.id.empty_tv)
    TextView mEmptyTv;
    @BindView(R.id.loading_layout)
    View mLoadingLayout;
    @BindView(R.id.fail_retry)
    View mFailRetry;
    private IncomeDetailAdapter mIncomeDetailAdapter;
    private LoadMoreHelperRx<IncomeDetailsVO, Integer> mLoadMoreHelper;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean hasLoadMore;
    private List<String> platformTypeList;
    private List<String> profitTypeList;
    private int sortType = CommonConst.E_UPLIMIT_SORT_DOWN;
    private int fansType = 0; // 礼包： 0全部 1专属 2普通
    private int platformType = 0;// 导购： 0 全部 1淘宝 2京东 3拼多多
    private int profitType = 2;  // 收益类型 1礼包 2导购


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_detail_layout);

        initImmersionBar(Color.WHITE,true);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        platformTypeList = new ArrayList<>();
        platformTypeList.add("导购收益");
        platformTypeList.add("淘宝");
        platformTypeList.add("京东");
        platformTypeList.add("拼多多");
        profitTypeList = new ArrayList<>();
        profitTypeList.add("礼包收益");
        profitTypeList.add("专属粉丝");
        profitTypeList.add("普通粉丝");
        mActionbarRl.setBackgroundColor(Color.WHITE);
        mActionbarBackTv.setText("");
        mActionbarTitleTv.setText(R.string.income_record_detail_actionbar_title_text);
        DividerItemDecoration2 mDividerItemDecoration = new DividerItemDecoration2(this, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDividerHeight(DeviceUtil.dip2px(this, 8));
        mDividerItemDecoration.setDividerColor(getResources().getColor(R.color.color_F2F2F5));
        mIncomeDetailRecyclerView.addItemDecoration(mDividerItemDecoration);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mIncomeDetailRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIncomeDetailAdapter = new IncomeDetailAdapter(this);
        mIncomeDetailRecyclerView.setAdapter(mIncomeDetailAdapter);
        mConditionFirstTv.setText("导购收益");
        mConditionSecondTv.setText("礼包收益");
        loadRecyclerView();
    }


    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<IncomeDetailsVO, Integer>(this)
                .adapter(mIncomeDetailAdapter)
                .recyclerView(mIncomeDetailRecyclerView)
                .emptyView(mEmptyTv)
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .noMoreText(getResources().getText(R.string.empty_load_more_text))
                .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<IncomeDetailsVO, Integer>() {
                    @Override
                    public Observable<? extends Collection<IncomeDetailsVO>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load requestIncomeDetail");

                        return ApiService.getIncomeApi().requestIncomeDetail(sortType, integer, pageSize, fansType, platformType, profitType)
                                .observeOn(AndroidSchedulers.mainThread())//指定下面map在主线程
                                .map(new Function<BaseBean<List<IncomeDetailsVO>>, Collection<IncomeDetailsVO>>() {
                                    @Override
                                    public Collection<IncomeDetailsVO> apply(BaseBean<List<IncomeDetailsVO>> listBaseBean) throws Exception {
                                        dismissLoadingDialog();
                                        if (listBaseBean.success()) {
                                            mIncomeDetailAdapter.setProfitType(profitType);
                                            List<IncomeDetailsVO> list = listBaseBean.data;
                                            boolean listEmpty = ArraysUtils.isListEmpty(list);
                                            hasLoadMore = !listEmpty && list.size() == pageSize;
                                            return list;
                                        }
                                        throw new Exception(listBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<IncomeDetailsVO> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();

        mScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    mLoadMoreHelper.loadDataMore();
                }
            }
        };

        mIncomeDetailRecyclerView.addOnScrollListener(mScrollListener);
        mLoadMoreHelper.loadData();

    }


    @OnClick({R.id.actionbar_back_tv, R.id.condition_first_tv, R.id.condition_second_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.condition_first_tv:
                //导购
                showPopupWindow(mConditionFirstTv, platformTypeList);
                break;
            case R.id.condition_second_tv:
                //礼包
                showPopupWindow(mConditionSecondTv, profitTypeList);
                break;
        }
    }

    /**
     * 类型选择
     *
     * @param view
     * @param list
     */
    private void showPopupWindow(TextView view, List<String> list) {
        view.setSelected(true);
        InComeDetailPopupWindow inComeDetailPopupWindow = new InComeDetailPopupWindow(this, list,DeviceUtil.dip2px(this, 92));
        inComeDetailPopupWindow.setOnItemClickPopListerner(new InComeDetailPopupWindow.OnItemClickInterface() {
            @Override
            public void itemClick(String txt, int position) {
                view.setText(txt);
                if (view == mConditionFirstTv) {
                    //导购
                    profitType = 2;
                    platformType = position;
                    ViewUtil.setDrawableOfTextView(mConditionFirstTv,R.drawable.income_detail_select_down, ViewUtil.DrawableDirection.RIGHT);
                    ViewUtil.setDrawableOfTextView(mConditionSecondTv,R.drawable.income_detail_down, ViewUtil.DrawableDirection.RIGHT);

                    mConditionFirstTv.setTextColor(Color.parseColor("#D8BC8A"));
                    mConditionSecondTv.setTextColor(getResources().getColor(R.color.color_999999));
                } else {
                    //礼包
                    profitType = 1;
                    fansType = position;
                    ViewUtil.setDrawableOfTextView(mConditionFirstTv,R.drawable.income_detail_down, ViewUtil.DrawableDirection.RIGHT);
                    ViewUtil.setDrawableOfTextView(mConditionSecondTv,R.drawable.income_detail_select_down, ViewUtil.DrawableDirection.RIGHT);

                    mConditionFirstTv.setTextColor(getResources().getColor(R.color.color_999999));
                    mConditionSecondTv.setTextColor(Color.parseColor("#D8BC8A"));
                }
                if (mLoadMoreHelper!=null){
                    showLoadingDialog();
                    mLoadMoreHelper.loadData();
                }
            }
        });
        inComeDetailPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                view.setSelected(false);
            }
        });
        inComeDetailPopupWindow.showAsDropDown(view, 2, 5);
    }

}
