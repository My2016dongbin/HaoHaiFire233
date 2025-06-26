package com.skyline.terraexplorer.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.models.PositionModel;
import com.skyline.terraexplorer.models.TrackModel;
import com.skyline.terraexplorer.utils.RequestUtils;

import org.xutils.common.Callback;
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
    private List<PositionModel> positionModelList;

    private Timer timer;
    private String currentLongitude = "";
    private String currentLatitude = "";
    private String oldLongitude = "";
    private String oldLatitude = "";
    private String currentTime;
    private static final int TIME_CHANGE = 10;
    public TrackService() {
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == TIME_CHANGE){
                Date date = new Date();
                String time = date.toLocaleString();
                Log.e(TAG, "handleMessage: 时间time为" +time);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentTime = dateFormat.format(date);

                if (currentLatitude.equals("") || currentLatitude== null || currentLongitude.equals("") || currentLongitude == null){
                    Toast.makeText(TrackService.this, "定位失败，正在重新开启定位", Toast.LENGTH_SHORT).show();
                    getLocation();
                    return;
                }

                //   Log.e(TAG, "handleMessage: " + user.getIsLogin());
                User user = new DbConfig(getApplicationContext()).getUser();
                if (user != null){      //用户不存在
                    if (user.getIsLogin() == 1){
                        if (currentLatitude.equals(oldLatitude) && currentLongitude.equals(oldLongitude)){      //经纬度不变
                            // Toast.makeText(this, "经纬度没发生改变", Toast.LENGTH_SHORT).show();
                        }else {     //经纬度变化了
                           // Toast.makeText(TrackService.this, "经纬度变化当前经度：" + currentLongitude +"当前纬度：" + currentLatitude, Toast.LENGTH_SHORT).show();
                            oldLongitude = currentLongitude;
                            oldLatitude = currentLatitude;
                            //上传运动轨迹信息
                            postTrackDataToService(user);
                        }
                    }
                }
            }
        }
    };

    private void postTrackDataToService(User user) {
        PositionModel positionModel = new PositionModel(currentLongitude, currentLatitude);
        TrackModel trackModel = new TrackModel(user.getFullName(), positionModel);
        Gson gson = new Gson();

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/trajectory");
        params.setAsJsonContent(true);
        params.setBodyContent(gson.toJson(trackModel));
        params.addHeader("Authorization","bearer " + user.getToken());
        Log.e(TAG, "postData:-- jsonObject.toString()--" + gson.toJson(trackModel));
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:service- " + result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: service" );

        positionModelList = new ArrayList<>();
        getLocation();
        Log.e(TAG, "onCreate: service2" );
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // (1) 使用handler发送消息
                Message message=new Message();
                message.what=TIME_CHANGE;
                mHandler.sendMessage(message);
            }
        },0, 5000);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行

    }


    /**
     * 获取当前位置经纬度
     * @return
     */
    // @JavascriptInterface
    @SuppressLint("MissingPermission")
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

                currentLongitude = longitude+"";
                currentLatitude = latitude+"";
                //   Toast.makeText(MainActivity.this, "经纬度发生改变了,经度" +longitude + "纬度" +latitude, Toast.LENGTH_SHORT).show();
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
            currentLongitude = longitude +"";
            currentLatitude = latitude + "";
         /*   BigDecimal   la   =   new BigDecimal(latitude);
            double   lat = la.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();*/
            //    return longitude + "," + latitude;
            //   return "0.00,0.00";
        }else {
            //  return "0.00,0.00";
        }
    }
}
