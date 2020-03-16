package com.by.lizhiyoupin.app.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.EndlessRecyclerOnScrollListener;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FansDataBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.IPageStrategy;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.loader.facade.LoadMoreBuilderRx;
import com.by.lizhiyoupin.app.user.adapter.PushFansSearchListAdapter;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/2 09:53
 * Summary: 运营商推送管理添加 推送粉丝列表
 */
@Route(path = "/app/PushAddFansActivity")
public class PushAddFansActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    public static final String TAG = PushAddFansActivity.class.getSimpleName();
    private TextView mBackTv;
    private TextView mTitleTv;
    private TextView mSubmitTv;
    private TextView mInviteTv;
    private Group mGroup;
    private TextView mSelectAllTv;
    private TextView mSearchCancelTv;
    private EditText mSearchEt;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTv;
    private View mLoadingLayout;
    private View mFailRetry;

    private LoadMoreHelperRx<FansDataBean.FansListBean, Integer> mLoadMoreHelper;
    private boolean hasLoadMore;
    private PushFansSearchListAdapter mAllListAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<FansDataBean.FansListBean> mFansSelectListBeans;
    //用户判断 当前列表是否包含点击选中条目（不适用id的话，需要重写FansDataBean.FansListBean equals方法已id为相等判断条件）
    private ArrayList<Long> mFansSelectIdsList;
    //保存记录不是搜索数据的第一页，用来全选时取出前3个bean
    private ArrayList<FansDataBean.FansListBean> unSearchfirstPageList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_add_fans_layout);
        initImmersionBar(Color.WHITE, true);
        Intent intent = getIntent();
        boolean selectAll = intent.getBooleanExtra(CommonConst.KEY_FANS_SELECT_All, false);
        LZLog.i(TAG, "selectAll==" + selectAll);
        if (selectAll) {
            //全选时，不用传输数据
            mFansSelectIdsList = new ArrayList<>();
            mFansSelectListBeans = new ArrayList<>();
        } else {
            //为全选时，获取传输数据
            ArrayList<Long> serializableExtra = (ArrayList<Long>) intent.getSerializableExtra(CommonConst.KEY_FANS_SELECT_IDS);
            if (serializableExtra != null) {
                mFansSelectIdsList = serializableExtra;
            } else {
                mFansSelectIdsList = new ArrayList<>();
            }
            ArrayList<FansDataBean.FansListBean> fansListBeans = intent.getParcelableArrayListExtra(CommonConst.KEY_FANS_SELECT);
            if (fansListBeans != null) {
                mFansSelectListBeans = fansListBeans;
            } else {
                mFansSelectListBeans = new ArrayList<>();
            }
        }
        initView(selectAll);
    }

    private void initView(boolean selectAll) {

        findViewById(R.id.actionbar_rl).setBackgroundColor(Color.WHITE);
        findViewById(R.id.search_ll).setBackgroundColor(Color.WHITE);
        mBackTv = findViewById(R.id.actionbar_back_tv);
        mTitleTv = findViewById(R.id.actionbar_title_tv);
        mSubmitTv = findViewById(R.id.submit_tv);
        mGroup = findViewById(R.id.normal_group);
        mInviteTv = findViewById(R.id.invite_tv);
        mSelectAllTv = findViewById(R.id.select_all_tv);
        mSearchCancelTv = findViewById(R.id.actionbar_search_cancel_tv);
        mRecyclerView = findViewById(R.id.recyclerView);
        mSearchEt = findViewById(R.id.actionbar_search_et);

        mEmptyTv = findViewById(R.id.empty_tv);
        mFailRetry = findViewById(R.id.fail_retry);
        mLoadingLayout = findViewById(R.id.loading_layout);

        findViewById(R.id.actionbar_search_back_iv).setVisibility(View.GONE);
        mBackTv.setText("");
        mTitleTv.setText("添加粉丝");
        mSearchCancelTv.setText("取消");
        mSearchEt.setHint("可输入粉丝手机号搜索粉丝");
        mEmptyTv.setText("您还没有粉丝信息，先去邀请粉丝吧~");
        ViewUtil.setDrawableOfTextView(mEmptyTv, R.drawable.empty_fans_pic, ViewUtil.DrawableDirection.TOP);
        mSearchEt.setOnEditorActionListener(this);
        mBackTv.setOnClickListener(this);
        mSubmitTv.setOnClickListener(this);
        mInviteTv.setOnClickListener(this);
        mSelectAllTv.setOnClickListener(this);
        mSearchCancelTv.setOnClickListener(this);
        mSelectAllTv.setSelected(selectAll);
        mSelectAllTv.setText(selectAll ? "取消全选" : "全选");
        mSubmitTv.setEnabled(selectAll || mFansSelectIdsList.size() > 0);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(this, RecyclerView.VERTICAL);
        dividerItemDecoration2.setDividerHeight(DeviceUtil.dip2px(this, 8));
        dividerItemDecoration2.setDividerColor(Color.TRANSPARENT);
        mRecyclerView.addItemDecoration(dividerItemDecoration2);
        mAllListAdapter = new PushFansSearchListAdapter(this, getSupportFragmentManager(),
                mFansSelectListBeans, mFansSelectIdsList, mSubmitTv, mSelectAllTv);
        mRecyclerView.setAdapter(mAllListAdapter);
        loadRecyclerView();

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
            case R.id.actionbar_search_cancel_tv:
                //取消
                mSearchEt.setText("");
                DeviceUtil.hideInputMethod(mSearchEt);
                mLoadMoreHelper.loadData();
                break;
            case R.id.select_all_tv:
                //全选
                mSelectAllTv.setSelected(!mSelectAllTv.isSelected());
                boolean nextSelect = mSelectAllTv.isSelected();
                mSelectAllTv.setText(nextSelect ? "取消全选" : "全选");
                if (nextSelect) {
                    //设置了全选
                    mFansSelectListBeans.clear();
                    mFansSelectIdsList.clear();
                    mSubmitTv.setEnabled(true);
                } else {
                    if (ArraysUtils.isListEmpty(mFansSelectIdsList)) {
                        //取消全选状态下，粉丝没有单选的则不可提交
                        mSubmitTv.setEnabled(false);
                    } else {
                        mSubmitTv.setEnabled(true);
                    }
                }
                //刷新选中状态
                mAllListAdapter.notifyDataSetChanged();

                break;
            case R.id.submit_tv:
                //提交
                submit();
                break;
            case R.id.invite_tv:
                //邀请好友
                CommonSchemeJump.showActivity(this, "/app/InvateActivity");
                break;
            default:
                break;
        }
    }

    private void submit() {
        Intent intent = new Intent();

        if (mSelectAllTv.isSelected()) {
            //全选状态下，传送前3个
            intent.putExtra(CommonConst.KEY_FANS_SELECT_All, true);
            ArrayList<FansDataBean.FansListBean> sendBeans = new ArrayList<>();
            if (unSearchfirstPageList != null && unSearchfirstPageList.size() > 3) {
                for (int i = 0; i < 3; i++) {
                    sendBeans.add(unSearchfirstPageList.get(0));
                }
                intent.putParcelableArrayListExtra(CommonConst.KEY_FANS_SELECT, sendBeans);
            } else {
                intent.putParcelableArrayListExtra(CommonConst.KEY_FANS_SELECT, unSearchfirstPageList);
            }
        } else {
            //未全选时
            intent.putParcelableArrayListExtra(CommonConst.KEY_FANS_SELECT, mFansSelectListBeans);
            intent.putExtra(CommonConst.KEY_FANS_SELECT_IDS, mFansSelectIdsList);
            intent.putExtra(CommonConst.KEY_FANS_SELECT_All, false);
        }

        setResult(PushEditorActivity.PUSH_EDIT_REQUEST_CODE, intent);
        finish();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            //点击搜索的时候隐藏软键盘
            DeviceUtil.hideInputMethod(v);
            mLoadMoreHelper.loadData();
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSearchEt != null) {
            DeviceUtil.hideInputMethod(mSearchEt);
        }
    }



    private void loadRecyclerView() {
        mLoadMoreHelper = new LoadMoreBuilderRx<FansDataBean.FansListBean, Integer>(this)
                .adapter(mAllListAdapter)
                .recyclerView(mRecyclerView)
                .emptyView(mEmptyTv)
                .emptyLoadingView(mLoadingLayout)
                .emptyRetryView(mFailRetry)
                .pageStrategy(new IPageStrategy.IndexPageStrategy<>(1, 20))
                .loader(new LoadMoreHelperRx.Loader<FansDataBean.FansListBean, Integer>() {
                    @Override
                    public Observable<? extends Collection<FansDataBean.FansListBean>> load(Integer integer, int pageSize) {
                        LZLog.i(TAG, "load requestGetFansList==" + integer);
                        String search = mSearchEt.getText().toString().trim();
                        return ApiService.getFansApi().requestGetFansList(0, mSearchEt.getText().toString(), 2, 0, integer, pageSize)
                                .observeOn(AndroidSchedulers.mainThread())//map主线程
                                .map(new Function<BaseBean<FansDataBean>, Collection<FansDataBean.FansListBean>>() {
                                    @Override
                                    public Collection<FansDataBean.FansListBean> apply(BaseBean<FansDataBean> beanBaseBean) throws Exception {

                                        if (beanBaseBean.success() && beanBaseBean.data != null) {
                                            List<FansDataBean.FansListBean> list = beanBaseBean.data.getFansList();
                                            boolean listEmpty = ArraysUtils.isListEmpty(list);
                                            hasLoadMore = !listEmpty && list.size() == pageSize;

                                            if (integer <= 1 && mFansSelectIdsList.size() == 0
                                                    && !listEmpty && !mSelectAllTv.isSelected()) {
                                                //没有全选时 默认选中第一个
                                                FansDataBean.FansListBean fansListBean = list.get(0);
                                                mFansSelectIdsList.add(fansListBean.getUid());
                                                mFansSelectListBeans.add(fansListBean);
                                                mSubmitTv.setEnabled(true);
                                            }
                                            if (integer <= 1 ) {
                                                if (TextUtils.isEmpty(search)){
                                                    //正常数据的第一页，保存用来全选时取出前3个bean
                                                    unSearchfirstPageList = (ArrayList<FansDataBean.FansListBean>) list;
                                                    if (listEmpty) {
                                                        mGroup.setVisibility(View.GONE);
                                                        mEmptyTv.setText("您还没有粉丝信息，先去邀请粉丝吧~");
                                                        ViewUtil.setDrawableOfTextView(mEmptyTv, R.drawable.empty_fans_pic, ViewUtil.DrawableDirection.TOP);
                                                        mInviteTv.setVisibility(View.VISIBLE);
                                                    } else {
                                                        mGroup.setVisibility(View.VISIBLE);
                                                        mInviteTv.setVisibility(View.GONE);
                                                    }
                                                }else {
                                                    //搜索
                                                    mEmptyTv.setText("");
                                                    ViewUtil.setDrawableOfTextView(mEmptyTv, null, ViewUtil.DrawableDirection.TOP);
                                                }

                                            }


                                            return list;
                                        }
                                        if (integer <= 1 && TextUtils.isEmpty(search)) {
                                            mGroup.setVisibility(View.GONE);
                                            mInviteTv.setVisibility(View.VISIBLE);
                                        }
                                        throw new Exception(beanBaseBean.msg);
                                    }
                                });
                    }

                    @Override
                    public boolean hasMore(Collection<FansDataBean.FansListBean> data, int pageCount) {
                        return hasLoadMore;
                    }
                }).build();

        EndlessRecyclerOnScrollListener mScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (hasLoadMore) {
                    LZLog.i(TAG, "onLoadMore requestGetFansList" + mLoadMoreHelper.curPage());
                    mLoadMoreHelper.loadDataMore();
                }
            }
        };

        mRecyclerView.addOnScrollListener(mScrollListener);
        mLoadMoreHelper.loadData();
    }

}
