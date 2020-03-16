package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * data:2019/12/3
 * author:jyx
 * function:
 */
public class IncomeRecordViewHolder implements IContentHolder, View.OnClickListener {
    public static final String TAG = IncomeRecordViewHolder.class.getSimpleName();

    private TextView mTipsTv;

    private Context context;
    private MessageBox mMessageBox;


    public IncomeRecordViewHolder(Context context) {
        this.context = context;

    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View content = LayoutInflater.from(context).inflate(R.layout.messagebox_income_record_layout, parent, false);
        initView(content);
        return content;
    }

    private void initView(View content) {
        content.findViewById(R.id.close_iv).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_iv) {

            mMessageBox.dismiss();
        }
    }
}
