package com.ruyiruyi.rylibrary.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;

/**
 * Created by Lenovo on 2018/3/12.
 */

public class ApplicationLoader implements IApplicationLoader {
    private final Application a;
    public static volatile Context applicationContext;
    public static volatile Handler applicationHandler;
    public static volatile boolean DEBUG_VERSION = false;
    public static volatile boolean isScreenOn = false;
    public static volatile boolean mainInterfacePaused = true;
    protected static final Object sync = new Object();

    public ApplicationLoader(Application var1) {
        this.a = var1;
        applicationContext = var1.getApplicationContext();
        DEBUG_VERSION = false;
        if(Build.VERSION.SDK_INT < 11) {
            System.setProperty("java.net.preferIPv4Stack", "true");
            System.setProperty("java.net.preferIPv6Addresses", "false");
        }

        applicationHandler = new Handler(var1.getMainLooper());
        if(Build.VERSION.SDK_INT >= 14) {
            new ForegroundDetector(var1);
        }

    }

    public Application getApplication() {
        return this.a;
    }

    public void onCreate() {
    }

    public void onConfigurationChanged(Configuration var1) {
        try {
            //AndroidUtilities.checkDisplaySize();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static int getAppVersion() {
        try {
            PackageInfo var0 = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0);
            return var0.versionCode;
        } catch (PackageManager.NameNotFoundException var1) {
            throw new RuntimeException("Could not get package name: " + var1);
        }
    }

    public void onLowMemory() {
    }

    public void onTrimMemory(int var1) {
    }

    public void onTerminate() {
    }

    public void onBaseContextAttached(Context var1) {
    }
}
