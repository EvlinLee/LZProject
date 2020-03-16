package com.by.lizhiyoupin.app.message_box.bean;


import android.view.Gravity;

import androidx.annotation.Keep;

/**
 * 按钮样式
 */
@SuppressWarnings("All")
@Keep
public class ButtonEntity {

    public String value;

    public String color;

    public String bgColor;

    public int width;

    public int height;

    public boolean enable = true;

    public boolean visable = true;

    public int gravity = Gravity.CENTER;

}
