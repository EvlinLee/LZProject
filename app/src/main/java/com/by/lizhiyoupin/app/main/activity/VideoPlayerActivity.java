package com.by.lizhiyoupin.app.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.dueeeke.videoplayer.player.VideoView;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/17 17:44
 * Summary: 简单视频播放
 */
public class VideoPlayerActivity extends BaseActivity {

    private VideoView mVideoView;
    private StandardVideoControllerImpl mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_layout);
        initVideoView();
    }

    /**
     * 初始化videoview播放
     */
    public void initVideoView() {
        //初始化VideoView
        mVideoView = findViewById(R.id.videoView);

        mController = new StandardVideoControllerImpl(this);

        ImageView thumb = mController.getThumb();
       // thumb.setImageResource(R.drawable.video_start_play);
        Glide.with(thumb.getContext())
                .load("http://static.lizhiyoupin.com/course/LitchiCourse_IMG_Main.jpg")
                .error(android.R.color.white)
                .placeholder(android.R.color.white)
                .into(thumb);
        //设置网络视频路径
        String url = "http://static.lizhiyoupin.com/course/LitchiCourse.mp4";
        View backButton = mController.getBackButton();
        if (backButton==null){
            return;
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mVideoView.setVideoController(mController);
        mVideoView.setUrl(url);
        mVideoView.startFullScreen();
        mVideoView.start();

    }


    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.resume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.release();
    }
}
