package com.haohai.platform.fireforestplatform.ui.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.haohai.platform.fireforestplatform.MainActivity;
import com.haohai.platform.fireforestplatform.R;
import com.haohai.platform.fireforestplatform.ui.listener.MyLocationListener;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.haohai.platform.platformmodel.ui.model.PositionModel;
import com.haohai.platform.platformmodel.ui.utils.TrackReceiver;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.utils.CommonData;
import com.ruyiruyi.rylibrary.utils.LatLngChangeNew;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TrackService extends Service {

    private static final String TAG = TrackService.class.getSimpleName();
    private Timer timer;
    public static int state = 0;  //0更改用户位置  1上传用户轨迹


    private double currentLongitude;
    private double currentLatitude;
    private String oldLongitude = "";
    private String oldLatitude = "";
    private String currentTime;
    private long currentLongTime;
    private static final int TIME_CHANGE = 10;
    private  List<PositionModel> positionModelList;
    private TrackReceiver trackReceiver;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == TIME_CHANGE){
                //Log.e(TAG, "service: 获取时间" );
                Date date = new Date();
                String time = date.toLocaleString();
                Log.e(TAG, "时间time为： " + time);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentTime = dateFormat.format(date);
                currentLongTime = date.getTime();
                //获取经纬度（百度地图）
                getBaiduLocation();
                User user = new DbConfig(getApplicationContext()).getUser();
                if (user != null){      //用户不存在
                    if (user.getIsLogin() == 1){
                        initLocation(user);
                    }
                }
            }
        }
    };

    private void initLocation(User user) {
        changeUserPosition(user);
    }

    private void changeUserPosition(User user) {
        double[] doubles = new LatLngChangeNew().calBD09toWGS84(CommonData.lat, CommonData.lng);
        double latParse = doubles[0];
        double lngParse = doubles[1];

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",user.getId());

            JSONObject posObj = new JSONObject();
            if(CommonData.lat!=0&&CommonData.lng!=0){
                posObj.put("lat",latParse);
                posObj.put("lng",lngParse);
                //存储位置信息
                DbConfig dbConfig = new DbConfig(getApplicationContext());
                DbManager db = dbConfig.getDbManager();
                User userM = dbConfig.getUser();
                if(userM == null){
                    return;
                }
                userM.setLatitude(CommonData.lat);
                userM.setLongitude(CommonData.lng);
                try {
                    db.delete(User.class);
                    db.saveOrUpdate(userM);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }else{
                return;
            }

            jsonObject.put("position",posObj);
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "oa/api/trajectory/trackUpload");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.setConnectTimeout(10000);
        Log.e(TAG, "service---" + params);
        Log.e(TAG, "service---" + jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
               Log.e(TAG, "onSuccess:更新用户位置成功 " + result);

              //  Toast.makeText(TrackService.this, "", Toast.LENGTH_SHORT).show();
                Log.i("UpdateGUI: ","11111");
                //broadcast
                // service 通过广播来更新GUI
                Intent intent=new Intent();
                intent.putExtra("message",jsonObject.toString());
                intent.setAction("zcd.voicerobot");
                sendBroadcast(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    public TrackService() {
    }

    private void getBaiduLocation() {
        Log.e(TAG, "getBaiduLocation: bingo" );
        mLocationClient.start();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initBaiduLoc();

        positionModelList = new ArrayList<>();
        initBaiduLoc();
        Log.e(TAG, "onCreate: 1");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "onCreate: 2");
                Message message=new Message();
                message.what=TIME_CHANGE;
                mHandler.sendMessage(message);   //需要开启轨迹上传 打开
            }
        },0, 10000);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行


    }



    void initBaiduLoc(){
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        //LocationMode.Fuzzy_Locating, 模糊定位模式；v9.2.8版本开始支持，可以降低API的调用频率，但同时也会降低定位精度；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true);
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), com.ruyiruyi.rylibrary.R.raw.alive_voice);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();*/

        Notification notification;
        Intent intent1 = new Intent(this, MainActivity.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("location","location", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(this,"location");
            builder.setContentIntent(PendingIntent.getActivity(this,0,intent1,0))
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(getString(R.string.app_name))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("为您持续巡护中...")
                    .setWhen(System.currentTimeMillis());
            notification = builder.build();
        }else{
            notification = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("为您持续巡护中...")
                    .setContentIntent(PendingIntent.getActivity(this,0,intent1,0))
                    .build();
        }
        startForeground(110,notification);

        return START_STICKY;
    }




    /**
     * 获取当前位置经纬度
     * @return
     */
    // @JavascriptInterface
    public void getLocation() {
        //获得位置服务
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_HIGH);//低功耗

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0.0001f, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double longitude = 0.00;
                double latitude = 0.00;
                try {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }catch (Exception e){

                }
                
                currentLongitude = longitude;
                currentLatitude = latitude;
                 //  Toast.makeText(TrackService.this, "经纬度发生改变了,经度" +longitude + "纬度" +latitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(), "GPS已开启", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(), "请打开GPS", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Toast.makeText(this, "请打开GPS和使用网络定位以提高精度", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        // 获取最好的定位方式
        String provider = locationManager.getBestProvider(criteria, true); // true 代表从打开的设备中查找

        // 获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        // 测试一般都在室内，这里颠倒了书上的判断顺序
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(this, "Please Open Your GPS or Location Service", Toast.LENGTH_SHORT).show();
            return;
        }


        //有位置提供器的情况
        if (provider != null) {
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // return null;
            }
            Location location= locationManager.getLastKnownLocation(provider);
            double longitude = 0.00;
            double latitude = 0.00;
            try {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }catch (Exception e){

            }

            oldLongitude = longitude +"";
            oldLatitude = latitude + "";
            currentLongitude = longitude ;
            currentLatitude = latitude ;
            Log.e(TAG, "getLocation: --" + longitude);
            Log.e(TAG, "getLocation: *--" + latitude);
         /*   BigDecimal   la   =   new BigDecimal(latitude);
            double   lat = la.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();*/
            //    return longitude + "," + latitude;
            //   return "0.00,0.00";
        }else {
            //  return "0.00,0.00";
        }
    }


}
