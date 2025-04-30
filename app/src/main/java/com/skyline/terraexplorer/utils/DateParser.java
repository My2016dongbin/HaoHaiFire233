package com.skyline.terraexplorer.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by geyang on 2019/11/20.
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
