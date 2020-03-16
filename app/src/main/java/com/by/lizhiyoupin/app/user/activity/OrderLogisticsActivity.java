package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.OrderLogisticsBean;
import com.by.lizhiyoupin.app.io.bean.TracesBean;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
import com.by.lizhiyoupin.app.user.adapter.OrderLogiticsAdapter;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
 *jyx
 * 自营订单物流查询页面
 * */
@Route(path = "/app/OrderLogisticsActivity")
public class OrderLogisticsActivity extends BaseActivity implements View.OnClickListener {
    private TextView actionbar_back_tv, logistics_title, logistics_phone, logistics_orderid,
            empty_tv;
    private RelativeLayout actionbar_rl;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private OrderLogiticsAdapter mSupportAdapter;
    private ImageView logistics_img;
    private String company, code;
    private List<TracesBean> traces;
    private ConstraintLayout order_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_logistics);
        initImmersionBar(Color.WHITE, true);
        company = getIntent().getStringExtra(CommonConst.ORDER_SELFSUPPORADDRESS);
        code = getIntent().getStringExtra(CommonConst.ORDER_SELFSUPPORNUMBER);
        initView();
    }

    private void initView() {
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        TextView actionbar_title_tv = findViewById(R.id.actionbar_title_tv);
        actionbar_title_tv.setText("物流信息");
        actionbar_back_tv.setText("");
        actionbar_back_tv.setOnClickListener(this);
        actionbar_rl = findViewById(R.id.actionbar_rl);
        actionbar_rl.setBackgroundColor(Color.WHITE);

        empty_tv = findViewById(R.id.empty_tv);
        order_company = findViewById(R.id.order_company);

        logistics_img = findViewById(R.id.logistics_img);
        logistics_title = findViewById(R.id.logistics_title);
        logistics_phone = findViewById(R.id.logistics_phone);
        logistics_orderid = findViewById(R.id.logistics_orderid);

        mRecyclerView = findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        loadRecyclerView();
    }

    private void loadRecyclerView() {
        SettingRequestManager.requestOrderLogistics(company, code/*"圆通速递","YT4339534352800"*/)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<OrderLogisticsBean>>() {
                    @Override
                    public void onNext(BaseBean<OrderLogisticsBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            return;
                        }
                        mRecyclerView.setVisibility(View.VISIBLE);
                        empty_tv.setVisibility(View.GONE);
                        order_company.setVisibility(View.VISIBLE);
                        OrderLogisticsBean result = userInfoBeanBaseBean.getResult();
                        if (result == null) {
                            return;
                        }
                        traces = result.getTraces();
                        mSupportAdapter = new OrderLogiticsAdapter(OrderLogisticsActivity.this,
                                traces);
                        mRecyclerView.setAdapter(mSupportAdapter);
                        logistics_orderid.setText(result.getShipperName() + " " + result.getLogisticCode());
                        logistics_phone.setText("官方电话 " + result.getShipperPhone() + " >");
                        logistics_title.setText(result.getShipperName());
                        Glide.with(OrderLogisticsActivity.this).load(result.getShipperImg()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(logistics_img);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "requestLitmitSkillTitle error==" + throwable);
                        mRecyclerView.setVisibility(View.GONE);
                        empty_tv.setVisibility(View.VISIBLE);
                        order_company.setVisibility(View.GONE);

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
        }
    }
}
