package com.ruyiruyi.rylibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ruyiruyi.rylibrary.log.FileLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Created by Lenovo on 2018/3/12.
 */
public class AndroidUtilities {
    private static final Hashtable<String, Typeface> a = new Hashtable();
    private static int b = -10;
    private static boolean c = false;
    private static final Object d = new Object();
    public static int statusBarHeight = 0;
    public static float density = 1.0F;
    public static Point displaySize = new Point();
    public static Integer photoSize = null;
    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static int leftBaseline;
    public static boolean usingHardwareInput;
    private static Boolean e = null;
    public static final int FLAG_TAG_BR = 1;
    public static final int FLAG_TAG_BOLD = 2;
    public static final int FLAG_TAG_COLOR = 4;
    public static final int FLAG_TAG_ALL = 7;
    public static final int NETWORK_TYPE_INVALID = 0;
    public static final int NETWORK_TYPE_WAP = 1;
    public static final int NETWORK_TYPE_2G = 2;
    public static final int NETWORK_TYPE_3G = 3;
    public static final int NETWORK_TYPE_WIFI = 4;
    public static final int NETWORK_TYPE_UNKNOWN = 5;

    public AndroidUtilities() {
    }

    public static void setMaterialTypeface(TextView var0) {
        if(var0 != null) {
            var0.setTypeface(getTypeface("fonts/google_material_design.ttf"));
        }

    }

    /* public static void lockOrientation(Activity var0) {
          if(var0 != null && b == -10 && Build.VERSION.SDK_INT >= 9) {
               try {
                    b = var0.getRequestedOrientation();
                    WindowManager var1 = (WindowManager)var0.getSystemService("window");
                    if(var1 != null && var1.getDefaultDisplay() != null) {
                         int var2 = var1.getDefaultDisplay().getRotation();
                         int var3 = var0.getResources().getConfiguration().orientation;
                         boolean var4 = true;
                         boolean var5 = true;
                         if(Build.VERSION.SDK_INT < 9) {
                              var4 = false;
                              var5 = true;
                         }

                         if(var2 == 3) {
                              if(var3 == 1) {
                                   var0.setRequestedOrientation(1);
                              } else {
                                   var0.setRequestedOrientation(8);
                              }
                         } else if(var2 == 1) {
                              if(var3 == 1) {
                                   var0.setRequestedOrientation(9);
                              } else {
                                   var0.setRequestedOrientation(0);
                              }
                         } else if(var2 == 0) {
                              if(var3 == 2) {
                                   var0.setRequestedOrientation(0);
                              } else {
                                   var0.setRequestedOrientation(1);
                              }
                         } else if(var3 == 2) {
                              var0.setRequestedOrientation(8);
                         } else {
                              var0.setRequestedOrientation(9);
                         }
                    }
               } catch (Exception var6) {
                    FileLog.e("xiaomajiajia", var6);
               }

          }
     }*/

     /*public static void unlockOrientation(Activity var0) {
          if(var0 != null && Build.VERSION.SDK_INT >= 9) {
               try {
                    if(b != -10) {
                         var0.setRequestedOrientation(b);
                         b = -10;
                    }
               } catch (Exception var2) {
                    FileLog.e("xiaomajiajia", var2);
               }

          }
     }*/

    public static Typeface getTypeface(String var0) {
        Hashtable var1 = a;
        synchronized(a) {
            if(!a.containsKey(var0)) {
                try {
                    Typeface var2 = Typeface.createFromAsset(ApplicationLoader.applicationContext.getAssets(), var0);
                    a.put(var0, var2);
                } catch (Exception var4) {
                    FileLog.e("Typefaces", "Could not get typeface \'" + var0 + "\' because " + var4.getMessage());
                    return null;
                }
            }

            return (Typeface)a.get(var0);
        }
    }

    public static boolean isWaitingForSms() {
        Object var1 = d;
        synchronized(d) {
            boolean var0 = c;
            return var0;
        }
    }

    public static void setWaitingForSms(boolean var0) {
        Object var1 = d;
        synchronized(d) {
            c = var0;
        }
    }

    /* public static void showKeyboard(View var0) {
          if(var0 != null) {
               InputMethodManager var1 = (InputMethodManager)var0.getContext().getSystemService("input_method");
               var1.showSoftInput(var0, 1);
          }
     }*/

     /*public static void requestFocus(EditText var0, boolean var1) {
          if(var0 != null) {
               var0.requestFocus(var0.getText().length());
               if(var1) {
                    showKeyboard(var0);
               } else if(isKeyboardShowed(var0)) {
                    hideKeyboard(var0);
               }

          }
     }*/
/*
     public static void clearFocus(EditText var0) {
          if(var0 != null) {
               var0.clearFocus();
               hideKeyboard(var0);
          }
     }*/

    /* public static boolean isKeyboardShowed(View var0) {
          if(var0 == null) {
               return false;
          } else {
               InputMethodManager var1 = (InputMethodManager)var0.getContext().getSystemService("input_method");
               return var1.isActive(var0);
          }
     }

     public static void hideKeyboard(View var0) {
          if(var0 != null) {
               InputMethodManager var1 = (InputMethodManager)var0.getContext().getSystemService("input_method");
               if(var1.isActive()) {
                    var1.hideSoftInputFromWindow(var0.getWindowToken(), 0);
               }
          }
     }*/

    public static File getCacheDir() {
        String var0 = null;

        try {
            var0 = Environment.getExternalStorageState();
        } catch (Exception var4) {
            FileLog.e("xiaomajiajia", var4);
        }

        File var1;
        if(var0 == null || var0.startsWith("mounted")) {
            try {
                var1 = ApplicationLoader.applicationContext.getExternalCacheDir();
                if(var1 != null) {
                    return var1;
                }
            } catch (Exception var3) {
                FileLog.e("xiaomajiajia", var3);
            }
        }

        try {
            var1 = ApplicationLoader.applicationContext.getCacheDir();
            if(var1 != null) {
                return var1;
            }
        } catch (Exception var2) {
            FileLog.e("xiaomajiajia", var2);
        }

        return new File("");
    }

    public static int dp(float var0) {
        return var0 == 0.0F?0:(int)Math.ceil((double)(density * var0));
    }

    public static float sp(int var0) {
        float var1 = TypedValue.applyDimension(2, (float)var0, ApplicationLoader.applicationContext.getResources().getDisplayMetrics());
        return var1;
    }

    public static void setTextSize(TextView var0, int var1) {
        if(var0 != null) {
            var0.setTextSize(2, (float)var1);
        }

    }

    public static int compare(int var0, int var1) {
        return var0 == var1?0:(var0 > var1?1:-1);
    }

    public static float dpf2(float var0) {
        return var0 == 0.0F?0.0F:density * var0;
    }

    public static void checkDisplaySize() {
        try {
            Configuration var0 = ApplicationLoader.applicationContext.getResources().getConfiguration();
            usingHardwareInput = var0.keyboard != 1 && var0.hardKeyboardHidden == 1;
            WindowManager var1 = (WindowManager)ApplicationLoader.applicationContext.getSystemService("window");
            if(var1 != null) {
                Display var2 = var1.getDefaultDisplay();
                if(var2 != null) {
                    var2.getMetrics(displayMetrics);
                    if(Build.VERSION.SDK_INT < 13) {
                        displaySize.set(var2.getWidth(), var2.getHeight());
                    } else {
                        var2.getSize(displaySize);
                    }

                    FileLog.e("xiaomajiajia", "display size = " + displaySize.x + " " + displaySize.y + " " + displayMetrics.xdpi + "x" + displayMetrics.ydpi);
                }
            }
        } catch (Exception var3) {
            FileLog.e("xiaomajiajia", var3);
        }

    }

    public static float getPixelsInCM(float var0, boolean var1) {
        return var0 / 2.54F * (var1?displayMetrics.xdpi:displayMetrics.ydpi);
    }

    public static long makeBroadcastId(int var0) {
        return 4294967296L | (long)var0 & 4294967295L;
    }

    public static int getMyLayerVersion(int var0) {
        return var0 & '\uffff';
    }

    public static int getPeerLayerVersion(int var0) {
        return var0 >> 16 & '\uffff';
    }

    public static int setMyLayerVersion(int var0, int var1) {
        return var0 & -65536 | var1;
    }

    public static int setPeerLayerVersion(int var0, int var1) {
        return var0 & '\uffff' | var1 << 16;
    }

    public static void runOnUIThread(Runnable var0) {
        runOnUIThread(var0, 0L);
    }

    public static void runOnUIThread(Runnable var0, long var1) {
        if(var1 == 0L) {
            ApplicationLoader.applicationHandler.post(var0);
        } else {
            ApplicationLoader.applicationHandler.postDelayed(var0, var1);
        }

    }

    public static void cancelRunOnUIThread(Runnable var0) {
        ApplicationLoader.applicationHandler.removeCallbacks(var0);
    }

     /*public static boolean isTablet() {
          if(e == null) {
               e = Boolean.valueOf(ApplicationLoader.applicationContext.getResources().getBoolean(bool.isTablet));
          }

          return e.booleanValue();
     }*/

    public static boolean isSmallTablet() {
        float var0 = (float)Math.min(displaySize.x, displaySize.y) / density;
        return var0 <= 700.0F;
    }

    public static int getMinTabletSide() {
        int var0;
        int var1;
        if(!isSmallTablet()) {
            var0 = Math.min(displaySize.x, displaySize.y);
            var1 = var0 * 35 / 100;
            if(var1 < dp(320.0F)) {
                var1 = dp(320.0F);
            }

            return var0 - var1;
        } else {
            var0 = Math.min(displaySize.x, displaySize.y);
            var1 = Math.max(displaySize.x, displaySize.y);
            int var2 = var1 * 35 / 100;
            if(var2 < dp(320.0F)) {
                var2 = dp(320.0F);
            }

            return Math.min(var0, var1 - var2);
        }
    }

    public static int getPhotoSize() {
        if(photoSize == null) {
            if(Build.VERSION.SDK_INT >= 16) {
                photoSize = Integer.valueOf(1280);
            } else {
                photoSize = Integer.valueOf(800);
            }
        }

        return photoSize.intValue();
    }

    public static void clearCursorDrawable(EditText var0) {
        if(var0 != null && Build.VERSION.SDK_INT >= 12) {
            try {
                Field var1 = TextView.class.getDeclaredField("mCursorDrawableRes");
                var1.setAccessible(true);
                var1.setInt(var0, 0);
            } catch (Exception var2) {
                FileLog.e("xiaomajiajia", var2);
            }

        }
    }

    public static void setProgressBarAnimationDuration(ProgressBar var0, int var1) {
        if(var0 != null) {
            try {
                Field var2 = ProgressBar.class.getDeclaredField("mDuration");
                var2.setAccessible(true);
                var2.setInt(var0, var1);
            } catch (Exception var3) {
                FileLog.e("xiaomajiajia", var3);
            }

        }
    }

    public static int getViewInset(View var0) {
        if(var0 != null && Build.VERSION.SDK_INT >= 21) {
            try {
                Field var1 = View.class.getDeclaredField("mAttachInfo");
                var1.setAccessible(true);
                Object var2 = var1.get(var0);
                if(var2 != null) {
                    Field var3 = var2.getClass().getDeclaredField("mStableInsets");
                    var3.setAccessible(true);
                    Rect var4 = (Rect)var3.get(var2);
                    return var4.bottom;
                }
            } catch (Exception var5) {
                FileLog.e("xiaomajiajia", var5);
            }

            return 0;
        } else {
            return 0;
        }
    }

    /* public static int getCurrentActionBarHeight() {
          return isTablet()?dp(64.0F):(ApplicationLoader.applicationContext.getResources().getConfiguration().orientation == 2?dp(48.0F):dp(56.0F));
     }*/

     /*public static Point getRealScreenSize() {
          Point var0 = new Point();

          try {
               WindowManager var1 = (WindowManager)ApplicationLoader.applicationContext.getSystemService("window");
               if(Build.VERSION.SDK_INT >= 17) {
                    var1.getDefaultDisplay().getRealSize(var0);
               } else {
                    try {
                         Method var2 = Display.class.getMethod("getRawWidth", new Class[0]);
                         Method var3 = Display.class.getMethod("getRawHeight", new Class[0]);
                         var0.set(((Integer)var2.invoke(var1.getDefaultDisplay(), new Object[0])).intValue(), ((Integer)var3.invoke(var1.getDefaultDisplay(), new Object[0])).intValue());
                    } catch (Exception var4) {
                         var0.set(var1.getDefaultDisplay().getWidth(), var1.getDefaultDisplay().getHeight());
                         FileLog.e("xiaomajiajia", var4);
                    }
               }
          } catch (Exception var5) {
               FileLog.e("xiaomajiajia", var5);
          }

          return var0;
     }*/

    public static void setListViewEdgeEffectColor(AbsListView var0, int var1) {
        if(Build.VERSION.SDK_INT >= 21) {
            try {
                Field var2 = AbsListView.class.getDeclaredField("mEdgeGlowTop");
                var2.setAccessible(true);
                EdgeEffect var3 = (EdgeEffect)var2.get(var0);
                if(var3 != null) {
                    var3.setColor(var1);
                }

                var2 = AbsListView.class.getDeclaredField("mEdgeGlowBottom");
                var2.setAccessible(true);
                EdgeEffect var4 = (EdgeEffect)var2.get(var0);
                if(var4 != null) {
                    var4.setColor(var1);
                }
            } catch (Exception var5) {
                FileLog.e("xiaomajiajia", var5);
            }
        }

    }

    @SuppressLint({"NewApi"})
    public static void clearDrawableAnimation(View var0) {
        if(Build.VERSION.SDK_INT >= 21 && var0 != null) {
            Drawable var1;
            if(var0 instanceof ListView) {
                var1 = ((ListView)var0).getSelector();
                if(var1 != null) {
                    var1.setState(StateSet.NOTHING);
                }
            } else {
                var1 = var0.getBackground();
                if(var1 != null) {
                    var1.setState(StateSet.NOTHING);
                    var1.jumpToCurrentState();
                }
            }

        }
    }
/*

     public static SpannableStringBuilder replaceTags(String var0) {
          return replaceTags(var0, 7);
     }
*/

     /*public static SpannableStringBuilder replaceTags(String var0, int var1) {
          try {
               StringBuilder var4 = new StringBuilder(var0);
               int var2;
               if((var1 & 1) != 0) {
                    label71:
                    while(true) {
                         if((var2 = var4.indexOf("<br>")) == -1) {
                              while(true) {
                                   if((var2 = var4.indexOf("<br/>")) == -1) {
                                        break label71;
                                   }

                                   var4.replace(var2, var2 + 5, "\n");
                              }
                         }

                         var4.replace(var2, var2 + 4, "\n");
                    }
               }

               ArrayList var5 = new ArrayList();
               int var3;
               if((var1 & 2) != 0) {
                    while((var2 = var4.indexOf("<b>")) != -1) {
                         var4.replace(var2, var2 + 3, "");
                         var3 = var4.indexOf("</b>");
                         if(var3 == -1) {
                              var3 = var4.indexOf("<b>");
                         }

                         var4.replace(var3, var3 + 4, "");
                         var5.add(Integer.valueOf(var2));
                         var5.add(Integer.valueOf(var3));
                    }
               }

               ArrayList var6 = new ArrayList();
               int var8;
               if((var1 & 4) != 0) {
                    while((var2 = var4.indexOf("<c#")) != -1) {
                         var4.replace(var2, var2 + 2, "");
                         var3 = var4.indexOf(">", var2);
                         int var7 = Color.parseColor(var4.substring(var2, var3));
                         var4.replace(var2, var3 + 1, "");
                         var3 = var4.indexOf("</c>");
                         var4.replace(var3, var3 + 4, "");
                         var6.add(Integer.valueOf(var2));
                         var6.add(Integer.valueOf(var3));
                         var6.add(Integer.valueOf(var7));
                    }

                    while((var2 = var4.indexOf("<c0x")) != -1) {
                         var4.replace(var2, var2 + 4, "");
                         var3 = var4.indexOf(">", var2);
                         String var10 = var4.substring(var2, var3);
                         var8 = Integer.parseInt(var10);
                         var4.replace(var2, var3 + 1, "");
                         var3 = var4.indexOf("</c>");
                         var4.replace(var3, var3 + 4, "");
                         var6.add(Integer.valueOf(var2));
                         var6.add(Integer.valueOf(var3));
                         var6.add(Integer.valueOf(var8));
                    }
               }

               SpannableStringBuilder var11 = new SpannableStringBuilder(var4);

               for(var8 = 0; var8 < var5.size() / 2; ++var8) {
                    var11.setSpan(new TypefaceSpan(getTypeface("fonts/rmedium.ttf")), ((Integer)var5.get(var8 * 2)).intValue(), ((Integer)var5.get(var8 * 2 + 1)).intValue(), 33);
               }

               for(var8 = 0; var8 < var6.size() / 3; ++var8) {
                    var11.setSpan(new ForegroundColorSpan(((Integer)var6.get(var8 * 3 + 2)).intValue()), ((Integer)var6.get(var8 * 3)).intValue(), ((Integer)var6.get(var8 * 3 + 1)).intValue(), 33);
               }

               return var11;
          } catch (Exception var9) {
               FileLog.e("xiaomajiajia", var9);
               return new SpannableStringBuilder(var0);
          }
     }*/

    /* public static void shakeTextView(final TextView var0, final float var1, final int var2) {
          if(var2 == 6) {
               ViewProxy.setTranslationX(var0, 0.0F);
               var0.clearAnimation();
          } else {
               AnimatorSetProxy var3 = new AnimatorSetProxy();
               var3.playTogether(new Object[]{ObjectAnimatorProxy.ofFloat(var0, "translationX", new float[]{(float)dp(var1)})});
               var3.setDuration(50L);
               var3.addListener(new AnimatorListenerAdapterProxy() {
                    public void onAnimationEnd(Object var1x) {
                         AndroidUtilities.shakeTextView(var0, var2 == 5?0.0F:-var1, var2 + 1);
                    }
               });
               var3.start();
          }
     }*/

    public static void checkForCrashes(Activity var0) {
    }

    public static void checkForUpdates(Activity var0) {
    }

    public static void unregisterUpdates() {
    }

    public static void addMediaToGallery(String var0) {
        if(var0 != null) {
            File var1 = new File(var0);
            Uri var2 = Uri.fromFile(var1);
            addMediaToGallery(var2);
        }
    }

    public static void addMediaToGallery(Uri var0) {
        if(var0 != null) {
            Intent var1 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            var1.setData(var0);
            ApplicationLoader.applicationContext.sendBroadcast(var1);
        }
    }

    private static File a() {
        File var0 = null;
        if("mounted".equals(Environment.getExternalStorageState())) {
            var0 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "xiaomajiajia");
            if(!var0.mkdirs() && !var0.exists()) {
                FileLog.d("xiaomajiajia", "failed to create directory");
                return null;
            }
        } else {
            FileLog.d("xiaomajiajia", "External storage is not mounted READ/WRITE.");
        }

        return var0;
    }

    @SuppressLint({"NewApi"})
    public static String getPath(Uri var0) {
        try {
            boolean var1 = Build.VERSION.SDK_INT >= 19;
            if(var1 && DocumentsContract.isDocumentUri(ApplicationLoader.applicationContext, var0)) {
                String var2;
                String[] var3;
                String var4;
                if(isExternalStorageDocument(var0)) {
                    var2 = DocumentsContract.getDocumentId(var0);
                    var3 = var2.split(":");
                    var4 = var3[0];
                    if("primary".equalsIgnoreCase(var4)) {
                        return Environment.getExternalStorageDirectory() + "/" + var3[1];
                    }
                } else {
                    if(isDownloadsDocument(var0)) {
                        var2 = DocumentsContract.getDocumentId(var0);
                        Uri var9 = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(var2).longValue());
                        return getDataColumn(ApplicationLoader.applicationContext, var9, (String)null, (String[])null);
                    }

                    if(isMediaDocument(var0)) {
                        var2 = DocumentsContract.getDocumentId(var0);
                        var3 = var2.split(":");
                        var4 = var3[0];
                        Uri var5 = null;
                        byte var7 = -1;
                        switch(var4.hashCode()) {
                            case 93166550:
                                if(var4.equals("audio")) {
                                    var7 = 2;
                                }
                                break;
                            case 100313435:
                                if(var4.equals("image")) {
                                    var7 = 0;
                                }
                                break;
                            case 112202875:
                                if(var4.equals("video")) {
                                    var7 = 1;
                                }
                        }

                        switch(var7) {
                            case 0:
                                var5 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                                break;
                            case 1:
                                var5 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                break;
                            case 2:
                                var5 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        String var6 = "_id=?";
                        String[] var10 = new String[]{var3[1]};
                        return getDataColumn(ApplicationLoader.applicationContext, var5, "_id=?", var10);
                    }
                }
            } else {
                if("content".equalsIgnoreCase(var0.getScheme())) {
                    return getDataColumn(ApplicationLoader.applicationContext, var0, (String)null, (String[])null);
                }

                if("file".equalsIgnoreCase(var0.getScheme())) {
                    return var0.getPath();
                }
            }
        } catch (Exception var8) {
            FileLog.e("xiaomajiajia", var8);
        }

        return null;
    }

    public static String getDataColumn(Context var0, Uri var1, String var2, String[] var3) {
        Cursor var4 = null;
        String var5 = "_data";
        String[] var6 = new String[]{"_data"};

        String var8;
        try {
            var4 = var0.getContentResolver().query(var1, var6, var2, var3, (String)null);
            if(var4 == null || !var4.moveToFirst()) {
                return null;
            }

            int var7 = var4.getColumnIndexOrThrow("_data");
            var8 = var4.getString(var7);
        } catch (Exception var12) {
            FileLog.e("xiaomajiajia", var12);
            return null;
        } finally {
            if(var4 != null) {
                var4.close();
            }

        }

        return var8;
    }

    public static boolean isExternalStorageDocument(Uri var0) {
        return "com.android.externalstorage.documents".equals(var0.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri var0) {
        return "com.android.providers.downloads.documents".equals(var0.getAuthority());
    }

    public static boolean isMediaDocument(Uri var0) {
        return "com.android.providers.media.documents".equals(var0.getAuthority());
    }

    public static File generatePicturePath() {
        try {
            File var0 = a();
            String var1 = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)).format(new Date());
            return new File(var0, "IMG_" + var1 + ".jpg");
        } catch (Exception var2) {
            FileLog.e("xiaomajiajia", var2);
            return null;
        }
    }

    /* public static CharSequence generateSearchName(String var0, String var1, String var2) {
          if(var0 == null && var1 == null) {
               return "";
          } else {
               SpannableStringBuilder var3 = new SpannableStringBuilder();
               String var4 = var0;
               if(var0 != null && var0.length() != 0) {
                    if(var1 != null && var1.length() != 0) {
                         var4 = var0 + " " + var1;
                    }
               } else {
                    var4 = var1;
               }

               var4 = var4.trim();
               String var5 = " " + var4.toLowerCase();

               int var6;
               int var7;
               int var9;
               for(var7 = 0; (var6 = var5.indexOf(" " + var2, var7)) != -1; var7 = var9) {
                    int var8 = var6 - (var6 == 0?0:1);
                    var9 = var2.length() + (var6 == 0?0:1) + var8;
                    if(var7 != 0 && var7 != var8 + 1) {
                         var3.append(var4.substring(var7, var8));
                    } else if(var7 == 0 && var8 != 0) {
                         var3.append(var4.substring(0, var8));
                    }

                    String var10 = var4.substring(var8, var9);
                    if(var10.startsWith(" ")) {
                         var3.append(" ");
                    }

                    var10 = var10.trim();
                    var3.append(replaceTags("<c#ff4d83b3>" + var10 + "</c>"));
               }

               if(var7 != -1 && var7 != var4.length()) {
                    var3.append(var4.substring(var7, var4.length()));
               }

               return var3;
          }
     }*/

    public static File generateVideoPath() {
        try {
            File var0 = a();
            String var1 = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)).format(new Date());
            return new File(var0, "VID_" + var1 + ".mp4");
        } catch (Exception var2) {
            FileLog.e("xiaomajiajia", var2);
            return null;
        }
    }

    public static String formatFileSize(long var0) {
        return var0 < 1024L?String.format("%d B", new Object[]{Long.valueOf(var0)}):(var0 < 1048576L?String.format("%.1f KB", new Object[]{Float.valueOf((float)var0 / 1024.0F)}):(var0 < 1073741824L?String.format("%.1f MB", new Object[]{Float.valueOf((float)var0 / 1024.0F / 1024.0F)}):String.format("%.1f GB", new Object[]{Float.valueOf((float)var0 / 1024.0F / 1024.0F / 1024.0F)})));
    }

    public static byte[] decodeQuotedPrintable(byte[] var0) {
        if(var0 == null) {
            return null;
        } else {
            ByteArrayOutputStream var1 = new ByteArrayOutputStream();

            for(int var2 = 0; var2 < var0.length; ++var2) {
                byte var3 = var0[var2];
                if(var3 == 61) {
                    try {
                        ++var2;
                        int var4 = Character.digit((char)var0[var2], 16);
                        ++var2;
                        int var5 = Character.digit((char)var0[var2], 16);
                        var1.write((char)((var4 << 4) + var5));
                    } catch (Exception var7) {
                        FileLog.e("xiaomajiajia", var7);
                        return null;
                    }
                } else {
                    var1.write(var3);
                }
            }

            byte[] var8 = var1.toByteArray();

            try {
                var1.close();
            } catch (Exception var6) {
                FileLog.e("xiaomajiajia", var6);
            }

            return var8;
        }
    }

    public static boolean copyFile(InputStream var0, File var1) throws IOException {
        FileOutputStream var2 = new FileOutputStream(var1);
        byte[] var3 = new byte[4096];

        int var4;
        while((var4 = var0.read(var3)) > 0) {
            Thread.yield();
            var2.write(var3, 0, var4);
        }

        var2.close();
        return true;
    }

    public static boolean copyFile(File var0, File var1) throws IOException {
        if(!var1.exists()) {
            var1.createNewFile();
        }

        FileInputStream var2 = null;
        FileOutputStream var3 = null;

        boolean var5;
        try {
            var2 = new FileInputStream(var0);
            var3 = new FileOutputStream(var1);
            var3.getChannel().transferFrom(var2.getChannel(), 0L, var2.getChannel().size());
            return true;
        } catch (Exception var9) {
            FileLog.e("xiaomajiajia", var9);
            var5 = false;
        } finally {
            if(var2 != null) {
                var2.close();
            }

            if(var3 != null) {
                var3.close();
            }

        }

        return var5;
    }

    /* public static boolean isEmulator(Context var0) {
          try {
               TelephonyManager var1 = (TelephonyManager)var0.getSystemService("phone");
               String var2 = var1.getDeviceId();
               return var2 != null && var2.equals("000000000000000")?true:Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk");
          } catch (Exception var3) {
               FileLog.e("AndroidUtilities.isEmulator", var3);
               return false;
          }
     }*/

    public static String getNetWorkTypeDesc(int var0) {
        switch(var0) {
            case 0:
                return "无网络";
            case 1:
                return "WAP网络";
            case 2:
                return "2G网络";
            case 3:
                return "3G或者4G网络";
            case 4:
                return "WIFI";
            default:
                return "未知";
        }
    }

    public static Double saveOneBitTwo(Double d){
        BigDecimal bd = new BigDecimal(d);
        Double tem = bd.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue();
        return tem;
    }

   /*  public static int getNetWorkType(Context var0) {
          ConnectivityManager var2 = (ConnectivityManager)var0.getSystemService("connectivity");
          NetworkInfo var3 = var2.getActiveNetworkInfo();
          int var1;
          if(var3 != null && var3.isConnected()) {
               String var4 = var3.getTypeName();
               if(var4.equalsIgnoreCase("WIFI")) {
                    var1 = 4;
               } else if(var4.equalsIgnoreCase("MOBILE")) {
                    String var5 = Proxy.getDefaultHost();
                    var1 = TextUtils.isEmpty(var5)?(a(var0)?3:2):1;
               } else {
                    var1 = 5;
               }
          } else {
               var1 = 0;
          }

          return var1;
     }

     private static boolean a(Context var0) {
          TelephonyManager var1 = (TelephonyManager)var0.getSystemService("phone");
          switch(var1.getNetworkType()) {
               case 0:
                    return false;
               case 1:
                    return false;
               case 2:
                    return false;
               case 3:
                    return true;
               case 4:
                    return false;
               case 5:
                    return true;
               case 6:
                    return true;
               case 7:
                    return false;
               case 8:
                    return true;
               case 9:
                    return true;
               case 10:
                    return true;
               case 11:
                    return false;
               case 12:
                    return true;
               case 13:
                    return true;
               case 14:
                    return true;
               case 15:
                    return true;
               default:
                    return false;
          }
     }

     public static void clip(Context var0, CharSequence var1) {
          ClipboardManager var2 = (ClipboardManager)var0.getSystemService("clipboard");
          ClipData var3 = ClipData.newPlainText("text", var1);
          var2.setPrimaryClip(var3);
     }*/

   /*  public static void shakeView(final View var0, final float var1, final int var2) {
          if(var2 == 6) {
               ViewProxy.setTranslationX(var0, 0.0F);
               var0.clearAnimation();
          } else {
               AnimatorSetProxy var3 = new AnimatorSetProxy();
               var3.playTogether(new Object[]{ObjectAnimatorProxy.ofFloat(var0, "translationX", new float[]{(float)dp(var1)})});
               var3.setDuration(50L);
               var3.addListener(new AnimatorListenerAdapterProxy() {
                    public void onAnimationEnd(Object var1x) {
                         AndroidUtilities.shakeView(var0, var2 == 5?0.0F:-var1, var2 + 1);
                    }
               });
               var3.start();
          }
     }*/

    /* public static boolean isMainProcess(Context var0) {
          ActivityManager var1 = (ActivityManager)var0.getSystemService("activity");
          List var2 = var1.getRunningAppProcesses();
          String var3 = var0.getPackageName();
          int var4 = Process.myPid();
          Iterator var5 = var2.iterator();

          ActivityManager.RunningAppProcessInfo var6;
          do {
               if(!var5.hasNext()) {
                    return false;
               }

               var6 = (ActivityManager.RunningAppProcessInfo)var5.next();
          } while(var6.pid != var4 || !var3.equals(var6.processName));

          return true;
     }*/
/*
     static {
          density = ApplicationLoader.applicationContext.getResources().getDisplayMetrics().density;
          leftBaseline = isTablet()?80:72;
          checkDisplaySize();
     }*/
}
