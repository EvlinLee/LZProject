package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodsBean;
import com.by.lizhiyoupin.app.manager.AccountManager;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import androidx.fragment.app.FragmentManager;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/5 18:00
 * Summary: Vip页面底部列表 adapter
 */
public class VipProductListAdapter extends CommonRecyclerViewAdapter {
    private FragmentManager mFragmentManager;
    private int mVipLevel = 1;//角色：1-普通 2-超级 3-Plus超级 4-运营商 5-plus运营商

    public VipProductListAdapter(Context context, FragmentManager fragmentManager) {
        super(context);
        this.mFragmentManager = fragmentManager;
    }

    public void setVipLevel(int vipLevel) {
        this.mVipLevel = vipLevel;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_vip_product_list_layout, parent, false);

        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        ImageView mItemListTopIv;//顶部图片
        TextView mItemListTitleTv;//顶部title
        TextView mItemListMoneyTv;//价格
        ImageView mItemListBottomIv;//图标
        TextView mItemListBottomTv;//送365天VIP权益
        int raduis;
        public ViewHolder(View itemView) {
            super(itemView);
            mItemListTopIv = itemView.findViewById(R.id.item_vip_iv);
            mItemListTitleTv = itemView.findViewById(R.id.item_vip_title_tv);
            mItemListMoneyTv = itemView.findViewById(R.id.item_vip_price_tv);
            mItemListBottomIv = itemView.findViewById(R.id.item_bottom_iv);
            mItemListBottomTv = itemView.findViewById(R.id.item_bottom_tv);
            raduis = DeviceUtil.dip2px(mContext, 8);

        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            VipGoodsBean bean = (VipGoodsBean) itemData;

            new GlideImageLoader(mContext, bean.getMainImg())
                    .error(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(raduis,0,RoundedCornersTransformation.CornerType.TOP) )
                    .into(mItemListTopIv);
            mItemListTitleTv.setText(bean.getTitle());
            ViewUtil.setMoneyText(mContext, mItemListMoneyTv, "¥" + StringUtils.getFormattedDoubleOrInt(bean.getPrice()), R.style.product_money03, R.style.product_money04);
            setLevelData();
        }

        private void setLevelData() {
            switch (mVipLevel) {
                case 1://1-普通
                    mItemListBottomIv.setImageResource(R.drawable.vip_tag);
                    mItemListBottomTv.setText(R.string.vip_list_item_normal_text);
                    break;
                case 2://2-超级
                case 3:// 3-Plus超级
                    mItemListBottomIv.setImageResource(R.drawable.vip_tag_popularize);
                    ViewUtil.setTextViewFormat(mContext, mItemListBottomTv, R.string.vip_item_reward_text, 30);
                    break;
                case 4:// 4-运营商
                    mItemListBottomIv.setImageResource(R.drawable.vip_tag_popularize);
                    ViewUtil.setTextViewFormat(mContext, mItemListBottomTv, R.string.vip_item_reward_text, 32);
                    break;
                case 5:// 5-plus运营商
                    mItemListBottomIv.setImageResource(R.drawable.vip_tag_popularize);
                    ViewUtil.setTextViewFormat(mContext, mItemListBottomTv, R.string.vip_item_reward_text, 55);
                    break;
            }
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
            UserInfoBean userInfoBean = accountManager.getAccountInfo();
            if (userInfoBean == null) {
                return;
            }
            String superiorId = userInfoBean.getSuperiorId();
            if (TextUtils.isEmpty(superiorId)) {
                //没有上级邀请码，要去填写上级邀请码
                DiaLogManager.showInvitationCodeDialog(mContext, mFragmentManager);
                return;
            }
            VipGoodsBean bean = (VipGoodsBean) getListData().get(mRealPosition);
            Bundle bundle = new Bundle();
            bundle.putLong(CommonConst.KEY_NATIVE_DETAIL_ID, bean.getId());
            CommonSchemeJump.showVipProductDetailActivity(mContext, bundle);
        }
    }
}
