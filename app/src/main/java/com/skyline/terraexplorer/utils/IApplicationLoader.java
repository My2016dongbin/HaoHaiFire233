package com.skyline.terraexplorer.utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by geyang on 2019/11/20.
 */


public interface IApplicationLoader {
    void onCreate();

    void onLowMemory();

    void onTrimMemory(int var1);

    void onTerminate();

    void onConfigurationChanged(Configuration var1);

    void onBaseContextAttached(Context var1);
}
