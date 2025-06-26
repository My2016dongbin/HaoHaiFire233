package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.db.Resource;
import com.skyline.terraexplorer.multitype.Plan;
import com.skyline.terraexplorer.multitype.PlanOrder;
import com.skyline.terraexplorer.multitype.PlanOrderViewBinder;
import com.skyline.terraexplorer.multitype.PlanViewBinder;
import com.skyline.terraexplorer.utils.LatLngChange;
import com.skyline.terraexplorer.utils.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class CheckPlanActivity extends HBaseActivity implements INaviInfoCallback, PlanViewBinder.OnPlanCheckItemClick ,PlanOrderViewBinder.OnPlanOrderItemClick{

    private static final String TAG = CheckPlanActivity.class.getSimpleName();
    private String planId;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public ProgressDialog progressDialog;
    public List<Plan> planList;

    private PlanOrder planOrder;
    private String jingweiStr;
    private String starWeidu;
    private String starJingdu;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_plan);
        Intent intent = getIntent();
        planId = intent.getStringExtra("plan_id");
        progressDialog = new ProgressDialog(this);
        planList = new ArrayList<>();
        initView();
        jingweiStr = getLocation();
        if (!jingweiStr.isEmpty()){
            List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
            starWeidu = jingweiList.get(1);
            starJingdu = jingweiList.get(0);
            Log.e(TAG, "onCreate:starJingdu-- "  + starJingdu);
            Log.e(TAG, "onCreate:starWeidu-- "  + starWeidu);
        }

        getPlanDataFromService();
    }

    private void getPlanDataFromService() {
        /*try {
            planList = new DbConfig(this).getDbManager().selector(Plan.class)
                    .where("planid","=",planId)
                    .findAll();
            Log.e(TAG, "getPlanDataFromService: planinfo=" + planList.size());
            planOrder = new DbConfig(this).getDbManager().selector(PlanOrder.class)
                    .where("id","=",planId)
                    .findAll()
                    .get(0);

        } catch (DbException e) {
            e.printStackTrace();
        }

        initData();
        */
        showDialogProgress(progressDialog,"加载数据...              ");

        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/checkPlan/getCheckPlanDetail");
        params.addParameter("id",planId);
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.setConnectTimeout(10000);
        Log.e(TAG, "plan: " + params );
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: plan"+ result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
                    JSONArray resourceJsonArray = data.getJSONArray("resourceList");
                    JSONObject checkPlanObject = data.getJSONObject("checkPlan");
                    Gson gson = new Gson();

                    planOrder = gson.fromJson(String.valueOf(checkPlanObject), new TypeToken<PlanOrder>(){}.getType());
                    planList.clear();
                    planList = gson.fromJson(String.valueOf(resourceJsonArray), new TypeToken<List<Plan>>(){}.getType());
                    initData();
                } catch (JSONException e) {
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
                progressDialog.dismiss();
            }
        });

    }

    private void initData() {
        items.clear();
        items.add(planOrder);
        Log.e(TAG, "initData:planinfo.s= " + planList.size());
        for (int i = 0; i < planList.size(); i++) {

            items.add(planList.get(i));
        }

        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listView = (RecyclerView) findViewById(R.id.plan_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        PlanViewBinder planViewBinder = new PlanViewBinder();
        planViewBinder.setListener(this);
        adapter.register(Plan.class, planViewBinder);
        PlanOrderViewBinder planOrderViewBinder = new PlanOrderViewBinder();
        planOrderViewBinder.setListener(this);
        adapter.register(PlanOrder.class, planOrderViewBinder);
    }

    @Override
    public void onPlanCheckItemButtonClick(int state, Plan plan) {  ////state 0隐患排查 1资源检查  2导航
        if (state == 0){
            Intent intent = new Intent(getApplicationContext(), HiddenDangerActivity.class);
            intent.putExtra("ID", plan.getResourceId());
            intent.putExtra("NAME", plan.getResourceName());
            intent.putExtra("TYPE", plan.getResourceType());
            intent.putExtra("JINGDU", plan.getLng());
            intent.putExtra("WEIDU", plan.getLat());
            intent.putExtra("GRID_ID", plan.getGridId());
            intent.putExtra("GRID_NAME", plan.getGridName());
            intent.putExtra("GRID_NO", plan.getGridNo());
            startActivity(intent);
        }else if (state == 1){
            String resourceType = plan.getResourceType();
            List<Resource> resourceList = new DbConfig(this).getResourceList();
            String apiUrl = "";
            String codeName = "";
            for (int i = 0; i < resourceList.size(); i++) {
                String name = resourceList.get(i).getName();
                String code = resourceList.get(i).getCode();
                if (plan.getResourceType().equals(code)){
                    apiUrl = resourceList.get(i).getApiUrl();
                    codeName = resourceList.get(i).getCode();
                }
            }
            Intent intent = new Intent(getApplicationContext(), ResourceCheckActivity.class);
            intent.putExtra("planId", planId);
            intent.putExtra("ID", plan.getResourceId());
            intent.putExtra("NAME", plan.getResourceName());
            intent.putExtra("TYPE", plan.getResourceType());
            intent.putExtra("CODE", codeName);
            intent.putExtra("APIURL", apiUrl);
            intent.putExtra("GRID_ID", plan.getGridId());
            intent.putExtra("GRID_NAME", plan.getGridName());
            intent.putExtra("GRID_NO", plan.getGridNo());
            startActivity(intent);
        }else if (state == 2){
           /* Log.e(TAG, "onPlanCheckItemButtonClick: lng=" + plan.getResourcePosition().getLng() );
            Log.e(TAG, "onPlanCheckItemButtonClick: lat=" + plan.getResourcePosition().getLat() );
            Intent intent = new Intent(getApplicationContext(), GPSNaviActivity.class);
            intent.putExtra("JINGDU", plan.getResourcePosition().getLng() +"");
            intent.putExtra("WEIDU",plan.getResourcePosition().getLat() +"");
            startActivity(intent);*/

            com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(plan.getResourcePosition().getLat(),plan.getResourcePosition().getLng()));

            Poi start = new Poi("", new LatLng(Double.parseDouble(starWeidu),Double.parseDouble(starJingdu)), "");
            Poi end = new Poi(plan.getResourceName(), new LatLng(latLng.latitude,latLng.longitude),"");
            AmapNaviParams params = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER);
            params.setUseInnerVoice(true);
            AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, CheckPlanActivity.this);
        }
    }

    /**
     * 导航规划点击回调
     */
    @Override
    public void onPlanOrderItemClickListener() {

        if(planList==null||planList.size()==0){
            Toast.makeText(this, "该检查计划没有可导航的资源点", Toast.LENGTH_SHORT).show();
            return;
        }
        LatLng p1 = new LatLng(39.993266, 116.473193);//首开广场
        LatLng p2 = new LatLng(39.917337, 116.397056);//故宫博物院
        LatLng p3 = new LatLng(39.904556, 116.427231);//北京站
        LatLng p4 = new LatLng(39.773801, 116.368984);//新三余公园(南5环)
        LatLng p5 = new LatLng(40.041986, 116.414496);//立水桥(北5环)
/*        List<Poi> poiList = new ArrayList();
        poiList.add(new Poi("首开广场", p1, ""));
        poiList.add(new Poi("故宫博物院", p2, ""));
        poiList.add(new Poi("北京站", p3, ""));*/
        Poi start = new Poi("", new LatLng(Double.parseDouble(starWeidu),Double.parseDouble(starJingdu)), "");
        Poi end = null;
        List<Poi> poiList = new ArrayList();
        for (int i = 0; i < planList.size(); i++) {
            if (i == planList.size() - 1){
                //终点
                com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(planList.get(i).getResourcePosition().getLat(),planList.get(i).getResourcePosition().getLng()));

                end = new Poi(planList.get(i).getResourceName(),  new LatLng(latLng.latitude,latLng.longitude), "");
            }else {
                com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(planList.get(i).getResourcePosition().getLat(),planList.get(i).getResourcePosition().getLng()));

                //途经点
                poiList.add(new Poi(planList.get(i).getResourceName(), new LatLng(latLng.latitude,latLng.longitude), ""));

            }
        }

        AmapNaviParams params = new AmapNaviParams(start, poiList, end, AmapNaviType.DRIVER);
        params.setUseInnerVoice(true);
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, CheckPlanActivity.this);
     /*   AmapNaviParams params = new AmapNaviParams(new Poi("北京站", p3, ""), null, new Poi("故宫博物院", p2, ""), AmapNaviType.DRIVER);
        params.setUseInnerVoice(true);
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, CheckPlanActivity.this);*/
       /* Poi start = new Poi("", new LatLng(Double.parseDouble(starWeidu),Double.parseDouble(starJingdu)), "");
        Poi end = null;
        List<Poi> poiList = new ArrayList();
        for (int i = 0; i < planList.size(); i++) {
            if (i == planList.size() - 1){
                //终点
                end = new Poi(planList.get(i).getResourceName(),  new LatLng(planList.get(i).getResourcePosition().getLat(),planList.get(i).getResourcePosition().getLng()), "");
            }else {
                //途经点
                poiList.add(new Poi(planList.get(i).getResourceName(), new LatLng(planList.get(i).getResourcePosition().getLat(),planList.get(i).getResourcePosition().getLng()), ""));

            }
        }


        AmapNaviParams amapNaviParams = new AmapNaviParams(start, poiList, end, AmapNaviType.DRIVER, AmapPageType.NAVI);
        amapNaviParams.setUseInnerVoice(true);
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), amapNaviParams, CheckPlanActivity.this);*/
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public View getCustomMiddleView() {
        return null;
    }

    @Override
    public void onNaviDirectionChanged(int i) {

    }

    @Override
    public void onDayAndNightModeChanged(int i) {

    }

    @Override
    public void onBroadcastModeChanged(int i) {

    }

    @Override
    public void onScaleAutoChanged(boolean b) {

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
            @SuppressLint("MissingPermission") Location location= locationManager.getLastKnownLocation(provider);
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
