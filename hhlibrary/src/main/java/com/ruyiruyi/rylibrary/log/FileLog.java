package com.ruyiruyi.rylibrary.log;

import android.text.TextUtils;
import android.util.Log;

import com.ruyiruyi.rylibrary.core.DispatchQueue;
import com.ruyiruyi.rylibrary.time.FastDateFormat;
import com.ruyiruyi.rylibrary.utils.ApplicationLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Lenovo on 2018/3/12.
 */


public class FileLog {
    private OutputStreamWriter a = null;
    private FastDateFormat b = null;
    private DispatchQueue c = null;
    private File d = null;
    private final String e;
    private static volatile boolean f = true;
    private static volatile boolean g = true;
    private static volatile FileLog h = null;

    public static FileLog getInstance() {
        FileLog var0 = h;
        if(var0 == null) {
            Class var1 = FileLog.class;
            synchronized(FileLog.class) {
                var0 = h;
                if(var0 == null) {
                    h = var0 = new FileLog();
                }
            }
        }

        return var0;
    }

    public static void setEnableLog(boolean var0) {
        f = var0;
    }

    public static void setEnableSystemLog(boolean var0) {
        g = var0;
    }

    public FileLog() {
        this.e = ApplicationLoader.applicationContext.getPackageName();
        this.b = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        FastDateFormat var1 = FastDateFormat.getInstance("yyyy_MM_dd_HH_mm");

        try {
            File var2 = ApplicationLoader.applicationContext.getExternalFilesDir((String)null);
            if(var2 == null) {
                return;
            }

            File var3 = new File(var2.getAbsolutePath() + "/romens/logs");
            var3.mkdirs();
            this.d = new File(var3, var1.format(System.currentTimeMillis()) + ".log");
            if(g) {
                Log.v("FileLog", "log path :" + var3.getPath());
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        try {
            this.c = new DispatchQueue("logQueue");
            this.d.createNewFile();
            FileOutputStream var6 = new FileOutputStream(this.d);
            this.a = new OutputStreamWriter(var6);
            this.a.write("-----start log " + this.b.format(System.currentTimeMillis()) + "-----\n");
            this.a.flush();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static void e(final String var0, final String var1, final Throwable var2) {
        if(g) {
            Log.e(var0, var1, var2);
        }

        if(getInstance().a != null) {
            getInstance().c.postRunnable(new Runnable() {
                public void run() {
                    try {
                        String var1x = FileLog.b();
                        FileLog.getInstance().a.write(var1x + " E/" + var0 + "﹕ " + var1 + "\n");
                        FileLog.getInstance().a.write(var2.toString());
                        FileLog.getInstance().a.flush();
                    } catch (Exception var2x) {
                        var2x.printStackTrace();
                    }

                }
            });
        }

    }

    public static void e(String var0) {
        e("LOG", var0);
    }

    public static void e(final String var0, final String var1) {
        if(g) {
            Log.e(var0, var1);
        }

        if(getInstance().a != null) {
            getInstance().c.postRunnable(new Runnable() {
                public void run() {
                    try {
                        String var1x = FileLog.b();
                        FileLog.getInstance().a.write(var1x + " E/" + var0 + "﹕ " + var1 + "\n");
                        FileLog.getInstance().a.flush();
                    } catch (Exception var2) {
                        var2.printStackTrace();
                    }

                }
            });
        }

    }

    public static void e(Throwable var0) {
        e(getInstance().e, var0);
    }

    public static void e(final String var0, final Throwable var1) {
        if(g) {
            Log.e(var0, var1.toString());
        }

        var1.printStackTrace();
        if(getInstance().a != null) {
            getInstance().c.postRunnable(new Runnable() {
                public void run() {
                    try {
                        String var1x = FileLog.b();
                        FileLog.getInstance().a.write(var1x + " E/" + var0 + "﹕ " + var1 + "\n");
                        StackTraceElement[] var2 = var1.getStackTrace();
                        StackTraceElement[] var3 = var2;
                        int var4 = var2.length;

                        for(int var5 = 0; var5 < var4; ++var5) {
                            StackTraceElement var6 = var3[var5];
                            FileLog.getInstance().a.write(var1x + " E/" + var0 + "﹕ " + var6 + "\n");
                        }

                        FileLog.getInstance().a.flush();
                    } catch (Exception var7) {
                        var7.printStackTrace();
                    }

                }
            });
        } else {
            var1.printStackTrace();
        }

    }

    private static String b() {
        String var0 = getInstance().b.format(System.currentTimeMillis());
        if(!TextUtils.isEmpty(getInstance().e)) {
            StringBuilder var10001 = (new StringBuilder()).append("/");
            getInstance();
            var0 = var0.concat(var10001.append(getInstance().e).toString());
        }

        return var0;
    }

    public static void d(String var0) {
        d("LOG", var0);
    }

    public static void d(final String var0, final String var1) {
        if(f) {
            if(g) {
                Log.d(var0, var1);
            }

            if(getInstance().a != null) {
                getInstance().c.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            String var1x = FileLog.b();
                            FileLog.getInstance().a.write(var1x + " D/" + var0 + "﹕ " + var1 + "\n");
                            FileLog.getInstance().a.flush();
                        } catch (Exception var2) {
                            var2.printStackTrace();
                        }

                    }
                });
            }

        }
    }

    public static void w(String var0) {
        w("LOG", var0);
    }

    public static void w(final String var0, final String var1) {
        if(f) {
            if(g) {
                Log.w(var0, var1);
            }

            if(getInstance().a != null) {
                getInstance().c.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            String var1x = FileLog.b();
                            FileLog.getInstance().a.write(var1x + " W/" + var0 + ": " + var1 + "\n");
                            FileLog.getInstance().a.flush();
                        } catch (Exception var2) {
                            var2.printStackTrace();
                        }

                    }
                });
            }

        }
    }

    public static void cleanupLogs() {
        File var0 = ApplicationLoader.applicationContext.getExternalFilesDir((String)null);
        File var1 = new File(var0.getAbsolutePath() + "/romens/logs");
        if(var1 != null && var1.exists()) {
            File[] var2 = var1.listFiles();
            File[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File var6 = var3[var5];
                if(getInstance().d == null || !var6.getAbsolutePath().equals(getInstance().d.getAbsolutePath())) {
                    var6.delete();
                }
            }
        }

    }
}
