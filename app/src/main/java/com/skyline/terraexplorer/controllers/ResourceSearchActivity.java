package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.skyline.terraexplorer.db.CemeteryDTO;
import com.skyline.terraexplorer.db.CheckStationDTO;
import com.skyline.terraexplorer.db.DangerSourceDTO;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.db.FireCommandDTO;
import com.skyline.terraexplorer.db.Grid;
import com.skyline.terraexplorer.db.HelicopterPointDTO;
import com.skyline.terraexplorer.db.MaterialRepositoryDTO;
import com.skyline.terraexplorer.db.MonitorDTO;
import com.skyline.terraexplorer.db.TeamDTO;
import com.skyline.terraexplorer.db.WatchTowerDTO;
import com.skyline.terraexplorer.db.WaterSourceDTO;
import com.skyline.terraexplorer.multitype.Resource;
import com.skyline.terraexplorer.multitype.ResourceType;
import com.skyline.terraexplorer.multitype.ResourceTypeViewBinder;
import com.skyline.terraexplorer.multitype.ResourceViewBinder;
import com.skyline.terraexplorer.multitype.ThreeGrid;
import com.skyline.terraexplorer.utils.LatLngChange;
import com.skyline.terraexplorer.utils.MapUtils;
import com.skyline.terraexplorer.utils.RxViewAction;
import com.skyline.terraexplorer.views.WheelView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
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

public class ResourceSearchActivity extends HBaseActivity implements INaviInfoCallback,ResourceTypeViewBinder.OnResourceTypeItemClick,ResourceViewBinder.OnResourceItemClick {

    private static final String TAG = ResourceSearchActivity.class.getSimpleName();
    private LinearLayout quLayout;
    private TextView quText;
    private LinearLayout jiedaoLayout;
    private TextView jiedaoText;
    public List<Grid> quList;
    public List<String> quStrList;
    public List<Grid> jiedaoList;
    public List<String> jiedaoStrList;
    private WheelView areaWy;
    public int currentQuanxian = 0;   //0是全国权限  1是省级权限
    public int currentChooseArea = 0;  //当前在选择区还是街道   0选择区  1选择街道
    public String currentChooseQu = "";
    public String currentChooseJiedao = "";
    public String currentChooseType = "";
    public int quSelectIndex = 0;
    public int jieDaoSelectIndex = 0;
    public int typeSelectIndex = 0;
    public boolean isChooseQu = false;
    public String currentQuId;
    public String currentStreeNo;
    private TextView searchView;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<ResourceType> resourceTypeList;
    public List<Resource> resourceChooseList;
    private TextView daohangGuihuaButton;
    public ProgressDialog progressDialog;
    private ImageView backButton;
    private LinearLayout leixingLayout;
    private TextView leixingView;

    public List<String> typeStrList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_search);

        quList = new ArrayList<>();
        quStrList = new ArrayList<>();
        jiedaoList = new ArrayList<>();
        jiedaoStrList = new ArrayList<>();
        resourceChooseList = new ArrayList<>();
        typeStrList = new ArrayList<>();
        resourceTypeList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        initView();
        getTypeFromDb();
    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listView = (RecyclerView) findViewById(R.id.list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        daohangGuihuaButton = (TextView) findViewById(R.id.daohang_guihua_button);
        searchView = (TextView) findViewById(R.id.search_view);
        quLayout = (LinearLayout) findViewById(R.id.qu_layout);
        quText = (TextView) findViewById(R.id.qu_text);
        jiedaoLayout = (LinearLayout) findViewById(R.id.jiedao_layout);
        jiedaoText = (TextView) findViewById(R.id.jiedao_text);
        leixingLayout = (LinearLayout) findViewById(R.id.type_layout);
        leixingView = (TextView) findViewById(R.id.type_view);

        RxViewAction.clickNoDouble(leixingLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showTypeDialog(typeStrList);
                    }
                });

        RxViewAction.clickNoDouble(searchView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (jiedaoText.getText().equals("请选择街道")){
                            Toast.makeText(ResourceSearchActivity.this, "请先选择街道", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.e(TAG, "call:currentChooseJiedao =  " + currentChooseJiedao);
                            Log.e(TAG, "call:jiedaoList.size =  " + jiedaoList.size());
                            for (int i = 0; i < jiedaoList.size(); i++) {
                                Log.e(TAG, "call: " + jiedaoList.get(i).getName() );
                                Log.e(TAG, "call: " + jiedaoList.get(i).getGridNo() );
                                if (jiedaoList.get(i).getName().equals(currentChooseJiedao)) {
                                    currentStreeNo = jiedaoList.get(i).getGridNo();
                                }
                            }

                            try {
                                getDataFromDb();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

        RxViewAction.clickNoDouble(quLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 0;
                        getAllQu();
                    }
                });
        RxViewAction.clickNoDouble(jiedaoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 1;
                        //TODO
                        try{
                            for (int i = 0; i < quList.size(); i++) {
                                if (quList.get(i).getName().equals(currentChooseQu)) {
                                    currentQuId = quList.get(i).getId();
                                }
                            }
                            if (quText.getText().equals("请选择区")){
                                Toast.makeText(ResourceSearchActivity.this, "请先选择区", Toast.LENGTH_SHORT).show();

                            }else {
                                getAllJieDao();
                            }
                        }catch (Exception e){

                        }

                    }
                });


        RxViewAction.clickNoDouble(daohangGuihuaButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: dianle" );
                        showDialogProgress(progressDialog,"导航规划中...");
                        MapUtils mapUtils = new MapUtils();
                        String jingweiStr = getLocation();
                        String starWeidu = "";
                        String starJingdu = "";
                        if (!jingweiStr.isEmpty()){
                            List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
                            starWeidu = jingweiList.get(1);
                            starJingdu = jingweiList.get(0);
                        }
                        resourceChooseList.clear();
                        for (int i = 0; i < resourceTypeList.size(); i++) {
                            List<Resource> resourceList = resourceTypeList.get(i).getResourceList();
                            for (int j = 0; j < resourceList.size(); j++) {
                                if (resourceList.get(j).isChoose) {
                                    double distance = mapUtils.GetDistance(Double.parseDouble(starWeidu), Double.parseDouble(starJingdu),resourceList.get(j).getLat(),resourceList.get(j).getLng()       );
                                    resourceList.get(j).setDistance(distance);
                                    resourceChooseList.add(resourceList.get(j));
                                }
                            }
                        }
                        if (resourceChooseList.size() == 0){
                            Toast.makeText(ResourceSearchActivity.this, "请选择资源点", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        Collections.sort(resourceChooseList, new Comparator<Resource>(){

                            @Override
                            public int compare(Resource o1, Resource o2) {
                                return (int) (o1.distance - o2.distance);
                            }
                        });

                        Poi start = new Poi("", new LatLng(Double.parseDouble(starWeidu),Double.parseDouble(starJingdu)), "");
                        Poi end = null;
                        List<Poi> poiList = new ArrayList();
/*
                        for (int i = 0; i < resourceChooseList.size(); i++) {

                            Log.e(TAG, "call: 距离--" + resourceChooseList.get(i).distance );
                            if (i == resourceChooseList.size() - 1){
                                //终点
                                com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(i).getLat(),resourceChooseList.get(i).getLng()));

                                end = new Poi(resourceChooseList.get(i).getName(),  new LatLng(latLng.latitude,latLng.longitude), "");
                            }else {
                                com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(i).getLat(),resourceChooseList.get(i).getLng()));

                                //途经点
                                poiList.add(new Poi(resourceChooseList.get(i).getName(), new LatLng(latLng.latitude,latLng.longitude), ""));

                            }
                        }
*/

                        //导航规划
                        if (resourceChooseList.size() ==1){ //只有一个点时 这个点时这个点是终点
                            //终点
                            com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(0).getLat(),resourceChooseList.get(0).getLng()));
                            end = new Poi(resourceChooseList.get(0).getName(),  new LatLng(latLng.latitude,latLng.longitude), "");
                        }else if (resourceChooseList.size() == 2){  //有两个点时 第一个点是途径点  第二个点是重点
                            //途经点
                            com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(0).getLat(),resourceChooseList.get(0).getLng()));
                            poiList.add(new Poi(resourceChooseList.get(0).getName(), new LatLng(latLng.latitude,latLng.longitude), ""));
                            //终点
                            com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(1).getLat(),resourceChooseList.get(1).getLng()));
                            end = new Poi(resourceChooseList.get(1).getName(),  new LatLng(latLng1.latitude,latLng1.longitude), "");

                        }else if (resourceChooseList.size() == 3){  //有三个点时  第一个点时途径点  判断第二个点跟第三个点距离第一个近的时途径点
                            //途经点1
                            com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(0).getLat(),resourceChooseList.get(0).getLng()));
                            poiList.add(new Poi(resourceChooseList.get(0).getName(), new LatLng(latLng.latitude,latLng.longitude), ""));
                            double distance2 = mapUtils.GetDistance(resourceChooseList.get(0).getLat(),resourceChooseList.get(0).getLng(),resourceChooseList.get(1).getLat(),resourceChooseList.get(1).getLng());
                            double distance3 = mapUtils.GetDistance(resourceChooseList.get(0).getLat(),resourceChooseList.get(0).getLng(),resourceChooseList.get(2).getLat(),resourceChooseList.get(2).getLng());

                            if (distance2 > distance3){     //如果第二个点比第三个点远  那么第三个点是途径点  第二个点是重点
                                //途经点2
                                com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(2).getLat(),resourceChooseList.get(2).getLng()));
                                poiList.add(new Poi(resourceChooseList.get(2).getName(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                //终点
                                com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(1).getLat(),resourceChooseList.get(1).getLng()));
                                end = new Poi(resourceChooseList.get(1).getName(),  new LatLng(latLng1.latitude,latLng1.longitude), "");
                            }else {
                                //途经点2
                                com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(1).getLat(),resourceChooseList.get(1).getLng()));
                                poiList.add(new Poi(resourceChooseList.get(1).getName(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                //终点
                                com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(2).getLat(),resourceChooseList.get(2).getLng()));
                                end = new Poi(resourceChooseList.get(2).getName(),  new LatLng(latLng1.latitude,latLng1.longitude), "");
                            }
                        }else if (resourceChooseList.size() == 4){
                            //途经点1
                            com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(0).getLat(),resourceChooseList.get(0).getLng()));
                            poiList.add(new Poi(resourceChooseList.get(0).getName(), new LatLng(latLng.latitude,latLng.longitude), ""));
                            double distance2 = mapUtils.GetDistance(resourceChooseList.get(0).getLat(),resourceChooseList.get(0).getLng(),resourceChooseList.get(1).getLat(),resourceChooseList.get(1).getLng());
                            double distance3 = mapUtils.GetDistance(resourceChooseList.get(0).getLat(),resourceChooseList.get(0).getLng(),resourceChooseList.get(2).getLat(),resourceChooseList.get(2).getLng());
                            double distance4 = mapUtils.GetDistance(resourceChooseList.get(0).getLat(),resourceChooseList.get(0).getLng(),resourceChooseList.get(3).getLat(),resourceChooseList.get(3).getLng());
                            double min = distance2 < distance3 ? distance2:distance3;
                            min = min < distance4 ? min : distance4;
                            if (min == distance2){
                                //途经点2
                                com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(1).getLat(),resourceChooseList.get(1).getLng()));
                                poiList.add(new Poi(resourceChooseList.get(1).getName(), new LatLng(latLng1.latitude,latLng1.longitude), ""));
                                if (distance3 > distance4){     //4是途径点 3是终点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(3).getLat(),resourceChooseList.get(3).getLng()));
                                    poiList.add(new Poi(resourceChooseList.get(3).getName(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(2).getLat(),resourceChooseList.get(2).getLng()));
                                    end = new Poi(resourceChooseList.get(2).getName(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }else {  //4是终点 3是途径点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(2).getLat(),resourceChooseList.get(2).getLng()));
                                    poiList.add(new Poi(resourceChooseList.get(2).getName(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(3).getLat(),resourceChooseList.get(3).getLng()));
                                    end = new Poi(resourceChooseList.get(3).getName(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }
                            }else if (min == distance3){
                                //途经点2
                                com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(2).getLat(),resourceChooseList.get(2).getLng()));
                                poiList.add(new Poi(resourceChooseList.get(2).getName(), new LatLng(latLng1.latitude,latLng1.longitude), ""));
                                if (distance2 > distance4){     //4是途径点 2是终点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(3).getLat(),resourceChooseList.get(3).getLng()));
                                    poiList.add(new Poi(resourceChooseList.get(3).getName(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(1).getLat(),resourceChooseList.get(1).getLng()));
                                    end = new Poi(resourceChooseList.get(1).getName(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }else {  //4是终点 3是途径点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(1).getLat(),resourceChooseList.get(1).getLng()));
                                    poiList.add(new Poi(resourceChooseList.get(1).getName(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(3).getLat(),resourceChooseList.get(3).getLng()));
                                    end = new Poi(resourceChooseList.get(3).getName(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }
                            }else if (min == distance4){
                                //途经点2
                                com.skyline.terraexplorer.utils.LatLng latLng1 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(3).getLat(),resourceChooseList.get(3).getLng()));
                                poiList.add(new Poi(resourceChooseList.get(3).getName(), new LatLng(latLng1.latitude,latLng1.longitude), ""));
                                if (distance2 > distance3){     //3是途径点 2是终点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(2).getLat(),resourceChooseList.get(2).getLng()));
                                    poiList.add(new Poi(resourceChooseList.get(2).getName(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(1).getLat(),resourceChooseList.get(1).getLng()));
                                    end = new Poi(resourceChooseList.get(1).getName(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }else {  //4是终点 3是途径点
                                    //途经点3
                                    com.skyline.terraexplorer.utils.LatLng latLng2 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(1).getLat(),resourceChooseList.get(1).getLng()));
                                    poiList.add(new Poi(resourceChooseList.get(1).getName(), new LatLng(latLng2.latitude,latLng2.longitude), ""));
                                    //终点
                                    com.skyline.terraexplorer.utils.LatLng latLng3 = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resourceChooseList.get(2).getLat(),resourceChooseList.get(2).getLng()));
                                    end = new Poi(resourceChooseList.get(2).getName(),  new LatLng(latLng3.latitude,latLng3.longitude), "");
                                }
                            }
                        }

                        AmapNaviParams params = new AmapNaviParams(start, poiList, end, AmapNaviType.DRIVER);
                        params.setUseInnerVoice(true);
                        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, ResourceSearchActivity.this);
                         progressDialog.dismiss();

                    }
                });


    }

    private void showTypeDialog(List<String> typeStrList) {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        areaWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        areaWy.setIsLoop(false);

        areaWy.setItems(typeStrList, typeSelectIndex);//init selected position is 0 初始选中位置为0


        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                    currentChooseType = areaWy.getSelectedItem();
                    typeSelectIndex = areaWy.getSelectedPosition();
                    leixingView.setText(currentChooseType);
            }
        });
        new AlertDialog.Builder(this)
                .setTitle("请选择资源类型")
                .setView(areaView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String areStr = "";
                        String area = areaWy.getSelectedItem();
                    }
                })
                .show();
    }

    private void getTypeFromDb() {
        List<com.skyline.terraexplorer.db.Resource> resourceList = new DbConfig(this).getResourceList();
        typeStrList.add("类型(选填)");
        //TODO
        try{
            for (int i = 0; i < resourceList.size(); i++) {
                typeStrList.add(resourceList.get(i).getName());
            }
        }catch (Exception e){

        }
    }

    /**
     * 从本地数据库里获取数据
     */
    private void getDataFromDb() throws DbException {
        resourceTypeList.clear();

        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        List<com.skyline.terraexplorer.db.Resource> resourceList = null;
        if (leixingView.getText().equals("类型(选填)")){
            resourceList =  x.getDb(new DbConfig(this).getDaoConfig()).selector(com.skyline.terraexplorer.db.Resource.class)
                    .where("state","=","ACTIVE")
                    .where("isdisplay","=","1")
                    .findAll();
        }else {
            resourceList =  x.getDb(new DbConfig(this).getDaoConfig()).selector(com.skyline.terraexplorer.db.Resource.class)
                    .where("state","=","ACTIVE")
                    .where("isdisplay","=","1")
                    .where("name","=", leixingView.getText().toString())
                    .findAll();
        }

        //专业队
     /*   List<TeamDTO> dtoList = db.selector(TeamDTO.class)
                .where("state", "=", "ACTIVE")
                .where("groupid", "=", currentGroupId)
                .findAll();
        List<Resource> TeamDTOResourceList = new ArrayList<>();
        for (int i = 0; i < dtoList.size(); i++) {
            TeamDTO dto = dtoList.get(i);
            TeamDTOResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLatitude()),Double.parseDouble(dto.getLongitude())));
        }
        resourceTypeList.add(new ResourceType("专业队",TeamDTOResourceList));*/


        for (int i = 0; i < resourceList.size(); i++) {
            Log.e(TAG, "getDataFromDb: name=" + resourceList.get(i).getName() );
            com.skyline.terraexplorer.db.Resource resource1 = resourceList.get(i);
            if (resourceList.get(i).getName().equals("护林检查站")) {
                //检查站
                List<CheckStationDTO> checkStationDTOList = db.selector(CheckStationDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("streetno", "=", currentStreeNo)
                        .findAll();
                List<Resource> checkStationDTOResourceList = new ArrayList<>();
                for (int  j= 0; j < checkStationDTOList.size(); j++) {
                    CheckStationDTO dto = checkStationDTOList.get(j);
                    checkStationDTOResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLatitude()),Double.parseDouble(dto.getLongitude()),dto.getType(),resource1.getCode(),resource1.getApiUrl(),dto.getGridId(),
                            dto.getGridName(),dto.getGridNo(),"护林检查站"));
                }
                resourceTypeList.add(new ResourceType("护林检查站",checkStationDTOResourceList));
            }else if (resourceList.get(i).getName().equals("专业队")){
                List<TeamDTO> dtoList = db.selector(TeamDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("streetno", "=", currentStreeNo)
                        .findAll();


                List<Resource> TeamDTOResourceList = new ArrayList<>();
                for (int j = 0; j < dtoList.size(); j++) {
                    TeamDTO dto = dtoList.get(j);
                    TeamDTOResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLatitude()),Double.parseDouble(dto.getLongitude()),dto.getType(),resource1.getCode(),resource1.getApiUrl(),dto.getGridId(),
                            dto.getGridName(),dto.getGridNo(),"专业队"));
                }
                resourceTypeList.add(new ResourceType("专业队",TeamDTOResourceList));
            }else if (resourceList.get(i).getName().equals("指挥部")){
                //指挥部
                List<FireCommandDTO> fireCommandDTOList = db.selector(FireCommandDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("streetno", "=", currentStreeNo)
                        .findAll();
                List<Resource> fireCommandDTOResourceList = new ArrayList<>();
                for (int j = 0; j < fireCommandDTOList.size(); j++) {
                    FireCommandDTO dto = fireCommandDTOList.get(j);
                    fireCommandDTOResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLatitude()),Double.parseDouble(dto.getLongitude()),dto.getType(),resource1.getCode(),resource1.getApiUrl(),dto.getGridId(),
                            dto.getGridName(),dto.getGridNo(),"指挥部"));
                }
                resourceTypeList.add(new ResourceType("指挥部",fireCommandDTOResourceList));
            }else if (resourceList.get(i).getName().equals("物资库")){

                //物资库
                List<MaterialRepositoryDTO> materialRepositoryDTOList = db.selector(MaterialRepositoryDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("streetno", "=", currentStreeNo)
                        .findAll();
                List<Resource> materialRepositoryDTOResourceList = new ArrayList<>();
                for (int j = 0; j < materialRepositoryDTOList.size(); j++) {
                    MaterialRepositoryDTO dto = materialRepositoryDTOList.get(j);
                    materialRepositoryDTOResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLatitude()),Double.parseDouble(dto.getLongitude()),dto.getType(),resource1.getCode(),resource1.getApiUrl(),dto.getGridId(),
                            dto.getGridName(),dto.getGridNo(),"物资库"));
                }
                resourceTypeList.add(new ResourceType("物资库",materialRepositoryDTOResourceList));
            } else if (resourceList.get(i).getName().equals("视频监控点")){

                //视频监控点
                List<MonitorDTO> monitorDTOList = db.selector(MonitorDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("streetno", "=", currentStreeNo)
                        .findAll();
                List<Resource> monitorDTOResourceList = new ArrayList<>();
                for (int j = 0; j < monitorDTOList.size(); j++) {
                    MonitorDTO dto = monitorDTOList.get(j);
                    monitorDTOResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLatitude()),Double.parseDouble(dto.getLongitude()),"","","","","","","视频监控点"));
                }
                resourceTypeList.add(new ResourceType("视频监控点",monitorDTOResourceList));
            }else if (resourceList.get(i).getName().equals("水源地")){
                //水源地
                List<WaterSourceDTO> waterSourceDTOList = db.selector(WaterSourceDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("streetno", "=", currentStreeNo)
                        .findAll();
                List<Resource> waterSourceDTOListResourceList = new ArrayList<>();
                for (int j = 0; j < waterSourceDTOList.size(); j++) {
                    WaterSourceDTO dto = waterSourceDTOList.get(j);
                    waterSourceDTOListResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLatitude()),Double.parseDouble(dto.getLongitude()),"","","","","","","水源地"));
                }
                resourceTypeList.add(new ResourceType("水源地",waterSourceDTOListResourceList));

            }else if (resourceList.get(i).getName().equals("瞭望塔")){
                //瞭望塔
                List<WatchTowerDTO> watchTowerDTOList = db.selector(WatchTowerDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("streetno", "=", currentStreeNo)
                        .findAll();
                List<Resource> watchTowerResourceList = new ArrayList<>();
                for (int j = 0; j < watchTowerDTOList.size(); j++) {
                    WatchTowerDTO dto = watchTowerDTOList.get(j);
                    watchTowerResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLatitude()),Double.parseDouble(dto.getLongitude()),"","","","","","","瞭望塔"));
                }
                resourceTypeList.add(new ResourceType("瞭望塔",watchTowerResourceList));
            }else if (resourceList.get(i).getName().equals("重大危险源")){

                //重大危险源
                List<DangerSourceDTO> dangerSourceDTOList = db.selector(DangerSourceDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("streetno", "=", currentStreeNo)
                        .findAll();
                List<Resource>dangerSourceDTOResourceList = new ArrayList<>();
                for (int j = 0; j < dangerSourceDTOList.size(); j++) {
                    DangerSourceDTO dto = dangerSourceDTOList.get(j);
                    dangerSourceDTOResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLatitude()),Double.parseDouble(dto.getLongitude()),"","","","","","","重大危险源"));
                }
                resourceTypeList.add(new ResourceType("重大危险源",dangerSourceDTOResourceList));
            }else if (resourceList.get(i).getName().equals("墓地")){

                //墓地
                List<CemeteryDTO> cemeteryDTOList = db.selector(CemeteryDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("streetno", "=", currentStreeNo)
                        .findAll();
                List<Resource> cemeteryResourceList = new ArrayList<>();
                for (int j = 0; j < cemeteryDTOList.size(); j++) {
                    CemeteryDTO dto = cemeteryDTOList.get(j);
                    cemeteryResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLatitude()),Double.parseDouble(dto.getLongitude()),"","","","","","","墓地"));
                }
                resourceTypeList.add(new ResourceType("墓地",cemeteryResourceList));
            }else if (resourceList.get(i).getName().equals("直升机机降点")){
                //直升机机降点
                List<HelicopterPointDTO> helicopterPointDTOList = db.selector(HelicopterPointDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("streetno", "=", currentStreeNo)
                        .findAll();
                List<Resource> helicopterPointResourceList = new ArrayList<>();
                for (int j = 0; j < helicopterPointDTOList.size(); j++) {
                    HelicopterPointDTO dto = helicopterPointDTOList.get(j);
                    helicopterPointResourceList.add(new Resource(dto.getId(),dto.getName(),Double.parseDouble(dto.getLat()),Double.parseDouble(dto.getLng()),"","","","","","","直升机机降点"));
                }
                resourceTypeList.add(new ResourceType("直升机机降点",helicopterPointResourceList));
            }

        }

        initData();

    }

    private void initData() {
        items.clear();
        for (int i = 0; i < resourceTypeList.size(); i++) {
            List<Resource> resourceList = resourceTypeList.get(i).getResourceList();
            if (resourceList.size() > 0){
                items.add(resourceTypeList.get(i));
                if (resourceTypeList.get(i).isOpen){

                    for (int j = 0; j < resourceList.size(); j++) {
                        items.add(resourceList.get(j));
                    }
                }
            }

        }
        if (items.size() == 0){
            Toast.makeText(this, "该区域暂无资源点", Toast.LENGTH_SHORT).show();

        }else {
            assertAllRegistered(adapter,items);
            adapter.notifyDataSetChanged();
        }

    }

    private void register() {
        ResourceViewBinder resourceViewBinder = new ResourceViewBinder();
        resourceViewBinder.setListener(this);
        adapter.register(Resource.class, resourceViewBinder);
        ResourceTypeViewBinder resourceTypeViewBinder = new ResourceTypeViewBinder();
        resourceTypeViewBinder.setListener(this);
        adapter.register(ResourceType.class, resourceTypeViewBinder);
    }

    /**
     * 获取所有街道数据
     */
    private void getAllJieDao() {
        jiedaoList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
            jiedaoList = db.selector(Grid.class)
                    .where("state", "=", "ACTIVE")
                    .where("level", "=", "1")
                    .where("parentid", "=", currentQuId)
                    .findAll();

            jiedaoStrList.clear();
            jiedaoStrList.add("请选择街道");
            for (int i = 0; i < jiedaoList.size(); i++) {
                jiedaoStrList.add(jiedaoList.get(i).getName());
            }
            showAreaDialog(jiedaoStrList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有区的数据
     */
    private void getAllQu() {
        quList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
             quList = db.selector(Grid.class)
                    .where("state", "=", "ACTIVE")
                     .and("level", "=", "3")
                     .and("groupid", "like", "001002%")
                    .findAll();

            quStrList.clear();
            quStrList.add("请选择区");
            //TODO
            try{
                for (int i = 0; i < quList.size(); i++) {
                    if (!quList.get(i).getName().equals("高新区")){
                        quStrList.add(quList.get(i).getName());
                    }
                }
            }catch (Exception e){

            }
            showAreaDialog(quStrList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void showAreaDialog(List<String> strList) {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        areaWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        areaWy.setIsLoop(false);
        if (currentChooseArea == 0){
            areaWy.setItems(strList, quSelectIndex);//init selected position is 0 初始选中位置为0
        }else {
            areaWy.setItems(strList, jieDaoSelectIndex);//init selected position is 0 初始选中位置为0
        }

        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                if (currentChooseArea == 0){   //选择省
                    isChooseQu = true;
                    currentChooseQu = areaWy.getSelectedItem();
                    quSelectIndex = areaWy.getSelectedPosition();
                    quText.setText(currentChooseQu);
                    currentChooseJiedao = "";
                    jiedaoText.setText("请选择街道");
                    jieDaoSelectIndex = 0;
                }else {                          //选择市
                    currentChooseJiedao = areaWy.getSelectedItem();
                    jieDaoSelectIndex = areaWy.getSelectedPosition();
                    jiedaoText.setText(currentChooseJiedao);
                }

            }
        });
        new AlertDialog.Builder(this)
                .setTitle("请选择区域")
                .setView(areaView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String areStr = "";
                        String area = areaWy.getSelectedItem();
                    }
                })
                .show();
    }

    /**
     * 资源点类型的点击 条目收缩
     * @param resourceTypeName
     * @param isOpen
     */
    @Override
    public void onResourceTypeItemClickListener(String resourceTypeName, boolean isOpen) {
        for (int i = 0; i < resourceTypeList.size(); i++) {
            if (resourceTypeList.get(i).getResourceTypeName().equals(resourceTypeName)) {
                resourceTypeList.get(i).setOpen(isOpen);
            }
        }
        initData();
    }

    /**
     * 导航回调
     * @param resource
     */
    @Override
    public void onDaoHangClickListener(Resource resource) {
        String jingweiStr = getLocation();
        String starWeidu = "";
        String starJingdu = "";
        if (!jingweiStr.isEmpty()){
            List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
            starWeidu = jingweiList.get(1);
            starJingdu = jingweiList.get(0);
        }
        com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(resource.getLat(),resource.getLng()));


        Poi start = new Poi("", new LatLng(Double.parseDouble(starWeidu),Double.parseDouble(starJingdu)), "");
        Poi end = new Poi(resource.getName(), new LatLng(latLng.latitude,latLng.longitude),"");
        AmapNaviParams params = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER);
        params.setUseInnerVoice(true);
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, ResourceSearchActivity.this);
    }

    /**
     * 资源点选中回调
     * @param resource
     * @param isChoose
     */
    @Override
    public void onItemChooseClickListener(Resource resource, boolean isChoose) {
        int count = 0;
        for (int i = 0; i < resourceTypeList.size(); i++) {
            List<Resource> resourceList = resourceTypeList.get(i).getResourceList();
            for (int j = 0; j < resourceList.size(); j++) {
                if (resourceList.get(j).isChoose) {
                    count ++;
                }
            }
        }
        if (count >= 4 && isChoose == true){
            Toast.makeText(this, "最多可以选取4个资源点", Toast.LENGTH_SHORT).show();
        }else {
            for (int i = 0; i < resourceTypeList.size(); i++) {
                List<Resource> resourceList = resourceTypeList.get(i).getResourceList();
                for (int j = 0; j < resourceList.size(); j++) {
                    if (resourceList.get(j).getId().equals(resource.getId())) {
                        resourceList.get(j).setChoose(isChoose);
                    }
                }
            }

            initData();
        }


    }

    /**
     * 资源点检查的点击
     * @param resource
     */
    @Override
    public void onItemCheckClickListener(Resource resource) {
        Intent intent = new Intent(getApplicationContext(), ResourceCheckActivity.class);
        intent.putExtra("ID", resource.getId());
        intent.putExtra("NAME", resource.getName());
        intent.putExtra("TYPE", resource.getType());
        intent.putExtra("CODE", resource.getCode());
        intent.putExtra("APIURL", resource.getApiUrl());
        intent.putExtra("GRID_ID", resource.getGridId());
        intent.putExtra("GRID_NAME", resource.getGridName());
        intent.putExtra("GRID_NO", resource.getGridNo());
        startActivity(intent);
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
