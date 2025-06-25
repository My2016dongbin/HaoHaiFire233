package com.haohai.platform.platformmodel.ui.service;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.haohai.platform.platformmodel.ui.model.PositionModel;
import com.haohai.platform.platformmodel.ui.utils.TrackReceiver;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.utils.CommonUtil;

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
    private final int uploadTime = 10000;//每10秒上传位置
    private final int downTime = 15;//15分钟后下线
    private int timeTokenDown = 0;
    private static final int TIME_CHANGE = 10;
    private  List<PositionModel> positionModelList;
    private TrackReceiver trackReceiver;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == TIME_CHANGE){

/*                //超时退出登录
                if(timeTokenDown >= 1000*60*downTime){
                    Log.e(TAG, "handleMessage: timeDown -0"  );
                    new CommonUtil().timeDown(TrackService.this);
                    timeTokenDown = 0;
                    onDestroy();
                }*/

                Date date = new Date();
                String time = date.toLocaleString();
                Log.e(TAG, "时间time为： " + time);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentTime = dateFormat.format(date);

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
        DbConfig dbConfig = new DbConfig(getApplicationContext());
        DbManager db = dbConfig.getDbManager();
        User userM = dbConfig.getUser();
        userM.setLatitude(currentLatitude);
        userM.setLongitude(currentLongitude);
        try {
            db.delete(User.class);
            db.saveOrUpdate(userM);
        } catch (DbException e) {
            e.printStackTrace();
        }

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",user.getId());

            JSONObject posObj = new JSONObject();
            posObj.put("lat",currentLatitude);
            posObj.put("lng",currentLongitude);

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

                // service 通过广播来更新GUI
                Intent intent=new Intent();
                intent.putExtra("message",jsonObject.toString());
                intent.setAction("zcd.voicerobot");
                sendBroadcast(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" + ex.toString());
                if(ex.toString().contains("998")){
                    new com.ruyiruyi.rylibrary.utils.CommonUtil().tokenDown(TrackService.this);
                }
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

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        positionModelList = new ArrayList<>();
        getLocation();
        Log.e(TAG, "onCreate: 1");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "onCreate: 2");
                Message message=new Message();
                message.what=TIME_CHANGE;
                timeTokenDown += uploadTime;
                mHandler.sendMessage(message);   //需要开启轨迹上传 打开
            }
        },0, uploadTime);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行

        //   initBaiduLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        mHandler.removeMessages(TIME_CHANGE);
    }

    /**
     * 获取当前位置经纬度
     * @return
     */
    // @JavascriptInterface
    public void getLocation() {
        //获得位置服务
        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_HIGH);//低功耗

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0.0001f, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e(TAG, "onLocationChanged: " + location.getLongitude() + "," + location.getLatitude());
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
