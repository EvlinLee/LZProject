package com.by.lizhiyoupin.app.io;


import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.FileUtil;
import com.by.lizhiyoupin.app.io.interceptor.HeaderInterceptor;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class OkHttpClientUtils {
    private static final File CACHE_URL = new File(FileUtil.getExternalCacheDirPath(ContextHolder.getInstance().getContext()), "httpcache");//缓存位置
    private static final int CACHE_SIZE = 10 * 1024 * 1024;//缓存大小
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private static final int DEFAULT_WRITE_TIMEOUT = 20000;
    private static final int DEFAULT_READ_TIMEOUT = 20000;


    private static OkHttpClient okNoCacheHttpClient;
    private static OkHttpClient okLongConentHttpClient;
    private static OkHttpClient retrofitHttpClient;
    private static Dispatcher dispatcher;

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            LZLog.i("OKHTTP", message);
    });
    public static HttpLoggingInterceptor getLoggingInterceptor(){
        if (DeviceUtil.isApkInDebug(ContextHolder.getInstance().getContext())){
            return loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }else {
            return loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }

    /**
     * retrofit获取OkHttpClient. 根据网络状况 缓存
     *
     * @return okHttpClient
     * 单例，所有的api都是通过该client发送请求
     */
    public static OkHttpClient getOkHttpClient() {
        if (retrofitHttpClient == null) {
            //添加请求日志
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                    .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)//连接超时时间【毫秒】
                    .addInterceptor(new HeaderInterceptor(true, true))
                    .addInterceptor(getLoggingInterceptor())
                    .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)//写入时间
                    .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)//读取时间
                    .cache(new Cache(CACHE_URL, CACHE_SIZE));

            retrofitHttpClient = builder.build();
            dispatcher = retrofitHttpClient.dispatcher();
        }
        return retrofitHttpClient;
    }

    /**
     * 获取OkHttpClient.  不缓存
     * @return okHttpClient
     * 单例，所有的api都是通过该client发送请求
     */
    public static OkHttpClient getOkHttpNoCacheClient() {
        if (okNoCacheHttpClient == null) {
            //添加请求日志
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                    .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)//连接超时时间【毫秒】
                    .addInterceptor(new HeaderInterceptor(true, false))
                    .addInterceptor(getLoggingInterceptor())
                    .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)//写入时间
                    .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)//读取时间
                    .cache(new Cache(CACHE_URL, CACHE_SIZE));

            okNoCacheHttpClient = builder.build();
            dispatcher = okNoCacheHttpClient.dispatcher();
        }
        return okNoCacheHttpClient;
    }

    /**
     * 超长超时 请求
     * @return
     */
    public static OkHttpClient getOkHttpLongTimeConnectClient() {
        if (okLongConentHttpClient == null) {
            //添加请求日志
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                    .connectTimeout(2 * 60 * 1000, TimeUnit.MILLISECONDS)//连接超时时间【毫秒】
                    .addInterceptor(getLoggingInterceptor())
                    .addInterceptor(new HeaderInterceptor(true, false))
                    .writeTimeout(2 * 60 * 1000, TimeUnit.MILLISECONDS)//写入时间
                    .readTimeout(2 * 60 * 1000, TimeUnit.MILLISECONDS)//读取时间
                    .cache(new Cache(CACHE_URL, CACHE_SIZE));

            okLongConentHttpClient = builder.build();
            dispatcher = okLongConentHttpClient.dispatcher();
        }
        return okLongConentHttpClient;
    }


    /**
     * 取消所有请求
     */
    public static void cancelAllRequest() {
        dispatcher.cancelAll();
    }

    /**
     * 取消除url外所有的请求
     *
     * @param url ： 在SuperiorLoanApi中定义的地址.   eg: 【"user/login.do"】
     */
    public static void cancelAllButCall(String url) {
        List<Call> runningCalls = getAllRunningCalls();
        if (runningCalls == null) {
            return;
        }
        for (int i = 0; i < runningCalls.size(); i++) {
            if (!runningCalls.get(i).request().url().toString().contains(url)) {
                runningCalls.get(i).cancel();
            }
        }
        List<Call> queuedCalls = getAllQueuedCalls();
        if (queuedCalls == null) {
            return;
        }
        for (int i = 0; i < queuedCalls.size(); i++) {
            if (!queuedCalls.get(i).request().url().toString().contains(url)) {
                queuedCalls.get(i).cancel();
            }
        }
    }

    /**
     * 获取所有等待的阻塞请求队列
     *
     * @return List<Call>
     */
    public static List<Call> getAllQueuedCalls() {
        return dispatcher == null ? null : dispatcher.queuedCalls();
    }

    /**
     * 获取所有正在运行的请求列表
     *
     * @return List<Call>
     */
    public static List<Call> getAllRunningCalls() {
        return dispatcher == null ? null : dispatcher.runningCalls();
    }

    /**
     * 获取等待请求的个数
     *
     * @return int
     */
    public static int getQueuedCallsCount() {
        return dispatcher == null ? 0 : dispatcher.queuedCallsCount();
    }

    /**
     * 获取正在运行的请求的个数
     *
     * @return int
     */
    public static int getRunningCallsCount() {
        return dispatcher == null ? 0 : dispatcher.runningCallsCount();
    }

}
