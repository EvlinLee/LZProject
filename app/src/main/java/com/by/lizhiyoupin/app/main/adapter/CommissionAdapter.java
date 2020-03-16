package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.io.bean.UserMessageVO;
import com.by.lizhiyoupin.app.user.adapter.CommissionRecyclerViewAdapter;

/**
 * data:2019/11/10
 * author:jyx
 * function:
 */
public class CommissionAdapter  extends CommissionRecyclerViewAdapter {
    public CommissionAdapter(Context context) {
        super(context);
    }
    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View root= mInflater.inflate(R.layout.item_commissionnotice_details, parent, false);
        return new CommissionAdapter.ViewHolder(root);
    }

    class ViewHolder extends CommonViewHolder{
        TextView commission_content;//内容
        ImageView commission_avar;//头像
        TextView commssion_order;//订单号
        TextView commssion_class;//订单类型
        TextView commssion_money;//订单金额
        TextView commssion_yonjin;//订单佣金
        private int corner;
        public ViewHolder(View itemView) {
            super(itemView);
            corner= DeviceUtil.dip2px(mContext, 8);
            commission_content = itemView.findViewById(R.id.commission_content);
            commission_avar = itemView.findViewById(R.id.commission_avar);
            commssion_order = itemView.findViewById(R.id.commssion_order);
            commssion_class = itemView.findViewById(R.id.commssion_class);
            commssion_money = itemView.findViewById(R.id.commssion_money);
            commssion_yonjin = itemView.findViewById(R.id.commssion_yonjin);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            UserMessageVO bean= (UserMessageVO) itemData;
            Glide.with(mContext).load(bean.getAvatar()).apply(RequestOptions.bitmapTransform(new CircleCrop())) .error(R.drawable.default_face)
                    .placeholder(R.drawable.default_face)
                    .into(commission_avar);
            commission_content.setText(Html.fromHtml(bean.getContent()));
            commssion_order.setText(bean.getOrderId());
            if (bean.getPlatformType()==1){
                commssion_class.setText("淘宝");
            }else if (bean.getPlatformType()==2){
                commssion_class.setText("京东");
            }else if (bean.getPlatformType()==3){
                commssion_class.setText("拼多多");
            }else if (bean.getPlatformType()==4){
                commssion_class.setText("自营");
            }
            commssion_money.setText(StringUtils.getFormattedDouble(bean.getPrice())+"元");
            commssion_yonjin.setText(StringUtils.getFormattedDouble(bean.getCommission())+"元");

        }
    }
}
