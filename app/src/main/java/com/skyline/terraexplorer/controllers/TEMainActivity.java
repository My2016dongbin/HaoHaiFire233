package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StrictMode;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.bean.FireMessageBuf;
import com.bean.MessageBuf;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.google.protobuf.InvalidProtocolBufferException;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.skyline.core.CoreServices;
import com.skyline.teapi.AltitudeTypeCode;
import com.skyline.teapi.ApiException;
import com.skyline.teapi.IPopupMessage;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.teapi.ISGWorld.OnLoadFinishedListener;
import com.skyline.teapi.ISGWorld.OnSGWorldMessageListener;
import com.skyline.teapi.ITerrainImageLabel;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.db.CemeteryDTO;
import com.skyline.terraexplorer.db.CheckStationDTO;
import com.skyline.terraexplorer.db.DangerSourceDTO;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.db.FireCommandDTO;
import com.skyline.terraexplorer.db.HelicopterPointDTO;
import com.skyline.terraexplorer.db.MaterialRepositoryDTO;
import com.skyline.terraexplorer.db.MonitorDTO;
import com.skyline.terraexplorer.db.TeamDTO;
import com.skyline.terraexplorer.db.User;
import com.skyline.terraexplorer.db.WatchTowerDTO;
import com.skyline.terraexplorer.db.WaterSourceDTO;
import com.skyline.terraexplorer.models.AppLinks;
import com.skyline.terraexplorer.models.ControlDragGestures;
import com.skyline.terraexplorer.models.ControlDragGestures.ControlDragGesturesDelegate;
import com.skyline.terraexplorer.models.ControlDragGestures.DragDirection;
import com.skyline.terraexplorer.models.ExternalStorage;
import com.skyline.terraexplorer.models.FavoriteItem;
import com.skyline.terraexplorer.models.FavoritesStorage;
import com.skyline.terraexplorer.models.ImageLabel;
import com.skyline.terraexplorer.models.ImageLabels;
import com.skyline.terraexplorer.models.LocalBroadcastManager;
import com.skyline.terraexplorer.models.MainButtonDragGestures;
import com.skyline.terraexplorer.models.MenuEntry;
import com.skyline.terraexplorer.models.Plot;
import com.skyline.terraexplorer.models.SaveData;
import com.skyline.terraexplorer.models.TEImageHelper;
import com.skyline.terraexplorer.models.ToolManager;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.db.JobOrder;
import com.skyline.terraexplorer.multitype.JobOrderViewBinder;
import com.skyline.terraexplorer.receiver.NetworkConnectChangedReceiver;
import com.skyline.terraexplorer.service.TrackService;
import com.skyline.terraexplorer.tools.ProjectsTool;
import com.skyline.terraexplorer.tools.SettingsTool;
import com.skyline.terraexplorer.utils.ImagPagerUtil;
import com.skyline.terraexplorer.utils.LatLngChange;
import com.skyline.terraexplorer.utils.ModelUtils;
import com.skyline.terraexplorer.utils.RequestUtils;
import com.skyline.terraexplorer.utils.RxViewAction;
import com.skyline.terraexplorer.views.CommonProgressDialog;
import com.skyline.terraexplorer.views.GLSurfaceView;
import com.skyline.terraexplorer.views.MainMenuView;
import com.skyline.terraexplorer.views.MainMenuView.MainMenuViewDelegate;
import com.skyline.terraexplorer.views.MessageView;
import com.skyline.terraexplorer.views.ModalDialog;
import com.skyline.terraexplorer.views.ModalDialog.ModalDialogDelegate;
import com.skyline.terraexplorer.views.TEGLRenderer;
import com.skyline.terraexplorer.views.TEView;
import com.skyline.terraexplorer.views.ToolContainer;
import com.skyline.test.entity.PointLatLng;
import com.skyline.test.mytool.PlottingTool;
import com.skyline.test.socket.NettyService;
import com.skyline.test.utils.Constance;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;


public class TEMainActivity extends Activity implements INaviInfoCallback, JobOrderViewBinder.OnJobOrderItemClick, OnClickListener, MainMenuViewDelegate, ControlDragGesturesDelegate, OnSGWorldMessageListener, ModalDialogDelegate {

    private MainMenuView mainMenu;
    private ImageButton mainButton;
    private View loadingView;
    private MessageView messageView;
    private ControlDragGestures menuButtonDragGestures;
    private TEView teView;
    public static final String USER_INITIAL_LOCATION = "com.skyline.terraexplorer.initial.Location";
    public static final String USER_STATE_PROJECT = "com.skyline.terraexplorer.UserState.Project";
    public static final String USER_STATE_LOCATION = "com.skyline.terraexplorer.UserState.Location";
    private boolean engineInitialized = false;
    private static final String TAG = "TEMainActivity";
    private RelativeLayout mLocation;
    ITerrainImageLabel imageLabel = null;
    private IPosition lastPosition = null;

    private MyReceiver receiver = null;
    List<ImageLabels> imageLabels = new ArrayList<ImageLabels>();
    private List<PointLatLng> pointLatLngList;
    private List<Plot> plotList;
    private List<Plot> plotNewList;
    //内部存储上的文件路径
    private static String res_file_path = "/storage/sdcard1/Android/data/com.skyline.terraexplorer";

    private BroadcastReceiver engineInitializedReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OnEngineInitialized();

            Log.e(TAG, "onReceive: 1");
            LocalBroadcastManager.getInstance(TEMainActivity.this).unregisterReceiver(engineInitializedReciever);
            final String mPosition = getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE).getString(USER_INITIAL_LOCATION, null);
            //地图初始化位置
            mLocation.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e(TAG, "onReceive: 2");
                    if (mPosition != null) {
                        final IPosition nPosition = UI.runOnRenderThread(new Callable<IPosition>() {
                            @Override
                            public IPosition call() throws Exception {
                                Log.e(TAG, "onReceive: 3");
                                try {
                                    String[] positionParts = mPosition.split(",");
                                    return ISGWorld.getInstance().getCreator().CreatePosition(Double.parseDouble(positionParts[0]), Double.parseDouble(positionParts[1]));

                                } catch (Exception ex) {

                                }
                                return null;
                            }

                        });
                        if (nPosition != null) {
                            UI.runOnRenderThread(new Runnable() {
                                @Override
                                public void run() {

                                    Log.e(TAG, "onReceive: 4");
                                    ISGWorld.getInstance().getNavigate().FlyTo(nPosition);
                                }
                            });
                        } else {
                            UI.runOnRenderThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG, "onReceive: 5");
                                    ISGWorld.getInstance().getCommand().Execute(1055, 5);
                                }
                            });
                        }
                    } else {
                        UI.runOnRenderThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "onReceive: 6");
                                ISGWorld.getInstance().getCommand().Execute(1055, 5);
                            }
                        });
                    }
                }
            });

        }
    };
    private Dialog ziyuanDialog;
    private View ziyuanInflater;
    private TextView textView;
    private Dialog searchDialog;
    private View searchInflater;
    private LinearLayout oneHoursLayout;
    private LinearLayout currntTimeLayout;
    private LinearLayout threeHoursLayout;
    private LinearLayout oneDayLayout;
    private LinearLayout threeDayLayout;
    private LinearLayout fiveDayLayout;
    private LinearLayout gaojiSearchLayout;
    private String versionService;

    private TextView nameView;
    private TextView jingduView;
    private TextView weiduView;
    private TextView goButton;
    private ZiyuanItemChangeReceiver ziyuanItemChangeReceiver;
    private TextView yinhuanView;
    private TextView miaoshuView;
    private TextView zerenrenView;
    private TextView zerenrenPhoneView;
    private LinearLayout daohangButton;
    private TextView dituView;
    private String currentLong = "120.378755";
    private String currentLat = "36.064017";
    private TextView jiantouView;
    private OutLoginReceiver outLoginReceiver;
    private int loginState;   //1是重新登陆的  初始话选中资源点
    private LinearLayout phoneView;
    private String versionCode;
    private CommonProgressDialog mBar;
    private static final String DOWNLOAD_NAME = "wanggehua_";
    public boolean isGengxin = false;
    private Uri tempUri;
    private Timer timer;
    private TextView openWeixingApp;
    private TextView wanggeView;
    private PlottingTool plottingTool;
    private LinearLayout tuliButton;
    private LinearLayout tuliLayout;
    private boolean isShowTuli;
    private ImageView tuliImage;
    private LinearLayout tuliListLayout;
    private Dialog jobOrderListDialog;
    private View jobOrderListInflater;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private int width;
    private List<JobOrder> jobOrderList;
    private TextView jobOrderButton;
    private ProgressDialog progressDialog;
    private JobOrderReceiver jobOrderReceiver;
    private LinearLayout jingduLayout;
    private TextView surroundResourceView;
    private TextView resourceFindView;
    private TextView currentPositionView;
    private TextView resourceCheckView;
    private boolean isPad;
    private LinearLayout resourceLayout;
    private TextView addResourceLayout;
    private TextView resourceDeleteView;
    private TextView editResourceView;
    private User user;
    private TextView resourceEditView;
    private LinearLayout resourceCaozuoLayout;
    private LinearLayout jianchazhanLayout;
    private TextView leixingView;
    private TextView zhibanView;
    private TextView renyuanView;
    private TextView shijianView;
    private LinearLayout shuiyuandiLayout;
    private TextView xushuiliangView;
    private ImageView photoView;
    private FrameLayout resourceDialogLayout;
    private TextView renyuanxingmingView;
    private TextView fengliMeihuojiShuliangView;
    private TextView meihuoshuiqiangView;
    private TextView erhaogongjuView;
    private TextView qitaGongjuView;
    private TextView jiankongView;
    private TextView shanxiView;
    private TextView zhishengjiQushuiView;
    private TextView shuiyuandiLeixingView;
    private TextView duiwurenshuView;
    private TextView zhibandianhuaView;
    private TextView xiaofangcheTeamView;
    private TextView yunbingcheTeamView;
    private TextView zhiuhuiTeamView;
    private TextView gaoyaTeamView;
    private TextView fenglimeihuoTeamView;
    private TextView erhaogongjuTeamView;
    private TextView meihuoshuiqiangTeamView;
    private TextView duijiangjiTeamView;
    private TextView zhuangbeiyunshuTeamView;
    private TextView yingfangTeamView;
    private TextView shuiguancheTeamView;
    private LinearLayout zhuanyeduiLayout;
    private LinearLayout wuzikuLayout;
    private TextView fenglimeihuoMrView;
    private TextView gaoyaxishuiMrView;
    private TextView gaoyashuibengMrView;
    private TextView erhaogongjuMrView;
    private TextView meihuoshuiqiangMrView;
    private TextView youjuMrView;
    private TextView geguanjiMrView;
    private TextView huochangqiegejiMrView;
    private TextView fanghuofuMrView;
    private TextView fanghuoshoutaoMrView;
    private TextView fanghuotoukuiMrView;
    private TextView fanghuoxieMrView;
    private TextView shuidaiMrView;
    private TextView shuinangMrView;
    private TextView youtongMrView;
    private TextView wuzikuLeixingMrView;
    private TextView zhuanyeduiLeixingTeamView;
    private LinearLayout jiancezhongxinLayout;
    private TextView jiancezhongxinTypeView;
    private LinearLayout liaowangtaLayout;
    private TextView jiancefanweiView;
    private LinearLayout shipinjiankongLayout;
    private TextView jiancefangweiMView;
    private TextView lianwangMView;
    private TextView zhinengkakouMView;
    private TextView jiankongleixingView;
    private LinearLayout mudiLayout;
    private TextView mudileixingView;
    private TextView fengtouView;
    private LinearLayout weixianyuanLayout;
    private TextView zhongdaweixianyuanView;
    private ScrollView resourceScrollView;
    private TextView checkStateView;
    public boolean isShowAll = true;      //是否全部显示
    private TextView resourceJiaoyanView;
    private TextView resourceErrorView;

    /**
     * 检查计划的点击回调
     *
     * @param id
     */
    @Override
    public void onJonOrderItemClickLisiener(String id) {
        Intent intent = new Intent(getApplicationContext(), CheckPlanActivity.class);
        intent.putExtra("plan_id", id);
        startActivity(intent);
    }


    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: 7");
            Bundle bundle = intent.getExtras();
            MessageBuf.JMTransfer mm = (MessageBuf.JMTransfer) bundle.get("count");
            Log.d(TAG, "onReceive: " + mm.getCmd());
            if (mm.getCmd() == 30000) {
                try {
                    FireMessageBuf.FireMessage.FireMessageContent fireContent = FireMessageBuf.FireMessage.FireMessageContent.parseFrom(mm.getBody());
                    addFire(fireContent);
                } catch (InvalidProtocolBufferException e) {
                    com.skyline.test.others.LogUtils.e(TAG, e.toString());
                    e.printStackTrace();
                }
            }

        }
    }

    private static boolean isExit = false;
    private String currentName;
    private String currentId;
    private AlertDialog.Builder builder;
    private String currentType;

    private String currentCode;
    private IPosition currentPosition;

    private static final int SHOWDIALOG = 1;
    private static final int EXIT = 10;
    private static final int RESOURCE_DELETE = 13;
    private static final int FINASH = 12;
    private String currentApiUrl;

    private Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXIT:
                    Log.e(TAG, "handleMessage: 5");
                    isExit = false;
                    break;
                case TIME_CHANGE:

                    //getSyncPlotListData();        //开启同步标会方法
                    String location = getLocation();
                    if (!location.isEmpty()) {
                        List<String> jingweiList = Arrays.asList(location.split(","));

                        currentLong = jingweiList.get(0);
                        currentLat = jingweiList.get(1);
                    }
                    currentPositionView.setText("当前位置:" + currentLong + "," + currentLat);
                    break;
                case FINASH:
                    finish();
                    break;
                case RESOURCE_DELETE:
                    ISGWorld.getInstance().getCreator().DeleteObject(currentResource.getObjectId());
                    /*DbConfig dbConfig = new DbConfig(getApplicationContext());
                    DbManager db = dbConfig.getDbManager();
                    try {
                        db.saveOrUpdate(user);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }*/

                    break;

            }

        }
    };

    class ResourceDeleteThread extends Thread {

        @Override
        public void run() {
            super.run();
            deleteResource();

        }
    }
    class ResourceJiaoyanThread extends Thread {

        @Override
        public void run() {
            super.run();

            for (int i = 0; i < imageLabels.size(); i++) {
                List<ImageLabel> labelList = imageLabels.get(i).labelList;
                for (int j = 0; j < labelList.size(); j++) {
                    if (labelList.get(j).getUuId().equals(currentId)) {
                        imageLabels.get(i).labelList.get(j).setCheckState("1");
                    }
                }
            }
        }
    }


    private String zerenrenPhone;
    private static final int TIME_CHANGE = 11;
    private String currentGridNo;
    private String currentGridName;
    private String currentGridId;
    private ImageLabel currentResource;
    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "onReceive: 8");
            Bundle data = msg.getData();
            int what = data.getInt("what");
            switch (what) {
                case SHOWDIALOG:
                    currentResource = ((ImageLabel) data.getSerializable("imageLabel"));
                    Log.e(TAG, "handleMessage: ----");
                    currentName = data.getString("name");
                    currentId = data.getString("uuId");
                    String jingdu = data.getString("jingdu");
                    String weidu = data.getString("weidu");
                    String miaoshu = data.getString("miaoshu");
                    String zerenren = data.getString("zerenren");
                    String wangge = data.getString("wangge");
                    zerenrenPhone = data.getString("zerenrenPhone");
                    currentCode = data.getString("code");
                    currentApiUrl = data.getString("apiUrl");
                    currentGridId = data.getString("gridId");
                    currentGridName = data.getString("gridName");
                    currentGridNo = data.getString("gridNo");
                    Glide.with(getApplicationContext()).load(RequestUtils.IAMGE_URL + currentResource.getPicture()).error(R.drawable.ic_no_pic)
                            .placeholder(R.drawable.ic_jaizai).into(photoView);

                    Log.e(TAG, "handleMessage: " + currentResource.getUpdateTime());
                    shijianView.setText(currentResource.getUpdateTime().substring(0, currentResource.getUpdateTime().indexOf(".")).replace("T", " "));


                    Log.e(TAG, "handleMessage: " + currentCode);
                    zhuanyeduiLayout.setVisibility(View.GONE);
                    jianchazhanLayout.setVisibility(View.GONE);
                    shuiyuandiLayout.setVisibility(View.GONE);
                    wuzikuLayout.setVisibility(View.GONE);
                    jiancezhongxinLayout.setVisibility(View.GONE);
                    liaowangtaLayout.setVisibility(View.GONE);
                    shipinjiankongLayout.setVisibility(View.GONE);
                    mudiLayout.setVisibility(View.GONE);
                    weixianyuanLayout.setVisibility(View.GONE);

                    if (currentCode.equals("checkStation")) {       //检查站
                        jianchazhanLayout.setVisibility(View.VISIBLE);
                        // type 1检查站，2护林房，3管护站
                        if (currentResource.getResourceType().equals("1")) {
                            leixingView.setText("检查站");
                        } else if (currentResource.getResourceType().equals("2")) {
                            leixingView.setText("护林房");
                        } else if (currentResource.getResourceType().equals("3")) {
                            leixingView.setText("管护站");
                        }
                        renyuanView.setText(currentResource.getPeopleCount().equals("null")?" ":currentResource.getPeopleCount());
                        renyuanxingmingView.setText(currentResource.getPeopleName().equals("null")?" ":currentResource.getPeopleName());
                        shanxiView.setText(currentResource.getMountain().equals("null")?" ":currentResource.getMountain());
                        meihuoshuiqiangView.setText(currentResource.getWaterPistolCount().equals("null")?" ":currentResource.getWaterPistolCount());
                        erhaogongjuView.setText(currentResource.getTwoToolCount().equals("null")?" ":currentResource.getTwoToolCount());
                        qitaGongjuView.setText(currentResource.getOtherToolCount().equals("null")?" ":currentResource.getOtherToolCount());
                        jiankongView.setText(currentResource.getHasMonitor().equals("0")?"否":"是");
                        fengliMeihuojiShuliangView.setText(currentResource.getExtinguisherCount().equals("null")?" ":currentResource.getExtinguisherCount());

                        try {
                            if (currentResource.getIsAllday().equals("0")) {
                                zhibanView.setText("否");
                            } else {
                                zhibanView.setText("是");

                            }
                        } catch (Exception e) {

                        }

                    } else if (currentCode.equals("waterSource")) {     //水源地
                        shuiyuandiLayout.setVisibility(View.VISIBLE);
                        xushuiliangView.setText(currentResource.getWaterCapacity().equals("null") ? "0" : currentResource.getWaterCapacity());
                        Log.e(TAG, "handleMessage: " +currentResource.getIsHelicopterWater());
                        zhishengjiQushuiView.setText(currentResource.getIsHelicopterWater().equals("")? "否" : currentResource.getIsHelicopterWater().equals("0")?"否":"是");
                        if(currentResource.getResourceType()!=null){
                            if (currentResource.getResourceType().equals("1")){ //1水囊2水罐 3蓄水池 4塘坝   新加 5水库  6水箱
                                shuiyuandiLeixingView.setText("水囊");
                            }else if (currentResource.getResourceType().equals("2")){
                                shuiyuandiLeixingView.setText("水罐");
                            }else if (currentResource.getResourceType().equals("3")){
                                shuiyuandiLeixingView.setText("蓄水池");
                            }else if (currentResource.getResourceType().equals("4")){
                                shuiyuandiLeixingView.setText("塘坝");
                            }else if (currentResource.getResourceType().equals("5")){
                                shuiyuandiLeixingView.setText("水库");
                            }else if (currentResource.getResourceType().equals("6")){
                                shuiyuandiLeixingView.setText("水箱");
                            }else {
                                shuiyuandiLeixingView.setText("其他");
                            }
                        }else {
                            shuiyuandiLeixingView.setText("其他");
                        }

                    } else if (currentCode.equals("team")) {     //专业队
                        zhuanyeduiLayout.setVisibility(View.VISIBLE);
                        duiwurenshuView.setText(currentResource.getTeamCount().equals("null")?" ":currentResource.getTeamCount());
                        zhibandianhuaView.setText(currentResource.getPhone().equals("null")?" ":currentResource.getPhone());
                        xiaofangcheTeamView.setText(currentResource.getTruckCountTeam().equals("null")?" ":currentResource.getTruckCountTeam());
                        yunbingcheTeamView.setText(currentResource.getTroopCarrierCount().equals("null")?" ":currentResource.getTroopCarrierCount());
                        zhiuhuiTeamView.setText(currentResource.getCommandCarCount().equals("null")?" ":currentResource.getCommandCarCount());
                        gaoyaTeamView.setText(currentResource.getWaterPumpCount().equals("null")?" ":currentResource.getWaterPumpCount());
                        fenglimeihuoTeamView.setText(currentResource.getWindFireCount().equals("null")?" ":currentResource.getWindFireCount());
                        erhaogongjuTeamView.setText(currentResource.getTwoToolCountTeam().equals("null")?" ":currentResource.getTwoToolCountTeam());
                        meihuoshuiqiangTeamView.setText(currentResource.getWaterPistolCountTeam().equals("null")?" ":currentResource.getWaterPistolCountTeam());
                        duijiangjiTeamView.setText(currentResource.getIntercomCount().equals("null")?" ":currentResource.getIntercomCount());
                        zhuangbeiyunshuTeamView.setText(currentResource.getEquipmentTruckCount().equals("null")?" ":currentResource.getEquipmentTruckCount());
                        yingfangTeamView.setText(currentResource.getBarracksMeasure().equals("null")?" ":currentResource.getBarracksMeasure());
                        shuiguancheTeamView.setText(currentResource.getWaterCarCount().equals("null")?" ":currentResource.getWaterCarCount());
                        if (currentResource.getResourceType() == null){
                            zhuanyeduiLeixingTeamView.setText("区市级专业队");
                        }else {
                            if (currentResource.getResourceType().equals("1")){
                                zhuanyeduiLeixingTeamView.setText("镇街级专业队");
                            }else {
                                zhuanyeduiLeixingTeamView.setText("区市级专业队");
                            }
                        }

                    }else if (currentCode.equals("materialRepository")) {     //物资库
                        wuzikuLayout.setVisibility(View.VISIBLE);
                        fenglimeihuoMrView.setText(currentResource.getWindFireCountMR().equals("null")?" ":currentResource.getWindFireCountMR());
                        gaoyaxishuiMrView.setText(currentResource.getSprayFireCountMR().equals("null")?" ":currentResource.getSprayFireCountMR());
                        gaoyashuibengMrView.setText(currentResource.getWaterPumpCountMR().equals("null")?" ":currentResource.getWaterPumpCountMR());
                        erhaogongjuMrView.setText(currentResource.getTwoToolCountMR().equals("null")?" ":currentResource.getTwoToolCountMR());
                        meihuoshuiqiangMrView.setText(currentResource.getWaterPistolCountMR().equals("null")?" ":currentResource.getWaterPistolCountMR());
                        youjuMrView.setText(currentResource.getChainSawCountMR().equals("null")?" ":currentResource.getChainSawCountMR());
                        geguanjiMrView.setText(currentResource.getBushCutterCountMR().equals("null")?" ":currentResource.getBushCutterCountMR());
                        huochangqiegejiMrView.setText(currentResource.getFireCutterCountMR().equals("null")?" ":currentResource.getFireCutterCountMR());
                        fanghuofuMrView.setText(currentResource.getFireproofClothesCountMR().equals("null")?" ":currentResource.getFireCutterCountMR());
                        fanghuoshoutaoMrView.setText(currentResource.getGlovesCountMR().equals("null")?" ":currentResource.getGlovesCountMR());
                        fanghuotoukuiMrView.setText(currentResource.getHelmetCountMR().equals("null")?" ":currentResource.getHelmetCountMR());
                        fanghuoxieMrView.setText(currentResource.getShoesCountMR().equals("null")?" ":currentResource.getShoesCountMR());
                        shuidaiMrView.setText(currentResource.getWaterBagCountMR().equals("null")?" ":currentResource.getWaterBagCountMR());
                        shuinangMrView.setText(currentResource.getWaterSacCountMR().equals("null")?" ":currentResource.getWaterSacCountMR());
                        youtongMrView.setText(currentResource.getOilDrumCountMR().equals("null")?" ":currentResource.getOilDrumCountMR());
                        if (currentResource.getResourceType() == null){
                            wuzikuLeixingMrView.setText("市级物资库");
                        }else {
                            if (currentResource.getResourceType().equals("2")){
                                wuzikuLeixingMrView.setText("镇街级物资库");
                            }else if (currentResource.getResourceType().equals("1")){
                                wuzikuLeixingMrView.setText("区市级物资库");
                            }else {
                                wuzikuLeixingMrView.setText("市级物资库");
                            }
                        }
                    }else if (currentCode.equals("fireCommand")) {     //监测中心  指挥部
                        jiancezhongxinLayout.setVisibility(View.VISIBLE);
                        if (currentResource.getResourceType() == null){
                            jiancezhongxinTypeView.setText("市级监测中心");
                        }else {
                            if (currentResource.getResourceType().equals("2")){
                                jiancezhongxinTypeView.setText("镇街级监测中心");
                            }else if (currentResource.getResourceType().equals("1")){
                                jiancezhongxinTypeView.setText("区市级监测中心");
                            }else {
                                jiancezhongxinTypeView.setText("市级监测中心");
                            }
                        }
                    }else if (currentCode.equals("watchTower")) {     //瞭望塔
                        liaowangtaLayout.setVisibility(View.VISIBLE);
                        jiancefanweiView.setText(currentResource.getWatchRange().equals("null")?" ":currentResource.getWatchRange());

                    } else if (currentCode.equals("monitor")) {     //视频监控点
                        shipinjiankongLayout.setVisibility(View.VISIBLE);
                        jiancefangweiMView.setText(currentResource.getMonitorRange().equals("null")?" ":currentResource.getMonitorRange());
                        if (currentResource.getIsNetworking() == null) {
                            lianwangMView.setText("否");
                        }else {
                            lianwangMView.setText(currentResource.getIsNetworking().equals("1")?"是":"否");
                        }
                        if (currentResource.getIsIntelligentEntry() == null) {
                            zhinengkakouMView.setText("否");
                        }else {
                            zhinengkakouMView.setText(currentResource.getIsIntelligentEntry().equals("1")?"是":"否");
                        }

                        if (currentResource.getResourceType() == null){
                            jiankongleixingView.setText("高山远程监控");
                        }else {
                            if (currentResource.getResourceType().equals("2")){
                                jiankongleixingView.setText("墓地监控");
                            }else if (currentResource.getResourceType().equals("1")){
                                jiankongleixingView.setText("进山出入口监控");
                            }else {
                                jiankongleixingView.setText("高山远程监控");
                            }
                        }

                    } else if (currentCode.equals("cemetery")) {     //墓地
                        mudiLayout.setVisibility(View.VISIBLE);
                        fengtouView.setText(currentResource.getGraveCount().equals("")?" ":currentResource.getGraveCount());
                        if (currentResource.getResourceType()==null) {
                            mudileixingView.setText("公墓");
                        }else {
                            mudileixingView.setText(currentResource.getResourceType().equals("1")?"集中墓地":"公墓");
                        }

                    } else if (currentCode.equals("dangerSource")) {     //危险源
                        weixianyuanLayout.setVisibility(View.VISIBLE);
                        if (currentResource.getIsMajorHazard() == null) {
                            zhongdaweixianyuanView.setText("否");
                        }else {
                            zhongdaweixianyuanView.setText(currentResource.getIsMajorHazard().equals("1")?"是":"否");
                        }

                    } else {
                        zhuanyeduiLayout.setVisibility(View.GONE);
                        jianchazhanLayout.setVisibility(View.GONE);
                        shuiyuandiLayout.setVisibility(View.GONE);
                        wuzikuLayout.setVisibility(View.GONE);
                        jiancezhongxinLayout.setVisibility(View.GONE);
                        liaowangtaLayout.setVisibility(View.GONE);
                        shipinjiankongLayout.setVisibility(View.GONE);
                        mudiLayout.setVisibility(View.GONE);
                        weixianyuanLayout.setVisibility(View.GONE);
                    }
                    if (currentResource.getCheckState()!=null){
                        if (currentResource.getCheckState().equals("1")){
                            nameView.setTextColor(getResources().getColor(R.color.c22));
                            nameView.setText(currentName+"(已校验)");
                        }else {
                            nameView.setTextColor(getResources().getColor(R.color.c19));
                            nameView.setText(currentName+"(未校验)");
                        }
                    }else {
                        nameView.setTextColor(getResources().getColor(R.color.c19));
                        nameView.setText(currentName+"(未校验)");
                    }


                    jingduView.setText(jingdu.equals("null") ? " " : jingdu);
                    weiduView.setText(weidu.equals("null") ? " " : weidu);
                    miaoshuView.setText(miaoshu.equals("null") ? " " : miaoshu);
                    zerenrenView.setText(zerenren.equals("null") ? " " : zerenren);
                    wanggeView.setText(wangge.equals("null") ? " " : wangge);

                    if (zerenrenPhone.equals("null") || zerenrenPhone.isEmpty()) {
                        phoneView.setVisibility(View.GONE);
                    } else {
                        phoneView.setVisibility(View.VISIBLE);
                    }
                    zerenrenPhoneView.setText(zerenrenPhone.equals("null") ? " " : zerenrenPhone);


                    if (currentCode.equals("materialRepository") || currentCode.equals("checkStation") || currentCode.equals("team") || currentCode.equals("fireCommand")) {
                        goButton.setVisibility(View.VISIBLE);
                        goButton.setText("开始检查");
                    } else if (currentCode.equals("monitor")) {
                        goButton.setVisibility(View.VISIBLE);
                        goButton.setText("查看监控");
                    } else {
                        goButton.setVisibility(View.GONE);
                    }

                    resourceScrollView.scrollTo(0,0);
                    searchDialog.show();
                    break;

            }

        }
    };


    /**
     * 添加卫星火点信息
     *
     * @param fireMessageContent
     */
    private void addFire(final FireMessageBuf.FireMessage.FireMessageContent fireMessageContent) {
        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.icon = R.drawable.fire;
        String desc = "火灾时间：" + fireMessageContent.getAlarmTime() + "\n火情状况：" + fireMessageContent.getAlarmContent();
        favoriteItem.desc = desc;
        favoriteItem.name = fireMessageContent.getAlarmTime();
        IPosition firPosition = UI.runOnRenderThread(new Callable<IPosition>() {
            @Override
            public IPosition call() throws Exception {
                Log.e(TAG, "onReceive: 9");
                LogUtils.dTag(TAG, fireMessageContent.getLon() + "--" + fireMessageContent.getLat());
                return ISGWorld.getInstance().getCreator().CreatePosition(fireMessageContent.getLon(), fireMessageContent.getLat());

            }
        });
        favoriteItem.position = firPosition;
        favoriteItem.showOn3D = true;
        FavoritesStorage.defaultStorage.saveItem(favoriteItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupStrictMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temain);
        TEApp.setMainActivityContext(this); //设置this为TEApp中的mainActivityContext
        getWindow().setBackgroundDrawable(null);
        //AppLinks.initializeAsync(); //设置三个默认地址
        CoreServices.Init(this);


        //开启轨迹上传服务
        startService(new Intent(this, TrackService.class));
        //
        //plottingTool = new PlottingTool();
        pointLatLngList = new ArrayList<PointLatLng>();
        plotList = new ArrayList<>();
        plotNewList = new ArrayList<>();
        jobOrderList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        //版本更新
        getVersion();

        Intent intent = getIntent();
        loginState = intent.getIntExtra("STATE", 0);
        //初始化选中资源点
        if (loginState == 1) {

        }

        String location = getLocation();
        if (!location.isEmpty()) {
            List<String> jingweiList = Arrays.asList(location.split(","));

            currentLong = jingweiList.get(0);
            currentLat = jingweiList.get(1);
            Log.e(TAG, "onCreate: jingdu-" + currentLong);
            Log.e(TAG, "onCreate: weidu-" + currentLat);
        }
        isPad = new ModelUtils().isPad(this);
        user = new DbConfig(this).getUser();
        Log.e(TAG, "user: isAdd=" + user.isAdd);
        Log.e(TAG, "user: isDelete= " + user.isDelete);
        Log.e(TAG, "user: isEdit=" + user.isEdit);
        initView();

        //配置点击查看大图
        initImageLoader();


        //权限获取
        // requestPower();

        //在这里添加回到特定位置按钮的事件,平时是隐藏的，只有移动的时候才会显示出来
        mLocation = (RelativeLayout) findViewById(R.id.temain_location);
        mLocation.setVisibility(View.INVISIBLE);
        mainMenu = (MainMenuView) findViewById(R.id.main_menu);
        mainMenu.setDelegate(this);
        mainButton = (ImageButton) findViewById(R.id.mainButton);
        mainMenu.setAnchor(mainButton);
        mainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onReceive: 10");
                /* for some reason main button is clickable through loading screen, which is bad.
                 so prevent main menu while loading fly
                 因为某些原因主要通过加载屏幕按钮可点击,这是坏的方面。所以防止主菜单加载时飞 */
                if (TEMainActivity.this.loadingView.getVisibility() == View.VISIBLE)
                    return;
                toggleMainMenu();
            }
        });

        ToolContainer.INSTANCE.attachRootViewTo(this.mainButton);

        messageView = (MessageView) findViewById(R.id.main_message_view);

        loadingView = findViewById(R.id.loadingView);

        menuButtonDragGestures = new ControlDragGestures(mainButton, this);

        teView = (TEView) findViewById(R.id.main_teview);
        teView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                messageView.hide();
                mainMenu.hide();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mLocation.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP: //手指抬起时位置消失
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                mLocation.setVisibility(View.INVISIBLE);
                            }
                        }, 3000);
                }

                return false;
            }
        });

        // show loading screen while TE initializes 显示加载屏幕，而TE初始化
        loadingView.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.loadingView_loading)).setText(R.string.initializing);
        ((TextView) findViewById(R.id.loadingView_projectName)).setText("");

        LocalBroadcastManager.getInstance(this).registerReceiver(engineInitializedReciever, new IntentFilter(TEGLRenderer.ENGINE_INITIALIZED));

        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.osanwen.nettydemo.NettyService");
        TEMainActivity.this.registerReceiver(receiver, filter);

        //加载缓存的地图点位图标

        //


        /**
         * 注册网络监听器
         */
        IntentFilter filter1 = new IntentFilter();//代码中注册
        filter1.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter1.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter1.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //注册监听 离线数据上传
        registerReceiver(new NetworkConnectChangedReceiver(), filter1);


        //计划单极光推送的广播
        IntentFilter joborderClick = new IntentFilter();
        joborderClick.addAction("job_order");
        jobOrderReceiver = new JobOrderReceiver();
        registerReceiver(jobOrderReceiver, joborderClick);

        //实例化IntentFilter对象
        IntentFilter filterFireClick = new IntentFilter();
        filterFireClick.addAction("item_click");
        ziyuanItemChangeReceiver = new ZiyuanItemChangeReceiver();
        //注册广播接收
        registerReceiver(ziyuanItemChangeReceiver, filterFireClick);

        //实例化IntentFilter对象
        IntentFilter filterOutLogin = new IntentFilter();
        filterOutLogin.addAction("out_login");
        outLoginReceiver = new OutLoginReceiver();
        //注册广播接收
        registerReceiver(outLoginReceiver, filterOutLogin);

        //teView.onStart();
        //ToolManager.INSTANCE.openTool(ProfileTool.class.getName());

    }

    /**
     * 获取检查计划
     */
    private void getCheckPlanFromService() {
        try {
            jobOrderList = new DbConfig(this).getDbManager().selector(JobOrder.class)
                    .findAll();
            initJobOrderData();
        } catch (DbException e) {
            e.printStackTrace();
        }


     /*   showDialogProgress(progressDialog,"加载中...");
        JSONObject jsonObject = new JSONObject();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/checkPlan/getUseCheckPlanList");
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "checkPlan: " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:-------checkPlan------ " + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject.getJSONArray("data");
                        jobOrderList.clear();
                        Gson gson = new Gson();
                        jobOrderList = gson.fromJson(String.valueOf(data), new TypeToken<List<JobOrder>>(){}.getType());

                        initJobOrderData();
                    }else {
                        Toast.makeText(TEMainActivity.this, "暂无检查计划！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(TEMainActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        });*/
    }

    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }

    private void initJobOrderData() {
        items.clear();
        if (jobOrderList != null) {
            for (int i = 0; i < jobOrderList.size(); i++) {

                items.add(jobOrderList.get(i));
            }
            jobOrderListDialog.show();
            //  progressDialog.dismiss();
            assertAllRegistered(adapter, items);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "暂无检查计划", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {


        checkStateView = (TextView) findViewById(R.id.check_state_view);
        resourceLayout = (LinearLayout) findViewById(R.id.resouce_layout);
        addResourceLayout = (TextView) findViewById(R.id.add_resource_view);
        currentPositionView = (TextView) findViewById(R.id.position_text);
        currentPositionView.setText("当前位置:" + currentLong + "," + currentLat);
        tuliButton = (LinearLayout) findViewById(R.id.tuli_button);
        tuliLayout = (LinearLayout) findViewById(R.id.tuli_layout);
        tuliImage = (ImageView) findViewById(R.id.tuli_image);
        tuliListLayout = (LinearLayout) findViewById(R.id.tuli_list_layout);
        isShowTuli = false;
        if (user.isAdd) {
            addResourceLayout.setVisibility(View.VISIBLE);
        } else {
            addResourceLayout.setVisibility(View.GONE);
        }
        //资源点隐藏显示控制 校验
        RxViewAction.clickNoDouble(checkStateView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        initResourceShow();

                    }
                });
        RxViewAction.clickNoDouble(addResourceLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), ResourceAddActivity.class);
                        intent.putExtra("LNG", currentLong);
                        intent.putExtra("LAT", currentLat);
                        startActivity(intent);
                    }
                });
        RxViewAction.clickNoDouble(tuliButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isShowTuli) {        //显示
                            isShowTuli = false;
                            ObjectAnimator animator = ObjectAnimator.ofFloat(tuliLayout, View.TRANSLATION_X, 0);
                            animator.setDuration(500);
                            animator.start();

                            ObjectAnimator animator1 = ObjectAnimator.ofFloat(tuliImage, View.ROTATION_Y, 360);
                            animator1.setDuration(500);
                            animator1.start();
                        } else {         //隐藏
                            isShowTuli = true;
                            ObjectAnimator animator = ObjectAnimator.ofFloat(tuliLayout, View.TRANSLATION_X, -tuliListLayout.getWidth());
                            animator.setDuration(500);
                            animator.start();

                            ObjectAnimator animator1 = ObjectAnimator.ofFloat(tuliImage, View.ROTATION_Y, 180);
                            animator1.setDuration(500);
                            animator1.start();
                        }
                    }
                });
        openWeixingApp = (TextView) findViewById(R.id.open_weixing_app);
        RxViewAction.clickNoDouble(openWeixingApp)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        /**知道要跳转应用的包命与目标Activity*/
                        ComponentName componentName = new ComponentName("com.hht.hsatellitemobile", "com.hht.hsatellitemobile.MainActivity");
                        intent.setComponent(componentName);
                        // intent.putExtra("", "");//这里Intent传值
                        startActivity(intent);
                    }
                });
        jiantouView = (TextView) findViewById(R.id.jiantou);
        /**
         * 同步标绘
         */
        RxViewAction.clickNoDouble(jiantouView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        PlottingTool plottingTool = new PlottingTool();
                        List<PointLatLng> pointLatLngList = new ArrayList<>();
                        pointLatLngList.add(new PointLatLng(36.221381, 120.255053));
                        pointLatLngList.add(new PointLatLng(36.231513, 120.262096));
                        plottingTool.setPoints(pointLatLngList);
                    }
                });

        searchDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        searchInflater = LayoutInflater.from(this).inflate(R.layout.dialog_search, null);
        searchInflater.setMinimumWidth(100000);
        resourceScrollView = ((ScrollView) searchInflater.findViewById(R.id.resource_scroll_view));
        nameView = ((TextView) searchInflater.findViewById(R.id.name_view));
        jingduView = ((TextView) searchInflater.findViewById(R.id.jingdu_view));
        weiduView = ((TextView) searchInflater.findViewById(R.id.weidu_view));
        wanggeView = ((TextView) searchInflater.findViewById(R.id.wangge_view));
        goButton = ((TextView) searchInflater.findViewById(R.id.go_button));
        yinhuanView = ((TextView) searchInflater.findViewById(R.id.yinhuan_view));
        miaoshuView = ((TextView) searchInflater.findViewById(R.id.miaoshu__view));
        zerenrenView = ((TextView) searchInflater.findViewById(R.id.zerenren_view));
        daohangButton = ((LinearLayout) searchInflater.findViewById(R.id.daohang_layout));
        phoneView = ((LinearLayout) searchInflater.findViewById(R.id.phone_layout));
        zerenrenPhoneView = ((TextView) searchInflater.findViewById(R.id.zerenren_phone_view));
        jingduLayout = ((LinearLayout) searchInflater.findViewById(R.id.jingdu_layout));
        surroundResourceView = ((TextView) searchInflater.findViewById(R.id.surround_resource_view));
        resourceDeleteView = ((TextView) searchInflater.findViewById(R.id.resource_delete_view));
        resourceJiaoyanView = ((TextView) searchInflater.findViewById(R.id.resource_jiaoyan_view));
        resourceEditView = ((TextView) searchInflater.findViewById(R.id.resource_edit_view));
        resourceCaozuoLayout = ((LinearLayout) searchInflater.findViewById(R.id.caozuo_layout));
        resourceErrorView = ((TextView) searchInflater.findViewById(R.id.resource_error_view));

        //检查站
        jianchazhanLayout = ((LinearLayout) searchInflater.findViewById(R.id.jianchazhan_layout));
        leixingView = ((TextView) searchInflater.findViewById(R.id.leixing_view));
        zhibanView = ((TextView) searchInflater.findViewById(R.id.zhiban_view));
        renyuanView = ((TextView) searchInflater.findViewById(R.id.renyuan_view));
        renyuanxingmingView = ((TextView) searchInflater.findViewById(R.id.renyuan_xingming_view));
        fengliMeihuojiShuliangView = ((TextView) searchInflater.findViewById(R.id.fengli_shuliang_view));
        meihuoshuiqiangView = ((TextView) searchInflater.findViewById(R.id.shuiqiang_shuliang_view));
        erhaogongjuView = ((TextView) searchInflater.findViewById(R.id.erhao_shuliang_View));
        qitaGongjuView = ((TextView) searchInflater.findViewById(R.id.qita_view));
        jiankongView = ((TextView) searchInflater.findViewById(R.id.jiankong_view));
        shanxiView = ((TextView) searchInflater.findViewById(R.id.shanxi_view));
        //水源地
        shuiyuandiLayout = ((LinearLayout) searchInflater.findViewById(R.id.shuiyuandi_layout));
        zhishengjiQushuiView = ((TextView) searchInflater.findViewById(R.id.zhishengjiqushui_view));
        xushuiliangView = ((TextView) searchInflater.findViewById(R.id.xushuiliang_view));
        shuiyuandiLeixingView = ((TextView) searchInflater.findViewById(R.id.shuiyuani_leixing_view));
        // 专业队
        zhuanyeduiLayout = ((LinearLayout) searchInflater.findViewById(R.id.zhuanyedui_layout));
        zhuanyeduiLeixingTeamView = ((TextView) searchInflater.findViewById(R.id.leixing_team_view));
        duiwurenshuView = ((TextView) searchInflater.findViewById(R.id.duiwurenshu_view));
        zhibandianhuaView = ((TextView) searchInflater.findViewById(R.id.zhibandianhua_view));
        xiaofangcheTeamView = ((TextView) searchInflater.findViewById(R.id.xiaofangche_team_view));
        yunbingcheTeamView = ((TextView) searchInflater.findViewById(R.id.yunbingche_team_view));
        zhiuhuiTeamView = ((TextView) searchInflater.findViewById(R.id.zhihuiche_team_view));
        gaoyaTeamView = ((TextView) searchInflater.findViewById(R.id.gaoya_team_view));
        fenglimeihuoTeamView = ((TextView) searchInflater.findViewById(R.id.fenglimeihuo_team_view));
        erhaogongjuTeamView = ((TextView) searchInflater.findViewById(R.id.erhaogongju_team_view));
        meihuoshuiqiangTeamView = ((TextView) searchInflater.findViewById(R.id.meihuoshuiqiang_team_view));
        duijiangjiTeamView = ((TextView) searchInflater.findViewById(R.id.duijiangji_team_view));
        zhuangbeiyunshuTeamView = ((TextView) searchInflater.findViewById(R.id.zhuanbeiyunshu_team_view));
        yingfangTeamView = ((TextView) searchInflater.findViewById(R.id.yingfang_team_view));
        shuiguancheTeamView = ((TextView) searchInflater.findViewById(R.id.shuiguanche_team_view));
        //物资库
        wuzikuLayout = ((LinearLayout) searchInflater.findViewById(R.id.wuziku_layout));
        wuzikuLeixingMrView = ((TextView) searchInflater.findViewById(R.id.leixing_mr_view));
        fenglimeihuoMrView = ((TextView) searchInflater.findViewById(R.id.fenglimeihuo_mr_view));
        gaoyaxishuiMrView = ((TextView) searchInflater.findViewById(R.id.gaoyaxishui_mr_view));
        gaoyashuibengMrView = ((TextView) searchInflater.findViewById(R.id.gaoyashuibeng_mr_view));
        erhaogongjuMrView = ((TextView) searchInflater.findViewById(R.id.erhaogongju_mr_view));
        meihuoshuiqiangMrView = ((TextView) searchInflater.findViewById(R.id.meihuoshuiqiang_mr_view));
        youjuMrView = ((TextView) searchInflater.findViewById(R.id.youju_mr_view));
        geguanjiMrView = ((TextView) searchInflater.findViewById(R.id.geguanji_mr_view));
        huochangqiegejiMrView = ((TextView) searchInflater.findViewById(R.id.huochangqiegeji_mr_view));
        fanghuofuMrView = ((TextView) searchInflater.findViewById(R.id.fanghuofu_mr_view));
        fanghuoshoutaoMrView = ((TextView) searchInflater.findViewById(R.id.fanghuoshoutao_mr_view));
        fanghuotoukuiMrView = ((TextView) searchInflater.findViewById(R.id.fanghuotoukui_mr_view));
        fanghuoxieMrView = ((TextView) searchInflater.findViewById(R.id.fanghuoxie_mr_view));
        shuidaiMrView = ((TextView) searchInflater.findViewById(R.id.shuidai_mr_view));
        shuinangMrView = ((TextView) searchInflater.findViewById(R.id.shuinangg_mr_view));
        youtongMrView = ((TextView) searchInflater.findViewById(R.id.youtong_mr_view));
        //监测中心 指挥部
        jiancezhongxinLayout = ((LinearLayout) searchInflater.findViewById(R.id.jiancezhongxin_layout));
        jiancezhongxinTypeView = ((TextView) searchInflater.findViewById(R.id.jiancezhongxin_type_view));
        //瞭望塔
        liaowangtaLayout = ((LinearLayout) searchInflater.findViewById(R.id.liaowangta_layout));
        jiancefanweiView = ((TextView) searchInflater.findViewById(R.id.jiancefanwei_view));
        //视频监控点
        shipinjiankongLayout = ((LinearLayout) searchInflater.findViewById(R.id.shipinjiankong_layout));
        jiancefangweiMView = ((TextView) searchInflater.findViewById(R.id.jiancefanwei_m_view));
        lianwangMView = ((TextView) searchInflater.findViewById(R.id.lianwang_m_view));
        zhinengkakouMView = ((TextView) searchInflater.findViewById(R.id.zhinengkakou_m_view));
        jiankongleixingView = ((TextView) searchInflater.findViewById(R.id.jianceleixing_m_view));
        //墓地
        mudiLayout = ((LinearLayout) searchInflater.findViewById(R.id.mudi_layout));
        mudileixingView = ((TextView) searchInflater.findViewById(R.id.mudileixing_c_view));
        fengtouView = ((TextView) searchInflater.findViewById(R.id.fengtou_c_view));
        //危险源
        weixianyuanLayout = ((LinearLayout) searchInflater.findViewById(R.id.weixianyuan_layout));
        zhongdaweixianyuanView = ((TextView) searchInflater.findViewById(R.id.zhongdaweixianyuan_view));


        shijianView = ((TextView) searchInflater.findViewById(R.id.shijian_view));
        photoView = (ImageView) searchInflater.findViewById(R.id.photo_view);
        resourceDialogLayout = ((FrameLayout) searchInflater.findViewById(R.id.resource_layout));
        searchDialog.setContentView(searchInflater);
        Window searchDialogWindow = searchDialog.getWindow();
        searchDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpSearch = searchDialogWindow.getAttributes();

        WindowManager searchWm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int height = searchWm.getDefaultDisplay().getHeight();
        lpSearch.height = (int) (height * 0.8);
        searchDialogWindow.setAttributes(lpSearch);
        searchDialog.setCanceledOnTouchOutside(true);
        phoneView.setOnClickListener(this);
        if (!user.isEdit && !user.isDelete) {
            resourceCaozuoLayout.setVisibility(View.GONE);
        } else {
            resourceCaozuoLayout.setVisibility(View.VISIBLE);
        }
        if (user.isEdit) {
            resourceEditView.setVisibility(View.VISIBLE);
        } else {
            resourceEditView.setVisibility(View.GONE);
        }
        if (user.isDelete) {
            resourceDeleteView.setVisibility(View.VISIBLE);
        } else {
            resourceDeleteView.setVisibility(View.GONE);
        }
        resourceDialogLayout.setOnTouchListener(new OnTouchListener() {
            private float mCurPosY;
            private float mCurPosX;
            private float mPosY;
            private float mPosX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();

                        break;
                    case MotionEvent.ACTION_UP:
                        if (mCurPosY - mPosY > 0
                                && (Math.abs(mCurPosY - mPosY) > 25)) {
                            //向下滑動
                            Log.e(TAG, "onTouch: 下滑动");
                            searchDialog.dismiss();
                        } else if (mCurPosY - mPosY < 0
                                && (Math.abs(mCurPosY - mPosY) > 25)) {
                            //向上滑动
                        }

                        break;
                }
                return true;
            }
        });
        RxViewAction.clickNoDouble(photoView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call:picture " + currentResource.getPicture());
                        ArrayList<String> picList = new ArrayList<>();
                        if (currentResource.equals("null")) {
                            picList.add(String.valueOf(getResources().getDrawable(R.drawable.ic_no_pic)));
                        } else {
                            Log.e(TAG, "call:pic " + RequestUtils.IAMGE_URL + currentResource.getPicture());
                            picList.add(RequestUtils.IAMGE_URL + currentResource.getPicture());
                        }

                        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(TEMainActivity.this, picList);
                        imagPagerUtil.setContentText("");
                        imagPagerUtil.show();
                    }
                });
      /*  RxViewAction.clickNoDouble(phoneView)
                .subscribe(new Action1<Void>() {
                    private AlertDialog.Builder phoneDialog;
                    @Override
                    public void call(Void aVoid) {
                       *//* if (zerenrenPhone.isEmpty()){
                            Toast.makeText(TEMainActivity.this, "手机号为空", Toast.LENGTH_SHORT).show();
                            return;
                        }*//*

                    }
                });*/

        RxViewAction.clickNoDouble(resourceEditView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), ResourceEditActivity.class);
                        intent.putExtra("RESOURCE", currentResource);
                        startActivity(intent);
                    }
                });

        /**
         * 资源点删除
         */
        RxViewAction.clickNoDouble(resourceDeleteView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDeleteDialog();

                    }
                });
        /**
         * 资源点校验
         */
        RxViewAction.clickNoDouble(resourceJiaoyanView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showJiaoyanDialog();

                    }
                });
 /**
         * 资源点错误上报
         */
        RxViewAction.clickNoDouble(resourceErrorView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(),ResourceErrorActivity.class);
                        intent.putExtra("RESOURCE",currentResource);
                        startActivity(intent);
                    }
                });

        RxViewAction.clickNoDouble(surroundResourceView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), SurroundResourceActivity.class);
                        intent.putExtra("ID", currentId);
                        intent.putExtra("JINGDU", jingduView.getText().toString());
                        intent.putExtra("WEIDU", weiduView.getText().toString());
                        startActivity(intent);
                    }
                });

        daohangButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(getApplicationContext(), GPSNaviActivity.class);
                intent.putExtra("JINGDU", jingduView.getText().toString());
                intent.putExtra("WEIDU", weiduView.getText().toString());
                startActivity(intent);*/

                String jingweiStr = getLocation();
                String starweidu = "";
                String starjingdu = "";
                if (!jingweiStr.isEmpty()) {
                    List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
                    starweidu = jingweiList.get(1);
                    starjingdu = jingweiList.get(0);
                }

                com.skyline.terraexplorer.utils.LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new com.skyline.terraexplorer.utils.LatLng(Double.parseDouble(weiduView.getText().toString()), Double.parseDouble(jingduView.getText().toString())));

                Poi start = new Poi("", new LatLng(Double.parseDouble(starweidu), Double.parseDouble(starjingdu)), "");
                Poi end = new Poi(currentName, new LatLng(latLng.latitude, latLng.longitude), "");
                AmapNaviParams params = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER);
                params.setUseInnerVoice(true);
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, TEMainActivity.this);
                //     AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null), TEMainActivity.this);
            }
        });


        yinhuanView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HiddenDangerActivity.class);
                intent.putExtra("ID", currentId);
                intent.putExtra("NAME", currentName);
                intent.putExtra("TYPE", currentCode);
                intent.putExtra("JINGDU", jingduView.getText().toString());
                intent.putExtra("WEIDU", weiduView.getText().toString());
                intent.putExtra("GRID_ID", currentGridId);
                intent.putExtra("GRID_NAME", currentGridName);
                intent.putExtra("GRID_NO", currentGridNo);
                startActivity(intent);
            }
        });
        /**
         * 资源检查
         */
        goButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCode.equals("monitor")) { //视频监控点查看监控

                } else {      //资源检查点 检查资源
                    Intent intent = new Intent(getApplicationContext(), ResourceCheckActivity.class);
                    intent.putExtra("ID", currentId);
                    intent.putExtra("NAME", currentName);
                    intent.putExtra("TYPE", currentCode);
                    intent.putExtra("CODE", currentCode);
                    intent.putExtra("APIURL", currentApiUrl);
                    intent.putExtra("GRID_ID", currentGridId);
                    intent.putExtra("GRID_NAME", currentGridName);
                    intent.putExtra("GRID_NO", currentGridNo);
                    startActivity(intent);
                }

            }
        });

        jobOrderButton = (TextView) findViewById(R.id.job_order_button);
        resourceFindView = (TextView) findViewById(R.id.resource_find_view);
        resourceCheckView = (TextView) findViewById(R.id.resource_check_view);
        //检查计划
        RxViewAction.clickNoDouble(jobOrderButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        //获取检查计划
                        getCheckPlanFromService();

                    }
                });
        //资源检索
        RxViewAction.clickNoDouble(resourceFindView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        startActivity(new Intent(getApplicationContext(), ResourceSearchActivity.class));

                    }
                });
        //综合检查
        RxViewAction.clickNoDouble(resourceCheckView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        Intent intent = new Intent(getApplicationContext(), ComprehensiveCheckHomeActivity.class);
                        intent.putExtra("LNG", currentLong);
                        intent.putExtra("LAT", currentLat
                        );

                        startActivity(intent);

                    }
                });

        jobOrderListDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        jobOrderListInflater = LayoutInflater.from(this).inflate(R.layout.dialog_job_order_list, null);
        jobOrderListInflater.setMinimumWidth(10000);
        listView = ((RecyclerView) jobOrderListInflater.findViewById(R.id.job_order_listview));
        jobOrderListDialog.setContentView(jobOrderListInflater);
        Window jobOrderListWindow = jobOrderListDialog.getWindow();
        jobOrderListWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams jobOrderListLp = jobOrderListWindow.getAttributes();

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        jobOrderListLp.height = (int) (height * 0.7);
        jobOrderListWindow.setAttributes(jobOrderListLp);
        jobOrderListDialog.setCanceledOnTouchOutside(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        //register
        JobOrderViewBinder jobOrderViewBinder = new JobOrderViewBinder();
        jobOrderViewBinder.setListener(this);
        adapter.register(JobOrder.class, jobOrderViewBinder);
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

    }

    private void showJiaoyanDialog() {
        builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_wanggehua).setTitle("网格化")
                .setMessage("确认要校验么？").setPositiveButton("校验", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      jiaoyanResourceFromService();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
    }

    private void jiaoyanResourceFromService() {
        showDialogProgress(progressDialog, "校验中...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", currentId);
            jsonObject.put("checkState", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + currentApiUrl.substring(1, currentApiUrl.length()));
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "deleteResourceFromService: " + params);
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:-------------delete " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        searchDialog.dismiss();
                        new ResourceJiaoyanThread().start();
                        progressDialog.dismiss();
                        Toast.makeText(TEMainActivity.this, "校验成功", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < imageLabels.size(); i++) {
                            List<ImageLabel> labelList = imageLabels.get(i).labelList;
                            for (int j = 0; j < labelList.size(); j++) {
                                if (labelList.get(j).getUuId().equals(currentId)) {
                                    imageLabels.get(i).labelList.get(j).setCheckState("1");
                                }
                            }
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(TEMainActivity.this, "校验失败，请联系管理员进行校验", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" + ex.toString());
                progressDialog.dismiss();
                Toast.makeText(TEMainActivity.this, "删除失败，请联系管理员进行删除", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 控制资源点显示隐藏
     */
    private void initResourceShow() {
        if (isShowAll){   //隐藏
            checkStateView.setText("校验    显示");
            isShowAll = false;

            UI.runOnRenderThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < imageLabels.size(); i++) {
                        List<ImageLabel> labels = imageLabels.get(i).labelList;
                        for (int j = 0; j < labels.size(); j++) {
                            try {
                                // Log.e(TAG, "didSelectRowAtIndexPath: ---1");
                                if (labels.get(j).getCheckState().equals("1")){
                                    Log.e(TAG, "initResourceShow: 校验资源点已隐藏" );
                                    ISGWorld.getInstance().getCreator().DeleteObject(labels.get(j).objectId);

                                }
                            } catch (Exception ex) {
                                Log.e("", ex.getMessage());
                                continue;
                            }
                        }
                    }
                }
            });
        }else {     //显示
            isShowAll = true;
            checkStateView.setText("校验    隐藏");
            UI.runOnRenderThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < imageLabels.size(); i++) {
                        for (int j = 0; j < imageLabels.get(i).labelList.size(); j++) {
                            ImageLabel label = imageLabels.get(i).labelList.get(j);
                            IPosition position = ISGWorld.getInstance().getCreator().CreatePosition(label.longitude, label.latitude, 0, AltitudeTypeCode.ATC_ON_TERRAIN);
                            // Toast.makeText(TEMainActivity.this,"ceshi", Toast.LENGTH_SHORT).show();
                            //  Log.e(TAG, "run: fileName" + TEImageHelper.prepareImageForTE(imageLabels.get(i).imageId, 35));
                            if (label.getCheckState()!=null){
                                if (label.getCheckState().equals("1")) {        //未检查过得显示 检查过的不显示
                                    //校验资源点已显示
                                    if (isPad) {
                                        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(position, TEImageHelper.prepareImageForTE(imageLabels.get(i).imageId, 25), null, imageLabels.get(i).groupId, label.name);
                                    } else {
                                        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(position, TEImageHelper.prepareImageForTE(imageLabels.get(i).imageId, 35), null, imageLabels.get(i).groupId, label.name);
                                    }
                                    imageLabel.getMessage().setMessageID(bindMessage(label.name));
                                    label.objectId = imageLabel.getID();
                                }

                            }

                        }
                    }
                  //  SaveData.setData(getApplicationContext(), imageLabels, "resource");
                }
            });
        }
    }

    /**
     * 删除dialog
     */
    private void showDeleteDialog() {
        builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_wanggehua).setTitle("网格化")
                .setMessage("确认要删除么？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteResourceFromService();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
    }

    private void deleteResourceFromService() {
        showDialogProgress(progressDialog, "删除中...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", currentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + currentApiUrl.substring(1, currentApiUrl.length()));
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "deleteResourceFromService: " + params);
        x.http().request(HttpMethod.DELETE, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:-------------delete " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        searchDialog.dismiss();
                        new ResourceDeleteThread().start();
                        progressDialog.dismiss();
                        Toast.makeText(TEMainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(TEMainActivity.this, "删除失败，请联系管理员进行删除", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" + ex.toString());
                progressDialog.dismiss();
                Toast.makeText(TEMainActivity.this, "删除失败，请联系管理员进行删除", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.phone_layout://最普通dialog
                if (zerenrenPhone.isEmpty()) {
                    Toast.makeText(this, "电话号码为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_wanggehua)
                        .setTitle("确认拨打电话")
                        .setMessage(zerenrenPhone)
                        .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + zerenrenPhone);
                                intent.setData(data);
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                }).create().show();
                break;

            default:
        }

    }

    private void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 1);
                Toast.makeText(TEMainActivity.this, "1111", Toast.LENGTH_SHORT).show();
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 1);
            }
        }
    }

    /**
     * 疑似点击
     *
     * @param text
     * @return
     */
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
     * 检测应用的问题，如内存泄漏等
     */
    private void setupStrictMode() {
        if (TEApp.isDebug()) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems 或.detectAll()对所有检测到的问题
                    .detectCustomSlowCalls()
                    .penaltyLog()
                    .penaltyDialog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .detectActivityLeaks()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }


    private void OnEngineInitialized() {

        engineInitialized = true;
        // restore loading screen text 恢复加载屏幕文本
        ((TextView) findViewById(R.id.loadingView_loading)).setText(R.string.loading);

        // register tools 注册工具
        ToolManager.INSTANCE.registerTools();

        // set views order 设置视图顺序
        mainButton.bringToFront();
        mainMenu.bringToFront();
        loadingView.bringToFront();
        messageView.bringToFront();


        // subscribe to load project from notification event 订阅从通知事件加载项目
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                openProjectFromNotification(intent);
            }
        }, ProjectsTool.LoadProjectFilter);

        UI.runOnRenderThread(new Runnable() {


            @Override
            public void run() {
                // subscribe to SGWorldMessages events 订阅SGWorldMessages活动
                ISGWorld.getInstance().addOnSGWorldMessageListener(TEMainActivity.this);
                // we talk to TE only in latlon, to smiplify talking to search, flying to favorites and warkign with query tool
                // 我们只能在Latlon聊天，简化与搜索，飞往收藏夹和查询工具的交谈
                ISGWorld.getInstance().getCoordServices().getSourceCoordinateSystem().InitLatLong();
                // set shadow color 设置阴影颜色
                ISGWorld.getInstance().SetOptionParam("GlobalShadowColor", 0x99000000);
                if (TEApp.isDebug()) {
                    ISGWorld.getInstance().SetParam(8360, null);
                }
                // set ui and screen scale factors 设置ui和屏幕比例因子

                ISGWorld.getInstance().SetParam(8350, getResources().getDisplayMetrics().density); // Pass the screen factor to TE 将屏幕因子传递到TE
                ISGWorld.getInstance().SetParam(8370, UI.scaleFactor()); // Pass the UI scale factor to TE 将UI比例因子传递给TE

                //  currentPosition = ISGWorld.getInstance().getCreator().CreatePosition(Double.parseDouble(currentLong), Double.parseDouble(currentLat)1000, AltitudeTypeCode.ATC_TERRAIN_ABSOLUTE, 0, -90, 0 );


            }
        });
        //获取当前经纬度信息 显示地图
        currentPosition = UI.runOnRenderThread(new Callable<IPosition>() {
            public IPosition call() throws Exception {
                try {
                    return ISGWorld.getInstance().getCreator().CreatePosition(Double.parseDouble(currentLong), Double.parseDouble(currentLat), 400000, AltitudeTypeCode.ATC_TERRAIN_ABSOLUTE, 0, -90, 0);


                } catch (NumberFormatException e) {
                    // ignore the error 忽略错误
                    e.printStackTrace();
                    Log.e(TAG, "call: " + e.getMessage());
                }
                return null;
            }
        });

        String lastOpenedProject = getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE).getString(USER_STATE_PROJECT, null);


        if (lastOpenedProject == null) {
            //lastOpenedProject = AppLinks.getDefaultFlyFile();
            lastOpenedProject = AppLinks.getLocalDefaultFlyFile();
        } else {
            final String position = getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE).getString(USER_INITIAL_LOCATION, null);
//            final String position = getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE).getString(USER_STATE_LOCATION, null);
            if (position != null) {
                lastPosition = UI.runOnRenderThread(new Callable<IPosition>() {
                    public IPosition call() throws Exception {
                        try {
                            String[] positionParts = position.split(",");
                            return ISGWorld.getInstance().getCreator().CreatePosition(Double.parseDouble(positionParts[0]), Double.parseDouble(positionParts[1]), 1000, AltitudeTypeCode.ATC_TERRAIN_ABSOLUTE, 0, -90, 0);


                        } catch (NumberFormatException e) {
                            // ignore the error 忽略错误
                            e.printStackTrace();
                            Log.e(TAG, "call: " + e.getMessage());
                        }
                        return null;
                    }
                });
            }
        }


        ArrayList<String> sortedProjects = getRecentProjects();
        ArrayList<String> diskProjects = getProjectsOnDisk();
        for (String project : diskProjects) {
            if (sortedProjects.contains(project))
                continue;
            sortedProjects.add(project);
        }
        //
        if (sortedProjects.size() > 0) {
            ways = new String[sortedProjects.size()];
            for (int i = 0; i < sortedProjects.size(); i++) {
                ways[i] = sortedProjects.get(i);
            }
//            ways = new String[]{sortedProjects.get(0), sortedProjects.get(1)};
            chooseProject = ways[0];

            String[] newWays = new String[ways.length];
            for (int i = 0; i < ways.length; i++) {
                if (ways[i].equals(NET_MAP)) {
                    newWays[i] = "网络地图";
                } else {
                    //newWays[i] = ways[i].substring(44);
                    newWays[i] = ways[i];
                }
            }
            final IPosition finalLastPosition = lastPosition;

            //默认显示本地地图  暂时不用用户选择
         /*  new AlertDialog.Builder(this)
                    .setTitle("请选择地图")
                    .setSingleChoiceItems(newWays,
                            0, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    Log.d(TAG, "onClick: which = " + which + ",way = " + ways[which]);
                                    chooseProject = ways[which];
                                    //需填写正确格式

                                    openProject(chooseProject, finalLastPosition);
                                }
                            })
                    .setCancelable(false)
                    .show();*/
            openProject(ways[ways.length - 1], currentPosition);
        }

        //GetLogin(1);
        startService(new Intent(this, NettyService.class));
        Log.i(TAG, "onCreate:  启动服务");
        // openProject(lastOpenedProject, lastPosition);
    }

    private String[] ways;
    private String chooseProject = "";
    private static final String NET_MAP = "http://www.skylineglobe.com/SkylineGlobeLayers/SkylineGlobe Mobile/SkylineGlobe Mobile.fly";
    private static final String RECENT_PROJECTS = "com.skyline.terraexplorer.RECENT_PROJECTS";
    private static final String RECENT_PROJECTS_SEPARATOR = "x,x,x,x,x,x";

    private ArrayList<String> getRecentProjects() {
        String[] recentProjects = getPreferences(MODE_PRIVATE).getString(RECENT_PROJECTS, "").split(RECENT_PROJECTS_SEPARATOR);
        if (TextUtils.isEmpty(recentProjects[0])) {
            recentProjects[0] = AppLinks.getDefaultFlyFile();
            return new ArrayList<String>(Arrays.asList(recentProjects));
        }

        ArrayList<String> userProjects = new ArrayList<String>(Arrays.asList(recentProjects));
        // remove all disk projects that are not on the disk anymore form recent list 删除最近列出的不在磁盘上的所有磁盘项目
        for (int i = 0; i < userProjects.size(); i++) {
            if (userProjects.get(i).startsWith("/") && new File(userProjects.get(i)).exists() == false) {
                userProjects.remove(i);
                i--;
            }
        }
        return userProjects;
    }

    /**
     * 获取外部sd卡的路径
     *
     * @param mContext
     * @param is_removale
     * @return
     */
    private static String getStoragePath(Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 本地存储和外部存储上查找地图文件
     *
     * @return
     */
    private ArrayList<String> getProjectsOnDisk() {
        ArrayList<String> projects = new ArrayList<String>();
        Map<String, File> externalStorage = ExternalStorage.getAllStorageLocations();
        //  projects.addAll(getProjectsInStorage(externalStorage.get(ExternalStorage.SD_CARD)));
        // projects.addAll(getProjectsInStorage(externalStorage.get(ExternalStorage.EXTERNAL_SD_CARD)));
        // from android 4.4
        projects.addAll(getProjectsInStorage(new File("/storage/extSdCard")));//内部存储
        // from android 5
        projects.addAll(getProjectsInStorage(new File("/storage/sdcard")));//内部存储*/
        projects.addAll(getProjectsInStorage(new File(String.valueOf(getObbDir()))));//内部存储*/



       /* if (getStoragePath(this, true) != null) {
            projects.addAll(getProjectsInStorage(new File(getStoragePath(this, true))));//外部sd卡
            Log.e(TAG, "getProjectsOnDisk: 存储路径=" + getStoragePath(this, true) );
        }*/

        //
        File f = Environment.getExternalStorageDirectory();
        // projects.addAll(getProjectsInStorage(f));
        return projects;
    }

    /**
     * 获取手机本地存储中的项目
     *
     * @param root
     * @return
     * @update ltz
     * @date 2019-5-5 14:42:19
     */
    private ArrayList<String> getProjectsInStorage(File root) {
        // Creates an array of ".fly" file pathes
        ArrayList<String> projects = new ArrayList<String>();

        if (root == null)
            return projects;
        // Scan all of the Documents directory (including sub directories) for .fly files
        getPermission();
        ArrayList<File> directoryQueue = new ArrayList<File>();
        File rootFile = new File(root, getDocumentsPath());
        // Log.e(TAG, "getProjectsInStorage: " + rootFile.exists());
        if (rootFile.exists() == false)
            return projects;

        directoryQueue.add(rootFile);
        while (directoryQueue.size() > 0) {
            File dir = directoryQueue.get(0);
            File[] files = dir.listFiles();//？
            for (File file : files) {
                Log.e(TAG, "getProjectsInStorage: 文件名" + file.getName());
                // I am adding item with full path here, so I wont' have to think if I need to prepend documentPath
                // on open (on documents projects) or not (on user specified projects)
                // it is easier to remember full data and perform formatting before showing it to user
                // 我在这里添加完整路径的项目，所以我不必考虑如果我需要预先添加documentPath;在开放（文件项目）或不（在用户指定的项目上）
                // 在向用户显示之前，记住完整的数据并执行格式化是比较容易的
                if (file.isDirectory())
                    directoryQueue.add(file);
                else if (file.getPath().toLowerCase().endsWith(".fly"))
                    projects.add(file.getPath());
            }
            directoryQueue.remove(0);
        }
        return projects;
    }

    void getPermission() {
        int permissionCheck1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    124);
        }
    }

    /**
     * 获取默认地图文件路径
     *
     * @return
     */
    private static String getDocumentsPath() {
        // return "/com.skyline.terraexplorer/files/";
        //return "/手机地图/";
        return "haohai";
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        messageView.hide();
        mainMenu.hide();
        return super.onTouchEvent(event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mainMenu.show(ToolManager.INSTANCE.getMenuEntries());
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (messageView.getVisibility() != View.GONE) {
                messageView.hide();
                return true;
            }
            if (mainMenu.getVisibility() != View.GONE) {
                mainMenu.hide();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    //通过继承 BroadcastReceiver建立动态广播接收器
    class ZiyuanItemChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: 11111");
            try {
                List<ImageLabels> t = SaveData.getData(getApplicationContext(), "resource");
                Log.e(TAG, "onReceive:imageLabels.size " + t.size());
                imageLabels.clear();
                imageLabels = t;

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*
        teOnClose();
		finish();
		System.exit(0);
		*/

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "onResume: ");
        // Return to the normal rendering mode (see comment in the onPause() call). 返回到正常的渲染模式（请参阅onPause（）调用中的注释）。
        teView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
       /* UI.runOnRenderThreadAsync(new Runnable() {
            @Override
            public void run() {
                if (lastPosition != null) {
                    ISGWorld.getInstance().getNavigate().SetPosition(lastPosition);
                }
                // disable sunlight. Big fix #18383 禁止阳光 大修
                if (ISGWorld.getInstance().getCommand().IsChecked(1026, 0))
                    ISGWorld.getInstance().getCommand().Execute(1026, 0);
            }
        });

*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
        // Instead of calling teView.onPause(), which will destroy the GL context and will stop the render thread (and will cause problems to TE) we simply "pause" the renderer by switching to "render when dirty"
        // 而不是调用teView.onPause（），这将破坏GL上下文并停止渲染线程（并将导致TE的问题），我们只需“暂停”渲染器，切换到“脏的时候渲染”
        teView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        // it is possible to get onPause before engine is initialized,
        // so don't do nothing if we do not have engine
        /* 在引擎初始化之前可以获取onPause，
         所以如果没有引擎，不要做任何事情 */
        if (engineInitialized == false)
            return;

        UI.runOnRenderThreadAsync(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "onReceive: 15");
                Log.e(TAG, "run: ");
                String projectName = ISGWorld.getInstance().getProject().getName();
                if (TextUtils.isEmpty(projectName))
                    return;
                IPosition pos = ISGWorld.getInstance().getNavigate().GetPosition();
                String positionStr =
                        Double.toString(pos.getX()) + "_" +
                                Double.toString(pos.getY()) + "_" +
                                Double.toString(pos.getAltitude()) + "_" +
                                Integer.toString(pos.getAltitudeType()) + "_" +
                                Double.toString(pos.getYaw()) + "_" +
                                Double.toString(pos.getPitch()) + "_" +
                                Double.toString(pos.getRoll());
                getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE)
                        .edit()
                        .putString(USER_STATE_PROJECT, projectName)
                        .putString(USER_STATE_LOCATION, positionStr)
                        .apply();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //timer.cancel();
        // teView.onPause();
        this.unregisterReceiver(receiver);
        unregisterReceiver(outLoginReceiver);
        unregisterReceiver(jobOrderReceiver);

        //fix bug #19718
        System.exit(0);
        //////

    }

    @Override
    public void onLowMemory() {
        Log.e(TAG, "onReceive: 16");
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "onReceive: 17");
                Log.e(TAG, "run: ");
                teView.teOnLowMemory();
            }
        });
        super.onLowMemory();
    }

    private void toggleMainMenu() {
        if (menuButtonDragGestures.isDragInProgress())
            return;

        if (mainMenu.getVisibility() == View.VISIBLE)
            mainMenu.hide();
        else
            mainMenu.show(ToolManager.INSTANCE.getMenuEntries());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e(TAG, "onConfigurationChanged: ");
        Log.e(TAG, "onReceive: 18");
        mainMenu.updateHeight();
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onMainMenuShow() {
        Log.e(TAG, "onMainMenuShow: ");
        Log.e(TAG, "onReceive: 19");
        ToolContainer.INSTANCE.setEnabled(false);
        menuButtonDragGestures.setEnabled(false);
    }

    @Override
    public void onMainMenuHide() {
        ToolContainer.INSTANCE.setEnabled(true);
        menuButtonDragGestures.setEnabled(true);
    }

    @Override
    public void onMainMenuAction(MenuEntry menuEntry) {
        Log.e(TAG, "onMainMenuAction: ");
        Log.e(TAG, "onReceive: 21");
        ToolManager.INSTANCE.openTool(menuEntry.toolId, menuEntry.param);
    }

    @Override
    public void dragGestureRecognizerFinishedWithDirection(ControlDragGestures recognizer, DragDirection dragDirection) {
        Log.e(TAG, "dragGestureRecognizerFinishedWithDirection: ");
        Log.e(TAG, "onReceive: 22");
        int actionIndex = -1;
        if (dragDirection == DragDirection.Right) {
            actionIndex = getSharedPreferences(SettingsTool.PREFERENCES_NAME, MODE_PRIVATE).getInt(getString(R.string.key_menubutton_slide_right), -1);
            if (actionIndex == -1)
                actionIndex = MainButtonDragGestures.instance.defaultRight();
        }
        if (dragDirection == DragDirection.Up) {
            actionIndex = getSharedPreferences(SettingsTool.PREFERENCES_NAME, MODE_PRIVATE).getInt(getString(R.string.key_menubutton_slide_up), -1);
            if (actionIndex == -1)
                actionIndex = MainButtonDragGestures.instance.defaultUp();
        }
        MainButtonDragGestures.instance.preformAction(actionIndex);
    }

    @Override
    public boolean OnSGWorldMessage(final String MessageID, final String SourceObjectID) {

        Log.e(TAG, "onReceive: 23");
        String[] unsupportedSourceIds = new String[]{"MessageBarText", "ContainerMessage", "LoadFlyContainer"};
        for (String unsupportedSourceId : unsupportedSourceIds) {
            if (SourceObjectID.equalsIgnoreCase(unsupportedSourceId))
                return false;
        }
     /*   if (messageView.showMessage(MessageID, SourceObjectID)){
            Log.e(TAG, "OnSGWorldMessage:MessageID --" + MessageID);
            Log.e(TAG, "OnSGWorldMessage:SourceObjectID --" + SourceObjectID);

        }*/
        //   Log.e(TAG, "OnSGWorldMessage: " + messageView.showMessage(MessageID, SourceObjectID) );

        Log.e(TAG, "OnSGWorldMessage:MessageID --" + MessageID);
        Log.e(TAG, "OnSGWorldMessage:SourceObjectID --" + SourceObjectID);
        Log.e(TAG, "OnSGWorldMessage:size--- " + imageLabels.size());
        for (int i = 0; i < imageLabels.size(); i++) {
            for (int j = 0; j < imageLabels.get(i).labelList.size(); j++) {
                ImageLabel label = imageLabels.get(i).labelList.get(j);
                if (label.objectId.equals(SourceObjectID)) {

                    Log.e(TAG, "OnSGWorldMessage: --" + label.name);

                    DecimalFormat df = new DecimalFormat("#.00000");
                    Message message = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("what", SHOWDIALOG);
                    b.putString("id", label.objectId);
                    b.putString("uuId", label.uuId);
                    b.putString("name", label.name);
                    b.putString("jingdu", Double.parseDouble(df.format(label.longitude)) + "");
                    b.putString("weidu", Double.parseDouble(df.format(label.latitude)) + "");
                    b.putString("miaoshu", label.description);
                    b.putString("zerenren", label.leaderName);
                    b.putString("zerenrenPhone", label.leaderPhone);
                    b.putString("wangge", label.wanggeStr.replace("null", ""));
                    b.putString("type", label.resourceType);
                    b.putString("code", label.code);
                    b.putString("apiUrl", label.apiUrl);
                    b.putString("gridId", label.gridId);
                    b.putString("gridName", label.gridName);
                    b.putString("gridNo", label.gridNo);
                    b.putSerializable("imageLabel", label);
                    message.setData(b);
                    mHandler.sendMessage(message);

                }
            }
        }
        return false;
        //return messageView.showMessage(MessageID, SourceObjectID);
    }

    private void openProjectFromNotification(Intent intent) {
        String projectPath = intent.getStringExtra(ProjectsTool.PROJECT_PATH);
        openProject(projectPath, null);
    }

    private class ValueHolder {
        ApiException ex;
        OnLoadFinishedListener listener;
    }

    private void openProject(final String projectPath, final IPosition startPosition) {

        // first try to close tool container 第一次尝试关闭工具容器
        if (ToolContainer.INSTANCE.hideAndClearDelegate() == false)
            return;
        // create loading screen over the main view 创建加载屏幕的主要观点
        loadingView.setVisibility(View.VISIBLE);
        String[] friendlyName = ProjectsActivity.getFriendlyName(projectPath);
        ((TextView) findViewById(R.id.loadingView_projectName)).setText(String.format("%s/%s", friendlyName[0], friendlyName[1]));
        UI.runOnRenderThreadAsync(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "onReceive: 24");
                final ValueHolder vh = new ValueHolder();
                vh.listener = new OnLoadFinishedListener() {
                    @Override
                    public void OnLoadFinished(boolean bSuccess) {
                        Log.e(TAG, "onReceive: 25");
                        ISGWorld.getInstance().removeOnLoadFinishedListener(vh.listener);
                        UI.runOnUiThreadAsync(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "onReceive: 26");
                                TEMainActivity.this.onLoadFinished(projectPath, startPosition, vh.ex);
                            }
                        });
                    }
                };
                try {
                    ISGWorld.getInstance().addOnLoadFinishedListener(vh.listener);
                    ISGWorld.getInstance().getProject().Open(projectPath);
                    /*
                                    加载地图的时候创建group；2107-05-31
									 */
                    ISGWorld.getInstance().getProjectTree().CreateGroup(Constance.groupName);
                } catch (ApiException ex) {
                    vh.ex = ex;
                }
            }
        });


    }


    /**
     * 地图文件加载完成
     *
     * @param projectPath
     * @param startPosition
     * @param error
     */
    private void onLoadFinished(String projectPath, final IPosition startPosition, ApiException error) {
        if (error == null) {
            tuliLayout.setVisibility(View.VISIBLE);
            currentPositionView.setVisibility(View.VISIBLE);
            resourceLayout.setVisibility(View.VISIBLE);
            //开启时间循环获取同步标绘信息
            if (timer == null) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // (1) 使用handler发送消息
                        Log.e(TAG, "service: 过了10秒");
                        Message message = new Message();
                        message.what = TIME_CHANGE;
                        mHandler1.sendMessage(message);
                    }
                }, 0, 60000);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行

            }

            //getSyncPlotListData();

            //试用授权
            //计划开发网络连接授权

            loadingView.setVisibility(View.GONE);
            UI.runOnRenderThreadAsync(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "onReceive: 27");
                    if (startPosition != null) {
                        ISGWorld.getInstance().getNavigate().SetPosition(startPosition);
                    }
                    // disable sunlight. Big fix #18383 禁止阳光 大修
                    if (ISGWorld.getInstance().getCommand().IsChecked(1026, 0))
                        ISGWorld.getInstance().getCommand().Execute(1026, 0);

                }
            });

            try {
                UI.runOnRenderThread(new Runnable() {
                    @Override
                    public void run() {
                        //初始化点图标
                        initImageLabel();
                    }
                });
                User user = new DbConfig(this).getUser();
                boolean dataIsChange = user.dataIsChange;
                Log.e(TAG, "onLoadFinished: dataIsChange=" + dataIsChange);
                final List<ImageLabels> t = SaveData.getData(getApplicationContext(), "resource");
                if (t != null) {
                    imageLabels = t;

                    if (loginState == 1 || dataIsChange) {       //将资源点全部初始化为未选中状态
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

                        user.setDataIsChange(false);
                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    } else {

                        UI.runOnRenderThread(new Runnable() {
                            @Override
                            public void run() {
                                //初始化点图标
                                //initImageLabel();

                                Log.e(TAG, "onReceive: 28");
                                for (int i = 0; i < imageLabels.size(); i++) {
                                    for (int j = 0; j < imageLabels.get(i).labelList.size(); j++) {
                                        ImageLabel label = imageLabels.get(i).labelList.get(j);
                                        IPosition position = ISGWorld.getInstance().getCreator().CreatePosition(label.longitude, label.latitude, 0, AltitudeTypeCode.ATC_ON_TERRAIN);
                                        // Toast.makeText(TEMainActivity.this,"ceshi", Toast.LENGTH_SHORT).show();
                                        //  Log.e(TAG, "run: fileName" + TEImageHelper.prepareImageForTE(imageLabels.get(i).imageId, 35));
                                        if (isShowAll){  //全部显示
                                            if (isPad) {
                                                imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(position, TEImageHelper.prepareImageForTE(imageLabels.get(i).imageId, 25), null, imageLabels.get(i).groupId, label.name);
                                            } else {
                                                imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(position, TEImageHelper.prepareImageForTE(imageLabels.get(i).imageId, 35), null, imageLabels.get(i).groupId, label.name);
                                            }
                                            imageLabel.getMessage().setMessageID(bindMessage(label.name));
                                            label.objectId = imageLabel.getID();

                                        }else {     //只显示为检查的
                                            if (!label.getCheckState().equals("1")) {        //未检查过得显示 检查过的不显示
                                                if (isPad) {
                                                    imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(position, TEImageHelper.prepareImageForTE(imageLabels.get(i).imageId, 25), null, imageLabels.get(i).groupId, label.name);
                                                } else {
                                                    imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(position, TEImageHelper.prepareImageForTE(imageLabels.get(i).imageId, 35), null, imageLabels.get(i).groupId, label.name);
                                                }
                                                imageLabel.getMessage().setMessageID(bindMessage(label.name));
                                                label.objectId = imageLabel.getID();
                                            }
                                        }


                                    }
                                }
                                // Toast.makeText(TEMainActivity.this, "ceshi", Toast.LENGTH_SHORT).show();
                                SaveData.setData(getApplicationContext(), imageLabels, "resource");
                            }
                        });
                    }

                }

            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
                Log.e(TAG, ex.getStackTrace().toString());
            }
        } else {
            // first of all, show error message 首先,显示错误消息
            String[] friendlyName = ProjectsActivity.getFriendlyName(projectPath);
            String friendlyNameText = String.format("%s/%s", friendlyName[0], friendlyName[1]);
            String errorMessage = String.format(getString(R.string.loading_project_error), friendlyNameText, error.getLocalizedMessage());
//            String errorMessage = String.format(getString(R.string.loading_project_error), friendlyNameText, error.getLocalizedMessage());
            // if this was an network project, assume that error was in network and suggest to reload 如果这是一个网络项目，假设错误在网络中，建议重新加载
            ModalDialog modalDialog = new ModalDialog(R.string.loading_error_title, this);
            modalDialog.setContentMessage(errorMessage);
            if (projectPath.startsWith("/")) {
                modalDialog.setOneButtonMode();
            } else {
                modalDialog.setOkButtonTitle(R.string.retry);
                modalDialog.setTag(new Object[]{projectPath, startPosition});
            }
            modalDialog.show();
        }
    }

    /**
     * 、
     * 初始化点图标
     */
    private void initImageLabel() {

        int imageSize = 0;
        if (isPad) {
            imageSize = 25;
        } else {
            imageSize = 35;
        }


        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_team, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("专业队1"), "专业队1");
        imageLabel.getMessage().setMessageID(bindMessage("专业队1"));

        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_check_station, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("护林检查站1"), "护林检查站1");
        imageLabel.getMessage().setMessageID(bindMessage("护林检查站1"));

        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_monitor, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("视频监控点1"), "视频监控点1");
        imageLabel.getMessage().setMessageID(bindMessage("视频监控点1"));

        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_water_source, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("水源地1"), "水源地1");
        imageLabel.getMessage().setMessageID(bindMessage("水源地1"));


        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_watch_tower, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("瞭望塔1"), "瞭望塔1");
        imageLabel.getMessage().setMessageID(bindMessage("瞭望塔1"));


        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.res_danger_source, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("重点防火目标1"), "重点防火目标1");
        imageLabel.getMessage().setMessageID(bindMessage("重点防火目标1"));


        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_res_city_command, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("指挥部1"), "指挥部1");
        imageLabel.getMessage().setMessageID(bindMessage("指挥部1"));


        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_danger_source, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("重大危险源1"), "重大危险源1");
        imageLabel.getMessage().setMessageID(bindMessage("重大危险源1"));

        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_cemetery, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("墓地1"), "墓地1");
        imageLabel.getMessage().setMessageID(bindMessage("墓地1"));

        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_material_repository, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("物资库1"), "物资库1");
        imageLabel.getMessage().setMessageID(bindMessage("物资库1"));

        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_helicopter, imageSize), null,
                ISGWorld.getInstance().getProjectTree().FindItem("直升机机降点1"), "直升机机降点1");
        imageLabel.getMessage().setMessageID(bindMessage("直升机机降点1"));

        imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(ISGWorld.getInstance().getCreator().CreatePosition(1000, 1000, 0, AltitudeTypeCode.ATC_ON_TERRAIN),
                TEImageHelper.prepareImageForTE(R.drawable.ic_map1, 40), null,
                ISGWorld.getInstance().getProjectTree().FindItem("1"), "1");
        imageLabel.getMessage().setMessageID(bindMessage("1"));
    }

    /**
     * 获取同步标会信息
     */
    private void getSyncPlotListData() {
        RequestParams params = new RequestParams("http://49.232.128.132:10171/api/SyncPlot/GetSyncPlotList");
        params.setConnectTimeout(10000);
        Log.e(TAG, "getSyncPlotListData: " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:同步标会-- " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");


                    for (int i = 0; i < data.length(); i++) {
                        plotNewList.clear();
                        JSONObject object = data.getJSONObject(i);
                        String plotID = object.getString("PlotID");
                        String plotUserID = object.getString("PlotUserID");
                        int plotType = object.getInt("PlotType");
                        String plotInterTime = object.getString("PlotInterTime");
                        String plotLineColor = object.getString("PlotLineColor");
                        String plotName = object.getString("PlotName");
                        String plotOrbitString = object.getString("PlotOrbitString");

                        String str = plotOrbitString.substring(0, plotOrbitString.indexOf(")"));
                        String plotStr = "";
                        if (plotType == 1) {
                            plotStr = str.substring(str.indexOf("(") + 2);
                        } else {
                            plotStr = str.substring(str.indexOf("(") + 1);
                        }

                        List<String> plotStrList = Arrays.asList(plotStr.split(", "));
                        pointLatLngList.clear();
                        for (int j = 0; j < plotStrList.size(); j++) {
                            List<String> jingweiStr = Arrays.asList(plotStrList.get(j).split(" "));
                            String lnt = jingweiStr.get(0);
                            String lat = jingweiStr.get(1);
                            PointLatLng pointLatLng = new PointLatLng(Double.parseDouble(lat), Double.parseDouble(lnt));
                            pointLatLngList.add(pointLatLng);
                        }
                        boolean isNewData = true;
                        for (int j = 0; j < plotList.size(); j++) {
                            if (plotList.get(j).getPlotID().equals(plotID)) {  //如果相等则为老数据 否则为新数据加到loatNewList中
                                isNewData = false;
                            }
                        }
                        if (isNewData) {
                            plotNewList.add(new Plot(plotID, plotUserID, plotType, plotInterTime, plotLineColor, plotName, pointLatLngList));
                        }
                        //  plotList.add(new Plot(plotID,plotUserID,plotType,plotInterTime,plotLineColor,plotName,pointLatLngList));
                        //开启同步标绘
                        initPlooting();
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

    /**
     * 进行同步标会得方法
     */
    private void initPlooting() {
        plotList.addAll(plotNewList);
        Log.e(TAG, "initPlooting:size" + plotNewList.size());

        PlottingTool plottingTool = new PlottingTool();
        for (int i = 0; i < plotNewList.size(); i++) {

            Plot plot = plotNewList.get(i);

            Log.e(TAG, "initPlooting:type" + plot.getPlotType());
            //plottingTool.setPoints(plot.getPointLatLngList(),plot.getPlotType(),plot.getPlotLineColor());
        }
        // plotList.addAll(plotNewList);

    }


    @Override
    public void modalDialogDidDismissWithOk(ModalDialog dlg) {
        Log.e(TAG, "onReceive: 29");
        Log.e(TAG, "modalDialogDidDismissWithOk: ");
        Object[] data = (Object[]) dlg.getTag();
        if (data != null) {
            String projectPath = (String) data[0];
            IPosition position = (IPosition) data[1];
            openProject(projectPath, position);
        } else {
            modalDialogDidDismissWithCancel(dlg);
        }
    }

    @Override
    public void modalDialogDidDismissWithCancel(ModalDialog dlg) {
        Log.e(TAG, "onReceive: 30");
        Log.e(TAG, "modalDialogDidDismissWithCancel: ");
        // ProjectsTool will call loadProject with delay, so clear the name of old project
        // ProjectsTool会延迟调用loadProject，清除旧项目的名称
        ((TextView) findViewById(R.id.loadingView_projectName)).setText("");
//        ToolManager.INSTANCE.openTool(ProjectsTool.class.getName(), true);
        ToolManager.INSTANCE.openTool(SettingsTool.class.getName(), true);
    }
    //接收火警推送消息


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");

    }

    @Override
    public void onBackPressed() {

        exit();

    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息


            mHandler1.sendEmptyMessageDelayed(EXIT, 2000);
        } else {
            //  moveTaskToBack(true);       //返回首页
            Log.e(TAG, "exit: -----");

            Intent intent1 = new Intent();
            intent1.setAction("out_login_loginactivity");
            sendBroadcast(intent1);
            this.finish();
            //  System.exit(0);
        }
    }

    /**
     * 获取当前位置经纬度
     *
     * @return
     */
    @JavascriptInterface
    public String getLocation() {
        //获得位置服务
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
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
            Location location = locationManager.getLastKnownLocation(provider);
            try {
                return location.getLongitude() + "," + location.getLatitude();
            } catch (Exception e) {
                return "120.378755,36.064017";
            }

        }
        return null;
    }

    /**
     * 定位器provider
     *
     * @param locationManager
     * @return
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if (prodiverlist.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;//网络定位
        } else if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;//GPS定位
        } else {
            Toast.makeText(this, "未开启本应用地理位置信息，请先开启！", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    //通过继承 BroadcastReceiver建立动态广播接收器
    class OutLoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // finish();
            // mHandler1.sendEmptyMessageDelayed(FINASH,0);
            onDestroy();
        }
    }

    /**
     * 版本更新
     */
    private void getVersion() {
        try {
            versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode + "";
            Log.e(TAG, "getVersion: versionCode -- " + versionCode);
        } catch (PackageManager.NameNotFoundException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL +"auth/api/androidUpgrade/getCurrent");
        Log.e(TAG, "version: " + params);
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());
        //   params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:version-- " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")) {
                        JSONObject object = jsonObject1.getJSONArray("data").getJSONObject(0);
                        versionService = object.getString("version");
                        String versionDescription = object.getString("versionDescription");
                        String apkUrl = object.getString("apkUrl");
                        String isForce = object.getString("isForce");
                        Log.e(TAG, "onSuccess:version--1 ");
                        Log.e(TAG, "onSuccess:version--versionCode " + versionCode);
                        Log.e(TAG, "onSuccess:version--versionService " + versionService);
                        // ShowDialog(versionService, apkUrl, versionDescription, isForce);
                        if (Integer.parseInt(versionCode) < Integer.parseInt(versionService)) {
                            Log.e(TAG, "onSuccess:version--2 ");
                            ShowDialog(versionService, apkUrl, versionDescription, isForce);
                        }

                    }

                } catch (JSONException e) {

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

    /**
     * 用户更新dialog
     *
     * @param version
     * @param downloadUrl
     * @param versionDesc
     * @param isMustUpgrade
     */
    private void ShowDialog(String version, final String downloadUrl, String versionDesc, String isMustUpgrade) {
        if (isMustUpgrade.equals("0")) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("版本更新")
                    .setMessage(versionDesc)
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            mBar = new CommonProgressDialog(TEMainActivity.this);
                            mBar.setCanceledOnTouchOutside(false);
                            mBar.setTitle("正在下载");
                            mBar.setCustomTitle(LayoutInflater.from(
                                    TEMainActivity.this).inflate(
                                    R.layout.title_dialog, null));
                            mBar.setMessage("正在下载");
                            mBar.setIndeterminate(true);
                            mBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            mBar.setCancelable(false);
                            // downFile(URLData.DOWNLOAD_URL);
                            final DownloadTask downloadTask = new DownloadTask(
                                    TEMainActivity.this);
                            downloadTask.execute(downloadUrl);
                            mBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        }
                    }).show();
        } else {
            Log.e(TAG, "onSuccess:version--4 ");
            new android.app.AlertDialog.Builder(this)
                    .setTitle("版本更新")
                    .setMessage(versionDesc)
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            mBar = new CommonProgressDialog(TEMainActivity.this);
                            mBar.setCanceledOnTouchOutside(false);
                            mBar.setTitle("正在下载");
                            mBar.setCustomTitle(LayoutInflater.from(
                                    TEMainActivity.this).inflate(
                                    R.layout.title_dialog, null));
                            mBar.setMessage("正在下载");
                            mBar.setIndeterminate(true);
                            mBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            mBar.setCancelable(false);
                            // downFile(URLData.DOWNLOAD_URL);
                            final DownloadTask downloadTask = new DownloadTask(
                                    TEMainActivity.this);
                            downloadTask.execute(downloadUrl);
                            mBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    /**
     * 下载应用
     *
     * @author Administrator
     */
    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error
                // report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(TEMainActivity.this.getObbDir().getAbsolutePath(),
                            DOWNLOAD_NAME + versionService + ".apk");

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }

                } else {
                    Toast.makeText(TEMainActivity.this, "sd卡未挂载",
                            Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mBar.setIndeterminate(false);
            mBar.setMax(100);
            mBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mBar.dismiss();
            if (result != null) {

//                // 申请多个权限。大神的界面
//                AndPermission.with(MainActivity.this)
//                        .requestCode(REQUEST_CODE_PERMISSION_OTHER)
//                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                        .rationale(new RationaleListener() {
//                                       @Override
//                                       public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
//                                           // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
//                                           AndPermission.rationaleDialog(MainActivity.this, rationale).show();
//                                       }
//                                   }
//                        )
//                        .send();
                // 申请多个权限。
               /* AndPermission.with(MainActivity.this)
                        .requestCode(REQUEST_CODE_PERMISSION_SD)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                        .rationale(rationaleListener
                        )
                        .send();*/


                Toast.makeText(context, "您未打开SD卡权限" + result, Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(context, "File downloaded",
                // Toast.LENGTH_SHORT)
                // .show();
                isGengxin = true;
                if (Build.VERSION.SDK_INT >= 26) {
                    update();
                   /* boolean b = getPackageManager().canRequestPackageInstalls();
                    if (b) {
                        update();
                    } else {
                        //请求安装未知应用来源的权限
                        //  ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10086);
                        //   Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,Uri.fromParts("package:"+ getPackageName()));
                        //  startActivityForResult(intent, 10086);

                        ActivityCompat.requestPermissions(TEMainActivity.this, new String[]{android.Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10086);
                    }*/
                } else {
                    update();
                }

            }

        }
    }

    private void update() {
        isGengxin = false;
        //安装应用
        //  Intent intent = new Intent(Intent.ACTION_VIEW);


        String fileName = TEMainActivity.this.getObbDir().getAbsolutePath() + "/" + DOWNLOAD_NAME + versionService + ".apk";
      /*  File file = null;
        file = new File(fileName);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            tempUri = FileProvider.getUriForFile(MainActivity.this, "com.hht.hsatellitemobile.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(this.getObbDir().getAbsolutePath(), DOWNLOAD_NAME + version + ".apk"));
        }

        Log.e(TAG, "update: ----" + tempUri);
        intent.setDataAndType(tempUri,
                "application/vnd.android.package-archive");
        startActivity(intent);*/
        if (Build.VERSION.SDK_INT >= 24) {
            File file = new File(fileName);
            tempUri = FileProvider.getUriForFile(TEMainActivity.this, "com.skyline.terraexplorer.fileProvider", file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(tempUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(install);
        }
    }

    class JobOrderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            jobOrderButton.setBackgroundResource(R.drawable.bg_button_red);
        }
    }


    /**
     * 导航需要
     */
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


    /**
     * 导航需要
     */
    @Override
    public void onScaleAutoChanged(boolean b) {

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

    /**
     * 删除地图资源点
     */
    private void deleteResource() {
        DbConfig dbConfig = new DbConfig(getApplicationContext());
        DbManager db = dbConfig.getDbManager();
        try {
            user.setDataIsChange(true);
            db.saveOrUpdate(user);
        } catch (DbException e) {
            e.printStackTrace();
        }

        /**
         * 删除地图上资源点
         */
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                ISGWorld.getInstance().getCreator().DeleteObject(currentResource.getObjectId());
            }
        });
        /**
         * 删除当前选中列表中的数据
         */
        try {
            List<ImageLabels> t = SaveData.getData(getApplicationContext(), "resource");
            for (int i = 0; i < t.size(); i++) {
                List<ImageLabel> labelList = t.get(i).labelList;
                for (int j = 0; j < labelList.size(); j++) {
                    if (labelList.get(i).getUuId().equals(currentResource.getUuId())) {
                        labelList.remove(i);
                        break;
                    }
                }
            }
            SaveData.setData(getApplicationContext(), t, "resource");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        /**
         * 删除数据库数据
         */
        String code = currentResource.getCode();
        try {
            if (code.equals("team")) {

                List<TeamDTO> dtoList = db.selector(TeamDTO.class)
                        .findAll();
                for (int i = 0; i < dtoList.size(); i++) {
                    if (dtoList.get(i).getId().equals(currentResource.getUuId())) {
                        dtoList.get(i).setState("DELETE");
                        db.delete(dtoList.get(i));
                    }
                }

            } else if (code.equals("checkStation")) {
                List<CheckStationDTO> dtoList = db.selector(CheckStationDTO.class)
                        .findAll();
                for (int i = 0; i < dtoList.size(); i++) {
                    if (dtoList.get(i).getId().equals(currentResource.getUuId())) {
                        dtoList.get(i).setState("DELETE");
                        db.delete(dtoList.get(i));
                    }
                }
            } else if (code.equals("monitor")) {
                List<MonitorDTO> dtoList = db.selector(MonitorDTO.class)
                        .findAll();
                for (int i = 0; i < dtoList.size(); i++) {
                    if (dtoList.get(i).getId().equals(currentResource.getUuId())) {
                        dtoList.get(i).setState("DELETE");
                        db.delete(dtoList.get(i));
                    }
                }
            } else if (code.equals("waterSource")) {
                List<WaterSourceDTO> dtoList = db.selector(WaterSourceDTO.class)
                        .findAll();
                for (int i = 0; i < dtoList.size(); i++) {
                    if (dtoList.get(i).getId().equals(currentResource.getUuId())) {
                        dtoList.get(i).setState("DELETE");
                        db.delete(dtoList.get(i));
                    }
                }
            } else if (code.equals("watchTower")) {
                List<WatchTowerDTO> dtoList = db.selector(WatchTowerDTO.class)
                        .findAll();
                for (int i = 0; i < dtoList.size(); i++) {
                    if (dtoList.get(i).getId().equals(currentResource.getUuId())) {
                        dtoList.get(i).setState("DELETE");
                        db.delete(dtoList.get(i));
                    }
                }
            } else if (code.equals("fireCommand")) {
                List<FireCommandDTO> dtoList = db.selector(FireCommandDTO.class)
                        .findAll();
                for (int i = 0; i < dtoList.size(); i++) {
                    if (dtoList.get(i).getId().equals(currentResource.getUuId())) {
                        dtoList.get(i).setState("DELETE");
                        db.delete(dtoList.get(i));
                    }
                }
            } else if (code.equals("dangerSource")) {
                List<DangerSourceDTO> dtoList = db.selector(DangerSourceDTO.class)
                        .findAll();
                for (int i = 0; i < dtoList.size(); i++) {
                    if (dtoList.get(i).getId().equals(currentResource.getUuId())) {
                        dtoList.get(i).setState("DELETE");
                        db.delete(dtoList.get(i));
                    }
                }
            } else if (code.equals("cemetery")) {
                List<CemeteryDTO> dtoList = db.selector(CemeteryDTO.class)
                        .findAll();
                for (int i = 0; i < dtoList.size(); i++) {
                    if (dtoList.get(i).getId().equals(currentResource.getUuId())) {
                        dtoList.get(i).setState("DELETE");
                        db.delete(dtoList.get(i));
                    }
                }
            } else if (code.equals("materialRepository")) {
                List<MaterialRepositoryDTO> dtoList = db.selector(MaterialRepositoryDTO.class)
                        .findAll();
                for (int i = 0; i < dtoList.size(); i++) {
                    if (dtoList.get(i).getId().equals(currentResource.getUuId())) {
                        dtoList.get(i).setState("DELETE");
                        db.delete(dtoList.get(i));
                    }
                }
            } else if (code.equals("helicopterPoint")) {
                List<HelicopterPointDTO> dtoList = db.selector(HelicopterPointDTO.class)
                        .findAll();
                for (int i = 0; i < dtoList.size(); i++) {
                    if (dtoList.get(i).getId().equals(currentResource.getUuId())) {
                        dtoList.get(i).setState("DELETE");
                        db.delete(dtoList.get(i));
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

}