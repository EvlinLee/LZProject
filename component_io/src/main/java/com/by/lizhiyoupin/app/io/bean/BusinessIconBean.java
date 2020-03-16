package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 21:20
 * Summary:
 */
public class BusinessIconBean {
    /**
     * id : 1
     * businessKindLevel : 1
     * sort : 1
     * kindName : 类目名称
     * superiorId : 1
     * businessSchoolKindImg : http://122.224.55.208:8220/web/#/page/edit/2/0
     * url : http://122.224.55.208:8220/web/#/page/edit/2/0
     * createTime : 2019-11-12 11:11:11
     * creatorId : 1
     * updateTime : 2019-11-12 11:11:11
     * updateId : 1
     */

    private long id;//主键id
    private String kindName;//类目名称
    private long superiorId;//上级id
    private String businessSchoolKindImg;//分类图片,如果级别为一级类目
    private String url;//url链接
    private int businessKindLevel;//类目级别 0 一级类目 1 二级类目

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(long superiorId) {
        this.superiorId = superiorId;
    }

    public String getBusinessSchoolKindImg() {
        return businessSchoolKindImg;
    }

    public void setBusinessSchoolKindImg(String businessSchoolKindImg) {
        this.businessSchoolKindImg = businessSchoolKindImg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBusinessKindLevel() {
        return businessKindLevel;
    }

    public void setBusinessKindLevel(int businessKindLevel) {
        this.businessKindLevel = businessKindLevel;
    }
}
