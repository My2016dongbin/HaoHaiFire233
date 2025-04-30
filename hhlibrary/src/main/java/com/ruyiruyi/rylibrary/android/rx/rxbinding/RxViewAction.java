package com.ruyiruyi.rylibrary.android.rx.rxbinding;

import android.view.View;
import com.jakewharton.rxbinding.view.RxView;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import rx.functions.Func0;


public class RxViewAction {
    public RxViewAction() {
    }

    public static rx.Observable<Void> click(View var0) {
        return RxView.clicks(var0);
    }

    public static rx.Observable<Void> clickNoDouble(View var0) {
        return RxView.clicks(var0).throttleFirst(500L, TimeUnit.MILLISECONDS);
    }

    public static rx.Observable<Void> longClick(View var0) {
        return RxView.longClicks(var0);
    }

    public static rx.Observable<Void> longClick(View var0, Func0<Boolean> var1) {
        return RxView.longClicks(var0, var1);
    }
}
