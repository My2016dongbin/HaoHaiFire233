package com.haohai.platform.mapmodel.activity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.haohai.platform.firelibrary.ui.activity.base.HhBaseActivity;
import com.haohai.platform.mapmodel.R;
import com.haohai.platform.mapmodel.multitype.OneBodyFire;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.DYLoadingView;
import com.ruyiruyi.rylibrary.utils.LatLngChangeNew;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class FireLineDetailActivity extends HhBaseActivity implements SensorEventListener {
    ImageView iv_back;
    private MapView baiduMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private DYLoadingView dy3;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private float mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private MyLocationData myLocationData;
    private float mCurrentAccracy;
    private boolean isFirstLoc = true;
    private double lat;
    private double lng;
    private String distance;
    private String angle;
    private OneBodyFire.MonitorInfo info;
    private OneBodyFire.MonitorInfo.Position infoPosition;
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        /**
         * 定位请求回调函数
         *
         * @param location 定位结果
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            mBaiduMap.getUiSettings().setCompassEnabled(true);
            // MapView 销毁后不在处理新接收的位置
            if (location == null || baiduMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            myLocationData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)// 设置定位数据的精度信息，单位：米
                    .direction(mCurrentDirection)// 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon)
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation
                    || location.getLocType() == BDLocation.TypeOffLineLocation) {
                if (isFirstLoc && infoPosition==null) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(17.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_line_detail);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        lat = intent.getDoubleExtra("lat",36.298606);
        lng = intent.getDoubleExtra("lng",120.301557);
        distance = intent.getStringExtra("distance");
        angle = intent.getStringExtra("angle");
        Log.e("FireLineDetailActivity", "onCreate: data = " + data );
        info = new Gson().fromJson(data,OneBodyFire.MonitorInfo.class);
        if(info!=null){
            infoPosition = info.getPosition();
        }
        init();
        startLocation();
    }


    private void init() {
        iv_back = findViewById(R.id.iv_back);
        baiduMapView = findViewById(R.id.baidu_mapview);
        dy3 = findViewById(R.id.dy3);
        RxViewAction.clickNoDouble(iv_back).subscribe(new Action1<Void>() {
            @Override
            public void call(Void unused) {
                finish();
            }
        });
        baiduMapView.showZoomControls(false);
        mBaiduMap = baiduMapView.getMap();
        //显示卫星图层
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationConfiguration myLocationConfiguration =
                new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
        mBaiduMap.getUiSettings().setCompassEnabled(false);
        // 获取传感器管理服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);

        //只显示道路 不显示其他标注
        mBaiduMap.showMapPoi(true);
        //设置最大最小缩放等级
        mBaiduMap.setMaxAndMinZoomLevel(17, 5);
        OneBodyFire.MonitorInfo.Position infoPosition = info.getPosition();
        Log.e("FireLineDetailActivity", "init: infoPosition = " + infoPosition );
        if(infoPosition!=null){
            double lng = infoPosition.getLng();
            double lat = infoPosition.getLat();
            double[] position = LatLngChangeNew.calWGS84toBD09(lat, lng);
            flyBaiduMap(position[0],position[1]);

            initMarker();
        }


        //定位初始化
        mLocationClient = new LocationClient(this);

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09"); // 设置坐标类型
        option.setScanSpan(1000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();

    }

    private void initMarker() {
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        com.baidu.mapapi.model.LatLng point0 = new com.baidu.mapapi.model.LatLng(lat, lng);
        double[] position0 = LatLngChangeNew.calWGS84toBD09(point0.latitude, point0.longitude);
        com.baidu.mapapi.model.LatLng point1 = new com.baidu.mapapi.model.LatLng(infoPosition.getLat(), infoPosition.getLng());
        double[] position1 = LatLngChangeNew.calWGS84toBD09(point1.latitude, point1.longitude);

        //构建线
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(new LatLng(position0[0],position0[1]));
        points.add(new LatLng(position1[0],position1[1]));

        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(5)
                .color(0xAA2222FF)
                .points(points);
        options.add(0,mOverlayOptions);

        List<InfoWindow> infoWindowList = new ArrayList<>();
        Bundle bundle0 = new Bundle();
        bundle0.putString("name", "火点" );
        bundle0.putString("title", "火点" );
        bundle0.putInt("type", 0);
        OverlayOptions option0 = new MarkerOptions()
                .position(new LatLng(position0[0],position0[1]))
                .extraInfo(bundle0)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_fire));
        options.add(1, option0);
        Button button0 = new Button(getApplicationContext());
        button0.setBackgroundResource(R.drawable.back_info);
        button0.setText("火点位于监控点正北方向顺时针" + angle + "°，距离监控点" + distance + "米");
        button0.setTextColor(Color.WHITE);
        button0.setPadding(10,14,10,14);
        button0.setTextSize(18);
        button0.setWidth(700);
        InfoWindow mInfoWindow0 = new InfoWindow(BitmapDescriptorFactory.fromView(button0), new LatLng(position0[0],position0[1]), 165, null);
        infoWindowList.add(mInfoWindow0);


        Bundle bundle1 = new Bundle();
        bundle1.putString("name", info.getName() );
        bundle1.putString("title", info.getName() );
        bundle1.putInt("type", 1);
        OverlayOptions option1 = new MarkerOptions()
                .position(new LatLng(position1[0],position1[1]))
                .extraInfo(bundle1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_monitor_haiyu));
        options.add(2, option1);
        Button button1 = new Button(getApplicationContext());
        button1.setBackgroundResource(R.drawable.back_info);
        button1.setText(info.getName() + "\n" + lng + "," + lat);
        button1.setTextColor(Color.WHITE);
        button1.setPadding(10,14,10,14);
        button1.setTextSize(18);
        button1.setWidth(570);

        InfoWindow mInfoWindow1 = new InfoWindow(BitmapDescriptorFactory.fromView(button1), new LatLng(position1[0],position1[1]), 165, null);
        infoWindowList.add(mInfoWindow1);




        mBaiduMap.addOverlays(options);
        mBaiduMap.showInfoWindows(infoWindowList);
    }


    private void flyBaiduMap(double lat, double lng) {
        Log.e("FireLineDetailActivity", "flyBaiduMap: " + lat + "," + lng );
        //飞到精确点上
        com.baidu.mapapi.model.LatLng ll = new com.baidu.mapapi.model.LatLng(
                lat, lng);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(17);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || baiduMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locData);
        }
    }


    /**
     * 启动定位
     */
    private void startLocation() {
        // 定位初始化
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(mListener);
        LocationClientOption locationClientOption = new LocationClientOption();
        // 可选，设置定位模式，默认高精度 LocationMode.Hight_Accuracy：高精度；
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，设置返回经纬度坐标类型，默认GCJ02
        locationClientOption.setCoorType("bd09ll");
        // 如果设置为0，则代表单次定位，即仅定位一次，默认为0
        // 如果设置非0，需设置1000ms以上才有效
        locationClientOption.setScanSpan(1000);
        //可选，设置是否使用gps，默认false
        locationClientOption.setOpenGps(true);
        // 可选，是否需要地址信息，默认为不需要，即参数为false
        // 如果开发者需要获得当前点的地址信息，此处必须为true
        locationClientOption.setIsNeedAddress(true);
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation
        locationClientOption.setIsNeedLocationPoiList(true);
        // 设置定位参数
        mLocationClient.setLocOption(locationClientOption);
        // 开启定位
        mLocationClient.start();
    }

    void showDY3(){
        dy3.setVisibility(View.VISIBLE);
        dy3.start();
    }
    void hideDY3(){
        dy3.setVisibility(View.GONE);
        dy3.stop();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (float) x;
            myLocationData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection)
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(myLocationData);
        }
        lastX = x;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
