package com.by.lizhiyoupin.app.manager;

import com.by.lizhiyoupin.app.component_ui.manager.IDialogManager;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.LZShare;

import androidx.appcompat.app.AppCompatActivity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/17 17:01
 * Summary:
 */
public class DiaLogManagerImpl implements IDialogManager {
    @Override
    public void showShareH5Dialog(String url, String title,String desc) {
        LZShare.share(LZShare.getChannelListH5(url,title, desc)).show();
    }

    @Override
    public void showAuthDialog(AppCompatActivity appCompatActivity, String url) {
        DiaLogManager.showAuthDialog(appCompatActivity, appCompatActivity.getSupportFragmentManager(), url);
    }

    @Override
    public void showSharePictureDialog(String url) {
        LZShare.share(LZShare.getChannelListPic(url)).show();
    }


}
