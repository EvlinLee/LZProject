package com.by.lizhiyoupin.app.io.interceptor;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.NetUtil;
import com.by.lizhiyoupin.app.common.utils.Sha1Util;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.manager.CacheManager;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.stack.ActivityStack;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class HeaderInterceptor implements Interceptor {
    public static final String KEY_PUBLISH_SECRET = "QAZxswEDCvfrTGBnhyUJMkiOLp";//秘钥
    //后台处理时nganx 下划线 _收不到
    private static final String KEY_VERSION_NO = "App-version";//APP版本号
    private static final String KEY_CHANNEL_NO = "channelNO";//渠道
    private static final String KEY_PLATFORM = "App-type";//手机系统类型 android
    private static final String KEY_MANUFACTURER = "Manufacturer";//手机产商
    private static final String KEY_MODEL = "Model";//手机型号
    private static final String KEY_PHONE_VERSION = "Version";//手机系统版本
    private static final String KEY_SAFETY_TOKEN = "Safety-Token";//防篡改Token
    private static final String KEY_SAFETY_CODE = "Safety-Code";//时间戳
    private static final String KEY_USER_TOKEN = "User-Token";//用户token

    private boolean addHeader;
    private boolean enableCache;

    public HeaderInterceptor(boolean addHeader, boolean enableCache) {
        this.addHeader = addHeader;
        this.enableCache = enableCache;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Context context = ActivityStack.getAppContext();
        Request request = addHeader(chain, addHeader);

        Response response;

        if (!enableCache) {
            response = chain.proceed(request);
        } else {
            String url = request.url().url().toString();
            String params = getPostParams(request);

            StringBuilder sb = new StringBuilder();
            sb.append(url).append(params);
            String key = sb.toString();
            if (NetUtil.isNetWorkConnected(context)) {//有网
                response = doRequest(chain, request, key);
            } else {//没网
                response = returnFormCache(chain, request, key);
            }
        }

        return response != null ? response : errorResponse(chain.request(), null);
    }

    private Request addHeader(Chain chain, boolean addHeader) {
        if (addHeader) {
            String time = String.valueOf(System.currentTimeMillis()/1000);
            String secret = Sha1Util.encryptBySHA1(time, KEY_PUBLISH_SECRET);
            IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                    .getManager(IAccountManager.class.getName());
            String userToken = "";
            if (accountManager != null) {
                userToken = accountManager.getUserToken();
            }
            return chain.request().newBuilder()
                    .addHeader(KEY_VERSION_NO, DeviceUtil.getVersionName(ContextHolder.getInstance().getContext()))
                    .addHeader(KEY_CHANNEL_NO, ContextHolder.getInstance().getChannelIDFromManifest())
                    .addHeader(KEY_PLATFORM, "Android")
                    .addHeader(KEY_MANUFACTURER, DeviceUtil.getDeviceBrand())
                    .addHeader(KEY_MODEL, DeviceUtil.getSystemModel())
                    .addHeader(KEY_PHONE_VERSION, DeviceUtil.getDeviceVersion())
                    .addHeader(KEY_SAFETY_TOKEN, secret == null ? "" : secret)
                    .addHeader(KEY_SAFETY_CODE, time)
                    .addHeader(KEY_USER_TOKEN, userToken == null ? "" : userToken)
                    .build();
        } else {
            return chain.request().newBuilder()
                    .build();
        }
    }

    private Response doRequest(Chain chain, Request request, String key) throws IOException {
        Context context = ActivityStack.getAppContext();
        Charset charset = Charset.forName("UTF-8");
        Response response = chain.proceed(request);
        CacheControl cacheControl = request.cacheControl();
        //cacheControl.noStore()表示不进行缓存
        if (!cacheControl.noStore() && response.isSuccessful()) {//请求成功且进行缓存
            ResponseBody responseBody = response.body();
            MediaType contentType = responseBody.contentType();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            //服务器返回的json原始数据
            String json = buffer.clone().readString(charset);
            CacheManager.getInstance(context).putCache(key, json);//写入磁盘
            return response
                    .newBuilder()
                    .body(ResponseBody.create(responseBody.contentType(), json))
                    .build();
        } else {
            return response;//丢给下一轮处理
        }
    }

    public Response returnFormCache(Chain chain, Request request, String key) throws IOException {
        Context context = ActivityStack.getAppContext();
        request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();
        String cache = CacheManager.getInstance(context).getCache(key);
        Response originalResponse = chain.proceed(request);
        ResponseBody responseBody = originalResponse.body();
        return originalResponse
                .newBuilder()
                .code(200)
                .message("OK")
                .body(ResponseBody.create(responseBody == null ? null : responseBody.contentType(), cache))
                .build();
    }

    private static Response errorResponse(Request request, MediaType contentType) {

        try {
            BaseBean<String> empty = new BaseBean<>();
            empty.setCode("-1");
            empty.setMessage("network connect error");
            empty.setResult("");
            return new Response.Builder()
                    .request(request)
                    .code(400)
                    .protocol(Protocol.HTTP_1_1)
                    .message("")
                    .body(ResponseBody.create(contentType, JSON.toJSONString(empty)))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response.Builder()
                .request(request)
                .code(400)
                .message("")
                .protocol(Protocol.HTTP_1_1)
                .build();
    }

    /**
     * 获取在Post方式下。得到向服务器发送的参数
     *
     * @param request
     * @return
     */
    private static String getPostParams(Request request) throws IOException {
        String reqBodyStr = "";
        String method = request.method();
        if ("POST".equals(method)) // 如果是Post，则尽可能解析每个参数
        {
            StringBuilder sb = new StringBuilder();
            sb.append(request.body().contentLength());
//            requestBody.contentType().type().equals("application/json;charset=UTF-8")
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                if (body != null && body.size() > 0) {
                    for (int i = 0; i < body.size(); i++) {
                        sb.append(body.encodedName(i)).append("=").append(body.encodedValue(i)).append(",");
                    }
                    sb.delete(sb.length() - 1, sb.length());
                }
                reqBodyStr = sb.toString();
                sb.delete(0, sb.length());
            }
        }
        return reqBodyStr;
    }
}
