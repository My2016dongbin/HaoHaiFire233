package com.ruyiruyi.rylibrary.utils;

import android.widget.FrameLayout;

/**
 * Created by Lenovo on 2018/3/12.
 */
public class LayoutHelper {

    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;

    public LayoutHelper() {
    }

    private static int a(float var0) {
        return (int)(var0 < 0.0F?var0:(float)AndroidUtilities.dp(var0));
    }

    public static FrameLayout.LayoutParams createFrame(int var0, float var1, int var2, float var3, float var4, float var5, float var6) {
        FrameLayout.LayoutParams var7 = new FrameLayout.LayoutParams(a((float)var0), a(var1), var2);
        var7.setMargins(AndroidUtilities.dp(var3), AndroidUtilities.dp(var4), AndroidUtilities.dp(var5), AndroidUtilities.dp(var6));
        return var7;
    }

    public static FrameLayout.LayoutParams createFrame(int var0, int var1, int var2) {
        return new FrameLayout.LayoutParams(a((float)var0), a((float)var1), var2);
    }

    public static FrameLayout.LayoutParams createFrame(int var0, float var1) {
        return new FrameLayout.LayoutParams(a((float)var0), a(var1));
    }

    public static android.widget.AbsListView.LayoutParams createList(int var0, float var1) {
        return new android.widget.AbsListView.LayoutParams(a((float)var0), a(var1));
    }

    public static android.widget.RelativeLayout.LayoutParams createRelative(float var0, float var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        android.widget.RelativeLayout.LayoutParams var9 = new android.widget.RelativeLayout.LayoutParams(a(var0), a(var1));
        if(var6 >= 0) {
            var9.addRule(var6);
        }

        if(var7 >= 0 && var8 >= 0) {
            var9.addRule(var7, var8);
        }

        var9.leftMargin = AndroidUtilities.dp((float)var2);
        var9.topMargin = AndroidUtilities.dp((float)var3);
        var9.rightMargin = AndroidUtilities.dp((float)var4);
        var9.bottomMargin = AndroidUtilities.dp((float)var5);
        return var9;
    }

    public static android.widget.RelativeLayout.LayoutParams createRelative(int var0, int var1, int var2, int var3, int var4, int var5) {
        return createRelative((float)var0, (float)var1, var2, var3, var4, var5, -1, -1, -1);
    }

    public static android.widget.RelativeLayout.LayoutParams createRelative(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
        return createRelative((float)var0, (float)var1, var2, var3, var4, var5, var6, -1, -1);
    }

    public static android.widget.RelativeLayout.LayoutParams createRelative(float var0, float var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        return createRelative(var0, var1, var2, var3, var4, var5, -1, var6, var7);
    }

    public static android.widget.RelativeLayout.LayoutParams createRelative(int var0, int var1, int var2, int var3, int var4) {
        return createRelative((float)var0, (float)var1, 0, 0, 0, 0, var2, var3, var4);
    }

    public static android.widget.RelativeLayout.LayoutParams createRelative(int var0, int var1) {
        return createRelative((float)var0, (float)var1, 0, 0, 0, 0, -1, -1, -1);
    }

    public static android.widget.RelativeLayout.LayoutParams createRelative(int var0, int var1, int var2) {
        return createRelative((float)var0, (float)var1, 0, 0, 0, 0, var2, -1, -1);
    }

    public static android.widget.RelativeLayout.LayoutParams createRelative(int var0, int var1, int var2, int var3) {
        return createRelative((float)var0, (float)var1, 0, 0, 0, 0, -1, var2, var3);
    }

    public static android.widget.LinearLayout.LayoutParams createLinear(int var0, int var1, float var2, int var3, int var4, int var5, int var6, int var7) {
        android.widget.LinearLayout.LayoutParams var8 = new android.widget.LinearLayout.LayoutParams(a((float)var0), a((float)var1), var2);
        var8.setMargins(AndroidUtilities.dp((float)var4), AndroidUtilities.dp((float)var5), AndroidUtilities.dp((float)var6), AndroidUtilities.dp((float)var7));
        var8.gravity = var3;
        return var8;
    }

    public static android.widget.LinearLayout.LayoutParams createLinear(int var0, int var1, float var2, int var3, int var4, int var5, int var6) {
        android.widget.LinearLayout.LayoutParams var7 = new android.widget.LinearLayout.LayoutParams(a((float)var0), a((float)var1), var2);
        var7.setMargins(AndroidUtilities.dp((float)var3), AndroidUtilities.dp((float)var4), AndroidUtilities.dp((float)var5), AndroidUtilities.dp((float)var6));
        return var7;
    }

    public static android.widget.LinearLayout.LayoutParams createLinear(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
        android.widget.LinearLayout.LayoutParams var7 = new android.widget.LinearLayout.LayoutParams(a((float)var0), a((float)var1));
        var7.setMargins(AndroidUtilities.dp((float)var3), AndroidUtilities.dp((float)var4), AndroidUtilities.dp((float)var5), AndroidUtilities.dp((float)var6));
        var7.gravity = var2;
        return var7;
    }

    public static android.widget.LinearLayout.LayoutParams createLinear(int var0, int var1, int var2, int var3, int var4, int var5) {
        android.widget.LinearLayout.LayoutParams var6 = new android.widget.LinearLayout.LayoutParams(a((float)var0), a((float)var1));
        var6.setMargins(AndroidUtilities.dp((float)var2), AndroidUtilities.dp((float)var3), AndroidUtilities.dp((float)var4), AndroidUtilities.dp((float)var5));
        return var6;
    }

    public static android.widget.LinearLayout.LayoutParams createLinear(int var0, int var1, float var2, int var3) {
        android.widget.LinearLayout.LayoutParams var4 = new android.widget.LinearLayout.LayoutParams(a((float)var0), a((float)var1), var2);
        var4.gravity = var3;
        return var4;
    }

    public static android.widget.LinearLayout.LayoutParams createLinear(int var0, int var1, int var2) {
        android.widget.LinearLayout.LayoutParams var3 = new android.widget.LinearLayout.LayoutParams(a((float)var0), a((float)var1));
        var3.gravity = var2;
        return var3;
    }

    public static android.widget.LinearLayout.LayoutParams createLinear(int var0, int var1, float var2) {
        return new android.widget.LinearLayout.LayoutParams(a((float)var0), a((float)var1), var2);
    }

    public static android.widget.LinearLayout.LayoutParams createLinear(int var0, int var1) {
        return new android.widget.LinearLayout.LayoutParams(a((float)var0), a((float)var1));
    }
}
