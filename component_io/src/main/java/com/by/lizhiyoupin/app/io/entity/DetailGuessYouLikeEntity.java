package com.by.lizhiyoupin.app.io.entity;

import com.by.lizhiyoupin.app.io.bean.PreciseListBean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/25 16:24
 * Summary:
 */
public class DetailGuessYouLikeEntity {
  private   List<PreciseListBean> guessYouLike;

    public List<PreciseListBean> getGuessYouLike() {
        return guessYouLike;
    }

    public void setGuessYouLike(List<PreciseListBean> guessYouLike) {
        this.guessYouLike = guessYouLike;
    }
}
