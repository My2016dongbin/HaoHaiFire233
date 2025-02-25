package com.hht.hsatellitemobile.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hht.hsatellitemobile.R;
import com.hht.hsatellitemobile.db.DbConfig;
import com.hht.hsatellitemobile.ui.activity.base.HhBaseActivity;
import com.hht.hsatellitemobile.ui.cell.MessagePicturesLayout;
import com.hht.hsatellitemobile.ui.model.FeedBack;
import com.hht.hsatellitemobile.utils.GifSizeFilter;
import com.hht.hsatellitemobile.utils.ImagPagerUtil;
import com.hht.hsatellitemobile.utils.OKHttpHelper;
import com.hht.hsatellitemobile.utils.RequestUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Request;
import rx.functions.Action1;

/**
 * 反馈界面
 */
public class FeedBackActivity extends HhBaseActivity implements MessagePicturesLayout.Callback{

    private static final String TAG = FeedBackActivity.class.getSimpleName();
    private FrameLayout oneImageLayout;
    private FrameLayout twoImageLayout;
    private ImageView oneImage;
    private ImageView twoImage;
    private ImageView oneImageDelete;
    private ImageView twoImageDelete;
    private static final int REQUEST_CODE_CHOOSE = 23;
    public List<Uri> uriChooseList;
    private ActionBar actionBar;
    private EditText dizhiEdit;
    private EditText huoqingEdit;
    private String id;
    private String longitude;
    private String latitude;
    private TextView fankuiButton;
    private Bitmap evaluateOne;
    private Bitmap evaluateTwo;
    private Bitmap evaluateThree;
    private ProgressDialog jinduDialog;
    private EditText jingduEdittext;
    private EditText weiduEdittext;
    private String jingweiStr;
    private TextView zhenshiFireText;
    private TextView wubaoFireText;
    private List<String> imglist;
    public int currentFireState = 1;  //1 是，0 否

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        imglist=new ArrayList<>();
        uriChooseList = new ArrayList<>();
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("反馈");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        longitude = intent.getStringExtra("LO");
        latitude = intent.getStringExtra("LA");

        Log.e(TAG, "onCreate: id---" + id);
        Log.e(TAG, "onCreate: longitude---" + longitude );
        Log.e(TAG, "onCreate: latitude ---" + latitude);
        jinduDialog = new ProgressDialog(this);

        jingweiStr = getLocation();

        Log.e(TAG, "经纬度: " + jingweiStr);
        //配置点击查看大图
        initImageLoader();
        initView();


    }

    private void initView() {
        fankuiButton = (TextView) findViewById(R.id.fankui_button);
        dizhiEdit = (EditText) findViewById(R.id.dizhi_edit);
        huoqingEdit = (EditText) findViewById(R.id.huoqing_edit);
        oneImageLayout = (FrameLayout) findViewById(R.id.one_image_layout);
        twoImageLayout = (FrameLayout) findViewById(R.id.two_imag_layout);
        oneImage = (ImageView) findViewById(R.id.one_image);
        twoImage = (ImageView) findViewById(R.id.two_image);
        oneImageDelete = (ImageView) findViewById(R.id.one_image_delete);
        twoImageDelete = (ImageView) findViewById(R.id.two_image_delete);
        jingduEdittext = (EditText) findViewById(R.id.jingdu_edit);
        weiduEdittext = (EditText) findViewById(R.id.weidu_edit);
        zhenshiFireText = (TextView) findViewById(R.id.zhenshi_fire_text);
        wubaoFireText = (TextView) findViewById(R.id.wubao_fire_text);

        RxViewAction.clickNoDouble(zhenshiFireText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentFireState = 1;
                        zhenshiFireText.setTextColor(getResources().getColor(R.color.c12));
                        zhenshiFireText.setBackgroundResource(R.drawable.bg_text_lan);
                        wubaoFireText.setTextColor(getResources().getColor(R.color.c6));
                        wubaoFireText.setBackgroundResource(R.drawable.bg_text_hui);
                    }
                });
        RxViewAction.clickNoDouble(wubaoFireText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentFireState = 0;
                        zhenshiFireText.setTextColor(getResources().getColor(R.color.c6));
                        zhenshiFireText.setBackgroundResource(R.drawable.bg_text_hui);
                        wubaoFireText.setTextColor(getResources().getColor(R.color.c12));
                        wubaoFireText.setBackgroundResource(R.drawable.bg_text_lan);
                    }
                });

        if (!jingweiStr.isEmpty()){
            List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
            jingduEdittext.setText(jingweiList.get(0));
            weiduEdittext.setText(jingweiList.get(1));
        }

        RxViewAction.clickNoDouble(fankuiButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        imglist.clear();
                        if (uriChooseList.size()>0) {
                            for (int i = 0; i <uriChooseList.size() ; i++) {
                                postPictoService(i+1);
                            }
                        }else {
                            postDataService();
                        }
                      // okHttpPostData();
                    }
                });

        RxViewAction.clickNoDouble(oneImageLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList.size() == 0){  //添加图片
                            addImage();
                        }else {         //查看图片
                            showBigImage(0);
                        }

                    }
                });

        RxViewAction.clickNoDouble(twoImageLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList.size() == 1){
                            addImage();
                        }else {
                            showBigImage(1);
                        }

                    }
                });

        RxViewAction.clickNoDouble(oneImageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList.size() == 1){  //只有一张图
                            uriChooseList.remove(0);
                            Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(oneImage);
                            oneImageDelete.setVisibility(View.GONE);
                            twoImageLayout.setVisibility(View.GONE);
                        }else {     //如果有两张图
                            uriChooseList.remove(0);
                            Glide.with(getApplicationContext()).load(uriChooseList.get(0)).into(oneImage);
                            Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
                            oneImageDelete.setVisibility(View.VISIBLE);
                            twoImageDelete.setVisibility(View.GONE);
                        }
                    }
                });

        RxViewAction.clickNoDouble(twoImageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        uriChooseList.remove(1);
                        Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
                        twoImageDelete.setVisibility(View.GONE);
                    }
                });
    }



    private void postDataService() {
        if (dizhiEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (huoqingEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入火情描述", Toast.LENGTH_SHORT).show();
            return;
        }

        showDialogProgress(jinduDialog, "正在上传中...");
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

        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("FireAlarmId",id);
            jsonObject.put("fire_id",id);
            jsonObject.put("Longitude",longitude);
            jsonObject.put("longitude",longitude);
            jsonObject.put("Latitude",latitude);
            jsonObject.put("latitude",latitude);
            jsonObject.put("IsReal",currentFireState);
            jsonObject.put("is_real",currentFireState);
            jsonObject.put("Address",dizhiEdit.getText().toString());
            jsonObject.put("address",dizhiEdit.getText().toString());
            jsonObject.put("description",huoqingEdit.getText().toString());
            jsonObject.put("pic_path3",huoqingEdit.getText().toString());
            jsonObject.put("UserName",new DbConfig(this).getUser().getUsername());
            jsonObject.put("operation_user",new DbConfig(this).getUser().getUsername());
            if (imglist.size()>0){
                jsonObject.put("pic_path1",imglist.get(0));
                if (imglist.size()>1){
                    jsonObject.put("pic_path2",imglist.get(1));
                }
            }
        } catch (JSONException e) {
        }*/
        FeedBack feedBack = new FeedBack();
        feedBack.setFire_id(id);
        feedBack.setLongitude(longitude);
        feedBack.setLatitude(latitude);
        feedBack.setIs_real(currentFireState);
        feedBack.setAddress(dizhiEdit.getText().toString());
        feedBack.setPic_path3(huoqingEdit.getText().toString());
        feedBack.setOperation_user(new DbConfig(this).getUser().getUsername());
        if (imglist.size()>0){
            feedBack.setPic_path1(imglist.get(0));
            Log.e(TAG, "postDataService: imglist.get(0) = " + imglist.get(0) );
            if (imglist.size()>1){
                feedBack.setPic_path2(imglist.get(1));
                Log.e(TAG, "postDataService: imglist.get(1) = " + imglist.get(1) );
            }
        }
        String toJson = new Gson().toJson(feedBack);

        //  RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_FANKUI);
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL+"Satellite/InsertFireFeedback");
        params.setAsJsonContent(true);
        params.setBodyContent(toJson);
        params.addBodyParameter("token",new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postDataService:反馈---11 ");
        params.setConnectTimeout(10000);
        Log.e(TAG, "反馈---" + params);
        Log.e(TAG, "反馈---" + toJson);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;
                    if (result.equals("1")){
                        Toast.makeText(FeedBackActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
                        finish();

                    }else {
                        Toast.makeText(FeedBackActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" + ex.toString() );
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(jinduDialog);
            }
        });
      /*  x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "反馈: " + result);
              *//*  JSONObject jsonObject = null;
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
                }*//*
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(FeedBackActivity.this, "网络异常，请检查网络链接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });*/
    }


    private void showBigImage(int phoneNum) {
        ArrayList<String> picList = new ArrayList<>();
        String oneUri = "";
        if (phoneNum == 0){
             oneUri = uriChooseList.get(0).toString();
            picList.add(oneUri);
            if (uriChooseList.size() == 2){
                picList.add(uriChooseList.get(1).toString());
            }
        }else {
             oneUri = uriChooseList.get(1).toString();
            picList.add(oneUri);
            picList.add(uriChooseList.get(0).toString());
        }
        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(FeedBackActivity.this, picList);
        imagPagerUtil.setContentText("");
        imagPagerUtil.show();


       /* picList.add(oneUri); //点击哪张 把哪张放第一个
        for (int i = 0; i < list.size(); i++) {     //除去点击那张  其他放进去
            if (!oneUri.equals(list.get(i).getUri().toString())){
                picList.add(list.get(i).getUri().toString());
            }
        };
        String content = evaluateEditText.getText().toString();     //放评论
        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(EvaluateActivity.this, picList);
        imagPagerUtil.setContentText(content);
        imagPagerUtil.show();*/
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
                        Matisse.from(FeedBackActivity.this)
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

    @Override
    public void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> uriList = Matisse.obtainResult(data);
            for (int i = 0; i < uriList.size(); i++) {
                uriChooseList.add(uriList.get(i));
            }

            if (uriChooseList.size()>1){  //有两张图
                twoImageLayout.setVisibility(View.VISIBLE);
                twoImageDelete.setVisibility(View.VISIBLE);
                oneImageDelete.setVisibility(View.VISIBLE   );
                Glide.with(this).load(uriChooseList.get(0)).into(oneImage);
                Glide.with(this).load(uriChooseList.get(1)).into(twoImage);
            }else {                     //有一张图
                twoImageLayout.setVisibility(View.VISIBLE);
                twoImageDelete.setVisibility(View.GONE);
                oneImageDelete.setVisibility(View.VISIBLE);
                Glide.with(this).load(uriChooseList.get(0)).into(oneImage);
                Glide.with(this).load(R.drawable.ic_bigphoto).into(twoImage);
            }

        }
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
    private void postPictoService(final int num){

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL+"AppFirealarm/Upload");
        params.addBodyParameter("token",new DbConfig(this).getUser().getToken());
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
                imglist.add(result.replaceAll("\"",""));
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
                    postDataService();
                }
            }
        });
    }
}
