package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/12 13:36
 * Summary:
 */
public class OrderFindSuccessViewHolder implements IContentHolder, View.OnClickListener {
    public static final String TAG = OrderFindSuccessViewHolder.class.getSimpleName();

    private TextView mTipsTv;

    private Context context;
    private MessageBox mMessageBox;

    public OrderFindSuccessViewHolder(Context context) {
        this.context = context;

    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View content = LayoutInflater.from(context).inflate(R.layout.messagebox_order_find_success_layout, parent, false);
        initView(content);
        return content;
    }

    private void initView(View content) {
        content.findViewById(R.id.close_iv).setOnClickListener(this);
        mTipsTv = content.findViewById(R.id.tips_tv);
        mTipsTv.setText(R.string.order_find_success_content);
        SpannableString sp = new SpannableString("马上去我的订单中查看吧");
       /* sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_EF1818)),
                0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
        sp.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //点击跳转去我的订单
                CommonSchemeJump.showOrderQueryActivity(context);
                mMessageBox.dismiss();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(context, R.color.color_EF1818));
                ds.setUnderlineText(false);
            }
        }, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTipsTv.append(sp);
        mTipsTv.setMovementMethod(LinkMovementMethod.getInstance()); //设置后 点击事件才能起效
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_iv) {
            mMessageBox.dismiss();
        }
    }
}
