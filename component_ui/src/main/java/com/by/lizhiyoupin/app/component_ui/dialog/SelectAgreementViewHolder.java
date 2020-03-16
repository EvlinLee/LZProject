package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.impl.SelectAvatarCallback;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * data:2019/11/5
 * author:jyx
 * function:
 */
public class SelectAgreementViewHolder implements IContentHolder, View.OnClickListener {
    private Context mContext;
    private MessageBox mMessageBox;
    private SelectAvatarCallback callback;
    private String text="亲爱的荔枝优品用户:\n感谢您使用荔枝优品！我们非常重视您的个人信息和隐私安全。为了更好地保障您的个人权益，在您使用我们的产品前，请您务必认真阅读并透彻理解《隐私政策》、《用户协议》的全部内容，特别是以粗体 / 粗体下划线标识的条款 ，您应重点阅读，在您确认充分理解、同意并接受全部条款后开始使用我们的产品和服务。我们会严格按照政策内容使用和保护您的个人信息 ，感谢您的信任。如对政策内容有任何疑问 、意见或建议，您可通过荔枝优品的各种联系方式与我们联系。如您同意 ，请点击 “同意” 开始接受我们的服务。";
    private TextView tv;

    public SelectAgreementViewHolder(Context context, SelectAvatarCallback callback) {
        mContext = context;
        this.callback = callback;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_agreement_type_layout, parent, false);
        mMessageBox = messageBox;
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        tv = inflate.findViewById(R.id.tv);
        setLoginLinkText();
        TextView agree = inflate.findViewById(R.id.agree);
        TextView agree_none = inflate.findViewById(R.id.agree_none);
        agree_none.setOnClickListener(this);
        agree.setOnClickListener(this);
}


    private void setLoginLinkText() {
        SpannableStringBuilder spanText = new SpannableStringBuilder(text);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
               // LZLog.i(TAG, "跳转《隐私政策》");
                CommonWebJump.showCommonWebActivity(mContext,  WebUrlManager.getLoginPrivacyProtocolUrl());
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(mContext, R.color.link_color));
                ds.setUnderlineText(true);
            }
        }, 77, 83, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //LZLog.i(TAG, "跳转《荔枝优品用户协议》");
                CommonWebJump.showCommonWebActivity(mContext, WebUrlManager.getLoginRegisterServiceUrl());
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(mContext, R.color.link_color));
                ds.setUnderlineText(true);
            }
        }, 84, 90, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spanText);
        tv.setHighlightColor(Color.TRANSPARENT);
        tv.setMovementMethod(LinkMovementMethod.getInstance()); //设置后 点击事件才能起效
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.agree){
            int type=0;
            callback.selectAvatar(type);
            mMessageBox.dismiss();
        }else if (v.getId()==R.id.agree_none){
            int type=1;
            callback.selectAvatar(type);
            mMessageBox.dismiss();
        }
    }
}
