package com.by.lizhiyoupin.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.weight.CenterAlignImageSpan;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/6 09:45
 * Summary:
 */
public class ViewUtil {
    private static Typeface mTypeface;
    /**
     * 获取自定义字体
     *
     * @return
     */
    public static Typeface getTypeface() {
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(ContextHolder.getInstance().getContext().getAssets(), "fonts/font_bold.otf");
        }
        return mTypeface;
    }

    public static void setMoneyText(final Context context, final TextView textView, final String content) {
        if (TextUtils.isEmpty(content) || content.length() < 2) {
            return;
        }
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new TextAppearanceSpan(context, R.style.product_money01),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(context, R.style.product_money02),
                1, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setTypeface(getTypeface());
        textView.setText(spannableString);

    }

    /**
     * 用与设置 ￥和价格 的字体大小颜色
     *
     * @param context
     * @param textView
     * @param content  ¥22.21
     * @param style1   ¥的大小及颜色style  如 R.style.product_money01
     * @param style2   22.21的大小及颜色style  如 R.style.product_money02
     */
    public static void setMoneyText(final Context context, final TextView textView, final String content, int style1, int style2) {
        if (TextUtils.isEmpty(content) || content.length() < 2) {
            return;
        }
        SpannableString spannableString = new SpannableString(content);
        if (style1 > 0) {
            spannableString.setSpan(new TextAppearanceSpan(context, style1),
                    0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (style2 > 0) {
            spannableString.setSpan(new TextAppearanceSpan(context, style2),
                    1, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setTypeface(getTypeface());
        textView.setText(spannableString);
    }

    /**
     *
     * @param context
     * @param userTypeface 是否使用 外置字体
     * @param textView
     * @param content 12.01元
     * @param style1 12.01 的大小及颜色style
     * @param style2 元 的大小及颜色style
     */
    public static void setLastText(final Context context, boolean userTypeface,final TextView textView, final String content, int style1, int style2){
        if (TextUtils.isEmpty(content) || content.length() < 2) {
            return;
        }
        SpannableString spannableString = new SpannableString(content);
        if (style1 > 0) {
            spannableString.setSpan(new TextAppearanceSpan(context, style1),
                    0, content.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (style2 > 0) {
            spannableString.setSpan(new TextAppearanceSpan(context, style2),
                    content.length()-1, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (userTypeface){
            textView.setTypeface(getTypeface());
        }
        textView.setText(spannableString);
    }

    /**
     * 对标题左边添加图片logo
     * @param context
     * @param textView
     * @param content
     * @param drawableRes
     * @param drawBound 图片大小 dp
     */
    public static void setTitleLeftDrawableSpan(final Context context, final TextView textView, final String content, int drawableRes,int drawBound) {
        if (context == null || TextUtils.isEmpty(content) || content.length() < 2) {
            return;
        }
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        int bound = DeviceUtil.dip2px(context, drawBound);
        drawable.setBounds(0, 0, bound, bound);
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable);

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }



    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 给TextView设置图片
     *
     * @param view
     * @param drawable
     * @param ltrb
     */
    public static void setDrawableOfTextView(@NonNull TextView view, @Nullable Drawable drawable, @DrawableDirection int ltrb) {
        if (ltrb == DrawableDirection.LEFT) {
            view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else if (ltrb == DrawableDirection.TOP) {
            view.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else if (ltrb == DrawableDirection.RIGHT) {
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else if (ltrb == DrawableDirection.BOTTOM) {
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
    }

    public static void setDrawableOfTextView(@NonNull TextView view, int drawableRes, @DrawableDirection int ltrb) {
        Drawable drawable = view.getContext().getResources().getDrawable(drawableRes);
        setDrawableOfTextView(view, drawable, ltrb);
    }

    @IntDef({DrawableDirection.LEFT, DrawableDirection.TOP,
            DrawableDirection.RIGHT, DrawableDirection.BOTTOM})
    public @interface DrawableDirection {
        int LEFT = 0x0;
        int TOP = 0x1;
        int RIGHT = 0x2;
        int BOTTOM = 0x3;
    }

    public static void setTextViewFormat(@NonNull final Context context, @NonNull TextView view, int stringRes, Object... args) {
        String txt = context.getResources().getString(stringRes);
        view.setText(String.format(txt, args));
    }


    public static String getIVolume(long volume) {
        if (volume >= 10000) {
            return volume / 10000 + "万+";
        } else {
            return String.valueOf(volume);
        }
    }
    /**
     * @return 转换成可区分 有码 无码的级别
     */
    public static int transFormLevel() {
        UserInfoBean accountInfo = LiZhiApplication.getApplication().getAccountManager().getAccountInfo();
        int roleLevel = accountInfo.getRoleLevel();
        String superiorId = accountInfo.getSuperiorId();
        //roleLevel 角色：1-普通 2-超级 3-Plus超级 4-运营商 5-plus运营商
        int level = 1;
        switch (roleLevel) {
            case 1:
                if (TextUtils.isEmpty(superiorId)) {
                    level = 2;
                } else {
                    level = 3;
                }
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                level = roleLevel + 2;
                break;
            default:
                break;
        }
        //level 文章查看权限会员等级限制  1全部  2普通无码会员  3普通有码会员  4超级会员  5Plus超级会员  6运营商  7Plus运营商',
        return level;
    }

    /**
     * 根据平台 获取图标
     * @param platformType
     * @return
     */
    public static int getIconImg(int platformType) {
        int drawRes;
        switch (platformType) {
            case CommonConst.PLATFORM_TAO_BAO:
                drawRes = com.by.lizhiyoupin.app.R.drawable.temp_detail_tao_big_pic;
                break;
            case CommonConst.PLATFORM_JING_DONG:
                drawRes = com.by.lizhiyoupin.app.R.drawable.temp_detail_jingd_pic;
                break;
            case CommonConst.PLATFORM_PIN_DUO_DUO:
                drawRes = com.by.lizhiyoupin.app.R.drawable.temp_detail_pingdd_big_pic;
                break;
            case CommonConst.PLATFORM_TIAN_MAO:
                drawRes = com.by.lizhiyoupin.app.R.drawable.temp_detail_tian_mao_pic;
                break;
            case CommonConst.PLATFORM_KAO_LA:
                drawRes = com.by.lizhiyoupin.app.R.drawable.temp_detail_kaola_pic;
                break;
            default:
                drawRes = com.by.lizhiyoupin.app.R.drawable.temp_detail_tao_big_pic;
                break;
        }
        return drawRes;
    }
}
