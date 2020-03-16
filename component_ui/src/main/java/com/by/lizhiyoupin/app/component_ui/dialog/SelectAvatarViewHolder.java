package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.impl.SelectAvatarCallback;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * data:2019/11/5
 * author:jyx
 * function:
 */
public class SelectAvatarViewHolder implements IContentHolder, View.OnClickListener {
    private Context mContext;
    private MessageBox mMessageBox;
    private LinearLayout makephoto,photo,cancle;
    private SelectAvatarCallback callback;
    private View space_view;

    public SelectAvatarViewHolder(Context context, SelectAvatarCallback callback) {
        mContext = context;
        this.callback = callback;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_avatar_type_layout, parent, false);
        mMessageBox = messageBox;
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
          makephoto = inflate.findViewById(R.id.makephoto);
          photo = inflate.findViewById(R.id.photo);
          cancle = inflate.findViewById(R.id.cancle);
          space_view = inflate.findViewById(R.id.space_view);
        makephoto.setOnClickListener(this);
        photo.setOnClickListener(this);
        cancle.setOnClickListener(this);
        space_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.photo){
            int type=0;
            callback.selectAvatar(type);
            mMessageBox.dismiss();
        }else if (v.getId()==R.id.makephoto){
            int type=1;
            callback.selectAvatar(type);
            mMessageBox.dismiss();
        }else if (v.getId()==R.id.cancle){
            mMessageBox.dismiss();
        }else if (v.getId()==R.id.space_view){
            mMessageBox.dismiss();
        }

    }
}
