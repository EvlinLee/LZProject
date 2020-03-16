package com.by.lizhiyoupin.app.message_box.listener;

import android.view.KeyEvent;

import com.by.lizhiyoupin.app.message_box.MessageBox;


public interface OnKeyPressClickListener {

    void onKeyClick(MessageBox messageBox, int keyCode, KeyEvent event);
}
