package com.haohai.platform.platformmodel.ui.service;

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
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.haohai.platform.platformmodel.R;

import java.util.List;

import static android.app.Notification.FLAG_NO_CLEAR;

public class ForegroundService extends Service {
    private static final int NOTIFICATION_ID = 2;
    private static final String TAG = ForegroundService.class.getSimpleName();
    private static final int FLAG_ONGOING_EVENT = 12;
    public static boolean serviceIsLive = false;
    private double currentLongitude = 0.00;
    private double currentLatitude = 0.00;
    private NotificationManager notificationManager;

    public ForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 获取服务通知
        serviceIsLive = true;
        getLocation();
        Notification notification = createForegroundNotification();
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, notification);
    }

    /**
     * 创建服务通知
     */
    private Notification createForegroundNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 唯一的通知通道的id.
        String notificationChannelId = "notification_channel_id_01";

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //用户可见的通道名称
            String channelName = "Foreground Service Notification";
            //通道的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("Channel description");
            //LED灯
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //震动
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
        //通知小图标
        builder.setSmallIcon(R.drawable.ic_launcher);
        //通知标题
        builder.setContentTitle("后台定位服务");
        //通知内容
        builder.setContentText("大平台后台服务");
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());
        //设定启动的内容
/*        Intent activityIntent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);*/

        //创建通知并返回
        return builder.build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        // 标记服务启动
        serviceIsLive = true;
        // 数据获取
        String data = intent.getStringExtra("Foreground");
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 标记服务关闭
        serviceIsLive = false;
        // 移除通知
        stopForeground(true);
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
                Log.e(TAG, "onLocationChanged: 经纬度发生改变了" + currentLongitude +"   " +currentLatitude);
                initTongzhiLan();
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

            currentLongitude = longitude ;
            currentLatitude = latitude ;
            initTongzhiLan();
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

    private void initTongzhiLan() {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//高版本需要渠道
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            //只在Android O之上需要渠道，这里的第一个参数要和下面的channelId一样
            NotificationChannel notificationChannel = new NotificationChannel("10","name",NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
            manager.createNotificationChannel(notificationChannel);
        }
//这里的第二个参数要和上面的第一个参数一样

        Notification notification = new Notification.Builder(getApplicationContext(), "10")

                .setContentTitle("当前位置")
                .setContentText("纬度" + currentLatitude + ",经度" + currentLongitude)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSound(null, importance)
                .setOnlyAlertOnce(true)
                .build();
        notification.flags |= FLAG_ONGOING_EVENT;
        notification.flags |= FLAG_NO_CLEAR;
        manager.notify(10,notification);
    }
}
