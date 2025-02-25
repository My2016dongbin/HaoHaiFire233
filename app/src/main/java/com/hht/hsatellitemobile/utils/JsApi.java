package com.hht.hsatellitemobile.utils;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.hht.hsatellitemobile.JsToNativeDataInterface;

import wendu.dsbridge.CompletionHandler;

/**
 * Created by 13589 on 2019/8/6.
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
    public void androidShowFire(Object msg,CompletionHandler<String> handler) {
          handler.complete(msg+"回调给js");
        //  listener.onJsFireInfoClickListener(msg.toString());
        Log.e(TAG, "android 接收到js的id== " +msg);
        listener.onJsFireInfoClickListener(msg.toString().replace("CurrSelect",""));
    }

    /**
     * 加载地图的请求错误
     * @param msg
     * @param handler
     */
    @JavascriptInterface
    public void requestError(Object msg,CompletionHandler<String> handler) {
        handler.complete("回调给js");

        Log.e(TAG, "requestError: qinqiuchucuol;e");
        listener.onMapRequestErrorListener();
    }


    /**
     * 火点数据加载错误
     * @param msg
     * @param handler
     */
    @JavascriptInterface
    public void queryFireError(Object msg,CompletionHandler<String> handler) {
        handler.complete("回调给js");

        Log.e(TAG, "requestError: qinqiuchucuol;e");
        listener.onQuearFireErrorListener();
    }

    @JavascriptInterface
    public void goodsYingXiangReturn(Object msg, CompletionHandler<String> handler) {
        handler.complete(msg+"回调给js");
        Log.e(TAG, "goodsYingXiangReturn:GGGGG ");
    }

    @JavascriptInterface
    public  String IsLogin(Object msg)
    {
      /*  ToastUtils.showShort(msg.toString());*/
        return  msg.toString();
    }
    @JavascriptInterface
    public String saveUser(Object msg)
    {
     /*   // ToastUtils.showShort(msg.toString());
        SPUtils.getInstance().put("userContent",msg.toString());*/
        return  msg.toString();
    }
    @JavascriptInterface
    public  void GetAlarmCount(Object msg)
    {

      /*  callData.onRecieveMsg(msg);
        // ResponseData("alarmCount",msg.toString());
        //callData.onRecieveMsg(msg);*/
    }
    @JavascriptInterface
    public  String ClearLogin(Object msg)
    {
      /*  //ToastUtils.showShort(msg.toString());
        SPUtils.getInstance().put("userContent","");*/
        return  msg.toString();
    }

    private  void  ResponseData(String extra,Object msg)
    {

      /*  Intent intent=new Intent();
        intent.putExtra(extra, msg.toString());
        intent.setAction("com.osanwen.nettydemo.NettyService");
        context.sendBroadcast(intent);*/
    }

    public interface OnJsClickListener{
        void onJsFireInfoClickListener(String id);
        void onMapRequestErrorListener();
        void onQuearFireErrorListener();
    }

}
