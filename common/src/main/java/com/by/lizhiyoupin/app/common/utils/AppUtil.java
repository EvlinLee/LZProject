/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.by.lizhiyoupin.app.common.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.by.lizhiyoupin.app.common.CommonConst;


public class AppUtil {

    private static String TAG = AppUtil.class.getSimpleName();

    public static final Application INSTANCE;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Log.e(TAG, "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e(TAG, "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            INSTANCE = app;
        }
    }

    private static Activity findActivityFrom(final Context context) {
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof Application || context instanceof Service) return null;
        if (!(context instanceof ContextWrapper)) return null;
        final Context base_context = ((ContextWrapper) context).getBaseContext();
        if (base_context == context) return null;
        return findActivityFrom(base_context);
    }

    public static void startActivity(final Context context, final Intent intent) {
        final Activity activity = findActivityFrom(context);
        if (activity != null) activity.startActivity(intent);
        else context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 获取Application标签内的meta data
     */
    public static String getChannelIDFromManifest(Context appContext) {
        String channel = "";
        // 正常发布版本，渠道号是数字，在这里读到
        if (null == appContext) {
            return "";
        }
        ApplicationInfo appInfo;
        try {
            appInfo = appContext.getPackageManager().getApplicationInfo(
                    appContext.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return channel;
        }

        String ch = appInfo.metaData.getString(CommonConst.KEY_CHANNEL_ID);
        if (TextUtils.isEmpty(ch)) {
            channel = "";
        } else {
            channel = String.valueOf(ch);
        }

        return channel;
    }
}