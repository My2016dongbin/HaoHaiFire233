package com.skyline.terraexplorer.mainapps.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.multitype.Checkfhjd;
import com.skyline.terraexplorer.mainapps.multitype.CheckfhjdViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.CheckslyeditViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Checksyledit;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.ImageUtils;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static com.skyline.terraexplorer.mainapps.utils.ImageUtils.rotaingImageView;
import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class AddjsxmActivity extends BaseActivity implements CheckfhjdViewBinder.OnCheckFieldItemClick,DatePicker.OnDateChangedListener,CheckslyeditViewBinder.OnChecksylItemClick{
    private static final String TAG = AddjsxmActivity.class.getSimpleName();;
    private ActionBar actionBar;
    private List<Checkfhjd> ChecksylList;
    private List<Checksyledit> ChecksyleditList;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private RecyclerView listView;
    private CheckfhjdViewBinder checkFhjdProvider;
    private CheckslyeditViewBinder checkslyeditViewBinder;
    private String code;
    private EditText sylddwEdit;
    private EditText jianchashijianView;
    private EditText jingduEdit;
    private EditText weiduEdit;
    private EditText bfhyqEdit;
    private EditText fzrEdit;
    private EditText lxdhEdit;
    private EditText dizhiEdit;
    private EditText quxianEdit;
    private TextView tijiaoButton;
    private String access_token;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private int choose1 = 0;
    private StringBuffer date;
    private int year;
    private int month;
    private int day;
    private fhjdReceiver Receiver;
    private int nonum=0;
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private String photoPath;
    private Uri tempUri;
    private Bitmap imgBitmap;
    private String img_Path = "";
    private boolean isChooseShipin = false;
    private boolean isChooseTupian = false;
    private String videoPath = "";
    private TextView shipinView;
    public List<Uri> uriChooseList;
    private ImageView oneImageDelete;
    private ImageView twoImageDelete;
    private LinearLayout photoLayout;
    private ImageView oneImage;
    private ImageView twoImage;
    private FrameLayout twoImageLayout;
    private Bitmap evaluateOne;
    private Bitmap evaluateTwo;
    private List<Object> imglist;
    private EditText xgqxEdit;
    private EditText bdcrEdit;
    private EditText jcrEdit;
    private String videostr="";
    private static final int REQUEST_CODE_CHOOSE = 23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsxm_add);
        access_token = new DbConfig(this).getUser().getToken();
        date = new StringBuffer();
        uriChooseList = new ArrayList<>();
        imglist=new ArrayList<>();
        initView();
        initDateTime();
    }

    private void initData() {
        ChecksylList.add(new Checkfhjd("i0","是否依法办理建设项目使用林地行政许可手续","licenseIsTransact",1));ChecksylList.add(new Checkfhjd("i1","是否按照建设项目使用林地核准同意书批准的时间使用林地","treesIsAccordingToTime",1));
        ChecksylList.add(new Checkfhjd("i2","是否按照建设项目使用林地核准同意书批准的小班使用林地","treesIsAccordingToSmallClass",1));ChecksylList.add(new Checkfhjd("i3","是否按照建设项目使用林地核准同意书批准的面积使用林地","treesIsAccordingToArea",1));
        ChecksylList.add(new Checkfhjd("i4","是否恢复林业产业并通过区市林业主管部门验收","tempIsRecoveryOfForestryIndustry",1));ChecksylList.add(new Checkfhjd("i5","是否恢复森林植被","tempIsRestoreTheVegetation",1));
        ChecksylList.add(new Checkfhjd("i6","是否移交原经营者","tempIsHandoverToOriginalOperator",1));ChecksylList.add(new Checkfhjd("i7","是否依法审批","dutyIsLawApproval",1));
        ChecksylList.add(new Checkfhjd("i8","是否开展监督检查","dutyIsSupervisionAndInspection",1));ChecksylList.add(new Checkfhjd("i9","案卷材料是否齐全","dutyIsCompleteMaterial",1));
        ChecksylList.add(new Checkfhjd("i10","是否建立台账","dutyIsEstablishList",1));
        ChecksyleditList.add(new Checksyledit("i0","是否依法办理建设项目使用林地行政许可手续","licenseIsTransactRemark",""));ChecksyleditList.add(new Checksyledit("i1","是否按照建设项目使用林地核准同意书批准的时间使用林地","treesIsAccordingToTimeRemark",""));
        ChecksyleditList.add(new Checksyledit("i2","是否按照建设项目使用林地核准同意书批准的小班使用林地","treesIsAccordingToSmallClassRemark",""));ChecksyleditList.add(new Checksyledit("i3","是否按照建设项目使用林地核准同意书批准的面积使用林地","treesIsAccordingToAreaRemark",""));
        ChecksyleditList.add(new Checksyledit("i4","是否恢复林业产业并通过区市林业主管部门验收","tempIsRecoveryOfForestryIndustryRemark",""));ChecksyleditList.add(new Checksyledit("i5","是否恢复森林植被","tempIsRestoreTheVegetation",""));
        ChecksyleditList.add(new Checksyledit("i6","是否移交原经营者","tempIsHandoverToOriginalOperator",""));ChecksyleditList.add(new Checksyledit("i7","是否依法审批","dutyIsLawApprovalRemark",""));
        ChecksyleditList.add(new Checksyledit("i8","是否开展监督检查","dutyIsSupervisionAndInspection",""));ChecksyleditList.add(new Checksyledit("i9","案卷材料是否齐全","dutyIsCompleteMaterial",""));
        ChecksyleditList.add(new Checksyledit("i10","是否建立台账","dutyIsEstablishListRemark",""));
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("添加食用林产品");
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
        ChecksylList =new ArrayList<>();
        ChecksyleditList=new ArrayList<>();
        initData();
        listView = findViewById(R.id.list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        if (ChecksylList.size() > 0){
            initCheckFieldData();}
        tijiaoButton = findViewById(R.id.xiugai_button);
        quxianEdit = findViewById(R.id.quxian_edit);
        sylddwEdit = findViewById(R.id.sylddw_edit);
        jianchashijianView= findViewById(R.id.jianchashijian_view);
        jingduEdit= findViewById(R.id.jingdu_edit);
        weiduEdit=findViewById(R.id.weidu_edit);
        bfhyqEdit=findViewById(R.id.bfhyq_edit);
        bfhyqEdit.setText("0");
        fzrEdit=findViewById(R.id.fcr_edit);
        lxdhEdit=findViewById(R.id.lxdh_edit);
        dizhiEdit=findViewById(R.id.dizhi_edit);
        oneImageDelete = (ImageView) findViewById(R.id.one_image_delete);
        twoImageDelete = (ImageView) findViewById(R.id.two_image_delete);
        photoLayout = (LinearLayout) findViewById(R.id.photo_layout);
        oneImage = (ImageView) findViewById(R.id.one_image);
        twoImage = (ImageView) findViewById(R.id.two_image);
        twoImageLayout = (FrameLayout) findViewById(R.id.two_imag_layout);
        shipinView = findViewById(R.id.shipin_view);
        xgqxEdit=findViewById(R.id.xgqx_edit);
        bdcrEdit=findViewById(R.id.bdcr_edit);
        jcrEdit=findViewById(R.id.jcr_edit);
        progressDialog = new ProgressDialog(this);
        Receiver = new fhjdReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("GPSinfo");
        registerReceiver(Receiver, filter);

        RxViewAction.clickNoDouble(tijiaoButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDialogProgress(progressDialog,"正在上传");
                        if (!img_Path.equals("")) {
                            postPictoService();
                        }else {
                            if (isChooseShipin){
                                postVideoToServiceRx();
                            }else {
                                postDataToService();
                            }
                        }
                    }
                });
        RxViewAction.clickNoDouble(quxianEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showLeibieChangeDailog(quxianEdit);
                    }
                });
        RxViewAction.clickNoDouble(jianchashijianView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                            showDataDialog(jianchashijianView);
                    }
                });
        RxViewAction.clickNoDouble(xgqxEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog(xgqxEdit);
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
        RxViewAction.clickNoDouble(oneImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (uriChooseList.size() == 2){
                            Toast.makeText(AddjsxmActivity.this, "最多可以添加两张图片", Toast.LENGTH_SHORT).show();
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
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 66);
                        }
                    }
                });
    }

    private void register() {
        checkFhjdProvider = new CheckfhjdViewBinder();
        checkFhjdProvider.setListener(this);
        checkslyeditViewBinder=new CheckslyeditViewBinder();
        checkslyeditViewBinder.setListener(this);
        adapter.register(Checkfhjd.class, checkFhjdProvider);
        adapter.register(Checksyledit.class,checkslyeditViewBinder);
    }

    private void initCheckFieldData() {
        items.clear();
        for (int i = 0; i < ChecksylList.size(); i++) {
            items.add(ChecksylList.get(i));
            items.add(ChecksyleditList.get(i));
        }
        Log.e(TAG, "initCheckFieldData: "+items.size() );
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckFieldItemClickLinstener(String id, int state) {
        nonum=0;
        for (int i = 0; i < ChecksylList.size(); i++) {
            if (ChecksylList.get(i).getId().equals(id)) {
                ChecksylList.get(i).state = state;
                Log.e(TAG, "onCheckFieldItemClickLinstener: "+ChecksylList.get(i).state );
            }
            if (ChecksylList.get(i).state==0){
                nonum++;
            }
            Log.e(TAG, "num: "+nonum );
        }
        bfhyqEdit.setText(String.valueOf(nonum));
    }
    @Override
    public void onChecksylItemClickLinstener(String id, String editmeg) {
        Log.e(TAG, "onChecksylItemClickLinstener: "+editmeg );
        for (int i = 0; i < ChecksylList.size(); i++) {
            if (ChecksyleditList.get(i).getId().equals(id)) {
                ChecksyleditList.get(i).editmeg = editmeg;
            }
        }
    }
    private void postDataToService() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address",dizhiEdit.getText().toString());
            jsonObject.put("region",quxianEdit.getText().toString());
            jsonObject.put("forestUnits",sylddwEdit.getText().toString());
            jsonObject.put("inspectionTime",jianchashijianView.getText().toString()+" 00:00:00");
            jsonObject.put("lng", NumberFormat.getInstance().parse(jingduEdit.getText().toString()));
            jsonObject.put("lat",NumberFormat.getInstance().parse(weiduEdit.getText().toString()));
            jsonObject.put("unqualifiedCount",Integer.parseInt(bfhyqEdit.getText().toString()));
            jsonObject.put("head",fzrEdit.getText().toString());
            jsonObject.put("phone",lxdhEdit.getText().toString());
            jsonObject.put("signatureForestUnits",bdcrEdit.getText().toString());
            jsonObject.put("signatureMonitor",jcrEdit.getText().toString());
            jsonObject.put("modifyTheTimeLimit",xgqxEdit.getText().toString()+" 00:00:00");
            for (int i = 0; i < ChecksylList.size(); i++) {
                jsonObject.put(ChecksylList.get(i).getCode(), ChecksylList.get(i).state);
            }
            for (int j = 0; j < ChecksyleditList.size(); j++) {
                jsonObject.put(ChecksyleditList.get(j).getCode(), ChecksyleditList.get(j).editmeg);
            }
            if (imglist.size()>0){
                jsonObject.put("image",imglist.get(0).toString());
                if (imglist.size()>1){
                    jsonObject.put("image",imglist.get(1).toString());
                }
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/constructionProjectUsesWoodland" );
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + access_token);
        params.setConnectTimeout(10000);
        Log.e(TAG, "postDataToServiceFromDb---" + params);
        Log.e(TAG, "postDataToServiceFromDb---" + jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --数据上传成功--" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(AddjsxmActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddjsxmActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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
    private void showLeibieChangeDailog(EditText editText) {
        //默认选中第一个
        final String[] items = {"青岛市", "市南区","市北区","崂山区","李沧区","城阳区","即墨区","胶州市","西海岸","平度市","莱西市"};

        builder = new AlertDialog.Builder(this).setTitle("选择区市")
                .setSingleChoiceItems(items, choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        choose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (choose1 == 0) {
                            editText.setText("青岛市");
                        } else if (choose1 == 1){
                            editText.setText("市南区");
                        }else if (choose1 == 2){
                            editText.setText("市北区");
                        }else if (choose1 == 3){
                            editText.setText("崂山区");
                        }else if (choose1 == 4){
                            editText.setText("李沧区");
                        }else if (choose1 == 5){
                            editText.setText("城阳区");
                        }else if (choose1 == 6){
                            editText.setText("即墨区");
                        }else if (choose1 == 7){
                            editText.setText("胶州市");
                        }else if (choose1 == 8){
                            editText.setText("西海岸");
                        }else if (choose1 == 9){
                            editText.setText("平度市");
                        }else if (choose1 == 10){
                            editText.setText("莱西市");
                        }

                    }
                });
        builder.create().show();
    }
    private void showDataDialog(EditText editText) {
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

                editText.setText(date);


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



    class fhjdReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {

            String msg = intent.getStringExtra("message");
            //Toast.makeText(context, "广播已经接收", Toast.LENGTH_SHORT).show();
            Log.e("fhjdReceive: ", msg);
            try {
                JSONObject result = new JSONObject(msg);
                JSONObject position = (JSONObject) result.get("position");
                jingduEdit.setText(position.get("lng").toString());
                weiduEdit.setText(position.get("lat").toString());
                dizhiEdit.setText(position.get("address").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult:resultCode " + resultCode + "requestcode" + requestCode);
//        Log.e(TAG, "onActivityResult:data ",data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> uriList = Matisse.obtainResult(data);
            for (int i = 0; i < uriList.size(); i++) {
                uriChooseList.add(uriList.get(i));
            }

            photoLayout.setVisibility(View.VISIBLE);
            if (uriChooseList.size() > 1) {  //有两张图
                twoImageLayout.setVisibility(View.VISIBLE);
                twoImageDelete.setVisibility(View.VISIBLE);
                oneImageDelete.setVisibility(View.VISIBLE);
                Glide.with(this).load(uriChooseList.get(0)).into(oneImage);
                Glide.with(this).load(uriChooseList.get(1)).into(twoImage);
            } else {                     //有一张图
                twoImageLayout.setVisibility(View.VISIBLE);
                twoImageDelete.setVisibility(View.GONE);
                oneImageDelete.setVisibility(View.VISIBLE);
                Glide.with(this).load(uriChooseList.get(0)).into(oneImage);
                //   Glide.with(this).load(R.drawable.ic_bigphoto).into(twoImage);
                twoImageLayout.setVisibility(View.GONE);
            }

        } else if (requestCode == 66 && resultCode == RESULT_OK && null != data) {
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
        } else if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            int degree = ImageUtils.getOrientation(getApplicationContext(), uri);
            uriChooseList.add(uri);
            twoImageLayout.setVisibility(View.VISIBLE);
            twoImageDelete.setVisibility(View.GONE);
            oneImageDelete.setVisibility(View.VISIBLE);
            Glide.with(this).load(uriChooseList.get(0)).into(oneImage);
            //   Glide.with(this).load(R.drawable.ic_bigphoto).into(twoImage);
            twoImageLayout.setVisibility(View.GONE);
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(AddjsxmActivity.this, uri);
            } catch (IOException e) {
            }
            imgBitmap = rotaingImageView(degree, photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            img_Path = ImageUtils.savePhoto(imgBitmap, this.getObbDir().getAbsolutePath(), "fhjdpic");
            Log.e(TAG, "fhjdpic: " + img_Path);
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

    }
    private void postPictoService(){

        RequestParams params = new RequestParams(RequestUtils.SAVE_IMAGE);
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        String picStr = null;
        for (int i = 0; i < uriChooseList.size(); i++) {
            try {

                Uri uri = uriChooseList.get(i);
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                if (i == 0){
                    evaluateOne = rotaingImageView(degree, photo);
                    picStr = ImageUtils.savePhoto(evaluateOne, this.getObbDir().getAbsolutePath(), "fileName" + i);
                }else if (i == 1){
                    evaluateTwo = rotaingImageView(degree, photo);
                    picStr = ImageUtils.savePhoto(evaluateTwo, this.getObbDir().getAbsolutePath(), "fileName" + i);
                }
                params.addBodyParameter("file", new File(img_Path),null,img_Path);

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
                            postDataToService();
                        }

                    }else {
                        Toast.makeText(AddjsxmActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
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
    private void postVideoToServiceRx() {

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

                        postDataToService();
                    }else {
                        Toast.makeText(AddjsxmActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
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
                        Intent openBendiPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openBendiPicIntent.setType("image/*");
                        startActivityForResult(openBendiPicIntent, CHOOSE_PICTURE);
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