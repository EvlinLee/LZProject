package com.by.lizhiyoupin.app.common.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/16 19:27
 * Summary:
 */
public class FixMemLeak {

    private static Field field;
    private static boolean hasField = true;

    /**
     * 修复华为手机 InputMethodManager 泄漏
     * @param context
     */
    public static void fixHuaweiLeak(Context context) {
        if (!hasField) {
            return;
        }
        if (OSUtils.getRomType()!= OSUtils.ROM.EMUI){
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mLastSrvView"};
        for (String param : arr) {
            try {
                if (field == null) {
                    field = imm.getClass().getDeclaredField(param);
                }
                if (field == null) {
                    hasField = false;
                }
                if (field != null) {
                    field.setAccessible(true);
                    field.set(imm, null);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
