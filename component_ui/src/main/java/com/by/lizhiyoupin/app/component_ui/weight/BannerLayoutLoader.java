package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.component_sdk.QRCodeGenerater;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.loader.ImageLoaderInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author:
 * @date :  2019/9/21 15:04
 * Summary:
 */
public class BannerLayoutLoader implements ImageLoaderInterface<View> {


    @Override
    public void displayImage(Context context, Object path, View imageView) {

        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        //   imageView.setScaleType(ImageView.ScaleType.FIT_XY);//拉伸图片，全部显示在imageView中
        Glide.with(context).load(path).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<?
                    super Drawable> transition) {
                imageView.findViewById(R.id.review).setBackground(resource);
            }
        });


    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    @Override
    public View createImageView(Context context) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        View view = LayoutInflater.from(context).inflate(R.layout.invate_code, null);
        String code = accountManager.getAccountInfo().getCode();
        String apiToken = accountManager.getAccountInfo().getApiToken();
        if (TextUtils.isEmpty(apiToken)) {

        } else {
            ImageView img_code = view.findViewById(R.id.img_code);
            TextView invate_text = view.findViewById(R.id.invate_text);
            invate_text.setVisibility(View.VISIBLE);
            invate_text.setText("邀请ID:" + code);
            Bitmap qrCode = QRCodeGenerater.createQRCode(WebUrlManager.getRegisterShareUrl(code), 400, 400);
            img_code.setImageBitmap(qrCode);
        }
        return view;


    }


}
