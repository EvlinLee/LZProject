package com.by.lizhiyoupin.app.main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.io.bean.InformationBean;
import com.by.lizhiyoupin.app.io.bean.UserMessageVO;
import com.by.lizhiyoupin.app.main.contract.MessageContract;
import com.by.lizhiyoupin.app.main.presenter.MessagePresenter;

import java.util.List;

/*
 * jyx
 * 消息页面
 * */
@Route(path = "/app/InformationActivity")
public class InformationActivity extends BaseMVPActivity<MessageContract.MessageView,
        MessageContract.MessagePresenters> implements MessageContract.MessageView,
        View.OnClickListener {
    private TextView mTitle, actionbar_back_tv, notice_time, notice_message, explosions_message,
            explosions_time,
            system_time, system_message, interactive_title1, interactive_message, interactive_time;
    private RelativeLayout explosions, notice, system, interactive;
    private ImageView img_notice, system_img, explosions_img, interactive_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initImmersionBar(Color.WHITE, true);
        initBar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        basePresenter.requestMainMessage();
    }

    @Override
    public MessageContract.MessagePresenters getBasePresenter() {
        return new MessagePresenter(this);
    }

    private void initView() {
        actionbar_back_tv.setOnClickListener(this);
        explosions = findViewById(R.id.explosions);
        explosions.setOnClickListener(this);//每日爆款
        notice = findViewById(R.id.notice);
        notice.setOnClickListener(this);//佣金
        system = findViewById(R.id.system);
        system.setOnClickListener(this);//系统消息

        interactive = findViewById(R.id.interactive);
        interactive.setOnClickListener(this);//互动消息
        notice_message = findViewById(R.id.notice_message);
        notice_time = findViewById(R.id.notice_time);
        img_notice = findViewById(R.id.img_notice);

        explosions_message = findViewById(R.id.explosions_message);
        explosions_time = findViewById(R.id.explosions_time);
        explosions_img = findViewById(R.id.explosions_img);

        system_time = findViewById(R.id.system_time);
        system_message = findViewById(R.id.system_message);
        system_img = findViewById(R.id.img_system);

        interactive_title1 = findViewById(R.id.interactive_title1);
        interactive_message = findViewById(R.id.interactive_message);
        interactive_time = findViewById(R.id.interactive_time);
        interactive_img = findViewById(R.id.interactive_img);

    }

    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setText("");
        mTitle.setText("消息");
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
            case R.id.notice:
                CommonSchemeJump.showActivity(this, "/app/CommissionInformationActivity");
                break;
            case R.id.system:
                CommonSchemeJump.showActivity(this, "/app/SystemessageActivity");
                break;
            case R.id.explosions:
                CommonSchemeJump.showActivity(this, "/app/DailyExplosionsActivity");
                break;
            case R.id.interactive:
                CommonSchemeJump.showActivity(this, "/app/InteractiveNewsActivity");
                break;


        }
    }

    @Override
    public void requestMessageSuccess(List<UserMessageVO> list) {

    }

    @Override
    public void requestMessageError(Throwable throwable) {

    }

    @Override
    public void requestMainMessageSuccess(List<InformationBean> list) {
        initData(list);
    }

    private void initData(List<InformationBean> list) {

        if(list.size()==0||list==null){
            return;
        }
        //系统
        system_img.setVisibility(list.get(0).getReadStatus() == 1 ? View.GONE : View.VISIBLE);
        system_time.setText(list.get(0).getCreateTime());
        system_message.setText(list.get(0).getTitle());

        //佣金
        explosions_img.setVisibility(list.get(2).getReadStatus() == 1 ? View.GONE : View.VISIBLE);
        explosions_time.setText(list.get(2).getCreateTime());
        explosions_message.setText(list.get(2).getTitle());
        //爆款
        img_notice.setVisibility(list.get(1).getReadStatus() == 1 ? View.GONE : View.VISIBLE);
        notice_message.setText(list.get(1).getTitle());
        notice_time.setText(list.get(1).getCreateTime());

        //互动
        interactive_img.setVisibility(list.get(3).getReadStatus() == 1 ? View.GONE : View.VISIBLE);
        interactive_message.setText(list.get(3).getTitle());
        interactive_time.setText(list.get(3).getCreateTime());


    }

    @Override
    public void requestMainMessageError(Throwable throwable) {

    }
}
