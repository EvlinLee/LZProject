package com.by.lizhiyoupin.app.detail.presenter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.detail.activity.PayOrderAddressEditActivity;
import com.by.lizhiyoupin.app.detail.contract.PayOrderContract;
import com.by.lizhiyoupin.app.detail.model.PayOrderModel;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.AddressBean;
import com.by.lizhiyoupin.app.io.bean.CityPickerBean;
import com.by.lizhiyoupin.app.io.bean.WeiXinPayVO;
import com.by.lizhiyoupin.app.io.entity.RequestAddressEntity;
import com.by.lizhiyoupin.app.stack.ActivityStack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/15 16:59
 * Summary:
 */
public class PayOrderAddressPresenter extends PayOrderContract.PayOrderPresenters implements Handler.Callback {
    public static final String TAG = PayOrderAddressEditActivity.class.getSimpleName();

    public static final int LOAD_ADDARESS_SUCCESS_CODE = 1001;
    private List<CityPickerBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private PayOrderContract.PayOrderView mOrderView;
    private PayOrderContract.PayOrderModel mOrderModel;
    private Handler mHandler;
    private int isLoaded = 0;//地址加载 0未加载，1正在加载，2加载完毕

    public PayOrderAddressPresenter(PayOrderContract.PayOrderView view) {
        this.mOrderView = view;
        mOrderModel = new PayOrderModel();
        mHandler = new Handler(Looper.getMainLooper(), this);
    }


    @Override
    public void requestGetUserAddress() {
        mOrderModel.requestGetUserAddress()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<AddressBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<AddressBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            mOrderView.requestGetUserAddressSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mOrderView.requestGetUserAddressError(throwable);
                    }
                });
    }

    @Override
    public void requestAddUserAddress(Long userId, String consignee, String mobile, String provinceName, String cityName, String districtName, String streetName, int isDefault) {
        RequestAddressEntity entity = new RequestAddressEntity();
        entity.setId(null);
        entity.setAddress(provinceName + cityName + districtName + streetName);
        entity.setProvinceName(provinceName);
        entity.setCityName(cityName);
        entity.setDistrictName(districtName);
        entity.setStreetName(streetName);
        entity.setConsignee(consignee);
        entity.setMobile(mobile);
        entity.setUserId(userId);
        entity.setIsDefault(isDefault);
        mOrderModel.requestUpdateUserAddress(entity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<AddressBean>>() {
                    @Override
                    public void onNext(BaseBean<AddressBean> bean) {
                        super.onNext(bean);
                        if (bean.success() && bean.getResult() != null) {
                            mOrderView.requestUpdateUserAddressSuccess(bean.getResult());
                        } else {
                            onError(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mOrderView.requestUpdateUserAddressError(throwable);
                    }
                });
    }

    //支付下单
    @Override
    public void requestPaymentOrder(Long gid, int type,Long userAddressId) {
        mOrderModel.requestPaymentOrder(gid,type,userAddressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<String>>() {
                    @Override
                    public void onNext(BaseBean<String> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            mOrderView.requestPaymentOrderSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mOrderView.requestPaymentOrderError(throwable);
                    }
                });
    }

    @Override
    public void requestWXPaymentOrder(Long gid, int type,Long userAddressId) {
        mOrderModel.requestWXPaymentOrder(gid,type,userAddressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<WeiXinPayVO>>() {
                    @Override
                    public void onNext(BaseBean<WeiXinPayVO> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            mOrderView.requestWXPaymentOrderSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mOrderView.requestWXPaymentOrderError(throwable);
                    }
                });
    }

    @Override
    public void requestUpdateUserAddress(Long addressId, Long userId, String consignee, String mobile, String provinceName,
                                         String cityName, String districtName, String streetName, int isDefault) {
        RequestAddressEntity entity = new RequestAddressEntity();
        entity.setId(addressId);
        entity.setAddress(provinceName + cityName + districtName + streetName);
        entity.setProvinceName(provinceName);
        entity.setCityName(cityName);
        entity.setDistrictName(districtName);
        entity.setStreetName(streetName);
        entity.setConsignee(consignee);
        entity.setMobile(mobile);
        entity.setUserId(userId);
        mOrderModel.requestUpdateUserAddress(entity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<AddressBean>>() {
                    @Override
                    public void onNext(BaseBean<AddressBean> bean) {
                        super.onNext(bean);
                        if (bean.success()) {
                            mOrderView.requestUpdateUserAddressSuccess(bean.getResult());
                        } else {
                            onError(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mOrderView.requestUpdateUserAddressError(throwable);
                    }
                });
    }

    /**
     * 显示地址选择
     *
     * @param context
     */
    public void showPicker(final Context context) {
        if (isLoaded == 0) {
            //准备加载
            isLoaded = 1;
            LZLog.i(TAG, "准备加载");
            loadPicker();
            return;
        } else if (isLoaded == 1) {
            //正在加载
            LZLog.i(TAG, "正在加载");
            return;
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;
                LZLog.i(TAG, "addsss==" + tx);
                mOrderView.getSelectedAddress(opt1tx, opt2tx, opt3tx, tx);
            }
        })
                // .setTitleText("城市选择")
                .setDividerColor(Color.parseColor("#999999"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setSubmitColor(Color.parseColor("#5491FE"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#999999"))//取消按钮文字颜色
                .setTypeface(Typeface.DEFAULT)
                .setContentTextSize(18)
                .build();
         /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();

    }

    private void loadPicker() {
        LiZhiApplication.getApplication().defaultExecutor.execute(new Runnable() {
            @Override
            public void run() {
                initJsonData();
                mHandler.sendEmptyMessage(LOAD_ADDARESS_SUCCESS_CODE);
            }
        });
    }


    private void initJsonData() {//解析数据
        List<CityPickerBean> jsonBean = loadAddress();

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }
    }

    public static List<CityPickerBean> loadAddress() {
        List<CityPickerBean> mAddress = null;
        String address;
        InputStream in = null;
        try {
            in = ContextHolder.getInstance().getContext().getResources().getAssets().open("province.json");
            byte[] arrayOfByte = new byte[in.available()];
            int read = in.read(arrayOfByte);
            address = new String(arrayOfByte, "UTF-8");
            Gson gson = new Gson();
            mAddress = gson.fromJson(address, new TypeToken<List<CityPickerBean>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mAddress;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case LOAD_ADDARESS_SUCCESS_CODE:
                isLoaded = 2;
                showPicker(ActivityStack.currentActivity());
                break;
        }
        return false;
    }
}
