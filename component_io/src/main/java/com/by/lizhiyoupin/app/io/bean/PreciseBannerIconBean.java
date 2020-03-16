package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 10:04
 * Summary: 首页banner+icon+广告推荐
 */
public class PreciseBannerIconBean {

    /**
     * id : 1
     * selectionType : 0
     * position : 1
     * bannerColor:"#2F0012",//banner颜色
     * title:"标题",
     * img : https://lzyp-static.oss-cn-hangzhou.aliyuncs.com/image/default_head_img.png
     * url : https://lzyp-static.oss-cn-hangzhou.aliyuncs.com/image/default_head_img.png
     * sort :0
     * createdTime : 2019-10-15 00:00:00
     * updateTime : 2019-10-15 00:00:00
     * creatorId : 1
     * updateId : 1
     */

    private long id;
    private int selectionType;//精选类型 0banner 1活动位 2icon入口
    private String img;//图片地址
    private String url;//跳转链接
    private String bannerColor;//banner颜色
    private String title;//icon标题
    private int sort;//位置排序
    private String createdTime;
    private String updateTime;
    private long creatorId;//创建人id
    private long updateId;//修改人id

    public String getBannerColor() {
        return bannerColor;
    }

    public void setBannerColor(String bannerColor) {
        this.bannerColor = bannerColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
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

    @Override
    public String toString() {
        return "PreciseBannerIconBean{" +
                "id=" + id +
                ", selectionType=" + selectionType +
                ", img='" + img + '\'' +
                ", url='" + url + '\'' +
                ", bannerColor='" + bannerColor + '\'' +
                ", title='" + title + '\'' +
                ", sort=" + sort +
                ", createdTime='" + createdTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", creatorId=" + creatorId +
                ", updateId=" + updateId +
                '}';
    }
}
