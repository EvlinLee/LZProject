package com.by.lizhiyoupin.app.component_ui.impl;

import com.by.lizhiyoupin.app.message_box.MessageBox;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/25 10:25
 * Summary:
 */
public interface ShareDialogClickContentCallback<T> {
    void clickCallback(MessageBox messageBox, T t);
}
