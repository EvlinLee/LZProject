package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.FansDataBean;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/11 09:45
 * Summary: 运营商粉丝推送搜索列表
 */
public class PushFansSearchListAdapter extends CommonRecyclerViewAdapter {

    private FragmentManager mFragmentManager;
    private ArrayList<FansDataBean.FansListBean> selectList;
    private ArrayList<Long> mFansSelectIdsList;
    private TextView mSubmitTv;
    private TextView mSelectAllTv;

    public PushFansSearchListAdapter(Context context, FragmentManager fragmentManager,
                                     ArrayList<FansDataBean.FansListBean> selectList, ArrayList<Long> fansSelectIdsList,
                                     TextView submitTv, TextView selectAllTv) {
        super(context);
        this.mFragmentManager = fragmentManager;
        this.selectList = selectList;
        this.mFansSelectIdsList = fansSelectIdsList;
        this.mSubmitTv = submitTv;
        this.mSelectAllTv = selectAllTv;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_push_fans_search_list_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {

        private ImageView selectIv;
        private ImageView photoIv;
        private ImageView iconIv;
        private TextView nameTv;
        private TextView numberTv;
        private TextView lastMonthTv;
        private TextView thisMonthTv;

        public ViewHolder(View itemView) {
            super(itemView);
            selectIv = itemView.findViewById(R.id.select_iv);
            photoIv = itemView.findViewById(R.id.item_fans_photo_iv);
            iconIv = itemView.findViewById(R.id.item_fans_photo_icon_iv);
            nameTv = itemView.findViewById(R.id.item_fans_name_tv);
            numberTv = itemView.findViewById(R.id.item_fans_number_tv);
            lastMonthTv = itemView.findViewById(R.id.item_fans_income_last_month_tv);
            thisMonthTv = itemView.findViewById(R.id.item_fans_income_this_month_tv);
            selectIv.setOnClickListener(this);
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
            if (mSelectAllTv.isSelected()) {
                selectIv.setSelected(true);
            } else {
                selectIv.setSelected(mFansSelectIdsList.contains(fansListBean.getUid()));
            }
        }

        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            switch (v.getId()) {
                case R.id.select_iv:
                    if (mSelectAllTv.isSelected()) {
                        ToastUtils.show("全选状态下无法修改");
                        return;
                    }
                    boolean selected = selectIv.isSelected();
                    if (!selected) {
                        //未选中
                        if (mFansSelectIdsList.size() >= 50) {
                            DiaLogManager.showToastDialog(mContext, mFragmentManager, "选中人数已满50人，不可再次选择",
                                    R.drawable.toast_close, Gravity.TOP, true);
                        } else {
                            FansDataBean.FansListBean bean = (FansDataBean.FansListBean) getListData().get(mRealPosition);
                            mFansSelectIdsList.add(bean.getUid());
                            selectList.add(bean);
                            selectIv.setSelected(true);
                        }
                    } else {
                        //已经选中
                        FansDataBean.FansListBean bean = (FansDataBean.FansListBean) getListData().get(mRealPosition);
                        mFansSelectIdsList.remove(bean.getUid());
                        selectList.remove(bean);
                        selectIv.setSelected(false);

                    }
                    mSubmitTv.setEnabled(mFansSelectIdsList.size() > 0);
                    break;
                default:
                    break;
            }
        }
    }
}
