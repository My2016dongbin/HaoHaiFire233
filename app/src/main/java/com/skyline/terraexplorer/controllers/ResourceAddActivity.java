package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.skyline.teapi.ISGWorld;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.db.Grid;
import com.skyline.terraexplorer.db.Resource;
import com.skyline.terraexplorer.db.User;
import com.skyline.terraexplorer.models.ImageLabel;
import com.skyline.terraexplorer.models.ImageLabels;
import com.skyline.terraexplorer.models.SaveData;
import com.skyline.terraexplorer.multitype.ChooseImage;
import com.skyline.terraexplorer.multitype.ChooseImageViewBinder;
import com.skyline.terraexplorer.utils.GifSizeFilter;
import com.skyline.terraexplorer.utils.ImagPagerUtil;
import com.skyline.terraexplorer.utils.ImageUtils;
import com.skyline.terraexplorer.utils.LatLngChangeNew;
import com.skyline.terraexplorer.utils.RequestUtils;
import com.skyline.terraexplorer.utils.RxViewAction;
import com.skyline.terraexplorer.views.MessagePicturesLayout;
import com.skyline.terraexplorer.views.WheelView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ResourceAddActivity extends HBaseActivity implements ChooseImageViewBinder.OnChooseImageClickListener ,MessagePicturesLayout.Callback{

    private static final String TAG = ResourceAddActivity.class.getSimpleName();
    private LinearLayout leixingLayout;
    private TextView leixingText;
    private List<Resource> resourceList;
    private List<String> resourceStrList;
    public static final int MAP_REUEST_CODE = 2;
    private String currentLng;
    private String currentLat;
    private ImageView toMapView;
    private LocationClient mLocationClient;
    private double latitude_double;
    private double longitude_double;
    private EditText nameEditView;
    private EditText addressEditView;
    private EditText lngEdit;
    private EditText latEdit;
    private EditText wanggeEdit;
    private EditText miaoshuEdit;
    private EditText zerenrenEdir;
    private EditText phoneEdit;
    private WheelView areaWy;
    private int leixingSelectIndex = 0;
    private String currentChooseLeixing = "";
    private ImageView backButton;

    private LinearLayout quLayout;
    private TextView quText;
    private LinearLayout jiedaoLayout;
    private TextView jiedaoText;
    public List<Grid> quList;
    public List<String> quStrList;
    public List<Grid> jiedaoList;
    public List<String> jiedaoStrList;
    public int currentQuanxian = 0;   //0是全国权限  1是省级权限
    public int currentChooseArea = 0;  //当前在选择区还是街道   0选择区  1选择街道 2选择社区
    public String currentChooseQu = "";
    public String currentChooseJiedao = "";
    public int quSelectIndex = 0;
    public int jieDaoSelectIndex = 0;
    public int shequSelectIndex = 0;
    public boolean isChooseQu = false;
    public String currentQuId;
    public String currentStreeNo;
    public String checkTime;
    private TextView addButton;
    private ProgressDialog progressDialog;
    private TextView dangqianView;
    private boolean isMapChoose = false;
    private String api_url;
    private LinearLayout jianchazhanLayout;
    private TextView hulinfangView;
    private TextView jianchazhanView;
    private Switch zhibanSwitch;
    private EditText renyuanEdit;
    private LinearLayout shuiyuandiLayout;
    private EditText xushuiliangEdit;
    private int currentJianChazhanType = 1;     //  1是检查站 2是护林房

    private FrameLayout oneImageLayout;
    private ImageView oneImage;
    private ImageView oneImageDelete;
    public List<Uri> uriChooseList;
    public List<Uri> uriOtherChooseList;
    private static final int REQUEST_CODE_CHOOSE = 23;

    public boolean isHasPermission = true;
    private List<ChooseImage> list = new ArrayList<>();
    private Bitmap picOne;
    private String pic1;
    private String fullPath;
    private boolean hasPic = false;
    private LinearLayout shequLayout;
    private TextView shequText;
    public List<Grid> shequList;
    public List<String> shequStrList;

    public String currentSheQuId;
    public String currentChooseSheQu = "";
    private Switch jiankongSwitch;
    private EditText renyuanNameEdit;
    private EditText shanxiEdit;
    private EditText fengliMeihuojiEdit;
    private EditText meihuoshuiqiangEdit;
    private EditText erhaoGongjuEdit;
    private EditText qitaGongjuEdit;
    private Switch zhishengjiSwitch;
    private TextView shuiyuandiTypeView;
    public List<String> shuiyuandiTypeList;
    private int shuiyuandiSelectIndex;
    private String currentShuiyuandiLeixing;
    public String shuiyuandiType = "0";
    private LinearLayout shuiyuandiTypeLayout;
    private LinearLayout zhuanyeduiLayout;
    private EditText duiwurenshuEdit;
    private EditText zhibanTeamEdit;
    private EditText duiwurenshuTeamEdit;
    private EditText xiaofangcheTeamEdit;
    private EditText yunbingcheTeamEdit;
    private EditText zhihuicheTeamEdit;
    private EditText gaoyashuibengTeamEdit;
    private EditText fenglimeihuojiTeamEdit;
    private EditText erhaogongjuTeamEdit;
    private EditText meihuoshuiqiangTeamEdit;
    private EditText duijiangjiTeamEdit;
    private EditText zhuangbeiyunshuTeamEdit;
    private EditText yingfangmianjiTeamEdit;
    private EditText shuiguancheTeamEdit;
    private TextView zhuanyeduitype0View;
    private TextView zhuanyeduitype1View;
    private int currentZhuanyeduiType = 0;     //  0是区市级专业队 1是街镇级专业队
    private LinearLayout wuzikuLayout;
    private EditText fenglimeihuojiMrEdit;
    private EditText gaoyaxishuiMrEdit;
    private EditText gaoyashuibengMrEdit;
    private EditText erhaogongjuMrEdit;
    private EditText meihuoshuiqiangMrEdit;
    private EditText youjuMrEdit;
    private EditText geguanjiMrEdit;
    private EditText huochangqiegejiMrEdit;
    private EditText fanghuofuMrEdit;
    private EditText fanghuoshoutaoMrEdit;
    private EditText fanghuotoukuiMrEdit;
    private EditText fanghuoxieMrEdit;
    private EditText shuidaiMrEdit;
    private EditText shuinangMrEdit;
    private EditText youtongMrEdit;
    private TextView wuzikuType0View;
    private TextView wuzikuType1View;
    private TextView wuzikuType2View;
    private int currentWuzikuType = 0;     //  0市级、1区市级、2街镇级
    private TextView jiancezhongxin0View;
    private TextView jiancezhongxin1View;
    private TextView jiancezhongxin2View;
    private LinearLayout jiancezhongxinLayout;
    private int currentJiancezhongxinType = 0;     //  0是区市级专业队 1是街镇级专业队
    private LinearLayout liaowangtaLayout;
    private EditText jiancefanweiEdit;
    private LinearLayout shipinjiankongLayout;
    private TextView jiankong0View;
    private TextView jiankong1View;
    private TextView jiankong2View;
    private EditText jiancefanweiMEdit;
    private Switch lianwangSwitch;
    private Switch zhinengkakouSwitch;
    private int currentshipinjiankongType = 0;     // 0高山远程监控、1进山出入口监控、2墓地监控
    private LinearLayout mudiLayout;
    private TextView mudi0View;
    private TextView mudi1View;
    private EditText fentouEdit;
    private int currentmudiType = 0;     // 0公墓、1集中墓地
    private LinearLayout weixianyuanLayout;
    private Switch zhongdaweixianyuanSwitch;

    private RecyclerView photoListView;
    private MultiTypeAdapter photoAdapter;
    private List<Object> photoItems = new ArrayList<>();
    private ChooseImageViewBinder chooseImageViewBinder;
    private int addPicType = 0;  //0是资源点照片  1是其他照片

    private String otherPicStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resource);
        uriChooseList = new ArrayList<>();
        uriOtherChooseList = new ArrayList<>();
        resourceList = new ArrayList<>();
        resourceStrList = new ArrayList<>();
        quList = new ArrayList<>();
        quStrList = new ArrayList<>();
        jiedaoList = new ArrayList<>();
        jiedaoStrList = new ArrayList<>();
        shequList=new ArrayList<>();
        shequStrList=new ArrayList<>();
        shuiyuandiTypeList=new ArrayList<>();
        list=new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        currentLng = intent.getStringExtra("LNG");
        currentLat = intent.getStringExtra("LAT");
        api_url = intent.getStringExtra("API_URL");

        Log.e(TAG, "onCreate: " +currentLng);
        Log.e(TAG, "onCreate: " +currentLat);
        Log.e(TAG, "onCreate: api_url =  " +api_url);

        initView();
        getResourcesFromDb();

        initLatLng();

        //配置点击查看大图
        initImageLoader();
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

    private void initLatLng() {

        //获取经纬度
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);                                //打开gps
        option.setCoorType("bd09ll");                           //设置坐标类型为bd09ll 百度需要的坐标，也可以返回其他type类型
        option.setPriority(LocationClientOption.NetWorkFirst);  //设置网络优先
//        option.setScanSpan(50000);                               //定时定位，每隔5秒钟定位一次。这个就看大家的需求了

        mLocationClient.setLocOption(option);
        mLocationClient.start();//这句代码百度api上给的没有，没有这个代码下面的回调方法不会执行的

        mLocationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //  这里可以获取经纬度，这是回调方法
/*                //默认天安数码城: latitude: 36.32087806111286, longitude: 120.44349123197962    (判断没有定位授权时为默认)
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED || (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
                    longitude_double = 120.44349123197962;
                    latitude_double = 36.32087806111286;
                } else {*/
                longitude_double = bdLocation.getLongitude();
                latitude_double = bdLocation.getLatitude();
/*                }*/
                //   Log.e(TAG, "registerclick11111: " + "longitude_double" + longitude_double + "latitude_double" + latitude_double);
            }
        });
    }

    /**
     * 从本地数据库获取资源数据
     */
    private void getResourcesFromDb() {
        resourceList = new DbConfig(this).getResourceList();
        resourceStrList.add("请选择类型");
        resourceStrList.add("护林检查站");
        resourceStrList.add("消防专业队");
        resourceStrList.add("物资储备库");
        resourceStrList.add("视频监控点");
        resourceStrList.add("水源地");
        resourceStrList.add("森林防火监测中心");
        resourceStrList.add("瞭望塔");
        resourceStrList.add("墓地");
        resourceStrList.add("危险源");
        resourceStrList.add("直升机机降点");
        /*for (int i = 0; i < resourceList.size(); i++) {
            resourceStrList.add(resourceList.get(i).getName());
        }*/
    }

    private void initView() {
        //9tu
        photoListView = (RecyclerView) findViewById(R.id.phote_recycle);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        photoListView.setLayoutManager(gridLayoutManager);
        photoAdapter = new MultiTypeAdapter(photoItems);
        photoListView.setHasFixedSize(true);
        photoListView.setNestedScrollingEnabled(false);
        chooseImageViewBinder = new ChooseImageViewBinder(this);
        chooseImageViewBinder.setListener(this);
        photoAdapter.register(ChooseImage.class, chooseImageViewBinder);
        photoListView.setAdapter(photoAdapter);
        assertHasTheSameAdapter(photoListView, photoAdapter);
        updateData();

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //检查站布局
        jianchazhanLayout = (LinearLayout) findViewById(R.id.jianchazhan_layout);
        hulinfangView = (TextView) findViewById(R.id.hulinfang_view);
        jianchazhanView = (TextView) findViewById(R.id.jianchazhan_view);
        zhibanSwitch = (Switch) findViewById(R.id.zhiban_switch);
        jiankongSwitch = (Switch) findViewById(R.id.jiankong_swith);
        renyuanEdit = (EditText) findViewById(R.id.renyuan_edit);
        renyuanNameEdit = (EditText) findViewById(R.id.renyuan_name_edit);
        shanxiEdit = (EditText) findViewById(R.id.shanxi_edit);
        fengliMeihuojiEdit = (EditText) findViewById(R.id.fenglimeihuoji_edit);
        meihuoshuiqiangEdit = (EditText) findViewById(R.id.meihuoshuiqiang_edit);
        erhaoGongjuEdit = (EditText) findViewById(R.id.erhaogongju_edit);
        qitaGongjuEdit = (EditText) findViewById(R.id.qitagongju_edit);

        //水源地布局
        shuiyuandiLayout = (LinearLayout) findViewById(R.id.shuiyuandi_layout);
        shuiyuandiTypeLayout = (LinearLayout) findViewById(R.id.shuiyuandi_type_layout);
        xushuiliangEdit = (EditText) findViewById(R.id.xushuiliang_view);
        zhishengjiSwitch = (Switch) findViewById(R.id.zhishengjiqushui_switch);
        initShuiyuandiTypeData();
        shuiyuandiTypeView = (TextView) findViewById(R.id.shuiyaundi_type_view);

        //专业队布局
        zhuanyeduiLayout = (LinearLayout) findViewById(R.id.zhuanyedui_layout);
        zhuanyeduitype0View = (TextView) findViewById(R.id.zhuanyeduitype0_view);
        zhuanyeduitype1View = (TextView) findViewById(R.id.zhuanyeduitype1_view);
        duiwurenshuTeamEdit = (EditText) findViewById(R.id.duiwurenshu_team_edit);
        zhibanTeamEdit = (EditText) findViewById(R.id.zhiban_team_edit);
        xiaofangcheTeamEdit = (EditText) findViewById(R.id.xiaofangche_team_edit);
        yunbingcheTeamEdit = (EditText) findViewById(R.id.yunbingche_team_edit);
        zhihuicheTeamEdit = (EditText) findViewById(R.id.zhihuiche_team_edit);
        gaoyashuibengTeamEdit = (EditText) findViewById(R.id.gaoyashuibeng_team_edit);
        fenglimeihuojiTeamEdit = (EditText) findViewById(R.id.fenglimeihuoji_team_edit);
        erhaogongjuTeamEdit = (EditText) findViewById(R.id.erhaogongju_team_edit);
        meihuoshuiqiangTeamEdit = (EditText) findViewById(R.id.meihuoshuiqiang_team_edit);
        duijiangjiTeamEdit = (EditText) findViewById(R.id.duijiangji_team_edit);
        zhuangbeiyunshuTeamEdit = (EditText) findViewById(R.id.zhuangbeiyunshu_team_edit);
        yingfangmianjiTeamEdit = (EditText) findViewById(R.id.yingfangmianji_team_edit);
        shuiguancheTeamEdit = (EditText) findViewById(R.id.shuiguanche_team_edit);
        //物资库
        wuzikuLayout = (LinearLayout) findViewById(R.id.wuziku_layout);
        wuzikuType0View = (TextView) findViewById(R.id.wuzikutype0_view);
        wuzikuType1View = (TextView) findViewById(R.id.wuzikutype1_view);
        wuzikuType2View = (TextView) findViewById(R.id.wuzikutype2_view);
        fenglimeihuojiMrEdit = (EditText) findViewById(R.id.fenglimeihuoji_mr_edit);
        gaoyaxishuiMrEdit = (EditText) findViewById(R.id.gaoyaxishui_mr_edit);
        gaoyashuibengMrEdit = (EditText) findViewById(R.id.gaoyashuibeng_mr_edit);
        erhaogongjuMrEdit = (EditText) findViewById(R.id.erhaogongju_mr_edit);
        meihuoshuiqiangMrEdit = (EditText) findViewById(R.id.meihuoshuiqiang_mr_edit);
        youjuMrEdit = (EditText) findViewById(R.id.youju_mr_edit);
        geguanjiMrEdit = (EditText) findViewById(R.id.geguanji_mr_edit);
        huochangqiegejiMrEdit = (EditText) findViewById(R.id.huochangqiegeji_mr_edit);
        fanghuofuMrEdit = (EditText) findViewById(R.id.fanghuofu_mr_edit);
        fanghuoshoutaoMrEdit = (EditText) findViewById(R.id.fanghuoshoutao_mr_edit);
        fanghuotoukuiMrEdit = (EditText) findViewById(R.id.fanghuotoukui_mr_edit);
        fanghuoxieMrEdit = (EditText) findViewById(R.id.fanghuoxie_mr_edit);
        shuidaiMrEdit = (EditText) findViewById(R.id.shuidai_mr_edit);
        shuinangMrEdit = (EditText) findViewById(R.id.shuinang_mr_edit);
        youtongMrEdit = (EditText) findViewById(R.id.youtong_mr_edit);

        //森林防火检测中心 指挥部
        jiancezhongxinLayout = (LinearLayout) findViewById(R.id.jiancezhongxin_layout);
        jiancezhongxin0View = (TextView) findViewById(R.id.jiancezhongxin0_view);
        jiancezhongxin1View = (TextView) findViewById(R.id.jiancezhongxin1_view);
        jiancezhongxin2View = (TextView) findViewById(R.id.jiancezhongxin2_view);

        //瞭望塔
        liaowangtaLayout = (LinearLayout) findViewById(R.id.liaowangta_layout);
        jiancefanweiEdit = (EditText) findViewById(R.id.jiancefanwei_edit);

        //视频监控点
        shipinjiankongLayout = (LinearLayout) findViewById(R.id.shipinjiankong_layout);
        jiankong0View = (TextView) findViewById(R.id.jiankong0_view);
        jiankong1View = (TextView) findViewById(R.id.jiankong1_view);
        jiankong2View = (TextView) findViewById(R.id.jiankong2_view);
        jiancefanweiMEdit = (EditText) findViewById(R.id.jiancefanwei_m_edit);
        lianwangSwitch = (Switch) findViewById(R.id.lianwang_m_switch);
        zhinengkakouSwitch = (Switch) findViewById(R.id.zhinengkakou_m_switch);
        //墓地
        mudiLayout = (LinearLayout) findViewById(R.id.mudi_layout);
        mudi0View = (TextView) findViewById(R.id.mudi0_view);
        mudi1View = (TextView) findViewById(R.id.mudi1_view);
        fentouEdit = (EditText) findViewById(R.id.fentou_c_edit);

        //危险源
        weixianyuanLayout = (LinearLayout) findViewById(R.id.weixianyuan_layout);
        zhongdaweixianyuanSwitch = (Switch) findViewById(R.id.zhongdaweixianyuan_switch);

        //综合布局
        dangqianView = (TextView) findViewById(R.id.dangqian_view);
        quLayout = (LinearLayout) findViewById(R.id.qu_layout);
        quText = (TextView) findViewById(R.id.qu_text);
        jiedaoLayout = (LinearLayout) findViewById(R.id.jiedao_layout);
        jiedaoText = (TextView) findViewById(R.id.jiedao_text);
        shequLayout = (LinearLayout) findViewById(R.id.shequ_layout);
        shequText = (TextView) findViewById(R.id.shequ_text);

        nameEditView = (EditText) findViewById(R.id.name_edit);
        addressEditView = (EditText) findViewById(R.id.address_edit);
        lngEdit = (EditText) findViewById(R.id.lng_edit);
        latEdit = (EditText) findViewById(R.id.lat_edit);
        miaoshuEdit = (EditText) findViewById(R.id.miaoshu_Edit);
        zerenrenEdir = (EditText) findViewById(R.id.zerenren_edit);
        phoneEdit = (EditText) findViewById(R.id.phone_edit);

        leixingLayout = (LinearLayout) findViewById(R.id.leixing_layout);
        leixingText = (TextView) findViewById(R.id.leixing_view);
        toMapView = (ImageView) findViewById(R.id.to_map_view);

        addButton = (TextView) findViewById(R.id.add_button);

        oneImageLayout = (FrameLayout) findViewById(R.id.one_image_layout);
        oneImage = (ImageView) findViewById(R.id.one_image);
        oneImageDelete = (ImageView) findViewById(R.id.one_image_delete);

        oneImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgePower();
                if (uriChooseList.size() == 0){  //添加图片
                    addPicType = 0;
                    addImage();
                }else {         //查看图片
                    showBigImage();
                }
            }
        });

        oneImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasPic = false;
                uriChooseList.remove(0);
                Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(oneImage);
                oneImageDelete.setVisibility(View.GONE);

            }
        });
        //墓地
        RxViewAction.clickNoDouble(mudi0View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentmudiType = 0;
                        mudi0View.setBackgroundResource(R.drawable.bg_text_lan);
                        mudi0View.setTextColor(getResources().getColor(R.color.c12));
                        mudi1View.setBackgroundResource(R.drawable.bg_text_hui);
                        mudi1View.setTextColor(getResources().getColor(R.color.c6));
                    }
                });

        RxViewAction.clickNoDouble(mudi1View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentmudiType = 1;
                        mudi0View.setBackgroundResource(R.drawable.bg_text_hui);
                        mudi0View.setTextColor(getResources().getColor(R.color.c6));
                        mudi1View.setBackgroundResource(R.drawable.bg_text_lan);
                        mudi1View.setTextColor(getResources().getColor(R.color.c12));
                    }
                });
        //视频监控点
        RxViewAction.clickNoDouble(jiankong0View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentshipinjiankongType = 0;
                        jiankong0View.setBackgroundResource(R.drawable.bg_text_lan);
                        jiankong0View.setTextColor(getResources().getColor(R.color.c12));
                        jiankong1View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiankong1View.setTextColor(getResources().getColor(R.color.c6));
                        jiankong2View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiankong2View.setTextColor(getResources().getColor(R.color.c6));
                    }
                });

        RxViewAction.clickNoDouble(jiankong1View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentshipinjiankongType = 1;
                        jiankong0View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiankong0View.setTextColor(getResources().getColor(R.color.c6));
                        jiankong1View.setBackgroundResource(R.drawable.bg_text_lan);
                        jiankong1View.setTextColor(getResources().getColor(R.color.c12));
                        jiancezhongxin2View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiancezhongxin2View.setTextColor(getResources().getColor(R.color.c6));
                    }
                });
        RxViewAction.clickNoDouble(jiankong2View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentshipinjiankongType = 2;
                        jiankong0View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiankong0View.setTextColor(getResources().getColor(R.color.c6));
                        jiankong1View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiankong1View.setTextColor(getResources().getColor(R.color.c6));
                        jiankong2View.setBackgroundResource(R.drawable.bg_text_lan);
                        jiankong2View.setTextColor(getResources().getColor(R.color.c12));
                    }
                });
        //森林防火检测中心 指挥部
        RxViewAction.clickNoDouble(jiancezhongxin0View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentJiancezhongxinType = 0;
                        jiancezhongxin0View.setBackgroundResource(R.drawable.bg_text_lan);
                        jiancezhongxin0View.setTextColor(getResources().getColor(R.color.c12));
                        jiancezhongxin1View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiancezhongxin1View.setTextColor(getResources().getColor(R.color.c6));
                        jiancezhongxin2View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiancezhongxin2View.setTextColor(getResources().getColor(R.color.c6));
                    }
                });

        RxViewAction.clickNoDouble(jiancezhongxin1View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentJiancezhongxinType = 1;
                        jiancezhongxin0View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiancezhongxin0View.setTextColor(getResources().getColor(R.color.c6));
                        jiancezhongxin1View.setBackgroundResource(R.drawable.bg_text_lan);
                        jiancezhongxin1View.setTextColor(getResources().getColor(R.color.c12));
                        jiancezhongxin2View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiancezhongxin2View.setTextColor(getResources().getColor(R.color.c6));
                    }
                });
        RxViewAction.clickNoDouble(jiancezhongxin2View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentJiancezhongxinType = 2;
                        jiancezhongxin0View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiancezhongxin0View.setTextColor(getResources().getColor(R.color.c6));
                        jiancezhongxin1View.setBackgroundResource(R.drawable.bg_text_hui);
                        jiancezhongxin1View.setTextColor(getResources().getColor(R.color.c6));
                        jiancezhongxin2View.setBackgroundResource(R.drawable.bg_text_lan);
                        jiancezhongxin2View.setTextColor(getResources().getColor(R.color.c12));
                    }
                });
        //物资库点击
        RxViewAction.clickNoDouble(wuzikuType0View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentZhuanyeduiType = 0;
                        wuzikuType0View.setBackgroundResource(R.drawable.bg_text_lan);
                        wuzikuType0View.setTextColor(getResources().getColor(R.color.c12));
                        wuzikuType1View.setBackgroundResource(R.drawable.bg_text_hui);
                        wuzikuType1View.setTextColor(getResources().getColor(R.color.c6));
                        wuzikuType2View.setBackgroundResource(R.drawable.bg_text_hui);
                        wuzikuType2View.setTextColor(getResources().getColor(R.color.c6));
                    }
                });

        RxViewAction.clickNoDouble(wuzikuType1View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentZhuanyeduiType = 1;
                        wuzikuType0View.setBackgroundResource(R.drawable.bg_text_hui);
                        wuzikuType0View.setTextColor(getResources().getColor(R.color.c6));
                        wuzikuType1View.setBackgroundResource(R.drawable.bg_text_lan);
                        wuzikuType1View.setTextColor(getResources().getColor(R.color.c12));
                        wuzikuType2View.setBackgroundResource(R.drawable.bg_text_hui);
                        wuzikuType2View.setTextColor(getResources().getColor(R.color.c6));
                    }
                });
        RxViewAction.clickNoDouble(wuzikuType2View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentZhuanyeduiType = 2;
                        wuzikuType0View.setBackgroundResource(R.drawable.bg_text_hui);
                        wuzikuType0View.setTextColor(getResources().getColor(R.color.c6));
                        wuzikuType1View.setBackgroundResource(R.drawable.bg_text_hui);
                        wuzikuType1View.setTextColor(getResources().getColor(R.color.c6));
                        wuzikuType2View.setBackgroundResource(R.drawable.bg_text_lan);
                        wuzikuType2View.setTextColor(getResources().getColor(R.color.c12));
                    }
                });
        //专业队点击
        RxViewAction.clickNoDouble(zhuanyeduitype0View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentZhuanyeduiType = 0;
                        zhuanyeduitype0View.setBackgroundResource(R.drawable.bg_text_lan);
                        zhuanyeduitype0View.setTextColor(getResources().getColor(R.color.c12));
                        zhuanyeduitype1View.setBackgroundResource(R.drawable.bg_text_hui);
                        zhuanyeduitype1View.setTextColor(getResources().getColor(R.color.c6));
                    }
                });

        RxViewAction.clickNoDouble(zhuanyeduitype1View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentZhuanyeduiType = 1;
                        zhuanyeduitype0View.setBackgroundResource(R.drawable.bg_text_hui);
                        zhuanyeduitype0View.setTextColor(getResources().getColor(R.color.c6));
                        zhuanyeduitype1View.setBackgroundResource(R.drawable.bg_text_lan);
                        zhuanyeduitype1View.setTextColor(getResources().getColor(R.color.c12));
                    }
                });

        //水源地点击
        RxViewAction.clickNoDouble(shuiyuandiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showShuiyuandiTypeDialgo();
                    }
                });
        //检查站点击
        RxViewAction.clickNoDouble(hulinfangView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentJianChazhanType = 2;
                        hulinfangView.setBackgroundResource(R.drawable.bg_text_lan);
                        hulinfangView.setTextColor(getResources().getColor(R.color.c12));
                        jianchazhanView.setBackgroundResource(R.drawable.bg_text_hui);
                        jianchazhanView.setTextColor(getResources().getColor(R.color.c6));
                    }
                });
        RxViewAction.clickNoDouble(jianchazhanView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentJianChazhanType = 1;
                        hulinfangView.setBackgroundResource(R.drawable.bg_text_hui);
                        hulinfangView.setTextColor(getResources().getColor(R.color.c6));
                        jianchazhanView.setBackgroundResource(R.drawable.bg_text_lan);
                        jianchazhanView.setTextColor(getResources().getColor(R.color.c12));
                    }
                });


        RxViewAction.clickNoDouble(dangqianView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isMapChoose = false;
                        getLocation();
                    }
                });

        RxViewAction.clickNoDouble(addButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (leixingText.getText().toString().equals("请选择类型")) {
                            Toast.makeText(getApplicationContext(), "请选择资源点类型", Toast.LENGTH_SHORT).show();
                            return;
                        }
                      /*  if (jiedaoText.getText().toString().equals("请选择街道")) {
                            Toast.makeText(getApplicationContext(), "请选择网格信息", Toast.LENGTH_SHORT).show();
                            return;
                        }*/
                        if (nameEditView.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "请选择资源点名称", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (addressEditView.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "请选择地址", Toast.LENGTH_SHORT).show();
                            return;
                        }

                  /*      if (miaoshuEdit.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "请输入描述信息", Toast.LENGTH_SHORT).show();
                            return;
                        }*/

                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

                        checkTime = dateFormat.format(date);
                        showDialogProgress(progressDialog,"正在上传");
                        if (uriChooseList.size() > 0){
                            postPicToService();
                        }else {
                            if (list.size()>0){
                                postOtherPicToService();
                            }else {
                                addResourceToService();
                            }
                        }

                    }
                });

        RxViewAction.clickNoDouble(toMapView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: " + currentLng);
                        Log.e(TAG, "call: " + currentLat);
                        Intent intent = new Intent(getApplicationContext(), BaiduiMapActivity.class);
                        intent.putExtra("longitude_double",longitude_double);
                        intent.putExtra("latitude_double", latitude_double);
                        startActivityForResult(intent, MAP_REUEST_CODE);
                    }
                });


        RxViewAction.clickNoDouble(leixingLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                       showResourceDialgo();
                    }
                });

        RxViewAction.clickNoDouble(quLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 0;
                        getAllQu();
                        showAreaDialog(quStrList);
                    }
                });
        RxViewAction.clickNoDouble(jiedaoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 1;
                        for (int i = 0; i < quList.size(); i++) {
                            if (quList.get(i).getName().equals(currentChooseQu)) {
                                currentQuId = quList.get(i).getId();
                            }
                        }
                        if (quText.getText().equals("请选择区")){
                            Toast.makeText(ResourceAddActivity.this, "请先选择区", Toast.LENGTH_SHORT).show();

                        }else {
                            getAllJieDao();
                        }

                    }
                });
        RxViewAction.clickNoDouble(shequLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 2;
                        for (int i = 0; i < jiedaoList.size(); i++) {
                            if (jiedaoList.get(i).getName().equals(currentChooseJiedao)) {
                                currentSheQuId = jiedaoList.get(i).getId();
                            }
                        }
                        if (jiedaoText.getText().equals("请选择街道")){
                            Toast.makeText(ResourceAddActivity.this, "请先选择街道", Toast.LENGTH_SHORT).show();

                        }else {
                            getAllSheQu();
                        }

                    }
                });
    }

    /**
     * 提交其他照片到服务器
     */
    private void postOtherPicToService() {
        RequestParams params = new RequestParams(RequestUtils.OTHER_IAMGE_URL + "oa/api/workReport/fileUploadAn");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要

        //    File[] fileData = new File[list.size()];
        for (int i = 0; i < list.size(); i++) {
            try {

                Log.e(TAG, "postPicToService: " + i);
                Uri uri = list.get(i).getUri();
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);

                Bitmap picOne = rotaingImageView(degree, photo);
                String picStr = ImageUtils.savePhoto(picOne, this.getObbDir().getAbsolutePath(), "fileName" + i);
                //  fileData[i] = new  File(picStr);
                params.addBodyParameter("file", new File(picStr),null,picStr);
            } catch (IOException e) {
            }
        }

        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());

        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------------- " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray imgStrArray = data.getJSONArray("img");
                        otherPicStr = data.getString("all");
                        addResourceToService();
                    }else {
                        Toast.makeText(ResourceAddActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" );
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }

    private void initShuiyuandiTypeData() {
        shuiyuandiTypeList.add("水源地类型");
        shuiyuandiTypeList.add("水囊");
        shuiyuandiTypeList.add("水罐");
        shuiyuandiTypeList.add("蓄水池");
        shuiyuandiTypeList.add("塘坝");
        shuiyuandiTypeList.add("水库");
        shuiyuandiTypeList.add("水箱");
        shuiyuandiTypeList.add("其他");
    }

    private void postPicToService() {

        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < uriChooseList.size(); i++) {
            try {
                Uri uri = uriChooseList.get(i);
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                picOne = rotaingImageView(degree, photo);

            } catch (IOException e) {

            }
        }

        if (picOne!=null){
            pic1 = ImageUtils.savePhoto(this.picOne, this.getObbDir().getAbsolutePath(),checkTime + "pic1");

        }



        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic1),null,pic1);
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postPicToService: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------------- " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        fullPath = data.getJSONObject(0).getString("fullPath");
                        hasPic = true;
                        Log.e(TAG, "pic: 1" );
                        if (list.size()>0){
                            postOtherPicToService();
                        }else {
                            addResourceToService();
                        }

                    }else {
                        Toast.makeText(ResourceAddActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
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

    private void showBigImage() {
        ArrayList<String> picList = new ArrayList<>();
        picList.add(uriChooseList.get(0).toString());
        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(ResourceAddActivity.this, picList);
        imagPagerUtil.setContentText("");
        imagPagerUtil.show();
    }

    private void addImage() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        int size = 1- uriChooseList.size();
                        Matisse.from(ResourceAddActivity.this)
                                .choose(MimeType.allOf())
                                .countable(true)
                                .capture(true)
                                .captureStrategy(
                                        new CaptureStrategy(true,"com.skyline.terraexplorer.fileProvider")
                                )
                                .maxSelectable(size)
                                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                .gridExpectedSize(
                                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                .thumbnailScale(0.85f)
                                .imageEngine(new GlideEngine())
                                .forResult(REQUEST_CODE_CHOOSE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 添加资源点
     */
    private void addResourceToService() {
        Log.e(TAG, "pic: 2" );


        Log.e(TAG, "pic: 3" );
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < resourceList.size(); i++) {
                if (resourceList.get(i).getName().equals(leixingText.getText().toString())) {
                    jsonObject.put("resourceType",resourceList.get(i).getCode());
                }
            }

            String quStr = quText.getText().toString();
            jsonObject.put("districtName", quStr);
            for (int i = 0; i < quList.size(); i++) {
                if (quList.get(i).getName().equals(quStr)) {
                    jsonObject.put("districtNo",quList.get(i).getGridNo());
                }
            }

            String jiedaoStr = jiedaoText.getText().toString();
            jsonObject.put("streetName", jiedaoStr);
            for (int i = 0; i < jiedaoList.size(); i++) {
                if (jiedaoList.get(i).getName().equals(jiedaoStr)) {
                    jsonObject.put("streetNo",jiedaoList.get(i).getGridNo());
                }
            }


            jsonObject.put("name",nameEditView.getText().toString());
            if (hasPic){
                jsonObject.put("picture",fullPath);     //上传图片
            }



            JSONObject postsion = new JSONObject();
            postsion.put("lat",Double.parseDouble(latEdit.getText().toString()));
            postsion.put("lng",Double.parseDouble(lngEdit.getText().toString()));
            jsonObject.put("position",postsion); // 整治描述 ,

            jsonObject.put("address",addressEditView.getText().toString());
            jsonObject.put("description",miaoshuEdit.getText().toString());
            jsonObject.put("leaderName",zerenrenEdir.getText().toString());
            jsonObject.put("leaderPhone",phoneEdit.getText().toString());
            if (list.size()>0){
                jsonObject.put("otherPic",otherPicStr);
            }

            if (currentChooseLeixing.equals("护林检查站")){      //护林检查站
                jsonObject.put("resourceType",currentJianChazhanType);

                if (zhibanSwitch.isChecked()){
                    jsonObject.put("isAllday",1);
                }else {
                    jsonObject.put("isAllday",0);
                }
                if (jiankongSwitch.isChecked()){
                    jsonObject.put("hasMonitor",1);
                }else {
                    jsonObject.put("hasMonitor",0);
                }
                jsonObject.put("peopleCount",renyuanEdit.getText().toString());
                jsonObject.put("peopleName",renyuanNameEdit.getText().toString());
                jsonObject.put("mountain",shanxiEdit.getText().toString());
                jsonObject.put("extinguisherCount",fengliMeihuojiEdit.getText().toString());
                jsonObject.put("waterPistolCount",meihuoshuiqiangEdit.getText().toString());
                jsonObject.put("twoToolCount",erhaoGongjuEdit.getText().toString());
                jsonObject.put("otherToolCount",qitaGongjuEdit.getText().toString());

            }else if (currentChooseLeixing.equals("水源地")){      //水源地
                jsonObject.put("waterCapacity",xushuiliangEdit.getText().toString());
                jsonObject.put("resourceType",shuiyuandiType);
                if (zhishengjiSwitch.isChecked()){
                    jsonObject.put("isHelicopterWater",1);
                }else {
                    jsonObject.put("isHelicopterWater",0);
                }
            }else if (currentChooseLeixing.equals("消防专业队")){      //专业队
                jsonObject.put("resourceType",currentZhuanyeduiType);
                jsonObject.put("teamCount",duiwurenshuTeamEdit.getText().toString());
                jsonObject.put("phone",zhibanTeamEdit.getText().toString());
                jsonObject.put("truckCount",xiaofangcheTeamEdit.getText().toString());
                jsonObject.put("troopCarrierCount",yunbingcheTeamEdit.getText().toString());
                jsonObject.put("commandCarCount",zhihuicheTeamEdit.getText().toString());
                jsonObject.put("waterPumpCount",gaoyashuibengTeamEdit.getText().toString());
                jsonObject.put("windFireCount",fenglimeihuojiTeamEdit.getText().toString());
                jsonObject.put("twoToolCount",erhaogongjuTeamEdit.getText().toString());
                jsonObject.put("waterPistolCount",meihuoshuiqiangTeamEdit.getText().toString());
                jsonObject.put("intercomCount",duijiangjiTeamEdit.getText().toString());
                jsonObject.put("equipmentTruckCount",zhuangbeiyunshuTeamEdit.getText().toString());
                jsonObject.put("barracksMeasure",yingfangmianjiTeamEdit.getText().toString());
                jsonObject.put("waterCarCount",shuiguancheTeamEdit.getText().toString());
            }else if (currentChooseLeixing.equals("物资储备库")){      //物资库
                jsonObject.put("resourceType",currentWuzikuType);
                jsonObject.put("windFireCount",fenglimeihuojiMrEdit.getText().toString());
                jsonObject.put("sprayFireCount",gaoyaxishuiMrEdit.getText().toString());
                jsonObject.put("waterPumpCount",gaoyashuibengMrEdit.getText().toString());
                jsonObject.put("twoToolCount",erhaogongjuMrEdit.getText().toString());
                jsonObject.put("waterPistolCount",meihuoshuiqiangMrEdit.getText().toString());
                jsonObject.put("chainSawCount",youjuMrEdit.getText().toString());
                jsonObject.put("bushCutterCount",geguanjiMrEdit.getText().toString());
                jsonObject.put("fireCutterCount",huochangqiegejiMrEdit.getText().toString());
                jsonObject.put("fireproofClothesCount",fanghuofuMrEdit.getText().toString());
                jsonObject.put("glovesCount",fanghuoshoutaoMrEdit.getText().toString());
                jsonObject.put("helmetCount",fanghuotoukuiMrEdit.getText().toString());
                jsonObject.put("shoesCount",fanghuoxieMrEdit.getText().toString());
                jsonObject.put("waterBagCount",shuidaiMrEdit.getText().toString());
                jsonObject.put("waterSacCount",shuinangMrEdit.getText().toString());
                jsonObject.put("oilDrumCount",youtongMrEdit.getText().toString());
            }else if (currentChooseLeixing.equals("森林防火监测中心")){      //森林防火监测中心
                jsonObject.put("resourceType",currentJiancezhongxinType);
            }else if (currentChooseLeixing.equals("瞭望塔")){      //瞭望塔
                jsonObject.put("watchRange",jiancefanweiEdit.getText().toString());
            }else if (currentChooseLeixing.equals("视频监控点")){      //视频监控点
                jsonObject.put("resourceType",currentshipinjiankongType);
                jsonObject.put("monitorRange",jiancefanweiMEdit.getText().toString());
                if (lianwangSwitch.isChecked()){
                    jsonObject.put("isNetworking",1);
                }else {
                    jsonObject.put("isNetworking",0);
                }
                if (zhinengkakouSwitch.isChecked()){
                    jsonObject.put("isIntelligentEntry",1);
                }else {
                    jsonObject.put("isIntelligentEntry",0);
                }
            }else if (currentChooseLeixing.equals("墓地")){      //墓地
                jsonObject.put("resourceType",currentmudiType);
                jsonObject.put("graveCount",fentouEdit.getText().toString());
            }else if (currentChooseLeixing.equals("危险源")){      //危险源

                if (zhongdaweixianyuanSwitch.isChecked()){
                    jsonObject.put("isMajorHazard",1);
                }else {
                    jsonObject.put("isMajorHazard",0);
                }
            }

        } catch (JSONException e) {
        }
        Log.e(TAG, "pic: 4" );
        RequestParams params = null;
        if (currentChooseLeixing.equals("护林检查站")){
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/checkStation");
        }else if (currentChooseLeixing.equals("水源地")){
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/waterSource");
        }else if (currentChooseLeixing.equals("消防专业队")){      //专业队
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/team");
        }else if (currentChooseLeixing.equals("物资储备库")){      //物资库
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/materialRepository");
        }else if (currentChooseLeixing.equals("森林防火监测中心")){      //森林防火监测中心
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/fireCommand");
        }else if (currentChooseLeixing.equals("瞭望塔")){      //瞭望塔
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/watchTower");
        }else if (currentChooseLeixing.equals("视频监控点")){      //视频监控点
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/monitor");
        }else if (currentChooseLeixing.equals("墓地")){      //墓地
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/cemetery");
        }else if (currentChooseLeixing.equals("危险源")){      //危险源
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/dangerSource");
        }else if (currentChooseLeixing.equals("直升机机降点")){      //直升机机降点
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/helicopterPoint");
        }else {
            params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/resourceList/saveResourceBase");
        }
        Log.e(TAG, "pic: 5" );
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        Log.e(TAG, "postData:-- jsonObject.toString()--" + params);
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")){
                        Toast.makeText(ResourceAddActivity.this, "资源点添加成功，重新打开地图后生效", Toast.LENGTH_SHORT).show();

                       /* final List<ImageLabels> t = SaveData.getData(getApplicationContext(), "resource");
                        if (t != null) {
                            List<ImageLabels> imageLabels = t;
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
                        }*/
                        User user = new DbConfig(getApplicationContext()).getUser();
                        user.setDataIsChange(true);
                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        finish();


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
     * 获取所有区的数据
     */
    private void getAllQu() {
        quList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
            quList = db.selector(Grid.class)
                    .where("level", "=", "3")
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

        } catch (DbException e) {
            e.printStackTrace();
        }
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
     * 获取所有社区数据
     */
    private void getAllSheQu() {
        shequList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
            shequList = db.selector(Grid.class)
                    .where("state", "=", "ACTIVE")
                    .where("level", "=", "1")
                    .where("parentid", "=", currentSheQuId)
                    .findAll();
            Log.e(TAG, "getAllSheQu: "+currentSheQuId);
            shequStrList.clear();
            shequStrList.add("请选择社区");
            for (int i = 0; i < shequList.size(); i++) {
                shequStrList.add(shequList.get(i).getName());
            }
            Log.e(TAG, "getAllSheQu2: "+shequStrList);
            showAreaDialog(shequStrList);
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
        }else if (currentChooseArea == 1) {
            areaWy.setItems(strList, jieDaoSelectIndex);//init selected position is 0 初始选中位置为0
        }else  {
            areaWy.setItems(strList, shequSelectIndex);//init selected position is 0 初始选中位置为0
        }

        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                if (currentChooseArea == 0) {   //选择省
                    isChooseQu = true;
                    currentChooseQu = areaWy.getSelectedItem();
                    quSelectIndex = areaWy.getSelectedPosition();
                    quText.setText(currentChooseQu);
                    currentChooseJiedao = "";
                    currentChooseSheQu="";
                    jiedaoText.setText("请选择街道");
                    shequText.setText("请选择社区");
                    jieDaoSelectIndex = 0;
                }else if (currentChooseArea == 2){  //选择社区
                    currentChooseSheQu = areaWy.getSelectedItem();
                    shequSelectIndex = areaWy.getSelectedPosition();
                    shequText.setText(currentChooseSheQu);
                }else {                          //选择市
                    currentChooseJiedao = areaWy.getSelectedItem();
                    jieDaoSelectIndex = areaWy.getSelectedPosition();
                    jiedaoText.setText(currentChooseJiedao);
                    currentChooseSheQu="";
                    shequText.setText("请选择社区");
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


    private void showResourceDialgo() {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        areaWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        areaWy.setIsLoop(false);
        areaWy.setItems(resourceStrList, leixingSelectIndex);//init selected position is 0 初始选中位置为0


        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                    currentChooseLeixing = areaWy.getSelectedItem();
                    leixingSelectIndex = areaWy.getSelectedPosition();
                    leixingText.setText(currentChooseLeixing);
                    jianchazhanLayout.setVisibility(View.GONE);
                    shuiyuandiLayout.setVisibility(View.GONE);
                    zhuanyeduiLayout.setVisibility(View.GONE);
                    wuzikuLayout.setVisibility(View.GONE);
                    jiancezhongxinLayout.setVisibility(View.GONE);
                    liaowangtaLayout.setVisibility(View.GONE);
                    shipinjiankongLayout.setVisibility(View.GONE);
                    mudiLayout.setVisibility(View.GONE);
                    weixianyuanLayout.setVisibility(View.GONE);
                    if (currentChooseLeixing.equals("护林检查站")){
                        jianchazhanLayout.setVisibility(View.VISIBLE);
                    }else if (currentChooseLeixing.equals("水源地")){
                        shuiyuandiLayout.setVisibility(View.VISIBLE);
                    }else if (currentChooseLeixing.equals("消防专业队")){
                        zhuanyeduiLayout.setVisibility(View.VISIBLE);
                    }else if (currentChooseLeixing.equals("物资储备库")){
                        wuzikuLayout.setVisibility(View.VISIBLE);
                    }else if (currentChooseLeixing.equals("森林防火监测中心")){
                        jiancezhongxinLayout.setVisibility(View.VISIBLE);
                    }else if (currentChooseLeixing.equals("瞭望塔")){
                        liaowangtaLayout.setVisibility(View.VISIBLE);
                    }else if (currentChooseLeixing.equals("视频监控点")){
                        shipinjiankongLayout.setVisibility(View.VISIBLE);
                    }else if (currentChooseLeixing.equals("墓地")){
                        mudiLayout.setVisibility(View.VISIBLE);
                    }else if (currentChooseLeixing.equals("危险源")){
                        weixianyuanLayout.setVisibility(View.VISIBLE);
                    }else {
                        jianchazhanLayout.setVisibility(View.GONE);
                        shuiyuandiLayout.setVisibility(View.GONE);
                        zhuanyeduiLayout.setVisibility(View.GONE);
                        wuzikuLayout.setVisibility(View.GONE);
                        jiancezhongxinLayout.setVisibility(View.GONE);
                        liaowangtaLayout.setVisibility(View.GONE);
                        shipinjiankongLayout.setVisibility(View.GONE);
                        mudiLayout.setVisibility(View.GONE);
                        weixianyuanLayout.setVisibility(View.GONE);
                    }

            }
        });
        new AlertDialog.Builder(this)
                .setTitle("请选择区域")
                .setView(areaView)
                .setPositiveButton("确定 ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String areStr = "";
                        String area = areaWy.getSelectedItem();


                    }
                })
                .show();
    }

    private void showShuiyuandiTypeDialgo() {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        areaWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        areaWy.setIsLoop(false);
        areaWy.setItems(shuiyuandiTypeList, shuiyuandiSelectIndex);//init selected position is 0 初始选中位置为0


        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                currentShuiyuandiLeixing = areaWy.getSelectedItem();
                shuiyuandiSelectIndex = areaWy.getSelectedPosition();
                shuiyuandiTypeView.setText(currentShuiyuandiLeixing);
                if (currentShuiyuandiLeixing.equals("水囊")){
                    shuiyuandiType = "1";
                }else if (currentShuiyuandiLeixing.equals("水罐")){
                    shuiyuandiType = "2";
                }else if (currentShuiyuandiLeixing.equals("蓄水池")){
                    shuiyuandiType = "3";
                }else if (currentShuiyuandiLeixing.equals("塘坝")){
                    shuiyuandiType = "4";
                }else if (currentShuiyuandiLeixing.equals("水库")){
                    shuiyuandiType = "5";
                }else if (currentShuiyuandiLeixing.equals("水箱")){
                    shuiyuandiType = "6";
                }else if (currentShuiyuandiLeixing.equals("其他")){
                    shuiyuandiType = "0";
                }else {
                    shuiyuandiType = "0";
                }

            }
        });
        new AlertDialog.Builder(this)
                .setTitle("请选择水源地类型")
                .setView(areaView)
                .setPositiveButton("确定 ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String areStr = "";
                        String area = areaWy.getSelectedItem();


                    }
                })
                .show();
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

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(), "GPS已开启", Toast.LENGTH_SHORT).show();
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

            getAddressData(longitude+"",latitude+"");

        }else {
            //  return "0.00,0.00";
        }
    }

    /**
     * 根据经纬度获取位置
     * @param lng
     * @param lat
     */
    private void getAddressData(final String lng, final String lat) {
        String url = "http://restapi.amap.com/v3/geocode/regeo?output=JSON&location=" + lng + "," + lat +"&key=9432225bac2c179002b3531e31a85410&radius=0&extensions=base";
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(10000);
        Log.e(TAG, "getSyncPlotListData: " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                    String formatted_address = regeocode.getString("formatted_address");
                    JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
                    String district = addressComponent.getString("district");
                    if (!isMapChoose){
                        addressEditView.setText(formatted_address);
                    }

                    lngEdit.setText(lng+"");
                    latEdit.setText(lat+"");
                    quText.setText(district);
                    currentChooseArea = 0;
                    getAllQu();

                    for (int i = 0; i < quStrList.size(); i++) {
                        if (quStrList.get(i).equals(district)) {
                            isChooseQu = true;
                            quSelectIndex = i;
                            currentChooseQu =district;
                            currentChooseJiedao = "";
                            jiedaoText.setText("请选择街道");
                            jieDaoSelectIndex = 0;
                            currentQuId = quList.get(i).getId();

                        }
                    }
                    Log.e(TAG, "onSuccess:带进来的经纬度 " + formatted_address);
                 //   fireAddressView.setText(formatted_address);
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

    private void judgePower() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权相机权限", Toast.LENGTH_SHORT).show();
            finish();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权定位权限", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_REUEST_CODE && resultCode == MAP_REUEST_CODE) {
            String longitude = data.getStringExtra("longitude");
            String latitude = data.getStringExtra("latitude");
            String address = data.getStringExtra("cityAddress");
            LatLngChangeNew latLngChangeNew = new LatLngChangeNew();
            double[] doubles = latLngChangeNew.calBD09toWGS84(Double.parseDouble(latitude), Double.parseDouble(longitude));
          /*  latEdit.setText(doubles[0]+"");
            lngEdit.setText(doubles[1]+"");*/
            addressEditView.setText(address);

            isMapChoose = true;
            getAddressData(doubles[1]+"",doubles[0]+"");

        }else if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            if (addPicType == 0){
                List<Uri> uriList = Matisse.obtainResult(data);
                for (int i = 0; i < uriList.size(); i++) {
                    uriChooseList.add(uriList.get(i));
                }

                oneImageDelete.setVisibility(View.VISIBLE);
                Glide.with(this).load(uriChooseList.get(0)).into(oneImage);
            }else {
                List<Uri> uriList = Matisse.obtainResult(data);

                //去掉重复图片
                int uriSize = uriList.size();
                int listSize = list.size();
                int index = 0;
                for (int i = 0; i < uriList.size(); i++) {
                    for (int j = 0; j < list.size(); j++) {
                        if (uriList.get(i).toString().equals(list.get(j).getUri().toString())) {

                            Toast.makeText(this, "不可添加重复图片！", Toast.LENGTH_SHORT).show();
                            uriList.remove(i);
                            if (uriList.size() == 0) {
                                return;
                            }

                        }
                    }
                }


                // 判断只能添加五张图片
                if ( (uriList.size() + list.size()) > 9){
                    Toast.makeText(this, "最多只能添加9张", Toast.LENGTH_SHORT).show();
                    int size =  9 - list.size();
                    for (int i = 0; i < size; i++) {
                        ChooseImage chooseImage = new ChooseImage();
                        chooseImage.setUri(uriList.get(i));
                        chooseImage.setAdd(false);
                        list.add(chooseImage);
                        // items.add(evaluateImage);
                    }
                }else {
                    //不足5张的添加  添加图片按钮
                    int size = uriList.size();
                    for (int i = 0; i < size; i++) {
                        ChooseImage chooseImage = new ChooseImage();
                        chooseImage.setUri(uriList.get(i));
                        chooseImage.setAdd(false);
                        list.add(chooseImage);
                        // items.add(evaluateImage);
                    }
                    ChooseImage chooseImage = new ChooseImage();
                    chooseImage.setAdd(true);
                }

                updateData();
            }



        }
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    @Override
    public void onImageAddClickListener(boolean add, Uri uri, String id,ChooseImage chooseImage) {

        if (add){
            addPicType = 1;
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            int size = 9 - list.size();
                            Matisse.from(ResourceAddActivity.this)
                                    .choose(MimeType.allOf())
                                    .countable(true)
                                    .capture(true)
                                    .captureStrategy(
                                            new CaptureStrategy(true,"com.skyline.terraexplorer.fileProvider")
                                    )
                                    .maxSelectable(size)
                                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                    .gridExpectedSize(
                                            getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new GlideEngine())
                                    .forResult(REQUEST_CODE_CHOOSE);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            //点击查看大图
            ArrayList<String> picList = new ArrayList<>();
            String oneUri = uri.toString();
            picList.add(oneUri); //点击哪张 把哪张放第一个
            for (int i = 0; i < list.size(); i++) {     //除去点击那张  其他放进去
                if (!oneUri.equals(list.get(i).getUri().toString())){
                    picList.add(list.get(i).getUri().toString());
                }
            };
            String content = "";     //放评论
            ImagPagerUtil imagPagerUtil = new ImagPagerUtil(ResourceAddActivity.this, picList);
            imagPagerUtil.setContentText(content);
            imagPagerUtil.show();
        }
    }

    @Override
    public void onImageDelete(Uri uri, String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUri().equals(uri)) {
                list.remove(i);
            }
        }
        updateData();
    }

    private void updateData() {
        photoItems.clear();
        if (list==null){
            ChooseImage chooseImage = new ChooseImage();
            chooseImage.setAdd(true);
            photoItems.add(chooseImage);
        }else {
            if (list.size()<9){
                for (int i = 0; i < list.size(); i++) {
                    photoItems.add(list.get(i));
                }
                ChooseImage chooseImage = new ChooseImage();
                chooseImage.setAdd(true);
                photoItems.add(chooseImage);
            }else {
                for (int i = 0; i < list.size(); i++) {
                    photoItems.add(list.get(i));
                }
            }

            assertAllRegistered(photoAdapter,photoItems);
            photoAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {

    }
}
