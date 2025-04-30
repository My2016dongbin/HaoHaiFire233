package com.skyline.terraexplorer.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.BaseActivity;
import com.skyline.terraexplorer.views.ActionBar;

import java.util.List;

public class BaiduiMapActivity extends BaseActivity {

    private static final String TAG = BaiduiMapActivity.class.getSimpleName();
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    /**
     * 当前地点击点
     */
    private LatLng currentPt;

    private String touchType;

    /**
     * 用于显示地图状态的面板
     */
    private TextView mStateBar;
    private TextView mStateBar2;
    private Button animateStatus;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.ic_fire1);
    private LatLng center;
    //地理编码
    private GeoCoder mSearch;
    private String cityAddress;
    private String state;
    private String latitude;
    private String longitude;
    private double longitude_double;
    private double latitude_double;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private MyLocationData locData;
    private ActionBar mActionBar;
    private String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidui_map);

        //ActionBar
        mActionBar = (ActionBar) findViewById(R.id.map_actionbar);
        mActionBar.setTitle("选择地图定位");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        //获取Intent传递的值
        Intent intent_g = getIntent();
        //默认天安数码城: latitude: 36.32087806111286, longitude: 120.44349123197962
        longitude_double = intent_g.getDoubleExtra("longitude_double", 0.00);
        latitude_double = intent_g.getDoubleExtra("latitude_double",0.00);
        if (longitude_double == 0.00 && latitude_double == 0.00) {
            Toast.makeText(BaiduiMapActivity.this, "请检查授予定位权限并开启定位!", Toast.LENGTH_SHORT).show();
        }
        //       Log.e(TAG, "registerclick333: " + "longitude_double" + longitude_double + "latitude_double" + latitude_double);
        //39.86017837104533   116.45288578361887

        Log.e(TAG, "onCreate: " +  longitude_double);
        Log.e(TAG, "onCreate: " +  latitude_double);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mStateBar = (TextView) findViewById(R.id.state);
        mStateBar2 = (TextView) findViewById(R.id.state2);

        //添加定位图层
        mBaiduMap.setMyLocationEnabled(true);

        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(latitude_double)
                .longitude(longitude_double).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

        LatLng ll = new LatLng(latitude_double,
                longitude_double);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        Matrix matrix = new Matrix();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_location_mine)).getBitmap();
        // 设置旋转角度
        matrix.setRotate(-100);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        mCurrentMarker = BitmapDescriptorFactory.fromBitmap(bitmap);
// 未旋转               .fromResource(R.drawable.ic_dingwei2);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));


        //创建新的地理编码检索实例；
        mSearch = GeoCoder.newInstance();
        initListener();


        //创建地理编码检索监听者；
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {



            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }

                //获取地理编码结果
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                }

                //获取反向地理编码结果
                //      Log.e(TAG, "onGetReverseGeoCodeResult: getAddress ==" + result.getAddress());
                //    Log.e(TAG, "onGetReverseGeoCodeResult: getBusinessCircle ==" + result.getBusinessCircle());
                //     Log.e(TAG, "onGetReverseGeoCodeResult: getSematicDescription ==" + result.getSematicDescription());
                //在result中获取点击最近地址
                List<PoiInfo> poiList = result.getPoiList();
                if (null == poiList || poiList.size() == 0) {
                    Toast.makeText(BaiduiMapActivity.this, "请在地图上标注资源点位置", Toast.LENGTH_SHORT).show();
                    mStateBar.setText("");
                    mStateBar2.setText("");
                } else {
                    PoiInfo poiInfo = poiList.get(0);
                    Log.e(TAG, "onGetReverseGeoCodeResult: ----" + poiInfo.toString() );
                    cityAddress = poiInfo.address + poiInfo.name;
                    city = poiInfo.city;

                    mStateBar.setText(poiInfo.name);
                    mStateBar2.setText(poiInfo.address);
/*                Log.e(TAG, "onGetReverseGeoCodeResult:--------------------------- " + cityAddress);
                for (int i = 0; i < poiList.size(); i++) {
                    Log.e(TAG, "onGetReverseGeoCodeResult:- " + poiList.get(i).address);
                    Log.e(TAG, "onGetReverseGeoCodeResult:- " + poiList.get(i).name);
                    Log.e(TAG, "onGetReverseGeoCodeResult:- " + poiList.get(i).city);
                    Log.e(TAG, "onGetReverseGeoCodeResult:- " + poiList.get(i).phoneNum);
                    Log.e(TAG, "onGetReverseGeoCodeResult:- " + poiList.get(i).uid);
                    Log.e(TAG, "onGetReverseGeoCodeResult:- " + poiList.get(i).postCode);
                }*/
                }

            }
        };
        //设置地理编码检索监听者；
        mSearch.setOnGetGeoCodeResultListener(listener);
    }

    /**
     * 对地图事件的消息响应
     */
    private void initListener() {
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {

            @Override
            public void onTouch(MotionEvent event) {

            }
        });


        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 单击地图
             */
            public void onMapClick(LatLng point) {
                touchType = "单击地图";
                currentPt = point;
                updateMapState();
            }

            /**
             * 单击地图中的POI点
             */
            public boolean onMapPoiClick(MapPoi poi) {
                touchType = "单击POI点";
                currentPt = poi.getPosition();
                updateMapState();
                return false;
            }
        });
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            /**
             * 长按地图
             */
            public void onMapLongClick(LatLng point) {
                touchType = "长按";
                currentPt = point;
                updateMapState();
            }
        });
        mBaiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            /**
             * 双击地图
             */
            public void onMapDoubleClick(LatLng point) {
                touchType = "双击";
                currentPt = point;
                updateMapState();
            }
        });

        /**
         * 地图状态发生变化
         */
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            public void onMapStatusChangeStart(MapStatus status) {
                updateMapState();
            }

            @Override
            public void onMapStatusChangeStart(MapStatus status, int reason) {

            }

            public void onMapStatusChangeFinish(MapStatus status) {
                updateMapState();
            }

            public void onMapStatusChange(MapStatus status) {
                updateMapState();
            }
        });

        updateMapState();


    }

    public void mapclick(View view) {
        switch (view.getId()) {
            case R.id.tv_map:
                if (null == longitude || null == latitude) {
                    Toast.makeText(BaiduiMapActivity.this, "请在地图上选择资源点位置", Toast.LENGTH_SHORT).show();
                } else {
                    if (city == "" || city == null){
                        Toast.makeText(this, "请重新选择", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent();
                        intent.putExtra("longitude", longitude);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("cityAddress", cityAddress);
                        intent.putExtra("city", city);
                        BaiduiMapActivity.this.setResult(ResourceAddActivity.MAP_REUEST_CODE, intent);
                        finish();
                    }

                }
                break;
        }
    }

    /**
     * 更新地图状态显示面板
     */
    private void updateMapState() {
        if (mStateBar == null) {
            return;
        }
        state = " ";
        if (currentPt == null) {
            state = "点击、长按、双击地图以获取经纬度和地图状态";
        } else {
            //发起地理编码检索；
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(currentPt));

            state = String.format("当前经度： %f 当前纬度：%f",
                    currentPt.longitude, currentPt.latitude);
            latitude = currentPt.latitude + "";
            longitude = currentPt.longitude + "";
            String string = currentPt.toString();
            //    Log.e(TAG, "updateMapState: " + string);
            MarkerOptions ooA = new MarkerOptions().position(currentPt).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_tomap));
            mBaiduMap.clear();
            mBaiduMap.addOverlay(ooA);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
