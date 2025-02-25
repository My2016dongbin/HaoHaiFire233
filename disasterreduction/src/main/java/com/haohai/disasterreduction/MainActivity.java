package com.haohai.disasterreduction;

import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.haohai.disasterreduction.drlibrary.ui.DRMapActivity;
import com.haohai.disasterreduction.drlibrary.ui.base.DrBaseActivity;

public class MainActivity extends DrBaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (RingtoneManager.getActualDefaultRingtoneUri(this , RingtoneManager.TYPE_NOTIFICATION) == null) {
            Log.e(TAG, "onCreate:手机静音了");
        }else {
            Log.e(TAG, "onCreate:手机没静音");
        }

        startActivity(new Intent(this, DRMapActivity.class));
    }
}
