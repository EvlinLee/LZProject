package com.by.lizhiyoupin.app.component_umeng.share.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.by.lizhiyoupin.app.io.entity.ChannelListBean;

import java.util.List;


public interface IShareManager {

    int STATE_SUCCESS = 1;
    int STATE_ERROR = 2;
    int STATE_CANCEL = 3;

    void showShareDialog(final Activity activity, final List<ChannelListBean> shareBean);

    void showShareDialog(final Activity activity, final  List<ChannelListBean> shareBean, IShareManager.ShareListener listener);

    void release(Context context);
    void onActivityResult(final Activity activity, final int requestCode, final int resultCode, final Intent data);

    interface ShareListener {
        /**
         *
         * @param state  1成功 2 失败 3 取消
         * @param plat
         */
        void onShareListener(int state, String plat);

        void onShareDismissed();
    }
}
