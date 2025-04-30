package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.db.CheckCom;
import com.skyline.terraexplorer.db.CheckComImage;
import com.skyline.terraexplorer.db.CheckImages;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.db.Grid;
import com.skyline.terraexplorer.models.CheckDetail;
import com.skyline.terraexplorer.models.CheckDto;
import com.skyline.terraexplorer.models.CheckSaveData;
import com.skyline.terraexplorer.multitype.CheckInfo;
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

public class ComprehensiveCheckHomeActivity extends HBaseActivity implements ChooseImageViewBinder.OnChooseImageClickListener ,MessagePicturesLayout.Callback{

    private static final String TAG = ComprehensiveCheckHomeActivity.class.getSimpleName();
    private TextView goJianChaView;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private ChooseImageViewBinder chooseImageViewBinder;
    private List<ChooseImage> list = new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 23;
    private TextView saveButton;
    private EditText resourceNameView;
    private EditText zongtiView;
    private EditText yinhuanView;
    private EditText zhenggaiView;
    private EditText checkMenView;
    private String checkSaveTime;
    private DbManager db;
    private CheckCom currentCheckCom;
    private ImageView backButton;
    private ProgressDialog progressDialog;
    private Paint paint;
    private String checkTime;
    private Bitmap evaluate;
    private String pic1;
    private String token;
    public List<String> fullList;
    private int currentPostNum = 0;
    private LinearLayout quLayout;
    private TextView quText;
    private LinearLayout jiedaoLayout;
    private TextView jiedaoText;
    public List<Grid> quList;
    public List<String> quStrList;
    public List<Grid> jiedaoList;
    public List<String> jiedaoStrList;
    private WheelView areaWy;
    public int currentQuanxian = 0;   //0是全国权限  1是省级权限
    public int currentChooseArea = 0;  //当前在选择区还是街道   0选择区  1选择街道
    public String currentChooseQu = "请选择区";
    public String currentChooseJiedao = "请选择街道";
    public int quSelectIndex = 0;
    public int jieDaoSelectIndex = 0;
    public boolean isChooseQu = false;
    public String currentQuId;
    public String currentStreeNo;
    private int checkNum = 0;
    private int yinhuanNum = 0;
    private TextView checkInfoView;
    private int checkInfoNum = 0;
    private Intent intent;
    private String currentLong = "0";
    private String currentLat = "0";
    private String checkDbData;
    private String currentData;
    private TextView checkListButton;

    private class ImagePostThread extends Thread {

        @Override
        public void run() {
            super.run();
            postMoreImagesService(0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprehensive_check_home);
        initView();
        progressDialog = new ProgressDialog(this);
        quList = new ArrayList<>();
        Log.e(TAG, "onCreate: " + quList.size());
        quStrList = new ArrayList<>();
        jiedaoList = new ArrayList<>();
        jiedaoStrList = new ArrayList<>();

        intent = getIntent();
        currentLong = intent.getStringExtra("LNG");
        currentLat = intent.getStringExtra("LAT");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        currentData = dateFormat.format(date);

        db = new DbConfig(this).getDbManager();
        token = new DbConfig(this).getUser().getToken();
      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);

        fullList = new ArrayList<>();
        //获取区数据
        getQuData();

        try {
            List<CheckCom> checkComList = db.selector(CheckCom.class).findAll();
            List<CheckInfo> checkInfoList = db.selector(CheckInfo.class).findAll();
            //获取新增检查点的数量
            if (checkInfoList !=null){
                checkInfoNum = checkInfoList.size();
            }
            //获取综合检查资源的数据
            if (checkComList != null &&checkComList.size()>0){
                currentCheckCom = checkComList.get(0);
                checkDbData = currentCheckCom.getId().substring(0,currentCheckCom.getId().indexOf("T"));
                Log.e(TAG, "onCreate: id===" + checkDbData);
                Log.e(TAG, "onCreate: id====" + currentData);



                if (checkDbData.equals(currentData)){  //当天数据
           //         Toast.makeText(this, "当天数据，不取消", Toast.LENGTH_SHORT).show();
                    List<CheckComImage> chooseImageList = db.selector(CheckComImage.class).where("checkcomid", "=", currentCheckCom.getId()).findAll();
                    /**
                     * 判断9图选择 是否有图  有图的田间
                     */
                    if (chooseImageList!=null){
                        for (int i = 0; i < chooseImageList.size(); i++) {
                            Log.e(TAG, "onCreate: String===" + chooseImageList.get(i).getPic());
                            Uri uri = Uri.parse((String) chooseImageList.get(i).getPic());
                            Log.e(TAG, "onCreate: uri ===" + uri);
                            list.add(new ChooseImage( chooseImageList.get(i).getCheckComId(), uri,chooseImageList.get(i).isAdd));
                        }
                    }


                    currentChooseQu = currentCheckCom.getQuName();
                    currentChooseJiedao = currentCheckCom.getJiedaoName();
                    if (currentChooseQu != null) {
                        quText.setText(currentChooseQu);
                    }
                    if (currentChooseJiedao != null) {
                        jiedaoText.setText(currentChooseJiedao);
                    }


                    checkNum = currentCheckCom.getCheckNum();
                    yinhuanNum = currentCheckCom.getYinhuanNum();
                    Log.e(TAG, "onCreate: checkNum=" + checkNum );
                    Log.e(TAG, "onCreate: yinhuanNum=" + yinhuanNum );
                    int num = checkNum + checkInfoNum;
                    if (num == 0){
                        checkInfoView.setText("暂无检查情况");
                    }else {

                        checkInfoView.setText("共检查" +  num + "个检查点,共发现问题" + yinhuanNum + "个" );
                    }

                    resourceNameView.setText(currentCheckCom.getCheckName());
                    zongtiView.setText( currentCheckCom.getZongti());
                    yinhuanView.setText(currentCheckCom.getYinhuan());
                    zhenggaiView.setText(currentCheckCom.getZhenggai());
                    checkMenView.setText(currentCheckCom.getCheckmen());
                }else {     //不是当天数据 清空资源点数据库
                    db.delete(CheckCom.class);
                 //   Toast.makeText(this, "不是当天数据，已删除", Toast.LENGTH_SHORT).show();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        updateData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            List<CheckInfo> checkInfoList =db.selector(CheckInfo.class).findAll();

            if (checkInfoList !=null){
                checkInfoNum = checkInfoList.size();
            }
            int num = checkNum + checkInfoNum;
            if (num == 0){
                checkInfoView.setText("暂无检查情况");
            }else {
                checkInfoView.setText("共检查" +  num + "个检查点,共发现问题" + yinhuanNum + "个" );
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void initView() {

        checkListButton = (TextView) findViewById(R.id.check_list_button);
        RxViewAction.clickNoDouble(checkListButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),ComprehensiveCheckListActivity.class));
                    }
                });
        checkInfoView = (TextView) findViewById(R.id.check_info_view);
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resourceNameView = (EditText) findViewById(R.id.check_name_view);
        resourceNameView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        resourceNameView.setGravity(Gravity.TOP);
        resourceNameView.setSingleLine(false);
        resourceNameView.setSelection(resourceNameView.getText().toString().length());
        resourceNameView.setHorizontallyScrolling(false); //水平滚动设置为False

        zongtiView = (EditText) findViewById(R.id.zongti_view);
        zongtiView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        zongtiView.setGravity(Gravity.TOP);
        zongtiView.setSingleLine(false);
        zongtiView.setHorizontallyScrolling(false); //水平滚动设置为False

        yinhuanView = (EditText) findViewById(R.id.yinhuan_view);
        yinhuanView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        yinhuanView.setGravity(Gravity.TOP);
        yinhuanView.setSingleLine(false);
        yinhuanView.setHorizontallyScrolling(false); //水平滚动设置为False

        zhenggaiView = (EditText) findViewById(R.id.zhenggai_view);
        zhenggaiView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        zhenggaiView.setGravity(Gravity.TOP);
        zhenggaiView.setSingleLine(false);
        zhenggaiView.setHorizontallyScrolling(false); //水平滚动设置为False

        checkMenView = (EditText) findViewById(R.id.check_men_view);
        checkMenView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        checkMenView.setGravity(Gravity.TOP);
        checkMenView.setSingleLine(false);
        checkMenView.setHorizontallyScrolling(false); //水平滚动设置为False

        saveButton = (TextView) findViewById(R.id.save_button);
        goJianChaView = (TextView) findViewById(R.id.go_jiancha_view);
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

        quLayout = (LinearLayout) findViewById(R.id.qu_layout);
        quText = (TextView) findViewById(R.id.qu_text);
        jiedaoLayout = (LinearLayout) findViewById(R.id.jiedao_layout);
        jiedaoText = (TextView) findViewById(R.id.jiedao_text);

        RxViewAction.clickNoDouble(quLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 0;
                        getAllQu();
                    }
                });
        RxViewAction.clickNoDouble(jiedaoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       // getQuData();
                        currentChooseArea = 1;
                        //TODO
                        try{
                            for (int i = 0; i < quList.size(); i++) {
                                if (quList.get(i).getName().equals(currentChooseQu)) {
                                    currentQuId = quList.get(i).getId();
                                }
                            }
                            if (quText.getText().equals("请选择区")){
                                Toast.makeText(ComprehensiveCheckHomeActivity.this, "请先选择区", Toast.LENGTH_SHORT).show();

                            }else {
                                getAllJieDao();
                            }
                        }catch (Exception e){

                        }

                    }
                });


        RxViewAction.clickNoDouble(goJianChaView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),ComprehensiveCheckActivity.class));
                    }
                });

        RxViewAction.clickNoDouble(saveButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Date date = new Date();

                        String time = date.toLocaleString();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

                        checkTime = dateFormat.format(date);
                       postDataToService();
                    }
                });
    }

    /**
     * 获取区数据
     */
    private void getQuData() {
      //  quList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
            quList = db.selector(Grid.class)
                    .where("state", "=", "ACTIVE")
                    .and("level", "=", "3")
                    .and("groupid", "like", "001002%")
                    .findAll();

            quStrList.clear();
            quStrList.add("请选择区");
            //TODO
            try{
                for (int i = 0; i < quList.size(); i++) {
                    quStrList.add(quList.get(i).getName());
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

            if (!currentChooseJiedao.equals("请选择街道")){
                for (int i = 0; i < jiedaoList.size(); i++) {
                    if (jiedaoList.get(i).getName().equals(currentChooseJiedao)) {
                        jieDaoSelectIndex = i+1;
                    }
                }
            }

            showAreaDialog(jiedaoStrList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有区的数据
     */
    private void getAllQu() {
        /*quList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
            quList = db.selector(Grid.class)
                    .where("state", "=", "ACTIVE")
                    .where("level", "=", "1")
                    .findAll();

            quStrList.clear();
            quStrList.add("请选择区");
            for (int i = 0; i < quList.size(); i++) {
                quStrList.add(quList.get(i).getName());
            }*/
            if (!currentChooseQu.equals("请选择区")){
                for (int i = 0; i < quList.size(); i++) {
                    if (quList.get(i).getName().equals(currentChooseQu)) {
                        quSelectIndex = i+1;
                    }
                }
            }
           showAreaDialog(quStrList);
       /* } catch (DbException e) {
            e.printStackTrace();
        }*/
    }

    private void showAreaDialog(List<String> strList) {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        areaWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        areaWy.setIsLoop(false);
        if (currentChooseArea == 0){
            areaWy.setItems(strList, quSelectIndex);//init selected position is 0 初始选中位置为0
        }else {
            areaWy.setItems(strList, jieDaoSelectIndex);//init selected position is 0 初始选中位置为0
        }

        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                if (currentChooseArea == 0){   //选择省
                    isChooseQu = true;
                    currentChooseQu = areaWy.getSelectedItem();
                    quSelectIndex = areaWy.getSelectedPosition();
                    quText.setText(currentChooseQu);
                    currentChooseJiedao = "";
                    jiedaoText.setText("请选择街道");
                    jieDaoSelectIndex = 0;
                }else {                          //选择市
                    currentChooseJiedao = areaWy.getSelectedItem();
                    jieDaoSelectIndex = areaWy.getSelectedPosition();
                    jiedaoText.setText(currentChooseJiedao);
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

    /**
     * 提交图片到服务器
     * @param index
     */
    private void postMoreImagesService(final int index) {
        Log.e(TAG, "postimage: " + index);
        ChooseImage chooseImage = list.get(index);
        try {

            Uri uri = chooseImage.getUri();
            int degree = ImageUtils.readPictureDegree(uri.toString());
            Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
            Bitmap shuiYinPhoto = drawTextToBitmap(this, photo,checkTime.replace("T"," ") + "  " + currentLong +"," + currentLat, "", "", "", "", "", paint, 10, 40);
            evaluate = rotaingImageView(degree, shuiYinPhoto);

        } catch (IOException e) {

        }
        pic1 = ImageUtils.savePhoto(this.evaluate, this.getObbDir().getAbsolutePath(),checkTime + "pic" +index);

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic1),null,pic1);
        params.addHeader("Authorization","bearer " + token);
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
                        String fullImagePath = data.getJSONObject(0).getString("fullPath");
                        fullList.add(fullImagePath);

                        currentPostNum++;
                      //  deleteImage(pic1,getApplicationContext());
                        Log.e(TAG, "postimage:--- " + currentPostNum );
                        Log.e(TAG, "postimage:list.size()--- " + list.size() );
                       /* if (index < list.size()){
                            postMoreImagesService(index + 1);
                        }else {
                            postQianmingService();//上传签名
                        }*/
                        if (currentPostNum >= list.size()){
                            Log.e(TAG, "postdata--3" );
                            postCheckDataToService();
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

    /**
     * 上传检查数据到服务器
     */
    private void postCheckDataToService() {

        String name = resourceNameView.getText().toString();
        String zongti = "";
        if (!checkInfoView.getText().equals("暂无检查情况")){
            zongti= checkInfoView.getText().toString() + "," +zongtiView.getText().toString();
        }else {
             zongti = zongtiView.getText().toString();
        }

        String yinhuan = yinhuanView.getText().toString();
        String zhenggai = zhenggaiView.getText().toString();
        String jiancharen = checkMenView.getText().toString();

        String quName="";
        String quCode="";
        String jiedaoName="";
        String jiedaoCode="";
        if (!quText.getText().toString().equals("请选择区")&& jiedaoText.equals("请选择街道")){      //只选择区 没选街道
            quName = quText.getText().toString();
            for (int i = 0; i < quList.size(); i++) {
                if (quList.get(i).getName().equals(quName)) {
                    quCode = quList.get(i).getGridNo();
                }
            }
        }else {     //选择到街道
            quName = quText.getText().toString();
            for (int i = 0; i < quList.size(); i++) {
                if (quList.get(i).getName().equals(quName)) {
                    quCode = quList.get(i).getGridNo();
                }
            }
            jiedaoName = jiedaoText.getText().toString();
            for (int i = 0; i < jiedaoList.size(); i++) {
                if (jiedaoList.get(i).getName().equals(jiedaoName)){
                    jiedaoCode = jiedaoList.get(i).getGridNo();
                }
            }
        }
        if (jiedaoName.equals("请选择街道")){
            jiedaoName = "";
        }

        CheckDto checkDto = new CheckDto(name, zongti, yinhuan, zhenggai, jiancharen, checkTime, quName, quCode, jiedaoName, jiedaoCode);
        for (int i = 0; i < fullList.size(); i++) {
            if (i == 0){
                checkDto.setPic1(fullList.get(i));
            }else  if (i == 1){
                checkDto.setPic2(fullList.get(i));
            }else  if (i == 2){
                checkDto.setPic3(fullList.get(i));
            }else  if (i == 3){
                checkDto.setPic4(fullList.get(i));
            }else  if (i == 4){
                checkDto.setPic5(fullList.get(i));
            }else  if (i == 5){
                checkDto.setPic6(fullList.get(i));
            }else  if (i == 6){
                checkDto.setPic7(fullList.get(i));
            }else  if (i == 7){
                checkDto.setPic8(fullList.get(i));
            }else  if (i == 8){
                checkDto.setPic9(fullList.get(i));
            }
        }
        List<CheckDetail> detailList = new ArrayList<>();

        try {
            List<CheckInfo> checkListFromDb = db.selector(CheckInfo.class).findAll();
            if (checkListFromDb!=null){
                for (int i = 0; i < checkListFromDb.size(); i++) {
                    //  List<CheckImages> fullImageList = checkListFromDb.get(i).getFullImageList();
                    List<CheckImages> fullImageList = db.selector(CheckImages.class).where("checkid", "=", checkListFromDb.get(i).getCheckId()).findAll();
                    String checkName = checkListFromDb.get(i).getCheckName();
                    String checkInfo = checkListFromDb.get(i).getCheckInfo();
                    CheckDetail checkDetail = new CheckDetail(checkName, checkInfo);
                    if (fullImageList!=null){
                        for (int j = 0; j < fullImageList.size(); j++) {
                            if (j==0){
                                checkDetail.setPic1(fullImageList.get(j).getFullStr());
                            }else if (j==1){
                                checkDetail.setPic2(fullImageList.get(j).getFullStr());
                            }else if (j==2){
                                checkDetail.setPic3(fullImageList.get(j).getFullStr());
                            }
                        }
                    }
                    detailList.add(checkDetail);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        CheckSaveData checkSaveData = new CheckSaveData(checkDto, detailList);
        Gson gson = new Gson();
        String json = gson.toJson(checkSaveData);
        Log.e(TAG, "postCheckDataToService: json=" + json );

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/generalCheck/saveDetails");
        params.setBodyContent(json);
        params.addHeader("Authorization","bearer " + token);
        Log.e(TAG, "postimage: " + params );
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "postData: " + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){
                        progressDialog.dismiss();
                        try {
                            db.delete(CheckCom.class);
                            db.delete(CheckComImage.class);
                            db.delete(CheckImages.class);
                            db.delete(CheckInfo.class);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }else {
                        Toast.makeText(ComprehensiveCheckHomeActivity.this, "上传失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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
     * 提交数据到服务器
     */
    private void postDataToService() {
        if (quText.getText().toString().equals("请选择区")){
            Toast.makeText(this, "请选择区域", Toast.LENGTH_SHORT).show();
            return;
        }
        if (quText.getText().toString().equals("")){
            Toast.makeText(this, "请选择区域", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resourceNameView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入检查点名称！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (zongtiView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入总体情况！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (yinhuanView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入隐患情况！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkMenView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入检查人！", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialogProgress(progressDialog,"正在上传");
        if (list.size()>0){
            new ImagePostThread().start();
        }else {
            postCheckDataToService();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveDataIntoDb();
    }

    /**
     * 将数据保存到本地
     */
    private void saveDataIntoDb() {
        try {
            db.delete(CheckComImage.class);
        } catch (DbException e) {
            e.printStackTrace();
        }


        if (currentCheckCom!=null){
            checkSaveTime = currentCheckCom.getId();
            checkNum = currentCheckCom.getCheckNum();
            yinhuanNum = currentCheckCom.getYinhuanNum();
        }else {
            Date date = new Date();
            String time = date.toLocaleString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

            checkSaveTime = dateFormat.format(date);
        }


        String quname = quText.getText().toString();
        String jiedaoName = jiedaoText.getText().toString();
        String name = resourceNameView.getText().toString();
        String zongti = zongtiView.getText().toString();
        String yinhuan = yinhuanView.getText().toString();
        String zhenggai = zhenggaiView.getText().toString();
        String checkmen = checkMenView.getText().toString();
       // CheckCom checkCom = new CheckCom(checkSaveTime, name, zongti, yinhuan, zhenggai, checkmen,quname,jiedaoName);
        CheckCom checkCom = new CheckCom(checkSaveTime, name, zongti, yinhuan, zhenggai, checkmen,checkNum,yinhuanNum,quText.getText().toString(),jiedaoText.getText().toString());
        List<CheckComImage> checkComImages = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
           // list.get(i).setuCheckId(checkSaveTime);
            Log.e(TAG, "saveDataIntoDb: uritoString ==" + list.get(i).getUri());
            Log.e(TAG, "saveDataIntoDb: uritoString ==" + list.get(i).getUri().toString());
            checkComImages.add(new CheckComImage(checkSaveTime,list.get(i).getUri().toString(),list.get(i).getAdd()));

        }

        try {
            Log.e(TAG, "saveDataIntoDb: list.size" + checkComImages.size() );
            db.saveOrUpdate(checkCom);
            db.saveBindingId(checkComImages);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onImageAddClickListener(boolean add, Uri uri, String id,ChooseImage chooseImage) {
        
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
                            Matisse.from(ComprehensiveCheckHomeActivity.this)
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
            ImagPagerUtil imagPagerUtil = new ImagPagerUtil(ComprehensiveCheckHomeActivity.this, picList);
            imagPagerUtil.setContentText(content);
            imagPagerUtil.show();
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

    @Override
    public void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
}
