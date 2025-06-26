package com.skyline.terraexplorer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.xutils.x;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

public class TEApp extends Application{

	private static Context appContext;
	private static Context activityContext;
	private static Context mainActivityContext;
	private List<Activity> oList;

	private Thread.UncaughtExceptionHandler androidDefaultUEH;

	private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
		public void uncaughtException(Thread thread, Throwable ex) {
			String path = Environment.getExternalStorageDirectory() + File.separator + "com.skyline.terraexplorer/files";
			File file = new File(path, "errors.txt");
			FileOutputStream stream = null;
			try {
				stream =  new FileOutputStream(file,true);
				stream.write((new Date().toGMTString() + ": ").getBytes());
				ex.printStackTrace(new PrintStream(stream));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally {
				if(stream != null)
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			//Log.e("TestApplication", "Uncaught exception is: ", ex);
			androidDefaultUEH.uncaughtException(thread, ex);
		}
	};

	public void onCreate(){
		super.onCreate();
		//初始化xUtils
		x.Ext.init(this);
		x.Ext.setDebug(true);

		//在使用SDK各组件之前初始化context信息，传入ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		//自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
		//包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
		SDKInitializer.setCoordType(CoordType.BD09LL);

		//初始化极光
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		initTPNS();
		oList = new ArrayList<Activity>();

		initialOkhttp(getApplicationContext());
		androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(handler);

		TEApp.appContext = getApplicationContext();
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

			@Override
			public void onActivityStopped(Activity activity) {
				// TODO Auto-generated method stub
				if(TEApp.activityContext == activity)
					TEApp.activityContext = null;
			}

			@Override
			public void onActivityStarted(Activity activity) {
				// TODO Auto-generated method stub
				TEApp.activityContext = activity;
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onActivityResumed(Activity activity) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onActivityPaused(Activity activity) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void initTPNS() {
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

		//开启华为推送
		XGPushConfig.enableOtherPush(getApplicationContext(), true);
		XGPushManager.registerPush(this, new XGIOperateCallback() {
			@Override
			public void onSuccess(Object data, int flag) {
				//token在设备卸载重装的时候有可能会变
				Log.d("TPush", "注册成功，设备token为：" + data);
			}

			@Override
			public void onFail(Object data, int errCode, String msg) {
				Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
			}
		});
	}

	/**
	 * 初始化okhttp
	 * @param c
	 */
	private  void  initialOkhttp(Context c)
	{

		ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(c));
		HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
				.writeTimeout(10000L,TimeUnit.MILLISECONDS)
				.cookieJar(cookieJar1)
				.hostnameVerifier(new HostnameVerifier()
				{
					@Override
					public boolean verify(String hostname, SSLSession session)
					{
						return true;
					}
				})
				.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
				.build();
		OkHttpUtils.initClient(okHttpClient);
	}
	public static Context getAppContext() {
		return TEApp.appContext;
	}
	public static Context getCurrentActivityContext() {
		if(TEApp.activityContext == null)
			return mainActivityContext;
		return TEApp.activityContext;
	}

	public static void setMainActivityContext(Context ctx)
	{
		mainActivityContext = ctx;
	}

	public static boolean isDebug() {
		return System.getProperty("DEBUG") != null;
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