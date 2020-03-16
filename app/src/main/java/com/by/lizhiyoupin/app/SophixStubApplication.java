package com.by.lizhiyoupin.app;

import android.content.Context;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import androidx.annotation.Keep;
import androidx.multidex.MultiDex;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/21 11:30
 * Summary: 真正的application 是LiZhiApplication,SophixStubApplication不要修改和添加任何东西
 */
public class SophixStubApplication extends SophixApplication {
    private final String TAG = "SophixStubApplication";
    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(LiZhiApplication.class)
    static class RealApplicationStub {}
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
         MultiDex.install(this);
        initSophix();
    }


    private void initSophix() {
        String appVersion = "0.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
            Log.i(TAG, "initSophix: appVersion="+appVersion);
        } catch (Exception e) {
            appVersion = "1.0.0";
            e.printStackTrace();
        }
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData("28215091-1", "d542cd9cdbb569b576818ac957a64519",
                        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCENagvEXJpF5zFN1Bc847/VouvzIyEB1sZ3w4nsrHCMaGE8oohK470fAnfkmWbfSj6tg1yjKgQrWAqEY7Hdy3Dg2dx82NT3NIHPUyKRj6IL45wnhjqAewfLW5ovsYOGMiEFml6EalZ1Ifi+cucByjgfWTvzbcX7Ve1l2g8ALVl6NHx3ucZgHabaVTlDK4bxUjnnKPWuwmi4gzvao65MoKs/S2MUcgc+5rxyCnxQB/QZfTBhlUqhqxSTTq1Mzl7ujKzWa4PM89apHTznEtGKY1vYk26ePXkUlb1xwLH427u9LrbM6bkb9BHfB85xCinxiVvDsK1SCZrZKtNBDBonS7lAgMBAAECggEADX94601JtAcQH2Z1Uvge2lzYqBsnIXeqympmhJEO6vesOOmWRqquzrE3RNcqfoR5wZcCqnZH1mQNFF50iFLXnkV1M0IZGv/JhamxfdCMI9VPIppRitqlY818nz8S829+UVYEcM0K0DwNn5PHQOfOOZUzz/PdxJibwtNxdebQnlXQHjhm8q2oKxHGF5niOickcinQ50Zac5F2ymyMWZkL2OIplHPqMwwm18Z3LVHQRRVAenqV8rL9ywdUKZwnyInUrQPvC/r3p0dLHh3D2aGfcy4/mdFh5tf70uGgOe8Pe7GWifysa97spCzzEo5IVFUgI7xy0k0T46KRR0yYh3sQQQKBgQDIM921j6ZGL9mvAJYUqUk1Lq4A5+/vxEWOK61mrjHUKyCaM2OyChRbKnHOpG9KU3vabQbQjQT5BZSc12r19X8mvROuCBVRq7+VcMSPprNXpInC5wBxYhkKXKabz2uHUxWloQukgPn0KbflUMfj+7XQ0CrxOmWselbFA4lBy6ItcQKBgQCpDplszPnf5IEaV7RR5iGu5QlNJkyfcIBSYwrBzkSxCP8u94nbf6n/nmzbBXXnoJdml99EcRa0o1aCgzU21Wf0yDkQ08+6Fjy98OwWtKPQN/nWT6odHt23dueU3JDL110QuozFftdxQFTNdf/eflNEohTO9uPsRkfn0pJkafTutQKBgBZMCH5HIGqiu6jM9OoaLIgaydBaBJP8lQ6PeMF91xjdMMVbwde7XacFpCUylR5/C8U47VBe1FXztCh/qW75R48F8eIpDk7JRQ4cwBeLpl+1eI5Xceou1cEcroWd5UjuDPQkaoqIvs3ds5h7lbejeg1mkdHKAjbibTWF/kxwpIBRAoGAKc3wZpUxXQoWWrtBXdc4kRUV/bvwyqR5NuZC7LXjCpwc7jJMJrklAo7cgDSTeVme1sHLdhM4+CZD6DYl3I9dC4zPf7fA8FU7ZATFfL/imkSdRT+h412Umh+PGKKKM73nx5E56kR5ZgnwbnY1dYFreddSqfMnXW/rrAnIJYiGBtkCgYEAxYxcF5QPhUKw7hJXMUrOT40/KHp1wTc74iyovP2uZA8sP5I8Y/8I5fEsnaN+cRXYNS35KitCJCHye2brDmF7dhlcHw6NsKGWUEOrW/6+8XNotJsu6Uvg4DgxaYQySvkl8g1EbiEreL6SXoxtbb21W1figRuvR3Ki975w51BDgQk=")
                .setEnableDebug(true)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.i(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.i(TAG, "sophix preload patch success. restart app to make effect.");
                        }
                    }
                }).initialize();
    }


}
