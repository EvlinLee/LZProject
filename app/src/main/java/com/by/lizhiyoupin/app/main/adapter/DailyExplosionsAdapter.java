package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.ExpandTextView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserMessageVO;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
import com.by.lizhiyoupin.app.user.adapter.CommissionRecyclerViewAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * data:2019/11/9
 * author:jyx
 * function:
 */
public class DailyExplosionsAdapter extends CommissionRecyclerViewAdapter {
    public DailyExplosionsAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_explosionsnotice_details, parent, false);
        return new DailyExplosionsAdapter.ViewHolder(root);
    }

    class ViewHolder extends CommonViewHolder {

        ExpandTextView explosions_content;//粉丝
        ImageView explosions_img, img_status;//图片
        TextView explosions_time;//创建时间


        private int corner;

        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            explosions_content = itemView.findViewById(R.id.explosions_content);
            explosions_img = itemView.findViewById(R.id.explosions_img);
            explosions_time = itemView.findViewById(R.id.explosions_time);
            img_status = itemView.findViewById(R.id.img_status);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            UserMessageVO bean = (UserMessageVO) itemData;
            explosions_time.setText(bean.getCreateTime());
            new GlideImageLoader(mContext, bean.getImage())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .into(explosions_img);
            explosions_content.setText(bean.getContent());

            img_status.setVisibility(bean.getReadStatus() == 1 ? View.GONE : View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TimeUtils.isFrequentOperation()) {
                        return;
                    }

                    CommonSchemeJump.showPreciseDetailActivity(mContext, bean.getCommodityId(),
                            bean.getPlatformType());

                    SettingRequestManager.requestMessageStatus("4", String.valueOf(bean.getId()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultRx2Subscribe<BaseBean<String>>() {
                                @Override
                                public void onNext(BaseBean<String> userInfoBeanBaseBean) {
                                    super.onNext(userInfoBeanBaseBean);
                                    if (!userInfoBeanBaseBean.success()) {
                                        onError(new Throwable(userInfoBeanBaseBean.msg));
                                        return;
                                    }
                                    img_status.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    super.onError(throwable);
                                }
                            });

                }
            });


        }
    }
}
