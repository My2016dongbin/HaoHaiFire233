package com.hht.hsatellitemobile.ui;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.hht.hsatellitemobile.R;

public class MediaService extends Service {
    private static final String TAG = MediaService.class.getSimpleName();

    public MediaService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");




    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: service : bofang");
        MediaPlayer mMediaPlayer;
        mMediaPlayer=MediaPlayer.create(this, R.raw.find_fire);
        mMediaPlayer.start();
    }
}
