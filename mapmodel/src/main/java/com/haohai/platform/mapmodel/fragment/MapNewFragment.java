package com.haohai.platform.mapmodel.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haohai.platform.firelibrary.ui.activity.FireStatisticsActivity;
import com.haohai.platform.firelibrary.ui.model.LatLng;
import com.haohai.platform.firelibrary.utils.LatLngChange;
import com.haohai.platform.mapmodel.R;
import com.haohai.platform.mapmodel.Utils.GetJsonDataUtil;
import com.haohai.platform.mapmodel.Utils.JsApi;
import com.haohai.platform.mapmodel.activity.PicActivity;
import com.haohai.platform.mapmodel.activity.PlayerActivity;
import com.haohai.platform.mapmodel.activity.PlayerRtspActivity;
import com.haohai.platform.mapmodel.activity.WeixingActivity;
import com.haohai.platform.mapmodel.fragment.base.HhBaseFragment;
import com.haohai.platform.mapmodel.listener.OnLoadMoreListener;
import com.haohai.platform.mapmodel.model.CemeteryDTO;
import com.haohai.platform.mapmodel.model.CheckStationCheckDTO;
import com.haohai.platform.mapmodel.model.CheckStationDTO;
import com.haohai.platform.mapmodel.model.MapModel;
import com.haohai.platform.mapmodel.model.MapPosition;
import com.haohai.platform.mapmodel.model.MonitorDTO;
import com.haohai.platform.mapmodel.model.WaterSourceDTO;
import com.haohai.platform.mapmodel.model.WeixingModel;
import com.haohai.platform.mapmodel.multitype.Empty;
import com.haohai.platform.mapmodel.multitype.EmptyViewBinder;
import com.haohai.platform.mapmodel.multitype.OneBodyFire;
import com.haohai.platform.mapmodel.multitype.OneBodyFireViewBinder;
import com.haohai.platform.mapmodel.multitype.ResourceList;
import com.haohai.platform.mapmodel.multitype.ResourceListViewBinder;
import com.haohai.platform.mapmodel.multitype.WeixingModelViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.Setting;
import com.ruyiruyi.rylibrary.db.User;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.route.RouteUtils;
import com.ruyiruyi.rylibrary.utils.LatLngChangeNew;
import com.ruyiruyi.rylibrary.utils.NumberUtils;
import com.ruyiruyi.rylibrary.utils.image.ImagPagerUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

/**
 * Created by geyang on 2020/7/21.
 */

public class MapNewFragment extends HhBaseFragment implements JsApi.OnJsClickListener, ResourceListViewBinder.OnResourceLsitItemClick, WeixingModelViewBinder.OnWeixingInfoItemClick, OneBodyFireViewBinder.OnOneBodyItemClick, DatePicker.OnDateChangedListener, INaviInfoCallback {

    private static final String TAG = "MapFragment";
    public static final double LATITUDE_DEF = 43.089946; //默认中心点
    public static final double LONGTITUDE_DEF = 81.23243;
    private DWebView dWebView;
    private Button jiaButton;
    private Button jianButton;
    private myReceiver Receiver;
    private TextView weixingButton;
    private LinearLayout weixingButtonLayout;

    private boolean isWeixingButtonShow = false;
    private boolean isZiyuanButtonShow = false;
    private boolean isonebodyButtonShow = false;
    private LinearLayout weixinLayout;


    private Dialog mapChooseDialog;
    private View mapChooseInflater;
    private LinearLayout gugeGaochengMap;
    private LinearLayout gugeYingxiangMap;
    private LinearLayout tiandiShiliangMap;
    private LinearLayout tiandiYingxiangMap;
    private TextView mapChooseButton;
    private TextView mapDimensionChoos;
    private Button chaoshiButton;
    private LinearLayout quyuLayout;
    private ImageView gugeYingxiangImage;
    private TextView gugeYingxiangText;
    //    private ImageView gugeGaochengImage;
//    private TextView gugeGaochengText;
    private ImageView tiandiShiliangImage;
    private TextView tiandiShiliangText;
    private ImageView tiandiYingxiangImage;
    private TextView tiandiYingxiangText;
    private ImageView tucengButton;
    private ImageView mapButton;
    private static final int EXIT = 1;
    private static final int GUGE_YINGXIANG = 2;
    private static final int GUGE_GAOCHENG = 3;
    private static final int TIANDI_SHILIANG = 4;
    private static final int TIANDI_YINGXIANG = 5;
    private static final int MAP_GAOFEN = 13;
    private static final int DIALOG_FIRE_SHOW = 6;
    private static final int DIALOG_ONEBODY_FIRE_SHOW = 20;
    private static final int DIALOG_MOINTOR_SHOW = 22;
    private static final int MAP_ERROR_SHOW = 8;
    private static final int FIRE_ERROR_SHOW = 9;
    private static final int TWO_D = 10;
    private static final int THREE_D = 11;
    private static final int SHOW_MAP = 7;
    private static final int TIME_CHANGE = 21;
    private static boolean isExit = false;
    private Number currentLongitude;
    private Number currentLatitude;
    private TextView weixingShezhiView;
    private TextView weixingChaxunView;
    private TextView weixingLiebiaoView;
    private TextView onebody_view;
    private TextView ddrw_view;
    private Dialog searchDialog;
    private View searchInflater;
    private LinearLayout oneHoursLayout;
    private LinearLayout currntTimeLayout;
    private LinearLayout threeHoursLayout;
    private LinearLayout oneDayLayout;
    private LinearLayout threeDayLayout;
    private LinearLayout fiveDayLayout;
    private LinearLayout gaojiSearchLayout;

    public int currentFireFindTime = 3;  //时间
    private ProgressDialog progressDialog;
    private User user;
    private List<WeixingModel> weixingModelList;
    private List<OneBodyFire> oneBodyFireList;
    private List<OneBodyFire> oneBodyFireFenleiList;
    private List<ResourceList> resourceListList;
    private List<MonitorDTO> monitorDTOList;
    private List<WaterSourceDTO> waterSourceDTOList;
    private List<CemeteryDTO> cemeteryDTOList;
    private List<CheckStationDTO> checkStationDTOList;

    private Dialog fireInfoListDialog;
    private Dialog onebodyListDialog;
    private View fireInfoListInflater;
    private View onebodyListInflater;
    private TextView fireCountText;
    private RecyclerView weixingListView;
    private RecyclerView onebodyListView;
    private LinearLayout onebodyLayout;
    private TextView fenleiView;
    private List<Object> weixingItems = new ArrayList<>();
    private MultiTypeAdapter weixingAdapter;
    private List<Object> onebodyItems = new ArrayList<>();
    private MultiTypeAdapter onebodyAdapter;
    private List<Object> resourceItems = new ArrayList<>();
    private MultiTypeAdapter resourceAdapter;
    private Dialog gaojiDialog;
    private View gaojiInflater;

    private int currentFireListType = 1;  //1是时间排序  2是编号分类
    private int choose1 = 1;
    private int shipinChoose1 = 0;
    private AlertDialog.Builder builder;
    private WeixingModel currentWeixingModel;
    private OneBodyFire currentOneBodyFire;
    private ResourceList currentResourceList;
    private Dialog weixingFireDialog;
    private View weixingFireInflater;
    private TextView fireAddressText;
    private TextView fireTimeText;
    private TextView jingWeiText;
    private TextView kexinText;
    private TextView mianjiText;
    private TextView cishuText;
    private TextView leixingText;
    private TextView shujuyuanText;
    private TextView huodianCodeText;
    private TextView fankuiText;
    private ImageView huodianOneImage;
    private ImageView huodianTwoImage;
    private TextView xiangyuanmianjiView;
    private TextView xiangyuanshuView;
    private TextView retypeView;
    private boolean isShowSearchDialog = false;
    private LinearLayout ziyuanLayout;
    private TextView ziyuanButton;
    private TextView onebodyButton;
    private LinearLayout ziyuanButtonLayout;
    private LinearLayout onebodyButtonLayout;
    private FireWeixingReceiver fireWeixingReceiver;
    private String weixingFireId = "";
    private boolean isTuisong = false;
    private String obTime;  //观测时间
    private LinearLayout gaojiStarTimeLayout;
    private LinearLayout gaojiEndTimeLayout;
    private TextView gaojiStartimeText;
    private TextView gaojiEndTimeText;
    private TextView chongzhiButton;
    private TextView findButton;
    private StringBuffer date;
    private StringBuffer endDate;
    private int year;
    private int month;
    private int day;
    public int chooseHour;
    public int chooseMinute;
    public boolean isChooseStarTime;
    private ProgressDialog gaojiFindDialog;
    private Dialog resourceinfoDialog;
    private View resourceInflater;
    String resorcetype = "";
    String kejianguangUrl = "";
    String kejianguangMId = "";
    String rechengxiangUrl = "";
    String rechengxiangMId = "";
    private int currentPage = 1;
    private int totalSize;
    private int lastPage;
    private TextView shaixuanbutton;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            int what = data.getInt("what");
            switch (what) {
                case DIALOG_FIRE_SHOW:
                    String id = data.getString("id");
                    for (int i = 0; i < weixingModelList.size(); i++) {
                        if (weixingModelList.get(i).getId().equals(id)) {
                            currentWeixingModel = weixingModelList.get(i);
                        }
                    }

                    //飞到精确点上
                    initWeixingFlyMap();
                    //加载卫星详细数据
                    initWeixinModelData();
                    break;
                case DIALOG_ONEBODY_FIRE_SHOW:
                    String oneBodyid = data.getString("id");
                    for (int i = 0; i < oneBodyFireList.size(); i++) {
                        if (oneBodyFireList.get(i).getId().equals(oneBodyid)) {
                            currentOneBodyFire = oneBodyFireList.get(i);
                        }
                    }

                    //飞到精确点上
                    initOneBodyFlyMap();
                    //加载卫星详细数据
                    initOneBodyFireModelData();
                    break;
                case DIALOG_MOINTOR_SHOW:
                    String resourceid = data.getString("id");
                    resourceinfoDialog.show();
                    getinfofromid(resourceid);
                    break;
            }

        }
    };
    private Dialog oneBodyFireDialog;
    private View oneBodyFireInflater;
    private TextView mingchengView;
    private TextView dizhiView;
    private TextView shijianView;
    private TextView jingweiduView;
    private ImageView yitijiOneView;
    private ImageView yitijiTwoView;
    private boolean isGaojiFind = false;
    private TextView resourcenameview;
    private TextView resoucedizhiview;
    private TextView resourcejingweiduview;
    private Button kejianguangbutton;
    private Button rechengxiangbutton;
    private Dialog resourceListDialog;
    private View resourceListInflater;
    private RecyclerView resourceListView;
    private LinearLayout zhenshiLayout;
    private TextView zhenshiButton;
    private TextView wubaoButton;
    private TextView zhenshiTextView;
    public int isReleas = 2;  //0疑似火情  1是真实火情  2是未处理
    public int isReleasList = 3;  //0疑似火情  1是真实火情  2是未处理 3是全部
    public int shipinState = 0;  //2：森林防火4：海域监控5：砂石盗采 0全部
    private TextView fenleiTextView;
    private LinearLayout fenleiLayout;
    private LinearLayout oneBodyFenleiLayout;
    private TextView weixingShijianView;
    private LinearLayout daohangLayout;
    private MapView baiduMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;

    public int markerType = 0;
    public static final int ONE_BODY = 0;
    public static final int WEI_XING = 1;
    public static final int RESOURCE_MONITOR = 2;
    private TextView resourceTypeView;
    private List<OverlayOptions> optionsAllList;
    public boolean isShowWeixing = true;
    public boolean isShowYitiji = true;
    public boolean isShowMonitor = false;
    public boolean isShowWaterSource = false;
    public boolean isShowCemetery = false;
    public boolean isShowCheckStation = false;
    private DistrictSearch mDistrictSearch;
    private SwipeRefreshLayout oneBodyRefreshLayout;
    public boolean isOneBodyShuaxin = false;
    private TextView shipinFenleiTextView;
    private LinearLayout shipinFenleiLayout;
    private TextView oneBodyKJGButton;
    private TextView oneBodyRCXButton;
    private LinearLayout oneBodyShipinLayout;
    private TextView statisticsButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_new, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());

        user = new DbConfig(getContext()).getUser();
        weixingModelList = new ArrayList<>();
        oneBodyFireList = new ArrayList<>();
        oneBodyFireFenleiList = new ArrayList<>();
        monitorDTOList = new ArrayList<>();
        waterSourceDTOList = new ArrayList<>();
        cemeteryDTOList = new ArrayList<>();
        checkStationDTOList = new ArrayList<>();
        resourceListList = new ArrayList<>();
        optionsAllList = new ArrayList<>();
        currentWeixingModel = new WeixingModel();
        currentWeixingModel = new WeixingModel();


        getLocation();
        initDateTime();
        initView();
        bindView();
        //代码 注册 广播接收器
        Receiver = new myReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("zcd.voicerobot");
        getActivity().registerReceiver(Receiver, filter);

        //实例化IntentFilter对象
        IntentFilter fireFilter = new IntentFilter();
        fireFilter.addAction("fire_weixing_tengxun");
        fireWeixingReceiver = new FireWeixingReceiver();
        //注册广播接收
        getContext().registerReceiver(fireWeixingReceiver, fireFilter);


        isShowSearchDialog = false;
        isTuisong = false;
        currentFireFindTime = 3;

        getWeixingDataFromSetvice();
        getonebodyDataFromSetvice();
        getResourcesListFromService();
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }


    /**
     * 卫星推送消息点击广播
     */
    class FireWeixingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            weixingFireId = intent.getStringExtra("fire_id");
            obTime = intent.getStringExtra("ob_time");
            Log.e(TAG, "onReceive:weixingFireId " + weixingFireId);
            Log.e(TAG, "onReceive:obTime " + obTime);
            isShowSearchDialog = false;
            isTuisong = true;
            isGaojiFind = false;
            getWeixingDataFromSetvice();
        }
    }

    private void bindView() {
        /**
         * 统计的点击
         */
        RxViewAction.clickNoDouble(statisticsButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), FireStatisticsActivity.class));
                    }
                });

        RxViewAction.clickNoDouble(oneBodyKJGButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String videoPath = currentOneBodyFire.getVideoPath1();
                        if (videoPath == null) {
                            Toast.makeText(getContext(), "暂无可见光视频", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(getContext(), PlayerActivity.class);
                        //     intent.putExtra("PLAYER_URL", "rtmp://10.135.49.202:1935/playBack/af7ae7cb-d637-66cb-c1cc-ab86af9cc9af-main/1616382481/1616381276.flv?streamType=1&manufacturer=1&startTime=1616381246&endTime=1616381276");
                        intent.putExtra("PLAYER_URL",videoPath.replace("59.213.220.32:85","112.44.67.43"));
                        intent.putExtra("PLAYER_NAME", currentOneBodyFire.getName());
                        startActivity(intent);
                        /*if (currentOneBodyFire.getUrlType().equals("rtmp")) {
                            Intent intent = new Intent(getContext(), PlayerRtspActivity.class);
                            //     intent.putExtra("PLAYER_URL", "rtmp://10.135.49.202:1935/playBack/af7ae7cb-d637-66cb-c1cc-ab86af9cc9af-main/1616382481/1616381276.flv?streamType=1&manufacturer=1&startTime=1616381246&endTime=1616381276");
                            intent.putExtra("PLAYER_URL",videoPath);
                            intent.putExtra("PLAYER_NAME", currentOneBodyFire.getName());
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(getContext(), PlayerActivity.class);
                            //     intent.putExtra("PLAYER_URL", "rtmp://10.135.49.202:1935/playBack/af7ae7cb-d637-66cb-c1cc-ab86af9cc9af-main/1616382481/1616381276.flv?streamType=1&manufacturer=1&startTime=1616381246&endTime=1616381276");
                            intent.putExtra("PLAYER_URL",videoPath);
                            intent.putExtra("PLAYER_NAME", currentOneBodyFire.getName());
                            startActivity(intent);
                        }*/

                    }
                });

        RxViewAction.clickNoDouble(oneBodyRCXButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String videoPath = currentOneBodyFire.getVideoPath2();
                        if (videoPath == null) {
                            Toast.makeText(getContext(), "暂无热成像视频", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(getContext(), PlayerActivity.class);
                        // intent.putExtra("PLAYER_URL", "rtmp://10.135.49.202:1935/playBack/d3b3dcba-905e-8c1f-c03e-430f8df8767f-main/1616382481/1616381276.flv?streamType=1&manufacturer=1&startTime=1616381246&endTime=1616381276");
                        intent.putExtra("PLAYER_URL", videoPath.replace("59.213.220.32:85","112.44.67.43"));
                        intent.putExtra("PLAYER_NAME", currentOneBodyFire.getName());
                        startActivity(intent);
                       /* if (currentOneBodyFire.getUrlType().equals("rtmp")) {
                            Intent intent = new Intent(getContext(), PlayerRtspActivity.class);
                            // intent.putExtra("PLAYER_URL", "rtmp://10.135.49.202:1935/playBack/d3b3dcba-905e-8c1f-c03e-430f8df8767f-main/1616382481/1616381276.flv?streamType=1&manufacturer=1&startTime=1616381246&endTime=1616381276");
                            intent.putExtra("PLAYER_URL", videoPath);
                            intent.putExtra("PLAYER_NAME", currentOneBodyFire.getName());
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(getContext(), PlayerActivity.class);
                            // intent.putExtra("PLAYER_URL", "rtmp://10.135.49.202:1935/playBack/d3b3dcba-905e-8c1f-c03e-430f8df8767f-main/1616382481/1616381276.flv?streamType=1&manufacturer=1&startTime=1616381246&endTime=1616381276");
                            intent.putExtra("PLAYER_URL", videoPath);
                            intent.putExtra("PLAYER_NAME", currentOneBodyFire.getName());
                            startActivity(intent);
                        }*/

                    }
                });
        /**
         * 百度地图点marker的点击事件
         */
                mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Bundle extraInfo = marker.getExtraInfo();
                        String id = extraInfo.getString("id");
                        int type = extraInfo.getInt("type", 0);
                Log.e(TAG, "onMarkerClick:id " + id);
                Log.e(TAG, "onMarkerClick:getId " + marker.getId());
                Log.e(TAG, "onMarkerClick:getTitle " + marker.getTitle());
                Log.e(TAG, "onMarkerClick:latitude " + marker.getPosition().latitude);
                Log.e(TAG, "onMarkerClick:latitudeE6 " + marker.getPosition().latitudeE6);
                Log.e(TAG, "onMarkerClick:longitude " + marker.getPosition().longitude);
                Log.e(TAG, "onMarkerClick:longitudeE6 " + marker.getPosition().longitudeE6);
                if (type == ONE_BODY) {      //一体机火警点击
                    for (int i = 0; i < oneBodyFireFenleiList.size(); i++) {
                        if (oneBodyFireFenleiList.get(i).getId().equals(id)) {
                            currentOneBodyFire = oneBodyFireFenleiList.get(i);
                        }
                    }
                    onebodyListDialog.dismiss();
                    //飞到一体机精确点上
                    initOneBodyFlyBaiduMap();
                    //加载一体机详细数据
                    initOneBodyFireModelData();
                } else if (type == RESOURCE_MONITOR) {

                    com.baidu.mapapi.model.LatLng ll = new com.baidu.mapapi.model.LatLng(
                            marker.getPosition().latitude, marker.getPosition().longitude);
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(13);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                    getinfofromid(id);

                } else if (type == WEI_XING) {
                    for (int i = 0; i < weixingModelList.size(); i++) {
                        if (weixingModelList.get(i).getId().equals(id)) {
                            currentWeixingModel = weixingModelList.get(i);
                        }
                    }

                    //飞到精确点上
                    flyBaiduMap(Double.parseDouble(currentWeixingModel.getLatitude()), Double.parseDouble(currentWeixingModel.getLongitude()));
                    //加载卫星详细数据
                    initWeixinModelData();
                }

                return false;
            }
        });
        /**
         * 导航
         */
        RxViewAction.clickNoDouble(daohangLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
   /*  //构建导航组件配置类，没有传入起点，所以起点默认为 “我的位置”
                        AmapNaviParams params = new AmapNaviParams(null, null, null, AmapNaviType.DRIVER, AmapPageType.ROUTE);
//启动导航组件
                        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, null);*/
                        String jingweiStr = getLocation();
                        String starweidu = "";
                        String starjingdu = "";
                        if (!jingweiStr.isEmpty()) {
                            List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
                            starweidu = jingweiList.get(1);
                            starjingdu = jingweiList.get(0);
                        }

                        LatLng latLng = new LatLngChange().transformFromWGSToGCJ(new LatLng(currentOneBodyFire.getAlarmLatitude(), currentOneBodyFire.getAlarmLongitude()));

                        Log.e(TAG, "call: starweidu=" + starweidu);
                        Log.e(TAG, "call:starjingdu= " + starjingdu);

                        Poi start = new Poi("", new com.amap.api.maps.model.LatLng(Double.parseDouble(starweidu), Double.parseDouble(starjingdu)), "");
                        Poi end = new Poi(currentOneBodyFire.getName(), new com.amap.api.maps.model.LatLng(latLng.latitude, latLng.longitude), "");
                        AmapNaviParams params = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.ROUTE);
                        params.setUseInnerVoice(true);
                        Log.e(TAG, "call: start" + starweidu);
                        Log.e(TAG, "call: end" + latLng.latitude);
                        AmapNaviPage.getInstance().showRouteActivity(getContext(), params, MapNewFragment.this);
                    }
                });
        /**
         * 视频分类点击
         */
        RxViewAction.clickNoDouble(shipinFenleiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showOneBodyShipinFenleiDailog();
                    }
                });
        /**
         * 分类点击
         */
        RxViewAction.clickNoDouble(oneBodyFenleiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showOneBodyFenleiChangeDailog();
                    }
                });
        /**
         * 真实火警点击
         */
        RxViewAction.clickNoDouble(zhenshiButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isReleas = 1;
                        postIsReleaseFireToService();
                    }
                });
        /**
         * 误报火警点击
         */
        RxViewAction.clickNoDouble(wubaoButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isReleas = 0;
                        postIsReleaseFireToService();
                    }
                });
        /**
         * 时间点击
         */
        RxViewAction.clickNoDouble(gaojiStartimeText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isChooseStarTime = true;
                        showDataDialog();
                    }
                });

        RxViewAction.clickNoDouble(gaojiEndTimeText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isChooseStarTime = false;
                        showDataDialog();
                    }
                });

        /**
         * 一体机火点图片1点击
         */
        RxViewAction.clickNoDouble(yitijiOneView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
/*                      Log.e(TAG, "call: getPicPath1==" + currentOneBodyFire.getPicPath1() );
                        Log.e(TAG, "call: getPicPath1==" + currentOneBodyFire.getPicPath1());
                        ArrayList<String> picList = new ArrayList<>();
                        picList.add(currentOneBodyFire.getPicPath1());
                       ImagPagerUtil imagPagerUtil = new ImagPagerUtil(getActivity(), picList);
                        imagPagerUtil.setContentText("");
                        imagPagerUtil.show();*/
                        if (currentOneBodyFire.getPicPath1()!=null){
                            Intent intent = new Intent(getActivity(), PicActivity.class);
                            intent.putExtra("pic",currentOneBodyFire.getPicPath1().replace("59.213.220.32:85","112.44.67.43"));
                            startActivity(intent);
                        }else {
                            Toast.makeText(getContext(), "暂无图片", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        /**
         * 一体机火点图片2 点击
         */
        RxViewAction.clickNoDouble(yitijiTwoView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: getPicPath2==" + currentOneBodyFire.getPicPath2());
                        ArrayList<String> picList = new ArrayList<>();
                        picList.add(currentOneBodyFire.getPicPath2());
//                        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(getActivity(), picList);
//                        imagPagerUtil.setContentText("");
//                        imagPagerUtil.show();

                        if (currentOneBodyFire.getPicPath2()!=null){
                            Intent intent = new Intent(getActivity(), PicActivity.class);
                            intent.putExtra("pic",currentOneBodyFire.getPicPath2().replace("59.213.220.32:85","112.44.67.43"));
                            startActivity(intent);
                        }else {
                            Toast.makeText(getContext(), "暂无图片", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        /*/
         * 按钮点击
         */
        RxViewAction.clickNoDouble(findButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isGaojiFind = true;
                        //findFirePost();
                        if (gaojiStartimeText.getText().toString().equals("请输入开始时间")) {
                            Toast.makeText(getContext(), "请选择开始时间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        gaojiDialog.dismiss();
                        getWeixingDataFromSetvice();
                    }
                });
        RxViewAction.clickNoDouble(chongzhiButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //时间初始化
                        gaojiStartimeText.setText("请输入开始时间");
                        gaojiEndTimeText.setText("请输入结束时间");
                    }
                });
        /**
         *  卫星火点图片1点击
         */
        RxViewAction.clickNoDouble(huodianOneImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ArrayList<String> picList = new ArrayList<>();
                        picList.add("http://web.ehaohai.com:2018" + currentWeixingModel.getLightImageAddress());
                        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(getActivity(), picList);
                        imagPagerUtil.setContentText("");
                        imagPagerUtil.show();
                    }
                });
        /**
         *  卫星火点图片2点击
         */
        RxViewAction.clickNoDouble(huodianTwoImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ArrayList<String> picList = new ArrayList<>();
                        picList.add("http://web.ehaohai.com:2018" + currentWeixingModel.getIrImageAddress());
                        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(getActivity(), picList);
                        imagPagerUtil.setContentText("");
                        imagPagerUtil.show();
                    }
                });
        //卫星列表数据  分类点击
        RxViewAction.clickNoDouble(fenleiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showLeibieChangeDailog();
                    }
                });


        // 顶部listdialog弹出的点击事件
        RxViewAction.clickNoDouble(mapButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mapChooseDialog.show();
                    }
                });
        //切换谷歌高程地图
        RxViewAction.clickNoDouble(gugeGaochengMap)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        gugeGaochengImage.setImageResource(R.drawable.ic_google_gc_select);
//                        gugeGaochengText.setTextColor(getResources().getColor(R.color.theme_primary));

                        gugeYingxiangImage.setImageResource(R.drawable.ic_google_sl);
                        gugeYingxiangText.setTextColor(getResources().getColor(R.color.c7));

                        tiandiShiliangImage.setImageResource(R.drawable.ic_tdt_sl);
                        tiandiShiliangText.setTextColor(getResources().getColor(R.color.c7));

                        tiandiYingxiangImage.setImageResource(R.drawable.ic_tdt_yx);
                        tiandiYingxiangText.setTextColor(getResources().getColor(R.color.c7));

                        /*dWebView.callHandler("google_gaocheng", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                mapChooseDialog.hide();
                            }
                        });*/
                    }
                });
        //本地地图
        RxViewAction.clickNoDouble(gugeYingxiangMap)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        gugeGaochengImage.setImageResource(R.drawable.ic_google_gc);
//                        gugeGaochengText.setTextColor(getResources().getColor(R.color.c7));

                        gugeYingxiangImage.setImageResource(R.drawable.ic_google_sl_select);
                        gugeYingxiangText.setTextColor(getResources().getColor(R.color.map));

                        tiandiShiliangImage.setImageResource(R.drawable.ic_tdt_sl);
                        tiandiShiliangText.setTextColor(getResources().getColor(R.color.c12));

                        tiandiYingxiangImage.setImageResource(R.drawable.ic_tdt_yx);
                        tiandiYingxiangText.setTextColor(getResources().getColor(R.color.c12));

                        /*dWebView.callHandler("google_yingxiang", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "onValue:  fanhiu2" + retValue);
                                mapChooseDialog.hide();
                            }
                        });*/

                    }
                });
        //天地图矢量地图
        RxViewAction.clickNoDouble(tiandiShiliangMap)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                                gugeGaochengImage.setImageResource(R.drawable.ic_google_gc);
//                                gugeGaochengText.setTextColor(getResources().getColor(R.color.c7));

                        gugeYingxiangImage.setImageResource(R.drawable.ic_google_sl);
                        gugeYingxiangText.setTextColor(getResources().getColor(R.color.c12));

                        tiandiShiliangImage.setImageResource(R.drawable.ic_tdt_sl_select);
                        tiandiShiliangText.setTextColor(getResources().getColor(R.color.map));

                        tiandiYingxiangImage.setImageResource(R.drawable.ic_tdt_yx);
                        tiandiYingxiangText.setTextColor(getResources().getColor(R.color.c12));
                        /*dWebView.callHandler("tianditu_shiliang", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "onValue:  fanhiu3" + retValue);
                                mapChooseDialog.hide();
                            }
                        });*/
                    }
                });
        //天地图影像地图  天地图中文标记
        RxViewAction.clickNoDouble(tiandiYingxiangMap)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

//                        gugeGaochengImage.setImageResource(R.drawable.ic_google_gc);
//                        gugeGaochengText.setTextColor(getResources().getColor(R.color.c7));

                        gugeYingxiangImage.setImageResource(R.drawable.ic_google_sl);
                        gugeYingxiangText.setTextColor(getResources().getColor(R.color.c12));

                        tiandiShiliangImage.setImageResource(R.drawable.ic_tdt_sl);
                        tiandiShiliangText.setTextColor(getResources().getColor(R.color.c12));

                        tiandiYingxiangImage.setImageResource(R.drawable.ic_tdt_yx_select);
                        tiandiYingxiangText.setTextColor(getResources().getColor(R.color.map));

                   /*     dWebView.callHandler("tianditu_yingxiang", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.e(TAG, "onValue:  fanhiu4" + retValue);
                                mapChooseDialog.hide();
                            }
                        });*/
                    }
                });
        /*卫星按钮点击*/
        //控制卫星按钮布局显示隐藏
        RxViewAction.clickNoDouble(weixingButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isWeixingButtonShow) {
                            isWeixingButtonShow = false;
                            weixingButtonLayout.setVisibility(View.GONE);
                        } else {
                            isWeixingButtonShow = true;
                            weixingButtonLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });

        //卫星设置点击
        RxViewAction.clickNoDouble(weixingShezhiView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), WeixingActivity.class));
                    }
                });
        //卫星查询点击
        RxViewAction.clickNoDouble(weixingChaxunView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        searchDialog.show();
                    }
                });
        //卫星列表点击
        RxViewAction.clickNoDouble(weixingLiebiaoView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        fireInfoListDialog.show();
                    }
                });
        //当前时间点击  1
        RxViewAction.clickNoDouble(currntTimeLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        weixingShijianView.setText("当前时间");
                        searchDialog.hide();
                        currentFireFindTime = 1;
                        isShowSearchDialog = true;
                        isTuisong = false;
                        isGaojiFind = false;
                        getWeixingDataFromSetvice();
                    }
                });

        //一小时内点击 1
        RxViewAction.clickNoDouble(oneHoursLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        weixingShijianView.setText("1小时内");
                        searchDialog.hide();
                        currentFireFindTime = 1;
                        isShowSearchDialog = true;
                        isTuisong = false;
                        isGaojiFind = false;
                        getWeixingDataFromSetvice();

                    }
                });

        //三小时内点击  3
        RxViewAction.clickNoDouble(threeHoursLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        weixingShijianView.setText("3小时内");
                        searchDialog.hide();
                        currentFireFindTime = 3;
                        isShowSearchDialog = true;
                        isTuisong = false;
                        isGaojiFind = false;
                        getWeixingDataFromSetvice();

                    }
                });

        //一天内点击  24
        RxViewAction.clickNoDouble(oneDayLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        weixingShijianView.setText("1天内");
                        searchDialog.hide();
                        currentFireFindTime = 24;
                        isShowSearchDialog = true;
                        isTuisong = false;
                        isGaojiFind = false;
                        getWeixingDataFromSetvice();

                    }
                });

        //三天内点击  72
        RxViewAction.clickNoDouble(threeDayLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        weixingShijianView.setText("3天内");
                        searchDialog.hide();
                        currentFireFindTime = 72;
                        isShowSearchDialog = true;
                        isTuisong = false;
                        isGaojiFind = false;
                        getWeixingDataFromSetvice();

                    }
                });

        //五天内点击 120
        RxViewAction.clickNoDouble(fiveDayLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        weixingShijianView.setText("5天内");
                        searchDialog.hide();
                        currentFireFindTime = 120;
                        isShowSearchDialog = true;
                        isTuisong = false;
                        isGaojiFind = false;
                        getWeixingDataFromSetvice();

                    }
                });

        //高级点击
        RxViewAction.clickNoDouble(gaojiSearchLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        weixingShijianView.setText("查询时间内");
                        searchDialog.hide();
                        isShowSearchDialog = true;
                        isTuisong = false;
                        gaojiDialog.show();

                    }
                });
        /*资源类型点击*/
        RxViewAction.clickNoDouble(retypeView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // startActivity(new Intent(getContext(), RecyclerViewActivity.class));
                        resourceListDialog.show();
                    }
                });
        RxViewAction.clickNoDouble(ziyuanButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isZiyuanButtonShow) {
                            isZiyuanButtonShow = false;
                            ziyuanButtonLayout.setVisibility(View.GONE);
                        } else {
                            isZiyuanButtonShow = true;
                            ziyuanButtonLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
        /*一体机点击*/
        RxViewAction.clickNoDouble(onebodyButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isonebodyButtonShow) {
                            isonebodyButtonShow = false;
                            onebodyButtonLayout.setVisibility(View.GONE);
                        } else {
                            isonebodyButtonShow = true;
                            onebodyButtonLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
        RxViewAction.clickNoDouble(onebody_view)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // removeMarkerBaiduMap();
                        initOneBodyFireData();
                        onebodyListDialog.show();
                    }
                });
        RxViewAction.clickNoDouble(kejianguangbutton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String urlcode = kejianguangUrl;
                        if (urlcode.equals("")) {
                            Toast.makeText(getContext(), "暂无可见光摄像头", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra("id", urlcode);
                        intent.putExtra("monitorId", kejianguangMId);
                        intent.setAction("video_play");
                        getContext().sendBroadcast(intent);
                        resourceinfoDialog.dismiss();
                    }
                });
        RxViewAction.clickNoDouble(rechengxiangbutton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String urlcode = rechengxiangUrl;
                        if (urlcode.equals("")) {
                            Toast.makeText(getContext(), "暂无热成像摄像头", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra("id", urlcode);
                        intent.putExtra("monitorId", rechengxiangMId);
                        intent.setAction("video_play");
                        getContext().sendBroadcast(intent);
                        resourceinfoDialog.dismiss();
                    }
                });
        RxViewAction.clickNoDouble(ddrw_view)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ARouter.getInstance().build(RouteUtils.FireMissionList)
                                .withString("token", new DbConfig(getContext()).getUser().getToken())
                                .navigation();
                    }
                });
        /*RxViewAction.clickNoDouble(shaixuanbutton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });*/
    }

    private void initOneBodyFlyBaiduMap() {
        double[] position = LatLngChangeNew.calWGS84toBD09(currentOneBodyFire.getAlarmLatitude(), currentOneBodyFire.getAlarmLongitude());
        com.baidu.mapapi.model.LatLng ll = new com.baidu.mapapi.model.LatLng(
                position[0], position[1]);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(13);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private void showOneBodyShipinFenleiDailog() {
        //默认选中第一个  //0疑似火情  1是真实火情  2是未处理 3是全部
       // final String[] items = {"全部", "森林防火", "砂石盗采", "海域监控"};
        final String[] items = {"全部", "森林防火", "砂石盗采"};

        builder = new AlertDialog.Builder(getContext()).setIcon(R.mipmap.ic_launcher).setTitle("事件分类")
                .setSingleChoiceItems(items, shipinChoose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        shipinChoose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: choose1=" + choose1);
                        oneBodyFireFenleiList.clear();
                        //getType 2：森林防火4：海域监控5：砂石盗采 0全部
                        //  isReleasList = 3;  //0疑似火情  1是真实火情  2是未处理 3是全部

                        if (shipinChoose1 == 1) {
                            shipinState = 2;
                            isShowSearchDialog = true;
                            shipinFenleiTextView.setText("森林防火");
                            getonebodyDataFromSetvice();
                          /* shipinFenleiTextView.setText("森林防火");
                           for (int j = 0; j < oneBodyFireList.size(); j++) {
                                if (oneBodyFireList.get(j).getIsReal()!=null) {
                                   if (isReleasList == 3){  //3全部
                                       if (oneBodyFireList.get(j).getType() == 2) {
                                           oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                       }
                                   }else if (isReleasList == 2){     //12 未处理状态是null
                                        if (oneBodyFireList.get(j).getIsReal()== null && oneBodyFireList.get(j).getType() == 2) {
                                            oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                        }
                                    }else {  //1真实
                                        if (oneBodyFireList.get(j).getIsReal()== 1 && oneBodyFireList.get(j).getType() == 2) {
                                            oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                        }
                                    }


                                }
                           }
                            //   currentPage = 1;
                            removeMarkerBaiduMap();*/
                        } else if (shipinChoose1 == 2) {
                            shipinState = 5;
                            isShowSearchDialog = true;
                            shipinFenleiTextView.setText("砂石盗采");
                            getonebodyDataFromSetvice();
                          /* shipinFenleiTextView.setText("砂石盗采");
                            for (int j = 0; j < oneBodyFireList.size(); j++) {
                                if (isReleasList == 3){  //3全部
                                    if (oneBodyFireList.get(j).getType() == 4) {
                                        oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                    }
                                } else if (isReleasList == 2){ //12 未处理状态是null
                                    if (oneBodyFireList.get(j).getIsReal()== null && oneBodyFireList.get(j).getType() == 4) {
                                        oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                    }
                                }else {
                                    if (oneBodyFireList.get(j).getIsReal()== isReleasList && oneBodyFireList.get(j).getType() == 4) {
                                        oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                    }
                                }

                            }
                            //   currentPage = 1;
                            removeMarkerBaiduMap();*/
                        } else if (shipinChoose1 == 3) {
                            shipinState = 4;
                            isShowSearchDialog = true;
                            shipinFenleiTextView.setText("海域监控");
                            getonebodyDataFromSetvice();
                         /*  shipinFenleiTextView.setText("海域监控");
                           for (int j = 0; j < oneBodyFireList.size(); j++) {
                               if (isReleasList == 3){  //3全部
                                   if (oneBodyFireList.get(j).getType() == 5) {
                                       oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                   }
                               } else if (isReleasList == 2){ //12 未处理状态是null
                                   if (oneBodyFireList.get(j).getIsReal()==null && oneBodyFireList.get(j).getType() == 5) {
                                       oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                   }
                               }else {
                                   if (oneBodyFireList.get(j).getIsReal()==isReleasList && oneBodyFireList.get(j).getType() == 5) {
                                       oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                   }
                               }

                           }
                           //   currentPage = 1;
                           removeMarkerBaiduMap();*/
                        } else {
                            shipinState = 0;
                            isShowSearchDialog = true;
                            shipinFenleiTextView.setText("全部");
                            getonebodyDataFromSetvice();
                         /*  shipinFenleiTextView.setText("全部");
                           for (int j = 0; j < oneBodyFireList.size(); j++) {
                               if (isReleasList == 3){  //3全部
                                   oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                               } else if (isReleasList == 2){ //12 未处理状态是null
                                   if (oneBodyFireList.get(j).getIsReal()==null) {
                                       oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                   }
                               }else {
                                   if (oneBodyFireList.get(j).getIsReal()==isReleasList) {
                                       oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                   }
                               }
                           }

                            removeMarkerBaiduMap();*/
                        }
                        //initWeixingData();
                    }
                });
        builder.create().show();
    }

    private void showOneBodyFenleiChangeDailog() {
        //默认选中第一个  //0疑似火情  1是真实火情  2是未处理 3是全部
        final String[] items = {"全部", "未处理", "已处理"};
        isReleasList = 3;
        builder = new AlertDialog.Builder(getContext()).setIcon(R.mipmap.ic_launcher).setTitle("处置情况")
                .setSingleChoiceItems(items, choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        choose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: choose1=" + choose1);
                        oneBodyFireFenleiList.clear();
                        /*if (choose1 == 3) {
                            isReleasList = 0;
                            fenleiTextView.setText("疑似火点");
                            for (int j = 0; j < oneBodyFireList.size(); j++) {
                                if (oneBodyFireList.get(j).getIsReal()!=null) {
                                    if (oneBodyFireList.get(j).getIsReal()== 0) {
                                        oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                    }

                                }
                            }
                          //  currentPage = 1;
                            initOneBodyFireData();
                        } else */
                        if (choose1 == 2) {
                            isReleasList = 1;
                            fenleiTextView.setText("已处理");
                            for (int j = 0; j < oneBodyFireList.size(); j++) {
                                if (oneBodyFireList.get(j).getIsReal() != null) {
                                    if (oneBodyFireList.get(j).getIsReal() == 1) {
                                        oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                    }

                                }
                            }
                            //   currentPage = 1;
                            removeMarkerBaiduMap();
                        } else if (choose1 == 1) {
                            isReleasList = 2;
                            fenleiTextView.setText("未处理");
                            for (int j = 0; j < oneBodyFireList.size(); j++) {
                                if (oneBodyFireList.get(j).getIsReal() == null) {
                                    oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                                }
                            }
                            //   currentPage = 1;
                            removeMarkerBaiduMap();
                        } else {
                            isReleasList = 3;
                            fenleiTextView.setText("全部");
                            oneBodyFireFenleiList.addAll(oneBodyFireList);

                            removeMarkerBaiduMap();
                        }
                        //initWeixingData();
                    }
                });
        builder.create().show();
    }

    /**
     * 是否是真实火情数据提交
     */
    private void postIsReleaseFireToService() {

        showDialogProgress(progressDialog, "提交中...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fireId", currentOneBodyFire.getId());
            jsonObject.put("type", isReleas);
            jsonObject.put("isAndroid", 2);
        } catch (JSONException e) {
        }
        RequestParams params = null;
        if (currentOneBodyFire.getType() == 2) {
            params = new RequestParams(RequestUtils.REQUEST_URL + "fire/api/monitorFirealarm/realOrError");
        } else if (currentOneBodyFire.getType() == 4) {
            params = new RequestParams(RequestUtils.REQUEST_URL + "fire/api/StealingFirealarm/realOrError");
        } else if (currentOneBodyFire.getType() == 5) {
            params = new RequestParams(RequestUtils.REQUEST_URL + "fire/api/BuildingFirealarm/realOrError");
        }

        // params.setBodyContent(jsonObject.toString());
        params.addParameter("id", currentOneBodyFire.getId());
        params.addParameter("type", isReleas);
        params.addParameter("isAndroid", 2);
        Log.e(TAG, "getDataFromService: " + jsonObject.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(getContext()).getUser().getToken());
        Log.e(TAG, "resource: --" + params);
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: 真实火点:" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        Toast.makeText(getContext(), "上报成功", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < oneBodyFireList.size(); i++) {
                            if (oneBodyFireList.get(i).getId().equals(currentOneBodyFire.getId())) {
                                oneBodyFireList.get(i).setIsReal(isReleas);
                            }
                        }
                        initOneBodyFireModelData();

                        //处理之后从移除该火情
                        for (int i = 0; i < oneBodyFireFenleiList.size(); i++) {
                            if (oneBodyFireFenleiList.get(i).getId().equals(currentOneBodyFire.getId())) {
                                oneBodyFireFenleiList.remove(i);
                                break;
                            }
                        }
                        if (isReleas == 0) {//0疑似火情  1是真实火情  2是未处理
                            //处理之后从移除该火情

                            for (int i = 0; i < oneBodyFireList.size(); i++) {
                                if (oneBodyFireList.get(i).getId().equals(currentOneBodyFire.getId())) {
                                    oneBodyFireList.remove(i);
                                    break;
                                }
                            }
                        }

                        Message message = mHandler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("id", oneBodyFireFenleiList.get(0).getId());
                        b.putInt("what", DIALOG_ONEBODY_FIRE_SHOW);
                        message.setData(b);
                        mHandler.sendMessage(message);

                    } else {
                        Toast.makeText(getContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时必须调用mMapView. onResume ()
        baiduMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        baiduMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(Receiver);
        getActivity().unregisterReceiver(fireWeixingReceiver);
        baiduMapView.onDestroy();
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        statisticsButton = ((TextView) getView().findViewById(R.id.statistics_button));

        baiduMapView = ((MapView) getView().findViewById(R.id.baidu_mapview));
        mBaiduMap = baiduMapView.getMap();
        //显示卫星图层
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        mBaiduMap.setMyLocationEnabled(true);

        //只显示道路 不显示其他标注
        mBaiduMap.showMapPoi(true);
        //设置最大最小缩放等级
        mBaiduMap.setMaxAndMinZoomLevel(13, 4);

       com.baidu.mapapi.model.LatLng ll = new com.baidu.mapapi.model.LatLng(
                LATITUDE_DEF,LONGTITUDE_DEF);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(13);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        //定位初始化
        mLocationClient = new LocationClient(getContext());

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

        //绘制区域边界
        OnGetDistricSearchResultListener listener = new OnGetDistricSearchResultListener() {

            @Override
            public void onGetDistrictResult(DistrictResult districtResult) {
                districtResult.getCenterPt();//获取行政区中心坐标点
                districtResult.getCityName();//获取行政区域名称
                List<List<com.baidu.mapapi.model.LatLng>> polyLines = districtResult.getPolylines();//获取行政区域边界坐标点
                //边界就是坐标点的集合，在地图上画出来就是多边形图层。有的行政区可能有多个区域，所以会有多个点集合。
                if (polyLines == null) {
                    Log.e(TAG, "onGetDistrictResult: 无数据" );
                    return;
                }
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                /*//原边界(地理编码)
                for (List<com.baidu.mapapi.model.LatLng> polyline : polyLines) {
                    Log.e(TAG, "onGetDistrictResult: bingo polyline.toString() = " +  polyline.toString() );
                    OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                            .points(polyline).dottedLine(true).color(Color.BLUE);
                    mBaiduMap.addOverlay(ooPolyline11);
                    OverlayOptions ooPolygon = new PolygonOptions().points(polyline)
                            .stroke(new Stroke(10, 0xFF0000FF)).fillColor(0x00FFFFFF);
                    mBaiduMap.addOverlay(ooPolygon);
                    for (com.baidu.mapapi.model.LatLng latLng : polyline) {
                        builder.include(latLng);
                    }
                }*/


                /*//自定义边界(json文件)
                try {
                    JSONObject jsonObject = new JSONObject(new GetJsonDataUtil().getJson(getActivity(),"areas.json"));
                    Log.e(TAG, "onGetDistrictResult: bingo jsonObject.toString() = " + jsonObject.toString() );
                    JSONArray features = jsonObject.getJSONArray("features");
                    JSONObject feature = (JSONObject) features.get(0);
                    JSONObject geometry = feature.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");

                    Log.e(TAG, "onGetDistrictResult: bingo coordinates.toString() = " + coordinates.toString() );
                    for (int i = 0; i < coordinates.length(); i++) {
                        Log.e(TAG, "onGetDistrictResult: bingo i = " + i );
                        JSONArray midArray = (JSONArray) coordinates.get(i);
                        Log.e(TAG, "onGetDistrictResult: bingo midArray.toString() = " + midArray.toString() );

                        List<com.baidu.mapapi.model.LatLng> pointList = new ArrayList<>();
                        for (int m = 0; m < midArray.length(); m++) {
                            JSONArray model = (JSONArray) midArray.get(i);
                            Log.e(TAG, "onGetDistrictResult: bingo model.toString() = " + model.toString() );
                            double lat = (double) model.get(1);
                            double lng = (double) model.get(0);
                            com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(lat,lng);
                            pointList.add(latLng);
                        }
                        Log.e(TAG, "onGetDistrictResult: bingo pointList.toString() = " + pointList.toString() );

                        OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                                .points(pointList).dottedLine(true).color(Color.BLUE);
                        mBaiduMap.addOverlay(ooPolyline11);
                        OverlayOptions ooPolygon = new PolygonOptions().points(pointList)
                                .stroke(new Stroke(10, 0xFF0000FF)).fillColor(0x00FFFFFF);
                        mBaiduMap.addOverlay(ooPolygon);
                        for (com.baidu.mapapi.model.LatLng latLng : pointList) {
                            builder.include(latLng);
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onGetDistrictResult: bingo error = " + e );
                }*/


                /*mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        .newLatLngBounds(builder.build()));*/
            }

        };

        mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(listener);//设置回调监听


         initQuyuBianjie();




    /*.
        超图的地图服务
        dWebView = ((DWebView) getView().findViewById(R.id.dwebview));
        dWebView.getSettings().setJavaScriptEnabled(true);

        String url = "file:///android_asset/map/mars_demo.html";
        //url：网页地址；name/pwd:cookie信息
        // String isUser = SPUtils.getInstance().getString("userContent");
        dWebView.loadUrl(url);
        dWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载逃生出口资源点
                // getDataFromService();
                //加载道路
                // getRoadDataFromService();
            }
        });


        dWebView.addJavascriptInterface(this, "jk");
        JsApi jsApi = new JsApi(getContext());
        jsApi.setListener(this);
        dWebView.addJavascriptObject(jsApi, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dWebView.setWebContentsDebuggingEnabled(true);
        }
        //添加跨域支持
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            dWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            dWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        } else {
            try {
                Class<?> clazz = dWebView.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(dWebView.getSettings(), true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // String mapUrl = "http://www.google.cn/maps/vt?lyrs=y@189&gl=cn&x={x}&y={y}&z={z}http://www.google.cn/maps/vt?lyrs=y@189&gl=cn&x={x}&y={y}&z={z}"; // 瓦片路径
        String mapUrl = "assets/qds_2005261619/{z}/{x}/{y}.png"; // 瓦片路径
        dWebView.callHandler("initMap", new Object[]{mapUrl}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.d("jsbridge", "call succeed,return value is " + retValue);
            }
        });*/

        gaojiFindDialog = new ProgressDialog(getContext());
        date = new StringBuffer();
        endDate = new StringBuffer();
         /*资源类型*/
        retypeView = ((TextView) getView().findViewById(R.id.retype_view));
        ziyuanLayout = ((LinearLayout) getView().findViewById(R.id.ziyuan_layout));
        ziyuanButton = ((TextView) getView().findViewById(R.id.ziyuan_button));
        onebodyButtonLayout = ((LinearLayout) getView().findViewById(R.id.onebody_button_layout));
        if (isonebodyButtonShow) {
            onebodyButtonLayout.setVisibility(View.VISIBLE);
        } else {
            onebodyButtonLayout.setVisibility(View.VISIBLE);
        }
        ziyuanButtonLayout = ((LinearLayout) getView().findViewById(R.id.ziyuan_button_layout));
        if (isZiyuanButtonShow) {
            ziyuanButtonLayout.setVisibility(View.VISIBLE);
        } else {
            ziyuanButtonLayout.setVisibility(View.VISIBLE);
        }
        /*一体机*/
        onebody_view = ((TextView) getView().findViewById(R.id.onebody_view));
        /*调度任务*/
        ddrw_view = ((TextView) getView().findViewById(R.id.ddrw_button));
        /*卫星布局加载*/
        weixingButton = ((TextView) getView().findViewById(R.id.weixing_button));
        weixingButtonLayout = ((LinearLayout) getView().findViewById(R.id.weixing_button_layout));
        weixinLayout = ((LinearLayout) getView().findViewById(R.id.weixing_layout));
        weixingShezhiView = ((TextView) getView().findViewById(R.id.weixing_shezhi_view));
        weixingChaxunView = ((TextView) getView().findViewById(R.id.weixing_chaxun_ciew));
        weixingLiebiaoView = ((TextView) getView().findViewById(R.id.weixing_list_view));

        if (isWeixingButtonShow) {
            weixingButtonLayout.setVisibility(View.VISIBLE);
        } else {
            weixingButtonLayout.setVisibility(View.GONE);
        }
        /**
         * 卫星查询信息 dialog
         */
        searchDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        searchInflater = LayoutInflater.from(getContext()).inflate(R.layout.dialog_search_weixing, null);
        searchInflater.setMinimumWidth(10000);
        oneHoursLayout = ((LinearLayout) searchInflater.findViewById(R.id.one_hours));
        currntTimeLayout = ((LinearLayout) searchInflater.findViewById(R.id.current_time));
        threeHoursLayout = ((LinearLayout) searchInflater.findViewById(R.id.three_hours));
        oneDayLayout = ((LinearLayout) searchInflater.findViewById(R.id.one_day));
        threeDayLayout = ((LinearLayout) searchInflater.findViewById(R.id.three_day));
        fiveDayLayout = ((LinearLayout) searchInflater.findViewById(R.id.five_day));
        gaojiSearchLayout = ((LinearLayout) searchInflater.findViewById(R.id.gaoji_search));
        searchDialog.setContentView(searchInflater);
        Window searchDialogWindow = searchDialog.getWindow();
        searchDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpSearch = searchDialogWindow.getAttributes();
        searchDialogWindow.setAttributes(lpSearch);
        searchDialog.setCanceledOnTouchOutside(true);
        /**
         * 火警信息 dialog
         */
        fireInfoListDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        fireInfoListInflater = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fireinfo_list, null);
        fireInfoListInflater.setMinimumWidth(10000);
        fireCountText = ((TextView) fireInfoListInflater.findViewById(R.id.fire_count_textview));
        weixingListView = ((RecyclerView) fireInfoListInflater.findViewById(R.id.fire_info_listview));
        fenleiLayout = ((LinearLayout) fireInfoListInflater.findViewById(R.id.fenlei_layout));
        fenleiView = ((TextView) fireInfoListInflater.findViewById(R.id.fenlei_View));
        weixingShijianView = ((TextView) fireInfoListInflater.findViewById(R.id.weixing_shijian_view));
        weixingShijianView.setText("3小时内");
        fireInfoListDialog.setContentView(fireInfoListInflater);
        Window fireListWindow = fireInfoListDialog.getWindow();
        fireListWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams fireListLp = fireListWindow.getAttributes();

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        fireListLp.height = (int) (height * 0.8);
        fireListWindow.setAttributes(fireListLp);
        fireInfoListDialog.setCanceledOnTouchOutside(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        weixingListView.setLayoutManager(linearLayoutManager);
        weixingAdapter = new MultiTypeAdapter(weixingItems);
        register();
        weixingListView.setAdapter(weixingAdapter);
        assertHasTheSameAdapter(weixingListView, weixingAdapter);

        /**
         * 火点信息dialog
         */
        weixingFireDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        weixingFireInflater = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fire_weixing, null);
        weixingFireInflater.setMinimumWidth(10000);
        fireAddressText = ((TextView) weixingFireInflater.findViewById(R.id.address_text));
        fireTimeText = ((TextView) weixingFireInflater.findViewById(R.id.time_text));
        jingWeiText = ((TextView) weixingFireInflater.findViewById(R.id.jing_wei_text));
        kexinText = ((TextView) weixingFireInflater.findViewById(R.id.kexin_text));
        mianjiText = ((TextView) weixingFireInflater.findViewById(R.id.mianji_text));
        cishuText = ((TextView) weixingFireInflater.findViewById(R.id.cishu_text));
        leixingText = ((TextView) weixingFireInflater.findViewById(R.id.leixing_text));
        shujuyuanText = ((TextView) weixingFireInflater.findViewById(R.id.shujuyuan_text));
        huodianCodeText = ((TextView) weixingFireInflater.findViewById(R.id.huodian_code_text));
        fankuiText = ((TextView) weixingFireInflater.findViewById(R.id.fankui_text));
        huodianOneImage = ((ImageView) weixingFireInflater.findViewById(R.id.huodian_image_one));
        huodianTwoImage = ((ImageView) weixingFireInflater.findViewById(R.id.huodian_image_two));
        xiangyuanmianjiView = ((TextView) weixingFireInflater.findViewById(R.id.xiangyuanmianji_view));
        xiangyuanshuView = ((TextView) weixingFireInflater.findViewById(R.id.xiangyuanshu_view));
        weixingFireDialog.setContentView(weixingFireInflater);
        Window fireDialogWindow = weixingFireDialog.getWindow();
        fireDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpFire = fireDialogWindow.getAttributes();
        fireDialogWindow.setAttributes(lpFire);
        weixingFireDialog.setCanceledOnTouchOutside(true);

        /**
         * 一体机 dialog
         */
        onebodyListDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        onebodyListInflater = LayoutInflater.from(getContext()).inflate(R.layout.dialog_onebody_list, null);
        onebodyListInflater.setMinimumWidth(10000);
        onebodyListView = ((RecyclerView) onebodyListInflater.findViewById(R.id.onebody_info_listview));
        oneBodyRefreshLayout = ((SwipeRefreshLayout) onebodyListInflater.findViewById(R.id.onebody_refresh_layout));
        //shaixuanbutton=((TextView) onebodyListInflater.findViewById(R.id.shaixuan));
        onebodyListDialog.setContentView(onebodyListInflater);
        onebodyButton = ((TextView) getView().findViewById(R.id.onebody_button));
        fenleiTextView = ((TextView) onebodyListInflater.findViewById(R.id.fenlei_view));
        oneBodyFenleiLayout = ((LinearLayout) onebodyListInflater.findViewById(R.id.fenlei_layout));
        shipinFenleiTextView = ((TextView) onebodyListInflater.findViewById(R.id.shipin_fenlei_view));
        shipinFenleiLayout = ((LinearLayout) onebodyListInflater.findViewById(R.id.shipin_fenlei_layout));
        if (user.getImToken().equals("0")) {
            shipinFenleiLayout.setVisibility(View.VISIBLE);
        } else {
            shipinFenleiLayout.setVisibility(View.GONE);
        }
        Window onebodyListWindow = onebodyListDialog.getWindow();
        onebodyListWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams onebodyListLp = onebodyListWindow.getAttributes();

        WindowManager obwm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int onebodyheight = obwm.getDefaultDisplay().getHeight();
        onebodyListLp.height = (int) (onebodyheight * 0.8);
        onebodyListWindow.setAttributes(onebodyListLp);
        onebodyListDialog.setCanceledOnTouchOutside(true);

        LinearLayoutManager onebodylinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        onebodyListView.setLayoutManager(onebodylinearLayoutManager);
        onebodyAdapter = new MultiTypeAdapter(onebodyItems);

        OneBodyFireViewBinder oneBodyFireViewBinder = new OneBodyFireViewBinder();
        oneBodyFireViewBinder.setListener(this);
        onebodyAdapter.register(OneBodyFire.class, oneBodyFireViewBinder);
        onebodyAdapter.register(Empty.class, new EmptyViewBinder());
        onebodyListView.setAdapter(onebodyAdapter);
        assertHasTheSameAdapter(onebodyListView, onebodyAdapter);
        //下拉刷新
        oneBodyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isOneBodyShuaxin = true;
                choose1 = 1;
                getonebodyDataFromSetvice();

            }
        });
        //加载更多
        onebodyListView.setOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e(TAG, "onLoadMore: lastPage-" + lastPage);
                Log.e(TAG, "onLoadMore: currentPage- " + currentPage);
           /*     if (lastPage > currentPage){
                    currentPage += 1;
                    Log.i(TAG, "onLoadMore: "+currentPage);
                    progressDialog.dismiss();
                     isShowSearchDialog = true;
                getonebodyDataFromSetvice();
                }*/

            }
        });
        /**
         *  一体机火点详细信息
         */
        oneBodyFireDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        oneBodyFireInflater = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fire_onebody, null);
        oneBodyFireInflater.setMinimumWidth(10000);
        mingchengView = ((TextView) oneBodyFireInflater.findViewById(R.id.mingcheng_view));
        dizhiView = ((TextView) oneBodyFireInflater.findViewById(R.id.dizhi_view));
        shijianView = ((TextView) oneBodyFireInflater.findViewById(R.id.shijian_view));
        jingweiduView = ((TextView) oneBodyFireInflater.findViewById(R.id.jingweidu_view));
        yitijiOneView = ((ImageView) oneBodyFireInflater.findViewById(R.id.yitiji_one_image));
        yitijiTwoView = ((ImageView) oneBodyFireInflater.findViewById(R.id.yitiji_two_image));
        zhenshiLayout = ((LinearLayout) oneBodyFireInflater.findViewById(R.id.zhenshi_layout));
        zhenshiButton = ((TextView) oneBodyFireInflater.findViewById(R.id.zhenshi_button));
        wubaoButton = ((TextView) oneBodyFireInflater.findViewById(R.id.wubao_button));
        zhenshiTextView = ((TextView) oneBodyFireInflater.findViewById(R.id.zhenshi_text_view));
        daohangLayout = ((LinearLayout) oneBodyFireInflater.findViewById(R.id.daohang_layout));
        oneBodyKJGButton = ((TextView) oneBodyFireInflater.findViewById(R.id.kejianguang_button));
        oneBodyRCXButton = ((TextView) oneBodyFireInflater.findViewById(R.id.rechengxiang_button));
        oneBodyShipinLayout = ((LinearLayout) oneBodyFireInflater.findViewById(R.id.onebody_shipin_layout));


        oneBodyFireDialog.setContentView(oneBodyFireInflater);
        Window oneBodyfireDialogWindow = oneBodyFireDialog.getWindow();
        oneBodyfireDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams oneBodyLpFire = oneBodyfireDialogWindow.getAttributes();
        oneBodyfireDialogWindow.setAttributes(oneBodyLpFire);
        oneBodyFireDialog.setCanceledOnTouchOutside(true);


        /**
         * 资源点列表 dialog
         */
        resourceListDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        resourceListInflater = LayoutInflater.from(getContext()).inflate(R.layout.dialog_resource_list, null);
        resourceListInflater.setMinimumWidth(10000);
        resourceListView = ((RecyclerView) resourceListInflater.findViewById(R.id.resource_listview));
        resourceListDialog.setContentView(resourceListInflater);
        Window resourceListWindow = resourceListDialog.getWindow();
        resourceListWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams resourceListLp = resourceListWindow.getAttributes();

        WindowManager resourcewm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int resourceheight = resourcewm.getDefaultDisplay().getHeight();
        resourceListLp.height = (int) (resourceheight * 0.8);
        resourceListWindow.setAttributes(fireListLp);
        resourceListDialog.setCanceledOnTouchOutside(true);

        LinearLayoutManager resourcelinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        resourceListView.setLayoutManager(resourcelinearLayoutManager);
        resourceAdapter = new MultiTypeAdapter(resourceItems);


        ResourceListViewBinder resourceListViewBinder = new ResourceListViewBinder();
        resourceListViewBinder.setListener(this);
        resourceAdapter.register(ResourceList.class, resourceListViewBinder);

        resourceListView.setAdapter(resourceAdapter);
        assertHasTheSameAdapter(resourceListView, resourceAdapter);

        /**
         *  资源点详细信息
         */
        resourceinfoDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        resourceInflater = LayoutInflater.from(getContext()).inflate(R.layout.dialog_resource_info, null);
        resourceInflater.setMinimumWidth(10000);
        resourcenameview = ((TextView) resourceInflater.findViewById(R.id.resourcename_view));
        resourceTypeView = ((TextView) resourceInflater.findViewById(R.id.resourcetype_view));
        resoucedizhiview = ((TextView) resourceInflater.findViewById(R.id.resoucedizhi_view));
        resourcejingweiduview = ((TextView) resourceInflater.findViewById(R.id.resourcejingweidu_view));
        kejianguangbutton = ((Button) resourceInflater.findViewById(R.id.kejianguang_button));
        rechengxiangbutton = ((Button) resourceInflater.findViewById(R.id.rechengxiang_button));

        resourceinfoDialog.setContentView(resourceInflater);
        Window resourceDialogWindow = resourceinfoDialog.getWindow();
        resourceDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams resourceLpFire = resourceDialogWindow.getAttributes();
        resourceDialogWindow.setAttributes(resourceLpFire);
        resourceinfoDialog.setCanceledOnTouchOutside(true);
        /** 高级查询
         */
        gaojiDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        gaojiInflater = LayoutInflater.from(getContext()).inflate(R.layout.dialog_gaoji, null);
        gaojiInflater.setMinimumWidth(10000);
        gaojiDialog.setContentView(gaojiInflater);
        Window gaojiDialogWindow = gaojiDialog.getWindow();
        gaojiDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams gaojiLp = gaojiDialogWindow.getAttributes();

        WindowManager wmGaoji = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int heightGaoji = wmGaoji.getDefaultDisplay().getHeight();
        gaojiLp.height = (int) (heightGaoji * 0.8);
        gaojiDialogWindow.setAttributes(gaojiLp);
        gaojiDialog.setCanceledOnTouchOutside(true);
        //时间
        gaojiStarTimeLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.gaoji_startime_layout));
        gaojiEndTimeLayout = ((LinearLayout) gaojiInflater.findViewById(R.id.gaoji_endtime_layout));
        gaojiStartimeText = ((TextView) gaojiInflater.findViewById(R.id.gaoji_startime_text));
        gaojiEndTimeText = ((TextView) gaojiInflater.findViewById(R.id.gaoji_endtime_text));
        //重置  查询
        chongzhiButton = ((TextView) gaojiInflater.findViewById(R.id.chongzhi_button));
        findButton = ((TextView) gaojiInflater.findViewById(R.id.find_button));
        /*地图布局加载*/

        mapButton = ((ImageView) getView().findViewById(R.id.map_list_button));
        /**
         * 地图切换 dialog
         */
        mapChooseDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyleLeft);
        mapChooseInflater = LayoutInflater.from(getContext()).inflate(R.layout.dialog_map_choose_new, null);
        mapChooseInflater.setMinimumWidth(10000);
        gugeGaochengMap = ((LinearLayout) mapChooseInflater.findViewById(R.id.guge_gaocheng));
        gugeYingxiangMap = ((LinearLayout) mapChooseInflater.findViewById(R.id.guge_yingxiang));
        tiandiShiliangMap = ((LinearLayout) mapChooseInflater.findViewById(R.id.tiandi_shiliang));
        tiandiYingxiangMap = ((LinearLayout) mapChooseInflater.findViewById(R.id.tiandi_yingxiang));
        gugeYingxiangImage = ((ImageView) mapChooseInflater.findViewById(R.id.guge_yingxiang_image));
        gugeYingxiangText = ((TextView) mapChooseInflater.findViewById(R.id.guge_yingxiang_text));
//        gugeGaochengImage = ((ImageView) mapChooseInflater.findViewById(R.id.guge_gaocheng_image));
//        gugeGaochengText = ((TextView) mapChooseInflater.findViewById(R.id.gugegaocheng_text));
        tiandiShiliangImage = ((ImageView) mapChooseInflater.findViewById(R.id.tiandi_shiliang_image));
        tiandiShiliangText = ((TextView) mapChooseInflater.findViewById(R.id.tiandi_shiliang_text));
        tiandiYingxiangImage = ((ImageView) mapChooseInflater.findViewById(R.id.tiandi_yingxiang_image));
        tiandiYingxiangText = ((TextView) mapChooseInflater.findViewById(R.id.tiandi_yingxiang_text));
        mapChooseDialog.setContentView(mapChooseInflater);
        Window dialogWindow = mapChooseDialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager wmMap = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wmMap.getDefaultDisplay().getWidth();
        int height1 = wmMap.getDefaultDisplay().getHeight();
        lp.width = (int) (width * 0.65);
        lp.height = (int) height1;
        dialogWindow.setAttributes(lp);
        mapChooseDialog.setCanceledOnTouchOutside(true);


        final JSONObject jsonObject = new JSONObject();
        JSONObject posObj = new JSONObject();
        try {
            posObj.put("lat", currentLatitude);
            posObj.put("lng", currentLongitude);
            jsonObject.put("position", posObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("GPSposition", jsonObject.toString());
//        dWebView.callHandler("GPSflyto", new Object[]{new Gson().toJson(jsonObject.toString())}, new OnReturnValue<String>() {
//            @Override
//            public void onValue(String retValue) {
//                Log.d("jsbridge", "call succeed,return value is " + retValue);
//            }
//        });


        //画边界
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawArea();
            }
        },2000);
    }

    void drawAreas(){

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //自定义边界(json文件)
        try {
            JSONObject jsonObject = new JSONObject(new GetJsonDataUtil().getJson(getActivity(),"areas.json"));
            Log.e(TAG, "onGetDistrictResult: bingo jsonObject.toString() = " + jsonObject.toString() );
            JSONArray features = jsonObject.getJSONArray("features");
            for (int t = 0; t < features.length(); t++) {
                JSONObject feature = (JSONObject) features.get(t);
                JSONObject geometry = feature.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                JSONArray arrayout = (JSONArray) coordinates.get(0);

                Log.e(TAG, "onGetDistrictResult: bingo arrayout.toString() = " + arrayout.toString() );

                List<com.baidu.mapapi.model.LatLng> pointList = new ArrayList<>();
                for (int m = 0; m < arrayout.length(); m++) {
                    JSONArray model = (JSONArray) arrayout.get(m);
                    Log.e(TAG, "onGetDistrictResult: bingo model.toString() = " + model.toString() );
                    double lat = (double) model.get(1);
                    double lng = (double) model.get(0);
                    com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(lat,lng);
                    pointList.add(latLng);
                }
                Log.e(TAG, "onGetDistrictResult: bingo pointList.toString() = " + pointList.toString() );

                OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                        .points(pointList).dottedLine(true).color(Color.BLUE);
                mBaiduMap.addOverlay(ooPolyline11);
                OverlayOptions ooPolygon = new PolygonOptions().points(pointList)
                        .stroke(new Stroke(10, 0xFF0000FF)).fillColor(0x00FFFFFF);
                mBaiduMap.addOverlay(ooPolygon);
                for (com.baidu.mapapi.model.LatLng latLng : pointList) {
                    builder.include(latLng);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "onGetDistrictResult: bingo error = " + e );
        }


        mBaiduMap.setMapStatus(MapStatusUpdateFactory
                .newLatLngBounds(builder.build()));
    }
    void drawArea(){
        new Runnable() {
            @Override
            public void run() {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                //自定义边界(json文件)
                try {
                    JSONObject jsonObject = new JSONObject(new GetJsonDataUtil().getJson(getActivity(),"area.json"));
                    JSONArray features = jsonObject.getJSONArray("features");

                    JSONObject feature = (JSONObject) features.get(0);
                    JSONObject geometry = feature.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    for (int x = 0; x < coordinates.length(); x++) {
                        JSONArray arrayout = (JSONArray) coordinates.get(x);
                        JSONArray arrayin = (JSONArray) arrayout.get(0);

                        List<com.baidu.mapapi.model.LatLng> pointList = new ArrayList<>();
                        for (int m = 0; m < arrayin.length(); m++) {
                            JSONArray model = (JSONArray) arrayin.get(m);
                            double lat = (double) model.get(1);
                            double lng = (double) model.get(0);
                            com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(lat,lng);
                            pointList.add(latLng);
                        }

                        OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                                .points(pointList).dottedLine(true).color(Color.BLUE);
                        mBaiduMap.addOverlay(ooPolyline11);
                        OverlayOptions ooPolygon = new PolygonOptions().points(pointList)
                                .stroke(new Stroke(10, 0xFF0000FF)).fillColor(0x00FFFFFF);
                        mBaiduMap.addOverlay(ooPolygon);
                        for (com.baidu.mapapi.model.LatLng latLng : pointList) {
                            builder.include(latLng);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onGetDistrictResult: bingo error = " + e );
                }


                mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        .newLatLngBounds(builder.build()));
            }
        }.run();
    }


    /**
     * 绘制区域边界
     */
    private void initQuyuBianjie() {
        /*DistrictSearchOption districtSearchOption = new DistrictSearchOption();
        districtSearchOption.cityName("伊犁哈萨克自治州");//检索城市名称
      //  districtSearchOption.districtName("阿坝藏族羌族自治州");//检索的区县名称
        mDistrictSearch.searchDistrict(districtSearchOption);//请求行政区数据*/

    }

    private void showLeibieChangeDailog() {
        //默认选中第一个
        final String[] items = {"按时间分类", "按编号分类"};

        if (currentFireListType == 1) {
            choose1 = 0;
        } else {
            choose1 = 1;
        }
        builder = new AlertDialog.Builder(getContext()).setIcon(R.mipmap.ic_launcher).setTitle("卫星分类")
                .setSingleChoiceItems(items, choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        choose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (choose1 == 0) { //按时间分类
                            currentFireListType = 1;
                            fenleiView.setText("按时间分类");
                        } else {  //按编号分类
                            currentFireListType = 2;
                            fenleiView.setText("按编号分类");
                        }
                        isTuisong = false;
                        initWeixingData();
                    }
                });
        builder.create().show();
    }

    private void register() {
        WeixingModelViewBinder weixingModelViewBinder = new WeixingModelViewBinder();
        weixingModelViewBinder.setListener(this);
        weixingAdapter.register(WeixingModel.class, weixingModelViewBinder);
        weixingAdapter.register(Empty.class, new EmptyViewBinder());
    }

    /**
     * 从服务器获取卫星数据
     */
    private void getWeixingDataFromSetvice() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();

        if (isShowSearchDialog) {
            showDialogProgress(progressDialog, "查询中...");
        }
        Setting setting = new DbConfig(getContext()).getSetting();
        final JSONObject jsonObject = new JSONObject();
        try {

            if (isTuisong) {     //是推送查询
                jsonObject.put("endTime", format.format(c.getTime()).replace(" ", "T"));
                jsonObject.put("startTime", obTime);
            } else {     //不是推送查询
                if (isGaojiFind) {       //gaoji
                    String startTimeStr = "";
                    if (!gaojiStartimeText.getText().toString().equals("请输入开始时间")) {
                        startTimeStr = gaojiStartimeText.getText().toString();
                    }
                    String endTimeStr = "";
                    if (!gaojiEndTimeText.getText().toString().equals("请输入结束时间")) {
                        endTimeStr = gaojiEndTimeText.getText().toString();
                    }
                    jsonObject.put("endTime", endTimeStr);
                    jsonObject.put("startTime", startTimeStr);
                } else {
                    jsonObject.put("endTime", format.format(c.getTime()).replace(" ", "T"));
                    c.add(Calendar.HOUR, -currentFireFindTime);    //获取currentFireFindTime小时之前的时间
                    //jsonObject.put("startTime","2020-11-08T10:00:00");   //测试用 上线要改过来
                    jsonObject.put("startTime", format.format(c.getTime()).replace(" ", "T"));
                }

            }


            if (!user.getGridNo().isEmpty()) {
                if (user.getGridNo().contains("0000")) {
                    jsonObject.put("provinceCode", user.getGridNo());
                } else if (user.getGridNo().contains("00")) {
                    jsonObject.put("cityCode", user.getGridNo());
                } else {
                    jsonObject.put("countyCode", user.getGridNo());
                }
            }
            if (setting != null) {
                jsonObject.put("satellite", "");
                jsonObject.put("landType", "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "fire/api/satelliteFirealarm/list");

        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization", "bearer " + user.getToken());
        Log.e(TAG, "getWeixingDataFromSetvice: " + params);
        Log.e(TAG, "getWeixingDataFromSetvice: " + jsonObject.toString());
        Log.e(TAG, "getWeixingDataFromSetvice: " + user.getToken());
        params.setConnectTimeout(100000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:weixing " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        Gson gson = new Gson();
                        weixingModelList.clear();
                        weixingModelList = gson.fromJson(String.valueOf(data), new TypeToken<List<WeixingModel>>() {
                        }.getType());

                        initWeixingData();
                    } else {
                        Toast.makeText(getContext(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (isShowSearchDialog) {
                    progressDialog.dismiss();
                }

            }
        });

    }

    /**
     * 从服务器获取资源列表
     */
    private void getResourcesListFromService() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isDisplay", "1");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/resourceList/list");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        Log.e(TAG, "getDataFromService: " + jsonObject.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(getContext()).getUser().getToken());
        Log.e(TAG, "resource: --" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        Gson gson = new Gson();
                        resourceListList.clear();
                        resourceListList = gson.fromJson(String.valueOf(data), new TypeToken<List<ResourceList>>() {
                        }.getType());

                        for (int i = 0; i < resourceListList.size(); i++) {
                            resourceListList.get(i).setCheck(false);
                        }
                        initResourceListData();
                    } else {
                        Toast.makeText(getContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
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
     * 从服务器获取一体机数据
     */
    private void getonebodyDataFromSetvice() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();

        if (isShowSearchDialog) {
            showDialogProgress(progressDialog, "查询中...");
        }
        final JSONObject jsonObject = new JSONObject();
        try {
            JSONObject dto = new JSONObject();
            jsonObject.put("dto", dto);
            jsonObject.put("limit", 120);
            jsonObject.put("page", 1);
           /* jsonObject.put("isReal", null);
            jsonObject.put("groupId", new DbConfig(getContext()).getUser().getGroupId());
            jsonObject.put("isHandle", 0);
            if (shipinState == 0) {
                jsonObject.put("type", null);
            } else {
                jsonObject.put("type", shipinState);
            }
*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams( RequestUtils.REQUEST_URL +"fire/api/monitorFirealarm/page");
       // RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/fire/api/Statistic/getAlarmList");
     //   RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "fire/api/Statistic/getAlarmList");


        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization", "bearer " + user.getToken());
        Log.e(TAG, "getonebodyDataFromSetvice: " + params);
        Log.e(TAG, "getonebodyDataFromSetvice: " + jsonObject.toString());
        Log.e(TAG, "getonebodyDataFromSetvice: " + user.getToken());
        params.setConnectTimeout(100000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "getonebodyDataonSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        JSONArray data = jsonObject1.getJSONArray("data").getJSONObject(0).getJSONArray("dataList");
                      /*  //totalSize=data.getInt("totalSize");
                        JSONObject getJsonObj = data.getJSONObject(0);//获取json数组中的第一项
                        JSONArray dataList = getJsonObj.getJSONArray("dataList");
                        Log.i(TAG, "dataList: "+dataList);
                        totalSize=getJsonObj.getInt("totalSize");
                        Log.i(TAG, "getonebodyDataonSuccess: "+totalSize);
                        lastPage= (totalSize + 50 -1) / 50;     //计算最大分页数
                        Log.i(TAG, "getonebodyDataonSuccess: "+lastPage);*/
                        Gson gson = new Gson();
                        oneBodyFireList.clear();
                        oneBodyFireFenleiList.clear();
                        List<OneBodyFire> oneBodyFireAllList = gson.fromJson(String.valueOf(data), new TypeToken<List<OneBodyFire>>() {
                        }.getType());
                        oneBodyFireFenleiList.addAll(oneBodyFireAllList);
                   /*
                        //将所有疑似火情剔除
                        for (int i = 0; i < oneBodyFireAllList.size(); i++) {
                            if (oneBodyFireAllList.get(i).getIsReal() != null) {
                                if (oneBodyFireAllList.get(i).getIsReal() == 1) {
                                    oneBodyFireList.add(oneBodyFireAllList.get(i));
                                }
                            } else {
                                oneBodyFireList.add(oneBodyFireAllList.get(i));
                            }
                        }
                        isReleasList = 2;
                        choose1 = 1;
                        oneBodyFireFenleiList.clear();

                        fenleiTextView.setText("未处理");
                        for (int j = 0; j < oneBodyFireList.size(); j++) {
                            if (oneBodyFireList.get(j).getIsReal() == null) {
                                oneBodyFireFenleiList.add(oneBodyFireList.get(j));
                            }
                        }*/
                        if (isOneBodyShuaxin) {
                            isOneBodyShuaxin = false;
                            oneBodyRefreshLayout.setRefreshing(false);
                        }
                        removeMarkerBaiduMap();

                    } else {
                        Toast.makeText(getContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (isShowSearchDialog) {
                    progressDialog.dismiss();
                }

            }
        });

    }

    /**
     * 初始化一体机火点数据
     */
    private void initOneBodyFireData() {
        //往地图上打一体机火点
        // initYitijiFireMap();  超图的方法
        initYitijiFireBiaduMap();
        //往列表上展示数据

        Log.e(TAG, "initOneBodyFireData:size== " + oneBodyFireFenleiList.size());
       /* if (oneBodyFireFenleiList.size()==0&&currentPage==1){
            onebodyItems.clear();
        }else {
            for (int i = 0; i < oneBodyFireFenleiList.size(); i++) {
                onebodyItems.add(oneBodyFireFenleiList.get(i));
            }
        }*/
        onebodyItems.clear();
        if (oneBodyFireFenleiList.size() == 0) {
            onebodyItems.add(new Empty("暂无数据"));
        } else {
            for (int i = 0; i < oneBodyFireFenleiList.size(); i++) {
                onebodyItems.add(oneBodyFireFenleiList.get(i));
            }
        }


        assertAllRegistered(onebodyAdapter, onebodyItems);
        onebodyAdapter.notifyDataSetChanged();
    }

    /**
     * 往百度地图上打点
     */
    private void initYitijiFireBiaduMap() {
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        Matrix matrix = new Matrix();
        Bitmap bitmap = null;
        BitmapDescriptor marker = null;
        for (int i = 0; i < oneBodyFireFenleiList.size(); i++) {
            bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_red_fire)).getBitmap();  //森林防火
           /* switch (oneBodyFireFenleiList.get(i).getType()){
                case 2:

                    break;
                case 4:
                     bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_blue_fire)).getBitmap();  //海域监控
                    break;
                case 5:
                     bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_yellow_fire)).getBitmap();  //国土报警
                    break;
            }*/
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            marker = BitmapDescriptorFactory.fromBitmap(bitmap);
            try {
                double[] doubles = LatLngChangeNew.calWGS84toBD09(oneBodyFireFenleiList.get(i).getAlarmLatitude(), oneBodyFireFenleiList.get(i).getAlarmLongitude());
                com.baidu.mapapi.model.LatLng point = new com.baidu.mapapi.model.LatLng(doubles[0], doubles[1]);
                Bundle bundle = new Bundle();
                bundle.putString("id", oneBodyFireFenleiList.get(i).getId());
                bundle.putInt("type", ONE_BODY);
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .extraInfo(bundle)
                        .icon(marker);
                options.add(i, option);
            }catch (Exception e){
                //经纬度位null 抛出异常
            }

        }
        optionsAllList.addAll(options);
        mBaiduMap.addOverlays(options);
    }


    private void initResourceListData() {
        resourceItems.clear();
        for (int i = 0; i < resourceListList.size(); i++) {
            resourceItems.add(resourceListList.get(i));
        }

        assertAllRegistered(resourceAdapter, resourceItems);
        resourceAdapter.notifyDataSetChanged();
    }

    /**
     * 地图上加载一体机火点
     */
    private void initYitijiFireMap() {
        /*//清除旧的卫星定位
        dWebView.callHandler("removeDataSource", new Object[]{"ic_onebody"}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.e(TAG, "onValue:  qingchu" + retValue);
            }
        });*/
        //打入新的卫星点位
        if (oneBodyFireFenleiList.size() > 0) {
            List<MapModel> mapModelList = new ArrayList<>();
            for (int i = 0; i < oneBodyFireFenleiList.size(); i++) {
                OneBodyFire oneBodyFire = oneBodyFireFenleiList.get(i);
                MapModel mapModel = new MapModel(oneBodyFire.getId(), oneBodyFire.getName(), new MapPosition(oneBodyFire.getAlarmLongitude(), oneBodyFire.getAlarmLatitude(), 0.00), "ic_onebody");
                mapModelList.add(mapModel);
            }

           /* Log.e(TAG, "initWeixingMap: " + new Gson().toJson(mapModelList));
            dWebView.callHandler("showPoint", new Object[]{"ic_onebody", new Gson().toJson(mapModelList), ""}, new OnReturnValue<String>() {
                @Override
                public void onValue(String retValue) {
                    Log.e(TAG, "onValue:  dadian" + retValue);
                }
            });*/
        }

    }

    /**
     * 初始化卫星数据
     */
    private void initWeixingData() {
        //往地图上打点
        //initWeixingMap();
        removeMarkerBaiduMap();

        //往列表上展示
        Log.e(TAG, "initWeixingData: " + weixingModelList.size());
        fireCountText.setText(weixingModelList.size() + "");
        weixingItems.clear();
        if (weixingModelList.size() > 0) {
            if (currentFireListType == 1) {      //按时间分类
                for (int i = 0; i < weixingModelList.size(); i++) {
                    if (i > 0) {
                        if (weixingModelList.get(i).getObservationDatetime().equals(weixingModelList.get(i - 1).getObservationDatetime())) {    //如果这一条如上一条时间相同
                            weixingModelList.get(i).setShowTime(false);
                            weixingModelList.get(i).setShowLine(false);
                        } else {
                            weixingModelList.get(i).setShowTime(true);
                            weixingModelList.get(i).setShowLine(true);
                        }
                    } else {
                        weixingModelList.get(i).setShowTime(true);
                        weixingModelList.get(i).setShowLine(false);
                    }
                    weixingModelList.get(i).setFireListType(1);
                    weixingItems.add(weixingModelList.get(i));
                }
            } else {         //按编号分类
                for (int i = 0; i < weixingModelList.size(); i++) {
                    if (i > 0) {
                        if (weixingModelList.get(i).getFireNo().equals(weixingModelList.get(i - 1).getFireNo())) {    //如果这一条如上一条时间相同
                            weixingModelList.get(i).setShowTime(false);
                            weixingModelList.get(i).setShowLine(false);
                        } else {
                            weixingModelList.get(i).setShowTime(true);
                            weixingModelList.get(i).setShowLine(true);
                        }
                    } else {
                        weixingModelList.get(i).setShowTime(true);
                        weixingModelList.get(i).setShowLine(false);
                    }
                    weixingModelList.get(i).setFireListType(2);
                    weixingItems.add(weixingModelList.get(i));
                }
            }

        } else {
            weixingItems.add(new Empty("暂无卫星火情"));
        }

        assertAllRegistered(weixingAdapter, weixingItems);
        weixingAdapter.notifyDataSetChanged();


        //显示消息列表
        if (isShowSearchDialog) {
            fireInfoListDialog.show();
        }

        //弹出推送详情
        if (isTuisong) {

            for (int i = 0; i < weixingModelList.size(); i++) {
                if (weixingModelList.get(i).getId().equals(weixingFireId)) {
                    currentWeixingModel = weixingModelList.get(i);
                }
            }

            //飞到精确点上
            // initWeixingFlyMap();
            //加载卫星详细数据
            initWeixinModelData();
        }

    }

    /**
     * 卫星数据百度地图打点
     */
    private void initWeixingBaiduMap() {
        //清除旧的卫星定位
        /*dWebView.callHandler("removeDataSource", new Object[]{"fire_weixing"}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.e(TAG, "onValue:  qingchu" + retValue);
            }
        });*/
        //打入新的卫星点位
        if (weixingModelList.size() > 0) {
            List<MapModel> mapModelList = new ArrayList<>();
            for (int i = 0; i < weixingModelList.size(); i++) {
                WeixingModel weixingModel = weixingModelList.get(i);
                MapModel mapModel = new MapModel(weixingModel.getId(), weixingModel.getFormattedAddress(), new MapPosition(Double.parseDouble(weixingModel.getLongitude()), Double.parseDouble(weixingModel.getLatitude()), 0.00), "fire_weixng");
                mapModelList.add(mapModel);
            }

            List<OverlayOptions> options = new ArrayList<OverlayOptions>();
            Matrix matrix = new Matrix();
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.fire_weixing)).getBitmap();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            BitmapDescriptor marker = BitmapDescriptorFactory.fromBitmap(bitmap);
            for (int i = 0; i < mapModelList.size(); i++) {

                double[] doubles = LatLngChangeNew.calWGS84toBD09(mapModelList.get(i).getPosition().getLat(), mapModelList.get(i).getPosition().getLng());
                com.baidu.mapapi.model.LatLng point = new com.baidu.mapapi.model.LatLng(doubles[0], doubles[1]);
                Bundle bundle = new Bundle();
                bundle.putString("id", mapModelList.get(i).getId());
                bundle.putInt("type", WEI_XING);
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .extraInfo(bundle)
                        .icon(marker);
                options.add(i, option);
            }
            optionsAllList.addAll(options);
            mBaiduMap.addOverlays(options);
        }
    }

    private void initWeixingListData() {

    }

    /**
     * 卫星数据地图打点
     */
    private void initWeixingMap() {
        //清除旧的卫星定位
      /*  dWebView.callHandler("removeDataSource", new Object[]{"fire_weixing"}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.e(TAG, "onValue:  qingchu" + retValue);
            }
        });*/
        //打入新的卫星点位
        if (weixingModelList.size() > 0) {
            List<MapModel> mapModelList = new ArrayList<>();
            for (int i = 0; i < weixingModelList.size(); i++) {
                WeixingModel weixingModel = weixingModelList.get(i);
                MapModel mapModel = new MapModel(weixingModel.getId(), weixingModel.getFormattedAddress(), new MapPosition(Double.parseDouble(weixingModel.getLongitude()), Double.parseDouble(weixingModel.getLatitude()), 0.00), "fire_weixng");
                mapModelList.add(mapModel);
            }
/*
            Log.e(TAG, "initWeixingMap: " + new Gson().toJson(mapModelList));
            dWebView.callHandler("showPoint", new Object[]{"fire_weixing", new Gson().toJson(mapModelList), ""}, new OnReturnValue<String>() {
                @Override
                public void onValue(String retValue) {
                    Log.e(TAG, "onValue:  dadian" + retValue);
                }
            });*/
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
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(getContext(), "请打开GPS和使用网络定位以提高精度", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getContext(), "Please Open Your GPS or Location Service", Toast.LENGTH_SHORT).show();

        }
        if (provider != null) {
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            try {
                return location.getLongitude() + "," + location.getLatitude();
            } catch (Exception e) {
                return "0.00,0.00";
            }

        }
        return null;
    }


    /**
     * 地图资源点点击回调
     */
    @Override
    public void onJsEscapeDetailsClickListener(String json) {
        Log.e(TAG, "onJsEscapeDetailsClickListener: " + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            String resourcetype = jsonObject.getString("resourcetype");
            if (resourcetype.equals("fire_weixing")) {
                String id = jsonObject.getString("id");
                Message message = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("id", id);
                b.putInt("what", DIALOG_FIRE_SHOW);
                message.setData(b);
                mHandler.sendMessage(message);
            } else if (resourcetype.equals("ic_onebody")) {
                String id = jsonObject.getString("id");
                Message message = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("id", id);
                b.putInt("what", DIALOG_ONEBODY_FIRE_SHOW);
                message.setData(b);
                mHandler.sendMessage(message);
            } else if (resourcetype.equals("monitor")) {
//                String id = jsonObject.getString("id");
//                Intent intent=new Intent();
//                intent.putExtra("id",id);
//                intent.setAction("video_play");
//                getContext().sendBroadcast(intent);

                String id = jsonObject.getString("id");
                Message message = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("id", id);
                b.putInt("what", DIALOG_MOINTOR_SHOW);
                message.setData(b);
                mHandler.sendMessage(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
      /*  if (type.equals("fire_weixing")){
            for (int i = 0; i < weixingModelList.size(); i++) {
                if (weixingModelList.get(i).getId().equals(id)) {
                    currentWeixingModel = weixingModelList.get(i);
                }
            }
        }

        //飞到精确点上
        initWeixingFlyMap();
        //加载卫星详细数据
        initWeixinModelData();*/
    }


    /**
     * 地图  飞到卫星火点精确点上
     */
    private void initWeixingFlyMap() {
        final JSONObject jsonObject = new JSONObject();
        JSONObject posObj = new JSONObject();
        try {
            posObj.put("lat", Double.parseDouble(currentWeixingModel.getLatitude()));
            posObj.put("lng", Double.parseDouble(currentWeixingModel.getLongitude()));
            jsonObject.put("position", posObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("GPSposition", jsonObject.toString());
        flyBaiduMap(Double.parseDouble(currentWeixingModel.getLatitude()), Double.parseDouble(currentWeixingModel.getLongitude()));
        /*dWebView.callHandler("GPSflyto", new Object[]{new Gson().toJson(jsonObject.toString())}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.d("jsbridge", "call succeed,return value is " + retValue);
            }
        });*/
    }

    /**
     * 加载卫星详细数据
     */
    private void initWeixinModelData() {

        Log.e(TAG, "initWeixinModelData: ceshi1");
        if (currentWeixingModel.getFormattedAddress().isEmpty()) {

            fireAddressText.setText("边境热源");
        } else {
            fireAddressText.setText(currentWeixingModel.getFormattedAddress());
        }
        fireTimeText.setText(currentWeixingModel.getObservationDatetime().replace("T", "  ").substring(0, currentWeixingModel.getObservationDatetime().indexOf(".")));
        jingWeiText.setText(NumberUtils.saveOneBitTwo(Double.parseDouble(currentWeixingModel.getLongitude())) + "  " + NumberUtils.saveOneBitTwo(Double.parseDouble(currentWeixingModel.getLatitude())));
        kexinText.setText(currentWeixingModel.getCredibility() + "");
        mianjiText.setText(currentWeixingModel.getArea() + "");
        cishuText.setText(currentWeixingModel.getObservationFrequency() + "");
        leixingText.setText("林地(" + getTwoDouble(currentWeixingModel.getWoodland() * 100) + "%)草地(" + getTwoDouble(currentWeixingModel.getGrassland() * 100) + "%)农田(" + getTwoDouble(currentWeixingModel.getFarmland() * 100) + "%)其他(" + getTwoDouble(currentWeixingModel.getOtherland() * 100) + "%)");
        Log.e(TAG, "initWeixinModelData: ceshi2");
        shujuyuanText.setText(currentWeixingModel.getSatellite());
        huodianCodeText.setText(currentWeixingModel.getFireNo());
        Log.e(TAG, "initWeixinModelData: ceshi3");
        xiangyuanmianjiView.setText(currentWeixingModel.getPixelArea() + "");
        xiangyuanshuView.setText(currentWeixingModel.getPixelNumber() + "");
        Log.e(TAG, "initWeixinModelData: ceshi4");
        Log.e(TAG, "initWeixinModelData: ceshi4" + currentWeixingModel.getLightImageAddress());
        if (currentWeixingModel.getLightImageAddress() != null) {
            Glide.with(getContext()).load("http://web.ehaohai.com:2018" + currentWeixingModel.getLightImageAddress())
                    .error(R.drawable.ic_no_pic)
                    .placeholder(R.drawable.ic_jaizai).into(huodianOneImage);
        } else {
            Glide.with(getContext()).load(R.drawable.ic_no_pic).into(huodianOneImage);
        }

        Log.e(TAG, "initWeixinModelData: ceshi5");
        if (currentWeixingModel.getIrImageAddress() != null) {
            Glide.with(getContext()).load("http://web.ehaohai.com:2018" + currentWeixingModel.getIrImageAddress())
                    .error(R.drawable.ic_no_pic)
                    .placeholder(R.drawable.ic_jaizai).into(huodianTwoImage);
        } else {
            Glide.with(getContext()).load(R.drawable.ic_no_pic).into(huodianTwoImage);
        }


        Log.e(TAG, "initWeixinModelData: ceshi6");
        weixingFireDialog.show();
    }

    public Double getTwoDouble(double f) {
        BigDecimal bg = new BigDecimal(f);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 日期选择控件
     */
    private void showDataDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
                }
                if (endDate.length() > 0) { //清除上次记录的日期
                    endDate.delete(0, endDate.length());
                }
                if (isChooseStarTime) {
                    date.append(String.valueOf(year));
                    if (month < 9) {
                        date.append("-0").append(String.valueOf(month + 1));
                    } else {
                        date.append("-").append(String.valueOf(month + 1));
                    }
                    if (day < 10) {
                        date.append("-0").append(String.valueOf(day));
                    } else {
                        date.append("-").append(String.valueOf(day));
                    }
                    gaojiStartimeText.setText(date);
                    //gaojiStartimeText.setText(date.append(String.valueOf(year)).append("/").append(String.valueOf(month + 1)).append("/").append(day));
                } else {
                    date.append(String.valueOf(year));
                    if (month < 9) {
                        date.append("-0").append(String.valueOf(month + 1));
                    } else {
                        date.append("-").append(String.valueOf(month + 1));
                    }
                    if (day < 10) {
                        date.append("-0").append(String.valueOf(day));
                    } else {
                        date.append("-").append(String.valueOf(day));
                    }
                    gaojiEndTimeText.setText(date);
                    // gaojiEndTimeText.setText(date.append(String.valueOf(year)).append("/").append(String.valueOf(month + 1)).append("/").append(day));
                }
                dialog.dismiss();
                showTimeDialog();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        final android.support.v7.app.AlertDialog dialog = builder.create();
        View dialogView = View.inflate(getContext(), R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        Calendar date = Calendar.getInstance();
        int year1 = date.get(Calendar.YEAR);
        int month1 = date.get(Calendar.MONTH);
        int day1 = date.get(Calendar.DATE);
        String endData = year1 - 10 + "-" + month1 + "-" + day1;
        Log.i(TAG, "showDataDialog: " + endData);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(endData);
        } catch (ParseException e) {

        }
        long starTimre = date2.getTime();


        long endTimre = System.currentTimeMillis();

        datePicker.setMaxDate(endTimre);
        datePicker.setMinDate(starTimre);

        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month, day, this);
    }

    /**
     * 日期选择控件
     */
    private void showTimeDialog() {
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder1.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isChooseStarTime) {
                    if (chooseHour < 10 && chooseMinute < 10) {
                        gaojiStartimeText.append("  0" + chooseHour + ":0" + chooseMinute + ":00");
                    } else if (chooseHour < 10 && chooseMinute > 10) {
                        gaojiStartimeText.append("  0" + chooseHour + ":" + chooseMinute + ":00");
                    } else if (chooseHour >= 10 && chooseMinute < 10) {
                        gaojiStartimeText.append("  " + chooseHour + ":0" + chooseMinute + ":00");
                    } else {
                        gaojiStartimeText.append("  " + chooseHour + ":" + chooseMinute + ":00");
                    }
                    //gaojiStartimeText.append("  " + chooseHour + ":" + chooseMinute);
                } else {
                    if (chooseHour < 10 && chooseMinute < 10) {
                        gaojiEndTimeText.append("  0" + chooseHour + ":0" + chooseMinute + ":00");
                    } else if (chooseHour < 10 && chooseMinute > 10) {
                        gaojiEndTimeText.append("  0" + chooseHour + ":" + chooseMinute + ":00");
                    } else if (chooseHour > 10 && chooseMinute < 10) {
                        gaojiEndTimeText.append("  " + chooseHour + ":0" + chooseMinute + ":00");
                    } else {
                        gaojiEndTimeText.append("  " + chooseHour + ":" + chooseMinute + ":00");
                    }
                    //  gaojiEndTimeText.append("  " + chooseHour + ":" + chooseMinute);
                }


                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        final android.support.v7.app.AlertDialog timeDialog = builder1.create();
        View dialogView = View.inflate(getContext(), R.layout.dialog_time, null);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timepicker);
        Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR);
        int minute = date.get(Calendar.MINUTE);

        timePicker.setIs24HourView(true);   //设置时间显示为24小时

        timePicker.setHour(hour);  //设置当前小时
        timePicker.setMinute(minute); //设置当前分（0-59）

        timeDialog.setTitle("设置时间");
        timeDialog.setView(dialogView);
        timeDialog.show();


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                chooseHour = hourOfDay;
                chooseMinute = minute;
            }
        });
        //初始化日期监听事件
        //   timePicker.init(year, month, day, this);
    }

    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        chooseHour = calendar.get(Calendar.HOUR);
        chooseMinute = calendar.get(Calendar.MINUTE);

    }

    class myReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {

            String msg = intent.getStringExtra("message");
            //Toast.makeText(context, "广播已经接收", Toast.LENGTH_SHORT).show();
            Log.i("onReceive: ", msg);
          /*  dWebView.callHandler("GPSpoint", new Object[]{new Gson().toJson(msg)}, new OnReturnValue<String>() {
                @Override
                public void onValue(String retValue) {
                    Log.d("jsbridge", "call succeed,return value is " + retValue);
                }
            });*/
        }
    }

    /**
     * 卫星列表条目点击
     *
     * @param weixingModel
     */
    @Override
    public void onWeixingInfoClick(WeixingModel weixingModel) {
        currentWeixingModel = weixingModel;

        fireInfoListDialog.hide();

        flyBaiduMap(Double.parseDouble(weixingModel.getLatitude()), Double.parseDouble(weixingModel.getLongitude()));
        //  initWeixingFlyMap();
        //加载卫星详细数据
        initWeixinModelData();

    }

    private void flyBaiduMap(double lat, double lnt) {
        //飞到精确点上
        double[] position = LatLngChangeNew.calWGS84toBD09(lat, lnt);
        com.baidu.mapapi.model.LatLng ll = new com.baidu.mapapi.model.LatLng(
                position[0], position[1]);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(13);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }

    /**
     * 一体机火点条目点击
     *
     * @param oneBodyFire
     */
    @Override
    public void onOneBodyItemClickListener(OneBodyFire oneBodyFire) {
        currentOneBodyFire = oneBodyFire;
        onebodyListDialog.dismiss();
        //飞到一体机精确点上
        initOneBodyFlyBaiduMap();
        //加载一体机详细数据
        initOneBodyFireModelData();

    }

    /**
     * 一体机火点详情
     */
    private void initOneBodyFireModelData() {
        mingchengView.setText(currentOneBodyFire.getName());
        dizhiView.setText(currentOneBodyFire.getAddress());
        shijianView.setText(currentOneBodyFire.getAlarmDatetime().replace("T", " ").substring(0, currentOneBodyFire.getAlarmDatetime().indexOf(".")));
        jingweiduView.setText(currentOneBodyFire.getAlarmLongitude() + "、" + currentOneBodyFire.getAlarmLatitude());
        Log.e(TAG, "initOneBodyFireModelData:getPicPath1== " + currentOneBodyFire.getPicPath1());
        Log.e(TAG, "initOneBodyFireModelData:getPicPath2== " + currentOneBodyFire.getPicPath2());
     //   Picasso.with(getContext()).load(currentOneBodyFire.getPicPath1()).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(yitijiOneView);
        if (currentOneBodyFire.getPicPath1() == null) {
            yitijiOneView.setVisibility(View.INVISIBLE);
        } else {
            yitijiOneView.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(currentOneBodyFire.getPicPath1().replace("59.213.220.32:85","112.44.67.43")).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(yitijiOneView);
        }

        if (currentOneBodyFire.getPicPath2() == null) {
            yitijiTwoView.setVisibility(View.INVISIBLE);
        } else {
            yitijiTwoView.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(currentOneBodyFire.getPicPath2().replace("59.213.220.32:85","112.44.67.43")).placeholder(R.drawable.ic_jaizai).error(R.drawable.ic_no_pic).into(yitijiTwoView);

        }

/*        Glide.with(getContext()).load(currentOneBodyFire.getPicPath1())
                .error(R.drawable.ic_no_pic)
                .placeholder(R.drawable.ic_jaizai).into(yitijiOneView);*/
/*        Glide.with(getContext()).load(currentOneBodyFire.getPicPath2())
                .error(R.drawable.ic_no_pic)
                .placeholder(R.drawable.ic_jaizai).into(yitijiTwoView);*/
        Log.e(TAG, "initOneBodyFireModelData:getVideoPath1== " + currentOneBodyFire.getVideoPath1());
        Log.e(TAG, "initOneBodyFireModelData:getVideoPath2== " + currentOneBodyFire.getVideoPath2());
        if ((currentOneBodyFire.getVideoPath1() == null && currentOneBodyFire.getVideoPath2() == null)) {
            oneBodyShipinLayout.setVisibility(View.GONE);
            Log.e(TAG, "initOneBodyFireModelData:1 " );
        } else {
            Log.e(TAG, "initOneBodyFireModelData:2" );
            oneBodyShipinLayout.setVisibility(View.VISIBLE);
        }

        if (currentOneBodyFire.getVideoPath1() == null) {
            Log.e(TAG, "initOneBodyFireModelData:3 " );
            oneBodyKJGButton.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "initOneBodyFireModelData:4 " );
            oneBodyKJGButton.setVisibility(View.VISIBLE);
        }
        if (currentOneBodyFire.getVideoPath2() == null) {
            Log.e(TAG, "initOneBodyFireModelData:5 " );
            oneBodyRCXButton.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "initOneBodyFireModelData:6 " );
            oneBodyRCXButton.setVisibility(View.VISIBLE);
        }

        if (currentOneBodyFire.getIsReal() == null) {
            zhenshiLayout.setVisibility(View.VISIBLE);
            zhenshiTextView.setVisibility(View.GONE);
        } else {
            zhenshiLayout.setVisibility(View.GONE);
            zhenshiTextView.setVisibility(View.VISIBLE);
            if (currentOneBodyFire.getIsReal() == 0) {
                zhenshiTextView.setText("疑似火情");
            } else {
                zhenshiTextView.setText("真实火情");
            }
        }
        oneBodyFireDialog.show();
    }

    /**
     * 资源点详情
     */
    private void initresourceFireModelData() {
        mingchengView.setText(currentOneBodyFire.getName());
        dizhiView.setText(currentOneBodyFire.getAddress());
        shijianView.setText(currentOneBodyFire.getAlarmDatetime().replace("T", " ").substring(0, currentOneBodyFire.getAlarmDatetime().indexOf(".")));
        jingweiduView.setText(currentOneBodyFire.getAlarmLongitude() + "、" + currentOneBodyFire.getAlarmLatitude());
        oneBodyFireDialog.show();
    }

    /**
     * 一体机飞到地图上
     */
    private void initOneBodyFlyMap() {
        final JSONObject jsonObject = new JSONObject();
        JSONObject posObj = new JSONObject();
        try {
            posObj.put("lat", currentOneBodyFire.getAlarmLatitude());
            posObj.put("lng", currentOneBodyFire.getAlarmLongitude());
            jsonObject.put("position", posObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("GPSposition", jsonObject.toString());
        flyBaiduMap(currentOneBodyFire.getAlarmLatitude(), currentOneBodyFire.getAlarmLongitude());
        /*dWebView.callHandler("GPSflyto", new Object[]{new Gson().toJson(jsonObject.toString())}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.d("jsbridge", "call succeed,return value is " + retValue);
            }
        });*/
    }

    /**
     * 资源列表的电机回调
     *
     * @param resourceList
     */
    @Override
    public void onResourceListItemClickListener(ResourceList resourceList) {
        for (int i = 0; i < resourceListList.size(); i++) {
            if (resourceListList.get(i).getId().equals(resourceList.getId())) {
                Log.e(TAG, "onResourceListItemClickListener: " + resourceList.isCheck());
                if (resourceList.getName().equals("视频监控点") || resourceList.getName().equals("水源地") ||resourceList.getName().equals("墓地")
                        ||resourceList.getName().equals("护林检查站")) {
                    if (resourceList.isCheck()) {    //隐藏资源点
                        initMapShow(resourceList.getName(),false);
                       // isShowMonitor = false;
                        resourceListList.get(i).setCheck(false);
                        removeMarkerBaiduMap();
                    } else {         //显示资源点逻辑
                       // isShowMonitor = true;
                        initMapShow(resourceList.getName(),true);
                        resourceListList.get(i).setCheck(true);
                        getResourceDataFromService(resourceList.getApiUrl(), resourceList.getName());
                    }
                } else {
                    if (resourceList.isCheck()) {    //隐藏资源点
                        isShowMonitor = false;
                        resourceListList.get(i).setCheck(false);
                    } else {         //显示资源点逻辑
                        isShowMonitor = true;
                        resourceListList.get(i).setCheck(true);

                    }
                }

            }
        }

        initResourceListData();
        resourceListDialog.dismiss();

    }
//resourceList.getName().equals("视频监控点") || resourceList.getName().equals("水源地") ||resourceList.getName().equals("墓地")||resourceList.getName().equals("护林检查站")
    private void initMapShow(String name, boolean isShow) {
        if (name.equals("视频监控点")){
            isShowMonitor = isShow;
        }else if (name.equals("水源地")){
            isShowWaterSource = isShow;
        }else if (name.equals("墓地")){
            isShowCemetery = isShow;
        }else if (name.equals("护林检查站")){
            isShowCheckStation = isShow;
        }

    }

    /**
     * 移除百度地图上的资源点
     */
    private void removeMarkerBaiduMap() {
        //清除地图上的所有覆盖物
        mBaiduMap.clear();
        //画边界
        initQuyuBianjie();
        drawArea();

        if (isShowMonitor) {
          //  initResourceDataIntoBaiduMap();
            initResourceMonitoerDataIntoBaiduMap();
        }
        if (isShowCemetery){
            initResourceCemeteryDataIntoBaiduMap();
        }
        if (isShowWaterSource){
            initResourceWaterSourceDataIntoBaiduMap();
        }
        if (isShowCheckStation){
            initResourceCheckStationDataIntoBaiduMap();
        }
        if (isShowYitiji) {
            initOneBodyFireData();
        }
        if (isShowWeixing) {
            initWeixingBaiduMap();
        }

      /*  for (int i = 0; i < optionsAllList.size(); i++) {
            MarkerOptions markerOptions = (MarkerOptions) optionsAllList.get(i);
            // Marker marker = (Marker) mBaiduMap.addOverlay(overlayOptions);
            Bundle extraInfo = markerOptions.getExtraInfo();
            String id = extraInfo.getString("id");
            int type = extraInfo.getInt("type", 0);
            if (type == markerType){
                markerOptions.visible(false);
            }
        }*/

    }

    private void initResourceCheckStationDataIntoBaiduMap() {
        Log.e(TAG, "initResourceDataIntoBaiduMap: " +checkStationDTOList.size() );
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        for (int i = 0; i < checkStationDTOList.size(); i++) {
            Matrix matrix = new Matrix();
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_check_station)).getBitmap();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            BitmapDescriptor marker = BitmapDescriptorFactory.fromBitmap(bitmap);
            double[] doubles = LatLngChangeNew.calWGS84toBD09(checkStationDTOList.get(i).getPosition().getLat(), checkStationDTOList.get(i).getPosition().getLng());
            com.baidu.mapapi.model.LatLng point = new com.baidu.mapapi.model.LatLng(doubles[0], doubles[1]);
            Bundle bundle = new Bundle();
            bundle.putString("id", checkStationDTOList.get(i).getId());
            bundle.putInt("type", RESOURCE_MONITOR);
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .extraInfo(bundle)
                    .icon(marker);
            options.add(i, option);

        }
        Log.e(TAG, "initResourceDataIntoBaiduMap: initMoniter4");
        optionsAllList.addAll(options);
        mBaiduMap.addOverlays(options);
    }

    private void initResourceWaterSourceDataIntoBaiduMap() {
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        for (int i = 0; i < waterSourceDTOList.size(); i++) {
            Matrix matrix = new Matrix();
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_water_source)).getBitmap();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            BitmapDescriptor marker = BitmapDescriptorFactory.fromBitmap(bitmap);
            double[] doubles = LatLngChangeNew.calWGS84toBD09(waterSourceDTOList.get(i).getPosition().getLat(), waterSourceDTOList.get(i).getPosition().getLng());
            com.baidu.mapapi.model.LatLng point = new com.baidu.mapapi.model.LatLng(doubles[0], doubles[1]);
            Bundle bundle = new Bundle();
            bundle.putString("id", waterSourceDTOList.get(i).getId());
            bundle.putInt("type", RESOURCE_MONITOR);
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .extraInfo(bundle)
                    .icon(marker);
            options.add(i, option);

        }
        Log.e(TAG, "initResourceDataIntoBaiduMap: initMoniter4");
        optionsAllList.addAll(options);
        mBaiduMap.addOverlays(options);
    }

    private void initResourceCemeteryDataIntoBaiduMap() {
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        for (int i = 0; i < cemeteryDTOList.size(); i++) {
            Matrix matrix = new Matrix();
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_cemetery1)).getBitmap();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            BitmapDescriptor marker = BitmapDescriptorFactory.fromBitmap(bitmap);
            double[] doubles = LatLngChangeNew.calWGS84toBD09(cemeteryDTOList.get(i).getPosition().getLat(), cemeteryDTOList.get(i).getPosition().getLng());
            com.baidu.mapapi.model.LatLng point = new com.baidu.mapapi.model.LatLng(doubles[0], doubles[1]);
            Bundle bundle = new Bundle();
            bundle.putString("id", cemeteryDTOList.get(i).getId());
            bundle.putInt("type", RESOURCE_MONITOR);
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .extraInfo(bundle)
                    .icon(marker);
            options.add(i, option);

        }
        Log.e(TAG, "initResourceDataIntoBaiduMap: initMoniter4");
        optionsAllList.addAll(options);
        mBaiduMap.addOverlays(options);
    }

    private void initResourceMonitoerDataIntoBaiduMap() {
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        for (int i = 0; i < monitorDTOList.size(); i++) {
            Matrix matrix = new Matrix();
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_monitor_haiyu)).getBitmap();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            BitmapDescriptor marker = BitmapDescriptorFactory.fromBitmap(bitmap);
            double[] doubles = LatLngChangeNew.calWGS84toBD09(monitorDTOList.get(i).getPosition().getLat(), monitorDTOList.get(i).getPosition().getLng());
            com.baidu.mapapi.model.LatLng point = new com.baidu.mapapi.model.LatLng(doubles[0], doubles[1]);
            Bundle bundle = new Bundle();
            bundle.putString("id", monitorDTOList.get(i).getId());
            bundle.putInt("type", RESOURCE_MONITOR);
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .extraInfo(bundle)
                    .icon(marker);
            options.add(i, option);
           /* if (monitorDTOList.get(i).getMonitorType() == 1) {
                Matrix matrix = new Matrix();
                Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_monitor_senlin)).getBitmap();
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                BitmapDescriptor marker = BitmapDescriptorFactory.fromBitmap(bitmap);
                double[] doubles = LatLngChangeNew.calWGS84toBD09(monitorDTOList.get(i).getPosition().getLat(), monitorDTOList.get(i).getPosition().getLng());
                com.baidu.mapapi.model.LatLng point = new com.baidu.mapapi.model.LatLng(doubles[0], doubles[1]);
                Bundle bundle = new Bundle();
                bundle.putString("id", monitorDTOList.get(i).getId());
                bundle.putInt("type", RESOURCE_MONITOR);
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .extraInfo(bundle)
                        .icon(marker);
                options.add(i, option);
            } else if (monitorDTOList.get(i).getMonitorType() == 2) {
                Matrix matrix = new Matrix();
                Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_monitor_shashi)).getBitmap();
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                BitmapDescriptor marker = BitmapDescriptorFactory.fromBitmap(bitmap);
                double[] doubles = LatLngChangeNew.calWGS84toBD09(monitorDTOList.get(i).getPosition().getLat(), monitorDTOList.get(i).getPosition().getLng());
                com.baidu.mapapi.model.LatLng point = new com.baidu.mapapi.model.LatLng(doubles[0], doubles[1]);
                Bundle bundle = new Bundle();
                bundle.putString("id", monitorDTOList.get(i).getId());
                bundle.putInt("type", RESOURCE_MONITOR);
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .extraInfo(bundle)
                        .icon(marker);
                options.add(i, option);
            } else if (monitorDTOList.get(i).getMonitorType() == 3) {
                Matrix matrix = new Matrix();
                Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_monitor_haiyu)).getBitmap();
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                BitmapDescriptor marker = BitmapDescriptorFactory.fromBitmap(bitmap);
                double[] doubles = LatLngChangeNew.calWGS84toBD09(monitorDTOList.get(i).getPosition().getLat(), monitorDTOList.get(i).getPosition().getLng());
                com.baidu.mapapi.model.LatLng point = new com.baidu.mapapi.model.LatLng(doubles[0], doubles[1]);
                Bundle bundle = new Bundle();
                bundle.putString("id", monitorDTOList.get(i).getId());
                bundle.putInt("type", RESOURCE_MONITOR);
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .extraInfo(bundle)
                        .icon(marker);
                options.add(i, option);
            }*/
            Log.e(TAG, "initResourceDataIntoBaiduMap: initMoniter3");

        }
        Log.e(TAG, "initResourceDataIntoBaiduMap: initMoniter4");
        optionsAllList.addAll(options);
        mBaiduMap.addOverlays(options);
    }

    /**
     * 获取资源数据
     *
     * @param ApiUrl
     * @param nameresult
     */
    private void getResourceDataFromService(String ApiUrl, String nameresult) {

        showDialogProgress(progressDialog, "加载中...");
        if (nameresult.equals("视频监控点")) {
            resorcetype = "monitor";
        } else if (nameresult.equals("消防专业队")) {
            resorcetype = "team";
        } else if (nameresult.equals("危险源")) {
            resorcetype = "dangerSource";
        } else if (nameresult.equals("物资储备库")) {
            resorcetype = "foreastRoom";
        } else if (nameresult.equals("水源地")) {
            resorcetype = "ic_water_source";
        } else if (nameresult.equals("墓地")) {
            resorcetype = "ic_cemetery";
        } else if (nameresult.equals("瞭望塔")) {
            resorcetype = "watchTower";
        } else if (nameresult.equals("护林检查站")) {
            resorcetype = "ic_check_station";
        } else if (nameresult.equals("森林防火监测中心")) {
            resorcetype = "foreastCenter";
        }
        JSONObject resorcelistobj = new JSONObject();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource" + ApiUrl + "/list");
        params.setAsJsonContent(true);
        params.setBodyContent(resorcelistobj.toString());
        Log.e(TAG, "getResourceList: " + resorcelistobj.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(getContext()).getUser().getToken());
        Log.e(TAG, "resource: --" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "getResourceonSuccess: --1-" + result);
                try {
                    JSONObject json0bject = new JSONObject(result);
                    Log.e("result", json0bject.toString());
                    JSONArray data = json0bject.getJSONArray("data");
                    Gson gson = new Gson();
                    if (resorcetype.equals("monitor")) { //视频监控
                        monitorDTOList.clear();
                        monitorDTOList = gson.fromJson(String.valueOf(data), new TypeToken<List<MonitorDTO>>() {
                        }.getType());
                    } else if (resorcetype.equals("ic_check_station")) {  //护林检查站
                        Log.e(TAG, "onSuccess:checkStationDTOList " );
                        checkStationDTOList.clear();
                        checkStationDTOList = gson.fromJson(String.valueOf(data), new TypeToken<List<CheckStationDTO>>() {
                        }.getType());
                        Log.e(TAG, "onSuccess: " + checkStationDTOList );
                    } else if (resorcetype.equals("ic_cemetery")) {  //墓地
                        cemeteryDTOList.clear();
                        cemeteryDTOList = gson.fromJson(String.valueOf(data), new TypeToken<List<CemeteryDTO>>() {
                        }.getType());
                    } else if (resorcetype.equals("ic_water_source")) {   //水源地
                        waterSourceDTOList.clear();
                        waterSourceDTOList = gson.fromJson(String.valueOf(data), new TypeToken<List<WaterSourceDTO>>() {
                        }.getType());
                    }

                    initResourceDataIntoBaiduMap();
                    Log.e("data1", data.toString());
                   /* dWebView.callHandler("showPointforresource", new Object[]{resorcetype, data, ""}, new OnReturnValue<String>() {
                        @Override
                        public void onValue(String retValue) {
                            Log.d("jsbridge", "call succeed,return value is " + retValue);
                        }
                    });*/
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

    /**
     * 往百度地图上加载资源点
     */
    private void initResourceDataIntoBaiduMap() {
        if (resorcetype.equals("monitor")) {
            Log.e(TAG, "initResourceDataIntoBaiduMap: initMoniter1");
            Log.e(TAG, "initResourceDataIntoBaiduMap: initMoniter2＝" + monitorDTOList.size());
            initResourceMonitoerDataIntoBaiduMap();
        } else if (resorcetype.equals("ic_check_station")) {  //护林检查站
            initResourceCheckStationDataIntoBaiduMap();
        } else if (resorcetype.equals("ic_cemetery")) {  //墓地
            initResourceCemeteryDataIntoBaiduMap();
        } else if (resorcetype.equals("ic_water_source")) {   //水源地
            initResourceWaterSourceDataIntoBaiduMap();

        }


    }




    /**
     * 根据监控点id获取信息
     */
    private void getinfofromid(final String resourceid) {
        showDialogProgress(progressDialog, "加载中...");
        //获取摄像头URL
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("monitorId", resourceid);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/camera/list");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        Log.e(TAG, "getDataFromService: " + jsonObject.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(getContext()).getUser().getToken());
        Log.e(TAG, "resource: --" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        kejianguangUrl ="";
                        kejianguangMId ="";
                        rechengxiangUrl ="";
                        rechengxiangMId ="";
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject dataListsObj = data.getJSONObject(i);
                            if (dataListsObj.getInt("cameraType") == 1) {
                                kejianguangUrl = dataListsObj.getString("id");
                                kejianguangMId = dataListsObj.getString("monitorId");
                                Log.i(TAG, "kejianguangUrl: " + kejianguangUrl);
                            } else {
                                rechengxiangUrl = dataListsObj.getString("id");
                                rechengxiangMId = dataListsObj.getString("monitorId");
                                Log.i(TAG, "rechengxiangUrl: " + rechengxiangUrl);
                            }
                        }

                        if (kejianguangMId.equals("")){
                            kejianguangbutton.setVisibility(View.GONE);
                        }else {
                            kejianguangbutton.setVisibility(View.VISIBLE);
                        }

                        if (rechengxiangMId.equals("")){
                            rechengxiangbutton.setVisibility(View.GONE);
                        }else {
                            rechengxiangbutton.setVisibility(View.VISIBLE);
                        }

                        //获取详情
                        getShipinReourceXiangqing(resourceid);
                    } else {
                        Toast.makeText(getContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    private void getShipinReourceXiangqing(String resourceid) {
        RequestParams infoparams = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/monitor");
        infoparams.addHeader("Authorization", "bearer " + new DbConfig(getContext()).getUser().getToken());
        infoparams.addParameter("id", resourceid);
        infoparams.addHeader("infoAuthorization", "bearer " + new DbConfig(getContext()).getUser().getToken());
        Log.e(TAG, "inforesource: --" + infoparams);
        x.http().get(infoparams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "infoonSuccess: --1-" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        JSONObject dataListsObj = data.getJSONObject(0);

                        resourcenameview.setText(dataListsObj.getString("name"));
                        if (dataListsObj.getString("districtName").equals("null") || dataListsObj.getString("districtName").isEmpty()) {
                            if (dataListsObj.getString("streetName").equals("null") || dataListsObj.getString("streetName").isEmpty()){
                                resoucedizhiview.setText("暂无详细地址");
                            }else {
                                resoucedizhiview.setText(dataListsObj.getString("streetName"));
                            }

                        } else {
                            resoucedizhiview.setText(dataListsObj.getString("districtName") + dataListsObj.getString("streetName"));
                        }
                        resourceTypeView.setText("森林防火");


                        String positionStr = dataListsObj.getString("position");
                        Log.e("position1", positionStr);
                        JSONObject positionObj = new JSONObject(positionStr);
                        resourcejingweiduview.setText(positionObj.getString("lng") + "，" + positionObj.getString("lat"));
                        resourceinfoDialog.show();

                    } else {
                        Toast.makeText(getContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
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
            Toast.makeText(getContext(), "未开启本应用地理位置信息，请先开启！", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}