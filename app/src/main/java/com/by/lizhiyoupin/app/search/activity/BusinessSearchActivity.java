package com.by.lizhiyoupin.app.search.activity;

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
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.search.adapter.SearchHistoryAdapter;
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
 * @date :  2019/11/21 15:38
 * Summary: 商学院搜索
 */
@Route(path = "/app/BusinessSearchActivity")
public class BusinessSearchActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = BusinessSearchActivity.class.getSimpleName();
    private TextView mSearchTv;
    private EditText mSearchEt;
    private ImageView mDeleteTv;

    private RecyclerView mHistoryRecyclerView;
    private FlexboxLayoutManager mHistoryFlexboxLayoutManager;
    private SearchHistoryAdapter mHistoryAdapter;
    private List<String> mHistoryList;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_search_layout);
        initImmersionBar(Color.WHITE,true);
        initList();
        initView();
        DeviceUtil.showInputMethodDelay(mSearchEt, 500);
    }


    private void initList() {
        mHistoryList = SPUtils.getDefault().getListData(CommonConst.KEY_SAVE_BUSINESS_SEARCH_HISTORY);
        if (mHistoryList == null) {
            mHistoryList = new ArrayList<>();
        }
        if (mHistoryList.size() > 30) {
            //超过30条不要了
            mHistoryList = mHistoryList.subList(0, 30);
        }

    }

    private void initView() {
        TextView backTv = findViewById(R.id.actionbar_back_tv);
        TextView titleTv = findViewById(R.id.actionbar_title_tv);

        mHistoryRecyclerView = findViewById(R.id.search_history_rv);

        mDeleteTv = findViewById(R.id.delete_iv);
        mSearchEt = findViewById(R.id.search_et);
        mSearchTv = findViewById(R.id.search_tv);
        mSearchTv.setOnClickListener(this);
        mDeleteTv.setOnClickListener(this);
        backTv.setOnClickListener(this);
        titleTv.setText(R.string.business_title_text);
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    return searchData();
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
                return DeviceUtil.dip2px(BusinessSearchActivity.this, 10);
            }

            @Override
            public int getIntrinsicHeight() {
                return DeviceUtil.dip2px(BusinessSearchActivity.this, 10);
            }
        });
        mHistoryRecyclerView.addItemDecoration(hItemDecoration);
        mHistoryFlexboxLayoutManager.setMaxLine(4);//实际行数要-1
        mHistoryAdapter = new SearchHistoryAdapter(this);
        mHistoryAdapter.setIsBusiness(true);
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);
        mHistoryAdapter.setListData(mHistoryList);
    }

    private boolean searchData() {
        String search = mSearchEt.getText().toString();
        if (TextUtils.isEmpty(search)) {
            return false;
        }
        if (!mHistoryList.contains(search)) {
            mHistoryList.add(0,search);
        }
        //点击搜索的时候隐藏软键盘
        DeviceUtil.hideInputMethod(mSearchEt);
        //  商学院搜索结果
        CommonSchemeJump.showBusinessSearchResultActivity(this, search);
        mHistoryAdapter.setListData(mHistoryList);
        mHistoryAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()){
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.search_tv:
                searchData();
                break;
            case R.id.delete_iv:
                mHistoryList.clear();
                mHistoryAdapter.setListData(null);
                mHistoryAdapter.notifyDataSetChanged();
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSearchEt!=null){
            DeviceUtil.hideInputMethod(mSearchEt);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.getDefault().putListData(CommonConst.KEY_SAVE_BUSINESS_SEARCH_HISTORY, mHistoryList);
    }
}
