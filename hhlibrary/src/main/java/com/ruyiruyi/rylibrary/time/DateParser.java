package com.ruyiruyi.rylibrary.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Lenovo on 2018/3/12.
 */

public interface DateParser {
    Date parse(String var1) throws ParseException;

    Date parse(String var1, ParsePosition var2);

    String getPattern();

    TimeZone getTimeZone();

    Locale getLocale();

    Object parseObject(String var1) throws ParseException;

    Object parseObject(String var1, ParsePosition var2);
}
