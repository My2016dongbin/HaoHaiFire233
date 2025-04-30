package com.haohai.platform.firelibrary.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.haohai.platform.firelibrary.R;
import com.haohai.platform.firelibrary.ui.activity.base.DaohangBaseActivity;
import com.haohai.platform.firelibrary.ui.model.LatLng;
import com.haohai.platform.firelibrary.utils.LatLngChange;

import java.util.Arrays;
import java.util.List;

public class GPSNaviActivity extends DaohangBaseActivity {

    private static final String TAG = GPSNaviActivity.class.getSimpleName();
    private Intent intent;
    private String jingdu;
    private String weidu;
    private String jingweiStr;
    private String starJingdu;
    private String starWeidu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsnavi);

        intent = getIntent();
        jingdu = intent.getStringExtra("JINGDU");
        weidu = intent.getStringExtra("WEIDU");
        jingweiStr = getLocation();
        Log.e(TAG, "onCreate:jingweiStr-- " + jingweiStr);

        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        AMapNaviViewOptions options = new AMapNaviViewOptions();
        options.setScreenAlwaysBright(false);
        mAMapNaviView.setViewOptions(options);

        if (!jingweiStr.isEmpty()){
            List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
            starWeidu = jingweiList.get(1);
            starJingdu = jingweiList.get(0);
            Log.e(TAG, "onCreate:starJingdu-- "  + starJingdu);
            Log.e(TAG, "onCreate:starWeidu-- "  + starWeidu);

        }
        Log.e(TAG, "onCreate:WGSweidu-- "  + weidu);
        Log.e(TAG, "onCreate:WGSjingdu-- "  + jingdu);

        LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new LatLng(Double.parseDouble(weidu), Double.parseDouble(jingdu)));
        Log.e(TAG, "onCreate:GCJjingdu-- "  + latLng.longitude);
        Log.e(TAG, "onCreate:GCJweidu-- "  + latLng.latitude);
        NaviLatLng mStartLatlng = new NaviLatLng(Double.parseDouble(starWeidu),Double.parseDouble(starJingdu));
        NaviLatLng mEndLatlng = new NaviLatLng(latLng.latitude,latLng.longitude);
        sList.add(mStartLatlng);
        eList.add(mEndLatlng);
    }


    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
/*
*
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true

*/

        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.setCarNumber("京", "DFZ588");
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);

    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }

    /**
     * 获取当前位置经纬度
     * @return
     */
    @JavascriptInterface
    public String getLocation() {
        //获得位置服务
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Toast.makeText(this, "请打开GPS和使用网络定位以提高精度", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        String provider = judgeProvider(locationManager);
        //有位置提供器的情况
        List<String> providerList = locationManager.getProviders(true);
        // 测试一般都在室内，这里颠倒了书上的判断顺序
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(this, "Please Open Your GPS or Location Service", Toast.LENGTH_SHORT).show();

        }
        if (provider != null) {
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location location= locationManager.getLastKnownLocation(provider);
            try {
                return location.getLongitude()+","+location.getLatitude();
            }catch (Exception e){
                return "0.00,0.00";
            }

        }
        return null;
    }

    /**
     * 定位器provider
     * @param locationManager
     * @return
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;//网络定位
        }else if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;//GPS定位
        }else{
            Toast.makeText(this,"未开启本应用地理位置信息，请先开启！",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}
