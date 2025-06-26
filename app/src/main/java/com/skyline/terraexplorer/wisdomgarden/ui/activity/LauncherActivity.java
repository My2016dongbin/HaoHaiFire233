package com.skyline.terraexplorer.wisdomgarden.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.wisdomgarden.MainActivity;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.base.HhBaseActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.db.User;
import com.ruyiruyi.rylibrary.request.HhRequestParams;
import com.ruyiruyi.rylibrary.request.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.LinkedHashSet;
import java.util.Set;

public class LauncherActivity extends HhBaseActivity {


    private static final int GO_GUIDE = 101;
    private static final String TAG = LauncherActivity.class.getSimpleName();
    public boolean isHasPermission = true;
    private String fromUserName="";
    private String token="";
    private ProgressDialog loginDialog;
    private String access_token;
    private String userPasswd;
    private String userCode;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_GUIDE:
                    Log.e(TAG, "handleMessage:11 " );
                    //        startService(new Intent(getApplicationContext(), TrackService.class));
                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    User user = new DbConfig(getApplicationContext()).getUser();
                    //     Log.e(TAG, "handleMessage: " + user.getIsLogin());
                    if (user == null){      //用户不存在
                        /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);*/
                        getAccount();
                        // finish();
                    }else {
                        if (user.getIsLogin() == 0) {       //用户未登录
                            /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();*/
                            getAccount();
                        }else {
                            Log.e(TAG, "onSuccess: login3");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    break;

            }

        }
    };

    private boolean isFromOtherApp = false;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
        setContentView(R.layout.activity_launcher);
        loginDialog = new ProgressDialog(this);
        intent = getIntent();
        SharedPreferences sharedPreferences = this.getSharedPreferences("sptest",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("userGuid",token);
        editor.commit();

        Log.e(TAG, "token: "+token );


        //权限获取
        requestPower();
    }


    private void getAccount() {
        ///本地存储账号密码
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        userCode = sharedPreferences.getString("username","");
        userPasswd = sharedPreferences.getString("password","");

        if((!userCode.isEmpty()) && (!userPasswd.isEmpty())){
            login();
        }else{
            Intent intent = new Intent(getApplicationContext(), com.skyline.terraexplorer.mainapps.activity.LoginActivity.class);
            startActivity(intent);
        }
    }

    private void posttoken() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("access_token", token);
        } catch (JSONException e) {
        }
        HhRequestParams params = new HhRequestParams(RequestUtils.REQUEST_URL + "api/user/loginNew1");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "posttoken: "+result);
                try {
                    JSONObject  jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                            JSONObject dataObj = data.getJSONObject(0);
                            userPasswd = dataObj.getString("userPasswd");
                            userCode = dataObj.getString("userCode");
                            Log.e(TAG, "userPasswd: " + userPasswd + "," + userCode);
                            login();
                    }else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        Toast.makeText(LauncherActivity.this, "账号不存在,请手动登录", Toast.LENGTH_SHORT).show();
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

    private void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 1);
                Toast.makeText(LauncherActivity.this, "1111", Toast.LENGTH_SHORT).show();
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 1);
            }
        } else {
            /*if (fromUserName!="") {
                showDialogProgress(loginDialog, "登录中...              ");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("userCode", fromUserName);
                    jsonObject.put("userPasswd", "123456");
                    jsonObject.put("groupId", usercode);
                    jsonObject.put("fullName", name);

                } catch (JSONException e) {
                }
                RequestParams params = new RequestParams(RequestUtils.LOGIN_URL + "api/user");
                params.setAsJsonContent(true);
                params.setBodyContent(jsonObject.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess: " + result);
                        login();
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
                        loginDialog.dismiss();
                    }
                });
            }else {
                handler.sendEmptyMessageDelayed(GO_GUIDE, 2000);
            }*/
            if (intent.getStringExtra("token")==null||"".equals(intent.getStringExtra("token"))){
                isFromOtherApp = false;
                handler.sendEmptyMessageDelayed(GO_GUIDE, 2000);
            }else {
                isFromOtherApp = true;
                token=intent.getStringExtra("token");

                access_token = token;
                getUserInfo();
            }

        }
    }

    private void login() {
                    final HhRequestParams params = new HhRequestParams(RequestUtils.LOGIN_URL + "oauth/token");
            // params.addBodyParameter("reqJson", jsonObject.toString());

            params.addParameter("username",userCode);
            params.addParameter("password",userPasswd);
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

                        getUserInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e(TAG, "onError: " + ex.toString());
                    if (ex.toString().contains("400")) {
                        Toast.makeText(LauncherActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }else  if (ex.toString().contains("401")) {
                        Toast.makeText(LauncherActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LauncherActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    loginDialog.dismiss();

                    Intent intent = new Intent(getApplicationContext(), com.skyline.terraexplorer.mainapps.activity.LoginActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

    }
    private void getUserInfo() {
        final HhRequestParams params = new HhRequestParams(RequestUtils.REQUEST_URL_BASE + "auth/api/auth/user/get/userinfo");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.setConnectTimeout(10000);
        params.addHeader("Authorization","bearer " + access_token);
        Log.e(TAG, "getUserInfo: token = " + access_token );
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
//                        String type = userJsonObj.getString("type");
//                        String isSuperAdmin = userJsonObj.getString("isSuperAdmin");
                        String comment = userJsonObj.getString("comment");
                        String groupId = userJsonObj.getString("groupId");

//                        String gridNo = userJsonObj.getString("gridNo");
                        String bkchar2 = userJsonObj.getString("bkchar2");
//                        String money = userJsonObj.getString("money");
//                        String lockMoney = userJsonObj.getString("lockMoney");
//                        String groupName = userJsonObj.getString("groupName");
                        String headUrl = userJsonObj.getString("headUrl");
//                        String state = userJsonObj.getString("state");
/*                        User user = new User(id, userCode,fromUserName, userCode, fullName, email, phone, sex, entryTime, birthday, comment, groupId, bkchar2, 1, access_token,headUrl);
                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.delete(User.class);
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }*/

                        //  doLogin();
                        loginDialog.dismiss();
                        Set<String> tagSet = new LinkedHashSet<String>();
                        tagSet.add("0");
                        Log.e(TAG, "onSuccess: login0");
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                       /* ARouter.getInstance().build(RouteUtils.LoginToMain)
                                .withInt("state",1)
                                .navigation();

*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       /* Log.e(TAG, "onRequestPermissionsResult:requestCode --" + requestCode);

        Log.e(TAG, "onRequestPermissionsResult: permissions--" + permissions.toString());
        Log.e(TAG, "onRequestPermissionsResult:  permissions.length--" +  permissions.length);
        Log.e(TAG, "onRequestPermissionsResult: grantResults--" + grantResults.toString());
        Log.e(TAG, "onRequestPermissionsResult: grantResults.length--" + grantResults.length);
*/
        for (int i = 0; i < permissions.length; i++) {

            Log.e(TAG, "onRequestPermissionsResult: permissions------" + permissions[i]);
        }


        if (requestCode == 1) {

            boolean isPremission = true;
            for (int i = 0; i < grantResults.length; i++) {
                Log.e(TAG, "onRequestPermissionsResult: permissions++++++" + grantResults[i]);
                if (grantResults[i] == -1) {
                    isPremission = false;
                }
                //  Log.e(TAG, "onRequestPermissionsResult: permissions++++++" +  grantResults[i]);
            }

            if (isPremission) {          //有权限
                if (token.isEmpty()){
                    handler.sendEmptyMessageDelayed(GO_GUIDE,3000);
                }
            } else {
                judgePower();
            }
        }
    }
    private void judgePower() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权相机权限", Toast.LENGTH_SHORT).show();
            finish();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            isHasPermission = false;
            Toast.makeText(this, "请授权定位权限", Toast.LENGTH_SHORT).show();

        }
    }
}
