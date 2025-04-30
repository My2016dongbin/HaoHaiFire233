package com.skyline.test.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by miao on 2017/5/19 13:54.
 */

public class ToastUtils {

    public static void shortShow(Context context,String msg) {
        if(msg != null) {
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }
    }

    public static void longShow(Context context,String msg) {
        if(msg != null) {
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        }
    }
}
