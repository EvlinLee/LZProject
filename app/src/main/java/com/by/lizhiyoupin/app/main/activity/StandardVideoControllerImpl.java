package com.by.lizhiyoupin.app.main.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.dueeeke.videocontroller.StandardVideoController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/18 11:36
 * Summary:
 */
public class StandardVideoControllerImpl extends StandardVideoController {

    public StandardVideoControllerImpl(@NonNull Context context) {
        this(context,null);
    }

    public StandardVideoControllerImpl(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StandardVideoControllerImpl(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    public View getBackButton(){
        return  mBackButton;
    }


}
