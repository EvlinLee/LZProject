package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.impl.DialogCallBack;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/12 13:36
 * Summary:
 */
public class DevelopSettingIpViewHolder implements IContentHolder, View.OnClickListener {
    public static final String TAG = DevelopSettingIpViewHolder.class.getSimpleName();

    private Context context;
    private MessageBox mMessageBox;
    private TextView dev_ip;
    private TextView test_ip;
    private TextView prod_tv;
    private DialogCallBack<View>  dialogCallBack;

    public DevelopSettingIpViewHolder(Context context, DialogCallBack<View> dialogCallBack) {
        this.context = context;
        this.dialogCallBack=dialogCallBack;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View content = LayoutInflater.from(context).inflate(R.layout.messagebox_develop_setting_layout, parent, false);
        initView(content);
        return content;
    }

    private void initView(View content) {
        prod_tv = content.findViewById(R.id.prod_tv);
        test_ip = content.findViewById(R.id.test_ip);
        dev_ip = content.findViewById(R.id.dev_ip);
        prod_tv.setOnClickListener(this);
        test_ip.setOnClickListener(this);
        dev_ip.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
       dialogCallBack.clickCallback(mMessageBox,v);
    }
}
