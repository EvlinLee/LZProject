package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/25 10:44
 * Summary: 种草
 */
public class GuideArticleBean {

    /**
     * id : 1
     * title : 文章短标题
     * longTitle : 文章长标题长长长
     * mainImg : http://122.224.55.208:8220/web/#/page/edit/2/0
     * label : sdgsdfsd
     * viewCount : 1111
     * issueName : 都发给对方答复
     * issueHeadImg : http://122.224.55.208:8220/web/#/page/edit/2/0
     * articleType : 1
     */

    private long id;//主键id
    private String title;//文章短标题
    private String longTitle;//文章长标题
    private String mainImg;//文章主图
    private String label;//标签
    private int viewCount;//浏览量
    private String issueName;//发布人名称
    private String issueHeadImg;//发布人头像
    private int articleType;//文章类型 0 本地文章 1 三方文章

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLongTitle() {
        return longTitle;
    }

    public void setLongTitle(String longTitle) {
        this.longTitle = longTitle;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getIssueHeadImg() {
        return issueHeadImg;
    }

    public void setIssueHeadImg(String issueHeadImg) {
        this.issueHeadImg = issueHeadImg;
    }

    public int getArticleType() {
        return articleType;
    }

    public void setArticleType(int articleType) {
        this.articleType = articleType;
    }
}
