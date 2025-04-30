package com.haohai.platform.mapmodel.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.haohai.ledge.videolibrary.GSYVideoManager;
import com.haohai.ledge.videolibrary.utils.OrientationUtils;
import com.haohai.ledge.videolibrary.video.MultiSampleVideo;
import com.haohai.ledge.videolibrary.video.StandardGSYVideoPlayer;
import com.haohai.platform.mapmodel.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ywl5320.wlmedia.WlMedia;
import com.ywl5320.wlmedia.enums.WlComplete;
import com.ywl5320.wlmedia.listener.WlOnMediaInfoListener;
import com.ywl5320.wlmedia.listener.WlOnVideoViewListener;
import com.ywl5320.wlmedia.surface.WlSurfaceView;
import com.ywl5320.wlmedia.widget.WlCircleLoadView;

public class PlayerActivity extends BaseActivity {


    private String player_url;
//    private StandardGSYVideoPlayer videoPlayer;
//    private OrientationUtils orientationUtils;
    private String player_name;
    private WlSurfaceView video1Player;
    private WlMedia wlMedia1;
    private WlCircleLoadView wlCircleLoadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        player_url = intent.getStringExtra("PLAYER_URL");
        player_name = intent.getStringExtra("PLAYER_NAME");
        initView();
    }


    private void initView() {
        /*videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.player_view);
        videoPlayer.setUpLazy(player_url, false, null, null, player_name);

        //增加封面
        ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.mipmap.ic_player);
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
        videoPlayer.startPlayLogic();*/


        android.util.Log.e("PlayerActivity", "initView: player_url = " +  player_url);

        video1Player = findViewById(R.id.video1_player2);
        wlCircleLoadView = findViewById(R.id.circleview);
        wlMedia1 = new WlMedia();
        wlMedia1.setFFOptions("rtsp_transport", "tcp");
        wlMedia1.setFFOptions("fflags", "nobuffer");
        wlMedia1.setSource(player_url);
        video1Player.setWlMedia(wlMedia1);

        wlMedia1.setOnMediaInfoListener(new WlOnMediaInfoListener() {
            @Override
            public void onPrepared() {
                //异步准备好后开始播放
                wlMedia1.start();
                Log.e("look", "onPrepared: 11111");
            }

            @Override
            public void onError(int code, String msg) {
                //错误回调，主要用于查看错误信息
                Log.e("look", "onError: "+msg+"+"+code);
            }

            @Override
            public void onComplete(WlComplete type, String msg) {
                //播放完成（包含：正常播放完成、超时播放完成、手动触发播放完成等）
                Log.e("look", "onComplete: "+type+"+"+msg );
                if (type.toString().equals("WL_COMPLETE_TIMEOUT")||type.toString().equals("WL_COMPLETE_EOF")){
                    Toast.makeText(PlayerActivity.this, "当前网络不佳，尝试重新播放中", Toast.LENGTH_SHORT).show();
                    wlMedia1.prepared();
                }
            }

            @Override
            public void onTimeInfo(double currentTime, double bufferTime) {
                //时间回调，当前时间和缓冲时间
                Log.e("look", "onTimeInfo: ");
            }

            @Override
            public void onSeekFinish() {
                //seek完成后回调，可用于类似iptv这种快进快退
                Log.e("look", "onSeekFinish: ");
            }

            @Override
            public void onLoopPlay(int loopCount) {
                //循环播放此时回调
                Log.e("look", "onLoopPlay: ");
            }

            @Override
            public void onLoad(boolean load) {
                Log.e("look", "onLoad: load " + load);
                //加载状态回调
                if(load)
                {
                    wlCircleLoadView.setVisibility(View.VISIBLE);
                }
                else{
                    wlCircleLoadView.setVisibility(View.GONE);
                }
            }

            @Override
            public byte[] decryptBuffer(byte[] encryptBuffer) {
                return new byte[0];
            }

            @Override
            public byte[] readBuffer(int read_size) {
                return new byte[0];
            }

            @Override
            public void onPause(boolean pause) {
                //暂停回调
            }
        });

        video1Player.setOnVideoViewListener(new WlOnVideoViewListener() {
            @Override
            public void initSuccess() {
                wlMedia1.prepared();
            }

            @Override
            public void onSurfaceChange(int width, int height)
            {
            }

            @Override
            public void moveX(double value, int move_type) {

            }

            @Override
            public void onSingleClick() {

            }

            @Override
            public void onDoubleClick() {

            }

            @Override
            public void moveLeft(double value, int move_type) {

            }

            @Override
            public void moveRight(double value, int move_type) {

            }
        });


        wlMedia1.prepared();
    }
    @Override
    protected void onPause() {
        super.onPause();
        /*videoPlayer.onVideoPause();*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        wlMedia1.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*videoPlayer.onVideoResume();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();*/
    }

    @Override
    public void onBackPressed() {
       /* //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);*/
        super.onBackPressed();
    }
}
