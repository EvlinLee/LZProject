package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 15:05
 * Summary:
 */
public class FindCircleTabListBean {
    private long id;//主键id
    private int ringKindLevel;//类目级别 0 一级类目 1 二级类目
    private int sort;//排序位置 越小越前面
    private String kindName;//类目名称
    private long superiorId;//上级id
    private String url;//url链接
    private String createTime;//创建时间
    private String updateTime;//修改时间
    private long creatorId;//创建人id
    private long updateId;//修改人id

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRingKindLevel() {
        return ringKindLevel;
    }

    public void setRingKindLevel(int ringKindLevel) {
        this.ringKindLevel = ringKindLevel;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(long updateId) {
        this.updateId = updateId;
    }
}
