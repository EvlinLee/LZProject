package com.by.lizhiyoupin.app.detail.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.impl.ShareDialogClickCallback;
import com.by.lizhiyoupin.app.component_ui.utils.DowanUtil;
import com.by.lizhiyoupin.app.component_ui.utils.ThreeAppJumpUtil;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.LZShare;
import com.by.lizhiyoupin.app.detail.adapter.CreateShareAdapter;
import com.by.lizhiyoupin.app.detail.contract.CreateShareContract;
import com.by.lizhiyoupin.app.detail.presenter.CreateSharePresenter;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.SelectPicBean;
import com.by.lizhiyoupin.app.io.entity.CreateShareEntity;
import com.by.lizhiyoupin.app.manager.AccountManager;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/11 13:39
 * Summary:商品详情 创建分享 页
 */
@Route(path = "/app/CreateShareActivity")
public class CreateShareActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, CreateShareContract.CreateShareView, Handler.Callback {
    public static final String TAG = CreateShareActivity.class.getSimpleName();
    public static final int DOWN_LOAD_SUCCES = 101;
    private TextView mActionBarBackTv;
    private TextView mActionBarTitleTv;
    private TextView mShareReturnTv;
    private TextView mShareCopyTv;

    private TextView mShareSelectedNumTv;
    private RecyclerView mShareRecyclerView;
    private CreateShareAdapter mCreateShareAdapter;

    private double mCommissionPrice;//返红包，佣金
    private double mOriginalPrice;//原价
    private double mAfterPrice;//券后价
    private String mProductTitle;//商品标题
    private String mInvitationCode;//邀请码
    private String mVolume;//销量
    private String mCouponamount;//优惠卷金额
    private String mMainPicturl;//主图url
    private String mRushAddressUrl;//抢购地址
    private ArrayList<String> mAllPicturls;//商品所有轮播图 包括主图
    private View mLoadingView;
    private ImageView mLoadingiV;

    private CreateSharePresenter mPresenter;
    private long commodityId;//商品id

    private int mPlatformType;//平台类型 0 淘宝 1 京东 2 拼多多
    private CreateShareEntity mShareEntity;
    private List<SelectPicBean> mList;
    private Handler mHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_share_layout);
        initImmersionBar(Color.WHITE,true);
        mHandler = new Handler(Looper.myLooper(), this);
        Intent intent = getIntent();
        mCommissionPrice = intent.getDoubleExtra(CommonConst.KEY_SHARE_COMMISSION_PRICE, 0D);
        mOriginalPrice = intent.getDoubleExtra(CommonConst.KEY_SHARE_ORIGINAL_PRICE, 0D);
        mAfterPrice = intent.getDoubleExtra(CommonConst.KEY_SHARE_AFTER_PRICE, 0D);
        mProductTitle = intent.getStringExtra(CommonConst.KEY_SHARE_PRODUCT_TITLE);
        mVolume = intent.getStringExtra(CommonConst.KEY_SHARE_PRODUCT_VOLUME);
        mCouponamount = intent.getStringExtra(CommonConst.KEY_SHARE_PRODUCT_COUPONAMOUNT);
        mMainPicturl = intent.getStringExtra(CommonConst.KEY_SHARE_PRODUCT_MAIN_PICTURL);
        mAllPicturls = intent.getStringArrayListExtra(CommonConst.KEY_SHARE_PRODUCT_ALL_PICTURL);
        mRushAddressUrl = intent.getStringExtra(CommonConst.KEY_SHARE_PRODUCT_RUSH_ADDRESS_URL);

        commodityId = intent.getLongExtra(CommonConst.KEY_THREE_DETAIL_ID, 0L);
        mPlatformType = intent.getIntExtra(CommonConst.KEY_SHARE_PLATFORM_TYPE, 0);

        AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        mInvitationCode = accountManager.getAccountInfo().getCode();
        mPresenter = new CreateSharePresenter(this);
        initView();

        mPresenter.requestShareDetail(commodityId, 2, mPlatformType,mProductTitle,String.valueOf(mOriginalPrice),
                String.valueOf(mAfterPrice),mVolume,mCouponamount,mMainPicturl);
    }

    private void initView() {
        mList = new ArrayList<>();
        mLoadingView = findViewById(R.id.loading_view);
        mLoadingiV = findViewById(R.id.loading_gif_iv);
        Glide.with(this).load(R.drawable.loading_dialog_g).into(mLoadingiV);
        mActionBarBackTv = findViewById(R.id.actionbar_back_tv);
        mActionBarTitleTv = findViewById(R.id.actionbar_title_tv);
        mShareReturnTv = findViewById(R.id.share_return_tv);
        mShareCopyTv = findViewById(R.id.share_copy_tv);
        mShareSelectedNumTv = findViewById(R.id.share_picture_selected_number_tv);
        mShareRecyclerView = findViewById(R.id.share_recyclerView);


        findViewById(R.id.share_copy_tip_tv).setOnClickListener(this);
        findViewById(R.id.share_img_tv).setOnClickListener(this);
        mShareCopyTv.setOnLongClickListener(this);
        mActionBarBackTv.setOnClickListener(this);
        mActionBarBackTv.setText("");
        mActionBarTitleTv.setText("创建分享");
        ViewUtil.setTextViewFormat(this, mShareSelectedNumTv, R.string.share_picture_selected_number_text, 0);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false);
        mShareRecyclerView.addItemDecoration(new SpaceItemDecoration(DeviceUtil.dip2px(this, 5), 2, RecyclerView.HORIZONTAL));
        mShareRecyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //每行item数据=spanCount/getSpanSize
                return 0 == position ? 2 : 1;
            }
        });
        mCreateShareAdapter = new CreateShareAdapter(this, mShareSelectedNumTv);
        mShareRecyclerView.setAdapter(mCreateShareAdapter);

        String code = LiZhiApplication.getApplication().getAccountManager().getAccountInfo().getCode();
        setData(WebUrlManager.getDownLoadH5(code), "", mRushAddressUrl);
    }

    /**
     * @param loadAddressUrl 下载地址
     * @param taoCommand     淘口令
     * @param bugAddressUrl  抢购地址
     */
    private void setData(String loadAddressUrl, String taoCommand, String bugAddressUrl) {
        ViewUtil.setTextViewFormat(this, mShareReturnTv, R.string.product_share_return_red_packet_text, mCommissionPrice);
        if (mPlatformType == CommonConst.PLATFORM_TAO_BAO || mPlatformType == CommonConst.PLATFORM_TIAN_MAO) {
            ViewUtil.setTextViewFormat(this, mShareCopyTv, R.string.product_share_taobao_copy_all_text,
                    mProductTitle, mOriginalPrice, mAfterPrice,
                    mCommissionPrice, mInvitationCode, loadAddressUrl, taoCommand);
        } else {
            ViewUtil.setTextViewFormat(this, mShareCopyTv, R.string.product_share_jd_pdd_copy_all_text,
                    mProductTitle, mOriginalPrice, mAfterPrice,
                    mCommissionPrice, mInvitationCode, loadAddressUrl, bugAddressUrl);
        }
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
            case R.id.share_copy_tip_tv:
                //复制口令
                copyToClipboard();
                break;
            case R.id.share_img_tv:
                //分享奖励
                shareClick();
                break;
            default:
                break;
        }
    }

    private void shareClick() {
        List<String> urllist = new ArrayList<>();
        for (SelectPicBean picBean : mList) {
            if (picBean.isSelected()) {
                urllist.add(picBean.getUrl());
            }
        }
        if (urllist.size() == 0) {
            MessageToast.showToast(this, "请选择分享图片");
            return;
        }
        //复制文案
        DeviceUtil.putClipboardText(this, mShareCopyTv.getText().toString());
        //下载图片或直接分享一张图
        if (urllist.size() > 1) {
            //多图 下载到本地
            showLoadingDialog();
            LiZhiApplication.getApplication().defaultExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < urllist.size(); i++) {
                        DowanUtil.saveImageToSdCard(CreateShareActivity.this, urllist.get(i));
                    }
                    mHandler.sendEmptyMessage(DOWN_LOAD_SUCCES);
                }
            });
        } else {
            LZShare.share(LZShare.getChannelListPic(urllist.get(0))).show();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return true;
        }
        switch (v.getId()) {
            case R.id.share_copy_tv:
                //复制口令
                copyToClipboard();
                break;
        }
        return true;
    }

    private void copyToClipboard() {

        if (mShareEntity != null) {
            //String clip = mShareEntity.getTaoCommand();
         /*   ClipData myClip = ClipData.newPlainText("text", mShareCopyTv.getText());
            ClipboardManager myClipboard = (ClipboardManager) this.getSystemService(Activity.CLIPBOARD_SERVICE);
            myClipboard.setPrimaryClip(myClip);*/
            DeviceUtil.putClipboardText(this, mShareCopyTv.getText());
            MessageToast.showToastBottom(this, "已复制到剪贴板！", Gravity.CENTER);
        }

    }

    @Override
    public void requestShareDetailSuccess(CreateShareEntity shareEntity) {
        LZLog.i(TAG, "requestShareDetailSuccess");
        mShareEntity = shareEntity;

        String taoCommand = shareEntity.getTaoCommand();
        mList.clear();
        List<String> images = shareEntity.getImages();
        if (images != null&&images.size()>0) {
            SelectPicBean selectPicEntity = new SelectPicBean();
            selectPicEntity.setSelected(true);
            selectPicEntity.setUrl(shareEntity.getImages().get(0));
            mList.add(selectPicEntity);
        }
        if (mAllPicturls!=null&&mAllPicturls.size()>1){
            for (int i = 1; i < mAllPicturls.size(); i++) {
                if (i>4){
                    break;
                }
                SelectPicBean selectPicEntity = new SelectPicBean();
                selectPicEntity.setSelected(false);
                selectPicEntity.setUrl(mAllPicturls.get(i));
                mList.add(selectPicEntity);
            }
        }
        mCreateShareAdapter.setListData(mList);
        mCreateShareAdapter.notifyDataSetChanged();
        String code = LiZhiApplication.getApplication().getAccountManager().getAccountInfo().getCode();
        setData(WebUrlManager.getDownLoadH5(code), taoCommand, mRushAddressUrl);
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void requestShareDetailError(Throwable throwable) {
        LZLog.i(TAG, "requestShareDetailError==" + throwable);
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case DOWN_LOAD_SUCCES:
                dismissLoadingDialog();
                DiaLogManager.showShareWXViewDialog(this, getSupportFragmentManager(), "去朋友圈分享", "打开朋友圈", new ShareDialogClickCallback() {
                    @Override
                    public void clickCallback() {
                        //点击打开微信
                        ThreeAppJumpUtil.jump2Wechat(CreateShareActivity.this);
                    }
                });
                break;
        }
        return true;
    }
}
