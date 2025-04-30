package com.skyline.terraexplorer.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HhBaseActivity;
import com.skyline.terraexplorer.db.DbConfig;
import com.skyline.terraexplorer.db.User;
import com.skyline.terraexplorer.utils.RequestUtils;
import com.skyline.terraexplorer.utils.RxViewAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.functions.Action1;

public class LoginActivity extends HhBaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText phoneView;
    private EditText passwordView;
    private TextView loginButton;
    private String access_token;
    private ProgressDialog loginDialog;
    private static boolean isExit = false;
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
        loginDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        String from = intent.getStringExtra("FROM");
        /*if (from.equals("main")){
           *//* Intent intent2= new Intent();
            intent2.setAction("out_login");
            sendBroadcast(intent2);*//*
        }*/
        initView();

        //导航栏沉浸式
        fullScreen(this);
    }

    private void initView() {
        phoneView = (EditText) findViewById(R.id.input_email);
        passwordView = (EditText) findViewById(R.id.input_password);
        loginButton = (TextView) findViewById(R.id.btn_login);

        try{
            SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username","");
            String password = sharedPreferences.getString("password","");
            phoneView.setText(username);
            passwordView.setText(password);
        }catch(Exception e){
            Log.e("Exception", "Exception user account password");
        }

        RxViewAction.clickNoDouble(loginButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        loginGetToken();
                    }
                });

    }

    private void loginGetToken() {
        showDialogProgress(loginDialog,"登陆中...              ");
        if (phoneView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordView.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "auth/oauth/token");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("username",phoneView.getText().toString());
        params.addParameter("password",passwordView.getText().toString());
        params.addParameter("grant_type","password");
        params.addParameter("client_id","client_password");
        params.addParameter("client_secret","123456");
        params.setConnectTimeout(10000);
        Log.e(TAG, "loginGetToken: --"  + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    access_token = jsonObject.getString("access_token");

                    ///本地存储账号密码
                    SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", phoneView.getText().toString());
                    editor.putString("password", passwordView.getText().toString());
                    editor.apply();

                    getUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, "用户名密码错误!", Toast.LENGTH_SHORT).show();
                loginDialog.dismiss();
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
    private void getUserInfo() {

        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "auth/api/auth/user/get/userinfo");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.setConnectTimeout(10000);
        params.addHeader("Authorization","bearer " + access_token);
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
                        String createUser = userJsonObj.getString("createUser");
                        String updateUser = userJsonObj.getString("updateUser");
                        String createTime = userJsonObj.getString("createTime");
                        String updateTime = userJsonObj.getString("updateTime");
                        String id = userJsonObj.getString("id");
                        String userCode = userJsonObj.getString("userCode");
                        String userPasswd = userJsonObj.getString("userPasswd");
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
                        String state = userJsonObj.getString("state");
                        Log.e(TAG, "onSuccess: --4-" );
                        User user = new User(id, userCode,phoneView.getText().toString(), passwordView.getText().toString(), fullName, email, phone, sex, entryTime, birthday, type, isSuperAdmin, comment, groupId,
                                 gridNo, bkchar2, money, lockMoney, groupName, state, 1, access_token);
                        user.setDataIsChange(false);
                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        getQuanxianDataFromService();
/*

                        Set<String> tagSet = new LinkedHashSet<String>();
                        tagSet.add("0");
                        JPushInterface.setTags(getApplicationContext(),tagSet,mTagsCallback);

                        Intent intent = new Intent(getApplicationContext(), TEMainActivity.class);
                        intent.putExtra("STATE",1);
                        startActivity(intent);
                        finish();
*/

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
             //   loginDialog.dismiss();
            }
        });

    }

    /**
     * 获取用户权限数据
     */
    private void getQuanxianDataFromService() {

        final RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "auth/api/auth/auth/list/element/from/menu");
        params.setConnectTimeout(10000);
        params.addParameter("menuCode","ResourceListManager");
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "getUserInfo:-- " + params );
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:--2- " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){

                        JSONArray data = jsonObject.getJSONArray("data");
                        User user = new DbConfig(getApplicationContext()).getUser();
                        user.setDelete(false);
                        user.setAdd(false);
                        user.setEdit(false);
                        for (int i = 0; i < data.length(); i++) {
                            String elementCode = data.getJSONObject(i).getString("elementCode");
                            Log.e(TAG, "onSuccess: " + elementCode);
                            if (elementCode.equals("ResourceListManager:btn_edit")){
                                Log.e(TAG, "onSuccess: 1" );
                                user.setEdit(true);
                            }else if (elementCode.equals("ResourceListManager:btn_del")){
                                Log.e(TAG, "onSuccess: 2" );
                                user.setDelete(true);
                            }else if (elementCode.equals("ResourceListManager:btn_add")){
                                Log.e(TAG, "onSuccess: 3" );
                                user.setAdd(true);
                            }
                        }

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        Set<String> tagSet = new LinkedHashSet<String>();
                        tagSet.add("0");
                        JPushInterface.setTags(getApplicationContext(),tagSet,mTagsCallback);

                        Intent intent = new Intent(getApplicationContext(), TEMainActivity.class);
                        intent.putExtra("STATE",1);
                        startActivity(intent);
                        finish();

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
                loginDialog.dismiss();
            }
        });

    }

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "推送注册成功";
                    Log.e(TAG, "gotResult:推送注册成功 ");
                    break;
                case 6002:
                    logs = "推送注册失败";
                    Log.e(TAG, "gotResult:推送注册失败 "  + alias);
                    Log.e(TAG, "gotResult:推送注册失败 "  + tags);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
            //Log.e("标签设置", logs);
            //System.out.println("推送状态:"+logs);
        }
    };

    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
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
           /* Intent intent = new Intent();
            intent.setAction("haohai.haohai.baseActivity");
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播*/
            Intent intent1= new Intent();
            intent1.setAction("out_login_loginactivity");
            sendBroadcast(intent1);
            this.finish();
            //System.exit(0);
        }
    }

    @Override
    public void onBackPressed() {

        exit();

    }


}
