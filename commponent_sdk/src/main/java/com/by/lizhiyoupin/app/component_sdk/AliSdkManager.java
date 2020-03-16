package com.by.lizhiyoupin.app.component_sdk;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.trade.biz.applink.adapter.AlibcFailModeType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.alibaba.baichuan.trade.common.utils.AlibcLogger;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.CommonToast;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/26 13:09
 * Summary:
 */
public class AliSdkManager {
    public static final String TAG = AliSdkManager.class.getSimpleName();

    public static void initSdk(Application application) {
        AlibcTradeSDK.asyncInit(application, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                LZLog.i(TAG, "阿里百川 初始化成功");
                // 初始化成功，设置相关的全局配置参数

                // 是否使用支付宝
                AlibcTradeSDK.setShouldUseAlipay(true);

                // 设置是否使用同步淘客打点
                AlibcTradeSDK.setSyncForTaoke(true);

            }

            @Override
            public void onFailure(int code, String msg) {
                LZLog.i(TAG, "阿里百川 初始化失败==" + msg);

            }
        });
    }

    //登录 阿里百川 淘宝
    public void loginTaoBao(Activity activity, WebView webView, String url) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int loginResult, String openId, String userNick) {
                // 参数说明：
                // loginResult(0--登录初始化成功；1--登录初始化完成；2--登录成功)
                // openId：用户id
                // userNick: 用户昵称
                LZLog.i(TAG, "获取淘宝用户信息: " + AlibcLogin.getInstance().getSession());
                // String url="https://oauth.taobao.com/authorize?response_type=code&client_id=27728261&redirect_uri=http://shop.lizhiyoupin.com&state=1212&view=wap";

                AliSdkManager.showGoods(activity, url, webView);
            }

            @Override
            public void onFailure(int code, String msg) {
                // code：错误码  msg： 错误信息
                LZLog.i(TAG, "code错误码 " + code);
                LZLog.i(TAG, "msg 错误信息" + msg);
                if (code==111){
                    logoutTaoBao();
                    CommonToast.showToast("请不要频繁使用授权");
                }else {
                    CommonToast.showToast(msg);
                }
                activity.finish();
            }
        });
    }

    /**
     * 跳转到淘宝
     * @param activity
     * @param url
     * @param webView
     */
    public static void showGoods(Activity activity, String url, @Nullable WebView webView) {
        //展示参数配置
        AlibcShowParams showParams = new AlibcShowParams();
        showParams.setOpenType(OpenType.Auto);//Native表示唤端，Auto表示不做设置
        showParams.setClientType("taobao");//taobao---唤起淘宝客户端；tmall---唤起天猫客户端
        showParams.setBackUrl("lichi://");//小把手，不传不展示,传入自定义的scheme或百川提供的默认scheme："alisdk://"
        /**
         * AlibcFailModeType（唤端失败模式）： 枚举值如下
         * AlibcNativeFailModeNONE：不做处理；
         * AlibcNativeFailModeJumpBROWER：跳转浏览器；
         * AlibcNativeFailModeJumpDOWNLOAD：跳转下载页；
         * AlibcNativeFailModeJumpH5：应用内webview打开）
         * （注：AlibcNativeFailModeJumpBROWER不推荐使用）
         */
        showParams.setNativeOpenFailedMode(AlibcFailModeType.AlibcNativeFailModeJumpH5);//（唤端失败模式）
        //  showParams.setDegradeUrl();//degradeUrl（降级url）：可自行设置降级url，如果唤端失败且设置了降级url，则加载该url


        // taokeParams（淘客）参数配置：配置aid或pid的方式分佣
  /*    参数说明：pid ,unionId ,subPid ,adzoneId ,extraParams
            （注：1、如果走adzoneId的方式分佣打点，需要在extraParams中显式传入taokeAppkey，否则打点失败；
            2、如果是打开店铺页面(shop)，需要在extraParams中显式传入sellerId，否则同步打点转链失败）
    */
        AlibcTaokeParams taokeParams = new AlibcTaokeParams("", "", "");

//         taokeParams.setPid("mm_474360102_751000258_109383350334");//阿里妈妈pid
        //adzoneid是需要taokeAppkey参数才可以转链成功&店铺页面需要卖家id（sellerId），具体设置方式如下：
        //adzoneid就是pid的最后一段,taokeappkey在阿里百川app控制台就能获取
//         taokeParams.setAdzoneid("109383350334");
        // taokeParams.extraParams.put("taokeAppkey", "xxxxx");
        //taokeParams.extraParams.put("sellerId", "xxxxx");
        taokeParams.setPid("mm_699490195_1036700304_109716700223");//mm_474360102_751000258_109383350334
        taokeParams.setAdzoneid("109716700223");//2244668877

        //自定义参数
        Map<String, String> trackParams = new HashMap<>();


        // 以显示传入url的方式打开页面（第二个参数是套件名称）
        // WebView webView = new WebView(activity);
        AlibcTrade.openByUrl(activity, "", url, webView,
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        LZLog.i(TAG, "shouldOverrideUrlLoading " + url);
                        return super.shouldOverrideUrlLoading(view, request);
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        LZLog.i(TAG, "onPageStarted " + url);
                        try {
                            final Uri uri = Uri.parse(url);
                            String queryParameter = uri.getQueryParameter("isSuccessLzyp");
                            if ("true".equals(queryParameter)) {
                                // 根据URL判断是否授权成功，然后关闭授权窗口
                                activity.finish();
                            }else if ("false".equals(queryParameter)){
                                CommonToast.showToast("授权失败，请检查是否授权多个荔枝账号");
                                activity.finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        // 根据URL判断是否授权成功，然后关闭授权窗口
                        LZLog.i(TAG, "onPageFinished " + url);
                    }

                }, new WebChromeClient(), showParams,
                taokeParams, trackParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        AlibcLogger.i(TAG, "request success");
                        LZLog.i(TAG, "conTradeSuccess ");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        AlibcLogger.e(TAG, "code=" + code + ", msg=" + msg);
                        if (code == -1) {
                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // 以code的方式打开电商页面（eg：详情--detail，店铺--shop）
        //  页面实例 /
        //AlibcBasePage page = new AlibcDetailPage(itemId) ;
        //  AlibcBasePage page2 = new AlibcShopPage(shopId);

      /*  AlibcTrade.openByBizCode(activity, page, null, new WebViewClient(),
                new WebChromeClient(), "detail", showParams, taokeParams,
                trackParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        // 交易成功回调（其他情形不回调）
                        AlibcLogger.i(TAG, "open detail page success");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // 失败回调信息
                        AlibcLogger.e(TAG, "code=" + code + ", msg=" + msg);
                        if (code == -1) {
                            Toast.makeText(activity, "唤端失败，失败模式为none", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
    }

    //登出 阿里百川 淘宝
    public static void logoutTaoBao() {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.logout(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int loginResult, String openId, String userNick) {
                // 参数说明：
                // loginResult(3--登出成功)
                // openId：用户id
                // userNick: 用户昵称
                boolean res=loginResult==3;
                LZLog.i(TAG,"退出成功=="+res);
            }

            @Override
            public void onFailure(int code, String msg) {
                // code：错误码  msg： 错误信息
            }
        });
    }
}
