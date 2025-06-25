package com.haohai.platform.firelibrary.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
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

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.location.LocationClient;
import com.bumptech.glide.Glide;
import com.haohai.platform.firelibrary.R;
import com.haohai.platform.firelibrary.ui.activity.base.HhBaseActivity;
import com.haohai.platform.firelibrary.ui.presenter.FirePresenter;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.MessagePicturesLayout;
import com.ruyiruyi.rylibrary.db.Area;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.route.RouteUtils;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;
import com.ruyiruyi.rylibrary.utils.GifSizeFilter;
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

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import rx.functions.Action1;

@Route(path = RouteUtils.FireAdd)
public class FireAddActivity extends HhBaseActivity implements DatePicker.OnDateChangedListener , MessagePicturesLayout.Callback {
    private static final String TAG = FireAddActivity.class.getSimpleName();
    private ImageView goToMapView;
    private LocationClient mLocationClient;

    public static final int MAP_REUEST_CODE = 2;
    public static final double LATITUDE_DEF = 0.00;//默认天安数码城: latitude: 36.32087806111286, longitude: 120.44349123197962
    public static final double LONGTITUDE_DEF = 0.00;//默认天安数码城: latitude: 36.32087806111286, longitude: 120.44349123197962
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
    public int currentChooseArea = 0;  //当前在选择省还是市   0选择省  1选择市

    public List<Area> shengList;
    public List<String> shengStrList;
    public List<Area> shiList;
    public List<String> shiStrList;
    public List<Area> quList;
    public List<String> quStrList;
    private LinearLayout addFirePhotoLayout;
    private static final int REQUEST_CODE_CHOOSE = 23;
    public List<Uri> uriChooseList;
    private FrameLayout oneImageLayout;
    private FrameLayout twoImageLayout;
    private ImageView oneImage;
    private ImageView twoImage;
    private ImageView oneImageDelete;
    private ImageView twoImageDelete;
    private LinearLayout photoLayout;
    private TextView addFireButton;
    private EditText mianjiView;
    private EditText firenameview;
    private Bitmap evaluateOne;
    private Bitmap evaluateTwo;
    private Bitmap evaluateThree;
    private String imgPath;
    private ProgressDialog addFireDialog;

    private List<Area> allAreaList;
    private WheelView areaWy;
    public int shengSelectIndex = 0;
    public int shiSelectIndex = 0;
    public int quSelectIndex = 0;
    public boolean isChooseSheng = false;
    public String currentChooseSheng = "";
    public String currentChooseShi = "";
    public String currentChooseQu = "";
    private boolean fromMap = false;
    private String access_token;
    private ImageView backView;
    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    private double currentLongitude = LATITUDE_DEF;
    private double currentLatitude = LONGTITUDE_DEF;
    private boolean isChooseShipin = false;
    private String videoPath = "";
    private TextView shipinView;
    private String videoSericePath;
    private List<Object> imglist;
    @Autowired
    String token;
    private FirePresenter firePrecenter;
    private String videostr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_add);
        ARouter.getInstance().inject(this);
        progressDialog = new ProgressDialog(this);
        shengList = new ArrayList<>();
        shiList = new ArrayList<>();
        quList=new ArrayList<>();
        shengStrList = new ArrayList<>();
        shiStrList = new ArrayList<>();
        quStrList=new ArrayList<>();
        uriChooseList = new ArrayList<>();
        allAreaList = new ArrayList<>();
        imglist=new ArrayList<>();
        date = new StringBuffer();
        endDate = new StringBuffer();
        addFireDialog = new ProgressDialog(this);
        initDateTime();
        initView();
        bindView();

        if (new DbConfig(getApplicationContext()).getAreaList() == null) {
            getAreaFromService();
        }else {
            allAreaList = new DbConfig(getApplicationContext()).getAreaList();
            initArea();
        }

        getLocation();

        Log.e(TAG, "onCreate:token= " + token);

    }




    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("火情上报");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;

                }
            }
        });
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
      //  addFirePhotoLayout = (LinearLayout) findViewById(R.id.add_fire_photo_layout);
        oneImageLayout = (FrameLayout) findViewById(R.id.one_image_layout);
        twoImageLayout = (FrameLayout) findViewById(R.id.two_imag_layout);
        oneImage = (ImageView) findViewById(R.id.one_image);
        twoImage = (ImageView) findViewById(R.id.two_image);
        oneImageDelete = (ImageView) findViewById(R.id.one_image_delete);
        twoImageDelete = (ImageView) findViewById(R.id.two_image_delete);
        photoLayout = (LinearLayout) findViewById(R.id.photo_layout);
        addFireButton = (TextView) findViewById(R.id.add_fire_button);
        mianjiView = (EditText) findViewById(R.id.mianji_view);
        shipinView = (TextView) findViewById(R.id.shipin_view);
        firenameview=(EditText) findViewById(R.id.fire_name_view);
    }

    private void bindView() {

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
                        Log.e(TAG, "call: currentLongitude" +currentLongitude);
                        Log.e(TAG, "call: currentLatitude" +currentLatitude);
                        intent.putExtra("longitude_double", currentLongitude);
                        intent.putExtra("latitude_double", currentLatitude);
                        startActivityForResult(intent, MAP_REUEST_CODE);
                    }
                });
        RxViewAction.clickNoDouble(addFireButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (addressView.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "请输入详细地址", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (jingduView.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "请输入经度", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (weiduView.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "请输入纬度", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (fireTimeText.getText().toString().equals("时间")){
                            Toast.makeText(getApplicationContext(), "请输入时间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (uriChooseList.isEmpty()){
                            Toast.makeText(getApplicationContext(), "请至少选择一张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                      /*  if (mianjiView.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "请输入面积", Toast.LENGTH_SHORT).show();
                            return;
                        }*/
                        showDialogProgress(addFireDialog, "正在上传中...");
                        //firePrecenter.fireData(addressView.getText().toString(),"123");
                        if (uriChooseList.size()>0) {
                            postPictoService();
                        }else {
                            if (isChooseShipin){
                                postVideoToServiceRx();
                            }else {
                                postFireToService();
                            }
                           // postPictoService();
                            //postFireToService();
                        }
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
                                Toast.makeText(FireAddActivity.this, "请先选择省", Toast.LENGTH_SHORT).show();
                            }else {
                                String currentShengId = "";
                                for (int i = 0; i < shengList.size(); i++) {
                                    if (shengList.get(i).getName().equals(currentChooseSheng)) {
                                        currentShengId = shengList.get(i).getId();
                                    }
                                }

                                initShi(currentShengId);;
                            }
                        }
                    }
                });
        RxViewAction.clickNoDouble(quText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 2;
                        Log.e(TAG, "call: 12321--" + shiText.getText().toString());
                        if (fromMap){
                            showAreaDialog(quStrList);
                        }else {
                            if (shiText.getText().toString().equals("请选择市")){
                                Toast.makeText(FireAddActivity.this, "请先选择市", Toast.LENGTH_SHORT).show();
                            }else {
                                String currentshiId = "";
                                for (int i = 0; i < shiList.size(); i++) {
                                    if (shiList.get(i).getName().equals(currentChooseShi)) {
                                        currentshiId = shiList.get(i).getId();
                                    }
                                }

                                initqu(currentshiId);;
                            }
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
                        if (uriChooseList.size() == 1){  //只有一张图
                            uriChooseList.remove(0);
                            //  Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(oneImage);
                            photoLayout.setVisibility(View.GONE);
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

    /*    RxViewAction.clickNoDouble(addFirePhotoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList.size() == 2){
                            Toast.makeText(FireAddActivity.this, "最多可以添加两张图片", Toast.LENGTH_SHORT).show();
                        }else {
                            addImage();
                        }

                    }
                });*/
        RxViewAction.clickNoDouble(oneImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList.size() == 2){
                            Toast.makeText(FireAddActivity.this, "最多可以添加两张图片", Toast.LENGTH_SHORT).show();
                        }else {
                            addImage();
                        }
                    }
                });
        RxViewAction.clickNoDouble(shipinView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (shipinView.getText().equals("视频选择")|| shipinView.getText().equals("重新选择视频")){
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 66);
                        }
                    }
                });
    }
    private void postPictoService(){

        RequestParams params = new RequestParams(RequestUtils.SAVE_IMAGE);
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        String picStr = null;
        for (int i = 0; i < uriChooseList.size(); i++) {
            try {

                Uri uri = uriChooseList.get(i);
                String pathStr = ImageUtils.getRealPathFromURI(FireAddActivity.this,uri);
                int degree = ImageUtils.readPictureDegree(pathStr);
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                if (i == 0){
                    evaluateOne = rotaingImageView(degree, photo);
                    picStr = ImageUtils.savePhoto(evaluateOne, this.getObbDir().getAbsolutePath(), "fileName" + i);
                }else if (i == 1){
                    evaluateTwo = rotaingImageView(degree, photo);
                    picStr = ImageUtils.savePhoto(evaluateTwo, this.getObbDir().getAbsolutePath(), "fileName" + i);
                }
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
                        Log.i(TAG, "imgStrArray: "+imgStrArray.length());
                        for (int i = 0; i<imgStrArray.length(); i++){
                            imglist.add(imgStrArray.get(i));
                        }
                        if (isChooseShipin){
                            postVideoToServiceRx();
                        }else {
                            postFireToService();
                        }

                    }else {
                        Toast.makeText(FireAddActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" + ex );
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }
    private void postVideoToServiceRx() {
      /*  JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("file", new File(videoPath));
        } catch (JSONException e) {
        }*/

        RequestParams params = new RequestParams(RequestUtils.SAVE_IMAGE);
        params.addBodyParameter("file", new File(videoPath),null,videoPath);
        params.setAsJsonContent(true);
        params.setMultipart(true);
      //  params.setBodyContent(jsonObject.toString());
      //  Log.e(TAG, "postVideoToService: " + jsonObject.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "resource: --"  + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );

                try {
                    JSONObject videoobj = new JSONObject(result);
                    String code = videoobj.getString("code");
                    if (code.equals("200")){
                        JSONObject data = videoobj.getJSONObject("data");
                        JSONArray imgStrArray = data.getJSONArray("img");
                        Log.i(TAG, "imgStrArray: "+imgStrArray.length());
                        for (int i = 0; i<imgStrArray.length(); i++){
                            videostr= (String) imgStrArray.get(i);
                        }

                        postFireToService();
                    }else {
                        Toast.makeText(FireAddActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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
    private void postFireToService() {

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
        String quStr = quText.getText().toString();
        String quId = "";
        for (int i = 0; i < quList.size(); i++) {
            if (quList.get(i).getName().equals(quStr)){
                quId = quList.get(i).getId();
            }
        }
        String addressStr = addressView.getText().toString();
        String jingduStr = jingduView.getText().toString();
        String weiduStr = weiduView.getText().toString();
        String timeStr = fireTimeText.getText().toString();
        String tudiMianjiStr = mianjiView.getText().toString();
        String nameStr =firenameview.getText().toString();

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
            jsonObject.put("fireName",nameStr);
            jsonObject.put("fireNo"," ");
            jsonObject.put("status",0);
            jsonObject.put("countyName",quStr);
            jsonObject.put("countyCode",quId);
            jsonObject.put("fireArea",tudiMianjiStr);
            jsonObject.put("videoPath1",videostr);
            if (imglist.size()>0){
                jsonObject.put("picPath1",imglist.get(0).toString());
                if (imglist.size()>1){
                    jsonObject.put("picPath2",imglist.get(1).toString());
                }
            }


        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL +  "fire/api/reportFirealarm");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());

        Log.e(TAG, "postFireToService: "+jsonObject.toString());
        Log.e(TAG, "postDataService:反馈---11 " + params);
        Log.e(TAG, "postDataService:反馈---11 " + weiduStr);
        params.setConnectTimeout(10000);
        params.addHeader("Authorization","bearer " + token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(FireAddActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(FireAddActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" + ex.toString() );
                Toast.makeText(FireAddActivity.this, "请连接内网上传", Toast.LENGTH_SHORT).show();
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
                        Matisse.from(FireAddActivity.this)
                                .choose(MimeType.allOf())
                                .countable(true)
                                .capture(true)
                                .captureStrategy(
                                        new CaptureStrategy(true,"com.haohai.platform.fireforestplatform")
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult:resultCode " + resultCode + "requestcode" + requestCode);
//        Log.e(TAG, "onActivityResult:data ",data);
        if (requestCode == MAP_REUEST_CODE && resultCode == RESULT_OK) {
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
                initquByQuId(currentCiryId);

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
                oneImageDelete.setVisibility(View.VISIBLE);
                Glide.with(this).load(uriChooseList.get(0)).into(oneImage);
                Glide.with(this).load(uriChooseList.get(1)).into(twoImage);
            }else {                     //有一张图
                twoImageLayout.setVisibility(View.VISIBLE);
                twoImageDelete.setVisibility(View.GONE);
                oneImageDelete.setVisibility(View.VISIBLE);
                Glide.with(this).load(uriChooseList.get(0)).into(oneImage);
                //   Glide.with(this).load(R.drawable.ic_bigphoto).into(twoImage);
                twoImageLayout.setVisibility(View.GONE);
            }

        }else if (requestCode == 66 && resultCode == RESULT_OK && null != data) {
            isChooseShipin = true;
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            videoPath = cursor.getString(columnIndex);
            cursor.close();
            Log.e(TAG, "onActivityResult: " + videoPath);
            shipinView.setText("重新选择视频");
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

    }
    private void initquByQuId(String currentShiId) {
        quList.clear();
        quStrList.clear();
        quStrList.add("请选择区");
        for (int i = 0; i < allAreaList.size(); i++) {
            if (allAreaList.get(i).getParentId().equals(currentShiId)) {
                quList.add(allAreaList.get(i));
                quStrList.add(allAreaList.get(i).getName());
            }
        }
        //  showAreaDialog(shiStrList);
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
    /**
     * 获取区域数据
     */
    private void getAreaFromService() {
        showDialogProgress(progressDialog,"数据获取中...");
        JSONObject jsonObject = new JSONObject();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL +  "auth/api/sysArea/getAllSysArea");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + token);
        Log.i(TAG, "getAreaFromService: "+params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:diqu -- " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    allAreaList.clear();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String parentId = object.getString("parentId");
                        String level = object.getString("level");
                        String createTime = object.getString("createTime");
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
               progressDialog.dismiss();
            }
        });
    }
    private void initqu(String currentshiId) {
        quList.clear();
        quStrList.clear();
        quStrList.add("请选择区");
        for (int i = 0; i < allAreaList.size(); i++) {
            if (allAreaList.get(i).getParentId().equals(currentshiId)) {
                quList.add(allAreaList.get(i));
                quStrList.add(allAreaList.get(i).getName());
            }
        }

        showAreaDialog(quStrList);
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

    private void initArea() {
        shengStrList.clear();
        shengList.clear();
        shengStrList.add("请选择省");
        for (int i = 0; i < allAreaList.size(); i++) {
            if (allAreaList.get(i).getLevel().equals("1")) {
                shengList.add(allAreaList.get(i));
                shengStrList.add(allAreaList.get(i).getName());
            }
        }
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
                    quText.setText("请选择市");
                    currentChooseQu = "请选择市";
                    quSelectIndex = 0;
                }else if(currentChooseArea == 1){                          //选择市
                    currentChooseShi = areaWy.getSelectedItem();
                    shiSelectIndex = areaWy.getSelectedPosition();
                    shiText.setText(currentChooseShi);
                    quText.setText("请选择市");
                    currentChooseQu = "请选择市";
                    quSelectIndex = 0;
                }else {
                    currentChooseQu = areaWy.getSelectedItem();
                    quSelectIndex = areaWy.getSelectedPosition();
                    quText.setText(currentChooseQu);
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

    /**
     * 日期选择控件
     */
    private void showDataDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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
    @RequiresApi(api = Build.VERSION_CODES.M)
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
        int hour = date.get(Calendar.HOUR_OF_DAY);
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
        chooseHour = calendar.get(Calendar.HOUR_OF_DAY);
        chooseMinute = calendar.get(Calendar.MINUTE);

    }

    @Override
    public void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {

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

    public void getLocation() {
        //获得位置服务
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
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

                currentLongitude = longitude;
                currentLatitude = latitude;
                //   Toast.makeText(MainActivity.this, "经纬度发生改变了,经度" +longitude + "纬度" +latitude, Toast.LENGTH_SHORT).show();
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


            currentLongitude = longitude ;
            currentLatitude = latitude ;
            Log.e(TAG, "getLocation: --" + longitude);
            Log.e(TAG, "getLocation: *--" + latitude);
         /*   BigDecimal   la   =   new BigDecimal(latitude);
            double   lat = la.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();*/
            //    return longitude + "," + latitude;
            //   return "0.00,0.00";
        }else {
            //  return "0.00,0.00";
        }
    }


}
