package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.db.CheckCom;
import com.skyline.terraexplorer.db.CheckField;
import com.skyline.terraexplorer.db.CheckRecord;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.db.ImagesUrl;
import com.skyline.terraexplorer.multitype.CheckFieldViewBinder;
import com.skyline.terraexplorer.multitype.ChooseImage;
import com.skyline.terraexplorer.multitype.ChooseImageViewBinder;
import com.skyline.terraexplorer.utils.GifSizeFilter;
import com.skyline.terraexplorer.utils.ImagPagerUtil;
import com.skyline.terraexplorer.utils.ImageUtils;
import com.skyline.terraexplorer.utils.RequestUtils;
import com.skyline.terraexplorer.utils.RxViewAction;
import com.skyline.terraexplorer.views.LinePathView;
import com.skyline.terraexplorer.views.MessagePicturesLayout;
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
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.ielse.view.imagewatcher.ImageWatcher;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ResourceCheckActivity extends HBaseActivity implements CheckFieldViewBinder.OnCheckFieldItemClick , ChooseImageViewBinder.OnChooseImageClickListener , MessagePicturesLayout.Callback{

    private static final String TAG = ResourceCheckActivity.class.getSimpleName();
    private Intent intent;
    private String planId;
    private String id;
    private String name;
    private String type;
    private LinePathView mPathView;
    private TextView clearQianmingView;
    private TextView postDataButton;
    private String access_token;
    private String fullPath = "";
    private String fullOnePath = "";
    private String fullImagePath = "";
    private String fullImagePath2 = "";
    public List<String> fullList;
    private Switch yingjiView;
    private Switch yanlianView;
    private Switch wanggehuaView;
    private Switch baozeView;
    private Switch wuziView;
    private Switch zhibanView;
    private Switch renyuanView;
    private Switch yinhuanView;
    private Switch jianchaView;
    private Switch jiaoyuView;
    private ProgressDialog progressDialog;
    private ImageView backButton;
    private Dialog qianmingDialog;
    private View qianmingInflater;
    private TextView qianmingButton;
    private String checkTime;
    private String checkTimeOne;
    private boolean hasOneQianming = false;

    private FrameLayout oneImageLayout;
    private FrameLayout twoImageLayout;
    private ImageView oneImage;
    private ImageView twoImage;
    private ImageView oneImageDelete;
    private ImageView twoImageDelete;
    public List<Uri> uriChooseList;
    public boolean isHasPermission = true;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private EditText beizhuEdit;
    private Bitmap evaluate;
    private Bitmap evaluateOne;
    private Bitmap evaluateTwo;
    private Bitmap evaluateThree;
    private String code;
    private List<CheckField> checkFieldList;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private List<Object> photoItems = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private CheckFieldViewBinder checkFieldProvider;
    private View rootView;
    private int rootViewVisibleHeight;
    private FrameLayout buttonLayout;
    private String apiurl;
    private String pic1 = "";
    private String pic2 = "";
    private Paint paint;
    private String gridId;
    private String gridName;
    private String gridNo;
    private Dialog qianmingOneDialog;
    private View qianmingOneInflater;
    private LinePathView mPathOneView;
    private TextView clearQianmingOneView;
    private TextView qianmingOneButton;
    private TextView qianmingPassButton;
    private RecyclerView photoListView;
    private MultiTypeAdapter photoAdapter;
    private ChooseImageViewBinder chooseImageViewBinder;
    private ImageWatcher vImageWatcher;

    private boolean hasNet = true;
    private List<ChooseImage> list = new ArrayList<>();
    public int currentPostNum = 0;
    private LinearLayout chooseLayout;
    private ImageView chooseImage;
    private boolean currentChoose = true;
    private DbManager db;

    class ImagePostThread extends Thread {

        @Override
        public void run() {
            super.run();

            postMoreImagesService(0);



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_check);
        progressDialog = new ProgressDialog(this);


        access_token = new DbConfig(this).getUser().getToken();
        db = new DbConfig(this).getDbManager();
        uriChooseList = new ArrayList<>();
        checkFieldList = new ArrayList<>();
        fullList = new ArrayList<>();
      /*  if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
        }*/
        intent = getIntent();
        planId = intent.getStringExtra("planId");
        id = intent.getStringExtra("ID");
        name = intent.getStringExtra("NAME");
        type = intent.getStringExtra("TYPE");
        code = intent.getStringExtra("CODE");
        apiurl = intent.getStringExtra("APIURL");
        //gridId,gridName,gridNo
        gridId = intent.getStringExtra("GRID_ID");
        gridName = intent.getStringExtra("GRID_NAME");
        gridNo = intent.getStringExtra("GRID_NO");

        Log.e(TAG, "onCreate:id= " +id );
        Log.e(TAG, "onCreate:name= " +name );
        Log.e(TAG, "onCreate:type= " +type );
        Log.e(TAG, "onCreate:code= " +code );
        Log.e(TAG, "onCreate:apiurl= " +apiurl );
        Log.e(TAG, "onCreate:gridId= " +gridId );
        Log.e(TAG, "onCreate:gridName= " +gridName );
        Log.e(TAG, "onCreate:gridNo= " +gridNo );

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);

        //loginGetToken();

        initView();

        //配置点击查看大图
        initImageLoader();

        initCheckFieldToDb();

    }

    private void initCheckFieldToDb() {
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        checkFieldList.clear();
        try {
            Log.e(TAG, "initCheckFieldToDb:-code-- " + code );
            checkFieldList = db.selector(CheckField.class)
                    .where("resourcetype", "=", code)
                    .findAll();
            Log.e(TAG, "initCheckFieldToDb:-size-- " + checkFieldList.size() );
            if (checkFieldList.size() > 0)
                initCheckFieldData();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initCheckFieldData() {
        items.clear();
        for (int i = 0; i < checkFieldList.size(); i++) {
            items.add(checkFieldList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        beizhuEdit = (EditText) findViewById(R.id.beizhu_edit);
        backButton = (ImageView) findViewById(R.id.back_button);

        chooseLayout = (LinearLayout) findViewById(R.id.choose_layout);
        chooseImage = (ImageView) findViewById(R.id.choose_image);
        RxViewAction.clickNoDouble(chooseLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentChoose){
                            currentChoose = false;
                            chooseImage.setImageResource(R.drawable.ic_choose_no);
                        }else {
                            currentChoose = true;
                            chooseImage.setImageResource(R.drawable.ic_choose);
                        }
                    }
                });

        listView = (RecyclerView) findViewById(R.id.list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        oneImageLayout = (FrameLayout) findViewById(R.id.one_image_layout);
        twoImageLayout = (FrameLayout) findViewById(R.id.two_imag_layout);
        oneImage = (ImageView) findViewById(R.id.one_image);
        twoImage = (ImageView) findViewById(R.id.two_image);
        oneImageDelete = (ImageView) findViewById(R.id.one_image_delete);
        twoImageDelete = (ImageView) findViewById(R.id.two_image_delete);

        // mPathView = (LinePathView) findViewById(R.id.path_view);
        //  clearQianmingView = (TextView) findViewById(R.id.clear_qianming_view);
        postDataButton = (TextView) findViewById(R.id.post_data_view);

        qianmingDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        qianmingInflater = LayoutInflater.from(this).inflate(R.layout.dialog_qianming,null);
        qianmingInflater.setMinimumWidth(10000);
        mPathView = ((LinePathView) qianmingInflater.findViewById(R.id.path_view));
        clearQianmingView = ((TextView) qianmingInflater.findViewById(R.id.clear_qianming_view));
        qianmingButton = ((TextView) qianmingInflater.findViewById(R.id.qianming_button));

        qianmingDialog.setContentView(qianmingInflater);
        Window qianmingDialogWindow = qianmingDialog.getWindow();
        qianmingDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpSearch = qianmingDialogWindow.getAttributes();
        qianmingDialogWindow.setAttributes(lpSearch);
        qianmingDialog.setCanceledOnTouchOutside(true);

        qianmingOneDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        qianmingOneInflater = LayoutInflater.from(this).inflate(R.layout.dialog_qianming_one,null);
        qianmingOneInflater.setMinimumWidth(10000);
        mPathOneView = ((LinePathView) qianmingOneInflater.findViewById(R.id.path_one_view));
        clearQianmingOneView = ((TextView) qianmingOneInflater.findViewById(R.id.clear_qianming_one_view));
        qianmingOneButton = ((TextView) qianmingOneInflater.findViewById(R.id.qianming_one_button));
        qianmingPassButton = ((TextView) qianmingOneInflater.findViewById(R.id.qianming_pass_button));

        qianmingOneDialog.setContentView(qianmingOneInflater);
        Window qianmingOneDialogWindow = qianmingOneDialog.getWindow();
        qianmingOneDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpOneSearch = qianmingOneDialogWindow.getAttributes();
        qianmingOneDialogWindow.setAttributes(lpOneSearch);
        qianmingOneDialog.setCanceledOnTouchOutside(true);

        mPathView.clear();
        mPathView.setBackColor(Color.WHITE);
        mPathView.setPaintWidth(20);
        mPathView.setPenColor(Color.BLACK);
        mPathView.setVisibility(View.VISIBLE);

        mPathOneView.clear();
        mPathOneView.setBackColor(Color.WHITE);
        mPathOneView.setPaintWidth(20);
        mPathOneView.setPenColor(Color.BLACK);
        mPathOneView.setVisibility(View.VISIBLE);


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
        //跳过第二次签名
        RxViewAction.clickNoDouble(qianmingPassButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        saveCheckIntoZongheDb();    //将检查计划同步到综合检查中
                        hasOneQianming = false;
                        if (list.size() > 0){
                            showDialogProgress(progressDialog,"正在上传");

                            new ImagePostThread().start();
                         //   postImageService();    //上传图片
                        }else {
                            showDialogProgress(progressDialog,"正在上传");
                            Log.e(TAG, "postdata--5" );
                            postQianmingService();//上传签名
                        }
                    }
                });
        //第二次签名
        RxViewAction.clickNoDouble(qianmingOneButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "postdata--qianmingOneButton ");
                        saveCheckIntoZongheDb();    //将检查计划同步到综合检查中
                        if (mPathOneView.getTouched()) {
                            hasOneQianming = true;
                            try {
                                Date date = new Date();

                                String time = date.toLocaleString();

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

                                checkTimeOne = dateFormat.format(date);

                                mPathOneView.save(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + checkTimeOne +"qm1.png", true, 10);


                                if (list.size() > 0){
                                    showDialogProgress(progressDialog,"正在上传");
                                    new ImagePostThread().start();
                                   /* for (int i = 0; i < list.size(); i++) {
                                        postMoreImagesService(i);
                                    }*/
                                   // postImageService();    //上传图片
                                }else {
                                    Log.e(TAG, "postdata--4" );
                                    showDialogProgress(progressDialog,"正在上传");
                                    postQianmingService();//上传签名
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, "postdata--e " + e.toString() );
                            }
                        } else {
                            Toast.makeText(ResourceCheckActivity.this, "您没有签名~请签名后提交", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //第一次签名
        RxViewAction.clickNoDouble(qianmingButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "postdata--qianmingButton ");
                        if (mPathView.getTouched()) {
                            try {
                                Date date = new Date();

                                String time = date.toLocaleString();

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

                                checkTime = dateFormat.format(date);

                                mPathView.save(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + checkTime +"qm.png", true, 10);

                                qianmingDialog.dismiss();
                                qianmingOneDialog.show();

                            } catch (IOException e) {
                                e.printStackTrace();

                                Log.e(TAG, "postdata--e " + e.toString() );
                            }
                        } else {
                            Toast.makeText(ResourceCheckActivity.this, "您没有签名~请签名后提交", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        clearQianmingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPathView.clear();
                mPathView.setBackColor(Color.WHITE);
                mPathView.setPaintWidth(20);
                mPathView.setPenColor(Color.BLACK);
            }
        });
        clearQianmingOneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPathOneView.clear();
                mPathOneView.setBackColor(Color.WHITE);
                mPathOneView.setPaintWidth(20);
                mPathOneView.setPenColor(Color.BLACK);
            }
        });
        RxViewAction.clickNoDouble(postDataButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        qianmingDialog.show();
                    }
                });
     /*   postDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qianmingDialog.show();

              *//*  if (mPathView.getTouched()) {
                    try {
                        mPathView.save("/sdcard/qm.png", true, 10);
                        postQianmingService();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ResourceCheckActivity.this, "您没有签名~请签名后提交", Toast.LENGTH_SHORT).show();
                }*//*
            }
        });*/

        oneImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgePower();
                if (uriChooseList.size() == 0){  //添加图片
                    addImage();
                }else {         //查看图片
                    showBigImage(0);
                }
            }
        });
        twoImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriChooseList.size() == 1){
                    addImage();
                }else {
                    showBigImage(1);
                }
            }
        });
        oneImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        twoImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriChooseList.remove(1);
                Glide.with(getApplicationContext()).load(R.drawable.ic_bigphoto).into(twoImage);
                twoImageDelete.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 将检查计划同步到综合检查中
     */
    private void saveCheckIntoZongheDb() {
        if (currentChoose) {        //选择同步后进行数据库存储
            try {
                int yinhuanNum = 0;
                for (int i = 0; i < checkFieldList.size(); i++) {
                    if (checkFieldList.get(i).state == 0) {
                        yinhuanNum = yinhuanNum + 1;
                    }
                }
                List<CheckCom> checkComList = db.selector(CheckCom.class).findAll();
                if (checkComList !=null ){

                    if (checkComList.size()>0){
                        Log.e(TAG, "saveCheckIntoZongheDb: --" + yinhuanNum);
                        CheckCom checkCom = checkComList.get(0);
                        checkCom.setCheckNum(checkCom.getCheckNum() + 1);
                        checkCom.setYinhuanNum(checkCom.getYinhuanNum() + yinhuanNum);
                        db.saveOrUpdate(checkCom);
                    }else {
                        Log.e(TAG, "saveCheckIntoZongheDb: ++" + yinhuanNum);
                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
                        String checkSaveTime = dateFormat.format(date);
                        CheckCom checkCom = new CheckCom(checkSaveTime, "", "", "", "", "",1,yinhuanNum,"","");
                        db.saveOrUpdate(checkCom);
                    }
                }else {
                    Log.e(TAG, "saveCheckIntoZongheDb: ++" + yinhuanNum);
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
                    String checkSaveTime = dateFormat.format(date);
                    CheckCom checkCom = new CheckCom(checkSaveTime, "", "", "", "", "",1,yinhuanNum,"","");
                    db.saveOrUpdate(checkCom);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void register() {
        checkFieldProvider = new CheckFieldViewBinder();
        checkFieldProvider.setListener(this);
        adapter.register(CheckField.class, checkFieldProvider);
    }

    private void postMoreImagesService(final int index){
         Log.e(TAG, "postimage: " + index);
        ChooseImage chooseImage = list.get(index);
        try {

            Uri uri = chooseImage.getUri();
            int degree = ImageUtils.readPictureDegree(uri.toString());
            Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
            Bitmap shuiYinPhoto = drawTextToBitmap(this, photo,checkTime.replace("T"," ") + "  " + name, "", "", "", "", "", paint, 10, 40);
            evaluate = rotaingImageView(degree, shuiYinPhoto);

        } catch (IOException e) {

        }
        pic1 = ImageUtils.savePhoto(this.evaluate, this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(),checkTime + "pic" +index);

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic1),null,pic1);
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "postimage: " + params );
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "postimage: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        fullImagePath = data.getJSONObject(0).getString("fullPath");
                        fullList.add(fullImagePath);
                        currentPostNum++;
                        deleteImage(pic1,getApplicationContext());
                        Log.e(TAG, "postimage:--- " + currentPostNum );
                        Log.e(TAG, "postimage:list.size()--- " + list.size() );
                       /* if (index < list.size()){
                            postMoreImagesService(index + 1);
                        }else {
                            postQianmingService();//上传签名
                        }*/
                        if (currentPostNum >= list.size()){
                            Log.e(TAG, "postdata--3" );
                            postQianmingService();//上传签名
                        }else {
                            postMoreImagesService(index +1);
                        }


                    }else {
                        Toast.makeText(ResourceCheckActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" );
                hasNet = false;
                savaDataIntoDb();


            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void postImageService() {

        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < uriChooseList.size(); i++) {
            try {

                Uri uri = uriChooseList.get(i);
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);

                Bitmap shuiYinPhoto = drawTextToBitmap(this, photo,checkTime.replace("T"," ") + "  " + name, "", "", "", "", "", paint, 10, 40);
                if (i == 0){
                    evaluateOne = rotaingImageView(degree, shuiYinPhoto);
                }else if (i == 1){
                    evaluateTwo = rotaingImageView(degree, shuiYinPhoto);
                }else if (i == 2){
                    evaluateThree = rotaingImageView(degree, shuiYinPhoto);
                }
            } catch (IOException e) {

            }
        }
        if (evaluateOne!=null){
            pic1 = ImageUtils.savePhoto(this.evaluateOne, this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(),checkTime + "pic1");

        }
        if (evaluateTwo!=null){
            pic2 = ImageUtils.savePhoto(this.evaluateTwo, this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(),checkTime + "pic2");

        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic1),null,pic1);
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "postDataService: ---" +"bearer " + access_token  );
        Log.e(TAG, "反馈---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        fullImagePath = data.getJSONObject(0).getString("fullPath");
                        if (evaluateTwo!=null){
                            postPic2Service();
                        }else {
                       //     postQianmingService();//上传签名
                        }

                    }else {
                        Toast.makeText(ResourceCheckActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" );
                hasNet = false;
                savaDataIntoDb();


            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }

    //请求失败保存到本地数据库的方法
    private void savaDataIntoDb() {
        Gson gson = new Gson();
        String json = gson.toJson(checkFieldList);
        Log.e(TAG, "onError:-- " + json );

        // List<CheckField> list = gson.fromJson(json, new TypeToken<List<CheckField>>(){}.getType());

        String qianmingUrlStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + checkTime +"qm.png";
        Bitmap qmBitmap = BitmapFactory.decodeFile(qianmingUrlStr);
        String qmSTR = savePhoto(qmBitmap,getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "", checkTime + "qm");

        String qianmingOneUrlStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + checkTimeOne +"qm1.png";
        Bitmap qmOneBitmap = BitmapFactory.decodeFile(qianmingOneUrlStr);
        String qmOneSTR = savePhoto(qmOneBitmap,getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "", checkTimeOne + "qm1");

        CheckRecord checkRecord = new CheckRecord(checkTime,json, pic1, pic2, qmSTR,qmOneSTR, checkTime, id, name, beizhuEdit.getText().toString(),apiurl,gridId,gridName,gridNo);

        List<ImagesUrl> imagesUrlList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ChooseImage chooseImage = list.get(i);
            try {

                Uri uri = chooseImage.getUri();
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                Bitmap shuiYinPhoto = drawTextToBitmap(this, photo,checkTime.replace("T"," ") + "  " + name, "", "", "", "", "", paint, 10, 40);
                evaluate = rotaingImageView(degree, shuiYinPhoto);

            } catch (IOException e) {

            }
            String  pic = ImageUtils.savePhoto(this.evaluate, this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(),checkTime + "pic" + i);
            imagesUrlList.add(new ImagesUrl(checkRecord.getId(),pic));
        }

        try {
            Log.e(TAG, "imagesUrlList:已经缓存在本地-- " + imagesUrlList.size() );
            DbConfig dbConfig = new DbConfig(getApplicationContext());
            DbManager db = dbConfig.getDbManager();
            db.saveOrUpdate(checkRecord);
            db.saveOrUpdate(imagesUrlList);
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (hasOneQianming){
            qianmingOneDialog.dismiss();
        }else {
            qianmingDialog.dismiss();
        }

        progressDialog.dismiss();
        finish();




    }

    private void postPic2Service() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic2),null,pic2);
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "postDataService: ---" +"bearer " + access_token  );
        Log.e(TAG, "反馈---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        fullImagePath2 = data.getJSONObject(0).getString("fullPath");
                       // postQianmingService();//上传签名


                    }else {
                        Toast.makeText(ResourceCheckActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
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

    private void loginGetToken() {
        final RequestParams params = new RequestParams("http://27.223.18.10:10100/auth/oauth/token");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("username","geyang");
        params.addParameter("password","123456");
        params.addParameter("grant_type","password");
        params.addParameter("client_id","client_password");
        params.addParameter("client_secret","123456");
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
     * 第一个人签名
     */
    private void postQianmingService() {

        String qianmingUrlStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + checkTime +"qm.png";
        Bitmap qmBitmap = BitmapFactory.decodeFile(qianmingUrlStr);
        String qmSTR = savePhoto(qmBitmap,this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "", checkTime + "qm");

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(qmSTR),null,qmSTR);
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "postDataService: ---" +"bearer " + access_token  );
        Log.e(TAG, "postDataService: ---" +"qmSTR " + qmSTR  );
        Log.e(TAG, "反馈---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        fullPath = data.getJSONObject(0).getString("fullPath");
                        Log.e(TAG, "onSuccess: ----"  );
                        //网络获取数据
                        // postDataToService();
                        //本地数据
                        if (hasOneQianming){
                            postQianmingOneService();
                        }else {
                            Log.e(TAG, "postdata--1" );
                            postDataToServiceFromDb();
                        }

                    }else {
                        Toast.makeText(ResourceCheckActivity.this, "签名上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " );
                hasNet = false;
                savaDataIntoDb();
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
     *第二个人签名
     */
    private void postQianmingOneService() {

        Log.e(TAG, "qianming1: ");
        String qianmingOneUrlStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + checkTimeOne +"qm1.png";
        Bitmap qmOneBitmap = BitmapFactory.decodeFile(qianmingOneUrlStr);
        String qmOneSTR = savePhoto(qmOneBitmap,this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "", checkTimeOne + "qm1");

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(qmOneSTR),null,qmOneSTR);
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "postDataService: ---" +"bearer " + access_token  );
        Log.e(TAG, "qianming1---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "qianming1:fullOnePath " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        fullOnePath = data.getJSONObject(0).getString("fullPath");

                        Log.e(TAG, "onSuccess: ----"  );
                        //网络获取数据
                        // postDataToService();
                        //本地数据
                        Log.e(TAG, "postdata--2" );
                        postDataToServiceFromDb();
                    }else {
                        Toast.makeText(ResourceCheckActivity.this, "签名上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " );
                hasNet = false;
                savaDataIntoDb();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }


    private void postDataToServiceFromDb() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("checkPlanId",planId);
            jsonObject.put("resourceId",id);
            jsonObject.put("name",name);

            for (int i = 0; i < checkFieldList.size(); i++) {
                jsonObject.put(checkFieldList.get(i).getCode(), checkFieldList.get(i).state+"");
            }

            // jsonObject.put("type",type);
            jsonObject.put("signaturePic",fullPath);
            for (int i = 0; i < fullList.size(); i++) {
                Log.e(TAG, "postDataToServiceFromDb: 已上传" + i+1);
                int num = i + 1;
                jsonObject.put("pic" + num ,fullList.get(i));
            }
          /*  jsonObject.put("pic1",fullImagePath);
            if (!pic2.isEmpty()){
                jsonObject.put("pic2",fullImagePath2);
            }*/
            Log.e(TAG, "checkTime: ---" + checkTime);
            jsonObject.put("checkTime",checkTime);
            jsonObject.put("description",beizhuEdit.getText().toString());
            jsonObject.put("gridId",gridId);
            jsonObject.put("gridName",gridName);
            jsonObject.put("gridNo",gridNo);
            if (hasOneQianming){
                jsonObject.put("signaturePic1",fullOnePath);
            }

        } catch (JSONException e) {
        }
        Log.e(TAG, "postDataToService: ---" + jsonObject.toString() );
        RequestParams params = new RequestParams(/*RequestUtils.REQUEST_URL + "resource"*/ RequestUtils.REQUEST_URL_BASE_ + apiurl + "Check" );
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + access_token);
        params.setConnectTimeout(10000);
        Log.e(TAG, "postDataToServiceFromDb---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --数据上传成功--" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(ResourceCheckActivity.this, "上传成功", Toast.LENGTH_SHORT).show();

                        if (hasOneQianming){
                            qianmingOneDialog.dismiss();
                        }else {
                            qianmingDialog.dismiss();
                        }
                        finish();
                    }else {
                        Toast.makeText(ResourceCheckActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" +ex.toString());
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

    /*
  * 根据String Path 删除图片
  * */
    public static void deleteImage(String imgPath, Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?",
                new String[]{imgPath}, null);
        boolean result = false;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            int count = context.getContentResolver().delete(uri, null, null);
            result = count == 1;
        } else {
            File file = new File(imgPath);
            result = file.delete();
        }

        if (result) {
         /*   imageList.remove(imgPath);
            adapter.notifyDataSetChanged();
            Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();*/
            Log.e("UtilsRY ", "DeleteImage: 图片删除成功");
        } else {
            Log.e("UtilsRY ", "DeleteImage: 图片删除失败");
        }
    }

    /***
     *
     */
    private void postDataToService() {


        Log.e(TAG, "postDataToService: ---1" );
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("resourceId",id);
            jsonObject.put("name",name);

            jsonObject.put("isContingencyPlan", yingjiView.isChecked() ? "1":"0");
            jsonObject.put("isDrill",yanlianView.isChecked()?"1":"0");
            jsonObject.put("isGrid",wanggehuaView.isChecked()?"1":"0");
            jsonObject.put("isLeaderDuty",baozeView.isChecked()?"1":"0");
            jsonObject.put("isMaterialAbundant",wuziView.isChecked()?"1" : "0");
            jsonObject.put("isOnDuty",zhibanView.isChecked()?"1":"0");
            jsonObject.put("isPeopleAbundant",renyuanView.isChecked()?"1":"0");
            jsonObject.put("isProblemHandle",yinhuanView.isChecked()?"1":"0");
            jsonObject.put("isPropaganda",jiaoyuView.isChecked()?"1":"0");
            jsonObject.put("isRegularCheck",jianchaView.isChecked()?"1":"0");

            jsonObject.put("type",type);
            jsonObject.put("signaturePic",fullPath);
            jsonObject.put("pic1",fullImagePath);
            jsonObject.put("description",beizhuEdit.getText().toString());


        } catch (JSONException e) {
        }
        Log.e(TAG, "postDataToService: ---2" );
        RequestParams params = new RequestParams("http://27.223.18.10:10100/resource/api/resourceCheck");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "postDataService:反馈---11 ");
        params.setConnectTimeout(10000);
        Log.e(TAG, "反馈---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(ResourceCheckActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(ResourceCheckActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" +ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
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


    public static String savePhoto(Bitmap photoBitmap, String path,
                                   String photoName) {
        String localPath = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path, photoName + ".png");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                            fileOutputStream)) { // 转换完成
                        localPath = photoFile.getPath();
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                        fileOutputStream = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return localPath;
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

    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
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
                        Matisse.from(ResourceCheckActivity.this)
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
        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(ResourceCheckActivity.this, picList);
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
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            List<Uri> uriList = new ArrayList<>();
            Uri uri = data.getData();
            uriList.add(uri);

            //  Log.e(TAG, "onActivityResult: " + uriList.get(0).toString());
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
                // items.add(evaluateImage);
            }
            // assertAllRegistered(adapter,items);;
            // adapter.notifyDataSetChanged();
            updateData();
        }
      /*  if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
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

        }*/
    }

    /**
     * 检察按钮点击回调
     * @param id
     * @param state
     */
    @Override
    public void onCheckFieldItemClickLinstener(String id, int state) {
        for (int i = 0; i < checkFieldList.size(); i++) {
            if (checkFieldList.get(i).getId().equals(id)) {
                checkFieldList.get(i).state = state;
            }
        }
    }

    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    public static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text, String name, String ctx,
                                           String text1, String name1, String ctx1,
                                           Paint paint, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, paddingLeft, paddingTop, paint);
        canvas.drawText(name, paddingLeft, paddingTop+100, paint);
        canvas.drawText(ctx, paddingLeft, paddingTop+200, paint);

        canvas.drawText(text1, paddingLeft, paddingTop+300, paint);
        canvas.drawText(name1, paddingLeft, paddingTop+400, paint);
        canvas.drawText(ctx1, paddingLeft, paddingTop+500, paint);

        return bitmap;
    }


    @Override
    public void onImageAddClickListener(boolean add, Uri uri,String id,ChooseImage chooseImage) {
        if (add){
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            int size = 9 - list.size();
                            /*Matisse.from(ResourceCheckActivity.this)
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
                                    .forResult(REQUEST_CODE_CHOOSE);*/

                            Intent openBendiPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            openBendiPicIntent.setType("image/*");
                            startActivityForResult(openBendiPicIntent, REQUEST_CODE_CHOOSE);
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
            ImagPagerUtil imagPagerUtil = new ImagPagerUtil(ResourceCheckActivity.this, picList);
            imagPagerUtil.setContentText(content);
            imagPagerUtil.show();
        }
    }

    @Override
    public void onImageDelete(Uri uri,String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUri().equals(uri)) {
                list.remove(i);
            }
        }
        updateData();
    }

    @Override
    public void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {
        vImageWatcher.show(i, imageGroupList, urlList);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
}