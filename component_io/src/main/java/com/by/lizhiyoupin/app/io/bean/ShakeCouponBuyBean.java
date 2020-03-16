package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/15 19:42
 * Summary: 抖券弹幕
 */
public class ShakeCouponBuyBean {

    /**
     * userpicture : https://lzyp-static.oss-cn-hangzhou.aliyuncs.com/touxiang/lz-touxiang168.jpg
     * type : 已购买该商品
     * username : 彩****畜
     */

    private String userpicture;
    private String type="";
    private String username;

    public String getUserpicture() {
        return userpicture;
    }

    public void setUserpicture(String userpicture) {
        this.userpicture = userpicture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
