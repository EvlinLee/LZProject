package com.by.lizhiyoupin.app.search.activity;

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
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.main.activity.VideoPlayerActivity;
import com.by.lizhiyoupin.app.search.adapter.SearchHistoryAdapter;
import com.by.lizhiyoupin.app.search.adapter.SearchHotAdapter;
import com.by.lizhiyoupin.app.search.contract.SearchContract;
import com.by.lizhiyoupin.app.search.presenter.SearchHotPresenter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/30 15:55
 * Summary: 搜索页面
 */
@Route(path = "/app/SearchActivity")
public class SearchActivity extends BaseMVPActivity<SearchContract.SearchView, SearchContract.SearchPresenters> implements View.OnClickListener, SearchContract.SearchView {
    public static final String TAG = SearchActivity.class.getSimpleName();
    private TextView mActionbarSearchTv;
    private EditText mActionbarSearchEt;
    private ImageView mImageIv;
    private ImageView mDeteleIv;
    private RecyclerView mHistoryRecyclerView;
    private RecyclerView mHotRecyclerView;
    private FlexboxLayoutManager mHistoryFlexboxLayoutManager;
    private FlexboxLayoutManager mHotFlexboxLayoutManager;
    private SearchHistoryAdapter mHistoryAdapter;
    private SearchHotAdapter mHotAdapter;
    private List<String> mHistoryList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        initImmersionBar(Color.WHITE,true);
        initList();
        initView();
        basePresenter.requestSearchHotList();
        DeviceUtil.showInputMethodDelay(mActionbarSearchEt, 500);
    }

    @Override
    public SearchContract.SearchPresenters getBasePresenter() {
        return new SearchHotPresenter(this);
    }

    private void initList() {
        mHistoryList = SPUtils.getDefault().getListData(CommonConst.KEY_SAVE_SEARCH_HISTORY);
        if (mHistoryList == null) {
            mHistoryList = new ArrayList<>();
        }
        if (mHistoryList.size() > 30) {
            //超过30条不要了
            mHistoryList = mHistoryList.subList(0, 30);
        }

    }

    private void initView() {
        findViewById(R.id.actionbar_search_back_iv).setVisibility(View.GONE);
        mActionbarSearchEt = findViewById(R.id.actionbar_search_et);
        mImageIv = findViewById(R.id.image_iv);
        mDeteleIv = findViewById(R.id.delete_iv);
        Glide.with(this).load("http://static.lizhiyoupin.com/course/LitchiCourse_IMG.png")
                .error(R.drawable.empty_pic_list_h).placeholder(R.drawable.empty_pic_list_h)
                .into(mImageIv);
        mHistoryRecyclerView = findViewById(R.id.search_history_rv);
        mHotRecyclerView = findViewById(R.id.search_recommend_rv);
        mActionbarSearchTv = findViewById(R.id.actionbar_search_cancel_tv);
        mActionbarSearchEt.setBackground(getResources().getDrawable(R.drawable.edit_bussiness_search_corners_four_shape));
        mActionbarSearchTv.setOnClickListener(this);
        findViewById(R.id.video_play_fl).setOnClickListener(this);
        mDeteleIv.setOnClickListener(this);
        mActionbarSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = mActionbarSearchEt.getText().toString();
                    if (TextUtils.isEmpty(search)) {
                        return false;
                    }
                    if (!mHistoryList.contains(search)){
                        mHistoryList.add(0,search);
                    }
                    //点击搜索的时候隐藏软键盘
                    DeviceUtil.hideInputMethod(v);
                    // 跳转到搜索结果页去搜索
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonConst.KEY_SEARCH_TITLE, search);
                    CommonSchemeJump.showActivity(SearchActivity.this, "/app/SearchResultActivity", bundle);
                    mHistoryAdapter.setListData(mHistoryList);
                    mHistoryAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

        mHistoryFlexboxLayoutManager = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
        mHistoryRecyclerView.setLayoutManager(mHistoryFlexboxLayoutManager);
        FlexboxItemDecoration hItemDecoration = new FlexboxItemDecoration(this);
        hItemDecoration.setDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return DeviceUtil.dip2px(SearchActivity.this, 10);
            }

            @Override
            public int getIntrinsicHeight() {
                return DeviceUtil.dip2px(SearchActivity.this, 10);
            }
        });
        mHistoryRecyclerView.addItemDecoration(hItemDecoration);
        mHistoryFlexboxLayoutManager.setMaxLine(4);//实际行数要-1
        mHistoryAdapter = new SearchHistoryAdapter(this);
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);
        mHistoryAdapter.setListData(mHistoryList);


        mHotFlexboxLayoutManager = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
        mHotRecyclerView.setLayoutManager(mHotFlexboxLayoutManager);
        FlexboxItemDecoration flexboxItemDecoration = new FlexboxItemDecoration(this);
        flexboxItemDecoration.setDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return DeviceUtil.dip2px(SearchActivity.this, 10);
            }

            @Override
            public int getIntrinsicHeight() {
                return DeviceUtil.dip2px(SearchActivity.this, 10);
            }
        });
        mHotRecyclerView.addItemDecoration(flexboxItemDecoration);
        mHotFlexboxLayoutManager.setMaxLine(4);//实际行数要-1
        mHotAdapter = new SearchHotAdapter(this,mHistoryList,mHistoryAdapter);
        mHotRecyclerView.setAdapter(mHotAdapter);
    }



    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()){
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_search_cancel_tv:
                finish();
                break;
            case R.id.video_play_fl:
                startActivity(new Intent(this, VideoPlayerActivity.class));
                break;
            case R.id.delete_iv:
                mHistoryList.clear();
                mHistoryAdapter.setListData(null);
                mHistoryAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void requestSearchHotListSuccess(List<String> hotList) {
        LZLog.i(TAG, "requestSearchHotListSuccess==");
        mHotAdapter.setListData(hotList);
        mHotAdapter.notifyDataSetChanged();
    }

    @Override
    public void requestSearchHotListError(Throwable throwable) {
        LZLog.i(TAG, "requestSearchHotListError==" + throwable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mActionbarSearchEt != null) {
            DeviceUtil.hideInputMethod(mActionbarSearchEt);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.getDefault().putListData(CommonConst.KEY_SAVE_SEARCH_HISTORY, mHistoryList);
    }
}
