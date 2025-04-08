package com.haohai.platform.platformmodel.ui.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by geyang on 2020/6/5.
 */

public class DateWeekUtils {

    //获得当前时间的 周一到周天
    public static String getWeeks() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c=Calendar.getInstance(Locale.CHINA);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        //当前时间，貌似多余，其实是为了所有可能的系统一致
        c.setTimeInMillis(System.currentTimeMillis());
        // System.out.println("当前时间:"+format.format(c.getTime()));
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // System.out.println("周一时间:"+format.format(c.getTime()));
        String dayOne = format.format(c.getTime());
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // System.out.println("周天时间:"+format.format(c.getTime()));
        String daySeven = format.format(c.getTime());
        // 拼接当前时间的周一到周天字符串;
        String weeks = dayOne+" - "+daySeven;
        return weeks;
    }

    // 得到上周一到周天
    public static String getLastWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        calendar2.add(Calendar.DATE, offset2 - 7);
        // System.out.println(sdf.format(calendar1.getTime()));// last Monday
        String lastBeginDate = sdf.format(calendar1.getTime());
        // System.out.println(sdf.format(calendar2.getTime()));// last Sunday
        String lastEndDate = sdf.format(calendar2.getTime());
        // 拼接下周一到周天字符串;
        String lastWeek = lastBeginDate + " - " + lastEndDate;
        return lastWeek;
    }

    // 得到下周一到周天
    public static String getNextWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 + 7);
        calendar2.add(Calendar.DATE, offset2 + 7);
        // System.out.println(sdf.format(calendar1.getTime()));// last Monday
        String lastBeginDate = sdf.format(calendar1.getTime());
        // System.out.println(sdf.format(calendar2.getTime()));// last Sunday
        String lastEndDate = sdf.format(calendar2.getTime());
        // 拼接下周一到周天字符串
        String nextWeek = lastBeginDate + " - " + lastEndDate;
        return nextWeek;
    }
    // 得到下下周一到周天
    public static String getNextNextWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 + 14);
        calendar2.add(Calendar.DATE, offset2 + 14);
        // System.out.println(sdf.format(calendar1.getTime()));// last Monday
        String lastBeginDate = sdf.format(calendar1.getTime());
        // System.out.println(sdf.format(calendar2.getTime()));// last Sunday
        String lastEndDate = sdf.format(calendar2.getTime());
        // 拼接下周一到周天字符串
        String nextWeek = lastBeginDate + " - " + lastEndDate;
        return nextWeek;
    }

    // 得到当前时间的下十分钟
    public static long getTenMinutes(Date time){
        return time.getTime()+600000;
    }

    // 得到明天->前10天的时间+星期
    public static List<String > getTimeAndWeek() throws ParseException {
        List<String> list = new ArrayList<>();
        for (int i = 1; i != -10; i--) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, i);//-1.昨天时间 0.当前时间 1.明天时间 *以此类推
            String time = sdf.format(c.getTime());
            Date parse = sdf.parse(time);
            String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
            Calendar cal = Calendar.getInstance();
            cal.setTime(parse);

            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0) {
                w = 0;
            }
            list.add(time+" "+weekDays[w]);
            // System.out.println(time+" "+weekDays[w]);
        }
        return list;
    }
}
