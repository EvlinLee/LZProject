package com.by.lizhiyoupin.app.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBuyBean;

import androidx.recyclerview.widget.LinearLayoutManager;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/16 16:16
 * Summary: 抖券 弹幕轮播
 */
public class ShakeBarrageAdapter extends CommonRecyclerViewAdapter {
    LinearLayoutManager mLayoutManager;

    public ShakeBarrageAdapter(Context context, LinearLayoutManager layoutManager) {
        super(context);
        mLayoutManager = layoutManager;
    }

    @Override
    public int getItemCount() {
        if (mListData == null) {
            return 0;
        } else if (mListData.size() < 2) {
            return mListData.size();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_shake_barrage_scroll_layout, parent, false);
        return new ViewHolder(inflate);
    }

    public Object getItemData(final int position) {

        final int size = mListData == null ? 0 : getItemCount();
        return position < size && position >= 0 ? mListData.get(position % (mListData.size() == 0 ? 1 : mListData.size())) : null;
    }

    private int getItemPosition(final int position) {
        final int size = mListData == null ? 0 : getItemCount();
        return position < size && position >= 0 ? position % (mListData.size() == 0 ? 1 : mListData.size()) : 0;
    }

    class ViewHolder extends CommonViewHolder {
        private ImageView userIv;
        private TextView userNameTv;
        private TextView userActionTv;
        private View itemRoot;
        private int radius;

        public ViewHolder(View itemView) {
            super(itemView);
            itemRoot = itemView.findViewById(R.id.item_shake_barrage_ll);
            userIv = itemView.findViewById(R.id.user_iv);
            userNameTv = itemView.findViewById(R.id.user_name_tv);
            userActionTv = itemView.findViewById(R.id.user_action_tv);
            radius = DeviceUtil.dip2px(mContext, 12);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            ShakeCouponBuyBean bean = (ShakeCouponBuyBean) itemData;
            if (bean == null) {
                return;
            }
            new GlideImageLoader(mContext, bean.getUserpicture())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(radius,
                            0, RoundedCornersTransformation.CornerType.ALL))
                    .into(userIv);
            userNameTv.setText(bean.getUsername());
            if (bean.getType() == null) {
                return;
            }
            switch (bean.getType().trim()) {
                case "已购买该商品":
                    userActionTv.setBackgroundResource(0);
                    itemRoot.setBackgroundResource(R.drawable.shape_barrage_buyed);
                    break;
                case "正在分享":
                    userActionTv.setBackgroundResource(R.drawable.shape_barrage_shareing);
                    itemRoot.setBackgroundResource(R.drawable.shape_bg_shake_barrage_12);
                    break;
                case "正在购买":
                    userActionTv.setBackgroundResource(R.drawable.shape_barrage_buying);
                    itemRoot.setBackgroundResource(R.drawable.shape_bg_shake_barrage_12);
                    break;
            }
            userActionTv.setText(bean.getType());

        }
    }
}
