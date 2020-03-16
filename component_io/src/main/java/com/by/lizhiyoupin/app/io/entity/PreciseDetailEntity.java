package com.by.lizhiyoupin.app.io.entity;

import com.by.lizhiyoupin.app.io.bean.HandPickDetailBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/21 17:26
 * Summary: 三方服务返回商详
 */
public class PreciseDetailEntity {
    private HandPickDetailBean handpickDetail;
    private List<PreciseListBean> guessYouLike;
    private List<PreciseListBean> recommend;
    private List<String> textAndUrl;
    private String jumpUrl;//本地找不到商品，返回302，则使用改地址直接跳转淘宝

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public HandPickDetailBean getHandpickDetail() {
        return handpickDetail;
    }

    public void setHandpickDetail(HandPickDetailBean handpickDetail) {
        this.handpickDetail = handpickDetail;
    }

    public List<PreciseListBean> getGuessYouLike() {
        return guessYouLike;
    }

    public void setGuessYouLike(List<PreciseListBean> guessYouLike) {
        this.guessYouLike = guessYouLike;
    }

    public List<PreciseListBean> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<PreciseListBean> recommend) {
        this.recommend = recommend;
    }

    public List<String> getTextAndUrl() {
        return textAndUrl;
    }

    public void setTextAndUrl(List<String> textAndUrl) {
        this.textAndUrl = textAndUrl;
    }
}
