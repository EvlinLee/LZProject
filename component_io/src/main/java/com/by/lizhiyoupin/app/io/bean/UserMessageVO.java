package com.by.lizhiyoupin.app.io.bean;

import java.io.Serializable;

/**
 * @title: UserFansDetailsVO
 * @projectName: lzyp
 * @author: xuezhijia
 * @description: 会员消息VO类
 * @date: 2019/11/9 19:54
 */
public class  UserMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    public UserMessageVO() {

    }

    /**
     * 会员ID
     */
    private Long uid;
    private Long id;//消息id

    /**
     * 通知类型 1系统消息 2普通佣金 3优选佣金 4每日爆款
     */
    private Integer type;
    /**
     * 读取状态 0未读 1已读
     */
    private Integer readStatus;
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 会员头像
     */
    private String avatar;
    /**
     * 图片
     */
    private String image;
    /**
     * 链接地址
     */
    private String link;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 平台类型 1淘宝 2京东 3拼多多 4自营
     */
    private int platformType;
    /**
     * 订单金额
     */
    private double price;
    /**
     * 获得佣金
     */
    private double commission;
    /**
     * 消息时间
     */
    private String createTime;
    /*
    * 商品id
    * */
    private Long commodityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserMessageVO{" +
                "uid=" + uid +
                ", type=" + type +
                ", readStatus=" + readStatus +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", avatar='" + avatar + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", orderId='" + orderId + '\'' +
                ", platformType=" + platformType +
                ", price=" + price +
                ", commission=" + commission +
                ", createTime=" + createTime +
                '}';
    }
}
