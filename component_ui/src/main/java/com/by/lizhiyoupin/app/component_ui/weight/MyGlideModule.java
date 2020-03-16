package com.by.lizhiyoupin.app.component_ui.weight;


import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.io.net.OkHttpUrlLoader;
import com.by.lizhiyoupin.app.io.net.SSLSocketClient;

import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/9 17:18
 * Summary: 自定义Glide模块  注解处理@GlideModule
 */
@GlideModule
public class MyGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        //配置 Glide链式调用默认配置，缓存，图片格式等等
        //配置target防止"You must not call setTag() on a view Glide is targeting" 异常
        ViewTarget.setTagId(R.id.glide_tag);
        //        设置缓存大小为20mb
        int memoryCacheSizeBytes = 1024 * 1024 * 50; // 50mb
        //        设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
    }


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        //注册组件，比如可在这替换Glide的网络加载
        OkHttpClient mHttpClient = new OkHttpClient().newBuilder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//忽略证书，使可加载https图,忽略https
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(mHttpClient));



    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}

