package com.by.lizhiyoupin.app.detail.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.SelectPicBean;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/11 15:23
 * Summary: 商品详情 图片的分享
 */
public class CreateShareAdapter extends CommonRecyclerViewAdapter {
    private TextView mShareSelectedNumTv;
    private int selectNum;

    public CreateShareAdapter(Context context, TextView shareSelectedNumTv) {
        super(context);
        this.mShareSelectedNumTv = shareSelectedNumTv;
    }

    @Override
    protected int getNormalViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return super.getNormalViewType(position);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_create_share, parent, false);
        ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
        if (viewType == 0) {
            layoutParams.width = DeviceUtil.dip2px(mContext, 116);
            layoutParams.height = DeviceUtil.dip2px(mContext, 206);
            selectNum = 1;
            inflate.findViewById(R.id.item_share_check_iv).setSelected(true);
            setSelectNumText(mShareSelectedNumTv, selectNum);
        } else {
            layoutParams.width = DeviceUtil.dip2px(mContext, 100);
            layoutParams.height = DeviceUtil.dip2px(mContext, 100);
        }
        inflate.setLayoutParams(layoutParams);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {

        private ImageView shareIv;
        private ImageView checkIv;

        public ViewHolder(View itemView) {
            super(itemView);
            shareIv = itemView.findViewById(R.id.item_share_iv);
            checkIv = itemView.findViewById(R.id.item_share_check_iv);
            shareIv.setOnClickListener(this);
            checkIv.setOnClickListener(this);

        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            SelectPicBean picBean = (SelectPicBean) itemData;
            new GlideImageLoader(mContext, picBean.getUrl()).into(shareIv);
            checkIv.setSelected(picBean.isSelected());

        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            boolean selected = checkIv.isSelected();
            if (selectNum == 1 && selected) {
                //如果 只有一个则当前这个不可取消选中
                return;
            }
            if (selected) {
                //已经选中 则-1
                selectNum--;
            } else {
                //未选中 则+1
                selectNum++;
            }
            SelectPicBean picBean = (SelectPicBean) getListData().get(mRealPosition);
            picBean.setSelected(!selected);
            checkIv.setSelected(picBean.isSelected());
            setSelectNumText(mShareSelectedNumTv, selectNum);

        }

        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            if (v.getId() == R.id.item_share_iv) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < getListData().size(); i++) {
                    SelectPicBean bean = (SelectPicBean) getListData().get(i);
                    list.add(bean.getUrl());
                }
                ImagePreview
                        .getInstance()
                        // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                        .setContext(mContext)

                        // 设置从第几张开始看（索引从0开始）
                        .setIndex(mRealPosition)

                        //=================================================================================================
                        // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                        // 1：第一步生成的imageInfo List
                        .setImageList(list)
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
            }else if (v.getId()==R.id.item_share_check_iv){
                boolean selected = checkIv.isSelected();
                if (selectNum == 1 && selected) {
                    //如果 只有一个则当前这个不可取消选中
                    return;
                }
                if (selected) {
                    //已经选中 则-1
                    selectNum--;
                } else {
                    //未选中 则+1
                    selectNum++;
                }
                SelectPicBean picBean = (SelectPicBean) getListData().get(mRealPosition);
                picBean.setSelected(!selected);
                checkIv.setSelected(picBean.isSelected());
                setSelectNumText(mShareSelectedNumTv, selectNum);
            }


        }
    }

    private void setSelectNumText(TextView view, int number) {
        String string =
                mContext.getResources().getString(R.string.share_picture_selected_number_text);
        view.setText(String.format(string, number));
    }
}
