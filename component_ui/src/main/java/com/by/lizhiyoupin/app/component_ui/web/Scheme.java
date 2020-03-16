package com.by.lizhiyoupin.app.component_ui.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Set;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 *
 * litchi://mainJump?id=0101600000&name=银行
 */
public final class Scheme implements ISchemeManager {
    public static final String TAG = "Scheme";

    // 自有scheme，用于从h5页面跳转native界面
    public static final String LITCHI_SCHEME = "litchi://";

    public static final String HTTP = "http";
    public static final String HTTPS = "https";

    // 页面类型，用于页面内打开URL连接时检查打开页面的类型
    public static final String TAG_DT_PAGE_TYPE = "lz_page_type";

    // 页面滑动返回的手势类型
    public static final String TAG_SWIPE_BACK_TYPE = "dt_sbt";

    //通过url参数 判断是否是需要跳转拦截处理（如不让跳转淘宝）
    public static final String TAG_WEB_NOT_JUMP_TAO = "lzypUrlType";

    public static final String PARAMS_NEED_LOGIN = "needLogin";//=1说明需要登录

    public static final String PARAMS_BODY = "body";
    public static final String PARAMS_URL = "url";



    @Override
    public int handleUrl(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return CommonConst.PROTOCAL_ERROR_URL_BLANK;
        }

        if (url.startsWith(LITCHI_SCHEME)) {
            //处理native scheme跳转
            return handleBeaconScheme(context, url);
        }

        if (!TextUtils.isEmpty(Uri.parse(url).getQueryParameter(TAG_WEB_NOT_JUMP_TAO))){
            //去 可拦截淘宝等 的webview
            CommonWebJump.showInterceptOtherWebActivity(context,url);
            return CommonConst.PROTOCAL_HANDLE_SUCCESS;
        }
        //跳转普通 webview
        CommonWebJump.showCommonWebActivity(context, url);
        return CommonConst.PROTOCAL_HANDLE_SUCCESS;
    }
    /**
     * 处理scheme协议
     */
    public int handleBeaconScheme(Context context, String url) {
        final Uri uri = Uri.parse(url);
        return handleLitchiScheme(context, uri);
    }

    @Override
    public int handleLitchiScheme(Context context, Uri uri) {
        if (uri == null) {
            return CommonConst.PROTOCAL_ERROR_URL_BLANK;
        }

        final String host = uri.getHost();//litchi://mainJump ----->host就是mainJump
        if (!TextUtils.isEmpty(host)) {
           /* if (!TextUtils.isEmpty(uri.getQueryParameter(PARAMS_NEED_LOGIN))) {
                //需要判断登录的
                if (!isLogin()) {
                    LZLog.w(TAG, "use not login");
                    LitchiProtocal.loginJump(context,uri);
                    return CommonConst.PROTOCAL_HANDLE_SUCCESS;
                }
            }

            try {
                if (!TextUtils.isEmpty(uri.getQueryParameter(PARAMS_BODY))) {
                    String body = uri.getQueryParameter(PARAMS_BODY);
                    if (!TextUtils.isEmpty(body)) {
                        JSONObject json = new JSONObject(body);
                        if ("1".equals(json.opt(PARAMS_NEED_LOGIN))) {
                            if (!isLogin()) {
                                LitchiProtocal.loginJump(context, uri);
                            }
                        }

                        if (!TextUtils.isEmpty(json.optString(PARAMS_URL))) {

                            return handleUrl(context, json.optString(PARAMS_URL));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
*/

            try {
                final Method method = LitchiProtocal.class.getMethod(host, Context.class, Uri.class);
                method.invoke(null, context, uri);
                return CommonConst.PROTOCAL_HANDLE_SUCCESS;
            } catch (Exception e) {
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(new Intent(CommonConst.ACTION_LITCHI_PROTOCAL_ERROR));
                e.printStackTrace();
            }
        } else {
            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(new Intent(CommonConst.ACTION_LITCHI_PROTOCAL_ERROR));
        }
        return CommonConst.PROTOCAL_ERROR_LITCHI_NOT_SUPPORT;
    }

    @Override
    public int handleWebViewUrl(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return CommonConst.PROTOCAL_ERROR_URL_BLANK;
        }

        if (url.startsWith(LITCHI_SCHEME)) {
            return handleBeaconScheme(context, url);
        }
        if (!TextUtils.isEmpty(Uri.parse(url).getQueryParameter(TAG_WEB_NOT_JUMP_TAO))){
            //去 可拦截淘宝等 的webview
            CommonWebJump.showInterceptOtherWebActivity(context,url);
            return CommonConst.PROTOCAL_HANDLE_SUCCESS;
        }

        return CommonConst.PROTOCAL_NOT_HANDLE;
    }


    public static String addUrlParam(String orgUrl, String key, String value) {
        if (!orgUrl.contains("?")) {
            return orgUrl + "?" + key + "=" + value;
        }

        String url;
        int index = orgUrl.indexOf("?");
        url = orgUrl.substring(0, index + 1);

        Uri uri = Uri.parse(orgUrl);
        Set<String> parameterNames = uri.getQueryParameterNames();
        int i = 0;
        int size = parameterNames.size();
        for (String parameterName : parameterNames) {
            if (parameterName.equalsIgnoreCase(key)) {
                url = url + parameterName + "=" + value;
            } else {
                try {
                    url = url + parameterName + "=" + URLEncoder.encode(uri.getQueryParameter(parameterName), CommonConst.CHARTSET_UTF8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return orgUrl;
                }
            }
            if (i < size - 1) {
                url += "&";
            }
            i++;
        }
        LZLog.d(TAG, "addUrlParam url=" + url);
        return url;
    }



    /**
     * appendParams("http://www.baidu.com/limitUpAnalyse.html", "dt_from=app&secCode=%s", securityCode)
     * @param url
     * @param paramsFormat
     * @param params
     * @return
     */
    public static String appendParams(final String url, final String paramsFormat, final Object... params) {
        String destUrl = null;
        if (url.indexOf('?') > -1) {
            destUrl = url + '&' + getParams(paramsFormat, params);
        } else {
            destUrl =  url + '?' + getParams(paramsFormat, params);
        }
        LZLog.d(TAG, "getUrl : " + destUrl);
        return destUrl;
    }
    private static String getParams(final String paramsFormat, final Object... params) {
        if (params == null) {
            return paramsFormat;
        }

        final int length = params.length;
        for (int i = 0; i < length; i++) {
            try {
                params[i] = URLEncoder.encode(params[i].toString(), "UTF8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return String.format(paramsFormat, params);
    }

    private boolean isLogin() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (null != accountManager) {
            return accountManager.isLogined();
        }
        return false;
    }
}
