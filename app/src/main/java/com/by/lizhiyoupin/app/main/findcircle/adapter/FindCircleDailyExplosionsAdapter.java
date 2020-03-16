package com.by.lizhiyoupin.app.main.findcircle.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.dialog.FindCircleSelectViewHolder;
import com.by.lizhiyoupin.app.component_ui.impl.Response2Callback;
import com.by.lizhiyoupin.app.component_ui.impl.ShareDialogClickContentCallback;
import com.by.lizhiyoupin.app.component_ui.utils.DowanUtil;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpaceItemDecoration;
import com.by.lizhiyoupin.app.component_umeng.share.manager.IShareManager;
import com.by.lizhiyoupin.app.component_umeng.share.manager.ShareManager;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.LZShare;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.FindCircleChildListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.stack.ActivityStack;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.player.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 16:13
 * Summary: 发圈 每日爆款 的adapter
 */
public class FindCircleDailyExplosionsAdapter extends CommonRecyclerViewAdapter {
    private FragmentManager mFragmentManager;

    public FindCircleDailyExplosionsAdapter(Context context, FragmentManager fragmentManager) {
        super(context);
        this.mFragmentManager = fragmentManager;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.itam_daily_child_layout, parent, false);
        return new ViewHolder(inflate);
    }


    class ViewHolder extends CommonViewHolder implements View.OnClickListener {
        ImageView avatarIv;
        TextView nameTv;
        TextView timeTv;
        TextView shareNumberTv;
        TextView contentTv;
        RecyclerView innerRecyclerView;
        RecyclerView bottomRecyclerView;
        VideoView mVideoView;//视频播放器
        private final GridLayoutManager mGridLayoutManager;
        private final DailyChildImageAdapter innerImageAdapter;
        private final DailyInnerBottomAdapter mBottomAdapter;
        private StandardVideoController mController;//视频播放器 管理

        public ViewHolder(View itemView) {
            super(itemView);
            avatarIv = itemView.findViewById(R.id.item_daily_avatar_iv);
            nameTv = itemView.findViewById(R.id.item_daily_name_tv);
            timeTv = itemView.findViewById(R.id.item_daily_time_tv);
            shareNumberTv = itemView.findViewById(R.id.item_daily_share_number_tv);
            contentTv = itemView.findViewById(R.id.daily_content_tv);
            innerRecyclerView = itemView.findViewById(R.id.daily_inner_recyclerView);
            bottomRecyclerView = itemView.findViewById(R.id.daily_bottom_recyclerView);
            mVideoView = itemView.findViewById(R.id.videoView);
            shareNumberTv.setOnClickListener(this);
            //图片
            mGridLayoutManager = new GridLayoutManager(mContext, 3);
            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return innerImageAdapter.getNormalViewType(position) == DailyChildImageAdapter.ITEM_SINGLE_TYPE ? 3 : 1;
                }
            });
            innerRecyclerView.addItemDecoration(new SpaceItemDecoration(DeviceUtil.dip2px(mContext, 5), 3));
            innerRecyclerView.setLayoutManager(mGridLayoutManager);
            innerRecyclerView.setFocusable(false);
            innerRecyclerView.setNestedScrollingEnabled(false);
            innerImageAdapter = new DailyChildImageAdapter(mContext);
            innerRecyclerView.setAdapter(innerImageAdapter);

            //底部推荐商品
            bottomRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            bottomRecyclerView.setFocusable(false);
            bottomRecyclerView.setNestedScrollingEnabled(false);
            mBottomAdapter = new DailyInnerBottomAdapter(mContext);
            bottomRecyclerView.setAdapter(mBottomAdapter);

            mController = new StandardVideoController(itemView.getContext());
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            FindCircleChildListBean bean = (FindCircleChildListBean) itemData;
            if (TextUtils.isEmpty(bean.getRingCommodityVideo())) {
                mVideoView.setVisibility(View.GONE);
            } else {
                mVideoView.setVisibility(View.VISIBLE);
                ImageView thumb = mController.getThumb();
                Glide.with(thumb.getContext())
                        .load(bean.getRingCommodityVideoImg())
                        .error(android.R.color.white)
                        .placeholder(android.R.color.white)
                        .into(thumb);
                mVideoView.setUrl(bean.getRingCommodityVideo());
                mVideoView.setVideoController(mController);
            }


            new GlideImageLoader(mContext, bean.getAvatar())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .into(avatarIv);
            nameTv.setText(bean.getName());
            timeTv.setText(bean.getUpdateTime());
            shareNumberTv.setText(String.valueOf(bean.getShareNumber()));
            //好单库数据，设置 标签,需要转3次
            Spanned spanned = Html.fromHtml(bean.getDescContext());
            Spanned spanned1 = Html.fromHtml(spanned.toString());
            contentTv.setText(Html.fromHtml(spanned1.toString()));

            //图片
            innerImageAdapter.setListData(bean.getCommodityImgList());
            innerImageAdapter.notifyDataSetChanged();
            //推荐商品
            mBottomAdapter.setListData(bean.getCommodityInfoList());
            mBottomAdapter.notifyDataSetChanged();

        }

        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            switch (v.getId()) {
                case R.id.item_daily_share_number_tv:
                    FindCircleChildListBean bean = (FindCircleChildListBean) getListData().get(mRealPosition);
                    if (bean != null) {
                        if (contentTv != null) {
                            DeviceUtil.putClipboardText(mContext,contentTv.getText());
                        }
                        showShareType(bean, (TextView) v);
                    }
                    break;
            }
        }

        /**
         * 增加分享次数
         * @param bean
         * @param textView
         */
        private void sendAddShareNumber(FindCircleChildListBean bean, TextView textView) {
            ApiService.getFindCircleApi().requestAddShareTimes(bean.getId()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>(){});
            bean.setShareNumber(bean.getShareNumber() + 1);
            if (textView != null) {
                textView.setText(String.valueOf(bean.getShareNumber()));
            }
        }

        /**
         * 根据type 弹出 选择分享样式（图片 视频 海报）弹框
         *
         * @param type
         * @param bean
         */
        private void showDialog(int type, FindCircleChildListBean bean, TextView textView) {
            DiaLogManager.showFindCircleShareDialog(mContext, mFragmentManager, type, new ShareDialogClickContentCallback<String>() {
                @Override
                public void clickCallback(MessageBox messageBox, String tag) {
                    switch (tag) {
                        case FindCircleSelectViewHolder.TYPE_POSTER:
                            //先请求 得到海报，再分享
                            if (!LiZhiApplication.getApplication().getAccountManager().isLogined()){
                                CommonSchemeJump.showLoginActivity(mContext);
                                return;
                            }
                            Long goodId = bean.getCommodityInfoList().get(0).getCommodityId();
                            int platformType = bean.getCommodityInfoList().get(0).getPlatformType();
                            ShareManager.requestSharePosterWithCallback(goodId, platformType, new Response2Callback<List<String>>() {
                                @Override
                                public void callbackSuccess(List<String> posters) {
                                    //点击后 弹出 分享渠道框
                                    if (!ArraysUtils.isListEmpty(posters)) {
                                        LZShare.share(LZShare.getChannelListPic(posters.get(0))).shareListener(new IShareManager.ShareListener() {
                                            @Override
                                            public void onShareListener(int state, String plat) {
                                                sendAddShareNumber(bean,textView);
                                            }

                                            @Override
                                            public void onShareDismissed() {

                                            }
                                        }).show();
                                    }
                                }

                                @Override
                                public void callbackError(String error) {
                                    MessageToast.showToast(mContext,"获取海报出错");
                                }
                            });
                            messageBox.dismiss();
                            break;
                        case FindCircleSelectViewHolder.TYPE_PICTURE:
                            //先弹出选择分享渠道弹框，在下载图片 ，然后弹出跳转app弹框
                            LZShare.share(LZShare.getChannelListNativeDeal()).shareListener(new IShareManager.ShareListener() {
                                @Override
                                public void onShareListener(int state, String plat) {
                                    Activity activity = ActivityStack.currentActivity();
                                    if (activity instanceof BaseActivity) {
                                        ((BaseActivity) activity).showLoadingDialog();
                                    }
                                    Observable.just(new ArrayList<String>())
                                            .map(new Function<ArrayList<String>, ArrayList<Uri>>() {
                                                @Override
                                                public ArrayList<Uri> apply(ArrayList<String> strings) throws Exception {
                                                    int listSize = bean.getCommodityImgList().size();
                                                    ArrayList<Uri> uris = new ArrayList<>();
                                                    for (int i = 0; i < listSize; i++) {
                                                        File file = DowanUtil.saveImageToSdCard(mContext, bean.getCommodityImgList().get(i));
                                                        if (file != null) {
                                                            uris.add(Uri.fromFile(file));
                                                        }
                                                    }
                                                    return uris;
                                                }
                                            })
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DefaultRx2Subscribe<ArrayList<Uri>>() {
                                                @Override
                                                public void onNext(ArrayList<Uri> picList) {
                                                    super.onNext(picList);
                                                    Activity activity = ActivityStack.currentActivity();
                                                    if (activity instanceof BaseActivity) {
                                                        ((BaseActivity) activity).dismissLoadingDialog();
                                                        if (picList.size() > 1 && "WEIXIN_CIRCLE".equals(plat)) {
                                                            //微信朋友圈 不支持多图分享
                                                            DiaLogManager.showOpenAppDialog((FragmentActivity) activity, plat, false);
                                                        } else {
                                                            LZShare.showNativePicToWeichat(activity, plat, picList);
                                                        }
                                                        sendAddShareNumber(bean,textView);
                                                    }
                                                }
                                            });
                                }

                                @Override
                                public void onShareDismissed() {

                                }
                            }).show();

                            messageBox.dismiss();
                            break;
                        case FindCircleSelectViewHolder.TYPE_VIDEO:
                            //先显示渠道选择框，在下载视频，然后弹出去其他app的框
                            LZShare.share(LZShare.getChannelListNativeDeal()).shareListener(new IShareManager.ShareListener() {
                                @Override
                                public void onShareListener(int state, String plat) {
                                    Activity activity = ActivityStack.currentActivity();
                                    if (activity instanceof BaseActivity) {
                                        ((BaseActivity) activity).showLoadingDialog();
                                    }
                                    Observable.just(new Object())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .map(new Function<Object, Object>() {
                                                @Override
                                                public Object apply(Object o) throws Exception {
                                                    DowanUtil.downloadVideo(mContext, bean.getRingCommodityVideo());
                                                    return o;
                                                }
                                            })
                                            .subscribe(new DefaultRx2Subscribe<Object>() {
                                                @Override
                                                public void onNext(Object o) {
                                                    super.onNext(o);
                                                    if (activity instanceof BaseActivity) {
                                                        ((BaseActivity) activity).dismissLoadingDialog();
                                                        DiaLogManager.showOpenAppDialog((FragmentActivity) activity, plat, true);
                                                    }
                                                    sendAddShareNumber(bean,textView);

                                                }
                                            });
                                }

                                @Override
                                public void onShareDismissed() {

                                }
                            }).show();
                            messageBox.dismiss();
                            break;
                    }
                }
            });
        }


        /**
         * 判断类型 选择需要显示的分享样式弹框
         *
         * @param bean
         */
        private void showShareType(FindCircleChildListBean bean, TextView textView) {

            if (TextUtils.isEmpty(bean.getRingCommodityVideo())) {
                //无视频分享
                if (ArraysUtils.isListEmpty(bean.getCommodityImgList())) {
                    //没有图片和视频
                    if (!ArraysUtils.isListEmpty(bean.getCommodityInfoList())) {
                        //1 ----  只分享 海报
                        showDialog(1, bean, textView);
                    }else {
                        //好单库 转3次 只分享文字
                        Spanned spanned = Html.fromHtml(Html.fromHtml(bean.getDescContext()).toString());
                        LZShare.share(LZShare.getChannelListText(Html.fromHtml(spanned.toString()).toString()))
                                .show();
                    }
                } else {
                    //没有视频，但有图片
                    if (!ArraysUtils.isListEmpty(bean.getCommodityInfoList())) {
                        //2 ----  图片或 海报
                        showDialog(4, bean, textView);
                    } else {
                        //3 ---- 只分享 图片
                        showDialog(2, bean, textView);
                    }
                }
            } else {//有视频
                if (ArraysUtils.isListEmpty(bean.getCommodityImgList())) {
                    if (!ArraysUtils.isListEmpty(bean.getCommodityInfoList())) {
                        //1 ----   分享 视频或海报
                        showDialog(5, bean, textView);
                    } else {
                        //2   只分享 视频
                        showDialog(3, bean, textView);
                    }
                } else {
                    // 有视频， 有图片
                    if (!ArraysUtils.isListEmpty(bean.getCommodityInfoList())) {
                        //3 ----  视频或 图片或 海报
                        showDialog(7, bean, textView);
                    } else {
                        //4 ---- 分享 视频或图片
                        showDialog(6, bean, textView);
                    }
                }

            }
        }
    }
}
