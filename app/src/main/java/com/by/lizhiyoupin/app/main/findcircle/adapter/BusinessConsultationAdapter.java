package com.by.lizhiyoupin.app.main.findcircle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.LZShare;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 16:40
 * Summary: 资讯列表adapter
 */
public class BusinessConsultationAdapter extends CommonRecyclerViewAdapter {

    public BusinessConsultationAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_business_consultation_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {
        View lineView;
        ImageView mImageIv;
        TextView mTitleTv;
        TextView mTimeTv;
        TextView mShareTv;

        public ViewHolder(View itemView) {
            super(itemView);
            lineView = itemView.findViewById(R.id.line_view);
            mImageIv = itemView.findViewById(R.id.image_iv);
            mTitleTv = itemView.findViewById(R.id.title_tv);
            mTimeTv = itemView.findViewById(R.id.time_tv);
            mShareTv = itemView.findViewById(R.id.share_tv);
            mShareTv.setOnClickListener(this);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            if (mRealPosition == getListData().size() - 1) {
                lineView.setVisibility(View.INVISIBLE);
            } else {
                lineView.setVisibility(View.VISIBLE);
            }
            BusinessArticleBean bean = (BusinessArticleBean) itemData;
            new GlideImageLoader(mContext, bean.getArticleImg())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .into(mImageIv);
            mTitleTv.setText(bean.getArticleTitle());
            mTimeTv.setText(TimeUtils.transDateToDateString(bean.getCreateTime(), 7));

        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            BusinessArticleBean bean2 = (BusinessArticleBean) getListData().get(mRealPosition);
            if (bean2 != null && bean2.getViewPermitLevelList() != null) {
                List<String> viewPermitLevelList = bean2.getViewPermitLevelList();
                if (viewPermitLevelList.contains("1")||viewPermitLevelList.contains(String.valueOf(ViewUtil.transFormLevel()))) {
                    CommonWebJump.showCommonWebActivity(mContext, bean2.getArticleClickUrl());
                } else {
                    MessageToast.showToast(mContext, "请提升会员级别!");
                }
            }else {
                MessageToast.showToast(mContext, "请提升会员级别!");
            }
        }

        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            switch (v.getId()) {
                case R.id.share_tv:
                    BusinessArticleBean bean = (BusinessArticleBean) getListData().get(mRealPosition);
                    if (bean != null) {
                        LZShare.share(LZShare.getChannelListH5(bean.getArticleClickUrl(),null,bean.getArticleTitle()))
                                .show();
                    }
                    break;
            }
        }
    }
}
