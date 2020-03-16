package com.by.lizhiyoupin.app.component_ui.manager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/17 16:56
 * Summary:
 */
public interface IDialogManager {

    void showShareH5Dialog(String url,String title,String desc);
    void showAuthDialog(AppCompatActivity appCompatActivity,String url);
    void showSharePictureDialog(String url);
}
