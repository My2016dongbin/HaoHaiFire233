package com.haohai.platform.mapmodel.Utils;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import wendu.dsbridge.CompletionHandler;

/**
 * Created by geyang on 2020/7/21.
 */

public class JsApi {
    private static final String TAG = JsApi.class.getSimpleName();
    public JsToNativeDataInterface callData;
    public Context context;
    public OnJsClickListener listener;
    public  JsApi(Context c,OnJsClickListener listener1)
    {
        context=c;
        listener=listener1;
    }

    public void setListener(OnJsClickListener listener) {
        this.listener = listener;
    }

    public JsApi(Context context) {
        this.context = context;
    }




    /**
     * 地图上的点的点击回调
     * @param msg
     * @param handler
     */
    @JavascriptInterface
    public void showAndroidEscapeDetails(Object msg,CompletionHandler<String> handler) {
        handler.complete(msg+"回调给js");
        Log.e(TAG, "android 接收到js的id== " +msg.toString());
        listener.onJsEscapeDetailsClickListener(msg.toString());
    }
    /**
     * 地图上的点的点击回调
     * @param msg
     * @param handler
     */
    @JavascriptInterface
    public void pointclick(Object msg,CompletionHandler<String> handler) {
        Log.e(TAG, "android 接收到js的id== " +msg.toString());
        listener.onJsEscapeDetailsClickListener(msg.toString());
    }

    public interface OnJsClickListener{
        void onJsEscapeDetailsClickListener(String json);
    }
}
