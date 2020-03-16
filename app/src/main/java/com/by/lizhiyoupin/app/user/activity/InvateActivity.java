package com.by.lizhiyoupin.app.user.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.FileUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_banner.Banner;
import com.by.lizhiyoupin.app.component_banner.BannerConfig;
import com.by.lizhiyoupin.app.component_banner.Transformer;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.BannerLayoutLoader;
import com.by.lizhiyoupin.app.component_umeng.share.manager.IShareManager;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.LZShare;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

/*
 * 邀请好友
 * jyx
 * */
@Route(path = "/app/InvateActivity")
public class InvateActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = InvateActivity.class.getSimpleName();
    private TextView mTitle, actionbar_back_tv, invate_scode, custom_wxcopy, custom_wx,
            invate_text, text_copy;
    private RelativeLayout linear_invate;
    private Banner mTopBanner;
    private List<Integer> mBannerList = new ArrayList<>();
    private LinearLayout share_link, share_poster;
    private String code;
    private static int[] images = {R.drawable.invateposter1, R.drawable.invateposter2,
            R.drawable.invateposter3, R.drawable.invateposter4, R.drawable.invateposter5};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invate);
        initImmersionBar(Color.WHITE,true);
        initBar();
        initView();
        initRecord();
        setBannerConfig();
        initCode();
    }

    private void initCode() {


    }

    private void setBannerConfig() {
        mTopBanner.setImageLoader(new BannerLayoutLoader())
                .setBannerStyle(BannerConfig.NOT_INDICATOR)
                .setOffscreenPageLimit(3)
                .setPageMargin(20)
                .setBannerAnimation(Transformer.ZoomOutSlide)
                .setIndicatorGravity(BannerConfig.CENTER)
                .isAutoPlay(false)
                .setViewPageMargin((int) 100, 0, (int) 100, 0)
                .start();


        for (int i = 0; i < images.length; i++) {
            mBannerList.add(images[i]);
        }
        mTopBanner.update(mBannerList);


        mTopBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        invate_text.setText("荔枝优品网罗全球尖端好货,\n可领全网95%的优惠券.\n花低价也能买到大牌商品,\n让你省钱买买买.\n开心赚赚赚！");
                        break;
                    case 1:
                        invate_text.setText("你网购和我网购的区别在哪里?\n答案就是下单前,\n在荔枝优品上领取优惠券,\n你花一样的价格买一件商品,我能买到两件,\n荔枝优品就是实实在在地帮你省钱!");
                        break;
                    case 2:
                        invate_text.setText("网购还在到处找优惠券吗?\n真正的低价在这里,\n海量淘宝、京东、拼多多优惠券轻松领,\n没有套路,领多少减多少,\n下载荔枝优品,每月帮你省省省!");
                        break;
                    case 3:
                        invate_text.setText("我们每天的常态都是\n用手机刷朋友圈、刷微博、看电视剧?\n不如利用空闲时间,\n边玩手机边赚钱,\n用荔枝优品分享赚钱,\n一不小心还能实现财务自由。");
                        break;
                    case 4:
                        invate_text.setText("很多人用尽全力去生活,\n每天在自己的岗位上打拼,\n但不过勉强温饱,\n加入荔枝优品,\n给自己一份副业,\n给自己的收入增加一份保障!");
                        break;
                }
                Log.e("position:",position+"");
            }
        });
    }

    private void initRecord() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        code = accountManager.getAccountInfo().getCode();
        invate_scode.setText(code);
        custom_wx.setText("lizhiyoupin888");

    }

    private void initView() {

        actionbar_back_tv.setOnClickListener(this);
        invate_scode = findViewById(R.id.invate_scode);//邀请码
        linear_invate = findViewById(R.id.linear_invate);
        linear_invate.setOnClickListener(this);//复制邀请码
        custom_wxcopy = findViewById(R.id.custom_wxcopy);
        custom_wxcopy.setOnClickListener(this);//复制邀请码
        custom_wx = findViewById(R.id.custom_wx);//客服微信
        text_copy = findViewById(R.id.text_copy);
        text_copy.setOnClickListener(this);//复制按钮
        invate_text = findViewById(R.id.invate_text);//复制文本
        mTopBanner = findViewById(R.id.top_banner);//banner图
        share_poster = findViewById(R.id.share_poster);//分享海报
        share_link = findViewById(R.id.share_link);//分享链接
        share_link.setOnClickListener(this);
        share_poster.setOnClickListener(this);

    }

    private void initBar() {
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setText("");
        mTitle.setText("邀请好友");
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
            case R.id.linear_invate:
                //复制邀请码
                copy(invate_scode.getText());
                break;
            case R.id.custom_wxcopy:
                //复制客服微信号
                copy(custom_wx.getText());
                break;
            case R.id.text_copy:
                //复制文本
                copy(invate_text.getText());
                break;
            case R.id.share_poster://分享海报
                int currentItem = mTopBanner.getViewPager().getCurrentItem();
                View view = mTopBanner.getImageViews().get(currentItem);
                String fileName = "poster" + currentItem;
                String screenPosterUrl = FileUtil.getScreenPosterUrl(fileName);
                //复制文本
                copy(invate_text.getText());
                File file = new File(screenPosterUrl);
                if (file.exists()) {
                    LZLog.i(TAG, "已存在直接分享");
                    LZShare.share(LZShare.getChannelListNativeFile(file)).show();
                } else {

                    LZLog.i(TAG, "不存在先生成分享");
                    ViewGroup review = view.findViewById(R.id.review);
                    Bitmap bitmap = onDrawBitmap(this, review,
                            new int[]{review.getWidth(),
                                    review.getHeight()}, fileName);
                    LZShare.share(LZShare.getChannelListNativeFile(file)).show();
                }
                break;
            case R.id.share_link://分享链接
                shareWithPlat();
                break;
        }
    }

    /**
     * @param texting 复制文本
     */
    private void copy(CharSequence texting) {
        CharSequence text = texting;
        DeviceUtil.putClipboardText(this, text);
        MessageToast.showToastBottom(InvateActivity.this,"已复制", Gravity.CENTER);
    }

    private void shareWithPlat() {

        List<ChannelListBean> channelListBeans = new ArrayList<>();

        ChannelListBean channelListBean = new ChannelListBean();
        ChannelListBean channelListBean2 = new ChannelListBean();
        channelListBean.setCode(0);
        channelListBean.setName("微信");
        channelListBean.setShareTitle("快来和我一起省钱赚钱吧");
        channelListBean.setShareType(2);
        channelListBean.setDesc("大品牌有保障，隐藏优惠不用抢，天天最低价，随心买，任性赚！");
        channelListBean.setLink(WebUrlManager.getRegisterShareUrl(code));
        channelListBeans.add(channelListBean);

        channelListBean2.setCode(1);
        channelListBean2.setName("朋友圈");
        channelListBean2.setShareTitle("快来和我一起省钱赚钱吧");
        channelListBean2.setShareType(2);
        channelListBean2.setDesc("大品牌有保障，隐藏优惠不用抢，天天最低价，随心买，任性赚！");
        channelListBean2.setLink(WebUrlManager.getRegisterShareUrl(code));
        channelListBeans.add(channelListBean2);


        final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                .getManager(IShareManager.class.getName());
        if (shareManager == null) {
            return;
        }
        shareManager.showShareDialog(this, channelListBeans);
    }

    public static Bitmap onDrawBitmap(Context mContext, ViewGroup viewGroup, int[] wh, String fileName) {
        Bitmap bitmap = null;
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        if (wh[0] <= 0) {
            wh[0] = widthPixels;
        }
        bitmap = Bitmap.createBitmap(wh[0], wh[1], Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        viewGroup.draw(canvas);
        try {
            String filePath = FileUtil.getScreenPosterUrl(fileName);
            File file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
