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
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/12 13:36
 * Summary:
 */
public class OrderFindErrorViewHolder implements IContentHolder, View.OnClickListener {
    public static final String TAG = OrderFindErrorViewHolder.class.getSimpleName();

    private TextView mTipsTv;

    private Context context;
    private MessageBox mMessageBox;


    public OrderFindErrorViewHolder(Context context) {
        this.context = context;

    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View content = LayoutInflater.from(context).inflate(R.layout.messagebox_order_find_error_layout, parent, false);
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
