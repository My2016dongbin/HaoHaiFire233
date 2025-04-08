package com.haohai.platform.platformmodel.ui.acticity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.functions.Action1;

public class PersoninfoActivity extends HhBaseActivity implements DatePicker.OnDateChangedListener {
    private static final String TAG ="PersoninfoActivity" ;
    private ActionBar actionBar;
    private User user;
    private TextView usercodeView;
    private EditText realnameView;
    private EditText emailView;
    private EditText phoneView;
    private TextView birthdayView;
    private ImageView backButton;
    private ImageView updateButton;
    public Boolean isEdit=false;
    private TextView xiugaiButton;
    private StringBuffer date;
    private StringBuffer endDate;
    private int year;
    private int month;
    private int day;
    public int chooseHour;
    public int chooseMinute;
    private String timestr;
    private static final int MAX_LENGTH = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initView();
        setEditState();
        initDateTime();
    }

    private void initView() {
        date = new StringBuffer();
        endDate = new StringBuffer();
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("个人信息");
        actionBar.setRightView("修改");
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
                        setEditState();
                        break;
                }
            }
        });
        usercodeView = findViewById(R.id.usercode_view);
        realnameView = findViewById(R.id.realname_view);
        realnameView.setFilters(new InputFilter[]{filter});
        emailView = findViewById(R.id.email_view);
        phoneView = findViewById(R.id.phone_view);
        birthdayView = findViewById(R.id.birthday_view);
        xiugaiButton = findViewById(R.id.xiugai_button);
//        backButton =findViewById(R.id.back_button1);
//        updateButton=findViewById(R.id.update_view);
        user = new DbConfig(this).getUser();
        if (user!=null){
            usercodeView.setText(user.getUserCode());
            realnameView.setText(user.getFullName());
            Log.e(TAG, "initView: "+user.getEmail() );
            emailView.setText(user.getEmail());
            if (!(user.getPhone().equals("null"))){
                phoneView.setText(user.getPhone());
            }
            birthdayView.setText(user.getBirthday().replace("T"," ").substring(0,user.getBirthday().indexOf(".")));
        }
        xiugaiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateinfo();
            }
        });
        RxViewAction.clickNoDouble(birthdayView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog();
                    }
                });
    }
    private void updateinfo() {
        JSONObject jsonObject = new JSONObject();
        if (birthdayView.getText()!="null"){
            timestr = (String) birthdayView.getText().subSequence(0,10);
        }
        try {
            jsonObject.put("id",user.getId());
            jsonObject.put("fullName",realnameView.getText().toString());
            jsonObject.put("phone",phoneView.getText().toString());
            jsonObject.put("email",emailView.getText().toString());
            jsonObject.put("birthday",timestr+"T00:00:00.000+0800");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "updateinfo: "+birthdayView.getText().toString() );
        Log.e(TAG, "updateinfo: "+ jsonObject.toString());
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "auth/api/auth/user");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.setConnectTimeout(10000);
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        Toast.makeText(PersoninfoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        //setResult( RequestCode.LIST_CHANGE);
                        getUserInfo();

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
                Toast.makeText(PersoninfoActivity.this, "数据提交失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void setEditState() {

        if (isEdit){
            xiugaiButton.setVisibility(View.VISIBLE);
            realnameView.setFocusable(true);
            realnameView.setFocusableInTouchMode(true);
            realnameView.requestFocus();
            emailView.setFocusable(true);
            emailView.setFocusableInTouchMode(true);
            phoneView.setFocusable(true);
            phoneView.setFocusableInTouchMode(true);
            birthdayView.setClickable(true);
        }else {
            birthdayView.setClickable(false);
            xiugaiButton.setVisibility(View.GONE);
            realnameView.setFocusable(false);
            realnameView.setFocusableInTouchMode(false);
            emailView.setFocusable(false);
            emailView.setFocusableInTouchMode(false);
            phoneView.setFocusable(false);
            phoneView.setFocusableInTouchMode(false);

        }

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
                birthdayView.setText(date);
                dialog.dismiss();
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
    private void getUserInfo() {
        final RequestParams params = new RequestParams(RequestUtils.LOGIN_URL + "auth/api/auth/user/get/userinfo");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.setConnectTimeout(10000);
        params.addHeader("Authorization","bearer " + user.getToken());
        Log.e(TAG, "getUserInfo:-- " + params );
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:--2- " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){
                        Log.e(TAG, "onSuccess: --3-" );
                        JSONArray data = jsonObject.getJSONArray("data");
                        JSONObject userJsonObj = data.getJSONObject(0);
                        String id = userJsonObj.getString("id");
                        String userCode = userJsonObj.getString("userCode");
                        String fullName = userJsonObj.getString("fullName");
                        String email = userJsonObj.getString("email");
                        String phone = userJsonObj.getString("phone");
                        String sex = userJsonObj.getString("sex");
                        String entryTime = userJsonObj.getString("entryTime");
                        String birthday = userJsonObj.getString("birthday");
                        String type = userJsonObj.getString("type");
                        String isSuperAdmin = userJsonObj.getString("isSuperAdmin");
                        String comment = userJsonObj.getString("comment");
                        String groupId = userJsonObj.getString("groupId");

                        String gridNo = userJsonObj.getString("gridNo");
                        String bkchar2 = userJsonObj.getString("bkchar2");
                        String money = userJsonObj.getString("money");
                        String lockMoney = userJsonObj.getString("lockMoney");
                        String groupName = userJsonObj.getString("groupName");
                        String headUrl = userJsonObj.getString("headUrl");
                        String state = userJsonObj.getString("state");
                        String imToken = userJsonObj.getString("imToken");

                        User user1 = new User(id, userCode,user.getUserCode(), user.getUserPasswd(), fullName, email, phone, sex, entryTime, birthday, type, isSuperAdmin, comment, groupId,
                                gridNo, bkchar2, money, lockMoney, groupName, state, 1, user.getToken(),headUrl,imToken);
                        user1.isShangchuan = false;
                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();

                        try {
                            db.delete(User.class);
                            db.saveOrUpdate(user1);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        Intent intent=new Intent();
                        intent.putExtra("message",fullName);
                        intent.setAction("namechange");
                        sendBroadcast(intent);
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
    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!isChinese(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
