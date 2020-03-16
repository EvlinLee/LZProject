package com.by.lizhiyoupin.app.main.weight;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/6 16:59
 * Summary: 抖券文案复制
 */
public class ShakeCouponCopyViewHolder implements IContentHolder  {
    private Activity context;
    private String content;

    private String bottomMessage;

    // View值
    private TextView contentTv;
    private TextView bottomMessageTv;


    public ShakeCouponCopyViewHolder(Activity context, String content) {
        this.context = context;
        this.content = content;
    }


    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        View tipsContent = LayoutInflater.from(context).inflate(R.layout.messagebox_copy_shakje_coupon_layout, parent, false);
        contentTv = tipsContent.findViewById(R.id.content_tv);

        bottomMessageTv = tipsContent.findViewById(R.id.goto_tv);
        updateTitleMessage(content);

        updateBottomMessage(bottomMessage);
        bottomMessageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                DeviceUtil.putClipboardText(context,contentTv.getText());
                shareWx();
                messageBox.dismiss();
            }
        });
        return tipsContent;
    }

    private void shareWx(){
       new ShareAction(context)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(null)
                .withText(contentTv.getText().toString())
                .share();

    }
    /**
     * 更新标题信息
     *
     * @param titleMessage
     */
    public void updateTitleMessage(String titleMessage) {
        this.content = titleMessage;
        if (!TextUtils.isEmpty(titleMessage)) {
            contentTv.setText(titleMessage);
        }
    }


    public void updateBottomMessage(String bottomMessage) {
        this.bottomMessage = bottomMessage;
        if (!TextUtils.isEmpty(bottomMessage)) {
            bottomMessageTv.setText(bottomMessage);
        }
    }


}
