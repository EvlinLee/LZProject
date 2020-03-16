package com.by.lizhiyoupin.app.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.SettingConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.impl.DialogCallBack;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.io.IPManager;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageBox;

/*
 * jyx
 * 关于我们页面
 * */
@Route(path = "/app/AboutUsActivity")
public class AboutUsActivity extends BaseActivity implements View.OnClickListener, DialogCallBack<View>, Handler.Callback {
    private TextView mTitle, actionbar_back_tv, version, name_ip_tv;
    private RelativeLayout yinsizhengce, userxieyi, jiaoyixieyi;
    private int mClickAboutCount;
    private View mMSearviceIpRl;
    private boolean showDevlep;
    private Handler mHandler;
    public static final int SETTING_CODE = 120;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initImmersionBar(Color.WHITE,true);
        mHandler = new Handler(Looper.getMainLooper(), this);
        initBar();
        initView();
        settingDevelop();
    }

    private void initView() {
        jiaoyixieyi = findViewById(R.id.jiaoyixieyi);//平台交易协议
        jiaoyixieyi.setOnClickListener(this);
        userxieyi = findViewById(R.id.userxieyi);//用户协议
        userxieyi.setOnClickListener(this);
        yinsizhengce = findViewById(R.id.yinsizhengce);//隐私协议
        yinsizhengce.setOnClickListener(this);
        version = findViewById(R.id.version);
        version.setText(DeviceUtil.getVersionName(this));
    }

    private void settingDevelop() {
        //服务器ip环境设置
        mMSearviceIpRl = findViewById(R.id.service_ip_rl);
        mMSearviceIpRl.setOnClickListener(this);
        findViewById(R.id.icon_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示切换环境设置
                if (!showDevlep) {
                    mClickAboutCount++;
                    if (mClickAboutCount > 7) {
                        showDevlep = true;
                        mClickAboutCount = 0;
                        mMSearviceIpRl.setVisibility(View.VISIBLE);
                        SPUtils.getPreferences(SettingConst.PREF_NAME_DEVELOPER_SETTINGS)
                                .putBoolean(SettingConst.KEY_SHOW_DEVELOPER_SETTING, true);
                    }
                }
            }
        });
        name_ip_tv = findViewById(R.id.name_ip_tv);
        name_ip_tv.setText(IPManager.getInstance().getServerTypeName());
        showDevlep = SPUtils.getPreferences(SettingConst.PREF_NAME_DEVELOPER_SETTINGS)
                .getBoolean(SettingConst.KEY_SHOW_DEVELOPER_SETTING, false);
        mMSearviceIpRl.setVisibility(showDevlep ? View.VISIBLE : View.GONE);

    }

    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setText("");
        actionbar_back_tv.setOnClickListener(this);
        mTitle.setText("关于我们");
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
            case R.id.yinsizhengce:
                CommonWebJump.showCommonWebActivity(this, WebUrlManager.getUserPrivacyServiceUrl());
                break;
            case R.id.userxieyi:
                CommonWebJump.showCommonWebActivity(this, WebUrlManager.getUserResginServicUrl());
                break;
            case R.id.jiaoyixieyi:
                break;
            case R.id.service_ip_rl:
                //切换ip环境
                DiaLogManager.showDevelopIpSettingDialog(this, getSupportFragmentManager(), this);
                break;

        }
    }

    @Override
    public void clickCallback(MessageBox messageBox, View view) {
        if (view.getId() == R.id.prod_tv && !IPManager.getInstance().isProdIp()) {
            SPUtils.getPreferences(SettingConst.PREF_NAME_DEVELOPER_SETTINGS)
                    .putInt(SettingConst.KEY_SERVER_TYPE, IPManager.PROD);
            mHandler.sendEmptyMessageDelayed(SETTING_CODE, 500);
        } else if (view.getId() == R.id.test_ip && !IPManager.getInstance().isTestIp()) {
            SPUtils.getPreferences(SettingConst.PREF_NAME_DEVELOPER_SETTINGS)
                    .putInt(SettingConst.KEY_SERVER_TYPE, IPManager.TEST);
            mHandler.sendEmptyMessageDelayed(SETTING_CODE, 500);
        } else if (view.getId() == R.id.dev_ip) {
            /*SPUtils.getPreferences(SettingConst.PREF_NAME_DEVELOPER_SETTINGS)
                    .putInt(SettingConst.KEY_SERVER_TYPE, IPManager.TEST);
            mHandler.sendEmptyMessageDelayed(SETTING_CODE,500);*/
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SETTING_CODE:
                Intent intent = new Intent(ContextHolder.getInstance().getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
        }
        return false;
    }

}
