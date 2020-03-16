package com.by.lizhiyoupin.app.io.bean;

import java.io.Serializable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/12 11:36
 * Summary: 收货人地址信息等
 */
public class AddressBean implements Serializable {
    private static final long serialVersionUID = -706210544600464481L;
    /**
     * id : 1
     * userId : 1
     * consignee : 收货人
     * mobile : 15757878787
     * province : 1
     * city : 1
     * district : 1
     * street : 1
     * provinceName : 省名
     * cityName : 市名
     * districtName : 区名
     * streetName : 街道名
     * address : 详细地址
     * postalCode : 邮编
     * isDefault : 1
     * createdTime : 2019-10-24 11:11:11
     * updatedTime : 2019-10-24 11:11:11
     */

    private Long id=null;//主键id
    private long userId;//会员id
    private String consignee="";//收货人
    private String mobile="";//手机号
    private String provinceName="";//省
    private String cityName="";//市
    private String districtName="";//区
    private String streetName="";//街道门牌
    private String address="";//全部详细地址
    private int isDefault;//是否默认收货地址 0.否 1.是
    private String postalCode;//邮编
    private String createdTime;
    private String updatedTime;

    public AddressBean() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
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


    @Override
    public String toString() {
        return "AddressBean{" +
                "id=" + id +
                ", userId=" + userId +
                ", consignee='" + consignee + '\'' +
                ", mobile='" + mobile + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", streetName='" + streetName + '\'' +
                ", address='" + address + '\'' +
                ", isDefault=" + isDefault +
                ", postalCode='" + postalCode + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                '}';
    }

}
