package com.by.lizhiyoupin.app.io.bean;

import com.by.lizhiyoupin.app.io.WebUrlManager;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 17:33
 * Summary:
 */
public class BusinessArticleBean {
    /**
     * id : 1
     * articleTitle : dfg
     * articleImg : http://122.224.55.208:8220/web/#/page/edit/2/0
     * articleContext : dfdfgdfgdsfdfbdf
     * schoolFirstKindId : 1,2,1,2,
     * schoolSecondKindId : 1,1,1,1,
     * status : 1
     * recommend : 1
     * information : 1
     * isTop : 1
     * sort : 1
     * topSort : 1
     * shareNumber : 1
     * viewPermitLevel : 1
     * isPay : 1
     * articlePrice : 1.1
     * createTime : 2019-11-12 11:11:11
     * creatorId : 1
     * updateTime : 2019-11-12 11:11:11
     * updateId : 1
     */

    private long id; //文章主键
    private String articleTitle;//文章标题
    private String articleImg;//文章图片地址
    private String articleContext;//文章正文
    private String schoolFirstKindId;//商学院类目管理表一级类目id
    private String schoolSecondKindId;//商学院类目管理表二级类目id
    private int status;//文章状态 1 显示 2 隐藏
    private int recommend;//是否是为你推荐 1 否 2 是
    private int information;//是否是荔枝资讯 1 否 2 是
    private int isTop;//是否置顶 1 不置顶 2 置顶
    private int sort;//普通排序 越大越前面
    private int topSort;//置顶排序 越大越前面
    private long shareNumber;//分享数值
    private List<String> viewPermitLevelList;//文章查看权限会员等级限制集合
    private int isPay;//是否付费文章 0 不是 1 是
    private double articlePrice;//文章查看费用
    private String createTime;//创建时间 2019-11-12 11:11:11
    private long creatorId;//创建人id
    private String updateTime;//修改时间
    private long updateId;//修改人id
    //articleUrl 自己添加的
    private String articleClickUrl;//CommonWebConst.BUSINESS_ARTICLE_URL+id;// 自己添加的--文章跳转url或分享url

    public List<String> getViewPermitLevelList() {
        return viewPermitLevelList;
    }

    public void setViewPermitLevelList(List<String> viewPermitLevelList) {
        this.viewPermitLevelList = viewPermitLevelList;
    }

    public String getArticleClickUrl() {
        return WebUrlManager.getBusinessArticleUrl(id);
    }

    public void setArticleClickUrl(String articleClickUrl) {
        this.articleClickUrl = articleClickUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
    }

    public String getArticleContext() {
        return articleContext;
    }

    public void setArticleContext(String articleContext) {
        this.articleContext = articleContext;
    }

    public String getSchoolFirstKindId() {
        return schoolFirstKindId;
    }

    public void setSchoolFirstKindId(String schoolFirstKindId) {
        this.schoolFirstKindId = schoolFirstKindId;
    }

    public String getSchoolSecondKindId() {
        return schoolSecondKindId;
    }

    public void setSchoolSecondKindId(String schoolSecondKindId) {
        this.schoolSecondKindId = schoolSecondKindId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getInformation() {
        return information;
    }

    public void setInformation(int information) {
        this.information = information;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getTopSort() {
        return topSort;
    }

    public void setTopSort(int topSort) {
        this.topSort = topSort;
    }

    public long getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(long shareNumber) {
        this.shareNumber = shareNumber;
    }


    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public double getArticlePrice() {
        return articlePrice;
    }

    public void setArticlePrice(double articlePrice) {
        this.articlePrice = articlePrice;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(long updateId) {
        this.updateId = updateId;
    }
}
