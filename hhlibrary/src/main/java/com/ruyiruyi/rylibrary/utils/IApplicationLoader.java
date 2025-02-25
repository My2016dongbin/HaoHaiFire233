package com.ruyiruyi.rylibrary.utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Lenovo on 2018/3/12.
 */

public interface IApplicationLoader {
    void onCreate();

    void onLowMemory();

    void onTrimMemory(int var1);

    void onTerminate();

    void onConfigurationChanged(Configuration var1);

    void onBaseContextAttached(Context var1);
}
