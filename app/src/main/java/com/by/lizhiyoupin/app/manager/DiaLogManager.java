package com.by.lizhiyoupin.app.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.PermissionsUtil;
import com.by.lizhiyoupin.app.component_ui.dialog.AuthViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.BottomCancelViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.BottomCloseViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.DevelopSettingIpViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.FansDetailViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.FindCircleSelectViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.HomeRedPagerViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.IncomeRecordViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.InvitationCodeViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.InviterViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.OrderFindErrorViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.OrderFindSuccessViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.PushTagsSelectViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.RedGiftPackageViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.RedPagerViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.SelectAgreementViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.SelectAvatarViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.SelectPayTypeViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.ShareWXViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.ToastXViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.UpdateForceViewHolder;
import com.by.lizhiyoupin.app.component_ui.dialog.UpdateTipsViewHolder;
import com.by.lizhiyoupin.app.component_ui.impl.DialogCallBack;
import com.by.lizhiyoupin.app.component_ui.impl.SelectAvatarCallback;
import com.by.lizhiyoupin.app.component_ui.impl.SelectCallback;
import com.by.lizhiyoupin.app.component_ui.impl.SelectPayTypeCallback;
import com.by.lizhiyoupin.app.component_ui.impl.ShareDialogClickCallback;
import com.by.lizhiyoupin.app.component_ui.impl.ShareDialogClickContentCallback;
import com.by.lizhiyoupin.app.component_ui.utils.ThreeAppJumpUtil;
import com.by.lizhiyoupin.app.io.bean.CommonCategoryBean;
import com.by.lizhiyoupin.app.io.bean.RedGiftBean;
import com.by.lizhiyoupin.app.io.bean.SignInRedPaperBean;
import com.by.lizhiyoupin.app.io.bean.UpdateViersionBean;
import com.by.lizhiyoupin.app.main.weight.HomeMoreViewHolder;
import com.by.lizhiyoupin.app.main.weight.SearchClipViewHolder;
import com.by.lizhiyoupin.app.main.weight.ShakeCouponCopyViewHolder;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.listener.ButtonClickListener;
import com.by.lizhiyoupin.app.weight.week.OnTimeWeekSelectListener;
import com.by.lizhiyoupin.app.weight.week.WeekSelectView;
import com.by.lizhiyoupin.app.weight.week.WeelTimePickerBuilder;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.transition.Slide;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/18 10:10
 * Summary:
 */
public class DiaLogManager {
    public static final String TAG = DiaLogManager.class.getSimpleName();

    /**
     * 开发者选项
     *
     * @param activity
     * @param fragmentManager
     * @param dialogCallBack
     */
    public static void showDevelopIpSettingDialog(Activity activity, FragmentManager fragmentManager, DialogCallBack<View> dialogCallBack) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new DevelopSettingIpViewHolder(activity, dialogCallBack))
                .bgColor(Color.WHITE)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(true)
                .build("develop_ip_setting_dialog")
                .show(fragmentManager);
    }

    /**
     * 强制更新,弹框不可取消
     *
     * @param context
     * @param fragmentManager
     */
    public static void showUpdateForceDialog(Context context, FragmentManager fragmentManager, UpdateViersionBean versionBean) {
        int radius = DeviceUtil.dip2px(context, 10);
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new UpdateForceViewHolder(context, versionBean))
                .bgColor(Color.WHITE)
                .dimAmount(0.6f)
                .radius(radius, radius, radius, radius)
                .cancelOnTouchOutside(false)
                .build("update_force_dialog")
                .show(fragmentManager);
    }

    /**
     * 更新提示,弹框可取消
     *
     * @param context
     * @param fragmentManager
     */
    public static void showUpdateTipsDialog(Context context, FragmentManager fragmentManager, UpdateViersionBean versionBean) {
        int radius = DeviceUtil.dip2px(context, 10);
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new UpdateTipsViewHolder(context, versionBean))
                .bgColor(Color.WHITE)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .radius(radius, radius, radius, radius)
                .build("update_tips_dialog")
                .show(fragmentManager);
    }

    /**
     * 首页 更多栏目弹框
     *
     * @param context
     * @param list
     * @param top
     */
    public static void showHomeMoreDialog(FragmentActivity context, List<CommonCategoryBean> list, float top) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(1f)
                .fullScreen(true)
                .titleHeight(0)
                .contentHolder(new HomeMoreViewHolder(context, list, top))
                .bgColor(Color.TRANSPARENT)
                .dimAmount(0f)
                .cancelOnTouchOutside(true)
                .build("home_more_dialog")
                .show(context.getSupportFragmentManager());
    }

    /**
     * 显示搜索粘贴板 弹框
     *
     * @param context
     * @param cliptext
     */
    public static void showClipDialog(AppCompatActivity context, String cliptext, Long goodsId) {
        int radius = DeviceUtil.dip2px(context, 8);
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new SearchClipViewHolder(context, "你是否想搜索以下商品", cliptext, goodsId))
                .bottomHolder(new BottomCloseViewHolder(context))
                .bgColor(Color.WHITE)
                .radius(radius, radius, radius, radius)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("search_clip_dialog")
                .show(context.getSupportFragmentManager());
    }

    /**
     * 发圈 分享内容选择弹框
     *
     * @param context
     * @param fragmentManager
     * @param type            1海报 2只有图片 3只有视频 4海报+图片 5海报+视频 6图片+视频 7海报+图片+视频
     */
    public static void showFindCircleShareDialog(Context context, FragmentManager fragmentManager, @IntRange(from = 1, to = 7) int type, ShareDialogClickContentCallback<String> callback) {
        int radius = DeviceUtil.dip2px(context, 13);
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.95f)
                .dialogGravity(Gravity.BOTTOM)
                .contentHolder(new FindCircleSelectViewHolder(context, type, callback))
                .bottomHolder(new BottomCancelViewHolder(context))
                .bgColor(Color.WHITE)
                .radius(radius, radius, radius, radius)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(true)
                .build("find_circle_share_dialog")
                .show(fragmentManager);
    }

    /**
     * 显示 绑定邀请人弹框
     *
     * @param context
     */
    public static void showInvitationCodeDialog(Context context, FragmentManager fragmentManager) {
        int radius = DeviceUtil.dip2px(context, 8);
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new InvitationCodeViewHolder(context))
                .bottomHolder(new BottomCloseViewHolder(context))
                .bgColor(Color.WHITE)
                .radius(radius, radius, radius, radius)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("invitation_code_dialog")
                .show(fragmentManager);
    }

    /**
     * 抖券文案复制
     *
     * @param context
     * @param cliptext
     */
    public static void showCopyShakeCouponDialog(AppCompatActivity context, String cliptext) {
        int radius = DeviceUtil.dip2px(context, 8);
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new ShakeCouponCopyViewHolder(context, cliptext))
                .bottomHolder(new BottomCloseViewHolder(context))
                .bgColor(Color.WHITE)
                .radius(radius, radius, radius, radius)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("copy_shake_coupon_dialog")
                .show(context.getSupportFragmentManager());
    }

    /**
     * 微信多图分享提示弹框
     *
     * @param context
     * @param fragmentManager
     * @param title
     * @param clickTitle
     * @param callback
     */
    public static void showShareWXViewDialog(Context context, FragmentManager fragmentManager, String title, String clickTitle, ShareDialogClickCallback callback) {
        showShareWXViewDialog(context, fragmentManager, title, clickTitle, callback, false);
    }

    /**
     * 微信多图或视频分享提示弹框
     *
     * @param context
     * @param fragmentManager
     * @param title
     * @param clickTitle
     * @param callback
     * @param isVideo         是否是视频分享
     */
    public static void showShareWXViewDialog(Context context, FragmentManager fragmentManager, String title, String clickTitle, ShareDialogClickCallback callback, boolean isVideo) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new ShareWXViewHolder(context, title, clickTitle, callback, isVideo))
                .bottomHolder(new BottomCloseViewHolder(context))
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("sx_tips_dialog")
                .show(fragmentManager);
    }


    /**
     * 提示去授权弹框
     *
     * @param context
     * @param fragmentManager
     */
    public static void showAuthDialog(Context context, FragmentManager fragmentManager, String url) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new AuthViewHolder(context, url, "申请淘宝授权", "前往淘宝授权", null))
                .bottomHolder(new BottomCloseViewHolder(context))
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("auth_tips_dialog")
                .show(fragmentManager);
    }

    /**
     * 升级vip提示
     *
     * @param context
     * @param fragmentManager
     */
    public static void showOrderVipTipsDialog(Context context, FragmentManager fragmentManager) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .title("提示")
                .contentMessage("确定升级荔枝优品VIP吗")
                .okTextColorRes(R.color.color_D60050)
                .cancelTextColor(R.color.color_333333)
                .okClickListener(new ButtonClickListener() {
                    @Override
                    public void onButtonClick(View buttonView, Bundle arguments, Object tag, CharSequence text, int position) {

                    }
                })
                .Ok("确定")
                .Cancel("取消")
                .bgContentColor(Color.WHITE)
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("order_vip_tips_dialog")
                .show(fragmentManager);
    }

    /**
     * 选择支付类型
     *
     * @param context
     * @param fragmentManager
     * @param callback
     */
    public static void showSelectPayTypeDialog(Context context, FragmentManager fragmentManager, SelectPayTypeCallback callback) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(1f)
                .contentHolder(new SelectPayTypeViewHolder(context, callback))
                .bgContentColor(Color.TRANSPARENT)
                .bgColor(Color.TRANSPARENT)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("pay_type_dialog")
                .show(fragmentManager);
    }

    /**
     * 选择图片
     *
     * @param context
     * @param fragmentManager
     * @param callback
     */
    public static void showSelectAvatarDialog(Context context, FragmentManager fragmentManager, SelectAvatarCallback callback) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(1f)
                .contentHolder(new SelectAvatarViewHolder(context, callback))
                .bgContentColor(Color.TRANSPARENT)
                .bgColor(Color.TRANSPARENT)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("avatar_type_dialog")
                .show(fragmentManager);

    }

    /**
     * 年月周时间选择器
     *
     * @param context
     */
    public static void showTimeWeekPicker(Context context, OnTimeWeekSelectListener weekSelectListener) {
        Calendar currentDate = Calendar.getInstance();
        //当前选中时间 需要比结束时间早，即需要先生成
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(1980, 0, 1);
        WeekSelectView build = new WeelTimePickerBuilder(context)
                .setType(new boolean[]{true, true, true, false, false, false})//分别对应年月日时分秒，默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setSubCalSize(14)
                .setContentTextSize(16)//滚轮文字大小
                .setTitleSize(16)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#3388FF"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#999999"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "周", "时", "分", "秒")
                .setLineSpacingMultiplier(2f)
                .setDate(currentDate)
                .isDialog(false)//是否显示为对话框样式
                .build();
        build.setOnTimeWeekSelectListener(weekSelectListener);
        build.show();
    }

    /**
     * 年月日选择器
     *
     * @param context
     * @param onTimeSelectListener
     */
    public static TimePickerView showYearMonthDayPicker(Context context, OnTimeSelectListener onTimeSelectListener) {
        Calendar currentDate = Calendar.getInstance();
        //当前选中时间 需要比结束时间早，即需要先生成
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(1980, 0, 1);
        TimePickerView build = new TimePickerBuilder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, false, false, false})//分别对应年月日时分秒，默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setSubCalSize(14)
                .setContentTextSize(16)//滚轮文字大小
                .setTitleSize(16)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#3388FF"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#999999"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLineSpacingMultiplier(2f)
                .setDate(currentDate)
                .isDialog(false)//是否显示为对话框样式
                .build();
        build.show();
        return build;
    }


    /**
     * 年月日时选择器
     *
     * @param context
     * @param onTimeSelectListener
     */
    public static TimePickerView showYearMonthDayHourPicker(Context context, OnTimeSelectListener onTimeSelectListener) {
        Calendar currentDate = Calendar.getInstance();
        //当前选中时间 需要比结束时间早，即需要先生成
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 3);
        TimePickerView build = new TimePickerBuilder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, true, false, false})//分别对应年月日时分秒，默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setSubCalSize(14)
                .setContentTextSize(16)//滚轮文字大小
                .setTitleSize(16)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#3388FF"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#999999"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLineSpacingMultiplier(2f)
                .setDate(currentDate)
                .isDialog(false)//是否显示为对话框样式
                .build();
        build.show();
        return build;
    }

    /**
     * 年月选择器
     *
     * @param context
     * @param onTimeSelectListener
     */
    public static TimePickerView showYearMonthPicker(Context context, Calendar currentDate, OnTimeSelectListener onTimeSelectListener) {

        //当前选中时间 需要比结束时间早，即需要先生成
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(1980, 0, 1);
        TimePickerView build = new TimePickerBuilder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, false, false, false, false})//分别对应年月日时分秒，默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setSubCalSize(14)
                .setContentTextSize(16)//滚轮文字大小
                .setTitleSize(16)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#3388FF"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#999999"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLineSpacingMultiplier(2f)
                .setDate(currentDate)
                .isDialog(false)//是否显示为对话框样式
                .build();
        build.show();
        return build;
    }

    /**
     * 显示粉丝详情
     *
     * @param context
     * @param uid
     * @param fragmentManager
     */
    public static void showFansDetailDialog(Context context, Long uid, FragmentManager fragmentManager) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new FansDetailViewHolder(context, uid))
                .bgColor(Color.WHITE)
                .radius(32, 32, 32, 32)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("fans_detail_dialog")
                .show(fragmentManager);
    }

    /**
     * 查看我的邀请人
     *
     * @param context
     * @param fragmentManager
     */
    public static void showMyInviterDialog(Context context, FragmentManager fragmentManager) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new InviterViewHolder(context))
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("my_inviter_dialog")
                .show(fragmentManager);
    }

    /**
     * 订单找回成功
     *
     * @param context
     * @param fragmentManager
     */
    public static void showOrderFindSuccessDialog(Context context, FragmentManager fragmentManager) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new OrderFindSuccessViewHolder(context))
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("order_find_success_dialog")
                .show(fragmentManager);
    }

    /**
     * 订单找回失败
     *
     * @param context
     * @param fragmentManager
     */
    public static void showOrderFindErrorDialog(Context context, FragmentManager fragmentManager) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new OrderFindErrorViewHolder(context))
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("order_find_error_dialog")
                .show(fragmentManager);
    }

    /**
     * 设置推送提醒
     *
     * @param context
     * @param fragmentManager
     */
    public static void showSettingPushTipsDialog(Context context, FragmentManager fragmentManager) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .Ok("去设置")
                .contentMessage("是否打开推送，设置推送提醒？")
                .Cancel("取消")
                .cancelTextSize(18)
                .okTextSize(18)
                .okClickListener(new ButtonClickListener() {
                    @Override
                    public void onButtonClick(View buttonView, Bundle arguments, Object tag, CharSequence text, int position) {
                        PermissionsUtil.requestNotify(context);
                    }
                })
                .okTextColorRes(R.color.color_FF005E)
                .cancelTextColorRes(R.color.color_999999)
                .cancelOnTouchOutside(false)
                .build("setting_push_tips_dialog")
                .show(fragmentManager);
    }

    /**
     * 推送开关 提示去打开
     *
     * @param context
     * @param fragmentManager
     * @param title
     * @param contentMesaage
     */
    public static void showSettingSignInPushTipsDialog(Context context, FragmentManager fragmentManager, String title, String contentMesaage) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .Ok("马上开启")
                .titleBold(true)
                .title(title)
                .contentMessage(contentMesaage)
                .Cancel("取消")
                .cancelTextSize(18)
                .okTextSize(18)
                .okClickListener(new ButtonClickListener() {
                    @Override
                    public void onButtonClick(View buttonView, Bundle arguments, Object tag, CharSequence text, int position) {
                        PermissionsUtil.requestNotify(context);
                    }
                })
                .okTextColorRes(R.color.color_FF005E)
                .cancelTextColorRes(R.color.color_999999)
                .cancelOnTouchOutside(false)
                .build("setting_push_sign_in_tips_dialog")
                .show(fragmentManager);
    }

    /**
     * 设置提醒成功
     *
     * @param context
     * @param fragmentManager
     */
    public static void showSettingPushSuccessDialog(Context context, FragmentManager fragmentManager) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.7f)
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .Ok("知道了")
                .contentMessage("提醒设置成功")
                .contentMessageSize(16)
                .contentMessageColor(context.getResources().getColor(R.color.color_333333))
                .okTextSize(18)
                .okTextColorRes(R.color.color_FF005E)
                .cancelOnTouchOutside(false)
                .build("setting_push_success_dialog")
                .show(fragmentManager);
    }

    /**
     * 分享 -- 打开其他 app
     *
     * @param activity
     * @param plat
     * @param isVideo
     */
    public static void showOpenAppDialog(FragmentActivity activity, String plat, boolean isVideo) {
        switch (plat) {
            case "QQ":
                DiaLogManager.showShareWXViewDialog(activity, activity.getSupportFragmentManager(),
                        "QQ", "打开QQ", new ShareDialogClickCallback() {
                            @Override
                            public void clickCallback() {
                                ThreeAppJumpUtil.jump2QQchat(activity, "");
                            }
                        }, isVideo);
                break;
            case "WEIXIN":
                DiaLogManager.showShareWXViewDialog(activity, activity.getSupportFragmentManager(),
                        "微信好友", "打开微信", new ShareDialogClickCallback() {
                            @Override
                            public void clickCallback() {
                                LZLog.i(TAG, "handleMessage: 开始打开微信APP");
                                ThreeAppJumpUtil.jump2Wechat(activity);
                            }
                        }, isVideo);
                break;
            case "WEIXIN_CIRCLE":
                DiaLogManager.showShareWXViewDialog(activity, activity.getSupportFragmentManager(),
                        "朋友圈", "打开朋友圈", new ShareDialogClickCallback() {
                            @Override
                            public void clickCallback() {
                                LZLog.i(TAG, "handleMessage: 开始打开朋友圈");
                                ThreeAppJumpUtil.jump2Wechat(activity);
                            }
                        }, isVideo);
                break;
            case "MORE"://视频分享
                DiaLogManager.showShareWXViewDialog(activity, activity.getSupportFragmentManager(),
                        "保存视频", "知道了", null, isVideo);
                break;
        }

    }

    /**
     * 打开 红包 弹框
     *
     * @param context
     * @param fragmentManager
     */
    public static void showRedGiftPackageDialog(Context context, FragmentManager fragmentManager, RedGiftBean redGiftBean) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new RedGiftPackageViewHolder(context, redGiftBean))
                .bottomHolder(new BottomCloseViewHolder(context))
                .bgColor(Color.TRANSPARENT)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("red_gift_package_dialog")
                .show(fragmentManager);
    }


    /**
     * 收益记录弹框
     *
     * @param context
     * @param fragmentManager
     */
    public static void showIncomeRecordDialog(Context context, FragmentManager fragmentManager) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.7f)
                .contentHolder(new IncomeRecordViewHolder(context))
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("income_record_dialog")
                .show(fragmentManager);
    }

    /**
     * 运营商推送标签选择
     *
     * @param context
     * @param fragmentManager
     * @param selectCallback
     */
    public static void showPushTagSelectDialog(Context context, FragmentManager fragmentManager,
                                               List<String> list, SelectCallback<String> selectCallback) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(1)
                .contentHolder(new PushTagsSelectViewHolder(context, list, selectCallback))
                .bgColor(Color.WHITE)
                .dialogGravity(Gravity.BOTTOM)
                .dimAmount(0.6f)
                .animationStyleRes(R.style.message_box_view_slide_anim)
                .cancelOnTouchOutside(true)
                .build("push_tags_dialog")
                .show(fragmentManager);
    }

    /**
     * 显示吐司 还未完成 不可用
     *
     * @param context
     * @param fragmentManager
     * @param message
     * @param drawRes
     * @param ltrb
     * @param canClickDismiss true 点击消失
     */
    public static void showToastDialog(Context context, FragmentManager fragmentManager, String message,
                                       int drawRes, @Slide.GravityFlag int ltrb, boolean canClickDismiss, DialogCallBack<Object> callBack) {
        MessageBox.builder()
                .autoFitWindowWidth(false)
                .contentHolder(new ToastXViewHolder(context, message, drawRes, ltrb, canClickDismiss, callBack))
                .bgColor(Color.TRANSPARENT)
                .dimAmount(0.0f)
                .cancelOnTouchOutside(false)
                .build("toast_x_dialog")
                .show(fragmentManager);
    }

    public static void showToastDialog(Context context, FragmentManager fragmentManager, String message,
                                       int drawRes, @Slide.GravityFlag int ltrb, boolean canClickDismiss) {
        showToastDialog(context, fragmentManager, message, drawRes, ltrb, canClickDismiss, null);
    }

    /**
     * 无佣金方式--详情跳转三方app确认
     *
     * @param fragmentManager
     * @param message
     * @param okClickListener
     * @param cancelClickListener
     */
    public static void showDetailConfirmDialog(FragmentManager fragmentManager, String message,
                                               ButtonClickListener okClickListener,
                                               ButtonClickListener cancelClickListener) {
        MessageBox.builder()
                .autoFitWindowWidth(false)
                .Ok("确定")
                .contentMessage(message)
                .okClickListener(okClickListener)
                .cancelClickListener(cancelClickListener)
                .Cancel("取消")
                .bgColor(Color.WHITE)
                .dimAmount(0.6f)
                .radius(20, 20, 20, 20)
                .cancelOnTouchOutside(false)
                .build("detail_confirm_dialog")
                .show(fragmentManager);
    }

    /**
     * 领取签到红包
     *
     * @param fragmentManager
     * @param context
     * @param inRedPaperBean
     * @param dialogCallBack
     */
    public static void showSignInRedPagerDialog(FragmentManager fragmentManager, Context context,
                                                SignInRedPaperBean inRedPaperBean, DialogCallBack<SignInRedPaperBean> dialogCallBack) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new RedPagerViewHolder(context, inRedPaperBean, dialogCallBack))
                .bgColor(Color.TRANSPARENT)//透明则不用管圓角了
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("sign_in_red_pager_dialog")
                .show(fragmentManager);
    }

    /**
     * 首页签到弹框
     * @param context
     * @param fragmentManager
     */
    public static void showHomeSignInRedPagerDialog( Context context,FragmentManager fragmentManager) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new HomeRedPagerViewHolder(context))
                .bottomHolder(new BottomCloseViewHolder(context))
                .bgColor(Color.TRANSPARENT)//透明则不用管圓角了
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("home_sign_in_red_pager_dialog")
                .show(fragmentManager);
    }

    /**
     * 新人优惠券领取提示
     *
     * @param fragmentManager
     * @param message
     */
    public static void showNewUserCouponDialog(FragmentManager fragmentManager, String message) {
        MessageBox.builder()
                .autoFitWindowWidth(false)
                .Ok("进入好券专区")
                .contentMessage(message)
                .contentMessageSize(16)
                .okTextColor(Color.parseColor("#FF005E"))
                .bgColor(Color.WHITE)
                .dimAmount(0.6f)
                .radius(20, 20, 20, 20)
                .cancelOnTouchOutside(false)
                .build("new_user_coupon_dialog")
                .show(fragmentManager);
    }

    /**
     * 协议弹框
     *
     * @param context
     * @param fragmentManager
     * @param callback
     */
    public static void showAgreementDialog(Context context, FragmentManager fragmentManager, SelectAvatarCallback callback) {
        MessageBox.builder()
                .autoFitWindowWidth(true)
                .autoFitWindowWidth(0.8f)
                .contentHolder(new SelectAgreementViewHolder(context, callback))
                .bgContentColor(Color.TRANSPARENT)
                .bgColor(Color.TRANSPARENT)
                .radius(20, 20, 20, 20)
                .dimAmount(0.6f)
                .cancelOnTouchOutside(false)
                .build("Agreement_type_dialog")
                .show(fragmentManager);

    }
}
