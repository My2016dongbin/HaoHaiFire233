/*
package com.skyline.terraexplorer.controllers;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ExpandableListView;


import com.skyline.teapi.AltitudeTypeCode;
import com.skyline.teapi.IMeshLayer;
import com.skyline.teapi.IPopupMessage;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.IProjectTree;
import com.skyline.teapi.ISGWorld;
import com.skyline.teapi.ITerrainImageLabel;
import com.skyline.teapi.ITerrainRasterLayer;
import com.skyline.teapi.ItemCode;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.db.CemeteryDTO;
import com.skyline.terraexplorer.db.CheckStationDTO;
import com.skyline.terraexplorer.db.DangerSourceDTO;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.db.FireCommandDTO;
import com.skyline.terraexplorer.db.HelicopterPointDTO;
import com.skyline.terraexplorer.db.MaterialRepositoryDTO;
import com.skyline.terraexplorer.db.MonitorDTO;
import com.skyline.terraexplorer.db.Resource;
import com.skyline.terraexplorer.db.TeamDTO;
import com.skyline.terraexplorer.db.WatchTowerDTO;
import com.skyline.terraexplorer.db.WaterSourceDTO;
import com.skyline.terraexplorer.models.ContextMenuEntry;
import com.skyline.terraexplorer.models.DisplayGroupItem;
import com.skyline.terraexplorer.models.DisplayItem;
import com.skyline.terraexplorer.models.ImageLabel;
import com.skyline.terraexplorer.models.ImageLabels;
import com.skyline.terraexplorer.models.ResourceModel;
import com.skyline.terraexplorer.models.SaveData;
import com.skyline.terraexplorer.models.TEImageHelper;
import com.skyline.terraexplorer.models.TableDataSource;
import com.skyline.terraexplorer.models.TableDataSourceDelegateBase;
import com.skyline.terraexplorer.models.ToolManager;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.models.UI.HeaderOptions;
import com.skyline.terraexplorer.tools.EditFeatureLayerTool;
import com.skyline.terraexplorer.tools.ProjectsTool;
import com.skyline.terraexplorer.views.SegmentedControl;
import com.skyline.terraexplorer.views.SegmentedControl.SegmentedControlDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Callable;

*/
/**
 * 重点资源选择界面
 *//*

public class ResourceSelectActivity extends MatchParentActivity implements SegmentedControlDelegate {


    private static final String TAG = ResourceSelectActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private String gridNo;
    private String groupId;

    private class TableDataSourceDelegate extends TableDataSourceDelegateBase {
        @Override
        public void didSelectRowAtIndexPath(long packedPosition) {
            Log.e(TAG, "sudu---------1 ");
            //   showDialogProgress(progressDialog, "加载中...");
            ResourceSelectActivity.this.didSelectRowAtIndexPath(packedPosition);
        }

        @Override
        public ContextMenuEntry[] contextMenuForPath(long packedPosition) {
            Log.e(TAG, "contextMenuForPath: ");
            return ResourceSelectActivity.this.contexMenuForPath(packedPosition);
        }

        @Override
        public void contextMenuItemTapped(int menuId, long packedPosition) {
            Log.e(TAG, "contextMenuItemTapped: ");
            ResourceSelectActivity.this.contextMenuItemTapped(menuId, packedPosition);
        }

        @Override
        public void accessoryButtonTappedForRowWithIndexPath(long packedPosition) {
            Log.e(TAG, "accessoryButtonTappedForRowWithIndexPath: ");
            ResourceSelectActivity.this.accessoryButtonTappedForRowWithIndexPath(packedPosition);
        }

        @Override
        public boolean accessoryButtonTappableForRowWithIndexPath(
                long packedPosition) {
            Log.e(TAG, "accessoryButtonTappableForRowWithIndexPath: ");
            return true;
        }
    }

    private static final int ITEM_FEATURE_LAYER = 1;
    private static final int ITEM_RASTER_LAYER = 2;
    private static final int ITEM_MESH_LAYER = 3;
    private static final int ITEM_GROUP = 4;
    private static final int ITEM_UP = 5;
    private static final int ITEM_OBJECTS = 6;

    public static final String SLOPE_MAP_ID = "com.skyline.terraexplorer.SLOPE_MAP_ID";
    public static final String CONTOUR_MAP_ID = "com.skyline.terraexplorer.CONTOUR_MAP_ID";


    private TableDataSource dataSource;
    private SegmentedControl segmentedControl;
    private ArrayList<String> modifyTerrainObjects;

    private List<ImageLabels> imageLabelsList = new ArrayList<ImageLabels>();

    private TableDataSourceDelegate dataSourceDelegate = new TableDataSourceDelegate();

    private ArrayList<DisplayItem> childItems = new ArrayList<DisplayItem>();
    private ArrayList<DisplayGroupItem> displayGroupItems = new ArrayList<DisplayGroupItem>();
    DisplayGroupItem displayGroupItem = new DisplayGroupItem("重点资源");
    ITerrainImageLabel imageLabel = null;
    private String access_token;
    private static final int GET_RESOURCE = 1;

    private static final int SHOW_DIALOG = 2;
    private static final int SHOW_DATA = 4;
    private static final int HIDEN_DIALOG = 3;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "onReceive: 8");
            Bundle data = msg.getData();
            int what = data.getInt("what");
            switch (what) {
                case SHOW_DIALOG:
                    showDialogProgress(progressDialog, "加载中...");
                    Log.e(TAG, "getResourceFromDb: 11--------------------110" );
                    // getResourceFromDb();

                    mHandler.sendEmptyMessageDelayed(HIDEN_DIALOG,10000);
                    break;
                case SHOW_DATA:
                    // showDialogProgress(progressDialog, "加载中...");
                    getResourceFromDb();
                    break;
                case HIDEN_DIALOG:
                    Log.e(TAG, "getResourceFromDb: 11--------------------2" );
                    progressDialog.dismiss();
                    dataSource.reloadData();
                    break;

            }

        }
    };
    private  List<ResourceModel> resourceModelList;
    private   DisplayItem displayItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_select);

        Log.e(TAG, "onCreate:----------------- " + getApplicationContext().getExternalFilesDir("") + "/resource/");

        progressDialog = new ProgressDialog(this);
        //在这里尝试去除层次的搜索
        EnumSet<HeaderOptions> options = EnumSet.noneOf(HeaderOptions.class);
        if (getIntent().getBooleanExtra(ProjectsTool.DISABLE_BACK_BUTTON, false))
            options = EnumSet.of(HeaderOptions.NoBackButton);
        // add header
        UI.addHeader(R.string.title_activity_resource, R.drawable.tool_resource_query, this, options);

        resourceModelList = new ArrayList<>();
        groupId = new DbConfig(this).getUser().getGroupId();


        segmentedControl = (SegmentedControl) findViewById(R.id.resource_segmentedControl);

        //这里有两种排序方式：一种是按类型排序，一种是按组排序，但是在这里按组排序好像没有用到，所以在这里暂时停掉
        //segmentedControl.setButtons(new int[]{R.string.layers_show_by_type, R.string.layers_show_by_group});
//        segmentedControl.setButtons(new int[]{R.string.layers_show_by_type});  // 停掉按组排序
        segmentedControl.setOnFilterValueChangedListener(this);

        modifyTerrainObjects = new ArrayList<String>();


        dataSource = new TableDataSource((ExpandableListView) findViewById(R.id.resource_list), dataSourceDelegate);


        //从本地获取资源列表数据
        getResourceListToDb();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    */
/**
     * 从数据库获取资源文件
     *//*

    private void getResourceListToDb() {
        List<Resource> resourceList = new DbConfig(this).getResourceList();

        List<ImageLabels> t = null;
        try {
            t = SaveData.getData(getApplicationContext(), "resource");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (t != null) {
            imageLabelsList = t;
        }
        for (int i = 0; i < resourceList.size(); i++) {
            DisplayItem displayItem = new DisplayItem();
            displayItem.id = resourceList.get(i).getId();

            displayItem.name = resourceList.get(i).getName();

            displayItem.icon = R.drawable.checkbox_off;
            displayItem.json = resourceList.get(i).getApiUrl();
            displayItem.img = resourceList.get(i).getIconFile();
            displayItem.code = resourceList.get(i).getCode();
            displayItem.groupId = resourceList.get(i).getGroupId();
            displayItem.apiUrl = resourceList.get(i).getApiUrl();
            Log.e(TAG, "getResourceListToDb: " + displayItem.name  + "--" + displayItem.apiUrl );
            for (int j = 0; j < imageLabelsList.size(); j++) {
                if (imageLabelsList.get(j).id.equals(displayItem.id)) {
                    displayItem.icon = R.drawable.checkbox_on;
                }
            }
            childItems.add(displayItem);

        }
        displayGroupItem.childItems = childItems;
        displayGroupItems.add(displayGroupItem);

        dataSource.setDataItems(displayGroupItems);
    }

    private void loginGetToken() {
        showDialogProgress(progressDialog, "加载中...");
        final RequestParams params = new RequestParams("http://27.223.18.10:10100/auth/oauth/token");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("username", "geyang");
        params.addParameter("password", "123456");
        params.addParameter("grant_type", "password");
        params.addParameter("client_id", "client_password");
        params.addParameter("client_secret", "123456");
        params.setConnectTimeout(10000);
        Log.e(TAG, "loginByPassword: param---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    access_token = jsonObject.getString("access_token");

                    getResourceList();


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

    private void getResourceList() {
        JSONObject jsonObject = new JSONObject();
        RequestParams params = new RequestParams("http://27.223.18.10:10100/api/resourceList/list");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization", "bearer " + access_token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        imageLabelsList.clear();
                        List<ImageLabels> t = SaveData.getData(getApplicationContext(), "resource");
                        Log.e(TAG, "onSuccess: t.size==" + t.size());
                        if (t != null) {
                            imageLabelsList = t;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject dataJSONObject = data.getJSONObject(i);
                            DisplayItem displayItem = new DisplayItem();
                            if (dataJSONObject.getString("isDisplay").equals("1")) {
                                displayItem.id = dataJSONObject.getString("id");

                                displayItem.name = dataJSONObject.getString("name");

                                displayItem.icon = R.drawable.checkbox_off;
                                displayItem.json = dataJSONObject.getString("apiUrl");
                                displayItem.img = dataJSONObject.getString("iconFile");
                                for (int j = 0; j < imageLabelsList.size(); j++) {
                                    Log.e(TAG, "onSuccess: imageLabelsList id--- " + imageLabelsList.get(j).id);
                                    Log.e(TAG, "onSuccess: displayItem.id----" + displayItem.id);
                                    if (imageLabelsList.get(j).id.equals(displayItem.id)) {
                                        displayItem.icon = R.drawable.checkbox_on;
                                    }
                                }
                                childItems.add(displayItem);
                            }
                        }
                        displayGroupItem.childItems = childItems;
                        displayGroupItems.add(displayGroupItem);

                        dataSource.setDataItems(displayGroupItems);
                        Log.e(TAG, "onSuccess: 11");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁时将数据缓存
        Log.e(TAG, "onDestroy: " + imageLabelsList.size());

    }

    @Override
    protected void onPause() {
        super.onPause();
        //imageLabelsList.clear();
        SaveData.setData(getApplicationContext(), imageLabelsList, "resource");
        Log.e(TAG, "onPause: ---" + imageLabelsList.size());
        Intent intent1 = new Intent();
        intent1.setAction("item_click");
        this.sendBroadcast(intent1);
    }

    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    */
/**
     * 选择了列表中的资源选项
     *
     * @param packedPosition
     *//*

    private void didSelectRowAtIndexPath(final long packedPosition) {

        Log.e(TAG, "sudu---------2 ");
        final DisplayItem item = dataSource.getItemForPath(packedPosition);
        if (item.tag == ITEM_UP) {
            accessoryButtonTappedForRowWithIndexPath(packedPosition);
            return;
        }


        for (DisplayItem a : childItems) {
            if (a.id.equals(item.id)) {
                displayItem = a;
            }
        }
        boolean makeVisible = item.icon == R.drawable.checkbox_off;

        if (makeVisible){
            item.icon = R.drawable.checkbox_on;

        } else{
            item.icon = R.drawable.checkbox_off;

        }
        dataSource.reloadData();

        if (displayItem != null) {
            Log.e(TAG, "didSelectRowAtIndexPath: makeVisible=" + makeVisible);
            if (makeVisible) {
                //从服务器获取资源点
                //  getResourceFromService(displayItem, item);
                //从本地数据库获取资源点
                getResourceFromDb();
               */
/* Log.e(TAG, "sudu---------3 ");
                Message msg = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putInt("what", SHOW_DIALOG);
                msg.setData(b);
                mHandler.sendMessage(msg);*//*


              */
/*  Message msg1 = mHandler.obtainMessage();
                Bundle b1 = new Bundle();
                b1.putInt("what", SHOW_DATA);
                msg1.setData(b1);
                mHandler.sendMessage(msg1);*//*




            } else {
                //清除显示
                Log.e(TAG, "didSelectRowAtIndexPath: ---- ");
                final DisplayItem finalDisplayItem = displayItem;
                UI.runOnRenderThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < imageLabelsList.size(); i++) {
                            if (imageLabelsList.get(i).id.equals(finalDisplayItem.id)) {
                                List<ImageLabel> labels = imageLabelsList.get(i).labelList;
                                for (int j = 0; j < labels.size(); j++) {
                                    try {
                                        Log.e(TAG, "didSelectRowAtIndexPath: ---1");
                                        ISGWorld.getInstance().getCreator().DeleteObject(labels.get(j).objectId);
                                    } catch (Exception ex) {
                                        Log.e("", ex.getMessage());
                                        continue;
                                    }

                                }
                                imageLabelsList.remove(i);
                            }
                        }

                        mHandler.sendEmptyMessageDelayed(HIDEN_DIALOG,1000);
                        //  dataSource.reloadData();
                    }
                });

            }

        }

    }

    */
/**
     * 从本地数据库获取资源信息
     *
     *//*

    private void getResourceFromDb() {
        //  Toast.makeText(this, "开始", Toast.LENGTH_SHORT).show();

        Log.e(TAG, "getResourceFromDb: 11--------------------groupId = " + groupId );

        resourceModelList.clear();

        getData();

        Log.e(TAG, "sudu---------4 resourceModelList.size" + resourceModelList.size());

        UI.runOnRenderThread(new Runnable() {
            private String gridId;
            private String gridNo;
            private String gridName;
            private String leaderPhone;
            private String leaderName;
            private String description;

            @Override
            public void run() {
                try {
                    Log.e(TAG, "sudu---------5 ");

                    if (displayItem != null) {
                        String groupId = ISGWorld.getInstance().getProjectTree().FindItem(displayItem.name);


                        int imgId = R.drawable.star_yellow;
                        //  String fileName = "";
                        if (displayItem.img.equals("res_city_team.png")) { //专业队 res_city_team.png
                            imgId = R.drawable.ic_team;
                        } else if (displayItem.img.equals("res_check_station.png")) { //护林检查站
                            imgId = R.drawable.ic_check_station;
                        } else if (displayItem.img.equals("res_video_monitor.png")) {  //视频监控点	res_video_monitor.png
                            imgId = R.drawable.ic_monitor;
                        } else if (displayItem.img.equals("res_water_source.png")) { //水源地	res_water_source.png
                            imgId = R.drawable.ic_water_source;
                        } else if (displayItem.img.equals("res_observation_tower.png")) { //瞭望塔	res_observation_tower.png
                            imgId = R.drawable.ic_watch_tower;
                        } else if (displayItem.img.equals("res_key_target.png")) {  //重点防火目标	res_key_target.png
                            imgId = R.drawable.res_danger_source;
                        } else if (displayItem.img.equals("res_city_command.png")) { //指挥部	res_city_command.png
                            imgId = R.drawable.ic_res_city_command;
                        } else if (displayItem.img.equals("res_danger_source.png")) { //重大危险源	res_danger_source.png
                            imgId = R.drawable.ic_danger_source;
                        }  else if (displayItem.img.equals("res_cemetery.png")) { //墓地	res_cemetery.png
                            imgId = R.drawable.ic_cemetery;
                        } else if (displayItem.img.equals("res_material.png")) { //物资库	res_material.png
                            imgId = R.drawable.ic_material_repository;
                        } else if (displayItem.img.equals("res_helicopter.png")) { //直升机机降点
                            imgId = R.drawable.ic_helicopter;
                        }
                        Log.e(TAG, "onSuccess: 1111111");

                        String fileName = TEImageHelper.prepareImageForTE(imgId, 35);

                        ImageLabels addLabels = new ImageLabels();
                        addLabels.groupId = groupId;
                        addLabels.id = displayItem.id;
                        addLabels.imageId = imgId;
                        addLabels.isChecked = true;
                        addLabels.type = displayItem.name;

                        Log.e(TAG, "run:fileName-- " + fileName );

                        addLabels.labelList = new ArrayList<ImageLabel>();
                        Log.e(TAG, "sudu---------6 ");
                        for (int i = 0; i < resourceModelList.size(); i++) {
                            try {
                                double lng = Double.parseDouble(resourceModelList.get(i).getLng());
                                double lat = Double.parseDouble(resourceModelList.get(i).getLat());
                                String uuId = resourceModelList.get(i).getId();
                                IPosition position = ISGWorld.getInstance().getCreator().CreatePosition(lng, lat, 0, AltitudeTypeCode.ATC_ON_TERRAIN);
                                String name = resourceModelList.get(i).getName();

                                String description = resourceModelList.get(i).getDescription();

                                String leaderName = resourceModelList.get(i).getLeaderName();
                                String leaderPhone = resourceModelList.get(i).getLeaderPhone();
                                String gridName = resourceModelList.get(i).getGridName();
                                String gridNo = resourceModelList.get(i).getGridNo();
                                String gridId = resourceModelList.get(i).getGridId();
                                String wanggeStr = resourceModelList.get(i).getWanggeStr();

                                imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(position, fileName, null, groupId, name);
                                imageLabel.getMessage().setMessageID(bindMessage(name));
                                addLabels.labelList.add(new ImageLabel(imageLabel.getID(), lng, lat, name, resourceModelList.get(i).getResourceType(), uuId, description, leaderName, leaderPhone,displayItem.code,displayItem.apiUrl,gridName,gridId,gridNo,wanggeStr));


                            } catch (Exception e) {
                                continue;
                            }

                        }
                        //   progressDialog.dismiss();
                        if (imageLabelsList.indexOf(addLabels) < 0) {
                            Log.e(TAG, "sudu---------8 ");
                            imageLabelsList.add(addLabels);
                        }
                       */
/* Message msg = mHandler.obtainMessage();
                        Bundle b1 = new Bundle();
                        b1.putInt("what", HIDEN_DIALOG);
                        msg.setData(b1);
                        mHandler.sendMessage(msg);*//*


                        // SaveData.setData(getApplicationContext(), imageLabelsList, "resource");
                        dataSource.reloadData();

                    }
                } catch (Exception ex) {

                    Log.e(ResourceSelectActivity.this.getClass().toString(), ex.getMessage());
                }


            }
        });

    }

    private void getData() {
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
            if (displayItem.code.equals("team")){                //专业队
                List<TeamDTO> teamDTOList = db.selector(TeamDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId +"%")
                        .findAll();
                if (teamDTOList!=null){
                    for (int i = 0; i < teamDTOList.size(); i++) {
                        String id = teamDTOList.get(i).getId();
                        String longitude = teamDTOList.get(i).getLongitude();
                        String latitude = teamDTOList.get(i).getLatitude();
                        String name = teamDTOList.get(i).getName();
                        String description = teamDTOList.get(i).getDescription();
                        String leaderName = teamDTOList.get(i).getLeaderName();
                        String leaderPhone = teamDTOList.get(i).getLeaderPhone();
                        String resourceType = teamDTOList.get(i).getResourceType();
                        String gridName = teamDTOList.get(i).getGridName();
                        String gridId = teamDTOList.get(i).getGridId();
                        String gridNo = teamDTOList.get(i).getGridNo();
                        String districtName = teamDTOList.get(i).getDistrictName();
                        String streetname = teamDTOList.get(i).getStreetname();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone,resourceType,gridName,gridNo,gridId,districtName+streetname+gridName);
                        resourceModelList.add(resourceModel);
                    }
                }

            }else if (displayItem.code.equals("checkStation")){      //检查站
                List<CheckStationDTO> dtoList = db.selector(CheckStationDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId+"%")
                        .findAll();
                if (dtoList!=null){
                    for (int i = 0; i < dtoList.size(); i++) {
                        String id = dtoList.get(i).getId();
                        String longitude = dtoList.get(i).getLongitude();
                        String latitude = dtoList.get(i).getLatitude();
                        String name = dtoList.get(i).getName();
                        String description = dtoList.get(i).getDescription();
                        String leaderName = dtoList.get(i).getLeaderName();
                        String leaderPhone = dtoList.get(i).getLeaderPhone();
                        String resourceType = dtoList.get(i).getResourceType();
                        String gridName = dtoList.get(i).getGridName();
                        String gridId = dtoList.get(i).getGridId();
                        String gridNo = dtoList.get(i).getGridNo();
                        String districtName = dtoList.get(i).getDistrictName();
                        String streetname = dtoList.get(i).getStreetName();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone,resourceType,gridName,gridNo,gridId,districtName+streetname+gridName);
                        resourceModelList.add(resourceModel);
                    }
                }
            }else if (displayItem.code.equals("monitor")){      //视频监控点
                List<MonitorDTO> dtoList = db.selector(MonitorDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId+"%")
                        .findAll();
                if (dtoList != null){
                    for (int i = 0; i < dtoList.size(); i++) {
                        String id = dtoList.get(i).getId();
                        String longitude = dtoList.get(i).getLongitude();
                        String latitude = dtoList.get(i).getLatitude();
                        String name = dtoList.get(i).getName();
                        String description = " ";
                        String leaderName = dtoList.get(i).getLeaderName();
                        String leaderPhone = dtoList.get(i).getLeaderPhone();
                        String resourceType = dtoList.get(i).getResourceType();
                        String gridName = dtoList.get(i).getGridName();
                        String gridId = dtoList.get(i).getGridId();
                        String gridNo = dtoList.get(i).getGridNo();
                        String districtName = dtoList.get(i).getDistrictName();
                        String streetname = dtoList.get(i).getStreetName();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone,resourceType,gridName,gridNo,gridId,districtName+streetname+gridName);
                        resourceModelList.add(resourceModel);
                    }
                }
            }else if (displayItem.code.equals("waterSource")){      //水源地
                List<WaterSourceDTO> dtoList = db.selector(WaterSourceDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId+"%")
                        .findAll();
                if (dtoList != null){
                    for (int i = 0; i < dtoList.size(); i++) {
                        String id = dtoList.get(i).getId();
                        String longitude = dtoList.get(i).getLongitude();
                        String latitude = dtoList.get(i).getLatitude();
                        String name = dtoList.get(i).getName();
                        String description = " ";
                        String leaderName = dtoList.get(i).getLeaderName();
                        String leaderPhone = dtoList.get(i).getLeaderPhone();
                        String resourceType = dtoList.get(i).getResourceType();
                        String gridName = dtoList.get(i).getGridName();
                        String gridId = dtoList.get(i).getGridId();
                        String gridNo = dtoList.get(i).getGridNo();
                        String districtName = dtoList.get(i).getDistrictName();
                        String streetname = dtoList.get(i).getStreetName();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone,resourceType,gridName,gridNo,gridId,districtName+streetname+gridName);
                        resourceModelList.add(resourceModel);
                    }
                }
            }else if (displayItem.code.equals("watchTower")){      //瞭望塔
                List<WatchTowerDTO> dtoList = db.selector(WatchTowerDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId+"%")
                        .findAll();
                if (dtoList != null){
                    for (int i = 0; i < dtoList.size(); i++) {
                        String id = dtoList.get(i).getId();
                        String longitude = dtoList.get(i).getLongitude();
                        String latitude = dtoList.get(i).getLatitude();
                        String name = dtoList.get(i).getName();
                        String description = " ";
                        String leaderName = dtoList.get(i).getLeaderName();
                        String leaderPhone = dtoList.get(i).getLeaderPhone();
                        String resourceType = dtoList.get(i).getResourceType();
                        String gridName = dtoList.get(i).getGridName();
                        String gridId = dtoList.get(i).getGridId();
                        String gridNo = dtoList.get(i).getGridNo();
                        String districtName = dtoList.get(i).getDistrictName();
                        String streetname = dtoList.get(i).getStreetName();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone,resourceType,gridName,gridNo,gridId,districtName+streetname+gridName);
                        resourceModelList.add(resourceModel);
                    }
                }
            }else if (displayItem.code.equals("fireCommand")){      //指挥部
                List<FireCommandDTO> dtoList = db.selector(FireCommandDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId+"%")
                        .findAll();
                if (dtoList != null){
                    for (int i = 0; i < dtoList.size(); i++) {
                        String id = dtoList.get(i).getId();
                        String longitude = dtoList.get(i).getLongitude();
                        String latitude = dtoList.get(i).getLatitude();
                        String name = dtoList.get(i).getName();
                        String description = " ";
                        String leaderName = dtoList.get(i).getLeaderName();
                        String leaderPhone = dtoList.get(i).getLeaderPhone();
                        String resourceType = dtoList.get(i).getResourceType();
                        String gridName = dtoList.get(i).getGridName();
                        String gridId = dtoList.get(i).getGridId();
                        String gridNo = dtoList.get(i).getGridNo();
                        String districtName = dtoList.get(i).getDistrictName();
                        String streetname = dtoList.get(i).getStreetname();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone,resourceType,gridName,gridNo,gridId,districtName+streetname+gridName);
                        resourceModelList.add(resourceModel);
                    }
                }
            }else if (displayItem.code.equals("dangerSource")){      //重大危险源
                List<DangerSourceDTO> dtoList = db.selector(DangerSourceDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId+"%")
                        .findAll();
                if (dtoList != null){
                    for (int i = 0; i < dtoList.size(); i++) {
                        String id = dtoList.get(i).getId();
                        String longitude = dtoList.get(i).getLongitude();
                        String latitude = dtoList.get(i).getLatitude();
                        String name = dtoList.get(i).getName();
                        String description = " ";
                        String leaderName = dtoList.get(i).getLeaderName();
                        String leaderPhone = dtoList.get(i).getLeaderPhone();
                        String resourceType = dtoList.get(i).getResourceType();
                        String gridName = dtoList.get(i).getGridName();
                        String gridId = dtoList.get(i).getGridId();
                        String gridNo = dtoList.get(i).getGridNo();
                        String districtName = dtoList.get(i).getDistrictName();
                        String streetname = dtoList.get(i).getStreetName();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone,resourceType,gridName,gridNo,gridId,districtName+streetname+gridName);
                        resourceModelList.add(resourceModel);
                    }
                }
            }else if (displayItem.code.equals("cemetery")){      //墓地
                List<CemeteryDTO> dtoList = db.selector(CemeteryDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId+"%")
                        .findAll();
                if (dtoList != null){
                    for (int i = 0; i < dtoList.size(); i++) {
                        String id = dtoList.get(i).getId();
                        String longitude = dtoList.get(i).getLongitude();
                        String latitude = dtoList.get(i).getLatitude();
                        String name = dtoList.get(i).getName();
                        String description = " ";
                        String leaderName = dtoList.get(i).getLeaderName();
                        String leaderPhone = dtoList.get(i).getLeaderPhone();
                        String resourceType = dtoList.get(i).getResourceType();
                        String gridName = dtoList.get(i).getGridName();
                        String gridId = dtoList.get(i).getGridId();
                        String gridNo = dtoList.get(i).getGridNo();
                        String districtName = dtoList.get(i).getDistrictName();
                        String streetname = dtoList.get(i).getStreetName();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone,resourceType,gridName,gridNo,gridId,districtName+streetname+gridName);
                        resourceModelList.add(resourceModel);
                    }
                }
            }else if (displayItem.code.equals("materialRepository")){      //物资库
                List<MaterialRepositoryDTO> dtoList = db.selector(MaterialRepositoryDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId+"%")
                        .findAll();
                if (dtoList != null){
                    for (int i = 0; i < dtoList.size(); i++) {
                        String id = dtoList.get(i).getId();
                        String longitude = dtoList.get(i).getLongitude();
                        String latitude = dtoList.get(i).getLatitude();
                        String name = dtoList.get(i).getName();
                        String description = " ";
                        String leaderName = dtoList.get(i).getLeaderName();
                        String leaderPhone = dtoList.get(i).getLeaderPhone();
                        String resourceType = dtoList.get(i).getResourceType();
                        String gridName = dtoList.get(i).getGridName();
                        String gridId = dtoList.get(i).getGridId();
                        String gridNo = dtoList.get(i).getGridNo();
                        String districtName = dtoList.get(i).getDistrictName();
                        String streetname = dtoList.get(i).getStreetName();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone,resourceType,gridName,gridNo,gridId,districtName+streetname+gridName);
                        resourceModelList.add(resourceModel);
                    }
                }
            }else if (displayItem.code.equals("helicopterPoint")){      //直升机机降点
                List<HelicopterPointDTO> dtoList = db.selector(HelicopterPointDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId+"%")
                        .findAll();
                if (dtoList != null){
                    for (int i = 0; i < dtoList.size(); i++) {
                        String id = dtoList.get(i).getId();
                        String longitude = dtoList.get(i).getLng();
                        String latitude = dtoList.get(i).getLat();
                        String name = dtoList.get(i).getName();
                        String description = " ";
                        String leaderName = dtoList.get(i).getLeaderName();
                        String leaderPhone = dtoList.get(i).getLeaderPhone();
                        String resourceType = dtoList.get(i).getResourceType();
                        String gridName = dtoList.get(i).getGridName();
                        String gridId = dtoList.get(i).getGridId();
                        String gridNo = dtoList.get(i).getGridNo();

                        String districtName = dtoList.get(i).getDistrictName();
                        String streetname = dtoList.get(i).getStreetName();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone,resourceType,gridName,gridNo,gridId,districtName+streetname+gridName);
                        resourceModelList.add(resourceModel);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public String bindMessage(String text) {
        String caption = "标题";
        int left = 20;
        int top = 20;
        int timeout = -1;
        IPopupMessage mess = ISGWorld.getInstance().getCreator().CreatePopupMessage(caption, null, left, top, 30, 20, timeout);
        //mess.setInnerHTML("<div>Hello World!</div>");
        mess.setInnerText(text);
        mess.setShowCaption(false);
        return mess.getID();
    }




    @Override
    public void onFilterValueChanged(SegmentedControl sender) {
        int index = sender.getSelectedSegmentIndex();
        switch (index) {
            case 0:
                break;
            case 1:
                break;
        }
    }


    private DisplayGroupItem getDisplayGroupForGroup(final String itemIdParam) {
        return UI.runOnRenderThread(new Callable<DisplayGroupItem>() {
            @Override
            public DisplayGroupItem call() throws Exception {
                String itemId = itemIdParam;
                IProjectTree projectTree = ISGWorld.getInstance().getProjectTree();
                DisplayGroupItem group = new DisplayGroupItem(null);
                if (itemId.equals(projectTree.getRootID()) == false) {
                    DisplayItem item = new DisplayItem(R.string.layers_up, R.drawable.blank);
                    item.tag = ITEM_UP;
                    item.accessoryIcon = R.drawable.one_level_up;
                    item.id = projectTree.GetNextItem(itemId, ItemCode.PARENT);
                    group.name = projectTree.GetItemName(itemId);
                    group.childItems.add(item);
                }
                itemId = projectTree.GetNextItem(itemId, ItemCode.CHILD);
                while (itemId.equals("") == false) {
                    int visible = projectTree.GetVisibility(itemId);
                    // do not show items without checkboxes
                    if (visible != -1) {
                        String name = projectTree.GetItemName(itemId);
                        int icon = visible == 0 ? R.drawable.checkbox_off : R.drawable.checkbox_on;
                        DisplayItem item = new DisplayItem(name, icon);
                        item.id = itemId;
                        if (projectTree.IsGroup(itemId) && projectTree.IsLayer(itemId) == false) {
                            item.accessoryIcon = R.drawable.open_group;
                            item.tag = ITEM_GROUP;
                            item.name = name;
                            //NSMutableAttributedString * attributedName = [[NSMutableAttributedString  alloc] initWithString:name];
                            //[attributedName setAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                            //                               [UIFont boldSystemFontOfSize:[TEAUI fontSizeLarge]], NSFontAttributeName, nil] range:NSMakeRange(0, [name length])];

                            //item.attributedName = attributedName;
                            item.tag = ITEM_GROUP;
                        }
                        group.childItems.add(item);
                    }
                    itemId = projectTree.GetNextItem(itemId, ItemCode.NEXT);
                }
                return group;
            }

        });
    }

    private ContextMenuEntry[] contexMenuForPath(long packedPosition) {
        DisplayItem item = dataSource.getItemForPath(packedPosition);
        switch (item.tag) {
            case ITEM_FEATURE_LAYER:
                return new ContextMenuEntry[]{
                        new ContextMenuEntry(R.drawable.refresh, 1),
//						new ContextMenuEntry(R.drawable.sync, 2),
                };
            case ITEM_RASTER_LAYER:
            case ITEM_MESH_LAYER:
                return new ContextMenuEntry[]{
                        new ContextMenuEntry(R.drawable.refresh, 1),
                };
        }
        return null;
    }

    private void accessoryButtonTappedForRowWithIndexPath(long packedPosition) {
        DisplayItem item = dataSource.getItemForPath(packedPosition);
        if (item.tag == ITEM_UP || item.tag == ITEM_GROUP) // go to group
        {
            dataSource.setDataItems(new DisplayGroupItem[]{getDisplayGroupForGroup(item.id)});
        } else if (item.tag == ITEM_FEATURE_LAYER) // edit layer
        {
            ToolManager.INSTANCE.openTool(EditFeatureLayerTool.class.getName(), item.id);
            finish();
        }
    }

    private void contextMenuItemTapped(final int menuId, long packedPosition) {
        final DisplayItem item = dataSource.getItemForPath(packedPosition);
        UI.runOnRenderThreadAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (menuId) {
                        case 1: // refresh
                        {
                            switch (item.tag) {
                                case ITEM_FEATURE_LAYER:
                                    ISGWorld.getInstance().getProjectTree().GetLayer(item.id).Refresh();
                                    break;
                                case ITEM_RASTER_LAYER:
                                    ISGWorld.getInstance().getCreator().GetObject(item.id).CastTo(ITerrainRasterLayer.class).Refresh(0);
                                    break;
                                case ITEM_MESH_LAYER:
                                    ISGWorld.getInstance().getCreator().GetObject(item.id).CastTo(IMeshLayer.class).Refresh();
                                    break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    // refresh may fail, i.e layer does not exists on disk 刷新可能会失败，即磁盘上不存在图层
                    // ignore it. 忽略它。
                }
            }
        });
    }

    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }


    //
}*/
