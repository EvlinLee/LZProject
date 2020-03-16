package com.by.lizhiyoupin.app.component_ui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.stack.ActivityStack;

import java.io.File;
import java.net.URL;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


/*对glide进行二次封装，升级或者替换框架会方便。*/
public class GlideImageLoader {

    private RequestOptions options = new RequestOptions();
    private RequestBuilder mRequestBuilder;

    public final static int AUTO = 0;
    public final static int GIF = 1;
    public final static int BITMAP = 2;

    public <T> GlideImageLoader(T context, Object url) {
        this(context, url, AUTO);
    }
    public <T> GlideImageLoader(T context, Object url, int type) {
        RequestManager manager = null;
        if (context == null) {
            return;
        }

        if (context instanceof Fragment) {
            manager = Glide.with((Fragment) context);
        } else if (context instanceof FragmentActivity) {
            if(!ActivityStack.isActivityDestoryed((FragmentActivity) context)) {
                manager = Glide.with((FragmentActivity) context);
            }
        } else if (context instanceof Activity) {
            if(!ActivityStack.isActivityDestoryed(((Activity) context))){
                manager = Glide.with((Activity) context);
            }
        } else if (context instanceof View) {
            manager = Glide.with((View) context);
        } else if (context instanceof Context) {
            manager = Glide.with((Context) context);
        } else {
            LZLog.d("GlideImageLoader", "===================>>> IllegalArgumentException");
        }

        if (manager != null) {
            if (type == GIF) {
                mRequestBuilder = load(manager.asGif(), url);
            } else if (type == BITMAP) {
                mRequestBuilder = load(manager.asBitmap(), url);
            } else {
                /*这里不指定为asBitmap是因为有些图片事先不知道是gif,还是普通图，不指定交由glide自己判断*/
                mRequestBuilder = load(manager.asDrawable(), url);
            }
        }
    }

    private static RequestBuilder load(RequestBuilder builder, Object url){
        if (url instanceof Uri) {
            return builder.load((Uri) url);
        } else if (url instanceof File) {
            return builder.load((File) url);
        } else if (url instanceof byte[]) {
            return builder.load((byte[]) url);
        } else if (url instanceof Bitmap) {
            return builder.load((Bitmap) url);
        } else if (url instanceof String) {
            return builder.load((String) url);
        } else if (url instanceof Drawable) {
            return builder.load((Drawable) url);
        } else if (url instanceof Integer) {
            return builder.load((Integer) url);
        } else if (url instanceof URL) {
            return builder.load((URL) url);
        } else {
            return builder.load(url);
        }
    }

    /**
     * #see 使用 创建时type传  BITMAP
     * @return
     */
    @Deprecated
    public GlideImageLoader asBitmap() {
        return this;
    }


    public GlideImageLoader centerCrop() {
        options.centerCrop();
        return this;
    }

    public GlideImageLoader fitCenter() {
        options.fitCenter();
        return this;
    }

    public GlideImageLoader priority(Priority priority) {
        options.priority(priority);
        return this;
    }

    public GlideImageLoader size(int width, int height) {
        options.override(width, height);
        return this;
    }

    public GlideImageLoader skipMemoryCache(boolean skip) {
        options.skipMemoryCache(skip);
        return this;
    }

    public GlideImageLoader placeholder(Drawable placeholder) {
        options.placeholder(placeholder);
        return this;
    }

    public GlideImageLoader placeholder(int placeholder) {
        options.placeholder(placeholder);
        return this;
    }

    public GlideImageLoader fallback(Drawable fallback) {
        options.fallback(fallback);
        return this;
    }

    public GlideImageLoader fallback(int fallback) {
        options.fallback(fallback);
        return this;
    }

    public GlideImageLoader error(Drawable error) {
        options.error(error);
        return this;
    }

    public GlideImageLoader error(int error) {
        options.error(error);
        return this;
    }

    public GlideImageLoader diskCacheStrategy(DiskCacheStrategy type) {
        options.diskCacheStrategy(type);
        return this;
    }

    public GlideImageLoader transform(Transformation<Bitmap> transform) {
        options.transform(transform);
        return this;
    }

    public <Y> void into(Y target) {
        if (mRequestBuilder == null) {
            LZLog.e(getClass().getSimpleName(), "NullPointer for mRequestBuilder ... ");
            return;
        }
        if (target instanceof ImageView) {
            mRequestBuilder.apply(options.theme(((ImageView) target).getContext().getTheme())).into((ImageView) target);
        } else if (target instanceof Target) {
            mRequestBuilder.apply(options).into((Target) target);
        }

    }


    public static Bitmap createRoundConerImage(Bitmap source, int mWidth, int mHeight, int mRadius) {
        if (mWidth <= 0 || mHeight <= 0) {
            LZLog.e("ImageLoaderUtils", "createRoundConerImage size <= 0");
            return null;
        }
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Matrix matrix = new Matrix();
        matrix.postScale(mWidth / (float) source.getWidth(), mHeight / (float) source.getHeight());
        canvas.drawBitmap(source, matrix, paint);
        return target;
    }

    /**
     * 恢复请求，一般在停止滚动的时候使用
     *
     * @param context
     */
    public void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }

    public void resumeRequests(Fragment fragment) {
        Glide.with(fragment).resumeRequests();
    }

    public void resumeRequests(Activity activity) {
        Glide.with(activity).resumeRequests();
    }

    /**
     * 暂停请求 正在滚动的时候使用
     *
     * @param context
     */
    public static void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }

    public static void pauseRequests(Fragment fragment) {
        Glide.with(fragment).pauseRequests();
    }

    public static void pauseRequests(Activity activity) {
        Glide.with(activity).pauseRequests();
    }

    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    public static void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
            }
        }).start();
    }

    /**
     * 清除内存缓存
     *
     * @param context
     */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();//清理内存缓存  可以在UI主线程中进行
    }

}
