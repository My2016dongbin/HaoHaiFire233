package com.haohai.platform.fireforestplatform.ui.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.haohai.platform.fireforestplatform.R;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;

import java.util.Objects;


public class BackgroundMp3Service extends Service {
    private static final String TAG = BackgroundMp3Service.class.getSimpleName();
    private String messageWeb;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.e(TAG, "handleMessage: 播放完毕");
                    BackgroundMp3Service.this.onDestroy();
                    break;
            }
        }
    };
    private String type;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate:");
    }

    private void startVoice() {
     //   Log.e(TAG, "onCreate:  messageWeb = " + messageWeb);
        Log.e(TAG, "startVoice: ");
        //接下来根据messageWeb分类通知语音播报

      /*  //方案二：网络音频
        String stringExtra = "http://zjlt.sc.chinaz.com/Files/DownLoad/sound1/201511/6553.mp3";
        Uri parse = Uri.parse(stringExtra);*/

        try {
          /*  //方案二：网络音频
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(BackgroundMp3Service.this, parse);
            mediaPlayer.prepare();// 进行缓冲
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });*/

            //方案一：本地音频
            Log.e(TAG, "startVoice: 开始播放" );
            User user = new DbConfig(this).getUser();
            int isyunyin = user.getIsyunyin();
            Log.e(TAG, "startVoice: " + isyunyin );
          //  RingtoneManager.getActualDefaultRingtoneUri(this , RingtoneManager.TYPE_NOTIFICATION) == null
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
          //  int max1 = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);// 1
            int current1 = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
            Log.e("service", "系统音量值：-" + current1);

          //  int max2 = am.getStreamMaxVolume(AudioManager.STREAM_RING);// 2
            int current2 = am.getStreamVolume(AudioManager.STREAM_RING);
            Log.e("service", "系统铃声值：-" + current2);

            if (isyunyin == 1 && current1!=0  && current2!=0){

                Log.e(TAG, "service  bofangle "  );
                /*//推送类型：1：人员上报火警任务下发,2：林业一体机报警任务下发,3：卫星火警任务下发,4：海洋报警任务下发,5国土报警任务下发
                    //11：发现新人员上报火警，12：发现林业一体机新火警,13：发现卫星新火警,14：发现海洋新火警,15：发现国土新报警*/
                if(Objects.equals(type, "11")||Objects.equals(type, "12")||Objects.equals(type, "13")||Objects.equals(type, "14")||Objects.equals(type, "15")){
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.find_fire);
                    mediaPlayer.start();
                    mHandler.sendEmptyMessageDelayed(1, 3000);
                }else{
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.find_order);
                    mediaPlayer.start();
                    mHandler.sendEmptyMessageDelayed(1, 3000);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand:" );
        try{
            type = intent.getStringExtra("type");
            startVoice();
        }catch(Exception e){

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}