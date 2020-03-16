package com.by.lizhiyoupin.app.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.hjq.toast.ToastUtils;

/**
 * 吐司
 */
public final class CommonToast {

	private CommonToast() {
	}

	public static void showToast(final String text) {
		ToastUtils.show(text);
		/*final Context context = ContextHolder.getInstance().getContext();
		if (context == null) {
			return;
		}

		if (ThreadUtils.isMainThread()) {
			showToastOnUi(context, text);
		} else {
			ThreadUtils.runOnUiThread(() -> showToastOnUi(context, text));
		}*/
	}

	public static void showToast(final int strId) {
		ToastUtils.show(strId);
		/*final Context context = ContextHolder.getInstance().getContext();
		if (context == null) {
			return;
		}
		showToast(context.getString(strId));*/
	}

	public static void showToastOnUi(final Context context, final String text) {
		if (!TextUtils.isEmpty(text))
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
