package com.by.lizhiyoupin.app.io.entity;

import com.by.lizhiyoupin.app.io.bean.HandPickDetailBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/24 11:31
 * Summary: 本地服务器 返回的商详(不包括 猜你喜欢和荔枝推荐)
 */
public class ProductDetailEntity {

    private HandPickDetailBean handpickDetail;
    private List<String> textAndUrl;
    private List<PreciseListBean> guessYouLike;
    private List<PreciseListBean> recommend;

    public HandPickDetailBean getHandpickDetail() {
        return handpickDetail;
    }

    public void setHandpickDetail(HandPickDetailBean handpickDetail) {
        this.handpickDetail = handpickDetail;
    }

    public List<String> getTextAndUrl() {
        return textAndUrl;
    }

    public void setTextAndUrl(List<String> textAndUrl) {
        this.textAndUrl = textAndUrl;
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
}
