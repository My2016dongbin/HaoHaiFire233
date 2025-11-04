package com.hht.hsatellitemobile.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hht.hsatellitemobile.MainActivity;
import com.hht.hsatellitemobile.R;
import com.hht.hsatellitemobile.db.DbConfig;
import com.hht.hsatellitemobile.ui.activity.base.HhBaseActivity;
import com.hht.hsatellitemobile.ui.cell.MessagePicturesLayout;
import com.hht.hsatellitemobile.ui.multitype.Empty;
import com.hht.hsatellitemobile.ui.multitype.EmptyViewBinder;
import com.hht.hsatellitemobile.ui.multitype.FeedBack;
import com.hht.hsatellitemobile.ui.multitype.FeedBackViewBinder;
import com.hht.hsatellitemobile.ui.multitype.FireInfo;
import com.hht.hsatellitemobile.utils.RequestUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

/**
 * 反馈界面
 */
public class FeedBackListActivity extends HhBaseActivity implements MessagePicturesLayout.Callback, FeedBackViewBinder.OnFeedBackItemClick {

    private static final String TAG = FeedBackListActivity.class.getSimpleName();
    public List<Uri> uriChooseList;
    private ActionBar actionBar;
    private String id;
    private String longitude;
    private String latitude;
    private String jingweiStr;

    private SmartRefreshLayout refreshLayout;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_list);
        uriChooseList = new ArrayList<>();
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("反馈详情");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        longitude = intent.getStringExtra("LO");
        latitude = intent.getStringExtra("LA");

        Log.e(TAG, "onCreate: id---" + id);
        Log.e(TAG, "onCreate: longitude---" + longitude );
        Log.e(TAG, "onCreate: latitude ---" + latitude);

        jingweiStr = getLocation();

        Log.e(TAG, "经纬度: " + jingweiStr);
        //配置点击查看大图
        initImageLoader();
        initView();

        getList();
    }

    private void initView() {
        refreshLayout = findViewById(R.id.refreshLayout);
        listView = findViewById(R.id.fire_info_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        FeedBackViewBinder feedBackViewBinder = new FeedBackViewBinder();
        feedBackViewBinder.setListener(this,this);
        adapter.register(FeedBack.class, feedBackViewBinder);
        EmptyViewBinder emptyViewBinder = new EmptyViewBinder();
        adapter.register(Empty.class, emptyViewBinder);
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);

                getList();
            }
        });
    }

    private void getList(){
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Satellite/GetFireFeedbacklist");
        params.addParameter("param", id);
        params.addParameter("token", new DbConfig(this).getUser().getToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "getList: param---" + id);
                //Log.e(TAG, "getList: result---" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    List<FeedBack> list = new Gson().fromJson(String.valueOf(rows), new TypeToken<List<FeedBack>>() {
                    }.getType());
                    items.clear();
                    if(list==null || list.isEmpty()){
                        items.add(new Empty());
                    }else{
                        items.addAll(list);
                    }
                    assertAllRegistered(adapter,items);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "getList: e---" + e.toString());
                    e.printStackTrace();
                }
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




    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    @Override
    public void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {

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

    @Override
    public void onFeedBackPicClick(String url) {
        Intent intent = new Intent(this, PicActivity.class);
        intent.putExtra("pic", url);
        startActivity(intent);
    }
}
