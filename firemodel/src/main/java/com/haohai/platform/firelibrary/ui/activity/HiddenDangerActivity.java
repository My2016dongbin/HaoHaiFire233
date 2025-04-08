package com.haohai.platform.firelibrary.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.haohai.platform.firelibrary.R;
import com.haohai.platform.firelibrary.ui.activity.base.HhBaseActivity;
import com.haohai.platform.firelibrary.ui.model.LeiBie;
import com.haohai.platform.firelibrary.ui.model.Leixing;
import com.haohai.platform.firelibrary.ui.multitype.ChooseImage;
import com.haohai.platform.firelibrary.ui.multitype.ChooseImageViewBinder;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.db.Area;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;
import com.ruyiruyi.rylibrary.utils.CommonData;
import com.ruyiruyi.rylibrary.utils.LatLngChangeNew;
import com.ruyiruyi.rylibrary.utils.image.ImagPagerUtil;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.MessagePicturesLayout;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.route.RouteUtils;
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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.ielse.view.imagewatcher.ImageWatcher;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;
@Route(path = RouteUtils.HiddenDangerr)
public class HiddenDangerActivity extends HhBaseActivity implements DatePicker.OnDateChangedListener ,ChooseImageViewBinder.OnChooseImageClickListener ,MessagePicturesLayout.Callback {
    private static final String TAG = HiddenDangerActivity.class.getSimpleName();
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private ChooseImageViewBinder chooseImageViewBinder;
    private List<ChooseImage> list = new ArrayList<>();
    private ImageWatcher vImageWatcher;

    private ImageView goToMapView;

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
    private EditText tudiTypeView;
    private EditText mianjiView;
    private Bitmap evaluateOne;
    private Bitmap evaluateTwo;
    private Bitmap evaluateThree;

    private ProgressDialog addFireDialog;

    private List<Area> allAreaList;
    private WheelView areaWy;
    public int shengSelectIndex = 0;
    public int shiSelectIndex = 0;
    public boolean isChooseSheng = false;
    public String currentChooseSheng = "";
    public String currentChooseShi = "";
    private boolean fromMap = false;
    private String access_token;
    private ImageView backView;
    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    private double currentLongitude = LATITUDE_DEF;
    private double currentLatitude = LONGTITUDE_DEF;
    private List<String> imgStrList;

    @Autowired
    String token;
    private TextView addButton;
    private LocationClient mLocationClient;
    private EditText fengxianEdit;
    private EditText zhengzhiEdit;
    private LinearLayout leibieLayout;
    private TextView leixingView;
    private LinearLayout leixingLayout;
    private TextView leibieView;
    public List<LeiBie> leiBieList;
    public int currentLeibie = 0;   //0 是 1否
    public int leibieSelectIndex = 0;
    public int leixingSelectIndex = 0;
    private WheelView typeWy;
    public String currentChooseLeibie = "";
    public String currentChooseLeixing = "";
    private EditText nameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_danger);
        ARouter.getInstance().inject(this);
        progressDialog = new ProgressDialog(this);
        shengList = new ArrayList<>();
        shiList = new ArrayList<>();
        shengStrList = new ArrayList<>();
        shiStrList = new ArrayList<>();
        uriChooseList = new ArrayList<>();
        allAreaList = new ArrayList<>();
        date = new StringBuffer();
        endDate = new StringBuffer();
        imgStrList = new ArrayList<>();
        leiBieList = new ArrayList<>();

        initDateTime();
        initView();

        bindView();
        if (new DbConfig(getApplicationContext()).getAreaList() == null) {
            getAreaFromService();
        }else {
            allAreaList = new DbConfig(getApplicationContext()).getAreaList();
            initArea();
        }
        updateData();

        getLocation();
        initLeixingData();
        Log.e(TAG, "onCreate: token=" + token);
    }

    private void bindView() {
        RxViewAction.clickNoDouble(leibieView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentLeibie = 0;
                        List<String> leibieStrList = new ArrayList<String>();
                        for (int i = 0; i < leiBieList.size(); i++) {
                            leibieStrList.add(leiBieList.get(i).getName());
                        }
                        showLeibieDialog(leibieStrList);
                    }
                });

        RxViewAction.clickNoDouble(leixingView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
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

        RxViewAction.clickNoDouble(addButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dataIsNull();


                      /*  if (list.size()>0){

                        }else {
                            postDataToService();
                        }*/

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
                        Log.e(TAG, "call: " +currentLongitude );
                        Log.e(TAG, "call: " + currentLatitude);
                        intent.putExtra("longitude_double", CommonData.lng);
                        intent.putExtra("latitude_double", CommonData.lat);
                        startActivityForResult(intent, MAP_REUEST_CODE);
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
                                Toast.makeText(HiddenDangerActivity.this, "请先选择省", Toast.LENGTH_SHORT).show();
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

    }

    /**
     * 判断提交数据是都是null
     */
    private void dataIsNull() {
        if (nameEdit.getText().toString().equals("")){
            Toast.makeText(this, "请输入资源点名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if(currentChooseLeibie.equals("")){
            Toast.makeText(this, "请选择类别", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentChooseLeixing.equals("")){
            Toast.makeText(this, "请选择类型", Toast.LENGTH_SHORT).show();
            return;
        }
     /*   if (currentChooseSheng.equals("")){
            Toast.makeText(this, "请选择省", Toast.LENGTH_SHORT).show();
            return;
        }*/

       /* if (currentChooseShi.equals("")){
            Toast.makeText(this, "请选择市", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (addressView.getText().toString().equals("")){
            Toast.makeText(this, "请输入地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (jingduView.getText().toString().equals("")){
            Toast.makeText(this, "请输入经度", Toast.LENGTH_SHORT).show();
            return;
        }
        if (weiduView.getText().toString().equals("")){
            Toast.makeText(this, "请输入纬度", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fireTimeText.getText().toString().equals("")){
            Toast.makeText(this, "请选择时间", Toast.LENGTH_SHORT).show();
            return;
        }

/*        if (fengxianEdit.getText().toString().equals("")){
            Toast.makeText(this, "请输入风险描述", Toast.LENGTH_SHORT).show();
            return;
        }
        if (zhengzhiEdit.getText().toString().equals("")){
            Toast.makeText(this, "请输入整治描述", Toast.LENGTH_SHORT).show();
            return;
        }*/
        postPicToService();
    }

    private void showLeibieDialog(final List<String> strList) {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        typeWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        typeWy.setIsLoop(false);
        if (currentLeibie == 0){
            typeWy.setItems(strList, leibieSelectIndex);//init selected position is 0 初始选中位置为0
        }else {
            typeWy.setItems(strList, leixingSelectIndex);//init selected position is 0 初始选中位置为0
        }

        typeWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                if (currentLeibie == 0){
                    currentChooseLeibie = typeWy.getSelectedItem();
                    leibieSelectIndex = typeWy.getSelectedPosition();
                    leibieView.setText(currentChooseLeibie);
                    currentChooseLeixing = leiBieList.get(leibieSelectIndex).getList().get(0).getName();
                    leixingSelectIndex = 0;
                    leixingView.setText(currentChooseLeixing);
                }else {
                    currentChooseLeixing = typeWy.getSelectedItem();
                    leixingSelectIndex = typeWy.getSelectedPosition();
                    leixingView.setText(currentChooseLeixing);
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
                            if (leibieSelectIndex == 0){
                                leibieView.setText(strList.get(0));
                                currentChooseLeibie = strList.get(0);
                            }


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
                            if (leixingSelectIndex == 0){
                                leixingView.setText(strList.get(0));
                                currentChooseLeixing = strList.get(0);
                            }

                        }
                    })
                    .show();
        }
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

    private void postPicToService() {
        showDialogProgress(progressDialog,"提交中...");
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "oa/api/workReport/fileUploadAn");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要

    //    File[] fileData = new File[list.size()];
        for (int i = 0; i < list.size(); i++) {
            try {

                Log.e(TAG, "postPicToService: " + i);
                Uri uri = list.get(i).getUri();
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);

                Bitmap picOne = rotaingImageView(degree, photo);
                String picStr = ImageUtils.savePhoto(picOne, this.getObbDir().getAbsolutePath(), "fileName" + i);
              //  fileData[i] = new  File(picStr);
                params.addBodyParameter("file", new File(picStr),null,picStr);
            } catch (IOException e) {
            }
        }


        // params.setBodyContent(jsonObject.toString());
     //   params.addParameter("file",fileData);
        Log.e(TAG, "postPicToService: " + params );
        Log.e(TAG, "postPicToService: " + token );
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------------- " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){
                        imgStrList.clear();
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray imgStrArray = data.getJSONArray("img");
                        for (int i = 0; i < imgStrArray.length(); i++) {
                            String imgStr = imgStrArray.getString(i);
                            imgStrList.add(imgStr);
                        }
                        postDataToService();
                    }else {
                        Toast.makeText(HiddenDangerActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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
            }
        });


    }

    /**
     * 提交数据到服务器
     */
    private void postDataToService() {
        double[] doubles = new LatLngChangeNew().calBD09toWGS84(currentLatitude, currentLongitude);
        double latParse = doubles[0];
        double lngParse = doubles[1];

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

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cityCode",shiId);
            jsonObject.put("cityName",shiStr);
            jsonObject.put("provinceCode",shengId);
            jsonObject.put("provinceName",shengStr);
            jsonObject.put("resourceName",nameEdit.getText().toString());
            jsonObject.put("address",addressView.getText().toString());
            jsonObject.put("dangerType",currentChooseLeixing);          //隐患类型
            jsonObject.put("dangerDescription",fengxianEdit.getText().toString());  //隐患描述
            jsonObject.put("remark",zhengzhiEdit.getText().toString());  //整治描述
            JSONObject postsion = new JSONObject();
            postsion.put("lat",latParse);
            postsion.put("lng",currentLongitude);
            jsonObject.put("position",postsion); // 整治描述 ,
            for (int i = 0; i < imgStrList.size(); i++) {
                jsonObject.put("pic" + (i+1),imgStrList.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST__URL_HLJ + "fire/api/dangerCheck");
        params.setBodyContent(jsonObject.toString());
        Log.e(TAG, "postPicToService: " + params );
        Log.e(TAG, "postPicToService: " + jsonObject.toString() );
        params.addHeader("Authorization","bearer " + new DbConfig(getApplicationContext()).getUser().getToken());
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
                Log.e(TAG, "onError: 请求失败" );
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

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("隐患排查");
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
        nameEdit = (EditText) findViewById(R.id.name_edit);
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
        oneImageLayout = (FrameLayout) findViewById(R.id.one_image_layout);
        twoImageLayout = (FrameLayout) findViewById(R.id.two_imag_layout);
        oneImage = (ImageView) findViewById(R.id.one_image);
        twoImage = (ImageView) findViewById(R.id.two_image);
        oneImageDelete = (ImageView) findViewById(R.id.one_image_delete);
        twoImageDelete = (ImageView) findViewById(R.id.two_image_delete);
        photoLayout = (LinearLayout) findViewById(R.id.photo_layout);
        addFireButton = (TextView) findViewById(R.id.add_fire_button);
        fengxianEdit = (EditText) findViewById(R.id.fengxian_edit);
        zhengzhiEdit = (EditText) findViewById(R.id.zhengzhi_edit);
        leibieLayout = (LinearLayout) findViewById(R.id.liebie_layout);
        leixingLayout = (LinearLayout) findViewById(R.id.leixing_layout);
        leibieView = (TextView) findViewById(R.id.liebie_view);
        leixingView = (TextView) findViewById(R.id.leixing_view);


        addButton = (TextView) findViewById(R.id.add_button);

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
    }

    /**
     * 图片添加
     */
    @Override
    public void onImageAddClickListener(boolean add, Uri uri,String id) {
        if (add){
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            int size = 2 - list.size();
                            Matisse.from(HiddenDangerActivity.this)
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

    private void updateData() {
        items.clear();
        if (list==null){
            ChooseImage chooseImage = new ChooseImage();
            chooseImage.setAdd(true);
            items.add(chooseImage);
        }else {
            if (list.size()<2){
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
                Toast.makeText(this, "最多只能添加2张", Toast.LENGTH_SHORT).show();
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
        }else  if (requestCode == MAP_REUEST_CODE && resultCode == MAP_REUEST_CODE) {
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
        }

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
