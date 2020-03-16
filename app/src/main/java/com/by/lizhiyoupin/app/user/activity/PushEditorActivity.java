package com.by.lizhiyoupin.app.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.impl.DialogCallBack;
import com.by.lizhiyoupin.app.component_ui.impl.SelectCallback;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.DividerItemDecoration2;
import com.by.lizhiyoupin.app.io.bean.FansDataBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.bean.PushEditBean;
import com.by.lizhiyoupin.app.io.bean.PushMessageDescBean;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.user.adapter.PushFansListAdapter;
import com.by.lizhiyoupin.app.user.contract.PushEditContract;
import com.by.lizhiyoupin.app.user.presenter.PushEditPresenter;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*
 * 运营商push消息管理
 * jyx
 * */
@Route(path = "/app/PushEditorActivity")
public class PushEditorActivity extends BaseMVPActivity<PushEditContract.IPushEditView, PushEditContract.IPushEditPresenter> implements View.OnClickListener, SelectCallback<String>, OnTimeSelectListener, PushEditContract.IPushEditView {
    public static final String TAG = PushEditorActivity.class.getSimpleName();
    public static final int PUSH_EDIT_GOODS_REQUEST_CODE = 1405;
    public static final int PUSH_EDIT_REQUEST_CODE = 1406;
    private TextView mTopTipsTv;
    private ImageView mSelectGoodsIv;
    private View mSingleCl;
    private ImageView mItemListTopIv;
    private TextView mItemListTitleTv;
    private TextView mItemListPriceTv;
    private TextView mItemListVolumeTv;
    private TextView mItemListBottomLeftTv;
    private TextView mItemListBottomRightTv;
    private ImageView mSelectPeopleIv;
    private RecyclerView mSelectPeopleRecyclerView;
    private TextView mPushContentTagTv;
    private TextView mTimeSelectTv;


    private List<String> pushTagsList;//推送标签数据
    private boolean selectAllFans = false;
    private PushFansListAdapter mPushFansListAdapter;
    private ArrayList<Long> peopleIdsList;//选中推送粉丝id集合
    private ArrayList<FansDataBean.FansListBean> peopleBeanList;//
    private PreciseListBean selectGoods;//推送商品
    private Date sendDate;//推送日期
    private long pushMessageId;//是否重新编辑
    private PushEditBean pushEditBean;//准备的发送消息
    private View mMoreProduct;//更多商品
    private TextView mMorePushPeopleTv;//更多粉丝

    @Override
    public PushEditContract.IPushEditPresenter getBasePresenter() {
        return new PushEditPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_editor);
        initImmersionBar(Color.WHITE, true);
        Intent intent = getIntent();
        pushMessageId = intent.getLongExtra(CommonConst.KEY_PUSH_HAD_EDIT_FROM_LIST, 0);
        initView();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //重置所有初始化数据
        pushTagsList=null;
        selectAllFans=false;
        peopleBeanList=null;
        peopleIdsList=null;
        selectGoods=null;
        sendDate=null;
        pushEditBean=null;
        pushMessageId = intent.getLongExtra(CommonConst.KEY_PUSH_HAD_EDIT_FROM_LIST, 0);
        LZLog.i(TAG,"onNewIntent 重置 pushMessageId=="+pushMessageId);
        //重新刷新数据
        initData();
    }

    private void initData() {
        basePresenter.requestPushEdit("");
        basePresenter.requestPushMessageDescList();
        if (pushMessageId > 0) {
            //需要请求数据对消息重新编辑
            basePresenter.requestGetPushDetailInfo(pushMessageId);
        }
    }

    /**
     * 初始化 已编辑消息
     *
     * @param editBean
     */
    private void initPushMessage(PushEditBean editBean) {
        updateSelectGoods(editBean.getCommondity());
        updateSelectPeople(editBean.getFansList());
        mPushContentTagTv.setText(editBean.getDescribe());
        mTimeSelectTv.setText(TimeUtils.DateToString(sendDate, 7));
    }

    private void initView() {
        View actionBarRl = findViewById(R.id.actionbar_rl);
        TextView actionBarBack = findViewById(R.id.actionbar_back_tv);
        TextView actionBarTitle = findViewById(R.id.actionbar_title_tv);
        TextView actionbar_right_tv = findViewById(R.id.actionbar_right_tv);
        mTopTipsTv = findViewById(R.id.top_tips_tv);
        mSelectGoodsIv = findViewById(R.id.select_goods_iv);

        mSingleCl = findViewById(R.id.single_cl);
        mItemListTopIv = findViewById(R.id.item_list_top_iv);
        mItemListTitleTv = findViewById(R.id.item_list_title_tv);
        mItemListPriceTv = findViewById(R.id.item_list_price_tv);
        mItemListVolumeTv = findViewById(R.id.item_list_volume_tv);
        mItemListBottomLeftTv = findViewById(R.id.item_list_bottom_left_tv);
        mItemListBottomRightTv = findViewById(R.id.item_list_bottom_right_tv);

        mMoreProduct = findViewById(R.id.more_push_product_tv);
        mMorePushPeopleTv = findViewById(R.id.more_push_people_tv);
        mSelectPeopleIv = findViewById(R.id.select_people_iv);
        mSelectPeopleRecyclerView = findViewById(R.id.select_people_Rv);
        View contentSelectRl = findViewById(R.id.content_select_Rl);
        mPushContentTagTv = findViewById(R.id.push_content_tag_tv);
        View timeSelectRl = findViewById(R.id.time_select_Rl);
        mTimeSelectTv = findViewById(R.id.time_select_tv);
        TextView submitTv = findViewById(R.id.submit_tv);

        mMoreProduct.setOnClickListener(this);
        mSelectGoodsIv.setOnClickListener(this);
        mSingleCl.setOnClickListener(this);
        mMorePushPeopleTv.setOnClickListener(this);
        mSelectPeopleIv.setOnClickListener(this);
        contentSelectRl.setOnClickListener(this);
        timeSelectRl.setOnClickListener(this);
        submitTv.setOnClickListener(this);

        actionBarRl.setBackgroundColor(Color.WHITE);
        actionBarTitle.setText("推送管理");
        actionBarBack.setText("");
        actionbar_right_tv.setText("推送列表");
        ViewUtil.setTextViewFormat(this, mTopTipsTv, R.string.push_edit_tips_text, 1);
        actionBarBack.setOnClickListener(this);
        actionbar_right_tv.setOnClickListener(this);
        //选中推送对象
        mSelectPeopleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(this, RecyclerView.VERTICAL);
        dividerItemDecoration2.setDividerColor(Color.TRANSPARENT);
        dividerItemDecoration2.setDividerHeight(DeviceUtil.dip2px(this, 8));
        mSelectPeopleRecyclerView.addItemDecoration(dividerItemDecoration2);
        mPushFansListAdapter = new PushFansListAdapter(this);
        mSelectPeopleRecyclerView.setAdapter(mPushFansListAdapter);

    }


    /**
     * 更新 选中显示的接收者 3个
     *
     * @param peopleSelectList
     */
    private void updateSelectPeople(@Nullable ArrayList<FansDataBean.FansListBean> peopleSelectList) {
        if (!ArraysUtils.isListEmpty(peopleSelectList)) {
            mSelectPeopleRecyclerView.setVisibility(View.VISIBLE);
            mSelectPeopleIv.setVisibility(View.GONE);
            mMorePushPeopleTv.setVisibility(View.VISIBLE);
            mPushFansListAdapter.setListData(peopleSelectList.size() > 3 ? peopleSelectList.subList(0, 3) : peopleSelectList);
            mPushFansListAdapter.notifyDataSetChanged();
        } else {
            mSelectPeopleRecyclerView.setVisibility(View.GONE);
            mSelectPeopleIv.setVisibility(View.VISIBLE);
            mMorePushPeopleTv.setVisibility(View.GONE);
        }
    }

    /**
     * 更新选中商品
     *
     * @param bean
     */
    private void updateSelectGoods(@Nullable PreciseListBean bean) {
        if (bean == null) {
            mSelectGoodsIv.setVisibility(View.VISIBLE);
            mSingleCl.setVisibility(View.GONE);
            mMoreProduct.setVisibility(View.GONE);
            return;
        }
        mSelectGoodsIv.setVisibility(View.GONE);
        mSingleCl.setVisibility(View.VISIBLE);
        mMoreProduct.setVisibility(View.VISIBLE);
        new GlideImageLoader(this, bean.getPictUrl())
                .placeholder(R.drawable.empty_pic_list)
                .error(R.drawable.empty_pic_list)
                .into(mItemListTopIv);
        mItemListTitleTv.setText(bean.getTitle());

        if (bean.getCouponAmount() == 0) {
            mItemListBottomLeftTv.setVisibility(View.GONE);
        } else {
            mItemListBottomLeftTv.setVisibility(View.VISIBLE);
        }
        ViewUtil.setTextViewFormat(this, mItemListBottomLeftTv, R.string.product_coupon_price, StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()));
        ViewUtil.setTextViewFormat(this, mItemListBottomRightTv, R.string.product_commission_rate_price, StringUtils.getFormattedDoubleOrInt(bean.getCommissionRate()), "%");
        mItemListPriceTv.setText(StringUtils.getFormattedDoubleOrInt(bean.getDiscountsPriceAfter()));
        ViewUtil.setTextViewFormat(this, mItemListVolumeTv, R.string.list_volume_text, ViewUtil.getIVolume(bean.getVolume()));
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
                CommonSchemeJump.showActivity(this, "/app/PushHistoryActivity");
                break;
            case R.id.select_goods_iv:
                //添加 推送商品
            case R.id.more_push_product_tv:
                //查看更多 修改推送商品
                Bundle goodBundle = new Bundle();
                goodBundle.putParcelable(CommonConst.KEY_PUSH_GOODS_SELECT, selectGoods);

                CommonSchemeJump.showActivityForResult(this, "/app/PushAddGoodsActivity",
                        goodBundle, PUSH_EDIT_GOODS_REQUEST_CODE);
                break;
            case R.id.single_cl:
                //  推送商品item点击

                break;
            case R.id.more_push_people_tv: //查看更多已选推送人
            case R.id.select_people_iv:
                //推送人添加
                Bundle bundle = new Bundle();
                bundle.putSerializable(CommonConst.KEY_FANS_SELECT_IDS, peopleIdsList);
                bundle.putParcelableArrayList(CommonConst.KEY_FANS_SELECT, peopleBeanList);

                bundle.putSerializable(CommonConst.KEY_FANS_SELECT_All, selectAllFans);
                CommonSchemeJump.showActivityForResult(this, "/app/PushAddFansActivity", bundle, PUSH_EDIT_REQUEST_CODE);
                break;
            case R.id.content_select_Rl:
                //推送内容标签 选择
                if (!ArraysUtils.isListEmpty(pushTagsList)) {
                    DiaLogManager.showPushTagSelectDialog(this, getSupportFragmentManager(), pushTagsList, this);
                }
                break;
            case R.id.time_select_Rl:
                //推送时间选择
                DiaLogManager.showYearMonthDayHourPicker(this, this);
                break;

            case R.id.submit_tv:
                //提交
                submitPushMessage();

                break;
            default:
                break;
        }
    }

    /**
     * 提交编辑消息
     */
    private void submitPushMessage() {
        if (selectGoods == null) {
            ToastUtils.show("请添加推送商品");
            return;
        }
        if (!selectAllFans && ArraysUtils.isListEmpty(peopleIdsList)) {
            //没有选择全部，且也没有单选
            ToastUtils.show("请选择推送对象");
            return;
        }
        String trim = mPushContentTagTv.getText().toString().trim();
        if ("请选择".equals(trim) || TextUtils.isEmpty(trim)) {
            ToastUtils.show("请选择推送内容");
            return;
        }
        String trim1 = mTimeSelectTv.getText().toString().trim();
        if ("请选择".equals(trim1) || TextUtils.isEmpty(trim1)) {
            ToastUtils.show("请选择推送时间");
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (pushEditBean == null) {
            pushEditBean = new PushEditBean();
        }
        pushEditBean.setUpdateTime(currentTimeMillis);
        pushEditBean.setDescribe(mPushContentTagTv.getText().toString());
        pushEditBean.setReceive(selectAllFans ? "0" : "");
        pushEditBean.setReceiveGroup(selectAllFans ? null : peopleIdsList);
        pushEditBean.setUserId(LiZhiApplication.getApplication().getAccountManager().getAccountId());
        pushEditBean.setSendTime(sendDate.getTime());
        pushEditBean.setCommondity(selectGoods);
        if (pushMessageId != 0) {
            //重新编辑
            if (pushEditBean.getSendStatus() == 1) {
                ToastUtils.show("该消息已经发送，不可再次编辑");
                pushEditBean.setId(-1);
                return;
            }
            LZLog.i(TAG, "更新消息");
            //更新
            basePresenter.requestUpdatePushDetailInfo(pushEditBean);
        } else {
            pushEditBean.setCreateTime(currentTimeMillis);
            LZLog.i(TAG, "添加消息");
            //添加
            basePresenter.requestAddPushDetailInfo(pushEditBean);
        }
        showLoadingDialog();
    }

    @Override
    public void selectBack(String s) {
        //推送内容 标签选中
        mPushContentTagTv.setText(s);
    }


    @Override
    public void onTimeSelect(Date date, View v) {
        //取整时
        long time = TimeUtils.transForDateToTime(date);
        date.setTime(time);
        sendDate = date;
        //推送时间选择
        mTimeSelectTv.setText(TimeUtils.DateToString(date, 7));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PUSH_EDIT_REQUEST_CODE) {
            //接收人列表
            if (data != null) {
                peopleBeanList = data.getParcelableArrayListExtra(CommonConst.KEY_FANS_SELECT);
                selectAllFans = data.getBooleanExtra(CommonConst.KEY_FANS_SELECT_All, false);
                peopleIdsList = (ArrayList<Long>) data.getSerializableExtra(CommonConst.KEY_FANS_SELECT_IDS);
                updateSelectPeople(peopleBeanList);

            }
        } else if (requestCode == PUSH_EDIT_GOODS_REQUEST_CODE) {
            //推荐商品
            if (data != null) {
                PreciseListBean parcelableExtra = data.getParcelableExtra(CommonConst.KEY_PUSH_GOODS_SELECT);
                selectGoods = parcelableExtra;
                updateSelectGoods(parcelableExtra);
            }
        }
    }

    @Override
    public void requestPushEditTipsSuccess(int integer) {
        LZLog.i(TAG, "requestPushEditTipsSuccess==" + integer);
        ViewUtil.setTextViewFormat(this, mTopTipsTv, R.string.push_edit_tips_text, integer);
    }

    @Override
    public void requestPushEditTipsError(Throwable throwable) {
        LZLog.i(TAG, "requestPushEditTipsError==" + throwable);
    }

    @Override
    public void requestGetPushDetailInfoSuccess(PushEditBean editBean) {
        LZLog.i(TAG, "requestGetPushDetailInfoSuccess==");
        pushEditBean = editBean;
        selectGoods = editBean.getCommondity();
        sendDate = new Date(editBean.getSendTime());
        peopleIdsList = editBean.getReceiveGroup();
        selectAllFans = "0".equals(editBean.getReceive());
        LZLog.i(TAG, "sendDate==" + sendDate.getTime());
        LZLog.i(TAG, "sendDate==" + TimeUtils.DateToString(sendDate, 7));
        //得到 已经编辑的消息
        initPushMessage(editBean);
    }

    @Override
    public void requestGetPushDetailInfoError(Throwable throwable) {
        LZLog.i(TAG, "requestGetPushDetailInfoError==" + throwable);
    }

    @Override
    public void requestAddPushDetailInfoSuccess() {
        LZLog.i(TAG, "requestAddPushDetailInfoSuccess==");
        dismissLoadingDialog();
        DiaLogManager.showToastDialog(this, getSupportFragmentManager(), "推送提交成功",
                R.drawable.toast_detail_success2, Gravity.TOP, true, new DialogCallBack<Object>() {
                    @Override
                    public void clickCallback(MessageBox messageBox, Object o) {
                        if (messageBox!=null){
                            messageBox.dismiss();
                        }
                        finish();
                    }
                });
    }

    @Override
    public void requestAddPushDetailInfoError(Throwable throwable) {
        LZLog.i(TAG, "requestGetPushDetailInfoError==" + throwable);
        dismissLoadingDialog();
        DiaLogManager.showToastDialog(this, getSupportFragmentManager(), throwable.getMessage(),
                R.drawable.toast_close, Gravity.TOP, true);
    }

    @Override
    public void requestUpdatePushDetailInfoSuccess() {
        LZLog.i(TAG, "requestUpdatePushDetailInfoSuccess==");
        dismissLoadingDialog();
        DiaLogManager.showToastDialog(this, getSupportFragmentManager(), "推送提交成功",
                R.drawable.toast_detail_success2, Gravity.TOP, true, new DialogCallBack<Object>() {
                    @Override
                    public void clickCallback(MessageBox messageBox, Object o) {
                        if (messageBox!=null){
                            messageBox.dismiss();
                        }
                        finish();
                    }
                });
    }

    @Override
    public void requestUpdatePushDetailInfoError(Throwable throwable) {
        LZLog.i(TAG, "requestUpdatePushDetailInfoError==" + throwable);
        dismissLoadingDialog();
        DiaLogManager.showToastDialog(this, getSupportFragmentManager(), throwable.getMessage(),
                R.drawable.toast_close, Gravity.TOP, true);
    }


    @Override
    public void requestPushMessageDescListSuccess(List<PushMessageDescBean> descBeanList) {
        LZLog.i(TAG, "requestPushMessageDescListSuccess==");
        pushTagsList = new ArrayList<>();
        for (PushMessageDescBean pushMessageDescBean : descBeanList) {
            pushTagsList.add(pushMessageDescBean.getDescribe());
        }

    }

    @Override
    public void requestPushMessageDescListError(Throwable throwable) {
        LZLog.i(TAG, "requestPushMessageDescListError==" + throwable);
    }


}
