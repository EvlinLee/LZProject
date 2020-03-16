package com.by.lizhiyoupin.app.user.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.UserHomeBean;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * data:2019/11/9
 * author:jyx
 * function:
 */
public interface AvatarContract {

    interface  AvatarView extends BaseView {
        void requestAvatorSuccess(String bean);
        void requestAvatorError(Throwable throwable);

        void requestUserHomeSuccess(UserHomeBean bean);
        void requestUserHomeError(Throwable throwable);

    }

    interface AvatarModel extends BaseModel {
        Observable<BaseBean<String>> requestAvatar(MultipartBody.Part file);
        Observable<BaseBean<UserHomeBean>> requestUserHome();


    }
    abstract class AvatarPresenters extends BasePresenter<AvatarView> {

        public abstract void requestAvatarupdate(File file);
        public abstract void requestHomeupdate();

    }



}
