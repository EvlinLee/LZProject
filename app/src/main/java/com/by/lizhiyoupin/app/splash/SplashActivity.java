package com.by.lizhiyoupin.app.splash;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.PermissionsUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.impl.SelectAvatarCallback;
import com.by.lizhiyoupin.app.component_ui.weight.CommonFragmentPagerAdapter;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.config.AccountConfig;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.bean.MessageButton;
import com.by.lizhiyoupin.app.weight.CircleFillNavigator;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/22 11:06
 * Summary: 启动 闪屏页面
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener, CommonFragmentPagerAdapter.FragmentFactoryCallback, SelectAvatarCallback {
    public static final String TAG = SplashActivity.class.getSimpleName();
    public static final int REQUEST_PERMISSION_CODE = 150;
    private TextView jumpTv;
    private ViewPager mViewPager;
    private MagicIndicator mMagicIndicator;
    private int[] images = new int[]{R.drawable.splash_01, R.drawable.splash_02, R.drawable.splash_03};
    private String[] titles = new String[]{"人工精选", "全网搜券", "一键分享"};
    private String[] descs = new String[]{"每日严选淘宝、天猫、京东、拼多多好货", "海量大牌券 优惠不断", "随心买 任性赚"};
    private MessageBox permissionDialog;
    private boolean splash_new_over_install = false;
    private Handler mHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            // 当前Activity是否是根 Activity （当前应用栈底 Activity ）
            //修复 启动页点击logo多次出现问题
            finish();
            return;
        }
        final LiZhiApplication liZhiApplication = LiZhiApplication.getApplication();
        liZhiApplication.mUiHandler.removeMessages(LiZhiApplication.MSG_KILL_MAIN_PROCESS);
        mHandler = new Handler();
        initImmersionBar(Color.WHITE,true);
        boolean aBoolean = SPUtils.getDefault().getBoolean(CommonConst.AGREEMENT_KEY_TYPE, false);
        if (aBoolean){
            init();
        }else{
            DiaLogManager.showAgreementDialog(this, getSupportFragmentManager(), this);
        }
        LZLog.i(TAG, "SplashActivity onCreate");

    }


    private void init() {
        if (LiZhiApplication.getApplication().isNewInstall() || LiZhiApplication.getApplication().isOverInstall()) {
            Log.i(TAG, "onCreate: splash1");
            setContentView(R.layout.activity_splash_layout);
            splash_new_over_install = true;

            initView();
        } else {
            Log.i(TAG, "onCreate: splash2");
            setContentView(R.layout.activity_splash_flash_layout);
            ImageView logoIv = findViewById(R.id.splash_logo_iv);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) logoIv.getLayoutParams();
            int navigationBarHeight = DeviceUtil.getNavigationBarHeight(this);
            Log.i(TAG, "onCreate: splash2==" + navigationBarHeight);
            layoutParams.setMargins(0, 0, 0,
                    DeviceUtil.dip2px(this, 80) - navigationBarHeight);
            splash_new_over_install = false;
            permissionRequest();
        }
    }
    private void initView() {
        mViewPager = findViewById(R.id.splashViewPager);
        mMagicIndicator = findViewById(R.id.splash_magicIndicator);
        jumpTv = findViewById(R.id.jump_tv);
        jumpTv.setOnClickListener(this);
        mViewPager.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(), images.length, this));
        initIndicator();
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == images.length - 1) {
                    mMagicIndicator.setVisibility(View.GONE);
                } else {
                    mMagicIndicator.setVisibility(View.VISIBLE);
                }
                jumpTv.setVisibility(position == images.length - 1 ? View.INVISIBLE : View.VISIBLE);
            }
        });
        permissionRequest();
    }

    private void permissionRequest() {
        List<String> permissionList = PermissionsUtil.checkPermissions(this,
                android.Manifest.permission.READ_PHONE_STATE,
                // android.Manifest.permission.WRITE_SETTINGS,//修改手机系统 亮度等设置权限
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (null != permissionList && permissionList.size() != 0) {
            PermissionsUtil.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]),
                    REQUEST_PERMISSION_CODE);
        } else {
            //都授权了，且不是新装，重装用户 就直接进入主页
            if (!splash_new_over_install) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpMainActivity();
                    }
                }, 250);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            LZLog.i(TAG, "申请权限 回调" + grantResults.length);
            //不是新装或重装，直接进入主页
            if (!splash_new_over_install) {
                requestUserInfo();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpMainActivity();
                    }
                }, 250);

                return;
            }
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//没有被授权的 权限数
                    permissionDialog = MessageBox.builder()
                            .contentMessage("为保证您的账号安全登录和产品功能正常使用，请在应用权限设置中开启“存储”和“手机信息”权限。")
                            .addButton(new MessageButton("去设置", getResources().getColor(R.color.color_5491FE))
                                    .clickListener((buttonView, arguments, tag, text, position) -> {
                                        PermissionsUtil.gotoPermissionSetting(this);

                                    }))
                            .cancelOnTouchOutside(false)
                            .onKeyPressClickListener((messageBox, keyCode, event) -> LZLog.d("SplashActivity", " ==================>>>>>>>> keyCode " + keyCode))
                            .build("permission_request");
                    permissionDialog.show(getSupportFragmentManager());
                    return;
                }
            }
            requestUserInfo();
        }
    }

    private void requestUserInfo() {
        //获取权限后再次 请求用户信息
        UserInfoBean accountInfoPref = AccountConfig.getAccountInfoPref();
        if (accountInfoPref != null && accountInfoPref.getId() > 0) {
            LoginRequestManager.requestGetuserinfo(accountInfoPref.getApiToken());
        }
    }

    private void initIndicator() {
        CircleFillNavigator circleNavigator = new CircleFillNavigator(this);
        circleNavigator.setCircleColor(Color.RED);
        circleNavigator.setNormalFill(true);
        circleNavigator.setNormalColor(getResources().getColor(R.color.color_F8E4EC));
        circleNavigator.setCircleCount(images.length);
        circleNavigator.setCircleClickListener(new CircleFillNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                //指示器点击
                // mViewPager.setCurrentItem(index);
            }
        });
        mMagicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.jump_tv:
                //进入主页
                jumpMainActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 进入主页
     */
    public void jumpMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        if (getIntent().getExtras() != null) {
            intent.putExtras(getIntent().getExtras());
        }
        startActivity(intent);
        finish();
    }

    @Override
    public Fragment createFragment(int index) {
        SplashFragment fragment = new SplashFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_SPLASH_TITLE, titles[index]);
        bundle.putString(CommonConst.KEY_SPLASH_DESC, descs[index]);
        bundle.putInt(CommonConst.KEY_SPLASH_IMG, images[index]);
        bundle.putBoolean(CommonConst.KEY_SPLASH_END, index == images.length - 1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void selectAvatar(int avatarType) {
        if (avatarType == 1) {
            finishAffinity();
            System.exit(0);
        }else{
            SPUtils.getDefault().putBoolean(CommonConst.AGREEMENT_KEY_TYPE,true);
            init();

        }
    }
}
