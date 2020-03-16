package com.by.lizhiyoupin.app.component_ui.web;

import android.app.Activity;
import android.net.Uri;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.weight.PicPickerDialog;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by xuebinliu on 2015/8/12.d \]
 */
public class LzWebChromeClient extends WebChromeClient implements PicPickerDialog.PickerCallback {

    private ValueCallback<Uri[]> mValueCallback;
    private PicPickerDialog mPicPickerDialog;

    public PicPickerDialog getPicPickerDialog(){
        if (mPicPickerDialog!=null){
            return mPicPickerDialog;
        }
        return null;
    }
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        //这里处理h5调用native相机等媒体数据处理，弹框等
        if (mValueCallback != null) {
            mValueCallback.onReceiveValue(null);
        }
        mValueCallback = valueCallback;

        PicPickerDialog dialog = null;

        if (fileChooserParams != null) {
            String [] accept = fileChooserParams.getAcceptTypes();
            if (accept.length<=0){
                dialog = new PicPickerDialog((Activity) webView.getContext(), this, PicPickerDialog.PICKER_MODE_ALBUM);
            }
            for (String a : accept) {
                LZLog.i("sssss=","a=="+a);
                if (a.contains("image")) {
                    dialog = new PicPickerDialog((Activity) webView.getContext(), this, PicPickerDialog.PICKER_MODE_ALBUM);
                } else if (a.contains("video")) {
                    dialog = new PicPickerDialog((Activity) webView.getContext(), this, PicPickerDialog.PICKER_MODE_CAPTURE_VIDEO);
                } else {
                    dialog = new PicPickerDialog((Activity) webView.getContext(), this, PicPickerDialog.PICKER_MODE_ALBUM);
                }
            }
        } else {
            dialog = new PicPickerDialog((Activity) webView.getContext(), this, PicPickerDialog.PICKER_MODE_ALBUM);
        }

        dialog.setCrop(false);
        dialog.show();
        mPicPickerDialog = dialog;

        return true;
    }


    @Override
    public void onReceivedTitle(WebView webView, String s) {
        super.onReceivedTitle(webView, s);
    }


    @Override
    public void onGetPicSuccess(Uri uri) {
        if (mValueCallback != null) {
            mValueCallback.onReceiveValue(uri == null ? null : new Uri[] { uri });
            LZLog.i("sssss=","uri=="+uri.toString());
            mValueCallback = null;
        }
    }

    @Override
    public void onGetPicCancel() {
        if (mValueCallback != null) {
            mValueCallback.onReceiveValue(null);
            mValueCallback = null;
        }
    }

    @Override
    public void onGetPicError() {
        if (mValueCallback != null) {
            mValueCallback.onReceiveValue(null);
            mValueCallback = null;
        }
    }
}
