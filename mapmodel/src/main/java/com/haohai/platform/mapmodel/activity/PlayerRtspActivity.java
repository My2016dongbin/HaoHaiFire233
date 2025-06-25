package com.haohai.platform.mapmodel.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.haohai.ledge.videolibrary.GSYVideoManager;
import com.haohai.ledge.videolibrary.listener.GSYSampleCallBack;
import com.haohai.ledge.videolibrary.utils.OrientationUtils;
import com.haohai.ledge.videolibrary.video.MultiSampleVideo;
import com.haohai.ledge.videolibrary.video.StandardGSYVideoPlayer;
import com.haohai.platform.mapmodel.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;

public class PlayerRtspActivity extends BaseActivity {
    private static final String TAG = PlayerRtspActivity.class.getSimpleName();
    private String player_url;
    private StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;
    private String player_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_rtsp);
        Intent intent = getIntent();
        player_url = intent.getStringExtra("PLAYER_URL");
      //  player_url = "rtsp://10.135.49.202/playBack/ba70227e-3020-fff1-2dba-f05d0a9ab3c8-main/1616128392/1616127831.flv?streamType=1&manufacturer=1&startTime=1616127801&endTime=1616127831";
       // player_url = "rtsp://10.135.49.202/playBack/ba70227e-3020-fff1-2dba-f05d0a9ab3c8-main/1613127831.flv?streamType=1&manufacturer=1&startTime=1616127801&endTime=1616127831";
       // player_url = "rtmp://10.135.49.202:1935/live/af7ae7cb-d637-66cb-c1cc-ab86af9cc9af?streamType=2&manufacturer=2";
       // player_url ="rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101";
        player_name = intent.getStringExtra("PLAYER_NAME");
        Log.e(TAG, "onCreate: " + player_url );
        Log.e(TAG, "onCreate: " + "rtsp://10.135.49.202/playBack/ba70227e-3020-fff1-2dba-f05d0a9ab3c8-main/1616128392/1616127831.flv?streamType=1&manufacturer=1&startTime=1616127801&endTime=1616127831");
        Log.e(TAG, "onCreate: " + "rtmp://10.135.49.202/playBack/ba70227e-3020-fff1-2dba-f05d0a9ab3c8-main/1616128392/1616127831.flv?streamType=1&manufacturer=1&startTime=1616127801&endTime=1616127831");
        initView();
    }

    private void initView() {
        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.player_view);
        videoPlayer.setUp(player_url, true, player_name);

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.ic_launcher);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videoPlayer.startPlayLogic();
    }
    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}
