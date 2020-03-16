package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfDelegateAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfViewHolder;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.GuideArticleBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/25 13:36
 * Summary: 种草文章列表adapter
 */
public class PreciseWantToBuyAdapter extends BaseOfDelegateAdapter {
    private  int margin;
    public PreciseWantToBuyAdapter(Context context, LayoutHelper layoutHelper, int normalViewType) {
        super(context, layoutHelper, normalViewType);
        margin=DeviceUtil.dip2px(context,10);
    }

    @Override
    protected BaseOfViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_precise_want_to_buy_layout, parent, false);

        return new ViewHolder(inflate);
    }

    class ViewHolder extends BaseOfViewHolder {
        ImageView mItemListTopIv;//顶部图片
        TextView mItemListTitleTv;//顶部title

        TextView mLabelTv;//标签
        TextView seeNumberTv;//看过人数
        int radius;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemListTopIv = itemView.findViewById(R.id.item_list_top_iv);
            mItemListTitleTv = itemView.findViewById(R.id.item_list_title_tv);
            seeNumberTv = itemView.findViewById(R.id.want_to_buy_see_number_tv);
            mLabelTv = itemView.findViewById(R.id.label_tv);
            radius = DeviceUtil.dip2px(mContext, 8);
        }

        @Override
        public void bindData(Object itemData, int viewType) {
            super.bindData(itemData, viewType);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (mRealPosition==0||mRealPosition==1){
                layoutParams.setMargins(0,margin,0,0);
            }else {
                layoutParams.setMargins(0,0,0,0);
            }

            GuideArticleBean bean = (GuideArticleBean) itemData;

            new GlideImageLoader(mContext, bean.getMainImg())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.TOP))
                    .into(mItemListTopIv);

            mItemListTitleTv.setText(bean.getLongTitle());
            if (bean.getLabel()!=null){
                mLabelTv.setVisibility(View.VISIBLE);
                String regex = "#(.*?)#";
                Pattern pattern = Pattern.compile(regex);
                Matcher m = pattern.matcher(bean.getLabel());
                boolean b = m.find();
                String group = m.group(0);
                mLabelTv.setText(b?group.replaceAll("#",""):"");
            }else {
                mLabelTv.setVisibility(View.GONE);
            }


            seeNumberTv.setText(String.valueOf(bean.getViewCount()));
        }


        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            GuideArticleBean bean = (GuideArticleBean) getListData().get(mRealPosition);
            if (bean == null) {
                return;
            }
            CommonWebJump.showCommonWebActivity(mContext, WebUrlManager.getZongCaoUrl(bean.getArticleType(),bean.getId()));
           // CommonSchemeJump.showWantToBuyDetailActivity(mContext, bean.getId(), bean.getArticleType());
        }
    }
}
