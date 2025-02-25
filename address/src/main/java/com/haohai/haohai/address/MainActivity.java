package com.haohai.haohai.address;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.haohai.haohai.address.baidumap.BDLocationUtils;
import com.haohai.haohai.address.baidumap.Const;
import com.haohai.haohai.address.db.DbConfig;
import com.haohai.haohai.address.db.model.Address;
import com.haohai.haohai.address.db.model.User;
import com.haohai.haohai.address.modle.AddressModel;
import com.haohai.haohai.address.modle.UserAddress;
import com.haohai.haohai.address.multitype.AddressMul;
import com.haohai.haohai.address.multitype.AddressViewBinder;
import com.haohai.haohai.address.multitype.Empty;
import com.haohai.haohai.address.multitype.EmptyViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MainActivity extends BaseActivity implements BDLocationListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView jingduText;
    private TextView weiduText;
    private String jingweiStr = "  ,  ";
    private Switch realTimeSwitch;
    private User user;
    private String realTime;
    private Timer timer;
    private static final int EXIT = 1;
    private static final int UPDATE_BAIDU = 2;
    private static boolean isExit = false;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){

                baiduJingduView.setText(baiduLongitude);
                baiduWeiduView.setText(baiduLatitude);

                Date date = new Date();
                String time = date.toLocaleString();
                Log.i("md", "时间time为： "+time);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentTime = dateFormat.format(date);
                Log.e(TAG, "handleMessage: " + currentTime);
                initLocation();
            //    getLocationBaiduMap();
            }else if (msg.what == EXIT ){
                isExit = false;
            }else if (msg.what == UPDATE_BAIDU){
               /* Log.e(TAG, "handleMessage: ---" + baiduLatitude );
                Log.e(TAG, "handleMessage: ---" + baiduLongitude );*/
               /* jingduText.setText(currentLatitude);
                weiduText.setText(currentLatitude);*/
            }
        }
    };
    private String currentLongitude = "";
    private String currentLatitude = "";
    private String oldLongitude = "";
    private String oldLatitude = "";
    private String currentTime;
    private boolean isOldAddress;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private List<AddressMul> addressMulList;
    private TextView settingView;
    private TextView successView;
    private TextView faildView;
    private boolean isSuccess = true;
    private TextView freshView;
    private TextView shoudongView;
    private String time;
    private String ip;
    private SwipeRefreshLayout refreshView;
    private TextView baiduJingduView;
    private TextView baiduWeiduView;
    private String baiduLongitude;
    private String baiduLatitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: test" );

        user = new DbConfig(this).getUser();

        Log.e(TAG, "onCreate:--- " +   user.getUsername());

        addressMulList = new ArrayList<>();
        realTime = user.getRealTime();
        time = user.getTime();
        ip = user.getIp();


        getLocation();
        Log.e(TAG, "onCreate: ---" +  jingweiStr);

      //  List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
     //   oldLongitude = jingweiList.get(0).toString();
      //  oldLatitude = jingweiList.get(1).toString();


        initView();

        initLocation();


        getDataFromService();

      //  getLocationBaiduMap();
    }

    private void getLocationBaiduMap() {
        //获取经纬度
        BDLocationUtils bdLocationUtils = new BDLocationUtils(getApplicationContext());
        bdLocationUtils.doLocation();//开启定位
        bdLocationUtils.mLocationClient.start();//开始定位

        Log.e(TAG, "getLocationBaiduMap: " + Const.LONGITUDE);
        Log.e(TAG, "getLocationBaiduMap: " + Const.LATITUDE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        timer.cancel();
        Log.e(TAG, "onRestart: test");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: test"  );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: test" );
        time = new DbConfig(this).getUser().getTime();
        ip = new DbConfig(this).getUser().getIp();
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // (1) 使用handler发送消息
                Message message=new Message();
                message.what=0;
                mHandler.sendMessage(message);
            }
        },0,Integer.parseInt(time) * 1000);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: test");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: test" );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: test");
    }

    private void getDataFromService() {

        RequestParams params = new RequestParams(ip + "/api/Position/GetUserTravel/{ID}");
        params.addBodyParameter("travel.userId", new DbConfig(this).getUser().getUserId());
        Log.e(TAG, "getDataFromService: param = " + params );
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result );
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data.length() > 0){
                        addressMulList .clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String longitude = object.getString("Longitude");
                            String latitude = object.getString("Latitude");
                            String insertTime = object.getString("InsertTime");
                            String uploadTime = object.getString("UploadTime");
                            addressMulList.add(new AddressMul(longitude,latitude,insertTime,uploadTime));
                        }
                        initAddressData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "网络不可用，请检查网络链接", Toast.LENGTH_SHORT).show();
                addressMulList .clear();
                initAddressData();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initAddressData() {
        items.clear();
        if (addressMulList.size() == 0){
            items.add(new Empty());
        }else {
            for (int i = 0; i < addressMulList.size(); i++) {
                items.add(addressMulList.get(i));
            }
        }

        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initLocation() {

       /* jingweiStr = getLocation();
        List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
        currentLongitude = jingweiList.get(0).toString();
        currentLatitude = jingweiList.get(1).toString();*/
        jingduText.setText(currentLongitude);
        weiduText.setText(currentLatitude);

      /*  if (realTime.equals("1")){
            postDataToService();
        }*/
        Log.e(TAG, "initLocation:currentLatitude== "+currentLatitude );
        Log.e(TAG, "initLocation:oldLatitude== "+ oldLatitude);
        Log.e(TAG, "initLocation:currentLongitude== "+ currentLongitude);
        Log.e(TAG, "initLocation:oldLongitude== "+ oldLongitude);
        if (currentLatitude.equals(oldLatitude) && currentLongitude.equals(oldLongitude)){      //经纬度不变

        }else {     //经纬度变化了
            Toast.makeText(this, "经纬度变化当前经度：" + currentLongitude +"当前纬度：" + currentLatitude , Toast.LENGTH_SHORT).show();
            oldLongitude = currentLongitude;
            oldLatitude = currentLatitude;
            if (realTime.equals("1")){
                postDataToService();
            }

        }


    }

    private void postDataToService() {
        User user = new DbConfig(this).getUser();
        List<Address> addressList = new DbConfig(this).getAddressList();
        List<AddressModel> addressModelList = new ArrayList<>();
        if (addressList != null){
            Log.e(TAG, "postDataToService: 上传前address得数量"+ addressList.size() );
            if (addressList.size() > 0){
                Log.e(TAG, "postDataToService: 添加旧数据" );
                isOldAddress = true;
               // Toast.makeText(this, "上传离线数据", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < addressList.size(); i++) {
                    addressModelList.add(new AddressModel(addressList.get(i).getLongitude(),addressList.get(i).getLatitude(),addressList.get(i).getInsertTime()));
                }
            }else {
                Log.e(TAG, "postDataToService: 没有旧数据" );
                isOldAddress = false;
            }
        }else {
            isOldAddress = false;
        }

        addressModelList.add(new AddressModel(currentLongitude,currentLatitude,currentTime));
        UserAddress userAddress = new UserAddress(user.getUserId(), user.getUsername(), user.getPhone(), "1", addressModelList);
        Gson gson = new Gson();
        String json = gson.toJson(userAddress);

        String uri =  ip + "/api/Position/InsetUserTravelList"; //user.getIp() + "/api/Position/InsetUserTravelList";
        Log.e(TAG, "postDataService:params " + uri);
        RequestParams params = new RequestParams(uri);
        params.setAsJsonContent(true);
        params.setBodyContent(json);


        Log.e(TAG, "postDataService:json " + json);
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                   
                    JSONObject jsonObject = new JSONObject(result);
                    String result1 = jsonObject.getString("result");
                    String message = jsonObject.getString("message");
                    if (result1.equals("1")){
                        if (isOldAddress){
                            new DbConfig(getApplicationContext()).clearAddress();
                            Log.e(TAG, "onSuccess: 上传完后address数量" + new DbConfig(getApplicationContext()).getAddressList().size() );
                        }

                        if (isSuccess){
                            Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            getDataFromService();
                        }else {
                            addressMulList.clear();
                            getFaildDataFromDb();
                        }

                    }else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" );
                Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                isSuccess = false;
                successView.setTextColor(getResources().getColor(R.color.c6));
                successView.setBackgroundResource(R.drawable.bg_text_hui);
                faildView.setTextColor(getResources().getColor(R.color.c12));
                faildView.setBackgroundResource(R.drawable.bg_text_lan);

                Address address = new Address(currentLongitude, currentLatitude, currentTime, "0");
                DbConfig dbConfig = new DbConfig(getApplicationContext());
                DbManager db = dbConfig.getDbManager();
                try {
                    db.saveOrUpdate(address);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if (!isSuccess){
                    getFaildDataFromDb();
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

    private void initView() {
        jingduText = (TextView) findViewById(R.id.jingdu_text);
        weiduText = (TextView) findViewById(R.id.weidu_text);
        realTimeSwitch = (Switch) findViewById(R.id.real_time_switch);
        listView = (RecyclerView) findViewById(R.id.listview);
        settingView = (TextView) findViewById(R.id.setting_view);
        successView = (TextView) findViewById(R.id.success_view);
        faildView = (TextView) findViewById(R.id.faild_view);
        freshView = (TextView) findViewById(R.id.fresh_view);
        shoudongView = (TextView) findViewById(R.id.shoudong_view);
        refreshView = (SwipeRefreshLayout) findViewById(R.id.refresh_view);
        baiduJingduView = (TextView) findViewById(R.id.baidu_jingdu_text);
        baiduWeiduView = (TextView) findViewById(R.id.baidu_weidu_text);
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isSuccess){
                    getDataFromService();
                }else {
                    getFaildDataFromDb();
                }
                refreshView.setRefreshing(false);
            }
        });

        RxViewAction.clickNoDouble(shoudongView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        postDataToService();
                    }
                });



        RxViewAction.clickNoDouble(freshView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       // jingweiStr = getLocation();
                       // List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
                     //   currentLongitude = jingweiList.get(0).toString();
                       // currentLatitude = jingweiList.get(1).toString();
                        jingduText.setText(currentLongitude);
                        weiduText.setText(currentLatitude);
                        //postDataToService();

                        Log.e(TAG, "initLocation:currentLatitude== "+currentLatitude );
                        Log.e(TAG, "initLocation:oldLatitude== "+ oldLatitude);
                        Log.e(TAG, "initLocation:currentLongitude== "+ currentLongitude);
                        Log.e(TAG, "initLocation:oldLongitude== "+ oldLongitude);
                        if (currentLatitude.equals(oldLatitude) && currentLongitude.equals(oldLongitude)){      //经纬度不变
                            Toast.makeText(MainActivity.this, "位置没有发生改变", Toast.LENGTH_SHORT).show();
                        }else {     //经纬度变化了
                            Toast.makeText(MainActivity.this, "经纬度变化当前经度：" + currentLongitude +"当前纬度：" + currentLatitude , Toast.LENGTH_SHORT).show();
                            oldLongitude = currentLongitude;
                            oldLatitude = currentLatitude;
                            postDataToService();
                        }
                    }
                });


        RxViewAction.clickNoDouble(successView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isSuccess = true;
                        successView.setTextColor(getResources().getColor(R.color.c12));
                        successView.setBackgroundResource(R.drawable.bg_text_lan);
                        faildView.setTextColor(getResources().getColor(R.color.c6));
                        faildView.setBackgroundResource(R.drawable.bg_text_hui);
                        getDataFromService();
                    }
                });


        RxViewAction.clickNoDouble(faildView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isSuccess = false;
                        successView.setTextColor(getResources().getColor(R.color.c6));
                        successView.setBackgroundResource(R.drawable.bg_text_hui);
                        faildView.setTextColor(getResources().getColor(R.color.c12));
                        faildView.setBackgroundResource(R.drawable.bg_text_lan);
                        getFaildDataFromDb();
                    }
                });

        RxViewAction.clickNoDouble(settingView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                    }
                });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);


        if (realTime.equals("1")){
            realTimeSwitch.setChecked(true);
        }else {
            realTimeSwitch.setChecked(false);
        }

        realTimeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    realTime = "1";
                }else {
                    realTime = "0";
                }
                User user = new DbConfig(getApplicationContext()).getUser();
                user.setRealTime(realTime);
                DbConfig dbConfig = new DbConfig(getApplicationContext());
                DbManager db = dbConfig.getDbManager();
                try {
                    db.saveOrUpdate(user);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private void getFaildDataFromDb() {
        List<Address> addressList = new DbConfig(this).getAddressList();
        addressMulList.clear();
        if (addressList != null){
            if (addressList.size() > 0){
                for (int i = 0; i < addressList.size(); i++) {
                    addressMulList.add(new AddressMul(addressList.get(i).getLongitude(),addressList.get(i).getLatitude(),addressList.get(i).getInsertTime(),""));
                }
            }
        }
        initAddressData();
    }

    private void register() {
        adapter.register(Empty.class,new EmptyViewBinder());
        adapter.register(AddressMul.class,new AddressViewBinder());
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

                currentLongitude = longitude+"";
                currentLatitude = latitude+"";
             //   Toast.makeText(MainActivity.this, "经纬度发生改变了,经度" +longitude + "纬度" +latitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(), "GPS开启了", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        exit();

    }
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次回到主页",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(EXIT, 2000);
        } else {
            moveTaskToBack(true);       //返回首页
            Log.e(TAG, "exit: -----");
        }
    }


    @Override
    public void onReceiveLocation(BDLocation location) {
        //获取定位结果
        StringBuffer sb = new StringBuffer(256);

        sb.append("time : ");
        sb.append(location.getTime());    //获取定位时间

        sb.append("\nerror code : ");
        sb.append(location.getLocType());    //获取类型类型

        sb.append("\nlatitude : ");
        sb.append(location.getLatitude()+"");    //获取纬度信息

        sb.append("\nlontitude : ");
        sb.append(location.getLongitude()+"");    //获取经度信息

        sb.append("\nradius : ");
        sb.append(location.getRadius());    //获取定位精准度

        if (location.getLocType() == BDLocation.TypeGpsLocation){

            // GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());    // 单位：公里每小时

            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());    //获取卫星数

            sb.append("\nheight : ");
            sb.append(location.getAltitude());    //获取海拔高度信息，单位米

            sb.append("\ndirection : ");
            sb.append(location.getDirection());    //获取方向信息，单位度

            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

            // 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\noperationers : ");
            sb.append(location.getOperators());    //获取运营商信息

            sb.append("\ndescribe : ");
            sb.append("网络定位成功");

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

            // 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");

        } else if (location.getLocType() == BDLocation.TypeServerError) {

            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");

        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

        }

        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());    //位置语义化信息

        List<Poi> list = location.getPoiList();    // POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }

        baiduLongitude = location.getLongitude() +"";
        Log.e(TAG, "onReceiveLocation: " + baiduLongitude);
        baiduLatitude = location.getLatitude() + "";
        Log.e(TAG, "onReceiveLocation: " + baiduLatitude);
        Log.i("BaiduLocationApiDem", sb.toString());
       /* baiduJingduView.setText("guiji");
        baiduWeiduView.setText(" guiji");*/

        Message message=new Message();
        message.what=UPDATE_BAIDU;
        mHandler.sendMessage(message);

        //现在已经定位成功，可以将定位的数据保存下来，Const就是保存数据的类
        Const.LONGITUDE = location.getLongitude();
        Const.LATITUDE = location.getLatitude();
    }
}
