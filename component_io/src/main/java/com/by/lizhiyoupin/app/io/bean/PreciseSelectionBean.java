package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 15:04
 * Summary: 精选
 */
public class PreciseSelectionBean {
    private List<PreciseBannerIconBean> banner;
    private List<PreciseBannerIconBean> inner;//icon
    private List<PreciseBannerIconBean> recommendActivityBanner;//推荐活动位顶部图集合信息（只取第一个）
    private List<PreciseBannerIconBean> recommendActivity;//推荐活动位信息
    private  List<PreciseBannerIconBean> buyActivity;//抢购活动位信息
    private  List<PreciseBannerIconBean> couponActivity;//领券活动位信息

    public List<PreciseBannerIconBean> getBanner() {
        return banner;
    }

    public void setBanner(List<PreciseBannerIconBean> banner) {
        this.banner = banner;
    }

    /**
     * 入口的icon
     * @return
     */
    public List<PreciseBannerIconBean> getInner() {
        return inner;
    }

    public void setInner(List<PreciseBannerIconBean> inner) {
        this.inner = inner;
    }

    public List<PreciseBannerIconBean> getRecommendActivity() {
        return recommendActivity;
    }

    public void setRecommendActivity(List<PreciseBannerIconBean> recommendActivity) {
        this.recommendActivity = recommendActivity;
    }

    public List<PreciseBannerIconBean> getBuyActivity() {
        return buyActivity;
    }

    public void setBuyActivity(List<PreciseBannerIconBean> buyActivity) {
        this.buyActivity = buyActivity;
    }

    public List<PreciseBannerIconBean> getCouponActivity() {
        return couponActivity;
    }

    public void setCouponActivity(List<PreciseBannerIconBean> couponActivity) {
        this.couponActivity = couponActivity;
    }

    public List<PreciseBannerIconBean> getRecommendActivityBanner() {
        return recommendActivityBanner;
    }

    public void setRecommendActivityBanner(List<PreciseBannerIconBean> recommendActivityBanner) {
        this.recommendActivityBanner = recommendActivityBanner;
    }

    @Override
    public String toString() {
        return "PreciseSelectionBean{" +
                "banner=" + banner +
                ", inner=" + inner +
                ", recommendActivity=" + recommendActivity +
                ", buyActivity=" + buyActivity +
                ", couponActivity=" + couponActivity +
                '}';
    }
}
