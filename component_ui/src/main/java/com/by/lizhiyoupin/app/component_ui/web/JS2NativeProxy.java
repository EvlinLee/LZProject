package com.by.lizhiyoupin.app.component_ui.web;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.by.lizhiyoupin.app.CommonWebConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.GsonUtil;
import com.by.lizhiyoupin.app.common.utils.PackageUtil;
import com.by.lizhiyoupin.app.component_sdk.AliSdkManager;
import com.by.lizhiyoupin.app.component_ui.bridge.CompletionHandler;
import com.by.lizhiyoupin.app.component_ui.manager.IDialogManager;
import com.by.lizhiyoupin.app.component_ui.manager.LocationManager;
import com.by.lizhiyoupin.app.component_ui.utils.DowanUtil;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/21 18:58
 * Summary:js 调android
 */
public class JS2NativeProxy {
    public static final String TAG = JS2NativeProxy.class.getSimpleName();

    private Handler mHandler;
    private final AppCompatActivity mContext;

    public JS2NativeProxy(Handler handler, AppCompatActivity context) {
        LZLog.i(TAG, "constructor handler=" + handler);
        mHandler = handler;
        this.mContext = context;
    }



    @JavascriptInterface
    public String inApp(Object json) {
        LZLog.d(TAG, "h5用来判断是否我们app");

        return "1";
    }

    /**
     * 关闭终端页面
     *
     * @param
     */
    @JavascriptInterface
    public void finish(Object json) {
        LZLog.d(TAG, "finish");

        Message msg = Message.obtain();
        msg.what = CommonWebConst.MSG_FINISH_PAGE;

        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    @JavascriptInterface
    public void login(Object json) {
        //登录
        LZLog.d(TAG, "login");
        Message msg = Message.obtain();
        msg.what = CommonWebConst.MSG_START_LOGIN_ACTIVITY;
        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    @JavascriptInterface
    public String accountInfo(Object json) {
        //获取用户信息
        LZLog.d(TAG, "accountInfo");
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        try {
            return GsonUtil.gsonString(accountManager.getAccountInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":error}";
        }

    }

    @JavascriptInterface
    public String getApiToken(Object json) {
        //获取apitoken
        LZLog.d(TAG, "getApiToken");
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        try {
            return accountManager.getAccountInfo().getApiToken();
        } catch (Exception e) {
            return "{\"error\":error}";
        }
    }

    @JavascriptInterface
    public void goHome(Object jsonObject) {
        //去首页
        CommonSchemeJump.showMainActivity(mContext);
    }

    @JavascriptInterface
    public void startNewActivity(Object jsonObject) {
        //打开新页面
        LZLog.d(TAG, "new start");
        final ISchemeManager manager = (ISchemeManager) ComponentManager.getInstance()
                .getManager(ISchemeManager.class.getName());
        try {
            String url = "";
            JSONObject object = (JSONObject) jsonObject;
            if (object.has("start_url")) {
                url = object.getString("start_url");
            }
            manager.handleUrl(mContext, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void downloadFile(Object jsonObject) {
        //下载文件等
        try {
            JSONObject object = (JSONObject) jsonObject;
            JSONArray fileUrl = object.getJSONArray("fileUrl");
            String message = object.getString("message");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < fileUrl.length(); i++) {
                        try {
                            String url = fileUrl.getString(i);
                            LZLog.i(TAG, "url==" + url);
                            DowanUtil.saveH5AnyFileToSdCard(mContext, url);
                            if (i == fileUrl.length() - 1) {
                                if (!TextUtils.isEmpty(message)) {
                                    MessageToast.showToast(mContext, message);
                                } else {
                                    MessageToast.showToast(mContext, "下载成功,请到相册查看");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            MessageToast.showToast(mContext, "下载失败");
                        }
                    }

                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public void shareBusiness(Object jsonObject) {
        //商学院文章分享
        LZLog.i(TAG, "shareBusiness==" + jsonObject);
        try {
            JSONObject object = (JSONObject) jsonObject;
            String shareUrl = object.getString("shareUrl");
            String title = object.getString("shareTitle");
            String desc = object.getString("shareDesc");
            final IDialogManager iDialogManager = (IDialogManager) ComponentManager.getInstance()
                    .getManager(IDialogManager.class.getName());
            if (iDialogManager != null) {
                iDialogManager.showShareH5Dialog(shareUrl, title,desc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void getLocation(Object json, CompletionHandler<Object> handler) {
        LZLog.i(TAG, "locationInApp=s=");
        double[] locationInApp = LocationManager.getInstance().getLocationInApp(mContext);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("location_x", locationInApp[0]);
            jsonObject.put("location_y", locationInApp[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.complete(jsonObject);

    }

    @JavascriptInterface
    public void openTaoBaoWithUrl(Object authorizationUrl) {
        //跳转淘宝（不包含授权）
        try {
            if (PackageUtil.checkAppInstalled(mContext, PackageUtil.PackageType.TAO_BAO) || PackageUtil.checkAppInstalled(mContext, PackageUtil.PackageType.TIAN_MAO)) {
                LZLog.i(TAG, "直接跳转淘宝");
                AliSdkManager.showGoods(mContext, (String) authorizationUrl, null);
            } else {
                LZLog.i(TAG, "跳转淘宝h5");
                CommonWebJump.showInterceptOtherWebActivity(mContext, (String) authorizationUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void getAuthorized(Object url) {
        //淘宝授权
        try {
            final IDialogManager iDialogManager = (IDialogManager) ComponentManager.getInstance()
                    .getManager(IDialogManager.class.getName());
            if (iDialogManager != null) {
                iDialogManager.showAuthDialog(mContext, (String) url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void changeWebViewCloseEvent(Object isSwitch){
        //返回按钮 开关
        // 0: 停止关闭按钮关闭。1: 按钮作用复原
        LZLog.i(TAG,"changeWebViewCloseEvent=="+isSwitch);
        Message obtain = Message.obtain();
        obtain.what=CommonWebConst.MSG_SET_BACK_BUTTON_LISTENER_ENABLE;
        obtain.obj=isSwitch;
        mHandler.sendMessage(obtain);
    }

    @JavascriptInterface
    public void shareQrCode(Object url) {
        //H5分享图片
        try {
            final IDialogManager iDialogManager = (IDialogManager) ComponentManager.getInstance()
                    .getManager(IDialogManager.class.getName());
            if (iDialogManager != null) {
                iDialogManager.showSharePictureDialog(String.valueOf(url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
