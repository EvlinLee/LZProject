package com.by.lizhiyoupin.app.common.utils;


import java.util.ArrayList;
import java.util.List;


public final class ArraysUtils {

    public static void arrayToList(String[] array, ArrayList<String> list) {
        for (String unicode : array) {
            list.add(unicode);
        }
    }

    public static boolean isListEmpty(List list) {
        return null == list || list.size() == 0;
    }
}
