package com.by.lizhiyoupin.app.user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.fragment.BaseFragment;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.IncomeReport;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.user.adapter.IncomeDataReportAdapter;
import com.by.lizhiyoupin.app.weight.week.OnTimeWeekSelectListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/7 19:38
 * Summary: 数据报表
 */
public class IncomeDataReportFragment extends BaseFragment implements OnTimeWeekSelectListener {
    public static final String TAG = IncomeDataReportFragment.class.getSimpleName();
    private Context mContext;
    private RecyclerView mRecyclerView;
    private View mEmptyLayout;
    private View mFailRetry;
    private View mloadingLayout;
    private SmartRefreshLayout mSmartRefreshLayout;
    private LoadMoreHelperRx<IncomeReport, Integer> mLoadMoreHelper;
    private IncomeDataReportAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int mTimeType;//0日报，1周报，2月报
    private int mTabType;//0上月（上周），1近3月（近3周），2 近6月（近6周）
    private String mDate;//2019-12-13
    private int type;// 0--单日 1--单周 2--单月 ,3--前几周 4--前几月
    private int num;//type类型012第几周、几月，type类型34向前几周或几月


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_income_data_record_layout, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTimeType = arguments.getInt(CommonConst.KEY_INCOME_TIME_TYPE, 0);
            mTabType = arguments.getInt(CommonConst.KEY_INCOME_TAB_SELECTED, 0);
        }
        mDate = TimeUtils.transForDate(System.currentTimeMillis(), "yyyy-MM-dd");
        initTypeData();
        initView(inflate);
        return inflate;
    }

    private void initTypeData() {
        switch (mTimeType) {
            case 0://日报
                type = 0;
                num = 1;
                break;
            case 1://周报
                switch (mTabType) {
                    case 0://（上周）
                        type = 3;
                        num = 1;
                        break;
                    case 1://（近3周）
                        type = 3;
                        num = 3;
                        break;
                    case 2://（近6周）
                        type = 3;
                        num = 6;
                        break;
                }
                break;
            case 2://月报
                switch (mTabType) {
                    case 0://上月
                        type = 4;
                        num =1;
                        break;
                    case 1://近3月
                        type = 4;
                        num = 3;
                        break;
                    case 2://近6月
                        type = 4;
                        num = 6;
                        break;
                }
                break;
        }
    }

    private void initView(View root) {
        mSmartRefreshLayout = root.findViewById(R.id.smartRefreshLayout);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mEmptyLayout = root.findViewById(R.id.empty_layout);
        mFailRetry = root.findViewById(R.id.fail_retry);
        mloadingLayout = root.findViewById(R.id.loading_layout);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(mContext, RecyclerView.VERTICAL);
        dividerItemDecoration2.setDividerHeight(DeviceUtil.dip2px(mContext, 15));
        dividerItemDecoration2.setDividerColor(ContextCompat.getColor(mContext, R.color.color_F2F2F5));
        mRecyclerView.addItemDecoration(dividerItemDecoration2);
        mAdapter = new IncomeDataReportAdapter(mContext, this, mTimeType, mTabType);
        mRecyclerView.setAdapter(mAdapter);
        setSmartRefreshLayout();
        loadRecyclerView();
    }


    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<IncomeReport, Integer>(mContext)
                .adapter(mAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyLayout)
                //.noMoreText(getResources().getText(R.string.empty_load_more_text))
                .emptyRetryView(mFailRetry)
                .emptyLoadingView(mloadingLayout)
                .refreshLayout(mSmartRefreshLayout)
              /*  .noMoreGravity(Gravity.CENTER)
                .noMoreLayoutColorRes(R.color.color_F2F2F5)*/
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<IncomeReport, Integer>() {
                    @Override
                    public Observable<? extends Collection<IncomeReport>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load seu"+integer);

                        return ApiService.getIncomeApi().requestIncomeReport(mDate, type, num)
                                .map(new Function<BaseBean<List<IncomeReport>>, Collection<IncomeReport>>() {
                                    @Override
                                    public Collection<IncomeReport> apply(BaseBean<List<IncomeReport>> listBaseBean) throws Exception {
                                        if (listBaseBean.success()) {
                                            return listBaseBean.data;
                                        }
                                        throw new Exception(listBaseBean.msg);
                                    }
                                });
                    }
                }).build();

        mLoadMoreHelper.loadData();
    }

    private void setSmartRefreshLayout(){
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mLoadMoreHelper!=null){
                    mLoadMoreHelper.loadData();
                }else {
                    mSmartRefreshLayout.finishRefresh(1000);
                }
            }
        });
        mSmartRefreshLayout.setEnableAutoLoadMore(false);
        mSmartRefreshLayout.setEnableLoadMore(false);
    }
    @Override
    public void onTimeWeekSelect(String date, int number, View v) {
        LZLog.i(TAG,"onTimeWeekSelect=="+date);
        LZLog.i(TAG,"onTimeWeekSelect number=="+number);
        mDate = date;
        num = number;
        if (mTimeType==1){
            //周报 ----"yyyy-MM-dd"
            type=1;
        }else if (mTimeType==2){
            //2月报
            type=2;
        }
        mLoadMoreHelper.loadData();
    }
}
