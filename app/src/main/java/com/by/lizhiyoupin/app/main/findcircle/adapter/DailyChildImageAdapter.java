package com.by.lizhiyoupin.app.main.findcircle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;

import cc.shinichi.library.ImagePreview;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 10:25
 * Summary: 发圈 图片
 */
public class DailyChildImageAdapter extends CommonRecyclerViewAdapter {
    public static final int ITEM_SINGLE_TYPE = 301;

    public DailyChildImageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getNormalViewType(int position) {
        if (getListData().size() == 1) {
            return ITEM_SINGLE_TYPE;
        }
        return super.getNormalViewType(position);
    }


    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate;
        if (viewType == ITEM_SINGLE_TYPE) {
            inflate = mInflater.inflate(R.layout.item_deily_child_single_center_layout, parent, false);
        } else {
            inflate = mInflater.inflate(R.layout.item_deily_child_center_layout, parent, false);
        }
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {
        ImageView showIv;

        public ViewHolder(View itemView) {
            super(itemView);
            showIv = itemView.findViewById(R.id.item_daily_show_iv);
            showIv.setOnClickListener(this);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            String url = (String) itemData;
            new GlideImageLoader(mContext, url)
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .into(showIv);
        }

        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()){
                return;
            }
            if (v.getId()==R.id.item_daily_show_iv){
                ImagePreview
                        .getInstance()
                        // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                        .setContext(mContext)
                        // 设置从第几张开始看（索引从0开始）
                        .setIndex(mRealPosition)
                        .setImageList(getListData())
                        // 缩放动画时长，单位ms
                        .setZoomTransitionDuration(300)
                        // 是否启用点击图片关闭。默认启用
                        .setEnableClickClose(true)
                        // 是否启用上拉/下拉关闭。默认不启用
                        .setEnableDragClose(true)
                        // 是否启用上拉关闭。默认不启用
                        .setEnableUpDragClose(true)
                        //页码
                        .setShowIndicator(true)
                        // 开启预览
                        .start();
            }
        }
    }
}
