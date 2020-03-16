package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 15:25
 * Summary: 单个banner
 */
public class HomeBannerBean {
    private String img;
    private String link;
    private String color;//#FF1211

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HomeBannerBean)) return false;

        HomeBannerBean bean = (HomeBannerBean) o;

        if (getImg() != null ? !getImg().equals(bean.getImg()) : bean.getImg() != null)
            return false;
        if (getLink() != null ? !getLink().equals(bean.getLink()) : bean.getLink() != null)
            return false;
        return getColor() != null ? getColor().equals(bean.getColor()) : bean.getColor() == null;
    }

    @Override
    public int hashCode() {
        int result = getImg() != null ? getImg().hashCode() : 0;
        result = 31 * result + (getLink() != null ? getLink().hashCode() : 0);
        result = 31 * result + (getColor() != null ? getColor().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HomeBannerBean{" +
                "img='" + img + '\'' +
                ", link='" + link + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
