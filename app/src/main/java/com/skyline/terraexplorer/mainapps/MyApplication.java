package com.skyline.terraexplorer.mainapps;

import com.skyline.terraexplorer.TEApp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by geyang on 2020/8/15.
 */

public class MyApplication extends TEApp {
    @Override
    public void onCreate() {
        super.onCreate();
        /*//初始化xUtils
        x.Ext.init(this);
        x.Ext.setDebug(true);*/
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}
