package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/6 15:02
 * Summary:   商品详情页 图文的adapter
 */
public class VipImageAdapter extends CommonRecyclerViewAdapter {
    private int screenWidth;

    public VipImageAdapter(Context context) {
        super(context);
        screenWidth = DeviceUtil.getScreenWidth(mContext);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.imageview_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_iv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);

            Glide.with(mContext)
                    .asBitmap()
                    .load(itemData)
                    .placeholder(R.drawable.empty_pic_list)
                    .error(R.drawable.empty_pic_list_h)
                   // .override(screenWidth, screenHeight)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)////不缓存资源
                    .skipMemoryCache(true)////禁止Glide内存缓存
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            int imageWidth = resource.getWidth();
                            int imageHeight = resource.getHeight();

                            //宽度固定,然后根据原始宽高比得到此固定宽度需要的高度
                            int height = screenWidth * imageHeight / imageWidth;
                            ViewGroup.LayoutParams para = mImageView.getLayoutParams();
                            para.height = height;
                            para.width = screenWidth;
                            mImageView.setImageBitmap(resource);
                        }
                    });

        }
    }
}
