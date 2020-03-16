package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/5 17:00
 * Summary: 版本更新
 */
public class UpdateViersionBean {


    /**
     * createdAt : 2019-11-20 20:59:37
     * deviceType : 2
     * downloadType : 2
     * downloadUrl : https://zhidao.baidu.com/question/332183900.html
     * id : 19
     * isForce : 2
     * updateLogs : 1.测试代码
     1.测试代码
     1.测试代码
     * version : 1.0.0
     * isUpdate: 是否需要更新 0 = 是 1 = 否
     */

    private int id; //编号
    private int deviceType;//设备类型 1安卓 2苹果
    private int isForce;//是否强制更新 （1是 0否）
    private String version;//版本号
    private int downloadType;//下载类型（1直接下载 2跳转下载）
    private List<String> updateLogs;//更新日志
    private String downloadUrl;//下载链接
    private int isUpdate; //是否需要更新 0 = 是 1 = 否

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(int downloadType) {
        this.downloadType = downloadType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsForce() {
        return isForce;
    }

    public void setIsForce(int isForce) {
        this.isForce = isForce;
    }

    public List<String> getUpdateLogs() {
        return updateLogs;
    }

    public void setUpdateLogs(List<String> updateLogs) {
        this.updateLogs = updateLogs;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
