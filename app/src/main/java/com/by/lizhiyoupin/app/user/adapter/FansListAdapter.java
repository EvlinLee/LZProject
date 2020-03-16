package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.FansDataBean;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import androidx.fragment.app.FragmentManager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/11 09:45
 * Summary:
 */
public class FansListAdapter extends CommonRecyclerViewAdapter {

    private FragmentManager mFragmentManager;
    public FansListAdapter(Context context, FragmentManager fragmentManager) {
        super(context);
        mFragmentManager=fragmentManager;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_fans_list_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {
        private View itemFansCl;
        private ImageView photoIv;
        private ImageView iconIv;
        private TextView nameTv;
        private TextView numberTv;
        private TextView lastMonthTv;
        private TextView thisMonthTv;

        public ViewHolder(View itemView) {
            super(itemView);
            itemFansCl = itemView.findViewById(R.id.item_fans_cl);
            photoIv = itemView.findViewById(R.id.item_fans_photo_iv);
            iconIv = itemView.findViewById(R.id.item_fans_photo_icon_iv);
            nameTv = itemView.findViewById(R.id.item_fans_name_tv);
            numberTv = itemView.findViewById(R.id.item_fans_number_tv);
            lastMonthTv = itemView.findViewById(R.id.item_fans_income_last_month_tv);
            thisMonthTv = itemView.findViewById(R.id.item_fans_income_this_month_tv);
            itemFansCl.setOnClickListener(this);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            FansDataBean.FansListBean fansListBean = (FansDataBean.FansListBean) itemData;
            nameTv.setText(fansListBean.getName());
            ViewUtil.setTextViewFormat(mContext, numberTv, R.string.fans_number_text, fansListBean.getFansCount());
            ViewUtil.setTextViewFormat(mContext, lastMonthTv, R.string.item_fans_income_last_month_text,
                    StringUtils.getFormattedDouble(fansListBean.getLastMonthEstimate()));
            ViewUtil.setTextViewFormat(mContext, thisMonthTv, R.string.item_fans_income_this_month_text,
                    StringUtils.getFormattedDouble(fansListBean.getNowMonthEstimate()));

            new GlideImageLoader(mContext, fansListBean.getAvatar())
                    .error(R.drawable.default_face)
                    .placeholder(R.drawable.default_face)
                    .into(photoIv);
            switch (fansListBean.getLevel()) {
                case 1:
                    iconIv.setImageResource(R.drawable.vip_normal_photo_icon);
                    break;
                case 2:
                case 3:
                    iconIv.setImageResource(R.drawable.vip_super_photo_icon);
                    break;
                case 4:
                case 5:
                    iconIv.setImageResource(R.drawable.vip_operator_photo_icon);
                    break;
                default:
                    iconIv.setImageResource(R.drawable.vip_normal_photo_icon);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_fans_cl:
                    FansDataBean.FansListBean listBean = (FansDataBean.FansListBean) getListData().get(mRealPosition);
                    DiaLogManager.showFansDetailDialog(mContext,listBean.getUid(),mFragmentManager);
                    break;
            }
        }
    }
}
