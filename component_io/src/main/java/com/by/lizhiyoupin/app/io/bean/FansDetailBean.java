package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/12 10:26
 * Summary:
 */
public class FansDetailBean {

    /**
     * allContribution : 0
     * avatar :
     * createTime : 2019-10-30 11:48:01
     * lastMonthContribution : 0
     * name : yungying2
     * nowMonthContribution : 0
     * uid : 62
     * weiXin : yungying2
     */

    private double allContribution;//累计为我贡献
    private String avatar;//头像
    private String createTime;
    private double lastMonthContribution;//上月为我贡献
    private String name;
    private double nowMonthContribution;//当月为我贡献
    private Long uid;//会员ID
    private String weiXin;//微信号

    public double getAllContribution() {
        return allContribution;
    }

    public void setAllContribution(double allContribution) {
        this.allContribution = allContribution;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getLastMonthContribution() {
        return lastMonthContribution;
    }

    public void setLastMonthContribution(double lastMonthContribution) {
        this.lastMonthContribution = lastMonthContribution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNowMonthContribution() {
        return nowMonthContribution;
    }

    public void setNowMonthContribution(double nowMonthContribution) {
        this.nowMonthContribution = nowMonthContribution;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getWeiXin() {
        return weiXin;
    }

    public void setWeiXin(String weiXin) {
        this.weiXin = weiXin;
    }
}
