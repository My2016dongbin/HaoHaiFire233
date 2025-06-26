package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.os.Bundle;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.LocationClient;
import com.bumptech.glide.Glide;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.db.Grid;
import com.skyline.terraexplorer.db.Resource;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.models.ImageLabel;
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
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
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
import org.xutils.http.HttpMethod;
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

public class ResourceEditActivity extends HBaseActivity implements ChooseImageViewBinder.OnChooseImageClickListener , MessagePicturesLayout.Callback{

    private static final String TAG = ResourceEditActivity.class.getSimpleName();
    private ImageLabel resource;
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
    private String currentChooseLeixing ;
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
    public int currentChooseArea = 0;  //当前在选择区还是街道   0选择区  1选择街道
    public String currentChooseQu = "";
    public String currentChooseJiedao = "";
    public int quSelectIndex = 0;
    public int jieDaoSelectIndex = 0;
    public boolean isChooseQu = false;
    public String currentQuId;
    public String currentStreeNo;
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
    private Switch jiankongSwitch;
    private EditText renyuanNameEdit;
    private EditText shanxiEdit;
    private EditText fengliMeihuojiEdit;
    private EditText meihuoshuiqiangEdit;
    private EditText erhaoGongjuEdit;
    private EditText qitaGongjuEdit;
    private LinearLayout shuiyuandiLayout;
    private EditText xushuiliangEdit;
    private int currentJianChazhanType = 1;     //  1是检查站 2是护林房

    private FrameLayout oneImageLayout;
    private ImageView oneImage;
    private ImageView oneImageDelete;
    public List<Uri> uriChooseList;
    private static final int REQUEST_CODE_CHOOSE = 23;

    public boolean isHasPermission = true;
    private List<ChooseImage> list = new ArrayList<>();
    private Bitmap picOne;
    private String pic1;
    private String fullPath;
    private int picType = 0;  //0是没有图片  1是有图片从本地选择   2是有图片从服务器上带来的
    public String checkTime;

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
    private TextView zerenrenView;
    private TextView zerenrendianhuaView;

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
    private String[] otherPicStrList;
    private List<ChooseImage> otherPicList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_edit);
        uriChooseList = new ArrayList<>();
        shuiyuandiTypeList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        resource = ((ImageLabel) intent.getSerializableExtra("RESOURCE"));
        list=new ArrayList<>();
        otherPicList=new ArrayList<>();

        Log.e(TAG, "onCreate:apiurl== " +resource.getApiUrl().substring(1,resource.getApiUrl().length()));
        Log.e(TAG, "onCreate:code== " +resource.getCode());
        Log.e(TAG, "onCreate:getGridName== " +resource.getGridName());
        Log.e(TAG, "onCreate:getGridNo== " +resource.getGridNo());
        Log.e(TAG, "onCreate:wanggeStr== " +resource.getWanggeStr());

        initView();

        initData();
        //配置点击查看大图
        initImageLoader();
    }

    private void initData() {
        Log.e(TAG, "initData: getOtherPic" + resource.getOtherPic());
        otherPicStrList = resource.getOtherPic().split(",");

        if (otherPicStrList.length>0){
            for (int i = 0; i < otherPicStrList.length; i++) {
                Log.e(TAG, "initData: " + otherPicStrList[i]);
                ChooseImage chooseImage = new ChooseImage("", null, false, otherPicStrList[i]);
                chooseImage.setNetPic(true);
                otherPicList.add(chooseImage);
            }
            updateData();
        }


        if (!resource.getPicture().equals("null")){
            picType = 2;
            oneImageDelete.setVisibility(View.VISIBLE);
            Glide.with(this).load(RequestUtils.IAMGE_URL + resource.getPicture()).into(oneImage);
        }

        lngEdit.setText(resource.getLongitude()+"");
        latEdit.setText(resource.getLatitude()+"");
        nameEditView.setText(resource.getName());
        addressEditView.setText(resource.getAddress().equals("null")?"":resource.getAddress());
        miaoshuEdit.setText(resource.getDescription().equals("null")?"":resource.getDescription());
        zerenrenEdir.setText(resource.getLeaderName().equals("null")?"":resource.getLeaderName());
        phoneEdit.setText(resource.getLeaderPhone().equals("null")?"":resource.getLeaderPhone());

        if (resource.getCode().equals("checkStation")) {    //显示检查站模块
            jianchazhanLayout.setVisibility(View.VISIBLE);
            renyuanEdit.setText(resource.getPeopleCount().equals("null")?"":resource.getPeopleCount());
            renyuanNameEdit.setText(resource.getPeopleName().equals("null")?"":resource.getPeopleName());
            shanxiEdit.setText(resource.getMountain().equals("null")?"":resource.getMountain());
            fengliMeihuojiEdit.setText(resource.getExtinguisherCount().equals("null")?"":resource.getExtinguisherCount());
            meihuoshuiqiangEdit.setText(resource.getWaterPistolCount().equals("null")?"":resource.getWaterPistolCount());
            erhaoGongjuEdit.setText(resource.getTwoToolCount().equals("null")?"":resource.getTwoToolCount());
            qitaGongjuEdit.setText(resource.getOtherToolCount().equals("null")?"":resource.getOtherToolCount());
            if (resource.getHasMonitor() == null){
                zhibanSwitch.setChecked(false);
            }else {
                if (resource.getHasMonitor().equals("1")){
                    zhibanSwitch.setChecked(true);
                }else {
                    zhibanSwitch.setChecked(false);
                }
            }

            if (resource.getIsAllday() == null){
                jiankongSwitch.setChecked(false);
            }else {
                if (resource.getIsAllday().equals("1")){
                    jiankongSwitch.setChecked(true);
                }else {
                    jiankongSwitch.setChecked(false);
                }
            }
            if (resource.getResourceType().equals("1")) {
                jianchazhanView.setBackgroundResource(R.drawable.bg_text_lan);
                jianchazhanView.setTextColor(getResources().getColor(R.color.c12));
                hulinfangView.setBackgroundResource(R.drawable.bg_text_hui);
                hulinfangView.setTextColor(getResources().getColor(R.color.c6));
            }else {
                jianchazhanView.setBackgroundResource(R.drawable.bg_text_hui);
                jianchazhanView.setTextColor(getResources().getColor(R.color.c6));
                hulinfangView.setBackgroundResource(R.drawable.bg_text_lan);
                hulinfangView.setTextColor(getResources().getColor(R.color.c12));
            }
        }else if (resource.getCode().equals("waterSource")) {   //显示水源地模块
            shuiyuandiLayout.setVisibility(View.VISIBLE);
            xushuiliangEdit.setText(resource.getWaterCapacity().equals("null")?"0":resource.getWaterCapacity());

            if (resource.getIsHelicopterWater() == null){
                zhishengjiSwitch.setChecked(false);
            }else {
                if (resource.getIsHelicopterWater().equals("1")){
                    zhishengjiSwitch.setChecked(true);
                }else {
                    zhishengjiSwitch.setChecked(false);
                }
            }
            if (resource.getResourceType()!=null){
                if (resource.getResourceType().equals("1")){
                    shuiyuandiTypeView.setText("水囊");
                    shuiyuandiSelectIndex = 1;
                }else  if (resource.getResourceType().equals("2")){
                    shuiyuandiTypeView.setText("水罐");
                    shuiyuandiSelectIndex = 2;
                }else  if (resource.getResourceType().equals("3")){
                }else  if (resource.getResourceType().equals("3")){
                    shuiyuandiTypeView.setText("蓄水池");
                    shuiyuandiSelectIndex = 3;
                }else  if (resource.getResourceType().equals("4")){
                    shuiyuandiTypeView.setText("塘坝");
                    shuiyuandiSelectIndex = 4;
                }else  if (resource.getResourceType().equals("5")){
                    shuiyuandiTypeView.setText("水库");
                    shuiyuandiSelectIndex = 5;
                }else  if (resource.getResourceType().equals("6")){
                    shuiyuandiTypeView.setText("水箱");
                    shuiyuandiSelectIndex = 6;
                }else  if (resource.getResourceType().equals("6")){
                    shuiyuandiTypeView.setText("其他");
                    shuiyuandiSelectIndex = 7;
                }else {
                    shuiyuandiSelectIndex = 0;
                }
            }else {
                shuiyuandiTypeView.setText("水源地类型");
                shuiyuandiSelectIndex = 0;
            }

        }else if (resource.getCode().equals("team")) {   //显示专业队
            zerenrenView.setText("队长:");
            zerenrendianhuaView.setText("队长电话:");
            zhuanyeduiLayout.setVisibility(View.VISIBLE);
            duiwurenshuTeamEdit.setText(resource.getTeamCount().equals("null")?"":resource.getTeamCount());
            zhibanTeamEdit.setText(resource.getPhone().equals("null")?"":resource.getPhone());
            xiaofangcheTeamEdit.setText(resource.getTruckCountTeam().equals("null")?"":resource.getTruckCountTeam());
            yunbingcheTeamEdit.setText(resource.getTroopCarrierCount().equals("null")?"":resource.getTroopCarrierCount());
            zhihuicheTeamEdit.setText(resource.getCommandCarCount().equals("null")?"":resource.getCommandCarCount());
            gaoyashuibengTeamEdit.setText(resource.getWaterPumpCount().equals("null")?"":resource.getWaterPumpCount());
            fenglimeihuojiTeamEdit.setText(resource.getWindFireCount().equals("null")?"":resource.getWindFireCount());
            erhaogongjuTeamEdit.setText(resource.getTwoToolCountTeam().equals("null")?"":resource.getTwoToolCountTeam());
            meihuoshuiqiangTeamEdit.setText(resource.getWaterPistolCountTeam().equals("null")?"":resource.getWaterPistolCountTeam());
            duijiangjiTeamEdit.setText(resource.getIntercomCount().equals("null")?"":resource.getIntercomCount());
            zhuangbeiyunshuTeamEdit.setText(resource.getEquipmentTruckCount().equals("null")?"":resource.getEquipmentTruckCount());
            yingfangmianjiTeamEdit.setText(resource.getBarracksMeasure().equals("null")?"":resource.getBarracksMeasure());
            shuiguancheTeamEdit.setText(resource.getWaterCarCount().equals("null")?"":resource.getWaterCarCount());
            if (resource.getResourceType() == null){
                currentZhuanyeduiType = 0;
            }else {
                 if (resource.getResourceType().equals("1")){
                    currentZhuanyeduiType = 1;
                    zhuanyeduitype0View.setBackgroundResource(R.drawable.bg_text_hui);
                    zhuanyeduitype0View.setTextColor(getResources().getColor(R.color.c6));
                    zhuanyeduitype1View.setBackgroundResource(R.drawable.bg_text_lan);
                    zhuanyeduitype1View.setTextColor(getResources().getColor(R.color.c12));
                }else {
                     currentZhuanyeduiType = 0;
                     zhuanyeduitype0View.setBackgroundResource(R.drawable.bg_text_lan);
                     zhuanyeduitype0View.setTextColor(getResources().getColor(R.color.c12));
                     zhuanyeduitype1View.setBackgroundResource(R.drawable.bg_text_hui);
                     zhuanyeduitype1View.setTextColor(getResources().getColor(R.color.c6));
                }

            }

        }else if (resource.getCode().equals("materialRepository")) {   //显示物资库
            wuzikuLayout.setVisibility(View.VISIBLE);
            fenglimeihuojiMrEdit.setText(resource.getWindFireCountMR().equals("null")?"":resource.getWindFireCountMR());
            gaoyaxishuiMrEdit.setText(resource.getSprayFireCountMR().equals("null")?"":resource.getSprayFireCountMR());
            gaoyashuibengMrEdit.setText(resource.getWaterPumpCountMR().equals("null")?"":resource.getWaterPumpCountMR());
            erhaogongjuMrEdit.setText(resource.getTwoToolCountMR().equals("null")?"":resource.getTwoToolCountMR());
            meihuoshuiqiangMrEdit.setText(resource.getWaterPistolCountMR().equals("null")?"":resource.getWaterPistolCountMR());
            youjuMrEdit.setText(resource.getChainSawCountMR().equals("null")?"":resource.getChainSawCountMR());
            geguanjiMrEdit.setText(resource.getBushCutterCountMR().equals("null")?"":resource.getBushCutterCountMR());
            huochangqiegejiMrEdit.setText(resource.getFireCutterCountMR().equals("null")?"":resource.getFireCutterCountMR());
            fanghuofuMrEdit.setText(resource.getFireproofClothesCountMR().equals("null")?"":resource.getFireproofClothesCountMR());
            fanghuoshoutaoMrEdit.setText(resource.getGlovesCountMR().equals("null")?"":resource.getGlovesCountMR());
            fanghuotoukuiMrEdit.setText(resource.getHelmetCountMR().equals("null")?"":resource.getHelmetCountMR());
            fanghuoxieMrEdit.setText(resource.getShoesCountMR().equals("null")?"":resource.getShoesCountMR());
            shuidaiMrEdit.setText(resource.getWaterBagCountMR().equals("null")?"":resource.getWaterBagCountMR());
            shuinangMrEdit.setText(resource.getWaterSacCountMR().equals("null")?"":resource.getWaterSacCountMR());
            youtongMrEdit.setText(resource.getOilDrumCountMR().equals("null")?"":resource.getOilDrumCountMR());
            if (resource.getResourceType() == null){
                currentWuzikuType = 0;
            }else {
                if (resource.getResourceType().equals("1")){
                    currentWuzikuType = 1;
                    wuzikuType0View.setBackgroundResource(R.drawable.bg_text_hui);
                    wuzikuType0View.setTextColor(getResources().getColor(R.color.c6));
                    wuzikuType1View.setBackgroundResource(R.drawable.bg_text_lan);
                    wuzikuType1View.setTextColor(getResources().getColor(R.color.c12));
                    wuzikuType2View.setBackgroundResource(R.drawable.bg_text_hui);
                    wuzikuType2View.setTextColor(getResources().getColor(R.color.c6));
                }else if (resource.getResourceType().equals("2")){
                    currentWuzikuType = 2;
                    wuzikuType0View.setBackgroundResource(R.drawable.bg_text_hui);
                    wuzikuType0View.setTextColor(getResources().getColor(R.color.c6));
                    wuzikuType1View.setBackgroundResource(R.drawable.bg_text_hui);
                    wuzikuType1View.setTextColor(getResources().getColor(R.color.c6));
                    wuzikuType2View.setBackgroundResource(R.drawable.bg_text_lan);
                    wuzikuType2View.setTextColor(getResources().getColor(R.color.c12));
                }else {
                    currentWuzikuType = 0;
                    wuzikuType0View.setBackgroundResource(R.drawable.bg_text_lan);
                    wuzikuType0View.setTextColor(getResources().getColor(R.color.c12));
                    wuzikuType1View.setBackgroundResource(R.drawable.bg_text_hui);
                    wuzikuType1View.setTextColor(getResources().getColor(R.color.c6));
                    wuzikuType2View.setBackgroundResource(R.drawable.bg_text_hui);
                    wuzikuType2View.setTextColor(getResources().getColor(R.color.c6));
                }

            }
        }else if (resource.getCode().equals("fireCommand")) {//森林防火监控中  指挥部
            jiancezhongxinLayout.setVisibility(View.VISIBLE);
            if (resource.getResourceType() == null){
                currentJiancezhongxinType = 0;
            }else {
                if (resource.getResourceType().equals("1")){
                    currentJiancezhongxinType = 1;
                    jiancezhongxin0View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiancezhongxin0View.setTextColor(getResources().getColor(R.color.c6));
                    jiancezhongxin1View.setBackgroundResource(R.drawable.bg_text_lan);
                    jiancezhongxin1View.setTextColor(getResources().getColor(R.color.c12));
                    jiancezhongxin2View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiancezhongxin2View.setTextColor(getResources().getColor(R.color.c6));
                }else if (resource.getResourceType().equals("2")){
                    currentJiancezhongxinType = 2;
                    jiancezhongxin0View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiancezhongxin0View.setTextColor(getResources().getColor(R.color.c6));
                    jiancezhongxin1View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiancezhongxin1View.setTextColor(getResources().getColor(R.color.c6));
                    jiancezhongxin2View.setBackgroundResource(R.drawable.bg_text_lan);
                    jiancezhongxin2View.setTextColor(getResources().getColor(R.color.c12));
                }else {
                    currentJiancezhongxinType = 0;
                    jiancezhongxin0View.setBackgroundResource(R.drawable.bg_text_lan);
                    jiancezhongxin0View.setTextColor(getResources().getColor(R.color.c12));
                    jiancezhongxin1View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiancezhongxin1View.setTextColor(getResources().getColor(R.color.c6));
                    jiancezhongxin2View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiancezhongxin2View.setTextColor(getResources().getColor(R.color.c6));
                }

            }
        } else if (resource.getCode().equals("watchTower")) {//森林防火监控中  指挥部
            liaowangtaLayout.setVisibility(View.VISIBLE);
            jiancefanweiEdit.setText(resource.getWatchRange().equals("null")?"":resource.getWatchRange());
        }else if (resource.getCode().equals("monitor")) {//视频监控点
            shipinjiankongLayout.setVisibility(View.VISIBLE);
            jiancefanweiMEdit.setText(resource.getMonitorRange().equals("null")?"":resource.getMonitorRange());
            if (resource.getIsNetworking() == null){
                lianwangSwitch.setChecked(false);
            }else {
                if (resource.getIsNetworking().equals("1")){
                    lianwangSwitch.setChecked(true);
                }else {
                    lianwangSwitch.setChecked(false);
                }
            }
            if (resource.getIsIntelligentEntry() == null){
                zhinengkakouSwitch.setChecked(false);
            }else {
                if (resource.getIsIntelligentEntry().equals("1")){
                    zhinengkakouSwitch.setChecked(true);
                }else {
                    zhinengkakouSwitch.setChecked(false);
                }
            }
            if (resource.getResourceType() == null){
                currentshipinjiankongType = 0;
            }else {
                if (resource.getResourceType().equals("1")){
                    currentshipinjiankongType = 1;
                    jiankong0View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiankong0View.setTextColor(getResources().getColor(R.color.c6));
                    jiankong1View.setBackgroundResource(R.drawable.bg_text_lan);
                    jiankong1View.setTextColor(getResources().getColor(R.color.c12));
                    jiankong2View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiankong2View.setTextColor(getResources().getColor(R.color.c6));
                }else if (resource.getResourceType().equals("2")){
                    currentshipinjiankongType = 2;
                    jiankong0View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiankong0View.setTextColor(getResources().getColor(R.color.c6));
                    jiankong1View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiankong1View.setTextColor(getResources().getColor(R.color.c6));
                    jiankong2View.setBackgroundResource(R.drawable.bg_text_lan);
                    jiankong2View.setTextColor(getResources().getColor(R.color.c12));
                }else {
                    currentshipinjiankongType = 0;
                    jiankong0View.setBackgroundResource(R.drawable.bg_text_lan);
                    jiankong0View.setTextColor(getResources().getColor(R.color.c12));
                    jiankong1View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiankong1View.setTextColor(getResources().getColor(R.color.c6));
                    jiankong2View.setBackgroundResource(R.drawable.bg_text_hui);
                    jiankong2View.setTextColor(getResources().getColor(R.color.c6));
                }

            }
        }else if (resource.getCode().equals("cemetery")) {//墓地
            mudiLayout.setVisibility(View.VISIBLE);
            fentouEdit.setText(resource.getGraveCount().equals("null")?"":resource.getGraveCount());

            if (resource.getResourceType() == null){
                currentmudiType = 0;
            }else {
                if (resource.getResourceType().equals("1")){
                    currentmudiType = 1;
                    mudi0View.setBackgroundResource(R.drawable.bg_text_hui);
                    mudi0View.setTextColor(getResources().getColor(R.color.c6));
                    mudi1View.setBackgroundResource(R.drawable.bg_text_lan);
                    mudi1View.setTextColor(getResources().getColor(R.color.c12));
                } else {
                    currentmudiType = 0;
                    mudi0View.setBackgroundResource(R.drawable.bg_text_lan);
                    mudi0View.setTextColor(getResources().getColor(R.color.c12));
                    mudi1View.setBackgroundResource(R.drawable.bg_text_hui);
                    mudi1View.setTextColor(getResources().getColor(R.color.c6));
                }

            }
        }else if (resource.getCode().equals("dangerSource")) {//危险源
            weixianyuanLayout.setVisibility(View.VISIBLE);
            if (resource.getIsMajorHazard() == null){
                zhongdaweixianyuanSwitch.setChecked(false);
            }else {
                if (resource.getIsMajorHazard().equals("1")){
                    zhongdaweixianyuanSwitch.setChecked(true);
                }else {
                    zhongdaweixianyuanSwitch.setChecked(false);
                }
            }
        }
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
        zerenrenView = (TextView) findViewById(R.id.zerenren_view);
        zerenrendianhuaView = (TextView) findViewById(R.id.zerenrendianhua_view);
        dangqianView = (TextView) findViewById(R.id.dangqian_view);
        quLayout = (LinearLayout) findViewById(R.id.qu_layout);
        quText = (TextView) findViewById(R.id.qu_text);
        jiedaoLayout = (LinearLayout) findViewById(R.id.jiedao_layout);
        jiedaoText = (TextView) findViewById(R.id.jiedao_text);

        nameEditView = (EditText) findViewById(R.id.name_edit);
        addressEditView = (EditText) findViewById(R.id.address_edit);
        lngEdit = (EditText) findViewById(R.id.lng_edit);
        latEdit = (EditText) findViewById(R.id.lat_edit);
        miaoshuEdit = (EditText) findViewById(R.id.miaoshu_Edit);
        zerenrenEdir = (EditText) findViewById(R.id.zerenren_edit);
        phoneEdit = (EditText) findViewById(R.id.phone_edit);

        leixingLayout = (LinearLayout) findViewById(R.id.leixing_layout);
        leixingText = (TextView) findViewById(R.id.leixing_text);
        toMapView = (ImageView) findViewById(R.id.to_map_view);

        addButton = (TextView) findViewById(R.id.add_button);

        oneImageLayout = (FrameLayout) findViewById(R.id.one_image_layout);
        oneImage = (ImageView) findViewById(R.id.one_image);
        oneImageDelete = (ImageView) findViewById(R.id.one_image_delete);

        oneImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgePower();
                if (picType == 0){
                    addPicType = 0;
                    addImage();
                }else if (picType ==1){
                    showBigImage();
                }else {
                    showBigImage();
                }

            }
        });

        oneImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picType == 2){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.ic_bigphoto)
                            .into(oneImage);
                    oneImageDelete.setVisibility(View.GONE);
                }else {
                    uriChooseList.remove(0);
                    Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(oneImage);
                    oneImageDelete.setVisibility(View.GONE);
                }
                picType = 0;


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
        //物资库点击
        RxViewAction.clickNoDouble(wuzikuType0View)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentWuzikuType = 0;
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
                        currentWuzikuType = 1;
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
                        currentWuzikuType = 2;
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
                        if (nameEditView.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "请选择资源点名称", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (addressEditView.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "请选择地址", Toast.LENGTH_SHORT).show();
                            return;
                        }

                   /*     if (miaoshuEdit.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "请输入描述信息", Toast.LENGTH_SHORT).show();
                            return;
                        }*/

                        showDialogProgress(progressDialog,"修改中...");

                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

                        checkTime = dateFormat.format(date);
                        if (picType == 1){
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

                        //showResourceDialgo();
                    }
                });

        RxViewAction.clickNoDouble(quLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 0;
                    //    getAllQu();
                     //   showAreaDialog(quStrList);
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
                            Toast.makeText(ResourceEditActivity.this, "请先选择区", Toast.LENGTH_SHORT).show();

                        }else {
                          //  getAllJieDao();
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
                        Toast.makeText(ResourceEditActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
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
                        Log.e(TAG, "pic: 1" );
                        if (list.size()>0){
                            postOtherPicToService();
                        }else {
                            addResourceToService();
                        }

                    }else {
                        Toast.makeText(ResourceEditActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
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
        if (picType == 1){
            picList.add(uriChooseList.get(0).toString());
        }else {
            picList.add(RequestUtils.IAMGE_URL + resource.getPicture());
        }

        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(ResourceEditActivity.this, picList);
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
                        int size = 2 - uriChooseList.size();
                        Matisse.from(ResourceEditActivity.this)
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
     * 提交数据到服务器
     */
    private void addResourceToService() {


        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id",resource.getUuId());
            jsonObject.put("name",nameEditView.getText().toString());

            if (picType == 1){
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
            if (otherPicList.size()>0){
                if (list.size()>0){
                    jsonObject.put("otherPic",resource.getOtherPic() +  "," + otherPicStr);
                }else {
                    jsonObject.put("otherPic",resource.getOtherPic());
                }
            }else {
                if (list.size()>0){
                    jsonObject.put("otherPic",otherPicStr);
                }
            }

            if (resource.getCode().equals("checkStation")){ //护林检查站
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
            }else if (resource.getCode().equals("waterSource")){        ////水源地
                jsonObject.put("waterCapacity",xushuiliangEdit.getText().toString());
                jsonObject.put("resourceType",shuiyuandiType);
                if (zhishengjiSwitch.isChecked()){
                    jsonObject.put("isHelicopterWater",1);
                }else {
                    jsonObject.put("isHelicopterWater",0);
                }
            }else if (resource.getCode().equals("team")){        ////专业队
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
            }else if (resource.getCode().equals("materialRepository")){        ////物资库
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
            }else if (resource.getCode().equals("fireCommand")){         //森林防火监测中心
                jsonObject.put("resourceType",currentJiancezhongxinType);
            }else if (resource.getCode().equals("watchTower")){         //瞭望塔
                jsonObject.put("watchRange",jiancefanweiEdit.getText().toString());
            }else if (resource.getCode().equals("monitor")){         //视频监控点
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
            }else if (resource.getCode().equals("cemetery")){         //墓地
                jsonObject.put("resourceType",currentmudiType);
                jsonObject.put("graveCount",fentouEdit.getText().toString());
            }else if (resource.getCode().equals("dangerSource")){         //危险源
                if (zhongdaweixianyuanSwitch.isChecked()){
                    jsonObject.put("isMajorHazard",1);
                }else {
                    jsonObject.put("isMajorHazard",0);
                }
            }

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + resource.getApiUrl().substring(1,resource.getApiUrl().length()));
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        Log.e(TAG, "postData:-- jsonObject.toString()--" + params);
        params.setConnectTimeout(10000);
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")){
                        Toast.makeText(ResourceEditActivity.this, "资源点修改成功，重新打开地图后生效", Toast.LENGTH_SHORT).show();

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
                        Log.e(TAG, "onSuccess:dataIsChange " + new DbConfig(getApplicationContext()).getUser().dataIsChange);
                        progressDialog.dismiss();
                        finish();


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult:resultCode " + resultCode + "requestcode" + requestCode);
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
                picType = 1;
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

    /**
     * 获取当前位置经纬度
     * @return
     */
    // @JavascriptInterface
    @SuppressLint("MissingPermission")
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


                    Log.e(TAG, "onSuccess:带进来的经纬度 " + formatted_address);
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
                            int size = 9 - list.size() - otherPicList.size();
                            Matisse.from(ResourceEditActivity.this)
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
            String oneUri = "";
            if (chooseImage.isNetPic){
                oneUri = chooseImage.getPicStr();
            }else {
                oneUri = uri.toString();
            }

            picList.add(oneUri); //点击哪张 把哪张放第一个
            for (int i = 0; i < list.size(); i++) {     //除去点击那张  其他放进去
                if (!chooseImage.isNetPic){
                    if (!oneUri.equals(list.get(i).getUri().toString())){
                        picList.add(list.get(i).getUri().toString());
                    }
                }
            };
            for (int i = 0; i < otherPicList.size(); i++) {     //除去点击那张  其他放进去
                if (chooseImage.isNetPic){
                    if (!oneUri.equals(otherPicList.get(i).getPicStr())){
                        picList.add(otherPicList.get(i).getPicStr());
                    }
                }
            };
            String content = "";     //放评论
            ImagPagerUtil imagPagerUtil = new ImagPagerUtil(ResourceEditActivity.this, picList);
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
            if (otherPicList.size()>0){
                for (int i = 0; i < otherPicList.size(); i++) {
                    photoItems.add(otherPicList.get(i));
                }
            }
            if (list.size()+otherPicList.size()<9){
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
