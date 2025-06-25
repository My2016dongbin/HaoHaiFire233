package com.haohai.platform.platformmodel.ui.acticity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.db.Attendance;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.utils.PhoneUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

public class AttendanceActivity extends HhBaseActivity {
    private static final String TAG = AttendanceActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView xingqiView;
    private TextView riqiView;
    private TextView qiandaoShijianView;
    private TextView qiantuishijianvView;
    private TextView qiandaoButton;
    private TextView qiantuiButton;
    private String currentRiqi;
    private String currentXingqi;
    private boolean isQiandao = false;
    private boolean isQiantui = false;
    private String currentLongitude = "";
    private String currentLatitude = "";
    private Dialog infoDialog;
    private View infoInflate;
    private EditText yuanyinEditView;
    private TextView yuanyinButton;
    private int currentPost = 0;  //0是签到 1是签退
    private boolean hasYuanyin = false;  //是否有原因
    private ProgressDialog progressDialog;
    private TextView qingkuangView;
    private DbConfig dbConfig;
    private DbManager db;
    private TextView lishiKaoqin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        progressDialog = new ProgressDialog(this);


        dbConfig = new DbConfig(getApplicationContext());
        db = dbConfig.getDbManager();


        initView();

        getData();
        getAttendanceInfo();


        initButton();



        String deviceID = PhoneUtils.getDeviceID();
        Log.e(TAG, "onCreate: " + deviceID);
    }

    /**
     * 初始化签到信息
     */
    private void getAttendanceInfo() {
        Attendance attendance = dbConfig.getAttendance();
        if (attendance == null){
            try {
                db.saveOrUpdate(new Attendance("1",currentRiqi,"","",false,false));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }else {
            if (!attendance.getRiqi().equals(currentRiqi)){      //不是当天的数据  初始化数据
                try {
                    db.saveOrUpdate(new Attendance("1",currentRiqi,"","",false,false));
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void initButton() {
        Attendance attendance = dbConfig.getAttendance();
        isQiandao = attendance.isQiandao();
        isQiantui = attendance.isQiantui();
        //更新按钮数据
        if (isQiandao){     //已经签到
            qiandaoButton.setBackgroundResource(R.drawable.bg_button_hui);
            qiantuiButton.setBackgroundResource(R.drawable.bg_button_lan);
            qiandaoButton.setClickable(false);
        }else {             //没有签到
            qiandaoButton.setBackgroundResource(R.drawable.bg_button_lan);
            qiantuiButton.setBackgroundResource(R.drawable.bg_button_hui);
            qiantuiButton.setClickable(false);
        }

        if (isQiantui){
            qiantuiButton.setBackgroundResource(R.drawable.bg_button_hui);
            qiantuiButton.setClickable(false);
        }



        //更新时间数据
        if (attendance.isQiandao()) {
            qiandaoShijianView.setVisibility(View.VISIBLE);
            qiandaoShijianView.setText(attendance.getQiandaoShijian());
        }else {
            qiandaoShijianView.setVisibility(View.GONE);
        }

        if (attendance.isQiantui()){
            qiantuishijianvView.setVisibility(View.VISIBLE);
            qiantuishijianvView.setText(attendance.getQiantuiShijian());
        }else {
            qiantuishijianvView.setVisibility(View.GONE);
        }
    }

    private void getData() {
        SimpleDateFormat sdfWeek = new SimpleDateFormat("EEEE");
        SimpleDateFormat sdfYearMonthDay = new SimpleDateFormat("yyyy年MM月dd日");

        currentRiqi = sdfYearMonthDay.format(new Date().getTime());
        currentXingqi = sdfWeek.format(new Date().getTime());

        xingqiView.setText(currentXingqi);
        riqiView.setText(currentRiqi);
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        break;
                }
            }
        });
        actionBar.setTitle("考勤统计");

        xingqiView = (TextView) findViewById(R.id.xingqi_view);
        riqiView = (TextView) findViewById(R.id.riqi_view);
        qiandaoShijianView = (TextView) findViewById(R.id.qiandao_shijian_view);
        qiantuishijianvView = (TextView) findViewById(R.id.qiantui_shijian_view);
        qiandaoButton = (TextView) findViewById(R.id.qiandao_button);
        qiantuiButton = (TextView) findViewById(R.id.qiantui_button);
        lishiKaoqin = (TextView) findViewById(R.id.lishi_view);


        RxViewAction.clickNoDouble(lishiKaoqin)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),AttendanceRecordActivity.class));
                    }
                });

        infoDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        infoInflate = LayoutInflater.from(this).inflate(R.layout.dialog_alarm_process,null);
        infoInflate.setMinimumWidth(10000);

        yuanyinEditView = ((EditText) infoInflate.findViewById(R.id.yuanyin_edit));
        yuanyinButton = ((TextView) infoInflate.findViewById(R.id.yuanyin_button));
        qingkuangView = ((TextView) infoInflate.findViewById(R.id.qingkuang_view));
        infoDialog.setContentView(infoInflate);
        Window infoWindow = infoDialog.getWindow();
        infoWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams infoListLp = infoWindow.getAttributes();

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int width = wm.getDefaultDisplay().getWidth();
        // infoListLp.height = (int) (height * 0.7);
        // infoWindow.setAttributes(infoListLp);
        infoDialog.setCanceledOnTouchOutside(true);

        RxViewAction.clickNoDouble(yuanyinButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (yuanyinEditView.getText().toString().equals("")) {
                            Toast.makeText(AttendanceActivity.this, "请输入说明情况", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (currentPost == 0){
                            postQiandaoDateToService();
                        }else {
                            postQiantuiDataToService();
                        }
                    }
                });


        /**
         * 签到功能
         */
        RxViewAction.clickNoDouble(qiandaoButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (isQiandao){
                            return;
                        }
                        getLocation();
                        currentPost = 0;
                        hasYuanyin = false;
                        postQiandaoDateToService();
                    }
                });
        /**
         * 签退功能
         */
        RxViewAction.clickNoDouble(qiantuiButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isQiantui || !isQiandao){       //已签退  未签到 都不可点击
                            return;
                        }
                        getLocation();
                        currentPost = 1;
                        hasYuanyin = false;
                        postQiantuiDataToService();
                    }
                });
    }

    private void postQiantuiDataToService() {
        showDialogProgress(progressDialog,"签退中...");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",new DbConfig(this).getUser().getId());
            if (hasYuanyin){
                jsonObject.put("cause",yuanyinEditView.getText().toString());
            }
            // jsonObject.put("signInTime",formatter.format(curDate).replace(" ","T"));
            jsonObject.put("signInTime","2020-06-10T08:28:59");
            JSONObject workPositionObj = new JSONObject();
            workPositionObj.put("lat",Double.parseDouble(currentLatitude));
            workPositionObj.put("lng",Double.parseDouble(currentLongitude));
            jsonObject.put("workPosition",workPositionObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "oa/api/attendance/nightSign");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postData:-- params--" + params);
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    Log.e(TAG, "onSuccess:code= " + code );
                    if (code.equals("200")){
                        Toast.makeText(AttendanceActivity.this, "签退成功", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        infoDialog.dismiss();
                        yuanyinEditView.setText("");
                        isQiantui = true;

                        //更新本地数据库签到状态信息
                        SimpleDateFormat sdfShiFenMiao = new SimpleDateFormat("HH:mm:ss");
                        String shifenmiao = sdfShiFenMiao.format(new Date().getTime());
                        Attendance attendance = dbConfig.getAttendance();
                        attendance.setQiantui(true);
                        attendance.setQiantuiShijian(shifenmiao);
                        try {
                            db.saveOrUpdate(attendance);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        initButton();

                    }else if (code.equals("204")){
                        hasYuanyin = true;
                        qingkuangView.setText("请说明早退原因:");
                        progressDialog.dismiss();
                        infoDialog.show();
                    }else if ( code.equals("202")){
                        hasYuanyin = true;
                        qingkuangView.setText("请说明签退范围之外原因:");
                        progressDialog.dismiss();
                        infoDialog.show();
                    }else if ( code.equals("203")){
                        hasYuanyin = true;
                        qingkuangView.setText("请说明早退原因及签退范围之外原因:");
                        progressDialog.dismiss();
                        infoDialog.show();
                    }else if ( code.equals("205")){
                        hasYuanyin = true;
                        qingkuangView.setText("请说明早退原因及签退范围之外原因:");
                        progressDialog.dismiss();
                        infoDialog.show();
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

    private void postQiandaoDateToService() {
        showDialogProgress(progressDialog,"签到中...");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",new DbConfig(this).getUser().getId());
            if (hasYuanyin){
                jsonObject.put("cause",yuanyinEditView.getText().toString());
            }

             jsonObject.put("signInTime",formatter.format(curDate).replace(" ","T"));
        //    jsonObject.put("signInTime","2020-06-10T08:28:59");
            JSONObject workPositionObj = new JSONObject();
            workPositionObj.put("lat",Double.parseDouble(currentLatitude));
            workPositionObj.put("lng",Double.parseDouble(currentLongitude));
            jsonObject.put("workPosition",workPositionObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "oa/api/attendance/earlySign");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postData:-- params--" + params);
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);

                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    Log.e(TAG, "onSuccess:code= " + code );
                    if (code.equals("200")){
                        Toast.makeText(AttendanceActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        infoDialog.dismiss();
                        yuanyinEditView.setText("");
                        isQiandao = true;

                        //更新本地数据库签到状态信息
                        SimpleDateFormat sdfShiFenMiao = new SimpleDateFormat("HH:mm:ss");
                        String shifenmiao = sdfShiFenMiao.format(new Date().getTime());
                        Attendance attendance = dbConfig.getAttendance();
                        attendance.setQiandao(true);
                        attendance.setQiandaoShijian(shifenmiao);
                        try {
                            db.saveOrUpdate(attendance);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        initButton();

                    }else if ( code.equals("201")){
                        hasYuanyin = true;
                        qingkuangView.setText("请说明迟到原因:");
                        progressDialog.dismiss();
                        infoDialog.show();
                    }else if ( code.equals("202")){
                        hasYuanyin = true;
                        qingkuangView.setText("请说明签到范围之外原因:");
                        progressDialog.dismiss();
                        infoDialog.show();
                    }else if ( code.equals("203")){
                        hasYuanyin = true;
                        qingkuangView.setText("请说明迟到原因及签到范围之外原因:");
                        progressDialog.dismiss();
                        infoDialog.show();
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

                currentLongitude = longitude+"";
                currentLatitude = latitude+"";
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


            currentLongitude = longitude +"";
            currentLatitude = latitude + "";
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
