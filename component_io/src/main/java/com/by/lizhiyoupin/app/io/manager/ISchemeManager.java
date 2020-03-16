package com.by.lizhiyoupin.app.io.manager;

import android.content.Context;
import android.net.Uri;

/**
 * Scheme跳转管理
 *   final ISchemeManager scheme=(ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
 *
 */
public interface ISchemeManager {
    /**
     * 根据url可跳转所有页面
     * @param context
     * @param url
     * @return
     */
    int handleUrl(Context context, String url);

    /**
     * 跳转native页
     * @param context
     * @param uri
     * @return
     */
    int handleLitchiScheme(Context context, Uri uri);

    /**
     * 跳转h5
     * @param context
     * @param url
     * @return
     */
    int handleWebViewUrl(Context context, String url);
}
