package com.by.lizhiyoupin.app.component_ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.by.lizhiyoupin.app.component_ui.R;

/**
 *
 */
public final class DetailLoadingDialog extends Dialog {
	private ImageView mLoadingView;

	public DetailLoadingDialog(Context context) {
		super(context, R.style.detail_dialog_center_theme);
		setContentView(R.layout.detail_dialog_loading);
		mLoadingView = findViewById(R.id.dialogLoading);
		Glide.with(context).load(R.drawable.loading_dialog_g).into(mLoadingView);
	}
}
