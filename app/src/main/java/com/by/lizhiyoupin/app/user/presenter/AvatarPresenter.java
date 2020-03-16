package com.by.lizhiyoupin.app.user.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserHomeBean;
import com.by.lizhiyoupin.app.user.contract.AvatarContract;
import com.by.lizhiyoupin.app.user.model.AvatarModel;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * data:2019/11/9
 * author:jyx
 * function:
 */
public class AvatarPresenter extends AvatarContract.AvatarPresenters {

    private AvatarContract.AvatarModel mAvatarModel;
    private AvatarContract.AvatarView avatarView;
    public AvatarPresenter(AvatarContract.AvatarView view) {
        this.avatarView = view;
        mAvatarModel = new AvatarModel();
    }

    @Override
    public void requestAvatarupdate(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        mAvatarModel.requestAvatar(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<String>>() {
                    @Override
                    public void onNext(BaseBean<String> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            avatarView.requestAvatorSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        avatarView.requestAvatorError(throwable);
                    }
                });
    }

    @Override
    public void requestHomeupdate() {
        mAvatarModel.requestUserHome()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserHomeBean>>() {
                    @Override
                    public void onNext(BaseBean<UserHomeBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            avatarView.requestUserHomeSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        avatarView.requestUserHomeError(throwable);
                    }
                });
    }
}
