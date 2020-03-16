package com.by.lizhiyoupin.app.detail.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.impl.SelectPayTypeCallback;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.detail.contract.PayOrderContract;
import com.by.lizhiyoupin.app.detail.presenter.PayOrderAddressPresenter;
import com.by.lizhiyoupin.app.io.bean.AddressBean;
import com.by.lizhiyoupin.app.io.bean.WeiXinPayVO;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.utils.AliPayUtil;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.by.lizhiyoupin.app.utils.WxPayUtil;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;


/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/11 20:45
 * Summary: 确认订单
 */
@Route(path = "/app/PayOrderActivity")
public class PayOrderActivity extends BaseActivity implements View.OnClickListener,
        PayOrderContract.PayOrderView, SelectPayTypeCallback, Handler.Callback {
    public static final String TAG = PayOrderActivity.class.getSimpleName();
    public static final int PAY_ADDRESS_REQUEST_CODE = 2001;
    public static final int PAY_ADDRESS_RESUlT_CODE = 2001;
    private TextView mActionBarBack;
    private TextView mActionBarTitle;
    private TextView mActionBarRight;
    private TextView mSettingAddressTv;//设置收货地址
    private Group mAddressDetailGroup;//收货人地址等的组合

    private TextView mAddressReceivingNameTv;//收货人
    private TextView mAddressReceivingPhoneTv;//收货人手机号
    private TextView mAddressReceivingAddressTv;//收货地址
    private ImageView mGoodsImgIv;
    private TextView mGoodsTitleTv;//商品标题
    private TextView mGoodsNormsTv;//规格：1件
    private TextView mGoodsNormsNumberTv;//x 1
    private TextView mGoodsTotalPriceTv;//总价
    private TextView mPayTypeSelectTv;//支付方式选择
    private TextView mPayFinalPriceTv;//最终价格
    private TextView mPaySubmitTv;//提交订单

    private AddressBean mAddressBean;

    private long mDetailId;//商品id
    private String mProductTitle;//商品title
    private int mProductNumber;//商品数量
    private double mProductPrice;//商品价格
    private String mProductPicture;//商品图
    private PayOrderAddressPresenter mPresenter;
    private int mPayType = 0;//支付方式 0支付宝，1微信
    private static final int SDK_PAY_FLAG = 0;
    private Handler mHandler;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DeviceUtil.fullScreen(this, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order_layout);
        StatusBarUtils.setColor(this, Color.WHITE, 0);
        Intent intent = getIntent();
        mDetailId = intent.getLongExtra(CommonConst.KEY_NATIVE_DETAIL_ID, 0);
        mProductTitle = intent.getStringExtra(CommonConst.KEY_ORDER_PRODUCT_TITLE);
        mProductNumber = intent.getIntExtra(CommonConst.KEY_ORDER_PRODUCT_NUMBER, 1);
        mProductPrice = intent.getDoubleExtra(CommonConst.KEY_ORDER_PRODUCT_PRICE, 0);
        mProductPicture = intent.getStringExtra(CommonConst.KEY_ORDER_PRODUCT_PICTURE);
        mPresenter = new PayOrderAddressPresenter(this);
        mHandler = new Handler(Looper.getMainLooper(), this);
        initView();
        initData();
    }


    private void initView() {
        View actionbar = findViewById(R.id.actionbar);
        actionbar.setBackgroundColor(getResources().getColor(R.color.white_100));
        mActionBarBack = actionbar.findViewById(R.id.actionbar_back_tv);
        mActionBarTitle = actionbar.findViewById(R.id.actionbar_title_tv);
        mActionBarBack.setText("");
        mSettingAddressTv = findViewById(R.id.pay_order_setting_address_tv);
        mAddressDetailGroup = findViewById(R.id.address_detail_group);
        mAddressReceivingNameTv = findViewById(R.id.address_receiving_name_tv);
        mAddressReceivingPhoneTv = findViewById(R.id.address_receiving_phone_tv);
        mAddressReceivingAddressTv = findViewById(R.id.address_receiving_address_tv);

        mGoodsImgIv = findViewById(R.id.order_pay_goods_img_iv);
        mGoodsTitleTv = findViewById(R.id.order_pay_goods_title_tv);
        mGoodsNormsTv = findViewById(R.id.order_pay_goods_norms_tv);
        mGoodsNormsNumberTv = findViewById(R.id.order_pay_goods_norms_number_tv);
        mGoodsTotalPriceTv = findViewById(R.id.pay_order_goods_total_price_tv);
        mPayTypeSelectTv = findViewById(R.id.pay_order_goods_pay_type_select_tv);
        mPayFinalPriceTv = findViewById(R.id.pay_order_pay_final_price_tv);
        mPaySubmitTv = findViewById(R.id.pay_order_goods_pay_submit_tv);

        findViewById(R.id.pay_order_address_show_tv).setOnClickListener(this);
        mActionBarBack.setOnClickListener(this);


        mPayTypeSelectTv.setOnClickListener(this);
        mPaySubmitTv.setOnClickListener(this);

    }

    private void initData() {
        mPresenter.requestGetUserAddress();
        new GlideImageLoader(this, mProductPicture).error(R.drawable.empty_pic_list).into(mGoodsImgIv);
        mGoodsTitleTv.setText(mProductTitle);

        mActionBarTitle.setText(getResources().getString(R.string.pay_order_pay_confirm_order_text));
        ViewUtil.setTextViewFormat(this, mGoodsNormsTv, R.string.pay_order_goods_norms_text,
                mProductNumber);
        ViewUtil.setTextViewFormat(this, mGoodsNormsNumberTv, R.string.pay_order_goods_norms_text
                , mProductNumber);

        ViewUtil.setTextViewFormat(this, mGoodsTotalPriceTv, R.string.pay_order_price_text,
                mProductPrice);
        ViewUtil.setTextViewFormat(this, mPayFinalPriceTv, R.string.pay_order_price_text,
                mProductPrice);

        mAddressDetailGroup.setVisibility(View.GONE);
        mSettingAddressTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.actionbar_right_tv:
                //刷新
                refreshData();
                break;
            case R.id.pay_order_address_show_tv:
                //跳转到 收货地址信息设置
                Intent intent = new Intent(this, PayOrderAddressEditActivity.class);

                intent.putExtra(CommonConst.KEY_ADDRESS_BEAN, mAddressBean == null ?
                        new AddressBean() : mAddressBean);
                startActivityForResult(intent, PAY_ADDRESS_REQUEST_CODE);
                break;
            case R.id.pay_order_goods_pay_type_select_tv:
                //支付方式选择
                DiaLogManager.showSelectPayTypeDialog(this, getSupportFragmentManager(), this);
                break;
            case R.id.pay_order_goods_pay_submit_tv: //提交订单
                //判断是否填写地址
                if (mAddressBean == null) {
                    CommonToast.showToast("请先填写地址");
                } else {
                    if (mPayType == 0) {
                        mPresenter.requestPaymentOrder(mDetailId, 0,mAddressBean.getId());//支付宝支付
                    } else {
                        mPresenter.requestWXPaymentOrder(mDetailId, 1,mAddressBean.getId());//微信支付
                    }
                }
                break;
        }
    }

    @Override
    public void selectPayType(int payType) {
        //支付方式选择
        mPayType = payType;
        switch (mPayType) {
            case 0:
                mPayTypeSelectTv.setText(R.string.pay_order_pay_alipay_text);
                mPayTypeSelectTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pay_alipay_icon, 0, R.drawable.direction_right_gray, 0);
                break;
            case 1:
                mPayTypeSelectTv.setText(R.string.pay_order_pay_wechat_text);
                mPayTypeSelectTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pay_wechat_icon, 0, R.drawable.direction_right_gray, 0);
                break;
        }

    }


    /**
     * 刷新数据
     */
    private void refreshData() {
        if (mAddressBean != null && mAddressBean.getId() != null) {
            LZLog.i(TAG, "refreshData ");
            mAddressDetailGroup.setVisibility(View.VISIBLE);
            mSettingAddressTv.setVisibility(View.GONE);
            ViewUtil.setTextViewFormat(this, mAddressReceivingNameTv,
                    R.string.pay_order_receiving_name_text, mAddressBean.getConsignee());
            mAddressReceivingPhoneTv.setText(mAddressBean.getMobile());
            mAddressReceivingAddressTv.setText(mAddressBean.getAddress());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAY_ADDRESS_REQUEST_CODE) {
            if (data != null && resultCode == PAY_ADDRESS_RESUlT_CODE) {
                mAddressBean =
                        (AddressBean) data.getSerializableExtra(CommonConst.KEY_ADDRESS_BEAN);
                LZLog.i(TAG, "onActivityResult==" + mAddressBean);
                refreshData();
            }
        }
    }

    @Override
    public void requestGetUserAddressSuccess(List<AddressBean> entity) {
        if (ArraysUtils.isListEmpty(entity)) {
            return;
        }
        LZLog.i(TAG, "requestGetUserAddressSuccess ");
        for (AddressBean addressBean : entity) {
            if (addressBean.getIsDefault() == 1) {
                mAddressBean = addressBean;
                refreshData();
                break;
            }
        }

    }

    @Override
    public void requestGetUserAddressError(Throwable throwable) {
        LZLog.i(TAG, "requestGetUserAddressError==" + throwable);
    }

    @Override
    public void requestUpdateUserAddressSuccess(AddressBean b) {

    }

    @Override
    public void requestUpdateUserAddressError(Throwable throwable) {

    }

    @Override
    public void getSelectedAddress(String province, String city, String county, String three) {

    }

    //支付下单
    @Override
    public void requestPaymentOrderSuccess(String bean) {
        LZLog.i(TAG, "requestPaymentOrderSuccess==" + bean);

        alipay(bean);//支付宝支付
    }

    private void alipay(String infoBean) {
        if (infoBean != null) {
            AliPayUtil.alipay(this, infoBean, mHandler, SDK_PAY_FLAG);
        }

    }

    @Override
    public void requestPaymentOrderError(Throwable throwable) {
        LZLog.i(TAG, "requestPaymentOrderError==" + throwable);
    }

    @Override
    public void requestWXPaymentOrderSuccess(WeiXinPayVO bean) {
        wxPay(bean);
    }

    private void wxPay(WeiXinPayVO bean) {
        if (bean != null) {
            WxPayUtil.getInstance(this).wxPay(bean.getAppId(), bean.getPartnerId(),
                    bean.getPrepayId(), bean.getNonceStr(), bean.getTimeStamp(),
                    bean.getPackageValue(), bean.getSign());

        }
    }

    @Override
    public void requestWXPaymentOrderError(Throwable throwable) {

    }


    @Override
    public boolean handleMessage(Message msg) {
        String result = (String) msg.obj;
        switch (msg.what) {
            case SDK_PAY_FLAG:
                if (result.contains("9000")) {
                    CommonSchemeJump.showActivity(this, "/app/PaySuccessActivity");
                    CommonToast.showToast("支付成功");

                } else {
                    if (result.contains("8000")) {
                        CommonToast.showToast("支付确认中...");

                    } else {
                        CommonToast.showToast("支付失败");

                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(this);
        }
    }
}
