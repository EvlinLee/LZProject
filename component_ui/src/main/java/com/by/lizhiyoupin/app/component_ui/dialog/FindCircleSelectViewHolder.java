package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.impl.ShareDialogClickContentCallback;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 10:07
 * Summary: 发圈选择 分享内容 弹框
 */
public class FindCircleSelectViewHolder implements IContentHolder, View.OnClickListener {
    private Context mContext;
    private int type;
    private MessageBox mMessageBox;
    private ShareDialogClickContentCallback<String> callback;
    public static final String TYPE_POSTER = "poster";
    public static final String TYPE_PICTURE = "picture";
    public static final String TYPE_VIDEO = "video";

    public FindCircleSelectViewHolder(Context context, int type, ShareDialogClickContentCallback<String> callback) {
        super();
        this.mContext = context;
        this.type = type;
        this.callback = callback;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.messagebox_find_circle_layout, parent, false);
        LinearLayout rootLl = inflate.findViewById(R.id.root_ll);
        mMessageBox = messageBox;
        addTopContent(rootLl, type);
        return inflate;
    }


    private void addTopContent(LinearLayout rootLl, int type) {
        rootLl.removeAllViews();
        if (type == 1) {
            //只有海报
            addWithPoster(rootLl);
        } else if (type == 2) {
            //只有图片
            addWithPicture(rootLl);
        } else if (type == 3) {
            //只有视频
            addWithVideo(rootLl);
        } else if (type == 4) {
            //海报+图片
            addWithPoster(rootLl);
            addWithLineView(rootLl);
            addWithPicture(rootLl);
        } else if (type == 5) {
            //海报+视频
            addWithPoster(rootLl);
            addWithLineView(rootLl);
            addWithVideo(rootLl);
        } else if (type == 6) {
            //图片+视频
            addWithPicture(rootLl);
            addWithLineView(rootLl);
            addWithVideo(rootLl);
        } else if (type == 7) {
            //海报+图片+视频
            addWithPoster(rootLl);
            addWithLineView(rootLl);
            addWithPicture(rootLl);
            addWithLineView(rootLl);
            addWithVideo(rootLl);
        }
    }



    private void addWithLineView(LinearLayout rootLl) {
        View lineView = LayoutInflater.from(mContext).inflate(R.layout.line_view_layout, rootLl, false);
        rootLl.addView(lineView);
    }

    private void addWithPoster(LinearLayout rootLl) {
        TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_share_text_layout, rootLl, false);
        textView.setTag(TYPE_POSTER);
        textView.setText("分享二维码海报");
        textView.setOnClickListener(this);
        rootLl.addView(textView);
    }

    private void addWithPicture(LinearLayout rootLl) {
        TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_share_text_layout, rootLl, false);
        textView.setTag(TYPE_PICTURE);
        textView.setText("分享图片");
        textView.setOnClickListener(this);
        rootLl.addView(textView);
    }

    private void addWithVideo(LinearLayout rootLl) {
        TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_share_text_layout, rootLl, false);
        textView.setTag(TYPE_VIDEO);
        textView.setText("分享视频");
        textView.setOnClickListener(this);
        rootLl.addView(textView);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (TimeUtils.isFrequentOperation() || TextUtils.isEmpty(tag)) {
            return;
        }
        if (callback != null) {
            callback.clickCallback(mMessageBox,tag);
        }

    }
}
