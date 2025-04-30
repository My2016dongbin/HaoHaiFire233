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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.controllers.base.HhBaseActivity;
import com.skyline.terraexplorer.db.DangerRecord;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.models.LeiBie;
import com.skyline.terraexplorer.models.Leixing;
import com.skyline.terraexplorer.multitype.ChooseImage;
import com.skyline.terraexplorer.multitype.ChooseImageViewBinder;
import com.skyline.terraexplorer.utils.GifSizeFilter;
import com.skyline.terraexplorer.utils.ImagPagerUtil;
import com.skyline.terraexplorer.utils.ImageUtils;
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

import org.apache.poi.ss.formula.functions.Choose;
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

public class HiddenDangerActivity extends HBaseActivity implements ChooseImageViewBinder.OnChooseImageClickListener ,MessagePicturesLayout.Callback{

    private static final String TAG = HiddenDangerActivity.class.getSimpleName();
    private EditText fengxianEditView;
    private EditText zhengzhiEdit;
    private TextView zhenshiFireText;
    private TextView wubaoFireText;
    public int currentFireState = 0;  //1 是，0 否
    private FrameLayout oneImageLayout;
    private FrameLayout twoImageLayout;
    private ImageView oneImage;
    private ImageView twoImage;
    private ImageView oneImageDelete;
    private ImageView twoImageDelete;
    public List<Uri> uriChooseList;
    private static final int REQUEST_CODE_CHOOSE = 23;
    public boolean isHasPermission = true;
    private TextView postButton;
    private ProgressDialog jinduDialog;
    private Bitmap evaluateOne;
    private Bitmap evaluateTwo;
    private Bitmap evaluateThree;
    private Intent intent;
    private String id;
    private String name;
    private String type;
    private String access_token;
    private String jingduStr;
    private String weiduStr;
    private String fullPath = "";
    private String fullPath2 = "";
    private TextView leibieText;
    private TextView leixingText;
    public List<LeiBie> leiBieList;
    public int currentLeibie = 0;   //0 是 1否
    public int leibieSelectIndex = 0;
    public int leixingSelectIndex = 0;
    private WheelView areaWy;
    public String currentChooseLeibie = "";
    public String currentChooseLeixing = "";
    private ImageView backButton;
    private String checkTime;
    private String pic1 = "";
    private String pic2 = "";
    private String gridId;
    private String gridName;
    private String gridNo;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private ChooseImageViewBinder chooseImageViewBinder;
    private List<ChooseImage> list = new ArrayList<>();
    private ImageWatcher vImageWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_danger);

        uriChooseList = new ArrayList<>();
       /* if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
        }*/
        leiBieList = new ArrayList<>();
        jinduDialog = new ProgressDialog(this);
        intent = getIntent();
        id = intent.getStringExtra("ID");
        name = intent.getStringExtra("NAME");
        type = intent.getStringExtra("TYPE");
        jingduStr = intent.getStringExtra("JINGDU");
        weiduStr = intent.getStringExtra("WEIDU");

        gridId = intent.getStringExtra("GRID_ID");
        gridName = intent.getStringExtra("GRID_NAME");
        gridNo = intent.getStringExtra("GRID_NO");
        loginGetToken();
        initLeixingData();
        // judgePower();
        //  requestPower();
        initView();
        //配置点击查看大图
        initImageLoader();
    }

    private void initLeixingData() {
        List<Leixing> leixings = new ArrayList<>();
        leixings.add(new Leixing(11,"火种"));
        leixings.add(new Leixing(12,"可燃物"));
        leiBieList.add(new LeiBie("火源管控",leixings));

        List<Leixing> leixings1 = new ArrayList<>();
        leixings1.add(new Leixing(21,"水罐"));
        leixings1.add(new Leixing(22,"灭火机"));
        leixings1.add(new Leixing(23,"水泵"));
        leiBieList.add(new LeiBie("灭火设施",leixings1));

        List<Leixing> leixings2 = new ArrayList<>();
        leixings2.add(new Leixing(31,"防火车辆"));
        leixings2.add(new Leixing(32,"通信器材"));
        leixings2.add(new Leixing(33,"个人装备"));
        leiBieList.add(new LeiBie("物资储备",leixings2));

        List<Leixing> leixings3 = new ArrayList<>();
        leixings3.add(new Leixing(41,"应急方案"));
        leixings3.add(new Leixing(42,"值班备勤"));
        leixings3.add(new Leixing(43,"宣传教育"));
        leiBieList.add(new LeiBie("日常管理",leixings3));

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
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
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

    private void initView() {
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fengxianEditView = (EditText) findViewById(R.id.fengxian_edit);
        zhengzhiEdit = (EditText) findViewById(R.id.zhengzhi_edit);
        zhenshiFireText = (TextView) findViewById(R.id.zhenshi_fire_text);
        wubaoFireText = (TextView) findViewById(R.id.wubao_fire_text);
        oneImageLayout = (FrameLayout) findViewById(R.id.one_image_layout);
        twoImageLayout = (FrameLayout) findViewById(R.id.two_imag_layout);
        oneImage = (ImageView) findViewById(R.id.one_image);
        twoImage = (ImageView) findViewById(R.id.two_image);
        oneImageDelete = (ImageView) findViewById(R.id.one_image_delete);
        twoImageDelete = (ImageView) findViewById(R.id.two_image_delete);
        postButton = (TextView) findViewById(R.id.fankui_button);
        leibieText = (TextView) findViewById(R.id.leibie_text);
        leixingText = (TextView) findViewById(R.id.leixing_text);

        listView = (RecyclerView) findViewById(R.id.phote_recycle);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        listView.setLayoutManager(gridLayoutManager);
        adapter = new MultiTypeAdapter(items);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);

        chooseImageViewBinder = new ChooseImageViewBinder(this);
        chooseImageViewBinder.setListener(this);
        adapter.register(ChooseImage.class, chooseImageViewBinder);
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        updateData();

        leibieText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLeibie = 0;
                List<String> leibieStrList = new ArrayList<String>();
                for (int i = 0; i < leiBieList.size(); i++) {
                    leibieStrList.add(leiBieList.get(i).getName());
                }
                showLeibieDialog(leibieStrList);
            }
        });
        leixingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLeibie =1;
                List<String> leixingStrList = new ArrayList<String>();
                if (leibieSelectIndex == 0){
                    List<Leixing> list = leiBieList.get(0).getList();
                    for (int i = 0; i < list.size(); i++) {
                        leixingStrList.add(list.get(i).getName());
                    }
                }else if(leibieSelectIndex == 1){
                    List<Leixing> list = leiBieList.get(1).getList();
                    for (int i = 0; i < list.size(); i++) {
                        leixingStrList.add(list.get(i).getName());
                    }
                }else if(leibieSelectIndex == 2){
                    List<Leixing> list = leiBieList.get(2).getList();
                    for (int i = 0; i < list.size(); i++) {
                        leixingStrList.add(list.get(i).getName());
                    }
                }else if(leibieSelectIndex == 3){
                    List<Leixing> list = leiBieList.get(3).getList();
                    for (int i = 0; i < list.size(); i++) {
                        leixingStrList.add(list.get(i).getName());
                    }
                }
                showLeibieDialog(leixingStrList);
            }
        });

        RxViewAction.clickNoDouble(postButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "onClick: size" + uriChooseList.size());
                        Date date = new Date();

                        String time = date.toLocaleString();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

                        checkTime = dateFormat.format(date);
                        showDialogProgress(jinduDialog,"正在上传");
                        if (uriChooseList.size() > 0){
                            postPic1Service();
                        }else {
                            postData();
                        }
                    }
                });



        zhenshiFireText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFireState = 1;
                zhenshiFireText.setTextColor(getResources().getColor(R.color.c12));
                zhenshiFireText.setBackgroundResource(R.drawable.bg_text_lan);
                wubaoFireText.setTextColor(getResources().getColor(R.color.c6));
                wubaoFireText.setBackgroundResource(R.drawable.bg_text_hui);
            }
        });
        wubaoFireText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFireState = 0;
                zhenshiFireText.setTextColor(getResources().getColor(R.color.c6));
                zhenshiFireText.setBackgroundResource(R.drawable.bg_text_hui);
                wubaoFireText.setTextColor(getResources().getColor(R.color.c12));
                wubaoFireText.setBackgroundResource(R.drawable.bg_text_lan);
            }
        });
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

    private void showLeibieDialog(List<String> leibieStrList) {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        areaWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        areaWy.setIsLoop(false);
        if (currentLeibie == 0){
            areaWy.setItems(leibieStrList, leibieSelectIndex);//init selected position is 0 初始选中位置为0
        }else {
            areaWy.setItems(leibieStrList, leixingSelectIndex);//init selected position is 0 初始选中位置为0
        }

        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                if (currentLeibie == 0){
                    currentChooseLeibie = areaWy.getSelectedItem();
                    leibieSelectIndex = areaWy.getSelectedPosition();
                    leibieText.setText(currentChooseLeibie);
                    currentChooseLeixing = leiBieList.get(leibieSelectIndex).getList().get(0).getName();
                    leixingSelectIndex = 0;
                    leixingText.setText(currentChooseLeixing);
                }else {
                    currentChooseLeixing = areaWy.getSelectedItem();
                    leixingSelectIndex = areaWy.getSelectedPosition();
                    leixingText.setText(currentChooseLeixing);
                }

            }
        });
        if (currentLeibie == 0){
            new AlertDialog.Builder(this)
                    .setTitle("请选择隐患类别")
                    .setView(areaView)
                    .setPositiveButton("确定 ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String areStr = "";
                            String area = areaWy.getSelectedItem();


                        }
                    })
                    .show();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("请选择隐患类型")
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

    }

    private void postPic1Service() {


        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < list.size(); i++) {
            try {

                Uri uri = list.get(i).getUri();
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(20);
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
            pic1 = ImageUtils.savePhoto(this.evaluateOne, this.getObbDir().getAbsolutePath(),checkTime + "pic1");

        }
        if (evaluateTwo!=null){
            pic2 = ImageUtils.savePhoto(this.evaluateTwo, this.getObbDir().getAbsolutePath(),checkTime + "pic2");

        }


        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic1),null,pic1);
        params.addHeader("Authorization","bearer " + access_token);
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
                        if (evaluateTwo!=null){
                            postPic2Service();
                        }else {
                            postData();
                        }

                    }else {
                        Toast.makeText(HiddenDangerActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败" );
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

    private void savaDataIntoDb() {
        if (fengxianEditView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入风险描述", Toast.LENGTH_SHORT).show();
            jinduDialog.dismiss();
            return;
        }


        String dangerType = "";
        for (int i = 0; i < leiBieList.size(); i++) {
            List<Leixing> list = leiBieList.get(i).getList();
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getName().equals(currentChooseLeixing)) {
                    dangerType = list.get(j).getId() +"";
                }
            }
        }
        DangerRecord dangerRecord = new DangerRecord(id, name, type, fengxianEditView.getText().toString(), dangerType, zhengzhiEdit.getText().toString(), currentFireState, weiduStr, jingduStr, "", pic1, pic2,gridId,gridName,gridNo);
        DbConfig dbConfig = new DbConfig(getApplicationContext());
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(dangerRecord);

        } catch (DbException e) {
            e.printStackTrace();
        }
        jinduDialog.dismiss();
        finish();
    }

    private void postPic2Service() {

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/file/upload");
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
                Log.e(TAG, "onSuccess:------------- " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        fullPath2 = data.getJSONObject(0).getString("fullPath");
                        postData();

                    }else {
                        Toast.makeText(HiddenDangerActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
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

    private void postData() {
        if (fengxianEditView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入风险描述", Toast.LENGTH_SHORT).show();
            jinduDialog.dismiss();
            return;
        }


        String dangerType = "";
        for (int i = 0; i < leiBieList.size(); i++) {
            List<Leixing> list = leiBieList.get(i).getList();
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getName().equals(currentChooseLeixing)) {
                    dangerType = list.get(j).getId() +"";
                }
            }
        }

        Log.e(TAG, "postData: dangerType---" + dangerType );

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("resourceId",id);
            jsonObject.put("resourceName",name);
            jsonObject.put("resourceType",type);
            jsonObject.put("dangerDescription",fengxianEditView.getText().toString()); //隐患描述
            jsonObject.put("dangerType",dangerType); // 隐患类型 ,
            jsonObject.put("remark",zhengzhiEdit.getText().toString()); // 整治描述 ,
            jsonObject.put("status",currentFireState); // 整治描述 ,
            JSONObject postsion = new JSONObject();
            postsion.put("lat",Double.parseDouble(weiduStr));
            postsion.put("lng",Double.parseDouble(jingduStr));
            jsonObject.put("position",postsion); // 整治描述 ,

            jsonObject.put("pic1",fullPath);
            jsonObject.put("pic2",fullPath2);
            jsonObject.put("checkTime",checkTime);
            jsonObject.put("gridId",gridId);
            jsonObject.put("gridName",gridName);
            jsonObject.put("gridNo",gridNo);

        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/dangerCheck");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(HiddenDangerActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(HiddenDangerActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                savaDataIntoDb();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                jinduDialog.dismiss();
            }
        });

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
                        Matisse.from(HiddenDangerActivity.this)
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
        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(HiddenDangerActivity.this, picList);
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
                Toast.makeText(HiddenDangerActivity.this, "1111", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            List<Uri> uriList = Matisse.obtainResult(data);

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
    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
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

    private void updateData() {
        items.clear();
        if (list==null){
            ChooseImage chooseImage = new ChooseImage();
            chooseImage.setAdd(true);
            items.add(chooseImage);
        }else {
            if (list.size()<9){
                for (int i = 0; i < list.size(); i++) {
                    items.add(list.get(i));
                }
                ChooseImage chooseImage = new ChooseImage();
                chooseImage.setAdd(true);
                items.add(chooseImage);
            }else {
                for (int i = 0; i < list.size(); i++) {
                    items.add(list.get(i));
                }
            }
        }

        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();

    }




    /**
     * 图片添加
     */
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
                            Matisse.from(HiddenDangerActivity.this)
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

            String oneUri = uri.toString();
            ArrayList<String> picList = new ArrayList<>();
            picList.add(oneUri); //点击哪张 把哪张放第一个
            for (int i = 0; i < list.size(); i++) {     //除去点击那张  其他放进去
                if (!oneUri.equals(list.get(i).getUri().toString())){
                    picList.add(list.get(i).getUri().toString());
                }
            };
            String content = "";     //放评论
            ImagPagerUtil imagPagerUtil = new ImagPagerUtil(HiddenDangerActivity.this, picList);
            imagPagerUtil.setContentText(content);
            imagPagerUtil.show();
        }
    }


    /**
     * 图片删除
     */
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
      /*  ImagPagerUtil imagPagerUtil = new ImagPagerUtil(HiddenDangerActivity.this, urlList);
        imagPagerUtil.show();*/
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}