package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/4 15:20
 * Summary: 运营商 编辑消息文案列表
 */
public class PushMessageDescBean {
    /**
     * createTime : 1578053458000
     * describe : 测试数据第二条
     * id : 2
     * systemId : 1
     * updateTime : 1580904656000
     */

    private long id;//文案ID
    private long createTime;//创建时间
    private String describe;//状态描述
    private long systemId;//添加人
    private long updateTime;//修改时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
