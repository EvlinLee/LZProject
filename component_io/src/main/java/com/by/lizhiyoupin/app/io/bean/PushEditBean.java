package com.by.lizhiyoupin.app.io.bean;

import java.util.ArrayList;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/4 13:53
 * Summary: 运营商 消息编辑bean
 */
public class PushEditBean {
    /**
     * commondity : {"commissionRate":30.2,"couponAmount":40,"commissionMoney":20,"discountsPriceAfter":200,"icon":2,"itemId":3070937317,"pictUrl":"http://www.baidu.com/img.jpg","title":"大瓶洗衣液 4斤装 全国包邮 低泡易漂 薰衣草香洗衣液 不含荧光剂","url":"","volume":100}
     * createTime : 1578018271000
     * describe : 测试的手动添加数据
     * id : 1
     * receive : 253
     * receiveGroup : [253,12132332]
     * sendTime : 1578018148000
     * updateTime : 1578018269000
     * userId : 58
     * sendStatus : 1
     */

    private long id; //消息ID
    private PreciseListBean commondity;//推送商品
    private ArrayList<FansDataBean.FansListBean> fansList;//选中粉丝详情列表（3个）
    private String describe;//推送消息描述
    private ArrayList<Long> receiveGroup;//选中粉丝id列表
    private String receive;//推送对象 是否全部，0代表全部
    private long sendTime;//发送时间
    private long createTime;//创建时间
    private long updateTime;//修改时间
    private long userId;//发送人会员id
    private int sendStatus;//发送状态 0未发送 1已发送

    public ArrayList<Long> getReceiveGroup() {
        return receiveGroup;
    }

    public void setReceiveGroup(ArrayList<Long> receiveGroup) {
        this.receiveGroup = receiveGroup;
    }

    public ArrayList<FansDataBean.FansListBean> getFansList() {
        return fansList;
    }

    public void setFansList(ArrayList<FansDataBean.FansListBean> fansList) {
        this.fansList = fansList;
    }

    public PreciseListBean getCommondity() {
        return commondity;
    }

    public void setCommondity(PreciseListBean commondity) {
        this.commondity = commondity;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

}
