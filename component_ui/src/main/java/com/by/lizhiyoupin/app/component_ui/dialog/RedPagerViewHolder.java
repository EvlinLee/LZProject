package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.impl.DialogCallBack;
import com.by.lizhiyoupin.app.component_ui.weight.MoneyTextView;
import com.by.lizhiyoupin.app.io.bean.SignInRedPaperBean;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/10 15:49
 * Summary: 领取签到红包
 */
public class RedPagerViewHolder implements IContentHolder, View.OnClickListener {

    private Context context;
    private MessageBox mMessageBox;
    private MoneyTextView moneyTv;
    private Button submitBtn;
    private TextView acceptTheRedEnvelope;
    private TextView content_tips_tv;
    private SignInRedPaperBean inRedPaperBean;
    private DialogCallBack<SignInRedPaperBean> dialogCallBack;
    public RedPagerViewHolder(Context context, SignInRedPaperBean inRedPaperBean, DialogCallBack<SignInRedPaperBean> dialogCallBack) {
        this.context = context;
        this.inRedPaperBean = inRedPaperBean;
        this.dialogCallBack = dialogCallBack;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_red_pager_layout, parent, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        moneyTv = root.findViewById(R.id.money_tv);
        content_tips_tv = root.findViewById(R.id.content_tips_tv);
        submitBtn = root.findViewById(R.id.submit_btn);
        acceptTheRedEnvelope = root.findViewById(R.id.accept_the_red_envelope);
        submitBtn.setOnClickListener(this);
        moneyTv.setText("¥" + inRedPaperBean.getSignBonusAmount());
        if (inRedPaperBean.getBonusType() == 2) {
            // 2 周日红包
            submitBtn.setText(R.string.dialog_sign_in_accept_red_text);
            content_tips_tv.setText(R.string.dialog_sign_in_tips_sunday_text);
            acceptTheRedEnvelope.setVisibility(View.GONE);
        } else {
            //0 周一到周六普通红包 1 周一到周六翻倍红包
            submitBtn.setText(R.string.dialog_sign_in_red_button_text);
            content_tips_tv.setText(R.string.dialog_sign_in_tips_text);
            acceptTheRedEnvelope.setVisibility(View.VISIBLE);
            acceptTheRedEnvelope.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()){
            return;
        }
        if (v.getId() == R.id.submit_btn) {
            if (inRedPaperBean.getBonusType()==2){
                // 周日收下红包
                if (dialogCallBack!=null){
                    inRedPaperBean.setShare(false);
                    dialogCallBack.clickCallback(mMessageBox,inRedPaperBean);
                }
            }else {
                // 周一到周六，邀请好友红包翻倍 分享
                if (dialogCallBack!=null){
                    inRedPaperBean.setShare(true);
                    dialogCallBack.clickCallback(mMessageBox,inRedPaperBean);
                }
            }
            mMessageBox.dismiss();
        } else if (v.getId() == R.id.accept_the_red_envelope) {
            // 收下红包
            if (dialogCallBack!=null){
                inRedPaperBean.setShare(false);
                dialogCallBack.clickCallback(mMessageBox,inRedPaperBean);
            }
            mMessageBox.dismiss();
        }

    }
}
