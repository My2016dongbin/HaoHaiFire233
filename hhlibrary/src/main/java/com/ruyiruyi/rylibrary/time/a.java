package com.ruyiruyi.rylibrary.time;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Lenovo on 2018/3/12.
 */

abstract class a<F extends Format> {
    private final ConcurrentMap<a.b, F> a = new ConcurrentHashMap(7);
    private static final ConcurrentMap<a.b, String> b = new ConcurrentHashMap(7);

    a() {
    }

    public F a() {
        return this.a(3, 3, TimeZone.getDefault(), Locale.getDefault());
    }

    public F c(String var1, TimeZone var2, Locale var3) {
        if(var1 == null) {
            throw new NullPointerException("pattern must not be null");
        } else {
            if(var2 == null) {
                var2 = TimeZone.getDefault();
            }

            if(var3 == null) {
                var3 = Locale.getDefault();
            }

            a.b var4 = new a.b(new Object[]{var1, var2, var3});
            Format var5 = (Format)this.a.get(var4);
            if(var5 == null) {
                var5 = this.b(var1, var2, var3);
                Format var6 = (Format)this.a.putIfAbsent(var4, (F) var5);
                if(var6 != null) {
                    var5 = var6;
                }
            }

            return (F) var5;
        }
    }

    protected abstract F b(String var1, TimeZone var2, Locale var3);

    private F a(Integer var1, Integer var2, TimeZone var3, Locale var4) {
        if(var4 == null) {
            var4 = Locale.getDefault();
        }

        String var5 = a(var1, var2, var4);
        return this.c(var5, var3, var4);
    }

    F a(int var1, int var2, TimeZone var3, Locale var4) {
        return this.a(Integer.valueOf(var1), Integer.valueOf(var2), var3, var4);
    }

    F a(int var1, TimeZone var2, Locale var3) {
        return this.a(Integer.valueOf(var1), (Integer)null, var2, var3);
    }

    F b(int var1, TimeZone var2, Locale var3) {
        return this.a((Integer)null, Integer.valueOf(var1), var2, var3);
    }

    static String a(Integer var0, Integer var1, Locale var2) {
        a.b var3 = new a.b(new Object[]{var0, var1, var2});
        String var4 = (String)b.get(var3);
        if(var4 == null) {
            try {
                DateFormat var5;
                if(var0 == null) {
                    var5 = DateFormat.getTimeInstance(var1.intValue(), var2);
                } else if(var1 == null) {
                    var5 = DateFormat.getDateInstance(var0.intValue(), var2);
                } else {
                    var5 = DateFormat.getDateTimeInstance(var0.intValue(), var1.intValue(), var2);
                }

                var4 = ((SimpleDateFormat)var5).toPattern();
                String var6 = (String)b.putIfAbsent(var3, var4);
                if(var6 != null) {
                    var4 = var6;
                }
            } catch (ClassCastException var7) {
                throw new IllegalArgumentException("No date time pattern for locale: " + var2);
            }
        }

        return var4;
    }

    private static class b {
        private final Object[] a;
        private int b;

        public b(Object... var1) {
            this.a = var1;
        }

        public boolean equals(Object var1) {
            return Arrays.equals(this.a, ((a.b)var1).a);
        }

        public int hashCode() {
            if(this.b == 0) {
                int var1 = 0;
                Object[] var2 = this.a;
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    Object var5 = var2[var4];
                    if(var5 != null) {
                        var1 = var1 * 7 + var5.hashCode();
                    }
                }

                this.b = var1;
            }

            return this.b;
        }
    }
}
