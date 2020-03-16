package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
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
public class SystemMessageAdapter extends CommissionRecyclerViewAdapter {
    public SystemMessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_systemnotice_details, parent, false);
        return new SystemMessageAdapter.ViewHolder(root);
    }

    class ViewHolder extends CommonViewHolder {
        TextView system_content;//内容
        TextView system_time;//创建时间
        private int corner;
        private ImageView img_status;//消息状态

        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            system_content = itemView.findViewById(R.id.system_content);
            system_time = itemView.findViewById(R.id.system_time);
            img_status = itemView.findViewById(R.id.img_status);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            UserMessageVO bean = (UserMessageVO) itemData;
            system_content.setText(bean.getContent());
            system_time.setText(bean.getCreateTime());
            img_status.setVisibility(bean.getReadStatus() == 1 ? View.GONE : View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TimeUtils.isFrequentOperation()) {
                        return;
                    }


                    SettingRequestManager.requestMessageStatus("1", String.valueOf(bean.getId()))
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
