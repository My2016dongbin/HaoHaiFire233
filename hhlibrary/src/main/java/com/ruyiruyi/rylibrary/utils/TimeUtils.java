package com.ruyiruyi.rylibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by geyang on 2020/3/28.
 */

public class TimeUtils {
    public TimeUtils() {
    }

    public String getDateToString(long milSecond) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Date date = new Date(milSecond * 1000);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public long getDataToLong(String str){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts / 1000;
    }

}
