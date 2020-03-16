package com.by.lizhiyoupin.app.jupsh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.io.bean.PushMessageBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/*
* 离线推送消息中转页
*
* */
public class PushActivity extends BaseActivity {
    public static final String TAG=PushActivity.class.getSimpleName();
    public static final String KEY_PUSH_START_EXTRAS = "key_push_start_extras";
    public static final String RECEIVED_ACTION = "received_action";

    /**消息Id**/
    private static final String KEY_MSGID = "msg_id";
    /**该通知的下发通道**/
    private static final String KEY_WHICH_PUSH_SDK = "rom_type";
    /**通知标题**/
    private static final String KEY_TITLE = "n_title";
    /**通知内容**/
    private static final String KEY_CONTENT = "n_content";
    /**通知附加字段**/
    public static final String KEY_EXTRAS = "n_extras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        handleOpenClick();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);
    }
    /**
     * 处理点击事件，当前启动配置的Activity都是使用
     * Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
     * 方式启动，只需要在onCreate中调用此方法进行处理
     */
    private void handleOpenClick() {
        /**
         * getIntent().getData()实例
         *
         * {
         * 	"msg_id":"123456",
         * 	"n_content":"this is content",
         * 	"n_extras":{"key1":"value1","key2":"value2"},
         * 	"n_title":"this is title",
         * 	"rom_type":0
         * }
         */
        String data = null;
        //获取华为平台附带的jpush信息
        if (getIntent().getData() != null) {
            data = getIntent().getData().toString();
        }

        //获取fcm、小米、oppo、vivo平台附带的jpush信息
        if(TextUtils.isEmpty(data) && getIntent().getExtras() != null){
            data = getIntent().getExtras().getString("JMessageExtra");
        }

        Log.w(TAG, "msg content is ===" +  data);
        if (TextUtils.isEmpty(data)){
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(data);
            String msgId = jsonObject.optString(KEY_MSGID);
            byte whichPushSDK = (byte) jsonObject.optInt(KEY_WHICH_PUSH_SDK);
            String title = jsonObject.optString(KEY_TITLE);
            String content = jsonObject.optString(KEY_CONTENT);
            String extras = jsonObject.optString(KEY_EXTRAS);
            StringBuilder sb = new StringBuilder();
            sb.append("msgId:");
            sb.append(String.valueOf(msgId));
            sb.append("\n");
            sb.append("title:");
            sb.append(String.valueOf(title));
            sb.append("\n");
            sb.append("content:");
            sb.append(String.valueOf(content));
            sb.append("\n");
            sb.append("extras:");
            sb.append(String.valueOf(extras));
            sb.append("\n");
            sb.append("platform:");
            sb.append(getPushSDKName(whichPushSDK));

            parceExtras(extras);
            //上报点击事件
            JPushInterface.reportNotificationOpened(this, msgId, whichPushSDK);
        } catch (Exception e) {
            Log.w(TAG, "parse notification error=="+e);
        }

    }
    private void parceExtras(String extra){
        if (extra != null) {
            try {
                PushMessageBean pushMessageBean = new Gson().fromJson(extra, PushMessageBean.class);
                if (LiZhiApplication.getApplication().getInitMain()){
                    final ISchemeManager scheme =
                            (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                    if (scheme != null && pushMessageBean != null) {
                        scheme.handleUrl(this, pushMessageBean.getMsgUrl());
                    }

                }else {
                    Intent intent = new  Intent(this, MainActivity.class);
                    intent.setAction(RECEIVED_ACTION);
                    intent.putExtra(KEY_PUSH_START_EXTRAS,pushMessageBean.getMsgUrl());
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getPushSDKName(byte whichPushSDK) {
        String name;
        switch (whichPushSDK){
            case 0:
                name = "jpush";
                break;
            case 1:
                name = "xiaomi";
                break;
            case 2:
                name = "huawei";
                break;
            case 3:
                name = "meizu";
                break;
            case 4:
                name= "oppo";
                break;
            case 5:
                name = "vivo";
                break;
            case 8:
                name = "fcm";
                break;
            default:
                name = "jpush";
        }
        return name;
    }

}
