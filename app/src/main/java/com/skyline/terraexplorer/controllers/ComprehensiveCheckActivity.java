package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.db.CheckImages;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.multitype.AddCheck;
import com.skyline.terraexplorer.multitype.AddCheckViewBinder;
import com.skyline.terraexplorer.multitype.CheckInfo;
import com.skyline.terraexplorer.multitype.CheckInfoViewBinder;
import com.skyline.terraexplorer.multitype.ChooseImage;
import com.skyline.terraexplorer.multitype.ChooseImageViewBinder;
import com.skyline.terraexplorer.utils.GifSizeFilter;
import com.skyline.terraexplorer.utils.ImagPagerUtil;
import com.skyline.terraexplorer.utils.ImageUtils;
import com.skyline.terraexplorer.utils.RequestUtils;
import com.skyline.terraexplorer.views.MessagePicturesLayout;
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

import static com.skyline.terraexplorer.utils.ImageUtils.rotaingImageView;
import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ComprehensiveCheckActivity extends HBaseActivity implements AddCheckViewBinder.OnAddCheckItemClick,CheckInfoViewBinder.OnCheckInfoItemClick,MessagePicturesLayout.Callback {

    private static final String TAG = ComprehensiveCheckActivity.class.getSimpleName();

    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private List<Object> photoItems = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private List<ChooseImage> list = new ArrayList<>();
    private List<CheckImages> fullPhtotList = new ArrayList<>();
    public List<CheckInfo> checkInfoList;
    public String currentId = "";

    private Paint paint;
    private ProgressDialog  progressDialog;
    private String photoCheckTime;
    private Bitmap evaluate;
    private String pic;
    private String token;
    private int currentPostNum = 0;
    private String currentCheckName;
    private String currentCheckInfoStr;
    private DbConfig dbConfig;
    private ImageView backButton;

    @Override
    public void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {

    }

    class ImagePostThread extends Thread {

        @Override
        public void run() {
            super.run();
            postMoreImagesService(0);
            fullPhtotList.clear();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprehensive_check);


        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        checkInfoList = new ArrayList<>();
        fullPhtotList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        dbConfig = new DbConfig(this);
        token = dbConfig.getUser().getToken();


        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        initView();

        try {
            List<CheckInfo> checkListFromDb = dbConfig.getDbManager().selector(CheckInfo.class).findAll();
            if (checkListFromDb!=null){
                checkInfoList = checkListFromDb;
               /* for (int i = 0; i < checkInfoList.size(); i++) {
                    List<CheckImages> checkImagesList = dbConfig.getDbManager().selector(CheckImages.class).where("checkid", "=", checkInfoList.get(i).getCheckId()).findAll();
                    checkInfoList.get(i).setFullImageList(checkImagesList);
                }*/
                updateData();
            }else {
                Date date = new Date();
                String time = date.toLocaleString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
                String checkTimeOne = dateFormat.format(date);
                checkInfoList.add(new CheckInfo(checkTimeOne,"","",list));
                updateData();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView = (RecyclerView) findViewById(R.id.check_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        CheckInfoViewBinder checkInfoViewBinder = new CheckInfoViewBinder();

        //checkInfoViewBinder.setImageListener(this);
        checkInfoViewBinder.setContext(this);
        checkInfoViewBinder.setListener(this);
        adapter.register(CheckInfo.class, checkInfoViewBinder);

        AddCheckViewBinder addCheckViewBinder = new AddCheckViewBinder();
        addCheckViewBinder.setListener(this);
        adapter.register(AddCheck.class, addCheckViewBinder);
    }

    /**
     * 图片添加
     */
    @Override
    public void onCheckImageAddClickListener(boolean add, Uri var2, String id, String name, String info) {

        currentId = id;
        currentCheckName = name;
        currentCheckInfoStr = info;
        Log.e(TAG, "onCheckImageAddClickListener: " + currentCheckName);
        Log.e(TAG, "onCheckImageAddClickListener: " + currentCheckInfoStr);
        if (add){
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            int size = 3 - list.size();
                            Matisse.from(ComprehensiveCheckActivity.this)
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
    }
    /**
     * 图片删除
     */
    @Override
    public void onCheckImageDelete(Uri uri, String id, String name, String info) {
        currentCheckName = name;
        currentCheckInfoStr = info;
        currentId = id;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUri().equals(uri)) {
                list.remove(i);
            }
        }
        for (int i = 0; i < checkInfoList.size(); i++) {
            if (checkInfoList.get(i).getCheckId().equals(currentId)) {
                checkInfoList.get(i).setChooseImageList(list);
                checkInfoList.get(i).setCheckName(currentCheckName);
                checkInfoList.get(i).setCheckInfo(currentCheckInfoStr);
            }
        }
        updateData();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
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
                Toast.makeText(this, "最多只能添加3张", Toast.LENGTH_SHORT).show();
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
            for (int i = 0; i < checkInfoList.size(); i++) {
                if (checkInfoList.get(i).getCheckId().equals(currentId)) {
                    checkInfoList.get(i).setChooseImageList(list);
                    Log.e(TAG, "onActivityResult: " + currentCheckName );
                    checkInfoList.get(i).setCheckName(currentCheckName);
                    checkInfoList.get(i).setCheckInfo(currentCheckInfoStr);
                }
            }
            // assertAllRegistered(adapter,items);;
            // adapter.notifyDataSetChanged();
            updateData();
        }

    }

    private void updateData() {
        items.clear();

      /*  for (int i = 0; i < checkInfoList.size(); i++) {
            List<CheckImages> checkImagesList = dbConfig.getDbManager().selector(CheckImages.class).where("checkid", "=", checkInfoList.get(i).getCheckId()).findAll();
            checkInfoList.get(i).setFullImageList(checkImagesList);
        }*/
        if (checkInfoList.size() == 0){
            Date date = new Date();
            String time = date.toLocaleString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
            String checkTimeOne = dateFormat.format(date);
            checkInfoList.add(new CheckInfo(checkTimeOne,"","",list));
        }

        for (int i = 0; i < checkInfoList.size(); i++) {
            List<CheckImages> checkImagesList = new ArrayList<>();
            try {
                checkImagesList = dbConfig.getDbManager().selector(CheckImages.class).where("checkid", "=", checkInfoList.get(i).getCheckId()).findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            checkInfoList.get(i).setFullImageList(checkImagesList);
            items.add(checkInfoList.get(i));
        }
        items.add(new AddCheck());

        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();

    }

    /**\
     * 添加资源点的饿点击回调
     */
    @Override
    public void onAddCheckItemClickListener() {
        if (checkInfoList.get(checkInfoList.size()-1).isSava) {     //上一条数据已经提交 //生成新的资源添加数据
            list.clear(); //上传成功后 删除图片
            Date date = new Date();
            String time = date.toLocaleString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
            String checkTimeOne = dateFormat.format(date);
            currentId = checkTimeOne;
            currentCheckInfoStr = "";
            currentCheckName = "";
            checkInfoList.add(new CheckInfo(checkTimeOne,"","",list));
            updateData();
        }else {
            Toast.makeText(this, "请保存资源点后继续添加下一资源点", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 资源点的保存或者删除
     * @param isSave
     */
    @Override
    public void OnCheckSaveOrDeleteClickListener(boolean isSave,String id,String checkName,String checkInfoStr) {
        currentId = id;
        currentCheckName = checkName;
        currentCheckInfoStr = checkInfoStr;
        if (isSave){        //保存  将图片上传到服务器
            if (currentCheckName.equals("")){
                Toast.makeText(this, "请输入检查点名称", Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentCheckInfoStr.equals("")){
                Toast.makeText(this, "请输入整体情况说明", Toast.LENGTH_SHORT).show();
                return;
            }
            Date date = new Date();
            String time = date.toLocaleString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

            photoCheckTime = dateFormat.format(date);
            showDialogProgress(progressDialog,"资源点上传中...");
            if (list.size() == 0){
                for (int i = 0; i < checkInfoList.size(); i++) {
                    if (checkInfoList.get(i).getCheckId().equals(currentId)) {
                        checkInfoList.get(i).setSava(true);
                        checkInfoList.get(i).setCheckName(currentCheckName);
                        checkInfoList.get(i).setCheckInfo(currentCheckInfoStr);

                        try {
                            dbConfig.getDbManager().saveOrUpdate(checkInfoList.get(i));
                            Log.e(TAG, "onSuccess: 对象保存成功" );
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        updateData();

                    }
                }
            }else {
                new ImagePostThread().start();
            }

        }else {      //删除  将该条数据从本地删除
            for (int i = 0; i < checkInfoList.size(); i++) {
                if (checkInfoList.get(i).getCheckId().equals(id)) {
                    try {
                        dbConfig.getDbManager().delete(checkInfoList.get(i));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    checkInfoList.remove(i);

                }
            }
            list.clear();

            updateData();
        }
    }


    private void postMoreImagesService(final int index){
        Log.e(TAG, "postMoreImagesService: list.size==" + list.size() );
        ChooseImage chooseImage = list.get(index);
        try {

            Uri uri = chooseImage.getUri();
            int degree = ImageUtils.readPictureDegree(uri.toString());
            Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
            Bitmap shuiYinPhoto = drawTextToBitmap(this, photo,photoCheckTime.replace("T"," ") + "  " + currentCheckName, "", "", "", "", "", paint, 10, 40);
            evaluate = rotaingImageView(degree, shuiYinPhoto);

        } catch (IOException e) {

        }
        pic = ImageUtils.savePhoto(this.evaluate, this.getObbDir().getAbsolutePath(),photoCheckTime + "pic" +index);

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic),null,pic);
        params.addHeader("Authorization","bearer " + token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:图片上传成功" + index );
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){

                        JSONArray data = jsonObject1.getJSONArray("data");
                        String fullImagePath = data.getJSONObject(0).getString("fullPath");
                        CheckImages checkImages = new CheckImages(currentId, fullImagePath);
                        fullPhtotList.add(checkImages);

                        currentPostNum++;
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveBindingId(checkImages);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        if (currentPostNum >= list.size()){ //已上传完
                            currentPostNum = 0;
                            for (int i = 0; i < checkInfoList.size(); i++) {
                                if (checkInfoList.get(i).getCheckId().equals(currentId)) {
                                    checkInfoList.get(i).setFullImageList(fullPhtotList);
                                    checkInfoList.get(i).setSava(true);
                                    checkInfoList.get(i).setCheckName(currentCheckName);
                                    checkInfoList.get(i).setCheckInfo(currentCheckInfoStr);

                                    try {
                                        db.saveOrUpdate(checkInfoList.get(i));
                                        Log.e(TAG, "onSuccess: 对象保存成功" );
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }


                                    progressDialog.dismiss();
                                    updateData();

                                }
                            }
                        }else {

                            postMoreImagesService(index +1);
                        }


                    }else {
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

    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text, String name, String ctx,
                                           String text1, String name1, String ctx1,
                                           Paint paint, int paddingLeft, int paddingTop) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
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
}
