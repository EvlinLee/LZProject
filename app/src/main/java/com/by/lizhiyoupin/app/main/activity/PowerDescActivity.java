package com.by.lizhiyoupin.app.main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.VipPowerBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.adapter.PowerDescAdapter;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/18 18:38
 * Summary: 荔枝权益
 */
@Route(path = "/app/PowerDescActivity")
public class PowerDescActivity extends BaseActivity implements View.OnClickListener {
public static final String TAG=PowerDescActivity.class.getSimpleName();
    private TextView mActionBarTitleTv;
    private TextView mActionBarRightTv;
    private TextView mActionBarBackTv;
    private RecyclerView mRecyclerView;
    private PowerDescAdapter mAdapter;
    private RelativeLayout actionbar_rl;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DeviceUtil.fullScreen(this,false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_desc_layout);
        StatusBarUtils.setColor( this, Color.parseColor("#FF160F0B"),0);

        initView();
        initData();
    }

    private void initView() {
        mActionBarTitleTv = findViewById(R.id.actionbar_title_tv);
        mActionBarRightTv = findViewById(R.id.actionbar_right_tv);
        mActionBarBackTv = findViewById(R.id.actionbar_back_tv);
        mRecyclerView = findViewById(R.id.power_rv);
        mActionBarBackTv.setOnClickListener(this);
        mActionBarTitleTv.setText(R.string.vip_power_actionbar_title_text);
        ViewUtil.setDrawableOfTextView(mActionBarRightTv, R.drawable.actionbar_refresh_white, ViewUtil.DrawableDirection.RIGHT);
        actionbar_rl = findViewById(R.id.actionbar_rl);
        actionbar_rl.setBackgroundColor(Color.parseColor("#FF160F0B"));
        ViewUtil.setDrawableOfTextView(mActionBarBackTv, R.drawable.direction_back_left_white, ViewUtil.DrawableDirection.LEFT);
        mActionBarBackTv.setTextColor(Color.WHITE);
        mActionBarTitleTv.setTextColor(Color.WHITE);
        mActionBarRightTv.setVisibility(View.GONE);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new PowerDescAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
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
            case R.id.actionbar_right_tv:
                //刷新
                initData();
                break;
        }
    }

    private void initData() {
        ApiService.getVipApi().requestVipPowerList("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<VipPowerBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<VipPowerBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()) {
                            LZLog.i(TAG, "requestVipPowerList success");
                            mAdapter.setListData(listBaseBean.data);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "requestVipPowerList error" + throwable);

                    }
                });
    }
}
