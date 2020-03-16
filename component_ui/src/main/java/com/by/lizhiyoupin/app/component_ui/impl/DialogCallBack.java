package com.by.lizhiyoupin.app.component_ui.impl;

import com.by.lizhiyoupin.app.message_box.MessageBox;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/30 14:24
 * Summary:
 */
public interface DialogCallBack<T> {
    void clickCallback(MessageBox messageBox, T t);
}
