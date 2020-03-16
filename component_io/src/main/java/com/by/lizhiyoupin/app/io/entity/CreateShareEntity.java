package com.by.lizhiyoupin.app.io.entity;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 20:05
 * Summary:
 */
public class CreateShareEntity {

    /**
     * downLoadUrl :  下载地址
     * taoCommand : 淘口令
     * mainImg : 主图
     * imgOne : //gw.alicdn.com/bao/uploaded/i4/3252696262/O1CN01HURIZV1w82eDxlucp_!!3252696262.jpg
     * imgTwo : //gw.alicdn.com/bao/uploaded/i4/3252696262/O1CN01HURIZV1w82eDxlucp_!!3252696262.jpg
     * imgThree : //gw.alicdn.com/bao/uploaded/i4/3252696262/O1CN01HURIZV1w82eDxlucp_!!3252696262.jpg
     * imgFour : //gw.alicdn.com/bao/uploaded/i4/3252696262/O1CN01HURIZV1w82eDxlucp_!!3252696262.jpg
     * images: //主图+分图
     */

    private String downLoadUrl;
    private String taoCommand;
    private String mainImg;
    private String imgOne;
    private String imgTwo;
    private String imgThree;
    private String imgFour;
    private List<String> images;//主图+分图

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getTaoCommand() {
        return taoCommand;
    }

    public void setTaoCommand(String taoCommand) {
        this.taoCommand = taoCommand;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getImgOne() {
        return imgOne;
    }

    public void setImgOne(String imgOne) {
        this.imgOne = imgOne;
    }

    public String getImgTwo() {
        return imgTwo;
    }

    public void setImgTwo(String imgTwo) {
        this.imgTwo = imgTwo;
    }

    public String getImgThree() {
        return imgThree;
    }

    public void setImgThree(String imgThree) {
        this.imgThree = imgThree;
    }

    public String getImgFour() {
        return imgFour;
    }

    public void setImgFour(String imgFour) {
        this.imgFour = imgFour;
    }
}
