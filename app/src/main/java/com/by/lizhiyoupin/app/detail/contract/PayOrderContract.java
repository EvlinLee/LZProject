package com.by.lizhiyoupin.app.detail.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.AddressBean;
import com.by.lizhiyoupin.app.io.bean.WeiXinPayVO;
import com.by.lizhiyoupin.app.io.entity.RequestAddressEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/25 09:54
 * Summary:
 */
public interface PayOrderContract {
    interface PayOrderView extends BaseView {
        void requestGetUserAddressSuccess(List<AddressBean> entity);
        void requestGetUserAddressError(Throwable throwable);

        void requestUpdateUserAddressSuccess(AddressBean bean);
        void requestUpdateUserAddressError(Throwable throwable);
        void requestPaymentOrderSuccess(String bean);
        void requestPaymentOrderError(Throwable throwable);

        void requestWXPaymentOrderSuccess(WeiXinPayVO bean);
        void requestWXPaymentOrderError(Throwable throwable);


        /**
         *
         * @param province
         * @param city
         * @param county
         * @param three 省+市+区
         */
        void getSelectedAddress(String province,String city,String county,String three);
    }

    interface PayOrderModel extends BaseModel {
        Observable<BaseBean<List<AddressBean>>> requestGetUserAddress();
        Observable<BaseBean<AddressBean>> requestUpdateUserAddress(RequestAddressEntity entity);

        Observable<BaseBean<String>> requestPaymentOrder(Long gid,int type,Long userAddressId);
        Observable<BaseBean<WeiXinPayVO>> requestWXPaymentOrder(Long gid,int type,Long userAddressId);
    }

    abstract class PayOrderPresenters extends BasePresenter<PayOrderView> {

        public abstract void requestGetUserAddress( );
        public abstract void requestUpdateUserAddress(Long addressId, Long userId,String consignee,String mobile,String provinceName,
                                                      String cityName,String districtName,String streetName,int isDefault);

        public abstract void requestAddUserAddress(Long userId,String consignee,String mobile,String provinceName,
                                                      String cityName,String districtName,String streetName,int isDefault);

        public abstract void  requestPaymentOrder(Long gid,int type,Long userAddressId);
        public abstract void  requestWXPaymentOrder(Long gid,int type,Long userAddressId);

    }
}
