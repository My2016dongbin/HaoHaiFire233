package com.skyline.terraexplorer.mainapps.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.databinding.ActivityAddfzxxBinding;
import com.skyline.terraexplorer.databinding.ActivityAddxmglBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.ImageUtils;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
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

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import rx.functions.Action1;

import static com.skyline.terraexplorer.mainapps.utils.ImageUtils.rotaingImageView;

public class AddfzxxActivity extends BaseActivity implements DatePicker.OnDateChangedListener{
    private ActionBar actionBar;
    private ActivityAddfzxxBinding binding;
    private AlertDialog.Builder builder;
    private int choose1 = 0;
    private int choose2 = 0;
    private final int CHOOSE_PICTURE = 0;
    private String access_token;
    private ProgressDialog progressDialog;
    private StringBuffer date;
    private int year;
    private int month;
    private int day;
    private static final String TAG = "AddxmglActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_addxmgl);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addfzxx);
        initView();
        initDateTime();
    }

    private void initView() {
        access_token = new DbConfig(this).getUser().getToken();
        date = new StringBuffer();
        progressDialog = new ProgressDialog(this);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("添加防治信息");
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
        RxViewAction.clickNoDouble(binding.quxianEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showLeibieChangeDailog(binding.quxianEdit);
                    }
                });
        RxViewAction.clickNoDouble(binding.fssjEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog(binding.fssjEdit);
                    }
                });
        RxViewAction.clickNoDouble(binding.fzsjEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog(binding.fzsjEdit);
                    }
                });
        RxViewAction.clickNoDouble(binding.tijiaoButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDialogProgress(progressDialog,"正在上传");
                        postDataToService();
                    }
                });
    }
    private void postDataToService() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (binding.fsmjEdit.getText().toString().equals("")){
                Toast.makeText(this, "请填写发生面积", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (binding.fssjEdit.getText().toString().equals("")){
                Toast.makeText(this, "请填写发生时间", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (binding.fzsjEdit.getText().toString().equals("")){
                Toast.makeText(this, "请填写防治时间", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (binding.fzfyEdit.getText().toString().equals("")){
                Toast.makeText(this, "请填写防治费用", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (binding.swzsEdit.getText().toString().equals("")){
                Toast.makeText(this, "请填写死亡株数", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (binding.jzmjEdit.getText().toString().equals("")){
                Toast.makeText(this, "请填写寄主面积", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (binding.yjmjEdit.getText().toString().equals("")){
                Toast.makeText(this, "请填写应监面积", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (binding.jcmjEdit.getText().toString().equals("")){
                Toast.makeText(this, "请填写检测面积", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (binding.sdEdit.getText().toString().equals("")){
                Toast.makeText(this, "请填写世代", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if (binding.wghhjEdit.getText().toString().equals("")){
                Toast.makeText(this, "请填写无公害合计", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            jsonObject.put("region",binding.quxianEdit.getText().toString());
            jsonObject.put("occurrenceAddress",binding.fsddEdit.getText().toString());
            jsonObject.put("occurrenceArea",Double.parseDouble(binding.fsmjEdit.getText().toString()));
            jsonObject.put("occurrenceTime",binding.fssjEdit.getText().toString()+" 00:00:00");
            jsonObject.put("preventTime", binding.fzsjEdit.getText().toString()+" 00:00:00");
            jsonObject.put("preventCost",Double.parseDouble(binding.fzfyEdit.getText().toString()));
            jsonObject.put("preventTotal",binding.fzhjEdit.getText().toString());
            jsonObject.put("deadTreesNum",Integer.parseInt(binding.swzsEdit.getText().toString()));
            jsonObject.put("hostTrees",binding.jzszEdit.getText().toString());
            jsonObject.put("pest",binding.hyswEdit.getText().toString());
            jsonObject.put("hostArea",Double.parseDouble(binding.jzmjEdit.getText().toString()));
            jsonObject.put("shouldMonitorArea",Double.parseDouble(binding.yjmjEdit.getText().toString()));
            jsonObject.put("monitoringArea",Double.parseDouble(binding.jcmjEdit.getText().toString()));
            jsonObject.put("generations",Integer.parseInt(binding.sdEdit.getText().toString()));
            jsonObject.put("pollutionFreeTotal",Integer.parseInt(binding.wghhjEdit.getText().toString()));
            jsonObject.put("fungi",binding.zjEdit.getText().toString());
            jsonObject.put("bacteria",binding.xjEdit.getText().toString());
            jsonObject.put("virus",binding.bdEdit.getText().toString());
            jsonObject.put("bee",binding.flEdit.getText().toString());
            jsonObject.put("birds",binding.nlEdit.getText().toString());
            jsonObject.put("plantOriginPotion",binding.zwyyjEdit.getText().toString());
            jsonObject.put("bionic",binding.fsEdit.getText().toString());
            jsonObject.put("artificial",binding.rgEdit.getText().toString());
            jsonObject.put("chemicalProtection",binding.hfEdit.getText().toString());
            jsonObject.put("pollutionFreeChemicalProtection",binding.wghhfEdit.getText().toString());
            jsonObject.put("otherPrevent",binding.qtfzEdit.getText().toString());
            jsonObject.put("otherBiocontrol",binding.qtsfEdit.getText().toString());
            jsonObject.put("otherPollutionFree",binding.qtwghEdit.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/pestPreventionControlInfo" );
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
                        Toast.makeText(AddfzxxActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddfzxxActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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
}