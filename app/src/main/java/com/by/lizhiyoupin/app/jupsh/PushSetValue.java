package com.by.lizhiyoupin.app.jupsh;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.manager.AccountManager;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * data:2019/12/2
 * author:jyx
 * function:
 */
public class PushSetValue {
    public static void setPushvalue(){
        Context context=LiZhiApplication.getApplication();
        AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        Set<String> tags = new HashSet<String>();
        long id = accountManager.getAccountInfo().getId();
        tags.add(String.valueOf(id));
        int roleLevel = accountManager.getAccountInfo().getRoleLevel();
        if (roleLevel==1){
            if (TextUtils.isEmpty(accountManager.getAccountInfo().getSuperiorId())){
                tags.add("v0");
            }else{
                tags.add("v1");
            }

        }else{
            tags.add("v"+roleLevel);
            Log.i("tag",roleLevel+"");
        }
        JPushInterface.setTags(context,10,tags);//每次调用设置有效的别名，覆盖之前的设置
        Log.i("id===",id+"");
        JPushInterface.setAlias(context,9,String.valueOf(id));//每次调用设置有效的别名，覆盖之前的设置
    }

    public static void setUnbindPush(){
        //清楚tag 和 Alias
        Context context=LiZhiApplication.getApplication();
        JPushInterface.cleanTags(context,8);
        JPushInterface.setAlias(context,7,"0");
    }
}
