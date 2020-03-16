package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.impl.DialogCallBack;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.Slide;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/23 16:35
 * Summary:
 */
public class ToastXViewHolder implements IContentHolder, View.OnClickListener {
    private Context context;
    private MessageBox mMessageBox;
    private TextView messageTv;
    private String mMessage;
    private int mDrawRes;
    @Slide.GravityFlag
    private int ltrb;
    private boolean canClickDismiss;
    private Runnable runnable;
    private DialogCallBack<Object> callBack;
    public ToastXViewHolder(Context context, String message, int drawRes, @Slide.GravityFlag int ltrb, boolean canClickDismiss, DialogCallBack<Object> callBack) {
        this.context = context;
        this.mMessage = message;
        this.mDrawRes = drawRes;
        this.ltrb = ltrb;
        this.canClickDismiss = canClickDismiss;
        this.callBack=callBack;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View root = LayoutInflater.from(context).inflate(R.layout.detail_toast_layout, parent, false);
        initView(root);
        return root;
    }


    private void initView(View root) {
        messageTv = root.findViewById(R.id.message_tv);
        messageTv.setText(mMessage);
        setDrawableOfTextView(messageTv, mDrawRes, ltrb);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mMessageBox != null) {
                    if (callBack!=null){
                        callBack.clickCallback(mMessageBox,"");
                    }else {
                        mMessageBox.dismiss();
                    }

                }
            }
        };
        messageTv.postDelayed(runnable, 3000);
        if (canClickDismiss) {
            root.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        if (mMessageBox != null && messageTv != null) {
            messageTv.removeCallbacks(runnable);
            if (callBack!=null){
                callBack.clickCallback(mMessageBox,"");
            }else {
                mMessageBox.dismiss();
            }
        }
    }

    public static void setDrawableOfTextView(@NonNull TextView view, @Nullable Drawable drawable, @Slide.GravityFlag int ltrb) {
        if (ltrb == Gravity.LEFT || ltrb == Gravity.START) {
            view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else if (ltrb == Gravity.TOP) {
            view.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else if (ltrb == Gravity.RIGHT || ltrb == Gravity.END) {
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else if (ltrb == Gravity.BOTTOM) {
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
    }

    public static void setDrawableOfTextView(@NonNull TextView view, int drawableRes, @Slide.GravityFlag int ltrb) {
        Drawable drawable = view.getContext().getResources().getDrawable(drawableRes);
        setDrawableOfTextView(view, drawable, ltrb);
    }

}
