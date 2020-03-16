package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;

/*
 *jyx
 * 订单页面
 * */
@Route(path = "/app/OrderActivity")
public class OrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView actionbar_back_tv, order_selfpurchase, order_team, order_selfsupport;
    private RelativeLayout actionbar_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initImmersionBar(Color.WHITE, true);
        initView();


    }

    private void initView() {

        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        TextView actionbar_title_tv = findViewById(R.id.actionbar_title_tv);
        actionbar_title_tv.setText("我的订单");
        actionbar_back_tv.setText("");
        actionbar_back_tv.setOnClickListener(this);
        order_selfpurchase = findViewById(R.id.order_selfpurchase);//自购订单
        order_selfpurchase.setOnClickListener(this);
        order_team = findViewById(R.id.order_team);//团队订单
        order_team.setOnClickListener(this);
        order_selfsupport = findViewById(R.id.order_selfsupport);//自营订单
        order_selfsupport.setOnClickListener(this);
        actionbar_rl = findViewById(R.id.actionbar_rl);
        actionbar_rl.setBackgroundColor(Color.WHITE);

    }


    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.order_selfpurchase:
                bundle.putInt(CommonConst.ORDER_SELFPURCHASE,1);
                CommonSchemeJump.showActivity(this, "/app/OrderQueryActivity",bundle);
                break;
            case R.id.order_team:
                bundle.putInt(CommonConst.ORDER_SELFPURCHASE,2);
                CommonSchemeJump.showActivity(this, "/app/OrderQueryActivity",bundle);
                break;
            case R.id.order_selfsupport:
                CommonSchemeJump.showActivity(this, "/app/OrderSupportActivity",bundle);
                break;
        }
    }
}
