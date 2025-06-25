package com.ruyiruyi.rylibrary.time;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Lenovo on 2018/3/12.
 */

public class FastDatePrinter implements DatePrinter, Serializable {
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private final String a;
    private final TimeZone b;
    private final Locale c;
    private transient FastDatePrinter.Rule[] d;
    private transient int e;
    private static final ConcurrentMap<FastDatePrinter.e, String> f = new ConcurrentHashMap(7);

    protected FastDatePrinter(String var1, TimeZone var2, Locale var3) {
        this.a = var1;
        this.b = var2;
        this.c = var3;
        this.a();
    }

    private void a() {
        List var1 = this.parsePattern();
        this.d = (FastDatePrinter.Rule[])var1.toArray(new FastDatePrinter.Rule[var1.size()]);
        int var2 = 0;
        int var3 = this.d.length;

        while(true) {
            --var3;
            if(var3 < 0) {
                this.e = var2;
                return;
            }

            var2 += this.d[var3].estimateLength();
        }
    }

    protected List<FastDatePrinter.Rule> parsePattern() {
        DateFormatSymbols var1 = new DateFormatSymbols(this.c);
        ArrayList var2 = new ArrayList();
        String[] var3 = var1.getEras();
        String[] var4 = var1.getMonths();
        String[] var5 = var1.getShortMonths();
        String[] var6 = var1.getWeekdays();
        String[] var7 = var1.getShortWeekdays();
        String[] var8 = var1.getAmPmStrings();
        int var9 = this.a.length();
        int[] var10 = new int[1];

        for(int var11 = 0; var11 < var9; ++var11) {
            var10[0] = var11;
            String var12 = this.parseToken(this.a, var10);
            var11 = var10[0];
            int var13 = var12.length();
            if(var13 == 0) {
                break;
            }

            char var15 = var12.charAt(0);
            Object var14;
            switch(var15) {
                case '\'':
                    String var16 = var12.substring(1);
                    if(var16.length() == 1) {
                        var14 = new FastDatePrinter.a(var16.charAt(0));
                    } else {
                        var14 = new FastDatePrinter.c(var16);
                    }
                    break;
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
                    throw new IllegalArgumentException("Illegal pattern component: " + var12);
                case 'D':
                    var14 = this.selectNumberRule(6, var13);
                    break;
                case 'E':
                    var14 = new FastDatePrinter.d(7, var13 < 4?var7:var6);
                    break;
                case 'F':
                    var14 = this.selectNumberRule(8, var13);
                    break;
                case 'G':
                    var14 = new FastDatePrinter.d(0, var3);
                    break;
                case 'H':
                    var14 = this.selectNumberRule(11, var13);
                    break;
                case 'K':
                    var14 = this.selectNumberRule(10, var13);
                    break;
                case 'M':
                    if(var13 >= 4) {
                        var14 = new FastDatePrinter.d(2, var4);
                    } else if(var13 == 3) {
                        var14 = new FastDatePrinter.d(2, var5);
                    } else if(var13 == 2) {
                        var14 = FastDatePrinter.j.a;
                    } else {
                        var14 = FastDatePrinter.m.a;
                    }
                    break;
                case 'S':
                    var14 = this.selectNumberRule(14, var13);
                    break;
                case 'W':
                    var14 = this.selectNumberRule(4, var13);
                    break;
                case 'Z':
                    if(var13 == 1) {
                        var14 = FastDatePrinter.g.b;
                    } else {
                        var14 = FastDatePrinter.g.a;
                    }
                    break;
                case 'a':
                    var14 = new FastDatePrinter.d(9, var8);
                    break;
                case 'd':
                    var14 = this.selectNumberRule(5, var13);
                    break;
                case 'h':
                    var14 = new FastDatePrinter.h(this.selectNumberRule(10, var13));
                    break;
                case 'k':
                    var14 = new FastDatePrinter.i(this.selectNumberRule(11, var13));
                    break;
                case 'm':
                    var14 = this.selectNumberRule(12, var13);
                    break;
                case 's':
                    var14 = this.selectNumberRule(13, var13);
                    break;
                case 'w':
                    var14 = this.selectNumberRule(3, var13);
                    break;
                case 'y':
                    if(var13 == 2) {
                        var14 = FastDatePrinter.l.a;
                    } else {
                        var14 = this.selectNumberRule(1, var13 < 4?4:var13);
                    }
                    break;
                case 'z':
                    if(var13 >= 4) {
                        var14 = new FastDatePrinter.f(this.b, this.c, 1);
                    } else {
                        var14 = new FastDatePrinter.f(this.b, this.c, 0);
                    }
            }

            var2.add(var14);
        }

        return var2;
    }

    protected String parseToken(String var1, int[] var2) {
        StringBuilder var3 = new StringBuilder();
        int var4 = var2[0];
        int var5 = var1.length();
        char var6 = var1.charAt(var4);
        if(var6 >= 65 && var6 <= 90 || var6 >= 97 && var6 <= 122) {
            var3.append(var6);

            while(var4 + 1 < var5) {
                char var8 = var1.charAt(var4 + 1);
                if(var8 != var6) {
                    break;
                }

                var3.append(var6);
                ++var4;
            }
        } else {
            var3.append('\'');

            for(boolean var7 = false; var4 < var5; ++var4) {
                var6 = var1.charAt(var4);
                if(var6 == 39) {
                    if(var4 + 1 < var5 && var1.charAt(var4 + 1) == 39) {
                        ++var4;
                        var3.append(var6);
                    } else {
                        var7 = !var7;
                    }
                } else {
                    if(!var7 && (var6 >= 65 && var6 <= 90 || var6 >= 97 && var6 <= 122)) {
                        --var4;
                        break;
                    }

                    var3.append(var6);
                }
            }
        }

        var2[0] = var4;
        return var3.toString();
    }

    protected FastDatePrinter.NumberRule selectNumberRule(int var1, int var2) {
        switch(var2) {
            case 1:
                return new FastDatePrinter.n(var1);
            case 2:
                return new FastDatePrinter.k(var1);
            default:
                return new FastDatePrinter.b(var1, var2);
        }
    }

    public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
        if(var1 instanceof Date) {
            return this.format((Date)var1, var2);
        } else if(var1 instanceof Calendar) {
            return this.format((Calendar)var1, var2);
        } else if(var1 instanceof Long) {
            return this.format(((Long)var1).longValue(), var2);
        } else {
            throw new IllegalArgumentException("Unknown class: " + (var1 == null?"<null>":var1.getClass().getName()));
        }
    }

    public String format(long var1) {
        GregorianCalendar var3 = this.b();
        var3.setTimeInMillis(var1);
        return this.a(var3);
    }

    private String a(Calendar var1) {
        return this.applyRules(var1, new StringBuffer(this.e)).toString();
    }

    private GregorianCalendar b() {
        return new GregorianCalendar(this.b, this.c);
    }

    public String format(Date var1) {
        GregorianCalendar var2 = this.b();
        var2.setTime(var1);
        return this.a(var2);
    }

    public String format(Calendar var1) {
        return this.format(var1, new StringBuffer(this.e)).toString();
    }

    public StringBuffer format(long var1, StringBuffer var3) {
        return this.format(new Date(var1), var3);
    }

    public StringBuffer format(Date var1, StringBuffer var2) {
        GregorianCalendar var3 = this.b();
        var3.setTime(var1);
        return this.applyRules(var3, var2);
    }

    public StringBuffer format(Calendar var1, StringBuffer var2) {
        return this.applyRules(var1, var2);
    }

    protected StringBuffer applyRules(Calendar var1, StringBuffer var2) {
        FastDatePrinter.Rule[] var3 = this.d;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            FastDatePrinter.Rule var6 = var3[var5];
            var6.appendTo(var2, var1);
        }

        return var2;
    }

    public String getPattern() {
        return this.a;
    }

    public TimeZone getTimeZone() {
        return this.b;
    }

    public Locale getLocale() {
        return this.c;
    }

    public int getMaxLengthEstimate() {
        return this.e;
    }

    public boolean equals(Object var1) {
        if(!(var1 instanceof FastDatePrinter)) {
            return false;
        } else {
            FastDatePrinter var2 = (FastDatePrinter)var1;
            return this.a.equals(var2.a) && this.b.equals(var2.b) && this.c.equals(var2.c);
        }
    }

    public int hashCode() {
        return this.a.hashCode() + 13 * (this.b.hashCode() + 13 * this.c.hashCode());
    }

    public String toString() {
        return "FastDatePrinter[" + this.a + "," + this.c + "," + this.b.getID() + "]";
    }

    static String a(TimeZone var0, boolean var1, int var2, Locale var3) {
        FastDatePrinter.e var4 = new FastDatePrinter.e(var0, var1, var2, var3);
        String var5 = (String)f.get(var4);
        if(var5 == null) {
            var5 = var0.getDisplayName(var1, var2, var3);
            String var6 = (String)f.putIfAbsent(var4, var5);
            if(var6 != null) {
                var5 = var6;
            }
        }

        return var5;
    }

    private static class e {
        private final TimeZone a;
        private final int b;
        private final Locale c;

        e(TimeZone var1, boolean var2, int var3, Locale var4) {
            this.a = var1;
            if(var2) {
                this.b = var3 | -2147483648;
            } else {
                this.b = var3;
            }

            this.c = var4;
        }

        public int hashCode() {
            return (this.b * 31 + this.c.hashCode()) * 31 + this.a.hashCode();
        }

        public boolean equals(Object var1) {
            if(this == var1) {
                return true;
            } else if(!(var1 instanceof FastDatePrinter.e)) {
                return false;
            } else {
                FastDatePrinter.e var2 = (FastDatePrinter.e)var1;
                return this.a.equals(var2.a) && this.b == var2.b && this.c.equals(var2.c);
            }
        }
    }

    private static class g implements FastDatePrinter.Rule {
        static final FastDatePrinter.g a = new FastDatePrinter.g(true);
        static final FastDatePrinter.g b = new FastDatePrinter.g(false);
        final boolean c;

        g(boolean var1) {
            this.c = var1;
        }

        public int estimateLength() {
            return 5;
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            int var3 = var2.get(15) + var2.get(16);
            if(var3 < 0) {
                var1.append('-');
                var3 = -var3;
            } else {
                var1.append('+');
            }

            int var4 = var3 / 3600000;
            var1.append((char)(var4 / 10 + 48));
            var1.append((char)(var4 % 10 + 48));
            if(this.c) {
                var1.append(':');
            }

            int var5 = var3 / '\uea60' - 60 * var4;
            var1.append((char)(var5 / 10 + 48));
            var1.append((char)(var5 % 10 + 48));
        }
    }

    private static class f implements FastDatePrinter.Rule {
        private final Locale a;
        private final int b;
        private final String c;
        private final String d;

        f(TimeZone var1, Locale var2, int var3) {
            this.a = var2;
            this.b = var3;
            this.c = FastDatePrinter.a(var1, false, var3, var2);
            this.d = FastDatePrinter.a(var1, true, var3, var2);
        }

        public int estimateLength() {
            return Math.max(this.c.length(), this.d.length());
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            TimeZone var3 = var2.getTimeZone();
            if(var3.useDaylightTime() && var2.get(16) != 0) {
                var1.append(FastDatePrinter.a(var3, true, this.b, this.a));
            } else {
                var1.append(FastDatePrinter.a(var3, false, this.b, this.a));
            }

        }
    }

    private static class i implements FastDatePrinter.NumberRule {
        private final FastDatePrinter.NumberRule a;

        i(FastDatePrinter.NumberRule var1) {
            this.a = var1;
        }

        public int estimateLength() {
            return this.a.estimateLength();
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            int var3 = var2.get(11);
            if(var3 == 0) {
                var3 = var2.getMaximum(11) + 1;
            }

            this.a.appendTo(var1, var3);
        }

        public void appendTo(StringBuffer var1, int var2) {
            this.a.appendTo(var1, var2);
        }
    }

    private static class h implements FastDatePrinter.NumberRule {
        private final FastDatePrinter.NumberRule a;

        h(FastDatePrinter.NumberRule var1) {
            this.a = var1;
        }

        public int estimateLength() {
            return this.a.estimateLength();
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            int var3 = var2.get(10);
            if(var3 == 0) {
                var3 = var2.getLeastMaximum(10) + 1;
            }

            this.a.appendTo(var1, var3);
        }

        public void appendTo(StringBuffer var1, int var2) {
            this.a.appendTo(var1, var2);
        }
    }

    private static class j implements FastDatePrinter.NumberRule {
        static final FastDatePrinter.j a = new FastDatePrinter.j();

        j() {
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            this.appendTo(var1, var2.get(2) + 1);
        }

        public final void appendTo(StringBuffer var1, int var2) {
            var1.append((char)(var2 / 10 + 48));
            var1.append((char)(var2 % 10 + 48));
        }
    }

    private static class l implements FastDatePrinter.NumberRule {
        static final FastDatePrinter.l a = new FastDatePrinter.l();

        l() {
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            this.appendTo(var1, var2.get(1) % 100);
        }

        public final void appendTo(StringBuffer var1, int var2) {
            var1.append((char)(var2 / 10 + 48));
            var1.append((char)(var2 % 10 + 48));
        }
    }

    private static class k implements FastDatePrinter.NumberRule {
        private final int a;

        k(int var1) {
            this.a = var1;
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            this.appendTo(var1, var2.get(this.a));
        }

        public final void appendTo(StringBuffer var1, int var2) {
            if(var2 < 100) {
                var1.append((char)(var2 / 10 + 48));
                var1.append((char)(var2 % 10 + 48));
            } else {
                var1.append(Integer.toString(var2));
            }

        }
    }

    private static class b implements FastDatePrinter.NumberRule {
        private final int a;
        private final int b;

        b(int var1, int var2) {
            if(var2 < 3) {
                throw new IllegalArgumentException();
            } else {
                this.a = var1;
                this.b = var2;
            }
        }

        public int estimateLength() {
            return 4;
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            this.appendTo(var1, var2.get(this.a));
        }

        public final void appendTo(StringBuffer var1, int var2) {
            int var3;
            if(var2 < 100) {
                var3 = this.b;

                while(true) {
                    --var3;
                    if(var3 < 2) {
                        var1.append((char)(var2 / 10 + 48));
                        var1.append((char)(var2 % 10 + 48));
                        break;
                    }

                    var1.append('0');
                }
            } else {
                if(var2 < 1000) {
                    var3 = 3;
                } else {
                    var3 = Integer.toString(var2).length();
                }

                int var4 = this.b;

                while(true) {
                    --var4;
                    if(var4 < var3) {
                        var1.append(Integer.toString(var2));
                        break;
                    }

                    var1.append('0');
                }
            }

        }
    }

    private static class m implements FastDatePrinter.NumberRule {
        static final FastDatePrinter.m a = new FastDatePrinter.m();

        m() {
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            this.appendTo(var1, var2.get(2) + 1);
        }

        public final void appendTo(StringBuffer var1, int var2) {
            if(var2 < 10) {
                var1.append((char)(var2 + 48));
            } else {
                var1.append((char)(var2 / 10 + 48));
                var1.append((char)(var2 % 10 + 48));
            }

        }
    }

    private static class n implements FastDatePrinter.NumberRule {
        private final int a;

        n(int var1) {
            this.a = var1;
        }

        public int estimateLength() {
            return 4;
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            this.appendTo(var1, var2.get(this.a));
        }

        public final void appendTo(StringBuffer var1, int var2) {
            if(var2 < 10) {
                var1.append((char)(var2 + 48));
            } else if(var2 < 100) {
                var1.append((char)(var2 / 10 + 48));
                var1.append((char)(var2 % 10 + 48));
            } else {
                var1.append(Integer.toString(var2));
            }

        }
    }

    private static class d implements FastDatePrinter.Rule {
        private final int a;
        private final String[] b;

        d(int var1, String[] var2) {
            this.a = var1;
            this.b = var2;
        }

        public int estimateLength() {
            int var1 = 0;
            int var2 = this.b.length;

            while(true) {
                --var2;
                if(var2 < 0) {
                    return var1;
                }

                int var3 = this.b[var2].length();
                if(var3 > var1) {
                    var1 = var3;
                }
            }
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            var1.append(this.b[var2.get(this.a)]);
        }
    }

    private static class c implements FastDatePrinter.Rule {
        private final String a;

        c(String var1) {
            this.a = var1;
        }

        public int estimateLength() {
            return this.a.length();
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            var1.append(this.a);
        }
    }

    private static class a implements FastDatePrinter.Rule {
        private final char a;

        a(char var1) {
            this.a = var1;
        }

        public int estimateLength() {
            return 1;
        }

        public void appendTo(StringBuffer var1, Calendar var2) {
            var1.append(this.a);
        }
    }

    private interface NumberRule extends FastDatePrinter.Rule {
        void appendTo(StringBuffer var1, int var2);
    }

    private interface Rule {
        int estimateLength();

        void appendTo(StringBuffer var1, Calendar var2);
    }
}
