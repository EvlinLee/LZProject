package com.by.lizhiyoupin.app.common.utils;

import android.text.TextUtils;
import android.util.Base64;

/**
 *
 * Base64编解码
 */
public final class NativeBase64Utils {

    public static String encode(final byte[] data) {
        if (data != null && data.length != 0) {
            return Base64.encodeToString(data, Base64.NO_WRAP);
        }
        return "";
    }

    public static byte[] encodeByte(final String data) {
        if (data != null && !TextUtils.isEmpty(data)) {
            return Base64.encode(data.getBytes(), Base64.NO_WRAP);
        }
        return null;
    }

    public static String encodeOnHttp(final byte[] data) {
        String encodeString = encode(data);
        encodeString = encodeString.replaceAll("=", ".");
        encodeString = encodeString.replaceAll("\\+", "!");
        encodeString = encodeString.replaceAll("/", "-");
        return encodeString;
    }

    public static byte[] decode(final String src) {
        return Base64.decode(src, Base64.DEFAULT);
    }

    public static byte[] decodeByte(final byte[] src) {
        return Base64.decode(src, Base64.DEFAULT);
    }
}
