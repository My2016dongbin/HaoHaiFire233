package com.ruyiruyi.rylibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import com.ruyiruyi.rylibrary.log.FileLog;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Lenovo on 2018/3/12.
 */

@SuppressLint({"NewApi"})
public class ForegroundDetector implements Application.ActivityLifecycleCallbacks {
    private int a;
    private boolean b = true;
    private long c = 0L;
    private CopyOnWriteArrayList<Listener> d = new CopyOnWriteArrayList();
    private static ForegroundDetector e = null;

    public static ForegroundDetector getInstance() {
        return e;
    }

    public ForegroundDetector(Application var1) {
        e = this;
        var1.registerActivityLifecycleCallbacks(this);
    }

    public boolean isForeground() {
        return this.a > 0;
    }

    public boolean isBackground() {
        return this.a == 0;
    }

    public void addListener(ForegroundDetector.Listener var1) {
        this.d.add(var1);
    }

    public void removeListener(ForegroundDetector.Listener var1) {
        this.d.remove(var1);
    }

    public void onActivityStarted(Activity var1) {
        if(++this.a == 1) {
            if(System.currentTimeMillis() - this.c < 200L) {
                this.b = false;
            }

               FileLog.e("ForegroundDetector", "switch to foreground");
            Iterator var2 = this.d.iterator();

            while(var2.hasNext()) {
                ForegroundDetector.Listener var3 = (ForegroundDetector.Listener)var2.next();

                try {
                    var3.onBecameForeground();
                } catch (Exception var5) {
                      FileLog.e("ForegroundDetector", var5);
                }
            }
        }

    }

    public boolean isWasInBackground(boolean var1) {
        if(var1 && Build.VERSION.SDK_INT >= 21 && System.currentTimeMillis() - this.c < 200L) {
            this.b = false;
        }

        return this.b;
    }

    public void resetBackgroundVar() {
        this.b = false;
    }

    public void onActivityStopped(Activity var1) {
        if(--this.a == 0) {
            this.c = System.currentTimeMillis();
            this.b = true;
            // FileLog.e("ForegroundDetector", "switch to background");
            Iterator var2 = this.d.iterator();

            while(var2.hasNext()) {
                ForegroundDetector.Listener var3 = (ForegroundDetector.Listener)var2.next();

                try {
                    var3.onBecameBackground();
                } catch (Exception var5) {
                  //  FileLog.e("ForegroundDetector", var5);
                }
            }
        }

    }

    public void onActivityCreated(Activity var1, Bundle var2) {
    }

    public void onActivityResumed(Activity var1) {
    }

    public void onActivityPaused(Activity var1) {
    }

    public void onActivitySaveInstanceState(Activity var1, Bundle var2) {
    }

    public void onActivityDestroyed(Activity var1) {
    }

    public interface Listener {
        void onBecameForeground();

        void onBecameBackground();
    }
}
