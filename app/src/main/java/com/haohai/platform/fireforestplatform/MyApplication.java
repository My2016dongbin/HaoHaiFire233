package com.haohai.platform.fireforestplatform;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.haohai.platform.fireforestplatform.ui.rxhttp.RequestUtils;
import com.haohai.platform.fireforestplatform.ui.utils.TraceServiceImpl;
import com.ruyiruyi.rylibrary.route.Action;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.xdandroid.hellodaemon.DaemonEnv;

import org.xutils.x;

import java.util.Enumeration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

import dalvik.system.DexFile;
import okhttp3.OkHttpClient;
import rxhttp.RxHttpPlugins;

/**
 * Created by geyang on 2020/6/2.
 */

public class MyApplication extends Application {

    private ConcurrentSkipListMap<String, Class> map;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDebug(true);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        initTengxunTuisong();


        //路由配置
        ARouter.openLog();     // Print log
        ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        ARouter.init(this); // As early as possible, it is recommended to initialize in the Application

        getAllActivities(this);


        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        //网易云信配置
       // NIMClient.init(this, loginInfo(), options());
        // 使用 `NIMUtil` 类可以进行主进程判断。
        // boolean mainProcess = NIMUtil.isMainProcess(context)
      //  if (NIMUtil.isMainProcess(this)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
      //  }
        DaemonEnv.initialize(this, TraceServiceImpl.class,DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
        TraceServiceImpl.sShouldStopService=false;
        DaemonEnv.startServiceMayBind(TraceServiceImpl.class);
    }

    private void initTengxunTuisong() {
        //腾讯推送
        XGPushConfig.enableDebug(this,true);
        //开启小米推送
        XGPushConfig.setMiPushAppId(getApplicationContext(), "2882303761518845436");
        XGPushConfig.setMiPushAppKey(getApplicationContext(), "5981884523436");

        //oppo推送
        XGPushConfig.setOppoPushAppId(getApplicationContext(), "93a4e3e3bcb3448387c2af2eadc1f4ee");
        XGPushConfig.setOppoPushAppKey(getApplicationContext(), "e317f095327f4ca0bf3f2837f9fece03");

        //打开第三方推送  华为推送
        XGPushConfig.enableOtherPush(getApplicationContext(), true);


        //设置魅族APPID和APPKEY
        XGPushConfig.setMzPushAppId(this, "136981");
        XGPushConfig.setMzPushAppKey(this, "ae5633cd5a4146a1832b7cc3caa4d205");


    }

    // 如果提供，将同时进行自动登录。如果当前还没有登录用户，请传入null。详见自动登录章节。
   /* private LoginInfo loginInfo() {

        return null;
    }*/

    // 设置初始化配置参数，如果返回值为 null，则全部使用默认参数。
    /*private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;


        return options;
    }*/

    private void getAllActivities(Context ctx){
        try {
            //通过资源路径获得DexFile
            DexFile e = new DexFile(ctx.getPackageResourcePath());
            Enumeration entries = e.entries();
            //遍历所有元素
            while(entries.hasMoreElements()) {
                String entryName = (String)entries.nextElement();
                //匹配Activity包名与类名
                if(entryName.contains("activity") && entryName.contains("Activity")) {
                    //通过反射获得Activity类
                    Class entryClass = Class.forName(entryName);
                    if(entryClass.isAnnotationPresent(Action.class)) {
                        Action action = (Action)entryClass.getAnnotation(Action.class);
                        this.map.put(action.value(), entryClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
