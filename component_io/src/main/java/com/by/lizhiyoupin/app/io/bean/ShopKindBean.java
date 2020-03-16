package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * data:2019/11/23
 * author:jyx
 * function:
 */
public class ShopKindBean {

        /**
         * describe : DrCiLabo海外旗舰店
         * id : 3
         * lzCommodityInfoVOs : [{"commissionMoney":4.55,"couponAmount":10,
         * "discountsPriceAfter":230,"icon":0,"itemId":"547620782300","pictUrl":"https://img
         * .alicdn.com/bao/uploaded/i1/3157354417/O1CN01VelAaJ1iV1zfZZi2J_!!0-item_pic.jpg",
         * "title":"城野医生冻干粉修复精华液女 寡肽原液祛痘印淡化痘疤熬夜救星男","volume":582},{"commissionMoney":4.16,
         * "couponAmount":50,"discountsPriceAfter":210,"icon":0,"itemId":"576526130058",
         * "pictUrl":"https://img.alicdn.com/bao/uploaded/i4/3157354417/O1CN01fpN4Yx1iV1zhmRzMw_
         * !!0-item_pic.jpg","title":"城野医生亲研日本进口温感洁面卸妆啫喱面部清洁卸妆乳洗面奶男女","volume":5}]
         * shopActivities :
         * shopImg : https://lzyp-static.oss-cn-hangzhou.aliyuncs
         * .com/IMAGE/201911/1d62e16ec31443a6a1dc2f2eec42b276.jpg
         * shopName : DrCiLabo海外旗舰店
         * url : DrCiLabo海外旗舰店
         */

        private String describe;
        private int id;
        private String shopActivities;
        private String shopImg;
        private String shopName;
        private String url;
        private int followStatus;

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    private List<LzCommodityInfoVOsBean> lzCommodityInfoVOs;

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShopActivities() {
            return shopActivities;
        }

        public void setShopActivities(String shopActivities) {
            this.shopActivities = shopActivities;
        }

        public String getShopImg() {
            return shopImg;
        }

        public void setShopImg(String shopImg) {
            this.shopImg = shopImg;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<LzCommodityInfoVOsBean> getLzCommodityInfoVOs() {
            return lzCommodityInfoVOs;
        }

        public void setLzCommodityInfoVOs(List<LzCommodityInfoVOsBean> lzCommodityInfoVOs) {
            this.lzCommodityInfoVOs = lzCommodityInfoVOs;
        }

        public static class LzCommodityInfoVOsBean {
            /**
             * commissionMoney : 4.55
             * couponAmount : 10
             * discountsPriceAfter : 230.0
             * icon : 0
             * itemId : 547620782300
             * pictUrl : https://img.alicdn
             * .com/bao/uploaded/i1/3157354417/O1CN01VelAaJ1iV1zfZZi2J_!!0-item_pic.jpg
             * title : 城野医生冻干粉修复精华液女 寡肽原液祛痘印淡化痘疤熬夜救星男
             * volume : 582
             */

            private double commissionMoney;
            private double couponAmount;
            private double discountsPriceAfter;
            private int icon;
            private Long itemId;
            private String pictUrl;
            private String title;
            private int volume;

            public double getCommissionMoney() {
                return commissionMoney;
            }

            public void setCommissionMoney(double commissionMoney) {
                this.commissionMoney = commissionMoney;
            }

            public double getCouponAmount() {
                return couponAmount;
            }

            public void setCouponAmount(double couponAmount) {
                this.couponAmount = couponAmount;
            }

            public double getDiscountsPriceAfter() {
                return discountsPriceAfter;
            }

            public void setDiscountsPriceAfter(double discountsPriceAfter) {
                this.discountsPriceAfter = discountsPriceAfter;
            }

            public int getIcon() {
                return icon;
            }

            public void setIcon(int icon) {
                this.icon = icon;
            }

            public Long getItemId() {
                return itemId;
            }

            public void setItemId(Long itemId) {
                this.itemId = itemId;
            }

            public String getPictUrl() {
                return pictUrl;
            }

            public void setPictUrl(String pictUrl) {
                this.pictUrl = pictUrl;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getVolume() {
                return volume;
            }

            public void setVolume(int volume) {
                this.volume = volume;
            }
        }
    }

