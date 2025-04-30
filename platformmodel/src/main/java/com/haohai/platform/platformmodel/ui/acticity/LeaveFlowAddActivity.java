package com.haohai.platform.platformmodel.ui.acticity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.haohai.platform.platformmodel.ui.utils.RequestCode;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rx.functions.Action1;

/**
 * 请假流程申请
 */
public class LeaveFlowAddActivity extends HhBaseActivity implements DatePicker.OnDateChangedListener{

    private static final String TAG = LeaveFlowAddActivity.class.getSimpleName();
    private FrameLayout quantianLayout;
    private FrameLayout kaishiLayout;
    private FrameLayout jieshuLayout;
    private Switch dataTypeSwitch;
    public boolean isDuoTian = false;
    private StringBuffer date;
    private int year;
    private int month;
    private int day;
    public int chooseHour;
    public int chooseMinute;
    public int currentDataState = 0 ; // 0是前天日期 1是开始日期  2结束日期
    public int currentChooseShijian = 0;  //0 全天 1 上午 2 下午
    private TextView quantianButton;
    private TextView shangwuButton;
    private TextView xiawuButton;
    private TextView kaishiView;
    private TextView jieshuView;
    private TextView quantianView;
    private ActionBar actionBar;
    private EditText liyouEdit;
    private TextView shangjiView;
    private FrameLayout shangjiLayout;
    private String currentShangji = "";
    private TextView jingliView;
    private FrameLayout jingliLayout;
    private String currentJingli = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_flow);
        date = new StringBuffer();
        progressDialog = new ProgressDialog(this);
        initDateTime();
        initView();
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("请假流程");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        if (isDuoTian ){  if (kaishiView.getText().toString().equals("")) {
                            Toast.makeText(LeaveFlowAddActivity.this, "请选择开始时间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                            if (jieshuView.getText().toString().equals("")) {
                                Toast.makeText(LeaveFlowAddActivity.this, "请选择结束时间", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }else {
                            if (quantianView.getText().toString().equals("")) {
                                Toast.makeText(LeaveFlowAddActivity.this, "请选择请假时间", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (liyouEdit.getText().toString().equals("")) {
                            Toast.makeText(LeaveFlowAddActivity.this, "请输入请假理由", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (currentShangji.equals("")){
                            Toast.makeText(LeaveFlowAddActivity.this, "请选择上级", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (currentJingli.equals("")){
                            Toast.makeText(LeaveFlowAddActivity.this, "请选择经理", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        postDataToService();
                        break;
                }
            }
        });
        actionBar.setRightView("下一步");

        dataTypeSwitch = (Switch) findViewById(R.id.data_type_switch);
        quantianLayout = (FrameLayout) findViewById(R.id.quantian_layout);
        kaishiLayout = (FrameLayout) findViewById(R.id.kaishi_layout);
        jieshuLayout = (FrameLayout) findViewById(R.id.jieshu_layout);
        kaishiView = (TextView) findViewById(R.id.kaishi_view);
        jieshuView = (TextView) findViewById(R.id.jieshu_view);
        quantianView = (TextView) findViewById(R.id.quantian_view);
        liyouEdit = (EditText) findViewById(R.id.liyou_edit);

        shangjiView = (TextView) findViewById(R.id.shangji_view);
        shangjiLayout = (FrameLayout) findViewById(R.id.shangji_layout);
        jingliView = (TextView) findViewById(R.id.jingli_view);
        jingliLayout = (FrameLayout) findViewById(R.id.jingli_layout);


        if(isDuoTian){
            currentChooseShijian = 1;   //默认半天
            quantianLayout.setVisibility(View.GONE);
            kaishiLayout.setVisibility(View.VISIBLE);
            jieshuLayout.setVisibility(View.VISIBLE);
        }else {
            currentChooseShijian = 0;   //默认 全天
            quantianLayout.setVisibility(View.VISIBLE);
            kaishiLayout.setVisibility(View.GONE);
            jieshuLayout.setVisibility(View.GONE);
        }

        bingView();
    }

    /**
     * 提交数据
     */
    private void postDataToService() {

        showDialogProgress(progressDialog,"提交中...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",new DbConfig(this).getUser().getId());
            jsonObject.put("reason",liyouEdit.getText().toString());
            jsonObject.put("leadership",currentShangji);            //第一位审批人
            jsonObject.put("deptLeadership",currentJingli);        //部门领导
            if (isDuoTian ){
                jsonObject.put("timeType",2);
                jsonObject.put("startTime",kaishiView.getText().toString());
                jsonObject.put("endTime",jieshuView.getText().toString());

            }else {
                jsonObject.put("timeType",1);
                jsonObject.put("oneDayTime",quantianView.getText().toString());
            }

        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/process/insertProcess");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.addParameter("ccs",1);
        params.addParameter("conditions",2);
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
                    if (code.equals("200")){
                        Toast.makeText(LeaveFlowAddActivity.this, "申请成功", Toast.LENGTH_SHORT).show();
                        setResult( RequestCode.LIST_CHANGE);
                        finish();
                    }else {
                        Toast.makeText(LeaveFlowAddActivity.this, "流程申请失败", Toast.LENGTH_SHORT).show();
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

    private void bingView() {
        RxViewAction.clickNoDouble(jingliLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), BumenChooseActivity.class);
                        intent.putExtra("CHOOSE_STATE" , 1);//0是选择上级 1是选择经理
                        startActivityForResult(intent,RequestCode.DATA_CHANGE);
                    }
                });
        RxViewAction.clickNoDouble(shangjiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), BumenChooseActivity.class);
                        intent.putExtra("CHOOSE_STATE" , 0);//0是选择上级 1是选择经理
                        startActivityForResult(intent,RequestCode.DATA_CHANGE);
                    }
                });

        /**
         * 全天选择
         */
        RxViewAction.clickNoDouble(quantianLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentDataState = 0 ;
                        showDataDialog();
                    }
                });
        /**
         * 开始日期选择
         */
        RxViewAction.clickNoDouble(kaishiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentDataState = 1 ;
                        showDataDialog();
                    }
                });
        /**
         * 结束日期选择
         */
        RxViewAction.clickNoDouble(jieshuLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentDataState = 2 ;
                        showDataDialog();
                    }
                });
        dataTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isDuoTian = true;
                    currentChooseShijian = 1;   //默认半天
                    quantianLayout.setVisibility(View.GONE);
                    kaishiLayout.setVisibility(View.VISIBLE);
                    jieshuLayout.setVisibility(View.VISIBLE);
                }else {
                    isDuoTian = false;
                    currentChooseShijian = 0;   //默认 全天
                    quantianLayout.setVisibility(View.VISIBLE);
                    kaishiLayout.setVisibility(View.GONE);
                    jieshuLayout.setVisibility(View.GONE);
                }
            }
        });
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
                if (currentDataState == 0){
                    if (currentChooseShijian == 0){
                        quantianView.setText(date + " 全天");
                    }else if (currentChooseShijian == 1){
                        quantianView.setText(date + " 上午");
                    }else if(currentChooseShijian == 2){
                        quantianView.setText(date + " 下午");
                    }

                }else if (currentDataState == 1){
                    if (currentChooseShijian == 1){
                        kaishiView.setText(date + " 上午");
                    }else if(currentChooseShijian == 2){
                        kaishiView.setText(date + " 下午");
                    }
                }else {
                    if (currentChooseShijian == 1){
                        jieshuView.setText(date + " 上午");
                    }else if(currentChooseShijian == 2){
                        jieshuView.setText(date + " 下午");
                    }
                }

                dialog.dismiss();
            //    showTimeDialog();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog  dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_date_qingjia, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        quantianButton = ((TextView) dialogView.findViewById(R.id.quantian_view));
        shangwuButton = ((TextView) dialogView.findViewById(R.id.shangwu_view));
        xiawuButton = ((TextView) dialogView.findViewById(R.id.xiawu_view));
        initDateButton();
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

       // datePicker.setMaxDate(endTimre);
        datePicker.setMinDate(endTimre);


        if (currentDataState == 0){
            dialog.setTitle("选择请假时间");
        }else if (currentDataState == 1){
            dialog.setTitle("选择开始时间");
        }else {
            dialog.setTitle("选择结束时间");
        }
        dialog.setView(dialogView);
        dialog.show();
       // Log.e(TAG, "showDataDialog:-- " + datePicker.getMeasuredWidth() );
        //初始化日期监听事件
        datePicker.init(year, month, day, this);

        /**
         * 选择全天
         */
        RxViewAction.clickNoDouble(quantianButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseShijian = 0;
                        initDateButton();

                    }
                });
        /**
         * 选择上午
         */
        RxViewAction.clickNoDouble(shangwuButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseShijian = 1;
                        initDateButton();
                    }
                });
        /**
         * 选择下午
         */
        RxViewAction.clickNoDouble(xiawuButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseShijian = 2;
                        initDateButton();
                    }
                });
    }

    private void initDateButton() {
        if (currentDataState == 0){
            quantianButton.setVisibility(View.VISIBLE);
        }else {
            quantianButton.setVisibility(View.GONE);
        }
        if (currentChooseShijian == 0){
            quantianButton.setBackgroundResource(R.drawable.bg_button_lan_yuanjiao);
            shangwuButton.setBackgroundResource(R.drawable.bg_button_hui_yuanjiao);
            xiawuButton.setBackgroundResource(R.drawable.bg_button_hui_yuanjiao);
        }else if (currentChooseShijian == 1){
            quantianButton.setBackgroundResource(R.drawable.bg_button_hui_yuanjiao);
            shangwuButton.setBackgroundResource(R.drawable.bg_button_lan_yuanjiao);
            xiawuButton.setBackgroundResource(R.drawable.bg_button_hui_yuanjiao);
        }else if (currentChooseShijian == 2){
            quantianButton.setBackgroundResource(R.drawable.bg_button_hui_yuanjiao);
            shangwuButton.setBackgroundResource(R.drawable.bg_button_hui_yuanjiao);
            xiawuButton.setBackgroundResource(R.drawable.bg_button_lan_yuanjiao);
        }
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
        chooseHour = calendar.get(Calendar.HOUR);
        chooseMinute = calendar.get(Calendar.MINUTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RequestCode.DATA_CHANGE) {

            int choose_state = data.getIntExtra("CHOOSE_STATE", 0);
            if (choose_state == 0){
                currentShangji = data.getStringExtra("SHANGJI");
                shangjiView.setText(currentShangji);
            }else {
                currentJingli = data.getStringExtra("SHANGJI");
                jingliView.setText(currentJingli);
            }

        }
    }
}
