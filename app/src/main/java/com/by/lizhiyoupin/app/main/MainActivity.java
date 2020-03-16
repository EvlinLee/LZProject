package com.by.lizhiyoupin.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.BuildConfig;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.ResponseCallback;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.RedGiftBean;
import com.by.lizhiyoupin.app.io.bean.SearchTaoBean;
import com.by.lizhiyoupin.app.io.bean.UpdateViersionBean;
import com.by.lizhiyoupin.app.io.entity.ActivitySwitchEntity;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.jupsh.PushActivity;
import com.by.lizhiyoupin.app.main.manager.TabFragmentManager;
import com.by.lizhiyoupin.app.manager.RequestDataManager;
import com.by.lizhiyoupin.app.manager.dhandler.DialogTaggerV2;
import com.taobao.sophix.SophixManager;

import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 主页面
 */
@Route(path = "/app/MainActivity")
public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private long mLastBackPressedTime = 0;
    public TabFragmentManager mTabManager;
    private String clipText;
    private static MainActivity instance;


    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        initBottomTabs();

        clipText = SPUtils.getDefault().getString(CommonConst.KEY_CLIP_SEARCH_TEXT, "");
        executeExtraIntent(getIntent());
        requestUpdateVersion();
        loadNewPatch();
        showSignInDialog();
        requestRedGiftInfo();
    }



    private void loadNewPatch() {
        //sophix 热修复
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
      /*  List<String> tags = new ArrayList<>();
        tags.add("production");
        //此处调用在queryAndLoadNewPatch()方法前
        SophixManager.getInstance().setTags(tags);*/
        //查询 下载新的补丁包
        if (!BuildConfig.DEBUG) {
            LZLog.i(TAG, "查询补丁包");
            SophixManager.getInstance().queryAndLoadNewPatch();
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        executeExtraIntent(intent);
    }


    private void executeExtraIntent(final Intent currentIntent) {
        if (isDestroy() && mTabManager == null) {
            return;
        }
        LZLog.d(TAG, "executeExtraIntent splash receive push message");

        String action = currentIntent.getAction();
        if (PushActivity.RECEIVED_ACTION.equals(action)) {
            //处理推送跳转事件
            final String url = currentIntent.getStringExtra(PushActivity.KEY_PUSH_START_EXTRAS);
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (scheme != null) {
                scheme.handleUrl(this, url);
            }
        } else if (Intent.ACTION_VIEW.equals(action)) {
            // 处理浏览器的scheme协议，跳转本地native页面
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (scheme != null) {
                scheme.handleLitchiScheme(this, currentIntent.getData());
            }

        } else if (TextUtils.isEmpty(action)) {
            String pageIndex = currentIntent.getStringExtra(CommonConst.KEY_MAIN_PAGE_INDEX);
            int subPageIndex = currentIntent.getIntExtra(CommonConst.KEY_MAIN_SUBPAGE_INDEX, 0);
            int childIndex = currentIntent.getIntExtra(CommonConst.KEY_CHILD_PAGE_INDEX, 0);
            switchTab(pageIndex, subPageIndex, childIndex);
        }
        LiZhiApplication.getApplication().setInitMain(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CharSequence clipboardText = DeviceUtil.getClipboardText(this);
        if (clipboardText != null && !TextUtils.equals(clipboardText.toString(), clipText)) {
            //和上次剪切板内容不一样,则弹出搜索框
            clipText = clipboardText.toString();
            String reg = ".*([\\p{Sc}])\\w{6,20}([\\p{Sc}]).*";
            String clipTextSimple = clipText.replaceAll("\r|\n", "");
            if (Pattern.matches(reg, clipTextSimple)) {
                LZLog.i(TAG, "匹配淘口令" + clipTextSimple);
                RequestDataManager.requestSearchTaoList(clipTextSimple, new ResponseCallback<SearchTaoBean>() {
                    @Override
                    public void success(SearchTaoBean taoBean) {
                        LZLog.i(TAG, "requestSearchTaoList success");
                        taoBean.setOldText(clipText);
                        DialogTaggerV2.getInstance().onHomeSearchTaoDialog(taoBean);
                    }

                    @Override
                    public void error(Throwable throwable) {
                        LZLog.i(TAG, "requestSearchTaoList error" + throwable);
                    }
                });
            } else {
                LZLog.i(TAG, "匹配关键词");
                DialogTaggerV2.getInstance().onHomeSearchAnyDialog(new SearchTaoBean(clipTextSimple,clipText));

            }

        }

    }

    private void initBottomTabs() {
        mTabManager = new TabFragmentManager(MainActivity.this, R.id.tab_content);
        switchTab(0);
    }

    public void switchTab(final String pageIndex, final int subPageIndex, final int childIndex) {
        if (mTabManager != null) {
            mTabManager.switchTab(pageIndex, subPageIndex, childIndex);
        }
    }

    public void switchTab(final int index) {
        if (mTabManager != null) {
            mTabManager.switchTab(index);
        }
    }


    @Override
    public void onBackPressed() {
        if (mTabManager == null || !mTabManager.onBackPressed()) {
            long now = System.currentTimeMillis();
            if (now - mLastBackPressedTime > 1000) {
                CommonToast.showToast(R.string.press_back_one_more_time_to_exit);
                mLastBackPressedTime = now;
            } else {
                super.onBackPressed();
                LiZhiApplication.getApplication().mUiHandler.sendEmptyMessageDelayed(LiZhiApplication.MSG_KILL_MAIN_PROCESS, 1000);
            }
        }

    }

    /**
     * 红包 弹框
     */
    private void requestRedGiftInfo() {
        if (!LiZhiApplication.getApplication().getAccountManager().isLogined()) {
            LZLog.i(TAG, "requestRedGiftInfo 请先登录");
            return;
        }
        ApiService.getHomeApi().requestRedGiftPackageInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<RedGiftBean>>() {
                    @Override
                    public void onNext(BaseBean<RedGiftBean> giftBeanBaseBean) {
                        super.onNext(giftBeanBaseBean);
                        LZLog.i(TAG, "requestRedGiftPackageInfo==success");
                        if (giftBeanBaseBean.success() && giftBeanBaseBean.getResult() != null) {
                            if (giftBeanBaseBean.getResult().getOrderCount() > 0) {
                                DialogTaggerV2.getInstance().onHomeOrderRedDialog(giftBeanBaseBean.getResult());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "requestRedGiftPackageInfo==error" + throwable);
                    }
                });
    }

    /**
     * 签到红包,固定按钮的入口开关 在精选页面 PreciseNewFragment
     */
    private void showSignInDialog() {
        DialogTaggerV2.getInstance().isSignInRequesting=true;
        ApiService.getSignInApi().requestSignInCanShow()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<ActivitySwitchEntity>>() {
                    @Override
                    public void onNext(BaseBean<ActivitySwitchEntity> baseBean) {
                        super.onNext(baseBean);
                        DialogTaggerV2.getInstance().isSignInRequesting=false;
                        if (baseBean.success()&&baseBean.getResult()!=null){
                            if (baseBean.getResult().getSignStatus()==1){
                                //签到活动开启
                                int times = SPUtils.getDefault().getInt(CommonConst.KEY_SIGN_IN_TIMES, 0);
                                long currentTimeMillis = System.currentTimeMillis();
                                if (times < CommonConst.SIGN_IN_DIALOG_MAX_TIMES) {
                                    //每日首次 弹出签到页
                                    LZLog.i(TAG, "首日签到页dialog");
                                    DialogTaggerV2.getInstance().onHomeSignInDialog(times);
                                } else {
                                    //大于3次时，且不是同一天，则重置次数，然后弹窗签到
                                    long signInTime = SPUtils.getDefault().getLong(CommonConst.KEY_SIGN_IN_EVERY_DAY_TIME, 0L);
                                    if (!TimeUtils.isTheSameDay(signInTime, currentTimeMillis)) {
                                        times = 0;
                                        //每日首次 弹出签到页
                                        LZLog.i(TAG, "不同天首次签到页dialog");
                                        DialogTaggerV2.getInstance().onHomeSignInDialog(times);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        DialogTaggerV2.getInstance().isSignInRequesting=false;
                    }
                });

    }

    /**
     * 检测版本更新
     */
    private void requestUpdateVersion() {
        DialogTaggerV2.getInstance().isRequesting = true;
        ApiService.getNewsApi().requestUpdateVersion(DeviceUtil.getVersionName(this), 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UpdateViersionBean>>() {
                    @Override
                    public void onNext(BaseBean<UpdateViersionBean> bean) {
                        super.onNext(bean);
                        DialogTaggerV2.getInstance().isRequesting = false;
                        LZLog.i(TAG, "requestUpdateVersion==success==" + bean.success());
                        if (bean.success() && bean.getResult() != null) {
                            UpdateViersionBean result = bean.getResult();
                            if (result.getIsUpdate() == 1) {
                                //不更新
                                return;
                            }
                            DialogTaggerV2.getInstance().upgradeDialogNeedShow = true;
                            DialogTaggerV2.getInstance().onNewVersionFound(result);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        DialogTaggerV2.getInstance().isRequesting = false;
                        LZLog.i(TAG, "requestUpdateVersion==error==" + throwable);
                    }
                });
    }

}
