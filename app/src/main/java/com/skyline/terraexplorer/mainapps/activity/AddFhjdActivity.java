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

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.databinding.ActivityFhjdAddBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.multitype.Checkfhjd;
import com.skyline.terraexplorer.mainapps.multitype.CheckfhjdViewBinder;
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

import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;
import io.reactivex.Observer;

import static com.skyline.terraexplorer.mainapps.utils.ImageUtils.rotaingImageView;
import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class AddFhjdActivity extends BaseActivity implements CheckfhjdViewBinder.OnCheckFieldItemClick,DatePicker.OnDateChangedListener{
    private static final String TAG = AddFhjdActivity.class.getSimpleName();;
    private ActionBar actionBar;
    private List<Checkfhjd> CheckfhjdList ;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private RecyclerView listView;
    private CheckfhjdViewBinder checkFhjdProvider;
    private String code;
    private EditText bjcdwEdit;
    private EditText jianchashijianView;
    private EditText jingduEdit;
    private EditText weiduEdit;
    private EditText bfhyqEdit;
    private EditText bdcrEdit;
    private EditText jcrEdit;
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
    private String videostr="";
    private static final int REQUEST_CODE_CHOOSE = 23;
    private ActivityFhjdAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_fhjd_add);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fhjd_add);
        access_token = new DbConfig(this).getUser().getToken();
        date = new StringBuffer();
        uriChooseList = new ArrayList<>();
        imglist=new ArrayList<>();
        initView();
        initDateTime();
    }

    private void initData() {
        CheckfhjdList.add(new Checkfhjd("i0","是否召开了全市森林防火会议进行工作部署","isWorkDeployment",1));CheckfhjdList.add(new Checkfhjd("i1","是否制定了年度森林防火工作方案（意见）","isWorkPlan",1));
        CheckfhjdList.add(new Checkfhjd("i2","是否实行领导干部包保责任","isLeaderLiability",1));CheckfhjdList.add(new Checkfhjd("i3","护林员是否严格落实网格化管理","isGridManagement",1));
        CheckfhjdList.add(new Checkfhjd("i4","在进山路口是否全部设置护林房（检查站）","isSetUpForestShelter",1));CheckfhjdList.add(new Checkfhjd("i5","各护林房、检查站等卡口执勤人员是否上岗在位","isDutyOfficerOnDuty",1));
        CheckfhjdList.add(new Checkfhjd("i6","各卡口是否严格收缴火种","isConfiscateFire",1));CheckfhjdList.add(new Checkfhjd("i7","道路两侧、林缘地带和墓地周边可燃物是否按规定清理","isCleanUpCombustibleMaterials",1));
        CheckfhjdList.add(new Checkfhjd("i8","各级森林消防专业队是否开展动态巡逻","isDynamicPatrol",1));CheckfhjdList.add(new Checkfhjd("i9","沿山脊线（或高点）、道路两侧和墓地周边等重点部位，合理布设水囊（蓄水池或蓄水罐）","isSetWaterSac",1));
        CheckfhjdList.add(new Checkfhjd("i10","水囊(水罐)是否悬挂安全警示标志","isSetSecurityWarning",1));CheckfhjdList.add(new Checkfhjd("i11","是否有水囊(水罐)布设台账","isHaveWaterSacList",1));
        CheckfhjdList.add(new Checkfhjd("i12","林缘地带是否安装隔离网","isInstallIsolationNets",1));CheckfhjdList.add(new Checkfhjd("i13","隔离网是否完好","isIsolationNetIntact",1));
        CheckfhjdList.add(new Checkfhjd("i14","区市森林防火指挥中心是否运行","isCommandCenterRunning",1));CheckfhjdList.add(new Checkfhjd("i15","远程监控点、进山路口监控是否接入本级森林防火指挥中心","isAccessToLocalCommandCenter",1));
        CheckfhjdList.add(new Checkfhjd("i16","监控点是否接入市级指挥中心","isAccessToMunicipalCommandCenter",1));CheckfhjdList.add(new Checkfhjd("i17","是否配备电子指挥地图、纸质或布质指挥地图","isEquippedWithMap",1));
        CheckfhjdList.add(new Checkfhjd("i18","区市、镇街森林消防专业队伍是否集中食宿","isFocusOnAccommodation",1));CheckfhjdList.add(new Checkfhjd("i29","是否开展训练和巡逻","isTrainingAndPatrolling",1));
        CheckfhjdList.add(new Checkfhjd("i20","是否进行扑救森林火灾安全防范教育培训","isFireTraining",1));CheckfhjdList.add(new Checkfhjd("i21","队员是否配备扑救火灾安全防护用具","isEquipProtectiveEquipment",1));
        CheckfhjdList.add(new Checkfhjd("i22","是否组建跨区域增援力量","isOrganizeReinforcements",1));CheckfhjdList.add(new Checkfhjd("i23","是否储备了火灾扑救应急物资","isRestock",1));
        CheckfhjdList.add(new Checkfhjd("i24","储备库是否物资油料分离存放","isOilSeparation",1));CheckfhjdList.add(new Checkfhjd("i25","是否修改完善森林火灾应急预案","isImproveEmergencyPlan",1));
        CheckfhjdList.add(new Checkfhjd("i26","林内景点、主要路口检查站等是否配备森林火险预警旗","isScenicSpotFireWarning",1));CheckfhjdList.add(new Checkfhjd("i27","在进山主要路口检查站位置是否设立了宣传标牌","isMainEntrancePropagandaSigns",1));
        CheckfhjdList.add(new Checkfhjd("i28","在林缘和林内主要道路两侧是否张贴、悬挂、粉刷宣传标语开展宣传","isRoadSidePropagandaSigns",1));CheckfhjdList.add(new Checkfhjd("i29","是否执行领导在岗带班制度和24 小时值班制度","isLeaderShiftSystem",1));
        CheckfhjdList.add(new Checkfhjd("i30","值班制度和森林防火应急处置流程等是否上墙","isSystemOnTheWall",1));CheckfhjdList.add(new Checkfhjd("i31","是否安排专人紧盯“齐鲁风云”卫星火点遥感监测终端","isWatchingTheTerminal",1));
        CheckfhjdList.add(new Checkfhjd("i32","森林消防专业队营房、各护林房、检查站等防火人员聚集区域是否开展水、电、气专项检查，并将各隐患清零","isHiddenTroubleReset",1));CheckfhjdList.add(new Checkfhjd("i33","森林防火车辆驾驶员是否按要求定期开展安全教育","isRegularSafetyEducation",1));
        CheckfhjdList.add(new Checkfhjd("i34","森林防火车辆是否开展全面检修，确保安全运行","isEnsureSafeOperation",1));CheckfhjdList.add(new Checkfhjd("i35","森林防火车辆是否年审，并缴纳保险","isYearOfCareful",1));
        CheckfhjdList.add(new Checkfhjd("i36","森林防火车辆是否安排专人驾驶，驾驶员证件与所驾车型是否相符","isMatchingDrivingDocuments",1));CheckfhjdList.add(new Checkfhjd("i37","野生动物来源是否合法","isFromLegitimateSources",1));
        CheckfhjdList.add(new Checkfhjd("i38","野生动物笼舍防护设施是否牢固","isProtectionSetFirmly",1));CheckfhjdList.add(new Checkfhjd("i39","野生动物驯养企业是否建立安全责任制度","isSafetyResponsibilitySystem",1));
        CheckfhjdList.add(new Checkfhjd("i40","野生动物驯养企业是否建立应急措施或预案","isFeedingEmergencyPlan",1));CheckfhjdList.add(new Checkfhjd("i41","旅游设施、组织大型活动有无许可","isPermits",1));
        CheckfhjdList.add(new Checkfhjd("i42","危险区域是否设立警示标志","isDangerAreaWarning",1));CheckfhjdList.add(new Checkfhjd("i43","安全检查、安全教育等制度、措施是否健全","isSoundSafetyMeasures",1));
        CheckfhjdList.add(new Checkfhjd("i44","是否建立了经常性安全生产检查档案","isSafetyCheckFile",1));CheckfhjdList.add(new Checkfhjd("i45","是否对所属人员组织经常性安全生产教育和培训","isSafetyEducationAndTraining",1));
        CheckfhjdList.add(new Checkfhjd("i46","是否建立安全防范和处置预案、应急处置和灾害救助机制","isDisasterReliefMechanism",1));CheckfhjdList.add(new Checkfhjd("i47","是否组织了训练演练","isTrainingDrills",1));
        CheckfhjdList.add(new Checkfhjd("i48","安全生产责任是否明确","isResponsibilityClear",1));CheckfhjdList.add(new Checkfhjd("i49","是否签订了安全目标责任书","isSafetyResponsibilityStatement",1));
        CheckfhjdList.add(new Checkfhjd("i50","区市森林防火指挥中心是否运行","isCommandCenterRunning",1));CheckfhjdList.add(new Checkfhjd("i51","远程监控点、进山路口监控是否接入本级森林防火指挥中心","isAccessToLocalCommandCenter",1));
        CheckfhjdList.add(new Checkfhjd("i52","隔离网是否完好","isIsolationNetIntact",1));CheckfhjdList.add(new Checkfhjd("i53","区市森林防火指挥中心是否运行","isCommandCenterRunning",1));
        CheckfhjdList.add(new Checkfhjd("i54","水囊(水罐)是否已经注满水","isSacFilledWater",1));CheckfhjdList.add(new Checkfhjd("i55","水车、水囊(水罐)、水泵是否成龙配套","isEquipmentMatching",1));
        CheckfhjdList.add(new Checkfhjd("i56","远程监控点、进山路口监控是否接入本级森林防火指挥中心","isAccessToLocalCommandCenter",1));
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("添加防火监督");
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
        CheckfhjdList =new ArrayList<>();
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
        if (CheckfhjdList.size() > 0){
            initCheckFieldData();}
        tijiaoButton = findViewById(R.id.xiugai_button);
        quxianEdit = findViewById(R.id.quxian_edit);
        bjcdwEdit = findViewById(R.id.bjcdw_edit);
        jianchashijianView= findViewById(R.id.jianchashijian_view);
        jingduEdit= findViewById(R.id.jingdu_edit);
        weiduEdit=findViewById(R.id.weidu_edit);
        bfhyqEdit=findViewById(R.id.bfhyq_edit);
        bfhyqEdit.setText("0");
        bdcrEdit=findViewById(R.id.bdcr_edit);
        jcrEdit=findViewById(R.id.jcr_edit);
        dizhiEdit=findViewById(R.id.dizhi_edit);
        oneImageDelete = (ImageView) findViewById(R.id.one_image_delete);
        twoImageDelete = (ImageView) findViewById(R.id.two_image_delete);
        photoLayout = (LinearLayout) findViewById(R.id.photo_layout);
        oneImage = (ImageView) findViewById(R.id.one_image);
        twoImage = (ImageView) findViewById(R.id.two_image);
        twoImageLayout = (FrameLayout) findViewById(R.id.two_imag_layout);
        shipinView = findViewById(R.id.shipin_view);
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
                            showDataDialog();
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
                            Toast.makeText(AddFhjdActivity.this, "最多可以添加两张图片", Toast.LENGTH_SHORT).show();
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
        adapter.register(Checkfhjd.class, checkFhjdProvider);
    }

    private void initCheckFieldData() {
        items.clear();
        for (int i = 0; i < CheckfhjdList.size(); i++) {
            items.add(CheckfhjdList.get(i));
        }
        Log.e(TAG, "initCheckFieldData: "+items.size() );
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckFieldItemClickLinstener(String id, int state) {
        nonum=0;
        for (int i = 0; i < CheckfhjdList.size(); i++) {
            if (CheckfhjdList.get(i).getId().equals(id)) {
                CheckfhjdList.get(i).state = state;
                Log.e(TAG, "onCheckFieldItemClickLinstener: "+CheckfhjdList.get(i).state );
            }
            if (CheckfhjdList.get(i).state==0){
                nonum++;
            }
            Log.e(TAG, "num: "+nonum );
        }
        bfhyqEdit.setText(String.valueOf(nonum));
    }
    private void postDataToService() {
        if (bjcdwEdit.getText().toString().equals("")){
            Toast.makeText(this, "请填写被检单位", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (jianchashijianView.getText().toString().equals("")){
            Toast.makeText(this, "请填写检查日期", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (bdcrEdit.getText().toString().equals("")){
            Toast.makeText(this, "请填写被检单位负责人签字", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (jcrEdit.getText().toString().equals("")){
            Toast.makeText(this, "请填写检查人员签字", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address",dizhiEdit.getText().toString());
            jsonObject.put("region",quxianEdit.getText().toString());
            jsonObject.put("inspectedUnit",bjcdwEdit.getText().toString());
            jsonObject.put("inspectionTime",jianchashijianView.getText().toString()+" 00:00:00");
            jsonObject.put("lng", NumberFormat.getInstance().parse(jingduEdit.getText().toString()));
            jsonObject.put("lat",NumberFormat.getInstance().parse(weiduEdit.getText().toString()));
            jsonObject.put("unqualifiedCount",Integer.parseInt(bfhyqEdit.getText().toString()));
            jsonObject.put("signatureOfDirector",bdcrEdit.getText().toString());
            jsonObject.put("signatureOfInspector",jcrEdit.getText().toString());
            for (int i = 0; i < CheckfhjdList.size(); i++) {
                jsonObject.put(CheckfhjdList.get(i).getCode(), CheckfhjdList.get(i).state);
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
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/forestFirePreventionAndProductionSafety" );
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
                        Toast.makeText(AddFhjdActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddFhjdActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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

                jianchashijianView.setText(date);


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
                photo = ImageUtils.getBitmapFormUri(AddFhjdActivity.this, uri);
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
                        Toast.makeText(AddFhjdActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddFhjdActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
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