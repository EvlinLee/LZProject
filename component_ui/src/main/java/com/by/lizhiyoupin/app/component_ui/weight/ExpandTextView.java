package com.by.lizhiyoupin.app.component_ui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/1/30 09:47
 * Summary:可折叠TextView   宽不能 wrap_content
 */
public class ExpandTextView extends AppCompatTextView implements View.OnClickListener, View.OnLongClickListener {
    public static final String TAG = ExpandTextView.class.getSimpleName();
    /**
     * 末尾省略号
     */
    private static final String ELLIPSE = "...";
    /**
     * 默认的折叠行数
     */
    public static final int COLLAPSED_LINES = 4;
    /**
     * 折叠时的默认文本
     */
    private static final String EXPANDED_TEXT = "展开";
    /**
     * 展开时的默认文本
     */
    private static final String COLLAPSED_TEXT = "收起";
    /**
     * 在文本末尾
     */
    public static final int END = 0;
    /**
     * 在文本下方
     */
    public static final int BOTTOM = 1;
    /**
     * 在文本右下方
     */
    public static final int BOTTOM_AND_END = 2;

    /**
     * 提示文字展示的位置
     */
    @IntDef({END, BOTTOM, BOTTOM_AND_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TipsGravityMode {
    }

    /**
     * 折叠后显示的行数
     */
    private int mCollapsedLines;
    /**
     * 是否 显示原始样式（false表示采用折叠 出现 ”展示“ ”收起“ 字样），
     * true直接以原始TextView样式显示
     * 默认false
     * mCollapsedLines为0
     */
    private boolean mNormalTextView;
    /**
     * 加粗字体
     */
    private boolean boldText = false;
    private StyleSpan expandBoldSpan = new StyleSpan(Typeface.BOLD);
    private StyleSpan collapsedBoldSpan = new StyleSpan(Typeface.BOLD);
    /**
     * 折叠时的提示文本
     */
    private String mExpandedText;
    /**
     * 展开时的提示文本
     */
    private String mCollapsedText;
    /**
     * 折叠时的图片资源
     */
    private Drawable mExpandedDrawable;
    /**
     * 展开时的图片资源
     */
    private Drawable mCollapsedDrawable;
    /**
     * 原始的文本
     */
    private CharSequence mOriginalText;
    /**
     * TextView中文字可显示的宽度
     */
    private int mShowWidth;
    /**
     * 是否是展开的
     */
    private boolean mIsExpanded = false;
    /**
     * 提示文字位置 (设置里这个则 mExpandTipsGravity和mCloseTipsGravity无效)
     */
    private int mTipsGravity;
    /**
     * "展开"  的位置
     */
    private int mExpandTipsGravity;
    /**
     * "收起"  的位置
     */
    private int mCloseTipsGravity;
    /**
     * 提示文字颜色
     */
    private int mTipsColor;
    /**
     * 提示文字是否显示下划线
     */
    private boolean mTipsUnderline;
    /**
     * 提示是否可点击
     */
    private boolean mTipsClickable;
    /**
     * 提示文本的点击事件
     */
    private ExpandedClickableSpan mClickableSpan = new ExpandedClickableSpan();
    private ViewTreeObserver.OnPreDrawListener preDrawListener;
    private BufferType pendingType;
    private CharSequence pendingText;
    /**
     * TextView的点击事件监听
     */
    private OnClickListener mListener;
    private OnSpanTipsClickListener mOnSpanTipsClickListener;
    private OnLongClickListener mLongClickListener;
    /**
     * 是否响应TextView的点击事件
     */
    private boolean mIsResponseListener = true;
    /**
     * 收起/展开的监听
     */
    private ExpandListener mExpandListener;
    /**
     * 当前的设置文本是否需要折叠显示
     */
    private boolean mIsOverCollapse = false;
    public Boolean originalTextOverCollapse = null;

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        // 使点击有效
        setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * 初始化属性
     */
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView);
            mCollapsedLines = typed.getInt(R.styleable.ExpandTextView_collapsedLines, COLLAPSED_LINES);
            mNormalTextView = typed.getBoolean(R.styleable.ExpandTextView_normalTextView, false);
            setExpandedText(typed.getString(R.styleable.ExpandTextView_expandedText));
            setCollapsedText(typed.getString(R.styleable.ExpandTextView_collapsedText));
            setExpandedDrawable(typed.getDrawable(R.styleable.ExpandTextView_expandedDrawable));
            setCollapsedDrawable(typed.getDrawable(R.styleable.ExpandTextView_collapsedDrawable));
            mTipsGravity = typed.getInt(R.styleable.ExpandTextView_tipsGravity, END);
            mExpandTipsGravity = typed.getInt(R.styleable.ExpandTextView_expandTipsGravity, END);
            mCloseTipsGravity = typed.getInt(R.styleable.ExpandTextView_closeTipsGravity, END);
            mTipsColor = typed.getColor(R.styleable.ExpandTextView_tipsColor, 0);
            mTipsUnderline = typed.getBoolean(R.styleable.ExpandTextView_tipsUnderline, false);
            mTipsClickable = typed.getBoolean(R.styleable.ExpandTextView_tipsClickable, true);
            typed.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置折叠行数
     *
     * @param collapsedLines 折叠行数
     */
    public void setCollapsedLines(@IntRange(from = 0) int collapsedLines) {
        this.mCollapsedLines = collapsedLines;
    }

    /**
     * 设置折叠时的提示文本
     *
     * @param expandedText 提示文本
     */
    public void setExpandedText(String expandedText) {
        this.mExpandedText = TextUtils.isEmpty(expandedText) ? EXPANDED_TEXT : expandedText;
    }

    /**
     * 设置字体 粗体
     *
     * @param boldText
     */
    public void setBoldText(boolean boldText) {
        this.boldText = boldText;
    }

    /**
     * 设置展开时的提示文本
     *
     * @param collapsedText 提示文本
     */
    public void setCollapsedText(String collapsedText) {
        this.mCollapsedText = TextUtils.isEmpty(collapsedText) ? COLLAPSED_TEXT : collapsedText;
    }

    /**
     * 设置折叠时的提示图片
     *
     * @param resId 图片资源
     */
    public void setExpandedDrawableRes(@DrawableRes int resId) {
        setExpandedDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    /**
     * 设置折叠时的提示图片
     *
     * @param expandedDrawable 图片
     */
    public void setExpandedDrawable(Drawable expandedDrawable) {
        if (expandedDrawable != null) {
            this.mExpandedDrawable = expandedDrawable;
            this.mExpandedDrawable.setBounds(0, 0, mExpandedDrawable.getIntrinsicWidth(), mExpandedDrawable.getIntrinsicHeight());
        }
    }

    /**
     * 设置展开时的提示图片
     *
     * @param resId 图片资源
     */
    public void setCollapsedDrawableRes(@DrawableRes int resId) {
        setCollapsedDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    /**
     * 设置展开时的提示图片
     *
     * @param collapsedDrawable 图片
     */
    public void setCollapsedDrawable(Drawable collapsedDrawable) {
        if (collapsedDrawable != null) {
            this.mCollapsedDrawable = collapsedDrawable;
            this.mCollapsedDrawable.setBounds(0, 0, mCollapsedDrawable.getIntrinsicWidth(), mCollapsedDrawable.getIntrinsicHeight());
        }
    }

    /**
     * 设置提示的位置
     * mTipsGravity设置后mExpandTipsGravity和mCloseTipsGravity无效
     *
     * @param tipsGravity END 表示在文字末尾，BOTTOM 表示在文字下方
     */
    public void setTipsGravity(@TipsGravityMode int tipsGravity) {
        this.mTipsGravity = tipsGravity;
    }

    public void setExpandTipsGravity(@TipsGravityMode int tipsGravity) {
        this.mExpandTipsGravity = tipsGravity;
    }

    public void setCloseTipsGravity(@TipsGravityMode int tipsGravity) {
        this.mCloseTipsGravity = tipsGravity;
    }

    /**
     * 设置文字提示的颜色
     *
     * @param tipsColor 颜色
     */
    public void setTipsColor(@ColorInt int tipsColor) {
        this.mTipsColor = tipsColor;
    }

    /**
     * 设置提示文字是否有下划线
     *
     * @param tipsUnderline true 表示有下划线
     */
    public void setTipsUnderline(boolean tipsUnderline) {
        this.mTipsUnderline = tipsUnderline;
    }

    /**
     * 设置提示文字是否可点击
     *
     * @param tipsClickable true 表示可点击
     */
    public void setTipsClickable(boolean tipsClickable) {
        this.mTipsClickable = tipsClickable;
    }

    /**
     * 展开 ，收齐
     * 用于TextView的点击事件设置展开收起
     */
    public void setInverseIsExpanded() {
        mIsExpanded = !mIsExpanded;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpandNotRefresh(boolean expand){
        this.mIsExpanded = expand;
    }

    /**
     * 展开
     */
    public void expand() {
        if (mIsOverCollapse) {
            mIsExpanded = true;
            setText(mOriginalText);
        }
    }

    /**
     * 收起
     */
    public void collapse() {
        if (mIsOverCollapse) {
            mIsExpanded = false;
            setText(mOriginalText);
        }
    }

    public boolean isOriginalOverCollapse() {
        return originalTextOverCollapse == null ? false : originalTextOverCollapse;
    }

    public void clearOriginalOverCollapse() {
        originalTextOverCollapse = null;
    }

    /**
     * 是否作为普通TextView 类似 mCollapsedLines为0
     *
     * @param show
     */
    public void setNormalTextView(boolean show) {
        mNormalTextView = show;
    }

    //因为setText(CharSequence text)方法是final的，
//并且setText(CharSequence text)最终调用的也是setText(CharSequence text, BufferType type)方法，
    @Override
    public void setText(final CharSequence text, final BufferType type) {
        pendingType = type;
        pendingText = text;
        // 如果text为空或mCollapsedLines为0,或作为普通TextView则直接显示
        if (TextUtils.isEmpty(text) || mCollapsedLines == 0 || mNormalTextView) {
            if (mExpandListener != null) {
                mExpandListener.onCalculateCollapse(false);
            }
            super.setText(text, type);
        } else if (mIsExpanded) {
            // 保存原始文本，去掉文本末尾的空字符
            this.mOriginalText = trimFrom(text);
            formatExpandedText(type);
            if (mExpandListener != null) {
                mExpandListener.onExpand();
            }
        } else {
            // 获取TextView中文字显示的宽度，需要在layout之后才能获取到，避免重复获取
            if (mShowWidth == 0 || getLayout() == null) {
                if (ViewCompat.isAttachedToWindow(this)) {
                    ensurePreDrawListener();
                    getViewTreeObserver().addOnPreDrawListener(preDrawListener);
                } else {
                    addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {
                            if (mShowWidth == 0 || getLayout() == null) {
                                ensurePreDrawListener();
                                getViewTreeObserver().addOnPreDrawListener(preDrawListener);
                            }
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                        }
                    });
                }
            } else {
                formatCollapsedText(type, text);
            }
        }
        if(mIsExpanded && getLineCount() <= mCollapsedLines){
            originalTextOverCollapse = false;
        }
        if(getLineCount() > mCollapsedLines){
            originalTextOverCollapse = true;
        }
    }

    private void ensurePreDrawListener() {
        if (preDrawListener == null) {
            preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                    if (mShowWidth != 0 && getLayout() != null) {
                        return true;
                    }
                    mShowWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
                    if (mIsExpanded) {
                        formatExpandedText(pendingType);
                    } else {
                        formatCollapsedText(pendingType, pendingText);
                    }
                    return false;
                }
            };
        }
    }

    /**
     * 格式化折叠时的文本
     *
     * @param type ref android.R.styleable#TextView_bufferType
     */
    private void formatCollapsedText(BufferType type, CharSequence text) {
        CharSequence oldOriginalText = mOriginalText;
        // 保存原始文本，去掉文本末尾的空字符
        this.mOriginalText = trimFrom(text);
        if (!TextUtils.equals(oldOriginalText, mOriginalText)) {
            originalTextOverCollapse = null;
        }
        // 获取 layout，用于计算行数
        Layout layout = getLayout();
        // 调用 setText 用于重置 Layout
        if (layout == null || !layout.getText().equals(mOriginalText)) {
            super.setText(mOriginalText, type);
            layout = getLayout();
        }
        // 获取 paint，用于计算文字宽度
        TextPaint paint = getPaint();
        int line = 1;
        if (null != layout) {
            line = layout.getLineCount();
        }
        if (line <= mCollapsedLines) {
            mIsOverCollapse = false;
            if (null != layout && originalTextOverCollapse == null) {
                originalTextOverCollapse = false;
            }
            if (boldText) {
                setTypeface(Typeface.DEFAULT_BOLD);
            }
            super.setText(mOriginalText, type);
        } else {
            mIsOverCollapse = true;
            if (null != layout && originalTextOverCollapse == null) {
                originalTextOverCollapse = true;
            }
            // 最后一行的开始字符位置
            int lastLineStart = layout.getLineStart(mCollapsedLines - 1);
            // 最后一行的结束字符位置
            int lastLineEnd = layout.getLineVisibleEnd(mCollapsedLines - 1);
            // 计算后缀的宽度
            int expandedTextWidth = 0;
            if (mTipsGravity == END) {
                //由于实际宽度与这个测量字的宽度可能不一致（不同字宽度不同），这里多测量一个字距离
                expandedTextWidth = (int) paint.measureText(ELLIPSE + " " + mExpandedText + "空");
            } else if (mTipsGravity == BOTTOM) {
                expandedTextWidth = (int) paint.measureText(ELLIPSE + " ");
            } else {
                expandedTextWidth = (int) paint.measureText(ELLIPSE + " " + mExpandedText + "空");

            }
            // 最后一行的宽
            float lastLineWidth = layout.getLineWidth(mCollapsedLines - 1);
            // 如果大于屏幕宽度则需要减去部分字符,使 后缀“...展开"能在这行显示全
            if (lastLineWidth + expandedTextWidth > mShowWidth) {
                int cutCount = paint.breakText(mOriginalText, lastLineStart, lastLineEnd, false, expandedTextWidth, null);
                lastLineEnd -= cutCount;
            }
            // 因设置的文本可能是带有样式的文本，如SpannableStringBuilder，所以根据计算的字符数从原始文本中截取
            SpannableStringBuilder spannable = new SpannableStringBuilder();
            // 截取文本，还是因为原始文本的样式原因不能直接使用paragraphs中的文本
            CharSequence ellipsizeText = mOriginalText.subSequence(0, lastLineEnd);
            spannable.append(ellipsizeText);
            if (boldText) {
                spannable.setSpan(collapsedBoldSpan, 0, ellipsizeText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            spannable.append(ELLIPSE);
            // 设置样式
            setSpan(spannable);
            super.setText(spannable, type);
            if (mExpandListener != null) {
                mExpandListener.onCollapse();
            }
        }
        if (mExpandListener != null) {
            mExpandListener.onCalculateCollapse(mIsOverCollapse);
        }
        LZLog.e("raytest", hashCode() + ":"+originalTextOverCollapse);
    }

    /**
     * 格式化展开式的文本，直接在后面拼接即可
     *
     * @param type
     */
    private void formatExpandedText(BufferType type) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(mOriginalText);
        if (boldText) {

            spannable.setSpan(expandBoldSpan, 0, mOriginalText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        setSpan(spannable);
        super.setText(spannable, type);
    }

    /**
     * 设置提示的样式
     *
     * @param spannable 需修改样式的文本
     */
    private void setSpan(SpannableStringBuilder spannable) {
        Drawable drawable;
        // 根据提示文本需要展示的文字拼接不同的字符
        if (mTipsGravity == END) {
            spannable.append(" ");
        } else {
            spannable.append("\n");
        }
        int tipsLen;
        // 判断是展开还是收起
        if (mIsExpanded) {
            spannable.append(mCollapsedText);
            drawable = mCollapsedDrawable;
            tipsLen = mCollapsedText.length();
        } else {
            spannable.append(mExpandedText);
            drawable = mExpandedDrawable;
            tipsLen = mExpandedText.length();
        }
        // 设置点击事件
        spannable.setSpan(mClickableSpan, spannable.length() - tipsLen,
                spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        // 如果提示的图片资源不为空，则使用图片代替提示文本
        if (drawable != null) {
            spannable.setSpan(new CenterAlignImageSpan(drawable, ImageSpan.ALIGN_BASELINE),
                    spannable.length() - tipsLen, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.append(" ");
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        // 保存TextView的点击监听事件
        this.mListener = l;
        super.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!mIsResponseListener) {
            // 如果不响应TextView的点击事件，将属性置为true
            mIsResponseListener = true;
        } else if (mListener != null) {
            // 如果响应TextView的点击事件且监听不为空，则响应点击事件
            mListener.onClick(v);
        }
    }

    @Override
    public void setOnLongClickListener(@Nullable View.OnLongClickListener l) {
        this.mLongClickListener = l;
        super.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        if (mLongClickListener != null) {
            return mLongClickListener.onLongClick(v);
        } else {
            return false;
        }
    }


    /**
     * 提示的点击事件
     */
    private class ExpandedClickableSpan extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            // 是否可点击
            if (mTipsClickable) {
                if (mOnSpanTipsClickListener != null) {
                    mOnSpanTipsClickListener.beforeSpanTipsClick(widget, mIsExpanded);
                }

                mIsResponseListener = false;
                mIsExpanded = !mIsExpanded;
                setText(mOriginalText);

                if (mOnSpanTipsClickListener != null) {
                    mOnSpanTipsClickListener.afterSpanTipsClick(widget, mIsExpanded);
                }
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // 设置提示文本的颜色和是否需要下划线
            ds.setColor(mTipsColor == 0 ? ds.linkColor : mTipsColor);
            ds.setUnderlineText(mTipsUnderline);
        }
    }


    /**
     * 去掉空白字符，这里只去掉了尾部的空白字符
     *
     * @param sequence 需处理的字符
     * @return 处理过的字符
     */
    public static CharSequence trimFrom(CharSequence sequence) {
        int len = sequence.length();
        int first = 0;
        int last;
//        for (first = 0; first < len; first++) {
//            if (!matches(sequence.charAt(first))) {
//                break;
//            }
//        }
        for (last = len - 1; last > first; last--) {
            if (!matches(sequence.charAt(last))) {
                break;
            }
        }

        return sequence.subSequence(first, last + 1);
    }

    private static boolean matches(char c) {
        switch (c) {
            case '\t':
            case '\n':
            case '\013':
            case '\f':
            case '\r':
            case ' ':
            case '\u0085':
            case '\u1680':
            case '\u2028':
            case '\u2029':
            case '\u205f':
            case '\u3000':
                return true;
            case '\u2007':
                return false;
            default:
                return c >= '\u2000' && c <= '\u200a';
        }
    }

    /**
     * 对 复文本 提示按钮（"展开" "收起" 或图片）的点击后监听
     *
     * @param l
     */
    public void setOnSpanTipsClickListener(OnSpanTipsClickListener l) {
        this.mOnSpanTipsClickListener = l;
    }

    public interface OnSpanTipsClickListener {
        /**
         * 针对富文本点击完成后，回调
         *
         * @param v
         * @param isExpanded 返回点击操做生效后 展开或收起
         */
        void afterSpanTipsClick(View v, boolean isExpanded);

        /**
         * 针对富文本点击操作前，回调
         *
         * @param v
         * @param isExpanded
         */
        void beforeSpanTipsClick(View v, boolean isExpanded);
    }

    /**
     * 对 textView setText展开 收起 操作的回调
     *
     * @param listener
     */
    public void setExpandListener(ExpandListener listener) {
        this.mExpandListener = listener;
    }

    public interface ExpandListener {
        void onExpand();

        void onCollapse();

        void onCalculateCollapse(boolean overCollapse);
    }
}