package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.user.contract.OrderRetrieveContract;
import com.by.lizhiyoupin.app.user.presenter.OrderRetrievePresenter;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/12 20:45
 * Summary: 找回订单
 */
@Route(path = "/app/OrderRetrieveActivity")
public class OrderRetrieveActivity extends BaseMVPActivity<OrderRetrieveContract.OrderRetrieveView,
        OrderRetrieveContract.OrderRetrievePresenters> implements View.OnClickListener, OrderRetrieveContract.OrderRetrieveView {
    public static final String TAG = OrderRetrieveActivity.class.getSimpleName();

    private EditText mSearchEt;
    private TextView mSearchTv;
    private TextView mTitleTt;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_retrieve_layout);
        initImmersionBar(Color.WHITE,true);
        initView();
    }

    @Override
    public OrderRetrieveContract.OrderRetrievePresenters getBasePresenter() {
        return new OrderRetrievePresenter(this);
    }

    private void initView() {
        TextView backTv = findViewById(R.id.actionbar_back_tv);
        backTv.setOnClickListener(this);
        backTv.setText("");
        mTitleTt = findViewById(R.id.actionbar_title_tv);
        mSearchEt = findViewById(R.id.order_search_et);
        mSearchTv = findViewById(R.id.order_search_tv);
        mTitleTt.setText(R.string.order_search_back_title);
        mSearchTv.setOnClickListener(this);
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    requestData();
                    //点击搜索的时候隐藏软键盘
                    DeviceUtil.hideInputMethod(v);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.order_search_tv:
                requestData();
                break;
        }
    }

    private void requestData() {
        String edit = mSearchEt.getText().toString();
        if (TextUtils.isEmpty(edit)) {
            return;
        }
        basePresenter.requestIncomeRecord(edit);
    }

    @Override
    public void requestOrderRetrieveSuccess(boolean b) {
        LZLog.i(TAG, "requestOrderRetrieveSuccess==" + b);
        if (b) {
            //订单找回成功
            DiaLogManager.showOrderFindSuccessDialog(this, getSupportFragmentManager());
        } else {
            //订单找回失败
            DiaLogManager.showOrderFindErrorDialog(this, getSupportFragmentManager());
        }
    }

    @Override
    public void requestOrderRetrieveError(Throwable throwable) {
        //接口请求错误
        LZLog.i(TAG, "requestOrderRetrieveError==" + throwable);
    }
}
