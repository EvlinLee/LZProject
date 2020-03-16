package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/25 16:18
 * Summary: 种草详情
 */
public class GuideArticleDetailBean {
    /**
     * id : 1
     * title : 文章短标题
     * longTitle : 文章长标题长长长
     * mainImg : http://122.224.55.208:8220/web/#/page/edit/2/0
     * viewCount : 1111
     * likeCount : 1111
     * context : 阿贾克斯的发挥空间闪电发货SDK剧本杀大V空间宝石大V快睡吧大V挥手道别是十点半是是框架的VB神盾局SDK军SDK不能说
     * issueName : 都发给对方答复
     * issueHeadImg : http://122.224.55.208:8220/web/#/page/edit/2/0
     * articleType : 1
     * commodityList : [{"commodityId":"121212","platformType":0,"commodityName":"sdfgsd","commodityImg":"http://122.224.55.208:8220/web/#/page/edit/2/0","realPrice":1212.12,"monthSales":121212,"couponAmount":112.12,"commissionRate":11.12,"commissionAmount":12.11}]
     */

    private long id;//主键id
    private String title;//文章短标题
    private String longTitle;//文章长标题
    private String mainImg;//文章主图
    private int viewCount;//浏览量
    private int likeCount;//点赞量
    private String context;//文章内容
    private String issueName;//发布人名称
    private String issueHeadImg;//发布人头像
    private int articleType;//文章类型 0 本地文章 1 三方文章
    private List<CommodityListBean> commodityList;//商品信息集合

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

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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

    public List<CommodityListBean> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<CommodityListBean> commodityList) {
        this.commodityList = commodityList;
    }

    public static class CommodityListBean {
        /**
         * commodityId : 121212
         * platformType : 0
         * commodityName : sdfgsd
         * commodityImg : http://122.224.55.208:8220/web/#/page/edit/2/0
         * realPrice : 1212.12
         * monthSales : 121212
         * couponAmount : 112.12
         * commissionRate : 11.12
         * commissionAmount : 12.11
         */

        private String commodityId;//商品id
        private int platformType;//平台类型
        private String commodityName;//商品名称
        private String commodityImg;//商品图片
        private String realPrice;//券后价
        private int monthSales;//月销量
        private double couponAmount;//优惠券金额
        private String commissionRate;//佣金比例
        private String commissionAmount;//佣金

        public String getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(String commodityId) {
            this.commodityId = commodityId;
        }

        public int getPlatformType() {
            return platformType;
        }

        public void setPlatformType(int platformType) {
            this.platformType = platformType;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public String getCommodityImg() {
            return commodityImg;
        }

        public void setCommodityImg(String commodityImg) {
            this.commodityImg = commodityImg;
        }

        public String getRealPrice() {
            return realPrice;
        }

        public void setRealPrice(String realPrice) {
            this.realPrice = realPrice;
        }

        public int getMonthSales() {
            return monthSales;
        }

        public void setMonthSales(int monthSales) {
            this.monthSales = monthSales;
        }

        public double getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(double couponAmount) {
            this.couponAmount = couponAmount;
        }

        public String getCommissionRate() {
            return commissionRate;
        }

        public void setCommissionRate(String commissionRate) {
            this.commissionRate = commissionRate;
        }

        public String getCommissionAmount() {
            return commissionAmount;
        }

        public void setCommissionAmount(String commissionAmount) {
            this.commissionAmount = commissionAmount;
        }
    }
}
