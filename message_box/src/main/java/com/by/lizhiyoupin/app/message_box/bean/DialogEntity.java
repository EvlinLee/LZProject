package com.by.lizhiyoupin.app.message_box.bean;


import java.util.List;

import androidx.annotation.Keep;

@SuppressWarnings("All")
@Keep
public class DialogEntity {

    public String title;

    public String titleColor;
    public boolean titleBold;

    public String content;

    public String contentColor;

    public int contentSize=-1;

    public List<ButtonEntity> buttons;

    public String bgColor;

    public MBPadding contentPadding;

    // 按钮栏背景色
    public String bottomBg;

    // 点击对话框外面销毁对话框
    public boolean cancelOnTouchOutside;

}
