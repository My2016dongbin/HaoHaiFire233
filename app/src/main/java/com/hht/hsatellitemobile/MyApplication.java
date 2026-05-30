package com.hht.hsatellitemobile;

import android.app.Activity;
import android.app.Application;
import android.util.Log;


import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 13589 on 2019/8/2.
 */

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();
    private List<Activity> oList;
    @Override
    public void onCreate() {
        super.onCreate();


        //initXinGe();



        //初始化极光
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDebug(true);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        oList = new ArrayList<Activity>();
    }

    private void initXinGe() {
        //开启小米推送
        XGPushConfig.setMiPushAppId(getApplicationContext(), "2882303761518735264");
        XGPushConfig.setMiPushAppKey(getApplicationContext(), "5631873515264");
        //打开第三方推送
        XGPushConfig.enableOtherPush(getApplicationContext(), true);

        XGPushConfig.enableDebug(this,true);
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.e("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
    }

    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
        //判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }

}
