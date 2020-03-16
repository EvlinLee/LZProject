package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_umeng.share.manager.IShareManager;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;

import java.util.ArrayList;
import java.util.List;
/*
jyx
* 提现成功页面
* */
@Route(path = "/app/CashSuccessActivity")
public class CashSuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTitle,actionbar_back_tv,actionbar_right_tv,share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_success);
        initImmersionBar(Color.WHITE,true);
        initBar();
        initView();
    }

    private void initView() {

        actionbar_back_tv.setOnClickListener(this);
        actionbar_right_tv.setOnClickListener(this);
          share = findViewById(R.id.share);//分享
         share.setOnClickListener(this);
    }

    private void initBar() {

        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv= findViewById(R.id.actionbar_back_tv);
        actionbar_right_tv= findViewById(R.id.actionbar_right_tv);
        actionbar_right_tv.setText("完成");
        actionbar_back_tv.setText("");
        mTitle.setText("提现");
        actionbar_back_tv.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_right_tv:
               finish();
               break;
            case R.id.share:
                shareWithPlat();
                break;


        }

    }
    private void shareWithPlat() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
       String code = accountManager.getAccountInfo().getCode();
        List<ChannelListBean> channelListBeans = new ArrayList<>();

        ChannelListBean channelListBean = new ChannelListBean();
        ChannelListBean channelListBean2 = new ChannelListBean();
        channelListBean.setCode(0);
        channelListBean.setName("微信");
        channelListBean.setShareTitle("快来和我一起省钱赚钱吧");
        channelListBean.setShareType(2);
        channelListBean.setDesc("大品牌有保障，隐藏优惠不用抢，天天最低价，随心买，任性赚！");
        channelListBean.setLink(WebUrlManager.getRegisterShareUrl(code));
        channelListBeans.add(channelListBean);

        channelListBean2.setCode(1);
        channelListBean2.setName("朋友圈");
        channelListBean2.setShareTitle("快来和我一起省钱赚钱吧");
        channelListBean2.setShareType(2);
        channelListBean2.setDesc("大品牌有保障，隐藏优惠不用抢，天天最低价，随心买，任性赚！");
        channelListBean2.setLink(WebUrlManager.getRegisterShareUrl(code));
        channelListBeans.add(channelListBean2);


        final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                .getManager(IShareManager.class.getName());
        if (shareManager == null) {
            return;
        }
        shareManager.showShareDialog(this, channelListBeans);
    }

}
