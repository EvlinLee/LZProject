package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.impl.ShareDialogClickCallback;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/6 16:59
 * Summary: 分享选择 微信朋友圈多图时
 */
public class ShareWXViewHolder implements IContentHolder  {
    private Context context;
    private String titleMessage;
    private String bottomMessage;

    // View值
    private TextView title;
    private TextView bottomMessageTv;
    private ShareDialogClickCallback clickCallback;
    private boolean isVideo;

    public ShareWXViewHolder(Context context, String titleMessage, String bottomMessage, ShareDialogClickCallback callback,boolean isVideo) {
        this.context = context;
        this.titleMessage = titleMessage;
        this.bottomMessage = bottomMessage;
        this.clickCallback=callback;
        this.isVideo=isVideo;
    }


    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        View tipsContent = LayoutInflater.from(context).inflate(R.layout.messagebox_share_wx_tips_layout, parent, false);
        title = tipsContent.findViewById(R.id.titleMessage_tv);
        bottomMessageTv = tipsContent.findViewById(R.id.bottomMessage_tv);
        CheckBox secondCb = tipsContent.findViewById(R.id.second_cb);
        if (isVideo){
            secondCb.setText("视频保存至手机相册");
        }
        updateTitleMessage(titleMessage);
        updateBottomMessage(bottomMessage);
        bottomMessageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/11/6    去朋友圈,其实就是打开微信
                if (clickCallback!=null){
                    clickCallback.clickCallback();
                }
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

    public void updateBottomMessage(String bottomMessage) {
        this.bottomMessage = bottomMessage;
        if (!TextUtils.isEmpty(bottomMessage)) {
            bottomMessageTv.setText(bottomMessage);
        }
    }


}
