package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/6 16:59
 * Summary: 淘宝等授权
 */
public class AuthViewHolder implements IContentHolder  {
    private Context context;
    private String titleMessage;
    private String decMessage;
    private String bottomMessage;

    // View值
    private TextView title;
    private TextView decTv;
    private TextView bottomMessageTv;
    private String url;

    public AuthViewHolder(Context context,String url, String titleMessage, String bottomMessage,String decMessage) {
        this.context = context;
        this.titleMessage = titleMessage;
        this.bottomMessage = bottomMessage;
        this.decMessage = decMessage;
        this.url=url;
    }


    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        View tipsContent = LayoutInflater.from(context).inflate(R.layout.messagebox_auth_tips_layout, parent, false);
        title = tipsContent.findViewById(R.id.titleMessage_tv);
        decTv = tipsContent.findViewById(R.id.auth_dec_tv);
        bottomMessageTv = tipsContent.findViewById(R.id.goto_tv);
        updateTitleMessage(titleMessage);
        updateDecMessage(decMessage);
        updateBottomMessage(bottomMessage);
        bottomMessageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去授权
                Bundle bundle = new Bundle();
                bundle.putString(CommonConst.KET_COUPON_CLICK_URL, url);
                CommonSchemeJump.showAutoWebActivity(context, bundle);
                messageBox.dismiss();
            }
        });
        return tipsContent;
    }

    /**
     * 更新标题信息
     *
     * @param titleMessage
     */
    public void updateTitleMessage(String titleMessage) {
        this.titleMessage = titleMessage;
        if (!TextUtils.isEmpty(titleMessage)) {
            title.setText(titleMessage);
        }
    }
    public void updateDecMessage(String dec) {
        this.decMessage = dec;
        if (!TextUtils.isEmpty(decMessage)) {
            decTv.setText(decMessage);
        }
    }

    public void updateBottomMessage(String bottomMessage) {
        this.bottomMessage = bottomMessage;
        if (!TextUtils.isEmpty(bottomMessage)) {
            bottomMessageTv.setText(bottomMessage);
        }
    }


}
