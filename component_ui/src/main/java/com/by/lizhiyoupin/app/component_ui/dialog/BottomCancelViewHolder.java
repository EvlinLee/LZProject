package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IBottomCloseHolder;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/11 20:18
 * Summary: 底部取消按鈕
 */
public class BottomCancelViewHolder implements IBottomCloseHolder {
    private Context context;

    public BottomCancelViewHolder(final Context context) {
        super();
        this.context = context;
    }

    @Override
    public View bottomCloseView(MessageBox messageBox, ViewGroup parent) {
        View tipsContent = LayoutInflater.from(context).inflate(R.layout.messagebox_bottom_cancel_layout, parent, false);
        tipsContent.findViewById(R.id.share_close_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBox.dismiss();
            }
        });
        return tipsContent;
    }


}
