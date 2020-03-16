package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2020/1/3
 * author:jyx
 * function:
 */
public class PushGoodsBean {
    private PreciseListBean commondity;//推送商品
    private Long id;//消息id
    private Long interactId;//推送消息发送端ID
    private int readStatus;//已读状态 0未读 1已读
    private String describe;//推送消息描述
    private long createTime;//创建时间
    private long updateTime;//修改时间
    private long sendTime;//推送时间
    private Long userId;//会员id
    private int sendStatus;//	发送状态 0未发送 1已发送

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public PreciseListBean getCommondity() {
        return commondity;
    }

    public void setCommondity(PreciseListBean commondity) {
        this.commondity = commondity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInteractId() {
        return interactId;
    }

    public void setInteractId(Long interactId) {
        this.interactId = interactId;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
