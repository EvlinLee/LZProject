package com.by.lizhiyoupin.app.manager.dhandler;

import android.app.Activity;
import android.content.Context;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.io.bean.RedGiftBean;
import com.by.lizhiyoupin.app.io.bean.SearchTaoBean;
import com.by.lizhiyoupin.app.io.bean.UpdateViersionBean;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.stack.ActivityStack;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

@SuppressWarnings("All")
public class DialogTaggerV2 extends SimpleTagger {

    private static final String TAG = DialogTaggerV2.class.getSimpleName();

    private static final String CLICK_TABS_COUNT = "click_tabs";
    private static final String TAB_ALERT_NEWVERSION = "alert_new_version";

    private static DialogTaggerV2 instance;

    private Context appContext;


    private boolean shouldAlertDialog = true;

    // 全局缓存参数
    //显示升级弹框
    public boolean upgradeDialogNeedShow = false;

    //正在请求新版本
    public boolean isRequesting = false;
    //签到弹框请求
    public boolean isSignInRequesting = false;

    public static DialogTaggerV2 getInstance() {
        if (instance == null) {
            instance = new DialogTaggerV2();
            instance.setLooperDuring(1800);
            // 优先级模式
            instance.setLooperMode(MODE_PRIORITY);
        }
        return instance;
    }

    private DialogTaggerV2() {
        appContext = LiZhiApplication.getApplication().getApplicationContext();
        initConsumer();
    }

    /**
     * 发现新版本
     *
     * @param upgrade
     */
    public void onNewVersionFound(UpdateViersionBean upgrade) {
        tagger(new SignalTagger(TYPE.CHECK_UPGRADE, upgrade).level(0).forceConsume(true));
    }

    /**
     * 搜索淘口令剪切板
     * 由于搜索 是onResume 中调用了，所以需要轮训，不然hasDialogAlertInThisPage 中isBackGround=true 导致弹不出来
     *
     * @param searchTaoBean
     */
    public void onHomeSearchTaoDialog(SearchTaoBean searchTaoBean) {
        tagger(new SignalTagger(TYPE.HOME_SEARCH_TAO_DIALOG, searchTaoBean).level(2).forceConsume(true));
    }

    /**
     * 搜索剪切板
     * 由于搜索 是onResume 中调用了，所以需要轮训，不然hasDialogAlertInThisPage 中isBackGround=true 导致弹不出来
     *
     * @param clipTextSimple
     */
    public void onHomeSearchAnyDialog(SearchTaoBean searchAnyBean) {
        tagger(new SignalTagger(TYPE.HOME_SEARCH_ANY_DIALOG, searchAnyBean).level(2).forceConsume(true));
    }

    /**
     * 需要签到弹框
     *
     * @param times 已经弹出次数
     */
    public void onHomeSignInDialog(int times) {
        tagger(new SignalTagger(TYPE.SIGNIN_HOME_POP, times).level(3).forceConsume(true));
    }

    /**
     * 订单红包
     *
     * @param redGiftBean
     */
    public void onHomeOrderRedDialog(RedGiftBean redGiftBean) {
        tagger(new SignalTagger(TYPE.HOME_ORDER_RED, redGiftBean).level(4).forceConsume(true));
    }


    /**
     * 添加消费者，排队处理
     */
    private void initConsumer() {
        //版本更新
        addConsumer(new Consumer() {
            private boolean upgradeDialogAlerted = false;

            @Override
            protected boolean consumer(SignalTagger signal, HashMap params) {

                // 正在请求新版本，不弹出对话框，保证版本更新的对话框为最高优先级
                if (signal.what == TYPE.CHECK_UPGRADE  // 新版本校验
                        && !isRequesting
                        && !hasDialogAlertInThisPage()
                        && isInMainPage()) {
                    // 在主界面，且有条件可以 弹框，同时有新版本，弹出新版本对话框
                    LZLog.i(TAG, "新版本");
                    return true;
                }
                LZLog.i(TAG, "新版本 false");
                return false;
            }

            @Override
            public void executeTask(SignalTagger signal, HashMap params) {
                if (signal.obj instanceof UpdateViersionBean
                        && !upgradeDialogAlerted
                        && upgradeDialogNeedShow) {
                    upgradeDialogAlerted = true;
                    UpdateViersionBean updateViersionBean = (UpdateViersionBean) signal.obj;
                    int isForce = updateViersionBean.getIsForce();
                    if (isForce == 1) {
                        //强制更新
                        DiaLogManager.showUpdateForceDialog(MainActivity.getInstance(), MainActivity.getInstance().getSupportFragmentManager(), updateViersionBean);
                    } else {
                        //提示更新
                        DiaLogManager.showUpdateTipsDialog(MainActivity.getInstance(), MainActivity.getInstance().getSupportFragmentManager(), updateViersionBean);
                    }
                }
            }
        });
        //搜索剪切板淘口令内容
        addConsumer(new Consumer() {
            @Override
            protected boolean consumer(SignalTagger signal, HashMap params) {

                if (signal.what == TYPE.HOME_SEARCH_TAO_DIALOG
                        && !isRequesting
                        && isInMainPage()
                        && !hasDialogAlertInThisPage()) {
                    LZLog.i(TAG, "搜索剪切板淘口令内容");
                    return true;
                }
                LZLog.i(TAG, "搜索剪切板淘口令内容 false");
                return false;
            }

            @Override
            public void executeTask(SignalTagger signal, HashMap params) {
                if (signal.obj instanceof SearchTaoBean) {
                    SearchTaoBean taoBean = (SearchTaoBean) signal.obj;
                    SPUtils.getDefault().putString(CommonConst.KEY_CLIP_SEARCH_TEXT, taoBean.getOldText());
                    DiaLogManager.showClipDialog(MainActivity.getInstance(), taoBean.getTitle(), taoBean.getItemId());
                }
            }
        });
        //搜索淘口令内容
        addConsumer(new Consumer() {
            @Override
            protected boolean consumer(SignalTagger signal, HashMap params) {

                if (signal.what == TYPE.HOME_SEARCH_ANY_DIALOG
                        && !isRequesting
                        && isInMainPage()
                        && !hasDialogAlertInThisPage()) {
                    LZLog.i(TAG, "搜索淘口令内容");
                    return true;
                }
                LZLog.i(TAG, "搜索淘口令内容 false");
                return false;
            }

            @Override
            public void executeTask(SignalTagger signal, HashMap params) {
                if (signal.obj instanceof SearchTaoBean) {
                    SearchTaoBean searchAnyBean = (SearchTaoBean) signal.obj;
                    SPUtils.getDefault().putString(CommonConst.KEY_CLIP_SEARCH_TEXT, searchAnyBean.getOldText());
                    DiaLogManager.showClipDialog(MainActivity.getInstance(), searchAnyBean.getTitle(), null);
                }
            }
        });

        //签到弹框
        addConsumer(new Consumer() {
            @Override
            protected boolean consumer(SignalTagger signal, HashMap params) {
                if (signal.what == TYPE.SIGNIN_HOME_POP
                        && !isRequesting
                        && isInMainPage()
                        && !hasDialogAlertInThisPage()) {
                    LZLog.i(TAG, "签到弹框");
                    return true;
                }
                LZLog.i(TAG, "签到弹框 false");
                return false;
            }

            @Override
            public void executeTask(SignalTagger signal, HashMap params) {
                int times = (int) signal.obj;
                //可能还未弹出，直接在签到页签到完成，导致times变化 所以需要再次更新
                int anIntTimes = SPUtils.getDefault().getInt(CommonConst.KEY_SIGN_IN_TIMES, times);
                if (anIntTimes < CommonConst.SIGN_IN_DIALOG_MAX_TIMES) {
                    DiaLogManager.showHomeSignInRedPagerDialog(MainActivity.getInstance(), MainActivity.getInstance().getSupportFragmentManager());
                    SPUtils.getDefault().putInt(CommonConst.KEY_SIGN_IN_TIMES, times + 1);
                    SPUtils.getDefault().putLong(CommonConst.KEY_SIGN_IN_EVERY_DAY_TIME, System.currentTimeMillis());
                }
            }
        });
        //订单红包弹框
        addConsumer(new Consumer() {
            @Override
            protected boolean consumer(SignalTagger signal, HashMap params) {
                if (signal.what == TYPE.HOME_ORDER_RED
                        && !isRequesting
                        && !isSignInRequesting
                        && isInMainPage()
                        && !hasDialogAlertInThisPage()) {

                    LZLog.i(TAG, "订单红包弹框");
                    return true;
                }
                LZLog.i(TAG, "订单红包弹框 false");
                return false;
            }

            @Override
            public void executeTask(SignalTagger signal, HashMap params) {
                if (signal.obj instanceof RedGiftBean) {
                    DiaLogManager.showRedGiftPackageDialog(MainActivity.getInstance(),
                            MainActivity.getInstance().getSupportFragmentManager(), (RedGiftBean) signal.obj);

                }
            }
        });

    }



    /**
     * 仅触发一次
     */
    public static void onTagger(@NonNull SignalTagger signal) {
        getInstance().tagger(signal);
    }

    /**
     * 仅触发一次
     */
    public static void onTagger(int what, Object obj) {
        getInstance().tagger(new SignalTagger(what, obj));
    }

    /**
     * 仅触发一次
     */
    public static void onTagger(int what, boolean force) {
        getInstance().tagger(new SignalTagger(what, null).forceConsume(force));
    }

    /**
     * 仅触发一次
     */
    public static void onTagger(int what, Object obj, boolean force) {
        getInstance().tagger(new SignalTagger(what, obj).forceConsume(force));
    }

    /**
     * 仅触发一次
     */
    public static void onTagger(int what, boolean force, int level) {
        getInstance().tagger(new SignalTagger(what, null).forceConsume(force).level(level));
    }

    /**
     * 仅触发一次
     */
    public static void onTagger(int what, Object obj, boolean force, int level) {
        getInstance().tagger(new SignalTagger(what, obj).forceConsume(force).level(level));
    }

    private boolean isInMainPage() {
        return ActivityStack.isInMainPage();
    }


    private boolean isLogin() {
        return LiZhiApplication.getApplication().getAccountManager().isLogined();
    }

    public void shouldAlertDialog(boolean should) {
        this.shouldAlertDialog = should;
    }

    /**
     * 比较笨的办法检测此Activity是否有Dialog弹出,后续优化
     * 仅支持DialogFragment，而且可能有事件误差
     *
     * @return true 有遮挡 不弹框
     */
    public final boolean hasDialogAlertInThisPage() {
        if (!shouldAlertDialog) {
            return true;
        }

        Activity activity = ActivityStack.instance().currentActivity();
        if (ActivityStack.isActivityDestoryed(activity)) {
            return true;
        }

        if (activity instanceof BaseActivity) {
            boolean backGround = ((BaseActivity) activity).isBackGround();
            LZLog.d(TAG, "===========>>>> backGround " + backGround);
            return backGround;
        }

        if (activity != null && activity instanceof AppCompatActivity && !activity.isFinishing()) {
            List<Fragment> fragments = ((AppCompatActivity) activity).getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    return true;
                }
            }
        }

        return false;
    }


    public static void addToGlobal(String key, Object value) {
        getInstance().addGlobalParams(key, value);
    }

    public static <T> T getFormGlobal(String key) {
        return (T) getInstance().global(key);
    }


    /**
     * 信号类型，有新的需求时，在此处添加
     */
    public static class TYPE {


        // 版本更新
        public static final int CHECK_UPGRADE = 110;


        // 首页签到弹窗
        public static final int SIGNIN_HOME_POP = 111;
        //订单红包
        public static final int HOME_ORDER_RED = 112;

        //搜索剪切板淘口令弹框
        public static final int HOME_SEARCH_TAO_DIALOG = 200;
        //搜索剪切板弹框
        public static final int HOME_SEARCH_ANY_DIALOG = 201;
    }
}
