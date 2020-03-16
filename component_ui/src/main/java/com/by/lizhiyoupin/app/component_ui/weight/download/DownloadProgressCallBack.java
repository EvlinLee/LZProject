package com.by.lizhiyoupin.app.component_ui.weight.download;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/10 16:38
 * Summary:
 */
public interface DownloadProgressCallBack {
    void downloadProgress(int progress);
    void downloadException(Exception e);
    void onInstallStart();
}
