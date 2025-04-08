package com.ruyiruyi.rylibrary.utils;

import android.os.Build;

/**
 * Created by geyang on 2020/6/10.
 */

public class PhoneUtils {
    public static String getDeviceID() {
        String deviceID= "";
        try{
            //一共13位  如果位数不够可以继续添加其他信息
            deviceID= ""+ Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                    Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                    Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                    Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                    Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                    Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                    Build.USER.length() % 10;
        }catch (Exception e){
            return "";
        }
        return deviceID;
    }

}
