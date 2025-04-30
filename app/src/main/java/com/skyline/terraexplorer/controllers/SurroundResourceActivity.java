package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.DngCreator;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.db.Resource;
import com.skyline.terraexplorer.multitype.ThreeGrid;
import com.skyline.terraexplorer.multitype.ThreeGridViewBinder;
import com.skyline.terraexplorer.utils.LatLngChange;
import com.skyline.terraexplorer.utils.MapUtils;
import com.skyline.terraexplorer.utils.RequestUtils;
import com.skyline.terraexplorer.utils.RxViewAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

/**
 * 周边资源
 */
public class SurroundResourceActivity extends HBaseActivity implements ThreeGridViewBinder.OnThreeGridItemClickListener,INaviInfoCallback {

    private static final String TAG = SurroundResourceActivity.class.getSimpleName();
    private String lng;
    private String lat;
    private String id;
    private  List<Resource> resourceList = new ArrayList<>();
    private  List<ThreeGrid> oneList ;
    private  List<ThreeGrid> threeList ;
    private  List<ThreeGrid> fiveList ;
    private  List<ThreeGrid> chooseList ;
    private RecyclerView oneListView;
    private List<Object> oneItems = new ArrayList<>();
    private MultiTypeAdapter oneAdapter;
    private RecyclerView threeListView;
    private List<Object> threeItems = new ArrayList<>();
    private MultiTypeAdapter threeAdapter;
    private RecyclerView fiveListView;
    private List<Object> fiveItems = new ArrayList<>();
    private MultiTypeAdapter ofiveAdapter;
    private ProgressDialog progressDialog;
    private ImageView backButton;
    private TextView daohangGuihuaButton;
    private TextView chooseResourceText;
    public int chooseNum = 0;
    public boolean isCanChoose = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surround_resource);

        oneList = new ArrayList<>();
        threeList = new ArrayList<>();
        fiveList = new ArrayList<>();
        chooseList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        lng = intent.getStringExtra("JINGDU");
        lat = intent.getStringExtra("WEIDU");
        resourceList = new DbConfig(this).getResourceList();
        Log.e(TAG, "onCreate: name=" + resourceList.get(0).getName());
        Log.e(TAG, "onCreate: code=" + resourceList.get(0).getCode());

        initView();

        getDataFromService();
    }

    private void initView() {
        daohangGuihuaButton = (TextView) findViewById(R.id.daohang_guihua_button);
        chooseResourceText = (TextView) findViewById(R.id.choose_resource_text);
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        oneListView = (RecyclerView) findViewById(R.id.one_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        oneListView.setLayoutManager(linearLayoutManager);
        oneListView.setHasFixedSize(true);
        oneListView.setNestedScrollingEnabled(false);
        oneAdapter = new MultiTypeAdapter(oneItems);
        ThreeGridViewBinder oneProvider = new ThreeGridViewBinder();
        oneProvider.setListener(this);
        oneAdapter.register(ThreeGrid.class, oneProvider);
        oneListView.setAdapter(oneAdapter);
        assertHasTheSameAdapter(oneListView, oneAdapter);


        threeListView = (RecyclerView) findViewById(R.id.three_listview);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        threeListView.setLayoutManager(linearLayoutManager1);
        threeListView.setHasFixedSize(true);
        threeListView.setNestedScrollingEnabled(false);
        threeAdapter = new MultiTypeAdapter(threeItems);
        ThreeGridViewBinder threeProvider = new ThreeGridViewBinder();
        threeProvider.setListener(this);
        threeAdapter.register(ThreeGrid.class, threeProvider);
        threeListView.setAdapter(threeAdapter);
        assertHasTheSameAdapter(threeListView, threeAdapter);

        fiveListView = (RecyclerView) findViewById(R.id.five_listview);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        fiveListView.setLayoutManager(linearLayoutManager2);
        fiveListView.setHasFixedSize(true);
        fiveListView.setNestedScrollingEnabled(false);
        ofiveAdapter = new MultiTypeAdapter(fiveItems);
        ThreeGridViewBinder fiveProvider = new ThreeGridViewBinder();
        fiveProvider.setListener(this);
        ofiveAdapter.register(ThreeGrid.class, fiveProvider);
        fiveListView.setAdapter(ofiveAdapter);
        assertHasTheSameAdapter(fiveListView, ofiveAdapter);

        /**
         * 导航规划
         */
        RxViewAction.clickNoDouble(daohangGuihuaButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        MapUtils mapUtils = new MapUtils();
                        String jingweiStr = getLocation();
                        String starWeidu = "";
                        String starJingdu = "";
                        if (!jingweiStr.isEmpty()){
                            List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
                            starWeidu = jingweiList.get(1);
                            starJingdu = jingweiList.get(0);
                        }
                        chooseList.clear();
                        for (int i = 0; i < oneList.size(); i++) {
                            if (oneList.get(i).isChoose) {
                                double distance = mapUtils.GetDistance(Double.parseDouble(starWeidu), Double.parseDouble(starJingdu), oneList.get(i).getLat(), oneList.get(i).getLng());
                                oneList.get(i).setDistance(distance);
                                chooseList.add(oneList.get(i));
                            }
                        }
                        for (int i = 0; i < threeList.size(); i++) {
                            if (threeList.get(i).isChoose) {
                                double distance = mapUtils.GetDistance(Double.parseDouble(starWeidu), Double.parseDouble(starJingdu),threeList.get(i).getLat(), threeList.get(i).getLng());
                                threeList.get(i).setDistance(distance);
                                chooseList.add(threeList.get(i));
                            }
                        }
                        for (int i = 0; i < fiveList.size(); i++) {
                            if (fiveList.get(i).isChoose) {
                                double distance = mapUtils.GetDistance(Double.parseDouble(starWeidu), Double.parseDouble(starJingdu), fiveList.get(i).getLat(), fiveList.get(i).getLng());
                                fiveList.get(i).setDistance(distance);
                                chooseList.add(fiveList.get(i));
                            }
                        }

                        Collections.sort(chooseList, new Comparator<ThreeGrid>(){

                            @Override
                            public int compare(ThreeGrid o1, ThreeGrid o2) {
                                return (int) (o1.distance - o2.distance);
                            }
                        });

                        Poi start = new Poi("", new LatLng(Double.parseDouble(starWeidu),Double.parseDouble(starJingdu)), "");
                        Poi end = null;
                        List<Poi> poiList = new ArrayList();
                        //普通的
                      /*  for (int i = 0; i < chooseList.size(); i++) {

                            Log.e(TAG, "call: 距离--" + chooseList.get(i).distance );
                            if (i == chooseList.size() - 1){
                                //终点
                                com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(Double.parseDouble(chooseList.get(i).getLat()),Double.parseDouble(chooseList.get(i).getLng())));

                                end = new Poi(chooseList.get(i).getText2(),  new LatLng(latLng.latitude,latLng.longitude), "");
                            }else {
                                com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(Double.parseDouble(chooseList.get(i).getLat()),Double.parseDouble(chooseList.get(i).getLng())));

                                //途经点
                                poiList.add(new Poi(chooseList.get(i).getText2(), new LatLng(latLng.latitude,latLng.longitude), ""));

                            }
                        }*/
                        //导航规划
                        if (chooseList.size() ==1){ //只有一个点时 这个点时这个点是终点
                            //终点
                            com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(0).getLat(),chooseList.get(0).getLng()));
                            end = new Poi(chooseList.get(0).getText2(),  new LatLng(latLng.latitude,latLng.longitude), "");
                        }else if (chooseList.size() == 2){  //有两个点时 第一个点是途径点  第二个点是重点
                            //途经点
                            com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(0).getLat(),chooseList.get(0).getLng()));
                            poiList.add(new Poi(chooseList.get(0).getText2(), new LatLng(latLng.latitude,latLng.longitude), ""));
                            //终点
                            com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(1).getLat(),chooseList.get(1).getLng()));
                            end = new Poi(chooseList.get(1).getText2(),  new LatLng(latLng1.latitude,latLng1.longitude), "");

                        }else if (chooseList.size() == 3){  //有三个点时  第一个点时途径点  判断第二个点跟第三个点距离第一个近的时途径点
                            //途经点1
                            com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(0).getLat(),chooseList.get(0).getLng()));
                            poiList.add(new Poi(chooseList.get(0).getText2(), new LatLng(latLng.latitude,latLng.longitude), ""));
                            double distance2 = mapUtils.GetDistance(chooseList.get(0).getLat(),chooseList.get(0).getLng(),chooseList.get(1).getLat(),chooseList.get(1).getLng());
                            double distance3 = mapUtils.GetDistance(chooseList.get(0).getLat(),chooseList.get(0).getLng(),chooseList.get(2).getLat(),chooseList.get(2).getLng());

                            if (distance2 > distance3){     //如果第二个点比第三个点远  那么第三个点是途径点  第二个点是重点
                                //途经点2
                                com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(2).getLat(),chooseList.get(2).getLng()));
                                poiList.add(new Poi(chooseList.get(2).getText2(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                //终点
                                com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(1).getLat(),chooseList.get(1).getLng()));
                                end = new Poi(chooseList.get(1).getText2(),  new LatLng(latLng1.latitude,latLng1.longitude), "");
                            }else {
                                //途经点2
                                com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(1).getLat(),chooseList.get(1).getLng()));
                                poiList.add(new Poi(chooseList.get(1).getText2(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                //终点
                                com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(2).getLat(),chooseList.get(2).getLng()));
                                end = new Poi(chooseList.get(2).getText2(),  new LatLng(latLng1.latitude,latLng1.longitude), "");
                            }
                        }else if (chooseList.size() == 4){
                            //途经点1
                            com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(0).getLat(),chooseList.get(0).getLng()));
                            poiList.add(new Poi(chooseList.get(0).getText2(), new LatLng(latLng.latitude,latLng.longitude), ""));
                            double distance2 = mapUtils.GetDistance(chooseList.get(0).getLat(),chooseList.get(0).getLng(),chooseList.get(1).getLat(),chooseList.get(1).getLng());
                            double distance3 = mapUtils.GetDistance(chooseList.get(0).getLat(),chooseList.get(0).getLng(),chooseList.get(2).getLat(),chooseList.get(2).getLng());
                            double distance4 = mapUtils.GetDistance(chooseList.get(0).getLat(),chooseList.get(0).getLng(),chooseList.get(3).getLat(),chooseList.get(3).getLng());
                            double min = distance2 < distance3 ? distance2:distance3;
                            min = min < distance4 ? min : distance4;
                            if (min == distance2){
                                //途经点2
                                com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(1).getLat(),chooseList.get(1).getLng()));
                                poiList.add(new Poi(chooseList.get(1).getText2(), new LatLng(latLng1.latitude,latLng1.longitude), ""));
                                if (distance3 > distance4){     //4是途径点 3是终点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(3).getLat(),chooseList.get(3).getLng()));
                                    poiList.add(new Poi(chooseList.get(3).getText2(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(2).getLat(),chooseList.get(2).getLng()));
                                    end = new Poi(chooseList.get(2).getText2(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }else {  //4是终点 3是途径点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(2).getLat(),chooseList.get(2).getLng()));
                                    poiList.add(new Poi(chooseList.get(2).getText2(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(3).getLat(),chooseList.get(3).getLng()));
                                    end = new Poi(chooseList.get(3).getText2(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }
                            }else if (min == distance3){
                                //途经点2
                                com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(2).getLat(),chooseList.get(2).getLng()));
                                poiList.add(new Poi(chooseList.get(2).getText2(), new LatLng(latLng1.latitude,latLng1.longitude), ""));
                                if (distance2 > distance4){     //4是途径点 2是终点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(3).getLat(),chooseList.get(3).getLng()));
                                    poiList.add(new Poi(chooseList.get(3).getText2(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(1).getLat(),chooseList.get(1).getLng()));
                                    end = new Poi(chooseList.get(1).getText2(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }else {  //4是终点 3是途径点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(1).getLat(),chooseList.get(1).getLng()));
                                    poiList.add(new Poi(chooseList.get(1).getText2(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(3).getLat(),chooseList.get(3).getLng()));
                                    end = new Poi(chooseList.get(3).getText2(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }
                            }else if (min == distance4){
                                //途经点2
                                com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(3).getLat(),chooseList.get(3).getLng()));
                                poiList.add(new Poi(chooseList.get(3).getText2(), new LatLng(latLng1.latitude,latLng1.longitude), ""));
                                if (distance2 > distance3){     //3是途径点 2是终点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(2).getLat(),chooseList.get(2).getLng()));
                                    poiList.add(new Poi(chooseList.get(2).getText2(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(1).getLat(),chooseList.get(1).getLng()));
                                    end = new Poi(chooseList.get(1).getText2(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }else {  //4是终点 3是途径点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(1).getLat(),chooseList.get(1).getLng()));
                                    poiList.add(new Poi(chooseList.get(1).getText2(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(chooseList.get(2).getLat(),chooseList.get(2).getLng()));
                                    end = new Poi(chooseList.get(2).getText2(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }
                            }
                        }

                        AmapNaviParams params = new AmapNaviParams(start, poiList, end, AmapNaviType.DRIVER);
                        params.setUseInnerVoice(true);
                        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, SurroundResourceActivity.this);

                    }
                });
    }

    private void getDataFromService() {
        showDialogProgress(progressDialog,"加载中...");
        JSONObject jsonObject = new JSONObject();
        JSONObject position = new JSONObject();

        // Log.e(TAG, "initResourceIntoDb:time-- " + time);
        try {
            jsonObject.put("id",id);
            position.put("lat", lat);
            position.put("lng", lng);
            jsonObject.put("position",position);
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/resourceList/getAroundResourceList");

        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------------- " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONObject data = jsonObject1.getJSONArray("data").getJSONObject(0);
                        JSONArray list1 = data.getJSONArray("list1");
                        oneList.clear();
                        oneList.add(new ThreeGrid("1","资源类型","资源名称","距离(公里)",0.00,0.00,false));
                        for (int i = 0; i < list1.length(); i++) {
                            try {
                                JSONObject object = list1.getJSONObject(i);
                                String resourceType = object.getString("resourceType");
                                String id = object.getString("id");
                                String resourceName = "无";
                                String name = object.getString("name");
                                String lng = object.getJSONObject("position").getString("lng");
                                String lat = object.getJSONObject("position").getString("lat");
                                Log.e(TAG, "onSuccess: 1" + name );
                                double distance = object.getDouble("distance")/1000;
                                for (int j = 0; j < resourceList.size(); j++) {
                                    String code1 = resourceList.get(j).getCode();
                                    if (resourceType.equals(code1)){
                                        Log.e(TAG, "onSuccess:resourceType- " +resourceType );
                                        Log.e(TAG, "onSuccess:code1- " +code1 );
                                        resourceName = resourceList.get(j).getName();
                                        Log.e(TAG, "onSuccess:resourceName- " + resourceName );
                                    }
                                }
                                oneList.add(new ThreeGrid(id,resourceName,name,String.format("%.2f",distance)+"",Double.parseDouble(lat),Double.parseDouble(lng),false));
                            }catch (Exception e){
                                continue;
                            }

                        }
                        JSONArray list3 = data.getJSONArray("list3");
                        threeList.clear();
                        threeList.add(new ThreeGrid("3","资源类型","资源名称","距离(公里)",0.00,0.00,false));
                        for (int i = 0; i < list3.length(); i++) {
                            try {
                                JSONObject object = list3.getJSONObject(i);
                                String resourceType = object.getString("resourceType");
                                String resourceName = "无";
                                String name = object.getString("name");
                                String id = object.getString("id");
                                String lng = object.getJSONObject("position").getString("lng");
                                String lat = object.getJSONObject("position").getString("lat");
                                Log.e(TAG, "onSuccess: 3" + name );
                                double distance = object.getDouble("distance")/1000;
                                for (int j = 0; j < resourceList.size(); j++) {
                                    String code1 = resourceList.get(j).getCode();
                                    if (resourceType.equals(code1)){
                                        resourceName = resourceList.get(j).getName();
                                    }
                                }
                                threeList.add(new ThreeGrid(id,resourceName,name,String.format("%.2f",distance)+"",Double.parseDouble(lat),Double.parseDouble(lng),false));
                            }catch (Exception e){
                                continue;
                            }

                        }
                        JSONArray list5 = data.getJSONArray("list5");
                        fiveList.clear();
                        fiveList.add(new ThreeGrid("5","资源类型","资源名称","距离(公里)",0.00,0.00,false));
                        for (int i = 0; i < list5.length(); i++) {
                            try {
                                JSONObject object = list5.getJSONObject(i);
                                final String resourceType = object.getString("resourceType");
                                String resourceName = "无";
                                String name = object.getString("name");
                                Log.e(TAG, "onSuccess: 5" + name );
                                String id = object.getString("id");
                                String lng = object.getJSONObject("position").getString("lng");
                                String lat = object.getJSONObject("position").getString("lat");
                                double distance = object.getDouble("distance")/1000;
                           //     List<Resource> collect = resourceList.stream().filter(t -> t.getCode().equals(resourceType)).collect(Collectors.toList());
                                for (int j = 0; j < resourceList.size(); j++) {
                                    String code1 = resourceList.get(j).getCode();
                                    if (resourceType.equals(code1)){
                                        resourceName = resourceList.get(j).getName();
                                    }
                                }
                                fiveList.add(new ThreeGrid(id,resourceName,name,String.format("%.2f",distance)+"",Double.parseDouble(lat),Double.parseDouble(lng),false));
                            }catch (Exception e){
                                continue;
                            }

                        }

                        initData();
                    }else {
                        Toast.makeText(SurroundResourceActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
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

            }
        });
    }

    private void initData() {
        oneItems.clear();
        if (oneList.size() ==1){
            oneList.add(new ThreeGrid("0","无","无","无",0.00,0.00,false));
        }
        for (int i = 0; i < oneList.size(); i++) {
            if (i == oneList.size() -1){
                ThreeGrid threeGrid = oneList.get(i);
                threeGrid.setHasBottomView(true);
                oneItems.add(threeGrid);
            }else {
                ThreeGrid threeGrid = oneList.get(i);
                oneItems.add(threeGrid);
            }
        }

        assertAllRegistered(oneAdapter,oneItems);
        oneAdapter.notifyDataSetChanged();

        threeItems.clear();
        if (threeList.size() ==1){
            threeList.add(new ThreeGrid("0","无","无","无",0.00,0.00,false));
        }
        for (int i = 0; i < threeList.size(); i++) {
            if (i == threeList.size() -1){
                ThreeGrid threeGrid = threeList.get(i);
                threeGrid.setHasBottomView(true);
                threeItems.add(threeGrid);
            }else {
                ThreeGrid threeGrid = threeList.get(i);
                threeItems.add(threeGrid);
            }

        }
        assertAllRegistered(threeAdapter,threeItems);
        threeAdapter.notifyDataSetChanged();

        fiveItems.clear();
        if (fiveList.size() ==1){
            fiveList.add(new ThreeGrid("0","无","无","无",0.00,0.00,false));
        }
        for (int i = 0; i < fiveList.size(); i++) {
            if (i == fiveList.size() -1){
                ThreeGrid threeGrid = fiveList.get(i);
                threeGrid.setHasBottomView(true);
                fiveItems.add(threeGrid);
            }else {
                ThreeGrid threeGrid = fiveList.get(i);
                fiveItems.add(threeGrid);
            }

        }
        assertAllRegistered(oneAdapter,fiveItems);
        ofiveAdapter.notifyDataSetChanged();

        progressDialog.dismiss();
    }

    /**
     * 导航点击
     * @param threeGrid
     */
    @Override
    public void onDaoHangViewClick(ThreeGrid threeGrid) {
        String jingweiStr = getLocation();
        String starWeidu = "";
        String starJingdu = "";
        if (!jingweiStr.isEmpty()){
            List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
            starWeidu = jingweiList.get(1);
            starJingdu = jingweiList.get(0);
        }
        Log.e(TAG, "onDaoHangViewClick:starWeidu= "+ starWeidu);
        Log.e(TAG, "onDaoHangViewClick:starJingdu= "+ starJingdu);
        com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(threeGrid.getLat(),threeGrid.getLng()));


        Poi start = new Poi("", new LatLng(Double.parseDouble(starWeidu),Double.parseDouble(starJingdu)), "");
        Poi end = new Poi(threeGrid.getText2(), new LatLng(latLng.latitude,latLng.longitude),"");
        AmapNaviParams params = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER);
        params.setUseInnerVoice(true);
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, SurroundResourceActivity.this);
    }

    /**
     * 是否选中点击
     * @param threeGrid
     * @param isChoose
     */
    @Override
    public void onChooseItemClick(ThreeGrid threeGrid, boolean isChoose) {

        if (isCanChoose || !isChoose){      //可以选择 或者是删除
            chooseNum = 0;
            String chooseStr = "已选中资源点:";
            for (int i = 0; i < oneList.size(); i++) {
                if (oneList.get(i).getId().equals(threeGrid.getId())) {
                    oneList.get(i).setChoose(isChoose);
                }
                if (oneList.get(i).isChoose){
                    chooseStr = chooseStr + oneList.get(i).getText2() + ";";
                    chooseNum = chooseNum + 1;
                }
            }
            for (int i = 0; i < threeList.size(); i++) {
                if (threeList.get(i).getId().equals(threeGrid.getId())) {
                    threeList.get(i).setChoose(isChoose);
                }
                if (threeList.get(i).isChoose){
                    chooseStr = chooseStr + threeList.get(i).getText2() + ";";
                    chooseNum = chooseNum + 1;
                }
            }
            for (int i = 0; i < fiveList.size(); i++) {
                if (fiveList.get(i).getId().equals(threeGrid.getId())) {
                    fiveList.get(i).setChoose(isChoose);
                }
                if (fiveList.get(i).isChoose){
                    chooseStr = chooseStr + fiveList.get(i).getText2() + ";";
                    chooseNum = chooseNum + 1;
                }
            }
            if (chooseNum >= 4){
                isCanChoose = false;
            }else {
                isCanChoose = true;
            }

            chooseResourceText.setText(chooseStr);

            initData();
        }else {
            Toast.makeText(this, "最多可以选取4个地点", Toast.LENGTH_SHORT).show();
        }
  
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
}
