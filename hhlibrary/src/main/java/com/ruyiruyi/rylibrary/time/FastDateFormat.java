package com.ruyiruyi.rylibrary.time;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Lenovo on 2018/3/12.
 */

public class FastDateFormat extends Format implements DateParser, DatePrinter {
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private static final a<FastDateFormat> a = new a<FastDateFormat>() {
        @Override
        protected FastDateFormat b(String var1, TimeZone var2, Locale var3) {
            return null;
        }

        protected FastDateFormat a(String var1, TimeZone var2, Locale var3) {
            return new FastDateFormat(var1, var2, var3);
        }
    };
    private final FastDatePrinter b;
    private final FastDateParser c;

    public static FastDateFormat getInstance() {
        return (FastDateFormat)a.a();
    }

    public static FastDateFormat getInstance(String var0) {
        return (FastDateFormat)a.c(var0, (TimeZone)null, (Locale)null);
    }

    public static FastDateFormat getInstance(String var0, TimeZone var1) {
        return (FastDateFormat)a.c(var0, var1, (Locale)null);
    }

    public static FastDateFormat getInstance(String var0, Locale var1) {
        return (FastDateFormat)a.c(var0, (TimeZone)null, var1);
    }

    public static FastDateFormat getInstance(String var0, TimeZone var1, Locale var2) {
        return (FastDateFormat)a.c(var0, var1, var2);
    }

    public static FastDateFormat getDateInstance(int var0) {
        return (FastDateFormat)a.a(var0, (TimeZone)null, (Locale)null);
    }

    public static FastDateFormat getDateInstance(int var0, Locale var1) {
        return (FastDateFormat)a.a(var0, (TimeZone)null, var1);
    }

    public static FastDateFormat getDateInstance(int var0, TimeZone var1) {
        return (FastDateFormat)a.a(var0, var1, (Locale)null);
    }

    public static FastDateFormat getDateInstance(int var0, TimeZone var1, Locale var2) {
        return (FastDateFormat)a.a(var0, var1, var2);
    }

    public static FastDateFormat getTimeInstance(int var0) {
        return (FastDateFormat)a.b(var0, (TimeZone)null, (Locale)null);
    }

    public static FastDateFormat getTimeInstance(int var0, Locale var1) {
        return (FastDateFormat)a.b(var0, (TimeZone)null, var1);
    }

    public static FastDateFormat getTimeInstance(int var0, TimeZone var1) {
        return (FastDateFormat)a.b(var0, var1, (Locale)null);
    }

    public static FastDateFormat getTimeInstance(int var0, TimeZone var1, Locale var2) {
        return (FastDateFormat)a.b(var0, var1, var2);
    }

    public static FastDateFormat getDateTimeInstance(int var0, int var1) {
        return (FastDateFormat)a.a(var0, var1, (TimeZone)null, (Locale)null);
    }

    public static FastDateFormat getDateTimeInstance(int var0, int var1, Locale var2) {
        return (FastDateFormat)a.a(var0, var1, (TimeZone)null, var2);
    }

    public static FastDateFormat getDateTimeInstance(int var0, int var1, TimeZone var2) {
        return getDateTimeInstance(var0, var1, var2, (Locale)null);
    }

    public static FastDateFormat getDateTimeInstance(int var0, int var1, TimeZone var2, Locale var3) {
        return (FastDateFormat)a.a(var0, var1, var2, var3);
    }

    protected FastDateFormat(String var1, TimeZone var2, Locale var3) {
        this(var1, var2, var3, (Date)null);
    }

    protected FastDateFormat(String var1, TimeZone var2, Locale var3, Date var4) {
        this.b = new FastDatePrinter(var1, var2, var3);
        this.c = new FastDateParser(var1, var2, var3, var4);
    }

    public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
        return this.b.format(var1, var2, var3);
    }

    public String format(long var1) {
        return this.b.format(var1);
    }

    public String format(Date var1) {
        return this.b.format(var1);
    }

    public String format(Calendar var1) {
        return this.b.format(var1);
    }

    public StringBuffer format(long var1, StringBuffer var3) {
        return this.b.format(var1, var3);
    }

    public StringBuffer format(Date var1, StringBuffer var2) {
        return this.b.format(var1, var2);
    }

    public StringBuffer format(Calendar var1, StringBuffer var2) {
        return this.b.format(var1, var2);
    }

    public Date parse(String var1) throws ParseException {
        return this.c.parse(var1);
    }

    public Date parse(String var1, ParsePosition var2) {
        return this.c.parse(var1, var2);
    }

    public Object parseObject(String var1, ParsePosition var2) {
        return this.c.parseObject(var1, var2);
    }

    public String getPattern() {
        return this.b.getPattern();
    }

    public TimeZone getTimeZone() {
        return this.b.getTimeZone();
    }

    public Locale getLocale() {
        return this.b.getLocale();
    }

    public int getMaxLengthEstimate() {
        return this.b.getMaxLengthEstimate();
    }

    public boolean equals(Object var1) {
        if(!(var1 instanceof FastDateFormat)) {
            return false;
        } else {
            FastDateFormat var2 = (FastDateFormat)var1;
            return this.b.equals(var2.b);
        }
    }

    public int hashCode() {
        return this.b.hashCode();
    }

    public String toString() {
        return "FastDateFormat[" + this.b.getPattern() + "," + this.b.getLocale() + "," + this.b.getTimeZone().getID() + "]";
    }

    protected StringBuffer applyRules(Calendar var1, StringBuffer var2) {
        return this.b.applyRules(var1, var2);
    }
}
