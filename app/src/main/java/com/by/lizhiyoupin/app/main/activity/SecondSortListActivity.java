package com.by.lizhiyoupin.app.main.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.main.fragment.SecondSortListFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/29 17:42
 * Summary: 二级列表页
 */
@Route(path = "/app/SecondSortListActivity")
public class SecondSortListActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitleTv;
    private String titleName;
    private long secondKindId=-1;
    private SecondSortListFragment mSecondSortListFragment;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_list_layout);
        initImmersionBar(Color.WHITE,true);
        Intent intent = getIntent();
        titleName = intent.getStringExtra(CommonConst.KEY_ACTIONBAR_TITLE);
        secondKindId = intent.getLongExtra(CommonConst.KEY_SECOND_COMMODITY_ID, -1);
        initView();
        initProductSortListFragment();
    }

    private void initView() {
        TextView backTv = findViewById(R.id.actionbar_back_tv);
        backTv.setText("");
        backTv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.common_back), null, null, null);
        backTv.setOnClickListener(this);
        mTitleTv = findViewById(R.id.actionbar_title_tv);
        mTitleTv.setText(titleName);
    }

    private void initProductSortListFragment() {
        mSecondSortListFragment = new SecondSortListFragment();
        Bundle bundle=new Bundle();
        bundle.putLong(CommonConst.KEY_SECOND_COMMODITY_ID,secondKindId);
        mSecondSortListFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_fl, mSecondSortListFragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.actionbar_back_tv:
                finish();
                break;
        }
    }


}
