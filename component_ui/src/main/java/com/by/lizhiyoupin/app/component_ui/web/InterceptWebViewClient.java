package com.by.lizhiyoupin.app.component_ui.web;

import android.net.Uri;
import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.tencent.smtt.sdk.WebView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/3 16:06
 * Summary:
 */
public class InterceptWebViewClient extends LZBaseWebViewClient {
    public static final String TAG = InterceptWebViewClient.class.getSimpleName();

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LZLog.d(TAG, "shouldOverrideUrlLoading url2=" + url);
        if (jumpToShopDetail(view, url)) {
            return true;
        }
        final Uri uri = Uri.parse(url);
        String host = uri.getHost();
        final String scheme = uri.getScheme();
        LZLog.i(TAG, "scheme host==" + host);
        LZLog.i(TAG, "scheme scheme==" + scheme);
        if (TextUtils.isEmpty(scheme)) {
            view.loadUrl(url);
            return false;
        } else if (TextUtils.equals(host, "detail.m.tmall.com")) {
            String id = uri.getQueryParameter("id");
            getGoodInfo(view, url, id, CommonConst.PLATFORM_TIAN_MAO);
            return true;
        } else if (TextUtils.equals(host, "h5.m.taobao.com")) {
            String id = uri.getQueryParameter("id");
            getGoodInfo(view, url, id, CommonConst.PLATFORM_TAO_BAO);
            return true;
        } else if (Scheme.HTTP.equalsIgnoreCase(scheme) || Scheme.HTTPS.equalsIgnoreCase(scheme)) {
            // view.loadUrl(url);
            return false;
        }
        return true;
    }


    private boolean jumpToShopDetail(WebView view, String url) {

        Uri uri = Uri.parse(url);
        String shopid;
        try {
            shopid = uri.getQueryParameter("id");
        } catch (Exception e) {
            return false;
        }
        //淘宝详情跳转
        if (!TextUtils.isEmpty(shopid) && url.contains("taobao") && url.contains("detail")) {//淘宝
            getGoodInfo(view, url, shopid, CommonConst.PLATFORM_TAO_BAO);
            return true;
        } else if (!TextUtils.isEmpty(shopid) && url.contains("tmall") && url.contains("detail")) {//天猫
            getGoodInfo(view, url, shopid, CommonConst.PLATFORM_TIAN_MAO);
            return true;
        } else if (url.contains("jd") && url.contains("product")) {//京东
            //https://item.m.jd.com/product/46372474664.html?sceneval=2
            try{
                int start = url.lastIndexOf("/") + 1;
                int end = url.indexOf(".html");
                if (start<end){
                    String id = url.substring(start, end);
                    getGoodInfo(view, url, id, CommonConst.PLATFORM_JING_DONG);
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return false;
    }

    private void getGoodInfo(WebView view, String url, String id, int platformType) {
        if (TextUtils.isEmpty(id)) {
            view.loadUrl(url);
            return;
        }
        try {
            final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
            boolean logined = accountManager.isLogined();
            if (logined) {
                CommonSchemeJump.showPreciseDetailActivity(view.getContext(), Long.parseLong(id), platformType);
            } else {
                CommonSchemeJump.showLoginActivity(view.getContext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
