package com.by.lizhiyoupin.app.detail.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.SplitUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.detail.contract.PayOrderContract;
import com.by.lizhiyoupin.app.detail.presenter.PayOrderAddressPresenter;
import com.by.lizhiyoupin.app.io.bean.AddressBean;
import com.by.lizhiyoupin.app.io.bean.WeiXinPayVO;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/12 11:30
 * Summary: 编辑收货地址等信息
 */
@Route(path = "/app/PayOrderAddressEditActivity")
public class PayOrderAddressEditActivity extends BaseActivity implements View.OnClickListener, PayOrderContract.PayOrderView {

    private TextView mActionBarBack;
    private TextView mActionBarTitle;
    private TextView mActionBarRight;

    private EditText nameEt;
    private EditText phoneEt;
    private TextView selectedAddress;
    private EditText detailEtTv;
    private TextView saveTv;
    private PayOrderAddressPresenter mPresenters;

    private AddressBean mAddressBean;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DeviceUtil.fullScreen(this, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order_address_layout);
        StatusBarUtils.setColor(this, Color.WHITE,0);
        Intent intent = getIntent();
        mAddressBean = (AddressBean) intent.getSerializableExtra(CommonConst.KEY_ADDRESS_BEAN);
        if (mAddressBean == null) {
            mAddressBean = new AddressBean();
        }
        mPresenters = new PayOrderAddressPresenter(this);
        initView();

        refreshData();
    }


    private void initView() {
        View actionbar = findViewById(R.id.actionbar_rl);
        actionbar.setBackgroundColor(getResources().getColor(R.color.white_100));
        mActionBarBack = actionbar.findViewById(R.id.actionbar_back_tv);
        mActionBarTitle = actionbar.findViewById(R.id.actionbar_title_tv);
        mActionBarRight = actionbar.findViewById(R.id.actionbar_right_tv);

        nameEt = findViewById(R.id.order_address_receiving_name_et);
        phoneEt = findViewById(R.id.order_address_receiving_phone_et);
        selectedAddress = findViewById(R.id.order_address_receiving_selected_address_et);
        detailEtTv = findViewById(R.id.order_address_receiving_detail_et);
        saveTv = findViewById(R.id.order_address_save_tv);
        mActionBarBack.setOnClickListener(this);
        mActionBarRight.setOnClickListener(this);
        selectedAddress.setOnClickListener(this);
        saveTv.setOnClickListener(this);
        ViewUtil.setDrawableOfTextView(mActionBarRight, R.drawable.actionbar_refresh, ViewUtil.DrawableDirection.RIGHT);
        mActionBarTitle.setText(getResources().getString(R.string.order_address_receiving_edit_text));
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                //关闭输入法
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.actionbar_right_tv:
                //刷新
                nameEt.setText("");
                phoneEt.setText("");
                detailEtTv.setText("");
                selectedAddress.setText("");
                break;
            case R.id.order_address_receiving_selected_address_et:
                //省市区
                mPresenters.showPicker(this);
                //关闭输入法
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.order_address_save_tv:
                //保存
                if (TextUtils.isEmpty(nameEt.getText())) {
                    CommonToast.showToast("请填写收货人");
                    return;
                }
                if (TextUtils.isEmpty(phoneEt.getText())) {
                    CommonToast.showToast("请填写手机号");
                    return;
                }
                if (!SplitUtils.isMobile(phoneEt.getText().toString().trim())){
                    CommonToast.showToast("请填写正确手机号");
                    return;
                }
                if (TextUtils.isEmpty(detailEtTv.getText())) {
                    CommonToast.showToast("请填写详情地址");
                    return;
                }
                saveAddress();
                break;

        }
    }

    private void refreshData() {
        nameEt.setText(mAddressBean.getConsignee());
        phoneEt.setText(mAddressBean.getMobile());
        detailEtTv.setText(mAddressBean.getStreetName());
        String three = mAddressBean.getProvinceName() + mAddressBean.getCityName() + mAddressBean.getDistrictName();
        if (!TextUtils.isEmpty(three)) {
            selectedAddress.setText(three);
        }
    }

    private void saveAddress() {
        showLoadingDialog();
        long accountId = LiZhiApplication.getApplication().getAccountManager().getAccountId();

        if (mAddressBean.getId() != null) {
            //有地址id说明是修改
            mPresenters.requestUpdateUserAddress(mAddressBean.getId(), accountId, nameEt.getText().toString(), phoneEt.getText().toString(),
                    mAddressBean.getProvinceName(), mAddressBean.getCityName(), mAddressBean.getDistrictName(), detailEtTv.getText().toString(), 1);
        } else {
            //新增地址
            mPresenters.requestAddUserAddress(accountId, nameEt.getText().toString(), phoneEt.getText().toString(),
                    mAddressBean.getProvinceName(), mAddressBean.getCityName(), mAddressBean.getDistrictName(), detailEtTv.getText().toString(), 1);
        }
    }

    @Override
    public void getSelectedAddress(String province, String city, String county, String three) {
        //地址选择
        mAddressBean.setProvinceName(province);
        mAddressBean.setCityName(city);
        mAddressBean.setDistrictName(county);
        selectedAddress.setText(three);
    }

    @Override
    public void requestPaymentOrderSuccess(String bean) {

    }


    @Override
    public void requestPaymentOrderError(Throwable throwable) {

    }

    @Override
    public void requestWXPaymentOrderSuccess(WeiXinPayVO bean) {

    }

    @Override
    public void requestWXPaymentOrderError(Throwable throwable) {

    }

    @Override
    public void requestUpdateUserAddressSuccess(AddressBean addressBean) {
        dismissLoadingDialog();
        LZLog.i(TAG, "修改或新装地址 success "+addressBean);
        Intent intent = new Intent();
        intent.putExtra(CommonConst.KEY_ADDRESS_BEAN, addressBean);
        setResult(PayOrderActivity.PAY_ADDRESS_RESUlT_CODE, intent);
        finish();
    }

    @Override
    public void requestUpdateUserAddressError(Throwable throwable) {
        dismissLoadingDialog();
        LZLog.w(TAG, "修改或新装地址 error==" + throwable);
        CommonToast.showToast("保存失败");
    }


    @Override
    public void requestGetUserAddressSuccess(List<AddressBean> entity) {

    }

    @Override
    public void requestGetUserAddressError(Throwable throwable) {

    }


}
