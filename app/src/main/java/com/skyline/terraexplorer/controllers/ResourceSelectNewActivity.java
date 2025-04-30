package com.skyline.terraexplorer.controllers;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.skyline.teapi.AltitudeTypeCode;
import com.skyline.teapi.IPopupMessage;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.teapi.ITerrainImageLabel;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
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
import com.skyline.terraexplorer.models.Container;
import com.skyline.terraexplorer.models.DisplayItem;
import com.skyline.terraexplorer.models.ImageLabel;
import com.skyline.terraexplorer.models.ImageLabels;
import com.skyline.terraexplorer.models.ResourceModel;
import com.skyline.terraexplorer.models.SaveData;
import com.skyline.terraexplorer.models.TEImageHelper;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.multitype.ResourceChoose;
import com.skyline.terraexplorer.multitype.ResourceChooseViewBinder;
import com.skyline.terraexplorer.utils.RxViewAction;
import com.skyline.terraexplorer.views.SegmentedControl;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ResourceSelectNewActivity extends HBaseActivity implements ResourceChooseViewBinder.OnResourceChooseItemClick ,View.OnTouchListener{

    private static final String TAG = ResourceSelectNewActivity.class.getSimpleName();
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private SegmentedControl segmentedControl;
    public List<Resource> resourceList;
    private TextView postButton;
    private List<ResourceModel> resourceModelList;
    private String groupId;
    ITerrainImageLabel imageLabel = null;
    private List<ImageLabels> imageLabelsList = new ArrayList<ImageLabels>();
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog1;

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

                    break;
                case SHOW_DATA:

                    break;
                case HIDEN_DIALOG:

                    break;

            }

        }
    };
    private String gridId;
    private String gridNo;
    private String gridName;
    private String leaderPhone;
    private String leaderName;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_select_new);

        resourceList = new ArrayList<>();
        resourceModelList = new ArrayList<>();
        initView();
        groupId = new DbConfig(this).getUser().getGroupId();
        progressDialog = new ProgressDialog(this);

        //从本地获取资源列表数据
        getResourceListToDb();
    }

    private void initView() {

        listView = (RecyclerView) findViewById(R.id.list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        adapter = new MultiTypeAdapter(items);
        ResourceChooseViewBinder resourceChooseViewBinder = new ResourceChooseViewBinder();
        resourceChooseViewBinder.setListener(this);
        adapter.register(Resource.class, resourceChooseViewBinder);
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        postButton = (TextView) findViewById(R.id.queding_button);
        postButton.setOnTouchListener(this);
       /* RxViewAction.clickNoDouble(postButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: xx" );


                    }
                });*/
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e(TAG, "call: anxia" );
            alertDialog1 = new AlertDialog.Builder(this)
                    .setTitle("这是标题")//标题
                    .setMessage("这是内容")//内容
                    .setIcon(R.drawable.ic_wanggehua)//图标
                    .create();

        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {// 松开处理
            //清楚已选中的资源点
            deleteResource();
            //更改本地数据库数据
            changeDbDate();
            //获取已选中的数据
            getChooseDate();
            //更改map信息
            changeMapData();

        }
        return false;
    }

    private void deleteResource() {
        Log.e(TAG, "call: 2" );
        try {
            final List<ImageLabels> imageLabels = SaveData.getData(getApplicationContext(), "resource");
            if (imageLabels != null) {
                for (int i = 0; i < imageLabels.size(); i++) {
                    List<ImageLabel> labels = imageLabels.get(i).labelList;
                    for (int j = 0; j < labels.size(); j++) {
                        try {
                            // Log.e(TAG, "didSelectRowAtIndexPath: ---1");
                            ISGWorld.getInstance().getCreator().DeleteObject(labels.get(j).objectId);
                        } catch (Exception ex) {
                            Log.e("", ex.getMessage());
                            continue;
                        }
                    }
                }
                List<ImageLabels> imageLabelsList = new ArrayList<>();
                SaveData.setData(getApplicationContext(), imageLabelsList, "resource");
            }


        } catch (Exception ex) {
        }
    }

    /**
     * 更改map信息
     */
    private void changeMapData() {
        Log.e(TAG, "call: 5" );
        for (int i = 0; i < resourceList.size(); i++) {
            if (resourceList.get(i).ischoose()) {
                final Resource resource = resourceList.get(i);

              /*  Message msg1 = mHandler.obtainMessage();
                Bundle b1 = new Bundle();
                b1.putInt("what", SHOW_DATA);
                b1.putSerializable("resource", resource);
                msg1.setData(b1);
                mHandler.sendMessage(msg1);
                Log.e(TAG, "call: 11" );*/
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
                            String groupId = ISGWorld.getInstance().getProjectTree().FindItem(resource.getName());

                            int imgId = R.drawable.star_yellow;
                            //  String fileName = "";
                            if (resource.getIconFile().equals("res_city_team.png")) { //专业队 res_city_team.png
                                imgId = R.drawable.ic_team;
                            } else if (resource.getIconFile().equals("res_check_station.png")) { //护林检查站
                                imgId = R.drawable.ic_check_station;
                            } else if (resource.getIconFile().equals("res_video_monitor.png")) {  //视频监控点	res_video_monitor.png
                                imgId = R.drawable.ic_monitor;
                            } else if (resource.getIconFile().equals("res_water_source.png")) { //水源地	res_water_source.png
                                imgId = R.drawable.ic_water_source;
                            } else if (resource.getIconFile().equals("res_observation_tower.png")) { //瞭望塔	res_observation_tower.png
                                imgId = R.drawable.ic_watch_tower;
                            } else if (resource.getIconFile().equals("res_key_target.png")) {  //重点防火目标	res_key_target.png
                                imgId = R.drawable.res_danger_source;
                            } else if (resource.getIconFile().equals("res_city_command.png")) { //指挥部	res_city_command.png
                                imgId = R.drawable.ic_res_city_command;
                            } else if (resource.getIconFile().equals("res_danger_source.png")) { //重大危险源	res_danger_source.png
                                imgId = R.drawable.ic_danger_source;
                            } else if (resource.getIconFile().equals("res_cemetery.png")) { //墓地	res_cemetery.png
                                imgId = R.drawable.ic_cemetery;
                            } else if (resource.getIconFile().equals("res_material.png")) { //物资库	res_material.png
                                imgId = R.drawable.ic_material_repository;
                            } else if (resource.getIconFile().equals("res_helicopter.png")) { //直升机机降点
                                imgId = R.drawable.ic_helicopter;
                            }

                            String fileName = TEImageHelper.prepareImageForTE(imgId, 35);

                            ImageLabels addLabels = new ImageLabels();
                            addLabels.groupId = groupId;
                            addLabels.id = resource.getId();
                            addLabels.imageId = imgId;
                            addLabels.isChecked = true;
                            addLabels.type = resource.getName();
                            addLabels.labelList = new ArrayList<ImageLabel>();

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
                                    addLabels.labelList.add(new ImageLabel(imageLabel.getID(), lng, lat, name));


                                } catch (Exception e) {
                                    continue;
                                }

                            }
                            if (imageLabelsList.indexOf(addLabels) < 0) {
                                imageLabelsList.add(addLabels);
                            }

                            SaveData.setData(getApplicationContext(), imageLabelsList, "resource");

                            Log.e(TAG, "call: 6" );
                            Log.e(TAG, "call: taiqi" );
                            alertDialog1.dismiss();

                        } catch (Exception ex) {

                            Log.e(ResourceSelectNewActivity.this.getClass().toString(), ex.getMessage());
                        }


                    }
                });
            }
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

    /**
     * 获取已选中的数据
     */
    private void getChooseDate() {
        Log.e(TAG, "call: 4" );
        resourceModelList.clear();

        for (int i = 0; i < resourceList.size(); i++) {
            if (resourceList.get(i).ischoose()) {
                //getResourcesData(resourceList.get(i).getCode());
            }
        }
    }

   /* private void getResourcesData(String str) {
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
            if (str.equals("team")) {                //专业队
                List<TeamDTO> teamDTOList = db.selector(TeamDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId + "%")
                        .findAll();
                if (teamDTOList != null) {
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
                        String picture = teamDTOList.get(i).getPicture();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone, resourceType, gridName, gridNo, gridId, districtName + streetname + gridName,picture);
                        resourceModelList.add(resourceModel);
                    }
                }

            } else if (str.equals("checkStation")) {      //检查站
                List<CheckStationDTO> dtoList = db.selector(CheckStationDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId + "%")
                        .findAll();
                if (dtoList != null) {
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
                        String picture = dtoList.get(i).getPicture();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone, resourceType, gridName, gridNo, gridId, districtName + streetname + gridName,picture);
                        resourceModelList.add(resourceModel);
                    }
                }
            } else if (str.equals("monitor")) {      //视频监控点
                List<MonitorDTO> dtoList = db.selector(MonitorDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId + "%")
                        .findAll();
                if (dtoList != null) {
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
                        String picture = "";
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone, resourceType, gridName, gridNo, gridId, districtName + streetname + gridName,picture);
                        resourceModelList.add(resourceModel);
                    }
                }
            } else if (str.equals("waterSource")) {      //水源地
                List<WaterSourceDTO> dtoList = db.selector(WaterSourceDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId + "%")
                        .findAll();
                if (dtoList != null) {
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
                        String picture = dtoList.get(i).getPicture();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone, resourceType, gridName, gridNo, gridId, districtName + streetname + gridName,picture);
                        resourceModelList.add(resourceModel);
                    }
                }
            } else if (str.equals("watchTower")) {      //瞭望塔
                List<WatchTowerDTO> dtoList = db.selector(WatchTowerDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId + "%")
                        .findAll();
                if (dtoList != null) {
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
                        String picture = dtoList.get(i).getPicture();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone, resourceType, gridName, gridNo, gridId, districtName + streetname + gridName,picture);
                        resourceModelList.add(resourceModel);
                    }
                }
            } else if (str.equals("fireCommand")) {      //指挥部
                List<FireCommandDTO> dtoList = db.selector(FireCommandDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId + "%")
                        .findAll();
                if (dtoList != null) {
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
                        String picture = dtoList.get(i).getPicture();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone, resourceType, gridName, gridNo, gridId, districtName + streetname + gridName,picture);
                        resourceModelList.add(resourceModel);
                    }
                }
            } else if (str.equals("dangerSource")) {      //重大危险源
                List<DangerSourceDTO> dtoList = db.selector(DangerSourceDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId + "%")
                        .findAll();
                if (dtoList != null) {
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
                        String picture = dtoList.get(i).getPicture();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone, resourceType, gridName, gridNo, gridId, districtName + streetname + gridName,picture);
                        resourceModelList.add(resourceModel);
                    }
                }
            } else if (str.equals("cemetery")) {      //墓地
                List<CemeteryDTO> dtoList = db.selector(CemeteryDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId + "%")
                        .findAll();
                if (dtoList != null) {
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
                        String picture = dtoList.get(i).getPicture();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone, resourceType, gridName, gridNo, gridId, districtName + streetname + gridName,picture);
                        resourceModelList.add(resourceModel);
                    }
                }
            } else if (str.equals("materialRepository")) {      //物资库
                List<MaterialRepositoryDTO> dtoList = db.selector(MaterialRepositoryDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId + "%")
                        .findAll();
                if (dtoList != null) {
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
                        String picture = dtoList.get(i).getPicture();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone, resourceType, gridName, gridNo, gridId, districtName + streetname + gridName,picture);
                        resourceModelList.add(resourceModel);
                    }
                }
            } else if (str.equals("helicopterPoint")) {      //直升机机降点
                List<HelicopterPointDTO> dtoList = db.selector(HelicopterPointDTO.class)
                        .where("state", "=", "ACTIVE")
                        .where("groupid", "like", groupId + "%")
                        .findAll();
                if (dtoList != null) {
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
                        String picture = dtoList.get(i).getPicture();
                        ResourceModel resourceModel = new ResourceModel(id, longitude, latitude, name, description, leaderName, leaderPhone, resourceType, gridName, gridNo, gridId, districtName + streetname + gridName,picture);
                        resourceModelList.add(resourceModel);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }*/

    /**
     * 更改本地数据库数据
     */
    private void changeDbDate() {
        Log.e(TAG, "call: 3" );
        DbConfig dbConfig = new DbConfig(getApplicationContext());
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(resourceList);
            Log.e(TAG, "call: 31" );
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库获取资源文件
     */
    private void getResourceListToDb() {
        resourceList = new DbConfig(this).getResourceList();

        initData();

    }

    private void initData() {
        items.clear();
        for (int i = 0; i < resourceList.size(); i++) {
            items.add(resourceList.get(i));
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResourceChooseItemClickListener(String id, boolean isChoose) {
        for (int i = 0; i < resourceList.size(); i++) {
            if (resourceList.get(i).getId().equals(id)) {
                resourceList.get(i).setIschoose(isChoose);
            }
        }
        initData();
    }



}
