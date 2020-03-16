package com.by.lizhiyoupin.app.io.entity;

import android.text.TextUtils;

import java.io.File;

public class ShareParams {
	public static final int TYPE_NATIVE_DIALOG=99;//99表示本地需要弹框不自动执行分享操作
	public static final int TYPE_PIC_URL = 0;//图片地址url
	public static final int TYPE_FILE = 1;//文本文件
	public static final int TYPE_H5_URL = 2;//h5链接
	public static final int TYPE_TEXT = 3;//文字

	public int shareType;
	public String title;
	public String msg;
	public String url;
	public String imageUrl;
	public File file;
	public File shareFile;
	public String shareTitle;

	public ShareParams(int shareType) {
		this.shareType = shareType;
	}

	public ShareParams() {
		this(TYPE_PIC_URL);
	}

	public boolean checkParams() {
		switch (shareType) {
			case TYPE_FILE:
				return file != null;
			case TYPE_H5_URL:
				return false;
			default:
				return !TextUtils.isEmpty(url);
		}
	}

	public boolean putFile(File f) {
		if(f != null && f.exists()) {
			file = f;
			shareType = TYPE_FILE;
			return true;
		}
		return false;
	}

	public boolean putFileByPath(String path) {
		return putFile(new File(path));
	}

	public static ShareParams createShareParams(String title, String msg, String url, String imageUrl, String shareTitle) {
		final ShareParams params = new ShareParams();
		params.title = title == null ? "" : title;
		params.msg = msg;
		params.url = url;
		params.imageUrl = imageUrl;
		params.shareTitle = shareTitle;
		return params;
	}

	public static ShareParams createShareParams(String title, String msg, String url, String imageUrl) {
		final ShareParams params = new ShareParams();
		params.title = title == null ? "" : title;
		params.msg = msg;
		params.url = url;
		params.imageUrl = imageUrl;
		return params;
	}
}
