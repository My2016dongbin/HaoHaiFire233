package com.ruyiruyi.rylibrary.time;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lenovo on 2018/3/12.
 */

public class FastDateParser implements DateParser, Serializable {
    static final Locale a = new Locale("ja", "JP", "JP");
    private final String b;
    private final TimeZone c;
    private final Locale d;
    private final int e;
    private final int f;
    private transient Pattern g;
    private transient FastDateParser.c[] h;
    private transient String i;
    private transient FastDateParser.c j;
    private static final Pattern k = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|S+|W+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|\'\'|\'[^\']++(\'\'[^\']*+)*+\'|[^\'A-Za-z]++");
    private static final ConcurrentMap<Locale, FastDateParser.c>[] l = new ConcurrentMap[17];
    private static final FastDateParser.c m = new FastDateParser.b(1) {
        void a(FastDateParser var1, Calendar var2, String var3) {
            int var4 = Integer.parseInt(var3);
            if(var4 < 100) {
                var4 = var1.a(var4);
            }

            var2.set(1, var4);
        }
    };
    private static final FastDateParser.c n = new FastDateParser.b(2) {
        int a(int var1) {
            return var1 - 1;
        }
    };
    private static final FastDateParser.c o = new FastDateParser.b(1);
    private static final FastDateParser.c p = new FastDateParser.b(3);
    private static final FastDateParser.c q = new FastDateParser.b(4);
    private static final FastDateParser.c r = new FastDateParser.b(6);
    private static final FastDateParser.c s = new FastDateParser.b(5);
    private static final FastDateParser.c t = new FastDateParser.b(8);
    private static final FastDateParser.c u = new FastDateParser.b(11);
    private static final FastDateParser.c v = new FastDateParser.b(11) {
        int a(int var1) {
            return var1 % 24;
        }
    };
    private static final FastDateParser.c w = new FastDateParser.b(10) {
        int a(int var1) {
            return var1 % 12;
        }
    };
    private static final FastDateParser.c x = new FastDateParser.b(10);
    private static final FastDateParser.c y = new FastDateParser.b(12);
    private static final FastDateParser.c z = new FastDateParser.b(13);
    private static final FastDateParser.c A = new FastDateParser.b(14);

    protected FastDateParser(String var1, TimeZone var2, Locale var3) {
        this(var1, var2, var3, (Date)null);
    }

    protected FastDateParser(String var1, TimeZone var2, Locale var3, Date var4) {
        this.b = var1;
        this.c = var2;
        this.d = var3;
        Calendar var5 = Calendar.getInstance(var2, var3);
        int var6;
        if(var4 != null) {
            var5.setTime(var4);
            var6 = var5.get(1);
        } else if(var3.equals(a)) {
            var6 = 0;
        } else {
            var5.setTime(new Date());
            var6 = var5.get(1) - 80;
        }

        this.e = var6 / 100 * 100;
        this.f = var6 - this.e;
        this.a(var5);
    }

    private void a(Calendar var1) {
        StringBuilder var2 = new StringBuilder();
        ArrayList var3 = new ArrayList();
        Matcher var4 = k.matcher(this.b);
        if(!var4.lookingAt()) {
            throw new IllegalArgumentException("Illegal pattern character \'" + this.b.charAt(var4.regionStart()) + "\'");
        } else {
            this.i = var4.group();
            FastDateParser.c var5 = this.a(this.i, var1);

            while(true) {
                var4.region(var4.end(), var4.regionEnd());
                if(!var4.lookingAt()) {
                    this.j = null;
                    if(var4.regionStart() != var4.regionEnd()) {
                        throw new IllegalArgumentException("Failed to parse \"" + this.b + "\" ; gave up at index " + var4.regionStart());
                    }

                    if(var5.a(this, var2)) {
                        var3.add(var5);
                    }

                    this.i = null;
                    this.h = (FastDateParser.c[])var3.toArray(new FastDateParser.c[var3.size()]);
                    this.g = Pattern.compile(var2.toString());
                    return;
                }

                String var6 = var4.group();
                this.j = this.a(var6, var1);
                if(var5.a(this, var2)) {
                    var3.add(var5);
                }

                this.i = var6;
                var5 = this.j;
            }
        }
    }

    public String getPattern() {
        return this.b;
    }

    public TimeZone getTimeZone() {
        return this.c;
    }

    public Locale getLocale() {
        return this.d;
    }

    public boolean equals(Object var1) {
        if(!(var1 instanceof FastDateParser)) {
            return false;
        } else {
            FastDateParser var2 = (FastDateParser)var1;
            return this.b.equals(var2.b) && this.c.equals(var2.c) && this.d.equals(var2.d);
        }
    }

    public int hashCode() {
        return this.b.hashCode() + 13 * (this.c.hashCode() + 13 * this.d.hashCode());
    }

    public String toString() {
        return "FastDateParser[" + this.b + "," + this.d + "," + this.c.getID() + "]";
    }

    public Object parseObject(String var1) throws ParseException {
        return this.parse(var1);
    }

    public Date parse(String var1) throws ParseException {
        Date var2 = this.parse(var1, new ParsePosition(0));
        if(var2 == null) {
            if(this.d.equals(a)) {
                throw new ParseException("(The " + this.d + " locale does not support dates before 1868 AD)\nUnparseable date: \"" + var1 + "\" does not match " + this.g.pattern(), 0);
            } else {
                throw new ParseException("Unparseable date: \"" + var1 + "\" does not match " + this.g.pattern(), 0);
            }
        } else {
            return var2;
        }
    }

    public Object parseObject(String var1, ParsePosition var2) {
        return this.parse(var1, var2);
    }

    public Date parse(String var1, ParsePosition var2) {
        int var3 = var2.getIndex();
        Matcher var4 = this.g.matcher(var1.substring(var3));
        if(!var4.lookingAt()) {
            return null;
        } else {
            Calendar var5 = Calendar.getInstance(this.c, this.d);
            var5.clear();
            int var6 = 0;

            while(var6 < this.h.length) {
                FastDateParser.c var7 = this.h[var6++];
                var7.a(this, var5, var4.group(var6));
            }

            var2.setIndex(var3 + var4.end());
            return var5.getTime();
        }
    }

    private static StringBuilder b(StringBuilder var0, String var1, boolean var2) {
        var0.append("\\Q");

        for(int var3 = 0; var3 < var1.length(); ++var3) {
            char var4 = var1.charAt(var3);
            switch(var4) {
                case '\'':
                    if(var2) {
                        ++var3;
                        if(var3 == var1.length()) {
                            return var0;
                        }

                        var4 = var1.charAt(var3);
                    }
                    break;
                case '\\':
                    ++var3;
                    if(var3 != var1.length()) {
                        var0.append(var4);
                        var4 = var1.charAt(var3);
                        if(var4 == 69) {
                            var0.append("E\\\\E\\");
                            var4 = 81;
                        }
                    }
            }

            var0.append(var4);
        }

        var0.append("\\E");
        return var0;
    }

    private static String[] a(int var0, boolean var1, Locale var2) {
        DateFormatSymbols var3 = new DateFormatSymbols(var2);
        switch(var0) {
            case 0:
                return var3.getEras();
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            default:
                return null;
            case 2:
                return var1?var3.getMonths():var3.getShortMonths();
            case 7:
                return var1?var3.getWeekdays():var3.getShortWeekdays();
            case 9:
                return var3.getAmPmStrings();
        }
    }

    private static void a(Map<String, Integer> var0, String[] var1) {
        if(var1 != null) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
                if(var1[var2] != null && var1[var2].length() > 0) {
                    var0.put(var1[var2], Integer.valueOf(var2));
                }
            }

        }
    }

    private static Map<String, Integer> a(int var0, Locale var1) {
        HashMap var2 = new HashMap();
        a((Map)var2, (String[])a(var0, false, var1));
        a((Map)var2, (String[])a(var0, true, var1));
        return var2.isEmpty()?null:var2;
    }

    private static Map<String, Integer> b(int var0, Calendar var1, Locale var2) {
        return a(var0, var2);
    }

    private int a(int var1) {
        int var2 = this.e + var1;
        return var1 >= this.f?var2:var2 + 100;
    }

    boolean a() {
        return this.j != null && this.j.a();
    }

    int b() {
        return this.i.length();
    }

    private FastDateParser.c a(String var1, Calendar var2) {
        switch(var1.charAt(0)) {
            case '\'':
                if(var1.length() > 2) {
                    return new FastDateParser.a(var1.substring(1, var1.length() - 1));
                }
            case '(':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case 'A':
            case 'B':
            case 'C':
            case 'I':
            case 'J':
            case 'L':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'X':
            case 'Y':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            case 'b':
            case 'c':
            case 'e':
            case 'f':
            case 'g':
            case 'i':
            case 'j':
            case 'l':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 't':
            case 'u':
            case 'v':
            case 'x':
            default:
                return new FastDateParser.a(var1);
            case 'D':
                return r;
            case 'E':
                return this.a(7, (Calendar)var2);
            case 'F':
                return t;
            case 'G':
                return this.a(0, (Calendar)var2);
            case 'H':
                return v;
            case 'K':
                return x;
            case 'M':
                return var1.length() >= 3?this.a(2, (Calendar)var2):n;
            case 'S':
                return A;
            case 'W':
                return q;
            case 'Z':
            case 'z':
                return this.a(15, (Calendar)var2);
            case 'a':
                return this.a(9, (Calendar)var2);
            case 'd':
                return s;
            case 'h':
                return w;
            case 'k':
                return u;
            case 'm':
                return y;
            case 's':
                return z;
            case 'w':
                return p;
            case 'y':
                return var1.length() > 2?o:m;
        }
    }

    private static ConcurrentMap<Locale, FastDateParser.c> b(int var0) {
        ConcurrentMap[] var1 = l;
        synchronized(l) {
            if(l[var0] == null) {
                l[var0] = new ConcurrentHashMap(3);
            }

            return l[var0];
        }
    }

    private FastDateParser.c a(int var1, Calendar var2) {
        ConcurrentMap var3 = b(var1);
        Object var4 = (FastDateParser.c)var3.get(this.d);
        if(var4 == null) {
            var4 = var1 == 15?new FastDateParser.e(this.d):new FastDateParser.d(var1, var2, this.d);
            FastDateParser.c var5 = (FastDateParser.c)var3.putIfAbsent(this.d, var4);
            if(var5 != null) {
                return var5;
            }
        }

        return (FastDateParser.c)var4;
    }

    private static class e extends FastDateParser.c {
        private final String a;
        private final SortedMap<String, TimeZone> b;

        e(Locale var1) {
            //   super(null);
            this.b = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            String[][] var2 = DateFormatSymbols.getInstance(var1).getZoneStrings();
            String[][] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String[] var6 = var3[var5];
                if(!var6[0].startsWith("GMT")) {
                    TimeZone var7 = TimeZone.getTimeZone(var6[0]);
                    if(!this.b.containsKey(var6[1])) {
                        this.b.put(var6[1], var7);
                    }

                    if(!this.b.containsKey(var6[2])) {
                        this.b.put(var6[2], var7);
                    }

                    if(var7.useDaylightTime()) {
                        if(!this.b.containsKey(var6[3])) {
                            this.b.put(var6[3], var7);
                        }

                        if(!this.b.containsKey(var6[4])) {
                            this.b.put(var6[4], var7);
                        }
                    }
                }
            }

            StringBuilder var8 = new StringBuilder();
            var8.append("(GMT[+\\-]\\d{0,1}\\d{2}|[+\\-]\\d{2}:?\\d{2}|");
            Iterator var9 = this.b.keySet().iterator();

            while(var9.hasNext()) {
                String var10 = (String)var9.next();
                FastDateParser.b(var8, var10, false).append('|');
            }

            var8.setCharAt(var8.length() - 1, ')');
            this.a = var8.toString();
        }

        boolean a(FastDateParser var1, StringBuilder var2) {
            var2.append(this.a);
            return true;
        }

        void a(FastDateParser var1, Calendar var2, String var3) {
            TimeZone var4;
            if(var3.charAt(0) != 43 && var3.charAt(0) != 45) {
                if(var3.startsWith("GMT")) {
                    var4 = TimeZone.getTimeZone(var3);
                } else {
                    var4 = (TimeZone)this.b.get(var3);
                    if(var4 == null) {
                        throw new IllegalArgumentException(var3 + " is not a supported timezone name");
                    }
                }
            } else {
                var4 = TimeZone.getTimeZone("GMT" + var3);
            }

            var2.setTimeZone(var4);
        }
    }

    private static class b extends FastDateParser.c {
        private final int a;

        b(int var1) {
            //   super(null);
            this.a = var1;
        }

        boolean a() {
            return true;
        }

        boolean a(FastDateParser var1, StringBuilder var2) {
            if(var1.a()) {
                var2.append("(\\p{Nd}{").append(var1.b()).append("}+)");
            } else {
                var2.append("(\\p{Nd}++)");
            }

            return true;
        }

        void a(FastDateParser var1, Calendar var2, String var3) {
            var2.set(this.a, this.a(Integer.parseInt(var3)));
        }

        int a(int var1) {
            return var1;
        }
    }

    private static class d extends FastDateParser.c {
        private final int a;
        private final Map<String, Integer> b;

        d(int var1, Calendar var2, Locale var3) {
            //    super(null);
            this.a = var1;
            this.b = FastDateParser.b(var1, var2, var3);
        }

        boolean a(FastDateParser var1, StringBuilder var2) {
            var2.append('(');
            Iterator var3 = this.b.keySet().iterator();

            while(var3.hasNext()) {
                String var4 = (String)var3.next();
                FastDateParser.b(var2, var4, false).append('|');
            }

            var2.setCharAt(var2.length() - 1, ')');
            return true;
        }

        void a(FastDateParser var1, Calendar var2, String var3) {
            Integer var4 = (Integer)this.b.get(var3);
            if(var4 != null) {
                var2.set(this.a, var4.intValue());
            } else {
                StringBuilder var5 = new StringBuilder(var3);
                var5.append(" not in (");
                Iterator var6 = this.b.keySet().iterator();

                while(var6.hasNext()) {
                    String var7 = (String)var6.next();
                    var5.append(var7).append(' ');
                }

                var5.setCharAt(var5.length() - 1, ')');
                throw new IllegalArgumentException(var5.toString());
            }
        }
    }

    private static class a extends FastDateParser.c {
        private final String a;

        a(String var1) {
            //  super(null);
            this.a = var1;
        }

        boolean a() {
            char var1 = this.a.charAt(0);
            if(var1 == 39) {
                var1 = this.a.charAt(1);
            }

            return Character.isDigit(var1);
        }

        boolean a(FastDateParser var1, StringBuilder var2) {
            FastDateParser.b(var2, this.a, true);
            return false;
        }
    }

    private abstract static class c {
        private c() {
        }

        boolean a() {
            return false;
        }

        void a(FastDateParser var1, Calendar var2, String var3) {
        }

        abstract boolean a(FastDateParser var1, StringBuilder var2);
    }
}
