package com.by.lizhiyoupin.app.component_umeng.share.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.impl.Response2Callback;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.LZShare;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.entity.RequestPosterBean;
import com.by.lizhiyoupin.app.io.entity.SharePosterEntity;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.stack.ActivityStack;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2018/9/7 11:26
 * Summary:  分享控件管理
 * final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
 * .getManager(IShareManager.class.getName());
 */
public class ShareManager implements IShareManager {
    public static final String TAG = ShareManager.class.getSimpleName();

    // 避免连续调用两次出现分享页面异
    private long latestShowTime;

    @Override
    public void showShareDialog(final Activity activity, final List<ChannelListBean> shareBean) {
        if (ActivityStack.isActivityDestoryed(activity)) return;

        LZShare.share(shareBean).show();
    }

    @Override
    public void showShareDialog(final Activity activity, final List<ChannelListBean> shareBean, IShareManager.ShareListener listener) {
        LZLog.d(TAG, "======================>>> share " + System.currentTimeMillis());
        long current = System.currentTimeMillis();
        if (latestShowTime != 0 && current - latestShowTime < 500) return;
        if (ActivityStack.isActivityDestoryed(activity)) return;
        latestShowTime = current;
        if (!(activity instanceof BaseActivity)) return;

        LZShare.share(shareBean)
                .shareListener(listener)
                .show();
    }

    @Override
    public void release(Context context) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    /**
     * @param activity
     * @param type        （0-礼包分享，1-其他，scenarioType为0时，其他参数必传）
     * @param commodityId
     * @param userId
     * @param inviteCode
     */
    public static void requestShareBean(Activity activity, int type, Long commodityId, long userId, String inviteCode) {
        ApiService.getShareApi().requestShareBean(type, commodityId, userId, inviteCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<ChannelListBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<ChannelListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()) {
                            LZLog.i(TAG, "requestShareBean success");
                            final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                                    .getManager(IShareManager.class.getName());
                            if (shareManager != null) {
                                shareManager.showShareDialog(activity, listBaseBean.data);
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "requestShareBean onError");
                    }
                });
    }

    /**
     * 获取分享海报图片集合
     * @param goodsId
     * @param platformType
     * @param callback
     */
    public static void requestSharePosterWithCallback(Long goodsId, int platformType, Response2Callback<List<String>> callback) {
        ArrayList<RequestPosterBean> list=new ArrayList<>();
        list.add(new RequestPosterBean(goodsId+"",platformType));
        ApiService.getVipApi().requestSharePosterList(list)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<SharePosterEntity>>() {
                    @Override
                    public void onNext(BaseBean<SharePosterEntity> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.data != null && !ArraysUtils.isListEmpty(listBaseBean.data.getCommodityImgList())) {
                            callback.callbackSuccess(listBaseBean.data.getCommodityImgList());
                        } else {
                            callback.callbackError(listBaseBean.msg);
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG, "requestShareBean onError");
                        callback.callbackError(throwable.toString());
                    }
                });
    }

}
