package com.skyline.terraexplorer.mainapps.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.bean.Adddyjy;
import com.skyline.terraexplorer.databinding.ActivityAdddyjyBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.multitype.BjdInfo;
import com.skyline.terraexplorer.mainapps.multitype.xmjd;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

public class AdddyjyActivity extends BaseActivity implements DatePicker.OnDateChangedListener{
    private ActionBar actionBar;
    private ActivityAdddyjyBinding binding;
    private AlertDialog.Builder builder;
    private int choose1 = 0;
    private int choose2 = 0;
    private String access_token;
    private ProgressDialog progressDialog;
    public List<xmjd> xmjdTypeList;
    private List<String> xmnameList;
    private StringBuffer date;
    private int year;
    private int month;
    private int day;
    private DbManager db;
    private static final String TAG = "AddssjdActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_addxmgl);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adddyjy);
        initView();
        getDataFromService();
        initDateTime();
    }

    private void initView() {
        access_token = new DbConfig(this).getUser().getToken();
        xmjdTypeList = new ArrayList<>();
        xmnameList =new ArrayList<>();
        db = new DbConfig(this).getDbManager();
        progressDialog = new ProgressDialog(this);
        date = new StringBuffer();
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("添加调运检疫");
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
        RxViewAction.clickNoDouble(binding.qfrqEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog(binding.qfrqEdit);
                    }
                });
        RxViewAction.clickNoDouble(binding.bjrqEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog(binding.bjrqEdit);
                    }
                });
        RxViewAction.clickNoDouble(binding.dcsjEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog(binding.dcsjEdit);
                    }
                });
        RxViewAction.clickNoDouble(binding.addBjd)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent=new Intent(AdddyjyActivity.this, AddbjdActivity.class);
                        startActivity(intent);
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
        if (binding.bjrqEdit.getText().toString().equals("")){
            Toast.makeText(this, "请填写报检时间", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (binding.bjdbhEdit.getText().toString().equals("")){
            Toast.makeText(this, "请填写报检编号", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (binding.bjdbhEdit.getText().toString().equals("")){
            Toast.makeText(this, "请填写报检编号", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (binding.hwzslEdit.getText().toString().equals("")){
            Toast.makeText(this, "请填写货物总量", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (binding.hwzhzEdit.getText().toString().equals("")){
            Toast.makeText(this, "请填写货物总货值", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        List<BjdInfo> checkInfoList1 = new ArrayList<>();
        try {
            checkInfoList1 = db.selector(BjdInfo.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        Adddyjy adddyjy = new Adddyjy();
        adddyjy.setInspectionDate(binding.bjrqEdit.getText().toString()+" 00:00:00");
        adddyjy.setPestPlantProductInfoDTOList(checkInfoList1);
        adddyjy.setFormNo(binding.bjdbhEdit.getText().toString());
        adddyjy.setTransportationUnit(binding.dydwEdit.getText().toString());
        adddyjy.setTransportationUnitAddress(binding.dydwdzEdit.getText().toString());
        adddyjy.setTransportationPerson(binding.dyrxmEdit.getText().toString());
        adddyjy.setIdNumber(binding.sfzEdit.getText().toString());
        adddyjy.setPhone(binding.zfjeEdit.getText().toString());
        adddyjy.setConsignee(binding.shdwEdit.getText().toString());
        adddyjy.setPlantSource(binding.zwcplyEdit.getText().toString());
        adddyjy.setShippingAddress(binding.shdwdzEdit.getText().toString());
        adddyjy.setTransportMeans(binding.ysgjEdit.getText().toString());
        adddyjy.setExportTime(binding.dcsjEdit.getText().toString()+" 00:00:00");
        adddyjy.setStorageLocation(binding.cfddEdit.getText().toString());
        adddyjy.setTransportStartEnd(binding.ysqyEdit.getText().toString());
        adddyjy.setQuantity(Double.parseDouble(binding.hwzslEdit.getText().toString()));
        adddyjy.setCargoTotalValue(Double.parseDouble(binding.hwzhzEdit.getText().toString()));
        adddyjy.setQuarantineResult(binding.jyjgEdit.getText().toString());
        adddyjy.setQuarantineOfficer(binding.jyyxmEdit.getText().toString());
        adddyjy.setQuarantineOfficerNumber(binding.jyybhEdit.getText().toString());
        adddyjy.setIssueDate(binding.qfrqEdit.getText().toString()+" 00:00:00");
        adddyjy.setRemark(binding.kgqmsEdit.getText().toString());
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(adddyjy);
        Log.e(TAG, "json1: "+json1 );
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/pestTransportationQuarantine" );
        //params.setAsJsonContent(true);
        params.setBodyContent(json1);
        params.addHeader("Authorization","bearer " + access_token);
        params.setConnectTimeout(10000);
        Log.e(TAG, "postDataToServiceFromDb---" + params);
        Log.e(TAG, "postDataToServiceFromDb---" + json1);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --数据上传成功--" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Toast.makeText(AdddyjyActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AdddyjyActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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
    private void getDataFromService() {
        JSONObject jsonObject = new JSONObject();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/projectPlanningAndManagement/list");
        params.setAsJsonContent(true);
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());
        params.setBodyContent(jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    Log.e(TAG, "getDataFromService: " + result);
                    JSONArray data = jsonObject1.getJSONArray("data");
                    Gson gson = new Gson();
                    xmjdTypeList = gson.fromJson(String.valueOf(data), new TypeToken<List<xmjd>>(){}.getType());
                    Log.e(TAG, "showxxmcChangeDailog: "+xmjdTypeList.size() );
                    for (int i = 0; i < xmjdTypeList.size(); i++) {
                        xmnameList.add(xmjdTypeList.get(i).getName());
                    }
                    Log.e(TAG, "showxxmcChangeDailog: "+xmnameList.size() );
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