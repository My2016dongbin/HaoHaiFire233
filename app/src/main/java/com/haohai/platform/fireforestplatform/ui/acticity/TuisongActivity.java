package com.haohai.platform.fireforestplatform.ui.acticity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.haohai.platform.fireforestplatform.MainActivity;
import com.haohai.platform.fireforestplatform.R;
import com.haohai.platform.fireforestplatform.ui.utils.AuthTypeUtil;
import com.haohai.platform.firelibrary.ui.activity.FireMissionListActivity;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.utils.CommonData;
import com.vsg.trustaccess.sdks.VSGService;
import com.vsg.trustaccess.sdks.logic.AuthStateManager;
import com.vsg.trustaccess.sdks.logic.TunnelStateManager;

import static com.vsg.trustaccess.sdks.logic.TunnelStateManager.TunnelState.CONNECTED;

public class TuisongActivity extends HhBaseActivity implements AuthStateManager.AuthStateListener,TunnelStateManager.TunnelTotalStateListener,VSGService.KeyCertStateListener{

    private static final String TAG = TuisongActivity.class.getSimpleName();
    protected AuthStateManager mStateManager;
    private  final int PREPARE_VPN_SERVICE = 0;
    private TunnelStateManager mTunnelStateManager = null;
    private TuisongReceiver tuisongReceiver;
    private String id;
    private String time;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuisong);

        IntentFilter fireFilter = new IntentFilter();
        fireFilter.addAction("fire_weixing_tengxun");
        tuisongReceiver = new TuisongReceiver();
        registerReceiver(tuisongReceiver,fireFilter);


        mStateManager = AuthStateManager.getStateManager();
        mTunnelStateManager = TunnelStateManager.getStateManager();
        mTunnelStateManager.registerTotalStateListener(this);

        if (mTunnelStateManager.getTunnelState() ==CONNECTED) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
           /* intent.putExtra("isTuisong",true);
            intent.putExtra("tuisongId",id);
            intent.putExtra("tuisongTime",time);
            intent.putExtra("tuisongType",type);*/
            startActivity(intent);
        }else {
            AuthTypeUtil.checkNetworkConnectivity(TuisongActivity.this);
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
        mStateManager.registerListener(TuisongActivity.this);

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
                AuthTypeUtil.userPasswordAuth(TuisongActivity.this, true);
                break;
            case MODIFY_PASSWD_SUCCESS:
                Log.e(TAG, "authStateChanged: 2" );
                Toast.makeText(getApplicationContext(), "修改密码成功", Toast.LENGTH_SHORT).show();
                break;
            case NEED_PASSWORD_AUTH:
                Log.e(TAG, "authStateChanged: 3" );
                AuthTypeUtil.userPasswordAuth(TuisongActivity.this,false);
                break;
            case NEED_CERT_AUTH:
                Log.e(TAG, "authStateChanged: 4" );
                AuthTypeUtil.certificateAuth(TuisongActivity.this,false);
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
                AuthTypeUtil.terminalAuth(TuisongActivity.this,false);
                break;
            case NEED_COMMIT_TERMINAL_INFO:
                Log.e(TAG, "authStateChanged: 8" );
                /*直接提交终端信息*/
                AuthTypeUtil.commitTerminalInfoAuth(TuisongActivity.this,false);
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
                AuthTypeUtil.checkNetworkConnectivity(TuisongActivity.this);
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
                    VSGService.getInstance().startNCTunnel(TuisongActivity.this,null);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("isTuisong",true);
                    intent.putExtra("tuisongId",id);
                    intent.putExtra("tuisongTime",time);
                    intent.putExtra("tuisongType",type);
                    startActivity(intent);
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


    private class TuisongReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            id = intent.getStringExtra("fire_id");
            time = intent.getStringExtra("ob_time");
            type = intent.getStringExtra("type");
        }
    }
}
