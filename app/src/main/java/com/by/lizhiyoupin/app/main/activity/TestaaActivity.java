package com.by.lizhiyoupin.app.main.activity;

import android.os.Bundle;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.weight.HomeHeaderLayout;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/30 12:00
 * Summary: 测试
 */
public class TestaaActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceUtil.fullScreen(this);
        setContentView(R.layout.activity_testsa_layout);
        RecyclerView recyclerView = findViewById(R.id.rv);
        HomeHeaderLayout d=findViewById(R.id.sds);
        d.setFullScreen();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public void init(){
        UMImage image = new UMImage(this, "https://img.alicdn.com/imgextra/i1/725677994/O1CN0128vIcq7lUdwzfe8_!!725677994.jpg");//网络图片
        image.setThumb(new UMImage(this,R.drawable.logo_lz));
        new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(null)
                .withText("微信").withMedia(image).share();
             /*   new ShareAction(getActivity()).withText("hello").withMedia(image).share();
                final UMWeb web = new UMWeb("https://blog.csdn.net/weixin_37651459/article/details/80956366");
                web.setTitle("页面");
                web.setThumb(image);
                web.setDescription("ddd");
                new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(null)
                        .withText("test").withMedia(web).share();*/
    }
}
