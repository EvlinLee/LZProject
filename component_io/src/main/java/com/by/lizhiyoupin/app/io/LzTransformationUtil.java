package com.by.lizhiyoupin.app.io;

import com.by.lizhiyoupin.app.io.bean.CommonCategoryBean;
import com.by.lizhiyoupin.app.io.bean.HandPickDetailBean;
import com.by.lizhiyoupin.app.io.bean.HomeBannerBean;
import com.by.lizhiyoupin.app.io.bean.HomeIconBean;
import com.by.lizhiyoupin.app.io.bean.PreciseBannerIconBean;
import com.by.lizhiyoupin.app.io.entity.RequestButtonRecommend;
import com.by.lizhiyoupin.app.io.entity.RequestShoppingCartEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 15:40
 * Summary:
 */
public class LzTransformationUtil {
    /**
     * Banner转换
     *
     * @param oldList
     * @return
     */
    public static List<HomeBannerBean> transformationBanner(List<PreciseBannerIconBean> oldList) {
        List<HomeBannerBean> list = new ArrayList<>();
        for (PreciseBannerIconBean preciseBannerIconBean : oldList) {
            HomeBannerBean bean = new HomeBannerBean();
            bean.setLink(preciseBannerIconBean.getUrl());
            bean.setImg(preciseBannerIconBean.getImg());
            bean.setColor(preciseBannerIconBean.getBannerColor());
            list.add(bean);
        }
        return list;
    }

    public static List<HomeBannerBean> transformation2Banner(List<CommonCategoryBean> oldList) {
        List<HomeBannerBean> list = new ArrayList<>();
        for (CommonCategoryBean bannerIconBean : oldList) {
            HomeBannerBean bean = new HomeBannerBean();
            bean.setLink(bannerIconBean.getUrl());
            bean.setImg(bannerIconBean.getKindImg());
            bean.setColor(bannerIconBean.getBannerColor());
            list.add(bean);
        }
        return list;
    }

    public static List<HomeIconBean> transformationIcon(List<PreciseBannerIconBean> oldList) {
        List<HomeIconBean> list = new ArrayList<>();
        for (PreciseBannerIconBean preciseBannerIconBean : oldList) {
            HomeIconBean bean = new HomeIconBean();
            bean.setLink(preciseBannerIconBean.getUrl());
            bean.setIcon(preciseBannerIconBean.getImg());
            bean.setTitle(preciseBannerIconBean.getTitle());
            list.add(bean);
        }
        return list;
    }

    public static List<HomeIconBean> transformation2Icon(List<CommonCategoryBean> oldList) {
        List<HomeIconBean> list = new ArrayList<>();
        for (CommonCategoryBean categoryBean : oldList) {
            HomeIconBean bean = new HomeIconBean();
            bean.setLink(categoryBean.getUrl());
            bean.setIcon(categoryBean.getKindImg());
            bean.setTitle(categoryBean.getKindName());
            bean.setKindId(categoryBean.getKindId());
            list.add(bean);
        }
        return list;
    }

    public static RequestShoppingCartEntity transformation2ShoppingCart(HandPickDetailBean detailBean, long userId) {
        RequestShoppingCartEntity cartEntity = new RequestShoppingCartEntity();
        if (detailBean == null) {
            return cartEntity;
        }
        cartEntity.setCommissionAmount(detailBean.getCommissionMoney());
        cartEntity.setCommodityId(detailBean.getItemId());
        cartEntity.setCommodityImg(detailBean.getPictUrl());
        cartEntity.setCommodityName(detailBean.getTitle());
        cartEntity.setCommodityLink(detailBean.getItemLink());

        cartEntity.setCommoditySource(String.valueOf(detailBean.getIcon()));
        cartEntity.setMonthSales(detailBean.getVolume());
        cartEntity.setOriginalPrice(detailBean.getZkFinalPrice());
        cartEntity.setUserId(userId);
        cartEntity.setRealPrice(detailBean.getDiscountsPriceAfter());
        cartEntity.setCouponAmount(detailBean.getCouponAmount());
        cartEntity.setCouponLink(detailBean.getCouponClickUrl());
        cartEntity.setShopName(detailBean.getShopTitle());

        return cartEntity;
    }

    public static RequestButtonRecommend transformation2RequestButtonRecommend(HandPickDetailBean detailBean) {
        RequestButtonRecommend cartEntity = new RequestButtonRecommend();
        if (detailBean == null) {
            return cartEntity;
        }
        cartEntity.setCommissionAmount(detailBean.getBasisCommissionMoney());
        cartEntity.setCommodityId(String.valueOf(detailBean.getItemId()));
        cartEntity.setCommodityImg(detailBean.getPictUrl());
        cartEntity.setCommodityName(detailBean.getTitle());
        cartEntity.setPlatformType(detailBean.getIcon());
        cartEntity.setMonthSales(detailBean.getVolume());
        cartEntity.setRealPrice(detailBean.getDiscountsPriceAfter());
        cartEntity.setCouponAmount(detailBean.getCouponAmount());
        cartEntity.setShopName(detailBean.getShopTitle());
        cartEntity.setCommissionRate(detailBean.getCommissionRate());
        return cartEntity;
    }
}
