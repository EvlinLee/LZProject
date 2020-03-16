package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.widget.ImageView;

import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.loader.ImageLoader;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/21 15:04
 * Summary:
 */
public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);//拉伸图片，全部显示在imageView中


        new GlideImageLoader(context, path)
//                .transform(new GlideRoundTransform(context, DeviceUtil.dip2px(context, 8)))
                .into(imageView);

    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    @Override
    public ImageView createImageView(Context context) {

        return new ImageView(context);
    }
}
