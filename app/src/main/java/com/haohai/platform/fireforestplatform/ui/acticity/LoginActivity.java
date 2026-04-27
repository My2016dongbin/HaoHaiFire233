package com.haohai.platform.fireforestplatform.ui.acticity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.VpnService;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.haohai.platform.fireforestplatform.MainActivity;
import com.haohai.platform.fireforestplatform.R;
import com.haohai.platform.fireforestplatform.ui.utils.AuthTypeUtil;
import com.haohai.platform.fireforestplatform.ui.utils.TraceServiceImpl;
import com.haohai.platform.mapmodel.Utils.MNCTransparentDialog;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.route.RouteUtils;
import com.ruyiruyi.rylibrary.utils.CommonData;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.vsg.trustaccess.sdks.VSGService;
import com.vsg.trustaccess.sdks.logic.AuthStateManager;
import com.vsg.trustaccess.sdks.logic.TunnelStateManager;

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

@Route(path = RouteUtils.OutLogin)
public class LoginActivity extends HhBaseActivity implements AuthStateManager.AuthStateListener,TunnelStateManager.TunnelTotalStateListener,VSGService.KeyCertStateListener{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText userNameEdit;
    private EditText passwordEdit;
    private TextView loginButton;
    private ProgressDialog loginDialog;
    private static boolean isExit = false;
    private long time = 0;
    private static final int EXIT = 1;
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
    private User user;


    protected AuthStateManager mStateManager;
    private  final int PREPARE_VPN_SERVICE = 0;
    private TunnelStateManager mTunnelStateManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        String downMessage = intent.getStringExtra("downMessage");
        if(downMessage != null){
            showTipsDialog(downMessage);
        }

        loginDialog = new ProgressDialog(this);


        mStateManager = AuthStateManager.getStateManager();
        mTunnelStateManager = TunnelStateManager.getStateManager();
        mTunnelStateManager.registerTotalStateListener(this);

        initView();
    }

    private void initView() {

        userNameEdit = (EditText) findViewById(R.id.username_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        loginButton = (TextView) findViewById(R.id.login_button);



        RxViewAction.clickNoDouble(loginButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        showDialogProgress(loginDialog,"登陆中...              ");
                        if(!CommonData.vpnState){
                            AuthTypeUtil.checkNetworkConnectivity(LoginActivity.this);
                        }else{
                            loginToService();
                        }
                    }
                });

        user = new DbConfig(this).getUserOut();
        Log.i(TAG, "initView: "+user);
        if (user!=null){
            userNameEdit.setText(user.getUserName());
            passwordEdit.setText(user.getUserPasswd());
            userNameEdit.setSelection(userNameEdit.getText().length());
        }
    }


    public void showTipsDialog(String msg) {
        final MNCTransparentDialog mncTransDialog = new MNCTransparentDialog(LoginActivity.this);
        mncTransDialog.setCancelable(false);
        View dialogView = LayoutInflater.from(LoginActivity.this).inflate(com.haohai.platform.mapmodel.R.layout.dialog_tips, null, false);
        TextView message_text = (TextView) dialogView.findViewById(com.haohai.platform.mapmodel.R.id.message_text);
        message_text.setText(msg);
        final TextView tv_queren = (TextView) dialogView.findViewById(com.haohai.platform.mapmodel.R.id.tv_right);
        final TextView tv_left = (TextView) dialogView.findViewById(com.haohai.platform.mapmodel.R.id.tv_left);
        //确定
        RxViewAction.clickNoDouble(tv_queren).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();

            }
        });
        //取消
        RxViewAction.clickNoDouble(tv_left).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mncTransDialog.dismiss();

            }
        });
        mncTransDialog.show();
        Window window = mncTransDialog.getWindow();//对话框窗口
        window.setGravity(Gravity.CENTER);//设置对话框显示在屏幕中间
        window.setWindowAnimations(com.haohai.platform.mapmodel.R.style.dialog_style);//添加动画
        window.setContentView(dialogView);
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
        showDialogProgress(loginDialog,"登陆中...              ");
        final RequestParams params = new RequestParams(RequestUtils.LOGIN_URL + "auth/oauth/token");
        // params.addBodyParameter("reqJson", jsonObject.toString());
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
                        String type = userJsonObj.getString("type");
                        String isSuperAdmin = userJsonObj.getString("isSuperAdmin");
                        String comment = userJsonObj.getString("comment");
                        String groupId = userJsonObj.getString("groupId");
                        /*String account_type = userJsonObj.getString("account_type");//1：管理员，2：护林员*/

                        String gridNo = userJsonObj.getString("gridNo");
                        String bkchar2 = userJsonObj.getString("bkchar2");
                        String money = userJsonObj.getString("money");
                        String lockMoney = userJsonObj.getString("lockMoney");
                        String groupName = userJsonObj.getString("groupName");
                        String headUrl = userJsonObj.getString("headUrl");
                        String state = userJsonObj.getString("state");
                        String imToken = userJsonObj.getString("imToken");

                        Set<String> tagSet = new LinkedHashSet<String>();
                        tagSet.add(gridNo);
                        tagSet.add(id);
                        tagSet.add("jm_" + groupId);
                        tagSet.add("JiMo231113");
                        XGPushManager.setTags(getApplicationContext(),"setTag",tagSet);
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(15000);//休眠3秒
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
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                /**
                                 * 要执行的操作
                                 */
                            }
                        }.start();
                        User user = new User(id, userCode,userNameEdit.getText().toString(), passwordEdit.getText().toString(), fullName, email, phone, sex, entryTime, birthday, type, isSuperAdmin, comment, groupId,
                                gridNo, bkchar2, money, lockMoney, groupName, state, 1, access_token,headUrl,imToken);
                        user.isShangchuan = false;
                        user.setIsyunyin(1);
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

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                       /* ARouter.getInstance().build(RouteUtils.LoginToMain)
                                .withInt("state",1)
                                .navigation();*/


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

    /**
     * 登录环信
     */
   /* private void doLogin() {
        LoginInfo info = new LoginInfo(userNameEdit.getText().toString(),access_token);
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        // your code
                        Log.e(TAG, "onSuccess: " +param);
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 302) {
                            Log.e(TAG, "账号密码错误 ");
                            // your code
                        } else {
                            // your code
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // your code
                    }
                };

        //执行手动登录
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);
    }*/

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

            Intent intent = new Intent("haohai.haohai.baseActivity");       //关闭程序
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播

            Intent intent1 = new Intent("out_login_main");       //关闭首页
            sendBroadcast(intent1);//发送广播

            this.finish();
            //  System.exit(0);
            TraceServiceImpl.stopService();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
        if(mTunnelStateManager != null){
            mTunnelStateManager.unregisterTotalStateListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: " );
        if(mStateManager != null){ mStateManager.unregisterListener(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: " );
        mStateManager.registerListener(LoginActivity.this);

    }


    @Override
    public void keyCertState(VSGService.KeyCertType keyCertType, int container) {
        VSGService.getInstance().unregisterKeyCertStateListener();
        /*
         ****************可以使用Key中证书*****************************
         * KEYCERTTYPE_RSA_ENC：国际标准，使用的加密证书
         * KEYCERTTYPE_RSA_SIGN：国际标准，使用的签名证书
         * KEYCERTTYPE_SM2_SIGN_ENC：国密标准，需要双证书(加密和签名同时存在)
         * ****************不可以使用卡中证书*****************************
         * KEYCERTTYPE_KEYNOTEXIST:Key不存在
         * KEYCERTTYPE_CERTNOTEXIST:证书不存在
         * KEYCERTTYPE_SM2_SIGN:国密签名证书存在
         * KEYCERTTYPE_SM2_ENC:国密加密证书存在
         * */
        Log.e(TAG, "keyCertState: 证书");
        switch (keyCertType) {
            case KEYCERTTYPE_RSA_ENC:
                Toast.makeText(getApplicationContext(),"国际加密证书存在，容器号："+container, Toast.LENGTH_SHORT).show();
                break;
            case KEYCERTTYPE_RSA_SIGN:
                Toast.makeText(getApplicationContext(),"国际签名证书存在，容器号："+container, Toast.LENGTH_SHORT).show();
                break;
            case KEYCERTTYPE_SM2_SIGN_ENC:
                Toast.makeText(getApplicationContext(),"国密双证书存在，容器号："+container, Toast.LENGTH_SHORT).show();
                break;
            case KEYCERTTYPE_SM2_SIGN:
                Toast.makeText(getApplicationContext(),"国密签名证书存在", Toast.LENGTH_SHORT).show();
                return;
            case KEYCERTTYPE_SM2_ENC:
                Toast.makeText(getApplicationContext(),"国密加密证书存在", Toast.LENGTH_SHORT).show();
                break;
            case KEYCERTTYPE_KEYNOTEXIST:
                Toast.makeText(getApplicationContext(),"KEY不存在！", Toast.LENGTH_SHORT).show();
                return;
            case KEYCERTTYPE_CERTNOTEXIST:
                Toast.makeText(getApplicationContext(),"证书不存在！", Toast.LENGTH_SHORT).show();
                return;
            default:
                return;
        }
    }

    @Override
    public void tunnelTotalStateChanged() {
        TunnelStateManager.TunnelState state = mTunnelStateManager.getTunnelState();
        TunnelStateManager.TunnelErrorState errorState = mTunnelStateManager.getTunnelErrorState();
        Log.e(TAG, "tunnelstate:"+state+",error:"+errorState);
        if(reportErrorTunnelState(errorState)){
            return;
        }
        switch (state){
            case DISABLED:
                CommonData.vpnState = false;
//                Toast.makeText(getApplicationContext(), "未连接", Toast.LENGTH_SHORT).show();
                break;
            case CONNECTING:
//                Toast.makeText(getApplicationContext(), "连接中", Toast.LENGTH_SHORT).show();
                break;
            case CONNECTED:
                CommonData.vpnState = true;
                //   Toast.makeText(getApplicationContext(), "已连接", Toast.LENGTH_SHORT).show();
                break;
            case DISCONNECTING:
                CommonData.vpnState = false;
//                Toast.makeText(getApplicationContext(), "断开连接中", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Vpn 回调接口
     */
    @Override
    public void authStateChanged() {
        AuthStateManager.AuthState state = mStateManager.getAuthState();
        Log.e(TAG, "authStateChanged: " +state);

        int errorCode = mStateManager.getAuthErrorCode();
        if(reportErrorState(errorCode)){ /*具体实现可以参考demo*/

            return;
        }
        switch (state){
            case CONNECTING_SEVER:
                Log.e(TAG, "authStateChanged: 0" );
                break;
            case CONNECTING_SERVER_SUCCESS:
                Log.e(TAG, "authStateChanged: 1" );
                AuthTypeUtil.userPasswordAuth(LoginActivity.this, true);
                break;
            case MODIFY_PASSWD_SUCCESS:
                Log.e(TAG, "authStateChanged: 2" );
                Toast.makeText(getApplicationContext(), "修改密码成功", Toast.LENGTH_SHORT).show();
                break;
            case NEED_PASSWORD_AUTH:
                Log.e(TAG, "authStateChanged: 3" );
                AuthTypeUtil.userPasswordAuth(LoginActivity.this,false);
                break;
            case NEED_CERT_AUTH:
                Log.e(TAG, "authStateChanged: 4" );
                AuthTypeUtil.certificateAuth(LoginActivity.this,false);
                break;
            case NEED_DYNAMIC_TOKEN:
                Log.e(TAG, "authStateChanged: 5" );
                //  startActivity(new Intent(MainActivity.this,DynamicTokenActivity.class));
                break;
            case NEED_SMS_AUTH:
                Log.e(TAG, "authStateChanged: 6" );
                //  startActivity(new Intent(MainActivity.this,SmsActivity.class));
                break;
            case NEED_TERMINAL_AUTH:
                Log.e(TAG, "authStateChanged: 7" );
                //     permissionRequest(mReadPhoneStatePermissions);
                AuthTypeUtil.terminalAuth(LoginActivity.this,false);
                break;
            case NEED_COMMIT_TERMINAL_INFO:
                Log.e(TAG, "authStateChanged: 8" );
                /*直接提交终端信息*/
                AuthTypeUtil.commitTerminalInfoAuth(LoginActivity.this,false);
                break;
            case NEED_MODIFY_PASSWORD:
                Log.e(TAG, "authStateChanged: 9" );
                /*Intent intent = new Intent(MainActivity.this,PwdChangeActivity.class);
                intent.putExtra(FIRSTPWDCHANGEACTION,true);
                startActivity(intent);*/
                break;
            case AUTH_SUCCESS:
                Log.e(TAG, "authStateChanged: 10" );
                //认证成功后 跳转进首页
                //  handler.sendEmptyMessageDelayed(GO_GUIDE, 2000);
//                Toast.makeText(getApplicationContext(), "认证成功", Toast.LENGTH_SHORT).show();
                break;
            case GET_INTERGRATION_XML:
                Log.e(TAG, "authStateChanged: 11" );
                break;
            case GET_INTERGRATION_XML_SUCCESS:
                Log.e(TAG, "authStateChanged: 12" );
//                Toast.makeText(getApplicationContext(), "获取资源成功", Toast.LENGTH_SHORT).show();
                if(!VSGService.getInstance().isHaveAccessResource()){
                    Toast.makeText(getApplicationContext(), "没有可访问的资源", Toast.LENGTH_SHORT).show();
                    return;
                }
                prepareVPNService();
                break;
            case SHARED_LOGIN_FAILED:
                Log.e(TAG, "authStateChanged: 13" );
                AuthTypeUtil.checkNetworkConnectivity(LoginActivity.this);
                break;
            case SHARED_LOGIN_SUCCESS:
                Log.e(TAG, "authStateChanged: 14" );
                break;
            default:
                break;
        }
    }
    private boolean reportErrorTunnelState(TunnelStateManager.TunnelErrorState error){
        if (error == TunnelStateManager.TunnelErrorState.NO_ERROR)
        {
            return false;
        }

        switch (error)
        {
            case PEER_AUTH_FAILED:
                Toast.makeText(this, "用户认证失败！", Toast.LENGTH_SHORT).show();
                break;
            case LOOKUP_FAILED:
                Toast.makeText(this,"lookup_failed", Toast.LENGTH_SHORT).show();
                break;
            case UNREACHABLE:
                Toast.makeText(this, "网关不可达", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "其他错误："+error, Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    private void prepareVPNService(){
        Intent intent;
        try {
            intent = VpnService.prepare(this);
        } catch (IllegalStateException ex) {
            /*
             * this happens if the always-on VPN feature (Android 4.2+) is
             * activated
             */
            Toast.makeText(getApplicationContext(), "不支持VpnService", Toast.LENGTH_SHORT).show();
            return;
        }
        /* store profile info until the user grants us permission */
        if (intent != null) {
            try {
                startActivityForResult(intent, PREPARE_VPN_SERVICE);
            } catch (ActivityNotFoundException ex) {
                /*
                 * it seems some devices, even though they come with Android 4,
                 * don't have the VPN components built into the system image.
                 * com.android.vpndialogs/com.android.vpndialogs.ConfirmDialog
                 * will not be found then
                 */
                Toast.makeText(getApplicationContext(), "不支持vpn", Toast.LENGTH_SHORT).show();
            }
        } else { /* user already granted permission to use VpnService */

            onActivityResult(PREPARE_VPN_SERVICE, RESULT_OK, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: 毁掉");
        switch (requestCode) {
            case PREPARE_VPN_SERVICE:
                Log.e(TAG, "onActivityResult: 1" );
                if (resultCode == RESULT_OK) {
                    VSGService.getInstance().startNCTunnel(LoginActivity.this,null);
                    loginToService();
                }
                break;
        }
    }

    private boolean reportErrorState(int error){
        if(error == 0){
            return false;
        }
        String errormsg = mStateManager.getAuthErrorMsg();
        switch (error){
            case AuthStateManager.LocalStateCode.GATEWAY_INACCESSIBLE:
                Toast.makeText(getApplicationContext(), "网关不可达,请稍后再试", Toast.LENGTH_SHORT).show();

                break;
            case AuthStateManager.AuthStateCode.USER_SESSION_NOT_FOUND:
                Toast.makeText(getApplicationContext(), "用户会话超时，请重新登录!", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getApplicationContext(), "errorstate:"+ Integer.toHexString(error)+",errormsg:"+errormsg, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
