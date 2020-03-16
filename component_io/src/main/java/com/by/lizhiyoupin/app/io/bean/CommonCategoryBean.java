package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 10:31
 * Summary:
 */
public class CommonCategoryBean {
    /**
     * id : 1 //主键id
     * kindLevel : 0 //类目级别 0 一级类目 1 二级类目 2 banner
     * sort : 1 //排序 越小越前面
     * platformType : 1 //平台类型
     * kindId : 1 //类目id
     * kindName : 类目或icon的名称
     * superiorId : 1 //上级类目id
     * kindImg : https://lzyp-static.oss-cn-hangzhou.aliyuncs.com/image/default_head_img.png //类目图片
     * url : //banner或Icon的跳转链接
     * bannerColor :  #FF1215
     * createTime : 2019-10-15 00:00:00
     * creatorId : 1 //创建人id
     */

    private long id;
    private String kindName;//类目或icon的名称
    private String kindImg;//banner或icon图片
    private String url; //banner或Icon的跳转链接
    private String bannerColor;//banner对应颜色
    private int kindLevel;
    private int sort;
    private int platformType;
    private long kindId;

    private long superiorId;

    private String createTime;
    private long creatorId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBannerColor() {
        return bannerColor;
    }

    public void setBannerColor(String bannerColor) {
        this.bannerColor = bannerColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getKindLevel() {
        return kindLevel;
    }

    public void setKindLevel(int kindLevel) {
        this.kindLevel = kindLevel;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public long getKindId() {
        return kindId;
    }

    public void setKindId(long kindId) {
        this.kindId = kindId;
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

    public String getKindImg() {
        return kindImg;
    }

    public void setKindImg(String kindImg) {
        this.kindImg = kindImg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}
