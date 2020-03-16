package com.by.lizhiyoupin.app.component_umeng.share.adapter;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2018/9/10 17:35
 * Summary:
 */
public class UMAuthListenerAdapter implements UMAuthListener {

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        LZLog.i(ShareDialogAdapter.TAG,"授权onStart");
    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
       LZLog.i(ShareDialogAdapter.TAG,share_media+"授权onError"+throwable);
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        LZLog.i(ShareDialogAdapter.TAG,share_media+"取消授权onCancel");
    }
}
