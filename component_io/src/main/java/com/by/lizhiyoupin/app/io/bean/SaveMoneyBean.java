package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/9 13:32
 * Summary:
 */
public class SaveMoneyBean {

    /**
     * allSave : 0.00
     * details : [{"createTime":"2019-11-08 17:41:27","imgages":"http://t00img.yangkeduo.com/goods/images/images/2019-09-19/5d7272b7ffdaea3be7d28382f635eaed.jpeg","orderId":"191031-633591890752522","payment":97.98,"platformType":3,"save":0,"title":"【南极人】羽绒裤女外穿加厚高腰中老年妈妈修身白鸭绒羽绒棉裤女"}]
     */

    private double allSave; //总省钱
    private List<DetailsBean> details;

    public double getAllSave() {
        return allSave;
    }

    public void setAllSave(double allSave) {
        this.allSave = allSave;
    }

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

    public static class DetailsBean {
        /**
         * createTime : 2019-11-08 17:41:27
         * imgages : http://t00img.yangkeduo.com/goods/images/images/2019-09-19/5d7272b7ffdaea3be7d28382f635eaed.jpeg
         * orderId : 191031-633591890752522
         * payment : 97.98
         * platformType : 3
         * save : 0.00
         * title : 【南极人】羽绒裤女外穿加厚高腰中老年妈妈修身白鸭绒羽绒棉裤女
         */

        private String createTime;//创建时间 2019-11-08 17:41:27
        private String imgages;//图片
        private String orderId; //订单号
        private double payment; //实付金额
        private int platformType;//平台类型 1淘宝 2京东 3拼多多
        private double save; //省钱金额
        private String title; //标题

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getImgages() {
            return imgages;
        }

        public void setImgages(String imgages) {
            this.imgages = imgages;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public double getPayment() {
            return payment;
        }

        public void setPayment(double payment) {
            this.payment = payment;
        }

        public int getPlatformType() {
            return platformType;
        }

        public void setPlatformType(int platformType) {
            this.platformType = platformType;
        }

        public double getSave() {
            return save;
        }

        public void setSave(double save) {
            this.save = save;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
