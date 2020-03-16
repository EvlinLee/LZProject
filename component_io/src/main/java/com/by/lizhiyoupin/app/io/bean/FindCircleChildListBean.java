package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 17:39
 * Summary:
 */
public class FindCircleChildListBean {
    /**
     * id : 1
     * descContext : jshksdbskdjghskjgbsvkbsvkjsdbvkjsdb
     * ringCommodityVideoImg : http://122.224.55.208:8220/web/#/page/edit/2/0
     * ringCommodirtVideo : http://122.224.55.208:8220/web/#/page/edit/2/0
     * ringFirstKindId : 1
     * ringSecondKindId : 1
     * sort : 1
     * topSort : 1
     * topStatus : 1
     * status : 1
     * avatar : http://122.224.55.208:8220/web/#/page/edit/2/0
     * name : sdsd
     * shareNumber : 1
     * releaseTime : 2019-11-12 11:11:11
     * createTime : 2019-11-12 11:11:11
     * creatorId : 1
     * updateTime : 2019-11-12 11:11:11
     * updateId : 1
     * commodityInfoList : [{"ringSecondKindId":1,"commodityName":"sdgsd","commodityImg":"sdfgfdgd.jpg","originalPrice":1,"realPrice":1,"commissionAmount":1,"platformType":1,"couponAmount":1}]
     * commodityImgList : ["sdfgfdgd.jpg","sdfgfdgd.jpg","sdfgfdgd.jpg"]
     * nextPage: "1"
     */

    private long id;//主键id
    private String nextPage;//下一页（只在每日爆款有用）
    private String descContext;//发圈文案
    private String ringCommodityVideo;//商品视频
    private String ringCommodityVideoImg;//商品视频第一张
    private long ringFirstKindId;//发圈类目管理表一级类目id
    private long ringSecondKindId;//发圈类目管理表二级类目id
    private int sort;//普通排序 数字越小排序越前
    private int topSort;//置顶排序 数字越大排序越前
    private int topStatus;//置顶状态 0 不置顶 1 置顶
    private int status;//发圈状态 1 显示 2 隐藏 3 定时发送
    private String avatar;//头像
    private String name;//昵称
    private long shareNumber;//分享次数
    private String releaseTime;//动态发送时间 2019-11-12 11:11:11
    private String createTime;//创建时间
    private long creatorId;//创建人
    private String updateTime;//修改时间
    private long updateId;//修改人
    private List<CommodityInfoListBean> commodityInfoList;//商品信息集合
    private List<String> commodityImgList;//商品图片集合

    public long getId() {
        return id;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescContext() {
        return descContext;
    }

    public void setDescContext(String descContext) {
        this.descContext = descContext;
    }

    public String getRingCommodityVideoImg() {
        return ringCommodityVideoImg;
    }

    public void setRingCommodityVideoImg(String ringCommodityVideoImg) {
        this.ringCommodityVideoImg = ringCommodityVideoImg;
    }

    public String getRingCommodityVideo() {
        return ringCommodityVideo;
    }

    public void setRingCommodityVideo(String ringCommodityVideo) {
        this.ringCommodityVideo = ringCommodityVideo;
    }

    public long getRingFirstKindId() {
        return ringFirstKindId;
    }

    public void setRingFirstKindId(long ringFirstKindId) {
        this.ringFirstKindId = ringFirstKindId;
    }

    public long getRingSecondKindId() {
        return ringSecondKindId;
    }

    public void setRingSecondKindId(long ringSecondKindId) {
        this.ringSecondKindId = ringSecondKindId;
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

    public int getTopStatus() {
        return topStatus;
    }

    public void setTopStatus(int topStatus) {
        this.topStatus = topStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(long shareNumber) {
        this.shareNumber = shareNumber;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
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

    public List<CommodityInfoListBean> getCommodityInfoList() {
        return commodityInfoList;
    }

    public void setCommodityInfoList(List<CommodityInfoListBean> commodityInfoList) {
        this.commodityInfoList = commodityInfoList;
    }

    public List<String> getCommodityImgList() {
        return commodityImgList;
    }

    public void setCommodityImgList(List<String> commodityImgList) {
        this.commodityImgList = commodityImgList;
    }

    public static class CommodityInfoListBean {
        /**
         * ringSecondKindId : 1
         * commodityId : 100120
         * commodityName : sdgsd
         * commodityImg : sdfgfdgd.jpg
         * originalPrice : 1
         * realPrice : 1
         * commissionAmount : 1
         * platformType : 1
         * couponAmount : 1
         */

        private long ringSecondKindId;//二级类目id
        private long commodityId;//商品id
        private String commodityName;//商品名称
        private String commodityImg;//商品图片
        private double originalPrice;//原价
        private double realPrice;//券后价
        private double commissionAmount;//佣金
        private int platformType;//平台类型 0 淘宝 1 京东 2 拼多多
        private double couponAmount;//优惠券金额

        public long getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(long commodityId) {
            this.commodityId = commodityId;
        }

        public long getRingSecondKindId() {
            return ringSecondKindId;
        }

        public void setRingSecondKindId(long ringSecondKindId) {
            this.ringSecondKindId = ringSecondKindId;
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

        public double getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(double originalPrice) {
            this.originalPrice = originalPrice;
        }

        public double getRealPrice() {
            return realPrice;
        }

        public void setRealPrice(double realPrice) {
            this.realPrice = realPrice;
        }

        public double getCommissionAmount() {
            return commissionAmount;
        }

        public void setCommissionAmount(double commissionAmount) {
            this.commissionAmount = commissionAmount;
        }

        public int getPlatformType() {
            return platformType;
        }

        public void setPlatformType(int platformType) {
            this.platformType = platformType;
        }

        public double getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(double couponAmount) {
            this.couponAmount = couponAmount;
        }
    }



}
