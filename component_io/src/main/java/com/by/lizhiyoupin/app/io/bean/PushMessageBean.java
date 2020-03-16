package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/12/3
 * author:jyx
 * function:
 */
public class PushMessageBean {
    private String title;//标题
    private String subtitle;//副标题
    private String content;//内容
    private String commodityId;//商品id
    private String msgUrl;//h5
    private int  msgType;//推送类型 1.系统消息 2.每日爆款

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
