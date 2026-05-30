package com.hht.hsatellitemobile.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hht.hsatellitemobile.db.model.Setting;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.hht.hsatellitemobile.MainActivity;
import com.hht.hsatellitemobile.R;
import com.hht.hsatellitemobile.db.DbConfig;
import com.hht.hsatellitemobile.db.model.User;
import com.hht.hsatellitemobile.ui.activity.base.HhBaseActivity;
import com.hht.hsatellitemobile.utils.RequestUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import rx.functions.Action1;

public class LoginActivity extends HhBaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText passwordEdit;
    private EditText usernameEdot;
    private TextView loginButton;
    public  String token;

    private ProgressDialog codeDialog;
    private TextView registerButton;
    private static boolean isExit = false;
    public static String yingjiTags = "370102,370103,370112,370113,370114,370124,370116,370117,370171,370212,370214,370215,370281,370283,370285,370302,370303,370304,370305,370306,370323,370481,370406,370403,370402,370404,370405,370503,370602,370611,370613,370612,370687,370682,370686,370683,370681,370685,370614,370672,370781,370782,370784,370724,370725,370881,370883,370826,370829,370831,370832,370902,370911,370982,370983,370921,370923,371002,371003,371082,371083,371071,371072,371073,371102,371103,371122,371121,371323,371326,371328,371325,371322,371324,371321,371327,371329,371681";
    public static String normalTag = "";
    private long time = 0;
    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cleanTags();
        codeDialog = new ProgressDialog(this);
        initView();
    }

    private void initView() {
        Log.e(TAG, "initView:kaishi denglu ");
        registerButton = (TextView) findViewById(R.id.register_button);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        usernameEdot = (EditText) findViewById(R.id.name_edit);
        loginButton = (TextView) findViewById(R.id.login_button);

        RxViewAction.clickNoDouble(registerButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                    }
                });

        RxViewAction.clickNoDouble(loginButton)
                .subscribe(new Action1<Void>() {
                    private String passwordStr;
                    private String userNameStr;

                    @Override
                    public void call(Void aVoid) {
                        userNameStr = usernameEdot.getText().toString();
                        passwordStr = passwordEdit.getText().toString();

                        if (userNameStr.isEmpty()) {
                            showDialog("用户名不能为空");
                        }else if (passwordStr.isEmpty()){
                            showDialog("密码不能为空");
                        }else {
                            loginByPassword(userNameStr, passwordStr);
                        }
                    }
                });

        SharedPreferences nw_qq=getSharedPreferences("hh_xml",MODE_PRIVATE);
        String account=nw_qq.getString("account","");
        String pasword=nw_qq.getString("password","");
        usernameEdot.setText(account);
        passwordEdit.setText(pasword);

    }

    private void loginByPassword(final String userNameStr, final String passwordStr) {
        showDialogProgress(codeDialog, "密码登陆中...");
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userNameStr);
            jsonObject.put("password", passwordStr);

        } catch (JSONException e) {
        }
        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Account/Login");
       // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("userName",userNameStr);
        params.addParameter("password",passwordStr);
        params.setConnectTimeout(10000);
        Log.e(TAG, "loginByPassword: param---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: Account/Login " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    String value = jsonObject.getString("value");
                    if (type.equals("1")){
                        token = jsonObject.getString("message");

                      /*  User user = new User();
                        user.setId(1);
                        user.setUsername(userNameStr);
                        user.setPassword(passwordStr);
                        user.setToken(token);
                        user.setIsLogin("1");
*/
                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                      /*  try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }*/

                        Setting setting = new Setting();
                        setting.setId(1);
                        setting.setWeixing("ALL");
                        setting.setTiankong("");
                        setting.setDimian("");
                        setting.setDimao("ALL");
                        setting.setNumber("100");
                        setting.setJingwai("中国");
                        setting.setHuanchong("0");
                        try {
                            db.saveOrUpdate(setting);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                      /*  XGPushManager.registerPush(getApplicationContext(), "geyang",new XGIOperateCallback() {
                            @Override
                            public void onSuccess(Object data, int flag) {
                                //token在设备卸载重装的时候有可能会变    1ac65aa647a80aa347ed8d34d4b24c61325b19b0
                                Log.e(TAG,  "注册成功，设备token为：" + data);
                            }
                            @Override
                            public void onFail(Object data, int errCode, String msg) {
                                Log.e(TAG, "注册失败，错误码：" + errCode + ",错误信息：" + msg);

                            }
                        });*/



                        getUserMsgFromService(token,userNameStr,passwordStr);
                        SharedPreferences.Editor qq_xml=getSharedPreferences("hh_xml",MODE_PRIVATE).edit();
                        qq_xml.putString("account",userNameStr);
                        qq_xml.putString("password",passwordStr);
                        qq_xml.apply();

                    }else {
                        String token = jsonObject.getString("message");
                        Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                        hideDialogProgress(codeDialog);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hideDialogProgress(codeDialog);
                Toast.makeText(LoginActivity.this, "网络异常，请检查网络链接", Toast.LENGTH_SHORT).show();
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
     * 获取用户信息
     */


    private void getUserMsgFromService(final String token,final String userNameStr, final String passwordStr) {
        Log.e(TAG, "getUserMsgFromService:11111 ");
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "Account/GetUserMsg");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("Token",token);
        //params.setConnectTimeout(20000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: userMeg===========" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final String userId = jsonObject.getString("UserId");
                    final String userName = jsonObject.getString("UserName");
                    final String companyName = jsonObject.getString("CompanyName");
                    final String provinceNo = jsonObject.getString("ProvinceNo");
                    final String provinceName = jsonObject.getString("ProvinceName");
                    final String cityNo = jsonObject.getString("CityNo");
                    final String cityName = jsonObject.getString("CityName");
                    final String countyNo = jsonObject.getString("CountyNo");
                    final String countyName = jsonObject.getString("CountyName");


                    /*//RequestParams requestParams = new RequestParams(RequestUtils.REQUEST_URL + "Account/GetLandTypeRole");
                    RequestParams requestParams = new RequestParams(RequestUtils.REQUEST_URL + "SatelliteFireAlarmConfig/GetFireAlarmConfigByUser");
                    requestParams.addParameter("token",token);
                    requestParams.addParameter("userId",userId);
                    Log.e(TAG, "onSuccess: role token " + token  );
                    Log.e(TAG, "onSuccess: role userId " + userId  );
                    Log.e(TAG, "onSuccess: role params " + requestParams.toString()  );
                    x.http().get(requestParams, new CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e(TAG, "onSuccess: role result " + result  );
                            try{
                                JSONObject obj = new JSONObject(result);
                                Log.e(TAG, "onSuccess: role " + obj.toString()  );
                                String tagString = "";

                                if(userName!=null&&userName.contains("山东省应急管理厅")){
                                    String[] yingJiTagList = yingjiTags.split(",");
                                    Set<String> tagSet = new LinkedHashSet<String>();
                                    for (int i = 0; i < yingJiTagList.length; i++) {
                                        tagSet.add(yingJiTagList[i]);
                                    }
                                    if(obj.getString("LandType") == null || Objects.equals(obj.getString("LandType"), "null") || Objects.equals(obj.getString("LandType"), "ALL")){
                                        tagSet.add("Farmland");
                                        tagSet.add("Woodland");
                                        tagSet.add("Grassland");
                                        tagSet.add("Otherland");
                                    }else{
                                        if(obj.getString("LandType").contains("Farmland")){
                                            tagSet.add("Farmland");
                                        }
                                        if(obj.getString("LandType").contains("Woodland")){
                                            tagSet.add("Woodland");
                                        }
                                        if(obj.getString("LandType").contains("Grassland")){
                                            tagSet.add("Grassland");
                                        }
                                        if(obj.getString("LandType").contains("Otherland")){
                                            tagSet.add("Otherland");
                                        }
                                    }
                                    if(obj.getString("Delay") == null || Objects.equals(obj.getString("Delay"), "ALL") || Objects.equals(obj.getString("Delay"), "0") || Objects.equals(obj.getString("Delay"), "") || Objects.equals(obj.getString("Delay"), "null")){
                                        tagSet.add("Delay_ALL");
                                    }else{
                                        int delay = Integer.parseInt(obj.getString("Delay"));
                                        for (int i = 0; i < delay; i++) {
                                            tagSet.add("Delay_" + i);
                                        }
                                    }

                                    tagString = tagSet.toString();
                                    //XGPushManager.setTags(getApplicationContext(),"setTag",tagSet);
                                    Log.e(TAG, "onSuccess: tagSet " + tagSet.toString() );
                                }else{
                                    Set<String> tagSet = new LinkedHashSet<String>();
                                    if(countyNo.equals("null")){
                                        if(cityNo.equals("null")){
                                            if(provinceNo.equals("null")){
                                                tagSet.add("0");
                                                normalTag = "0";
                                            }else{
                                                tagSet.add(provinceNo);
                                                normalTag = provinceNo;
                                            }
                                        }else{
                                            tagSet.add(cityNo);
                                            normalTag = cityNo;
                                        }
                                    }else{
                                        tagSet.add(countyNo);
                                        normalTag = countyNo;
                                    }
                                    if(obj.getString("LandType") == null || Objects.equals(obj.getString("LandType"), "null") || Objects.equals(obj.getString("LandType"), "ALL")){
                                        tagSet.add("Farmland");
                                        tagSet.add("Woodland");
                                        tagSet.add("Grassland");
                                        tagSet.add("Otherland");
                                    }else{
                                        if(obj.getString("LandType").contains("Farmland")){
                                            tagSet.add("Farmland");
                                        }
                                        if(obj.getString("LandType").contains("Woodland")){
                                            tagSet.add("Woodland");
                                        }
                                        if(obj.getString("LandType").contains("Grassland")){
                                            tagSet.add("Grassland");
                                        }
                                        if(obj.getString("LandType").contains("Otherland")){
                                            tagSet.add("Otherland");
                                        }
                                    }
                                    if(obj.getString("Delay") == null || Objects.equals(obj.getString("Delay"), "ALL") || Objects.equals(obj.getString("Delay"), "0") || Objects.equals(obj.getString("Delay"), "") || Objects.equals(obj.getString("Delay"), "null")){
                                        tagSet.add("Delay_ALL");
                                    }else{
                                        int delay = Integer.parseInt(obj.getString("Delay"));
                                        for (int i = 0; i < delay; i++) {
                                            tagSet.add("Delay_" + i);
                                        }
                                    }


                                    tagSet.add("test20230411");
                                    tagString = tagSet.toString();
                                    //XGPushManager.setTags(getApplicationContext(),"setTag",tagSet);
                                    Log.e(TAG, "onSuccess: tagSet " + tagSet.toString() );
                                }

                                //开启华为推送
                                XGPushConfig.enableOtherPush(getApplicationContext(), true);
                                XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
                                    @Override
                                    public void onSuccess(Object data, int flag) {
                                        //token在设备卸载重装的时候有可能会变
                                        Log.d("TPush", "注册成功，设备token为：" + data);
                                    }

                                    @Override
                                    public void onFail(Object data, int errCode, String msg) {
                                        Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                                    }
                                });

                                User user = new User();
                                user.setId(1);
                                if(userName!=null&&userName.contains("山东省应急管理厅")){
                                    user.setPushTag(normalTag);
                                }
                                user.setUsername(userNameStr);
                                user.setPassword(passwordStr);
                                user.setToken(token);
                                user.setIsLogin("1");
                                user.setUserId(userId);
                                user.setCompanyName(companyName);
                                user.setProvinceName(provinceName);
                                user.setProvinceNo(provinceNo);
                                user.setCityName(cityName);
                                user.setCityNo(cityNo);
                                user.setCountyName(countyName);
                                user.setCountyNo(countyNo);
                                user.setTagSet(tagString);
                                user.setJpushStr("");
                                user.setJpush(false);

                                DbConfig dbConfig = new DbConfig(getApplicationContext());
                                DbManager db = dbConfig.getDbManager();
                                try {
                                    db.saveOrUpdate(user);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("token",token);
                                bundle.putString("START_TYPE","APP");
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }catch(Exception e){

                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e(TAG, "onError: error " + ex.toString() );
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });*/




                    User user = new User();
                    user.setId(1);
                    if(userName!=null&&userName.contains("山东省应急管理厅")){
                        user.setPushTag(normalTag);
                    }
                    user.setUsername(userNameStr);
                    user.setPassword(passwordStr);
                    user.setToken(token);
                    user.setIsLogin("1");
                    user.setUserId(userId);
                    user.setCompanyName(companyName);
                    user.setProvinceName(provinceName);
                    user.setProvinceNo(provinceNo);
                    user.setCityName(cityName);
                    user.setCityNo(cityNo);
                    user.setCountyName(countyName);
                    user.setCountyNo(countyNo);
                    //user.setTagSet(tagString);
                    user.setJpushStr("");
                    user.setJpush(false);

                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    DbManager db = dbConfig.getDbManager();
                    try {
                        db.saveOrUpdate(user);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("token",token);
                    bundle.putString("START_TYPE","APP");
                    intent.putExtras(bundle);
                    startActivity(intent);


                } catch (JSONException e) {
                    Log.e(TAG, "onSuccess: userMeg=========== error" + e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //     Toast.makeText(MainActivity.this, "网络异常，请检查网络链接14", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: " + ex.toString());
                // showUserTokenDialog("您的身份已失效,请重新登录");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(codeDialog);
            }
        });

    }




    private void showDialog(String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("慧眼卫星");
        dialog.setIcon(R.drawable.ic_icon_start);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {

        exit();

    }
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Log.e(TAG, "exit: -----");
            Intent intent = new Intent("haohai.haohai.baseActivity");
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播

            /* this.finish();
             System.exit(0);*/
        }
    }



}
