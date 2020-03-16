package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.impl.SelectPayTypeCallback;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/31 10:09
 * Summary: 支付方式选择
 */
public class SelectPayTypeViewHolder implements IContentHolder, View.OnClickListener {
    private Context mContext;
    private MessageBox mMessageBox;
    private TextView mConfirmTv;
    private View mAliPayRl;
    private View mWechatPayRl;
    private CheckBox mAliCheckBox;
    private CheckBox mWechatCheckBox;
    private SelectPayTypeCallback callback;

    public SelectPayTypeViewHolder(Context context, SelectPayTypeCallback callback) {
        this.mContext = context;
        this.callback = callback;
    }


    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_pay_type_layout, parent, false);
        mMessageBox = messageBox;
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        mConfirmTv = inflate.findViewById(R.id.select_pay_confirm_tv);
        mAliPayRl = inflate.findViewById(R.id.ali_pay_rl);
        mWechatPayRl = inflate.findViewById(R.id.wechat_pay_rl);
        mAliCheckBox = inflate.findViewById(R.id.ali_checkBox);
        mWechatCheckBox = inflate.findViewById(R.id.wechat_checkBox);
        inflate.findViewById(R.id.space_view).setOnClickListener(this);
        mAliPayRl.setOnClickListener(this);
        mWechatPayRl.setOnClickListener(this);
        mConfirmTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ali_pay_rl) {
            mAliCheckBox.setChecked(true);
            mWechatCheckBox.setChecked(false);
        } else if (v.getId() == R.id.wechat_pay_rl) {
            mAliCheckBox.setChecked(false);
            mWechatCheckBox.setChecked(true);
        } else if (v.getId() == R.id.select_pay_confirm_tv) {
            if (callback != null) {
                int type = mAliCheckBox.isChecked() ? 0 : 1;
                callback.selectPayType(type);
            }
            mMessageBox.dismiss();
        }else if (v.getId()==R.id.space_view){
            mMessageBox.dismiss();
        }
    }
}
