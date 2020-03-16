package com.by.lizhiyoupin.app.detail.presenter;

import android.app.Activity;

import com.by.lizhiyoupin.app.component_umeng.share.UmengSocialSDKUtils;
import com.by.lizhiyoupin.app.detail.contract.CreateShareContract;
import com.by.lizhiyoupin.app.detail.model.CreateShareModel;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.entity.CreateShareEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 19:51
 * Summary:
 */
public class CreateSharePresenter extends CreateShareContract.CreateSharePresenters {
    private CreateShareContract.CreateShareModel mShareModel;
    private CreateShareContract.CreateShareView mShareView;

    public CreateSharePresenter(CreateShareContract.CreateShareView shareView) {
        super();
        this.mShareView = shareView;
        mShareModel = new CreateShareModel();
    }

    @Override
    public void requestShareDetail(Long commodityId, int type, int platformType,
                                   String title,
                                   String zkFinalPrice,
                                   String discountsPriceAfter,
                                   String volume,
                                   String couponAmount,
                                   String pictUrl) {
        mShareModel.requestShareDetail(commodityId, type, platformType,
                title, zkFinalPrice, discountsPriceAfter, volume, couponAmount, pictUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<CreateShareEntity>>() {
                    @Override
                    public void onNext(BaseBean<CreateShareEntity> createShareEntityBaseBean) {
                        super.onNext(createShareEntityBaseBean);
                        if (createShareEntityBaseBean.success() && createShareEntityBaseBean.getResult() != null) {
                            mShareView.requestShareDetailSuccess(createShareEntityBaseBean.data);
                        } else {
                            onError(new Throwable(createShareEntityBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mShareView.requestShareDetailError(throwable);
                    }
                });
    }

    public void showShareDialog(Activity activity, int type, Long commodityId) {


        List<ChannelListBean> channelListBeans = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ChannelListBean channelListBean = new ChannelListBean();
            channelListBean.setCode(i);
            channelListBean.setName("微信");
            channelListBean.setShareTitle("分享出去的title");
            channelListBean.setShareType(0);
            channelListBean.setDesc("描述信息");
            channelListBean.setLink("http://www.baidu.com");
            channelListBean.setShareImgUrl("http://shop.lizhiyoupin.com/uploads/image/159/0/2019/08/5a942a162dd58e5650744a8ce298a3dc.jpg");
            channelListBean.setShareImgUrls(new String[]{"http://shop.lizhiyoupin.com/uploads/image/159/0/2019/08/d63efcd00e24b8f539cead4b029a4c6c.jpg"});
            channelListBeans.add(channelListBean);
        }


        UmengSocialSDKUtils.shareShow(activity, channelListBeans);

    }
}
