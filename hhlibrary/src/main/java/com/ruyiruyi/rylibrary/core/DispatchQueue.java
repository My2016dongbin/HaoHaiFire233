package com.ruyiruyi.rylibrary.core;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Lenovo on 2018/3/12.
 */

public class DispatchQueue extends Thread {
    public volatile Handler handler = null;
    private final Object a = new Object();

    public DispatchQueue(String var1) {
        this.setName(var1);
        this.start();
    }

    public void cancelRunnable(Runnable var1) {
        if(this.handler == null) {
            Object var2 = this.a;
            synchronized(this.a) {
                if(this.handler == null) {
                    try {
                        this.a.wait();
                    } catch (Throwable var5) {
                        var5.printStackTrace();
                    }
                }
            }
        }

        if(this.handler != null) {
            this.handler.removeCallbacks(var1);
        }

    }

    public void postRunnable(Runnable var1) {
        this.postRunnable(var1, 0L);
    }

    public void postRunnable(Runnable var1, long var2) {
        if(this.handler == null) {
            Object var4 = this.a;
            synchronized(this.a) {
                if(this.handler == null) {
                    try {
                        this.a.wait();
                    } catch (Throwable var7) {
                        var7.printStackTrace();
                    }
                }
            }
        }

        if(this.handler != null) {
            if(var2 <= 0L) {
                this.handler.post(var1);
            } else {
                this.handler.postDelayed(var1, var2);
            }
        }

    }

    public void cleanupQueue() {
        if(this.handler != null) {
            this.handler.removeCallbacksAndMessages((Object)null);
        }

    }

    public void run() {
        Looper.prepare();
        Object var1 = this.a;
        synchronized(this.a) {
            this.handler = new Handler();
            this.a.notify();
        }

        Looper.loop();
    }
}