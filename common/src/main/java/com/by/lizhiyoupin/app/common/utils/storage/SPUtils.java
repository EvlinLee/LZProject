package com.by.lizhiyoupin.app.common.utils.storage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.FileUtil;
import com.by.lizhiyoupin.app.common.utils.storage.sd.SharedPreferencesStorageImpl;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.core.content.ContextCompat;

public class SPUtils {
    private static final String TAG = SPUtils.class.getSimpleName();
    private static final boolean DEBUG = false;
    private static Context context;
    private static final Map<String, PrefsProxy> PREFS_CACHE = new ConcurrentHashMap<>();
    private static String defaultPrefName;
    //内置sp
    private static PrefsProxy defaultPrefs;
    //外置sd
    private static PrefsProxy storagePrefs;
    //无读写权限，内存缓存
    private static PrefsProxy storageNoPermissionPrefs;

    public static void initDefault(Context context, String defaultPrefName) {
        SPUtils.context = context.getApplicationContext();
        if (!TextUtils.equals(SPUtils.defaultPrefName, defaultPrefName)) {
            SPUtils.defaultPrefName = defaultPrefName;
            defaultPrefs = null;
        }
    }

    /**
     *  默认sp存储
     * @return
     */
    public static PrefsProxy getDefault() {
        if (defaultPrefs == null) {
            if (context == null) {
                throw new RuntimeException("使用 SPUtils，必须先调用 #initDefault 方法");
            }
            defaultPrefs = new PrefsProxy(context.getSharedPreferences(defaultPrefName, Context.MODE_PRIVATE));
        }
        return defaultPrefs;
    }

    public static PrefsProxy getPreferences(String name) {
        PrefsProxy prefs = PREFS_CACHE.get(name);
        if (prefs == null) {
            if (context == null) {
                throw new RuntimeException("使用 SPUtils，必须先调用 #initDefault 方法");
            }
            prefs = new PrefsProxy(context.getSharedPreferences(name, Context.MODE_PRIVATE));
            PREFS_CACHE.put(name, prefs);
        }
        return prefs;
    }

    /**
     * 存储于 外置sd
     * @return
     */
    public static PrefsProxy getStoragePreferences() {
        if (context == null) {
            throw new RuntimeException("使用 SPUtils，必须先调用 #initDefault 方法");
        }
        PrefsProxy proxy;
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (DEBUG) {
                LZLog.w(TAG, "SPUtils no permission, provide no operate class");
            }
            if (storageNoPermissionPrefs == null) {
                storageNoPermissionPrefs = new PrefsProxy(new EmptyStoragePreference());
            }
            proxy = storageNoPermissionPrefs;
        }else {
            if (storagePrefs == null) {
                String path = FileUtil.getExternalStorageDirectory() + "/zlyp/files/" + "storage_sp.xml";
                File file = new File(path);
                storagePrefs = new PrefsProxy(new SharedPreferencesStorageImpl(file, Context.MODE_PRIVATE));
            }
            proxy = storagePrefs;
        }
        return proxy;
    }

    /**
     * 清空内存缓存
     */
    public static void clearCache() {
        PREFS_CACHE.clear();
        defaultPrefs = null;
    }

    /**
     * 清空文件
     */
    public static void clear() {
        if (defaultPrefs != null) {
            defaultPrefs.clear();
        }
        for (PrefsProxy p : PREFS_CACHE.values()) {
            p.clear();
        }
    }
}
