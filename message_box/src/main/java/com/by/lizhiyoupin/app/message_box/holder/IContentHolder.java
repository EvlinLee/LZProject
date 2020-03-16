package com.by.lizhiyoupin.app.message_box.holder;

import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.message_box.MessageBox;


/**
 * 对话框View
 */
public interface IContentHolder {

    View contentView(MessageBox messageBox, ViewGroup parent);
}
