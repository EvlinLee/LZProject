package com.by.lizhiyoupin.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.SettingConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_sdk.AliSdkManager;
import com.by.lizhiyoupin.app.component_umeng.share.SocialComponent;
import com.by.lizhiyoupin.app.io.IPManager;
import com.by.lizhiyoupin.app.manager.AccountManager;
import com.by.lizhiyoupin.app.manager.LZActivityLifecycleCallbacks;
import com.by.lizhiyoupin.app.stack.ActivityStack;
import com.by.lizhiyoupin.app.wxapi.WXEntryActivity;
import com.dueeeke.videoplayer.exo.ExoMediaPlayerFactory;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.hjq.toast.ToastUtils;
import com.kepler.jd.Listener.AsyncInitListener;
import com.kepler.jd.login.KeplerApiManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.jpush.android.api.JPushInterface;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/19 20:22
 * Summary:
 */
public class LiZhiApplication extends Application {
    public static final String TAG = LiZhiApplication.class.getSimpleName();
    private boolean mIsMainProcess;

    private static LiZhiApplication instance;
    private volatile AccountManager accountManager;
    public final Executor defaultExecutor = Executors.newCachedThreadPool();

    public static final int MSG_KILL_MAIN_PROCESS = 1;

    public static final long INSTALL_STATE_NORMAL = 0L;//正常
    public static final long INSTALL_STATE_NEW_INSTALL = 1L;//新装
    public static final long INSTALL_STATE_OVER_INSTALL = 2L;//覆盖安装
    private long mInstallState = INSTALL_STATE_NORMAL;
    private long mLastVersionCode = -1L;
    public static final String kpl_appKey = "b1ddeec2e012d1a890c8e3817d3e6d52";//开普勒
    public static final String kpl_keySecret = "e168fcd6383c458fb155b4b3a0cad6d7";//开普勒
    private boolean isInitMain;

    public Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_KILL_MAIN_PROCESS:
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;
            }
        }
    };

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.transparent, android.R.color.black);//全局设置主题颜色
                // 指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于
                // %s"));
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20).setFinishDuration(0);
            }
        });
    }



    public boolean getInitMain() {
        return isInitMain;
    }

    public void setInitMain(boolean isInitMain) {
        this.isInitMain = isInitMain;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        long start = System.currentTimeMillis();
        instance = this;
        //写在了SophixStubApplication
        //MultiDex.install(this);
        mIsMainProcess = DeviceUtil.isMainProcess(this);
        if (mIsMainProcess) {
            ContextHolder.getInstance().init(this);
        }
        Log.d(TAG, "LiZhiApplication.attachBaseContext spend " + (System.currentTimeMillis() - start));
    }

    @Override
    public void onCreate() {
        ActivityStack.instance().setAppContext(this);
        SPUtils.initDefault(this, CommonConst.PREF_NAME_SETTING);
        initLog();
        super.onCreate();
        refWatcher=setupLeakCanary();

        int serverType = SPUtils.getPreferences(SettingConst.PREF_NAME_DEVELOPER_SETTINGS)
                .getInt(SettingConst.KEY_SERVER_TYPE, IPManager.PROD);
        LZLog.i(TAG, " ===============>>> 当前连线 serverType " + serverType);

        IPManager.getInstance().setServerType(serverType);

        initSync();
        initJush();
        initAsync();
        initKaipule();
    }
    private RefWatcher refWatcher;
    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }
    public static RefWatcher getRefWatcher(Context context) {
        LiZhiApplication leakApplication = (LiZhiApplication) context.getApplicationContext();
        return leakApplication.refWatcher;
    }
    private void initJush() {
        if (mIsMainProcess){
            JPushInterface.setDebugMode(true);
            JPushInterface.init(this);
            JPushInterface.setLatestNotificationNumber(this,10);
            Log.d("jpush", " jpush初始化成功");
        }
    }


    public boolean isDebug() {
        return !BuildConfig.BUILD_TYPE.equals("release");
    }

    /**
     * 同步
     */
    private void initSync() {
        if (mIsMainProcess) {
            registerActivityLifecycleCallbacks(new LZActivityLifecycleCallbacks());
            ToastUtils.init(this);
        }
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:【友盟+】 AppKey  Manifest文件中已配置可为空
         * 参数3:【友盟+】 Channel   Manifest文件中已配置可为空
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret，不需要push可为 空
         */
        UMConfigure.init(this, null
                , null, UMConfigure.DEVICE_TYPE_PHONE, null);
        
        WXEntryActivity.getWxApiInstance(this);
        //微信原生和友盟一起需要重写WXEntryActivity内方法 防止code重复使用
        PlatformConfig.setWeixin(CommonConst.WEIXIN_APP_ID, CommonConst.WEIXIN_APP_SECRTE);
        //其他的分享 QQ 新浪等平台需要的话后续再添加
        //PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        PlatformConfig.setQQZone("101772096", "9d454b06455037fd5830db24b9c62d01");
        // 统计 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }


    /**
     * 异步
     */
    private void initAsync() {
        defaultExecutor.execute(() -> {
            //初始化一些
            if (mIsMainProcess) {
                initWebView();
                initComponentsAsync();
                checkOldVersion();
                VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                        //使用使用IjkPlayer解码
                        //  .setPlayerFactory(IjkPlayerFactory.create())
                        //使用ExoPlayer解码
                        .setLogEnabled(BuildConfig.DEBUG)
                        .setPlayerFactory(ExoMediaPlayerFactory.create())
                        //使用MediaPlayer解码
                        //   .setPlayerFactory(AndroidMediaPlayerFactory.create())
                        .build());
                initAccount(this);

                AliSdkManager.initSdk(LiZhiApplication.this);
            }


        });
    }

    public static void initAccount(Context appContext) {
        LZLog.i("inita", "initAccount");
        AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        accountManager.init();

    }

    private void initComponentsAsync() {
        ComponentManager.registerComponent(LiZhiComponent.class.getName());
        ComponentManager.registerComponent(SocialComponent.class.getName());
    }

    private void initWebView() {
        //异步初始化 X5 webview 所需环境
        if (!mIsMainProcess){
            return;
        }
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d(TAG, " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口,initX5Environment 内部会创建一个线程向后台查询当前可用内核版本号,是异步操作
        QbSdk.initX5Environment(getApplicationContext(), cb);
        QbSdk.forceSysWebView();
    }

    public static LiZhiApplication getApplication() {
        return instance;
    }

    public synchronized AccountManager getAccountManager() {
        if (accountManager == null) {
            accountManager = new AccountManager();
        }
        return accountManager;
    }

    public boolean isNewInstall() {
        return mInstallState == INSTALL_STATE_NEW_INSTALL;
    }

    public boolean isOverInstall() {
        return mInstallState == INSTALL_STATE_OVER_INSTALL;
    }

    /**
     * 启动调用一次
     * 判断版本，是否新装 覆盖安装
     */
    public void checkOldVersion() {
        if (DeviceUtil.isMainProcess(LiZhiApplication.getApplication())) {
            LZLog.i("inita", "checkOldVersion");
            mInstallState = checkOverInstallOldVersion();
            if (mInstallState == INSTALL_STATE_NORMAL) {

            } else if (mInstallState == INSTALL_STATE_OVER_INSTALL) {

            } else if (mInstallState == INSTALL_STATE_NEW_INSTALL) {

            } else {

            }
        }
    }

    public long getLastVersionCode() {
        return mLastVersionCode;
    }

    private long checkOverInstallOldVersion() {
        final long lastVersion =
                SPUtils.getDefault().getLong(SettingConst.KEY_LAST_OVER_INSTALL_VERSION, 0L);
        final long curVersion = DeviceUtil.getVersionCode(this);

        mLastVersionCode = lastVersion;
        if (curVersion != lastVersion) {
            SPUtils.getDefault().putLong(SettingConst.KEY_LAST_OVER_INSTALL_VERSION, curVersion);
            if (lastVersion > 0) {
                return INSTALL_STATE_OVER_INSTALL;
            } else {
                return INSTALL_STATE_NEW_INSTALL;
            }
        }
        return INSTALL_STATE_NORMAL;
    }

    /**
     * 日志 控制
     */
    private void initLog() {
        LZLog.init(this);
        if (isDebug()) {
            LZLog.setConsoleLogEnable(true);
        }
        //后面 在写 设置页 控制
        boolean saveLogToFile = SPUtils.getPreferences(SettingConst.PREF_NAME_DEVELOPER_SETTINGS)
                .getBoolean(SettingConst.KEY_SAVE_LOG_TO_FILE,
                        SettingConst.DEFAULT_SAVE_LOG_TO_FILE);
        LZLog.setFileLogEnable(saveLogToFile);
    }

    /**
     * 初始化京东开普勒
     */
    private void initKaipule() {
        KeplerApiManager.asyncInitSdk(this, kpl_appKey, kpl_keySecret,
                new AsyncInitListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("Kepler", "Kepler asyncInitSdk onSuccess ");
                    }

                    @Override
                    public void onFailure() {
                        Log.e("Kepler",
                                "Kepler asyncInitSdk 授权失败，请检查lib 工程资源引用；包名,签名证书是否和注册一致");
                    }
                });
    }


}
