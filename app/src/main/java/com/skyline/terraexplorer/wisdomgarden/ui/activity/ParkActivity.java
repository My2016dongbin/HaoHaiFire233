package com.skyline.terraexplorer.wisdomgarden.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.views.WheelView;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.base.HhBaseActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.wisdomgarden.ui.model.ParkType;
import com.skyline.terraexplorer.wisdomgarden.ui.multitype.Park;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.request.HhRequestParams;
import com.ruyiruyi.rylibrary.request.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
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

import rx.functions.Action1;
import rxhttp.wrapper.param.RxHttp;

import static com.ruyiruyi.rylibrary.image.ImageUtils.rotaingImageView;

public class ParkActivity extends HhBaseActivity implements DatePicker.OnDateChangedListener{

    private static final String TAG = ParkActivity.class.getSimpleName();
    private TextView chooseShipinView;
    private TextView postView;
    private String videoPath = "";
    private String parkId;
    private Park park;
    private ProgressDialog progressDialog;
    private TextView mingchengView;
    private TextView quanshuView;
    private EditText weizhiEdit;
    private EditText mianjiEdit;
    private EditText yanghudanweiEdit;
    private EditText yanghuneirongEdit;
    private EditText lianxirenEdit;
    private EditText lianxifangshiEdit;
    private EditText gongyuanleixingEdit;
    private TextView xiugaiButton;
    private ActionBar actionBar;
    public List<String> quList;
    public List<String> leixingList;
    public int quSelectIndex  = 0;
    public String currentChooseQu  ;
    private WheelView quWy;
    private TextView jianchengshijianView;
    private LinearLayout jianchengshijianLayout;
    public boolean isEdit = false;
    private StringBuffer date;
    private int year;
    private int month;
    private int day;
    private ImageView shijianImageView;
    private ImageView gongyuanImage;
    private ImageView bofangImage;
    private TextView shipinView;
    private boolean isChooseShipin = false;
    private boolean isChooseTupian = false;
    private ProgressDialog shipinDialog;
    private String videoSericePath;
    private TextView tupianChooseView;
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private String photoPath;
    private Uri tempUri;
    private Bitmap imgBitmap;
    private String img_Path = "";
    private Boolean isNewPic = false;
    private String imageServicePath;
    private TextView type1ChooseView;
    private TextView type2ChooseView;
    private TextView type3ChooseView;
    private LinearLayout type1Layout;
    private LinearLayout type2Layout;
    private LinearLayout type3Layout;
    public List<ParkType> parkTypeList;
    public List<String> type1List;
    public List<String> type2List;
    public List<String> type3List;
    private int currentChooseType = 1;    //1是1级选择  2是2级选择 3是三级选择
    private int isChooseType = 1;    //1是已选择第一级  2是已选择第二级 3是已选择第三级
    private int type1SelectIndex = 0;
    private int type2SelectIndex = 0;
    private int type3SelectIndex = 0;
    private String currentChooseType1 = "";
    private String currentChooseType2 = "";
    private String currentChooseType3 = "";
    private WheelView typeWy;
    private boolean isChooseType1 = false;
    private boolean isChooseType2 = false;
    private boolean isChooseType3 = false;
    private String currentType1Id;
    private String currentType2Id;
    private String currentType3Id;
    private LinearLayout typeChooseLayout;
    private TextView typeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);
        progressDialog = new ProgressDialog(this);
        date = new StringBuffer();
        quList = new ArrayList<>();
        leixingList = new ArrayList<>();
        parkTypeList = new ArrayList<>();
        type1List = new ArrayList<>();
        type2List = new ArrayList<>();
        type3List = new ArrayList<>();
        initDateTime();
        Intent intent = getIntent();
        parkId = intent.getStringExtra("PARK_ID");
        initQu();
        initView();

        setEditState();


        getDataFromService();

    }

    private void initQu() {
        quList.add("选择区域");
        quList.add("市南区");
        quList.add("市北区");
        quList.add("李沧区");
        quList.add("西海岸新区");
        quList.add("崂山区");
        quList.add("城阳区");
        quList.add("高新区");
        quList.add("即墨区");
        quList.add("胶州市");
        quList.add("平度市");
        quList.add("莱西市");

        leixingList.add("请选择类型");
        leixingList.add("公园绿地");
        leixingList.add("防护绿地");
        leixingList.add("广场用地");
        leixingList.add("附属绿地");
        leixingList.add("区域绿地");

    }

    private void getDataFromService() {

        showDialogProgress(progressDialog,"加载中...");


        HhRequestParams params = new HhRequestParams(RequestUtils.REQUEST_URL + "api/garden?id=" + parkId);
        params.setAsJsonContent(true);

        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());

        Log.e(TAG, "park: --"  + params);
        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONObject parkObj = jsonObject1.getJSONArray("data").getJSONObject(0);
                        Gson gson = new Gson();
                        park = gson.fromJson(String.valueOf(parkObj), Park.class);
                        getGongyuanTypeDataFromService();

                    }else {
                        Toast.makeText(ParkActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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
     * 获取公园类型数据
     */
    private void getGongyuanTypeDataFromService() {
        JSONObject jsonObject = new JSONObject();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/greenType/list");

        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());

        params.setBodyContent(jsonObject.toString());
        Log.e(TAG, "park: --"  + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        Gson gson = new Gson();
                        parkTypeList = gson.fromJson(String.valueOf(data), new TypeToken<List<ParkType>>(){}.getType());
                        initData();
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



    private void initData() {
        //设置图片
        if (park.getImageFile()==null) {
            Log.e(TAG, "initData: 1");
            Glide.with(getApplicationContext()).load(R.drawable.ic_park).into(gongyuanImage);
        }else {
            Log.e(TAG, "initData: 1");
            Glide.with(this).load(park.getImageFile()).into(gongyuanImage);
        }

        //设置视频
        if (park.getVideoFile()==null){
            bofangImage.setVisibility(View.GONE);
            shipinView.setVisibility(View.VISIBLE);
            shipinView.setText("暂无视频");
        }else {
            bofangImage.setVisibility(View.VISIBLE);
            shipinView.setVisibility(View.GONE);
        }

        //设置权属
        if (park.getOwnership()==null){
            quSelectIndex = 0;
        }else {
            for (int i = 0; i < quList.size(); i++) {
                if (quList.get(i).equals(park.getOwnership())) {
                    quSelectIndex = i;
                }
            }
        }

        mingchengView.setText(park.getName() == null ? "  ": park.getName());
        quanshuView.setText(park.getOwnership() == null ? "选择区域" : park.getOwnership());
        weizhiEdit.setText(park.getAddress()== null ? "  " : park.getAddress());
        mianjiEdit.setText(park.getArea()== null ? "  " : park.getArea());

        yanghudanweiEdit.setText(park.getMaintenanceUnit()== null ? "  " : park.getMaintenanceUnit());
        yanghuneirongEdit.setText(park.getMaintenanceContent()== null ? "  " : park.getMaintenanceContent());
        lianxirenEdit.setText(park.getPeople()== null ? " " : park.getPeople());
        lianxifangshiEdit.setText(park.getPhone()== null ? "  " : park.getPhone());
        typeView.setText(park.getType()== null ? "  " : park.getType());
        if (park.getFinishTime()== null) {
            jianchengshijianView.setText("  ");
        }else {
            jianchengshijianView.setText(park.getFinishTime().substring(0,park.getFinishTime().indexOf("T")));
        }

    //    Glide.with(getApplicationContext()).load(R.drawable.ic_park).into(gongyuanImage);


    }


    private void setEditState() {

        if (isEdit){
            typeChooseLayout.setVisibility(View.VISIBLE);
            typeView.setVisibility(View.GONE);
            xiugaiButton.setVisibility(View.VISIBLE);
            shijianImageView.setVisibility(View.VISIBLE);
            weizhiEdit.setFocusable(true);
            weizhiEdit.setFocusableInTouchMode(true);
            weizhiEdit.requestFocus();
            mianjiEdit.setFocusable(true);
            mianjiEdit.setFocusableInTouchMode(true);
            yanghudanweiEdit.setFocusable(true);
            yanghudanweiEdit.setFocusableInTouchMode(true);
            yanghuneirongEdit.setFocusable(true);
            yanghuneirongEdit.setFocusableInTouchMode(true);
            lianxirenEdit.setFocusable(true);
            lianxirenEdit.setFocusableInTouchMode(true);
            lianxifangshiEdit.setFocusable(true);
            lianxifangshiEdit.setFocusableInTouchMode(true);
            shipinView.setText("视频选择");
        }else {
            typeChooseLayout.setVisibility(View.GONE);
            typeView.setVisibility(View.VISIBLE);
            xiugaiButton.setVisibility(View.GONE);
            shijianImageView.setVisibility(View.GONE);
            weizhiEdit.setFocusable(false);
            weizhiEdit.setFocusableInTouchMode(false);
            mianjiEdit.setFocusable(false);
            mianjiEdit.setFocusableInTouchMode(false);
            yanghudanweiEdit.setFocusable(false);
            yanghudanweiEdit.setFocusableInTouchMode(false);
            yanghuneirongEdit.setFocusable(false);
            yanghuneirongEdit.setFocusableInTouchMode(false);
            lianxirenEdit.setFocusable(false);
            lianxirenEdit.setFocusableInTouchMode(false);
            lianxifangshiEdit.setFocusable(false);
            lianxifangshiEdit.setFocusableInTouchMode(false);
        }

    }


    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("公园详情");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        if (isEdit){
                            actionBar.setRightView("修改");
                            isEdit = false;
                        }else {
                            actionBar.setRightView("取消");
                            isEdit = true;
                        }
                        if (isEdit){
                            shipinView.setVisibility(View.VISIBLE);
                            shipinView.setText("视频选择");
                            tupianChooseView.setVisibility(View.VISIBLE);
                        }else {
                            //设置视频
                            if (park.getVideoFile()==null){
                                bofangImage.setVisibility(View.GONE);
                                shipinView.setVisibility(View.VISIBLE);
                                shipinView.setText("暂无视频");
                            }else {
                                bofangImage.setVisibility(View.VISIBLE);
                                shipinView.setVisibility(View.GONE);
                            }
                            tupianChooseView.setVisibility(View.GONE);
                        }

                        setEditState();
                        break;
                }
            }
        });
        actionBar.setRightView("修改");
        
        //公园类型
        type1ChooseView = (TextView) findViewById(R.id.type1_choose_view);
        type2ChooseView = (TextView) findViewById(R.id.type2_choose_view);
        type3ChooseView = (TextView) findViewById(R.id.type3_choose_view);
        type1Layout = (LinearLayout) findViewById(R.id.type1_layout);
        type2Layout = (LinearLayout) findViewById(R.id.type2_layout);
        type3Layout = (LinearLayout) findViewById(R.id.type3_layout);
        typeChooseLayout = (LinearLayout) findViewById(R.id.type_choose_layout);
        typeView = (TextView) findViewById(R.id.type_view);

        tupianChooseView = (TextView) findViewById(R.id.tupian_choose_view);
        gongyuanImage = (ImageView) findViewById(R.id.gongyuan_image);

        bofangImage = (ImageView) findViewById(R.id.bofang_image);
        shipinView = (TextView) findViewById(R.id.shipin_view);

        chooseShipinView = (TextView) findViewById(R.id.choose_shipin_view);
        jianchengshijianView = (TextView) findViewById(R.id.jianchengshijian_view);
        jianchengshijianLayout = (LinearLayout) findViewById(R.id.jianchengshijian_layout);
        shijianImageView = (ImageView) findViewById(R.id.shijian_image);
        postView = (TextView) findViewById(R.id.post_view);
        mingchengView = (TextView) findViewById(R.id.mingcheng_view);
        quanshuView = (TextView) findViewById(R.id.quanshu_view);
        weizhiEdit = (EditText) findViewById(R.id.weizhi_edit);
        mianjiEdit = (EditText) findViewById(R.id.mianji_edit);
        yanghudanweiEdit = (EditText) findViewById(R.id.yanghudanwei_edit);
        yanghuneirongEdit = (EditText) findViewById(R.id.yanghuneirong_edit);
        lianxirenEdit = (EditText) findViewById(R.id.lianxiren_edit);
        lianxifangshiEdit = (EditText) findViewById(R.id.lianxifangshi_edit);
        xiugaiButton = (TextView) findViewById(R.id.xiugai_button);

        //进度条
        final int MAX_PROGRESS = 100;

        shipinDialog = new ProgressDialog(ParkActivity.this);
        shipinDialog.setProgress(0);
        shipinDialog.setTitle("数据上传中");
        shipinDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        shipinDialog.setMax(MAX_PROGRESS);

        RxViewAction.clickNoDouble(type1Layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseType = 1;
                        getType1();
                        showAreaDialog(type1List);
                    }
                });
        RxViewAction.clickNoDouble(type2Layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseType = 2;
                        for (int i = 0; i < parkTypeList.size(); i++) {
                            if (parkTypeList.get(i).getTypeName().equals(currentChooseType1)) {
                                currentType1Id = parkTypeList.get(i).getId();
                            }
                        }

                        if (type1ChooseView.getText().equals("请选择类型")){
                            Toast.makeText(ParkActivity.this, "请先选择一级类型", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        getType2();
                        showAreaDialog(type2List);
                    }
                });
        RxViewAction.clickNoDouble(type3Layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseType = 3;
                        for (int i = 0; i < parkTypeList.size(); i++) {
                            if (parkTypeList.get(i).getTypeName().equals(currentChooseType2)) {
                                currentType2Id = parkTypeList.get(i).getId();
                            }
                        }

                        if (type2ChooseView.getText().equals("请选择类型")){
                            Toast.makeText(ParkActivity.this, "请先选择二级类型", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        getType3();
                        showAreaDialog(type3List);
                    }
                });

        /**
         * 图片选择
         */
        RxViewAction.clickNoDouble(tupianChooseView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showPicInputDialog();
                    }
                });


        xiugaiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipinDialog.show();
                if (isChooseShipin || isChooseTupian) {
                    postVideoToServiceRx();
                }else {
                    postDataToService();
                }
            }
        });


        RxViewAction.clickNoDouble(bofangImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: " +  park.getVideoFile());
                       Intent intent = new Intent(getApplicationContext(),PlayerActivity.class);
                        intent.putExtra("PLAYER_URL", park.getVideoFile());
                     //   intent.putExtra("PLAYER_URL", "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
                        //intent.putExtra("PLAYER_URL", "rtmp://mobliestream.c3tv.com:554/live/goodtv.sdp");
                        startActivity(intent);
                    }
                });



        RxViewAction.clickNoDouble(shipinView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (shipinView.getText().equals("视频选择")|| shipinView.getText().equals("重新选择视频")){
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 66);
                        }
                    }
                });

        RxViewAction.clickNoDouble(jianchengshijianLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isEdit){
                            showDataDialog();
                        }
                    }
                });

        RxViewAction.clickNoDouble(quanshuView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isEdit){
                            showQuDialog();
                        }

                    }
                });


        RxViewAction.clickNoDouble(chooseShipinView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 66);
                    }
                });
        RxViewAction.clickNoDouble(postView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: " + new File(videoPath).length());

                        //postVideoToService();
                        postVideoToServiceRx();
                    }
                });


    }

    private void getType3() {
        type3List.clear();;
        type3List.add("请选择类型");
        for (int i = 0; i < parkTypeList.size(); i++) {
            if (parkTypeList.get(i).getParentId().equals(currentType2Id)) {
                type3List.add(parkTypeList.get(i).getTypeName());
            }
        }
    }

    private void getType2() {
        type2List.clear();;
        type2List.add("请选择类型");
        for (int i = 0; i < parkTypeList.size(); i++) {
            if (parkTypeList.get(i).getParentId().equals(currentType1Id)) {
                type2List.add(parkTypeList.get(i).getTypeName());
            }
        }
    }

    private void getType1() {
        type1List.clear();;
        type1List.add("请选择类型");
        for (int i = 0; i < parkTypeList.size(); i++) {
            if (parkTypeList.get(i).getParentId().equals("ROOT")) {
                type1List.add(parkTypeList.get(i).getTypeName());
            }
        }
    }

    /**
     * 公园类型选择
     * @param type1List
     */
    private void showAreaDialog(List<String> type1List) {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_type, null);
        typeWy = ((WheelView) areaView.findViewById(R.id.wheel_view_type));
        typeWy.setIsLoop(false);
        if (currentChooseType == 1){
            typeWy.setItems(type1List, type1SelectIndex);//init selected position is 0 初始选中位置为0
        }else if (currentChooseType == 2) {
            typeWy.setItems(type1List, type2SelectIndex);//init selected position is 0 初始选中位置为0
        }else  {
            typeWy.setItems(type1List, type3SelectIndex);//init selected position is 0 初始选中位置为0
        }

        typeWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(int selectedIndex, String item) {
                if (currentChooseType == 1) {   //选择以及类型
                    currentChooseType1 = typeWy.getSelectedItem();
                    if (currentChooseType1.equals("请选择类型")){
                        isChooseType = 0;
                    }else {
                        isChooseType = 1;
                    }
                    type1SelectIndex = typeWy.getSelectedPosition();
                    type1ChooseView.setText(currentChooseType1);
                    currentChooseType2 = "";
                    currentChooseType3="";
                    type2ChooseView.setText("请选择类型");
                    type3ChooseView.setText("请选择类型");
                    type2SelectIndex = 0;
                    type3SelectIndex = 0;
                }else if (currentChooseType == 2){  //选择二级类型

                    currentChooseType2 = typeWy.getSelectedItem();
                    if (currentChooseType2.equals("请选择类型")){
                        isChooseType = 1;
                    }else {
                        isChooseType = 2;
                    }
                    type2SelectIndex = typeWy.getSelectedPosition();
                    type2ChooseView.setText(currentChooseType2);
                    currentChooseType3="";
                    type3SelectIndex = 0;
                    type3ChooseView.setText("请选择类型");
                }else {                          //选择三级类型
                    currentChooseType3 = typeWy.getSelectedItem();
                    if (currentChooseType3.equals("请选择类型")){
                        isChooseType = 2;
                    }else {
                        isChooseType = 3;
                    }
                    type3SelectIndex = typeWy.getSelectedPosition();
                    type3ChooseView.setText(currentChooseType3);
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
                        String area = typeWy.getSelectedItem();
                    }
                })
                .show();
    }



    /**
     * 图片选择
     */
    private void showPicInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传照片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE://选择本地照片
                        Intent openBendiPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openBendiPicIntent.setType("image/*");
                        startActivityForResult(openBendiPicIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE://拍照
                        takePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(ParkActivity.this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        file = new File(this.getObbDir().getAbsolutePath(), "gongyuan.jpg");
        photoPath = file.getPath();
        Log.e(TAG, "takePicture: path_ = " + photoPath);

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(ParkActivity.this, "com.haohai.garden.wisdomgarden", file);
        } else {
            tempUri = Uri.fromFile(new File(this.getObbDir().getAbsolutePath(), "gongyuan.jpg"));
        }
        Log.e(TAG, "takePicture: " +tempUri );
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    private void showDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
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

                jianchengshijianView.setText(date);


                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog  dialog = builder.create();
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

    private void showQuDialog() {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_qu, null);
        quWy = ((WheelView) areaView.findViewById(R.id.wheel_view_qu));
        quWy.setIsLoop(false);

        quWy.setItems(quList, quSelectIndex);//init selected position is 0 初始选中位置为0


        quWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {

                currentChooseQu = quWy.getSelectedItem();
                quSelectIndex = quWy.getSelectedPosition();
                quanshuView.setText(currentChooseQu);

            }
        });
        new AlertDialog.Builder(this)
                .setTitle("请选择区域")
                .setView(areaView)
                .setPositiveButton("确定 ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String areStr = "";
                        String area = quWy.getSelectedItem();


                    }
                })
                .show();
    }

    private void postVideoToServiceRx() {


        RxHttp.postForm("http://api.ehaohai.com:10100/oa/api/workReport/fileUploadAnByNotToken")
               // .addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken())
                .add("file",new File(videoPath))
                .add("file",new File(img_Path))
                .asUpload(progress -> {
                    //上传进度回调,0-100，仅在进度有更新时才会回调,最多回调101次，最后一次回调Http执行结果
                    int currentProgress = progress.getProgress(); //当前进度 0-100
                    long currentSize = progress.getCurrentSize(); //当前已上传的字节大小
                    long totalSize = progress.getTotalSize();     //要上传的总字节大小
                    if (currentProgress < 100){
                        shipinDialog.setProgress(currentProgress);
                    }


                    Log.e(TAG, "postVideoToServiceRx:当前进度= " +currentProgress );
                    Log.e(TAG, "postVideoToServiceRx:当前已上传的字节大小= " +currentSize );
                    Log.e(TAG, "postVideoToServiceRx:要上传的总字节大小= " +totalSize );
                })
                .subscribe(s -> {
                    Log.e(TAG, "postVideoToServiceRx: 上传成功" +s);
                    JSONObject jsonObject = new JSONObject(s);
                    if (!isChooseShipin || isChooseTupian){
                        imageServicePath = jsonObject.getJSONObject("data").getJSONArray("img").getString(0);
                    }else if (isChooseShipin || !isChooseTupian){
                        videoSericePath = jsonObject.getJSONObject("data").getJSONArray("img").getString(0);
                    }else {
                        videoSericePath = jsonObject.getJSONObject("data").getJSONArray("img").getString(0);
                        imageServicePath = jsonObject.getJSONObject("data").getJSONArray("img").getString(1);
                    }

                    postDataToService();
                },throwable -> {
                    Log.e(TAG, "postVideoToServiceRx: 上传失败"+throwable.toString());
                    shipinDialog.dismiss();
                });
    }

    private void postDataToService() {
        if (mianjiEdit.getText().toString().equals("")){
            Toast.makeText(this, "请填写公园面积", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id",park.getId());
            jsonObject.put("name",mingchengView.getText().toString());
            jsonObject.put("ownership",currentChooseQu);
            jsonObject.put("address",weizhiEdit.getText().toString());
            jsonObject.put("area",mianjiEdit.getText().toString());
            jsonObject.put("finishTime",jianchengshijianView.getText().toString());
            jsonObject.put("maintenanceUnit",yanghudanweiEdit.getText().toString());
            jsonObject.put("maintenanceContent",yanghuneirongEdit.getText().toString());
            jsonObject.put("people",lianxirenEdit.getText().toString());
            jsonObject.put("phone",lianxifangshiEdit.getText().toString());
            if (isChooseType == 1){
                jsonObject.put("type",currentChooseType1);
            }else if (isChooseType == 2){
                jsonObject.put("type",currentChooseType2);
            }else if (isChooseType == 3){
                jsonObject.put("type",currentChooseType3);
            }else {
                jsonObject.put("type",park.getType());
            }

            if (isChooseShipin){
                jsonObject.put("videoFile",videoSericePath);
            }

            if (isChooseTupian){
                jsonObject.put("imageFile",imageServicePath);
            }
           // jsonObject.put("imageFile","");



        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/garden");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.addParameter("ccs",1);
       // params.setCharset("GBK");
        params.addParameter("conditions",2);
        Log.e(TAG, "postData:-- params--" + params);
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        Toast.makeText(ParkActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        //setResult( RequestCode.LIST_CHANGE);
                        finish();
                    }else {
                    //    Toast.makeText(LeaveFlowAddActivity.this, "流程申请失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
                Toast.makeText(ParkActivity.this, "数据提交失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                shipinDialog.setProgress(100);
                shipinDialog.dismiss();
            }
        });
    }



    private void videoYasuo() {
/*        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    *//**
                     * 视频压缩
                     * 第一个参数:视频源文件路径
                     * 第二个参数:压缩后视频保存的路径
                     *//*
                    String path= SiliCompressor.with(getApplicationContext()).compressVideo(videoPath , Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
                    String comPressPath = SiliCompressor.with(getApplicationContext()).compressVideo(videoPath, path);
                    Log.e(TAG, "run: " + comPressPath);
//                    if (!StringUtil.isEmpty(comPressPath)) {
//                        notCompressedVideo.setCompressPath(comPressPath);
//                        compressVideo();
//                    } else {
//                        stopCompress("失败");
//                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ) {
            Log.e(TAG, "onActivityResult:data= " + data);
            Log.e(TAG, "onActivityResult:requestCode= " + requestCode);
            //submit_vd_ad.setText(videoPath);
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    isChooseTupian = true;
                    Uri uri = data.getData();
                    setImageToViewFromPhone(uri, false);
                    break;
                case TAKE_PICTURE:
                    isChooseTupian = true;

                    setImageToViewFromPhone(tempUri, true);

                    break;
                case 66:
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
                    break;

            }
        }

    }

    private void setImageToViewFromPhone(Uri uri, boolean isCamera) {
        int degree = 0;
        if (isCamera) {
            degree = ImageUtils.readPictureDegree(photoPath);
        } else {
            degree = ImageUtils.getOrientation(getApplicationContext(), uri);
            Log.e(TAG, "setImageToViewFromPhone:  degree = " + degree);
        }
        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(ParkActivity.this, uri);
            } catch (IOException e) {
            }

            imgBitmap = rotaingImageView(degree, photo);

//   2          mGoodsImg.setImageBitmap(imgBitmap);
            //Glide 加载BitMap需要先将bitmap对象转换为字节,在加载;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            Glide.with(this).load(bytes)
                    .into(gongyuanImage);

            //此时记录头像已更改 并生成文件地址
            isNewPic = true;
            img_Path = ImageUtils.savePhoto(imgBitmap, this.getObbDir().getAbsolutePath(), "gongyuan");


        }
    }


    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }
}
