package com.skyline.terraexplorer.mainapps.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skyline.terraexplorer.mainapps.MainActivity;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
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

import rx.functions.Action1;
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginDialog = new ProgressDialog(this);

        initView();
    }

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText userNameEdit;
    private EditText passwordEdit;
    private TextView loginButton;
    private ProgressDialog loginDialog;
    private static boolean isExit = false;
    private long time = 0;
    private static final int EXIT = 1;
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case EXIT:
                    isExit = false;
                    break;
            }
        }
    };
    private TextView registerButton;
    private String access_token;


    private void initView() {

        userNameEdit = (EditText) findViewById(R.id.username_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        loginButton = (TextView) findViewById(R.id.login_button);

        try{
            SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username","");
            String password = sharedPreferences.getString("password","");
            userNameEdit.setText(username);
            passwordEdit.setText(password);
        }catch(Exception e){
            Log.e("Exception", "Exception user account password");
        }



        RxViewAction.clickNoDouble(loginButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        loginToService();
                    }
                });


    }

    private void loginToService() {

        if (userNameEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialogProgress(loginDialog,"登录中...              ");
        final RequestParams params = new RequestParams(RequestUtils.LOGIN_URL + "auth/oauth/token");
        // params.addBodyParameter("reqJson", jsonObject.toString());
        //String username64=java.util.Base64.getEncoder().encodeToString(userNameEdit.getText().toString().getBytes());
        //String userpas64=java.util.Base64.getEncoder().encodeToString(passwordEdit.getText().toString().getBytes());
        params.addParameter("username",userNameEdit.getText().toString());
        params.addParameter("password",passwordEdit.getText().toString());
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
                    editor.putString("username", userNameEdit.getText().toString());
                    editor.putString("password", passwordEdit.getText().toString());
                    editor.apply();

                    getUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
                if (ex.toString().contains("400")) {
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }else  if (ex.toString().contains("401")) {
                    Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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

    private void getUserInfo() {
        final RequestParams params = new RequestParams(RequestUtils.LOGIN_URL + "auth/api/auth/user/get/userinfo");
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
                        String jobTitle = userJsonObj.getString("jobTitle");

//                        String gridNo = userJsonObj.getString("gridNo");
                        String bkchar2 = userJsonObj.getString("bkchar2");
//                        String money = userJsonObj.getString("money");
//                        String lockMoney = userJsonObj.getString("lockMoney");
//                        String groupName = userJsonObj.getString("groupName");
                        String headUrl = userJsonObj.getString("headUrl");
                        String state = userJsonObj.getString("state");
                        User user = new User(jobTitle,id, userCode,passwordEdit.getText().toString(), fullName, email, phone, sex, entryTime, birthday, comment, groupId, bkchar2, state, 1,access_token,headUrl);
                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.delete(User.class);
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        //  doLogin();
                        loginDialog.dismiss();
                        Set<String> tagSet = new LinkedHashSet<String>();
                        tagSet.add("0");
                        Log.e(TAG, "onSuccess: login1");


                        Set<String> tagSetXG = new LinkedHashSet<>();
                        tagSetXG.add(id);
                        tagSetXG.add("debug_20240327");
                        XGPushManager.setTags(LoginActivity.this, "setTag", tagSetXG);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

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
    public void onBackPressed() {

        exit();

    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次回到主页",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(EXIT, 2000);
        } else {
              System.exit(0);
        }
    }
    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }
}
