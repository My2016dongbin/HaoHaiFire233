package com.ruyiruyi.rylibrary.utils;

/*
* 将 int year, int month, int day   转化为 2000-02-02格式
* */
public class FormatDateUtil {
    public String formatDateAll(int year, int month, int day) {
        String dates = "";
        dates = year + "-";
        if (month < 10) {
            dates = dates + "0" + month + "-";
        } else {
            dates = dates + month + "-";
        }
        if (day < 10) {
            dates = dates + "0" + day;
        } else {
            dates = dates + day;
        }

        return dates;
    }

    /*
    * 将 int month ,转化为 02
    * */
    public String formatMonthOrDay(int month) {
        String dates = "";
        if (month < 10) {
            dates = dates + "0" + month;
        } else {
            dates = dates + month;
        }

        return dates;
    }

    public static boolean isNumber(String str) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(?!^0*(\\.0{1,2})?$)^\\d{1,13}(\\.\\d{1,2})?$");// 不为0的价格 小数点后最多两位小数
//        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(([1-9]\\d*)(\\.\\d{1,2})?|0\\.([1-9]|\\d[1-9])0)$");//0.0/0.00 都不行
        java.util.regex.Matcher match = pattern.matcher(str);
        return match.matches();
    }

}