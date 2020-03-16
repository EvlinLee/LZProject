package com.by.lizhiyoupin.app.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.utils.DowanUtil;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CannotScrollRecyclerView;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpeedLinearLayoutManager;
import com.by.lizhiyoupin.app.component_umeng.share.manager.IShareManager;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.LZShare;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBuyBean;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.stack.ActivityStack;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.dueeeke.videoplayer.controller.BaseVideoController;
import com.dueeeke.videoplayer.controller.MediaPlayerControl;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.L;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 抖券动态视频 配合 ShakeCouponVideoAdapter
 * 抖音
 * Created by xinyu on 2018/1/6.
 */

public class TikTokController2 extends BaseVideoController<MediaPlayerControl> implements View.OnClickListener, Handler.Callback {
   public static final String TAG=TikTokController2.class.getSimpleName();
    public static final int NEWS_FLASH_FLAG = 230;
    public static final int DOWNLOAD_SUCCESS = 231;
    public static final int NEWS_FLASH_FLAG_DELAY_TIME = 1500;
    private ImageView thumb;
    private ImageView mPlayBtn;

    TextView mShakeCouponLoveTv;
    TextView mShakeCouponTextTv;
    TextView mShakeCouponShareTv;
    ImageView mItemListTopIv;
    TextView mItemListTitleTv;
    TextView mItemListYTv;
    TextView mItemListPriceTv;
    TextView mItemListVolumeTv;
    TextView mItemListBottomLeftTv;
    TextView mItemListBottomRightTv;
    TextView mShakeCouponBuyTv;

    private ShakeCouponBean mShakeCouponBean;

    private CannotScrollRecyclerView mBarrageRecyclerView;
    private ShakeBarrageAdapter mShakeBarrageAdapter;
    private List<ShakeCouponBuyBean> mBarrageList;
    public Handler mHandler;
    private int newsFlashCurrentIndex=1;
    private boolean isThumb;

    public TikTokController2(@NonNull Context context) {
        this(context, null);
    }

    public TikTokController2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TikTokController2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tiktok_s_controller;
    }

    @Override
    protected void initView() {
        super.initView();
        mBarrageList = new ArrayList<>();
        thumb = mControllerView.findViewById(R.id.iv_thumb);
        mPlayBtn = mControllerView.findViewById(R.id.play_btn);

        mShakeCouponLoveTv = mControllerView.findViewById(R.id.shake_coupon_love_tv);
        mShakeCouponTextTv = mControllerView.findViewById(R.id.shake_coupon_text_tv);
        mShakeCouponShareTv = mControllerView.findViewById(R.id.shake_coupon_share_tv);
        mItemListTopIv = mControllerView.findViewById(R.id.item_list_top_iv);
        mItemListTitleTv = mControllerView.findViewById(R.id.item_list_title_tv);
        mItemListYTv = mControllerView.findViewById(R.id.item_list_y_tv);
        mItemListPriceTv = mControllerView.findViewById(R.id.item_list_price_tv);
        mItemListVolumeTv = mControllerView.findViewById(R.id.item_list_volume_tv);
        mItemListBottomLeftTv = mControllerView.findViewById(R.id.item_list_bottom_left_tv);
        mItemListBottomRightTv = mControllerView.findViewById(R.id.item_list_bottom_right_tv);
        mShakeCouponBuyTv = mControllerView.findViewById(R.id.shake_coupon_buy_tv);
        //弹幕
        newsFlashCurrentIndex = 0;
        mHandler = new Handler(Looper.getMainLooper(), this);
        mBarrageRecyclerView = mControllerView.findViewById(R.id.barrage_recyclerView);
        SpeedLinearLayoutManager speedLinearLayoutManager = new SpeedLinearLayoutManager(getContext());
        mBarrageRecyclerView.setLayoutManager(speedLinearLayoutManager);
        mShakeBarrageAdapter = new ShakeBarrageAdapter(getContext(),speedLinearLayoutManager);
        mBarrageRecyclerView.setNestedScrollingEnabled(false);
        mBarrageRecyclerView.setClickable(false);
        mBarrageRecyclerView.setAdapter(mShakeBarrageAdapter);

        mShakeCouponTextTv.setOnClickListener(this);
        mShakeCouponLoveTv.setOnClickListener(this);
        mShakeCouponShareTv.setOnClickListener(this);
        mShakeCouponBuyTv.setOnClickListener(this);
        setOnClickListener(this);
    }

    @Override
    public void setPlayState(int playState) {
        super.setPlayState(playState);

        switch (playState) {
            case VideoView.STATE_IDLE:
                L.e("STATE_IDLE " + hashCode());
                thumb.setVisibility(VISIBLE);
                break;
            case VideoView.STATE_PLAYING:
                L.e("STATE_PLAYING " + hashCode());
                thumb.setVisibility(GONE);
                mPlayBtn.setVisibility(GONE);
                break;
            case VideoView.STATE_PAUSED:
                L.e("STATE_PAUSED " + hashCode());
                thumb.setVisibility(GONE);
                mPlayBtn.setVisibility(VISIBLE);
                break;
            case VideoView.STATE_PREPARED:
                L.e("STATE_PREPARED " + hashCode());
                break;
            case VideoView.STATE_ERROR:
                L.e("STATE_ERROR " + hashCode());
                Toast.makeText(getContext(), R.string.dkplayer_error_message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean showNetWarning() {
        //不显示移动网络播放警告
        return false;
    }

    public void stopBarrageScroll() {
        mHandler.removeMessages(NEWS_FLASH_FLAG);
    }

    /**
     * 设置弹幕数据
     *
     * @param list
     */
    public void setBarrageList(List<ShakeCouponBuyBean> list) {
        mBarrageList.clear();
        if (list != null) {
            mBarrageList.addAll(list);
        }
        mShakeBarrageAdapter.setListData(mBarrageList);
        mShakeBarrageAdapter.notifyDataSetChanged();
    }

    /**
     * 设置视频外其他数据
     *
     * @param shakeCouponBean
     */
    public void refreshProductView(ShakeCouponBean shakeCouponBean) {
        if (shakeCouponBean == null) {
            return;
        }
        mShakeCouponBean = shakeCouponBean;
        mShakeCouponLoveTv.setText(StringUtils.getThumbsCount(mShakeCouponBean.getDyThumbsCount()));
        new GlideImageLoader(this, mShakeCouponBean.getPictUrl())
                .error(R.drawable.empty_pic_list)
                .placeholder(R.drawable.empty_pic_list)
                .into(mItemListTopIv);
        mItemListTitleTv.setText(mShakeCouponBean.getTitle());
        if (mShakeCouponBean.getCouponAmount() == 0) {
            mItemListBottomLeftTv.setVisibility(View.GONE);
        } else {
            mItemListBottomLeftTv.setVisibility(View.VISIBLE);
            ViewUtil.setTextViewFormat(getContext(), mItemListBottomLeftTv,
                    R.string.product_coupon_price, StringUtils.getFormattedDoubleOrInt(mShakeCouponBean.getCouponAmount()));
        }
        ViewUtil.setTextViewFormat(getContext(), mItemListBottomRightTv, R.string.product_commission_price,
                StringUtils.getFormattedDoubleOrInt(mShakeCouponBean.getCommissionMoney()));
        mItemListPriceTv.setText(StringUtils.getFormattedDoubleOrInt(mShakeCouponBean.getZkFinalPrice()));
        ViewUtil.setTextViewFormat(getContext(), mItemListVolumeTv, R.string.list_payment_text, StringUtils.getIVolume(mShakeCouponBean.getVolume()));

        ViewUtil.setDrawableOfTextView(mItemListTitleTv,ViewUtil.getIconImg(mShakeCouponBean.getIcon()), ViewUtil.DrawableDirection.LEFT);

        mHandler.sendEmptyMessageDelayed(NEWS_FLASH_FLAG, NEWS_FLASH_FLAG_DELAY_TIME);
        mShakeCouponShareTv.setText(String.valueOf(mShakeCouponBean.getDyShareCount()));

    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()){
            return;
        }
        switch (v.getId()) {
            case R.id.shake_coupon_love_tv:
                //点赞
                mShakeCouponLoveTv.setSelected(!isThumb);
                isThumb=!isThumb;
                break;
            case R.id.shake_coupon_text_tv:
                //文案
                if (mShakeCouponBean == null) {
                    LZLog.w(TAG,"抖券案分享错误");
                    return;
                }
                DiaLogManager.showCopyShakeCouponDialog((AppCompatActivity) ActivityStack.currentActivity(), mShakeCouponBean.getDyTitle());

                break;
            case R.id.shake_coupon_share_tv:
                //分享
                Activity activity =   ActivityStack.currentActivity();
                //多图 下载到本地
                ((BaseActivity)activity).showLoadingDialog();
                LiZhiApplication.getApplication().defaultExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        DowanUtil.downloadVideo(activity,mShakeCouponBean.getDyVideoUrl());
                        //延迟1秒 提升下载体验
                        Message obtain = Message.obtain();
                        obtain.what=DOWNLOAD_SUCCESS;
                        mHandler.sendMessageDelayed(obtain,1000);
                    }
                });
                break;
            case R.id.shake_coupon_buy_tv:
                if (mShakeCouponBean == null) {
                    return;
                }
                //去购买 跳转商品详情页
                CommonSchemeJump.showPreciseDetailActivity(getContext(), mShakeCouponBean.getItemId(),mShakeCouponBean.getIcon());
                break;
            default:
                doPauseResume();
                break;
        }
    }
    private void shareClick(){
        Activity activity =   ActivityStack.currentActivity();
        if (mShakeCouponBean == null) {
            return;
        }
        if (activity instanceof BaseActivity){
            //复制文案
            DeviceUtil.putClipboardText(activity,mShakeCouponBean.getDyTitle());
            LZShare.share(LZShare.getChannelListNativeVideo(null)).shareListener(new IShareManager.ShareListener() {
                @Override
                public void onShareListener(int state, String plat) {
                    Log.i(TAG, "handleMessage: 分享跳转弹框");
                    DiaLogManager.showOpenAppDialog(((BaseActivity)activity),plat,true);
                }

                @Override
                public void onShareDismissed() {

                }
            }).show();

        }


    }

    @Override
    public boolean handleMessage(Message msg) {
        mHandler.removeMessages(NEWS_FLASH_FLAG);
        if (msg.what == NEWS_FLASH_FLAG) {
            mBarrageRecyclerView.smoothScrollToPosition(newsFlashCurrentIndex);
            newsFlashCurrentIndex++;
            mHandler.sendEmptyMessageDelayed(NEWS_FLASH_FLAG, NEWS_FLASH_FLAG_DELAY_TIME);
            return true;
        }else if (msg.what==DOWNLOAD_SUCCESS){
            Activity activity = ActivityStack.currentActivity();
            if (activity instanceof BaseActivity){
                ((BaseActivity)activity).dismissLoadingDialog();
                Log.i(TAG, "handleMessage: 下载成功");
                shareClick();
            }

        }
        return false;
    }


}
