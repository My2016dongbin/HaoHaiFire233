package com.skyline.terraexplorer.wisdomgarden.utils;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.skyline.terraexplorer.wisdomgarden.models.point;

import wendu.dsbridge.CompletionHandler;

public class JsUtils {
    Gson gson = new Gson();
    String TAG="point";
    //同步API
    @JavascriptInterface
    public String testSyn(Object msg)  {
        return msg + "［syn call］";
    }

    //异步API
    @JavascriptInterface
    public void testAsyn(Object msg, CompletionHandler<String> handler) {
        handler.complete(msg+" [ asyn call]");
        Log.e(TAG, "testAsyn: "+msg );
    }
    //获取点位坐标
    @JavascriptInterface
    public void givejavapoint(Object msg, CompletionHandler<String> handler) {
        handler.complete(msg+" [ asyn call]");
        point point=gson.fromJson(String.valueOf(msg), point.class);
        PrefsManager.getpoint();
        Log.e(TAG,point.latitude);
    }
}
