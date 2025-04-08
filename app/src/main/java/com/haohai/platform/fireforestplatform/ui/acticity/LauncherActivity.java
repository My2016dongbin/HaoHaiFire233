package com.haohai.platform.fireforestplatform.ui.acticity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.haohai.platform.fireforestplatform.MainActivity;
import com.haohai.platform.fireforestplatform.R;
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
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.LinkedHashSet;
import java.util.Set;

public class LauncherActivity extends AppCompatActivity {

    private static final int GO_GUIDE = 101;
    private static final String TAG = LauncherActivity.class.getSimpleName();
    public boolean isHasPermission = true;
    private String access_token;
    private User user;
    private ProgressDialog loginDialog;
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
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                         finish();
                    }else {
                        if (user.getIsLogin() == 0) {       //用户未登录
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        user = new DbConfig(this).getUserOut();
        loginDialog = new ProgressDialog(this);
        //权限获取
        requestPower();
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
            //   handler.sendEmptyMessage(GO_GUIDE);
           // handler.sendEmptyMessageDelayed(GO_GUIDE, 2000);

            if (user !=null){
                loginToService();
            }else{
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
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
                handler.sendEmptyMessageDelayed(GO_GUIDE,3000);
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
    private void loginToService() {

        final RequestParams params = new RequestParams(RequestUtils.LOGIN_URL + "auth/api/auth/user/appOrWeb/login");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        params.addParameter("username",user.getUserName());
        params.addParameter("password",user.getUserPasswd());
        params.addParameter("accountType","3");
        params.setConnectTimeout(10000);
        Log.e(TAG, "loginGetToken: --"  + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    access_token = jsonObject.getJSONArray("data").getJSONObject(0).getString("access_token");
                    user.setToken(access_token);
                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    DbManager db = dbConfig.getDbManager();

                    try {
                        db.delete(User.class);
                        db.saveOrUpdate(user);
                    } catch (DbException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessageDelayed(GO_GUIDE, 1000  );
                    }

                    handler.sendEmptyMessageDelayed(GO_GUIDE, 1000  );
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessageDelayed(GO_GUIDE, 1000  );
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
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
