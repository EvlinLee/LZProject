package com.by.lizhiyoupin.app.detail.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.main.adapter.DetailSelectionListAdapter;
import com.by.lizhiyoupin.app.main.manager.TabFragmentManager;

import java.util.Collection;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

@Route(path = "/app/PaySuccessActivity")
public class PaySuccessActivity extends BaseActivity implements View.OnClickListener {
    //猜你喜欢
    private TextView back_home;
    private RecyclerView mRecyclerView;
    private DetailSelectionListAdapter mAdapter;
    private LoadMoreHelperRx<PreciseListBean, Integer> mLoadMoreHelper;
    private boolean hasMore;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DeviceUtil.fullScreen(this, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        StatusBarUtils.setColor(this, Color.TRANSPARENT, 0);
        initView();
    }

    private void initView() {
        back_home = findViewById(R.id.back_home);
        back_home.setOnClickListener(this);
        mRecyclerView=findViewById(R.id.guess_you_like_list_rv);
        mAdapter = new DetailSelectionListAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DeviceUtil.dip2px(this, 5), 2));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //每行item数据=spanCount/getSpanSize
                //底部最后一个显示加载更多 或暂无数据
                return mLoadMoreHelper.getData().size() == position ? 2 : 1;
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mLoadMoreHelper = new LoadMoreBuilderRx<PreciseListBean, Integer>(this)
                .recyclerView(mRecyclerView)
                .adapter(mAdapter)
//                .emptyView(mEmpty)
                .noMoreText("已经到底啦")
                .noMoreLayoutColorRes(R.color.color_F4F4F7)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<PreciseListBean>(1, 10))
                .loader(new LoadMoreHelperRx.Loader<PreciseListBean, Integer>() {

                    @Override
                    public Observable<? extends Collection<PreciseListBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "integer==" + integer);
                        LZLog.i(TAG, "pageSize==" + pageSize);
                        String uniqueId = DeviceUtil.getImei(ContextHolder.getInstance().getContext());
                        return ApiService.getHomeApi().requestPreciseSelectionList(integer, pageSize,uniqueId)
                                .map(new Function<BaseBean<List<PreciseListBean>>, Collection<PreciseListBean>>() {
                                    @Override
                                    public Collection<PreciseListBean> apply(BaseBean<List<PreciseListBean>> listBaseBean) throws Exception {
                                        if (listBaseBean.success()) {
                                            List<PreciseListBean> result = listBaseBean.getResult();
                                            hasMore = !ArraysUtils.isListEmpty(result);
                                            LZLog.i(TAG, "pageSize==success"+result);
                                            return result;
                                        }
                                        LZLog.i(TAG, "pageSize==error" );
                                        throw new Exception(listBaseBean.msg);

                                    }
                                })
                                ;
                    }

                    @Override
                    public boolean hasMore(Collection<PreciseListBean> data, int pageCount) {
                        return hasMore;
                    }
                }).build();

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasMore) {
                    LZLog.i(TAG, "requestGetFansList onLoadMore" + currentPage);
                    mLoadMoreHelper.loadDataMore();
                }
            }
        });
        mLoadMoreHelper.loadData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_home:
                checkExit();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            checkExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void checkExit() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        LoginRequestManager.requestGetuserinfo(accountManager.getAccountInfo().getApiToken());
        CommonSchemeJump.showMainActivity(this,
                TabFragmentManager.MAIN_HOME_PAGE_TAB_NAME, 0, 0);
    }


}
