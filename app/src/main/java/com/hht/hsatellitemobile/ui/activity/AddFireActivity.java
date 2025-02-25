package com.hht.hsatellitemobile.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.hht.hsatellitemobile.R;
import com.hht.hsatellitemobile.db.DbConfig;
import com.hht.hsatellitemobile.ui.activity.base.HhBaseFullActivity;
import com.hht.hsatellitemobile.ui.cell.MessagePicturesLayout;
import com.hht.hsatellitemobile.db.model.Area;
import com.hht.hsatellitemobile.utils.GifSizeFilter;
import com.hht.hsatellitemobile.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import rx.functions.Action1;

public class AddFireActivity extends HhBaseFullActivity implements DatePicker.OnDateChangedListener , MessagePicturesLayout.Callback{

    private static final String TAG = AddFireActivity.class.getSimpleName();
    private ImageView goToMapView;
    private LocationClient mLocationClient;

    public static final int MAP_REUEST_CODE = 2;
    public static final double LATITUDE_DEF = 36.32087806111286;//默认天安数码城: latitude: 36.32087806111286, longitude: 120.44349123197962
    public static final double LONGTITUDE_DEF = 120.44349123197962;//默认天安数码城: latitude: 36.32087806111286, longitude: 120.44349123197962

    private double latitude_double = LATITUDE_DEF;
    private double longitude_double = LONGTITUDE_DEF;
    private String longitude;
    private String latitude;
    private String cityAddress;
    private EditText addressView;
    private EditText jingduView;
    private EditText weiduView;
    private StringBuffer date;
    private StringBuffer endDate;
    private int year;
    private int month;
    private int day;
    public int chooseHour;
    public int chooseMinute;
    public boolean isChooseStarTime ;
    private TextView fireTimeText;
    private String currentCity;
    private LinearLayout shiLayout;
    private LinearLayout shengLayout;
    private LinearLayout quLayout;
    private TextView shengText;
    private TextView shiText;
    private TextView quText;
    private List<Object> imglist;
    public int currentChooseArea = 0;  //当前在选择省还是市   0选择省  1选择市

    public List<Area> shengList;
    public List<String> shengStrList;
    public List<Area> shiList;
    public List<String> shiStrList;
    private LinearLayout addFirePhotoLayout;
    private LinearLayout addFireVideoLayout;
    private static final int REQUEST_CODE_CHOOSE = 23;
    public List<Uri> uriChooseList;
    private FrameLayout oneImageLayout;
    private FrameLayout twoImageLayout;
    private FrameLayout videoLayout;
    private ImageView oneImage;
    private ImageView twoImage;
    private ImageView video;
    private ImageView oneImageDelete;
    private ImageView twoImageDelete;
    private ImageView videoDelete;
    private LinearLayout photoLayout;
    private TextView addFireButton;
    private EditText tudiTypeView;
    private EditText mianjiView;
    private Bitmap evaluateOne;
    private Bitmap evaluateTwo;
    private Bitmap evaluateThree;

    private ProgressDialog addFireDialog;

    private List<Area> allAreaList;
    private List<Area> allShiList;
    private WheelView areaWy;
    public int shengSelectIndex = 0;
    public int shiSelectIndex = 0;
    public boolean isChooseSheng = false;
    public String currentChooseSheng = "";
    public String currentChooseShi = "";
    private boolean fromMap = false;
    private String access_token;
    private ImageView backView;
    private String videoPath = "";
    private String videoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fire);

        shengList = new ArrayList<>();
        shiList = new ArrayList<>();
        shengStrList = new ArrayList<>();
        shiStrList = new ArrayList<>();
        uriChooseList = new ArrayList<>();
        allAreaList = new ArrayList<>();
        allShiList=new ArrayList<>();
        date = new StringBuffer();
        endDate = new StringBuffer();
        imglist=new ArrayList<>();
        addFireDialog = new ProgressDialog(this);
        access_token = new DbConfig(this).getUser().getToken();
        initDateTime();

        if (new DbConfig(getApplicationContext()).getAreaList() == null) {
            getAre(access_token);
        }else {
            hideDialogProgress(addFireDialog);
            allAreaList = new DbConfig(getApplicationContext()).getAreaList();
            initArea();
        }

        initView();
    }

    private void initView() {

        goToMapView = (ImageView) findViewById(R.id.to_map_view);
        addressView = (EditText) findViewById(R.id.address_view);
        jingduView = (EditText) findViewById(R.id.jingdu_view);
        weiduView = (EditText) findViewById(R.id.weidu_view);
        fireTimeText = (TextView) findViewById(R.id.fire_time_text);
        shiLayout = ((LinearLayout) findViewById(R.id.gao_shi_layout));
        shengLayout = ((LinearLayout) findViewById(R.id.sheng_layout));
        quLayout = ((LinearLayout) findViewById(R.id.qu_layout));
        shengText = ((TextView) findViewById(R.id.sheng_add_fire_text));
        shiText = ((TextView) findViewById(R.id.shi_add_fire_text));
        quText = ((TextView) findViewById(R.id.qu_text));
        addFirePhotoLayout = (LinearLayout) findViewById(R.id.add_fire_photo_layout);
        addFireVideoLayout = (LinearLayout) findViewById(R.id.add_fire_video_layout);
        oneImageLayout = (FrameLayout) findViewById(R.id.one_image_layout);
        twoImageLayout = (FrameLayout) findViewById(R.id.two_imag_layout);
        videoLayout = (FrameLayout) findViewById(R.id.video_layout);
        oneImage = (ImageView) findViewById(R.id.one_image);
        twoImage = (ImageView) findViewById(R.id.two_image);
        video = (ImageView) findViewById(R.id.video);
        oneImageDelete = (ImageView) findViewById(R.id.one_image_delete);
        twoImageDelete = (ImageView) findViewById(R.id.two_image_delete);
        videoDelete = (ImageView) findViewById(R.id.video_delete);
        photoLayout = (LinearLayout) findViewById(R.id.photo_layout);
        addFireButton = (TextView) findViewById(R.id.add_fire_button);
        tudiTypeView = (EditText) findViewById(R.id.tudi_type_view);
        mianjiView = (EditText) findViewById(R.id.mianji_view);
        backView = (ImageView) findViewById(R.id.back_image_view);



        RxViewAction.clickNoDouble(backView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onBackPressed();
                    }
                });

        RxViewAction.clickNoDouble(addFireButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        imglist.clear();
                        if (uriChooseList.size()>0) {
                            for (int i = 0; i <uriChooseList.size() ; i++) {
                                postPictoService(i+1);
                            }
                        }else {
                            postFireToService();
                        }
                    }
                });

        RxViewAction.clickNoDouble(oneImageLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       // showBigImage(0);
                    }
                });

        RxViewAction.clickNoDouble(twoImageLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                      //  showBigImage(1);
                    }
                });

        RxViewAction.clickNoDouble(oneImageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: uriChooseList.size() = " + uriChooseList.size() );
                        if (uriChooseList.size() == 1){  //只有一张图
                            uriChooseList.remove(0);
                            oneImageDelete.setVisibility(View.GONE);
                            oneImageLayout.setVisibility(View.GONE);
                            if(Objects.equals(videoPath, "")){
                                photoLayout.setVisibility(View.GONE);
                            }
                        }else {     //如果有两张图
                            uriChooseList.remove(0);
                            Glide.with(getApplicationContext()).load(uriChooseList.get(0)).into(oneImage);
                         //   Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
                            oneImageDelete.setVisibility(View.VISIBLE);
                            twoImageDelete.setVisibility(View.GONE);
                            twoImageLayout.setVisibility(View.GONE);
                        }
                    }
                });

        RxViewAction.clickNoDouble(twoImageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        uriChooseList.remove(1);
                       // Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
                        //twoImageDelete.setVisibility(View.GONE);
                        twoImageLayout.setVisibility(View.GONE);
                    }
                });
        RxViewAction.clickNoDouble(videoDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        videoPath = "";
                        videoLayout.setVisibility(View.GONE);
                    }
                });

        RxViewAction.clickNoDouble(addFirePhotoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList.size() == 2){
                            Toast.makeText(AddFireActivity.this, "最多可以添加两张图片", Toast.LENGTH_SHORT).show();
                        }else {
                            addImage();
                        }

                    }
                });
        RxViewAction.clickNoDouble(addFireVideoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 66);
                    }
                });

        /**
         * 省市的点击
         */
        RxViewAction.clickNoDouble(shengText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       currentChooseArea = 0;
                   //     getAllAre();
                        showAreaDialog(shengStrList);
                    }
                });
        RxViewAction.clickNoDouble(shiText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 1;
                        Log.e(TAG, "call: 12321--" + shengText.getText().toString());
                        if (fromMap){
                            showAreaDialog(shiStrList);
                        }else {
                            if (shengText.getText().toString().equals("请选择省")){
                                Toast.makeText(AddFireActivity.this, "请先选择省", Toast.LENGTH_SHORT).show();
                            }else {
                                String currentShengId = "";
                                for (int i = 0; i < shengList.size(); i++) {
                                    if (shengList.get(i).getName().equals(currentChooseSheng)) {
                                        currentShengId = shengList.get(i).getId();
                                    }
                                }
                                getShengAre(currentShengId);

                            }
                        }


                    }
                });

        RxViewAction.clickNoDouble(fireTimeText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog();
                    }
                });

        RxViewAction.clickNoDouble(goToMapView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), FireMapActivity.class);
                        intent.putExtra("longitude_double", longitude_double);
                        intent.putExtra("latitude_double", latitude_double);
                        startActivityForResult(intent, MAP_REUEST_CODE);
                    }
                });

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

    private void initShi(String currentShengId) {
        shiList.clear();
        shiStrList.clear();
        shiStrList.add("请选择市");
        for (int i = 0; i < allAreaList.size(); i++) {
            if (allAreaList.get(i).getParentId().equals(currentShengId)) {
                shiList.add(allAreaList.get(i));
                shiStrList.add(allAreaList.get(i).getName());
            }
        }

        showAreaDialog(shiStrList);
    }
    private void initShiByShiId(String currentShengId,String currentShiId) {
        shiList.clear();
        shiStrList.clear();
        shiStrList.add("请选择市");
        for (int i = 0; i < allAreaList.size(); i++) {
            if (allAreaList.get(i).getParentId().equals(currentShengId)) {
                shiList.add(allAreaList.get(i));
                shiStrList.add(allAreaList.get(i).getName());
            }
        }
        for (int i = 0; i < shiList.size(); i++) {
            if (shiList.get(i).getId().equals(currentShiId)) {
                shiSelectIndex = i + 1;
            }
        }

      //  showAreaDialog(shiStrList);
    }


    private void postFireToService() {
        if (addressView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (jingduView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入经度", Toast.LENGTH_SHORT).show();
            return;
        }
        if (weiduView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入纬度", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fireTimeText.getText().toString().equals("时间")){
            Toast.makeText(this, "请输入时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tudiTypeView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入土地类型", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mianjiView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入面积", Toast.LENGTH_SHORT).show();
            return;
        }
        String shengStr = shengText.getText().toString();
        String shengId = "";
        for (int i = 0; i < shengList.size(); i++) {
            if (shengList.get(i).getName().equals(shengStr)) {
                shengId = shengList.get(i).getId();
            }
        }
        String shiStr = shiText.getText().toString();
        String shiId = "";
        for (int i = 0; i < shiList.size(); i++) {
            if (shiList.get(i).getName().equals(shiStr)){
                shiId = shiList.get(i).getId();
            }
        }
        String addressStr = addressView.getText().toString();
        String jingduStr = jingduView.getText().toString();
        String weiduStr = weiduView.getText().toString();
        String timeStr = fireTimeText.getText().toString();
        String tudiTypeStr = tudiTypeView.getText().toString();
        String tudiMianjiStr = mianjiView.getText().toString();


        showDialogProgress(addFireDialog, "正在上传中...");
        for (int i = 0; i < uriChooseList.size(); i++) {
            try {

                Uri uri = uriChooseList.get(i);
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                if (i == 0){
                    evaluateOne = rotaingImageView(degree, photo);
                }else if (i == 1){
                    evaluateTwo = rotaingImageView(degree, photo);
                }else if (i == 2){
                    evaluateThree = rotaingImageView(degree, photo);
                }
            } catch (IOException e) {

            }
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address",addressStr);
            jsonObject.put("cityCode",shiId);
            jsonObject.put("cityName",shiStr);
            jsonObject.put("provinceCode",shengId);
            jsonObject.put("provinceName",shengStr);

            jsonObject.put("longitude",jingduStr);
            jsonObject.put("latitude",weiduStr);

            jsonObject.put("discoverTime",timeStr.replace("  ","T"));
            jsonObject.put("fireName","app测试添加火点");
            jsonObject.put("fireNo"," ");
            jsonObject.put("status",0);
            if (imglist.size()>0){
                jsonObject.put("pic_path1",imglist.get(0).toString());
                if (imglist.size()>1){
                    jsonObject.put("pic_path2",imglist.get(1).toString());
                }
            }
            jsonObject.put("video_path",videoUrl);
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL+"AppFirealarm/Create");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("token",access_token);
        Log.e(TAG, "postDataService:反馈---11 " + jingduStr);
        Log.e(TAG, "postDataService:反馈---11 " + weiduStr);
        params.setConnectTimeout(10000);
        Log.e(TAG, "postFireToService: "+jsonObject.toString() );
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "postFireToService: "+access_token );
        Log.e(TAG, "postFireToService: "+params );
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    Log.e(TAG, "onSuccess: "+jsonObject1.getString("message") );
                    String message = jsonObject1.getString("message");
                    if (message.equals("创建成功")){
                        Toast.makeText(AddFireActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddFireActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: "+ex );
                Log.e(TAG, "onError: 请求失败" );
                Toast.makeText(AddFireActivity.this, "请连接内网上传", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(addFireDialog);
            }
        });
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
                        Matisse.from(AddFireActivity.this)
                                .choose(MimeType.allOf())
                                .countable(true)
                                .capture(true)
                                .captureStrategy(
                                        new CaptureStrategy(true,"com.hht.hsatellitemobile.fileProvider")
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


    private void getAllAre() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Account/GetAreaListByParentId");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("Token",new DbConfig(this).getUser().getToken());
        params.addParameter("parentId",0);
        params.setConnectTimeout(100000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    shengList.clear();
                    shengStrList.clear();
                    shengStrList.add("请选择省");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject area = jsonArray.getJSONObject(i);
                        String id = area.getString("Id");
                        String name = area.getString("Name");
                        String parentId = area.getString("ParentId");
                        shengList.add(new Area(id,name,parentId));
                        shengStrList.add(name);
                    }


                    //showAreaDialog(shengStrList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //   Toast.makeText(MainActivity.this, "网络异常，请检查网络链接12", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void getShengAre( String id) {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Account/GetAreaListByParentId");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("Token",new DbConfig(this).getUser().getToken());
        params.addParameter("parentId",id);
        params.setConnectTimeout(100000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: "+result );
                    shiList.clear();
                    shiStrList.clear();
                    JSONArray jsonArray = new JSONArray(result);
                    shiStrList.add("请选择市");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject area = jsonArray.getJSONObject(i);
                        String id = area.getString("Id");
                        String name = area.getString("Name");
                        String parentId = area.getString("ParentId");
                        shiList.add(new Area(id,name,parentId));
                        shiStrList.add(name);
                    }

                    //showAreaDialog(shiStrList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    String value = jsonObject.getString("value");
                    if (type.equals("1")){
                        String token = jsonObject.getString("message");

                        getUserInfo(token);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //   Toast.makeText(MainActivity.this, "网络异常，请检查网络链接14", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
        initShi(id);
    }

    /**
     * 显示地区选择的dialog
     */
    private void showAreaDialog(List<String> strList) {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        areaWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        areaWy.setIsLoop(false);
        if (currentChooseArea == 0){
            areaWy.setItems(strList, shengSelectIndex);//init selected position is 0 初始选中位置为0
        }else {
            areaWy.setItems(strList, shiSelectIndex);//init selected position is 0 初始选中位置为0
        }

        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                fromMap = false;
                if (currentChooseArea == 0){   //选择省
                    isChooseSheng = true;
                    currentChooseSheng = areaWy.getSelectedItem();
                    shengSelectIndex = areaWy.getSelectedPosition();
                    shengText.setText(currentChooseSheng);
                    //选择剩要初始化市
                    shiText.setText("请选择市");
                    currentChooseShi = "请选择市";
                    shiSelectIndex = 0;
                }else {                          //选择市
                    currentChooseShi = areaWy.getSelectedItem();
                    shiSelectIndex = areaWy.getSelectedPosition();
                    shiText.setText(currentChooseShi);
                }

              /*  currentSheng = shengWv.getSelectedItem();
                getShi();
                ;
                shiWv.setItems(shiList, currentShiPosition);
                currentShi = shiWv.getSelectedItem();
                getXian();
                xianWv.setItems(xianList, currentXianPosition);*//**//**/
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult:resultCode " + resultCode + "requestcode" + requestCode);
        if (requestCode == MAP_REUEST_CODE && resultCode == MAP_REUEST_CODE) {
            longitude = data.getStringExtra("longitude");
            latitude = data.getStringExtra("latitude");
            cityAddress = data.getStringExtra("cityAddress");
            currentCity = data.getStringExtra("city");
            if (!currentCity.isEmpty()){
                String currentCiryParentId = "";
                String currentCiryId = "";

                String currentPro = "";
                String currentProId = "";
                for (int i = 0; i < allAreaList.size(); i++) {
                    if (allAreaList.get(i).getName().equals(currentCity)) {
                        currentCiryParentId = allAreaList.get(i).getParentId();
                        currentCiryId = allAreaList.get(i).getId();
                    }
                }

                for (int i = 0; i < allAreaList.size(); i++) {
                    if (allAreaList.get(i).getId().equals(currentCiryParentId)){
                        currentPro = allAreaList.get(i).getName();
                        currentProId = allAreaList.get(i).getId();
                    }
                }
                shengStrList.add(currentPro);
                shiStrList.add(currentCity);
                isChooseSheng = true;

                shengText.setText(currentPro);
                shiText.setText(currentCity);
                fromMap = true;
                initAreaById(currentPro);
                initShiByShiId(currentProId,currentCiryId);
            }

            addressView.setText(cityAddress);
            addressView.setSelection(cityAddress.length());
            jingduView.setText(longitude);
            weiduView.setText(latitude);
//            Toast.makeText(this, "经度=" + longitude + "纬度=" + latitude + "cityAddress=" + cityAddress, Toast.LENGTH_SHORT).show();
        }else  if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> uriList = Matisse.obtainResult(data);
            for (int i = 0; i < uriList.size(); i++) {
                uriChooseList.add(uriList.get(i));
            }

            photoLayout.setVisibility(View.VISIBLE);
            if (uriChooseList.size()>1){  //有两张图
                twoImageLayout.setVisibility(View.VISIBLE);
                twoImageDelete.setVisibility(View.VISIBLE);
                oneImageLayout.setVisibility(View.VISIBLE);
                oneImageDelete.setVisibility(View.VISIBLE);
                Glide.with(this).load(uriChooseList.get(0)).into(oneImage);
                Glide.with(this).load(uriChooseList.get(1)).into(twoImage);
            }else {                     //有一张图
                twoImageLayout.setVisibility(View.GONE);
                twoImageDelete.setVisibility(View.GONE);
                oneImageLayout.setVisibility(View.VISIBLE);
                oneImageDelete.setVisibility(View.VISIBLE);
                Glide.with(this).load(uriChooseList.get(0)).into(oneImage);
             //   Glide.with(this).load(R.drawable.ic_bigphoto).into(twoImage);
                twoImageLayout.setVisibility(View.GONE);
            }

        }else  if (requestCode == 66 && resultCode == RESULT_OK && null != data) {
            photoLayout.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.VISIBLE);
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            videoPath = cursor.getString(columnIndex);
            cursor.close();
            Log.e(TAG, "onActivityResult: " + videoPath);

        }

    }

    /**
     * 日期选择控件
     */
    private void showDataDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
                }
                if (endDate.length() > 0) { //清除上次记录的日期
                    endDate.delete(0, endDate.length());
                }
                date.append(String.valueOf(year));
                if (month < 9){
                    date.append("-0").append(String.valueOf(month + 1));
                }else {
                    date.append("-").append(String.valueOf(month + 1));
                }
                if (day <10){
                    date.append("-0").append(String.valueOf(day));
                } else {
                    date.append("-").append(String.valueOf(day));
                }
                fireTimeText.setText(date);
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
        View dialogView = View.inflate(this, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        Calendar date = Calendar.getInstance();
        int year1 = date.get(Calendar.YEAR);
        int month1 = date.get(Calendar.MONTH);
        int day1 = date.get(Calendar.DATE);
        String endData = year1 - 10 + "-" + month1 + "-" + day1;
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
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(this);
        builder1.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (chooseHour < 10 && chooseMinute <10){
                    fireTimeText.append("  0" + chooseHour + ":0" + chooseMinute + ":00");
                }else if (chooseHour < 10 && chooseMinute >10){
                    fireTimeText.append("  0" + chooseHour + ":" + chooseMinute + ":00");
                }else if (chooseHour > 10 && chooseMinute < 10){
                    fireTimeText.append("  " + chooseHour + ":0" + chooseMinute + ":00");
                }else {
                    fireTimeText.append("  " + chooseHour + ":" + chooseMinute + ":00");
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
        View dialogView = View.inflate(this, R.layout.dialog_time, null);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timepicker);
        Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR);
        int minute = date.get(Calendar.MINUTE);
      /*  String endData = year1 - 10 + "-" + month1 + "-" + day1;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(endData);
        } catch (ParseException e) {

        }
        long starTimre = date2.getTime();


        long endTimre = System.currentTimeMillis();*/

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
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
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

    @Override
    public void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {

    }

    /**
     * 压缩后转base64
     * @param filePath
     * @param type
     * @return
     */
    public String compressImage(String filePath, String type) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.toLowerCase().contains("png")) {
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else {
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }

        byte[] datas = baos.toByteArray();
        Log.e("size", (datas.length / 1024) + "");
        return Base64.encodeToString(datas,Base64.DEFAULT);

    }

    public  int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
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

    private void getAre(String token) {
        JSONObject jsonObject = new JSONObject();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL+"Account/GetAreaListByParentId");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + token);
        params.addBodyParameter("token",token);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:diqu -- " + result);
             JSONArray jsonObject = null;
                try {
                    jsonObject = new JSONArray(result);
                    //JSONArray data = jsonObject.getJSONArray("data");
                    allAreaList.clear();
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject object = jsonObject.getJSONObject(i);
                        String id = object.getString("Id");
                        String name = object.getString("Name");
                        String parentId = object.getString("ParentId");
                        String level = object.getString("Sort");
                        String createTime = object.getString("CreateTime");
                        Area area = new Area(id, name, parentId, createTime, level);
                        allAreaList.add(area);
                    }
                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    DbManager db = dbConfig.getDbManager();
                    try {
                        db.saveOrUpdate(allAreaList);
                    } catch (DbException e) {

                    }

                    initArea();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(addFireDialog);
            }
        });
    }
    private void initArea() {
        shengStrList.clear();
        shengList.clear();
        shengStrList.add("请选择省");
        for (int i = 0; i < allAreaList.size(); i++) {
            if (allAreaList.get(i).getParentId().equals("0")) {
                shengList.add(allAreaList.get(i));
                shengStrList.add(allAreaList.get(i).getName());
            }
        }
    }
    private void initAreaById(String shengName) {
        shengStrList.clear();
        shengList.clear();
        shengStrList.add("请选择省");
        for (int i = 0; i < allAreaList.size(); i++) {
            if (allAreaList.get(i).getLevel().equals("1")) {
                shengList.add(allAreaList.get(i));
                shengStrList.add(allAreaList.get(i).getName());
            }
        }

        for (int i = 0; i < shengList.size(); i++) {
            if (shengList.get(i).getName().equals(shengName)) {
                shengSelectIndex = i + 1;
            }
        }
    }
    private void postPictoService(final int num){

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL+"AppFirealarm/Upload");
        params.addBodyParameter("token",access_token);
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        String picStr = null;
        Log.e(TAG, "postPictoService: "+params );
        Log.e(TAG, "postPictoService: "+uriChooseList.size() );
            try {

                Uri uri = uriChooseList.get(num-1);
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                if (num == 1){
                    evaluateOne = rotaingImageView(degree, photo);
                    picStr = ImageUtils.savePhoto(evaluateOne, this.getObbDir().getAbsolutePath(), "fileName" + num);
                }else if (num == 2){
                    evaluateTwo = rotaingImageView(degree, photo);
                    picStr = ImageUtils.savePhoto(evaluateTwo, this.getObbDir().getAbsolutePath(), "fileName" + num);
                }
                params.addBodyParameter("file", new File(picStr),null,picStr);
            } catch (IOException e) {
            }
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------------- " + result);
                imglist.add(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" );
                Log.e(TAG, "onError: "+ex );
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (num==uriChooseList.size()){
                    if(Objects.equals(videoPath, "")){
                        postFireToService();
                    }else{
                        postVideoToService();
                    }
                }
            }
        });
    }
    private void postVideoToService(){

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL+"AppFirealarm/Upload");
        params.addBodyParameter("token",access_token);
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        String picStr = null;
        Log.e(TAG, "postPictoService: "+params );
        Log.e(TAG, "postPictoService: "+uriChooseList.size() );
        params.addBodyParameter("file", new File(videoPath),null,videoPath);
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------------- " + result);
                videoUrl = result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" );
                Log.e(TAG, "onError: "+ex );
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                postFireToService();
            }
        });
    }
}
