package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 11:51
 * Summary: vip导购详情
 */
public class VipGoodsBean {

    /**
     * id : 1
     * bizId : 1
     * title : 标题
     * mainImg : "http://www.ss.jpg"
     * skatingImgList : ["http://www.ss.jpg"]
     * detailImgList : ["http://www.ss.jpg"]
     * type : 1
     * level : 1
     * price : 1.11
     * describe : 描述
     * privilege : 特权描述
     * rule : 规则
     * status : 1
     * createdTime : 2019-10-22 11:11:11
     * updatedTime : 2019-10-22 11:11:11
     * teamInfo : 团队返佣
     * selfBuy : 1
     * provinceRate : 1
     * cityRate : 1
     * districtRate : 1
     * rebateInfo : 返佣等级配置
     * opRebateInfo : 运营商分佣配置
     * sort : 2
     */
   private long id;//主键id
    private int bizId;//商户id
    private String title;//商品名称
    private String mainImg;//商品主图
    private int type;//1月付 2年付 3永久
    private int level;//等级id
    private double price;//价格
    private String describe;//商品描述
    private String privilege;//特权描述
    private String rule;//规则介绍
    private int status;//状态，1启用，0关闭
    private String createdTime;
    private String updatedTime;
    private String teamInfo;//团队返佣
    private int selfBuy;//自购比例
    private int provinceRate;//省代理奖励比例
    private int cityRate;//市代理奖励比例
    private int districtRate;//区代理奖励比例
    private String rebateInfo;//返佣等级配置
    private String opRebateInfo;//运营商分佣配置
    private int sort;//排序
    private List<String> skatingImgList;//轮播图集合
    private List<String> detailImgList;//详情介绍图集合

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBizId() {
        return bizId;
    }

    public void setBizId(int bizId) {
        this.bizId = bizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(String teamInfo) {
        this.teamInfo = teamInfo;
    }

    public int getSelfBuy() {
        return selfBuy;
    }

    public void setSelfBuy(int selfBuy) {
        this.selfBuy = selfBuy;
    }

    public int getProvinceRate() {
        return provinceRate;
    }

    public void setProvinceRate(int provinceRate) {
        this.provinceRate = provinceRate;
    }

    public int getCityRate() {
        return cityRate;
    }

    public void setCityRate(int cityRate) {
        this.cityRate = cityRate;
    }

    public int getDistrictRate() {
        return districtRate;
    }

    public void setDistrictRate(int districtRate) {
        this.districtRate = districtRate;
    }

    public String getRebateInfo() {
        return rebateInfo;
    }

    public void setRebateInfo(String rebateInfo) {
        this.rebateInfo = rebateInfo;
    }

    public String getOpRebateInfo() {
        return opRebateInfo;
    }

    public void setOpRebateInfo(String opRebateInfo) {
        this.opRebateInfo = opRebateInfo;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<String> getSkatingImgList() {
        return skatingImgList;
    }

    public void setSkatingImgList(List<String> skatingImgList) {
        this.skatingImgList = skatingImgList;
    }

    public List<?> getDetailImgList() {
        return detailImgList;
    }

    public void setDetailImgList(List<String> detailImgList) {
        this.detailImgList = detailImgList;
    }
}
